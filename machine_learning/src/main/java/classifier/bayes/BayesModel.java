package classifier.bayes;

import java.util.ArrayList;
import java.util.List;

import classifier.AbstractModel;

public class BayesModel extends AbstractModel {
		
	//似然概率
	Double[][][] likelihood;
		
    //先验概率
    Double[] prior;
    
    //每个特诊取值的范围
    List<Integer[]> ajs = new ArrayList<Integer[]>();
    
    //类别的数量
    int classNumb = 0;
    
    public BayesModel(){}

	public BayesModel(Double[][][] likelihood, Double[] prior,
			List<Integer[]> ajs, int classNumb) {
		super();
		this.likelihood = likelihood;
		this.prior = prior;
		this.ajs = ajs;
		this.classNumb = classNumb;
	}

	public Double[][][] getLikelihood() {
		return likelihood;
	}

	public void setLikelihood(Double[][][] likelihood) {
		this.likelihood = likelihood;
	}

	public Double[] getPrior() {
		return prior;
	}

	public void setPrior(Double[] prior) {
		this.prior = prior;
	}

	public List<Integer[]> getAjs() {
		return ajs;
	}

	public void setAjs(List<Integer[]> ajs) {
		this.ajs = ajs;
	}

	public int getClassNumb() {
		return classNumb;
	}

	public void setClassNumb(int classNumb) {
		this.classNumb = classNumb;
	}
    
}
