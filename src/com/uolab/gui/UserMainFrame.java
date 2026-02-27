package com.uolab.gui;

import com.uolab.entity.User;
import javax.swing.*;
import java.awt.*;

public class UserMainFrame extends JFrame {
    private User currentUser;

    public UserMainFrame(User user) {
        this.currentUser = user;
        initComponents();
    }

    private void initComponents() {
        setTitle("UOLab实验室管理系统 - 普通用户");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // 菜单栏
        JMenuBar menuBar = new JMenuBar();
        JMenu systemMenu = new JMenu("系统");
        JMenuItem logoutItem = new JMenuItem("退出登录");
        JMenuItem exitItem = new JMenuItem("退出系统");

        logoutItem.addActionListener(e -> {
            dispose();
            new LoginFrame();
        });

        exitItem.addActionListener(e -> System.exit(0));

        systemMenu.add(logoutItem);
        systemMenu.addSeparator();
        systemMenu.add(exitItem);

        // 个人信息菜单
        JMenu personalMenu = new JMenu("个人信息");
        JMenuItem viewInfoItem = new JMenuItem("查看信息");
        JMenuItem modifyInfoItem = new JMenuItem("修改信息");
        JMenuItem changePasswordItem = new JMenuItem("修改密码");

        personalMenu.add(viewInfoItem);
        personalMenu.add(modifyInfoItem);
        personalMenu.add(changePasswordItem);

        // 公开课菜单
        JMenu courseMenu = new JMenu("公开课");
        JMenuItem viewCourseItem = new JMenuItem("查看公开课");
        JMenuItem signCourseItem = new JMenuItem("报名公开课");

        courseMenu.add(viewCourseItem);
        courseMenu.add(signCourseItem);

        // 学分菜单
        JMenu creditMenu = new JMenu("创新学分");
        JMenuItem queryCreditItem = new JMenuItem("学分查询");

        creditMenu.add(queryCreditItem);

        menuBar.add(systemMenu);
        menuBar.add(personalMenu);
        menuBar.add(courseMenu);
        menuBar.add(creditMenu);

        setJMenuBar(menuBar);

        // 主面板
        JPanel mainPanel = new JPanel(new BorderLayout());

        // 欢迎信息
        JLabel welcomeLabel = new JLabel(
                "<html><center><h1>欢迎 " + currentUser.getRealName() + "！</h1>" +
                        "<p>欢迎使用UOLab联合开放实验室管理系统</p>" +
                        "<p>请使用左侧菜单进行相关操作</p></center></html>",
                SwingConstants.CENTER
        );

        mainPanel.add(welcomeLabel, BorderLayout.CENTER);

        // 状态栏
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.add(new JLabel("当前用户: " + currentUser.getRealName() + " (普通用户)"));

        mainPanel.add(statusPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }
}
