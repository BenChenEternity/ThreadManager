package ajie.wiki.UI;

import ajie.wiki.Main;
import ajie.wiki.thread.CustomThread;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class AddPanel extends JFrame {
    private final List<JTextField> textFieldList=new ArrayList<>();
    public AddPanel() {
        setTitle("新建线程");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(420, 200);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 2));

        String[] labels = {"所属进程ID（留空自动分配）:", "线程ID（留空自动分配）:", "优先级:", "开销:", "到达时间:"};
        String[] defaultValues = {"", "", "0", "10", "0"};
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
                int pid;
                int tid = 0;
                int priority;
                int consume;
                int arrivalTime;
                boolean allocateTID = false;
                if(Objects.equals(textFieldList.get(0).getText(), "")){
                    pid= new Random().nextInt(10);
                }else{
                    try {
                        pid = Integer.parseInt(textFieldList.get(0).getText());
                    }catch (Exception ex){
                        JOptionPane.showMessageDialog(null, "所属进程ID 输入有误", "错误", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
                if(Objects.equals(textFieldList.get(1).getText(), "")){
                    allocateTID=true;
                }else{
                    try {
                        tid = Integer.parseInt(textFieldList.get(1).getText());
                    }catch (Exception ex){
                        JOptionPane.showMessageDialog(null, "线程ID 输入有误", "错误", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
                try {
                    priority = Integer.parseInt(textFieldList.get(2).getText());
                }catch (Exception ex){
                    JOptionPane.showMessageDialog(null, "优先级 输入有误", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                try {
                    consume = Integer.parseInt(textFieldList.get(3).getText());
                }catch (Exception ex){
                    JOptionPane.showMessageDialog(null, "开销 输入有误", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                try {
                    arrivalTime = Integer.parseInt(textFieldList.get(4).getText());
                }catch (Exception ex){
                    JOptionPane.showMessageDialog(null, "到达时间 输入有误", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if(allocateTID){
                    tid= CustomThread.threadIdPool.get();
                }
                Main.threadModule.addThread(new CustomThread(pid,tid,priority,consume,arrivalTime));
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

