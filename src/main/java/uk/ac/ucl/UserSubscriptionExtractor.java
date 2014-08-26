package uk.ac.ucl;

import org.springframework.data.jdbc.core.OneToManyResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserSubscriptionExtractor extends OneToManyResultSetExtractor<User,UserSubscription,String> {

	public UserSubscriptionExtractor() {
		super(new UserMapper(), new UserSubscriptionMapper());
	}
	
	@Override
	protected String mapPrimaryKey(ResultSet rs) throws SQLException {
		return rs.getString("user.email_address");
	}
	
	@Override
	protected String mapForeignKey(ResultSet rs) throws SQLException {
		if(rs.getObject("subscription.email_address")== null) return null;
		else return rs.getString("subscription.email_address");
	}
	
	@Override
	protected void addChild(User root, UserSubscription child)
	{
		if(child.getType().equals("Application")) root.addApplication(child.getSub(),child.getFrequency());
		if(child.getType().equals("Category")) root.addCategory(child.getSub(),child.getFrequency());
	}
	
	public static final class UserMapper implements RowMapper<User> {
		public User mapRow(ResultSet rs, int rowNum) throws SQLException
		{
			return new User(rs.getString("user.email_address"),rs.getString("user.password"),rs.getString("user.first_name"),
					rs.getString("user.last_name"));
		}
	}
	
	public static final class UserSubscriptionMapper implements RowMapper<UserSubscription> {
		public UserSubscription mapRow(ResultSet rs, int rowNum) throws SQLException
		{
			return new UserSubscription(rs.getString("subscription.email_address"),
					rs.getString("subscription.type"),rs.getString("subscription.sub"),rs.getString("subscription.frequency"));
		}
	}	
}
