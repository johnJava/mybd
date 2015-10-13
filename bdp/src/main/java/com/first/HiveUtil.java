package com.first;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.DriverManager;


public class HiveUtil {
	private static String driverName = "org.apache.hive.jdbc.HiveDriver";

	public static void main(String[] args) throws SQLException {
		try {
			Class.forName(driverName);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		}
		//!connect jdbc:hive2://192.168.8.11:10000 scott tiger org.apache.hive.jdbc.HiveDriver
		//!connect jdbc:hive2://192.168.8.11:10000/default scott tiger org.apache.hive.jdbc.HiveDriver
		//!connect jdbc:hive2://192.168.8.11:10000 scott tiger org.apache.hive.jdbc.HiveDriver
		Connection con = DriverManager.getConnection("jdbc:hive2://cdh5:10000/default;auth=NoSasl", "scott", "tiger");
	    Statement stmt = con.createStatement();
		String tableName = "eamhive";
		stmt.execute("drop table if exists " + tableName);
		stmt.execute("create table " + tableName + " (key int, value string)");
		System.out.println("Create table success!");
		// show tables
		String sql = "show tables '" + tableName + "'";
		System.out.println("Running: " + sql);
		ResultSet res = stmt.executeQuery(sql);
		if (res.next()) {
			System.out.println(res.getString(1));
		}

		// describe table
		sql = "describe " + tableName;
		System.out.println("Running: " + sql);
		res = stmt.executeQuery(sql);
		while (res.next()) {
			System.out.println(res.getString(1) + "\t" + res.getString(2));
		}

		sql = "LOAD DATA LOCAL INPATH '/opt/hivetemp/kv1.txt' OVERWRITE INTO TABLE eamhive";
		System.out.println("Running: " + sql);
		stmt.executeUpdate(sql);
		sql = "select * from " + tableName;
		res = stmt.executeQuery(sql);
		while (res.next()) {
			System.out.println(String.valueOf(res.getInt(1)) + "\t"
					+ res.getString(2));
		}
	}
}
