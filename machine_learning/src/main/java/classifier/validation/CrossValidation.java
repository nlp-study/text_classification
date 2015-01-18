package classifier.validation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import classifier.AbstractInfer;
import classifier.AbstractTrainer;
import classifier.bayes.BayesInfer;
import classifier.bayes.BayesTrainer;
import evaluation.ClassifyEvaluationIndex;
import base.ClassifyResult;
import base.InputFeature;
import base.InstanceD;

public class CrossValidation {
	InputFeature inputFeature ;
	
	public CrossValidation(InputFeature inputFeature)
	{
		this.inputFeature = inputFeature;
	}
	
	public void crossCheck(AbstractTrainer trainer,AbstractInfer infer,String path) throws Exception
	{
		CrossSections crossSections = new CrossSections(inputFeature);
		crossSections.excute();
		List<ValidationID> verificationIDs = crossSections.getVerificationIDs();
		
		for(int i=0;i<verificationIDs.size();++i)
		{
			Set<Integer> trains = verificationIDs.get(i).getTrainids();
			Set<Integer> infers = verificationIDs.get(i).getTrainids();
			List<ClassifyResult> classifyResults = new ArrayList<ClassifyResult>();
			
			InputFeature trainFeature = new InputFeature();
			InputFeature inferFeature = new InputFeature();
			
			
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
			
			
			train( trainer, trainFeature, path);
			
			int[] result = infer( infer, inferFeature);
			
			for(int j=0;j<result.length;++j)
			{
				ClassifyResult classifyResult = new ClassifyResult(inferFeature.getClassID(j),result[j]);
				classifyResults.add(classifyResult);
			}
			
			calcualteIndex(classifyResults,trainer.getClassNumb());
						
		}
		
	}
	
	public void train(AbstractTrainer trainer,InputFeature inputFeature,String path) throws Exception
	{
		trainer.init(inputFeature);
		trainer.train();
		trainer.saveModel(path);
	}
	
	public int[] infer(AbstractInfer infer,InputFeature inputFeature) throws Exception	{
		
		infer.init("data/result/bayes_model.m");
		int[] results = new int[inputFeature.getSize()];
		int i=0;
		for(InstanceD instance:inputFeature.getInstances())
		{
			results[i] = infer.infer(instance.getVector());
			++i;
		}
		
		return results;		
	}
	
	
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

			ClassifyEvaluationIndex classifyEvaluationIndex = new ClassifyEvaluationIndex(i,nt,nf,ntf);
			classifyEvaluationIndex.calculate();
		}
	}

}
