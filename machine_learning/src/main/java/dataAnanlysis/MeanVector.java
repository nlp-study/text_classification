package dataAnanlysis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import util.Pair;
import util.VectorOperation;
import validation.Iris;
import base.InstanceD;
import base.InstanceSetD;
import cluster.kmeans.KMeansTrainer;

/**
 * @author xiaohe
 * 创建于：2015年2月12日
 * 计算各个类的中心向量，然后计算各个特征相对中心向量的距离来确定类别，看看数据的分布
 */
public class MeanVector {
	static Logger logger = Logger.getLogger(KMeansTrainer.class);
	
	//真实的类别id，计算出来的类别id
	List<Pair<Integer,Integer>>  compareResult = new ArrayList<Pair<Integer,Integer>>();
	
	InstanceSetD instances;
	
	//类别的数量
	int K;
	
	double[][] meanVectors;
	
	int length;
	
	int inputSize;
		
	public void init(InstanceSetD instances)
	{
		this.K = instances.getClassNumb();
		this.length = instances.getLength();
		this.instances = instances;
		this.inputSize = instances.getSize();
		
		meanVectors = new double[K][length];
		
		initMeanVector();
	}
	
	public void initMeanVector()
	{
		double[][] tempVector = new double[K][length];
		int[] numb = new int[K];
		Arrays.fill(numb, 0);
		
		for(int i=0;i<K;++i)
		{
			Arrays.fill(tempVector[i], 0.0);
		}
		
		for(int i=0;i<instances.getSize();++i)
		{
			int typeid = instances.getClassID(i);
			tempVector[typeid] = VectorOperation.addition(tempVector[typeid], instances.getInstanceD(i).getVector());
			numb[typeid]++;
		}
		
		for(int i=0;i<K;++i)
		{
			tempVector[i] = VectorOperation.constantMultip(tempVector[i], 1.0/numb[i]);
		}
		
		meanVectors = tempVector;
	}
	
	public void excute()
	{
		confirmClass();
		calculateResult();
	}
	
	
	/**
	 * @comment:各个输入向量确定自己的类别
	 * @return:void
	 */
	public void confirmClass()
	{
		int typeid = 0;
			
		for(int i=0;i<inputSize;++i)
		{
			double min = Double.MAX_VALUE;	
			for(int j=0;j<K;++j)
			{
				double tempSum = VectorOperation.errorSumSquares(instances.getInstanceD(i).getVector(),meanVectors[j] );
			    if(tempSum < min)
			    {
			    	typeid = j;
			    	min = tempSum;
			    }
			}
			
			Pair<Integer,Integer> pair = new Pair<Integer,Integer>(instances.getClassID(i),typeid);
			
			compareResult.add(pair);
		}
	}
	
	public void calculateResult()
	{
		int[] everyClassNumb = new int[K];
		int[] everyRealClassNumb = new int[K];
		
		
		for(int i=0;i<compareResult.size();++i)
		{
			Pair<Integer,Integer> pair = compareResult.get(i);
			if(pair.key == pair.value)
			{
				everyClassNumb[pair.key]++;
			}
			everyRealClassNumb[pair.key]++;
		}
		
		double[] classDistrub = new double[K];
		for(int i=0;i<K;++i)
		{
			classDistrub[i] = (double)everyClassNumb[i]/everyRealClassNumb[i];
		}
		
		logger.info(Arrays.toString(classDistrub));
	}
	
	public static void main(String[] args) throws IOException
	{
		PropertyConfigurator.configure("log4j.properties");
		
		String path = "data/corpus/iris.data";
		Iris iris = new Iris();
		iris.readData(path);
		InstanceSetD instances = iris.getInputFeature();
		
		double[] temp1 = {1.0,1.0};
		double[] temp2 = {2.0,2.0};
		double[] temp3 = {10.0,10.0};
		double[] temp4 = {12.0,12.0};
		
		InstanceD instance1 = new InstanceD(0,2,temp1);
		InstanceD instance2 = new InstanceD(0,2,temp2);
		InstanceD instance3 = new InstanceD(1,2,temp3);
		InstanceD instance4 = new InstanceD(1,2,temp4);
		
//		InstanceSetD instances = new InstanceSetD();
//		instances.add(instance1);
//		instances.add(instance2);
//		instances.add(instance3);
//		instances.add(instance4);
		
		List<double[]> inputFeature = new ArrayList<double[]>();
		
		for(int i=0;i<instances.getSize();++i)
		{
			double[] temp = instances.getInstanceD(i).getVector();
			inputFeature.add(temp);
		}
		
		
		MeanVector  meanVector = new MeanVector();
		meanVector.init(instances);
		meanVector.excute();
		
	}
	
	
	

}
