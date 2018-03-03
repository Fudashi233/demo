

import java.math.BigInteger;
import java.security.MessageDigest;
/*
MD5(Message Digest algorithm 5����ϢժҪ�㷨) 
ͨ�����ǲ�ֱ��ʹ������MD5���ܡ�ͨ����MD5�������ֽ����齻��BASE64�ټ���һ�ѣ��õ���Ӧ���ַ���
Digest:���
*/
public class MD5 {
	public static final String KEY_MD5 = "MD5";
	
	public static String getResult(String inputStr)
	{
		System.out.println("=======����ǰ������:" + inputStr);
		BigInteger bigInteger = null;
		try {
			MessageDigest md = MessageDigest.getInstance(KEY_MD5);
			byte[] inputData = inputStr.getBytes();
			md.update(inputData);
			bigInteger = new BigInteger(md.digest());
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("MD5���ܺ�:" + bigInteger.toString(16));
		return bigInteger.toString(16);
	}

	public static void main(String args[]) 
	{
		try 
		{
			String inputStr = "�򵥼���8888888888888888888";
			getResult(inputStr);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

}