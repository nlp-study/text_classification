package regress.softmax;

import regress.AbstractRegressModel;

public class SoftmaxModel extends AbstractRegressModel {
	    //权重
		double [][] weight;
		
		int dim;
		
		int classNumb;
		
		public SoftmaxModel()
		{
			
		}

		public SoftmaxModel(double[][] weight, int dim, int classNumb) {
			this.weight = weight;
			this.dim = dim;
			this.classNumb = classNumb;
		}

		public double[][] getWeight() {
			return weight;
		}

		public void setWeight(double[][] weight) {
			this.weight = weight;
		}

		public int getDim() {
			return dim;
		}

		public void setDim(int dim) {
			this.dim = dim;
		}

		public int getClassNumb() {
			return classNumb;
		}

		public void setClassNumb(int classNumb) {
			this.classNumb = classNumb;
		}
		
		
		
}
