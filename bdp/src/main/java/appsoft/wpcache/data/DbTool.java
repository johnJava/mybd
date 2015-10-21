package appsoft.wpcache.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import appsoft.wpcache.data.DbTool;
import appsoft.wpcache.data.PropertiesValue;


public class DbTool 
{
	boolean isKeyUpper=false;//key是否为大写
	private static DbTool dbToolInstance;

	public static DbTool getDbTool()
	{
		if(dbToolInstance!=null)
		{
			return dbToolInstance;
		}
		else
		{
			dbToolInstance=new DbTool();
		}
		return dbToolInstance;
	}

	public  ArrayList<HashMap<String,String>> listAll(String sql,Connection connInput) throws Exception
	{ 
		return listAll( sql,null,connInput);
	}


	public  ArrayList<HashMap<String,String>> listAll(String sql) throws Exception
	{ 
		return listAll( sql,null,null);
	}

	public  ArrayList<HashMap<String,String>> listAll(String sql,String tableName,Connection connInput) throws Exception
	{   

		ArrayList<HashMap<String,String>>  al=new  ArrayList<HashMap<String,String>>();
		Connection connection = null;
		Statement statement=null;
		ResultSet resultSet=null;
		try
		{
			long startTime=System.currentTimeMillis();
			if(connInput==null)
			{
				connection=getConnection();
			}
			else
			{
				connection=connInput;
			}
			
			long endTime1=System.currentTimeMillis();
			statement=connection.createStatement();
			resultSet=statement.executeQuery(sql);
			long endTime=System.currentTimeMillis();
			double secondsUsed=(endTime-startTime+0.0)/1000.0;
			String timeUsedStr="";
			if((endTime-startTime)<1000)
			{
				timeUsedStr="用时:"+(endTime-startTime)+"毫秒";
			}
			else
			{
				timeUsedStr="用时:"+secondsUsed+"秒";
			}
			ResultSetMetaData resultSetmd  = resultSet.getMetaData();
			while(resultSet.next())
			{
				HashMap<String,String> 	hb= new HashMap<String,String>();
				for(int i = 1; i <= resultSetmd.getColumnCount(); i++)
				{ 
					String value="";
					int columnType=resultSetmd.getColumnType(i);
					if( columnType==Types.DATE || columnType==Types.TIME ||columnType==Types.TIMESTAMP )
					{
						SimpleDateFormat dateFormat = new  SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						java.util.Date date=resultSet.getTimestamp(resultSetmd.getColumnName(i));
						if (date!=null&&!date.equals("")) 
						{
							value=dateFormat.format(date);
						}else
						{
							value="";
						}
					}
					else
					{
						value=resultSet.getString(resultSetmd.getColumnName(i))==null?"":resultSet.getString(resultSetmd.getColumnName(i));

					}
					if(isKeyUpper)
					{
						hb.put(resultSetmd.getColumnName(i).toUpperCase(),value);
					}
					else
					{
						hb.put(resultSetmd.getColumnName(i).toLowerCase(),value);
					}
				}
				al.add(hb);
			}
			resultSet.close();
			statement.close();
			if(connInput==null)
			{
				connection.close();
			}
		}
		catch (Exception  ex)
		{

			ex.printStackTrace();
		}
		finally
		{
			try
			{
				try
				{
					resultSet.close();
				}
				catch(Exception e1)
				{
					e1.printStackTrace();	
				}
				try
				{
					statement.close();
				}
				catch(Exception e1)
				{
					e1.printStackTrace();	
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();	
			}
			try {
				if(connection!=null)
				{
					if(!connection.isClosed())
					{
						if(connInput==null)
						{
							connection.close();
						}
						
					}
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return al;
	}
	public  int updateDb(String sql) throws Exception
	{   
		int countUpdate=0;
		Connection connection = null;
		Statement statement=null;
		try
		{
			connection =getConnection();
			statement=connection.createStatement();
			
			long start=System.currentTimeMillis();
			countUpdate=statement.executeUpdate(sql);
			long end=System.currentTimeMillis();
			
			statement.close();
			connection.close();
		}
		catch (Exception  ex)
		{

			ex.printStackTrace();

		}
		finally
		{
			try
			{
				try
				{
					statement.close();
				}
				catch(Exception e1)
				{
					e1.printStackTrace();	
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();	
			}
			try {
				if(connection!=null)
				{
					if(!connection.isClosed())
					{
						connection.close();
					}
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return countUpdate;
	}
	
	public static String driverName ="com.mysql.jdbc.Driver";
	public static String url="jdbc:mysql://ch6:3306/windpower_monitor?characterEncoding=UTF-8";
	public static String username="root";
	public static String password="appsoft";
	
	public static Connection getConnection() {
	    try {
	    	PropertiesValue pv=PropertiesValue.getPropValueInstance();
	    	HashMap<String, String> map = pv.getPropMap();
	    	driverName = map.get("as.db.driver");
	    	url=map.get("as.db.url");
	    	username=map.get("as.db.user");
	    	password=map.get("as.db.password");
	        Class.forName(driverName);
	        Connection conn = DriverManager.getConnection(url, username, password);
	        return conn;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    }
	}

}