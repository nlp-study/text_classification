package validation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import regress.AbstractRegressInfer;
import regress.AbstractRegressTrainer;
import regress.perceptron.PerceptronInfer;
import regress.perceptron.PerceptronTrainer;
import regress.softmax.SoftmaxInfer;
import regress.softmax.SoftmaxTrainer;
import regress.svm.CopyOfSVMTrainer;
import regress.svm.SVMInfer;
import regress.svm.SVMTrainer;
import validation.slice.DataSlice;
import validation.slice.KFolderDataSlice;
import validation.slice.ValidationID;
import base.ClassifyResult;
import base.InstanceD;
import base.InstanceSetD;
import evaluation.ClassifyEvaluation;

/**
 * @author xiaohe 创建于：2015年2月15日 二分类回归的交叉验证类
 */
public class BinaryRegreeCrossValidation {

	Logger logger = Logger.getLogger(BinaryRegreeCrossValidation.class);

	InstanceSetD inputFeature;
	List<ValidationID> verificationIDs;
	List<Double> recalls = new ArrayList<Double>();

	public BinaryRegreeCrossValidation(InstanceSetD inputFeature) {
		this.inputFeature = inputFeature;
		int classNumb = inputFeature.getClassNumb();
		if (classNumb > 2 || classNumb < 1) {
			logger.error("输入特征的类别数量错误！");
		}
	}

	/**
	 * @comment:将类别为0的类设为类别为1的类的负例，可以设置的值多为：0,1
	 * @param typeid
	 * @return:void
	 */
	public void setCounterExampleTypeID(int typeid) {
		for (int i = 0; i < inputFeature.getSize(); ++i) {
			int id = inputFeature.getClassID(i);
			if (id == 0) {
				inputFeature.setClassID(i, typeid);
			}
		}
	}

	public void sliceData(DataSlice dataSlice) {
		dataSlice.init(inputFeature);
		dataSlice.excute();
		verificationIDs = dataSlice.getVerificationIDs();
	}

	public List<ValidationID> getVerificationIDs() {
		return verificationIDs;
	}

	/**
	 * @comment:交叉验证
	 * @param trainer
	 * @param infer
	 * @param path
	 * @throws Exception
	 * @return:void
	 */
	public void crossCheck(AbstractRegressTrainer trainer,
			AbstractRegressInfer infer, String path) throws Exception {
		for (int i = 0; i < verificationIDs.size(); ++i) {
			Set<Integer> trains = verificationIDs.get(i).getTrainids();
			Set<Integer> infers = verificationIDs.get(i).getInferids();
			List<ClassifyResult> classifyResults = new ArrayList<ClassifyResult>();

			InstanceSetD trainFeature = new InstanceSetD();
			InstanceSetD inferFeature = new InstanceSetD();

			for (Integer j : trains) {
				InstanceD instance = inputFeature.getInstanceD(j);
				trainFeature.add(instance);
			}

			for (Integer j : infers) {
				InstanceD instance = inputFeature.getInstanceD(j);
				inferFeature.add(instance);
			}

			train(trainer, trainFeature, path);

			int[] result = infer(infer, inferFeature, path);

			for (int j = 0; j < result.length; ++j) {
				ClassifyResult classifyResult = new ClassifyResult(
						inferFeature.getClassID(j), result[j]);
				classifyResults.add(classifyResult);
			}

			calculateRecall(classifyResults);

		}

	}

	/**
	 * @comment:值跑一次验证
	 * @param trainer
	 * @param infer
	 * @param path
	 * @throws Exception
	 * @return:void
	 */
	public void singleCheck(AbstractRegressTrainer trainer,
			AbstractRegressInfer infer, String path) throws Exception {
		int i = 0;

		Set<Integer> trains = verificationIDs.get(i).getTrainids();
		Set<Integer> infers = verificationIDs.get(i).getInferids();
		List<ClassifyResult> classifyResults = new ArrayList<ClassifyResult>();

		InstanceSetD trainFeature = new InstanceSetD();
		InstanceSetD inferFeature = new InstanceSetD();

		for (Integer j : trains) {
			InstanceD instance = inputFeature.getInstanceD(j);
			trainFeature.add(instance);
		}

		for (Integer j : infers) {
			InstanceD instance = inputFeature.getInstanceD(j);
			inferFeature.add(instance);
		}

		train(trainer, trainFeature, path);

		int[] result = infer(infer, inferFeature, path);

		for (int j = 0; j < result.length; ++j) {
			ClassifyResult classifyResult = new ClassifyResult(
					inferFeature.getClassID(j), result[j]);
			classifyResults.add(classifyResult);
		}

		calculateRecall(classifyResults);

	}

	public void train(AbstractRegressTrainer trainer,
			InstanceSetD inputFeature, String path) throws Exception {
		trainer.clear();
		trainer.init(inputFeature);
		trainer.train();
		trainer.saveModel(path);
	}

	public int[] infer(AbstractRegressInfer infer, InstanceSetD inputFeature,
			String path) throws Exception {

		infer.init(path);
		int[] results = new int[inputFeature.getSize()];
		int i = 0;
		for (InstanceD instance : inputFeature.getInstances()) {
			results[i] = infer.infer(instance.getVector());
			++i;
		}

		return results;
	}

	public void calculateRecall(List<ClassifyResult> classifyResults) {
		int numb = 0;
		int i = 0;
		for (ClassifyResult classifyResult : classifyResults) {
			if (classifyResult.isSame()) {
				++numb;
				// logger.info(i+":ok"+classifyResult);
			} else {
				// logger.info(i+":false"+classifyResult);
			}
			++i;
		}

		double recall = (double) numb / (double) classifyResults.size();
		recalls.add(recall);
	}

	public void showResult() {
		for (Double d : recalls) {
			System.out.println("recall:" + d);
		}
	}

	/**
	 * @comment:
	 * @param classifyResults
	 * @param classNumb
	 * @throws IOException
	 * @return void 暂时没有用
	 */
	public void calcualteIndex(List<ClassifyResult> classifyResults,
			int classNumb) throws IOException {
		int nt = 0; // 训练集中某个类别的数量
		int ntf = 0; // 测试集和训练集中分类结果相同的
		int nf = 0; // 测试集中某个类别的数量
		for (int i = 0; i < classNumb; ++i) {
			for (ClassifyResult classifyResult : classifyResults) {
				if (classifyResult.gettClass() == i
						&& classifyResult.getfClass() == i) {
					++nt;
					++nf;
					++ntf;
				} else {
					if (classifyResult.gettClass() == i) {
						++nt;
					}
					if (classifyResult.getfClass() == i) {
						++nf;
					}
				}
			}

			ClassifyEvaluation classifyEvaluation = new ClassifyEvaluation(i,
					nt, nf, ntf);
			classifyEvaluation.calculate();
		}

	}

	public static void main(String[] args) throws Exception {
		PropertyConfigurator.configure("log4j.properties");

		// 产生数据
		// String path = "data/corpus/iris_bin.data";
		// Iris iris = new Iris();
		// iris.readData(path);
		// InstanceSetD inputFeature = iris.getInputFeature();

		String path = "data/corpus/heart_scale.txt";
		HeartScale heartScale = new HeartScale();
		heartScale.readData(path);
		InstanceSetD inputFeature = heartScale.getInputFeature();

		/********************** 使用的回归模型 ********************/
		// perceptron
		AbstractRegressTrainer trainer = new PerceptronTrainer();
		AbstractRegressInfer infer = new PerceptronInfer();

		// svm
		// AbstractRegressTrainer trainer = new SVMTrainer();
		// AbstractRegressTrainer trainer = new CopyOfSVMTrainer();
		// AbstractRegressInfer infer = new SVMInfer();

		/********************** 使用的回归模型 ********************/

		// 交叉运行
		BinaryRegreeCrossValidation crossValidation = new BinaryRegreeCrossValidation(
				inputFeature);
		// ！！！！！设置负例的类别值，可以设置的值多为-1,1
		crossValidation.setCounterExampleTypeID(-1);

		DataSlice dataSlice = new KFolderDataSlice();
		crossValidation.sliceData(dataSlice);
		String modelPath = "data/result/model.m";
		crossValidation.crossCheck(trainer, infer, modelPath);
		crossValidation.showResult();
	}

}
