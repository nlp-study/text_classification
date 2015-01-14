package file.classify;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import base.ClassName;
import util.FileRead;
import dict.inverted_index.InvertedIndexBuilder;

public class FileClassifyList extends ArrayList<FileCategory> {
	Logger logger = Logger.getLogger(FileClassifyList.class);
	
	ClassName className = new ClassName();

	public FileClassifyList() {
	}
	
	public FileClassifyList(FileClassifyList fileClassifyList) {
		addAll(fileClassifyList);
	}
	
	public void addFile(int id, String fileame, String className)
	{
		FileCategory fileCategory = new FileCategory(id,fileame,className);
		add(fileCategory);
	}

	/**
	 * @param path
	 * @return void
	 * @comment:解析语料库，将文档编号，提取路径，决定类别
	 */
	public void processFolder(String path) {
		File file = new File(path);
		String[] folderList = file.list();

		int id = 0;
		for (int i = 0; i < folderList.length; ++i) {
			String str = folderList[i];

			File tempFile = new File(path + str);
			String[] fileList = tempFile.list();
			String[] tempSplitNames = str.split("-");
			if(tempSplitNames.length <2)
			{
				logger.error("输入不正常！");
				return ;
			}
			
			//类别都用大写来表示
			String tempName = tempSplitNames[1];
			tempName = tempName.toUpperCase();
			for (int j = 0; j < fileList.length; ++j) {
				FileCategory fileCategory = new FileCategory();
				className.add(tempName);
				fileCategory.setClassName(tempName);
				fileCategory.setFileame(path + str + "/" + fileList[j]);
				fileCategory.setId(id);
				add(fileCategory);
				++id;
			}
		}
	}
	
	
	/**
	 * @comment:只处理录入的类别的文档
	 * @param path
	 * @param className
	 * @return:void
	 */
	public void processFolder(String path,ClassName className)
	{
		this.className = className;
		
		File file = new File(path);
		String[] folderList = file.list();

		int id = 0;
		for (int i = 0; i < folderList.length; ++i) {
			String str = folderList[i];

			File tempFile = new File(path + str);
			String[] fileList = tempFile.list();
			String[] tempSplitNames = str.split("-");
			if(tempSplitNames.length <2)
			{
				logger.error("输入不正常！");
				return ;
			}
			
			//类别都用大写来表示
			String tempName = tempSplitNames[1];
			tempName = tempName.toUpperCase();
			
			//不包含的类别就不录入
			if(!className.contains(tempName))
			{
				continue;
			}
			
			for (int j = 0; j < fileList.length; ++j) {
				FileCategory fileCategory = new FileCategory();
				className.add(tempName);
				fileCategory.setClassName(tempName);
				fileCategory.setFileame(path + str + "/" + fileList[j]);
				fileCategory.setId(id);
				add(fileCategory);
				++id;
			}
		}
	}

	/**
	 * @param path
	 * @throws IOException,                   
	 * @return void
	 * @comment:从已经生成的结果文件中恢复出该类
	 */
	public void recoverFileCategs(String path) throws IOException {
		List<String> fileCategorys = FileRead.readLine(path);
		for (String str : fileCategorys) {
			FileCategory fileCategory = str2FileCategory(str);
			add(fileCategory);
		}
	}

	public FileCategory str2FileCategory(String input) {
		FileCategory fileCategory = new FileCategory();
		String regex = "^\\d+(?=\\-)";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(input);

		if (matcher.find()) {
			int id = Integer.parseInt(matcher.group());
			fileCategory.setId(id);
		} else {
			logger.error("Can not extract id!");
		}

		regex = "(?<=\\-).+(?=\\-[A-Z]+)";
		pattern = Pattern.compile(regex);
		matcher = pattern.matcher(input);

		if (matcher.find()) {
			String path = matcher.group();
			fileCategory.setFileame(path);
		} else {
			logger.error("Can not extract path!");
		}

		regex = "(?<=\\-)[A-Z]+$";
		pattern = Pattern.compile(regex);
		matcher = pattern.matcher(input);

		if (matcher.find()) {
			String classname = matcher.group();
			classname = classname.toUpperCase();
			fileCategory.setClassName(classname);
			className.add(classname);
		}

		return fileCategory;
	}
	

	/**
	 * @param className
	 * @return int
	 * @comment:求取指定类的文档的数量
	 */
	public int numbForClass(String tempName)
	{
		if(!className.contains(tempName))
		{
			logger.error("该类不存在！："+tempName);
			return 0;
		}
		
		int numb = 0;
		for(FileCategory fileCategory:this)
		{
			if(fileCategory.classSame(tempName))
			{
				++numb;
			}
		}
		return numb;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();

		for (FileCategory fileCategory : this) {
			sb.append(fileCategory);
			sb.append("\r\n");
		}
		return sb.toString();
	}

	/**
	 * @param path
	 * @throws IOException
	 * @return:void
	 * @comment: 将结果记录到文件，包括类别列表
	 */
	public void writeFileAndClass(String path) throws IOException {
		writeFiles(path);
		writeClassNames(path);
	}
	
	public void writeFiles(String path) throws IOException
	{
		BufferedWriter buffWrite = new BufferedWriter(new FileWriter(path+"file_index.txt"));

		for (FileCategory fileCategory : this) {
			buffWrite.write(fileCategory.toString() + "\r\n");
		}

		buffWrite.close();
	}
	
	public void writeClassNames(String path) throws IOException
	{
		className.writeClassWithID(path+"classid.txt");
	}

	public static void main(String[] args) {
		FileClassifyList fileProcess = new FileClassifyList();
		fileProcess.processFolder("data/answer/");

		System.out.println(fileProcess);
	}
}
