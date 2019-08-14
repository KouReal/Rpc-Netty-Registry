package typehandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;


public class EnableCardTypeHandler implements TypeHandler<EnableCard> {
	private final Map<Integer, EnableCard> enabledMap = new HashMap<Integer, EnableCard>();

	public EnableCardTypeHandler() {
		for(EnableCard enabled : EnableCard.values()){
			enabledMap.put(enabled.getValue(), enabled);
		}
	}
	
	public EnableCardTypeHandler(Class<?> type) {
		this();
	}

	@Override
	public void setParameter(PreparedStatement ps, int i, EnableCard parameter, JdbcType jdbcType) throws SQLException {
		ps.setInt(i, parameter.getValue());
	}

	@Override
	public EnableCard getResult(ResultSet rs, String columnName) throws SQLException {
		Integer value = rs.getInt(columnName);
		return enabledMap.get(value);
	}

	@Override
	public EnableCard getResult(ResultSet rs, int columnIndex) throws SQLException {
		Integer value = rs.getInt(columnIndex);
		return enabledMap.get(value);
	}

	@Override
	public EnableCard getResult(CallableStatement cs, int columnIndex) throws SQLException {
		Integer value = cs.getInt(columnIndex);
		return enabledMap.get(value);
	}

}
