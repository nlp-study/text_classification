package base;

import java.io.UnsupportedEncodingException;

public class CodePrase {
	public static final String full2HalfChange(String QJstr)  
            throws UnsupportedEncodingException {  
  
        StringBuffer outStrBuf = new StringBuffer("");  
  
        String Tstr = "";  
  
        byte[] b = null;  
  
        for (int i = 0; i < QJstr.length(); i++) {  
  
            Tstr = QJstr.substring(i, i + 1);  
  
            // 全角空格转换成半角空格  
  
            if (Tstr.equals("　")) {  
  
                outStrBuf.append(" ");  
  
                continue;  
  
            }  
  
            b = Tstr.getBytes("unicode");  
  
            // 得到 unicode 字节数据  
  
            if (b[2] == -1) {  
  
                // 表示全角？  
  
                b[3] = (byte) (b[3] + 32);  
  
                b[2] = 0;  
  
                outStrBuf.append(new String(b, "unicode"));  
  
            } else {  
  
                outStrBuf.append(Tstr);  
  
            }  
  
        } // end for.  
  
        return outStrBuf.toString();    
    }  

}
