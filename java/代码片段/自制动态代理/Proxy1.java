package cn.edu.jxau.test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;

import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

/**
 * 为任意的接口生成指定的代理类
 * 
 * @author Fudashi
 */
public class Proxy1 {

	public static void main(String[] args)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException, IOException {

		Say say = (Say) Proxy1.newProxyInstance(Say.class);
		say.say();
	}

	private Proxy1() {
		throw new UnsupportedOperationException("不可实例化Proxy1");
	}

	public static Object newProxyInstance(Class<?> interfaz)
			throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {

		StringBuilder builder = new StringBuilder();
		builder.append("package cn.edu.jxau.test;\r\n");
		builder.append("\r\n");
		builder.append(String.format("public class StaticProxy implements %s {\r\n", interfaz.getSimpleName()));
		builder.append("\r\n");
		builder.append(String.format("private %s real;\r\n", interfaz.getSimpleName()));
		builder.append("\r\n");
		builder.append(String.format("public StaticProxy(%s real) {\r\n", interfaz.getSimpleName()));
		builder.append("this.real = real;\r\n");
		builder.append("}\r\n");
		Method[] methodArr = interfaz.getDeclaredMethods();
		for (Method method : methodArr) {
			builder.append(String.format("public %s %s() {\r\n", method.getReturnType(), method.getName()));
			builder.append("\r\n");
			builder.append("System.out.println(\"before\");\r\n");
			builder.append(String.format("real.%s();\r\n", method.getName()));
			builder.append("System.out.println(\"after\");\r\n");
			builder.append("}\r\n");
			builder.append("\r\n");
		}
		builder.append("}");
		System.out.println(builder.toString());

		File file = new File(System.getProperty("java.io.tmpdir"), "src/cn/edu/jxau/test/StaticProxy.java");
		System.out.println(file.getCanonicalFile());
		try (FileWriter writer = new FileWriter(file)) {
			writer.write(builder.toString());
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
