package com.zhj.helper;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

/**
 * 用于完成最基本的数据库操作
 * @author ZhangHuijun
 *
 */
public class DataBaseUnit {
	private static Connection mConnection = null;//数据库连接
	private static String mURL = "jdbc:mysql://127.0.0.1:3306/jd_phone?seUnicode=true&characterEncoding=UTF8";
	private static String mUser="admin";
	private static String mPwd="admin";
	private static Logger mLogger = Logger.getLogger(DataBaseUnit.class.getName());
	private DataBaseUnit(){
		try{
			if(mConnection == null){
				Class.forName("com.mysql.jdbc.Driver");//加载数据库驱动
				// 与数据库建立连接
				mConnection = DriverManager.getConnection(mURL,mUser,mPwd);
			}else{
				return;
			}
		}catch(Exception e){
			e.printStackTrace();
			mLogger.error("链接异常："+e);
		}
	}
	
	/*
	 * 执行数据库操作
	 */
	public static ResultSet executeQuery(String sql){
		
		try{
			if(mConnection == null)
				new DataBaseUnit();
			//执行数据库查询
			return mConnection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE).executeQuery(sql);
			
		}catch(SQLException e){
			e.printStackTrace();
			mLogger.error("查询异常："+e);
		}
		return null;		
	}
	/*
	 * 执行数据库更新操作
	 */
	public static int execteUpdate(String sql){
		try{
			if(mConnection == null){
				new DataBaseUnit();
			}
			return mConnection.createStatement().executeUpdate(sql);
		}catch(SQLException e){
			e.printStackTrace();
			mLogger.error("插入异常 ："+e);
			return -1;
		}finally{
		}
	}
	/*
	 * 关闭连接
	 */
	public static void close(){
		try{
			if(mConnection != null){
				mConnection.close();
			}
		}catch(SQLException e){
			e.printStackTrace();
			mLogger.error("关闭异常："+e);
		}finally{
			mConnection = null;
		}
	}
}
