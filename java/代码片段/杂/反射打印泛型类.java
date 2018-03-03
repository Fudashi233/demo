import java.applet.Applet;
import java.lang.reflect.*;

import java.util.*;

public class Test {

	public static void main(String[] args) throws NoSuchFieldException, SecurityException 
	{
	    Field c = Pair.class.getField("first");  
	    System.out.println(c.toGenericString());  
	}
	public static void printClass(Class<?> cl) {

		System.out.print(cl);	//class Pair

		printTypes(cl.getTypeParameters(), "<", ", ", ">", true);	//打印类型参数

		Type sc = cl.getGenericSuperclass();

		if (sc != null) {

			System.out.print(" extends ");

			printType(sc, false);

		}
		printTypes(cl.getGenericInterfaces(), " implements ", ", ", "", false);

		System.out.println();

	}

	public static void printTypes(Type[] types, String pre, String sep,

	String suf, boolean isDefinition) {

		if (pre.equals(" extends ") && Arrays.equals(types, new Type[] { Object.class }))

			return;

		if (types.length > 0)

			System.out.print(pre);

		for (int i = 0; i < types.length; i++) {

			if (i > 0)

				System.out.print(sep);

			printType(types[i], isDefinition);

		}

		if (types.length > 0)

			System.out.print(suf);

	}

	public static void printType(Type type, boolean isDefinition) 
	{

		if (type instanceof Class) {

			Class<?> t = (Class<?>) type;

			System.out.print(t.getName());

		} else if (type instanceof TypeVariable) {

			TypeVariable<?> t = (TypeVariable<?>) type;

			System.out.print(t.getName());

			if (isDefinition)

				printTypes(t.getBounds(), " extends ", " & ", "", false);

		} else if (type instanceof WildcardType) {

			WildcardType t = (WildcardType) type;

			System.out.print("?");

			printTypes(t.getUpperBounds(), " extends ", " & ", "", false);

			printTypes(t.getLowerBounds(), " super ", " & ", "", false);

		} else if (type instanceof ParameterizedType) {

			ParameterizedType t = (ParameterizedType) type;

			Type owner = t.getOwnerType();

			if (owner != null) {

				printType(owner, false);

				System.out.print(".");

			}

			printType(t.getRawType(), false);

			printTypes(t.getActualTypeArguments(), "<", ", ", ">", false);

		} else if (type instanceof GenericArrayType) {

			GenericArrayType t = (GenericArrayType) type;

			System.out.print("");

			printType(t.getGenericComponentType(), isDefinition);

			System.out.print("[]");

		}

	}
	public static void printMethod(Method m) {

		String name = m.getName();

		System.out.print(Modifier.toString(m.getModifiers()));

		System.out.print(" ");

		printTypes(m.getTypeParameters(), "<", ", ", "> ", true);

		printType(m.getGenericReturnType(), false);

		System.out.print(" ");

		System.out.print(name);

		System.out.print("(");

		printTypes(m.getGenericParameterTypes(), "", ", ", "", false);

		System.out.println(")");

	}

}

class Pair<T> implements Comparable<Integer>,Iterable<Integer>
{
	private T first;
	private T second;

	public Pair() {

	}

	public void setFirst(T first) {
		this.first = first;
	}

	public void  setSecond(T second) {
		this.second = (T)second;
	}

	public T getFirst(T first) {
		return this.first;
	}

	public T getSecond(T second) {
		return this.second;
	}

	@Override
	public int compareTo(Integer arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Iterator<Integer> iterator() {
		// TODO Auto-generated method stub
		return null;
	}
}

class Employee {
	public static void f(Object o) {
		System.out.println("public void f(Object o)");
	}
}

class Manager extends Employee {
	public void f(Integer i) {
		System.out.println("public void f(Integer i)");
	}
}