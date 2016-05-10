import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by simon on 2016-04-01.
 */
public class StringToMD5 {
    static public String getMD5(String target){
        String MD5 = "";
        try{
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(target.getBytes());
            byte byteDate[] = md.digest();
            StringBuffer sb = new StringBuffer();
            for(int i=0; i<byteDate.length; i++)
                sb.append(Integer.toString((byteDate[i]&0xff) + 0x100, 16).substring(1));
            MD5 = sb.toString();
        } catch (NoSuchAlgorithmException e){
            e.printStackTrace();
            MD5 = null;
        }
        return MD5;
    }
}
