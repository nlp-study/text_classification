package classifier.bayes;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import base.InputFeature;
import base.InstanceD;
import base.InstanceI;
import util.FileRead;
import classifier.AbstractTrainer;

public class BayesTrainer extends AbstractTrainer {
	Logger logger = Logger.getLogger(BayesTrainer.class);

	BayesModel model = null;
	
	int featureNumb = 0;
	int classNumb = 0;
	int docNumb = 0;
	
	//统计每个类别样本的数量
	int[] priorNumb ;
	
	//用于平滑的值
	int lambda;
	
	//每个特诊取值的范围
	List<Double[]> ajs = new ArrayList<Double[]>();
	
	//输入的每个特征
	List<InstanceD> instances = new ArrayList<InstanceD>();
	
	//似然概率
	Double[][][] likelihood;
	
	//先验概率
	Double[] prior;

	public BayesTrainer()
	{
	}
	
	public BayesTrainer(int featureNumb, int classNumb, List<Double[]> ajs,
			List<InstanceD> vsms,int lambda) {
		this.lambda = lambda;
		this.featureNumb = featureNumb;
		this.classNumb = classNumb;
		this.ajs = ajs;
		this.instances = vsms;
		this.prior = new Double[classNumb];
		this.likelihood = new Double[classNumb][featureNumb][];
		
		for(int i=0;i<classNumb;++i)
		{
			for(int j=0;j<ajs.size();++j)
			{
				likelihood[i][j] = new Double[ajs.get(j).length];
			}
		}
		
		docNumb = vsms.size();
		priorNumb = new int[classNumb];
	}
	
	public void init(InputFeature inputFeature)
	{
		this.lambda = 1;
		this.featureNumb = inputFeature.getSize();
		this.classNumb = inputFeature.getClassNumb();
		this.instances = inputFeature.getInstances();
		calculateAJS();
		
		this.prior = new Double[classNumb];
		this.likelihood = new Double[classNumb][featureNumb][];
		
		for(int i=0;i<classNumb;++i)
		{
			for(int j=0;j<ajs.size();++j)
			{
				likelihood[i][j] = new Double[ajs.get(j).length];
			}
		}
		
		docNumb = instances.size();
		priorNumb = new int[classNumb];
	}
	
	//计算每个特征总共有几个值，用值得排序来确定
	public void calculateAJS()
	{
		ajs = new ArrayList<Double[]>();
		
		for(int i=0;i<featureNumb;++i)
		{
			List<Double> featureValues = new ArrayList<Double>();
			for(InstanceD vsm:instances)
			{
				if(!featureValues.contains(vsm.getFeature(i)))
				{
					featureValues.add(vsm.getFeature(i));
				}
			}
			
			logger.info(featureValues);
			Double[] features = (Double[])featureValues.toArray(new Double[featureValues.size()]);
			ajs.add(features);
		}

	}

	public int getClassNumb() {
		return classNumb;
	}

	public void setClassNumb(int classNumb) {
		this.classNumb = classNumb;
	}

	public List<Double[]> getAjs() {
		return ajs;
	}

	public void setAjs(List<Double[]> ajs) {
		this.ajs = ajs;
	}

	public Double[][][] getLikelihood() {
		return likelihood;
	}

	public void setLikelihood(Double[][][] likelihood) {
		this.likelihood = likelihood;
	}

	public Double[] getPrior() {
		return prior;
	}

	public void setPrior(Double[] prior) {
		this.prior = prior;
	}

	public void train()
	{
		calculatePrior();
		calculateLikelihood();
	}
	
	public void calculatePrior()
	{
		for(int i=0;i<classNumb;++i)
		{
			priorNumb[i] = 0;
		}
		
		for(InstanceD vsm:instances)
		{
			priorNumb[vsm.getType()] = priorNumb[vsm.getType()]+1;
		}
		
		for(int i=0;i<classNumb;++i)
		{
			prior[i] = ((double)(priorNumb[i]+lambda))/(double)(docNumb+classNumb*lambda);
		}
	}
	
	public void calculateLikelihood()
	{
        Integer[][][] likelihoodNumb = new Integer[classNumb][featureNumb][];
		
        //初始化统计数量的矩阵
		for(int i=0;i<classNumb;++i)
		{
			for(int j=0;j<ajs.size();++j)
			{
				likelihoodNumb[i][j] = new Integer[ajs.get(j).length];
				
				for(int k=0;k<ajs.get(j).length;++k)
				{
					likelihoodNumb[i][j][k] = 0;
				}
			}
		}
		
		//统计各个特征的数量
		for(InstanceD vsm:instances)
		{
			int typeID = vsm.getType();
			for(int j=0;j<featureNumb;++j)
			{
				double value = vsm.getFeature(j);
				int index = featureID(j, value);
				likelihoodNumb[typeID][j][index] +=1;  
			}
		}
		
		//统计
		for(int i=0;i<classNumb;++i)
		{
			for(int j=0;j<ajs.size();++j)
			{
				for(int k=0;k<ajs.get(j).length;++k)
				{
					double member = (double)(likelihoodNumb[i][j][k] + lambda);
					double denominator = (double)(priorNumb[i]+ajs.get(j).length*lambda);
					logger.info("member:"+member+" denominator"+denominator);
					
					likelihood[i][j][k] =  member/denominator;
				}
			}
		}
	}
	
	public int featureID(int index,Double value)
	{
		if(index<0||index>ajs.size())
		{
			logger.error("ajs下标出界！");
			return -1 ;
		}
		
		for(int i=0;i<ajs.get(index).length;++i)
		{
			if(ajs.get(index)[i].equals(value))
			{
				return i;
			}
		}
		
		logger.error("输入的特征值，不在该特征对应的词典中！value:"+value);
		return -1;
	}
	
	public void saveModel(String path) throws IOException
	{
		model = new BayesModel(likelihood, prior,ajs, classNumb);
		
		super.saveModel(path, model);
//		 FileOutputStream fo = new FileOutputStream(path);   
//	     ObjectOutputStream so = new ObjectOutputStream(fo);   
//	  
//	     try {   
//	            so.writeObject(model);   
//	            so.close();   
//	  
//	     } catch (IOException e) {   
//	            System.out.println(e);   
//	     }   
	}
	
	public void writeFile(String path) throws IOException
	{
		BufferedWriter buffWriter = new BufferedWriter(new FileWriter(path));
		
		for(int i=0;i<prior.length;++i)
		{
			buffWriter.write(prior[i]+" ");
		}
		buffWriter.write("\r\n");
		
		for(int i=0;i<classNumb;++i)
		{
			for(int j=0;j<ajs.size();++j)
			{
				for(int k=0;k<ajs.get(j).length;++k)
				{
					buffWriter.write("class:"+i+" feature id:"+j+" feature value:"+k+"  "+likelihood[i][j][k]+"\r\n");
				}
			}
		}
		
		buffWriter.close();
	}
	
	public static void main(String[] args) throws Exception
	{
		
	}
}
