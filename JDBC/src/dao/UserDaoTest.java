package cn.itcast.jdbc.dao;

import java.util.Date;

import cn.itcast.jdbc.dao.refactor.UserDaoImpl2;
import cn.itcast.jdbc.domain.User;

/**
 * 
 * 2008-12-6
 * 
 * @author <a href="mailto:liyongibm@gmail.com">liyong</a>
 * 
 */
public class UserDaoTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		UserDao userDao = DaoFactory.getInstance().getUserDao();
		// UserDao userDao = new UserDaoJdbcImpl();
		// System.out.println(userDao);
		//		
		User user = new User();
		user.setBirthday(new Date());
		user.setName("dao name2");
		user.setMoney(1000.0f);

		// userDao.addUser(user);
		// System.out.println(user.getId());

		User u = userDao.findUser(user.getName(), null);
		System.out.println(u.getId());

		u = userDao.getUser(1);
		u.setMoney(20000.1f);
		userDao.update(u);

		// User u1 = userDao.getUser(8);
		// userDao.delete(u1);

		// UserDaoImpl udi = new UserDaoImpl();
		// User u1 = udi.findUser(user.getName(), null);

	}

}
