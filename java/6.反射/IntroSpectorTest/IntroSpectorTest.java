package IntroSpectorTest;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

public class IntroSpectorTest {

	//根据属性名获取一个JavaBean对象该属性的值
	public static <T> Object getPropertyValue(T entity,String propertyName) throws Exception{
		PropertyDescriptor pd=new PropertyDescriptor(propertyName,entity.getClass());
		Method readMethod = pd.getReadMethod();
		Object retValue = readMethod.invoke(entity);
		return retValue;
	}
	
	//根据属性名设置一个JavaBean对象该属性的值
	public static <T> void setPropertyValue(T entity,String propertyName,Object value) throws Exception{
		PropertyDescriptor pd=new PropertyDescriptor(propertyName,entity.getClass());
		Method writeMethod = pd.getWriteMethod();
		writeMethod.invoke(entity, value);
	}
	
	
}
