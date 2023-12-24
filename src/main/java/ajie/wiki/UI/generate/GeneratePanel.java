package ajie.wiki.UI.generate;

import ajie.wiki.Main;

import javax.swing.*;
import java.awt.*;

public class GeneratePanel extends JFrame {
    public GeneratePanel() {
        setTitle("生成");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(200, 160);
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JButton btn1 = new JButton("预设");
        JButton btn2 = new JButton("精确随机生成");
        JButton btn3 = new JButton("快速随机生成");

        btn1.setPreferredSize(new Dimension(160, 32));
        btn1.setFont(new Font("SimSun", Font.BOLD, 16));
        btn1.addActionListener(e -> {
            SwingUtilities.invokeLater(()->{
                new PredefinedPanel();
                this.dispose();
            });
        });

        btn2.setPreferredSize(new Dimension(160, 32));
        btn2.setFont(new Font("SimSun", Font.BOLD, 16));
        btn2.addActionListener(e -> {
            SwingUtilities.invokeLater(()->{
                new ParameterPanel();
                this.dispose();
            });
        });

        btn3.setPreferredSize(new Dimension(160, 32));
        btn3.setFont(new Font("SimSun", Font.BOLD, 16));
        btn3.addActionListener(e -> {
            Main.threadModule.getThreadInfos().clear();
            Main.threadModule.create(10,5,10,0,10,2,20);
            Main.reset(0);
            this.dispose();
        });

        panel.add(btn1);
        panel.add(Box.createVerticalStrut(10));
        panel.add(btn2);
        panel.add(Box.createVerticalStrut(10));
        panel.add(btn3);

        add(panel);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
