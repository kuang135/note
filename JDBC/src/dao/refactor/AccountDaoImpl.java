package cn.itcast.jdbc.dao.refactor;

import java.sql.ResultSet;
import java.sql.SQLException;

import cn.itcast.jdbc.domain.Account;

/**
 * 
 * 2008-12-13
 * 
 * @author <a href="mailto:liyongibm@gmail.com">liyong</a>
 * 
 */
public class AccountDaoImpl extends AbstractDao {
	public Account findAccount(int id) {
		String sql = "select id, name, money from account where id=?";
		Object[] args = new Object[] { id };
		Object account = super.find(sql, args);
		return (Account) account;
	}

	@Override
	protected Object rowMapper(ResultSet rs) throws SQLException {
		Account a = new Account();
		a.setId(rs.getInt("id"));
		// ...
		return a;
	}

}
