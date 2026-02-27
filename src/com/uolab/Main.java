package com.uolab;

import com.uolab.gui.LoginFrame;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // 设置系统外观
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 启动登录界面
        SwingUtilities.invokeLater(() -> {
            new LoginFrame();
        });
    }
}