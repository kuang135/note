package cn.itcast.jdbc;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import cn.itcast.jdbc.domain.User;

/**
 * 
 * 2008-12-7
 * 
 * @author <a href="mailto:liyongibm@gmail.com">liyong</a>
 * 
 */
public class ReflectTest {

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		Class clazz = User.class;
		
		// clazz = Bean.class;
		Object obj = create(clazz);
		System.out.println(obj);
		
		System.out.println("---------");
		invoke1(obj, "showName");

		System.out.println("---------");
		field(clazz);
	}

	static Object create(Class clazz) throws Exception {
		Constructor con = clazz.getConstructor(String.class);
		Object obj = con.newInstance("test name");
		return obj;
	}

	static void invoke1(Object obj, String methodName)
			throws IllegalArgumentException, IllegalAccessException,
			InvocationTargetException, Exception, NoSuchMethodException {
		Method[] ms = obj.getClass().getDeclaredMethods();
		ms = obj.getClass().getMethods();
		for (Method m : ms) {
			// System.out.println(m.getName());
			if (methodName.equals(m.getName()))
				m.invoke(obj, null);
		}

		Method m = obj.getClass().getMethod(methodName, null);
		m.invoke(obj, null);
	}

	static void field(Class clazz) throws Exception {
		Field[] fs = clazz.getDeclaredFields();
		//fs = clazz.getFields();
		for (Field f : fs)
			System.out.println(f.getName());
	}
	
	static void annon(Class clazz) throws Exception {
		Annotation[] as = clazz.getAnnotations();
	}

}
