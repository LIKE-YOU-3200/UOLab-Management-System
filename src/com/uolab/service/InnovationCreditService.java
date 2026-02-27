package com.uolab.service;

import com.uolab.dao.InnovationCreditDAO;
import com.uolab.entity.InnovationCredit;
import java.util.Date;
import java.util.List;

/**
 * 创新学分服务层
 */
public class InnovationCreditService {
    private InnovationCreditDAO creditDAO = new InnovationCreditDAO();

    // 添加创新学分记录
    public int addCredit(InnovationCredit credit) {
        if (!validateCreditData(credit)) {
            return -1;
        }
        return creditDAO.addCredit(credit);
    }

    // 查询创新学分记录
    public List<InnovationCredit> findCredits(String stuId, String stuName,
                                              Date startDate, Date endDate) {
        // 直接传递Date对象，DAO会处理转换
        return creditDAO.findCredits(stuId, stuName, startDate, endDate);
    }

    // 根据学号查询总学分数
    public double getTotalCreditByStuId(String stuId) {
        return creditDAO.getTotalCreditByStuId(stuId);
    }

    // 统计指定时间段的学分记录
    public List<InnovationCredit> getCreditsByDateRange(Date startDate, Date endDate) {
        // 直接传递Date对象，DAO会处理转换
        return creditDAO.getCreditsByDateRange(startDate, endDate);
    }

    // 获取所有学分类别
    public List<String> getAllCreditTypes() {
        List<String> types = creditDAO.getAllCreditTypes();
        // 如果数据库中没有数据，添加默认类别
        if (types.isEmpty()) {
            types.add("公开课参与");
            types.add("竞赛获奖");
            types.add("科研项目");
            types.add("论文发表");
            types.add("专利成果");
            types.add("志愿服务");
            types.add("社会实践");
            types.add("其他");
        }
        return types;
    }

    // 数据验证
    private boolean validateCreditData(InnovationCredit credit) {
        // ... 验证逻辑保持不变 ...
        return true;
    }
}