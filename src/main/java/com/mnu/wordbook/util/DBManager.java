package com.mnu.wordbook.util;

import java.sql.*;

import javax.naming.spi.DirStateFactory.Result;
public class DBManager {
	
	
	public static Connection getConnection() {
		
		Connection conn = null;
		String myDriver="oracle.jdbc.OracleDriver";
		String myURL="jdbc:oracle:thin:@localhost:1521:xe";
		String myID="";
		String myPass="";
		try {
			//1. 드라이버 로딩
			Class.forName(myDriver);
			conn = DriverManager.getConnection(myURL,myID,myPass);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return conn;
	}
	public static void close(Connection conn, PreparedStatement pstmt, ResultSet rs) {
        try {
           rs.close();
           pstmt.close();
           conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	public static void close(Connection conn, PreparedStatement pstmt) {
        try {
            pstmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
