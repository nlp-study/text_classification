package regress.svm;

import base.InstanceSetD;
import classifier.AbstractModel;

public class SVMModel extends AbstractModel {

	double[] w;

	double b;
	
	double[] alpha;
	
	InstanceSetD instanceSet;
	
	
	public SVMModel() {	
	}

	public SVMModel(double[] w, double b, double[] alpha,
			InstanceSetD instanceSet) {
		this.w = w;
		this.b = b;
		this.alpha = alpha;
		this.instanceSet = instanceSet;
	}

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

	public double[] getAlpha() {
		return alpha;
	}

	public void setAlpha(double[] alpha) {
		this.alpha = alpha;
	}

	public InstanceSetD getInstanceSet() {
		return instanceSet;
	}

	public void setInstanceSet(InstanceSetD instanceSet) {
		this.instanceSet = instanceSet;
	}

	
}
