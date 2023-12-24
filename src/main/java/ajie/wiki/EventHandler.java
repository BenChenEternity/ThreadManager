package ajie.wiki;

import ajie.wiki.file.ThreadInfo;
import ajie.wiki.thread.CustomThread;
import ajie.wiki.util.DecimalFormatter;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class EventHandler {
    private final Queue<CustomEvent> eventQueue;

    public EventHandler() {
        this.eventQueue = new LinkedList<>();
    }

    public synchronized void add(CustomEvent event) {
        this.eventQueue.add(event);
        notify();
    }

    private void handleEvent(CustomEvent event) {
        switch (event.eventType) {
            case ON_BAR_MOUSE_ENTER -> {
                int selectedThreadId = (int) event.params.get("id");
                Main.uiModule.threadTable.setHighlight(selectedThreadId);
                int startTime = (int) event.params.get("startTime");
                int endTime = (int) event.params.get("endTime");
                boolean spinning = (boolean) event.params.get("spinning");
                Main.uiModule.progressBar.showTime(selectedThreadId, spinning, startTime, endTime);
            }
            case ON_BAR_MOUSE_EXIT -> {
                Main.uiModule.threadTable.setHighlight(-1);
                Main.uiModule.progressBar.clearLabel();
            }
            case THREAD_ADD -> {
                int PID = (int) event.params.get("PID");
                int TID = (int) event.params.get("TID");
                int priority = (int) event.params.get("priority");
                Color idColor = (Color) event.params.get("idColor");
                int totalConsume = (int) event.params.get("totalConsume");
                int arrivalTime = (int) event.params.get("arrivalTime");
                Main.uiModule.progressBar.addTotalConsume(totalConsume);
                Main.uiModule.threadTable.add(PID, TID, priority, idColor, totalConsume, arrivalTime);
            }
            case THREAD_REMOVE -> {
                int threadId = (int) event.params.get("tid");
                int consume = (int) event.params.get("consume");
                Main.uiModule.progressBar.removeTotalConsume(consume);
                Main.uiModule.threadTable.remove(threadId);
            }
            case THREAD_MODIFY -> {
                int oldTID=(int) event.params.get("OldTID");
                int PID = (int) event.params.get("PID");
                int TID = (int) event.params.get("TID");
                int priority = (int) event.params.get("priority");
                int originalConsume=(int) event.params.get("originalConsume");
                int totalConsume = (int) event.params.get("totalConsume");
                int arrivalTime = (int) event.params.get("arrivalTime");
                Color color=(Color) event.params.get("color");
                Main.uiModule.progressBar.addTotalConsume(totalConsume-originalConsume);
                Main.uiModule.threadTable.modify(oldTID,PID,TID,priority,totalConsume,arrivalTime,color);
            }
            case THREAD_PROGRESS -> {
                int threadId = (int) event.params.get("tid");
                int rate = (int) event.params.get("rate");
                int progress = (int) event.params.get("progress");
                Color color = (Color) event.params.get("color");
                int time = (int) event.params.get("time");
                Main.uiModule.threadTable.setThreadProgress(threadId, rate);
                Main.uiModule.progressBar.addProgress(progress, threadId, color, time);
            }
            case THREAD_SPINNING -> {
                int threadId = (int) event.params.get("tid");
                Color color = (Color) event.params.get("color");
                int time = (int) event.params.get("time");
                Main.uiModule.progressBar.addSpinning(threadId, color, time);
            }
            case THREAD_BLOCK -> {
                int threadId = (int) event.params.get("tid");
                int rowIndex = Main.uiModule.threadTable.findRowIndexByTID(threadId);
                Main.uiModule.threadTable.setValueAt(CustomThread.ThreadStatus.BLOCKED, rowIndex, 2);
            }
            case THREAD_READY -> {
                int threadId = (int) event.params.get("tid");
                int rowIndex = Main.uiModule.threadTable.findRowIndexByTID(threadId);
                Main.uiModule.threadTable.setValueAt(CustomThread.ThreadStatus.READY, rowIndex, 2);
            }
            case THREAD_RUNNING -> {
                int threadId = (int) event.params.get("tid");
                int rowIndex = Main.uiModule.threadTable.findRowIndexByTID(threadId);
                Main.uiModule.threadTable.setValueAt(CustomThread.ThreadStatus.RUNNING, rowIndex, 2);
            }
            case THREAD_TERMINATED -> {
                int threadId = (int) event.params.get("tid");
                int globalTime = (int) event.params.get("time");
                int rowIndex = Main.uiModule.threadTable.findRowIndexByTID(threadId);
                Main.uiModule.threadTable.setValueAt(CustomThread.ThreadStatus.TERMINATED, rowIndex, 2);
                Main.uiModule.threadTable.setValueAt(globalTime, rowIndex, 7);
                int turnaroundTime = globalTime - Main.threadModule.getThreadsMap().get(threadId).getArrivalTime();
                Main.uiModule.threadTable.setValueAt(turnaroundTime, rowIndex, 8);
                double weightTurnaroundTime = (double) turnaroundTime / Main.threadModule.getThreadsMap().get(threadId).getTotalConsume();
                String value = DecimalFormatter.setPrecision(weightTurnaroundTime, 2);
                Main.uiModule.threadTable.setValueAt(value, rowIndex, 9);
                Main.uiModule.dashboard.setTotalTurnaround(DecimalFormatter.setPrecision(Main.uiModule.threadTable.calculateTotal(8), 0));
                Main.uiModule.dashboard.setAvgTurnaround(DecimalFormatter.setPrecision(Main.uiModule.threadTable.calculateAverage(8), 2));
                Main.uiModule.dashboard.setAvgWeightTurnaround(DecimalFormatter.setPrecision(Main.uiModule.threadTable.calculateAverage(9), 2));
            }
            case IDLE_STATE -> {
                int time = (int) event.params.get("time");
                Main.uiModule.progressBar.addSpinning(-1, Color.BLACK, time);
            }
            case CALCULATE_TOTAL_CONSUME -> {
                int totalConsume = 0;
                for (ThreadInfo info : Main.threadModule.getThreadInfos()) {
                    totalConsume += info.getConsume();
                }
                Main.uiModule.dashboard.setTotalConsume(String.valueOf(totalConsume));
            }
            case SCHEDULER_DONE -> {
                Main.clockModule.stop();
                int actualConsume = (int) event.params.get("actualConsume");
                Main.uiModule.dashboard.setActualTotalConsume(String.valueOf(actualConsume));
                Main.uiModule.progressBar.getTimeList().get(Main.uiModule.progressBar.getTimeList().size() - 1).setEndTime(actualConsume);
            }
            case CLOCK_START -> {//锁定相关操作 算法选择 删除按钮
                Main.uiModule.setDisable();
            }
            case REFRESH_TABLE -> {
                Main.uiModule.threadTable.repaint();
            }
        }
    }

    public synchronized void run() {
        while (true) {
            if (this.eventQueue.isEmpty()) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                CustomEvent event = eventQueue.poll();
                handleEvent(event);
            }
        }
    }
}
