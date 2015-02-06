package util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * @author xiaohe
 * 创建于：2015年2月6日
 * 清除文件下的所有文件，不会清除子文件夹
 */
public class CleanResultFolder {
	public static void cleanFolder(String path)
	{
		List<String> files = new ArrayList<String>();
		Queue<String> folders = new LinkedList<String>();		
		File file = new File(path);

		String[] fileArray = file.list();

		for (String str : fileArray) {
			File childfile = new File(path + str);
			if (childfile.isFile()) {
				files.add(path + str);
			} else if (childfile.isDirectory()) {
				folders.offer(path + str);
			}
		}

		while (folders.size() != 0) {
			String subFolder = folders.poll();
			File childfile = new File(subFolder);
			String[] childList = childfile.list();

			for (String str : childList) {
				File tempfile = new File(subFolder + "/" + str);
				if (tempfile.isFile()) {
					files.add(subFolder + "/" + str);
				} else if (tempfile.isDirectory()) {
					folders.offer(subFolder + "/" + str);
				}
			}
		}

		System.out.println(files);

		for (String subFile : files) {
			File tempFile = new File(subFile);
			tempFile.delete();
		}
	}

	public static void main(String[] args) throws IOException,
			ClassNotFoundException {
		String path = "D:/1/";
		CleanResultFolder.cleanFolder(path);
	}

}
