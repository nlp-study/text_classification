package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileRead {
	public static List<String> readLine(String path) throws IOException
	{
		File file = new File(path);
		
		BufferedReader breader = new BufferedReader(new FileReader(path));
		
		String tempStr;
		List<String> sentences = new ArrayList<String>();
		
		while((tempStr = breader.readLine())!=null)
		{
			if(tempStr.length() != 0)
			{
				sentences.add(tempStr);		
			}
		}
		
		return sentences;		
	}
	
	public static String readFile(String path) throws IOException
	{
		File file = new File(path);
		
		BufferedReader breader = new BufferedReader(new FileReader(path));
		
		String tempStr;
		StringBuilder sb = new StringBuilder();
		
		while((tempStr = breader.readLine())!=null)
		{
			sb.append(tempStr);
		}
		
		return sb.toString();		
	}
	
}
