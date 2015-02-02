package validation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.PropertyConfigurator;

import validation.slice.DataSlice;
import validation.slice.KFolderDataSlice;
import validation.slice.ValidationID;
import classifier.AbstractInfer;
import classifier.AbstractTrainer;
import classifier.bayes.BayesInfer;
import classifier.bayes.BayesTrainer;
import classifier.util.FeatureDoubleToInt;
import evaluation.ClassifyEvaluation;
import base.ClassifyResult;
import base.InstanceSetD;
import base.InstanceSetI;
import base.InstanceD;
import base.InstanceI;

public class ClassifierCrossValidation {
	InstanceSetD inputFeature ;
	List<ValidationID> verificationIDs;
	List<Double> recalls = new ArrayList<Double>();
	
	public ClassifierCrossValidation(InstanceSetD inputFeature)
	{
		this.inputFeature = inputFeature;
	}
	
	public void sliceData(DataSlice dataSlice)
	{
		dataSlice.init(inputFeature);
		dataSlice.excute();
		verificationIDs = dataSlice.getVerificationIDs();
	}
	
	
	
   public List<ValidationID> getVerificationIDs() {
		return verificationIDs;
   }

	public void crossCheck(AbstractTrainer trainer,AbstractInfer infer,String path) throws Exception
	{	
		for(int i=0;i<verificationIDs.size();++i)
		{
			Set<Integer> trains = verificationIDs.get(i).getTrainids();
			Set<Integer> infers = verificationIDs.get(i).getInferids();
			List<ClassifyResult> classifyResults = new ArrayList<ClassifyResult>();
			
			InstanceSetD trainFeature = new InstanceSetD();
			InstanceSetD inferFeature = new InstanceSetD();
			
			
			for(Integer j:trains)
			{
				InstanceD instance = inputFeature.getInstanceD(j);
				trainFeature.add(instance);
			}
			
			for(Integer j:infers)
			{
				InstanceD instance = inputFeature.getInstanceD(j);
				inferFeature.add(instance);
			}
			
			//把double的特征整理成int型
			FeatureDoubleToInt featureDoubleToInt = new FeatureDoubleToInt(trainFeature);
			featureDoubleToInt.trans();
			InstanceSetI trainFeatureI = featureDoubleToInt.getOutputfeature();
			
			featureDoubleToInt.init(inferFeature);
			featureDoubleToInt.trans();
			InstanceSetI inferFeatureI = featureDoubleToInt.getOutputfeature();
			
			train( trainer, trainFeatureI, path);
			
			int[] result = infer( infer, inferFeatureI,path);
			
			for(int j=0;j<result.length;++j)
			{
				ClassifyResult classifyResult = new ClassifyResult(inferFeature.getClassID(j),result[j]);
				classifyResults.add(classifyResult);
			}
			
			calculateRecall(classifyResults);
						
		}
		
	}
	
	public void train(AbstractTrainer trainer,InstanceSetI inputFeature,String path) throws Exception
	{
		trainer.clear();
		trainer.init(inputFeature);
		trainer.train();
		trainer.saveModel(path);
	}
	
	public int[] infer(AbstractInfer infer,
			InstanceSetI inputFeature,String path) throws Exception	{
		
		infer.init(path);
		int[] results = new int[inputFeature.getSize()];
		int i=0;
		for(InstanceI instance:inputFeature.getInstances())
		{
			results[i] = infer.infer(instance.getVector());
			++i;
		}
		
		return results;		
	}
	
	public void calculateRecall(List<ClassifyResult> classifyResults)
	{
		int numb = 0;
		for(ClassifyResult classifyResult:classifyResults)
		{
			if(classifyResult.isSame())
			{
				++numb;
			}
		}
		
		double recall = (double)numb/(double)classifyResults.size();
		recalls.add(recall);
	}
	
	public void showResult()
	{
		for(Double d:recalls)
		{
			System.out.println("recall:"+d);
		}
	}
	
	
	/**
	 * @comment:
	 * @param classifyResults
	 * @param classNumb
	 * @throws IOException
	 * @return void
	 * 暂时没有用
	 */
	public void calcualteIndex(List<ClassifyResult> classifyResults,int classNumb) throws IOException {
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

			ClassifyEvaluation classifyEvaluation = new ClassifyEvaluation(i,nt,nf,ntf);
			classifyEvaluation.calculate();
		}
		
	}
	
	public static void main(String[] args) throws Exception
	{
		PropertyConfigurator.configure("log4j.properties");
		
		String path = "data/corpus/iris.data";
		Iris iris = new Iris();
		iris.readData(path);
		InstanceSetD inputFeature = iris.getInputFeature();
		
		ClassifierCrossValidation crossValidation = new ClassifierCrossValidation(inputFeature);
		DataSlice dataSlice = new KFolderDataSlice();
		crossValidation.sliceData(dataSlice);
		AbstractTrainer trainer = new BayesTrainer();
		AbstractInfer infer = new BayesInfer();
		String modelPath = "data/result/model.m";
		List<ValidationID> validationIDs = crossValidation.getVerificationIDs();
		crossValidation.crossCheck(trainer,infer,modelPath);
		crossValidation.showResult();
	}
}
