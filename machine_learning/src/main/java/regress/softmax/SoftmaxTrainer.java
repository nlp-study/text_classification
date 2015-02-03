package regress.softmax;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import base.InstanceD;
import base.InstanceSetD;
import regress.AbstractRegressTrainer;
import regress.logistic.LogisticTrainer;
import util.VectorOperation;

public class SoftmaxTrainer extends AbstractRegressTrainer {
	Logger logger = Logger.getLogger(LogisticTrainer.class);
	
	SoftmaxModel softmaxModel = new SoftmaxModel();
	
    int classNumb;
	
	//权重
	double[][] weight;
	
	//输入的特征和类别标签
	List<InstanceD> instances = new ArrayList<InstanceD>();
	
	//特征的维度
	int dim;
	
	//最大迭代次数
	static final int ITERATIONS_NUMB = 1000;
	
	static final double ITERATIONS_VALUE = 0.01;
	
	//学习率
	private double alpha;
	
	//权重衰减参数
	private double lambda;
	
	public void init(InstanceSetD inputFeature) {
		if(instances.size() == 0 )
		{
			logger.error("输入的特征向量为空！");
		}
		
		trans2logist(inputFeature);
		
		classNumb = inputFeature.getClassNumb();
		
		dim = inputFeature.getLength();
		
		weight = new double[classNumb][dim+1];
		
		for(int i=0;i<classNumb;++i)
		{
			Arrays.fill(weight[i], 0);
		}
		
		alpha = 0.001;
		lambda = 1;
		
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

	public void train() {
		
		for(int i=0;i<ITERATIONS_NUMB;++i)
		{
			for(int j=0;j<classNumb;++j)
			{
				weight[j] = iteration(j);
			}
		}
		
	}
	
	public double[] iteration(int typeid)
	{
		double[] temp = new double[dim+1];
		Arrays.fill(temp, 0);
		double sum = 0;
		
		for(int i=0;i<instances.size();++i)
		{
			if(instances.get(i).getType() == typeid)
			{
				sum+=infer(typeid,instances.get(i).getVector());
			}
		}
		
		return temp;
	}
	
	public double infer(int typeid,double[] vector)
	{
		double result = 0;
		
		double[] ex = new double[classNumb];
		double sum = 0;
				
		for(int i=0;i<classNumb;++i)
		{
			ex[i] = VectorOperation.innerProduct(weight[i],vector);
			sum += ex[i];
		}
		
		result = ex[typeid]/sum;
		
		
		return result;
	}
	
	
	
	public void saveModel(String path) throws Exception {

	}
	
}
