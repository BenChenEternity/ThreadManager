package ajie.wiki.UI;

import ajie.wiki.Main;
import ajie.wiki.clock.Timeable;
import ajie.wiki.util.Img;

import javax.swing.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class ClockControlPanel extends JPanel{
    private JLabel clockLabel;
    private JButton buttonStepForward;

    public ClockControlPanel() {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        JButton buttonPause = new JButton(new ImageIcon(Objects.requireNonNull(Img.get("/pause.png"))));
        JButton buttonContinue = new JButton(new ImageIcon(Objects.requireNonNull(Img.get("/start.png"))));
        buttonStepForward = new JButton(new ImageIcon(Objects.requireNonNull(Img.get("/stepForward.png"))));
        buttonPause.setEnabled(false);
        JButton buttonAgain = new JButton(new ImageIcon(Objects.requireNonNull(Img.get("/again.png"))));

        clockLabel = new JLabel("当前时间:  0");

        JLabel stepLabel = new JLabel("步进步数：");
        JTextField stepLengthField=new JTextField("1");

        stepLengthField.setMinimumSize(new Dimension(100, 30));
        stepLengthField.setMaximumSize(new Dimension(100, 30));

        buttonPause.setPreferredSize(new java.awt.Dimension(48, 48));
        buttonContinue.setPreferredSize(new java.awt.Dimension(48, 48));
        buttonStepForward.setPreferredSize(new java.awt.Dimension(48, 48));
        buttonAgain.setPreferredSize(new java.awt.Dimension(48, 48));

        buttonPause.addActionListener(e -> {
            Main.clockModule.pause();
            buttonPause.setEnabled(false);
            buttonContinue.setEnabled(true);
        });

        buttonContinue.addActionListener(e -> {
            if(Main.threadModule.getThreadInfos().size()>0){
                Main.clockModule.continueRunning();
                buttonContinue.setEnabled(false);
                buttonPause.setEnabled(true);
            }else{
                JOptionPane.showMessageDialog(null, "没有线程", "错误", JOptionPane.ERROR_MESSAGE);
            }
        });

        buttonStepForward.addActionListener(e->{
            int stepLength;
            try {
                stepLength = Integer.parseInt(stepLengthField.getText());
            }catch (Exception ex){
                stepLengthField.setText("1");
                stepLength=1;
            }
            for(int i=0;i<stepLength;i++){
                Main.clockModule.cycle();
            }
        });

        buttonAgain.addActionListener(e -> {
            Main.reset(Main.uiModule.getThreadControlBar().getSchedulerChoosePanel().getSchedulerComboBox().getSelectedIndex());
        });

        add(buttonPause);
        add(Box.createHorizontalStrut(10));
        add(buttonContinue);
        add(Box.createHorizontalStrut(10));
        add(buttonStepForward);
        add(Box.createHorizontalStrut(10));
        add(buttonAgain);
        add(Box.createHorizontalStrut(10));
        add(clockLabel);
        add(Box.createHorizontalStrut(30));
        add(stepLabel);
        add(Box.createHorizontalStrut(10));
        add(stepLengthField);
    }
    public void setTime() {
        this.clockLabel.setText("当前时间:  " + Main.clockModule.getGlobalTime());
    }
}


