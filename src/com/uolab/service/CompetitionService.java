/*
中北大学 软件学院 2413040317 李柯延
*/
package com.uolab.service;

import com.uolab.dao.CompetitionDAO;
import com.uolab.entity.Competition;
import java.util.List;

/**
 * @author likeyan
 * @version 1.0
 */
public class CompetitionService {
    private CompetitionDAO competitionDAO = new CompetitionDAO();

    // 添加参赛信息
    public int addCompetition(Competition competition) {
        if (!validateCompetitionData(competition)) {
            return -1;
        }
        return competitionDAO.addCompetition(competition);
    }

    // 根据ID获取参赛信息
    public Competition getCompetitionById(int competitionId) {
        return competitionDAO.getCompetitionById(competitionId);
    }

    // 根据年度查询参赛信息
    public List<Competition> getCompetitionsByYear(Integer year) {
        return competitionDAO.getCompetitionsByYear(year);
    }

    // 更新参赛信息
    public boolean updateCompetition(Competition competition) {
        if (competition.getCompetitionId() <= 0) {
            throw new IllegalArgumentException("无效的参赛信息ID");
        }
        return competitionDAO.updateCompetition(competition);
    }

    // 获取所有竞赛类别
    public List<String> getAllCategories() {
        return competitionDAO.getAllCategories();
    }

    // 获取所有参赛年度
    public List<Integer> getAllYears() {
        return competitionDAO.getAllYears();
    }

    // 数据验证
    private boolean validateCompetitionData(Competition competition) {
        // 竞赛类别验证
        if (competition.getCategory() == null || competition.getCategory().trim().isEmpty()) {
            throw new IllegalArgumentException("竞赛类别不能为空");
        }

        // 项目题目验证（2-30个汉字字母组合）
        if (competition.getTitle() == null || competition.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("项目题目不能为空");
        }

        String title = competition.getTitle().trim();
        if (!title.matches("^[\\u4e00-\\u9fa5a-zA-Z]{2,30}$")) {
            throw new IllegalArgumentException("项目题目必须为2-30个汉字或字母组合");
        }

        // 参赛年度验证
        if (competition.getYear() < 2000 || competition.getYear() > 2100) {
            throw new IllegalArgumentException("参赛年度必须在2000-2100之间");
        }

        // 组长验证（汉字2-6个）
        if (competition.getLeader() == null || competition.getLeader().trim().isEmpty()) {
            throw new IllegalArgumentException("组长姓名不能为空");
        }

        String leader = competition.getLeader().trim();
        if (!leader.matches("^[\\u4e00-\\u9fa5]{2,6}$")) {
            throw new IllegalArgumentException("组长姓名必须为2-6个汉字");
        }

        // 组员验证（汉字2-6个，多个用逗号分隔）
        if (competition.getMembers() != null && !competition.getMembers().trim().isEmpty()) {
            String members = competition.getMembers().trim();
            String[] memberArray = members.split(",");
            for (String member : memberArray) {
                String trimmedMember = member.trim();
                if (!trimmedMember.matches("^[\\u4e00-\\u9fa5]{2,6}$")) {
                    throw new IllegalArgumentException("组员姓名必须为2-6个汉字，多个组员用逗号分隔");
                }
            }
        }

        // 指导老师验证（汉字2-6个）
        if (competition.getAdvisor() != null && !competition.getAdvisor().trim().isEmpty()) {
            String advisor = competition.getAdvisor().trim();
            if (!advisor.matches("^[\\u4e00-\\u9fa5]{2,6}$")) {
                throw new IllegalArgumentException("指导老师姓名必须为2-6个汉字");
            }
        }

        // 获奖等级验证
        String[] validAwards = {"无", "优秀奖", "三等奖", "二等奖", "一等奖", "特等奖"};
        boolean validAward = false;
        for (String award : validAwards) {
            if (award.equals(competition.getAwardLevel())) {
                validAward = true;
                break;
            }
        }

        if (!validAward) {
            throw new IllegalArgumentException("获奖等级无效");
        }

        return true;
    }
}
