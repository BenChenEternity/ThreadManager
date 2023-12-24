package ajie.wiki.UI;

import javax.swing.*;
import java.awt.*;

public class Dashboard extends JPanel {
    JLabel totalConsume;
    JLabel actualTotalConsume;
    JLabel totalTurnaround;
    JLabel avgTurnaround;
    JLabel avgWeightTurnaround;

    public Dashboard() {
        setLayout(new GridLayout(1, 2)); // Set GridLayout with 1 row and 2 columns

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2)); // Set GridLayout with 5 rows and 2 columns

        totalConsume = new JLabel("总开销（时间）: ");
        actualTotalConsume = new JLabel("所有线程完成耗时（时间）: ");
        totalTurnaround = new JLabel("总周转时间: ");
        avgTurnaround = new JLabel("平均周转时间: ");
        avgWeightTurnaround = new JLabel("平均带权周转时间: ");

        panel.add(totalConsume);
        panel.add(new JLabel()); // Placeholder for the empty space in the grid
        panel.add(actualTotalConsume);
        panel.add(new JLabel()); // Placeholder
        panel.add(totalTurnaround);
        panel.add(new JLabel()); // Placeholder
        panel.add(avgTurnaround);
        panel.add(new JLabel()); // Placeholder
        panel.add(avgWeightTurnaround);
        panel.add(new JLabel()); // Placeholder

        add(panel);
    }

    public void setTotalConsume(String text) {
        totalConsume.setText("总开销（时间）: " + text);
    }

    public void setActualTotalConsume(String text) {
        actualTotalConsume.setText("所有线程完成耗时（时间）: " + text);
    }

    public void setTotalTurnaround(String text) {
        totalTurnaround.setText("总周转时间: " + text);
    }

    public void setAvgTurnaround(String text) {
        avgTurnaround.setText("平均周转时间: " + text);
    }

    public void setAvgWeightTurnaround(String text) {
        avgWeightTurnaround.setText("平均带权周转时间: " + text);
    }
}
