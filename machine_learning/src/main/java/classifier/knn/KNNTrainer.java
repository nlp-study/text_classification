package classifier.knn;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import manager.SingleValidation;

import org.apache.log4j.Logger;

import vsm.VSM;
import vsm.VSMBuilder;
import classifier.AbstractTrainer;

/**
 * @author xiaohe
 * 创建于：2014年12月31日
 * KNN算法的最简单的实现，没有用交叉验证确定k值，也没有建立快速的查找算法，以后需要改进
 */
public class KNNTrainer implements AbstractTrainer {
	Logger logger = Logger.getLogger(KNNTrainer.class);

	KNNModel model = new KNNModel();
	
    List<VSM> vsms = new ArrayList<VSM>();
	
	@Override
	public void init(VSMBuilder vsmBuilder) {
		// TODO Auto-generated method stub
		vsms.addAll(vsmBuilder.getVsms());
		logger.info(vsms.size());
	}

	@Override
	public void train() {
		// TODO Auto-generated method stub

	}

	@Override
	public void saveModel(String path) throws Exception {
		// TODO Auto-generated method stub
		 logger.info(vsms.size());
		 logger.info(vsms.toString());
		 for(VSM vsm:vsms)
		 {
			 logger.info(Arrays.toString(vsm.getVector()));
		 }
		 model.setVsms(vsms);
		 logger.info("model size:"+model.getVsms().size());
		
		 FileOutputStream fo = new FileOutputStream(path);   
	     ObjectOutputStream so = new ObjectOutputStream(fo);   
	  
	     try {   
	            so.writeObject(model);   
	            so.close();   
	  
	     } catch (IOException e) {   
	            System.out.println(e);   
	     }   
	}

}
