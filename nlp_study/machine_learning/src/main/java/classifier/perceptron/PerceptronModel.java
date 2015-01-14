package classifier.perceptron;

import classifier.AbstractModel;

public class PerceptronModel extends AbstractModel {
	double[] w;
	double b;
	
	public double[] getW() {
		return w;
	}
	public void setW(double[] w) {
		this.w = w;
	}
	public double getB() {
		return b;
	}
	public void setB(double b) {
		this.b = b;
	}
	
}
