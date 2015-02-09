package classifier.maxent;

import java.util.ArrayList;
import java.util.List;

import util.Pair;
import classifier.AbstractModel;

public class MaxEntropyModel extends AbstractModel {
	 //每维的特征对应的特征值
		int[][] features;
		
		//每维的特征值在特征函数中id，
		//这个数组的第一维是xi在输入特征中的维数，
		//第二维是xi的取值的id，即在features中的第二位的下标
		int[][] featuresID;	
		
		double[] weight;
		
		int classNumb;
		
		int length;
		
		//特征函数  Pair<特征，类别id>
		List<Pair<Integer,Integer>> featureFunctionList = new ArrayList<Pair<Integer,Integer>>();

	
		
		
		public MaxEntropyModel(int[][] features, int[][] featuresID,
				double[] weight, int classNumb, int length,
				List<Pair<Integer, Integer>> featureFunctionList) {
			this.features = features;
			this.featuresID = featuresID;
			this.weight = weight;
			this.classNumb = classNumb;
			this.length = length;
			this.featureFunctionList = featureFunctionList;
		}

		public int[][] getFeatures() {
			return features;
		}

		public void setFeatures(int[][] features) {
			this.features = features;
		}

		public int[][] getFeaturesID() {
			return featuresID;
		}

		public void setFeaturesID(int[][] featuresID) {
			this.featuresID = featuresID;
		}

		public double[] getWeight() {
			return weight;
		}

		public void setWeight(double[] weight) {
			this.weight = weight;
		}

		public int getClassNumb() {
			return classNumb;
		}

		public void setClassNumb(int classNumb) {
			this.classNumb = classNumb;
		}

		public int getLength() {
			return length;
		}

		public void setLength(int length) {
			this.length = length;
		}

		public List<Pair<Integer, Integer>> getFeatureFunctionList() {
			return featureFunctionList;
		}

		public void setFeatureFunctionList(
				List<Pair<Integer, Integer>> featureFunctionList) {
			this.featureFunctionList = featureFunctionList;
		}
			
}
