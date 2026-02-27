package com.uolab.service;

import com.uolab.dao.UserDAO;
import com.uolab.entity.User;
import java.util.List;

/**
 * 用户服务层
 */
public class UserService {
    private UserDAO userDAO = new UserDAO();

    public User login(String username, String password) {
        try {
            // 1. 根据用户名查询用户
            User user = userDAO.getUserByUsername(username);

            if (user == null) {
                return null; // 用户不存在
            }

            // 2. 检查用户状态
            if (!"启用".equals(user.getStatus())) {
                return null; // 用户被禁用
            }

            // 3. 验证密码
            if (password != null && password.equals(user.getPassword())) {
                return user; // 密码正确，返回用户对象
            }

            return null; // 密码错误

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 添加用户（仅超级管理员可调用）
    public int addUser(User user) {
        if (!validateUserData(user)) {
            return -1;
        }

        // 检查用户名是否已存在
        User existingUser = userDAO.getUserByUsername(user.getUsername());
        if (existingUser != null) {
            throw new IllegalArgumentException("用户名已存在");
        }

        return userDAO.addUser(user);
    }

    // 根据ID获取用户
    public User getUserById(int userId) {
        return userDAO.getUserById(userId);
    }

    // 获取所有用户
    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }

    // 组合查询用户
    public List<User> findUsers(String realName, Integer collegeId, Integer majorId,
                                String grade, String className) {
        return userDAO.findUsers(realName, collegeId, majorId, grade, className);
    }

    // 更新用户信息
    public boolean updateUser(User user) {
        if (user.getUserId() <= 0) {
            throw new IllegalArgumentException("无效的用户ID");
        }
        return userDAO.updateUser(user);
    }

    // 重置密码
    public boolean resetPassword(int userId) {
        return userDAO.resetPassword(userId);
    }

    // 启用用户
    public boolean enableUser(int userId) {
        return userDAO.setUserStatus(userId, "启用");
    }

    // 禁用用户
    public boolean disableUser(int userId) {
        return userDAO.setUserStatus(userId, "禁用");
    }

    // 删除用户（检查是否产生过信息）
    public boolean deleteUser(int userId) {
        // 检查用户是否产生过信息
        if (userDAO.hasUserGeneratedInfo(userId)) {
            throw new IllegalStateException("该用户已产生过信息，不能删除，只能禁用");
        }
        return userDAO.deleteUser(userId);
    }

    // 用户数据验证
    private boolean validateUserData(User user) {
        // 用户名验证（字母数字组合，开头是字母，3-15个字符）
        if (user.getUsername() == null || !user.getUsername().matches("^[a-zA-Z][a-zA-Z0-9]{2,14}$")) {
            throw new IllegalArgumentException("用户名必须是字母开头，3-15个字母数字组合");
        }

        // 密码验证（6-20个字符，由数字和字母构成）
        if (user.getPassword() == null || !user.getPassword().matches("^[a-zA-Z0-9]{6,20}$")) {
            throw new IllegalArgumentException("密码必须是6-20个字母数字组合");
        }

        // 角色验证
        if (user.getRoleId() != 1 && user.getRoleId() != 2) {
            throw new IllegalArgumentException("角色无效");
        }

        // 状态验证
        if (!"启用".equals(user.getStatus()) && !"禁用".equals(user.getStatus())) {
            throw new IllegalArgumentException("用户状态无效");
        }

        // 真实姓名验证（2-5个汉字）
        if (user.getRealName() == null || !user.getRealName().matches("^[\u4e00-\u9fa5]{2,5}$")) {
            throw new IllegalArgumentException("真实姓名必须是2-5个汉字");
        }

        // 手机号验证
        if (user.getPhone() == null || !user.getPhone().matches("^1[3-9]\\d{9}$")) {
            throw new IllegalArgumentException("请输入有效的手机号码");
        }

        // QQ号验证（可选）
        if (user.getQq() != null && !user.getQq().trim().isEmpty() && !user.getQq().matches("^\\d{5,15}$")) {
            throw new IllegalArgumentException("QQ号必须是5-15位数字");
        }

        return true;
    }
}