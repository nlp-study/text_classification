package regress.svm;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import regress.AbstractRegressTrainer;
import regress.softmax.SoftmaxTrainer;
import util.Pair;
import util.VectorOperation;
import base.InstanceSetD;
import base.InstanceSetI;
import classifier.AbstractTrainer;

/**
 * @author xiaohe
 * 创建于：2015年2月13日
 * 注：用smo算法进行学习
 *
 * 基本公式：
 * f(x) = sign(w·x+b)
 * 
 */
public class SVMTrainer extends AbstractRegressTrainer {
	Logger logger = Logger.getLogger(SVMTrainer.class);
	
	//最大迭代次数		
	static final double ITERATIONS_VALUE = 0.001;
    
	
	/********************模型相关***********************/
	//每个输入特征的参数
	double[] alpha;
	
	//权重向量
	double[] w;
	
    //截距
	double b;
	
	//α的上界
	int C;
	
	//输入向量的误差
	double[] E;
	
	
	
	
	/********************输入特征相关***********************/
	InstanceSetD instanceSet;
	
	int featureSize;
	
	int length;
	
	int classNumb;
	
	SVMModel model;
	

	@Override
	public void init(InstanceSetD instanceSet) {
		this.instanceSet = instanceSet;
		this.featureSize = instanceSet.getSize();
		this.length = instanceSet.getLength();
		this.classNumb = instanceSet.getClassNumb();
		
		alpha = new double[featureSize];
		w = new double[length];
		b = 0;
		C = 1;
		E = new double[featureSize];
		
	}

	@Override
	public void train() {
		
		while(isStop())
		{
			iteration();
		}
		
	}
	
	/**
	 * @comment:一次迭代过程
	 * @return:void
	 */
	public void iteration()
	{
		calculateE();
		
		int alpha1ID = selectPeriphery();
		int alpha2ID = selectSubcoat(alpha1ID);
		
		double alpha2 = clipAlpha2(alpha1ID, alpha2ID);
		double alpha1 = calculateAlpha1(alpha1ID, alpha2ID,alpha2);
				
		b = calculateB(alpha1ID,alpha2ID,alpha1,alpha2);
		
		E[alpha1ID] = alpha1;
		E[alpha2ID] = alpha2;
	}
	
	public void calculateE()
	{
		for(int i=0;i<featureSize;++i)
		{
			int y = instanceSet.getClassID(i);
			double pred = predict(i);
			E[i] = pred - y;
		}
	}
	
	public double predict(int id)
	{
		double sum = 0;
		double[] target = instanceSet.getInstanceD(id).getVector();
		
		for(int i=0;i<featureSize;++i)
		{
			int y = instanceSet.getClassID(i);
			double[] temp = instanceSet.getInstanceD(i).getVector();
			double kernal = kernelFunction(target,temp);
			
			sum += alpha[i]*y*kernal;
		}
		sum+=b;
		
		return sum;
	}
	
	//外层循环
	public int selectPeriphery()
	{
		for(int i=0;i<featureSize;++i)
		{
			if(alpha[i]<C && alpha[i]>0)
			{
				double pred = predict(i);
				int y = instanceSet.getClassID(i);
				if(pred*y != 1)
				{
					return i;
				}
			}
		}
		
		for(int i=0;i<featureSize;++i)
		{
			if(alpha[i] == C )
			{
				double pred = predict(i);
				int y = instanceSet.getClassID(i);
				if(pred*y > 1)
				{
					return i;
				}
			}
			else if(alpha[i] == 0)
			{
				double pred = predict(i);
				int y = instanceSet.getClassID(i);
				if(pred*y < 1)
				{
					return i;
				}
			}
		}
		
		return 0;
	}
	
	
	/**
	 * @comment:内层循环
	 * @return
	 * @return:int
	 */
	public int selectSubcoat(int id)
	{
		int resultID = 0;
		
		if(E[id]<0)
		{
			resultID = arrayMinID(E);
		}
		else
		{
			resultID = arrayMaxID(E);
		}
		
		return resultID;
	}
	
	public int arrayMaxID(double[] array)
	{
		double max = Double.MIN_VALUE;
		int id = 0;
		
		for(int i=0;i<array.length;++i)
		{
			if(array[i]>max)
			{
				max = array[i];
				id = i;
			}
		}
		
		return id;
	}
	
	
	public int arrayMinID(double[] array)
	{
		double min = Double.MAX_VALUE;
		int id = 0;
		
		for(int i=0;i<array.length;++i)
		{
			if(array[i]<min)
			{
				min = array[i];
				id = i;
			}
		}
		
		return id;
	}
	
	
	/**
	 * @comment: 计算α2的值
	 * @return
	 * @return:double
	 */
	public double calculateAlpha2(int alpha1ID,int alpha2ID)
	{
		double newValue = 0;
		double oldValue = alpha[alpha2ID];
		int typeid = instanceSet.getClassID(alpha2ID);
		double E1 = E[alpha1ID];
		double E2 = E[alpha2ID];
		double K11 = kernelFunction(instanceSet.getInstanceD(alpha1ID).getVector(),
				instanceSet.getInstanceD(alpha1ID).getVector());
		double K12 = kernelFunction(instanceSet.getInstanceD(alpha1ID).getVector(),
				instanceSet.getInstanceD(alpha2ID).getVector());
		double K22 = kernelFunction(instanceSet.getInstanceD(alpha2ID).getVector(),
				instanceSet.getInstanceD(alpha2ID).getVector());
		double eta = K11+K22-2*K12;
		
		newValue = oldValue + typeid*(E1 - E2)/eta;
		
		
		return newValue;
	}
	
	
	/**
	 * @comment:根据α2的限制条件，确定α2的最终值
	 * @return
	 * @return:double
	 */
	public double clipAlpha2(int alpha1ID,int alpha2ID)
	{
		double alpha2 = calculateAlpha2(alpha1ID,alpha2ID);
		Pair<Double,Double> pair = calculateAlphaLimit(alpha1ID,alpha2ID);
		
		if(alpha2 > pair.value)
		{
			alpha2 = pair.value;
		}else if(alpha2 < pair.key)
		{
			alpha2 = pair.key;
		}
		
		return alpha2;
	}
	
	/**
	 * @comment: 计算α2的下限和上限
	 * @return
	 * @return:Pair<Double,Double> <下限，上限>
	 */
	public Pair<Double,Double> calculateAlphaLimit(int alpha1ID,int alpha2ID)
	{
		
		double H = 0;
		double L = 0;
		
		int y1 = instanceSet.getClassID(alpha1ID);
		int y2 = instanceSet.getClassID(alpha2ID);
		double old1 = alpha[alpha1ID];
		double old2 = alpha[alpha2ID];
		
		
		if(y1 == y2)
		{
			L = old2-old1;
			
			H = C+old2-old1;
		}
		else
		{
			L = old2+old1-C;
		
			H = old2+old1;
		}
		
		if(L<0)
		{
			L = 0;
		}
		
		if(H>C)
		{
			H = C;
		}
		
		Pair<Double,Double> pair = new Pair<Double,Double>(L,H);
		
		return pair;
	}
	
	

	/**
	 * @comment:计算α1
	 * @param alpha1ID
	 * @param alpha2ID
	 * @param new2
	 * @return
	 * @return:double
	 */
	public double calculateAlpha1(int alpha1ID,int alpha2ID,double new2)
	{
		int y1 = instanceSet.getClassID(alpha1ID);
		int y2 = instanceSet.getClassID(alpha2ID);
		double old1 = alpha[alpha1ID];
		double old2 = alpha[alpha2ID];
		
		double new1 = old1 + y1*y2*( old2- new2);
		
		return new1;
	}
	

	/**
	 * @comment:计算b
	 * @return:void
	 */
	public double calculateB(int alpha1ID,int alpha2ID,
			double new1,double new2)
	{
		double b1 = calculateB1(alpha1ID,alpha2ID,new1,new2);
		double b2 = calculateB2(alpha1ID,alpha2ID,new1,new2);
		
		if(b1 == b2)
		{
			return b1;
		}
	
		return (b1+b2) / 2;
		
	}
	
	/**
	 * @comment:计算b1
	 * @return:void
	 */
	public double calculateB1(int alpha1ID,int alpha2ID,
			double new1,double new2)
	{
		int y1 = instanceSet.getClassID(alpha1ID);
		int y2 = instanceSet.getClassID(alpha2ID);
		double old1 = alpha[alpha1ID];
		double old2 = alpha[alpha2ID];
		
		double K11 = kernelFunction(instanceSet.getInstanceD(alpha1ID).getVector(),
				instanceSet.getInstanceD(alpha1ID).getVector());
		double K21 = kernelFunction(instanceSet.getInstanceD(alpha1ID).getVector(),
				instanceSet.getInstanceD(alpha2ID).getVector());
		
		double newb1 = -E[alpha1ID] - y1*K11*(new1 - old1) - y2*K21*(new2 - old2) + b;
	    
		return newb1;
		
	}
	
	/**
	 * @comment:计算b2
	 * @return:void
	 */
	public double calculateB2(int alpha1ID,int alpha2ID,
			double new1,double new2)
	{
		int y1 = instanceSet.getClassID(alpha1ID);
		int y2 = instanceSet.getClassID(alpha2ID);
		double old1 = alpha[alpha1ID];
		double old2 = alpha[alpha2ID];
		
		double K12 = kernelFunction(instanceSet.getInstanceD(alpha1ID).getVector(),
				instanceSet.getInstanceD(alpha2ID).getVector());
		double K22 = kernelFunction(instanceSet.getInstanceD(alpha2ID).getVector(),
				instanceSet.getInstanceD(alpha2ID).getVector());
		
		double newb1 = -E[alpha2ID] - y1*K12*(new1 - old1) - y2*K22*(new2 - old2) + b;
	    
		return newb1;
	}
	
	/**
	 * @comment:核函数
	 * @param xi
	 * @param xj
	 * @return
	 * @return:double
	 */
	public double kernelFunction(double[] xi,double[] xj)
	{
		double result = Kernel.linear(xi, xj);
		return result;
	}
	
	/**
	 * @comment:是否停止迭代
	 * @return
	 * @return:boolean
	 */
	public boolean isStop()
	{
		for(int i=0;i<featureSize;++i)
		{
			if(E[i] > ITERATIONS_VALUE)
			{
				return false;
			}
		}
		
		return true;
	}


	@Override
	public void clear() {
		
		alpha = null;
		
		w = null;
	    
		b = 0;
		
		C = 0;
		
		E = null;
		
		instanceSet.clear();
		
		featureSize = 0;
		
		length = 0;
		
		classNumb = 0;
	}
	
	public void saveModel(String path)
	{
		
	}

}
