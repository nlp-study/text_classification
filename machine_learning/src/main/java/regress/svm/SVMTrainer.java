package regress.svm;

import java.io.IOException;
import java.util.Arrays;

import org.apache.log4j.Logger;

import regress.AbstractRegressTrainer;
import util.Pair;
import base.InstanceSetD;

/**
 * @author xiaohe
 * 创建于：2015年2月13日
 * 注：用smo算法进行学习
 *
 * 基本公式：
 * f(x) = sign(w·x+b)
 * 
 * 实现中遇到的问题：
 * 1. 算法不能自动停止
 * 2. 对E为0，但是精度达不到要求的情况没有考虑到
 * 3. 迭代次数非常少，估计是实现中有什么问题没有考虑到，还需要改进
 */
public class SVMTrainer extends AbstractRegressTrainer {
	Logger logger = Logger.getLogger(SVMTrainer.class);
	
	//最大迭代次数		
	static final int ITERATIONS_NUMB = 1000;
	
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
	
	/**
	 * 松弛变量
	 */
	private double tolerance = 0.001;
	
	/********************输入特征相关***********************/
	InstanceSetD instanceSet = new InstanceSetD();
	
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
		
		int i = 0;
		double[] alphaLast = new double[featureSize];
		double[] ELast = new double[featureSize];
		double bLast = 0;
		
		while(isKeppingRun())
		{
			System.arraycopy(alpha, 0, alphaLast, 0, alpha.length);
			System.arraycopy(E, 0, ELast, 0, E.length);
			bLast = b;
			
			logger.info("iteration:"+i);
			
			iteration();

//			logger.info("α:"+Arrays.toString(alpha));
			++i;
			if(i == ITERATIONS_NUMB)
			{
				break;
			}
			if(Arrays.equals(alphaLast, alpha) && Arrays.equals(ELast, E) && bLast == b)
			{
				break;
			}
		}
		
	}
	
	
	/**
	 * @comment:一次迭代过程
	 * @return:void
	 */
	public void iteration()
	{
		
		calculateE();
//		logger.info("E:"+Arrays.toString(E));
		
		int alpha1ID = selectPeriphery();
		int alpha2ID = selectSubcoat(alpha1ID);
		
//		logger.info("α1 id:"+alpha1ID+" "+"α2 id:"+alpha2ID);
		
		double alpha2 = clipAlpha2(alpha1ID, alpha2ID);
		double alpha1 = calculateAlpha1(alpha1ID, alpha2ID,alpha2);
				
		b = calculateB(alpha1ID,alpha2ID,alpha1,alpha2);
		
		alpha[alpha1ID] = alpha1;
		alpha[alpha2ID] = alpha2;
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
				if(pred*y > 1-tolerance)
				{
					return i;
				}
			}
			else if(alpha[i] == 0)
			{
				double pred = predict(i);
				int y = instanceSet.getClassID(i);
				if(pred*y < 1 +tolerance)
				{
					return i;
				}
			}
			
		}
		
		logger.info("所有的输入特征都满足了要求！");
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
			resultID = arrayMaxID(E,id);
		}
		else if(E[id]>0)
		{
			resultID = arrayMinID(E,id);
		}
		else
		{
			resultID = arrayMaxDiff(E,id);
		}
		
		
		return resultID;
	}
	
	public int arrayMaxID(double[] array,int excludedID)
	{
		double max = -Double.MAX_VALUE;
		int id = 0;
		
		for(int i=0;i<array.length;++i)
		{
			if(array[i]>max && i!=excludedID)
			{
				max = array[i];
				id = i;
			}
		}
		
		return id;
	}
	
	
	public int arrayMinID(double[] array,int excludedID)
	{
		double min = Double.MAX_VALUE;
		int id = 0;
		
		for(int i=0;i<array.length;++i)
		{
			if(array[i]<min && i!=excludedID)
			{
				min = array[i];
				id = i;
			}
		}
		
		return id;
	}
	
	/**
	 * @comment:最大差
	 * @param array
	 * @param input
	 * @return
	 * @return:int
	 */
	public int arrayMaxDiff(double[] array,int excludedID)
	{
		double max = 0;
		int id = 0;
		double input = array[excludedID];
		
		for(int i=0;i<array.length;++i)
		{
			double diff  = Math.abs(array[i] - input);
			if(max<diff && i!=excludedID)
			{
				max = diff;
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
		
		
		if(y1 != y2)
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
	public boolean isKeppingRun()
	{
		for(int i=0;i<featureSize;++i)
		{
			double functMargin = calculateFunctionMargin(i);
			
			if(alpha[i] > C || alpha[i] < 0)
			{
				return true;
			}
			else if(alpha[i]  == 0)
			{
				if( functMargin<1)
					return true;
			}
			else if(alpha[i]  == C)
			{
				if(functMargin>1)
					return true;
			}
			else 
			{
				if(functMargin == 1)
					return true;
			}
			
//			if(Math.abs(E[i]) > ITERATIONS_VALUE)
//			{
//				logger.info("还不满足精度要求,"+"id:"+E[i]+" value:"+E[i]);
//				return true;
//			}
		}
		
		return false;
	}
	
	public double calculateFunctionMargin(int id)
	{
		double margin = instanceSet.getClassID(id)*predict(id);
		return margin;
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
	
	public void saveModel(String path) throws IOException
	{
		model = new SVMModel(w,b,alpha,instanceSet);
		super.saveModel(path, model);
	}

}
