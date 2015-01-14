package feature_selection.chi_square;

public class ChiSquare {
	public static double excute(ChiUnit chiUnit)
	{
		int A = chiUnit.getA();
		int B = chiUnit.getB();
		int C = chiUnit.getC();
		int D = chiUnit.getD();
	   return excute(A,B,C,D);
	}
	
	public static double excute(int A,int B,int C,int D)
	{
		int N = A+B+C+D;
		double value = Math.pow((A*D-B*C),2)/((A+B)*(C+D));
		return value;
	}
	
	public static void main(String[] args)
	{
		Double d = ChiSquare.excute(1, 1, 1, 4);
		System.out.println(d);
	}
	
}
