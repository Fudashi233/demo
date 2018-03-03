
/*
HMAC 
HMAC(Hash Message Authentication Code��ɢ����Ϣ�����룬������Կ��Hash�㷨����֤Э�顣
��Ϣ������ʵ�ּ����ԭ���ǣ��ù�����������Կ����һ���̶����ȵ�ֵ��Ϊ��֤��ʶ���������ʶ������Ϣ�������ԡ�
ʹ��һ����Կ����һ���̶���С��С���ݿ飬
��MAC����������뵽��Ϣ�У�Ȼ���䡣���շ������뷢�ͷ��������Կ���м�����֤�ȡ�*/
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.cn.comm.Tools;

/**
 * �����������
 */
public abstract class HMAC {
	public static final String KEY_MAC = "HmacMD5";

	/**
	 * ��ʼ��HMAC��Կ
	 * 
	 * @return
	 * @throws Exception
	 */
	public static String initMacKey() throws Exception {
		KeyGenerator keyGenerator = KeyGenerator.getInstance(KEY_MAC);
		SecretKey secretKey = keyGenerator.generateKey();
		return BASE64.encryptBASE64(secretKey.getEncoded());
	}

	/**
	 * HMAC���� ����Ҫ����
	 * 
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String encryptHMAC(byte[] data, String key) throws Exception {

		SecretKey secretKey = new SecretKeySpec(BASE64.decryptBASE64(key), KEY_MAC);
		Mac mac = Mac.getInstance(secretKey.getAlgorithm());
		mac.init(secretKey);
		return new String(mac.doFinal(data));

	}

	public static String getResult1(String inputStr) {
		String path = Tools.getClassPath();
		String fileSource = path + "/file/HMAC_key.txt";
		System.out.println("=======����ǰ������:" + inputStr);
		String result = null;
		try {
			byte[] inputData = inputStr.getBytes();
			String key = HMAC.initMacKey(); /* ������Կ */
			System.out.println("Mac��Կ:===" + key);
			/* ����Կд�ļ� */
			Tools.WriteMyFile(fileSource, key);
			result = HMAC.encryptHMAC(inputData, key);
			System.out.println("HMAC���ܺ�:===" + result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result.toString();
	}

	public static String getResult2(String inputStr) {
		System.out.println("=======����ǰ������:" + inputStr);
		String path = Tools.getClassPath();
		String fileSource = path + "/file/HMAC_key.txt";
		String key = null;
		;
		try {
			/* ����Կ���ļ��ж�ȡ */
			key = Tools.ReadMyFile(fileSource);
			System.out.println("getResult2��Կ:===" + key);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		String result = null;
		try {
			byte[] inputData = inputStr.getBytes();
			/* �����ݽ��м��� */
			result = HMAC.encryptHMAC(inputData, key);
			System.out.println("HMAC���ܺ�:===" + result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result.toString();
	}

	public static void main(String args[]) {
		try {
			String inputStr = "�򵥼���";
			/* ʹ��ͬһ��Կ�������ݽ��м��ܣ��鿴���μ��ܵĽ���Ƿ�һ�� */
			getResult1(inputStr);
			getResult2(inputStr);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}