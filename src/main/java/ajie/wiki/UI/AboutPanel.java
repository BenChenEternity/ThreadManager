package ajie.wiki.UI;

import javax.swing.*;
import java.awt.*;

public class AboutPanel extends JFrame {

    public AboutPanel() {
        setTitle("关于");
        JLabel titleLabel = new JLabel("线程调度管理器", JLabel.CENTER);
        titleLabel.setFont(new Font("SimSun", Font.BOLD, 16));
        JLabel contentLabel = new JLabel(
                "<html>这是一个线程调度模拟软件<br><br>"
                        + "202130440194 陈嘉杰 21网络工程<br>"
                        + "操作系统课程设计</html>", JLabel.CENTER);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(contentLabel, BorderLayout.CENTER);
        getContentPane().add(panel);
        setSize(220, 140);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }
}