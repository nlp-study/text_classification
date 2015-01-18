package base;

import java.util.Arrays;

import org.apache.log4j.Logger;

public class InstanceI extends Instance {
	Logger logger = Logger.getLogger(InstanceI.class);

	public int[] vector;  //需要改成基本类型double
	
	public InstanceI(int type,int length, int[] vector)
	{
		this.type = type;
		this.length = length;
		this.vector = vector;
	}

	public int[] getVector() {
		return vector;
	}

	public void setVector(int[] vector) {
		this.vector = vector;
	}
	
	public int getFeature(int index)
	{
		if(index < this.length)
		{
			return vector[index];
		}
		else
		{
			logger.error("outoff the vector length!");
			return -1;
		}
	}
	
	public String toString()
	{
		String str = type+" "+length+" "+Arrays.toString(vector);
		return str;
	}
	
}
