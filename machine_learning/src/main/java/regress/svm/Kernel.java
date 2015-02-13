package regress.svm;

import util.VectorOperation;

public class Kernel {
	public static double linear(double[] xi,double[] xj)
	{
		double result = VectorOperation.innerProduct(xi, xj);
		return result;
	}

}
