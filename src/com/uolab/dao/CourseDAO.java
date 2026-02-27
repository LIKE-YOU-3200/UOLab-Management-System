/*
中北大学 软件学院 2413040317 李柯延
*/
package com.uolab.dao;

/**
 * @author likeyan
 * @version 1.0
 */
import com.uolab.entity.Course;
import com.uolab.entity.CourseParticipant;
import com.uolab.util.DbUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseDAO {

    // 添加公开课 - 已修复 ✓
    public int addCourse(Course course) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DbUtil.getConnection();
            // 使用正确的字段名
            String sql = "INSERT INTO course (topic, course_date, speaker, organizer, " +
                    "location, target_group, method, credit_value, participants_count, remark) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, course.getTopic());
            pstmt.setDate(2, new java.sql.Date(course.getCourseDate().getTime()));
            pstmt.setString(3, course.getSpeaker());
            pstmt.setString(4, course.getOrganizers());      // 对应organizer字段
            pstmt.setString(5, course.getLocation());
            pstmt.setString(6, course.getGroupDesc());       // 对应target_group字段
            pstmt.setString(7, course.getMethod());
            pstmt.setDouble(8, course.getCredit());          // 对应credit_value字段
            pstmt.setInt(9, course.getAttendance());         // 对应participants_count字段
            pstmt.setString(10, course.getRemark());

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

    // 根据ID获取公开课 - 需要修复 ✗
    public Course getCourseById(int courseId) {
        Course course = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DbUtil.getConnection();
            String sql = "SELECT * FROM course WHERE course_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, courseId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                course = new Course();
                course.setCourseId(rs.getInt("course_id"));
                course.setTopic(rs.getString("topic"));
                course.setCourseDate(rs.getDate("course_date"));
                course.setSpeaker(rs.getString("speaker"));
                course.setOrganizers(rs.getString("organizer"));       // 注意：是organizer，不是organizers
                course.setLocation(rs.getString("location"));
                course.setGroupDesc(rs.getString("target_group"));     // 注意：是target_group，不是group_desc
                course.setMethod(rs.getString("method"));
                course.setCredit(rs.getDouble("credit_value"));        // 注意：是credit_value，不是credit
                course.setAttendance(rs.getInt("participants_count")); // 注意：是participants_count，不是attendance
                course.setRemark(rs.getString("remark"));
                // 注意：表中没有create_time和update_time字段
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtil.close(conn, pstmt, rs);
        }
        return course;
    }

    // 查询公开课 - 需要修复 ✗
    public List<Course> findCourses(String topic, String speaker,
                                    java.sql.Date startDate, java.sql.Date endDate,
                                    String method) {
        List<Course> courses = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DbUtil.getConnection();
            StringBuilder sql = new StringBuilder("SELECT * FROM course WHERE 1=1");
            List<Object> params = new ArrayList<>();
            int paramIndex = 1;

            // 条件构建
            if (topic != null && !topic.trim().isEmpty()) {
                sql.append(" AND topic LIKE ?");
                params.add("%" + topic + "%");
            }

            if (speaker != null && !speaker.trim().isEmpty()) {
                sql.append(" AND speaker LIKE ?");
                params.add("%" + speaker + "%");
            }

            if (startDate != null) {
                sql.append(" AND course_date >= ?");
                params.add(startDate);
            }

            if (endDate != null) {
                sql.append(" AND course_date <= ?");
                params.add(endDate);
            }

            if (method != null && !method.trim().isEmpty() && !"全部".equals(method)) {
                sql.append(" AND method = ?");
                params.add(method);
            }

            sql.append(" ORDER BY course_date DESC");

            pstmt = conn.prepareStatement(sql.toString());
            for (Object param : params) {
                pstmt.setObject(paramIndex++, param);
            }

            rs = pstmt.executeQuery();

            while (rs.next()) {
                Course course = new Course();
                course.setCourseId(rs.getInt("course_id"));
                course.setTopic(rs.getString("topic"));
                course.setCourseDate(rs.getDate("course_date"));
                course.setSpeaker(rs.getString("speaker"));
                course.setOrganizers(rs.getString("organizer"));       // 注意字段名
                course.setLocation(rs.getString("location"));
                course.setGroupDesc(rs.getString("target_group"));     // 注意字段名
                course.setMethod(rs.getString("method"));
                course.setCredit(rs.getDouble("credit_value"));        // 注意字段名
                course.setAttendance(rs.getInt("participants_count")); // 注意字段名
                course.setRemark(rs.getString("remark"));

                courses.add(course);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtil.close(conn, pstmt, rs);
        }
        return courses;
    }

    // 更新公开课 - 需要修复 ✗
    public boolean updateCourse(Course course) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DbUtil.getConnection();
            // 使用正确的字段名
            String sql = "UPDATE course SET topic = ?, course_date = ?, speaker = ?, " +
                    "organizer = ?, location = ?, target_group = ?, method = ?, " +
                    "credit_value = ?, participants_count = ?, remark = ? WHERE course_id = ?";

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, course.getTopic());
            pstmt.setDate(2, new java.sql.Date(course.getCourseDate().getTime()));
            pstmt.setString(3, course.getSpeaker());
            pstmt.setString(4, course.getOrganizers());      // 对应organizer字段
            pstmt.setString(5, course.getLocation());
            pstmt.setString(6, course.getGroupDesc());       // 对应target_group字段
            pstmt.setString(7, course.getMethod());
            pstmt.setDouble(8, course.getCredit());          // 对应credit_value字段
            pstmt.setInt(9, course.getAttendance());         // 对应participants_count字段
            pstmt.setString(10, course.getRemark());
            pstmt.setInt(11, course.getCourseId());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DbUtil.close(conn, pstmt);
        }
    }

    // 删除公开课 - 已修复 ✓
    public boolean deleteCourse(int courseId) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DbUtil.getConnection();
            // 先删除参与者记录
            String deleteParticipantsSql = "DELETE FROM course_participant WHERE course_id = ?";
            pstmt = conn.prepareStatement(deleteParticipantsSql);
            pstmt.setInt(1, courseId);
            pstmt.executeUpdate();
            pstmt.close();

            // 再删除课程
            String deleteCourseSql = "DELETE FROM course WHERE course_id = ?";
            pstmt = conn.prepareStatement(deleteCourseSql);
            pstmt.setInt(1, courseId);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DbUtil.close(conn, pstmt);
        }
    }

    // 添加参与者 - 已修复 ✓
    public boolean addParticipant(CourseParticipant participant) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DbUtil.getConnection();
            String sql = "INSERT INTO course_participant (course_id, stu_id, is_credited) VALUES (?, ?, ?)";

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, participant.getCourseId());
            pstmt.setString(2, participant.getStuId());
            pstmt.setInt(3, participant.getIsCredited());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DbUtil.close(conn, pstmt);
        }
    }

    // 获取课程的参与者列表 - 已修复 ✓
    public List<CourseParticipant> getParticipantsByCourseId(int courseId) {
        List<CourseParticipant> participants = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DbUtil.getConnection();
            String sql = "SELECT cp.*, m.name FROM course_participant cp " +
                    "LEFT JOIN member m ON cp.stu_id = m.stu_id " +
                    "WHERE cp.course_id = ? ORDER BY cp.sign_time DESC";

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, courseId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                CourseParticipant participant = new CourseParticipant();
                participant.setId(rs.getInt("id"));
                participant.setCourseId(rs.getInt("course_id"));
                participant.setStuId(rs.getString("stu_id"));
                participant.setSignTime(rs.getTimestamp("sign_time"));
                participant.setIsCredited(rs.getInt("is_credited"));

                participants.add(participant);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtil.close(conn, pstmt, rs);
        }
        return participants;
    }

    // 统计课程参与者数量 - 已修复 ✓
    public int countParticipants(int courseId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DbUtil.getConnection();
            String sql = "SELECT COUNT(*) FROM course_participant WHERE course_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, courseId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        } finally {
            DbUtil.close(conn, pstmt, rs);
        }
    }
}