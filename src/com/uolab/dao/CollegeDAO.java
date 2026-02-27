package com.uolab.dao;

import com.uolab.entity.College;
import com.uolab.util.DbUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CollegeDAO {

    // 获取所有院系
    public List<College> getAllColleges() {
        List<College> colleges = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = DbUtil.getConnection();
            stmt = conn.createStatement();
            String sql = "SELECT * FROM college ORDER BY college_code";
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                College college = new College();
                college.setCollegeId(rs.getInt("college_id"));
                college.setCollegeName(rs.getString("college_name"));
                college.setCollegeCode(rs.getString("college_code"));
                college.setRemark(rs.getString("remark"));
                colleges.add(college);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtil.close(conn, stmt, rs);
        }
        return colleges;
    }

    // 添加院系
    public boolean addCollege(College college) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DbUtil.getConnection();
            String sql = "INSERT INTO college (college_name, college_code, remark) VALUES (?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, college.getCollegeName());
            pstmt.setString(2, college.getCollegeCode());
            pstmt.setString(3, college.getRemark());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DbUtil.close(conn, pstmt);
        }
    }
}

