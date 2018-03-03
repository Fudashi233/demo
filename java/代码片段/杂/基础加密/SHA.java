
import java.math.BigInteger;
import java.security.MessageDigest;

/*
SHA(Secure Hash Algorithm����ȫɢ���㷨��������ǩ��������ѧӦ������Ҫ�Ĺ��ߣ�
���㷺��Ӧ���ڵ����������Ϣ��ȫ������Ȼ��SHA��MD5ͨ����ײ�������ƽ��ˣ� 
����SHA��Ȼ�ǹ��ϵİ�ȫ�����㷨����֮MD5��Ϊ��ȫ*/
public class SHA {
     public static final String KEY_SHA = "SHA";   

    public static  String  getResult(String inputStr)
    {
        BigInteger sha =null;
        System.out.println("=======����ǰ������:"+inputStr);
        byte[] inputData = inputStr.getBytes();   
        try 
        {
             MessageDigest messageDigest = MessageDigest.getInstance(KEY_SHA);  
             messageDigest.update(inputData);
             sha = new BigInteger(messageDigest.digest());   
             System.out.println("SHA���ܺ�:" + sha.toString(32));   
        }
        catch (Exception e) {e.printStackTrace();}
        return sha.toString(32);
    }

    public static void main(String args[])
    {
        try 
        {
             String inputStr = "�򵥼���";   
             getResult(inputStr);
        } 
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

}