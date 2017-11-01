package AnnotationTest;

import java.lang.reflect.Field;

public class AnnotationHandler {

	public static void main(String[] args) throws Exception{
		if(args.length<1){
			System.out.println("please input class name");
			System.exit(0);
		}
		//遍历输入的类名
		for(String className:args){
			StringBuilder sqlBuffer=new StringBuilder("CREATE TABLE ");
			//获取Class
			Class<?> clazz = Class.forName("AnnotationTest."+className);
			//判断该类上有没有加@Table
			Table tableA = clazz.getAnnotation(Table.class);
			if(tableA==null){
				continue;
			}
			
			//获取表名
			String tableName = tableA.name();
			if(tableName.length()<1){
				tableName=className;
			}
			sqlBuffer.append(tableName+"(");
			//获取class的所有字段,并遍历,判断是否加@Column和@Constraint注解
			Field[] fields = clazz.getDeclaredFields();
			for (Field field : fields) {
				//@Column注解
				Column columnA = field.getAnnotation(Column.class);
				if(columnA==null){
					continue;
				}
				String columnName = columnA.columnName();
				if(columnName.length()<1){
					columnName=field.getName();
				}
				String columnType=columnA.typeName();
				//@Constraint注解
				Constraint constraintA = field.getAnnotation(Constraint.class);
				if(constraintA==null){
					sqlBuffer.append(columnName+" "+columnType+" NULL,");
					continue;
				}
				if(constraintA.isPrimaryKey()){
					sqlBuffer.append(columnName+" "+columnType+" PRIMARY KEY NOT NULL,");
					continue;
				}
				if(constraintA.notNull()){
					sqlBuffer.append(columnName+" "+columnType+" NOT NULL,");
					continue;
				}
			}
			
			String sql=sqlBuffer.toString();
			sql=sql.substring(0, sql.length()-1)+")";
			System.out.println(sql);
		}
	
		
		
		
	}
	
}
