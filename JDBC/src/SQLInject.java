package cn.itcast.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
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
public class SQLInject {

	/**
	 * @param args
	 * @throws SQLException
	 */
	public static void main(String[] args) throws SQLException {
		// long start = System.currentTimeMillis();
		// for (int i = 0; i < 100; i++)
		read("name1");
		// long end = System.currentTimeMillis();
		// System.out.println("read:" + (end - start));

		// start = System.currentTimeMillis();
		// for (int i = 0; i < 100; i++)
		read1("name1");
		// end = System.currentTimeMillis();
		// System.out.println("read1:" + (end - start));
	}

	static void read(String name) throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			// 2.建立连接
			conn = JdbcUtils.getConnection();

			// conn = JdbcUtilsSing.getInstance().getConnection();
			// 3.创建语句
			String sql = "select id, name, money, birthday  from user where name=?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, name);
			// 4.执行语句
			rs = ps.executeQuery();

			// 5.处理结果
			while (rs.next()) {
				System.out.println(rs.getInt("id") + "\t"
						+ rs.getString("name") + "\t" + rs.getDate("birthday")
						+ "\t" + rs.getFloat("money"));
			}

		} finally {
			JdbcUtils.free(rs, ps, conn);
		}
	}

	static void read1(String name) throws SQLException {
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			// 2.建立连接
			conn = JdbcUtils.getConnection();
			// conn = JdbcUtilsSing.getInstance().getConnection();

			// 3.创建语句
			String sql = "select id, name, money, birthday  from user where name='"
					+ name + "'";
			st = conn.createStatement();
			// 4.执行语句
			rs = st.executeQuery(sql);

			// 5.处理结果
			while (rs.next()) {
				// System.out.println(rs.getObject("id") + "\t"
				// + rs.getObject("name") + "\t"
				// + rs.getObject("birthday") + "\t"
				// + rs.getObject("money"));
			}
		} finally {
			JdbcUtils.free(rs, st, conn);
		}
	}

}
