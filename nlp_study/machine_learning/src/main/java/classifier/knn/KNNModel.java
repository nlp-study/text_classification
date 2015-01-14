package classifier.knn;

import java.util.ArrayList;
import java.util.List;

import vsm.VSM;
import classifier.AbstractModel;

public class KNNModel extends AbstractModel {
    List<VSM> vsms = new ArrayList<VSM>();
    
    public KNNModel()
    {
    	
    }

    
	public KNNModel(List<VSM> vsms) {
		this.vsms = vsms;
	}


	public List<VSM> getVsms() {
		return vsms;
	}

	public void setVsms(List<VSM> vsms) {
		this.vsms = vsms;
	}
    
}
