package classifier.maxent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import util.Pair;
import classifier.AbstractInfer;

public class MaxEntropyInfer extends AbstractInfer {
	Logger logger = Logger.getLogger(MaxEntropyInfer.class);

	 //每维的特征对应的特征值
	int[][] features;
	
	//每维的特征值在特征函数中id，
	//这个数组的第一维是xi在输入特征中的维数，
	//第二维是xi的取值的id，即在features中的第二位的下标
	int[][] featuresID;	
	
	double[] weight;
	
	int classNumb;
	
	int length;
	
	//特征函数  Pair<特征，类别id>
	List<Pair<Integer,Integer>> featureFunctionList = new ArrayList<Pair<Integer,Integer>>();
	   	
	MaxEntropyModel model;
	
	@Override
	public void init(String path) throws Exception {
		// TODO Auto-generated method stub
		model = (MaxEntropyModel)super.initModel(path);
		
		features = model.getFeatures();
		
		featuresID = model.getFeaturesID();
		
		weight = model.getWeight();
		
		classNumb = model.getClassNumb();
		
		length = model.getLength();
	}

	@Override
	public int infer(int[] input) {
		// TODO Auto-generated method stub
		
		if(length != input.length)
		{
			logger.error("input vector length is wrong!");
			return -1;
		}
		
		double[] porb = new double[classNumb];
		double sum = 0;
		double max = 0;
		int typeid = -1;
		
		for(int j=0;j<classNumb;++j)
		{
			double tempSum = 0;
		      for(int i=0;i<length;++i)
		      {	
		    	
				int tempid = selectFeatureFunctionID(i,input[i]);
				if(tempid !=-1 )
				{
					Pair<Integer,Integer> pair = new Pair<Integer,Integer>(tempid,j);
					int index = featureFunctionList.indexOf(pair);
					if(index == -1)
					{
						logger.error("Error,no exist pair！");
					}
					else
					{
						tempSum+= weight[index];
					}
				}				
			}
		      
		    porb[j] =  Math.exp(tempSum);
		    if(max< porb[j])
		    {
		    	typeid = j;
		    	max = porb[j];
		    }
		    sum += porb[j];
		}
		
		for(int j=0;j<classNumb;++j)
		{
			porb[j]= porb[j]/sum;
		}
		
		logger.info("概率是："+Arrays.toString(porb));
				
		return typeid;
	}
	
	
	public int selectFeatureFunctionID(int dim,int value)
	{
		
		for(int i=0;i<features[dim].length;++i)
		{
		   if(value == features[dim][i])
		   {
			   return i;
		   }
		}
		return -1;
		
	}
	
	

}
