package regress.logistic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import regress.AbstractRegressTrainer;
import base.InstanceSetD;
import base.InstanceD;
import util.DistanceCalculation;
import util.VectorOperation;
import validation.Iris;

/**
 * @author xiaohe
 * 创建于：2015年1月13日
 * 该logistic 没有用求导的方式学习，使用的SDG（随机梯度下降法）来求解的
 */
public class LogisticTrainer extends AbstractRegressTrainer {
	Logger logger = Logger.getLogger(LogisticTrainer.class);

	int classNumb;
	
	//权重
	double[] weight;
	
	//输入的特征和类别标签
	List<InstanceD> instances = new ArrayList<InstanceD>();
	
	//特征的维度
	int dim;
	
	//最大迭代次数
	static final int ITERATIONS_NUMB = 1000;
	
	static final double ITERATIONS_VALUE = 0.01;
	
	//学习率
	private double rate;

	public void init(InstanceSetD inputFeature) {
		// TODO Auto-generated method stub
//		this.instances = inputFeature.getInstances();
		
		
		if(instances.size() == 0 )
		{
			logger.error("输入的特征向量为空！");
		}
		
		trans2logist(inputFeature);
		
		dim = inputFeature.getLength();
		weight = new double[dim+1];
		
		Arrays.fill(weight, 0);
		
		rate = 0.001;
		logger.info("dim:"+dim+" rate:"+rate);
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
	}

	public void train() {
		 int controlNumb = 3;
		 double [][] iterationControl = new double[controlNumb][weight.length];
		 for(int i=0;i<controlNumb;++i)
		 {
			 Arrays.fill(iterationControl[i], 0);
		 }
		 
		 for(int i=0;i<ITERATIONS_NUMB;++i)
		 {
			 logger.info("iteration:"+i);
			 for(InstanceD instance:instances)
			 {
				 double prediction = sigmoid(instance.getVector());
				 double diff  = instance.getType() - prediction;
				 diff = rate * diff;
				 double[] temp = VectorOperation.constantMultip(instance.getVector(), diff);
				 
				 weight = VectorOperation.addition(weight, temp);
			 }
			 if(i>controlNumb-1)
			 {
				 for(int j=controlNumb-1;j>0;--j)
				 {
//					 iterationControl[j] = iterationControl[j-1];
//					 System.arraycopy(iterationControl[j-1], 0, iterationControl[j], 0, weight.length);
					 
					 copyWeight(iterationControl[j-1],iterationControl[j]);
					 
				 }
//				 iterationControl[0] = weight;
				 
//				 System.arraycopy(weight, 0, iterationControl[0], 0, weight.length);
				 				 
				 copyWeight(weight,iterationControl[0]);
				 
				 logger.info(Arrays.deepToString(iterationControl));
				 if(isStop(iterationControl))
				 {
					 break;
				 }
			 }
			 else
			 {
//				 iterationControl[i] = weight;
//				 System.arraycopy(weight, 0, iterationControl[i], 0, weight.length);
				 
				 copyWeight(weight,iterationControl[i]);
				 logger.info(Arrays.deepToString(iterationControl));
			 }
			 
			 
			 
			
			 logger.info(Arrays.toString(weight));
		 }
	}
	
	private void copyWeight(double[]v1,double[] v2)
	{
		for(int i=0;i<v1.length;++i)
		{
			v2[i] = v1[i];
		}
	}
	
	private boolean isStop(double [][] iterationControl)
	{
		for(int i=1;i<iterationControl.length;++i)
		{
			double result = DistanceCalculation.EuclideanDistance(iterationControl[i], iterationControl[i-1]);
//			logger.info(Arrays.toString(iterationControl[i]));
//			logger.info(Arrays.toString(iterationControl[i-1]));
			logger.info("result:"+result);
			if(result>ITERATIONS_VALUE)
			{
				return false;
			}
		}		
		
		return true;
	}
	
    private double sigmoid(double[] featureVector) {
		double linearTerm = VectorOperation.innerProduct(featureVector, weight);
		return 1 / (1 + Math.exp(-linearTerm));
	}

	@Override
	public void saveModel(String path) throws Exception {
		// TODO Auto-generated method stub

	}
	
	public int getClassNumb() {
		return classNumb;
	}


	public void setClassNumb(int classNumb) {
		this.classNumb = classNumb;
	}
	
	public double[] getWeight() {
		return weight;
	}

	public void setWeight(double[] weight) {
		this.weight = weight;
	}

	public static void main(String[] args) throws IOException
	{
		PropertyConfigurator.configure("log4j.properties");
		String path = "data/corpus/iris_bin.data";
		Iris iris = new Iris();
		iris.readData(path);
		InstanceSetD inputFeature = iris.getInputFeature();
		
		LogisticTrainer logisticTrainer = new LogisticTrainer();
		logisticTrainer.init(inputFeature);
		logisticTrainer.train();
		double[] weight = logisticTrainer.getWeight();
		System.out.println(Arrays.toString(weight));
		
		for(int i=0;i<inputFeature.getSize();++i)
		{
           double[] tempVector = new double[inputFeature.getInstanceD(i).getLength()+1];
			
			Arrays.fill(tempVector, 1);
			System.arraycopy(inputFeature.getInstanceD(i).getVector(), 0, tempVector, 0, inputFeature.getInstanceD(i).getLength());
			
			int classid;
			double temp = logisticTrainer.sigmoid(tempVector);
			
			if(temp>0.5)
			{
				classid = 1;
			}
			else
			{
				classid = 0;
			}
			
			if(inputFeature.getInstanceD(i).getType() != classid)
			{
				System.out.println("infer failed:"+i);
			}
			else
			{
				System.out.println("infer success:"+i);
			}
		}
	}

}
