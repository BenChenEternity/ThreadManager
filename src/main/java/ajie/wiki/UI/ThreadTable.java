package ajie.wiki.UI;

import ajie.wiki.Main;
import ajie.wiki.thread.CustomThread;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

public class ThreadTable extends JTable {
    public DefaultTableModel tableModel;

    public ThreadTable() {
        String[] columnNames = {"<html>线程所属<br>进程ID", "<html>线程ID", "<html>状态",
                "<html>优先级", "<html>进度", "<html>总开销", "<html>到达时间", "<html>完成时间",
                "<html>周转时间", "<html>带权周转<br>时间"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        setModel(tableModel);
        getColumnModel().getColumn(0).setCellRenderer(new MyRenderer());
        getColumnModel().getColumn(1).setCellRenderer(new MyRenderer());
        getColumnModel().getColumn(2).setCellRenderer(new MyRenderer());
        getColumnModel().getColumn(3).setCellRenderer(new MyRenderer());
        getColumnModel().getColumn(4).setCellRenderer(new ProgressBarRenderer());
        getColumnModel().getColumn(5).setCellRenderer(new MyRenderer());
        getColumnModel().getColumn(6).setCellRenderer(new MyRenderer());
        getColumnModel().getColumn(7).setCellRenderer(new MyRenderer());
        getColumnModel().getColumn(8).setCellRenderer(new MyRenderer());
        getColumnModel().getColumn(9).setCellRenderer(new MyRenderer());

        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        getSelectionModel().addListSelectionListener(e -> {
            ListSelectionModel lsm = (ListSelectionModel) e.getSource();
            MyRenderer.selectedRow = lsm.getMinSelectionIndex();
        });

        int[] columnWidths = {80, 60, 140, 60, 150, 80, 80, 80, 80, 80};
        for (int i = 0; i < columnWidths.length; i++) {
            TableColumn column = getColumnModel().getColumn(i);
            column.setPreferredWidth(columnWidths[i]);
        }
    }

    public void setHighlight(int TID) {
        MyRenderer.highlightRow = findRowIndexByTID(TID);
        repaint();
    }

    public void add(int PID, int TID, int priority, Color idColor, int totalConsume, int arrivalTime) {
        JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setForeground(idColor);
        Object[] rowData = {
                PID,
                TID,
                CustomThread.ThreadStatus.NEW,
                priority,
                progressBar,
                totalConsume,
                arrivalTime
        };
        tableModel.addRow(rowData);
    }

    public void remove(int TID) {
        int rowIndex = Main.uiModule.threadTable.findRowIndexByTID(TID);
        tableModel.removeRow(rowIndex);
    }

    public void setThreadProgress(int threadId, int progress) {
        int rowCount = tableModel.getRowCount();
        int rowIndex = -1;
        for (int i = 0; i < rowCount; i++) {
            if ((int) tableModel.getValueAt(i, 1) == threadId) {
                rowIndex = i;
                break;
            }
        }
        if (rowIndex == -1) {
            return;
        }
        JProgressBar progressBar = (JProgressBar) tableModel.getValueAt(rowIndex, 4);
        progressBar.setValue(progress);
        repaint();
    }

    public int findRowIndexByTID(int threadId) {
        DefaultTableModel model = (DefaultTableModel) getModel();
        int rowCount = model.getRowCount();
        for (int row = 0; row < rowCount; row++) {
            if ((int) model.getValueAt(row, 1) == threadId) {
                return row;
            }
        }
        return -1;
    }

    public void modify(int oldTID, int pid, int tid, int priority, int totalConsume, int arrivalTime,Color color) {
        int rowIndex=findRowIndexByTID(oldTID);
        setValueAt(pid,rowIndex,0);
        setValueAt(tid,rowIndex,1);
        setValueAt(priority,rowIndex,3);
        setValueAt(totalConsume,rowIndex,5);
        setValueAt(arrivalTime,rowIndex,6);
        JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setForeground(color);
        setValueAt(progressBar,rowIndex,4);
    }

    static class MyRenderer extends DefaultTableCellRenderer {
        public static int highlightRow = -1;
        public static int selectedRow = -1;

        public MyRenderer() {
            setHorizontalAlignment(JLabel.CENTER);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component rendererComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if (row == selectedRow) {
                rendererComponent.setBackground(new Color(184, 207, 229));
            } else {
                rendererComponent.setBackground(Color.WHITE);
            }

            if (row == highlightRow) {
                rendererComponent.setBackground(Color.YELLOW);
            } else if (column == 2) {
                JLabel label = (JLabel) rendererComponent;
                CustomThread.ThreadStatus status = (CustomThread.ThreadStatus) value;
                switch (status) {
                    case NEW:
                        label.setForeground(Color.GRAY);
                        break;
                    case READY:
                        label.setForeground(Color.ORANGE);
                        break;
                    case BLOCKED:
                        label.setForeground(Color.RED);
                        break;
                    case RUNNING:
                        label.setForeground(Color.BLUE);
                        break;
                    case TERMINATED:
                        label.setForeground(new Color(7, 171, 18));
                        break;
                    default:
                        label.setForeground(table.getForeground());
                }
                Font font = label.getFont();
                label.setFont(font.deriveFont(font.getStyle() | Font.BOLD));
                return label;
            }
            return rendererComponent;
        }
    }


    static class ProgressBarRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (value instanceof JProgressBar) {
                return (JProgressBar) value;
            }
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }
    }

    public double calculateAverage(int column) {
        int rowCount = getRowCount();
        double sum = 0.0;
        int count = 0;

        for (int i = 0; i < rowCount; i++) {
            Object value = getValueAt(i, column);
            if (value != null) {
                try {
                    double numericValue = Double.parseDouble(value.toString());
                    sum += numericValue;
                    count++;
                } catch (NumberFormatException ignored) {
                }
            }
        }

        if (count > 0) {
            return sum / count;
        } else {
            return 0;
        }
    }

    public double calculateTotal(int column) {
        int rowCount = getRowCount();
        double sum = 0.0;
        for (int i = 0; i < rowCount; i++) {
            Object value = getValueAt(i, column);
            if (value != null) {
                try {
                    double numericValue = Double.parseDouble(value.toString());
                    sum += numericValue;
                } catch (NumberFormatException ignored) {
                }
            }
        }
        return sum;
    }
}


