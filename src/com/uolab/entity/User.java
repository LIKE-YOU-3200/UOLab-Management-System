package com.uolab.entity;

import java.util.Date;

/**
 * 用户实体类
 */
public class User {
    private int userId;          // 用户ID
    private String username;     // 用户名（字母数字组合，开头是字母，3-15个字符）
    private String password;     // 密码（6-20个字符，由数字和字母构成）
    private int roleId;          // 角色ID：1-超级管理员，2-管理员
    private String status;       // 用户状态：启用、禁用
    private String realName;     // 真实姓名（2-5个汉字）
    private int collegeId;       // 院系ID
    private int majorId;         // 专业ID
    private String grade;        // 年级
    private String className;    // 班级
    private String studentPosition; // 校内职务
    private String phone;        // 手机号（明文存储）
    private String qq;           // QQ号
    private String remark;       // 备注
    private Date createTime;     // 创建时间
    private Date updateTime;     // 更新时间

    // 构造方法
    public User() {}

    // Getter和Setter方法
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public int getRoleId() { return roleId; }
    public void setRoleId(int roleId) { this.roleId = roleId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getRealName() { return realName; }
    public void setRealName(String realName) { this.realName = realName; }

    public int getCollegeId() { return collegeId; }
    public void setCollegeId(int collegeId) { this.collegeId = collegeId; }

    public int getMajorId() { return majorId; }
    public void setMajorId(int majorId) { this.majorId = majorId; }

    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }

    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }

    public String getStudentPosition() { return studentPosition; }
    public void setStudentPosition(String studentPosition) { this.studentPosition = studentPosition; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getQq() { return qq; }
    public void setQq(String qq) { this.qq = qq; }

    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }

    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }

    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }

    // 辅助方法
    public String getRoleName() {
        return roleId == 1 ? "超级管理员" : "管理员";
    }

    @Override
    public String toString() {
        return realName + " (" + username + ")";
    }
}