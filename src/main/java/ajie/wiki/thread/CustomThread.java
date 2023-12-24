package ajie.wiki.thread;

import ajie.wiki.CustomEvent;
import ajie.wiki.Main;
import ajie.wiki.file.ThreadInfo;

import java.awt.*;
import java.util.HashMap;
import java.util.Random;


public class CustomThread {
    private int PID;
    private int TID;
    private Color idColor;
    private int totalConsume;
    private int progress;
    private int priority;
    private ThreadStatus status;

    public CustomThread(ThreadInfo threadInfo) {
        this(threadInfo.getPID(), threadInfo.getTID(), threadInfo.getPriority(), threadInfo.getConsume(), threadInfo.getArrivalTime());
    }

    public enum ThreadStatus {
        NEW, READY, RUNNING, BLOCKED, TERMINATED
    }

    private int arrivalTime;
    private int completeTime;
    private int turnaroundTime;
    private double weightTurnaroundTime;
    public static final ThreadIdPool threadIdPool = new ThreadIdPool();
    public static final ColorPool colorPool = new ColorPool();

    public CustomThread(int totalConsume) {
        this(0, totalConsume, 0);
    }

    public CustomThread(int totalConsume, int arrivalTime) {
        this(0, totalConsume, arrivalTime);
    }

    public CustomThread(int PID, int TID, int priority, int totalConsume, int arrivalTime) {
        this.PID = PID;
        this.TID = TID;
        this.idColor = colorPool.get();
        this.totalConsume = totalConsume;
        this.status = ThreadStatus.NEW;
        this.priority = priority;
        this.arrivalTime = arrivalTime;
        this.completeTime = -1;
        this.turnaroundTime = -1;
        this.weightTurnaroundTime = -1;
    }

    public CustomThread(int priority, int totalConsume, int arrivalTime) {
        this(new Random().nextInt(5), threadIdPool.get(), priority, totalConsume, arrivalTime);
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(int completeTime) {
        this.completeTime = completeTime;
    }

    public int getTurnaroundTime() {
        return turnaroundTime;
    }

    public void setTurnaroundTime(int turnaroundTime) {
        this.turnaroundTime = turnaroundTime;
    }

    public double getWeightTurnaroundTime() {
        return weightTurnaroundTime;
    }

    public void setWeightTurnaroundTime(double weightTurnaroundTime) {
        this.weightTurnaroundTime = weightTurnaroundTime;
    }

    public ThreadStatus getStatus() {
        return status;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getTotalConsume() {
        return totalConsume;
    }

    public void setTotalConsume(int totalConsume) {
        this.totalConsume = totalConsume;
    }

    public int getProgress() {
        return progress;
    }

    public void addProgress(int progress) {
        if (this.status == ThreadStatus.TERMINATED) {
            return;
        }
        int rate;
        if (this.progress + progress >= this.totalConsume) {
            progress = this.totalConsume - this.progress;
            this.progress = this.totalConsume;
            setTerminated();
            rate = 100;
//            System.out.println("线程结束：" + getTID());
        } else {
            this.progress += progress;
            rate = this.progress * 100 / this.totalConsume;
        }
        HashMap<String, Object> params = new HashMap<>();
        params.put("tid", this.TID);
        params.put("rate", rate);
        params.put("color", this.idColor);
        params.put("progress", progress);
        params.put("time", Main.clockModule.getGlobalTime());
        Main.eventHandler.add(new CustomEvent(CustomEvent.EventType.THREAD_PROGRESS, params));
    }

    public void addSpinning() {//线程空转
        HashMap<String, Object> params = new HashMap<>();
        params.put("tid", this.TID);
        params.put("color", this.idColor);
        params.put("time", Main.clockModule.getGlobalTime());
        Main.eventHandler.add(new CustomEvent(CustomEvent.EventType.THREAD_SPINNING, params));
    }

    public void setBlocked() {
        this.status = ThreadStatus.BLOCKED;
        HashMap<String, Object> params = new HashMap<>();
        params.put("tid", TID);
        Main.eventHandler.add(new CustomEvent(CustomEvent.EventType.THREAD_BLOCK, params));
    }

    public void setReady() {
        this.status = ThreadStatus.READY;
        HashMap<String, Object> params = new HashMap<>();
        params.put("tid", TID);
        Main.eventHandler.add(new CustomEvent(CustomEvent.EventType.THREAD_READY, params));
    }

    public void setRunning() {
        this.status = ThreadStatus.RUNNING;
        HashMap<String, Object> params = new HashMap<>();
        params.put("tid", TID);
        Main.eventHandler.add(new CustomEvent(CustomEvent.EventType.THREAD_RUNNING, params));
    }

    public void setTerminated() {
        this.status = ThreadStatus.TERMINATED;
        HashMap<String, Object> params = new HashMap<>();
        params.put("tid", TID);
        params.put("time", Main.clockModule.getGlobalTime() + 1);
        Main.eventHandler.add(new CustomEvent(CustomEvent.EventType.THREAD_TERMINATED, params));
    }

    public int getPID() {
        return PID;
    }

    public void setPID(int PID) {
        this.PID = PID;
    }

    public int getTID() {
        return TID;
    }

    public void setTID(int TID) {
        this.TID = TID;
    }

    public Color getIdColor() {
        return idColor;
    }

    public void setIdColor(Color idColor) {
        this.idColor = idColor;
    }
}
