//package classifier.decision_tree;
//
//import java.util.HashMap;
//
//import base.tree.TreeNode;
//
//
///**
// * @author xiaohe
// * 创建于：2015年1月9日
// * 决策树节点
// */
//public class DecTreeNode extends TreeNode  {
//	
//		private boolean isLeafNode;
//		
//		//这个树节点表示的特征的ID，如果该特诊是页节点则表示类别
//		private int featureID;
//		
//		//当前特征中的那个特征
//		private int valueID;
//		
//		//parent对应的特征id
//		private int parentID;
//		
//		//子节点的数量
//		private Integer childCount;
//		
//		//key 是所在特诊对应的特征值的id
//		private  HashMap<Integer, DecTreeNode> childNodes = new HashMap<Integer, DecTreeNode>();
//		
//		public DecTreeNode() {
//			this.childCount = 0;
//		}
//		
//		public boolean isLeafNode() {
//			return isLeafNode;
//		}
//
//		public void setLeafNode(boolean isLeafNode) {
//			this.isLeafNode = isLeafNode;
//		}
//
//		public int getFeatureID() {
//			return featureID;
//		}
//
//		public void setFeatureID(int featureID) {
//			this.featureID = featureID;
//		}
//
//		public int getValueID() {
//			return valueID;
//		}
//
//		public void setValueID(int valueID) {
//			this.valueID = valueID;
//		}
//
//		public int getParentID() {
//			return parentID;
//		}
//
//		public void setParentID(int parentID) {
//			this.parentID = parentID;
//		}
//
//		public Integer getChildCount() {
//			return childCount;
//		}
//
//		public void setChildCount(Integer childCount) {
//			this.childCount = childCount;
//		}
//
//
//
//		public HashMap<Integer, DecTreeNode> getChildNodes() {
//			return childNodes;
//		}
//
//
//
//		public void setChildNodes(HashMap<Integer, DecTreeNode> childNodes) {
//			this.childNodes = childNodes;
//		}
//
//
//
//		public void addChileNodes(Integer id,DecTreeNode node)
//		{
//			childNodes.put(id, node);
//		}
//		
//		public String toString()
//		{
//			String str = "featureID:"+featureID+" valueID:"+valueID+" parentID:"+parentID;
//			return str;
//		}
//		
//}
