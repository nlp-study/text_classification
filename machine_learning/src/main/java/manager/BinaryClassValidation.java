package manager;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import util.CleanResultFolder;
import util.FileRead;
import base.ClassName;
import base.ClassifyEvaluationIndex;
import base.ClassifyResult;
import classifier.AbstractInfer;
import classifier.AbstractTrainer;
import classifier.bayes.BayesInfer;
import classifier.bayes.BayesTrainer;
import classifier.knn.KNNInfer;
import classifier.knn.KNNTrainer;
import classifier.perceptron.PerceptronInfer;
import classifier.perceptron.PerceptronTrain;
import file.classify.DocsDivide;
import file.classify.FileClassifyList;

/**
 * @author xiaohe
 * 创建于：2015年1月5日
 * 专门处理二分类的类
 */
public class BinaryClassValidation {
Logger logger = Logger.getLogger(BinaryClassValidation.class);
	
	//原始语料对应的目录
	String sourcePath;
	
	//放置结果的根目录
	String rootPath;
	
	//放置模型文件的目录
	String modelPath;
	
	//停用词目录
	String stopwordPath;
	
	//需要处理的类，只能包含两个类别
	ClassName className = new ClassName();
	
	//训练文件列表
	FileClassifyList trainFileList = new FileClassifyList();
	
	//测试文件列表
	FileClassifyList inferFileList = new FileClassifyList();
	
	//测试的结果
	List<ClassifyResult> classifyResults = new ArrayList<ClassifyResult>();
		
	//各类的测试指标
	List<ClassifyEvaluation> classifyEvaluationIndexs = new ArrayList<ClassifyEvaluation>();
		
	//给每一个class赋予一个id
	Map<String, Integer> classIDs = new HashMap<String, Integer>();
	
	
	public BinaryClassValidation(){}

	public BinaryClassValidation(String sourcePath, 
			String rootPath,String stopwordPath
			,String classname1,String classname2)  {
		this.sourcePath = sourcePath;
		this.rootPath = rootPath;
		this.stopwordPath = stopwordPath;
		className.add(classname1);
		className.add(classname2);
	}
	
	public void init() throws IOException
	{
		modelPath = rootPath+"train/model.m";		
		separateCorpus();
		trainFileList.writeFileAndClass(rootPath+"file/");
	}
	
	/**
	 * @comment:将输入的语料分割成训练和测试两部分
	 * @return:void
	 * @throws IOException 
	 */
	public void separateCorpus() throws IOException
	{
        int proportion = 10; 
        FileClassifyList allList = new FileClassifyList();
		allList.processFolder(sourcePath,className);
		
        DocsDivide.singleDivide(allList,proportion,trainFileList,inferFileList);
        
        inferFileList.writeFiles("data/result/test/infer");
		
	}

	public void train(AbstractTrainer trainer) throws Exception
	{		
		TrainerManager trainerManager = new TrainerManager(trainFileList,rootPath,stopwordPath);
		trainerManager.init();
		trainerManager.train(trainer,modelPath);
		
	}
	
	public void infer(AbstractInfer infer) throws Exception
	{

		String resultPaht = rootPath+"test/evaluation_index.txt";
		String classifyPath = rootPath+"test/classifyResult1.txt";
		String classifyResultPath = rootPath+"test/classifyResult.txt";
		
		InferMananger inferManager = new InferMananger(inferFileList,rootPath,stopwordPath);
		inferManager.init();
		classifyResults = inferManager.infer(infer, modelPath,classifyResultPath);
		
		for(int i=0;i<classifyResults.size();++i)
		{
			if(classifyResults.get(i).getfClass() == -1){
				ClassifyResult classifyResult = classifyResults.get(i);
				classifyResult.setfClass(0);
				classifyResults.set(i,classifyResult);
			}
		}
		
		calcualteIndex();
		writeResult(resultPaht);
		writeClassifyResults(classifyPath);
	}
	
	public void createClassMap(String path) throws IOException
	{
		List<String> classids = FileRead.readLine(path);
		
		for(String str:classids)
		{
			String[] classid = str.split(" ");
			classIDs.put(classid[0], Integer.parseInt(classid[1]));
		}
	}
	
	/**
	 * @return:void
	 * @throws IOException 
	 * @comment:计算分类的评价指标
	 */
	public void calcualteIndex() throws IOException {
		createClassMap(rootPath+"file/classid.txt");
		
		int nt = 0; // 训练集中某个类别的数量
		int ntf = 0; // 测试集和训练集中分类结果相同的
		int nf = 0; // 测试集中某个类别的数量
		int classNumb = classIDs.size();
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

			ClassifyEvaluation classifyEvaluationIndex = new ClassifyEvaluation(i,nt,nf,ntf);
			classifyEvaluationIndex.calculate();
			classifyEvaluationIndexs.add(classifyEvaluationIndex);
			
			nt = 0;
			nf = 0;
			ntf = 0;
		}
	}
	
	
	
	public void writeResult(String path) throws IOException
	{
        BufferedWriter buffWriter = new BufferedWriter(new FileWriter(path));
		
	    for(ClassifyEvaluation classifyEvaluationIndex:classifyEvaluationIndexs)
	    {
	    	buffWriter.write(classifyEvaluationIndex.toString()+"\r\n");
	    }
		
		buffWriter.close();
	}
	
	public void writeClassifyResults(String path) throws IOException
	{
        BufferedWriter buffWriter = new BufferedWriter(new FileWriter(path));
		
        int i=0;
	    for(ClassifyResult classifyResult :classifyResults)
	    {
	    	buffWriter.write(i+" "+classifyResult.toString()+"\r\n");
	    	++i;
	    }
		
		buffWriter.close();
	}
	
	public static void main(String[] args) throws Exception
	{
		PropertyConfigurator.configure("log4j.properties");
		
		String sourcePath = "C:/projectStudy/data/text_classify/data/answer/";		
//		String sourcePath = "data/corpus/";	
//		String sourcePath = "F:/分类数据/复旦语料库/answer/";
		String path = "data/result/";
		String stopwordPath = "data/stopword/";
		
		//清除输出目录中的文件
		CleanResultFolder.cleanFolder(path);
		CleanResultFolder.cleanFolder("logs/");
		
		String classname1 = "AGRICULTURE";
		String classname2 = "POLITICS";
		
		PerceptronTrain perceptronTrain = new PerceptronTrain();
		PerceptronInfer perceptronInfer = new PerceptronInfer();
		
		
		KNNTrainer knnTrainer = new KNNTrainer();
		KNNInfer knnInfter = new KNNInfer();
		
		BayesTrainer bayesTrainer = new BayesTrainer();
		BayesInfer bayesInfter = new BayesInfer();
		
		BinaryClassValidation validation = new BinaryClassValidation(sourcePath,path,stopwordPath,classname1,classname2);
		validation.init();
		validation.train(perceptronTrain);
		validation.infer(perceptronInfer);
		
		
	}

}
