package validation.slice;

import java.util.List;

/**
 * @author xiaohe
 * 2015年1月18日
 * 产生交叉验证的接口
 */
public interface DataSlice {
	public void excute();	
	public List<ValidationID> getVerificationIDs();

}
