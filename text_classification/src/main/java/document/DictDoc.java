package document;

import java.util.ArrayList;
import java.util.List;

public class DictDoc extends Document {
	List<String> segs = new ArrayList<String>();

	public List<String> getSegs() {
		return segs;
	}

	public void setSegs(List<String> segs) {
		this.segs = segs;
	}
	
	public void removeWord(String word)
	{
		segs.remove(word);
	}

}
