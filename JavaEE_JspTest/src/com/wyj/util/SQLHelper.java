package com.wyj.util;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Executable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

import com.wyj.domain.Users;

public class SQLHelper {
	
	static Connection ct = null;
	
	static PreparedStatement ps = null;
	
	static ResultSet rs = null;
	
	static String url ="";
	
	static String user = "";
	
	static String password = "";
	
	static String driver = "";
	
	static Properties pro = null;
	
	static InputStream file = null;

	static{
		try {
			pro = new Properties();
			file = SQLHelper.class.getClassLoader().getResourceAsStream("db.properties");
			pro.load(file); 
			url = pro.getProperty("url");
			user = pro.getProperty("user");
			password = pro.getProperty("password");
			driver = pro.getProperty("driver");
			Class.forName(driver); 
			} catch (Exception e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
				}finally {
				try {
					file.close();
					} catch (IOException e) {
						// TODO 自动生成的 catch 块
					e.printStackTrace();
				}finally {
					file = null;
			}	
		}
	}

	public static Connection getCt() {	
		return ct;
	}
	
	public static PreparedStatement getPs() {
		return ps;
	}

	public static ResultSet getRs() {
		return rs;
	}

	public static Connection getConnection() {
		
		try {
			ct = DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return ct;
	}
	
	public static void executeUpdate(String sql,String[]parameters) {
		
		try {
			ct = getConnection();
			ps = ct.prepareStatement(sql);
			if(parameters!=null) {
				for(int i = 0;i<parameters.length;i++) {
					ps.setString(i+1,parameters[i]);
				}
			} 
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();	
		}finally {
			close(rs,ps,ct);
		}
	}
	
	public ArrayList executeQuery2(String sql,String []parameters){
	ArrayList al=new ArrayList();
	try {
		ct=getConnection();;
		ps=ct.prepareStatement(sql);
		//给sql问号赋值
		for (int i = 0; i < parameters.length; i++){
			ps.setString(i+1, parameters[i]);
			}
		rs=ps.executeQuery(); 
		ResultSetMetaData rsmd=rs.getMetaData();
		//用法rs可以的到有多少列
		int columnNum=rsmd.getColumnCount();
		//循环从a1中取出数据封装到ArrayList中
		while(rs.next()){
			Object []objects=new Object[columnNum];
			for(int i=0;i<objects.length;i++)
			{
				objects[i]=rs.getObject(i+1); //返回对象数组
				}
			al.add(objects);
			}
			return al;
		} catch (Exception e) 
		{
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}finally{
			
		}
}
		
		

	
	public static ResultSet executeQuery(String sql,String[]parameters) {
		
		try {
			ct = getConnection();
			ps = ct.prepareStatement(sql);
			if(parameters!=null) {
				for(int i = 0;i<parameters.length;i++) {
					ps.setString(i+1,parameters[i]);
				}
			}
			rs = ps.executeQuery();
		} catch (SQLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();	
		}
		return rs;
	}
	
	//函数的二次封装，避免调用者手动关闭结果集合，根据业务逻辑的需要，model
	public static ArrayList<Users> executeQueryUser(String sql,String[]parameters){
		
		ArrayList<Users> allUser = new ArrayList<Users>();
		
		try {
			ct = getConnection();
			ps = ct.prepareStatement(sql);
			if(parameters!=null) {
				for(int i = 0;i<parameters.length;i++) {
					ps.setString(i+1, parameters[i]);
				}
			}
			rs = ps.executeQuery();
			while(rs.next()) {
				Users user = new Users();
				user.setId(rs.getInt(1));
				user.setUsername(rs.getString(2));
				user.setPassword(rs.getString(3));
				user.setGrade(rs.getInt(4));
				user.setEmail(rs.getString(5));				
				allUser.add(user);
			}
				 
		} catch (SQLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();	
		}finally {
			close(rs,ps,ct);
		}
		return allUser;
	}
	
	public static void close(ResultSet rs,Statement ps,Connection ct) {
		
		if(rs!=null) {
			try {
				rs.close();
			} catch (SQLException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
		if(ps!=null) {
			try {
				ps.close();
			} catch (SQLException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
		if(ct!=null) {
		try {
			ct.close();
		} catch (SQLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
		
	}

	
	
}

