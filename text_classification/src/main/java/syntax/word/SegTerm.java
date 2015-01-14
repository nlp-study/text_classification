package syntax.word;

public class SegTerm {
	String word;
	String tagging;
	
	public SegTerm()
	{
		
	}
	
	
	public SegTerm(String word, String tagging) {
		super();
		this.word = word;
		this.tagging = tagging;
	}


	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	public String getTagging() {
		return tagging;
	}
	public void setTagging(String tagging) {
		this.tagging = tagging;
	}
	
}
