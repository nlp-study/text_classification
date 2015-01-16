package manager;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import syntax.word.StopWords;
import vsm.VSMBuilder;
import vsm.VSMWordSelector;
import classifier.AbstractTrainer;
import classifier.bayes.BayesTrainer;
import file.classify.FileClassifyList;

public class TrainerManager {
	Logger logger = Logger.getLogger(TrainerManager.class);

	//原始语料
	String corpusFile;
	
	//存放各种结果的根目录
	String stopwordPath;
	
	//存放各种结果的根目录
	String path;
	
	//训练文件的输入文档
	FileClassifyList trainFileList = new FileClassifyList();
	
	//停用词
	StopWords stopWords = new StopWords();
	
	// 根据前面输入的信息，把每一篇文档转化成vsm
	VSMBuilder vsmBuilder = new VSMBuilder();
	
	public TrainerManager()
	{
		
	}
	
	public TrainerManager(String corpusFile,String path,String stopwordPath) throws IOException
	{
	     this.corpusFile = 	corpusFile;
	     this.path = path;
	     this.stopwordPath = stopwordPath;
	     loadFile();
	}
	
	public TrainerManager(FileClassifyList trainFileList,String path,String stopwordPath)
	{
	     this.trainFileList = trainFileList;
	     this.stopwordPath = stopwordPath;
	     this.path = path;
	}
	
	public void init() throws IOException
	{
		loadStopword();
		
		/**以下操作都把结果保存在本地**/
		buildDict();
		selectFeature();
		buildFeatureSet();
		
		buildVSM();
	}
	
	public void loadFile() throws IOException
	{
		trainFileList.processFolder(corpusFile);
		trainFileList.writeFileAndClass(path+"file/");
	}
	
	public void loadStopword() throws IOException
	{
		stopWords.addWords(stopwordPath+"cn_stopwords.txt");
		stopWords.addWords(stopwordPath+"english_stopwords.txt");
		stopWords.addWords(stopwordPath+"punc_stopwords.txt");
	}
	
	public void buildDict() throws IOException
	{
		DictManager dictManager = new DictManager();		
		dictManager.init(trainFileList,stopWords);
		dictManager.createEveryClassDict(path);
		dictManager.mergeIndex(path+"dict/");
	}
	
	public void selectFeature() throws IOException
	{
		 CHiSquareManager chiSquareManager = new CHiSquareManager();
		 chiSquareManager.init(path,trainFileList);
		 chiSquareManager.excute();
	}
	
	/**
	 * @comment:從排好序的特征中構建特征集合
	 * @return:void
	 * @throws IOException 
	 */
	public void buildFeatureSet() throws IOException
	{
		VSMWordSelector vsmWordSelector = new VSMWordSelector();
		String slectedPath = path+"chi_square/";
		vsmWordSelector.init(slectedPath);
		vsmWordSelector.writeFeatureWords( path+"train/vsmword.txt");
	}
	
	public void buildVSM() throws IOException
	{
		vsmBuilder = new VSMBuilder();		
		vsmBuilder.init(path,trainFileList,stopWords);
		vsmBuilder.docs2vsms();
	}
	
	public void train(AbstractTrainer trainer,String path) throws Exception
	{
		trainer.init(vsmBuilder);
		trainer.train();
		trainer.saveModel(path);
	}
	
	public static void main(String[] args) throws Exception {
		PropertyConfigurator.configure("log4j.properties");

		String sourcePath = "C:/projectStudy/data/text_classify/data/answer/";
//		String sourcePath = "data/corpus/";
		String path = "data/result/";
		String stopwordPath = "data/stopword/";
		
		TrainerManager trainerManager = new TrainerManager(sourcePath,path,stopwordPath);
		trainerManager.init();
		BayesTrainer knnTrainer = new BayesTrainer();
		trainerManager.train(knnTrainer,path+"train/bayes.m");
	}
}
