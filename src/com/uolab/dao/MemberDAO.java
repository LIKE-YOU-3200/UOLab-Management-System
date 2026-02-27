/*
中北大学 软件学院 2413040317 李柯延
*/
package com.uolab.dao;

/**
 * @author likeyan
 * @version 1.0
 */
import com.uolab.entity.Member;
import com.uolab.util.DbUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MemberDAO {

    // 添加实验室成员
    public boolean addMember(Member member) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DbUtil.getConnection();
            String sql = "INSERT INTO member (stu_id, name, gender, grade, hometown, phone, " +
                    "college_id, major_id, student_position, join_date, status, " +
                    "lab_position, remark, create_time) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW())";

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, member.getStuId());
            pstmt.setString(2, member.getName());
            pstmt.setString(3, member.getGender());
            pstmt.setString(4, member.getGrade());
            pstmt.setString(5, member.getHometown());
            pstmt.setString(6, member.getPhone());
            pstmt.setInt(7, member.getCollegeId());
            pstmt.setInt(8, member.getMajorId());
            pstmt.setString(9, member.getStudentPosition());
            pstmt.setDate(10, new java.sql.Date(member.getJoinDate().getTime()));
            pstmt.setString(11, member.getStatus());
            pstmt.setString(12, member.getLabPosition());
            pstmt.setString(13, member.getRemark());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DbUtil.close(conn, pstmt);
        }
    }

    // 修改成员信息（只能修改特定字段）
    public boolean updateMember(Member member) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DbUtil.getConnection();
            String sql = "UPDATE member SET phone = ?, student_position = ?, " +
                    "status = ?, lab_position = ?, remark = ?, update_time = NOW() " +
                    "WHERE member_id = ?";

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, member.getPhone());
            pstmt.setString(2, member.getStudentPosition());
            pstmt.setString(3, member.getStatus());
            pstmt.setString(4, member.getLabPosition());
            pstmt.setString(5, member.getRemark());
            pstmt.setInt(6, member.getMemberId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DbUtil.close(conn, pstmt);
        }
    }

    // 根据ID获取成员信息
    public Member getMemberById(int memberId) {
        Member member = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DbUtil.getConnection();
            String sql = "SELECT m.*, c.college_name, maj.major_name " +
                    "FROM member m " +
                    "LEFT JOIN college c ON m.college_id = c.college_id " +
                    "LEFT JOIN major maj ON m.major_id = maj.major_id " +
                    "WHERE m.member_id = ?";

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, memberId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                member = new Member();
                member.setMemberId(rs.getInt("member_id"));
                member.setStuId(rs.getString("stu_id"));
                member.setName(rs.getString("name"));
                member.setGender(rs.getString("gender"));
                member.setGrade(rs.getString("grade"));
                member.setHometown(rs.getString("hometown"));
                member.setPhone(rs.getString("phone"));
                member.setCollegeId(rs.getInt("college_id"));
                member.setMajorId(rs.getInt("major_id"));
                member.setStudentPosition(rs.getString("student_position"));
                member.setJoinDate(rs.getDate("join_date"));
                member.setStatus(rs.getString("status"));
                member.setLabPosition(rs.getString("lab_position"));
                member.setRemark(rs.getString("remark"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtil.close(conn, pstmt, rs);
        }
        return member;
    }

    // 多条件查询成员
    public List<Member> findMembers(String name, String grade, String phone,
                                    Integer collegeId, Integer majorId,
                                    String status, Boolean hasManagementPosition) {
        List<Member> members = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DbUtil.getConnection();
            StringBuilder sql = new StringBuilder(
                    "SELECT m.*, c.college_name, maj.major_name " +
                            "FROM member m " +
                            "LEFT JOIN college c ON m.college_id = c.college_id " +
                            "LEFT JOIN major maj ON m.major_id = maj.major_id " +
                            "WHERE 1=1"
            );

            List<Object> params = new ArrayList<>();
            int paramIndex = 1;

            if (name != null && !name.trim().isEmpty()) {
                sql.append(" AND m.name LIKE ?");
                params.add("%" + name + "%");
            }

            if (grade != null && !grade.trim().isEmpty()) {
                sql.append(" AND m.grade = ?");
                params.add(grade);
            }

            if (phone != null && !phone.trim().isEmpty()) {
                sql.append(" AND m.phone LIKE ?");
                params.add("%" + phone + "%");
            }

            if (collegeId != null && collegeId > 0) {
                sql.append(" AND m.college_id = ?");
                params.add(collegeId);
            }

            if (majorId != null && majorId > 0) {
                sql.append(" AND m.major_id = ?");
                params.add(majorId);
            }

            if (status != null && !status.trim().isEmpty() && !"全部".equals(status)) {
                sql.append(" AND m.status = ?");
                params.add(status);
            }

            if (hasManagementPosition != null) {
                if (hasManagementPosition) {
                    sql.append(" AND m.lab_position != '无'");
                } else {
                    sql.append(" AND (m.lab_position = '无' OR m.lab_position IS NULL)");
                }
            }

            sql.append(" ORDER BY m.create_time DESC");

            pstmt = conn.prepareStatement(sql.toString());
            for (Object param : params) {
                pstmt.setObject(paramIndex++, param);
            }

            rs = pstmt.executeQuery();

            while (rs.next()) {
                Member member = new Member();
                member.setMemberId(rs.getInt("member_id"));
                member.setStuId(rs.getString("stu_id"));
                member.setName(rs.getString("name"));
                member.setGender(rs.getString("gender"));
                member.setGrade(rs.getString("grade"));
                member.setHometown(rs.getString("hometown"));
                member.setPhone(rs.getString("phone"));
                member.setCollegeId(rs.getInt("college_id"));
                member.setMajorId(rs.getInt("major_id"));
                member.setStudentPosition(rs.getString("student_position"));
                member.setJoinDate(rs.getDate("join_date"));
                member.setStatus(rs.getString("status"));
                member.setLabPosition(rs.getString("lab_position"));
                member.setRemark(rs.getString("remark"));
                member.setCreateTime(rs.getTimestamp("create_time"));

                members.add(member);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtil.close(conn, pstmt, rs);
        }
        return members;
    }

    // 检查学号是否已存在
    public boolean isStuIdExists(String stuId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DbUtil.getConnection();
            String sql = "SELECT COUNT(*) FROM member WHERE stu_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, stuId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DbUtil.close(conn, pstmt, rs);
        }
    }

    // 按学院统计
    public List<Object[]> statByCollege(Integer collegeId) {
        List<Object[]> result = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DbUtil.getConnection();
            String sql =
                    "SELECT " +
                            "    c.college_name as 学院, " +
                            "    COUNT(*) as 人数, " +
                            "    SUM(CASE WHEN gender = 'M' THEN 1 ELSE 0 END) as 男生, " +
                            "    SUM(CASE WHEN gender = 'F' THEN 1 ELSE 0 END) as 女生, " +
                            "    COUNT(CASE WHEN lab_position != '无' AND lab_position IS NOT NULL THEN 1 END) as 管理人员 " +
                            "FROM member m " +
                            "LEFT JOIN college c ON m.college_id = c.college_id " +
                            (collegeId != null ? "WHERE m.college_id = ? " : "") +
                            "GROUP BY c.college_name ORDER BY 人数 DESC";

            pstmt = conn.prepareStatement(sql);
            if (collegeId != null) {
                pstmt.setInt(1, collegeId);
            }

            rs = pstmt.executeQuery();

            while (rs.next()) {
                Object[] row = new Object[5];
                row[0] = rs.getString("学院");
                row[1] = rs.getInt("人数");
                row[2] = rs.getInt("男生");
                row[3] = rs.getInt("女生");
                row[4] = rs.getInt("管理人员");
                result.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtil.close(conn, pstmt, rs);
        }
        return result;
    }

    // 按年级统计
    public List<Object[]> statByGrade(String grade) {
        List<Object[]> result = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DbUtil.getConnection();
            String sql =
                    "SELECT " +
                            "    grade as 年级, " +
                            "    COUNT(*) as 人数, " +
                            "    SUM(CASE WHEN gender = 'M' THEN 1 ELSE 0 END) as 男生, " +
                            "    SUM(CASE WHEN gender = 'F' THEN 1 ELSE 0 END) as 女生, " +
                            "    COUNT(CASE WHEN lab_position != '无' AND lab_position IS NOT NULL THEN 1 END) as 管理人员 " +
                            "FROM member " +
                            (grade != null ? "WHERE grade = ? " : "") +
                            "GROUP BY grade ORDER BY grade DESC";

            pstmt = conn.prepareStatement(sql);
            if (grade != null) {
                pstmt.setString(1, grade);
            }

            rs = pstmt.executeQuery();

            while (rs.next()) {
                Object[] row = new Object[5];
                row[0] = rs.getString("年级");
                row[1] = rs.getInt("人数");
                row[2] = rs.getInt("男生");
                row[3] = rs.getInt("女生");
                row[4] = rs.getInt("管理人员");
                result.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtil.close(conn, pstmt, rs);
        }
        return result;
    }

    // 按专业统计
    public List<Object[]> statByMajor() {
        List<Object[]> result = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DbUtil.getConnection();
            String sql =
                    "SELECT " +
                            "    maj.major_name as 专业, " +
                            "    COUNT(*) as 人数, " +
                            "    SUM(CASE WHEN gender = 'M' THEN 1 ELSE 0 END) as 男生, " +
                            "    SUM(CASE WHEN gender = 'F' THEN 1 ELSE 0 END) as 女生, " +
                            "    COUNT(CASE WHEN lab_position != '无' AND lab_position IS NOT NULL THEN 1 END) as 管理人员 " +
                            "FROM member m " +
                            "LEFT JOIN major maj ON m.major_id = maj.major_id " +
                            "GROUP BY maj.major_name ORDER BY 人数 DESC";

            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Object[] row = new Object[5];
                row[0] = rs.getString("专业");
                row[1] = rs.getInt("人数");
                row[2] = rs.getInt("男生");
                row[3] = rs.getInt("女生");
                row[4] = rs.getInt("管理人员");
                result.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtil.close(conn, pstmt, rs);
        }
        return result;
    }

    // 按状态统计
    public List<Object[]> statByStatus() {
        List<Object[]> result = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DbUtil.getConnection();
            String sql =
                    "SELECT " +
                            "    status as 状态, " +
                            "    COUNT(*) as 人数, " +
                            "    SUM(CASE WHEN gender = 'M' THEN 1 ELSE 0 END) as 男生, " +
                            "    SUM(CASE WHEN gender = 'F' THEN 1 ELSE 0 END) as 女生, " +
                            "    COUNT(CASE WHEN lab_position != '无' AND lab_position IS NOT NULL THEN 1 END) as 管理人员 " +
                            "FROM member " +
                            "GROUP BY status ORDER BY FIELD(status, '正常', '退出', '毕业')";

            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Object[] row = new Object[5];
                row[0] = rs.getString("状态");
                row[1] = rs.getInt("人数");
                row[2] = rs.getInt("男生");
                row[3] = rs.getInt("女生");
                row[4] = rs.getInt("管理人员");
                result.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtil.close(conn, pstmt, rs);
        }
        return result;
    }
    public List<Member> getAllMembers() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        List<Member> memberList = new ArrayList<>();

        try {
            conn = DbUtil.getConnection();
            stmt = conn.createStatement();
            // 这里可能需要根据您的实际表结构调整SQL语句
            String sql = "SELECT * FROM member ORDER BY join_date DESC";
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Member member = new Member();
                member.setMemberId(rs.getInt("member_id"));
                member.setStuId(rs.getString("stu_id"));
                member.setName(rs.getString("name"));
                member.setGender(rs.getString("gender"));
                member.setGrade(rs.getString("grade"));
                member.setHometown(rs.getString("hometown"));
                member.setPhone(rs.getString("phone"));
                member.setCollegeId(rs.getInt("college_id"));
                member.setMajorId(rs.getInt("major_id"));
                member.setStudentPosition(rs.getString("student_position"));
                member.setJoinDate(rs.getDate("join_date"));
                member.setStatus(rs.getString("status"));
                member.setLabPosition(rs.getString("lab_position"));
                member.setRemark(rs.getString("remark"));

                memberList.add(member);
            }
            return memberList;
        } catch (SQLException e) {
            e.printStackTrace();
            return memberList;
        } finally {
            DbUtil.close(conn, stmt, rs);
        }
    }
}
