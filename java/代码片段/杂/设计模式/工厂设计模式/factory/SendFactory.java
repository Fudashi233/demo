//http://www.cnblogs.com/zhangchenliang/p/3700820.html
//http://blog.csdn.net/zhengzhb/article/details/7359385/
//http://haolloyin.blog.51cto.com/1177454/332802
package factory;















/*LV6 �򵥹�����Ϸ���Ľ����󹤳�
 * 
 * 
 * 
 * */

class EmailX extends Email 
{
	@Override
	public void send() 
	{
		System.out.println("I am EmailX");
	}
}

class EmailY extends Email 
{
	@Override
	public void send() 
	{
		System.out.println("I am EmailY");
	}
}

class SMSX extends SMS 
{
	@Override
	public void send() 
	{
		System.out.println("I am SMSX");
	}
}
class SMSY extends SMS 
{
	@Override
	public void send() 
	{
		System.out.println("I am SMSY");
	}
}
public class SendFactory
{
	public static Email produceEmail(String email) throws InstantiationException, IllegalAccessException, ClassNotFoundException
	{
		return (Email)Class.forName(email).newInstance();
	}
	public static SMS produceSMS(String SMS) throws InstantiationException,IllegalAccessException,ClassNotFoundException
	{
		return (SMS)Class.forName(SMS).newInstance();
	}
	public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException
	{
		Email email = SendFactory.produceEmail("factory.SMS");
		email.send();
	}
}









/*LV5 ���󹤳�ģʽ
 * 
 * �ȼ��ṹ����������ģʽ�ṩ��һϵ�в�Ʒ��Ϊһ���ȼ��ṹ(Email��SMS)
 * ��Ʒ�壺ָλ�ڲ�ͬ��Ʒ�ȼ��ṹ�У�����������Ĳ�Ʒ��ɵļ���(Email��EmailX/EmailY;SMS��SMSX/SMSY)
 * �ȼ��ṹ-��Ʒ�壺��Ʒ����λ�ڲ�ͬ�ȼ��ṹ�µ�һ������
 * ȱ�㣺��չ��Ʒ����һ��ʮ�ַ������£����Ӽ����²�Ʒ���������еĹ����඼Ҫ�޸�
 * 
 * */
//class EmailX extends Email 
//{
//	@Override
//	public void send() 
//	{
//		System.out.println("I am EmailX");
//	}
//}
//
//class EmailY extends Email 
//{
//	@Override
//	public void send() 
//	{
//		System.out.println("I am EmailY");
//	}
//}
//
//class SMSX extends SMS 
//{
//	@Override
//	public void send() 
//	{
//		System.out.println("I am SMSX");
//	}
//}
//class SMSY extends SMS 
//{
//	@Override
//	public void send() 
//	{
//		System.out.println("I am SMSY");
//	}
//}
//interface XFactory
//{
//	public Email produceEmail();
//	public SMS produceSMS();
//}
//interface YFactory
//{
//	public Email produceEmail();
//	public SMS produceSMS();
//}
//class XFactoryImpl implements XFactory
//{
//	@Override
//	public Email produceEmail()
//	{
//		return new EmailX();
//	}
//	@Override 
//	public SMS produceSMS()
//	{
//		return new SMSX();
//	}
//}
//class YFactoryImpl implements YFactory
//{
//	@Override
//	public Email produceEmail()
//	{
//		return new EmailY();
//	}
//	@Override
//	public SMS produceSMS()
//	{
//		return new SMSY();
//	}
//}
//public class SendFactory
//{
//	public static void main(String[] args)
//	{
//		Email e = new YFactoryImpl().produceEmail();
//		e.send();
//	}
//}








/*LV4 ��������ģʽ
 * 
 * ��ͨ����ģʽΥ���˿���ԭ��ÿ����Ҫ��չʱ����Ҫ�޸Ĵ���
 * ����������ģʽ�Ͳ�ͬ�����������Sender���ֻ࣬����Ӧ����һ��SendFactory����
 * 
 * */

//class SMSFactory implements SendFactory
//{
//	@Override
//	public Sender produce()
//	{
//		return new SMS();
//	}
//}
//class EmailFactory implements SendFactory
//{
//	@Override
//	public Sender produce()
//	{
//		return new Email();
//	}
//}
//
//public interface SendFactory
//{
//	public Sender produce();
//}

















/*
 * LV3 �򵥹���ģʽ-�����̬����
 * ���ַ�ʽ�Ĺ���ģʽ����ÿ�������д��һ��produce�����ķ�ʽ
 * �����LV2�����Բ���ʵ����������Ϳ�ʹ��produce����������һ��
 * */

//public class SendFactory
//{
//	public static Sender produceEmail()
//	{
//		return new Email();
//	}
//	public static Sender produceSMS()
//	{
//		return new SMS();
//	}
//	public static void main(String[] args)
//	{
//		Sender email = SendFactory.produceEmail();
//		email.send();
//	}
//}





/*
 * LV2 �򵥹���ģʽ-�������
 * �����LV3
 * */

//public class SendFactory
//{
//	public SendFactory()
//	{
//		
//	}
//	public Sender produceEmail()
//	{
//		return new Email();
//	}
//	public Sender produceSMS()
//	{
//		return new SMS();
//	}
//	public static void main(String[] args)
//	{
//		Sender e = new SendFactory().produceSMS();
//		e.send();
//	}
//}




/*
 * LV1 �򵥹���ģʽ-��ͨ 
 * ���ַ�ʽ�Ĺ������ģʽ���������͵�cmd���жϴ�������ʵ��
 * һ����������cmd��GG��
 * */

//public class SendFactory 
//{
//	public SendFactory()
//	{
//		
//	}	
//	public Sender newInstance(String cmd)
//	{
//		if("email".equals(cmd.trim()))
//		{
//			return new Email();
//		}
//		else if("SMS".equals(cmd.trim()))
//		{
//			return new SMS();
//		}
//		else
//		{
//			return null;
//		}
//	}
//	public static void main(String[] args)
//	{
//		Sender e = new SendFactory().newInstance("email");
//		e.send();
//	}
//}
