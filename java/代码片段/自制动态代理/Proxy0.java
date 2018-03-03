package cn.edu.jxau.test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;

import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

/**
 * 为指定的接口生成指定的代理类
 * @author Fudashi
 */
public class Proxy0 {

	public static void main(String[] args)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException, IOException {

		Say say = (Say) Proxy0.newProxyInstance();
		say.say();
	}

	private Proxy0() {
		throw new UnsupportedOperationException("不可实例化Proxy0");
	}

	public static Object newProxyInstance()
			throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {

		String src = "package cn.edu.jxau.test;\r\n" + 
				"\r\n" + 
				"public class StaticProxy implements Say {\r\n" + 
				"\r\n" + 
				"	private Say real;\r\n" + 
				"\r\n" + 
				"	public StaticProxy(Say real) {\r\n" + 
				"		this.real = real;\r\n" + 
				"	}\r\n" + 
				"\r\n" + 
				"	@Override\r\n" + 
				"	public void say() {\r\n" + 
				"		\r\n" + 
				"		System.out.println(\"before\");\r\n" + 
				"		real.say();\r\n" + 
				"		System.out.println(\"after\");\r\n" + 
				"	}\r\n" + 
				"	\r\n" + 
				"	public void bindTo(Say real) {\r\n" + 
				"		this.real = real;\r\n" + 
				"	}\r\n" + 
				"}\r\n";
		
		File file = new File(System.getProperty("java.io.tmpdir"), "src/cn/edu/jxau/test/StaticProxy.java");
		System.out.println(file.getCanonicalFile());
		try (FileWriter writer = new FileWriter(file)) {
			writer.write(src);
		}

		// 动态编译这段Java代码,生成.class文件//
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		StandardJavaFileManager sjfm = compiler.getStandardFileManager(null, null, null);
		Iterable<? extends JavaFileObject> iter = sjfm.getJavaFileObjects(file);
		CompilationTask ct = compiler.getTask(null, sjfm, null, null, null, iter);
		ct.call();
		sjfm.close();

		// 加载代理类的字节码文件，生成代理类实例 //
		URL[] urlArr = new URL[] { new File(System.getProperty("java.io.tmpdir"), "src").toURI().toURL() };
		System.out.println(urlArr[0]);
		URLClassLoader loader = new URLClassLoader(urlArr);
		Class<?> clazz = loader.loadClass("cn.edu.jxau.test.StaticProxy");
		loader.close();
		return clazz.getConstructor(Say.class).newInstance(new Person());
	}
}
