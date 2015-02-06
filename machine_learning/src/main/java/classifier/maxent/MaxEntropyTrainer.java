package classifier.maxent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import base.InstanceI;
import base.InstanceSetI;
import classifier.AbstractTrainer;
import classifier.util.InstanceSetCensus;
import util.DistanceCalculation;
import util.Pair;
import util.VectorOperation;

public class MaxEntropyTrainer extends AbstractTrainer {
	
	//最大迭代次数
	static final int ITERATIONS_NUMB = 1000;
		
	static final double ITERATIONS_VALUE = 0.1;
	
	double[] weight;
	
	//每维的特征对应的特征值
	int[][] features;
	
	//每维的特征值在特征函数中id，
	//这个数组的第一维是xi在输入特征中的维数，
	//第二维是xi的取值的id，即在features中的第二位的下标
	int[][] featuresID;
	
	//所有不同值特征的个数
	int size;
	
	//特征函数  Pair<特征，类别id>
	List<Pair<Integer,Integer>> featureFunctionList = new ArrayList<Pair<Integer,Integer>>();
   
	//各个特征函数的数量
	List<Integer> featureFunctionNumb = new ArrayList<Integer>();
   
    //经过处理的特征向量，特征值都是features中的id
  	private List<InstanceI> instanceSet = new ArrayList<InstanceI>();
  	
  	//所有特征函数数量的和
  	private int C = 0;
  	
  	//不同特征函数的数量，不是特征函数的总数
  	private int functionSize = 0;
  	
  	//特征向量的长度
  	int length;
  	
  	//特征的数量
  	int classNumb;
  	
	@Override
	public void init(InstanceSetI instanceSet) {
		InstanceSetCensus instanceSetCensus = new InstanceSetCensus(instanceSet.getInstances());
		instanceSetCensus.excute();
		this.features = instanceSetCensus.getFeatures();
		this.instanceSet = instanceSetCensus.getOutputFeature();
		this.classNumb = instanceSetCensus.getK();
		this.length = instanceSet.getLength();
		
		setFeatureFunctionID();
		calculateFeatureFunction();
		
		functionSize = featureFunctionList.size();
		
		weight = new double[functionSize];
		Arrays.fill(weight, 0);
		
		C = length*instanceSet.getSize();
		
		
	}
	
	public void setFeatureFunctionID()
	{
		featuresID = new int[features.length][];
		
		int index = 0;
		for(int i=0;i<features.length;++i)
		{
			featuresID[i] = new int[features[i].length];
			
			for(int j=0;j<features[i].length;++j)
			{
				featuresID[i][j] = index;
				++index;
			}
		}
		
		this.size = index;
	}
	
	
	public void calculateFeatureFunction()
	{
		for(int i=0;i<instanceSet.size();++i)
		{
			for(int j=0;j<length;++j)
			{
				int temp = instanceSet.get(i).getFeature(j);
				temp = featuresID[j][temp];
				
				Pair pair = new Pair(temp,instanceSet.get(i).getType());
				int index = featureFunctionList.indexOf(pair);
				
				if(index == -1)
				{
					featureFunctionList.add(pair);
					featureFunctionNumb.add(1);
				}
				else
				{
					featureFunctionNumb.set(index, featureFunctionNumb.get(index)+1);
				}
			}
		}
	}

	@Override
	public void train() {
		
		double[] lastWeight = new double[weight.length];
		
		for(int i=0;i<ITERATIONS_NUMB;++i)
		{
			lastWeight = weight;
			double[] delta = new double[functionSize];
			
			for(int j=0;j<instanceSet.size();++j)
			{
				delta = calculateDelta(instanceSet.get(i).getType(),instanceSet.get(i).getVector());
				weight = VectorOperation.addition(weight, delta);
			}
						
			lastWeight = weight;
			
			double eps = DistanceCalculation.EuclideanDistance(lastWeight,weight);
			if(eps<ITERATIONS_VALUE)
			{
				break;
			}
		}
	}
	
	private double[] calculateDelta(int typeid,int[] vector){
		double[] EEmp = new double[functionSize];
		double[] EReal = new double[functionSize];	
		double[] delta = new double[functionSize];
		
		Arrays.fill(EEmp, 0);
		Arrays.fill(EReal, 0);
		Arrays.fill(delta, 0);
		
		for(int i=0;i<length;++i)
		{
			int temp = featuresID[i][vector[i]];
			Pair pair = new Pair(temp,typeid);
			int index = featureFunctionList.indexOf(pair);
			EEmp[index] = featureFunctionNumb.get(index);
			
		}
		
		return delta;
	}
	
	private double[] calculateRealEstimation(int x,int typeid)
	{
		
	}
	
	private double infer(int[] vector,int typeid)
	{
		double[] porb = new double[functionSize];
		double sum = 0;
		
		for(int i=0;i<length;++i)
		{
			for(int j=0;j<classNumb;++j)
			{
				int temp
			}
			porb[i] = 
		}
	}

	@Override
	public int getClassNumb() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub

	}

}
