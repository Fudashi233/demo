package cn.edu.jxau.test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import javax.tools.JavaCompiler.CompilationTask;

/**
 * 为任意的接口生成任意的代理类
 * @author Fudashi
 */
public class Proxy2 {

	public static void main(String[] args)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException, IOException {

		Say say = (Say) Proxy2.newProxyInstance(Say.class, new Handler() {

			@Override
			public Object invoke(Object instance, Method method)
					throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {

				System.out.println("before");
				System.out.println("invoke "+method.getName());
				System.out.println("after");
				return null;
			}
		});
		say.say();
	}

	private Proxy2() {
		throw new UnsupportedOperationException("不可实例化Proxy1");
	}

	public static Object newProxyInstance(Class<?> interfaz, Handler h)
			throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {

		StringBuilder builder = new StringBuilder();
		builder.append("package cn.edu.jxau.test;\r\n");
		builder.append("\r\n");
		builder.append("import java.lang.reflect.Method;\r\n");
		builder.append(String.format("public class $Proxy implements %s {\r\n", interfaz.getSimpleName()));
		builder.append("\r\n");
		builder.append("private Handler h;\r\n");
		builder.append("\r\n");
		builder.append("public $Proxy(Handler h) {\r\n");
		builder.append("this.h = h;\r\n");
		builder.append("}\r\n");
		Method[] methodArr = interfaz.getDeclaredMethods();
		for (Method method : methodArr) {
			builder.append(String.format("public %s %s() {\r\n", method.getReturnType(), method.getName()));
			builder.append("\r\n");
			builder.append("try {\r\n");
			builder.append("Class<?> clazz = Class.forName(\"" + interfaz.getName() + "\");\r\n");
			builder.append("Method m = clazz.getDeclaredMethod(\"" + method.getName() + "\");\r\n");
			builder.append("h.invoke(this,m);\r\n");
			builder.append("} catch(Exception e) {\r\n");
			builder.append("e.printStackTrace();\r\n");
			builder.append("}");
			builder.append("}\r\n");
			builder.append("\r\n");
		}
		builder.append("}");

		File file = new File(System.getProperty("java.io.tmpdir"), "src/cn/edu/jxau/test/$Proxy.java");
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
		Class<?> clazz = loader.loadClass("cn.edu.jxau.test.$Proxy");
		loader.close();
		return clazz.getConstructor(Handler.class).newInstance(h);
	}

}