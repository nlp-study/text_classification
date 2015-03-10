package tagging.hmm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import evaluation.ClassifyEvaluation;
import tagging.TaggingInput;
import tagging.TaggingInstance;
import tagging.TaggingTrainer;

/**
 * @author xiaohe
 * 创建于：2015年3月9日
 * 隐马尔科夫模型
 */
public class HMMTrainer extends TaggingTrainer {
	Logger logger = Logger.getLogger(HMMTrainer.class);
	
	/**
     * 状态转移矩阵
     */
    double[][] A;
    /**
     * 发射概率
     */
    double[][] B;
    /**
     * 初始概率
     */
    double[] pi;
    /**
     * 前向概率
     */
    double[][] alpha;
    /**
     * 后向概率
     */
    double[][] beta;
    
    //内部参数    
    /**
     * 状态的数量，状体的最大id是：stateSize-1
     */
    int stateSize;
    /**
     * 输入的数量，状体的最大id是：inputSize-1
     */
    int inputSize;
    /**
     * 词的数量，状体的最大id是：wordSize-1
     */
    int wordSize;
    
    /**
     * 输入的语料对
     */
    List<TaggingInput> instances = new ArrayList<TaggingInput>();
	
	@Override
	public void init(TaggingInstance instances) {
		this.stateSize = instances.getMaxTaggingID() + 1;
		this.wordSize = instances.getMaxWordID() + 1;
		this.inputSize = instances.getInstanceSize();
		this.instances = instances.getInstances();
		
		A = new double[stateSize][stateSize];
		B = new double[stateSize][wordSize];
		pi = new double[stateSize];
		alpha = new double[stateSize][inputSize];
		beta = new double[stateSize][inputSize];
	}
	
	/**
	 * @comment:
	 * @param A
	 * @param B
	 * @param pi
	 * @param stateSize
	 * @param inputSize
	 * @return:void
	 * 
	 * 临时的初始化，用于测试前向后向算法
	 */
	public void temporaryInit(double[][] A,double[][] B,
			double[] pi,int stateSize,int inputSize,
			List<TaggingInput> instances)
	{
		this.A = A;
		this.B = B;
		this.pi = pi;
		
		alpha = new double[inputSize][stateSize];
		beta = new double[inputSize][stateSize];
		
		this.inputSize = inputSize;
		this.stateSize = stateSize;
		this.instances = instances;
	}


	@Override
	public void train() {

	}
	
	public void forward()
	{
		for(int t=0;t<inputSize;++t)
		{
			if(t == 0)
			{
				for(int j=0;j<stateSize;++j)
				{
					alpha[0][j] = pi[j]*B[j][instances.get(t).getWord()];
				}
			}
			else
			{
				for(int i=0;i<stateSize;++i)
				{
					alpha[t][i] = 0;
					
					for(int j=0;j<stateSize;++j)
					{
						alpha[t][i] += alpha[t-1][j]*A[j][i];
					}
					logger.info("t:"+t+" i:"+i+" alpha[t][i]:"+alpha[t][i]);
					alpha[t][i] *= B[i][instances.get(t).getWord()];
					logger.info("t:"+t+" i:"+i+" B[t][j]:"+B[i][instances.get(t).getWord()]);
				}
			}
			
			logger.info(Arrays.toString(alpha[t]));
		}
		
		double sum = 0;
		
		for(int i=0;i<stateSize;++i)
		{
			sum+= alpha[inputSize-1][i];
		}
		
		logger.info("prob is:"+sum);
		
	}
	
	public void backward()
	{
		
	}

	@Override
	public void clear() {

	}
	
	public static void main(String[] args)
	{
		PropertyConfigurator.configure("log4j.properties");
		
		HMMTrainer hmmTrainer = new HMMTrainer();
		double[][] A = {{0.5,0.2,0.3},{0.3,0.5,0.2},{0.2,0.3,0.5}};
		double[][] B = {{0.5,0.5},{0.4,0.6},{0.7,0.3}};
		double[] pi= {0.2,0.4,0.4};
		int stateSize = 3;
		int inputSize = 3;
		
		TaggingInput taggingInput1 = new TaggingInput(0,-1);
		TaggingInput taggingInput2 = new TaggingInput(1,-1);
		TaggingInput taggingInput3 = new TaggingInput(0,-1);
		
		List<TaggingInput> instances = new ArrayList<TaggingInput>();
		instances.add(taggingInput1);
		instances.add(taggingInput2);
		instances.add(taggingInput3);
		
		hmmTrainer.temporaryInit(A, B, pi, stateSize, inputSize,instances);
		hmmTrainer.forward();
	}
}
