/*
中北大学 软件学院 2413040317 李柯延
*/
package com.uolab.entity;
import java.util.Date;
/**
 * @author likeyan
 * @version 1.0
 */
public class Competition {
    private int competitionId;      // 主键ID
    private String category;        // 竞赛类别
    private String title;           // 项目题目
    private int year;               // 参赛年度
    private String leader;          // 组长姓名
    private String members;         // 组员姓名（多个用逗号分隔）
    private String awardLevel;      // 获奖等级
    private String advisor;         // 指导老师
    private String remark;          // 备注
    private Date createTime;        // 创建时间

    // 构造方法
    public Competition() {}

    public Competition(String category, String title, int year, String leader,
                       String members, String awardLevel, String advisor) {
        this.category = category;
        this.title = title;
        this.year = year;
        this.leader = leader;
        this.members = members;
        this.awardLevel = awardLevel;
        this.advisor = advisor;
    }

    // Getter和Setter方法
    public int getCompetitionId() { return competitionId; }
    public void setCompetitionId(int competitionId) { this.competitionId = competitionId; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    public String getLeader() { return leader; }
    public void setLeader(String leader) { this.leader = leader; }

    public String getMembers() { return members; }
    public void setMembers(String members) { this.members = members; }

    public String getAwardLevel() { return awardLevel; }
    public void setAwardLevel(String awardLevel) { this.awardLevel = awardLevel; }

    public String getAdvisor() { return advisor; }
    public void setAdvisor(String advisor) { this.advisor = advisor; }

    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }

    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }

    @Override
    public String toString() {
        return title + " (" + year + "年)";
    }
}