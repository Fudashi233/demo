import java.lang.reflect.Method;


public class UseCaseTracker
{
	public static void main(String[] args)
	{
		UseCaseTracker tracker = new UseCaseTracker(Util.class);
		tracker.printStackTrace();
	}
	private Class<?> classX;
	UseCaseTracker(Class<?> classX)
	{
		this.classX = classX;
	}
	public void printStackTrace()
	{
		Method[] methods = classX.getDeclaredMethods();
		UseCase useCase = null;
		for(int i=0;i<methods.length;i++)
		{
			useCase = methods[i].getAnnotation(UseCase.class);
			if(!"".equals(useCase.description().trim()))
				System.out.println(useCase.id()+"		"+useCase.description());
		}
	}
}
