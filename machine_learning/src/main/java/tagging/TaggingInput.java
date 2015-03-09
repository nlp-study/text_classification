package tagging;

import java.io.Serializable;

/**
 * @author xiaohe
 * 创建于：2015年3月9日
 * 序列标注的输入
 */
public class TaggingInput implements Serializable{
	/**
	 * 标记，这里用标记的id来表示,0~n
	 */
	int tagging;
	
	/**
	 * 词语的id,0~m
	 */
	int word;

	public int getTagging() {
		return tagging;
	}

	public void setTagging(int tagging) {
		this.tagging = tagging;
	}

	public int getWord() {
		return word;
	}

	public void setWord(int word) {
		this.word = word;
	}
}
