/*
中北大学 软件学院 2413040317 李柯延
*/
package com.uolab.entity;
import java.util.Date;
/**
 * @author likeyan
 * @version 1.0
 */
public class InnovationCredit {
    private int creditId;           // 学分ID
    private String stuId;           // 学号
    private String stuName;         // 学生姓名
    private String grade;           // 年级
    private String college;         // 院系
    private String major;           // 专业
    private double creditValue;     // 学分分值
    private String creditType;      // 学分类别
    private String reason;          // 事由说明
    private Date acquireDate;       // 取得时间（年月）
    private Date recordTime;        // 录入时间
    private String recorder;        // 录入人
    private String remark;          // 备注

    // 构造方法
    public InnovationCredit() {}

    public InnovationCredit(String stuId, String stuName, String grade,
                            String college, double creditValue,
                            String creditType, String reason, Date acquireDate) {
        this.stuId = stuId;
        this.stuName = stuName;
        this.grade = grade;
        this.college = college;
        this.creditValue = creditValue;
        this.creditType = creditType;
        this.reason = reason;
        this.acquireDate = acquireDate;
    }

    // Getter和Setter方法
    public int getCreditId() { return creditId; }
    public void setCreditId(int creditId) { this.creditId = creditId; }

    public String getStuId() { return stuId; }
    public void setStuId(String stuId) { this.stuId = stuId; }

    public String getStuName() { return stuName; }
    public void setStuName(String stuName) { this.stuName = stuName; }

    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }

    public String getCollege() { return college; }
    public void setCollege(String college) { this.college = college; }

    public String getMajor() { return major; }
    public void setMajor(String major) { this.major = major; }

    public double getCreditValue() { return creditValue; }
    public void setCreditValue(double creditValue) { this.creditValue = creditValue; }

    public String getCreditType() { return creditType; }
    public void setCreditType(String creditType) { this.creditType = creditType; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public Date getAcquireDate() { return acquireDate; }
    public void setAcquireDate(Date acquireDate) { this.acquireDate = acquireDate; }

    public Date getRecordTime() { return recordTime; }
    public void setRecordTime(Date recordTime) { this.recordTime = recordTime; }

    public String getRecorder() { return recorder; }
    public void setRecorder(String recorder) { this.recorder = recorder; }

    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }

    @Override
    public String toString() {
        return stuName + " (" + stuId + ") - " + creditValue + "分";
    }
}
