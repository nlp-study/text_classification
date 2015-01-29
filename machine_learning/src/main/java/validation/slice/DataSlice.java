package validation.slice;

import java.util.List;

import base.InputFeatureD;

/**
 * @author xiaohe
 * 2015年1月18日
 * 产生交叉验证的接口
 */
public interface DataSlice {
	public void init(InputFeatureD inputFeature);
	public void excute();	
	public List<ValidationID> getVerificationIDs();

}
