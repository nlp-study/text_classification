package classifier;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import base.InputFeatureD;
import base.InputFeatureI;


/**
 * @author xiaohe
 * 创建于：2014年12月29日
 * 抽象的训练类
 */
public abstract class AbstractTrainer {
	
	public abstract void init(InputFeatureI inputFeature);
	
	public  abstract void train();

	public void saveModel(String path)throws Exception{}
	
	public abstract int getClassNumb();
	
	public abstract void clear();
	
	public <T> void saveModel(String path,T model) throws IOException
	{
		FileOutputStream fo = new FileOutputStream(path);   
	     ObjectOutputStream so = new ObjectOutputStream(fo);   
	  
	     try {   
	            so.writeObject(model);   
	            so.close();   
	     } catch (IOException e) {   
	            System.out.println(e);   
	     }   
	}
	
}
