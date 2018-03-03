package cn.edu.jxau.utils;

import java.io.File;
import java.io.FilenameFilter;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

public class ScanClassUtil {

	private String[] packageNames;
	public ScanClassUtil() {
		
		
	}
	public ScanClassUtil(String...packageNames) {
		
		this.packageNames = packageNames;
	}
	public Set<Class<?>> scanClass() {
		
		String classPath = this.getClassPath();
		Set<Class<?>> classSet = new HashSet<Class<?>>();
		String packagePath = "";
		File packageFile = null;
		for(int i=0;i<packageNames.length;i++) {
			
			packagePath = getPackagePath(classPath,packageNames[i]);
			packageFile = new File(packagePath);
			if(packageFile.exists()) {
				
				addClass(classSet,packageFile,packageNames[i],"");
			}
		}

		return classSet;
	}
	
	/**
	 * 递归寻找指定路径下的字节码文件
	 * 
	 * @param set	Class类添加到这个set中
	 * @param dir	指定的路径
	 * @param packageName	包名
	 * @param subDirName	路径下的子文件夹名
	 */
	private void addClass(Set<Class<?>> set,File dir,String packageName,String subDirName) {
		
		File[] dirs = dir.listFiles();
		try {
			
			for(int i=0;i<dirs.length;i++) {
				
				if(dirs[i].isDirectory()) {	//如果这个direcion是个文件夹，则递归进入文件夹，继续查找
					
					addClass(set,dirs[i],packageName,dirs[i].getName());
				}
				String filename = dirs[i].getName();
				if(filename.matches(".+\\.class$")) {	//确保是.class文件
					
					String className = filename.substring(0,filename.indexOf(".class"));	//获取类名
					if(subDirName==null || "".equals(subDirName.trim())) {
						
						set.add(Class.forName(packageName+"."+className));
					} else {
						
						set.add(Class.forName(packageName+"."+subDirName+"."+className));
					}
				}
				
			}
		} catch(ClassNotFoundException ex) {
			
			ex.printStackTrace();
		}
		
	}
	
	
	/**
	 * 获取类字节码路径
	 * 
	 * @return 工程字节码路径
	 */
	private String getClassPath() {
		
		return ScanClassUtil.class.getClassLoader().getResource("").getPath();
	}
	
	/**
	 * 根据字节码路径和包名构造一个路径，指向指定的包
	 * 
	 * @return 指定包的路径
	 */
	private String getPackagePath(String classPath,String packageName) {
		
		String packagePath = packageName.replace(".","/");
		return classPath+packagePath;
	}
}
