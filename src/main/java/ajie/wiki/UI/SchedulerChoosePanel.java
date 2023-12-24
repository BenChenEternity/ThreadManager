package ajie.wiki.UI;

import ajie.wiki.Main;
import ajie.wiki.schedule.*;

import javax.swing.*;
import java.awt.*;

public class SchedulerChoosePanel extends JPanel {
    private final JComboBox<String> schedulerComboBox;
    private int currentSelection;

    public SchedulerChoosePanel() {
        currentSelection = 0;
        setLayout(new FlowLayout());
        String[] schedulerOptions = {"FCFS 先来先服务", "SJF 短作业优先", "SRTN 最短剩余时间优先", "非抢占式优先级调度", "抢占式优先级调度", "RR 时间片轮转"};
        schedulerComboBox = new JComboBox<>(schedulerOptions);
        schedulerComboBox.setSelectedIndex(0);
        schedulerComboBox.addActionListener(e -> {
            int selectedIndex = schedulerComboBox.getSelectedIndex();
            if (currentSelection != selectedIndex) {
                Main.clockModule.getTimeables().clear();
                switch (selectedIndex) {
                    case 0: {
                        Main.scheduler = new FCFSScheduler();
                        currentSelection = 0;
                        break;
                    }
                    case 1: {
                        Main.scheduler = new SJFScheduler();
                        currentSelection = 1;
                        break;
                    }
                    case 2: {
                        Main.scheduler = new SRTNScheduler();
                        currentSelection = 2;
                        break;
                    }
                    case 3: {
                        Main.scheduler = new PriorityScheduler();
                        currentSelection = 3;
                        break;
                    }
                    case 4: {
                        Main.scheduler = new PPriorityScheduler();
                        currentSelection = 4;
                        break;
                    }
                    case 5: {
                        String userInput = JOptionPane.showInputDialog(null, "请输入轮转时间片的单位耗时:",RRScheduler.occupiedTimeSlice);
                        int timeSplice;
                        if(userInput==null){
                            JOptionPane.showMessageDialog(null, "没有输入\n使用默认值 2", "错误", JOptionPane.ERROR_MESSAGE);
                            timeSplice=2;
                        }else{
                            try {
                                timeSplice=Integer.parseInt(userInput);
                                if(timeSplice<=0){
                                    JOptionPane.showMessageDialog(null, "输入有误，请输入一个正数\n使用默认值 2", "错误", JOptionPane.ERROR_MESSAGE);
                                    timeSplice=2;
                                }
                            }catch (Exception ex){
                                JOptionPane.showMessageDialog(null, "输入有误，请输入一个有效的数字\n使用默认值 2", "错误", JOptionPane.ERROR_MESSAGE);
                                timeSplice=2;
                            }
                        }
                        RRScheduler.occupiedTimeSlice=timeSplice;
                        Main.scheduler = new RRScheduler();
                        currentSelection = 5;
                        break;
                    }
                }
            }
        });
        add(new JLabel("选择调度算法:"));
        add(schedulerComboBox);
    }

    public JComboBox<String> getSchedulerComboBox() {
        return schedulerComboBox;
    }
}