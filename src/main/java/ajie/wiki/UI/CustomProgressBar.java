package ajie.wiki.UI;

import ajie.wiki.CustomEvent;
import ajie.wiki.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CustomProgressBar extends JPanel {
    private int totalConsume;
    private int progress;
    private List<Color> colorList;
    private List<Integer> progressList;
    private List<Integer> idList;
    private List<StartEndTime> timeList;
    private List<Boolean> isSpinningList;
    private boolean done;
    private final int height;
    private List<JLabel> consumeLabel;

    public CustomProgressBar(int width, int height, int consume) {
        this.progress = 0;
        this.totalConsume = consume;
        this.setPreferredSize(new Dimension(width, height + 70));
        this.colorList = new ArrayList<>();
        this.progressList = new ArrayList<>();
        this.idList = new ArrayList<>();
        this.done = false;
        this.height = height;
        this.consumeLabel = new ArrayList<>();
        this.timeList = new ArrayList<>();
        this.isSpinningList = new ArrayList<>();
        this.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        this.setMinimumSize(new Dimension(10, 50));
        this.setLayout(null);
    }

    public List<StartEndTime> getTimeList() {
        return timeList;
    }

    public void addTask(int taskConsume) {
        if (this.done) {
            this.done = false;
        }
        this.totalConsume += taskConsume;
        refresh();
    }

    public void addProgress(int newProgress, int threadId, Color color, int progressTime) {
        if (this.done) {
            return;
        }
        this.progress += newProgress;
        if (this.progress > this.totalConsume) {
            this.progress = this.totalConsume;
            done = true;
        }
        int lastIdIndex = this.idList.size() - 1;
        if (lastIdIndex >= 0 && !this.isSpinningList.get(lastIdIndex) && threadId == this.idList.get(lastIdIndex)) {
            this.progressList.set(lastIdIndex, this.progressList.get(lastIdIndex) + newProgress);
        } else {
            this.progressList.add(newProgress);
            this.colorList.add(color);
            this.idList.add(threadId);
            this.isSpinningList.add(false);
            StartEndTime startEndTime = new StartEndTime();
            startEndTime.setStartTime(progressTime);
            this.timeList.add(startEndTime);
            if (lastIdIndex >= 0) {
                this.timeList.get(lastIdIndex).setEndTime(progressTime);
            }
        }
        refresh();
    }

    public void addSpinning(int threadId, Color color, int spinningTime) {
        this.totalConsume += 1;
        this.progress += 1;
        int lastIdIndex = this.idList.size() - 1;
        if (lastIdIndex >= 0 && this.isSpinningList.get(lastIdIndex) && threadId == this.idList.get(lastIdIndex)) {
            this.progressList.set(lastIdIndex, this.progressList.get(lastIdIndex) + 1);
        } else {
            this.progressList.add(1);
            this.colorList.add(color);
            this.idList.add(threadId);
            this.isSpinningList.add(true);
            StartEndTime startEndTime = new StartEndTime();
            startEndTime.setStartTime(spinningTime);
            this.timeList.add(startEndTime);
            if (lastIdIndex >= 0) {
                this.timeList.get(lastIdIndex).setEndTime(spinningTime);
            }
        }
        refresh();
    }

    private void refresh() {
        removeAll();
        revalidate();
        repaint();

        double segmentWidth = (double) getWidth() / totalConsume;
        int accumulatedConsume = 0;

        for (int i = 0; i < colorList.size(); i++) {
            int consume = progressList.get(i);

            if (accumulatedConsume + consume > totalConsume) {
                consume = totalConsume - accumulatedConsume;
            }

            int startOffset = (int) (accumulatedConsume * segmentWidth);
            accumulatedConsume += consume;
            int endOffset = (int) (accumulatedConsume * segmentWidth);

            IdButton idButton = new IdButton(idList.get(i), startOffset, endOffset, this.height, colorList.get(i), this.timeList.get(i), this.isSpinningList.get(i));
            this.add(idButton);

        }
    }

    public void addTotalConsume(int consume) {
        this.totalConsume += consume;
        this.refresh();
    }

    public int getProgress() {
        return this.progress;
    }

    public int getTotalConsume() {
        return this.totalConsume;
    }

    public void showTime(int threadId, boolean isSpinning, int startTime, int endTime) {
        String end;
        String span;
        if (endTime == -1) {
            end = "结束时间:  未结束";
            span = "时间跨度:  未结束";
        } else {
            end = "结束时间:  " + endTime;
            span = "时间跨度:  " + (endTime - startTime);
        }

        JLabel startTimeLabel = new JLabel("开始时间:  " + startTime);
        JLabel endTimeLabel = new JLabel(end);
        JLabel timeSpan = new JLabel(span);

        JLabel threadIdLabel = new JLabel();
        JLabel spinningLabel = new JLabel();

        if (threadId == -1) {//闲置
            threadIdLabel.setText("CPU闲置");
        } else {
            if (isSpinning) {
                spinningLabel.setText("CPU等待阻塞状态的线程");
            }
            threadIdLabel.setText("线程ID:  " + threadId);
        }

        startTimeLabel.setBounds(30, this.height + 5, 200, 20);
        endTimeLabel.setBounds(30, this.height + 25, 200, 20);
        timeSpan.setBounds(30, this.height + 45, 200, 20);

        threadIdLabel.setBounds(300, this.height + 5, 200, 20);
        spinningLabel.setBounds(300, this.height + 25, 200, 20);

        this.add(startTimeLabel);
        this.add(endTimeLabel);
        this.add(timeSpan);
        this.add(threadIdLabel);
        this.add(spinningLabel);

        this.consumeLabel.add(startTimeLabel);
        this.consumeLabel.add(endTimeLabel);
        this.consumeLabel.add(timeSpan);
        this.consumeLabel.add(threadIdLabel);
        this.consumeLabel.add(spinningLabel);

        revalidate();
        repaint();
    }

    public List<JLabel> getConsumeLabel() {
        return consumeLabel;
    }

    public void clearLabel() {
        for (JLabel label : this.consumeLabel) {
            this.remove(label);
        }
        this.consumeLabel.clear();
        revalidate();
        repaint();
    }

    public void removeTotalConsume(int consume) {
        this.totalConsume -= consume;
        this.refresh();
    }
}

class IdButton extends JButton {
    private int id;
    private Color color;
    private int width;
    private int height;
    private StartEndTime startEndTime;
    private boolean isSpinning;

    public IdButton(int id, int startOffset, int endOffset, int height, Color color, StartEndTime startEndTime, boolean isSpinning) {
        this.id = id;
        this.color = color;
        this.height = height;
        this.width = endOffset - startOffset;
        this.startEndTime = startEndTime;
        this.setBounds(startOffset, 0, this.width, height);
        this.isSpinning = isSpinning;
        if (this.isSpinning) {
            setBackground(Color.WHITE);
        } else {
            setBackground(this.color);
        }
        this.addActionListener(e -> {
            System.out.println("Button " + id + " clicked!");
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                HashMap<String, Object> params = new HashMap<>();
                params.put("id", id);
                params.put("startTime", startEndTime.getStartTime());
                params.put("endTime", startEndTime.getEndTime());
                params.put("spinning", isSpinning);
                Main.eventHandler.add(new CustomEvent(CustomEvent.EventType.ON_BAR_MOUSE_ENTER, params));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                Main.eventHandler.add(new CustomEvent(CustomEvent.EventType.ON_BAR_MOUSE_EXIT, null));
            }
        });
    }

    public int getId() {
        return this.id;
    }


}
