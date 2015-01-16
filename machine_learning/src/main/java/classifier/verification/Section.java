package classifier.verification;

/**
 * @author xiaohe
 * 2015年1月16日
 * 用来记录测试数据在整个数据中的区间，可以用在交叉验证中
 */
public class Section {
	int start;
	int end;
	
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getEnd() {
		return end;
	}
	public void setEnd(int end) {
		this.end = end;
	}

}
