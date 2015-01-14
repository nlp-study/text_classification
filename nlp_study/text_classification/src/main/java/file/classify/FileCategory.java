package file.classify;

import base.ClassName;


public class FileCategory {
	private int id;
	private String fileame;
	private String className;
	
	public FileCategory()
	{
		
	}
	
	public FileCategory(int id, String fileame, String className) {
		super();
		this.id = id;
		this.fileame = fileame;
		this.className = className;
	}



	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getFileame() {
		return fileame;
	}
	public void setFileame(String fileame) {
		this.fileame = fileame;
	}
	
	public String getClassName() {
		return className;
	}
	
	public void setClassName(String className) {
		this.className = className;
	}
	
	public boolean classSame(String tempClassName)
	{
		if(this.className.equals(tempClassName))
		{
			return true;
		}
		return false;
	}
	
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(id);
		sb.append("-");
		sb.append(fileame);
		sb.append("-");
		sb.append(className);
		return sb.toString();
	}

}
