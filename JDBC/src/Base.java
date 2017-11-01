package cn.itcast.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 
 * 2008-12-6
 * 
 * @author <a href="mailto:liyongibm@hotmail.com">李勇</a>
 * 
 */
public class Base {

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		for (int i = 0; i < 10; i++) {
			Connection conn = JdbcUtils.getConnection();
			System.out.println(conn.getClass().getName());
			JdbcUtils.free(null, null, conn);
		}
		// template();
	}

	static void template() throws Exception {
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			// 2.建立连接
			conn = JdbcUtils.getConnection();
			// conn = JdbcUtilsSing.getInstance().getConnection();
			// 3.创建语句
			st = conn.createStatement();

			// 4.执行语句
			rs = st.executeQuery("select * from user");

			// 5.处理结果
			while (rs.next()) {
				// 参数中的1,2,3,4是指sql中的列索引
				System.out.println(rs.getObject(1) + "\t" + rs.getObject(2)
						+ "\t" + rs.getObject(3) + "\t" + rs.getObject(4));
			}
		} finally {
			JdbcUtils.free(rs, st, conn);
		}

	}

	static void test() throws SQLException, ClassNotFoundException {
		// 1.注册驱动
		DriverManager.registerDriver(new com.mysql.jdbc.Driver());
		System.setProperty("jdbc.drivers", "com.mysql.jdbc.Driver");
		Class.forName("com.mysql.jdbc.Driver");// 推荐方式

		// 2.建立连接
		String url = "jdbc:mysql://localhost:3306/jdbc";
		String user = "root";
		String password = "";
		Connection conn = DriverManager.getConnection(url, user, password);

		// 3.创建语句
		Statement st = conn.createStatement();

		// 4.执行语句
		ResultSet rs = st.executeQuery("select * from user");

		// 5.处理结果
		while (rs.next()) {
			System.out.println(rs.getObject(1) + "\t" + rs.getObject(2) + "\t"
					+ rs.getObject(3) + "\t" + rs.getObject(4));
		}

		// 6.释放资源
		rs.close();
		st.close();
		conn.close();
	}
}
