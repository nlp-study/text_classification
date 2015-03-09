package tagging;

import java.util.ArrayList;
import java.util.List;

public class TaggingInstance {
	List<TaggingInput> instances = new ArrayList<TaggingInput>();
	
	
	public int getMaxTaggingID()
	{
		int max = -1;
		
		for(TaggingInput instance:instances)
		{
			if(instance.getTagging() > max)
			{
				max = instance.getTagging();
			}
		}
		
		return max;
	}
	
	
	public int getMaxWordID()
	{
		int max = -1;
		
		for(TaggingInput instance:instances)
		{
			if(instance.getWord() > max)
			{
				max = instance.getWord();
			}
		}
		
		return max;
	}
	
	public int getInstanceSize()
	{
		return instances.size();
	}

	public List<TaggingInput> getInstances() {
		return instances;
	}

	public void setInstances(List<TaggingInput> instances) {
		this.instances = instances;
	}
}
