package cn.itcast.utils;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import javax.sql.DataSource;
import com.mchange.v2.c3p0.ComboPooledDataSource;

//�ѵõ����Ӽ������йصķ���д��������
public class TransactionUtil {
	
	// �ڲ���ά���� һ�� map , ���map ��key ʼ�� ���� ��ǰ ���߳� 
	private static ThreadLocal<Connection> tl = new ThreadLocal<Connection>();
	
	private static DataSource ds = new ComboPooledDataSource();
	
	public static DataSource getDataSource(){
		return ds;
	}
	
	//  ����, ���һ�� connection  ���� 
	public static Connection getConnection(){
		try {
			Connection conn = tl.get();
			if(conn==null){
				conn = ds.getConnection();
				tl.set(conn);
			}
			return conn;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	public static void startTransaction(){   // ��������
		try {
			Connection conn = tl.get();
			if(conn==null){
				conn = getConnection();
//				tl.set(conn);
			}
			conn.setAutoCommit(false);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	public static void rollback(){    // �ع����� 
		try { 
			Connection conn = tl.get();  // ��֤�� ͬһ��connection ����
			if(conn==null){
				conn = getConnection();
//				tl.set(conn);
			}
			conn.rollback();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	public static void commit(){  // �ύ����
		try {
			Connection conn = tl.get();
			if(conn==null){
				conn = getConnection();
//				tl.set(conn);
			}
			conn.commit();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	public static void relase(){
		try {
			Connection conn = tl.get();
			if(conn!=null){
				conn.close();
				tl.remove();
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
