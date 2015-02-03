package manager;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.sun.management.OperatingSystemMXBean;

import base.ClassName;
import base.CodePrase;
import dict.inverted_index.InvertedIndexBuilder;
import dict.inverted_index.MergeIndex;
import feature_selection.base.BaseFeatureSelection;
import file.classify.FileCategory;
import file.classify.FileClassifyList;
import sun.management.ManagementFactory;
import syntax.word.StopWords;
import syntax.word.WordTagging;
import util.CleanResultFolder;
import util.FileRead;
import vsm.VSMBuilder;
import vsm.VSMWordSelector;


/**
 * @author xiaohe
 * 由于词典数量巨大，内存放不下，因此先建立各个类的倒排表，写入文件。然后在将各个类的倒排表并归到一个主表中
 */
public class DictManager {
	Logger logger = Logger.getLogger(InvertedIndexBuilder.class);

	//停用词
	private StopWords stopWords = new StopWords();
	
	//输入文档
	private FileClassifyList fileProcess = new FileClassifyList();
	
	//建立的倒排表
	private InvertedIndexBuilder invertedIndexBuilder = new InvertedIndexBuilder();
	
	//合并各类倒排表的类
	private MergeIndex mergeIndex = new MergeIndex();

	public void init(FileClassifyList fileProcess,StopWords stopWords) throws IOException {
		this.fileProcess = fileProcess;		
		invertedIndexBuilder.init(stopWords);		
	}

	/**
	 * @throws IOException
	 * @return void
	 * @comment:建立所有类的倒排索引，一个类一个文件
	 */
	public void createEveryClassDict(String path) throws IOException {
		
		int index=0;

		for (int j=0;j<ClassName.size();++j) {
			logger.info("index:"+index+"className:"+ClassName.git(j));
			invertedIndexBuilder.clear();
            
			for (int i = 0; i < fileProcess.size(); ++i) {
				if(fileProcess.get(i).getClassName().equals(ClassName.git(j)))
				{
					logger.info("filename:"+fileProcess.get(i).getFileame());
					FileCategory fileCategory = fileProcess.get(i);
					inputDoc(fileCategory.getFileame(),fileCategory.getId());
				}
			}

			createDict();
			
			writeToFile(path+"dict/"+ClassName.git(j)+"-");
		}
	}
	
	
	public void mergeIndex(String path) throws IOException
	{
		File file = new File(path);
		String[] paths = file.list();
		
		for(String tempPath:paths)
		{
			if(tempPath.contains("invertedIndex"))
			{
				logger.info(path+tempPath);
				mergeIndex.merge(path+tempPath);
			}
		}
		
		mergeIndex.CreateDict();
		mergeIndex.writeInvertedIndex(path+"InvertedIndex.txt");
		mergeIndex.writeDict(path);
		
	}

	public void writeToFile(String path) throws IOException {
		logger.info("path:"+path);
		invertedIndexBuilder.saveInvertedIndex(path);
	}
	
	public void inputDoc(String file,int docID) throws IOException
	{
		logger.info(docID + "-" + file);
		String sourceFile = FileRead.readFile(file);
		sourceFile = CodePrase.full2HalfChange(sourceFile);
		List<String> words = WordTagging.segForWord(sourceFile);
		addWords(words, docID);
	}	

	public void addWords(List<String> words, int docID) {
		invertedIndexBuilder.build(words, docID);
	}

	public void createDict() {
		invertedIndexBuilder.createDict();
	}

	public static void main(String[] args) throws IOException {
		PropertyConfigurator.configure("log4j.properties");
		String path = "data/";
		
		//清除输出目录中的文件
		CleanResultFolder.cleanFolder("data/result/");
		CleanResultFolder.cleanFolder("logs/");
				
		String sourcePath = "C:/projectStudy/data/text_classify/data/answer/";
//		String sourcePath = "data/corpus/";
		FileClassifyList fileProcess = new FileClassifyList();
		fileProcess.processFolder(sourcePath);
		fileProcess.writeFileAndClass("data/result/file/");
		
//		StopWords stopWords = new StopWords();		
//		stopWords.addWords(path+"stopword/cn_stopwords.txt");
//		stopWords.addWords(path+"stopword/english_stopwords.txt");
//		stopWords.addWords(path+"stopword/punc_stopwords.txt");
//		
//		//创建词典
//		DictManager dictManager = new DictManager();		
//		dictManager.init(fileProcess,stopWords);
//		dictManager.createEveryClassDict("data/result/");
//		dictManager.mergeIndex("data/result/dict/");
		
		/***********chi square select****************/
		//从词中选择特征
//	    CHiSquareManager chiSquareManager = new CHiSquareManager();
//		chiSquareManager.init("data/result/",fileProcess);
//		chiSquareManager.excute();
//		
//		//从各个类中收集特征组成一个总的特征集合
//		VSMWordSelector vsmWordSelector = new VSMWordSelector();
//		String slectedPath = "data/result/chi_square/";
//		vsmWordSelector.init(slectedPath);
//		vsmWordSelector.writeFeatureWords("data/result/train/vsmword.txt");
		/***********chi square select****************/
		
		BaseFeatureSelection baseFeatureSelection = new BaseFeatureSelection("data/result/dict/wordNumb.txt");
		baseFeatureSelection.init();
		baseFeatureSelection.execute();
		baseFeatureSelection.writeFeature("data/result/train/vsmword.txt");
		baseFeatureSelection.clear();
		
//		System.out.println(getEMS());
		
		
		//把每一篇文档都处理成一个向量
		VSMBuilder vsmBuilder = new VSMBuilder();		
		vsmBuilder.init(path,fileProcess);
//		vsmBuilder.docs2vsms();
//		vsmBuilder.writeVSMs("data/result/train/everyVsmword.txt");
		vsmBuilder.docs2vsms("data/result/train/everyVsmword.txt");
		
	}
	
	public static String getEMS() {  
	    StringBuffer sb=new StringBuffer();  
	    OperatingSystemMXBean osmb = (OperatingSystemMXBean) ManagementFactory  
	            .getOperatingSystemMXBean();  
	       sb.append("系统物理内存总计：" + osmb.getTotalPhysicalMemorySize()  
	            / 1024 / 1024 + "MB<br>");  
	       sb.append("系统物理可用内存总计：" + osmb.getFreePhysicalMemorySize()  
	            / 1024 / 1024 + "MB");  
	    return sb.toString();  
	}  
}
