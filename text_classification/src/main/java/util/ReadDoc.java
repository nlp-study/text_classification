package util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import document.DictDoc;

public class ReadDoc {
	public List<DictDoc> readAll(String path) throws IOException
	{
		List<DictDoc> dictDocs = new ArrayList<DictDoc>();
		File file = new File(path);
		
		String[] folders = file.list();
		
		for(int i=0;i<folders.length;++i)
		{
			File childerFile = new File(folders[i]);
			String[] docPaths = childerFile.list();
			for(String textPath:docPaths)
			{
				String tempDoc = FileRead.readFile(textPath);
				DictDoc dictDoc = new DictDoc();
				dictDoc.setText(tempDoc);
				dictDocs.add(dictDoc);
			}
			
		}
		return dictDocs;		
	}

}
