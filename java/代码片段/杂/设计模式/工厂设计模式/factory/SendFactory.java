//http://www.cnblogs.com/zhangchenliang/p/3700820.html
//http://blog.csdn.net/zhengzhb/article/details/7359385/
//http://haolloyin.blog.51cto.com/1177454/332802
package factory;















/*LV6 简单工厂配合反射改进抽象工厂
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









/*LV5 抽象工厂模式
 * 
 * 等级结构：工厂方法模式提供的一系列产品称为一个等级结构(Email与SMS)
 * 产品族：指位于不同产品等级结构中，功能相关联的产品组成的家族(Email的EmailX/EmailY;SMS的SMSX/SMSY)
 * 等级结构-产品族：产品族是位于不同等级结构下的一个集合
 * 缺点：拓展产品族是一件十分费力的事，增加几个新产品，几乎所有的工厂类都要修改
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








/*LV4 工厂方法模式
 * 
 * 普通工厂模式违反了开闭原则，每次需要拓展时就需要修改代码
 * 而工厂方法模式就不同，如果新增了Sender子类，只需相应增加一个SendFactory子类
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
 * LV3 简单工厂模式-多个静态方法
 * 这种方式的工厂模式采用每个子类编写的一个produce方法的方式
 * 相对于LV2，可以不用实例化工厂类就可使用produce方法，方便一点
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
 * LV2 简单工厂模式-多个方法
 * 详情见LV3
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
 * LV1 简单工厂模式-普通 
 * 这种方式的工厂设计模式，依靠传送的cmd来判断创建哪种实例
 * 一旦输入错误的cmd就GG了
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
