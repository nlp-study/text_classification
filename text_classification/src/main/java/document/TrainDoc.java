package document;

import java.util.List;

import base.ClassName;
import document.Document;

public class TrainDoc extends Document{
	ClassName className;
	List<String> words;
	List<String> featureWords;
	Double[] vsm;
	
	public ClassName getClassName() {
		return className;
	}
	public void setClassName(ClassName className) {
		this.className = className;
	}
	public List<String> getWords() {
		return words;
	}
	public void setWords(List<String> words) {
		this.words = words;
	}
	public List<String> getFeatureWords() {
		return featureWords;
	}
	public void setFeatureWords(List<String> featureWords) {
		this.featureWords = featureWords;
	}
	public Double[] getVsm() {
		return vsm;
	}
	public void setVsm(Double[] vsm) {
		this.vsm = vsm;
	}
}
