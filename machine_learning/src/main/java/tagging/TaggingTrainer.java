package tagging;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;

import base.InstanceSetD;

public abstract class TaggingTrainer
{
	public abstract void init(TaggingInstance instances);
	
	public  abstract void train();

	public void saveModel(String path)throws Exception{}
	
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