package ajie.wiki.UI;

import ajie.wiki.Main;
import ajie.wiki.UI.generate.GeneratePanel;
import ajie.wiki.thread.CustomThread;
import ajie.wiki.util.Img;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class ThreadControlBar extends JPanel {
    JButton deleteButton;
    JButton addButton;
    JButton modifyButton;
    JButton generateButton;
    SchedulerChoosePanel schedulerChoosePanel;

    public ThreadControlBar() {
        setLayout(new FlowLayout(FlowLayout.LEFT));

        JButton blockButton = new JButton(new ImageIcon(Objects.requireNonNull(Img.get("/blocked.png"))));
        JButton notifyButton = new JButton(new ImageIcon(Objects.requireNonNull(Img.get("/run.png"))));
        deleteButton = new JButton(new ImageIcon(Objects.requireNonNull(Img.get("/delete.png"))));
        addButton = new JButton(new ImageIcon(Objects.requireNonNull(Img.get("/add.png"))));
        modifyButton = new JButton(new ImageIcon(Objects.requireNonNull(Img.get("/modify.png"))));
        generateButton = new JButton(new ImageIcon(Objects.requireNonNull(Img.get("/generate.png"))));
        schedulerChoosePanel = new SchedulerChoosePanel();

        blockButton.addActionListener(e -> {
            int selectedRow = Main.uiModule.threadTable.getSelectedRow();
            if (selectedRow >= 0) {
                int TID = (int) Main.uiModule.threadTable.getValueAt(selectedRow, 1);
                if (Main.threadModule.getThreadsMap().get(TID).getStatus() == CustomThread.ThreadStatus.TERMINATED ||
                        Main.threadModule.getThreadsMap().get(TID).getStatus() == CustomThread.ThreadStatus.NEW ||
                        Main.threadModule.getThreadsMap().get(TID).getStatus() == CustomThread.ThreadStatus.BLOCKED) {
                    return;
                }
                Main.threadModule.getThreadsMap().get(TID).setBlocked();
            }
        });

        notifyButton.addActionListener(e -> {
            int selectedRow = Main.uiModule.threadTable.getSelectedRow();
            if (selectedRow >= 0) {
                int TID = (int) Main.uiModule.threadTable.getValueAt(selectedRow, 1);
                if (Main.threadModule.getThreadsMap().get(TID).getStatus() == CustomThread.ThreadStatus.TERMINATED || Main.threadModule.getThreadsMap().get(TID).getStatus() == CustomThread.ThreadStatus.NEW || Main.threadModule.getThreadsMap().get(TID).getStatus() == CustomThread.ThreadStatus.RUNNING) {
                    return;
                }
                Main.threadModule.getThreadsMap().get(TID).setReady();
            }
        });

        addButton.addActionListener(e -> {
            SwingUtilities.invokeLater(AddPanel::new);
        });

        modifyButton.addActionListener(e -> {
            SwingUtilities.invokeLater(()-> {
                int selectedRow = Main.uiModule.threadTable.getSelectedRow();
                if (selectedRow >= 0) {
                    new ModifyPanel(selectedRow);
                }
            });
        });

        generateButton.addActionListener(e -> {
            SwingUtilities.invokeLater(GeneratePanel::new);
        });

        deleteButton.addActionListener(e -> {
            int selectedRow = Main.uiModule.threadTable.getSelectedRow();
            if (selectedRow >= 0) {
                int TID = (int) Main.uiModule.threadTable.getValueAt(selectedRow, 1);
                Main.threadModule.removeThread(TID);
            }
        });


        add(addButton);
        add(modifyButton);
        add(deleteButton);
        add(generateButton);
        add(schedulerChoosePanel);
        add(blockButton);
        add(notifyButton);
    }

    public SchedulerChoosePanel getSchedulerChoosePanel() {
        return schedulerChoosePanel;
    }
}
