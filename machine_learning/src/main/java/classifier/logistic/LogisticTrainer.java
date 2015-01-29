package classifier.logistic;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;








import manager.SingleValidation;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import base.InputFeatureD;
import base.InstanceD;
import util.VectorOperation;
import validation.Iris;
import classifier.AbstractTrainer;

/**
 * @author xiaohe
 * 创建于：2015年1月13日
 * 该logistic 没有用求导的方式学习，使用的SDG（随机梯度下降法）来求解的
 */
public class LogisticTrainer extends AbstractTrainer {
	Logger logger = Logger.getLogger(LogisticTrainer.class);

	int classNumb;
	
	//权重
	double[] weight;
	
	//输入的特征和类别标签
	List<InstanceD> instances;
	
	//特征的维度
	int dim;
	
	//最大迭代次数
	static final int ITERATIONS_NUMB = 1000;
	
	//学习率
	private double rate;
	

	public void init(InputFeatureD inputFeature) {
		// TODO Auto-generated method stub
		this.instances = inputFeature.getInstances();
		if(instances.size() == 0 )
		{
			logger.error("输入的特征向量为空！");
		}
		
		dim = inputFeature.getLength();
		weight = new double[dim];
		
		Arrays.fill(weight, 0);
		
		rate = 0.001;
		logger.info("dim:"+dim+" rate:"+0.001);
	}

	public void train() {
		 for(int i=0;i<ITERATIONS_NUMB;++i)
		 {
			 int j=0;
			 for(InstanceD vsm:instances)
			 {
				 double prediction = sigmoid(vsm.getVector());
				 double diff  = prediction - vsm.getType();
				 logger.info("迭代次数："+i+"-"+j+" 差距："+diff);
				 double[] temp = VectorOperation.constantMultip(vsm.getVector(), diff);
				 
				 weight = VectorOperation.addition(weight, temp);
				 ++j;
			 }
			 logger.info(Arrays.toString(weight));
			
		 }
	}
	
    private double sigmoid(double[] featureVector) {
		double linearTerm = VectorOperation.innerProduct(featureVector, weight);
		return 1.0 / (1.0 + Math.exp(-linearTerm));
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
		InputFeatureD inputFeature = iris.getInputFeature();
		
		LogisticTrainer logisticTrainer = new LogisticTrainer();
		logisticTrainer.init(inputFeature);
		logisticTrainer.train();
		double[] weight = logisticTrainer.getWeight();
		System.out.println(Arrays.toString(weight));
		
		for(int i=0;i<inputFeature.getSize();++i)
		{
			int classid;
			double temp = logisticTrainer.sigmoid(inputFeature.getInstanceD(i).getVector());
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
		}
	}

}
