package uk.ac.ucl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.data.jdbc.core.OneToManyResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

public class NotificationCategoriesExtractor extends OneToManyResultSetExtractor<Notification,String,Integer>
{

	public NotificationCategoriesExtractor() {
		super(new NotificationMapper(),new NotificationCategoryMapper());
	}
	
	@Override
	protected Integer mapPrimaryKey(ResultSet rs) throws SQLException {
		return Integer.parseInt(rs.getString("notification.message_id"));
	}
	
	@Override
	protected Integer mapForeignKey(ResultSet rs) throws SQLException {
		if(rs.getObject("notificationcategories.message_id")== null) return null;
		else return Integer.parseInt(rs.getString("notificationcategories.message_id"));
	}
	
	@Override
	protected void addChild(Notification root, String category)
	{
		root.addCategory(category);
	}
	
	private static final class NotificationMapper implements RowMapper<Notification> {
		private SimpleDateFormat old_format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		private SimpleDateFormat new_format = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		public Notification mapRow(ResultSet rs, int rowNum) throws SQLException
		{
			String new_date;
			try
			{
				Date original = old_format.parse(rs.getString("updated_on"));
				new_date = new_format.format(original);
			}
			catch(Exception e)
			{
				new_date = rs.getString("updated_on");
			}			
			return new Notification(Integer.parseInt(rs.getString("notification.message_id")),rs.getString("notification.message"),
					rs.getString("notification.application"),new_date);
		}
	}
	
	private static final class NotificationCategoryMapper implements RowMapper<String> {
		public String mapRow(ResultSet rs, int rowNum) throws SQLException
		{
			return rs.getString("notificationcategories.category");
		}
	}
	
}