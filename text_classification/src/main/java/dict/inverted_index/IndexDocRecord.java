package dict.inverted_index;

import java.util.ArrayList;
import java.util.List;

public class IndexDocRecord {
	int docID;
	List<Integer> pos = new ArrayList<Integer>();
	
	public int getDocID() {
		return docID;
	}
	public void setDocID(int docID) {
		this.docID = docID;
	}
	public List<Integer> getPos() {
		return pos;
	}
	public void setPos(List<Integer> pos) {
		this.pos = pos;
	}
	
	public void insert(int tempPos)
	{
		if(!pos.contains(tempPos))
		{
			pos.add(tempPos);
		}
	}
	
	public int size()
	{
		return pos.size();
	}
	
	
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append(docID);
		sb.append("-");
		
	    for(int i=0;i<pos.size();++i)
	    {
	    	if(i == pos.size()-1)
	    	{
	    		sb.append(pos.get(i));
	    		continue;
	    	}
	    	sb.append(pos.get(i));
	    	sb.append(",");
	    }
	    
	    return sb.toString();
	}

}
