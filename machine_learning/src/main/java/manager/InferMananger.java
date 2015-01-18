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

import classifier.AbstractInfer;
import classifier.bayes.BayesInfer;
import base.ClassName;
import base.ClassifyEvaluationIndex;
import base.ClassifyResult;
import syntax.word.StopWords;
import util.FileRead;
import vsm.VSM;
import vsm.VSMBuilder;
import vsm.VSMWordSelector;
import file.classify.FileClassifyList;

public class InferMananger {
	Logger logger = Logger.getLogger(InferMananger.class);

	//训练文件的输入文档
	FileClassifyList inferFileList = new FileClassifyList();
	
	//存放各种结果的根目录
	String stopwordPath;
		
	//停用词
	StopWords stopWords = new StopWords();
	
	//存放各种结果的根目录
	String path;
	
	// 根据前面输入的信息，把每一篇文档转化成vsm
	VSMBuilder vsmBuilder = new VSMBuilder();
	
	//测试的结果
	List<ClassifyResult> classifyResults = new ArrayList<ClassifyResult>();
	
	//各类的测试指标
	List<ClassifyEvaluation> classifyEvaluationIndexs = new ArrayList<ClassifyEvaluation>();
	
	
	public InferMananger()
	{
		
	}
	
	public InferMananger(FileClassifyList inferFileList,String path,String stopwordPath)
	{
	     this.inferFileList = inferFileList;
	     this.stopwordPath = stopwordPath;
	     this.path = path;
	}
	
	public void init() throws IOException
	{
		loadStopword();		
		buildVSM();
		createClassMap(path+"file/classid.txt");
	}	
	
	public void loadStopword() throws IOException
	{
		stopWords.addWords(stopwordPath+"cn_stopwords.txt");
		stopWords.addWords(stopwordPath+"english_stopwords.txt");
		stopWords.addWords(stopwordPath+"punc_stopwords.txt");
	}

	public void buildVSM() throws IOException
	{
		vsmBuilder.init(path,inferFileList,stopWords);
		vsmBuilder.docs2vsms();
	}
	
	public void createClassMap(String path) throws IOException
	{
		List<String> classids = FileRead.readLine(path);
		
		for(String str:classids)
		{
			String[] classid = str.split(" ");
		}
	}
	
	public List<ClassifyResult> infer(AbstractInfer infer,String modelPath,String classifyResultPath) throws Exception
	{
		infer.init(modelPath);
		
		List<VSM> testVsms = vsmBuilder.getVsms();
		for(VSM vsm:testVsms)
		{
			double[] tempVector = new double[vsm.getVector().length];
			for(int i=0;i<tempVector.length;++i)
			{
				tempVector[i] = vsm.getVector()[i];
			}
			int retusltType =  infer.infer(tempVector);
			ClassifyResult classifyResult = new ClassifyResult(vsm.getType(),retusltType);
			classifyResults.add(classifyResult);
		}
		
		return classifyResults;
	}
	
	public static void main(String[] args) throws Exception
	{
		PropertyConfigurator.configure("log4j.properties");

		
		String sourcePath = "data/corpus/";		
		String path = "data/result/";
		String stopwordPath = "data/stopword/";
		
		FileClassifyList inferFileList = new FileClassifyList();
		inferFileList.processFolder(sourcePath);
		
		InferMananger inferManager = new InferMananger(inferFileList,path,stopwordPath);
		inferManager.init();
		BayesInfer knnInfter = new BayesInfer();
		String modelPath = path+"train/bayes.m";
		String resultPaht = path+"test/evaluation_index.txt";
		String classifyResultPath = path+"test/classifyResult.txt";
		inferManager.infer(knnInfter, modelPath,classifyResultPath);
		
	}

}
