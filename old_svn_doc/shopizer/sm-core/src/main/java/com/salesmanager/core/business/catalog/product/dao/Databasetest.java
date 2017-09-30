package com.salesmanager.core.business.catalog.product.dao;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

public class Databasetest {
	 private static int count; 

	  public static Connection getConnection() throws SQLException, 
	             java.lang.ClassNotFoundException 
	    {	      
	//(1)加载MySQL的JDBC的驱动	  
		  		Class.forName("com.mysql.jdbc.Driver");	
	//取得连接的url,能访问MySQL数据库的用户名,密码,数据库名	
	         String url = "jdbc:mysql://123.59.49.58:3306/SALESMANAGER?autoReconnect=true&useUnicode=true&characterEncoding=UTF-8";        
	         String username = "bshopizer"; 
	         String password ="mySQL@2015Bio"; 
	      //(2)创建与MySQL数据库的连接类的实例 
	       
	         Connection con = (Connection) DriverManager.getConnection(url, username, password);        
	        return con;  
	    }
	  
	  
	  public static void main(String args[]) { 
	        try 
	        { 
	            //(3)获取连接实例con,用con创建Statement对象实例 sql_statement 
	             Connection con = getConnection();            
	             Statement sql_statement = (Statement) con.createStatement(); 

	           //(4)执行查询,用ResultSet类的对象,返回查询结果	           
	             String query = "SELECT a.AUTH_ID ,a.AUTH_TYPE from AUTHORIZATION a, MERCHANT_STORE e,AUTH_BRAND d WHERE a.MERCHANT_ID = e.MERCHANT_ID AND d.AUTH_ID = a.AUTH_ID AND d.brand_id=? AND a.MERCHANT_ID = ?"; 
	             PreparedStatement sta = con.prepareStatement(query);
	             String id = "1";
	             String merchantId = "350";
	             sta.setString(1, id);
	             sta.setString(2, merchantId);
	             ResultSet result = sta.executeQuery(); 
	            
	           //对获得的查询结果进行处理,对Result类的对象进行操作 
	            while (result.next()) 
	            { 
	                String number=result.getString("AUTH_ID"); 
	                System.out.println(number);
	                 String name=result.getString("AUTH_TYPE"); 
	                 System.out.println(name);
	             } 
	            
	           //关闭连接和声明	            
	            sql_statement.close(); 
	             con.close(); 
	            
	         } catch(java.lang.ClassNotFoundException e) { 
	           //加载JDBC错误,所要用的驱动没有找到	        
	                         System.err.print("ClassNotFoundException"); 
	           //其他错误             

	             System.err.println(e.getMessage()); 
	         } catch (SQLException ex) { 

	            //显示数据库连接错误或查询错误	             
	                  System.err.println("SQLException: " + ex.getMessage()); 
	         } 
	     } 
	} 
