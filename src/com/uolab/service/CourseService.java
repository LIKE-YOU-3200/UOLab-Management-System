/*
中北大学 软件学院 2413040317 李柯延
*/
package com.uolab.service;

/**
 * @author likeyan
 * @version 1.0
 */
import com.uolab.dao.CourseDAO;
import com.uolab.entity.Course;
import com.uolab.entity.CourseParticipant;
import java.util.Date;
import java.util.List;

public class CourseService {
    private CourseDAO courseDAO = new CourseDAO();

    // 添加公开课
    public int addCourse(Course course) {
        if (!validateCourseData(course)) {
            return -1;
        }
        return courseDAO.addCourse(course);
    }

    // 修改公开课
    public List<Course> findCourses(String topic, String speaker, Date startDate, Date endDate, String method) {
        // 将java.util.Date转换为java.sql.Date
        java.sql.Date sqlStartDate = null;
        java.sql.Date sqlEndDate = null;

        if (startDate != null) {
            sqlStartDate = new java.sql.Date(startDate.getTime());
        }
        if (endDate != null) {
            sqlEndDate = new java.sql.Date(endDate.getTime());
        }

        // 如果所有条件都为空，查询所有数据
        if ((topic == null || topic.trim().isEmpty()) &&
                (speaker == null || speaker.trim().isEmpty()) &&
                startDate == null && endDate == null &&
                (method == null || method.trim().isEmpty() || "全部".equals(method))) {
            // 直接调用获取所有数据的方法
            return courseDAO.findCourses(null, null, null, null, null);
        }

        return courseDAO.findCourses(topic, speaker, sqlStartDate, sqlEndDate, method);
    }

    // 查询公开课
    public boolean updateCourse(Course course) {
        if (course.getCourseId() <= 0) {
            throw new IllegalArgumentException("无效的课程ID");
        }
        return courseDAO.updateCourse(course);
    }

    // 根据ID获取公开课
    public Course getCourseById(int courseId) {
        return courseDAO.getCourseById(courseId);
    }

    // 数据验证
    private boolean validateCourseData(Course course) {
        if (course.getTopic() == null || course.getTopic().trim().isEmpty()) {
            throw new IllegalArgumentException("技术专题不能为空");
        }

        if (!course.getTopic().matches("^[a-zA-Z\\u4e00-\\u9fa5]{1,100}$")) {
            throw new IllegalArgumentException("技术专题只能包含字母和汉字，1-100个字符");
        }

        if (course.getSpeaker() == null || !course.getSpeaker().matches("^[\u4e00-\u9fa5]{2,10}$")) {
            throw new IllegalArgumentException("主讲人必须为2-10个汉字");
        }

        if (course.getCourseDate() == null) {
            throw new IllegalArgumentException("开课日期不能为空");
        }

        if (course.getGroupDesc() == null || course.getGroupDesc().trim().isEmpty()) {
            throw new IllegalArgumentException("群体说明不能为空");
        }

        if (course.getCredit() < 0 || course.getCredit() > 10) {
            throw new IllegalArgumentException("创新学分值必须在0-10之间");
        }

        if (course.getAttendance() < 0) {
            throw new IllegalArgumentException("参加人数不能为负数");
        }

        return true;
    }


}
