/*
中北大学 软件学院 2413040317 李柯延
*/
package com.uolab.entity;

/**
 * @author likeyan
 * @version 1.0
 */
import java.util.Date;
import java.util.List;

public class Course {
    private int courseId;          // course_id
    private String topic;          // topic
    private Date courseDate;       // course_date
    private String speaker;        // speaker
    private String organizers;     // organizer (数据库字段名)
    private String location;       // location
    private String groupDesc;      // target_group (数据库字段名)
    private String method;         // method
    private double credit;         // credit_value (数据库字段名)
    private int attendance;        // participants_count (数据库字段名)
    private String remark;         // remark

    // 构造函数
    public Course() {}

    // Getter和Setter方法
    public int getCourseId() { return courseId; }
    public void setCourseId(int courseId) { this.courseId = courseId; }

    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }

    public Date getCourseDate() { return courseDate; }
    public void setCourseDate(Date courseDate) { this.courseDate = courseDate; }

    public String getSpeaker() { return speaker; }
    public void setSpeaker(String speaker) { this.speaker = speaker; }

    public String getOrganizers() { return organizers; }
    public void setOrganizers(String organizers) { this.organizers = organizers; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getGroupDesc() { return groupDesc; }
    public void setGroupDesc(String groupDesc) { this.groupDesc = groupDesc; }

    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }

    public double getCredit() { return credit; }
    public void setCredit(double credit) { this.credit = credit; }

    public int getAttendance() { return attendance; }
    public void setAttendance(int attendance) { this.attendance = attendance; }

    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }

    public Date getCreateTime() { return courseDate; }
    public void setCreateTime(Date createTime) { this.courseDate = createTime; }

    @Override
    public String toString() {
        return topic + " (" + speaker + ")";
    }
}
