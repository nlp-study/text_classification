//package classifier.decision_tree;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//import org.apache.log4j.Logger;
//import org.apache.log4j.PropertyConfigurator;
//
//import vsm.VSM;
//import vsm.VSMtransformer;
//
///**
// * @author xiaohe
// * 创建于：2015年1月12日
// * 按照李航書已經基本完成，和文本結合的還沒有處理，沒有剪枝操作
// * 算法有瑕疵，但願之後能重構這個分類器
// */
//public class DecTreeBuilder {
//	Logger logger = Logger.getLogger(DecTreeBuilder.class);
//	
//	int dim = 0;
//	
//	//提取出每个特诊有几个取值
//	private double[][] features;
//	
//	List<Integer> title = new ArrayList<Integer>();
//	
//	public int getDim() {
//		return dim;
//	}
//
//	public void setDim(int dim) {
//		this.dim = dim;
//	}
//
//	public double[][] getFeatures() {
//		return features;
//	}
//
//	public void setFeatures(double[][] features) {
//		this.features = features;
//	}
//
//	public List<Integer> getTitle() {
//		return title;
//	}
//
//	public void setTitle(List<Integer> title) {
//		this.title = title;
//	}
//
//	public void init(List<VSM> vsms)
//	{
//		if(vsms.size()>0)
//		{
//			dim = vsms.get(0).getSize();
//		}
//		else
//		{
//			logger.error("输入的向量为空！");
//		}
//		
//		for(int i=0;i<dim;++i)
//		{
//			title.add(i);
//		}
//		
//		FeatureDoubleToInt vsmFromDoubleToInt = new FeatureDoubleToInt(vsms);
//		vsmFromDoubleToInt.excute();
//		features = vsmFromDoubleToInt.getFeatures();
//	}
//	
//	public DecTreeNode build(List<VSM> vsms,List<Integer> tempTitle,int parentID,int valueID )
//	{
//		DecTreeNode root = new DecTreeNode();
//		InformationGainRatio informationGainRatio = new InformationGainRatio(vsms);
//		logger.info("vsms:"+vsms.toString());
//		int tempDim = informationGainRatio.slectMaxGain();
//		logger.info("tempDim:"+tempDim);
//		
//		int[][][] featureCalssNumb = informationGainRatio.getFeatureCalssNumb();
//		double[][] tempFeatures = informationGainRatio.getFeatures();
//		logger.info("Features:"+Arrays.deepToString(tempFeatures));
//		logger.info("featureCalssNumb:"+Arrays.deepToString(featureCalssNumb));
//		
//		int currenFeatureID = tempTitle.get(tempDim);
//		int childCount = tempFeatures[currenFeatureID].length;
//		
//		root.setLeafNode(false);
//		root.setParentID(parentID);
//		root.setChildCount(childCount);
//		root.setFeatureID(currenFeatureID);
//		root.setValueID(valueID);
//		
//		for(int i=0;i<childCount;++i)
//		{
//			logger.info("class legth:"+featureCalssNumb[tempDim][i].length+" i:"+i+" childCount"+childCount);
//			if(isNoChild(featureCalssNumb,tempDim,i))
//			{
//				logger.info("leaf!");
//				DecTreeNode leaf = new DecTreeNode();
//				int classid = getClassID(vsms,tempDim,i,tempFeatures);
//				leaf.setFeatureID(classid);
//				leaf.setParentID(currenFeatureID);
//				leaf.setLeafNode(true);
//				leaf.setValueID(i);
//				leaf.setChildCount(0);
//				leaf.setChildNodes(null);
//				root.addChileNodes(i, leaf);
//			}
//			else
//			{
//				double childVaule = tempFeatures[tempDim][i];
//				List<Integer> childTitle = new ArrayList<Integer>();
//				childTitle.addAll(tempTitle);
//				List<VSM> tempVsms = SplitVSM.slecetSubVSM(vsms,tempDim,childVaule, childTitle);
//				logger.info("Is notleaf:"+tempVsms);
//				logger.info("featureCalssNumb:"+featureCalssNumb[tempDim][i].length+" tempDim:"+tempDim+" i:"+i);
//				DecTreeNode ChildNode = build(tempVsms,childTitle,currenFeatureID,i);
//				root.addChileNodes(i, ChildNode);
//			}
//		}
//		
//		return root;
//	}
//	
//	public int getClassID(List<VSM> vsms,int tempDim,int valueID,double[][] tempFetures)
//	{
//		double value = tempFetures[tempDim][valueID];
//		
//		for(VSM vsm:vsms)
//		{
//			if(vsm.getFeature(tempDim) == value)
//			{
//				return vsm.getType();
//			}
//		}
//		
//		return -1;
//	}
//	
//	public boolean isNoChild(int[][][] featureCalssNumb,int tempDim,int tempFetureID)
//	{
//		if(featureCalssNumb[tempDim][tempFetureID].length == 1)
//		{
//			return true;
//		}
//		
//		int numb = 0;
//		for(int i=0;i<featureCalssNumb[tempDim][tempFetureID].length;++i)
//		{
//			if(featureCalssNumb[tempDim][tempFetureID][i] != 0)
//			{
//				++numb;
//			}
//		}
//		
//		if(numb == 1)
//		{
//			return true;
//		}
//		
//		return false;
//	}
//	
//	public void showTree(DecTreeNode node,int showStar)
//	{
//		for(int j=0;j<showStar;++j)
//		{
//			System.out.print(" ");
//		}
//		System.out.println(node);
//		
//		if(node.getChildNodes() == null)
//		{
//			return;
//		}
//
//		for(int i=0;i<node.getChildNodes().size();++i)
//		{
//			showTree(node.getChildNodes().get(i),showStar+5);
//		}		
//	}
//	
//	public static void main(String[] args)
//	{
//        PropertyConfigurator.configure("log4j.properties");
//
//		double[] x1 = {1,0,0,0};
//		double[] x2 = {1,0,0,1};
//		double[] x3 = {1,1,0,1};
//		double[] x4 = {1,1,1,0};
//		double[] x5 = {1,0,0,0};
//		double[] x6 = {2,0,0,0};
//		double[] x7 = {2,0,0,1};
//		double[] x8 = {2,1,1,1};
//		double[] x9 = {2,0,1,2};
//		double[] x10 = {2,0,1,2};
//		double[] x11 = {3,0,1,2};
//		double[] x12 = {3,0,1,1};
//		double[] x13 = {3,1,0,1};
//		double[] x14 = {3,1,0,2};
//		double[] x15 = {3,0,0,0};
//		
//		VSM.setSize(4);
//		VSM vsm1 = new VSM(0,x1);
//		VSM vsm2 = new VSM(0,x2);
//		VSM vsm3 = new VSM(1,x3);
//		VSM vsm4 = new VSM(1,x4);
//		VSM vsm5 = new VSM(0,x5);
//		VSM vsm6 = new VSM(0,x6);
//		VSM vsm7 = new VSM(0,x7);
//		VSM vsm8 = new VSM(1,x8);
//		VSM vsm9 = new VSM(1,x9);
//		VSM vsm10 = new VSM(1,x10);
//		VSM vsm11 = new VSM(1,x11);
//		VSM vsm12 = new VSM(1,x12);
//		VSM vsm13 = new VSM(1,x13);
//		VSM vsm14 = new VSM(1,x14);
//		VSM vsm15 = new VSM(0,x15);
//		
//		List<VSM> vsms = new ArrayList<VSM>();
//		vsms.add(vsm1);
//		vsms.add(vsm2);
//		vsms.add(vsm3);
//		vsms.add(vsm4);
//		vsms.add(vsm5);
//		vsms.add(vsm6);
//		vsms.add(vsm7);
//		vsms.add(vsm8);
//		vsms.add(vsm9);
//		vsms.add(vsm10);
//		vsms.add(vsm11);
//		vsms.add(vsm12);
//		vsms.add(vsm13);
//		vsms.add(vsm14);
//		vsms.add(vsm15);
//		
//		DecTreeBuilder decTreeBuilder = new DecTreeBuilder();
//		decTreeBuilder.init(vsms);
//		DecTreeNode node = decTreeBuilder.build(vsms, decTreeBuilder.getTitle(), 0, 0);
//		decTreeBuilder.showTree(node,0);
//	}
//}
