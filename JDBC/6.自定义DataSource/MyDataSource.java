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
			throw new RuntimeException("���ݿ���������ʧ��...");
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
		System.out.println("���ӳع������,����"+pool.size()+"������.");
	}
	
	public Connection getConnection() throws DataSourceNoConnectionException{
		Connection proxyConn=null;
		if(current<=init){//�ڳ�ʼ���ĸ�������
			final Connection conn=pool.removeFirst();
			//����һ��Connection�Ĵ�����,�رյ�ʱ�����ӷŻص����ӳ�
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
			System.out.println("���ӳ������ӹ���,ֱ���õ�����,���ӳػ�ʣ"+pool.size()+"������---��ǰ��"+current+"������.");
		}else if(current<=max){//�ڳ�ʼ���ĸ�������,������������
			try {
				final Connection conn=DriverManager.getConnection(url, user, password);
				//����һ��Connection�Ĵ�����,�رյ�ʱ�����ӷŻص����ӳ�
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
				System.out.println("���ӳ������Ӳ�����,�������Ӻ��õ�,���ӳػ�ʣ"+pool.size()+"������---��ǰ��"+current+"������.");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}else{//������������,�͵ȴ�,ʱ�̼��pool��size�Ƿ�С��max
			throw new DataSourceNoConnectionException("���ӳ����Ѿ�û��������.....");
		}
		current++;
		return proxyConn;
	}
	
	public void putBack(Connection conn){
		if(conn!=null){
			current--;
			pool.addLast(conn);
			System.out.println("�Ż�һ������,�������ӳ��л�ʣ"+pool.size()+"������.");
		}
	}
}
