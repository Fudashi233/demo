import java.io.File;
import java.io.FilenameFilter;
import java.util.regex.Pattern;

public class Directory 
{
	public void local(File dir)
	{
		local(dir,".*");
	}
	public void local(File dir,String regex)
	{
		FilenameFilter filter = new FilenameFilter(){
			
			public boolean accept(File dir,String filename)
			{
				return filename.matches(regex);		//filename是文件名,不能单纯的拿dir.getName()来使用
			}
			
		};
		File[] dirArr = dir.listFiles(filter);
		for(int i=0;i<dirArr.length;i++)
		{
			System.out.println(dirArr[i].getName());
		}
	}
	public void walk(File dir)
	{
		walk(dir,".*",0);
	}
	public void walk(File dir,String regex,int n)
	{
		File[] fileArr = dir.listFiles();
		for(int i=0;i<fileArr.length;i++)
		{
			if(fileArr[i].isDirectory())
			{
				for(int j=0;j<n;j++)
					System.out.print("--- ");
				System.out.println(fileArr[i]);
				n++;
				walk(fileArr[i],regex,n);
				n--;
			}
			else
			{
				if(fileArr[i].getName().matches(regex))
				{
					for(int j=0;j<n;j++)
						System.out.print("--- ");
					System.out.println(fileArr[i].getName());
				}
			}
		}
	}
	public static void main(String[] args)
	{
		File dir = new File(System.getProperty("user.dir"));
		new Directory().walk(dir);
	}
}