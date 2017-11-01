package cn.itcast.jdbc.dao.refactor;

import java.sql.ResultSet;
import java.sql.SQLException;

import cn.itcast.jdbc.domain.User;

/**
 * 
 * 2008-12-13
 * 
 * @author <a href="mailto:liyongibm@gmail.com">liyong</a>
 * 
 */
public class UserDaoImpl2 {
	MyDaoTemplate template = new MyDaoTemplate();

	public User findUser(String loginName, String password) {
		String sql = "select id, name, money, birthday  from user where name=?";
		Object[] args = new Object[] { loginName };
		RowMapper mapper = new UserRowMapper();
		Object user = this.template.find(sql, args, mapper);
		return (User) user;
	}

	public String findUserName(int id) {
		String sql = "select name from user where id=?";
		Object[] args = new Object[] { id };
		Object name = this.template.find(sql, args, new RowMapper() {//ƒ‰√˚¿‡

			public Object mapRow(ResultSet rs) throws SQLException {
				return rs.getString("name");
			}
		});
		return (String) name;
	}
}

class UserRowMapper implements RowMapper {
	public Object mapRow(ResultSet rs) throws SQLException {
		User user = new User();
		user.setId(rs.getInt("id"));
		user.setName(rs.getString("name"));
		user.setMoney(rs.getFloat("money"));
		user.setBirthday(rs.getDate("birthday"));
		return user;
	}

}
