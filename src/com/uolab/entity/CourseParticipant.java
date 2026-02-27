/*
中北大学 软件学院 2413040317 李柯延
*/
package com.uolab.entity;

/**
 * @author likeyan
 * @version 1.0
 */
import java.util.Date;

public class CourseParticipant {
    private int id;
    private int courseId;
    private String stuId;
    private Date signTime;
    private int isCredited;  // 0-未录入学分, 1-已录入学分

    // 构造函数
    public CourseParticipant() {}

    public CourseParticipant(int courseId, String stuId) {
        this.courseId = courseId;
        this.stuId = stuId;
        this.isCredited = 0;
    }

    // Getter和Setter方法
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getCourseId() { return courseId; }
    public void setCourseId(int courseId) { this.courseId = courseId; }

    public String getStuId() { return stuId; }
    public void setStuId(String stuId) { this.stuId = stuId; }

    public Date getSignTime() { return signTime; }
    public void setSignTime(Date signTime) { this.signTime = signTime; }

    public int getIsCredited() { return isCredited; }
    public void setIsCredited(int isCredited) { this.isCredited = isCredited; }

    // 便捷方法
    public boolean isCredited() {
        return isCredited == 1;
    }

    public void setCredited(boolean credited) {
        this.isCredited = credited ? 1 : 0;
    }
}
