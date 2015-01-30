package regress.knn;

import java.util.ArrayList;
import java.util.List;

import regress.AbstractRegressModel;
import base.InstanceD;
import classifier.AbstractModel;

public class KNNModel extends AbstractRegressModel {
	List<InstanceD> instances = new ArrayList<InstanceD>();
    
    public KNNModel()
    {  	
    }
    
	public KNNModel(List<InstanceD> instances) {
		this.instances = instances;
	}

	public List<InstanceD> getInstances() {
		return instances;
	}

	public void setInstances(List<InstanceD> instances) {
		this.instances = instances;
	}

	
    
}
