/*
中北大学 软件学院 2413040317 李柯延
*/
package com.uolab.service;

/**
 * @author likeyan
 * @version 1.0
 */
import com.uolab.dao.MemberDAO;
import com.uolab.entity.Member;
import java.util.List;

public class MemberService {
    private MemberDAO memberDAO = new MemberDAO();

    // 添加成员
    public boolean addMember(Member member) {
        if (!validateMemberData(member)) {
            return false;
        }

        if (memberDAO.isStuIdExists(member.getStuId())) {
            throw new IllegalArgumentException("学号已存在：" + member.getStuId());
        }

        return memberDAO.addMember(member);
    }

    // 修改成员信息
    public boolean updateMember(Member member) {
        if (member.getMemberId() <= 0) {
            throw new IllegalArgumentException("无效的成员ID");
        }

        // 验证电话格式
        if (!isValidPhone(member.getPhone())) {
            throw new IllegalArgumentException("请输入正确的手机号码");
        }

        return memberDAO.updateMember(member);
    }

    // 根据ID获取成员
    public Member getMemberById(int memberId) {
        return memberDAO.getMemberById(memberId);
    }

    // 多条件查询成员
    public List<Member> findMembers(String name, String grade, String phone,
                                    Integer collegeId, Integer majorId,
                                    String status, Boolean hasManagementPosition) {
        return memberDAO.findMembers(name, grade, phone, collegeId, majorId, status, hasManagementPosition);
    }

    // 获取所有成员
    public List<Member> getAllMembers() {
        return memberDAO.getAllMembers();
    }

    // 检查学号是否存在
    public boolean isStuIdExists(String stuId) {
        return memberDAO.isStuIdExists(stuId);
    }

    // 统计数据方法
    public List<Object[]> getCollegeStatistics(Integer collegeId) {
        return memberDAO.statByCollege(collegeId);
    }

    public List<Object[]> getGradeStatistics(String grade) {
        return memberDAO.statByGrade(grade);
    }

    public List<Object[]> getMajorStatistics() {
        return memberDAO.statByMajor();
    }

    public List<Object[]> getStatusStatistics() {
        return memberDAO.statByStatus();
    }

    // 数据验证方法
    private boolean validateMemberData(Member member) {
        if (member.getName() == null || !member.getName().matches("^[\u4e00-\u9fa5]{2,5}$")) {
            throw new IllegalArgumentException("姓名必须为2-5个汉字");
        }

        if (member.getStuId() == null || !member.getStuId().matches("^\\d+$")) {
            throw new IllegalArgumentException("学号必须为数字");
        }

        if (member.getGender() == null ||
                (!"M".equals(member.getGender()) && !"F".equals(member.getGender()))) {
            throw new IllegalArgumentException("性别必须为男(M)或女(F)");
        }

        if (member.getGrade() == null || member.getGrade().trim().isEmpty()) {
            throw new IllegalArgumentException("年级不能为空");
        }

        if (!isValidPhone(member.getPhone())) {
            throw new IllegalArgumentException("请输入正确的手机号码");
        }

        if (member.getCollegeId() <= 0) {
            throw new IllegalArgumentException("请选择院系");
        }

        if (member.getMajorId() <= 0) {
            throw new IllegalArgumentException("请选择专业");
        }

        if (member.getJoinDate() == null) {
            throw new IllegalArgumentException("请选择加入日期");
        }

        if (member.getStatus() == null ||
                (!"正常".equals(member.getStatus()) &&
                        !"退出".equals(member.getStatus()) &&
                        !"毕业".equals(member.getStatus()))) {
            throw new IllegalArgumentException("状态必须为正常、退出或毕业");
        }

        return true;
    }

    // 验证手机号码
    private boolean isValidPhone(String phone) {
        return phone != null && phone.matches("^1[3-9]\\d{9}$");
    }

}
