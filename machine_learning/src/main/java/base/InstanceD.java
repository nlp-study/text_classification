package base;


import java.util.Arrays;

import org.apache.log4j.Logger;

public class InstanceD extends Instance {
	Logger logger = Logger.getLogger(InstanceD.class);
	
	public double[] vector;  //需要改成基本类型double

	public InstanceD(int typeid,int length, double[] vector) {
		this.type = typeid;
		this.length = length;
		this.vector = vector;
	}
	
	public int getLength()
	{
		return vector.length;
	}

	public double[] getVector() {
		return vector;
	}

	public void setVector(double[] vector) {
		this.vector = vector;
	}
	
	public double getFeature(int index)
	{
		if(index < this.length)
		{
			return vector[index];
		}
		else
		{
			logger.error("outoff the vector length!");
			return -1.0;
		}
	}
	
	public String toString()
	{
		String str = type+":"+Arrays.toString(vector);
		return str;
	}
}
