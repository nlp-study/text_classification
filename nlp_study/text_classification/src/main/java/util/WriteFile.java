package util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class WriteFile {
	public static void writeFile(List<String> list,String path) throws IOException
	{
		File file = new File(path);
		if(!file.exists())
		{
			file.createNewFile();
		}
			
		FileWriter writer = new FileWriter(file);
		BufferedWriter bwriter = new BufferedWriter(writer);
		
		for(String str:list)
		{
			bwriter.write(str+"\r\n");
		}
		
		bwriter.close();
		writer.close();
	}
	
	public static void writeFile(Map<String,Integer> map,String path) throws IOException
	{
		File file = new File(path);
		if(!file.exists())
		{
			file.createNewFile();
		}
			
		FileWriter writer = new FileWriter(file);
		BufferedWriter bwriter = new BufferedWriter(writer);
		
		for (String key : map.keySet()) {
		
			bwriter.write(map.get(key)+"-"+key+"\r\n");
		}
		
		bwriter.close();
		writer.close();
	}
	
	public static void main(String[] args) throws IOException
	{
		String[] name = {"zhang","li"};
		WriteFile.writeFile(Arrays.asList(name), "D:/1.txt");
	}

}
