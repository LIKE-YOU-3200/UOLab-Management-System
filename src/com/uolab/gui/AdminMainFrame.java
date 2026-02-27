package com.uolab.gui;

import com.uolab.entity.*;
import com.uolab.service.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;


public class AdminMainFrame extends JFrame {
    private User currentUser;
    private JTabbedPane tabbedPane;

    public AdminMainFrame(User user) {
        this.currentUser = user;
        initComponents();
    }

    private void initComponents() {
        setTitle("UOLab实验室管理系统 - 管理员");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        // 菜单栏
        JMenuBar menuBar = new JMenuBar();

        // 系统菜单
        JMenu systemMenu = new JMenu("系统");
        JMenuItem logoutItem = new JMenuItem("退出登录");
        JMenuItem exitItem = new JMenuItem("退出系统");

        logoutItem.addActionListener(e -> logout());
        exitItem.addActionListener(e -> System.exit(0));

        systemMenu.add(logoutItem);
        systemMenu.addSeparator();
        systemMenu.add(exitItem);

        // 用户管理菜单
        JMenu userMenu = new JMenu("用户管理");
        JMenuItem addUserItem = new JMenuItem("添加用户");
        JMenuItem queryUserItem = new JMenuItem("查询用户");

        addUserItem.addActionListener(e -> openAddUserPanel());
        queryUserItem.addActionListener(e -> openQueryUserPanel());

        userMenu.add(addUserItem);
        userMenu.add(queryUserItem);

        // 院系管理菜单
        JMenu collegeMenu = new JMenu("院系管理");
        JMenuItem addCollegeItem = new JMenuItem("添加院系");
        JMenuItem addMajorItem = new JMenuItem("添加专业");

        addCollegeItem.addActionListener(e -> openAddCollegePanel());
        addMajorItem.addActionListener(e -> openAddMajorPanel());

        collegeMenu.add(addCollegeItem);
        collegeMenu.add(addMajorItem);

        // 成员管理菜单
        JMenu memberMenu = new JMenu("成员管理");
        JMenuItem addMemberItem = new JMenuItem("添加成员");
        JMenuItem queryMemberItem = new JMenuItem("查询成员");
        JMenuItem statMemberItem = new JMenuItem("成员统计");

        addMemberItem.addActionListener(e -> openAddMemberPanel());
        queryMemberItem.addActionListener(e -> openQueryMemberPanel());
        statMemberItem.addActionListener(e -> openMemberStatPanel());

        memberMenu.add(addMemberItem);
        memberMenu.add(queryMemberItem);
        memberMenu.add(statMemberItem);

        // 公开课菜单
        JMenu courseMenu = new JMenu("公开课管理");
        JMenuItem addCourseItem = new JMenuItem("录入公开课");
        JMenuItem queryCourseItem = new JMenuItem("查询公开课");

        addCourseItem.addActionListener(e -> openAddCoursePanel());
        queryCourseItem.addActionListener(e -> openQueryCoursePanel());

        courseMenu.add(addCourseItem);
        courseMenu.add(queryCourseItem);

        // 参赛管理菜单
        JMenu competitionMenu = new JMenu("参赛管理");
        JMenuItem addCompetitionItem = new JMenuItem("参赛信息录入");
        JMenuItem queryCompetitionItem = new JMenuItem("参赛信息查询");

        addCompetitionItem.addActionListener(e -> openAddCompetitionPanel());
        queryCompetitionItem.addActionListener(e -> openQueryCompetitionPanel());

        competitionMenu.add(addCompetitionItem);
        competitionMenu.add(queryCompetitionItem);

        // 学分管理菜单
        JMenu creditMenu = new JMenu("创新学分");
        JMenuItem addCreditItem = new JMenuItem("录入学分");
        JMenuItem queryCreditItem = new JMenuItem("学分查询");
        JMenuItem statCreditItem = new JMenuItem("学分统计");

        addCreditItem.addActionListener(e -> openAddCreditPanel());
        queryCreditItem.addActionListener(e -> openQueryCreditPanel());
        statCreditItem.addActionListener(e -> openCreditStatPanel());

        creditMenu.add(addCreditItem);
        creditMenu.add(queryCreditItem);
        creditMenu.add(statCreditItem);

        // 添加到菜单栏
        menuBar.add(systemMenu);
        menuBar.add(userMenu);
        menuBar.add(collegeMenu);
        menuBar.add(memberMenu);
        menuBar.add(courseMenu);
        menuBar.add(competitionMenu);
        menuBar.add(creditMenu);

        setJMenuBar(menuBar);

        // 状态栏
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel userLabel = new JLabel("当前用户: " + currentUser.getRealName() +
                " (" + (currentUser.getRoleId() == 1 ? "超级管理员" : "管理员") + ")");
        statusPanel.add(userLabel);

        // 主内容面板
        JPanel mainPanel = new JPanel(new BorderLayout());
        tabbedPane = new JTabbedPane();

        // 欢迎标签页
        JPanel welcomePanel = new JPanel(new BorderLayout());
        JLabel welcomeLabel = new JLabel("欢迎使用UOLab联合开放实验室管理系统",
                SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("微软雅黑", Font.BOLD, 24));
        welcomePanel.add(welcomeLabel, BorderLayout.CENTER);
        tabbedPane.addTab("首页", welcomePanel);

        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        mainPanel.add(statusPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "确定要退出登录吗？",
                "确认",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            dispose(); // 关闭当前窗口
            new LoginFrame(); // 重新打开登录窗口
        }
    }

    private void openAddUserPanel() {
        // 检查当前用户是否为超级管理员
        if (currentUser.getRoleId() != 1) {
            JOptionPane.showMessageDialog(this,
                    "只有超级管理员才能添加用户！",
                    "权限不足",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 标题
        JLabel titleLabel = new JLabel("添加用户（仅限超级管理员）");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        // --- 用户名（字母数字组合，开头是字母，3-15个字符） ---
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        panel.add(new JLabel("用户名*:"), gbc);

        gbc.gridx = 1;
        JTextField usernameField = new JTextField(20);
        panel.add(usernameField, gbc);

        // --- 初始密码（固定为123456，用户登录后可修改） ---
        gbc.gridy = 2;
        gbc.gridx = 0;
        panel.add(new JLabel("初始密码:"), gbc);

        gbc.gridx = 1;
        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setText("123456");
        passwordField.setEnabled(false); // 初始密码固定，不可修改
        panel.add(passwordField, gbc);

        // --- 真实姓名（2-5个汉字，必填） ---
        gbc.gridy = 3;
        gbc.gridx = 0;
        panel.add(new JLabel("真实姓名*:"), gbc);

        gbc.gridx = 1;
        JTextField realNameField = new JTextField(20);
        panel.add(realNameField, gbc);

        // --- 角色（超级管理员、管理员） ---
        gbc.gridy = 4;
        gbc.gridx = 0;
        panel.add(new JLabel("角色*:"), gbc);

        gbc.gridx = 1;
        JComboBox<String> roleCombo = new JComboBox<>(new String[]{"管理员", "超级管理员"});
        panel.add(roleCombo, gbc);

        // --- 用户状态（启用或禁用） ---
        gbc.gridy = 5;
        gbc.gridx = 0;
        panel.add(new JLabel("用户状态:"), gbc);

        gbc.gridx = 1;
        JComboBox<String> statusCombo = new JComboBox<>(new String[]{"启用", "禁用"});
        panel.add(statusCombo, gbc);

        // --- 手机（必填，明文存储） ---
        gbc.gridy = 6;
        gbc.gridx = 0;
        panel.add(new JLabel("手机号*:"), gbc);

        gbc.gridx = 1;
        JTextField phoneField = new JTextField(20);
        panel.add(phoneField, gbc);

        // --- QQ（可选） ---
        gbc.gridy = 7;
        gbc.gridx = 0;
        panel.add(new JLabel("QQ号:"), gbc);

        gbc.gridx = 1;
        JTextField qqField = new JTextField(20);
        panel.add(qqField, gbc);

        // --- 院系专业 ---
        gbc.gridy = 8;
        gbc.gridx = 0;
        panel.add(new JLabel("院系专业:"), gbc);

        gbc.gridx = 1;
        JPanel collegeMajorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        // 院系下拉框
        JComboBox<String> collegeCombo = new JComboBox<>();
        collegeCombo.addItem("请选择院系");
        collegeCombo.addItem("软件学院");
        collegeCombo.addItem("计算机学院");
        collegeCombo.addItem("信息与通信工程学院");

        // 专业下拉框
        JComboBox<String> majorCombo = new JComboBox<>();
        majorCombo.addItem("请先选择院系");
        majorCombo.setEnabled(false);

        // 院系选择监听器
        collegeCombo.addActionListener(e -> {
            String selectedCollege = (String) collegeCombo.getSelectedItem();
            if (!"请选择院系".equals(selectedCollege)) {
                majorCombo.removeAllItems();
                majorCombo.addItem("请选择专业");
                majorCombo.setEnabled(true);

                // 根据选择的院系加载专业
                if ("软件学院".equals(selectedCollege)) {
                    majorCombo.addItem("软件工程");
                    majorCombo.addItem("网络工程");
                    majorCombo.addItem("信息安全");
                } else if ("计算机学院".equals(selectedCollege)) {
                    majorCombo.addItem("计算机科学");
                    majorCombo.addItem("人工智能");
                    majorCombo.addItem("数据科学");
                } else if ("信息与通信工程学院".equals(selectedCollege)) {
                    majorCombo.addItem("通信工程");
                    majorCombo.addItem("电子信息工程");
                    majorCombo.addItem("物联网工程");
                }
            } else {
                majorCombo.removeAllItems();
                majorCombo.addItem("请先选择院系");
                majorCombo.setEnabled(false);
            }
        });

        collegeMajorPanel.add(collegeCombo);
        collegeMajorPanel.add(new JLabel("-"));
        collegeMajorPanel.add(majorCombo);
        panel.add(collegeMajorPanel, gbc);

        // --- 年级 ---
        gbc.gridy = 9;
        gbc.gridx = 0;
        panel.add(new JLabel("年级:"), gbc);

        gbc.gridx = 1;
        JComboBox<String> gradeCombo = new JComboBox<>(new String[]{"2021", "2022", "2023", "2024", "2025"});
        panel.add(gradeCombo, gbc);

        // --- 班级 ---
        gbc.gridy = 10;
        gbc.gridx = 0;
        panel.add(new JLabel("班级:"), gbc);

        gbc.gridx = 1;
        JTextField classField = new JTextField(10);
        panel.add(classField, gbc);

        // --- 校内职务 ---
        gbc.gridy = 11;
        gbc.gridx = 0;
        panel.add(new JLabel("校内职务:"), gbc);

        gbc.gridx = 1;
        JComboBox<String> positionCombo = new JComboBox<>(
                new String[]{"无", "班干部", "学生会干部", "社团干部"}
        );
        panel.add(positionCombo, gbc);

        // --- 备注 ---
        gbc.gridy = 12;
        gbc.gridx = 0;
        panel.add(new JLabel("备注:"), gbc);

        gbc.gridx = 1;
        JTextArea remarkArea = new JTextArea(3, 30);
        remarkArea.setLineWrap(true);
        JScrollPane remarkScroll = new JScrollPane(remarkArea);
        panel.add(remarkScroll, gbc);

        // --- 保存和取消按钮 ---
        gbc.gridy = 13;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton saveButton = new JButton("保存");
        JButton cancelButton = new JButton("取消");
        JButton resetButton = new JButton("重置");

        // 保存按钮事件
        saveButton.addActionListener(e -> {
            try {
                // 收集表单数据
                String username = usernameField.getText().trim();
                String password = "123456"; // 固定初始密码
                String realName = realNameField.getText().trim();
                String role = (String) roleCombo.getSelectedItem();
                String status = (String) statusCombo.getSelectedItem();
                String phone = phoneField.getText().trim();
                String qq = qqField.getText().trim();
                String selectedCollege = (String) collegeCombo.getSelectedItem();
                String selectedMajor = (String) majorCombo.getSelectedItem();
                String grade = (String) gradeCombo.getSelectedItem();
                String className = classField.getText().trim();
                String studentPosition = (String) positionCombo.getSelectedItem();
                String remark = remarkArea.getText().trim();

                // 验证必填字段
                if (username.isEmpty() || realName.isEmpty() || phone.isEmpty()) {
                    JOptionPane.showMessageDialog(panel, "带*的字段为必填项！");
                    return;
                }

                // 创建User对象
                User user = new User();
                user.setUsername(username);
                user.setPassword(password);
                user.setRealName(realName);
                user.setRoleId("超级管理员".equals(role) ? 1 : 2);
                user.setStatus(status);
                user.setPhone(phone);
                user.setQq(qq);

                // 设置院系和专业ID（这里需要根据名称查询ID）
                // 实际项目中应该从数据库查询ID
                java.util.Map<String, Integer> collegeMap = new java.util.HashMap<>();
                collegeMap.put("软件学院", 1);
                collegeMap.put("计算机学院", 2);
                collegeMap.put("信息与通信工程学院", 3);

                java.util.Map<String, Integer> majorMap = new java.util.HashMap<>();
                majorMap.put("软件工程", 1);
                majorMap.put("网络工程", 2);
                majorMap.put("信息安全", 3);
                majorMap.put("计算机科学", 4);
                majorMap.put("人工智能", 5);
                majorMap.put("数据科学", 6);
                majorMap.put("通信工程", 7);
                majorMap.put("电子信息工程", 8);
                majorMap.put("物联网工程", 9);

                if (!"请选择院系".equals(selectedCollege)) {
                    user.setCollegeId(collegeMap.getOrDefault(selectedCollege, 0));
                }

                if (!"请先选择院系".equals(selectedMajor) && !"请选择专业".equals(selectedMajor)) {
                    user.setMajorId(majorMap.getOrDefault(selectedMajor, 0));
                }

                user.setGrade(grade);
                user.setClassName(className);
                user.setStudentPosition(studentPosition);
                user.setRemark(remark);

                // 调用Service保存
                UserService userService = new UserService();
                int userId = userService.addUser(user);

                if (userId > 0) {
                    JOptionPane.showMessageDialog(panel,
                            "用户添加成功！\n" +
                                    "用户名：" + username + "\n" +
                                    "真实姓名：" + realName + "\n" +
                                    "初始密码：123456",
                            "添加成功",
                            JOptionPane.INFORMATION_MESSAGE);

                    // 清空表单
                    resetUserForm(usernameField, passwordField, realNameField, roleCombo,
                            statusCombo, phoneField, qqField, collegeCombo, majorCombo,
                            gradeCombo, classField, positionCombo, remarkArea);
                } else {
                    JOptionPane.showMessageDialog(panel,
                            "添加失败，请稍后重试！",
                            "添加失败",
                            JOptionPane.ERROR_MESSAGE);
                }

            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(panel,
                        ex.getMessage(),
                        "输入错误",
                        JOptionPane.WARNING_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel,
                        "添加失败：" + ex.getMessage(),
                        "系统错误",
                        JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        // 重置按钮事件
        resetButton.addActionListener(e -> {
            resetUserForm(usernameField, passwordField, realNameField, roleCombo,
                    statusCombo, phoneField, qqField, collegeCombo, majorCombo,
                    gradeCombo, classField, positionCombo, remarkArea);
        });

        // 取消按钮事件
        cancelButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(panel,
                    "确定要取消添加吗？未保存的数据将丢失。",
                    "确认取消",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                tabbedPane.remove(panel);
            }
        });

        buttonPanel.add(saveButton);
        buttonPanel.add(resetButton);
        buttonPanel.add(cancelButton);
        panel.add(buttonPanel, gbc);

        // 设置面板大小
        panel.setPreferredSize(new Dimension(600, 700));

        // 添加到标签页
        tabbedPane.addTab("添加用户", panel);
        tabbedPane.setSelectedComponent(panel);
    }

    // 重置用户表单
    private void resetUserForm(JTextField usernameField, JPasswordField passwordField,
                               JTextField realNameField, JComboBox<String> roleCombo,
                               JComboBox<String> statusCombo, JTextField phoneField,
                               JTextField qqField, JComboBox<String> collegeCombo,
                               JComboBox<String> majorCombo, JComboBox<String> gradeCombo,
                               JTextField classField, JComboBox<String> positionCombo,
                               JTextArea remarkArea) {

        usernameField.setText("");
        passwordField.setText("123456");
        realNameField.setText("");
        roleCombo.setSelectedIndex(0);
        statusCombo.setSelectedIndex(0);
        phoneField.setText("");
        qqField.setText("");
        collegeCombo.setSelectedIndex(0);
        majorCombo.setSelectedIndex(0);
        gradeCombo.setSelectedIndex(0);
        classField.setText("");
        positionCombo.setSelectedIndex(0);
        remarkArea.setText("");

        usernameField.requestFocus();
    }

    private void openEditUserPanel(int userId) {
        try {
            // 获取用户信息
            UserService userService = new UserService();
            User user = userService.getUserById(userId);

            if (user == null) {
                JOptionPane.showMessageDialog(this, "找不到指定的用户！", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JPanel panel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            // 标题
            JLabel titleLabel = new JLabel("修改用户信息 - " + user.getRealName() + " (" + user.getUsername() + ")");
            titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 16));
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 2;
            panel.add(titleLabel, gbc);

            // 基本信息（只读）
            gbc.gridwidth = 1;
            gbc.gridy = 1;
            gbc.gridx = 0;
            panel.add(new JLabel("用户名:"), gbc);

            gbc.gridx = 1;
            JLabel usernameLabel = new JLabel(user.getUsername());
            usernameLabel.setForeground(Color.GRAY);
            panel.add(usernameLabel, gbc);

            gbc.gridy = 2;
            gbc.gridx = 0;
            panel.add(new JLabel("真实姓名:"), gbc);

            gbc.gridx = 1;
            JLabel realNameLabel = new JLabel(user.getRealName());
            realNameLabel.setForeground(Color.GRAY);
            panel.add(realNameLabel, gbc);

            gbc.gridy = 3;
            gbc.gridx = 0;
            panel.add(new JLabel("当前角色:"), gbc);

            gbc.gridx = 1;
            JLabel roleLabel = new JLabel(user.getRoleName());
            roleLabel.setForeground(Color.GRAY);
            panel.add(roleLabel, gbc);

            gbc.gridy = 4;
            gbc.gridx = 0;
            panel.add(new JLabel("当前状态:"), gbc);

            gbc.gridx = 1;
            JLabel statusLabel = new JLabel(user.getStatus());
            statusLabel.setForeground(Color.GRAY);
            panel.add(statusLabel, gbc);

            // --- 可修改字段：手机号 ---
            gbc.gridy = 5;
            gbc.gridx = 0;
            panel.add(new JLabel("手机号*:"), gbc);

            gbc.gridx = 1;
            JTextField phoneField = new JTextField(20);
            phoneField.setText(user.getPhone() != null ? user.getPhone() : "");
            panel.add(phoneField, gbc);

            // --- 可修改字段：QQ号 ---
            gbc.gridy = 6;
            gbc.gridx = 0;
            panel.add(new JLabel("QQ号:"), gbc);

            gbc.gridx = 1;
            JTextField qqField = new JTextField(20);
            qqField.setText(user.getQq() != null ? user.getQq() : "");
            panel.add(qqField, gbc);

            // --- 可修改字段：校内职务 ---
            gbc.gridy = 7;
            gbc.gridx = 0;
            panel.add(new JLabel("校内职务:"), gbc);

            gbc.gridx = 1;
            JComboBox<String> positionCombo = new JComboBox<>(
                    new String[]{"无", "班干部", "学生会干部", "社团干部"}
            );
            if (user.getStudentPosition() != null) {
                String position = user.getStudentPosition();
                for (int i = 0; i < positionCombo.getItemCount(); i++) {
                    if (position.equals(positionCombo.getItemAt(i))) {
                        positionCombo.setSelectedIndex(i);
                        break;
                    }
                }
            }
            panel.add(positionCombo, gbc);

            // --- 可修改字段：角色 ---
            gbc.gridy = 8;
            gbc.gridx = 0;
            panel.add(new JLabel("角色:"), gbc);

            gbc.gridx = 1;
            JComboBox<String> roleCombo = new JComboBox<>(new String[]{"管理员", "超级管理员"});
            roleCombo.setSelectedIndex(user.getRoleId() == 1 ? 1 : 0);
            panel.add(roleCombo, gbc);

            // --- 可修改字段：备注 ---
            gbc.gridy = 9;
            gbc.gridx = 0;
            panel.add(new JLabel("备注:"), gbc);

            gbc.gridx = 1;
            JTextArea remarkArea = new JTextArea(3, 30);
            remarkArea.setLineWrap(true);
            if (user.getRemark() != null) {
                remarkArea.setText(user.getRemark());
            }
            JScrollPane remarkScroll = new JScrollPane(remarkArea);
            panel.add(remarkScroll, gbc);

            // --- 按钮区域 ---
            gbc.gridy = 10;
            gbc.gridx = 0;
            gbc.gridwidth = 2;
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
            JButton saveButton = new JButton("保存修改");
            JButton resetPasswordButton = new JButton("重置密码");
            JButton disableButton = new JButton("禁用用户");
            JButton enableButton = new JButton("启用用户");
            JButton deleteButton = new JButton("删除用户");
            JButton cancelButton = new JButton("取消");

            // 保存修改按钮事件
            saveButton.addActionListener(e -> {
                try {
                    String phone = phoneField.getText().trim();
                    String qq = qqField.getText().trim();
                    String studentPosition = (String) positionCombo.getSelectedItem();
                    String role = (String) roleCombo.getSelectedItem();
                    String remark = remarkArea.getText().trim();

                    // 验证必填字段
                    if (phone.isEmpty()) {
                        JOptionPane.showMessageDialog(panel, "手机号不能为空！");
                        phoneField.requestFocus();
                        return;
                    }

                    if (!phone.matches("^1[3-9]\\d{9}$")) {
                        JOptionPane.showMessageDialog(panel, "请输入有效的手机号码！");
                        phoneField.requestFocus();
                        return;
                    }

                    if (!qq.trim().isEmpty() && !qq.matches("^\\d{5,15}$")) {
                        JOptionPane.showMessageDialog(panel, "QQ号必须是5-15位数字！");
                        qqField.requestFocus();
                        return;
                    }

                    // 更新用户信息
                    User updatedUser = new User();
                    updatedUser.setUserId(userId);
                    updatedUser.setPhone(phone);
                    updatedUser.setQq(qq);
                    updatedUser.setStudentPosition(studentPosition);
                    updatedUser.setRoleId("超级管理员".equals(role) ? 1 : 2);
                    updatedUser.setRemark(remark);

                    boolean success = userService.updateUser(updatedUser);

                    if (success) {
                        JOptionPane.showMessageDialog(panel,
                                "用户信息修改成功！",
                                "成功",
                                JOptionPane.INFORMATION_MESSAGE);
                        tabbedPane.remove(panel);

                        // 刷新查询列表
                        refreshUserQueryTable();
                    } else {
                        JOptionPane.showMessageDialog(panel, "修改失败，请稍后重试！",
                                "错误", JOptionPane.ERROR_MESSAGE);
                    }

                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(panel, ex.getMessage(),
                            "验证错误", JOptionPane.WARNING_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel,
                            "修改失败：" + ex.getMessage(),
                            "系统错误",
                            JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            });

            // 重置密码按钮事件
            resetPasswordButton.addActionListener(e -> {
                int confirm = JOptionPane.showConfirmDialog(panel,
                        "确定要将用户密码重置为123456吗？",
                        "确认重置密码",
                        JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        boolean success = userService.resetPassword(userId);

                        if (success) {
                            JOptionPane.showMessageDialog(panel,
                                    "密码重置成功！\n新密码：123456",
                                    "重置成功",
                                    JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(panel,
                                    "重置失败，请稍后重试！",
                                    "重置失败",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(panel,
                                "重置失败：" + ex.getMessage(),
                                "系统错误",
                                JOptionPane.ERROR_MESSAGE);
                        ex.printStackTrace();
                    }
                }
            });

            // 禁用用户按钮事件
            disableButton.addActionListener(e -> {
                if ("禁用".equals(user.getStatus())) {
                    JOptionPane.showMessageDialog(panel, "用户已被禁用！");
                    return;
                }

                int confirm = JOptionPane.showConfirmDialog(panel,
                        "确定要禁用该用户吗？\n禁用后用户将无法登录系统。",
                        "确认禁用",
                        JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        boolean success = userService.disableUser(userId);

                        if (success) {
                            JOptionPane.showMessageDialog(panel,
                                    "用户禁用成功！",
                                    "禁用成功",
                                    JOptionPane.INFORMATION_MESSAGE);
                            tabbedPane.remove(panel);
                            refreshUserQueryTable();
                        } else {
                            JOptionPane.showMessageDialog(panel,
                                    "禁用失败，请稍后重试！",
                                    "禁用失败",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(panel,
                                "禁用失败：" + ex.getMessage(),
                                "系统错误",
                                JOptionPane.ERROR_MESSAGE);
                        ex.printStackTrace();
                    }
                }
            });

            // 启用用户按钮事件
            enableButton.addActionListener(e -> {
                if ("启用".equals(user.getStatus())) {
                    JOptionPane.showMessageDialog(panel, "用户已被启用！");
                    return;
                }

                int confirm = JOptionPane.showConfirmDialog(panel,
                        "确定要启用该用户吗？",
                        "确认启用",
                        JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        boolean success = userService.enableUser(userId);

                        if (success) {
                            JOptionPane.showMessageDialog(panel,
                                    "用户启用成功！",
                                    "启用成功",
                                    JOptionPane.INFORMATION_MESSAGE);
                            tabbedPane.remove(panel);
                            refreshUserQueryTable();
                        } else {
                            JOptionPane.showMessageDialog(panel,
                                    "启用失败，请稍后重试！",
                                    "启用失败",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(panel,
                                "启用失败：" + ex.getMessage(),
                                "系统错误",
                                JOptionPane.ERROR_MESSAGE);
                        ex.printStackTrace();
                    }
                }
            });

            // 删除用户按钮事件
            deleteButton.addActionListener(e -> {
                int confirm = JOptionPane.showConfirmDialog(panel,
                        "确定要删除该用户吗？\n此操作不可恢复！\n注意：已产生过信息的用户只能禁用，不能删除。",
                        "确认删除",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);

                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        boolean success = userService.deleteUser(userId);

                        if (success) {
                            JOptionPane.showMessageDialog(panel,
                                    "用户删除成功！",
                                    "删除成功",
                                    JOptionPane.INFORMATION_MESSAGE);
                            tabbedPane.remove(panel);
                            refreshUserQueryTable();
                        }
                    } catch (IllegalStateException ex) {
                        JOptionPane.showMessageDialog(panel,
                                ex.getMessage() + "\n请使用禁用功能。",
                                "不能删除",
                                JOptionPane.WARNING_MESSAGE);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(panel,
                                "删除失败：" + ex.getMessage(),
                                "系统错误",
                                JOptionPane.ERROR_MESSAGE);
                        ex.printStackTrace();
                    }
                }
            });

            cancelButton.addActionListener(e -> tabbedPane.remove(panel));

            buttonPanel.add(saveButton);
            buttonPanel.add(resetPasswordButton);
            buttonPanel.add(disableButton);
            buttonPanel.add(enableButton);
            buttonPanel.add(deleteButton);
            buttonPanel.add(cancelButton);
            panel.add(buttonPanel, gbc);

            // 根据用户状态设置按钮状态
            if ("禁用".equals(user.getStatus())) {
                disableButton.setEnabled(false);
                enableButton.setEnabled(true);
            } else {
                disableButton.setEnabled(true);
                enableButton.setEnabled(false);
            }

            // 设置面板大小
            panel.setPreferredSize(new Dimension(600, 600));

            tabbedPane.addTab("修改用户信息", panel);
            tabbedPane.setSelectedComponent(panel);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "打开修改页面失败：" + e.getMessage(),
                    "系统错误",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void openQueryUserPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // 查询条件面板
        JPanel conditionPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // 标题
        JLabel titleLabel = new JLabel("用户查询");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        conditionPanel.add(titleLabel, gbc);

        // 第一行：姓名和院系
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        conditionPanel.add(new JLabel("姓名:"), gbc);

        gbc.gridx = 1;
        JTextField nameField = new JTextField(10);
        conditionPanel.add(nameField, gbc);

        gbc.gridx = 2;
        conditionPanel.add(new JLabel("院系:"), gbc);

        gbc.gridx = 3;
        JComboBox<String> collegeCombo = new JComboBox<>();
        collegeCombo.addItem("全部");
        collegeCombo.addItem("软件学院");
        collegeCombo.addItem("计算机学院");
        collegeCombo.addItem("信息与通信工程学院");
        conditionPanel.add(collegeCombo, gbc);

        // 第二行：专业和班级
        gbc.gridy = 2;
        gbc.gridx = 0;
        conditionPanel.add(new JLabel("专业:"), gbc);

        gbc.gridx = 1;
        JComboBox<String> majorCombo = new JComboBox<>();
        majorCombo.addItem("全部");
        majorCombo.addItem("软件工程");
        majorCombo.addItem("网络工程");
        majorCombo.addItem("信息安全");
        majorCombo.addItem("计算机科学");
        majorCombo.addItem("人工智能");
        majorCombo.addItem("数据科学");
        conditionPanel.add(majorCombo, gbc);

        gbc.gridx = 2;
        conditionPanel.add(new JLabel("班级:"), gbc);

        gbc.gridx = 3;
        JTextField classField = new JTextField(8);
        conditionPanel.add(classField, gbc);

        // 第三行：年级和状态
        gbc.gridy = 3;
        gbc.gridx = 0;
        conditionPanel.add(new JLabel("年级:"), gbc);

        gbc.gridx = 1;
        JComboBox<String> gradeCombo = new JComboBox<>();
        gradeCombo.addItem("全部");
        gradeCombo.addItem("2021");
        gradeCombo.addItem("2022");
        gradeCombo.addItem("2023");
        gradeCombo.addItem("2024");
        gradeCombo.addItem("2025");
        conditionPanel.add(gradeCombo, gbc);

        gbc.gridx = 2;
        conditionPanel.add(new JLabel("状态:"), gbc);

        gbc.gridx = 3;
        JComboBox<String> statusCombo = new JComboBox<>(new String[]{"全部", "启用", "禁用"});
        conditionPanel.add(statusCombo, gbc);

        // 第四行：查询按钮
        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.gridwidth = 4;
        JPanel buttonPanelTop = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        JButton searchButton = new JButton("查询");
        JButton resetButton = new JButton("重置");
        JButton exportButton = new JButton("导出数据");
        buttonPanelTop.add(searchButton);
        buttonPanelTop.add(resetButton);
        buttonPanelTop.add(exportButton);
        conditionPanel.add(buttonPanelTop, gbc);

        // 用户列表表格
        // 注意：不显示用户名和密码，手机号显示明文
        String[] columns = {"ID", "姓名", "院系", "专业", "班级", "年级", "手机", "QQ", "校内职务", "用户状态", "角色"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable userTable = new JTable(tableModel);
        userTable.setRowHeight(25);
        userTable.setAutoCreateRowSorter(true);

        // 设置列宽
        userTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        userTable.getColumnModel().getColumn(1).setPreferredWidth(80);
        userTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        userTable.getColumnModel().getColumn(3).setPreferredWidth(100);
        userTable.getColumnModel().getColumn(4).setPreferredWidth(60);
        userTable.getColumnModel().getColumn(5).setPreferredWidth(60);
        userTable.getColumnModel().getColumn(6).setPreferredWidth(100);
        userTable.getColumnModel().getColumn(7).setPreferredWidth(80);
        userTable.getColumnModel().getColumn(8).setPreferredWidth(80);
        userTable.getColumnModel().getColumn(9).setPreferredWidth(60);
        userTable.getColumnModel().getColumn(10).setPreferredWidth(80);

        JScrollPane scrollPane = new JScrollPane(userTable);

        // 底部功能按钮
        JPanel buttonPanelBottom = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JButton viewButton = new JButton("查看详情");
        JButton editButton = new JButton("修改信息");
        JButton addButton = new JButton("添加用户");
        JButton refreshButton = new JButton("刷新数据");

        buttonPanelBottom.add(viewButton);
        buttonPanelBottom.add(editButton);
        buttonPanelBottom.add(addButton);
        buttonPanelBottom.add(refreshButton);

        // 添加事件监听器
        searchButton.addActionListener(e -> {
            performUserSearch(tableModel, nameField, collegeCombo, majorCombo,
                    classField, gradeCombo, statusCombo);
        });

        resetButton.addActionListener(e -> {
            nameField.setText("");
            collegeCombo.setSelectedIndex(0);
            majorCombo.setSelectedIndex(0);
            classField.setText("");
            gradeCombo.setSelectedIndex(0);
            statusCombo.setSelectedIndex(0);
            tableModel.setRowCount(0);
        });

        viewButton.addActionListener(e -> {
            int selectedRow = userTable.getSelectedRow();
            if (selectedRow >= 0) {
                int userId = (int) tableModel.getValueAt(selectedRow, 0);
                viewUserDetail(userId);
            } else {
                JOptionPane.showMessageDialog(panel, "请选择要查看的用户", "提示", JOptionPane.WARNING_MESSAGE);
            }
        });

        editButton.addActionListener(e -> {
            int selectedRow = userTable.getSelectedRow();
            if (selectedRow >= 0) {
                int userId = (int) tableModel.getValueAt(selectedRow, 0);
                openEditUserPanel(userId);
            } else {
                JOptionPane.showMessageDialog(panel, "请选择要修改的用户", "提示", JOptionPane.WARNING_MESSAGE);
            }
        });

        addButton.addActionListener(e -> {
            // 检查当前用户是否为超级管理员
            if (currentUser.getRoleId() != 1) {
                JOptionPane.showMessageDialog(panel,
                        "只有超级管理员才能添加用户！",
                        "权限不足",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            openAddUserPanel();
        });

        refreshButton.addActionListener(e -> {
            performUserSearch(tableModel, nameField, collegeCombo, majorCombo,
                    classField, gradeCombo, statusCombo);
        });

        exportButton.addActionListener(e -> {
            exportUserData(tableModel);
        });

        panel.add(conditionPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanelBottom, BorderLayout.SOUTH);

        tabbedPane.addTab("用户查询", panel);
        tabbedPane.setSelectedComponent(panel);

        // 页面加载时显示所有用户
        SwingUtilities.invokeLater(() -> {
            performUserSearch(tableModel, nameField, collegeCombo, majorCombo,
                    classField, gradeCombo, statusCombo);
        });
    }

    // 执行用户查询
    private void performUserSearch(DefaultTableModel tableModel,
                                   JTextField nameField,
                                   JComboBox<String> collegeCombo,
                                   JComboBox<String> majorCombo,
                                   JTextField classField,
                                   JComboBox<String> gradeCombo,
                                   JComboBox<String> statusCombo) {
        try {
            // 获取查询条件
            String realName = nameField.getText().trim();
            String collegeStr = (String) collegeCombo.getSelectedItem();
            String majorStr = (String) majorCombo.getSelectedItem();
            String className = classField.getText().trim();
            String grade = (String) gradeCombo.getSelectedItem();
            String status = (String) statusCombo.getSelectedItem();

            // 转换院系和专业为ID
            Integer collegeId = null;
            Integer majorId = null;

            // 院系映射
            java.util.Map<String, Integer> collegeMap = new java.util.HashMap<>();
            collegeMap.put("软件学院", 1);
            collegeMap.put("计算机学院", 2);
            collegeMap.put("信息与通信工程学院", 3);

            // 专业映射
            java.util.Map<String, Integer> majorMap = new java.util.HashMap<>();
            majorMap.put("软件工程", 1);
            majorMap.put("网络工程", 2);
            majorMap.put("信息安全", 3);
            majorMap.put("计算机科学", 4);
            majorMap.put("人工智能", 5);
            majorMap.put("数据科学", 6);

            if (!"全部".equals(collegeStr) && collegeMap.containsKey(collegeStr)) {
                collegeId = collegeMap.get(collegeStr);
            }

            if (!"全部".equals(majorStr) && majorMap.containsKey(majorStr)) {
                majorId = majorMap.get(majorStr);
            }

            // 处理年级
            if ("全部".equals(grade)) {
                grade = null;
            }

            // 处理状态
            if ("全部".equals(status)) {
                status = null;
            }

            // 调用Service查询
            UserService userService = new UserService();
            List<User> users = userService.findUsers(
                    realName.isEmpty() ? null : realName,
                    collegeId,
                    majorId,
                    grade,
                    className.isEmpty() ? null : className
            );

            // 如果指定了状态，需要过滤
            List<User> filteredUsers = users;
            if (status != null) {
                filteredUsers = new ArrayList<>();
                for (User user : users) {
                    if (status.equals(user.getStatus())) {
                        filteredUsers.add(user);
                    }
                }
            }

            // 更新表格
            updateUserTable(tableModel, filteredUsers);

            if (filteredUsers.isEmpty()) {
                JOptionPane.showMessageDialog(tabbedPane,
                        "未找到符合条件的用户",
                        "查询结果",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(tabbedPane,
                        "找到 " + filteredUsers.size() + " 个用户",
                        "查询结果",
                        JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(tabbedPane,
                    "查询过程中出现错误：" + ex.getMessage(),
                    "错误", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    // 更新用户表格
    private void updateUserTable(DefaultTableModel tableModel, List<User> users) {
        tableModel.setRowCount(0);

        // 院系名称映射
        java.util.Map<Integer, String> collegeNameMap = new java.util.HashMap<>();
        collegeNameMap.put(1, "软件学院");
        collegeNameMap.put(2, "计算机学院");
        collegeNameMap.put(3, "信息与通信工程学院");

        // 专业名称映射
        java.util.Map<Integer, String> majorNameMap = new java.util.HashMap<>();
        majorNameMap.put(1, "软件工程");
        majorNameMap.put(2, "网络工程");
        majorNameMap.put(3, "信息安全");
        majorNameMap.put(4, "计算机科学");
        majorNameMap.put(5, "人工智能");
        majorNameMap.put(6, "数据科学");

        for (User user : users) {
            Object[] rowData = {
                    user.getUserId(),
                    user.getRealName(),
                    collegeNameMap.getOrDefault(user.getCollegeId(), ""),
                    majorNameMap.getOrDefault(user.getMajorId(), ""),
                    user.getClassName() != null ? user.getClassName() : "",
                    user.getGrade() != null ? user.getGrade() : "",
                    user.getPhone() != null ? user.getPhone() : "", // 手机号显示明文
                    user.getQq() != null ? user.getQq() : "",
                    user.getStudentPosition() != null ? user.getStudentPosition() : "无",
                    user.getStatus(),
                    user.getRoleName()
            };
            tableModel.addRow(rowData);
        }

        tableModel.fireTableDataChanged();
    }

    // 查看用户详情
    private void viewUserDetail(int userId) {
        try {
            UserService userService = new UserService();
            User user = userService.getUserById(userId);

            if (user == null) {
                JOptionPane.showMessageDialog(tabbedPane, "找不到指定的用户", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 院系名称映射
            java.util.Map<Integer, String> collegeNameMap = new java.util.HashMap<>();
            collegeNameMap.put(1, "软件学院");
            collegeNameMap.put(2, "计算机学院");
            collegeNameMap.put(3, "信息与通信工程学院");

            // 专业名称映射
            java.util.Map<Integer, String> majorNameMap = new java.util.HashMap<>();
            majorNameMap.put(1, "软件工程");
            majorNameMap.put(2, "网络工程");
            majorNameMap.put(3, "信息安全");
            majorNameMap.put(4, "计算机科学");
            majorNameMap.put(5, "人工智能");
            majorNameMap.put(6, "数据科学");

            // 格式化详细信息
            StringBuilder detail = new StringBuilder();
            detail.append("用户信息详情\n");
            detail.append("====================\n");
            detail.append("用户ID：").append(user.getUserId()).append("\n");
            detail.append("用户名：").append(user.getUsername()).append("\n");
            detail.append("真实姓名：").append(user.getRealName()).append("\n");
            detail.append("角色：").append(user.getRoleName()).append("\n");
            detail.append("状态：").append(user.getStatus()).append("\n");
            detail.append("院系：").append(collegeNameMap.getOrDefault(user.getCollegeId(), "未设置")).append("\n");
            detail.append("专业：").append(majorNameMap.getOrDefault(user.getMajorId(), "未设置")).append("\n");
            detail.append("年级：").append(user.getGrade() != null ? user.getGrade() : "未设置").append("\n");
            detail.append("班级：").append(user.getClassName() != null ? user.getClassName() : "未设置").append("\n");
            detail.append("校内职务：").append(user.getStudentPosition() != null ? user.getStudentPosition() : "无").append("\n");
            detail.append("手机号：").append(user.getPhone() != null ? user.getPhone() : "未设置").append("\n");
            detail.append("QQ号：").append(user.getQq() != null ? user.getQq() : "未设置").append("\n");
            detail.append("备注：").append(user.getRemark() != null ? user.getRemark() : "无").append("\n");

            if (user.getCreateTime() != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                detail.append("创建时间：").append(sdf.format(user.getCreateTime())).append("\n");
            }

            JTextArea textArea = new JTextArea(detail.toString());
            textArea.setEditable(false);
            textArea.setFont(new Font("微软雅黑", Font.PLAIN, 12));

            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(400, 300));

            JOptionPane.showMessageDialog(tabbedPane, scrollPane,
                    "用户信息详情 - " + user.getRealName(),
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(tabbedPane,
                    "查看详情失败：" + e.getMessage(),
                    "错误", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // 刷新用户查询表格
    private void refreshUserQueryTable() {
        try {
            Component selectedTab = tabbedPane.getSelectedComponent();
            if (selectedTab != null && selectedTab instanceof JPanel) {
                JPanel panel = (JPanel) selectedTab;
                String tabTitle = tabbedPane.getTitleAt(tabbedPane.indexOfComponent(selectedTab));

                if (tabTitle != null && tabTitle.contains("用户查询")) {
                    System.out.println("刷新用户查询表格...");

                    // 简单方案：直接重新打开查询页面
                    int index = tabbedPane.indexOfComponent(selectedTab);
                    if (index >= 0) {
                        tabbedPane.remove(index);
                        openQueryUserPanel();
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("刷新用户表格时发生错误：" + e.getMessage());
            e.printStackTrace();
        }
    }

    // 导出用户数据
    private void exportUserData(DefaultTableModel tableModel) {
        // 导出为CSV格式
        StringBuilder csv = new StringBuilder();

        // 添加表头
        for (int i = 1; i < tableModel.getColumnCount(); i++) { // 从1开始，跳过ID列
            csv.append(tableModel.getColumnName(i));
            if (i < tableModel.getColumnCount() - 1) {
                csv.append(",");
            }
        }
        csv.append("\n");

        // 添加数据
        for (int row = 0; row < tableModel.getRowCount(); row++) {
            for (int col = 1; col < tableModel.getColumnCount(); col++) { // 从1开始，跳过ID列
                Object value = tableModel.getValueAt(row, col);
                csv.append(value != null ? value.toString() : "");
                if (col < tableModel.getColumnCount() - 1) {
                    csv.append(",");
                }
            }
            csv.append("\n");
        }

        // 显示导出结果
        JTextArea textArea = new JTextArea(csv.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("宋体", Font.PLAIN, 12));

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(600, 400));

        JOptionPane.showMessageDialog(tabbedPane, scrollPane,
                "导出用户数据（可复制到Excel）",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void openAddCollegePanel() {
        // 创建添加院系面板
        JPanel addCollegePanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 标题
        JLabel titleLabel = new JLabel("添加院系");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        addCollegePanel.add(titleLabel, gbc);

        // 院系名称
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        addCollegePanel.add(new JLabel("院系名称:"), gbc);

        gbc.gridx = 1;
        JTextField nameField = new JTextField(20);
        addCollegePanel.add(nameField, gbc);

        // 院系代码
        gbc.gridy = 2;
        gbc.gridx = 0;
        addCollegePanel.add(new JLabel("院系代码:"), gbc);

        gbc.gridx = 1;
        JTextField codeField = new JTextField(10);
        addCollegePanel.add(codeField, gbc);

        // 备注
        gbc.gridy = 3;
        gbc.gridx = 0;
        addCollegePanel.add(new JLabel("备注:"), gbc);

        gbc.gridx = 1;
        JTextArea remarkArea = new JTextArea(3, 20);
        remarkArea.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(remarkArea);
        addCollegePanel.add(scrollPane, gbc);

        // 按钮
        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton saveButton = new JButton("保存");
        JButton cancelButton = new JButton("取消");

        saveButton.addActionListener(e -> {
            String collegeName = nameField.getText().trim();
            String collegeCode = codeField.getText().trim();
            String remark = remarkArea.getText().trim();

            if (collegeName.isEmpty() || collegeCode.isEmpty()) {
                JOptionPane.showMessageDialog(this, "院系名称和代码不能为空！");
                return;
            }

            // 调用Service层保存院系
            try {
                College college = new College();
                college.setCollegeName(collegeName);
                college.setCollegeCode(collegeCode);
                college.setRemark(remark);

                // TODO: 调用CollegeService保存
                JOptionPane.showMessageDialog(this, "院系添加成功！");
                tabbedPane.remove(addCollegePanel);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "添加失败：" + ex.getMessage());
            }
        });

        cancelButton.addActionListener(e -> {
            tabbedPane.remove(addCollegePanel);
        });

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        addCollegePanel.add(buttonPanel, gbc);

        // 添加到标签页
        tabbedPane.addTab("添加院系", addCollegePanel);
        tabbedPane.setSelectedComponent(addCollegePanel);
    }
    // 在AdminMainFrame类中添加以下方法
    private void openAddMajorPanel() {
        // 创建添加专业面板
        JPanel addMajorPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 标题
        JLabel titleLabel = new JLabel("添加专业");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        addMajorPanel.add(titleLabel, gbc);

        // 专业名称
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        addMajorPanel.add(new JLabel("专业名称:"), gbc);

        gbc.gridx = 1;
        JTextField nameField = new JTextField(20);
        addMajorPanel.add(nameField, gbc);

        // 所属院系
        gbc.gridy = 2;
        gbc.gridx = 0;
        addMajorPanel.add(new JLabel("所属院系:"), gbc);

        gbc.gridx = 1;
        // 获取所有院系供选择
        JComboBox<String> collegeCombo = new JComboBox<>();
        collegeCombo.addItem("请选择院系");
        // TODO: 从数据库加载院系列表
        collegeCombo.addItem("软件学院");
        collegeCombo.addItem("计算机学院");
        collegeCombo.addItem("信息与通信工程学院");
        addMajorPanel.add(collegeCombo, gbc);

        // 备注
        gbc.gridy = 3;
        gbc.gridx = 0;
        addMajorPanel.add(new JLabel("备注:"), gbc);

        gbc.gridx = 1;
        JTextArea remarkArea = new JTextArea(3, 20);
        remarkArea.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(remarkArea);
        addMajorPanel.add(scrollPane, gbc);

        // 按钮
        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton saveButton = new JButton("保存");
        JButton cancelButton = new JButton("取消");

        saveButton.addActionListener(e -> {
            String majorName = nameField.getText().trim();
            String selectedCollege = (String) collegeCombo.getSelectedItem();
            String remark = remarkArea.getText().trim();

            if (majorName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "专业名称不能为空！");
                return;
            }

            if ("请选择院系".equals(selectedCollege)) {
                JOptionPane.showMessageDialog(this, "请选择所属院系！");
                return;
            }

            // TODO: 调用MajorService保存专业
            try {
                JOptionPane.showMessageDialog(this, "专业添加成功！");
                tabbedPane.remove(addMajorPanel);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "添加失败：" + ex.getMessage());
            }
        });

        cancelButton.addActionListener(e -> {
            tabbedPane.remove(addMajorPanel);
        });

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        addMajorPanel.add(buttonPanel, gbc);

        // 添加到标签页
        tabbedPane.addTab("添加专业", addMajorPanel);
        tabbedPane.setSelectedComponent(addMajorPanel);
    }

    private void openAddMemberPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 标题
        JLabel titleLabel = new JLabel("添加实验室成员");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        // --- 第一行：学号 ---
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        panel.add(new JLabel("学号*:"), gbc);

        gbc.gridx = 1;
        JTextField stuIdField = new JTextField(20);
        panel.add(stuIdField, gbc);

        // --- 第二行：姓名 ---
        gbc.gridy = 2;
        gbc.gridx = 0;
        panel.add(new JLabel("姓名*:"), gbc);

        gbc.gridx = 1;
        JTextField nameField = new JTextField(20);
        panel.add(nameField, gbc);

        // --- 第三行：性别 ---
        gbc.gridy = 3;
        gbc.gridx = 0;
        panel.add(new JLabel("性别*:"), gbc);

        gbc.gridx = 1;
        JComboBox<String> genderCombo = new JComboBox<>(new String[]{"男", "女"});
        panel.add(genderCombo, gbc);

        // --- 第四行：年级 ---
        gbc.gridy = 4;
        gbc.gridx = 0;
        panel.add(new JLabel("年级*:"), gbc);

        gbc.gridx = 1;
        JComboBox<String> gradeCombo = new JComboBox<>(new String[]{"2021", "2022", "2023", "2024", "2025"});
        panel.add(gradeCombo, gbc);

        // --- 第五行：籍贯 ---
        gbc.gridy = 5;
        gbc.gridx = 0;
        panel.add(new JLabel("籍贯*:"), gbc);

        gbc.gridx = 1;
        JTextField hometownField = new JTextField(20);
        panel.add(hometownField, gbc);

        // --- 第六行：电话 ---
        gbc.gridy = 6;
        gbc.gridx = 0;
        panel.add(new JLabel("电话*:"), gbc);

        gbc.gridx = 1;
        JTextField phoneField = new JTextField(20);
        panel.add(phoneField, gbc);

        // --- 第七行：院系专业 ---
        gbc.gridy = 7;
        gbc.gridx = 0;
        panel.add(new JLabel("院系专业*:"), gbc);

        gbc.gridx = 1;
        JPanel collegeMajorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        // 院系下拉框（需要从数据库加载）
        JComboBox<String> collegeCombo = new JComboBox<>();
        collegeCombo.addItem("请选择院系");
        // TODO: 从数据库加载院系
        collegeCombo.addItem("软件学院");
        collegeCombo.addItem("计算机学院");
        collegeCombo.addItem("信息与通信工程学院");

        // 专业下拉框（根据院系动态加载）
        JComboBox<String> majorCombo = new JComboBox<>();
        majorCombo.addItem("请先选择院系");
        majorCombo.setEnabled(false);

        // 院系选择监听器
        collegeCombo.addActionListener(e -> {
            String selectedCollege = (String) collegeCombo.getSelectedItem();
            if (!"请选择院系".equals(selectedCollege)) {
                majorCombo.removeAllItems();
                majorCombo.addItem("请选择专业");
                majorCombo.setEnabled(true);

                // 根据选择的院系加载专业
                if ("软件学院".equals(selectedCollege)) {
                    majorCombo.addItem("软件工程");
                    majorCombo.addItem("网络工程");
                    majorCombo.addItem("信息安全");
                } else if ("计算机学院".equals(selectedCollege)) {
                    majorCombo.addItem("计算机科学");
                    majorCombo.addItem("人工智能");
                    majorCombo.addItem("数据科学");
                } else if ("信息与通信工程学院".equals(selectedCollege)) {
                    majorCombo.addItem("通信工程");
                    majorCombo.addItem("电子信息工程");
                    majorCombo.addItem("物联网工程");
                }
            } else {
                majorCombo.removeAllItems();
                majorCombo.addItem("请先选择院系");
                majorCombo.setEnabled(false);
            }
        });

        collegeMajorPanel.add(collegeCombo);
        collegeMajorPanel.add(new JLabel("-"));
        collegeMajorPanel.add(majorCombo);
        panel.add(collegeMajorPanel, gbc);

        // --- 第八行：校内职务 ---
        gbc.gridy = 8;
        gbc.gridx = 0;
        panel.add(new JLabel("校内职务:"), gbc);

        gbc.gridx = 1;
        JComboBox<String> studentPositionCombo = new JComboBox<>(
                new String[]{"无", "班干部", "学生会干部", "社团干部"}
        );
        panel.add(studentPositionCombo, gbc);

        // --- 第九行：加入日期 ---
        gbc.gridy = 9;
        gbc.gridx = 0;
        panel.add(new JLabel("加入日期*:"), gbc);

        gbc.gridx = 1;
        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField joinDateField = new JTextField(10);
        joinDateField.setText(new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()));
        JButton datePickerButton = new JButton("选择日期");

        datePickerButton.addActionListener(e -> {
            // 可以使用JCalendar等组件，这里简单处理
            String dateStr = JOptionPane.showInputDialog(panel, "请输入日期(yyyy-MM-dd):",
                    joinDateField.getText());
            if (dateStr != null && dateStr.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
                joinDateField.setText(dateStr);
            }
        });

        datePanel.add(joinDateField);
        datePanel.add(datePickerButton);
        panel.add(datePanel, gbc);

        // --- 第十行：状态 ---
        gbc.gridy = 10;
        gbc.gridx = 0;
        panel.add(new JLabel("状态*:"), gbc);

        gbc.gridx = 1;
        JComboBox<String> statusCombo = new JComboBox<>(
                new String[]{"正常", "退出", "毕业"}
        );
        panel.add(statusCombo, gbc);

        // --- 第十一行：实验室职务 ---
        gbc.gridy = 11;
        gbc.gridx = 0;
        panel.add(new JLabel("实验室职务:"), gbc);

        gbc.gridx = 1;
        JComboBox<String> labPositionCombo = new JComboBox<>(
                new String[]{"无", "秘书长", "副秘书长", "技术总监", "项目经理", "组长"}
        );
        panel.add(labPositionCombo, gbc);

        // --- 第十二行：备注 ---
        gbc.gridy = 12;
        gbc.gridx = 0;
        panel.add(new JLabel("备注:"), gbc);

        gbc.gridx = 1;
        JTextArea remarkArea = new JTextArea(3, 30);
        remarkArea.setLineWrap(true);
        JScrollPane remarkScroll = new JScrollPane(remarkArea);
        panel.add(remarkScroll, gbc);

        // --- 按钮区域 ---
        gbc.gridy = 13;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton saveButton = new JButton("保存");
        JButton cancelButton = new JButton("取消");
        JButton resetButton = new JButton("重置");

        // 设置按钮样式
        saveButton.setFont(new Font("微软雅黑", Font.BOLD, 14));
        cancelButton.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        resetButton.setFont(new Font("微软雅黑", Font.PLAIN, 14));

        saveButton.setPreferredSize(new Dimension(80, 30));
        cancelButton.setPreferredSize(new Dimension(80, 30));
        resetButton.setPreferredSize(new Dimension(80, 30));

        // 保存按钮事件
        saveButton.addActionListener(e -> {
            try {
                // 收集表单数据
                String stuId = stuIdField.getText().trim();
                String name = nameField.getText().trim();
                String gender = "男".equals(genderCombo.getSelectedItem()) ? "M" : "F";
                String grade = (String) gradeCombo.getSelectedItem();
                String hometown = hometownField.getText().trim();
                String phone = phoneField.getText().trim();
                String selectedCollege = (String) collegeCombo.getSelectedItem();
                String selectedMajor = (String) majorCombo.getSelectedItem();
                String studentPosition = (String) studentPositionCombo.getSelectedItem();
                String joinDateStr = joinDateField.getText().trim();
                String status = (String) statusCombo.getSelectedItem();
                String labPosition = (String) labPositionCombo.getSelectedItem();
                String remark = remarkArea.getText().trim();

                // 基本验证
                if (stuId.isEmpty() || name.isEmpty() || hometown.isEmpty() || phone.isEmpty() ||
                        joinDateStr.isEmpty()) {
                    JOptionPane.showMessageDialog(panel, "带*的字段为必填项！",
                            "输入错误", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if ("请选择院系".equals(selectedCollege) || "请先选择院系".equals(selectedMajor) ||
                        "请选择专业".equals(selectedMajor)) {
                    JOptionPane.showMessageDialog(panel, "请选择院系和专业！",
                            "选择错误", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // 数据格式验证
                if (!stuId.matches("^\\d+$")) {
                    JOptionPane.showMessageDialog(panel, "学号必须为数字！",
                            "格式错误", JOptionPane.WARNING_MESSAGE);
                    stuIdField.requestFocus();
                    return;
                }

                if (!name.matches("^[\u4e00-\u9fa5]{2,5}$")) {
                    JOptionPane.showMessageDialog(panel, "姓名必须为2-5个汉字！",
                            "格式错误", JOptionPane.WARNING_MESSAGE);
                    nameField.requestFocus();
                    return;
                }

                if (!phone.matches("^1[3-9]\\d{9}$")) {
                    JOptionPane.showMessageDialog(panel, "请输入正确的手机号码！",
                            "格式错误", JOptionPane.WARNING_MESSAGE);
                    phoneField.requestFocus();
                    return;
                }

                if (!joinDateStr.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
                    JOptionPane.showMessageDialog(panel, "日期格式应为yyyy-MM-dd！",
                            "格式错误", JOptionPane.WARNING_MESSAGE);
                    joinDateField.requestFocus();
                    return;
                }

                // 创建Member对象
                Member member = new Member();
                member.setStuId(stuId);
                member.setName(name);
                member.setGender(gender);
                member.setGrade(grade);
                member.setHometown(hometown);
                member.setPhone(phone);

                // 设置院系和专业ID（这里需要根据名称查询ID）
                // 实际项目中应该从数据库查询ID
                Map<String, Integer> collegeMap = Map.of(
                        "软件学院", 1,
                        "计算机学院", 2,
                        "信息与通信工程学院", 3
                );
                Map<String, Integer> majorMap = Map.of(
                        "软件工程", 1,
                        "网络工程", 2,
                        "信息安全", 3,
                        "计算机科学", 4,
                        "人工智能", 5,
                        "数据科学", 6,
                        "通信工程", 7,
                        "电子信息工程", 8,
                        "物联网工程", 9
                );

                member.setCollegeId(collegeMap.getOrDefault(selectedCollege, 1));
                member.setMajorId(majorMap.getOrDefault(selectedMajor, 1));
                member.setStudentPosition(studentPosition);
                member.setJoinDate(java.sql.Date.valueOf(joinDateStr));
                member.setStatus(status);
                member.setLabPosition(labPosition);
                member.setRemark(remark);

                // 调用Service层保存
                MemberService memberService = new MemberService();
                boolean success = memberService.addMember(member);

                if (success) {
                    JOptionPane.showMessageDialog(panel,
                            "成员添加成功！\n姓名：" + name + "\n学号：" + stuId,
                            "成功",
                            JOptionPane.INFORMATION_MESSAGE);
                    tabbedPane.remove(panel);
                } else {
                    JOptionPane.showMessageDialog(panel, "添加失败，请稍后重试！",
                            "错误", JOptionPane.ERROR_MESSAGE);
                }

            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(panel, ex.getMessage(),
                        "验证错误", JOptionPane.WARNING_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel,
                        "添加失败：" + ex.getMessage(),
                        "系统错误",
                        JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        // 取消按钮事件
        cancelButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(panel,
                    "确定要取消添加吗？未保存的数据将丢失。",
                    "确认取消",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                tabbedPane.remove(panel);
            }
        });

        // 重置按钮事件
        resetButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(panel,
                    "确定要重置所有输入吗？",
                    "确认重置",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                stuIdField.setText("");
                nameField.setText("");
                genderCombo.setSelectedIndex(0);
                gradeCombo.setSelectedIndex(0);
                hometownField.setText("");
                phoneField.setText("");
                collegeCombo.setSelectedIndex(0);
                studentPositionCombo.setSelectedIndex(0);
                joinDateField.setText(new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()));
                statusCombo.setSelectedIndex(0);
                labPositionCombo.setSelectedIndex(0);
                remarkArea.setText("");
                stuIdField.requestFocus();
            }
        });

        buttonPanel.add(saveButton);
        buttonPanel.add(resetButton);
        buttonPanel.add(cancelButton);
        panel.add(buttonPanel, gbc);

        // 添加面板到标签页
        tabbedPane.addTab("添加成员", panel);
        tabbedPane.setSelectedComponent(panel);
    }

    private void openEditMemberPanel(int memberId) {
        try {
            // 获取成员信息
            MemberService memberService = new MemberService();
            Member member = memberService.getMemberById(memberId);

            if (member == null) {
                JOptionPane.showMessageDialog(this, "找不到指定的成员信息！", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JPanel panel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            // 标题
            JLabel titleLabel = new JLabel("修改成员信息 - " + member.getName() + " (" + member.getStuId() + ")");
            titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 16));
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 2;
            panel.add(titleLabel, gbc);

            // 基本信息（只读）
            gbc.gridwidth = 1;
            gbc.gridy = 1;
            gbc.gridx = 0;
            panel.add(new JLabel("学号:"), gbc);

            gbc.gridx = 1;
            JLabel stuIdLabel = new JLabel(member.getStuId());
            stuIdLabel.setForeground(Color.GRAY);
            panel.add(stuIdLabel, gbc);

            gbc.gridy = 2;
            gbc.gridx = 0;
            panel.add(new JLabel("姓名:"), gbc);

            gbc.gridx = 1;
            JLabel nameLabel = new JLabel(member.getName());
            nameLabel.setForeground(Color.GRAY);
            panel.add(nameLabel, gbc);

            gbc.gridy = 3;
            gbc.gridx = 0;
            panel.add(new JLabel("性别:"), gbc);

            gbc.gridx = 1;
            String genderStr = "M".equals(member.getGender()) ? "男" : "女";
            JLabel genderLabel = new JLabel(genderStr);
            genderLabel.setForeground(Color.GRAY);
            panel.add(genderLabel, gbc);

            gbc.gridy = 4;
            gbc.gridx = 0;
            panel.add(new JLabel("年级:"), gbc);

            gbc.gridx = 1;
            JLabel gradeLabel = new JLabel(member.getGrade());
            gradeLabel.setForeground(Color.GRAY);
            panel.add(gradeLabel, gbc);

            // 院系专业 - 这里需要根据ID获取名称
            gbc.gridy = 5;
            gbc.gridx = 0;
            panel.add(new JLabel("院系专业:"), gbc);

            gbc.gridx = 1;
            String collegeName = getCollegeNameById(member.getCollegeId());
            String majorName = getMajorNameById(member.getMajorId());
            JLabel collegeMajorLabel = new JLabel(collegeName + " - " + majorName);
            collegeMajorLabel.setForeground(Color.GRAY);
            panel.add(collegeMajorLabel, gbc);

            // 可修改字段：电话
            gbc.gridy = 6;
            gbc.gridx = 0;
            panel.add(new JLabel("电话*:"), gbc);

            gbc.gridx = 1;
            JTextField phoneField = new JTextField(20);
            phoneField.setText(member.getPhone() != null ? member.getPhone() : "");
            panel.add(phoneField, gbc);

            // 可修改字段：校内职务
            gbc.gridy = 7;
            gbc.gridx = 0;
            panel.add(new JLabel("校内职务:"), gbc);

            gbc.gridx = 1;
            JComboBox<String> studentPositionCombo = new JComboBox<>(
                    new String[]{"无", "班干部", "学生会干部", "社团干部"}
            );
            // 设置默认选中项
            if (member.getStudentPosition() != null) {
                String position = member.getStudentPosition();
                for (int i = 0; i < studentPositionCombo.getItemCount(); i++) {
                    if (position.equals(studentPositionCombo.getItemAt(i))) {
                        studentPositionCombo.setSelectedIndex(i);
                        break;
                    }
                }
            }
            panel.add(studentPositionCombo, gbc);

            // 可修改字段：状态
            gbc.gridy = 8;
            gbc.gridx = 0;
            panel.add(new JLabel("状态*:"), gbc);

            gbc.gridx = 1;
            JComboBox<String> statusCombo = new JComboBox<>(
                    new String[]{"正常", "退出", "毕业"}
            );
            // 设置默认选中项
            if (member.getStatus() != null) {
                String status = member.getStatus();
                for (int i = 0; i < statusCombo.getItemCount(); i++) {
                    if (status.equals(statusCombo.getItemAt(i))) {
                        statusCombo.setSelectedIndex(i);
                        break;
                    }
                }
            }
            panel.add(statusCombo, gbc);

            // 可修改字段：实验室职务
            gbc.gridy = 9;
            gbc.gridx = 0;
            panel.add(new JLabel("实验室职务:"), gbc);

            gbc.gridx = 1;
            JComboBox<String> labPositionCombo = new JComboBox<>(
                    new String[]{"无", "秘书长", "副秘书长", "技术总监", "项目经理", "组长"}
            );
            // 设置默认选中项
            if (member.getLabPosition() != null) {
                String labPosition = member.getLabPosition();
                for (int i = 0; i < labPositionCombo.getItemCount(); i++) {
                    if (labPosition.equals(labPositionCombo.getItemAt(i))) {
                        labPositionCombo.setSelectedIndex(i);
                        break;
                    }
                }
            }
            panel.add(labPositionCombo, gbc);

            // 可修改字段：备注
            gbc.gridy = 10;
            gbc.gridx = 0;
            panel.add(new JLabel("备注:"), gbc);

            gbc.gridx = 1;
            JTextArea remarkArea = new JTextArea(4, 30);
            remarkArea.setLineWrap(true);
            if (member.getRemark() != null) {
                remarkArea.setText(member.getRemark());
            }
            JScrollPane remarkScroll = new JScrollPane(remarkArea);
            panel.add(remarkScroll, gbc);

            // 按钮区域
            gbc.gridy = 11;
            gbc.gridx = 0;
            gbc.gridwidth = 2;
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
            JButton saveButton = new JButton("保存修改");
            JButton cancelButton = new JButton("取消");

            // 保存按钮事件
            saveButton.addActionListener(e -> {
                try {
                    // 收集修改的数据
                    String phone = phoneField.getText().trim();
                    String studentPosition = (String) studentPositionCombo.getSelectedItem();
                    String status = (String) statusCombo.getSelectedItem();
                    String labPosition = (String) labPositionCombo.getSelectedItem();
                    String remark = remarkArea.getText().trim();

                    // 验证必填字段
                    if (phone.isEmpty()) {
                        JOptionPane.showMessageDialog(panel, "电话不能为空！",
                                "输入错误", JOptionPane.WARNING_MESSAGE);
                        phoneField.requestFocus();
                        return;
                    }

                    if (!phone.matches("^1[3-9]\\d{9}$")) {
                        JOptionPane.showMessageDialog(panel, "请输入正确的手机号码！",
                                "格式错误", JOptionPane.WARNING_MESSAGE);
                        phoneField.requestFocus();
                        return;
                    }

                    // 更新成员信息
                    Member updatedMember = new Member();
                    updatedMember.setMemberId(memberId);
                    updatedMember.setPhone(phone);
                    updatedMember.setStudentPosition(studentPosition);
                    updatedMember.setStatus(status);
                    updatedMember.setLabPosition(labPosition);
                    updatedMember.setRemark(remark);

                    // 调用Service层保存
                    boolean success = memberService.updateMember(updatedMember);

                    if (success) {
                        JOptionPane.showMessageDialog(panel,
                                "成员信息修改成功！",
                                "成功",
                                JOptionPane.INFORMATION_MESSAGE);
                        tabbedPane.remove(panel);

                        // 刷新查询列表（如果当前有查询页面）
                        refreshMemberQueryTable();
                    } else {
                        JOptionPane.showMessageDialog(panel, "修改失败，请稍后重试！",
                                "错误", JOptionPane.ERROR_MESSAGE);
                    }

                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(panel, ex.getMessage(),
                            "验证错误", JOptionPane.WARNING_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel,
                            "修改失败：" + ex.getMessage(),
                            "系统错误",
                            JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            });

            cancelButton.addActionListener(e -> tabbedPane.remove(panel));

            buttonPanel.add(saveButton);
            buttonPanel.add(cancelButton);
            panel.add(buttonPanel, gbc);

            // 设置面板的最小大小以确保显示完整
            panel.setPreferredSize(new Dimension(600, 550));

            tabbedPane.addTab("修改成员", panel);
            tabbedPane.setSelectedComponent(panel);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "打开修改页面失败：" + e.getMessage(),
                    "系统错误",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    private void openQueryMemberPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // 查询条件面板
        JPanel conditionPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 标题
        JLabel titleLabel = new JLabel("成员查询");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        conditionPanel.add(titleLabel, gbc);

        // 第一行：姓名和年级
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        conditionPanel.add(new JLabel("姓名:"), gbc);

        gbc.gridx = 1;
        JTextField nameField = new JTextField(10);
        conditionPanel.add(nameField, gbc);

        gbc.gridx = 2;
        conditionPanel.add(new JLabel("年级:"), gbc);

        gbc.gridx = 3;
        JComboBox<String> gradeCombo = new JComboBox<>(new String[]{"全部", "2021", "2022", "2023", "2024"});
        conditionPanel.add(gradeCombo, gbc);

        // 第二行：电话和状态
        gbc.gridy = 2;
        gbc.gridx = 0;
        conditionPanel.add(new JLabel("电话:"), gbc);

        gbc.gridx = 1;
        JTextField phoneField = new JTextField(15);
        conditionPanel.add(phoneField, gbc);

        gbc.gridx = 2;
        conditionPanel.add(new JLabel("状态:"), gbc);

        gbc.gridx = 3;
        JComboBox<String> statusCombo = new JComboBox<>(new String[]{"全部", "正常", "退出", "毕业"});
        conditionPanel.add(statusCombo, gbc);

        // 第三行：院系和专业
        gbc.gridy = 3;
        gbc.gridx = 0;
        conditionPanel.add(new JLabel("院系:"), gbc);

        gbc.gridx = 1;
        JComboBox<String> collegeCombo = new JComboBox<>(new String[]{"全部", "软件学院", "计算机学院", "信息与通信工程学院"});
        conditionPanel.add(collegeCombo, gbc);

        gbc.gridx = 2;
        conditionPanel.add(new JLabel("专业:"), gbc);

        gbc.gridx = 3;
        JComboBox<String> majorCombo = new JComboBox<>(new String[]{"全部", "软件工程", "计算机科学", "通信工程"});
        conditionPanel.add(majorCombo, gbc);

        // 第四行：管理职务和按钮
        gbc.gridy = 4;
        gbc.gridx = 0;
        conditionPanel.add(new JLabel("管理职务:"), gbc);

        gbc.gridx = 1;
        JComboBox<String> positionCombo = new JComboBox<>(new String[]{"全部", "是", "否"});
        conditionPanel.add(positionCombo, gbc);

        gbc.gridx = 2;
        gbc.gridwidth = 2;
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton searchButton = new JButton("查询");
        JButton resetButton = new JButton("重置");
        JButton exportButton = new JButton("导出查询结果");

        searchButton.addActionListener(e -> performMemberSearch());
        resetButton.addActionListener(e -> resetQueryFields());
        exportButton.addActionListener(e -> exportQueryResults());

        buttonPanel.add(searchButton);
        buttonPanel.add(resetButton);
        buttonPanel.add(exportButton);
        conditionPanel.add(buttonPanel, gbc);

        // 成员列表表格
        String[] columnNames = {"ID", "学号", "姓名", "性别", "年级", "电话", "院系", "专业",
                "校内职务", "实验室职务", "状态", "加入日期"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable memberTable = new JTable(tableModel);
        memberTable.setRowHeight(25);
        memberTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        memberTable.getColumnModel().getColumn(1).setPreferredWidth(100);

        JScrollPane scrollPane = new JScrollPane(memberTable);

        // 操作按钮面板
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton viewButton = new JButton("查看详情");
        JButton editButton = new JButton("修改信息");

        viewButton.addActionListener(e -> viewMemberDetail(memberTable, tableModel));
        editButton.addActionListener(e -> editMemberInfo(memberTable, tableModel));

        actionPanel.add(viewButton);
        actionPanel.add(editButton);

        panel.add(conditionPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(actionPanel, BorderLayout.SOUTH);

        tabbedPane.addTab("成员查询", panel);
        tabbedPane.setSelectedComponent(panel);

        // 执行查询
        performMemberSearch();
    }

    /**
     * 查看成员详情
     */
    private void viewMemberDetail(JTable memberTable, DefaultTableModel tableModel) {
        int selectedRow = memberTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "请先选择一个成员！", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 从表格中获取选中的成员信息
        int memberId = (int) tableModel.getValueAt(selectedRow, 0); // 第一列是ID
        String stuId = (String) tableModel.getValueAt(selectedRow, 1);
        String name = (String) tableModel.getValueAt(selectedRow, 2);

        // TODO: 实际项目中应该从数据库获取详细信息
        // 这里先显示一个简单的详情对话框
        String detail = String.format("成员详情：\n" +
                        "-------------------\n" +
                        "ID：%d\n" +
                        "学号：%s\n" +
                        "姓名：%s\n" +
                        "性别：%s\n" +
                        "年级：%s\n" +
                        "院系：%s\n" +
                        "专业：%s\n" +
                        "校内职务：%s\n" +
                        "实验室职务：%s\n" +
                        "状态：%s",
                memberId,
                stuId,
                name,
                tableModel.getValueAt(selectedRow, 3), // 性别
                tableModel.getValueAt(selectedRow, 4), // 年级
                tableModel.getValueAt(selectedRow, 6), // 院系
                tableModel.getValueAt(selectedRow, 7), // 专业
                tableModel.getValueAt(selectedRow, 8), // 校内职务
                tableModel.getValueAt(selectedRow, 9), // 实验室职务
                tableModel.getValueAt(selectedRow, 10)); // 状态

        JOptionPane.showMessageDialog(this, detail, "成员详情", JOptionPane.INFORMATION_MESSAGE);
    }
    /**
     * 编辑成员信息
     */
    private void editMemberInfo(JTable memberTable, DefaultTableModel tableModel) {
        int selectedRow = memberTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "请先选择一个成员！", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 从表格中获取成员ID
        int memberId = (int) tableModel.getValueAt(selectedRow, 0);

        // 调用已有的编辑方法（需要先实现这个方法）
        // 如果还没有 openEditMemberPanel 方法，需要先实现它
        openEditMemberPanel(memberId);
    }

    // 执行成员查询
    private void performMemberSearch() {
        try {
            // 1. 获取当前查询面板
            Component selectedTab = tabbedPane.getSelectedComponent();
            if (selectedTab == null || !(selectedTab instanceof JPanel)) {
                return;
            }

            JPanel panel = (JPanel) selectedTab;

            // 2. 从面板中获取查询条件（简化版）
            String name = "";
            String grade = "";
            String phone = "";
            String status = "";
            Boolean hasManagementPosition = null;

            // 直接获取界面组件（需要根据你的实际界面结构调整）
            // 这里假设组件是直接添加的
            Component[] components = panel.getComponents();
            for (Component comp : components) {
                if (comp instanceof JPanel) {
                    // 递归查找组件
                    findQueryFields((JPanel) comp);
                }
            }

            // 3. 调用Service查询
            MemberService memberService = new MemberService();
            java.util.List<Member> members = memberService.findMembers(
                    name, grade, phone, null, null, status, hasManagementPosition
            );

            // 4. 更新表格
            updateMemberTable(members);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "查询失败: " + e.getMessage(),
                    "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    // 添加这个辅助方法来获取查询字段
    private void findQueryFields(JPanel panel) {
        // 这里需要根据你的实际界面结构实现
        // 临时返回空值，让查询正常工作
    }

    // 确保 updateMemberTable 方法存在
    private void updateMemberTable(java.util.List<Member> members) {
        DefaultTableModel tableModel = getCurrentTableModel();
        if (tableModel != null) {
            tableModel.setRowCount(0); // 清空现有数据

            for (Member member : members) {
                Object[] row = new Object[12];
                row[0] = member.getMemberId();
                row[1] = member.getStuId();
                row[2] = member.getName();
                row[3] = "M".equals(member.getGender()) ? "男" : "女";
                row[4] = member.getGrade();
                row[5] = member.getPhone();
                row[6] = getCollegeNameById(member.getCollegeId());
                row[7] = getMajorNameById(member.getMajorId());
                row[8] = member.getStudentPosition();
                row[9] = member.getLabPosition();
                row[10] = member.getStatus();
                row[11] = member.getJoinDate();

                tableModel.addRow(row);
            }

            System.out.println("更新了 " + members.size() + " 条记录到表格");
        }
    }

    // 重置查询条件
    private void resetQueryFields() {
        // TODO: 实现重置逻辑
        System.out.println("重置查询条件");
    }

    // 导出查询结果
    private void exportQueryResults() {
        // TODO: 实现导出逻辑
        System.out.println("导出查询结果");
    }


    private void openMemberStatPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // 统计选项
        JPanel optionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        optionPanel.add(new JLabel("统计维度:"));
        JComboBox<String> dimensionCombo = new JComboBox<>(
                new String[]{"按年级分布", "按院系分布", "按实验室职务", "按状态统计"}
        );
        optionPanel.add(dimensionCombo);

        JButton genButton = new JButton("生成统计");
        optionPanel.add(genButton);

        // 统计图表区域
        JPanel chartPanel = new JPanel(new BorderLayout());
        chartPanel.add(new JLabel("此处显示统计图表", SwingConstants.CENTER), BorderLayout.CENTER);

        // 统计数据表格
        String[] statColumns = {"类别", "人数", "占比"};
        Object[][] statData = {
                {"2021级", 15, "30%"},
                {"2022级", 20, "40%"},
                {"2023级", 10, "20%"},
                {"2024级", 5, "10%"}
        };

        JTable statTable = new JTable(statData, statColumns);
        JScrollPane statScrollPane = new JScrollPane(statTable);

        panel.add(optionPanel, BorderLayout.NORTH);
        panel.add(chartPanel, BorderLayout.CENTER);
        panel.add(statScrollPane, BorderLayout.SOUTH);

        tabbedPane.addTab("成员统计", panel);
        tabbedPane.setSelectedComponent(panel);
    }

    private void openAddCoursePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 标题
        JLabel titleLabel = new JLabel("公开课信息录入");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        panel.add(titleLabel, gbc);

        // --- 技术专题 ---
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        panel.add(new JLabel("技术专题*:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        JTextField topicField = new JTextField(30);
        panel.add(topicField, gbc);

        // --- 开课日期（带日历选择） ---
        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        panel.add(new JLabel("开课日期*:"), gbc);

        gbc.gridx = 1;
        JTextField dateField = new JTextField(12);
        dateField.setText(new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()));
        panel.add(dateField, gbc);

        gbc.gridx = 2;
        JButton datePickerButton = new JButton("选择日期");
        datePickerButton.addActionListener(e -> {
            // 简单日期选择对话框
            String dateStr = JOptionPane.showInputDialog(panel,
                    "请输入开课日期(yyyy-MM-dd):", dateField.getText());
            if (dateStr != null && dateStr.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
                dateField.setText(dateStr);
            }
        });
        panel.add(datePickerButton, gbc);

        // --- 主讲人 ---
        gbc.gridy = 3;
        gbc.gridx = 0;
        panel.add(new JLabel("主讲人*:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        JTextField speakerField = new JTextField(20);
        panel.add(speakerField, gbc);

        // --- 组织人（多选） ---
        gbc.gridy = 4;
        gbc.gridx = 0;
        panel.add(new JLabel("组织人*:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        JPanel organizerPanel = new JPanel(new BorderLayout());

        // 创建多选列表
        DefaultListModel<String> organizerModel = new DefaultListModel<>();
        // 从数据库加载学生和老师（这里用示例数据）
        organizerModel.addElement("张三（学生）");
        organizerModel.addElement("李四（学生）");
        organizerModel.addElement("王老师（UOLab）");
        organizerModel.addElement("赵老师（UOLab）");
        organizerModel.addElement("刘老师（UOLab）");

        JList<String> organizerList = new JList<>(organizerModel);
        organizerList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane organizerScroll = new JScrollPane(organizerList);
        organizerScroll.setPreferredSize(new Dimension(250, 80));

        organizerPanel.add(organizerScroll, BorderLayout.CENTER);

        // 添加/删除按钮
        JPanel organizerButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addOrganizerButton = new JButton("添加");
        JTextField newOrganizerField = new JTextField(15);

        addOrganizerButton.addActionListener(e -> {
            String newOrganizer = newOrganizerField.getText().trim();
            if (!newOrganizer.isEmpty()) {
                organizerModel.addElement(newOrganizer);
                newOrganizerField.setText("");
            }
        });

        organizerButtonPanel.add(new JLabel("自定义:"));
        organizerButtonPanel.add(newOrganizerField);
        organizerButtonPanel.add(addOrganizerButton);

        organizerPanel.add(organizerButtonPanel, BorderLayout.SOUTH);
        panel.add(organizerPanel, gbc);

        // --- 地点 ---
        gbc.gridy = 5;
        gbc.gridx = 0;
        panel.add(new JLabel("地点*:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        JTextField locationField = new JTextField(30);
        panel.add(locationField, gbc);

        // --- 群体说明 ---
        gbc.gridy = 6;
        gbc.gridx = 0;
        panel.add(new JLabel("群体说明*:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        JTextArea groupDescArea = new JTextArea(3, 30);
        groupDescArea.setLineWrap(true);
        JScrollPane groupDescScroll = new JScrollPane(groupDescArea);
        panel.add(groupDescScroll, gbc);

        // --- 授课方式 ---
        gbc.gridy = 7;
        gbc.gridx = 0;
        panel.add(new JLabel("授课方式*:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        JComboBox<String> methodCombo = new JComboBox<>(new String[]{"线下", "直播", "录播"});
        panel.add(methodCombo, gbc);

        // --- 创新学分值 ---
        gbc.gridy = 8;
        gbc.gridx = 0;
        panel.add(new JLabel("创新学分值*:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        JPanel creditPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JSpinner creditSpinner = new JSpinner(new SpinnerNumberModel(0.5, 0.0, 10.0, 0.5));
        creditSpinner.setPreferredSize(new Dimension(80, 25));
        creditPanel.add(creditSpinner);
        creditPanel.add(new JLabel("分"));
        panel.add(creditPanel, gbc);

        // --- 参加人数 ---
        gbc.gridy = 9;
        gbc.gridx = 0;
        panel.add(new JLabel("参加人数:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        JSpinner attendanceSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 500, 1));
        attendanceSpinner.setPreferredSize(new Dimension(80, 25));
        panel.add(attendanceSpinner, gbc);

        // --- 备注 ---
        gbc.gridy = 10;
        gbc.gridx = 0;
        panel.add(new JLabel("备注:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        JTextArea remarkArea = new JTextArea(3, 30);
        remarkArea.setLineWrap(true);
        JScrollPane remarkScroll = new JScrollPane(remarkArea);
        panel.add(remarkScroll, gbc);

        // --- 保存和取消按钮 ---
        gbc.gridy = 11;
        gbc.gridx = 0;
        gbc.gridwidth = 3;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton saveButton = new JButton("保存");
        JButton cancelButton = new JButton("取消");

        // 保存按钮事件
        saveButton.addActionListener(e -> {
            try {
                // 收集数据
                String topic = topicField.getText().trim();
                String dateStr = dateField.getText().trim();
                String speaker = speakerField.getText().trim();

                // 获取选中的组织人（对应organizer字段）
                java.util.List<String> selectedOrganizers = organizerList.getSelectedValuesList();
                String organizers = String.join("; ", selectedOrganizers);

                String location = locationField.getText().trim();
                String targetGroup = groupDescArea.getText().trim();  // 对应target_group字段
                String method = (String) methodCombo.getSelectedItem();
                double creditValue = (Double) creditSpinner.getValue();  // 对应credit_value字段
                int participantsCount = (Integer) attendanceSpinner.getValue();  // 对应participants_count字段
                String remark = remarkArea.getText().trim();

                // 验证必填字段
                if (topic.isEmpty() || dateStr.isEmpty() || speaker.isEmpty() ||
                        targetGroup.isEmpty()) {  // target_group是NOT NULL
                    JOptionPane.showMessageDialog(panel, "带*的字段为必填项！");
                    return;
                }

                // 创建Course对象
                Course course = new Course();
                course.setTopic(topic);
                course.setCourseDate(java.sql.Date.valueOf(dateStr));
                course.setSpeaker(speaker);
                course.setOrganizers(organizers);
                course.setLocation(location);
                course.setGroupDesc(targetGroup);
                course.setMethod(method);
                course.setCredit(creditValue);
                course.setAttendance(participantsCount);
                course.setRemark(remark);

                // 调用Service保存
                CourseService courseService = new CourseService();
                int courseId = courseService.addCourse(course);

                // ... 后续处理 ...

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // 取消按钮事件
        cancelButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(panel,
                    "确定要取消录入吗？未保存的数据将丢失。",
                    "确认取消",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                tabbedPane.remove(panel);
            }
        });

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        panel.add(buttonPanel, gbc);

        // 设置面板大小
        panel.setPreferredSize(new Dimension(600, 700));

        // 添加到标签页
        tabbedPane.addTab("公开课录入", panel);
        tabbedPane.setSelectedComponent(panel);
    }

    /**
     * 清空公开课表单
     */
    private void clearCourseForm(JTextField topicField, JTextField dateField,
                                 JTextField speakerField, JList<String> organizerList,
                                 JTextField locationField, JTextArea groupDescArea,
                                 JComboBox<String> methodCombo, JSpinner creditSpinner,
                                 JSpinner attendanceSpinner, JTextArea remarkArea) {
        topicField.setText("");
        dateField.setText(new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()));
        speakerField.setText("");
        organizerList.clearSelection();
        locationField.setText("");
        groupDescArea.setText("");
        methodCombo.setSelectedIndex(0);
        creditSpinner.setValue(0.5);
        attendanceSpinner.setValue(0);
        remarkArea.setText("");
        topicField.requestFocus();
    }

    private void openQueryCoursePanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // 查询条件面板 - 使用GridBagLayout进行更灵活的布局
        JPanel conditionPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        int row = 0;

        // 第一行：技术专题关键词
        gbc.gridx = 0;
        gbc.gridy = row;
        conditionPanel.add(new JLabel("技术专题:"), gbc);

        gbc.gridx = 1;
        JTextField topicField = new JTextField(15);
        conditionPanel.add(topicField, gbc);

        gbc.gridx = 2;
        conditionPanel.add(new JLabel("主讲人:"), gbc);

        gbc.gridx = 3;
        JTextField speakerField = new JTextField(10);
        conditionPanel.add(speakerField, gbc);

        // 第二行：时间范围
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        conditionPanel.add(new JLabel("开始日期:"), gbc);

        gbc.gridx = 1;
        JTextField startDateField = new JTextField(10);
        startDateField.putClientProperty("JTextField.placeholderText", "格式:2024-01-01");
        conditionPanel.add(startDateField, gbc);

        gbc.gridx = 2;
        conditionPanel.add(new JLabel("结束日期:"), gbc);

        gbc.gridx = 3;
        JTextField endDateField = new JTextField(10);
        endDateField.putClientProperty("JTextField.placeholderText", "格式:2024-12-31");
        conditionPanel.add(endDateField, gbc);

        // 第三行：院系、年级、专业
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        conditionPanel.add(new JLabel("目标院系:"), gbc);

        gbc.gridx = 1;
        JComboBox<String> departmentCombo = new JComboBox<>(new String[]{"全部", "软件学院", "计算机学院", "信息学院", "电子学院"});
        conditionPanel.add(departmentCombo, gbc);

        gbc.gridx = 2;
        conditionPanel.add(new JLabel("目标年级:"), gbc);

        gbc.gridx = 3;
        JComboBox<String> gradeCombo = new JComboBox<>(new String[]{"全部", "2021级", "2022级", "2023级", "2024级"});
        conditionPanel.add(gradeCombo, gbc);

        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        conditionPanel.add(new JLabel("目标专业:"), gbc);

        gbc.gridx = 1;
        JComboBox<String> majorCombo = new JComboBox<>(new String[]{"全部", "软件工程", "计算机科学与技术", "网络工程", "数据科学"});
        conditionPanel.add(majorCombo, gbc);

        gbc.gridx = 2;
        conditionPanel.add(new JLabel("授课方式:"), gbc);

        gbc.gridx = 3;
        JComboBox<String> methodCombo = new JComboBox<>(new String[]{"全部", "线下", "线上", "混合"});
        conditionPanel.add(methodCombo, gbc);

        // 第四行：查询按钮
        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 4;
        gbc.anchor = GridBagConstraints.CENTER;

        JPanel buttonPanelTop = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        JButton searchButton = new JButton("查询");
        JButton resetButton = new JButton("重置");
        buttonPanelTop.add(searchButton);
        buttonPanelTop.add(resetButton);
        conditionPanel.add(buttonPanelTop, gbc);

        // 课程列表
        String[] columns = {"ID", "技术专题", "主讲人", "开课日期", "目标群体", "授课方式", "学分值", "参加人数", "地点"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(tableModel);
        table.setRowHeight(25);
        table.setAutoCreateRowSorter(true);

        // 设置列宽
        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(1).setPreferredWidth(150);
        table.getColumnModel().getColumn(2).setPreferredWidth(80);
        table.getColumnModel().getColumn(3).setPreferredWidth(100);
        table.getColumnModel().getColumn(4).setPreferredWidth(120);
        table.getColumnModel().getColumn(5).setPreferredWidth(70);
        table.getColumnModel().getColumn(6).setPreferredWidth(60);
        table.getColumnModel().getColumn(7).setPreferredWidth(70);
        table.getColumnModel().getColumn(8).setPreferredWidth(100);

        JScrollPane scrollPane = new JScrollPane(table);

        // 底部功能按钮
        JPanel buttonPanelBottom = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JButton detailButton = new JButton("查看详情");
        JButton exportButton = new JButton("导出名单");
        JButton notifyButton = new JButton("发送通知");
        JButton statsButton = new JButton("统计信息");

        buttonPanelBottom.add(detailButton);
        buttonPanelBottom.add(exportButton);
        buttonPanelBottom.add(notifyButton);
        buttonPanelBottom.add(statsButton);

        // 添加事件监听器
        searchButton.addActionListener(e -> {
            performCourseSearch(tableModel, topicField, speakerField, startDateField,
                    endDateField, departmentCombo, gradeCombo, majorCombo, methodCombo);
        });

        resetButton.addActionListener(e -> {
            topicField.setText("");
            speakerField.setText("");
            startDateField.setText("");
            endDateField.setText("");
            departmentCombo.setSelectedIndex(0);
            gradeCombo.setSelectedIndex(0);
            majorCombo.setSelectedIndex(0);
            methodCombo.setSelectedIndex(0);
            tableModel.setRowCount(0);
        });

        panel.add(conditionPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanelBottom, BorderLayout.SOUTH);

        tabbedPane.addTab("公开课查询", panel);
        tabbedPane.setSelectedComponent(panel);
    }

    // 执行课程查询的方法
    private void performCourseSearch(DefaultTableModel tableModel,
                                     JTextField topicField,
                                     JTextField speakerField,
                                     JTextField startDateField,
                                     JTextField endDateField,
                                     JComboBox<String> departmentCombo,
                                     JComboBox<String> gradeCombo,
                                     JComboBox<String> majorCombo,
                                     JComboBox<String> methodCombo) {

        try {
            // 获取查询条件
            String topic = topicField.getText().trim();
            String speaker = speakerField.getText().trim();
            String startDateStr = startDateField.getText().trim();
            String endDateStr = endDateField.getText().trim();
            String department = (String) departmentCombo.getSelectedItem();
            String grade = (String) gradeCombo.getSelectedItem();
            String major = (String) majorCombo.getSelectedItem();
            String method = (String) methodCombo.getSelectedItem();

            // 转换日期
            java.sql.Date startDate = null;
            java.sql.Date endDate = null;

            if (!startDateStr.isEmpty()) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    java.util.Date utilDate = sdf.parse(startDateStr);
                    startDate = new java.sql.Date(utilDate.getTime());
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(tabbedPane,
                            "开始日期格式错误，请使用YYYY-MM-DD格式",
                            "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            if (!endDateStr.isEmpty()) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    java.util.Date utilDate = sdf.parse(endDateStr);
                    endDate = new java.sql.Date(utilDate.getTime());
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(tabbedPane,
                            "结束日期格式错误，请使用YYYY-MM-DD格式",
                            "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            // 调用Service进行查询
            CourseService courseService = new CourseService();
            List<Course> courses = courseService.findCourses(
                    topic.isEmpty() ? null : topic,
                    speaker.isEmpty() ? null : speaker,
                    startDate,
                    endDate,
                    "全部".equals(method) ? null : method
            );

            // 更新表格
            updateCourseTable(tableModel, courses);

            if (courses.isEmpty()) {
                JOptionPane.showMessageDialog(tabbedPane,  // 注意：是tabbedPane，不是tabledPane
                        "未找到符合条件的课程记录",  // 去掉message:
                        "查询结果",  // 去掉title:
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(tabbedPane,
                        "找到 " + courses.size() + " 条记录",
                        "查询结果",
                        JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(tabbedPane,
                    "查询过程中出现错误：" + ex.getMessage(),
                    "错误", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    // 更新课程表格的辅助方法 - 移到类级别
    private void updateCourseTable(DefaultTableModel tableModel, List<Course> courses) {
        tableModel.setRowCount(0);

        System.out.println("=== 开始更新表格 ===");
        System.out.println("查询到 " + courses.size() + " 条记录");

        for (Course course : courses) {
            Object[] rowData = {
                    course.getCourseId(),
                    course.getTopic(),
                    course.getSpeaker(),
                    formatDate(course.getCourseDate()),
                    course.getGroupDesc() != null ? course.getGroupDesc() : "",
                    course.getMethod() != null ? course.getMethod() : "",
                    course.getCredit(),
                    course.getAttendance(),
                    course.getLocation() != null ? course.getLocation() : ""
            };
            tableModel.addRow(rowData);

            // 调试输出
            System.out.println("添加到表格: " + course.getTopic() + " - " + course.getSpeaker());
        }

        System.out.println("=== 表格更新完成 ===");

        // 强制刷新表格
        tableModel.fireTableDataChanged();
    }

    // 日期格式化辅助方法 - 移到类级别
    private String formatDate(java.util.Date date) {
        if (date == null) {
            return "";
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return sdf.format(date);
        } catch (Exception e) {
            return date.toString();
        }
    }

    private void openAddCompetitionPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 标题
        JLabel titleLabel = new JLabel("参赛信息录入");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        // --- 竞赛类别（选择） ---
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        panel.add(new JLabel("竞赛类别*:"), gbc);

        gbc.gridx = 1;
        // 创建下拉框，并添加默认选项
        JComboBox<String> categoryCombo = new JComboBox<>();
        categoryCombo.addItem("请选择竞赛类别");

        // 从数据库加载已有的竞赛类别
        CompetitionService competitionService = new CompetitionService();
        List<String> categories = competitionService.getAllCategories();
        for (String category : categories) {
            categoryCombo.addItem(category);
        }

        // 添加其他常见竞赛类别
        categoryCombo.addItem("挑战杯");
        categoryCombo.addItem("互联网+");
        categoryCombo.addItem("ACM程序设计大赛");
        categoryCombo.addItem("全国大学生数学建模竞赛");
        categoryCombo.addItem("全国大学生电子设计竞赛");
        categoryCombo.addItem("全国大学生软件创新大赛");
        categoryCombo.addItem("全国大学生信息安全竞赛");
        categoryCombo.addItem("全国大学生机器人竞赛");
        categoryCombo.addItem("蓝桥杯全国软件和信息技术专业人才大赛");
        categoryCombo.addItem("其他");

        // 添加可编辑功能
        JPanel categoryPanel = new JPanel(new BorderLayout());
        categoryPanel.add(categoryCombo, BorderLayout.CENTER);

        // 添加自定义输入框
        JTextField customCategoryField = new JTextField(15);
        customCategoryField.setVisible(false);
        categoryPanel.add(customCategoryField, BorderLayout.SOUTH);

        // 监听选择变化
        categoryCombo.addActionListener(e -> {
            if ("其他".equals(categoryCombo.getSelectedItem())) {
                customCategoryField.setVisible(true);
                categoryPanel.revalidate();
            } else {
                customCategoryField.setVisible(false);
                categoryPanel.revalidate();
            }
        });

        panel.add(categoryPanel, gbc);

        // --- 项目题目（2-30个汉字字母组合） ---
        gbc.gridy = 2;
        gbc.gridx = 0;
        panel.add(new JLabel("项目题目*:"), gbc);

        gbc.gridx = 1;
        JTextField titleField = new JTextField(30);
        panel.add(titleField, gbc);

        // --- 参赛年度（默认当前年度） ---
        gbc.gridy = 3;
        gbc.gridx = 0;
        panel.add(new JLabel("参赛年度*:"), gbc);

        gbc.gridx = 1;
        int currentYear = java.time.Year.now().getValue();
        JComboBox<Integer> yearCombo = new JComboBox<>();
        for (int y = currentYear - 5; y <= currentYear + 1; y++) {
            yearCombo.addItem(y);
        }
        yearCombo.setSelectedItem(currentYear);
        panel.add(yearCombo, gbc);

        // --- 组长（汉字2-6个） ---
        gbc.gridy = 4;
        gbc.gridx = 0;
        panel.add(new JLabel("组长姓名*:"), gbc);

        gbc.gridx = 1;
        JTextField leaderField = new JTextField(20);
        panel.add(leaderField, gbc);

        // --- 组员（汉字2-6个，多个用逗号分隔） ---
        gbc.gridy = 5;
        gbc.gridx = 0;
        panel.add(new JLabel("组员姓名:"), gbc);

        gbc.gridx = 1;
        JTextArea membersArea = new JTextArea(3, 30);
        membersArea.setLineWrap(true);
        membersArea.setWrapStyleWord(true);
        JScrollPane membersScroll = new JScrollPane(membersArea);

        JPanel membersPanel = new JPanel(new BorderLayout());
        membersPanel.add(membersScroll, BorderLayout.CENTER);

        // 添加提示标签
        JLabel membersHint = new JLabel("多个组员用逗号分隔，如：张三,李四,王五");
        membersHint.setFont(new Font("微软雅黑", Font.PLAIN, 10));
        membersHint.setForeground(Color.GRAY);
        membersPanel.add(membersHint, BorderLayout.SOUTH);

        panel.add(membersPanel, gbc);

        // --- 获奖等级（特等、一等、二等、三等、优秀奖、无） ---
        gbc.gridy = 6;
        gbc.gridx = 0;
        panel.add(new JLabel("获奖等级:"), gbc);

        gbc.gridx = 1;
        JComboBox<String> awardCombo = new JComboBox<>(
                new String[]{"无", "优秀奖", "三等奖", "二等奖", "一等奖", "特等奖"}
        );
        awardCombo.setSelectedItem("无");
        panel.add(awardCombo, gbc);

        // --- 指导老师（汉字2-6个） ---
        gbc.gridy = 7;
        gbc.gridx = 0;
        panel.add(new JLabel("指导老师:"), gbc);

        gbc.gridx = 1;
        JTextField advisorField = new JTextField(20);
        panel.add(advisorField, gbc);

        // --- 备注 ---
        gbc.gridy = 8;
        gbc.gridx = 0;
        panel.add(new JLabel("备注:"), gbc);

        gbc.gridx = 1;
        JTextArea remarkArea = new JTextArea(3, 30);
        remarkArea.setLineWrap(true);
        JScrollPane remarkScroll = new JScrollPane(remarkArea);
        panel.add(remarkScroll, gbc);

        // --- 保存和取消按钮 ---
        gbc.gridy = 9;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton saveButton = new JButton("保存");
        JButton cancelButton = new JButton("取消");
        JButton resetButton = new JButton("重置");

        // 保存按钮事件
        saveButton.addActionListener(e -> {
            try {
                // 获取竞赛类别
                String category;
                if ("其他".equals(categoryCombo.getSelectedItem())) {
                    category = customCategoryField.getText().trim();
                } else {
                    category = (String) categoryCombo.getSelectedItem();
                }

                if ("请选择竞赛类别".equals(category)) {
                    JOptionPane.showMessageDialog(panel, "请选择或输入竞赛类别！");
                    return;
                }

                // 获取其他数据
                String title = titleField.getText().trim();
                int year = (Integer) yearCombo.getSelectedItem();
                String leader = leaderField.getText().trim();
                String members = membersArea.getText().trim();
                String awardLevel = (String) awardCombo.getSelectedItem();
                String advisor = advisorField.getText().trim();
                String remark = remarkArea.getText().trim();

                // 验证数据
                if (title.isEmpty() || leader.isEmpty()) {
                    JOptionPane.showMessageDialog(panel, "带*的字段为必填项！");
                    return;
                }

                // 创建参赛信息对象
                Competition competition = new Competition();
                competition.setCategory(category);
                competition.setTitle(title);
                competition.setYear(year);
                competition.setLeader(leader);
                competition.setMembers(members);
                competition.setAwardLevel(awardLevel);
                competition.setAdvisor(advisor);
                competition.setRemark(remark);

                // 调用Service保存
                CompetitionService service = new CompetitionService();
                int competitionId = service.addCompetition(competition);

                if (competitionId > 0) {
                    JOptionPane.showMessageDialog(panel,
                            "参赛信息保存成功！\n" +
                                    "项目题目：" + title + "\n" +
                                    "竞赛类别：" + category + "\n" +
                                    "参赛年度：" + year,
                            "保存成功",
                            JOptionPane.INFORMATION_MESSAGE);

                    // 清空表单
                    resetCompetitionForm(categoryCombo, customCategoryField, titleField,
                            yearCombo, leaderField, membersArea,
                            awardCombo, advisorField, remarkArea);
                } else {
                    JOptionPane.showMessageDialog(panel,
                            "保存失败，请稍后重试！",
                            "保存失败",
                            JOptionPane.ERROR_MESSAGE);
                }

            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(panel,
                        ex.getMessage(),
                        "输入错误",
                        JOptionPane.WARNING_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel,
                        "保存失败：" + ex.getMessage(),
                        "系统错误",
                        JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        // 重置按钮事件
        resetButton.addActionListener(e -> {
            resetCompetitionForm(categoryCombo, customCategoryField, titleField,
                    yearCombo, leaderField, membersArea,
                    awardCombo, advisorField, remarkArea);
        });

        // 取消按钮事件
        cancelButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(panel,
                    "确定要取消录入吗？未保存的数据将丢失。",
                    "确认取消",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                tabbedPane.remove(panel);
            }
        });

        buttonPanel.add(saveButton);
        buttonPanel.add(resetButton);
        buttonPanel.add(cancelButton);
        panel.add(buttonPanel, gbc);

        // 设置面板大小
        panel.setPreferredSize(new Dimension(600, 650));

        // 添加到标签页
        tabbedPane.addTab("参赛信息录入", panel);
        tabbedPane.setSelectedComponent(panel);
    }

    // 重置参赛信息表单
    private void resetCompetitionForm(JComboBox<String> categoryCombo, JTextField customCategoryField,
                                      JTextField titleField, JComboBox<Integer> yearCombo,
                                      JTextField leaderField, JTextArea membersArea,
                                      JComboBox<String> awardCombo, JTextField advisorField,
                                      JTextArea remarkArea) {

        categoryCombo.setSelectedIndex(0);
        customCategoryField.setText("");
        customCategoryField.setVisible(false);
        titleField.setText("");

        int currentYear = java.time.Year.now().getValue();
        yearCombo.setSelectedItem(currentYear);

        leaderField.setText("");
        membersArea.setText("");
        awardCombo.setSelectedItem("无");
        advisorField.setText("");
        remarkArea.setText("");

        titleField.requestFocus();
    }

    private void openEditCompetitionPanel(int competitionId) {
        try {
            // 获取参赛信息
            CompetitionService service = new CompetitionService();
            Competition competition = service.getCompetitionById(competitionId);

            if (competition == null) {
                JOptionPane.showMessageDialog(this, "找不到指定的参赛信息！", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JPanel panel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            // 标题
            JLabel titleLabel = new JLabel("修改参赛信息 - " + competition.getTitle() + " (" + competition.getYear() + "年)");
            titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 16));
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 2;
            panel.add(titleLabel, gbc);

            // 显示基本信息（只读）
            // 竞赛类别
            gbc.gridwidth = 1;
            gbc.gridy = 1;
            gbc.gridx = 0;
            panel.add(new JLabel("竞赛类别:"), gbc);

            gbc.gridx = 1;
            JLabel categoryLabel = new JLabel(competition.getCategory());
            categoryLabel.setForeground(Color.GRAY);
            panel.add(categoryLabel, gbc);

            // 项目题目
            gbc.gridy = 2;
            gbc.gridx = 0;
            panel.add(new JLabel("项目题目:"), gbc);

            gbc.gridx = 1;
            JLabel titleLabelField = new JLabel(competition.getTitle());
            titleLabelField.setForeground(Color.GRAY);
            panel.add(titleLabelField, gbc);

            // 参赛年度
            gbc.gridy = 3;
            gbc.gridx = 0;
            panel.add(new JLabel("参赛年度:"), gbc);

            gbc.gridx = 1;
            JLabel yearLabel = new JLabel(String.valueOf(competition.getYear()) + "年");
            yearLabel.setForeground(Color.GRAY);
            panel.add(yearLabel, gbc);

            // 组长
            gbc.gridy = 4;
            gbc.gridx = 0;
            panel.add(new JLabel("组长姓名:"), gbc);

            gbc.gridx = 1;
            JLabel leaderLabel = new JLabel(competition.getLeader());
            leaderLabel.setForeground(Color.GRAY);
            panel.add(leaderLabel, gbc);

            // 指导老师
            gbc.gridy = 5;
            gbc.gridx = 0;
            panel.add(new JLabel("指导老师:"), gbc);

            gbc.gridx = 1;
            JLabel advisorLabel = new JLabel(competition.getAdvisor() != null ? competition.getAdvisor() : "未填写");
            advisorLabel.setForeground(Color.GRAY);
            panel.add(advisorLabel, gbc);

            // --- 可修改字段：组员姓名 ---
            gbc.gridy = 6;
            gbc.gridx = 0;
            panel.add(new JLabel("组员姓名:"), gbc);

            gbc.gridx = 1;
            JTextArea membersArea = new JTextArea(3, 30);
            membersArea.setLineWrap(true);
            membersArea.setWrapStyleWord(true);
            if (competition.getMembers() != null) {
                membersArea.setText(competition.getMembers());
            }
            JScrollPane membersScroll = new JScrollPane(membersArea);

            JPanel membersPanel = new JPanel(new BorderLayout());
            membersPanel.add(membersScroll, BorderLayout.CENTER);

            JLabel membersHint = new JLabel("多个组员用逗号分隔，如：张三,李四,王五");
            membersHint.setFont(new Font("微软雅黑", Font.PLAIN, 10));
            membersHint.setForeground(Color.GRAY);
            membersPanel.add(membersHint, BorderLayout.SOUTH);

            panel.add(membersPanel, gbc);

            // --- 可修改字段：获奖等级 ---
            gbc.gridy = 7;
            gbc.gridx = 0;
            panel.add(new JLabel("获奖等级:"), gbc);

            gbc.gridx = 1;
            JComboBox<String> awardCombo = new JComboBox<>(
                    new String[]{"无", "优秀奖", "三等奖", "二等奖", "一等奖", "特等奖"}
            );
            // 设置当前值
            String currentAward = competition.getAwardLevel() != null ? competition.getAwardLevel() : "无";
            awardCombo.setSelectedItem(currentAward);
            panel.add(awardCombo, gbc);

            // --- 可修改字段：备注 ---
            gbc.gridy = 8;
            gbc.gridx = 0;
            panel.add(new JLabel("备注:"), gbc);

            gbc.gridx = 1;
            JTextArea remarkArea = new JTextArea(3, 30);
            remarkArea.setLineWrap(true);
            if (competition.getRemark() != null) {
                remarkArea.setText(competition.getRemark());
            }
            JScrollPane remarkScroll = new JScrollPane(remarkArea);
            panel.add(remarkScroll, gbc);

            // --- 按钮区域 ---
            gbc.gridy = 9;
            gbc.gridx = 0;
            gbc.gridwidth = 2;
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
            JButton saveButton = new JButton("保存修改");
            JButton deleteButton = new JButton("删除记录");
            JButton cancelButton = new JButton("取消");

            // 保存修改按钮事件
            saveButton.addActionListener(e -> {
                try {
                    String members = membersArea.getText().trim();
                    String awardLevel = (String) awardCombo.getSelectedItem();
                    String remark = remarkArea.getText().trim();

                    // 验证组员格式
                    if (!members.isEmpty()) {
                        String[] memberArray = members.split(",");
                        for (String member : memberArray) {
                            String trimmedMember = member.trim();
                            if (!trimmedMember.isEmpty() && !trimmedMember.matches("^[\\u4e00-\\u9fa5]{2,6}$")) {
                                JOptionPane.showMessageDialog(panel,
                                        "组员姓名必须为2-6个汉字，多个组员用逗号分隔",
                                        "输入错误",
                                        JOptionPane.WARNING_MESSAGE);
                                return;
                            }
                        }
                    }

                    // 更新参赛信息
                    Competition updatedCompetition = new Competition();
                    updatedCompetition.setCompetitionId(competitionId);
                    updatedCompetition.setMembers(members);
                    updatedCompetition.setAwardLevel(awardLevel);
                    updatedCompetition.setRemark(remark);

                    boolean success = service.updateCompetition(updatedCompetition);

                    if (success) {
                        JOptionPane.showMessageDialog(panel,
                                "参赛信息修改成功！",
                                "成功",
                                JOptionPane.INFORMATION_MESSAGE);
                        tabbedPane.remove(panel);

                        // 刷新查询列表（如果当前有查询页面）
                        refreshCompetitionQueryTable();
                    } else {
                        JOptionPane.showMessageDialog(panel, "修改失败，请稍后重试！",
                                "错误", JOptionPane.ERROR_MESSAGE);
                    }

                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(panel, ex.getMessage(),
                            "验证错误", JOptionPane.WARNING_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel,
                            "修改失败：" + ex.getMessage(),
                            "系统错误",
                            JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            });

            cancelButton.addActionListener(e -> tabbedPane.remove(panel));

            buttonPanel.add(saveButton);
            buttonPanel.add(deleteButton);
            buttonPanel.add(cancelButton);
            panel.add(buttonPanel, gbc);

            // 设置面板的最小大小以确保显示完整
            panel.setPreferredSize(new Dimension(600, 550));

            tabbedPane.addTab("修改参赛信息", panel);
            tabbedPane.setSelectedComponent(panel);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "打开修改页面失败：" + e.getMessage(),
                    "系统错误",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void openQueryCompetitionPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // 查询条件面板
        JPanel conditionPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // 标题
        JLabel titleLabel = new JLabel("参赛信息查询");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        conditionPanel.add(titleLabel, gbc);

        // 年度查询
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        conditionPanel.add(new JLabel("查询年度:"), gbc);

        gbc.gridx = 1;
        JComboBox<String> yearCombo = new JComboBox<>();
        yearCombo.addItem("全部");  // 添加"全部"选项

        // 从数据库加载年度
        CompetitionService service = new CompetitionService();
        List<Integer> years = service.getAllYears();
        for (Integer year : years) {
            yearCombo.addItem(String.valueOf(year));
        }

        // 如果没有数据，添加最近5年
        if (years.isEmpty()) {
            int currentYear = java.time.Year.now().getValue();
            for (int y = currentYear; y >= currentYear - 5; y--) {
                yearCombo.addItem(String.valueOf(y));
            }
        }

        conditionPanel.add(yearCombo, gbc);

        // 获奖等级查询
        gbc.gridx = 2;
        conditionPanel.add(new JLabel("获奖等级:"), gbc);

        gbc.gridx = 3;
        JComboBox<String> awardCombo = new JComboBox<>(
                new String[]{"全部", "无", "优秀奖", "三等奖", "二等奖", "一等奖", "特等奖"}
        );
        conditionPanel.add(awardCombo, gbc);

        // 查询按钮
        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.gridwidth = 4;
        JPanel buttonPanelTop = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        JButton searchButton = new JButton("查询");
        JButton resetButton = new JButton("重置");
        buttonPanelTop.add(searchButton);
        buttonPanelTop.add(resetButton);
        conditionPanel.add(buttonPanelTop, gbc);

        // 参赛信息列表表格
        String[] columns = {"ID", "竞赛类别", "项目题目", "参赛年度", "组长", "获奖等级", "指导老师", "组员人数", "创建时间"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable competitionTable = new JTable(tableModel);
        competitionTable.setRowHeight(25);
        competitionTable.setAutoCreateRowSorter(true);

        // 设置列宽
        competitionTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        competitionTable.getColumnModel().getColumn(1).setPreferredWidth(120);
        competitionTable.getColumnModel().getColumn(2).setPreferredWidth(180);
        competitionTable.getColumnModel().getColumn(3).setPreferredWidth(80);
        competitionTable.getColumnModel().getColumn(4).setPreferredWidth(80);
        competitionTable.getColumnModel().getColumn(5).setPreferredWidth(80);
        competitionTable.getColumnModel().getColumn(6).setPreferredWidth(80);
        competitionTable.getColumnModel().getColumn(7).setPreferredWidth(80);
        competitionTable.getColumnModel().getColumn(8).setPreferredWidth(120);

        JScrollPane scrollPane = new JScrollPane(competitionTable);

        // 底部功能按钮
        JPanel buttonPanelBottom = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JButton viewButton = new JButton("查看详情");
        JButton editButton = new JButton("修改信息");
        JButton exportButton = new JButton("导出数据");
        JButton addButton = new JButton("新增记录");

        buttonPanelBottom.add(viewButton);
        buttonPanelBottom.add(editButton);
        buttonPanelBottom.add(exportButton);
        buttonPanelBottom.add(addButton);

        // 添加事件监听器
        searchButton.addActionListener(e -> {
            performCompetitionSearch(tableModel, yearCombo, awardCombo);
        });

        resetButton.addActionListener(e -> {
            yearCombo.setSelectedIndex(0);
            awardCombo.setSelectedIndex(0);
            tableModel.setRowCount(0);
        });

        viewButton.addActionListener(e -> {
            int selectedRow = competitionTable.getSelectedRow();
            if (selectedRow >= 0) {
                int competitionId = (int) tableModel.getValueAt(selectedRow, 0);
                viewCompetitionDetail(competitionId);
            } else {
                JOptionPane.showMessageDialog(panel, "请选择要查看的记录", "提示", JOptionPane.WARNING_MESSAGE);
            }
        });

        editButton.addActionListener(e -> {
            int selectedRow = competitionTable.getSelectedRow();
            if (selectedRow >= 0) {
                int competitionId = (int) tableModel.getValueAt(selectedRow, 0);
                openEditCompetitionPanel(competitionId);
            } else {
                JOptionPane.showMessageDialog(panel, "请选择要修改的记录", "提示", JOptionPane.WARNING_MESSAGE);
            }
        });

        addButton.addActionListener(e -> {
            openAddCompetitionPanel();
        });

        exportButton.addActionListener(e -> {
            exportCompetitionData(tableModel);
        });

        panel.add(conditionPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanelBottom, BorderLayout.SOUTH);

        tabbedPane.addTab("参赛信息查询", panel);
        tabbedPane.setSelectedComponent(panel);

        // 页面加载时执行默认查询（显示所有数据）
        SwingUtilities.invokeLater(() -> {
            performCompetitionSearch(tableModel, yearCombo, awardCombo);
        });
    }

    // 执行参赛信息查询
    private void performCompetitionSearch(DefaultTableModel tableModel,
                                          JComboBox<String> yearCombo,
                                          JComboBox<String> awardCombo) {
        try {
            // 获取查询条件
            String yearStr = (String) yearCombo.getSelectedItem();
            String awardLevel = (String) awardCombo.getSelectedItem();

            Integer year = null;
            if (!"全部".equals(yearStr)) {
                try {
                    year = Integer.parseInt(yearStr);
                } catch (NumberFormatException e) {
                    // 忽略转换错误
                }
            }

            // 调用Service查询
            CompetitionService service = new CompetitionService();
            List<Competition> competitions = service.getCompetitionsByYear(year);

            // 如果指定了获奖等级，需要过滤
            List<Competition> filteredCompetitions = new ArrayList<>();
            if ("全部".equals(awardLevel)) {
                filteredCompetitions = competitions;
            } else {
                for (Competition comp : competitions) {
                    if (awardLevel.equals(comp.getAwardLevel())) {
                        filteredCompetitions.add(comp);
                    }
                }
            }

            // 更新表格
            updateCompetitionTable(tableModel, filteredCompetitions);

            if (filteredCompetitions.isEmpty()) {
                JOptionPane.showMessageDialog(tabbedPane,
                        "未找到符合条件的参赛记录",
                        "查询结果",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(tabbedPane,
                        "找到 " + filteredCompetitions.size() + " 条记录",
                        "查询结果",
                        JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(tabbedPane,
                    "查询过程中出现错误：" + ex.getMessage(),
                    "错误", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    // 更新参赛信息表格
    private void updateCompetitionTable(DefaultTableModel tableModel, List<Competition> competitions) {
        tableModel.setRowCount(0);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        for (Competition competition : competitions) {
            // 计算组员人数
            int memberCount = 0;
            if (competition.getMembers() != null && !competition.getMembers().trim().isEmpty()) {
                String[] members = competition.getMembers().split(",");
                memberCount = members.length;
            }

            Object[] rowData = {
                    competition.getCompetitionId(),
                    competition.getCategory(),
                    competition.getTitle(),
                    competition.getYear(),
                    competition.getLeader(),
                    competition.getAwardLevel(),
                    competition.getAdvisor() != null ? competition.getAdvisor() : "",
                    memberCount,
                    competition.getCreateTime() != null ? sdf.format(competition.getCreateTime()) : ""
            };
            tableModel.addRow(rowData);
        }

        tableModel.fireTableDataChanged();
    }

    // 查看参赛信息详情
    private void viewCompetitionDetail(int competitionId) {
        try {
            CompetitionService service = new CompetitionService();
            Competition competition = service.getCompetitionById(competitionId);

            if (competition == null) {
                JOptionPane.showMessageDialog(tabbedPane, "找不到指定的参赛信息", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 格式化详细信息
            StringBuilder detail = new StringBuilder();
            detail.append("参赛信息详情\n");
            detail.append("====================\n");
            detail.append("ID：").append(competition.getCompetitionId()).append("\n");
            detail.append("竞赛类别：").append(competition.getCategory()).append("\n");
            detail.append("项目题目：").append(competition.getTitle()).append("\n");
            detail.append("参赛年度：").append(competition.getYear()).append("年\n");
            detail.append("组长姓名：").append(competition.getLeader()).append("\n");

            if (competition.getMembers() != null && !competition.getMembers().trim().isEmpty()) {
                detail.append("组员名单：").append(competition.getMembers()).append("\n");
            } else {
                detail.append("组员名单：无\n");
            }

            detail.append("获奖等级：").append(competition.getAwardLevel()).append("\n");

            if (competition.getAdvisor() != null && !competition.getAdvisor().trim().isEmpty()) {
                detail.append("指导老师：").append(competition.getAdvisor()).append("\n");
            }

            if (competition.getRemark() != null && !competition.getRemark().trim().isEmpty()) {
                detail.append("备注：").append(competition.getRemark()).append("\n");
            }

            if (competition.getCreateTime() != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                detail.append("创建时间：").append(sdf.format(competition.getCreateTime())).append("\n");
            }

            JTextArea textArea = new JTextArea(detail.toString());
            textArea.setEditable(false);
            textArea.setFont(new Font("微软雅黑", Font.PLAIN, 12));

            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(400, 300));

            JOptionPane.showMessageDialog(tabbedPane, scrollPane,
                    "参赛信息详情 - " + competition.getTitle(),
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(tabbedPane,
                    "查看详情失败：" + e.getMessage(),
                    "错误", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // 刷新参赛信息查询表格
    private void refreshCompetitionQueryTable() {
        try {
            Component selectedTab = tabbedPane.getSelectedComponent();
            if (selectedTab != null) {
                String tabTitle = tabbedPane.getTitleAt(tabbedPane.indexOfComponent(selectedTab));

                if (tabTitle != null && tabTitle.contains("参赛信息查询")) {
                    // 查找查询组件并重新执行查询
                    JPanel panel = (JPanel) selectedTab;

                    // 查找查询条件组件
                    JComboBox<String> yearCombo = null;
                    JComboBox<String> awardCombo = null;
                    JTable competitionTable = null;


                    for (Component comp : panel.getComponents()) {
                        if (comp instanceof JPanel) {
                            findCompetitionSearchComponents((JPanel) comp);
                        }
                    }

                    // 重新执行查询
                    performCompetitionSearch((DefaultTableModel) competitionTable.getModel(), yearCombo, awardCombo);
                }
            }
        } catch (Exception e) {
            System.err.println("刷新参赛信息表格时发生错误：" + e.getMessage());
        }
    }

    // 导出参赛数据
    private void exportCompetitionData(DefaultTableModel tableModel) {
        // 这里实现导出功能（可以导出为Excel或CSV）
        // 示例：导出为CSV
        StringBuilder csv = new StringBuilder();

        // 添加表头
        for (int i = 0; i < tableModel.getColumnCount(); i++) {
            csv.append(tableModel.getColumnName(i));
            if (i < tableModel.getColumnCount() - 1) {
                csv.append(",");
            }
        }
        csv.append("\n");

        // 添加数据
        for (int row = 0; row < tableModel.getRowCount(); row++) {
            for (int col = 0; col < tableModel.getColumnCount(); col++) {
                Object value = tableModel.getValueAt(row, col);
                csv.append(value != null ? value.toString() : "");
                if (col < tableModel.getColumnCount() - 1) {
                    csv.append(",");
                }
            }
            csv.append("\n");
        }

        // 显示导出结果
        JTextArea textArea = new JTextArea(csv.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("宋体", Font.PLAIN, 12));

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(600, 400));

        JOptionPane.showMessageDialog(tabbedPane, scrollPane,
                "导出数据（可复制到Excel）",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void openAddCreditPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 标题
        JLabel titleLabel = new JLabel("创新学分录入");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        panel.add(titleLabel, gbc);

        // --- 选择学生部分 ---
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        panel.add(new JLabel("选择学生*:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        JPanel studentPanel = new JPanel(new BorderLayout());

        // 学号输入框
        JTextField stuIdField = new JTextField(12);
        studentPanel.add(stuIdField, BorderLayout.CENTER);

        // 选择按钮
        JButton selectButton = new JButton("选择");
        studentPanel.add(selectButton, BorderLayout.EAST);

        panel.add(studentPanel, gbc);

        // --- 学生信息显示区域 ---
        gbc.gridy = 2;
        gbc.gridx = 0;
        panel.add(new JLabel("学生信息:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        JPanel infoPanel = new JPanel(new GridLayout(2, 2, 5, 5));

        JLabel nameLabel = new JLabel("姓名：");
        JTextField nameField = new JTextField(10);
        nameField.setEditable(false);
        nameField.setBackground(Color.LIGHT_GRAY);

        JLabel gradeLabel = new JLabel("年级：");
        JTextField gradeField = new JTextField(8);
        gradeField.setEditable(false);
        gradeField.setBackground(Color.LIGHT_GRAY);

        JLabel collegeLabel = new JLabel("院系：");
        JTextField collegeField = new JTextField(15);
        collegeField.setEditable(false);
        collegeField.setBackground(Color.LIGHT_GRAY);

        JLabel majorLabel = new JLabel("专业：");
        JTextField majorField = new JTextField(15);
        majorField.setEditable(false);
        majorField.setBackground(Color.LIGHT_GRAY);

        infoPanel.add(nameLabel);
        infoPanel.add(nameField);
        infoPanel.add(gradeLabel);
        infoPanel.add(gradeField);
        infoPanel.add(collegeLabel);
        infoPanel.add(collegeField);
        infoPanel.add(majorLabel);
        infoPanel.add(majorField);

        panel.add(infoPanel, gbc);

        // 选择按钮事件
        selectButton.addActionListener(e -> {
            String stuId = stuIdField.getText().trim();
            if (stuId.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "请输入学号！");
                return;
            }

            // TODO: 从数据库查询学生信息
            // 这里用示例数据
            if (stuId.equals("202101001")) {
                nameField.setText("张三");
                gradeField.setText("2021级");
                collegeField.setText("软件学院");
                majorField.setText("软件工程");
            } else if (stuId.equals("202101002")) {
                nameField.setText("李四");
                gradeField.setText("2021级");
                collegeField.setText("计算机学院");
                majorField.setText("计算机科学");
            } else {
                JOptionPane.showMessageDialog(panel, "未找到该学号的学生信息！");
                nameField.setText("");
                gradeField.setText("");
                collegeField.setText("");
                majorField.setText("");
            }
        });

        // --- 学分类别（选择公开课） ---
        gbc.gridy = 3;
        gbc.gridx = 0;
        panel.add(new JLabel("学分类别*:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        InnovationCreditService creditService = new InnovationCreditService();
        List<String> creditTypes = creditService.getAllCreditTypes();
        JComboBox<String> typeCombo = new JComboBox<>();
        for (String type : creditTypes) {
            typeCombo.addItem(type);
        }
        panel.add(typeCombo, gbc);

        // --- 学分分值 ---
        gbc.gridy = 4;
        gbc.gridx = 0;
        panel.add(new JLabel("学分分值*:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        JPanel creditValuePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JSpinner creditSpinner = new JSpinner(new SpinnerNumberModel(0.5, 0.0, 10.0, 0.5));
        creditSpinner.setPreferredSize(new Dimension(80, 25));
        creditValuePanel.add(creditSpinner);
        creditValuePanel.add(new JLabel("分"));
        panel.add(creditValuePanel, gbc);

        // --- 事由（选择公开课） ---
        gbc.gridy = 5;
        gbc.gridx = 0;
        panel.add(new JLabel("事由说明*:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        // 获取公开课列表供选择
        CourseService courseService = new CourseService();
        List<Course> courses = courseService.findCourses(null, null, null, null, null);

        JComboBox<String> reasonCombo = new JComboBox<>();
        reasonCombo.addItem("请选择公开课");
        for (Course course : courses) {
            String reason = course.getTopic() + " (" + course.getSpeaker() + ", " +
                    new SimpleDateFormat("yyyy-MM-dd").format(course.getCourseDate()) + ")";
            reasonCombo.addItem(reason);
        }

        // 添加自定义事由输入框
        JTextField customReasonField = new JTextField(25);
        customReasonField.setVisible(false);

        JPanel reasonPanel = new JPanel(new BorderLayout());
        reasonPanel.add(reasonCombo, BorderLayout.NORTH);
        reasonPanel.add(customReasonField, BorderLayout.SOUTH);

        // 监听选择变化
        reasonCombo.addActionListener(e -> {
            if ("请选择公开课".equals(reasonCombo.getSelectedItem())) {
                customReasonField.setVisible(false);
            } else if ("其他".equals(reasonCombo.getSelectedItem())) {
                customReasonField.setVisible(true);
                customReasonField.requestFocus();
            } else {
                customReasonField.setVisible(false);
            }
            reasonPanel.revalidate();
        });

        panel.add(reasonPanel, gbc);

        // --- 取得时间（年月） ---
        gbc.gridy = 6;
        gbc.gridx = 0;
        panel.add(new JLabel("取得时间*:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        JPanel acquireDatePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        // 年份选择
        int currentYear = java.time.Year.now().getValue();
        JComboBox<Integer> yearCombo = new JComboBox<>();
        for (int y = currentYear - 5; y <= currentYear; y++) {
            yearCombo.addItem(y);
        }
        yearCombo.setSelectedItem(currentYear);

        // 月份选择
        JComboBox<Integer> monthCombo = new JComboBox<>();
        for (int m = 1; m <= 12; m++) {
            monthCombo.addItem(m);
        }
        monthCombo.setSelectedItem(java.time.LocalDate.now().getMonthValue());

        acquireDatePanel.add(yearCombo);
        acquireDatePanel.add(new JLabel("年"));
        acquireDatePanel.add(monthCombo);
        acquireDatePanel.add(new JLabel("月"));

        panel.add(acquireDatePanel, gbc);

        // --- 录入时间（自动生成） ---
        gbc.gridy = 7;
        gbc.gridx = 0;
        panel.add(new JLabel("录入时间:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        JLabel recordTimeLabel = new JLabel(
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        recordTimeLabel.setForeground(Color.GRAY);
        panel.add(recordTimeLabel, gbc);

        // --- 录入人（自动生成） ---
        gbc.gridy = 8;
        gbc.gridx = 0;
        panel.add(new JLabel("录入人:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        JLabel recorderLabel = new JLabel(currentUser.getRealName());
        recorderLabel.setForeground(Color.GRAY);
        panel.add(recorderLabel, gbc);

        // --- 备注 ---
        gbc.gridy = 9;
        gbc.gridx = 0;
        panel.add(new JLabel("备注:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        JTextArea remarkArea = new JTextArea(3, 30);
        remarkArea.setLineWrap(true);
        JScrollPane remarkScroll = new JScrollPane(remarkArea);
        panel.add(remarkScroll, gbc);

        // --- 保存和取消按钮 ---
        gbc.gridy = 10;
        gbc.gridx = 0;
        gbc.gridwidth = 3;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton saveButton = new JButton("保存");
        JButton cancelButton = new JButton("取消");
        JButton resetButton = new JButton("重置");

        // 保存按钮事件
        saveButton.addActionListener(e -> {
            try {
                // 获取表单数据
                String stuId = stuIdField.getText().trim();
                String stuName = nameField.getText().trim();
                String grade = gradeField.getText().trim();
                String college = collegeField.getText().trim();
                String major = majorField.getText().trim();
                double creditValue = (Double) creditSpinner.getValue();
                String creditType = (String) typeCombo.getSelectedItem();

                // 获取事由
                String reason;
                if ("请选择公开课".equals(reasonCombo.getSelectedItem())) {
                    JOptionPane.showMessageDialog(panel, "请选择事由！");
                    return;
                } else if ("其他".equals(reasonCombo.getSelectedItem())) {
                    reason = customReasonField.getText().trim();
                    if (reason.isEmpty()) {
                        JOptionPane.showMessageDialog(panel, "请输入事由说明！");
                        return;
                    }
                } else {
                    reason = (String) reasonCombo.getSelectedItem();
                }

                // 获取取得时间
                int year = (Integer) yearCombo.getSelectedItem();
                int month = (Integer) monthCombo.getSelectedItem();
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month - 1, 1); // 设置为该月的第一天
                Date acquireDate = calendar.getTime();

                String recorder = currentUser.getRealName();
                String remark = remarkArea.getText().trim();

                // 验证必填字段
                if (stuId.isEmpty() || stuName.isEmpty() || grade.isEmpty() || college.isEmpty()) {
                    JOptionPane.showMessageDialog(panel, "请先选择学生！");
                    return;
                }

                if (creditValue <= 0) {
                    JOptionPane.showMessageDialog(panel, "学分分值必须大于0！");
                    return;
                }

                if (reason.isEmpty()) {
                    JOptionPane.showMessageDialog(panel, "事由不能为空！");
                    return;
                }

                // 创建创新学分对象
                InnovationCredit credit = new InnovationCredit();
                credit.setStuId(stuId);
                credit.setStuName(stuName);
                credit.setGrade(grade);
                credit.setCollege(college);
                credit.setMajor(major);
                credit.setCreditValue(creditValue);
                credit.setCreditType(creditType);
                credit.setReason(reason);
                credit.setAcquireDate(acquireDate);
                credit.setRecorder(recorder);
                credit.setRemark(remark);

                // 调用Service保存
                InnovationCreditService service = new InnovationCreditService();
                int creditId = service.addCredit(credit);

                if (creditId > 0) {
                    JOptionPane.showMessageDialog(panel,
                            "创新学分录入成功！\n" +
                                    "学号：" + stuId + "\n" +
                                    "姓名：" + stuName + "\n" +
                                    "学分：" + creditValue + "分\n" +
                                    "事由：" + reason,
                            "录入成功",
                            JOptionPane.INFORMATION_MESSAGE);

                    // 清空表单（保留学生信息）
                    creditSpinner.setValue(0.5);
                    typeCombo.setSelectedIndex(0);
                    reasonCombo.setSelectedIndex(0);
                    customReasonField.setText("");
                    customReasonField.setVisible(false);
                    yearCombo.setSelectedItem(currentYear);
                    monthCombo.setSelectedItem(java.time.LocalDate.now().getMonthValue());
                    remarkArea.setText("");
                } else {
                    JOptionPane.showMessageDialog(panel,
                            "录入失败，请稍后重试！",
                            "录入失败",
                            JOptionPane.ERROR_MESSAGE);
                }

            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(panel,
                        ex.getMessage(),
                        "输入错误",
                        JOptionPane.WARNING_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel,
                        "录入失败：" + ex.getMessage(),
                        "系统错误",
                        JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        // 重置按钮事件
        resetButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(panel,
                    "确定要重置表单吗？",
                    "确认重置",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                stuIdField.setText("");
                nameField.setText("");
                gradeField.setText("");
                collegeField.setText("");
                majorField.setText("");
                creditSpinner.setValue(0.5);
                typeCombo.setSelectedIndex(0);
                reasonCombo.setSelectedIndex(0);
                customReasonField.setText("");
                customReasonField.setVisible(false);
                yearCombo.setSelectedItem(currentYear);
                monthCombo.setSelectedItem(java.time.LocalDate.now().getMonthValue());
                remarkArea.setText("");
                stuIdField.requestFocus();
            }
        });

        // 取消按钮事件
        cancelButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(panel,
                    "确定要取消录入吗？未保存的数据将丢失。",
                    "确认取消",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                tabbedPane.remove(panel);
            }
        });

        buttonPanel.add(saveButton);
        buttonPanel.add(resetButton);
        buttonPanel.add(cancelButton);
        panel.add(buttonPanel, gbc);

        // 设置面板大小
        panel.setPreferredSize(new Dimension(600, 650));

        // 添加到标签页
        tabbedPane.addTab("创新学分录入", panel);
        tabbedPane.setSelectedComponent(panel);
    }

    private void openQueryCreditPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // 查询条件面板
        JPanel conditionPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // 标题
        JLabel titleLabel = new JLabel("创新学分查询");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        conditionPanel.add(titleLabel, gbc);

        // 学号查询
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        conditionPanel.add(new JLabel("学号:"), gbc);

        gbc.gridx = 1;
        JTextField stuIdField = new JTextField(12);
        conditionPanel.add(stuIdField, gbc);

        // 姓名查询
        gbc.gridx = 2;
        conditionPanel.add(new JLabel("姓名:"), gbc);

        gbc.gridx = 3;
        JTextField stuNameField = new JTextField(10);
        conditionPanel.add(stuNameField, gbc);

        // 时间段查询
        gbc.gridy = 2;
        gbc.gridx = 0;
        conditionPanel.add(new JLabel("开始时间:"), gbc);

        gbc.gridx = 1;
        JPanel startDatePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JComboBox<Integer> startYearCombo = new JComboBox<>();
        JComboBox<Integer> startMonthCombo = new JComboBox<>();
        setupYearMonthComboBox(startYearCombo, startMonthCombo, true);
        startDatePanel.add(startYearCombo);
        startDatePanel.add(new JLabel("年"));
        startDatePanel.add(startMonthCombo);
        startDatePanel.add(new JLabel("月"));
        conditionPanel.add(startDatePanel, gbc);

        gbc.gridx = 2;
        conditionPanel.add(new JLabel("结束时间:"), gbc);

        gbc.gridx = 3;
        JPanel endDatePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JComboBox<Integer> endYearCombo = new JComboBox<>();
        JComboBox<Integer> endMonthCombo = new JComboBox<>();
        setupYearMonthComboBox(endYearCombo, endMonthCombo, false);
        endDatePanel.add(endYearCombo);
        endDatePanel.add(new JLabel("年"));
        endDatePanel.add(endMonthCombo);
        endDatePanel.add(new JLabel("月"));
        conditionPanel.add(endDatePanel, gbc);

        // 查询按钮
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 4;
        JPanel buttonPanelTop = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        JButton searchButton = new JButton("查询");
        JButton resetButton = new JButton("重置");
        buttonPanelTop.add(searchButton);
        buttonPanelTop.add(resetButton);
        conditionPanel.add(buttonPanelTop, gbc);

        // 学分记录表格
        String[] columns = {"ID", "学号", "姓名", "年级", "院系", "学分类别", "学分值", "事由", "取得时间", "录入时间", "录入人"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable creditTable = new JTable(tableModel);
        creditTable.setRowHeight(25);
        creditTable.setAutoCreateRowSorter(true);

        // 设置列宽
        creditTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        creditTable.getColumnModel().getColumn(1).setPreferredWidth(100);
        creditTable.getColumnModel().getColumn(2).setPreferredWidth(80);
        creditTable.getColumnModel().getColumn(3).setPreferredWidth(80);
        creditTable.getColumnModel().getColumn(4).setPreferredWidth(100);
        creditTable.getColumnModel().getColumn(5).setPreferredWidth(100);
        creditTable.getColumnModel().getColumn(6).setPreferredWidth(60);
        creditTable.getColumnModel().getColumn(7).setPreferredWidth(150);
        creditTable.getColumnModel().getColumn(8).setPreferredWidth(100);
        creditTable.getColumnModel().getColumn(9).setPreferredWidth(120);
        creditTable.getColumnModel().getColumn(10).setPreferredWidth(80);

        JScrollPane scrollPane = new JScrollPane(creditTable);

        // 统计信息面板
        JPanel statPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
        statPanel.setBorder(BorderFactory.createTitledBorder("统计信息"));

        JLabel totalRecordsLabel = new JLabel("总记录数: 0");
        JLabel totalCreditLabel = new JLabel("总学分数: 0.0");
        JLabel avgCreditLabel = new JLabel("平均学分: 0.0");

        statPanel.add(totalRecordsLabel);
        statPanel.add(totalCreditLabel);
        statPanel.add(avgCreditLabel);

        // 底部功能按钮
        JPanel buttonPanelBottom = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JButton exportButton = new JButton("导出数据");
        JButton printButton = new JButton("打印报表");
        JButton detailButton = new JButton("查看详情");

        buttonPanelBottom.add(exportButton);
        buttonPanelBottom.add(printButton);
        buttonPanelBottom.add(detailButton);

        // 添加事件监听器
        searchButton.addActionListener(e -> {
            performCreditSearch(tableModel, statPanel, stuIdField, stuNameField,
                    startYearCombo, startMonthCombo, endYearCombo, endMonthCombo);
        });

        resetButton.addActionListener(e -> {
            stuIdField.setText("");
            stuNameField.setText("");
            setupYearMonthComboBox(startYearCombo, startMonthCombo, true);
            setupYearMonthComboBox(endYearCombo, endMonthCombo, false);
            tableModel.setRowCount(0);
            totalRecordsLabel.setText("总记录数: 0");
            totalCreditLabel.setText("总学分数: 0.0");
            avgCreditLabel.setText("平均学分: 0.0");
        });

        panel.add(conditionPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        // 创建包含统计信息和按钮的底部面板
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(statPanel, BorderLayout.NORTH);
        bottomPanel.add(buttonPanelBottom, BorderLayout.SOUTH);

        panel.add(bottomPanel, BorderLayout.SOUTH);

        tabbedPane.addTab("创新学分查询", panel);
        tabbedPane.setSelectedComponent(panel);
    }

    // 设置年月下拉框的辅助方法
    private void setupYearMonthComboBox(JComboBox<Integer> yearCombo,
                                        JComboBox<Integer> monthCombo, boolean isStart) {
        yearCombo.removeAllItems();
        monthCombo.removeAllItems();

        int currentYear = java.time.Year.now().getValue();
        for (int y = currentYear - 5; y <= currentYear; y++) {
            yearCombo.addItem(y);
        }

        for (int m = 1; m <= 12; m++) {
            monthCombo.addItem(m);
        }

        if (isStart) {
            yearCombo.setSelectedItem(currentYear - 1);
            monthCombo.setSelectedItem(1);
        } else {
            yearCombo.setSelectedItem(currentYear);
            monthCombo.setSelectedItem(java.time.LocalDate.now().getMonthValue());
        }
    }

    // 执行学分查询
    private void performCreditSearch(DefaultTableModel tableModel, JPanel statPanel,
                                     JTextField stuIdField, JTextField stuNameField,
                                     JComboBox<Integer> startYearCombo, JComboBox<Integer> startMonthCombo,
                                     JComboBox<Integer> endYearCombo, JComboBox<Integer> endMonthCombo) {
        try {
            // 获取查询条件
            String stuId = stuIdField.getText().trim();
            String stuName = stuNameField.getText().trim();

            // 获取开始时间
            Integer startYear = (Integer) startYearCombo.getSelectedItem();
            Integer startMonth = (Integer) startMonthCombo.getSelectedItem();
            Date startDate = null;
            if (startYear != null && startMonth != null) {
                Calendar cal = Calendar.getInstance();
                cal.set(startYear, startMonth - 1, 1);
                startDate = cal.getTime();
            }

            // 获取结束时间
            Integer endYear = (Integer) endYearCombo.getSelectedItem();
            Integer endMonth = (Integer) endMonthCombo.getSelectedItem();
            Date endDate = null;
            if (endYear != null && endMonth != null) {
                Calendar cal = Calendar.getInstance();
                cal.set(endYear, endMonth - 1, 1);
                cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
                endDate = cal.getTime();
            }

            // 调用Service查询
            InnovationCreditService service = new InnovationCreditService();
            List<InnovationCredit> credits = service.findCredits(
                    stuId.isEmpty() ? null : stuId,
                    stuName.isEmpty() ? null : stuName,
                    startDate, endDate
            );

            // 更新表格
            updateCreditTable(tableModel, credits);

            // 更新统计信息
            updateCreditStatistics(statPanel, credits);

            if (credits.isEmpty()) {
                JOptionPane.showMessageDialog(tabbedPane,
                        "未找到符合条件的学分记录",
                        "查询结果",
                        JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(tabbedPane,
                    "查询过程中出现错误：" + ex.getMessage(),
                    "错误", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    // 更新学分表格
    private void updateCreditTable(DefaultTableModel tableModel, List<InnovationCredit> credits) {
        tableModel.setRowCount(0);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        for (InnovationCredit credit : credits) {
            Object[] rowData = {
                    credit.getCreditId(),
                    credit.getStuId(),
                    credit.getStuName(),
                    credit.getGrade(),
                    credit.getCollege(),
                    credit.getCreditType(),
                    credit.getCreditValue(),
                    credit.getReason(),
                    credit.getAcquireDate() != null ? dateFormat.format(credit.getAcquireDate()) : "",
                    credit.getRecordTime() != null ? timeFormat.format(credit.getRecordTime()) : "",
                    credit.getRecorder()
            };
            tableModel.addRow(rowData);
        }

        tableModel.fireTableDataChanged();
    }

    // 更新学分统计信息
    private void updateCreditStatistics(JPanel statPanel, List<InnovationCredit> credits) {
        int totalRecords = credits.size();
        double totalCredit = 0.0;
        double avgCredit = 0.0;

        for (InnovationCredit credit : credits) {
            totalCredit += credit.getCreditValue();
        }

        if (totalRecords > 0) {
            avgCredit = totalCredit / totalRecords;
        }

        // 更新统计标签
        for (Component comp : statPanel.getComponents()) {
            if (comp instanceof JLabel) {
                JLabel label = (JLabel) comp;
                String text = label.getText();
                if (text.startsWith("总记录数:")) {
                    label.setText("总记录数: " + totalRecords);
                } else if (text.startsWith("总学分数:")) {
                    label.setText("总学分数: " + String.format("%.2f", totalCredit));
                } else if (text.startsWith("平均学分:")) {
                    label.setText("平均学分: " + String.format("%.2f", avgCredit));
                }
            }
        }
    }

    private void openCreditStatPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // 查询条件面板
        JPanel conditionPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // 标题
        JLabel titleLabel = new JLabel("创新学分统计");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        conditionPanel.add(titleLabel, gbc);

        // 统计年度选择
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        conditionPanel.add(new JLabel("统计年度:"), gbc);

        gbc.gridx = 1;
        JComboBox<Integer> statYearCombo = new JComboBox<>();
        int currentYear = java.time.Year.now().getValue();
        for (int y = currentYear; y >= currentYear - 5; y--) {
            statYearCombo.addItem(y);
        }
        statYearCombo.setSelectedItem(currentYear);
        conditionPanel.add(statYearCombo, gbc);

        // 统计按钮
        gbc.gridx = 2;
        JButton statButton = new JButton("开始统计");
        conditionPanel.add(statButton, gbc);

        // 汇总统计面板
        JPanel summaryPanel = new JPanel(new GridLayout(2, 4, 10, 10));
        summaryPanel.setBorder(BorderFactory.createTitledBorder("年度汇总统计"));

        JLabel totalStudentsLabel = new JLabel("获得学分总人数: 0", SwingConstants.CENTER);
        JLabel totalCreditsLabel = new JLabel("总学分数: 0.0", SwingConstants.CENTER);
        JLabel avgCreditsLabel = new JLabel("人均学分: 0.0", SwingConstants.CENTER);
        JLabel maxCreditsLabel = new JLabel("最高学分: 0.0", SwingConstants.CENTER);

        JLabel topCollegeLabel = new JLabel("学分最高院系: 无", SwingConstants.CENTER);
        JLabel topStudentLabel = new JLabel("学分最高学生: 无", SwingConstants.CENTER);
        JLabel creditTypesLabel = new JLabel("学分类别数: 0", SwingConstants.CENTER);
        JLabel completionRateLabel = new JLabel("完成率: 0%", SwingConstants.CENTER);

        // 设置字体和颜色
        Font statFont = new Font("微软雅黑", Font.BOLD, 12);
        totalStudentsLabel.setFont(statFont);
        totalCreditsLabel.setFont(statFont);
        avgCreditsLabel.setFont(statFont);
        maxCreditsLabel.setFont(statFont);
        topCollegeLabel.setFont(statFont);
        topStudentLabel.setFont(statFont);
        creditTypesLabel.setFont(statFont);
        completionRateLabel.setFont(statFont);

        totalCreditsLabel.setForeground(Color.BLUE);
        maxCreditsLabel.setForeground(Color.RED);
        topStudentLabel.setForeground(new Color(0, 100, 0));

        summaryPanel.add(totalStudentsLabel);
        summaryPanel.add(totalCreditsLabel);
        summaryPanel.add(avgCreditsLabel);
        summaryPanel.add(maxCreditsLabel);
        summaryPanel.add(topCollegeLabel);
        summaryPanel.add(topStudentLabel);
        summaryPanel.add(creditTypesLabel);
        summaryPanel.add(completionRateLabel);

        // 学分明细表格
        String[] columns = {"学号", "姓名", "年级", "院系", "专业", "总学分", "公开课", "竞赛", "科研", "论文", "专利", "其他"};
        DefaultTableModel statTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable statTable = new JTable(statTableModel);
        statTable.setRowHeight(25);
        statTable.setAutoCreateRowSorter(true);

        // 设置列宽
        for (int i = 0; i < columns.length; i++) {
            statTable.getColumnModel().getColumn(i).setPreferredWidth(80);
        }

        JScrollPane scrollPane = new JScrollPane(statTable);

        // 底部按钮
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JButton exportButton = new JButton("导出统计");
        JButton printButton = new JButton("打印报表");
        JButton chartButton = new JButton("查看图表");

        buttonPanel.add(exportButton);
        buttonPanel.add(printButton);
        buttonPanel.add(chartButton);

        // 添加事件监听器
        statButton.addActionListener(e -> {
            performCreditStatistics(statTableModel, summaryPanel, statYearCombo);
        });

        exportButton.addActionListener(e -> {
            exportCreditStatistics(statTableModel);
        });

        chartButton.addActionListener(e -> {
            showCreditStatisticsChart(statYearCombo);
        });

        panel.add(conditionPanel, BorderLayout.NORTH);
        panel.add(summaryPanel, BorderLayout.CENTER);
        panel.add(scrollPane, BorderLayout.SOUTH);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        tabbedPane.addTab("创新学分统计", panel);
        tabbedPane.setSelectedComponent(panel);

        // 页面加载时自动统计当前年度
        SwingUtilities.invokeLater(() -> {
            performCreditStatistics(statTableModel, summaryPanel, statYearCombo);
        });
    }

    // 执行学分统计
    private void performCreditStatistics(DefaultTableModel tableModel,
                                         JPanel summaryPanel,
                                         JComboBox<Integer> yearCombo) {
        try {
            Integer selectedYear = (Integer) yearCombo.getSelectedItem();
            if (selectedYear == null) {
                JOptionPane.showMessageDialog(tabbedPane, "请选择统计年度！");
                return;
            }

            // 设置时间范围（整个年度）
            Calendar startCal = Calendar.getInstance();
            startCal.set(selectedYear, 0, 1); // 1月1日

            Calendar endCal = Calendar.getInstance();
            endCal.set(selectedYear, 11, 31); // 12月31日

            // 调用Service获取该年度的学分记录
            InnovationCreditService service = new InnovationCreditService();
            List<InnovationCredit> credits = service.getCreditsByDateRange(
                    startCal.getTime(), endCal.getTime()
            );

            if (credits.isEmpty()) {
                JOptionPane.showMessageDialog(tabbedPane,
                        selectedYear + "年度没有学分记录",
                        "统计结果",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // 统计学生学分信息
            Map<String, StudentCreditInfo> studentCredits = new HashMap<>();
            Map<String, Double> collegeCredits = new HashMap<>();
            Set<String> creditTypes = new HashSet<>();
            double maxCredit = 0.0;
            String topStudent = "";
            String topCollege = "";

            for (InnovationCredit credit : credits) {
                String studentKey = credit.getStuId() + "|" + credit.getStuName();

                // 更新学生学分信息
                StudentCreditInfo studentInfo = studentCredits.getOrDefault(studentKey,
                        new StudentCreditInfo(credit.getStuId(), credit.getStuName(),
                                credit.getGrade(), credit.getCollege(),
                                credit.getMajor()));

                studentInfo.addCredit(credit.getCreditValue(), credit.getCreditType());
                studentCredits.put(studentKey, studentInfo);

                // 更新院系学分
                collegeCredits.put(credit.getCollege(),
                        collegeCredits.getOrDefault(credit.getCollege(), 0.0) + credit.getCreditValue());

                // 更新最高学分学生
                if (studentInfo.getTotalCredit() > maxCredit) {
                    maxCredit = studentInfo.getTotalCredit();
                    topStudent = credit.getStuName() + " (" + credit.getStuId() + ")";
                }

                // 记录学分类别
                creditTypes.add(credit.getCreditType());
            }

            // 找出学分最高的院系
            double maxCollegeCredit = 0.0;
            for (Map.Entry<String, Double> entry : collegeCredits.entrySet()) {
                if (entry.getValue() > maxCollegeCredit) {
                    maxCollegeCredit = entry.getValue();
                    topCollege = entry.getKey();
                }
            }

            // 更新统计表格
            updateCreditStatTable(tableModel, studentCredits.values());

            // 更新汇总统计信息
            updateSummaryStatistics(summaryPanel, studentCredits.size(),
                    credits.size(), maxCredit, topStudent,
                    topCollege, creditTypes.size());

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(tabbedPane,
                    "统计过程中出现错误：" + ex.getMessage(),
                    "错误", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    // 学生学分信息类（内部类）
    class StudentCreditInfo {
        private String stuId;
        private String stuName;
        private String grade;
        private String college;
        private String major;
        private double totalCredit;
        private Map<String, Double> creditByType;

        public StudentCreditInfo(String stuId, String stuName, String grade,
                                 String college, String major) {
            this.stuId = stuId;
            this.stuName = stuName;
            this.grade = grade;
            this.college = college;
            this.major = major;
            this.totalCredit = 0.0;
            this.creditByType = new HashMap<>();
        }

        public void addCredit(double credit, String type) {
            this.totalCredit += credit;
            this.creditByType.put(type,
                    this.creditByType.getOrDefault(type, 0.0) + credit);
        }

        public double getTotalCredit() { return totalCredit; }
        public String getStuId() { return stuId; }
        public String getStuName() { return stuName; }
        public String getGrade() { return grade; }
        public String getCollege() { return college; }
        public String getMajor() { return major; }
        public double getCreditByType(String type) {
            return creditByType.getOrDefault(type, 0.0);
        }
    }

    // 更新学分统计表格
    private void updateCreditStatTable(DefaultTableModel tableModel,
                                       Collection<StudentCreditInfo> studentInfos) {
        tableModel.setRowCount(0);

        // 定义学分类型
        String[] creditTypes = {"公开课参与", "竞赛获奖", "科研项目", "论文发表", "专利成果", "其他"};

        for (StudentCreditInfo info : studentInfos) {
            Object[] rowData = new Object[12];
            rowData[0] = info.getStuId();
            rowData[1] = info.getStuName();
            rowData[2] = info.getGrade();
            rowData[3] = info.getCollege();
            rowData[4] = info.getMajor();
            rowData[5] = String.format("%.2f", info.getTotalCredit());

            // 各类别学分
            for (int i = 0; i < creditTypes.length; i++) {
                rowData[6 + i] = String.format("%.2f", info.getCreditByType(creditTypes[i]));
            }

            tableModel.addRow(rowData);
        }

        tableModel.fireTableDataChanged();
    }

    // 更新汇总统计信息
    private void updateSummaryStatistics(JPanel summaryPanel, int studentCount,
                                         int recordCount, double maxCredit,
                                         String topStudent, String topCollege,
                                         int typeCount) {
        // 计算其他统计信息
        double totalCredit = 0.0;
        DefaultTableModel model = (DefaultTableModel) ((JTable)((JScrollPane)summaryPanel.getParent()
                .getComponent(2)).getViewport().getView()).getModel();

        for (int i = 0; i < model.getRowCount(); i++) {
            totalCredit += Double.parseDouble(model.getValueAt(i, 5).toString());
        }

        double avgCredit = studentCount > 0 ? totalCredit / studentCount : 0.0;

        // 更新标签
        for (Component comp : summaryPanel.getComponents()) {
            if (comp instanceof JLabel) {
                JLabel label = (JLabel) comp;
                String text = label.getText();

                if (text.startsWith("获得学分总人数:")) {
                    label.setText("获得学分总人数: " + studentCount);
                } else if (text.startsWith("总学分数:")) {
                    label.setText("总学分数: " + String.format("%.2f", totalCredit));
                } else if (text.startsWith("人均学分:")) {
                    label.setText("人均学分: " + String.format("%.2f", avgCredit));
                } else if (text.startsWith("最高学分:")) {
                    label.setText("最高学分: " + String.format("%.2f", maxCredit));
                } else if (text.startsWith("学分最高院系:")) {
                    label.setText("学分最高院系: " + (topCollege.isEmpty() ? "无" : topCollege));
                } else if (text.startsWith("学分最高学生:")) {
                    label.setText("学分最高学生: " + (topStudent.isEmpty() ? "无" : topStudent));
                } else if (text.startsWith("学分类别数:")) {
                    label.setText("学分类别数: " + typeCount);
                } else if (text.startsWith("完成率:")) {
                    // TODO: 计算完成率（需要总学生数）
                    label.setText("完成率: " + "N/A");
                }
            }
        }
    }

    // 导出学分统计
    private void exportCreditStatistics(DefaultTableModel tableModel) {
        StringBuilder csv = new StringBuilder();

        // 添加表头
        for (int i = 0; i < tableModel.getColumnCount(); i++) {
            csv.append(tableModel.getColumnName(i));
            if (i < tableModel.getColumnCount() - 1) {
                csv.append(",");
            }
        }
        csv.append("\n");

        // 添加数据
        for (int row = 0; row < tableModel.getRowCount(); row++) {
            for (int col = 0; col < tableModel.getColumnCount(); col++) {
                Object value = tableModel.getValueAt(row, col);
                csv.append(value != null ? value.toString() : "");
                if (col < tableModel.getColumnCount() - 1) {
                    csv.append(",");
                }
            }
            csv.append("\n");
        }

        // 显示导出结果
        JTextArea textArea = new JTextArea(csv.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("宋体", Font.PLAIN, 12));

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(600, 400));

        JOptionPane.showMessageDialog(tabbedPane, scrollPane,
                "学分统计数据（可复制到Excel）",
                JOptionPane.INFORMATION_MESSAGE);
    }

    // 显示学分统计图表（简化版）
    private void showCreditStatisticsChart(JComboBox<Integer> yearCombo) {
        Integer selectedYear = (Integer) yearCombo.getSelectedItem();
        if (selectedYear == null) {
            JOptionPane.showMessageDialog(tabbedPane, "请先选择统计年度！");
            return;
        }

        // 这里可以实现图表显示功能
        // 可以使用JFreeChart等图表库
        // 简化版：显示提示信息
        JOptionPane.showMessageDialog(tabbedPane,
                "图表功能开发中...\n" +
                        "统计年度: " + selectedYear + "\n" +
                        "功能包括：\n" +
                        "1. 学分分布饼图\n" +
                        "2. 院系学分对比柱状图\n" +
                        "3. 月度学分趋势图",
                "图表功能",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * 根据院系ID获取院系名称
     * @param collegeId 院系ID
     * @return 院系名称
     */
    private String getCollegeNameById(int collegeId) {
        // 实际项目中应该从数据库或缓存获取院系名称
        java.util.Map<Integer, String> collegeMap = new java.util.HashMap<>();
        collegeMap.put(1, "软件学院");
        collegeMap.put(2, "计算机学院");
        collegeMap.put(3, "信息与通信工程学院");
        return collegeMap.getOrDefault(collegeId, "未知院系");
    }

    /**
     * 根据专业ID获取专业名称
     * @param majorId 专业ID
     * @return 专业名称
     */
    private String getMajorNameById(int majorId) {
        // 实际项目中应该从数据库或缓存获取专业名称
        java.util.Map<Integer, String> majorMap = new java.util.HashMap<>();
        majorMap.put(1, "软件工程");
        majorMap.put(2, "网络工程");
        majorMap.put(3, "信息安全");
        majorMap.put(4, "计算机科学");
        majorMap.put(5, "人工智能");
        majorMap.put(6, "数据科学");
        majorMap.put(7, "通信工程");
        majorMap.put(8, "电子信息工程");
        majorMap.put(9, "物联网工程");
        return majorMap.getOrDefault(majorId, "未知专业");
    }

    /**
     * 刷新成员查询表格
     */
    private void refreshMemberQueryTable() {
        // 查找当前打开的查询面板并刷新数据
        try {
            Component selectedTab = tabbedPane.getSelectedComponent();
            if (selectedTab != null) {
                String tabTitle = tabbedPane.getTitleAt(tabbedPane.indexOfComponent(selectedTab));

                // 如果是成员查询页面，重新执行查询
                if (tabTitle != null && tabTitle.contains("成员查询")) {
                    // 查找并刷新表格
                    JTable memberTable = findMemberTableInPanel((JPanel) selectedTab);
                    if (memberTable != null) {
                        DefaultTableModel model = (DefaultTableModel) memberTable.getModel();
                        model.setRowCount(0); // 清空现有数据

                        // 重新执行查询逻辑
                        performMemberSearch();

                        System.out.println("已刷新成员查询表格");
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("刷新表格时发生错误: " + e.getMessage());
        }
    }

    /**
     * 在面板中查找成员表格
     * @param panel 面板
     * @return 找到的表格，如果没有则返回null
     */
    private JTable findMemberTableInPanel(JPanel panel) {
        for (Component comp : panel.getComponents()) {
            if (comp instanceof JScrollPane) {
                JScrollPane scrollPane = (JScrollPane) comp;
                if (scrollPane.getViewport().getView() instanceof JTable) {
                    return (JTable) scrollPane.getViewport().getView();
                }
            }
        }
        return null;
    }

    /**
     * 获取当前表格模型
     * @return 当前表格的模型
     */
    private DefaultTableModel getCurrentTableModel() {
        Component selectedTab = tabbedPane.getSelectedComponent();
        if (selectedTab instanceof JPanel) {
            JPanel panel = (JPanel) selectedTab;
            JTable memberTable = findMemberTableInPanel(panel);
            if (memberTable != null) {
                return (DefaultTableModel) memberTable.getModel();
            }
        }
        return null;
    }
    private void findCompetitionSearchComponents(JPanel panel) {
        // 这个方法是为了查找查询条件组件，但在简化版中可以不需要
        // 保持空实现或添加调试信息
        System.out.println("查找参赛查询组件...");
    }
} // 类结束的大括号

