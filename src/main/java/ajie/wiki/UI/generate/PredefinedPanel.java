package ajie.wiki.UI.generate;

import ajie.wiki.Main;

import javax.swing.*;
import java.awt.*;

public class PredefinedPanel extends JFrame {
    public PredefinedPanel() {
        setTitle("使用预设");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(450, 280);
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JButton btn1 = new JButton("生成适用于FCFS（先来先服务）的线程");
        JButton btn2 = new JButton("生成适用于SJF（短作业优先）的线程");
        JButton btn3 = new JButton("生成适用于SRTN（最短剩余时间优先）的线程");
        JButton btn4 = new JButton("生成适用于非抢占式优先级调度算法的线程");
        JButton btn5 = new JButton("生成适用于抢占式优先级调度算法的线程");
        JButton btn6 = new JButton("生成适用于RR（时间片轮转）的线程");

        btn1.setPreferredSize(new Dimension(400, 32));
        btn1.setFont(new Font("SimSun", Font.BOLD, 16));
        btn1.addActionListener(e -> {
            Main.threadModule.getThreadInfos().clear();
            Main.threadModule.createFCFS();
            Main.reset(0);
            this.dispose();
        });

        btn2.setPreferredSize(new Dimension(400, 32));
        btn2.setFont(new Font("SimSun", Font.BOLD, 16));
        btn2.addActionListener(e -> {
            Main.threadModule.getThreadInfos().clear();
            Main.threadModule.createSJF();
            Main.reset(1);
            this.dispose();
        });

        btn3.setPreferredSize(new Dimension(400, 32));
        btn3.setFont(new Font("SimSun", Font.BOLD, 16));
        btn3.addActionListener(e -> {
            Main.threadModule.getThreadInfos().clear();
            Main.threadModule.createSRTN();
            Main.reset(2);
            this.dispose();
        });

        btn4.setPreferredSize(new Dimension(400, 32));
        btn4.setFont(new Font("SimSun", Font.BOLD, 16));
        btn4.addActionListener(e -> {
            Main.threadModule.getThreadInfos().clear();
            Main.threadModule.createPriority();
            Main.reset(3);
            this.dispose();
        });

        btn5.setPreferredSize(new Dimension(400, 32));
        btn5.setFont(new Font("SimSun", Font.BOLD, 16));
        btn5.addActionListener(e -> {
            Main.threadModule.getThreadInfos().clear();
            Main.threadModule.createPPriority();
            Main.reset(4);
            this.dispose();
        });

        btn6.setPreferredSize(new Dimension(400, 32));
        btn6.setFont(new Font("SimSun", Font.BOLD, 16));
        btn6.addActionListener(e -> {
            Main.threadModule.getThreadInfos().clear();
            Main.threadModule.createRR();
            Main.reset(5);
            this.dispose();
        });


        panel.add(btn1);
        panel.add(Box.createVerticalStrut(10));
        panel.add(btn2);
        panel.add(Box.createVerticalStrut(10));
        panel.add(btn3);
        panel.add(Box.createVerticalStrut(10));
        panel.add(btn4);
        panel.add(Box.createVerticalStrut(10));
        panel.add(btn5);
        panel.add(Box.createVerticalStrut(10));
        panel.add(btn6);

        add(panel);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
