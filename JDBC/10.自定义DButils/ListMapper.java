package cn.itcast.DBTemplate;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

public class ListMapper implements ResultMapper {

	private Class clazz=null;
	
	public ListMapper(Class clazz){
		this.clazz=clazz;
	}
	
	//JavaBean的属性名和对应的表的列名一致
	@Override
	public Object mapper(ResultSet rs) throws Exception {
		List<Object> list=new ArrayList<Object>();
		ResultSetMetaData rmd = rs.getMetaData();
		int columnCount = rmd.getColumnCount();
		while(rs.next()){
			Object obj=clazz.newInstance();
			for(int i=1;i<columnCount;i++){
				String columnName = rmd.getColumnName(i);
				Object value = rs.getObject(columnName);
				Field field = clazz.getDeclaredField(columnName);
				field.setAccessible(true);
				field.set(obj, value);
			}
			list.add(obj);
		}
		return list;
	}

}
