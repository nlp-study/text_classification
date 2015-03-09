package tagging.hmm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

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
			double[] pi,int stateSize,int inputSize)
	{
		this.A = A;
		this.B = B;
		this.pi = pi;
		
		alpha = new double[inputSize][stateSize];
		beta = new double[inputSize][stateSize];
		
		this.inputSize = inputSize;
		this.stateSize = stateSize;
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
					alpha[t][i] *= B[i][instances.get(t).getWord()];
				}
			}
			
			logger.info(Arrays.toString(alpha[t]));
		}
		
	}
	
	public void backward()
	{
		
	}

	@Override
	public void clear() {

	}
	
	public static void main(String[] args)
	{
		HMMTrainer hmmTrainer = new HMMTrainer();
		
		
		
	}
	
	

}
