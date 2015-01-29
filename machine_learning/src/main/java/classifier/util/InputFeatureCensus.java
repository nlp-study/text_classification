package classifier.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import base.InstanceD;
import base.InstanceI;

/**
 * @author xiaohe
 * 创建于：2015年1月29日
 * 为分类算法做前期的统计工作，主要是统计类别的数量，特征的数量，每个特征的值
 */
public class InputFeatureCensus {
Logger logger = Logger.getLogger(InputFeatureCensus.class);
	
	// 类别的数量
	private int K = 0;
	
	//特征总的维度
	private int dim;

	//提取出每个特诊有几个取值
	private int[][] features;

	//记录包含某个特征的取值的某个取值的样本的数量,第一维是特诊的id，第二维是取值的id
	private int[][] featuresNumb;

	//输入的特征向量
	private List<InstanceI> instanceInput = new ArrayList<InstanceI>();

	// 转化成功的int型的vsm
	private List<InstanceI> instanceOutput = new ArrayList<InstanceI>();;
	
	public InputFeatureCensus() {
	}

	public InputFeatureCensus(List<InstanceI> instanceDs) {
		this.instanceInput = instanceDs;
		if(instanceDs.size()>0)
		{
			dim = instanceDs.get(0).getLength();
		}	
	}
	
	
	public int getK() {
		return K;
	}

	public void setK(int k) {
		K = k;
	}

	public int getDim() {
		return dim;
	}

	
	public List<InstanceI> getVsms() {
		return instanceInput;
	}

	public void setInputFeature(List<InstanceI> instanceDs) {
		this.instanceInput = instanceDs;
	}

	public List<InstanceI> getOutputFeature() {
		return instanceOutput;
	}	
	
	public int[][] getFeatures() {
		return features;
	}

	public int[][] getFeaturesNumb() {
		return featuresNumb;
	}

	//用带参数的构造函数的时候，就不需要再调用这个方法
	public void init() {
		
		if(instanceInput.size()>0)
		{
			dim = instanceInput.get(0).getLength();
		}	
	}
	
	public void excute()
	{
		calcualteClassNumb();
		calculateFeatures();
		vsm2Int();
	}
	
	public void calcualteClassNumb()
	{
		Set<Integer> temp = new HashSet<Integer>();
		for(int i=0;i<instanceInput.size();++i)
		{
			temp.add(instanceInput.get(i).getType());
		}
		K = temp.size();
	}

	
	/**
	 * @comment:计算各个特诊的值，以及各个特征值的数量
	 * @return void
	 */
	public void calculateFeatures() {
		features = new int[dim][];
		featuresNumb = new int[dim][];

		for (int i = 0; i < dim; ++i) {
			List<Integer> temp = new ArrayList<Integer>();
			for (int j = 0; j < instanceInput.size(); ++j) {
				if (!temp.contains(instanceInput.get(j).getVector()[i])) {
					temp.add(instanceInput.get(j).getVector()[i]);
				}
			}

			features[i] = new int[temp.size()];
			featuresNumb[i] = new int[temp.size()];

			Collections.sort(temp);

			for (int k = 0; k < temp.size(); ++k) {
				features[i][k] = temp.get(k);
				featuresNumb[i][k] = 0;
			}
			
			for (int j = 0; j < instanceInput.size(); ++j) {
				for (int k = 0; k < temp.size(); ++k) {
					if(features[i][k] == instanceInput.get(j).getVector()[i])
					{
						++featuresNumb[i][k];
					}
				}
			}
		}
	}

	public void vsm2Int() {
		logger.info("vsmints size:"+instanceOutput.size());

		for (int j = 0; j < instanceInput.size(); ++j) {
			
			int[] temp = new int[dim];
			
			for (int i = 0; i < dim; ++i) {
                 for(int k=0;k<features[i].length;++k)
                 {
                	 if(instanceInput.get(j).getVector()[i] == features[i][k])
                	 {
                		 temp[i] = k;
                	 }
                 }
			}
			
			InstanceI vsmint = new InstanceI(instanceInput.get(j).getType(),dim, temp);
			instanceOutput.add(j, vsmint);
		}
		
		logger.info("vsmints size:"+instanceOutput.size());
		logger.info(instanceOutput.toString());
	}
	
	public String toString()
	{
		String str = "";
		str += K+"\r\n ";
		str += dim+"\r\n ";
		str += Arrays.deepToString(features)+"\r\n";
		str += Arrays.deepToString(featuresNumb)+"\r\n";
		str += instanceOutput.toString()+"\r\n";
		
		return str;
	}
}
