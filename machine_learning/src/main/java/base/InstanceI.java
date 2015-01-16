package base;

import java.util.Arrays;

public class InstanceI extends Instance {
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
	
	public String toString()
	{
		String str = type+" "+length+" "+Arrays.toString(vector);
		return str;
	}
	
}
