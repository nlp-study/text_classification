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
import base.ClassifyEvaluationIndex;
import base.ClassifyResult;
import classifier.AbstractInfer;
import classifier.AbstractTrainer;
import classifier.knn.KNNInfer;
import classifier.knn.KNNTrainer;
import file.classify.DocsDivide;
import file.classify.FileClassifyList;

/**
 * @author Administrator
 *  把总体的数据分成训练和推导两部分，做简单的一次测试，不是交叉验证
 */
public class SingleValidation {
	Logger logger = Logger.getLogger(SingleValidation.class);
	
	//原始语料对应的目录
	private String sourcePath;
	
	//放置结果的根目录
	private String rootPath;
	
	//放置模型文件的目录
	private String modelPath;
	
	//停用词目录
	private String stopwordPath;
	
	//训练文件列表
	private FileClassifyList trainFileList = new FileClassifyList();
	
	//测试文件列表
	private FileClassifyList inferFileList = new FileClassifyList();
	
	//测试的结果
	private List<ClassifyResult> classifyResults = new ArrayList<ClassifyResult>();
		
	//各类的测试指标
	private List<ClassifyEvaluationIndex> classifyEvaluationIndexs = new ArrayList<ClassifyEvaluationIndex>();
		
	//给每一个class赋予一个id
	private Map<String, Integer> classIDs = new HashMap<String, Integer>();
	
	
	public SingleValidation(){}

	public SingleValidation(String sourcePath, String rootPath,String stopwordPath)  {
		this.sourcePath = sourcePath;
		this.rootPath = rootPath;
		this.stopwordPath = stopwordPath;
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
        DocsDivide.singleDivide(sourcePath,proportion,trainFileList,inferFileList);
        
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

			ClassifyEvaluationIndex classifyEvaluationIndex = new ClassifyEvaluationIndex(i,nt,nf,ntf);
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
		
	    for(ClassifyEvaluationIndex classifyEvaluationIndex:classifyEvaluationIndexs)
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
		
//		String sourcePath = "C:/projectStudy/data/text_classify/data/answer/";		
		String sourcePath = "data/corpus/";	
//		String sourcePath = "F:/分类数据/复旦语料库/answer/";
		String path = "data/result/";
		String stopwordPath = "data/stopword/";
		
		//清除输出目录中的文件
		CleanResultFolder.cleanFolder(path);
		CleanResultFolder.cleanFolder("logs/");
		
//		BayesTrainer knnTrainer = new BayesTrainer();
//		BayesInfer knnInfter = new BayesInfer();
		
		KNNTrainer knnTrainer = new KNNTrainer();
		KNNInfer knnInfter = new KNNInfer();
		
		SingleValidation crossValidation = new SingleValidation(sourcePath,path,stopwordPath);
		crossValidation.init();		
		crossValidation.train(knnTrainer);		
		crossValidation.infer(knnInfter);
	}
}
