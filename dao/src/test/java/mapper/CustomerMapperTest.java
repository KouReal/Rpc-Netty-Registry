package mapper;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.junit.Assert;
import org.junit.Test;

import pojo.Customer;
import typehandler.EnableCard;


public class CustomerMapperTest extends BaseMapperTest {
	
	@Test
	public void testSelectById(){
		//获取 sqlSession
		SqlSession sqlSession = getSqlSession();
		try {
			//获取 UserMapper 接口
			CustomerMapper mapper = sqlSession.getMapper(CustomerMapper.class);
			//调用 selectById 方法，查询 id = 1 的用户
			Customer customer = mapper.selectByPrimaryKey(5l);
			Assert.assertNotNull(customer);
			//userName = admin
			Assert.assertEquals("mike", customer.getUserName());
		} finally {
			//不要忘记关闭 sqlSession
			sqlSession.close();
		}
	}
	
	@Test
	public void testSelectByUserName(){
		//获取 sqlSession
		SqlSession sqlSession = getSqlSession();
		try {
			//获取 UserMapper 接口
			CustomerMapper mapper = sqlSession.getMapper(CustomerMapper.class);
			Customer customer = mapper.selectByUserName("jack");
			Assert.assertNotNull(customer);
			//userName = admin
			Assert.assertEquals("123456", customer.getPassword());
		} finally {
			//不要忘记关闭 sqlSession
			sqlSession.close();
		}
	}
	
	@Test
	public void testsearch() {
		// 获取 sqlSession
		SqlSession sqlSession = getSqlSession();
		try {
			// 获取 UserMapper 接口
			CustomerMapper mapper = sqlSession.getMapper(CustomerMapper.class);
			Integer num = mapper.searchByIdAndPassword("mike", "123456");
			System.out.println(num);
		} finally {
			// 不要忘记关闭 sqlSession
			sqlSession.close();
		}
	}
	
	@Test
	public void testSelectAll() {
		// 获取 sqlSession
		SqlSession sqlSession = getSqlSession();
		try {
			// 获取 UserMapper 接口
			CustomerMapper mapper = sqlSession.getMapper(CustomerMapper.class);
			// 调用 selectById 方法，查询 id = 1 的用户
			List<Customer> cs = mapper.selectAll();
			for (Customer c : cs) {
				System.out.println(c.toString());
			}
		} finally {
			// 不要忘记关闭 sqlSession
			sqlSession.close();
		}
	}
	
	@Test
	public void testUpdate(){
		SqlSession sqlSession = getSqlSession();
		try{
			CustomerMapper mapper = sqlSession.getMapper(CustomerMapper.class);
			Customer customer = new Customer(5l, null, "789", "中国", "909", EnableCard.havecard);
			/*customer.setId(5l);
			customer.setCountry("china中国");
			customer.setPassword("789");
			customer.setEnablecard(EnableCard.havecard);
			*/
			mapper.updateByPrimaryKeySelective(customer);
			
			Customer c2 = mapper.selectByPrimaryKey(5l);
			System.out.println(c2);
		}finally{
			sqlSession.commit();
			sqlSession.close();
		}
	}
	
	@Test
	public void testDelete(){
		SqlSession sqlSession = getSqlSession();
		try{
			CustomerMapper mapper = sqlSession.getMapper(CustomerMapper.class);
			mapper.deleteByPrimaryKey(6l);
			int a=1 / 0;
			Customer c2 = mapper.selectByPrimaryKey(6l);
			System.out.println(c2);
		}catch (Exception e) {
			sqlSession.rollback();
		}finally{
			sqlSession.commit();
			sqlSession.close();
		}
	}
	
	@Test
	public void testInsert(){
		SqlSession sqlSession = getSqlSession();
		try {
			CustomerMapper mapper = sqlSession.getMapper(CustomerMapper.class);
			Customer customer = new Customer(5l, null, "789", "中国", "909", EnableCard.havecard);
			customer.setCountry("中国");
			customer.setUserName("mike");
			customer.setEnablecard(EnableCard.nocard);
			//将新建的对象插入数据库中，特别注意，这里的返回值 result 是执行的 SQL 影响的行数
			int result = mapper.insert(customer);
			
		} finally {
			//为了不影响数据库中的数据导致其他测试失败，这里选择回滚
			//由于默认的 sqlSessionFactory.openSession() 是不自动提交的，
			//因此不手动执行 commit 也不会提交到数据库
//			sqlSession.rollback();
			sqlSession.commit();
			//不要忘记关闭 sqlSession
			sqlSession.close();
		}
	}
	
	

}
