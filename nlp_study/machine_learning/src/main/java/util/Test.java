package util;

import java.util.Arrays;

public class Test {
	
	public static void main(String[] args)
	{
		double[] names = {1.0,2,4,11,13,9};
		
		Arrays.sort(names);
		
		for(double d:names)
		{
			System.out.println(d);
		}
		
	}

}
