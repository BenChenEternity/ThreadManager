package ajie.wiki.UI;

import ajie.wiki.CustomEvent;
import ajie.wiki.Main;
import ajie.wiki.file.FileIO;
import ajie.wiki.file.ThreadInfo;
import ajie.wiki.thread.CustomThread;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.util.List;

public class UIModule {
    public CustomProgressBar progressBar;
    public ThreadTable threadTable;
    public Dashboard dashboard;
    public ClockControlPanel clockControlPanel;
    private final JFrame frame;
    private final ThreadControlBar threadControlBar;

    public UIModule() {
        frame = new JFrame("线程调度管理器");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addMenu();

        frame.getContentPane().setLayout(new BorderLayout());

        progressBar = new CustomProgressBar(640, 30, 0);
        frame.getContentPane().add(progressBar, BorderLayout.NORTH);

        JPanel threadPanel = new JPanel();
        threadPanel.setLayout(new BoxLayout(threadPanel, BoxLayout.Y_AXIS));
        threadControlBar = new ThreadControlBar();
        threadTable = new ThreadTable();
        JScrollPane scrollPane = new JScrollPane(threadTable);
        dashboard = new Dashboard();
        threadPanel.add(threadControlBar);
        threadPanel.add(scrollPane);
        threadPanel.add(dashboard);

        frame.getContentPane().add(threadPanel, BorderLayout.CENTER);

        clockControlPanel = new ClockControlPanel();
        frame.getContentPane().add(clockControlPanel, BorderLayout.SOUTH);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void addMenu() {
        // 创建菜单栏
        JMenuBar menuBar = new JMenuBar();

        // 文件菜单
        JMenu fileMenu = new JMenu("文件");
        JMenuItem saveMenuItem = new JMenuItem("保存");
        JMenuItem saveAsMenuItem = new JMenuItem("另存为");
        JMenuItem openMenuItem = new JMenuItem("打开");

        saveMenuItem.addActionListener(e -> {
            if (FileIO.FILE_SAVE_PATH == null) {
                JFileChooser fileChooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter("线程管理模拟文件 Thread Management Simulator File (*.tms)", "tms");
                fileChooser.setFileFilter(filter);
                int result = fileChooser.showSaveDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    String filePath = selectedFile.getAbsolutePath();
                    if (!filePath.toLowerCase().endsWith(".tms")) {
                        filePath += ".tms";
                    }
                    FileIO.FILE_SAVE_PATH = filePath;
                    FileIO.FILE_READ_DEFAULT_PATH = filePath;
                    frame.setTitle("线程调度管理器 - " + filePath);
                } else {
                    return;
                }
            }
            FileIO.save(Main.threadModule.getThreadInfos(), FileIO.FILE_SAVE_PATH);
        });

        saveAsMenuItem.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("线程管理模拟文件 Thread Management Simulator File (*.tms)", "tms");
            fileChooser.setFileFilter(filter);
            int result = fileChooser.showSaveDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                String filePath = selectedFile.getAbsolutePath();
                if (!filePath.toLowerCase().endsWith(".tms")) {
                    filePath += ".tms";
                }
                FileIO.FILE_SAVE_PATH = filePath;
                FileIO.save(Main.threadModule.getThreadInfos(), filePath);
                frame.setTitle("线程调度管理器 - " + filePath);
            }
        });

        openMenuItem.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();

            if (FileIO.FILE_READ_DEFAULT_PATH != null) {
                fileChooser.setCurrentDirectory(new File(FileIO.FILE_READ_DEFAULT_PATH));
            }
            FileNameExtensionFilter filter = new FileNameExtensionFilter("线程管理模拟文件 Thread Management Simulator File (*.tms)", "tms");
            fileChooser.setFileFilter(filter);

            int result = fileChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                String selectedFile = fileChooser.getSelectedFile().toString();
                FileIO.FILE_READ_DEFAULT_PATH = selectedFile;
                FileIO.FILE_SAVE_PATH = selectedFile;
                List<ThreadInfo> infoList = FileIO.read(selectedFile);
                Main.threadModule.getThreadInfos().clear();
                Main.threadModule.getThreadInfos().addAll(infoList);
                Main.reset("线程调度管理器 - " + selectedFile);
            }
        });


        fileMenu.add(saveMenuItem);
        fileMenu.add(saveAsMenuItem);
        fileMenu.add(openMenuItem);

        // 关于菜单
        JMenu aboutMenu = new JMenu("关于");
        JMenuItem authorMenuItem = new JMenuItem("作者");
        aboutMenu.add(authorMenuItem);

        authorMenuItem.addActionListener(e -> {
            SwingUtilities.invokeLater(()->{
                AboutPanel aboutPanel = new AboutPanel();
                aboutPanel.setVisible(true);
            });
        });

        // 将菜单添加到菜单栏
        menuBar.add(fileMenu);
        menuBar.add(aboutMenu);

        // 设置菜单栏
        frame.setJMenuBar(menuBar);
    }

    public JFrame getFrame() {
        return frame;
    }

    public void setDisable() {
        this.threadControlBar.addButton.setEnabled(false);
        this.threadControlBar.deleteButton.setEnabled(false);
        this.threadControlBar.modifyButton.setEnabled(false);
        this.threadControlBar.schedulerChoosePanel.getSchedulerComboBox().setEnabled(false);

    }

    public ThreadControlBar getThreadControlBar() {
        return threadControlBar;
    }
}
