package com.uolab.dao;

import com.uolab.entity.InnovationCredit;
import com.uolab.util.DbUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 创新学分DAO
 */
public class InnovationCreditDAO {

    // 添加创新学分记录
    public int addCredit(InnovationCredit credit) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DbUtil.getConnection();
            String sql = "INSERT INTO innovation_credit (stu_id, stu_name, grade, college, " +
                    "major, credit_value, credit_type, reason, acquire_date, " +
                    "record_time, recorder, remark) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, NOW(), ?, ?)";

            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, credit.getStuId());
            pstmt.setString(2, credit.getStuName());
            pstmt.setString(3, credit.getGrade());
            pstmt.setString(4, credit.getCollege());
            pstmt.setString(5, credit.getMajor());
            pstmt.setDouble(6, credit.getCreditValue());
            pstmt.setString(7, credit.getCreditType());
            pstmt.setString(8, credit.getReason());

            // 转换java.util.Date为java.sql.Date
            java.sql.Date acquireDate = null;
            if (credit.getAcquireDate() != null) {
                acquireDate = new java.sql.Date(credit.getAcquireDate().getTime());
            }
            pstmt.setDate(9, acquireDate);

            pstmt.setString(10, credit.getRecorder());
            pstmt.setString(11, credit.getRemark());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            return -1;

        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        } finally {
            DbUtil.close(conn, pstmt, rs);
        }
    }

    // 查询创新学分记录 - 使用java.util.Date参数
    public List<InnovationCredit> findCredits(String stuId, String stuName,
                                              java.util.Date startDate, java.util.Date endDate) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DbUtil.getConnection();
            StringBuilder sql = new StringBuilder(
                    "SELECT * FROM innovation_credit WHERE 1=1");
            List<Object> params = new ArrayList<>();

            if (stuId != null && !stuId.trim().isEmpty()) {
                sql.append(" AND stu_id LIKE ?");
                params.add("%" + stuId + "%");
            }

            if (stuName != null && !stuName.trim().isEmpty()) {
                sql.append(" AND stu_name LIKE ?");
                params.add("%" + stuName + "%");
            }

            // 处理开始日期
            if (startDate != null) {
                sql.append(" AND acquire_date >= ?");
                params.add(new java.sql.Date(startDate.getTime()));
            }

            // 处理结束日期
            if (endDate != null) {
                sql.append(" AND acquire_date <= ?");
                params.add(new java.sql.Date(endDate.getTime()));
            }

            sql.append(" ORDER BY acquire_date DESC, record_time DESC");

            pstmt = conn.prepareStatement(sql.toString());

            // 设置参数
            for (int i = 0; i < params.size(); i++) {
                Object param = params.get(i);
                if (param instanceof String) {
                    pstmt.setString(i + 1, (String) param);
                } else if (param instanceof java.sql.Date) {
                    pstmt.setDate(i + 1, (java.sql.Date) param);
                }
            }

            rs = pstmt.executeQuery();

            List<InnovationCredit> credits = new ArrayList<>();
            while (rs.next()) {
                InnovationCredit credit = new InnovationCredit();
                credit.setCreditId(rs.getInt("credit_id"));
                credit.setStuId(rs.getString("stu_id"));
                credit.setStuName(rs.getString("stu_name"));
                credit.setGrade(rs.getString("grade"));
                credit.setCollege(rs.getString("college"));
                credit.setMajor(rs.getString("major"));
                credit.setCreditValue(rs.getDouble("credit_value"));
                credit.setCreditType(rs.getString("credit_type"));
                credit.setReason(rs.getString("reason"));
                credit.setAcquireDate(rs.getDate("acquire_date"));
                credit.setRecordTime(rs.getTimestamp("record_time"));
                credit.setRecorder(rs.getString("recorder"));
                credit.setRemark(rs.getString("remark"));

                credits.add(credit);
            }

            return credits;

        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            DbUtil.close(conn, pstmt, rs);
        }
    }

    // 根据学号查询总学分数
    public double getTotalCreditByStuId(String stuId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DbUtil.getConnection();
            String sql = "SELECT SUM(credit_value) as total_credit FROM innovation_credit WHERE stu_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, stuId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getDouble("total_credit");
            }
            return 0.0;

        } catch (SQLException e) {
            e.printStackTrace();
            return 0.0;
        } finally {
            DbUtil.close(conn, pstmt, rs);
        }
    }

    // 统计指定时间段的学分记录 - 使用java.util.Date参数
    public List<InnovationCredit> getCreditsByDateRange(java.util.Date startDate, java.util.Date endDate) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DbUtil.getConnection();
            String sql = "SELECT * FROM innovation_credit WHERE acquire_date BETWEEN ? AND ? " +
                    "ORDER BY college, stu_id, acquire_date DESC";

            pstmt = conn.prepareStatement(sql);

            // 设置开始日期
            if (startDate != null) {
                pstmt.setDate(1, new java.sql.Date(startDate.getTime()));
            } else {
                // 如果没有开始日期，设置为最小日期
                pstmt.setDate(1, java.sql.Date.valueOf("2000-01-01"));
            }

            // 设置结束日期
            if (endDate != null) {
                pstmt.setDate(2, new java.sql.Date(endDate.getTime()));
            } else {
                // 如果没有结束日期，设置为最大日期
                pstmt.setDate(2, java.sql.Date.valueOf("2100-12-31"));
            }

            rs = pstmt.executeQuery();

            List<InnovationCredit> credits = new ArrayList<>();
            while (rs.next()) {
                InnovationCredit credit = new InnovationCredit();
                credit.setCreditId(rs.getInt("credit_id"));
                credit.setStuId(rs.getString("stu_id"));
                credit.setStuName(rs.getString("stu_name"));
                credit.setGrade(rs.getString("grade"));
                credit.setCollege(rs.getString("college"));
                credit.setMajor(rs.getString("major"));
                credit.setCreditValue(rs.getDouble("credit_value"));
                credit.setCreditType(rs.getString("credit_type"));
                credit.setReason(rs.getString("reason"));
                credit.setAcquireDate(rs.getDate("acquire_date"));
                credit.setRecordTime(rs.getTimestamp("record_time"));
                credit.setRecorder(rs.getString("recorder"));
                credit.setRemark(rs.getString("remark"));

                credits.add(credit);
            }

            return credits;

        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        } finally {
            DbUtil.close(conn, pstmt, rs);
        }
    }

    // 获取所有学分类别
    public List<String> getAllCreditTypes() {
        List<String> types = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DbUtil.getConnection();
            String sql = "SELECT DISTINCT credit_type FROM innovation_credit ORDER BY credit_type";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                types.add(rs.getString("credit_type"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtil.close(conn, pstmt, rs);
        }
        return types;
    }

    // 删除所有其他可能的重载方法，只保留上述方法
}