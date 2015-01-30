package base;

import java.io.Serializable;

/**
 * @author xiaohe
 * 创建于：2015年1月15日
 * @param <T>
 * 输入给分类器特征可能是整型或者double型
 */
public class Instance implements Serializable {
	protected int length = 0;
	protected int type;//需要修改成int型
	
	
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	
	
}
