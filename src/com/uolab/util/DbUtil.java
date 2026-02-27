package com.uolab.util;
/*
中北大学 软件学院 2413040317 李柯延
*/

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author likeyan
 * @version 1.0
 */
public class DbUtil {
    public static void main(String[] args) {
        System.out.println(DbUtil.getConnection());
    }

    /**
     * 得到数据库连接
     *
     * @return conn 要被关闭的数据库连接
     */
    public static Connection getConnection() {
        Connection conn = null;
        //定义连接字符串
        String url = "jdbc:mysql://localhost:3306/uolab_system?useSSL=false&serverTimezone=UTC";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, "root", "123456");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return conn;
    }

    public static void close(Connection conn, Statement stmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
        } catch (SQLException e) {
        }
        try {
            if (stmt != null) stmt.close();
        } catch (SQLException e) {
        }
        close(conn);
    }

    public static void close(Connection conn, Statement stmt) {
        close(conn, stmt, null);
    }

    public static void close(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
                System.out.println("✅ 连接已关闭");
            } catch (SQLException e) {
                System.err.println("关闭连接出错: " + e.getMessage());
            }
        }
    }
}