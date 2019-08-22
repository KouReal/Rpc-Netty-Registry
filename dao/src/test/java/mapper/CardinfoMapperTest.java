package mapper;

import java.net.PasswordAuthentication;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.junit.Assert;
import org.junit.Test;

import com.alibaba.fastjson.JSONObject;

import pojo.CardTrade;
import pojo.Cardinfo;
import pojo.CardinfoExample;
import pojo.CardinfoExample.Criteria;
import pojo.Customer;
import pojo.Tradeinfo;
import pojo.UserCard;
import typehandler.EnableCard;


public class CardinfoMapperTest extends BaseMapperTest {
	
	

	
	@Test
	public void testTransferAccount(){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("payer", "2");
		map.put("receiver", "1");
		map.put("password", "123456");
		map.put("amount", "99");

		JSONObject params = new JSONObject(map);
		
		String jstr = params.toJSONString();
		//...net...
		TransferAccount(jstr);
	}
	public boolean TransferAccount(String jstr){
		JSONObject jso = JSONObject.parseObject(jstr);
		Long payer = Long.valueOf((String)jso.get("payer"));
		Long receiver = Long.valueOf((String)jso.get("receiver"));
		String password = (String)jso.get("password");
		Long amount = Long.valueOf((String)jso.get("amount"));
		
		SqlSession sqlSession = getSqlSession();
		try{
			CardinfoMapper cardinfoMapper = sqlSession.getMapper(CardinfoMapper.class);
			TradeinfoMapper tradeinfoMapper = sqlSession.getMapper(TradeinfoMapper.class);
			CardTradeMapper cardTradeMapper = sqlSession.getMapper(CardTradeMapper.class);
			CardinfoExample cardinfoExample = new CardinfoExample();
			CardinfoExample.Criteria  cardinfocriteria= cardinfoExample.createCriteria();
			cardinfocriteria.andIdEqualTo(payer);
			cardinfocriteria.andPasswordEqualTo(password);
			List<Cardinfo> cardinfolist = cardinfoMapper.selectByExample(cardinfoExample);
			if(cardinfolist==null||cardinfolist.size()==0){
				System.out.println("密码错误");
				return false;
			}
			
			Cardinfo payercard = cardinfoMapper.selectByPrimaryKey(payer);
			Long paybalance = payercard.getBalance();
			if(paybalance<amount){
				System.out.println("余额不足");
				return false;
			}
			Cardinfo receivercard = cardinfoMapper.selectByPrimaryKey(receiver);
			if(receiver==null){
				System.out.println("对方账户不存在");
				return false;
			}
			Long receiverbalance = receivercard.getBalance();
			payercard.setBalance(paybalance-amount);
			receivercard.setBalance(receiverbalance+amount);
			
			cardinfoMapper.updateByPrimaryKeySelective(payercard);
			cardinfoMapper.updateByPrimaryKeySelective(receivercard);
			
			Tradeinfo tradeinfo = new Tradeinfo();
			tradeinfo.setPayer(payer);
			tradeinfo.setReceiver(receiver);
			tradeinfo.setAmount(amount);
			tradeinfo.setCreateTime(new Date());
			long tradeId = tradeinfoMapper.insert(tradeinfo);
			//tradeinfoMapper.insert(tradeinfo);
			
			CardTrade cardTrade1 = new CardTrade();
			cardTrade1.setCardId(payer);
			cardTrade1.setTradeId(tradeId);
			cardTrade1.setTradeType(0);
			cardTradeMapper.insert(cardTrade1);
			
			CardTrade cardTrade2 = new CardTrade();
			cardTrade2.setCardId(receiver);
			cardTrade2.setTradeId(tradeId);
			cardTrade2.setTradeType(1);
			cardTradeMapper.insert(cardTrade2);
			return true;
			
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("异常");
			sqlSession.rollback();
		}finally {
			sqlSession.commit();
			sqlSession.close();
		}
		return true;
		
	}

	@Test 
	public void testOpenAccount(){
		SqlSession sqlSession = getSqlSession();
		try {
			CardinfoMapper mapper = sqlSession.getMapper(CardinfoMapper.class);
			Cardinfo cardinfo = new Cardinfo();
			cardinfo.setCreateDate(new Date());
			cardinfo.setUserName("jack");
			cardinfo.setPassword("000000");
			cardinfo.setBalance(1000l);
			long cardid = mapper.insert(cardinfo);
			
			UserCardMapper m2 = sqlSession.getMapper(UserCardMapper.class);
			UserCard uc = new UserCard();
			uc.setCardId(cardid);
			uc.setCustomerId(6l);
			m2.insert(uc);
		}catch(Exception e){
			sqlSession.rollback();
		}finally {
			//为了不影响数据库中的数据导致其他测试失败，这里选择回滚
			//由于默认的 sqlSessionFactory.openSession() 是不自动提交的，
			//因此不手动执行 commit 也不会提交到数据库
//			sqlSession.rollback();
			sqlSession.commit();
			//不要忘记关闭 sqlSession
			sqlSession.close();
		}
	}
	
	@Test
	public void test3(){
		SqlSession session = getSqlSession();
		try{
			CardinfoMapper cardinfoMapper = session.getMapper(CardinfoMapper.class);
			Cardinfo cardinfo = new Cardinfo(null, "123456", Long.valueOf("100"), new Date(), "trump03");
			cardinfoMapper.insert(cardinfo);
			System.out.println(cardinfo.getId());
			session.commit();
		}catch(Exception e){
			session.rollback();
			
		}finally{
			session.close();
		}
		
	}
	
	@Test
	public void test4(){
		SqlSession session = getSqlSession();
		try{
			UserCardMapper userCardMapper = session.getMapper(UserCardMapper.class);
			Long cardid = userCardMapper.selectCardidByCustomerid(6l);
			System.out.println(cardid);
		}catch(Exception e){
			session.rollback();
			
		}finally{
			session.close();
		}
	}
	

	
	

}
