package util;

import java.io.Serializable;

public class Pair<S,T> implements Serializable
{
	public S key;
	public T value;
	
	public Pair()
	{
		
	}
	
	public Pair(S s,T t)
	{
		this.key = s;
		this.value = t;
	}
	
	public String toString()
	{
		String str = key.toString()+" "+value.toString();
		return str;
	}
	
	public boolean equals(Object obj){
		Pair tempPair = (Pair)obj;
		if(this.key.equals(tempPair.key) && this.value.equals(tempPair.value))
		{
			return true;
		}
		return false;
	}
	
	public static void main(String[] args)
	{
		String s = "afda";
		Double d = 1.0;
		Pair pair = new Pair(s,d);
		System.out.println(pair);
	}
}
