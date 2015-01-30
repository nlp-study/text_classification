package regress.knn;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import regress.AbstractRegressInfer;
import util.DistanceCalculation;
import classifier.AbstractInfer;
import classifier.bayes.BayesModel;
import base.InstanceD;

public class KNNInfer extends AbstractRegressInfer {
	Logger logger = Logger.getLogger(KNNInfer.class);

	class Distance {
		int classid;
		double distance;

		public Distance() {

		}

		public Distance(int classid, double distance) {
			this.classid = classid;
			this.distance = distance;
		}

		public int getClassid() {
			return classid;
		}

		public void setClassid(int classid) {
			this.classid = classid;
		}

		public double getDistance() {
			return distance;
		}

		public void setDistance(double distance) {
			this.distance = distance;
		}
		
		public String toString()
		{
			return classid+" "+distance;
		}

	}

	private KNNModel model = new KNNModel();
	private List<InstanceD> instances = new ArrayList<InstanceD>();

	//最后用来比较的向量的数量
	private int k;
	
	private int dim;
	
	//实际结果的数量
	private int size = 0;

	private Distance[] distances;
	
	
	public List<InstanceD> getInstance() {
		return instances;
	}

	public void setInstance(List<InstanceD> instances) {
		this.instances = instances;
	}

	public int getK() {
		return k;
	}

	public void setK(int k) {
		this.k = k;
	}

	@Override
	public void init(String path) throws Exception {
		// TODO Auto-generated method stub
		logger.info("KNNInfer 初始化:"+path);
				
		model = (KNNModel)super.initModel(path);
		

		instances.addAll(model.getInstances());

		k = 5;

		distances = new Distance[k];
		
		if(instances.size()>0)
		{
			dim = instances.get(0).getLength();
		}
		else
		{
			logger.error("模型为空");
		}

		initDistances();
	}

	@Override
	public int infer(double[] input) {
        logger.info("infer start!");
        initDistances();
        
		if (input.length != dim) {
			logger.error("input size is wrong! input size is:" + input.length);
		}

		for (InstanceD instance : instances) {
			double diff = DistanceCalculation.EuclideanDistance(
					instance.getVector(), input);
			logger.info("距离是："+diff);
			inputResult(instance.getType() , diff);
		}
		
		return decideClassID();
	}
	
	public void initDistances()
	{
		size = 0;
		for (int i = 0; i < k; ++i) {
			Distance distance = new Distance(-1, 0);
			distances[i] = distance;
		}
	}

	/**
	 * @comment:这里存在问题，
	 *    如果最后计算出的结果中各类都是相等的，而k又是固定的，因此会造成分类错误
	 * @param classid
	 * @param diff
	 * @return:void
	 */
	public void inputResult(int classid , double diff) {
		++size;
		logger.info(classid + " " + diff);

		for (int i = 0; i < k; ++i) {
			if(distances[i].getClassid() == -1)
			{
				Distance distance = new Distance(classid, diff);
				distances[i] = distance;
				break;
			}
			else if (distances[i].getDistance() > diff) {
				if (i != k - 1) {
					for (int j = k-1; j >i; --j) {
						distances[j].setClassid(distances[j - 1].getClassid()); 
						distances[j].setDistance(distances[j - 1].getDistance()); 
					}
				}
				Distance distance = new Distance(classid, diff);
				distances[i] = distance;
				break;
			}
		}
		showDistance();
	}
	
	/**
	 * @comment:从K个最靠近的结果中选择最多的一个
	 * @return
	 * @return int
	 */
	public int decideClassID()
	{
		//如果结果的数量特别少的，则直接放回最近值
		if(size>0 && size<k)
		{
			return distances[0].getClassid();
		}
		
		//结果数量比较多的，则在k中选择类最多的
		Map<Integer,Integer> results = new HashMap<Integer,Integer>();
		
		for(Distance distance:distances)
		{
			if(distance.getClassid()!= -1 && results.keySet().contains(distance.getClassid()))
			{
				int numb = results.get(distance.getClassid());
				results.put(distance.getClassid(), ++numb);
			}
			else if(distance.getClassid()!= -1)
			{
				results.put(distance.getClassid() , 1);
			}
		}
		
		int resultid = 0;
		int resultNumb = 0;
		
		for(Integer it:results.keySet())
		{
			if(results.get(it)>resultNumb)
			{
				resultid = it;
				resultNumb = results.get(it);
			}
		}
		logger.info("result id: " + resultid);
		return resultid;
	}
	
	public void test()
	{
		k = 9;

		distances = new Distance[k];

		for (int i = 0; i < k; ++i) {
			Distance distance = new Distance(-1, 0);
			distances[i] = distance;
		}
		
		inputResult(1,12);
		inputResult(3,11);
		inputResult(2,3);
		inputResult(2,55);
		inputResult(1,4);
		inputResult(3,2);
		inputResult(2,23);
		inputResult(2,5);
		inputResult(1,4);
		inputResult(3,2);
		inputResult(2,45);
		inputResult(1,4);
		inputResult(3,2);
		
		for(int i=0;i<k;++i)
		{
			logger.info("distances:" + Arrays.toString(distances));
		}
		
		int result = decideClassID();
		logger.info("result:"+result);
	}
	
	public void showDistance()
	{
		for(int i=0;i<k;++i)
		{
			logger.info("distances:"+distances[i].getClassid()+" "+distances[i].getDistance());
		}
	}
	
	public static void main(String[] args) throws Exception
	{
		PropertyConfigurator.configure("log4j.properties");

		KNNInfer knnInfer = new KNNInfer();
		
		knnInfer.test();
	}

}
