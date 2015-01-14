package base;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import dict.inverted_index.InvertedIndexBuilder;
import util.FileRead;

public class ClassName {
	Logger logger = Logger.getLogger(InvertedIndexBuilder.class);

	private static List<String> names = new ArrayList<String>();
	
	public  void add(String name)
	{
		if(names.contains(name))
		{
			return ;
		}
		names.add(name);
	}
	
	public  void init(String path) throws IOException
	{
		names = FileRead.readLine(path);
	}
	
	public  void setClassname(List<String> tempNames )
	{
		names = tempNames;
	}
	
	public static List<String> getNames()
	{
		return names;
	}
	
	public static int size()
	{
		return names.size();
	}
	
	public static boolean contains(String name)
	{
		if(names.contains(name))
		{
			return true;
		}
		return false;
	}
	
	public static String git(int i)
	{
		if(i<names.size())
		{
			return names.get(i);
		}
		return "";
	}
	
	public static String toString1()
	{
	    return  names.toString();
	}
	
	public void writeClassWithID(String path) throws IOException
	{
		if(names.size() == 0)
		{
			logger.error("类别的数量为0");
		}
		
		BufferedWriter buffWrite = new BufferedWriter(new FileWriter(path));
		
		for(int i=0;i<names.size();++i)
		{
			buffWrite.write(names.get(i)+" "+i+"\r\n");
		}
		
		buffWrite.close();		
	}
	
	public static void main(String[] args) {

	}

}
