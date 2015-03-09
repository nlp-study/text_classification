package tagging;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;

public abstract class TaggingInfer {
	
	/**
	 * @param path
	 * @return:void
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 * @comment:导入模型文件
	 */
	public Object initModel(String path) throws IOException, ClassNotFoundException
	{
		FileInputStream fi = new FileInputStream(path);

		ObjectInputStream si = new ObjectInputStream(fi);

		try {

			Object model = si.readObject();

			si.close();
			return model;

		} catch (IOException e)
		{
			System.out.println(e);
		}
		return null;
	}
	
	public abstract void init(String path)throws Exception;
	
	/**
	 * @param input
	 * @return
	 * @return:Object
	 * @comment:推导
	 */
	public abstract List<TaggingInput> infer(int[] input);

}
