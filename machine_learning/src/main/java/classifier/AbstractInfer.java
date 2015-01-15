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
public abstract class AbstractInfer {
	
	/**
	 * @param path
	 * @return:void
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 * @comment:导入模型文件
	 */
	public <T>  void init(String path,T model) throws IOException, ClassNotFoundException
	{
		FileInputStream fi = new FileInputStream(path);

		ObjectInputStream si = new ObjectInputStream(fi);

		try {

			model = (T) si.readObject();

			si.close();
			

		} catch (IOException e)
		{
			System.out.println(e);
		}	
	}
	
	public abstract void init(String path)throws Exception;
	
	/**
	 * @param input
	 * @return
	 * @return:Object
	 * @comment:推导
	 */
	public abstract int infer(double[] input);
	
	public <T> void test()
	{
		
	}
	

}
