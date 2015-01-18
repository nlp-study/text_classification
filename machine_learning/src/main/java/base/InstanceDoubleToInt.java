package base;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import manager.BinaryClassValidation;

import org.apache.log4j.Logger;

/**
 * @author xiaohe 2015年1月7日 将vsm 由double类型转化成int类型
 */
public class InstanceDoubleToInt {
	Logger logger = Logger.getLogger(InstanceDoubleToInt.class);
	
	// 类别的数量
	private int K = 0;
	
	//特征总的维度
	private int dim;

	// 提取出每个特诊有几个取值
	private double[][] features;

	// 记录某个特征的取值和每个取值的样本的数量,第一维是特诊的id，第二维是取值的id
	private int[][] featuresNumb;

	// 输入的特征向量
	private List<InstanceD> instanceDs = new ArrayList<InstanceD>();

	// 转化成功的int型的vsm
	private List<InstanceI> instanceIs = new ArrayList<InstanceI>();;
	
	public InstanceDoubleToInt() {
	}

	public InstanceDoubleToInt(List<InstanceD> instanceDs) {
		this.instanceDs = instanceDs;
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

	
	public List<InstanceD> getVsms() {
		return instanceDs;
	}

	public void setVsms(List<InstanceD> instanceDs) {
		this.instanceDs = instanceDs;
	}

	public List<InstanceI> getVsmints() {
		return instanceIs;
	}

	public void setVsmints(List<InstanceI> vsmints) {
		this.instanceIs = vsmints;
	}
	
	public double[][] getFeatures() {
		return features;
	}

	public void setFeatures(double[][] features) {
		this.features = features;
	}

	public int[][] getFeaturesNumb() {
		return featuresNumb;
	}

	public void setFeaturesNumb(int[][] featuresNumb) {
		this.featuresNumb = featuresNumb;
	}

	//用带参数的构造函数的时候，就不需要再调用这个方法
	public void init() {
		
		if(instanceDs.size()>0)
		{
			dim = instanceDs.get(0).getLength();
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
		for(int i=0;i<instanceDs.size();++i)
		{
			temp.add(instanceDs.get(i).getType());
		}
		K = temp.size();
	}

	
	/**
	 * @comment:计算各个特诊的值，以及各个特征值的数量
	 * @return void
	 */
	public void calculateFeatures() {
		features = new double[dim][];
		featuresNumb = new int[dim][];

		for (int i = 0; i < dim; ++i) {
			List<Double> temp = new ArrayList<Double>();
			for (int j = 0; j < instanceDs.size(); ++j) {
				if (!temp.contains(instanceDs.get(j).getVector()[i])) {
					temp.add(instanceDs.get(j).getVector()[i]);
				}
			}

			features[i] = new double[temp.size()];
			featuresNumb[i] = new int[temp.size()];

			Collections.sort(temp);

			for (int k = 0; k < temp.size(); ++k) {
				features[i][k] = temp.get(k);
			}
		}
	}

	public void vsm2Int() {
		logger.info("vsmints size:"+instanceIs.size());

		for (int j = 0; j < instanceDs.size(); ++j) {
			
			int[] temp = new int[dim];
			
			for (int i = 0; i < dim; ++i) {
                 for(int k=0;k<features[i].length;++k)
                 {
                	 if(instanceDs.get(j).getVector()[i] == features[i][k])
                	 {
                		 temp[i] = k;
                	 }
                 }
			}
			
			InstanceI vsmint = new InstanceI(instanceDs.get(j).getType(),dim, temp);
			instanceIs.add(j, vsmint);
		}
		
		logger.info("vsmints size:"+instanceIs.size());
		logger.info(instanceIs.toString());
	}

}
