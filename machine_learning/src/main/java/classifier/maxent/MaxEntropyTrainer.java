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
	
	//特征所有不同值的数量
	int size;
	
	//特征函数
	List<Pair<Integer,Integer>> featureFunctionList = new ArrayList<Pair<Integer,Integer>>();
   
	//各个特征函数的数量
	List<Integer> featureFunctionNumb = new ArrayList<Integer>();
   
    //经过处理的特征向量，特征值都是features中的id
  	private List<InstanceI> instanceSet = new ArrayList<InstanceI>();
    
  	//每个输入向量对应的类别
  	private List<Integer>  labels = new ArrayList<Integer>();
  	
  	//所有特征函数数量的和
  	private int C = 0;
  	
  	//特征向量的长度
  	int length;
  	
	@Override
	public void init(InstanceSetI instanceSet) {
		InstanceSetCensus instanceSetCensus = new InstanceSetCensus(instanceSet.getInstances());
		instanceSetCensus.excute();
		this.features = instanceSetCensus.getFeatures();
		this.instanceSet = instanceSetCensus.getOutputFeature();
		this.labels = instanceSet.getLabels();
		this.length = instanceSet.getLength();
		
		setFeatureFunctionID();
		calculateFeatureFunction();
		
		weight = new double[featureFunctionList.size()];
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
			double[] delta = calculateDelta();
			weight = VectorOperation.addition(weight, delta);
			
			lastWeight = weight;
			
			double eps = DistanceCalculation.EuclideanDistance(lastWeight,weight);
			if(eps<ITERATIONS_VALUE)
			{
				break;
			}
		}
	}
	
	private double[] calculateDelta(){
		return null;
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
