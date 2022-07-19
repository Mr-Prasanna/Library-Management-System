package com.jmi.librarymanagment.dbconnection;

import java.io.*;
import java.sql.*;
import java.util.Properties;

public class DbConnector {

    public static final	 String driver = "db.driver.class";
	public static final String url = "db.conn.url";
	public static final String username = "db.username";
	public static final String pwd = "db.password";

	public static Connection connect() {
		Connection conn = null;
		Properties properties = null;
		try {
			properties = new Properties();
			properties.load(
					new FileInputStream("E:\\Prasnna\\JAVA\\java\\LibraryManagement\\resource\\config.properties"));
			Class.forName(properties.getProperty(driver));
			conn = DriverManager.getConnection(properties.getProperty(url), properties.getProperty(username),
					properties.getProperty(pwd));

		} catch (Exception e) {
			e.printStackTrace();
		}

		return conn;
	}
}