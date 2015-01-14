package classifier;

import base.VSMBuilder;




/**
 * @author xiaohe
 * 创建于：2014年12月29日
 * 抽象的训练类
 */
public interface AbstractTrainer {
	
	public void init(VSMBuilder vsmBuilder);
	
	public  void train();

	
	public void saveModel(String path)throws Exception;
	
}
