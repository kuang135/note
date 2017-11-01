package cn.itcast.util;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class TransactionUtil {

	private static DataSource dataSource=new ComboPooledDataSource();
	private static ThreadLocal<Connection> local=new ThreadLocal<Connection>();
	
	//从本地线程中获取connection,没有就创建一个,放入本地线程
	public static Connection getConnection(){
		Connection con=local.get();
		if(con==null){
			try {
				con=dataSource.getConnection();
			} catch (SQLException e) {
				throw new RuntimeException();
			}
			local.set(con);
		}
	//	System.out.println("get--"+con);
		return con;
	}
	
	
	public static void startTransaction(){
		Connection con=getConnection();
		try {
			con.setAutoCommit(false);
		} catch (SQLException e) {
			throw new RuntimeException();
		}
	//	System.out.println("start--"+con);
	}
	
	public static void rollBackTransaction(){
		Connection con=getConnection();
		try {
			con.rollback();
		} catch (SQLException e) {
			throw new RuntimeException();
		}
	//	System.out.println("rollback--"+con);
	}
	
	
	public static void commitTransaction(){
		Connection con=getConnection();
		try {
			con.commit();
		} catch (SQLException e) {
			throw new RuntimeException();
		}
	//	System.out.println("commit--"+con);
	}
	
	public static void realse(){
		Connection con=getConnection();
	//	System.out.println("realse--"+con);
		local.remove();
		try {
			con.close();
		} catch (SQLException e) {
			throw new RuntimeException();
		}
	}
}
