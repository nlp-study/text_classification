package base;

public class ClassifyResult {
	int tClass; //训练集中的类别
	int fClass; //测试出的结果
	String docName; //测试的文档的名称
	
	public ClassifyResult() {

	}
	
	public ClassifyResult(int tClass, int fClass) {
		super();
		this.tClass = tClass;
		this.fClass = fClass;
	}
	
	public int gettClass() {
		return tClass;
	}

	public void settClass(int tClass) {
		this.tClass = tClass;
	}

	public int getfClass() {
		return fClass;
	}

	public void setfClass(int fClass) {
		this.fClass = fClass;
	}

	public String getDocName() {
		return docName;
	}

	public void setDocName(String docName) {
		this.docName = docName;
	}

	public boolean isSame()
	{
		if(tClass == fClass)
		{
			return true;
		}
		return false;
	}
	
	public String toString()
	{
		return tClass+" "+fClass;
	}

}
