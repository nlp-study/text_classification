package tagging;

import java.util.ArrayList;
import java.util.List;

public class TaggingInstance {
	private List<TaggingInput> instances = new ArrayList<TaggingInput>();
	private boolean isTagging = false;
	
	
	public TaggingInstance(List<TaggingInput> instances, boolean isTagging) {
		this.instances = instances;
		this.isTagging = isTagging;
	}

	public boolean isTagging() {
		return isTagging;
	}
	
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
}
