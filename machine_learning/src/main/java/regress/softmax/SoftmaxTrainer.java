package regress.softmax;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import classifier.bayes.BayesModel;
import base.InstanceD;
import base.InstanceSetD;
import regress.AbstractRegressTrainer;
import regress.logistic.LogisticTrainer;
import util.VectorOperation;

/**
 * @author xiaohe
 * 创建于：2015年2月4日
 * 梯度下降法求解softmax
 * 
 * 上述问题已经修正，效果不好因为：
 * 1. 公式推导错误
 * 2. 正定项的参数错误
 * 
 * 具体参考：
 * http://deeplearning.stanford.edu/wiki/index.php/Softmax%E5%9B%9E%E5%BD%92
 */
public class SoftmaxTrainer extends AbstractRegressTrainer {
	Logger logger = Logger.getLogger(SoftmaxTrainer.class);
	
	SoftmaxModel model;
	
    int classNumb;
	
	//权重
	double[][] weight;
	
	//输入的特征和类别标签
	List<InstanceD> instances = new ArrayList<InstanceD>();
	
	int instanceSize = 0;
	
	//特征的维度
	int dim;
	
	//最大迭代次数
	static final int ITERATIONS_NUMB = 1000;
	
	static final double ITERATIONS_VALUE = 0.1;
	
	//学习率，步长
	private double alpha = 0.1;
	
	//正定项控制参数
	private double lambda = 0.005;
	
	//误差控制率,这个参数需要通过交叉验证来确定，非常重要的参数
	private double eps = 0.2;
	
	public void init(InstanceSetD instanceSet) {
		if(instanceSet.getSize() == 0 )
		{
			logger.error("输入的特征向量为空！");
		}
		
		trans2logist(instanceSet);
		
		classNumb = instanceSet.getClassNumb();
		
		dim = instanceSet.getLength()+1;
		
		weight = new double[classNumb][dim];
		
		for(int i=0;i<classNumb;++i)
		{
			Arrays.fill(weight[i], 0);
		}
		
		instanceSize = instanceSet.getSize();
		
		logger.info("dim:"+dim+" rate:"+alpha);
	}
	
	/**
	 * @comment:让输入的向量的长度加1，并且在最后赋值为1，表示截距
	 * @return:void
	 */
	public void trans2logist(InstanceSetD inputFeature)
	{
		for(InstanceD instance:inputFeature.getInstances())
		{
			double[] tempVector = new double[instance.getLength()+1];
			
			Arrays.fill(tempVector, 1);
			System.arraycopy(instance.getVector(), 0, tempVector, 0, instance.getLength());
			InstanceD instanceD = new InstanceD(instance.getType(),instance.getLength(),tempVector);
			instances.add(instanceD);
		}
	}
	
	public void clear()
	{
		instances.clear();
		weight = null;
	}
	
	/*
	 * 迭代公式：
	 * θj := θj-(αλ/2)θj+α/m*∑((1(j=j)-p(y=j|x;θj))*x)
	 */
	public void train() {
		
		for(int loops=0;loops<ITERATIONS_NUMB;++loops)
		{
			List<Integer> numbs = new ArrayList<Integer>();
			logger.info("loops:"+loops);
			for(int j=0;j<classNumb;++j)
			{
				weight[j] = iteration(j,numbs);
				
			}
			
			if(numbs.size()> classNumb*instanceSize*0.8)
			{
				break;
			}
			
//			System.out.println(Arrays.deepToString(weight));
		}
		
		
	}
	
	public  double[] iteration(int typeid,List<Integer> numbs)
	{
		double[] temp = new double[dim];
		double[] sum = new double[dim];
		double[] sumxi = new double[dim];
		double[] xi = new double[dim];
		double diff = 0;
		
		Arrays.fill(temp, 0);
		Arrays.fill(sum, 0);
		Arrays.fill(sumxi, 0);
		Arrays.fill(xi, 0);
		
		double tempPara = -alpha*lambda;
		temp = VectorOperation.constantMultip(weight[typeid], tempPara);
//		logger.info(Arrays.toString(temp));
		sum = VectorOperation.addition(weight[typeid], temp);
		
		for(int i=0;i<instances.size();++i)
		{
			if(instances.get(i).getType() == typeid)
			{
				diff = 1 - infer(typeid,instances.get(i).getVector());
			}
			else
			{
				diff = 0 - infer(typeid,instances.get(i).getVector());
			}
			
			if(Math.abs(diff)<eps)
			{
				numbs.add(i);
			}
//			logger.info("diff:"+diff);
			
			xi = VectorOperation.constantMultip(instances.get(i).getVector(),diff);
			sumxi = VectorOperation.addition(sumxi, xi);
			
		}
		
		temp = VectorOperation.constantMultip(sumxi,alpha/instanceSize);
		
		sum = VectorOperation.addition(temp, sum);
		
		return sum;
	}
	
	public double infer(int typeid,double[] vector)
	{
		double result = 0;
		
		double[] ex = new double[classNumb];
		double sum = 0;
				
		for(int i=0;i<classNumb;++i)
		{
			double temp = VectorOperation.innerProduct(weight[i],vector);
			ex[i] = Math.exp(temp);
			sum += ex[i];
		}
		
		result = ex[typeid]/sum;
		
//		logger.info("result:"+result);
		return result;
	}
	
	
	
	public void saveModel(String path) throws Exception {
        model = new SoftmaxModel(weight, dim,classNumb);
		
		super.saveModel(path, model);
	}
	
	
	public static void main(String[] args)
	{
	
	}
	
}
