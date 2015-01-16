package classifier.decision_tree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import base.ClassName;
import vsm.VSM;
import vsm.VSMtransformer;
import vsm.VSMInt;

/**
 * @author xiaohe
 * 创建于：2015年1月8日
 * 信息增益率
 */
public class InformationGainRatio {
	Logger logger = Logger.getLogger(InformationGainRatio.class);
	
	//类别的数量
	private int K = 0;
	
	//特征总的维度
	private int dim;
	
    //总的样本数量
	private int allNumb = 0;
	
	//属于某个类别的样本的数量
	private Map<Integer,Integer> everyClassNumb;
	
	//每个类别的id，输入的类别已经是编过号了，但是因为输入的编号可能不是连续的，所以需要重新编号
	private List<Integer> everyClassID;
	
	//提取出每个特诊有几个取值
	private double[][] features;
	
	//记录某个特征的取值和每个取值的样本的数量,第一维是特诊的id，第二维是取值的id，第三维是取所在值的数量
	private  int[][] featuresNumb;
	
	//同时具有某个类别A的某个取值i和类别k的样本的数量
	private int[][][] featureCalssNumb;
	
	//输入的特征向量
	private List<VSM> vsms = new ArrayList<VSM>();
	
	//转成整数的vsm，特征的值都转化成整数
	private List<VSMInt> intVsms = new ArrayList<VSMInt>();
	
	//整个文本的熵
	private double HD = 0;
		
	//条件熵 H（D|A）
	private Map<Integer,Double> HDAS;
	
	//排好序的信息增益比
	private Map<Integer,Double> GRDA;
	
	//排好序的信息增益
	private Map<Integer,Double> GDA;
	
	//返回的结果
	private int resultDim;
	
	InformationGainRatio(){}

	/**
	 * @comment:输入的vsm中的类别id，必须是从0~n的连续整数
	 * @param vsms
	 * @param className
	 */
	public InformationGainRatio(List<VSM> vsms) {		
		this.vsms = vsms;
//		K = classNumb;
		calculateClassNumb();
	
		allNumb = vsms.size();
		HDAS = new TreeMap<Integer,Double>();
		GDA = new LinkedHashMap<Integer,Double>();
		GRDA = new LinkedHashMap<Integer,Double>();
		if(vsms.size() > 0)
		{
			dim = vsms.get(0).getSize();
		}
	}
	
	public int getK() {
		return K;
	}

	public void setK(int k) {
		K = k;
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

	public int[][][] getFeatureCalssNumb() {
		return featureCalssNumb;
	}

	public void setFeatureCalssNumb(int[][][] featureCalssNumb) {
		this.featureCalssNumb = featureCalssNumb;
	}

	public List<VSM> getVsms() {
		return vsms;
	}

	public void setVsms(List<VSM> vsms) {
		this.vsms = vsms;
	}

	public int getResultDim() {
		return resultDim;
	}

	public void setResultDim(int resultDim) {
		this.resultDim = resultDim;
	}

	public void init()
	{
		calculateEveryClassNumb();
		transVSM2Int();
		calculateFeatureCalssNumb();	
	}
	
	public void calculateClassNumb()
	{
		List<Integer> temp = new ArrayList<Integer>();
		for(int i=0;i<vsms.size();++i)
		{
			if(!temp.contains(vsms.get(i).getType()))
			{
				temp.add(vsms.get(i).getType());
			}
		}
		
		K = temp.size();
	}
	
	public void calculateEveryClassNumb()
	{
		everyClassNumb = new HashMap<Integer,Integer>();
		everyClassID = new ArrayList<Integer>();
		
		for(int i=0;i<vsms.size();++i)
		{
			if(!everyClassNumb.keySet().contains(vsms.get(i).getType()))
			{
				everyClassNumb.put(vsms.get(i).getType(), 1);
			}
			else
			{
				int size = everyClassNumb.get(vsms.get(i).getType());
				everyClassNumb.put(vsms.get(i).getType(),size+1);
			}
		}
		
		for(Integer i:everyClassNumb.keySet())
		{
			everyClassID.add(i);
		}
		
	}
	
	public void transVSM2Int()
	{
		InstanceDoubleToInt vsmFromDoubleToInt = new InstanceDoubleToInt(vsms);
		vsmFromDoubleToInt.excute();
		features = vsmFromDoubleToInt.getFeatures();
		intVsms = vsmFromDoubleToInt.getVsmints();
	}	
	
	/**
	 * @comment:计算每个特征的每个特征值下有多少样本
	 * @return:void
	 */
	public void calculateFeatureCalssNumb()
	{
		featuresNumb = new int[features.length][];
		for(int i=0;i<features.length;++i)
		{
			featuresNumb[i] = new int[features[i].length];
			Arrays.fill(featuresNumb[i], 0);
		}
		featureCalssNumb = new int[features.length][][];
		for(int i=0;i<featureCalssNumb.length;++i)
		{
			featureCalssNumb[i] = new int[features[i].length][K];
			for(int j=0;j<features[i].length;++j){
				Arrays.fill(featureCalssNumb[i][j],0);
			}
		}
		
				
		for(int i=0;i<dim;++i)
		{
			for(int j=0;j<intVsms.size();++j)
			{
				featuresNumb[i][intVsms.get(j).getVector()[i]]++;
				if(everyClassID.contains(intVsms.get(j).getType()))
				{
					int tempIndex = everyClassID.indexOf(intVsms.get(j).getType());
					featureCalssNumb[i][intVsms.get(j).getVector()[i]][tempIndex]++;
				}
				else
				{
					logger.error("the class id:"+intVsms.get(j).getType()+" is wrong!");
				}
			}
		}
		
		logger.info(Arrays.deepToString(featuresNumb));
		logger.info(Arrays.deepToString(featureCalssNumb));
	}
	
	public void calculateHD()
	{
		for(Integer i:everyClassNumb.keySet())
		{
			double prob = (double)everyClassNumb.get(i)/(double)allNumb;
			if(prob == 0)
			{
				continue;
			}
			else
			{
				HD += -prob*(Math.log(prob)/Math.log((double)2));
			}
		}
		logger.info("HD:"+HD);
	}
	
	public void calculateHDA()
	{
		for(int i=0;i<VSM.size;++i)
		{
			double hda = 0;
			for(int j=0;j<featuresNumb[i].length;++j)
			{
				double probA = (double)featuresNumb[i][j]/(double)allNumb;
				double hdik = 0;
				for(int k=0;k<K;++k)
				{
					double probIK = (double)featureCalssNumb[i][j][k]/(double)featuresNumb[i][j];
					if(probIK == 0)
					{
						continue;
					}
					else
					{
						hdik += probIK*(Math.log(probIK)/Math.log((double)2));
					}
					
				}
				hda +=  -probA*hdik;
			}
			HDAS.put(i,hda);			
		}
		
		
		logger.info(HDAS);
	}
	
	/**
	 * @comment:计算信息增益
	 * @return:void
	 */
	public void calculateGDA()
	{
		for(Integer index:HDAS.keySet())
		{
			double temp = HD - HDAS.get(index);
			GDA.put(index, temp);
		}
		
		GDA = sortMap(GDA);
		  
		logger.info("GDA:"+GDA.toString());
	}
	
	/**
	 * @comment:计算信息增益比
	 * @return:void
	 */
	public void calculateGRDA()
	{
		double max = 0;
		for(int i=0;i<dim;++i)
		{
			double grda = 0;
			double hda = 0;
			for(int j=0;j<featuresNumb[i].length;++j)
			{
				double probA = (double)featuresNumb[i][j]/(double)allNumb;
				if(probA == 0)
				{
					continue;
				}
				else
				{
					hda += -probA*(Math.log(probA)/Math.log((double)2));
				}
			}
			grda = GDA.get(i)/hda;
			GRDA.put(i, grda);
			
			if(max<grda)
			{
				max = grda;
				resultDim = i;
			}
		}
		
		GRDA = sortMap(GRDA);   //排序
		logger.info("GRDA:"+GRDA);
	}
	
	public LinkedHashMap<Integer,Double> sortMap(Map<Integer,Double> input)
	{
		LinkedHashMap<Integer,Double> result = new LinkedHashMap<Integer,Double>();
		List<Map.Entry<Integer,Double>> mappingList = new ArrayList<Map.Entry<Integer,Double>>(input.entrySet()); 
		  Collections.sort(mappingList, new Comparator<Map.Entry<Integer,Double>>(){ 
			   public int compare(Map.Entry<Integer,Double> mapping1,Map.Entry<Integer,Double> mapping2){ 
			    return mapping2.getValue().compareTo(mapping1.getValue()); 
			   } 
		 }); 
		  
		for(Map.Entry<Integer,Double> mapping:mappingList){
			result.put(mapping.getKey(), mapping.getValue());
		} 
		return result;
	}
	
	public void excute()
	{
		logger.info("start!");
		calculateHD();
		calculateHDA();
		calculateGDA();
		
	    calculateGRDA();
	}
	
	public int slectMaxGain()
	{
		init();
		excute();
		return resultDim;
	}
	
	/**
	 * @comment:李航书中的例子
	 * @param args
	 * @return:void
	 */
	public static void main(String[] args)
	{
        PropertyConfigurator.configure("log4j.properties");
		
		double[] x1 = {1,0,0,0};
		double[] x2 = {1,0,0,1};
		double[] x3 = {1,1,0,1};
		double[] x4 = {1,1,1,0};
		double[] x5 = {1,0,0,0};
		double[] x6 = {2,0,0,0};
		double[] x7 = {2,0,0,1};
		double[] x8 = {2,1,1,1};
		double[] x9 = {2,0,1,2};
		double[] x10 = {2,0,1,2};
		double[] x11 = {3,0,1,2};
		double[] x12 = {3,0,1,1};
		double[] x13 = {3,1,0,1};
		double[] x14 = {3,1,0,2};
		double[] x15 = {3,0,0,0};
		
		
		VSM.setSize(4);
		VSM vsm1 = new VSM(0,x1);
		VSM vsm2 = new VSM(0,x2);
		VSM vsm3 = new VSM(1,x3);
		VSM vsm4 = new VSM(1,x4);
		VSM vsm5 = new VSM(0,x5);
		VSM vsm6 = new VSM(0,x6);
		VSM vsm7 = new VSM(0,x7);
		VSM vsm8 = new VSM(1,x8);
		VSM vsm9 = new VSM(1,x9);
		VSM vsm10 = new VSM(1,x10);
		VSM vsm11 = new VSM(1,x11);
		VSM vsm12 = new VSM(1,x12);
		VSM vsm13 = new VSM(1,x13);
		VSM vsm14 = new VSM(1,x14);
		VSM vsm15 = new VSM(0,x15);
		
		
		List<VSM> vsms = new ArrayList<VSM>();
		vsms.add(vsm1);
		vsms.add(vsm2);
		vsms.add(vsm3);
		vsms.add(vsm4);
		vsms.add(vsm5);
		vsms.add(vsm6);
		vsms.add(vsm7);
		vsms.add(vsm8);
		vsms.add(vsm9);
		vsms.add(vsm10);
		vsms.add(vsm11);
		vsms.add(vsm12);
		vsms.add(vsm13);
		vsms.add(vsm14);
		vsms.add(vsm15);
		
		InformationGainRatio informationGainRatio = new InformationGainRatio(vsms);

//		informationGainRatio.init();		
//		informationGainRatio.excute();
//		int dim = informationGainRatio.getResultDim();
		int dim = informationGainRatio.slectMaxGain();
		System.out.println(dim);
	}

}
