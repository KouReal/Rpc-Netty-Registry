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


public class TradeInfoMapperTest extends BaseMapperTest {
	@Test
	public void testsearchtradeinfos(){
		//...net...
		//根据tokenid查询token,对应的attachinfo,get("customerid")=customerid
		searchtradeinfobycustomerid(6l);
	}
	public List<Tradeinfo> searchtradeinfobycustomerid(long customerid){
		SqlSession sqlSession = getSqlSession();
		try {
			UserCardMapper uc = sqlSession.getMapper(UserCardMapper.class);
			TradeinfoMapper tf = sqlSession.getMapper(TradeinfoMapper.class);
			long cardid = uc.selectCardidByCustomerid(customerid);
			List<Tradeinfo> tradeinfos = tf.selectAllTradeInfoById(cardid);
			return tradeinfos;
		}catch(Exception e){
			return null;
		}finally {
			sqlSession.close();
		}
	}
}
