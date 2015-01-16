package classifier.verification;

import java.util.HashSet;
import java.util.Set;

public class VerificationID {
	Set<Integer> trainids = new HashSet<Integer>();
	Set<Integer> inferids = new HashSet<Integer>();
	
	public VerificationID(Set<Integer> trainids,Set<Integer> inferids)
	{
		this.trainids = trainids;
		this.inferids = inferids;
	}

	public Set<Integer> getTrainids() {
		return trainids;
	}

	public void setTrainids(Set<Integer> trainids) {
		this.trainids = trainids;
	}

	public Set<Integer> getInferids() {
		return inferids;
	}

	public void setInferids(Set<Integer> inferids) {
		this.inferids = inferids;
	}
	
	public boolean checkValidity(int numb)
	{
		Set<Integer> temp = new HashSet<Integer>();
		temp.addAll(trainids);
		temp.retainAll(inferids);
		int allNumb = trainids.size() + inferids.size();
		
		if(allNumb == numb && temp.size() == 0)
		{
			return true;
		}
		return false;
	}
	
	public String toString()
	{
		String str = trainids.toString()+"\r\n";
		str += inferids.toString()+"\r\n";
		return str;
	}

}
