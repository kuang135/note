package cn.itcast.DBTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultMapper {

	//提供了两个实现类,来处理返回是一个对象和一批对象的情况
	//用的是反射的方式,JavaBean中的属性必须和表中对应的列名一致
	//当然也可以自定义实现类,不用反射的方式
	public Object mapper(ResultSet rs) throws Exception ;
}
