package vsm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


import org.apache.log4j.Logger;

/**
 * @author xiaohe 2015年1月7日 将vsm 由double类型转化成int类型
 */
public class VSMtransformer {
	Logger logger = Logger.getLogger(VSMtransformer.class);
	
	// 类别的数量
	private int K = 0;
	
	//特征总的维度
	private int dim;

	// 提取出每个特诊有几个取值
	private double[][] features;

	// 记录某个特征的取值和每个取值的样本的数量,第一维是特诊的id，第二维是取值的id
	private int[][] featuresNumb;

	// 输入的特征向量
	private List<VSM> vsms = new ArrayList<VSM>();

	// 转化成功的int型的vsm
	private List<VSMInt> vsmints;
	
	public VSMtransformer() {
	}

	public VSMtransformer(List<VSM> vsms) {
		this.vsms = vsms;
		VSMInt.size = VSM.size;
		vsmints = new ArrayList<VSMInt>();
		
		if(vsms.size()>0)
		{
			dim = vsms.get(0).getSize();
		}
	}

	
	public List<VSM> getVsms() {
		return vsms;
	}

	public void setVsms(List<VSM> vsms) {
		this.vsms = vsms;
	}

	public List<VSMInt> getVsmints() {
		return vsmints;
	}

	public void setVsmints(List<VSMInt> vsmints) {
		this.vsmints = vsmints;
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
		VSMInt.size = VSM.size;

		vsmints = new ArrayList<VSMInt>();
		
		if(vsms.size()>0)
		{
			dim = vsms.get(0).getSize();
		}
		
		logger.info("VSMINT size:"+VSMInt.size);
	}
	
	public void excute()
	{
		calculateFeatures();
		vsm2Int();
	}

	public void calculateFeatures() {
		features = new double[dim][];
		featuresNumb = new int[dim][];

		for (int i = 0; i < dim; ++i) {
			List<Double> temp = new ArrayList<Double>();
			for (int j = 0; j < vsms.size(); ++j) {
				if (!temp.contains(vsms.get(j).getVector()[i])) {
					temp.add(vsms.get(j).getVector()[i]);
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
		logger.info("vsmints size:"+vsmints.size());

		for (int j = 0; j < vsms.size(); ++j) {
			
			int[] temp = new int[dim];
			
			for (int i = 0; i < dim; ++i) {
                 for(int k=0;k<features[i].length;++k)
                 {
                	 if(vsms.get(j).getVector()[i] == features[i][k])
                	 {
                		 temp[i] = k;
                	 }
                 }
			}
			
			VSMInt vsmint = new VSMInt(vsms.get(j).getType(), temp);
			vsmints.add(j, vsmint);
		}
		
		logger.info("vsmints size:"+vsmints.size());
		logger.info(vsmints.toString());
	}

}
