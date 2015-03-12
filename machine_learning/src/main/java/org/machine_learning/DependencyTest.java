package org.machine_learning;

import java.util.Arrays;

/**
 * Hello world!
 *
 */
public class  DependencyTest
{
	public String name = "tom1";
	
    public static void main( String[] args )
    {
    	double[][] A = {{1,2},{3,4}};
    	double[][] B = new double[2][2];
    	for(int i=0;i<2;++i)
    	{
    		System.arraycopy(A[i], 0, B[i], 0, A[i].length);
    	}
    	
    	
    	
    	
        System.out.println( Arrays.deepToString(A) );
        System.out.println( Arrays.deepToString(B) );
        
        A[0][0] = 20;
        
        System.out.println( Arrays.deepToString(A) );
        System.out.println( Arrays.deepToString(B) );
    }
}
