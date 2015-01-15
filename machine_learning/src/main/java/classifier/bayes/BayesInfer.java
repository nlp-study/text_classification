package classifier.bayes;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;



import base.ClassifyResult;
import classifier.AbstractInfer;


public class BayesInfer extends AbstractInfer {
	Logger logger = Logger.getLogger(BayesInfer.class);

	// 导入的训练模型文件
	BayesModel model = null;

	// 似然概率
	Double[][][] likelihood;

	// 先验概率
	Double[] prior;

	// 每个特诊取值的范围
	List<Double[]> ajs = new ArrayList<Double[]>();

	// 类别的数量
	int classNumb = 0;

	public void init(String path) throws ClassNotFoundException, IOException {
//		FileInputStream fi = new FileInputStream(path);
//
//		ObjectInputStream si = new ObjectInputStream(fi);
//
//		try {
//
//			model = (BayesModel) si.readObject();
//
//			si.close();
//
//		} catch (IOException e)
//
//		{
//			System.out.println(e);
//		}
		model = new BayesModel();
		super.init(path,model);
		likelihood = model.getLikelihood();
		prior = model.getPrior();
		ajs = model.getAjs();
		classNumb = model.getClassNumb();
	}

	public int infer(double[] vector) {
		double prevalue = 0.0;
		double currentValue = 0.0;
		int resultClass = 0;

		for (int i = 0; i < classNumb; ++i) {
			currentValue = calcualteEveryClass(vector, i);
			logger.info("currvalue:" + currentValue);
			if (currentValue > prevalue) {
				prevalue = currentValue;
				resultClass = i;
			}
		}
		
		return resultClass;
	}	

	
	
	private double calcualteEveryClass(double[] vector, int classid) {
		double result = prior[classid];
		logger.info("prior:"+result);
		for (int i = 0; i < vector.length; ++i) {
			int index = featureID(i, vector[i]);
			result *= likelihood[classid][i][index];
			logger.info("likehood:"+likelihood[classid][i][index]);
		}
		return result;
	}

	private int featureID(int index, Double value) {
		if (index < 0 || index > ajs.size()) {
			logger.error("ajs下标出界！");
			return -1;
		}

		for (int i = 0; i < ajs.get(index).length; ++i) {
			if (ajs.get(index)[i].equals(value)) {
				return i;
			}
		}

		logger.error("输入的特征值，不在该特征对应的词典中！value:" + value);
		return -1;
	}

	
}
