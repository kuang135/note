package cn.itcast.DBTemplate;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

public class OneMapper implements ResultMapper {
	
	private Class clazz=null;
	
	public OneMapper(Class clazz){
		this.clazz=clazz;
	}

	//JavaBean的属性名和对应的表的列名一致
	@Override
	public Object mapper(ResultSet rs) throws  Exception {
		Object obj=clazz.newInstance();
		ResultSetMetaData rm = rs.getMetaData();
		int columnCount = rm.getColumnCount();
		if(rs.next()){
			for(int i=1;i<=columnCount;i++){
				String columnName = rm.getColumnName(i);
				Object value = rs.getObject(columnName);
				Field field = clazz.getDeclaredField(columnName);
				field.setAccessible(true);
				field.set(obj, value);
			}
		}
		return obj;
	}

}
