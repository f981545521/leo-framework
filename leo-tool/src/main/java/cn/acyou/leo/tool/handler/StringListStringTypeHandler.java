package cn.acyou.leo.tool.handler;

import com.alibaba.fastjson2.JSON;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class StringListStringTypeHandler extends BaseTypeHandler<List<String>> {

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, List<String> parameter, JdbcType jdbcType) throws SQLException {
		String content = ObjectUtils.isEmpty(parameter) ? null : JSON.toJSONString(parameter);
		ps.setString(i, content);
	}

	@Override
	public List<String> getNullableResult(ResultSet rs, String columnName) throws SQLException {
		return this.getListByString(rs.getString(columnName));
	}

	@Override
	public List<String> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		return this.getListByString(rs.getString(columnIndex));
	}

	@Override
	public List<String> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		return  this.getListByString(cs.getString(columnIndex));
	}

	private List<String> getListByString(String content) {
		return StringUtils.isBlank(content)
				? Collections.emptyList()
				: new ArrayList<>(Arrays.asList(StringUtils.split(content, ",")));
	}

}
