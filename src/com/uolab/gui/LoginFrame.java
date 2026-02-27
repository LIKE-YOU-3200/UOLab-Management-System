package com.uolab.gui;

import com.uolab.entity.User;
import com.uolab.service.UserService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton resetButton;
    private UserService userService;

    public LoginFrame() {
        userService = new UserService();
        initComponents();
    }

    private void initComponents() {
        setTitle("UOLab联合开放实验室管理系统 - 登录");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null); // 居中显示

        // 主面板
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // 标题
        JLabel titleLabel = new JLabel("UOLab实验室管理系统登录");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(titleLabel, gbc);

        // 用户名
        JLabel usernameLabel = new JLabel("用户名:");
        usernameLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(usernameLabel, gbc);

        usernameField = new JTextField(15);
        usernameField.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(usernameField, gbc);

        // 密码
        JLabel passwordLabel = new JLabel("密码:");
        passwordLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(passwordLabel, gbc);

        passwordField = new JPasswordField(15);
        passwordField.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(passwordField, gbc);

        // 按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        loginButton = new JButton("登录");
        resetButton = new JButton("重置");

        // 设置按钮字体
        loginButton.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        resetButton.setFont(new Font("微软雅黑", Font.PLAIN, 14));

        // 设置按钮大小
        loginButton.setPreferredSize(new Dimension(100, 30));
        resetButton.setPreferredSize(new Dimension(100, 30));

        buttonPanel.add(loginButton);
        buttonPanel.add(resetButton);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(buttonPanel, gbc);

        // 添加事件监听器
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performLogin();
            }
        });

        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                usernameField.setText("");
                passwordField.setText("");
            }
        });

        // 支持回车键登录
        passwordField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performLogin();
            }
        });

        add(mainPanel);
        setVisible(true);
    }

    private void performLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "用户名和密码不能为空！",
                    "提示",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        User user = userService.login(username, password);

        if (user != null) {
            JOptionPane.showMessageDialog(this,
                    "登录成功！欢迎 " + user.getRealName(),
                    "成功",
                    JOptionPane.INFORMATION_MESSAGE);

            // 根据角色跳转到不同界面
            dispose(); // 关闭登录窗口
            openMainFrame(user);
        } else {
            JOptionPane.showMessageDialog(this,
                    "用户名或密码错误！",
                    "登录失败",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openMainFrame(User user) {
        // 根据用户角色打开不同的主界面
        if (user.getRoleId() == 1 || user.getRoleId() == 2) {
            // 管理员或超级管理员
            new AdminMainFrame(user);
        } else {
            // 普通用户
            new UserMainFrame(user);
        }
    }

    public static void main(String[] args) {
        // 使用SwingUtilities确保线程安全
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LoginFrame();
            }
        });
    }
}
