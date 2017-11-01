package cn.itcast.datasource;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;

public class MyDataSource {
	
	private static int max=10;
	private static int init=5;
	private static int current=1;
	private static LinkedList<Connection> pool=null;
	private String url="jdbc:mysql://localhost:3306/mydb1";
	private String user="root";
	private String password="password";
	
	static{
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException("数据库驱动加载失败...");
		}
	}
	
	public MyDataSource(){
		pool=new LinkedList<Connection>();
		for(int i=0;i<init;i++){
			Connection conn=null;
			try {
				conn=DriverManager.getConnection(url, user, password);
			} catch (SQLException e) {
				e.printStackTrace();
				return;
			}
			pool.addLast(conn);
		}
		System.out.println("连接池构造完成,共有"+pool.size()+"个连接.");
	}
	
	public Connection getConnection() throws DataSourceNoConnectionException{
		Connection proxyConn=null;
		if(current<=init){//在初始化的个数以内
			final Connection conn=pool.removeFirst();
			//产生一个Connection的代理类,关闭的时候将连接放回到连接池
			proxyConn=(Connection)Proxy.newProxyInstance(Connection.class.getClassLoader(),conn.getClass().getInterfaces() , new InvocationHandler(){
					@Override
					public Object invoke(Object proxy, Method method, Object[] args)
							throws Throwable {
						if("close".equals(method.getName())){
							putBack(conn);
							return null;
						}else{
							return method.invoke(conn, args);
						}
					}
				});
			System.out.println("连接池中连接够用,直接拿到连接,连接池还剩"+pool.size()+"个连接---当前第"+current+"个连接.");
		}else if(current<=max){//在初始化的个数以外,在最大个数以内
			try {
				final Connection conn=DriverManager.getConnection(url, user, password);
				//产生一个Connection的代理类,关闭的时候将连接放回到连接池
				proxyConn=(Connection)Proxy.newProxyInstance(MyDataSource.class.getClassLoader(),Connection.class.getInterfaces() , new InvocationHandler(){
						@Override
						public Object invoke(Object proxy, Method method, Object[] args)
								throws Throwable {
							if("close".equals(method.getName())){
								putBack(conn);
								return null;
							}else{
								return method.invoke(conn, args);
							}
						}
					});
				System.out.println("连接池中连接不够用,创建连接后拿到,连接池还剩"+pool.size()+"个连接---当前第"+current+"个连接.");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}else{//在最大个数以外,就等待,时刻监控pool的size是否小于max
			throw new DataSourceNoConnectionException("连接池中已经没有连接了.....");
		}
		current++;
		return proxyConn;
	}
	
	public void putBack(Connection conn){
		if(conn!=null){
			current--;
			pool.addLast(conn);
			System.out.println("放回一个连接,现在连接池中还剩"+pool.size()+"个连接.");
		}
	}
}
