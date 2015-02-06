package util;

public class Pair<S,T>
{
	public S s;
	public T t;
	
	public Pair(S s,T t)
	{
		this.s = s;
		this.t = t;
	}
	
	public String toString()
	{
		String str = s.toString()+" "+t.toString();
		return str;
	}
	
	public boolean equals(Object obj){
		Pair tempPair = (Pair)obj;
		if(this.s.equals(tempPair.s) && this.t.equals(tempPair.t))
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
