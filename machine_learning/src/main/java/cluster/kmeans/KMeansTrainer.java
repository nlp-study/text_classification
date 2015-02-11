package cluster.kmeans;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import util.VectorOperation;
import validation.Iris;
import base.InstanceD;
import base.InstanceSetD;

public class KMeansTrainer {
	static Logger logger = Logger.getLogger(KMeansTrainer.class);

	// 最大迭代次数
	static final int ITERATIONS_NUMB = 1000;

	//误差精度
	static final double ITERATIONS_VALUE = 0.1;

	//key 是输入向量的id，value是所对应的class的id，0~K-1
	Map<Integer,Integer> classes = new HashMap<Integer,Integer>();;
	
	List<double[]> inputFeature = new ArrayList<double[]>();
	
	//类别的数量
	int K;
	
	double[][] meanVectors;
	
	int length;
	
	int inputSize;
		
	public Map<Integer, Integer> getClasses() {
		return classes;
	}

	public void setClasses(Map<Integer, Integer> classes) {
		this.classes = classes;
	}

	public void init(int K,int length,List<double[]> inputFeature)
	{
		this.K = K;
		this.length = length;
		this.inputFeature = inputFeature;
		this.inputSize = inputFeature.size();
		meanVectors = new double[K][length];
		
		for(int i=0;i<inputSize;++i)
		{
			classes.put(i, -1);
		}
	}
	
	public void initMeanVector()
	{
		Random random = new  Random();
		
		int temp = 0;
		for(int i=0;i<K;++i)
		{
			temp = random.nextInt(inputSize);
			meanVectors[i] = inputFeature.get(temp);
		}
	}
	
	public void excute()
	{
		initMeanVector();
		
		for(int i=0;i<ITERATIONS_NUMB;++i)
		{
			confirmClass();
			calculateMeanVector();
		}
	}
	
	
	/**
	 * @comment:各个输入向量确定自己的类别
	 * @return:void
	 */
	public void confirmClass()
	{
		int typeid = 0;
		double max = -1;		
		for(int i=0;i<inputSize;++i)
		{
			for(int j=0;j<K;++j)
			{
				double tempSum = VectorOperation.errorSumSquares(inputFeature.get(i),meanVectors[j] );
			    if(tempSum > max)
			    {
			    	typeid = j;
			    	max = tempSum;
			    }
			}
			classes.put(i, typeid);
		}
	}
	
	public void calculateMeanVector()
	{
		double[][] tempVector = new double[K][length];
		int[] numb = new int[K];
		Arrays.fill(numb, 0);
		
		for(int i=0;i<K;++i)
		{
			Arrays.fill(tempVector[i], 0.0);
		}
		
		for(int i=0;i<inputSize;++i)
		{
			int typeid = classes.get(i);
			tempVector[typeid] = VectorOperation.addition(tempVector[typeid], inputFeature.get(i));
			numb[typeid]++;
		}
		
		for(int i=0;i<K;++i)
		{
			tempVector[i] = VectorOperation.constantMultip(tempVector[i], 1.0/numb[i]);
		}
		
		meanVectors = tempVector;
	}
	
	public static void main(String[] args) throws IOException
	{
		PropertyConfigurator.configure("log4j.properties");
		
//		String path = "data/corpus/iris.data";
//		Iris iris = new Iris();
//		iris.readData(path);
//		InstanceSetD instances = iris.getInputFeature();
		
		double[] temp1 = {1.0,1.0};
		double[] temp2 = {2.0,2.0};
		double[] temp3 = {10.0,10.0};
		
		InstanceD instance1 = new InstanceD(1,2,temp1);
		InstanceD instance2 = new InstanceD(1,2,temp2);
		InstanceD instance3 = new InstanceD(2,2,temp3);
		
		
		InstanceSetD instances = new InstanceSetD();
		instances.add(instance1);
		instances.add(instance2);
		instances.add(instance3);
		
		
		List<double[]> inputFeature = new ArrayList<double[]>();
		
		for(int i=0;i<instances.getSize();++i)
		{
			double[] temp = instances.getInstanceD(i).getVector();
			inputFeature.add(temp);
		}
		
		
		KMeansTrainer kmeans = new KMeansTrainer();
		kmeans.init(instances.getClassNumb(), instances.getLength(), inputFeature);
		kmeans.excute();
		
		Map<Integer,Integer> classes = kmeans.getClasses();
		
		int sum = 0;
		for(int i=0;i<instances.getSize();++i)
		{
			int target = classes.get(i);
			int real = instances.getClassID(i);
			
			if(target == real)
			{
				sum++;
				logger.info("ok:"+i);
			}
			else
			{
				logger.info("wrong:"+i);
			}
		}
		
		double p = (double)sum/instances.getSize();
		
		logger.info("准确率："+p);
		
	}

}
