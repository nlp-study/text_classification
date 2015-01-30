package regress.knn;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import regress.AbstractRegressTrainer;
import base.InputFeatureD;
import base.InstanceD;
import classifier.AbstractTrainer;
import classifier.bayes.BayesModel;
import classifier.bayes.BayesTrainer;

/**
 * @author xiaohe
 * 创建于：2014年12月31日
 * KNN算法的最简单的实现，没有用交叉验证确定k值，也没有建立快速的查找算法，以后需要改进
 */
public class KNNTrainer extends AbstractRegressTrainer {
	Logger logger = Logger.getLogger(KNNTrainer.class);

	KNNModel model;
	
    List<InstanceD> instances = new ArrayList<InstanceD>();
	

	public void init(InputFeatureD inputFeature) {
		// TODO Auto-generated method stub
		instances.addAll(inputFeature.getInstances());
		logger.info("输入特诊的数量："+instances.size());
	}
	
	public void train() {
	}
	
	public void clear()
	{
		instances.clear();
	}

	
	public void saveModel(String path) throws Exception {
		logger.info("fdafdas");
		model = new KNNModel(instances);
		
		super.saveModel(path, model);
		logger.info("输入特诊的数量："+instances.size());
	}

}
