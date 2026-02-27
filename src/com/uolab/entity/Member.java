package com.uolab.entity;

import java.util.Date;

public class Member {
    private int memberId;
    private String stuId;
    private String name;
    private String gender; // M男，F女
    private String grade;
    private String hometown;
    private String phone;
    private int collegeId;
    private int majorId;
    private String studentPosition; // 校内职务
    private Date joinDate;
    private String status; // 正常/退出/毕业
    private String labPosition; // 实验室职务
    private String remark;
    private Date createTime;
    private Date updateTime;

    public Member() {}

    public Member(String stuId, String name, String gender, String grade) {
        this.stuId = stuId;
        this.name = name;
        this.gender = gender;
        this.grade = grade;
    }

    // Getter和Setter方法
    public int getMemberId() { return memberId; }
    public void setMemberId(int memberId) { this.memberId = memberId; }

    public String getStuId() { return stuId; }
    public void setStuId(String stuId) { this.stuId = stuId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }

    public String getHometown() { return hometown; }
    public void setHometown(String hometown) { this.hometown = hometown; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public int getCollegeId() { return collegeId; }
    public void setCollegeId(int collegeId) { this.collegeId = collegeId; }

    public int getMajorId() { return majorId; }
    public void setMajorId(int majorId) { this.majorId = majorId; }

    public String getStudentPosition() { return studentPosition; }
    public void setStudentPosition(String studentPosition) {
        this.studentPosition = studentPosition;
    }

    public Date getJoinDate() { return joinDate; }
    public void setJoinDate(Date joinDate) { this.joinDate = joinDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getLabPosition() { return labPosition; }
    public void setLabPosition(String labPosition) { this.labPosition = labPosition; }

    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }

    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }

    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }

    @Override
    public String toString() {
        return name + " (" + stuId + ")";
    }
}
