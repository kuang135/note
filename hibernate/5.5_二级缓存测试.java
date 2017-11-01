package cn.itcast.hibernate.d_secondcache;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.stat.Statistics;
import org.junit.Test;

import cn.itcast.domain.Customer;
import cn.itcast.domain.Order;
import cn.itcast.utils.HibernateUtils;

public class HibernateSecondCacheTest {
	@Test
	// 使用线程绑定的Session对象
	public void testSessionManage() {
		Session session = HibernateUtils.getCurrentSession();
		session.beginTransaction();

		Customer customer = (Customer) session.get(Customer.class, 76);
		System.out.println(customer);

		// 当事务提交时，session自动关闭
		session.getTransaction().commit();
	}

	@Test
	// 测试二级缓存可用
	public void testSecondCache() {
		Session session = HibernateUtils.openSession();
		session.beginTransaction();

		// 第一次查询，放入一级缓存，放入二级缓存
		Customer customer1 = (Customer) session.get(Customer.class, 76);
		System.out.println(customer1 + ",地址：" + customer1.hashCode());

		// 从一级缓存查询
		Customer customer2 = (Customer) session.get(Customer.class, 76);
		System.out.println(customer2 + ",地址：" + customer2.hashCode());

		session.getTransaction().commit();
		session.close();

		// 开启新的会话
		session = HibernateUtils.openSession();
		session.beginTransaction();

		// 从二级缓存查询
		Customer customer3 = (Customer) session.get(Customer.class, 76);
		System.out.println(customer3 + ",地址：" + customer3.hashCode());

		session.getTransaction().commit();
		session.close();

		// 开启新的会话
		session = HibernateUtils.openSession();
		session.beginTransaction();

		// 从二级缓存查询
		Customer customer4 = (Customer) session.get(Customer.class, 76);
		System.out.println(customer4 + ",地址：" + customer4.hashCode());

		session.getTransaction().commit();
		session.close();
	}

	@Test
	// 测试二级缓存 读写性
	public void testSecondCache2() {
		Session session = HibernateUtils.openSession();
		session.beginTransaction();

		// 第一次查询，放入一级缓存，放入二级缓存
		Customer customer1 = (Customer) session.createQuery("from Customer where id=76").uniqueResult();
		System.out.println(customer1 + ",地址：" + customer1.hashCode());

		session.getTransaction().commit();
		session.close();

		// 开启新的会话
		session = HibernateUtils.openSession();
		session.beginTransaction();

		// 从二级缓存查询
		Customer customer2 = (Customer) session.get(Customer.class, 76);
		System.out.println(customer2 + ",地址：" + customer2.hashCode());

		session.getTransaction().commit();
		session.close();

		// 开启新的会话
		session = HibernateUtils.openSession();
		session.beginTransaction();

		// 从二级缓存查询
		Iterator iterator = session.createQuery("from Customer where id=76").iterate();
		Customer customer3 = (Customer) iterator.next();
		System.out.println(customer3 + ",地址：" + customer3.hashCode());

		session.getTransaction().commit();
		session.close();
	}

	@Test
	// 测试集合缓存区域的可用
	public void testSecondCache3() {
		Session session = HibernateUtils.openSession();
		session.beginTransaction();

		Customer customer1 = (Customer) session.get(Customer.class, 76);
		// 第一次查询，放入二级缓存
		System.out.println(customer1.getOrders());

		session.getTransaction().commit();
		session.close();

		// 开启新的会话
		session = HibernateUtils.openSession();
		session.beginTransaction();

		Customer customer2 = (Customer) session.get(Customer.class, 76);
		// 从二级缓存获取
		System.out.println(customer2.getOrders());

		session.getTransaction().commit();
		session.close();

	}

	@Test
	// 测试一级缓存自动同步二级缓存
	public void testSecondCache4() {
		Session session = HibernateUtils.openSession();
		session.beginTransaction();

		Customer customer1 = (Customer) session.get(Customer.class, 76);
		System.out.println(customer1);
		customer1.setName("mark"); // 修改一级缓存，自动同步到二级缓存

		session.getTransaction().commit();
		session.close();

		// 开启新的会话
		session = HibernateUtils.openSession();
		session.beginTransaction();

		Customer customer2 = (Customer) session.get(Customer.class, 76);
		System.out.println(customer2);

		session.getTransaction().commit();
		session.close();
	}

	@Test
	// 测试，当内存缓冲区满了后，能否将数据输出到硬盘
	@SuppressWarnings("unchecked")
	public void testSecondCache5() {
		Session session = HibernateUtils.openSession();
		session.beginTransaction();

		List<Order> list = session.createQuery("from Order").list();
		System.out.println(list);

		session.getTransaction().commit();
		session.close();
	}

	@Test
	// 测试 更新时间戳区域的使用
	public void testSecondCache6() {
		Session session = HibernateUtils.openSession();
		session.beginTransaction();

		// 放入二级缓存时间t1
		Customer customer1 = (Customer) session.get(Customer.class, 76);
		System.out.println(customer1);
		// 修改数据, 直接update，更新时间t2， 记录到更新时间戳区域
		session.createQuery("update Customer set name='itcast' where id=76").executeUpdate();

		session.getTransaction().commit();
		session.close();

		// 开启新的会话
		session = HibernateUtils.openSession();
		session.beginTransaction();

		// 因为t2 > t1 所以缓存无效
		Customer customer2 = (Customer) session.get(Customer.class, 76);
		System.out.println(customer2);

		session.getTransaction().commit();
		session.close();
	}

	@Test
	// 测试查询缓存使用
	public void testQueryCache() {
		Session session = HibernateUtils.openSession();
		session.beginTransaction();
		// 查询订单总数量
		Query query = session.createQuery("select count(o) from Order o");
		// 使用查询缓存
		query.setCacheable(true);
		long total = (Long) query.uniqueResult();
		System.out.println(total);
		session.getTransaction().commit();
		session.close();

		// 开启新的会话
		session = HibernateUtils.openSession();
		session.beginTransaction();
		Query query2 = session.createQuery("select count(t) from Order t");
		// 使用查询缓存
		query2.setCacheable(true);
		long total2 = (Long) query2.uniqueResult();
		System.out.println(total2);
		session.getTransaction().commit();
		session.close();
	}

	@Test
	// 测试查询缓存使用 --- 监测
	public void testQueryCache2() {
		// 获取统计对象，通过SessionFactory
		Statistics statistics = HibernateUtils.getSessionFactory().getStatistics();
		System.out.println("命中:" + statistics.getQueryCacheHitCount() + "，丢失：" + statistics.getQueryCacheMissCount());

		Session session = HibernateUtils.openSession();
		session.beginTransaction();
		// 查询订单总数量
		Query query = session.createQuery("select count(o) from Order o");
		// 使用查询缓存
		query.setCacheable(true);
		long total = (Long) query.uniqueResult();
		System.out.println(total);
		session.getTransaction().commit();
		session.close();

		System.out.println("命中:" + statistics.getQueryCacheHitCount() + "，丢失：" + statistics.getQueryCacheMissCount());

		// 开启新的会话
		session = HibernateUtils.openSession();
		session.beginTransaction();
		Query query2 = session.createQuery("select count(t) from Order t");
		// 使用查询缓存
		query2.setCacheable(true);
		long total2 = (Long) query2.uniqueResult();
		System.out.println(total2);
		session.getTransaction().commit();
		session.close();
		System.out.println("命中:" + statistics.getQueryCacheHitCount() + "，丢失：" + statistics.getQueryCacheMissCount());

	}
}
