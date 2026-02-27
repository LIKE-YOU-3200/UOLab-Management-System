package com.uolab.dao;

import com.uolab.entity.User;
import com.uolab.util.DbUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户DAO
 */
public class UserDAO {

    // 添加用户（仅超级管理员可调用）
    public int addUser(User user) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DbUtil.getConnection();
            String sql = "INSERT INTO user (username, password, role_id, status, real_name, " +
                    "college_id, major_id, grade, class_name, student_position, " +
                    "phone, qq, remark) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setInt(3, user.getRoleId());
            pstmt.setString(4, user.getStatus());
            pstmt.setString(5, user.getRealName());
            pstmt.setInt(6, user.getCollegeId());
            pstmt.setInt(7, user.getMajorId());
            pstmt.setString(8, user.getGrade());
            pstmt.setString(9, user.getClassName());
            pstmt.setString(10, user.getStudentPosition());
            pstmt.setString(11, user.getPhone());
            pstmt.setString(12, user.getQq());
            pstmt.setString(13, user.getRemark());

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

    // 根据用户名查询用户（用于登录验证）
    public User getUserByUsername(String username) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DbUtil.getConnection();
            String sql = "SELECT u.*, c.college_name, m.major_name FROM user u " +
                    "LEFT JOIN college c ON u.college_id = c.college_id " +
                    "LEFT JOIN major m ON u.major_id = m.major_id " +
                    "WHERE u.username = ?";

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return createUserFromResultSet(rs);
            }
            return null;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            DbUtil.close(conn, pstmt, rs);
        }
    }

    // 根据ID查询用户
    public User getUserById(int userId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DbUtil.getConnection();
            String sql = "SELECT u.*, c.college_name, m.major_name FROM user u " +
                    "LEFT JOIN college c ON u.college_id = c.college_id " +
                    "LEFT JOIN major m ON u.major_id = m.major_id " +
                    "WHERE u.user_id = ?";

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return createUserFromResultSet(rs);
            }
            return null;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            DbUtil.close(conn, pstmt, rs);
        }
    }

    // 查询所有用户
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DbUtil.getConnection();
            String sql = "SELECT u.*, c.college_name, m.major_name FROM user u " +
                    "LEFT JOIN college c ON u.college_id = c.college_id " +
                    "LEFT JOIN major m ON u.major_id = m.major_id " +
                    "ORDER BY u.user_id DESC";

            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                users.add(createUserFromResultSet(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtil.close(conn, pstmt, rs);
        }
        return users;
    }

    // 组合查询用户
    public List<User> findUsers(String realName, Integer collegeId, Integer majorId,
                                String grade, String className) {
        List<User> users = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DbUtil.getConnection();
            StringBuilder sql = new StringBuilder(
                    "SELECT u.*, c.college_name, m.major_name FROM user u " +
                            "LEFT JOIN college c ON u.college_id = c.college_id " +
                            "LEFT JOIN major m ON u.major_id = m.major_id WHERE 1=1"
            );

            List<Object> params = new ArrayList<>();

            if (realName != null && !realName.trim().isEmpty()) {
                sql.append(" AND u.real_name LIKE ?");
                params.add("%" + realName + "%");
            }

            if (collegeId != null && collegeId > 0) {
                sql.append(" AND u.college_id = ?");
                params.add(collegeId);
            }

            if (majorId != null && majorId > 0) {
                sql.append(" AND u.major_id = ?");
                params.add(majorId);
            }

            if (grade != null && !grade.trim().isEmpty()) {
                sql.append(" AND u.grade = ?");
                params.add(grade);
            }

            if (className != null && !className.trim().isEmpty()) {
                sql.append(" AND u.class_name = ?");
                params.add(className);
            }

            sql.append(" ORDER BY u.user_id DESC");

            pstmt = conn.prepareStatement(sql.toString());
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }

            rs = pstmt.executeQuery();

            while (rs.next()) {
                users.add(createUserFromResultSet(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtil.close(conn, pstmt, rs);
        }
        return users;
    }

    // 更新用户信息（手机号、QQ、校内职务、角色、备注）
    public boolean updateUser(User user) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DbUtil.getConnection();
            String sql = "UPDATE user SET phone = ?, qq = ?, student_position = ?, " +
                    "role_id = ?, remark = ?, update_time = CURRENT_TIMESTAMP " +
                    "WHERE user_id = ?";

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, user.getPhone());
            pstmt.setString(2, user.getQq());
            pstmt.setString(3, user.getStudentPosition());
            pstmt.setInt(4, user.getRoleId());
            pstmt.setString(5, user.getRemark());
            pstmt.setInt(6, user.getUserId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DbUtil.close(conn, pstmt);
        }
    }

    // 重置密码为123456
    public boolean resetPassword(int userId) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DbUtil.getConnection();
            String sql = "UPDATE user SET password = '123456', update_time = CURRENT_TIMESTAMP " +
                    "WHERE user_id = ?";

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DbUtil.close(conn, pstmt);
        }
    }

    // 启用/禁用用户
    public boolean setUserStatus(int userId, String status) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DbUtil.getConnection();
            String sql = "UPDATE user SET status = ?, update_time = CURRENT_TIMESTAMP " +
                    "WHERE user_id = ?";

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, status);
            pstmt.setInt(2, userId);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DbUtil.close(conn, pstmt);
        }
    }

    // 检查用户是否产生过信息（有相关记录）
    public boolean hasUserGeneratedInfo(int userId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DbUtil.getConnection();

            // 检查用户是否有相关记录（如成员信息、公开课记录等）
            String sql = "SELECT COUNT(*) as count FROM ( " +
                    "SELECT 1 FROM member WHERE user_id = ? UNION " +
                    "SELECT 1 FROM course WHERE creator_id = ? UNION " +
                    "SELECT 1 FROM competition WHERE creator_id = ? " +
                    ") as temp";

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            pstmt.setInt(2, userId);
            pstmt.setInt(3, userId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("count") > 0;
            }
            return false;

        } catch (SQLException e) {
            e.printStackTrace();
            return true; // 如果检查出错，保守起见认为有记录
        } finally {
            DbUtil.close(conn, pstmt, rs);
        }
    }

    // 删除用户（仅限未产生过信息的用户）
    public boolean deleteUser(int userId) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DbUtil.getConnection();
            String sql = "DELETE FROM user WHERE user_id = ?";

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DbUtil.close(conn, pstmt);
        }
    }

    // 从ResultSet创建User对象的辅助方法
    private User createUserFromResultSet(ResultSet rs) throws SQLException {
        User user = new User();
        user.setUserId(rs.getInt("user_id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setRoleId(rs.getInt("role_id"));
        user.setStatus(rs.getString("status"));
        user.setRealName(rs.getString("real_name"));
        user.setCollegeId(rs.getInt("college_id"));
        user.setMajorId(rs.getInt("major_id"));
        user.setGrade(rs.getString("grade"));
        user.setClassName(rs.getString("class_name"));
        user.setStudentPosition(rs.getString("student_position"));
        user.setPhone(rs.getString("phone"));
        user.setQq(rs.getString("qq"));
        user.setRemark(rs.getString("remark"));
        user.setCreateTime(rs.getTimestamp("create_time"));
        user.setUpdateTime(rs.getTimestamp("update_time"));

        // 设置额外的显示字段
        try {
            user.setRemark(rs.getString("college_name")); // 临时借用remark字段存储院系名称
        } catch (SQLException e) {
            // 忽略，字段可能不存在
        }

        return user;
    }
}