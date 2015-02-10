package classifier.maxent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import base.InstanceI;
import base.InstanceSetI;
import classifier.AbstractTrainer;
import classifier.bayes.BayesModel;
import classifier.util.InstanceSetCensus;
import util.DistanceCalculation;
import util.Pair;
import util.VectorOperation;
import validation.Maxent;

public class MaxEntropyTrainer extends AbstractTrainer {
	Logger logger = Logger.getLogger(MaxEntropyTrainer.class);

	//最大迭代次数
	static final int ITERATIONS_NUMB = 1000;
		
	static final double ITERATIONS_VALUE = 0.001;
	
	double[] weight;
	
	//每维的特征对应的特征值
	int[][] features;
	
	//每维的特征值在特征函数中id，
	//这个数组的第一维是xi在输入特征中的维数，
	//第二维是xi的取值的id，即在features中的第二位的下标
	int[][] featuresID;
	
	//所有不同值特征的个数
	int featureSize;
	
	//特征函数  Pair<特征，类别id>
	List<Pair<Integer,Integer>> featureFunctionList = new ArrayList<Pair<Integer,Integer>>();
   
	//各个特征函数的数量
	List<Integer> featureFunctionNumb = new ArrayList<Integer>();
   
    //经过处理的特征向量，特征值都是features中的id
  	private List<InstanceI> instanceSet = new ArrayList<InstanceI>();
  	
  	//输入特征维度的最大值，这里输入的特征都是定维度的因此是一个固定的值
  	private int C = 0;
  	
  	//不同特征函数的数量，不是特征函数的总数
  	private int functionSize;
  	
  	//特征向量的长度
  	int length;
  	
  	MaxEntropyModel  model;
  	
  	/**
  	 * 类别的数量
  	 */
  	int classNumb;
  	
	@Override
	public void init(InstanceSetI instanceSet) {
		InstanceSetCensus instanceSetCensus = new InstanceSetCensus(instanceSet.getInstances());
		instanceSetCensus.excute();
		this.features = instanceSetCensus.getFeatures();
		this.instanceSet = instanceSetCensus.getOutputFeature();
		this.classNumb = instanceSetCensus.getClassNumb();
		this.length = instanceSet.getLength();
		
		setFeatureFunctionID();
		calculateFeatureFunction();
		
		functionSize = featureFunctionList.size();
		
		weight = new double[functionSize];
		Arrays.fill(weight, 0);
		
		this.C = length;
		
		
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
		
		this.featureSize = index;
	}
	
	
	public void calculateFeatureFunction()
	{
		for(int i=0;i<instanceSet.size();++i)
		{
			for(int j=0;j<length;++j)
			{
				int temp = instanceSet.get(i).getFeature(j);
				temp = featuresID[j][temp];
				
				Pair<Integer,Integer> pair = new Pair(temp,instanceSet.get(i).getType());
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
		logger.info("start");
		
		double[] lastWeight = new double[weight.length];
		
		double[] empiricalE = new double[functionSize];
		for(int i=0;i<functionSize;++i)
		{
			empiricalE[i] = (double)featureFunctionNumb.get(i)/instanceSet.size();
		}
		
		for(int i=0;i<ITERATIONS_NUMB;++i)
		{
			logger.info("iteration:"+i);
			lastWeight = weight;
			double[] delta = new double[functionSize];
			
			
			delta = calculateDelta(empiricalE);
			weight = VectorOperation.addition(weight, delta);
			
			
			double eps = DistanceCalculation.EuclideanDistance(lastWeight,weight);
			
			if(eps<ITERATIONS_VALUE)
			{
				logger.info(eps);
				break;
			}	
			
//			if(checkConvergence(lastWeight,weight))
//			{
//				break;
//			}
		}
		
		logger.info(Arrays.toString(weight));
	}
	
	private double[] calculateDelta(double[] empiricalE){
		
		double[] modelE = new double[functionSize];	
		double[] delta = new double[functionSize];
		
		Arrays.fill(modelE, 0);
		Arrays.fill(delta, 0);
		
		
		modelE = calculateModelE();
		
		
		for(int i=0;i<functionSize;++i)
		{
			delta[i] = (1.0/(double)3)*Math.log(((double)empiricalE[i]/modelE[i]));
		}
		return delta;
	}
	
	private double[] calculateModelE()
	{
		double[] modelE = new double[functionSize];
		Arrays.fill(modelE, 0);
		
		for(InstanceI instance:instanceSet)
		{
			double[] porb = infer(instance.getVector());
			for(int j = 0;j<classNumb;++j)
			{
				for(int i=0;i<length;++i)
				{
					int tempid = instance.getVector()[i];
					tempid  = featuresID[i][tempid];
					Pair<Integer,Integer> pair = new Pair<Integer,Integer>(tempid,j);
					int index = featureFunctionList.indexOf(pair);
					if(index != -1)
					{
						modelE[index] += porb[j]* (1.0 / instanceSet.size());
					}
					
				}
			}
		}
		
		return modelE;
	}
	
	private double[] infer(int[] vector)
	{
		double[] porb = new double[classNumb];
		double sum = 0;
		
		for(int j=0;j<classNumb;++j)
		{
			double tempSum = 0;
		      for(int i=0;i<length;++i)
		      {
			    int tempid = featuresID[i][vector[i]];
				Pair<Integer,Integer> pair = new Pair<Integer,Integer>(tempid,j);
				int index = featureFunctionList.indexOf(pair);
				if(index != -1)
				{
					tempSum+= weight[index];
				}
				
			}
		      
		    porb[j] =  Math.exp(tempSum);
		    sum += porb[j];
		}
		
		for(int j=0;j<classNumb;++j)
		{
			porb[j] = porb[j]/sum;
		}
		
		return porb;
	}
	
	public boolean checkConvergence(double[] w1, double[] w2)
    {
        for (int i = 0; i < w1.length; ++i)
        {
            if (Math.abs(w1[i] - w2[i]) >= 0.01)    // 收敛阀值0.01可自行调整
                return false;
        }
        return true;
    }

	@Override
	public int getClassNumb() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void clear() {
		weight = null;
		
		features = null;
		
		featuresID = null;
		
		featureSize = 0;
		
		featureFunctionList.clear();
		
		featureFunctionNumb.clear();
	    
	  	instanceSet.clear();
	  	
	  	C = 0;
	  	
	  	functionSize = 0;
	  	
	  	length = 0;

	}
	
	public void saveModel(String path) throws IOException
	{
		model = new MaxEntropyModel(features, featuresID,
				weight, classNumb, length,
				featureFunctionList);
		
		super.saveModel(path, model);
	}
	
	public static void main(String args) throws IOException
	{
		String path = "data/corpus/maxent.data";
	    Maxent maxent = new Maxent();
	    maxent.readData(path);	    
	    InstanceSetI inputFeature = maxent.getInputFeature();
	    
		MaxEntropyTrainer maxEntropy = new MaxEntropyTrainer();
		maxEntropy.init(inputFeature);
		maxEntropy.train();
		
	}

}
