package classifier;



import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * @author xiaohe
 * 创建于：2014年12月29日
 * 抽象的推理类
 */
public interface AbstractInfer {
	
	/**
	 * @param path
	 * @return:void
	 * @comment:导入模型文件
	 */
	public abstract void init(String path)throws Exception; 
	
	/**
	 * @param input
	 * @return
	 * @return:Object
	 * @comment:推导
	 */
	public int infer(double[] input);
	

}
