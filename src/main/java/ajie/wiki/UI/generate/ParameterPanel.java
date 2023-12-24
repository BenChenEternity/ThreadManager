package ajie.wiki.UI.generate;

import ajie.wiki.Main;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ParameterPanel extends JFrame {
    private final List<JTextField> textFieldList=new ArrayList<>();
    public ParameterPanel() {
        setTitle("线程生成参数设置");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(280, 300);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 2));

        String[] labels = {"线程个数:", "优先级最小值:", "优先级最大值:", "开销最小值:", "开销最大值:", "到达时间最小值:", "到达时间最大值:"};
        String[] defaultValues = {"20", "0", "20", "5", "20", "0", "40"};
        Font labelFont=new Font("SimSun", Font.BOLD, 14);
        for (int i=0;i<labels.length;i++) {
            JLabel label = new JLabel(labels[i]);
            label.setFont(labelFont);
            JTextField textField = new JTextField();
            textField.setFont(labelFont);
            textField.setText(defaultValues[i]);
            textFieldList.add(textField);
            panel.add(label);
            panel.add(textField);
        }

        JButton okButton = new JButton("确定");
        okButton.setFont(labelFont);
        okButton.addActionListener(e -> {
            SwingUtilities.invokeLater(()->{
                try {
                    int count = Integer.parseInt(textFieldList.get(0).getText());
                    int minPriority = Integer.parseInt(textFieldList.get(1).getText());
                    int maxPriority = Integer.parseInt(textFieldList.get(2).getText());
                    int minConsume = Integer.parseInt(textFieldList.get(3).getText());
                    int maxConsume = Integer.parseInt(textFieldList.get(4).getText());
                    int minArrivalTime = Integer.parseInt(textFieldList.get(5).getText());
                    int maxArrivalTime = Integer.parseInt(textFieldList.get(6).getText());
                    Main.threadModule.create(count,minConsume,maxConsume-minConsume,minPriority,maxPriority-minPriority,minArrivalTime,maxArrivalTime-minArrivalTime);
                }catch (Exception ex){
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "输入有误", "错误", JOptionPane.ERROR_MESSAGE);
                }
                this.dispose();
            });
        });

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(okButton);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }
}

