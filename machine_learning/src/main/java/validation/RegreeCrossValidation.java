package validation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.PropertyConfigurator;

import regress.AbstractRegressInfer;
import regress.AbstractRegressTrainer;
import regress.knn.KNNInfer;
import regress.knn.KNNTrainer;
import validation.slice.DataSlice;
import validation.slice.KFolderDataSlice;
import validation.slice.ValidationID;
import base.ClassifyResult;
import base.InputFeatureD;
import base.InstanceD;
import evaluation.ClassifyEvaluation;

public class RegreeCrossValidation {
	InputFeatureD inputFeature ;
	List<ValidationID> verificationIDs;
	List<Double> recalls = new ArrayList<Double>();
	
	public RegreeCrossValidation(InputFeatureD inputFeature)
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

	public void crossCheck(AbstractRegressTrainer trainer,AbstractRegressInfer infer,String path) throws Exception
	{	
		for(int i=0;i<verificationIDs.size();++i)
		{
			Set<Integer> trains = verificationIDs.get(i).getTrainids();
			Set<Integer> infers = verificationIDs.get(i).getInferids();
			List<ClassifyResult> classifyResults = new ArrayList<ClassifyResult>();
			
			InputFeatureD trainFeature = new InputFeatureD();
			InputFeatureD inferFeature = new InputFeatureD();
			
			
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
			
			train(trainer, trainFeature, path);
			
			int[] result = infer( infer, inferFeature,path);
			
			for(int j=0;j<result.length;++j)
			{
				ClassifyResult classifyResult = new ClassifyResult(inferFeature.getClassID(j),result[j]);
				classifyResults.add(classifyResult);
			}
			
			calculateRecall(classifyResults);
						
		}
		
	}
	
	public void train(AbstractRegressTrainer trainer,InputFeatureD inputFeature,String path) throws Exception
	{
		trainer.clear();
		trainer.init(inputFeature);
		trainer.train();
		trainer.saveModel(path);
	}
	
	public int[] infer(AbstractRegressInfer infer,
			InputFeatureD inputFeature,String path) throws Exception	{
		
		infer.init(path);
		int[] results = new int[inputFeature.getSize()];
		int i=0;
		for(InstanceD instance:inputFeature.getInstances())
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
		
		//产生数据
		String path = "data/corpus/iris.data";
		Iris iris = new Iris();
		iris.readData(path);
		InputFeatureD inputFeature = iris.getInputFeature();
		
		//使用的回归数据
		AbstractRegressTrainer trainer = new KNNTrainer();
		AbstractRegressInfer infer = new KNNInfer();
		
		//交叉运行的数据
		RegreeCrossValidation crossValidation = new RegreeCrossValidation(inputFeature);
		DataSlice dataSlice = new KFolderDataSlice();
		crossValidation.sliceData(dataSlice);		
		String modelPath = "data/result/model.m";
		crossValidation.crossCheck(trainer,infer,modelPath);
		crossValidation.showResult();
	}

}
