package classifier.verification;

import java.util.List;
import java.util.Set;

import base.InputFeature;
import base.InstanceD;

public class CrossVerification {
	InputFeature inputFeature ;
	
	public CrossVerification(InputFeature inputFeature)
	{
		this.inputFeature = inputFeature;
	}
	
	public void crossCheck()
	{
		CrossSections crossSections = new CrossSections(inputFeature);
		crossSections.excute();
		List<VerificationID> verificationIDs = crossSections.getVerificationIDs();
		
		for(int i=0;i<verificationIDs.size();++i)
		{
			Set<Integer> trains = verificationIDs.get(i).getTrainids();
			Set<Integer> infers = verificationIDs.get(i).getTrainids();
			
			InputFeature trainFeature = new InputFeature();
			InputFeature inferFeature = new InputFeature();
			
			trainFeature.setLength(inputFeature.getLength());
			inferFeature.setLength(inputFeature.getLength());
			
			for(Integer j:trains)
			{
				InstanceD instance = inputFeature.getInstanceD(j);
				trainFeature.add(instance);
			}
			
			for(Integer j:infers)
			{
				inferFeature.add(inputFeature.getInstanceD(j));
			}
			
			
			
		}
		
	}

}
