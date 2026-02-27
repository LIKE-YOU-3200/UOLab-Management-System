/*
中北大学 软件学院 2413040317 李柯延
*/
package com.uolab.dao;

import com.uolab.entity.Competition;
import com.uolab.util.DbUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author likeyan
 * @version 1.0
 */
public class CompetitionDAO {

    // 添加参赛信息
    public int addCompetition(Competition competition) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DbUtil.getConnection();
            String sql = "INSERT INTO competition (category, title, year, leader, " +
                    "members, award_level, advisor, remark) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, competition.getCategory());
            pstmt.setString(2, competition.getTitle());
            pstmt.setInt(3, competition.getYear());
            pstmt.setString(4, competition.getLeader());
            pstmt.setString(5, competition.getMembers());
            pstmt.setString(6, competition.getAwardLevel());
            pstmt.setString(7, competition.getAdvisor());
            pstmt.setString(8, competition.getRemark());

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

    // 根据ID获取参赛信息
    public Competition getCompetitionById(int competitionId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DbUtil.getConnection();
            String sql = "SELECT * FROM competition WHERE competition_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, competitionId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                Competition competition = new Competition();
                competition.setCompetitionId(rs.getInt("competition_id"));
                competition.setCategory(rs.getString("category"));
                competition.setTitle(rs.getString("title"));
                competition.setYear(rs.getInt("year"));
                competition.setLeader(rs.getString("leader"));
                competition.setMembers(rs.getString("members"));
                competition.setAwardLevel(rs.getString("award_level"));
                competition.setAdvisor(rs.getString("advisor"));
                competition.setRemark(rs.getString("remark"));
                competition.setCreateTime(rs.getTimestamp("create_time"));

                return competition;
            }
            return null;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            DbUtil.close(conn, pstmt, rs);
        }
    }

    // 根据年度查询参赛信息
    public List<Competition> getCompetitionsByYear(Integer year) {
        List<Competition> competitions = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DbUtil.getConnection();
            StringBuilder sql = new StringBuilder("SELECT * FROM competition WHERE 1=1");

            if (year != null) {
                sql.append(" AND year = ?");
            }
            sql.append(" ORDER BY year DESC, create_time DESC");

            pstmt = conn.prepareStatement(sql.toString());

            if (year != null) {
                pstmt.setInt(1, year);
            }

            rs = pstmt.executeQuery();

            while (rs.next()) {
                Competition competition = new Competition();
                competition.setCompetitionId(rs.getInt("competition_id"));
                competition.setCategory(rs.getString("category"));
                competition.setTitle(rs.getString("title"));
                competition.setYear(rs.getInt("year"));
                competition.setLeader(rs.getString("leader"));
                competition.setMembers(rs.getString("members"));
                competition.setAwardLevel(rs.getString("award_level"));
                competition.setAdvisor(rs.getString("advisor"));
                competition.setRemark(rs.getString("remark"));
                competition.setCreateTime(rs.getTimestamp("create_time"));

                competitions.add(competition);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtil.close(conn, pstmt, rs);
        }
        return competitions;
    }

    // 更新参赛信息（主要更新组员和获奖等级）
    public boolean updateCompetition(Competition competition) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DbUtil.getConnection();
            String sql = "UPDATE competition SET members = ?, award_level = ?, remark = ? " +
                    "WHERE competition_id = ?";

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, competition.getMembers());
            pstmt.setString(2, competition.getAwardLevel());
            pstmt.setString(3, competition.getRemark());
            pstmt.setInt(4, competition.getCompetitionId());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DbUtil.close(conn, pstmt);
        }
    }

    // 获取所有竞赛类别（用于下拉框）
    public List<String> getAllCategories() {
        List<String> categories = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DbUtil.getConnection();
            String sql = "SELECT DISTINCT category FROM competition ORDER BY category";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                categories.add(rs.getString("category"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtil.close(conn, pstmt, rs);
        }
        return categories;
    }

    // 获取所有参赛年度（用于下拉框）
    public List<Integer> getAllYears() {
        List<Integer> years = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DbUtil.getConnection();
            String sql = "SELECT DISTINCT year FROM competition ORDER BY year DESC";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                years.add(rs.getInt("year"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtil.close(conn, pstmt, rs);
        }
        return years;
    }
}
