package classifier.logistic;

import java.util.Arrays;
import java.util.List;

import manager.TrainerManager;

import org.apache.log4j.Logger;

import util.VectorOperation;
import vsm.VSM;
import vsm.VSMBuilder;
import classifier.AbstractTrainer;

/**
 * @author xiaohe
 * 创建于：2015年1月13日
 * 该logistic 没有用求导的方式学习，使用的SDG（随机梯度下降法）来求解的
 */
public class LogisticTrainer implements AbstractTrainer {
	Logger logger = Logger.getLogger(LogisticTrainer.class);

	//权重
	double[] weight;
	
	//输入的特征和类别标签
	List<VSM> vsms;
	
	//特征的维度
	int dim;
	
	//最大迭代次数
	static final int ITERATIONS_NUMB = 1000;
	
	//学习率
	private double rate;
	
	@Override
	public void init(VSMBuilder vsmBuilder) {
		// TODO Auto-generated method stub
		this.vsms = vsmBuilder.getVsms();
		if(vsms.size() == 0 )
		{
			logger.error("输入的特征向量为空！");
		}
		
		dim = vsms.get(0).getSize();
		
		Arrays.fill(weight, 0);
		
		rate = 0.001;
	}

	@Override
	public void train() {
		 for(int i=0;i<ITERATIONS_NUMB;++i)
		 {
			 for(VSM vsm:vsms)
			 {
				 double prediction = sigmoid(vsm.getVector());
				 double diff  = prediction - vsm.getType();
				 double[] temp = VectorOperation.constantMultip(vsm.getVector(), diff);
				 weight = VectorOperation.addition(weight, temp);
			 }
		 }
	}
	
    private double sigmoid(double[] featureVector) {
		double linearTerm = VectorOperation.innerProduct(featureVector, weight);
		return 1.0 / (1.0 + Math.exp(-linearTerm));
	}

	@Override
	public void saveModel(String path) throws Exception {
		// TODO Auto-generated method stub

	}

}
