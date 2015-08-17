//package classifier.decision_tree;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//import org.apache.log4j.PropertyConfigurator;
//
//import vsm.VSM;
//
//public class SplitVSM {
//	
//	/**
//	 * @comment:
//	 * @param vsms  输入的需要提取子特征的特征向量
//	 * @param index  需要去掉的特征所在的维度
//	 * @param featureValue  特诊所对应的值
//	 * @return
//	 * @return:List<VSM>
//	 */
//	public static  List<VSM> slecetSubVSM(List<VSM> vsms,int index,double featureValue,List<Integer> title)
//	{
//		int breSize = VSM.size;
//		List<VSM> resultVSM = new ArrayList<VSM>();
//		
//		for(VSM vsm:vsms)
//		{
//			if(vsm.getFeature(index) == featureValue)
//			{
//				int type = vsm.getType();
//				double[] tempVector = vsm.getVector();
//				double[] resultVector = new double[tempVector.length - 1];
//				
//				int j=0;
//				for(int i=0;i<tempVector.length;++i)
//				{
//					if(i != index)
//					{
//						resultVector[j] = tempVector[i];
//						++j;
//					}
//				}
//				VSM tempVSM = new VSM(type,resultVector);
//				resultVSM.add(tempVSM);
//			}
//		}
//		
//		title.remove(index);
//		
//		VSM.size = breSize - 1;
//		return resultVSM;
//	}
//	
//	
//	public static void main(String[] args)
//	{
//        PropertyConfigurator.configure("log4j.properties");
//		
//		double[] x1 = {1,0,0,0};
//		double[] x2 = {1,0,0,1};
//		double[] x3 = {1,1,0,1};
//		double[] x4 = {1,1,1,0};
//		
//		VSM.setSize(4);
//		VSM vsm1 = new VSM(0,x1);
//		VSM vsm2 = new VSM(0,x2);
//		VSM vsm3 = new VSM(1,x3);
//		VSM vsm4 = new VSM(1,x4);
//		
//		List<VSM> vsms = new ArrayList<VSM>();
//		vsms.add(vsm1);
//		vsms.add(vsm2);
//		vsms.add(vsm3);
//		vsms.add(vsm4);
//		
//		SplitVSM splitVSM = new SplitVSM();
//		Integer[] titleArray = {0,1,2,3};
//		List<Integer> title = new ArrayList<Integer>(Arrays.asList(titleArray));
//		List<VSM> result =  splitVSM.slecetSubVSM(vsms,1,1,title);
//		
//		System.out.println(result);
//		System.out.println(title);
//		
//	}
//
//}
