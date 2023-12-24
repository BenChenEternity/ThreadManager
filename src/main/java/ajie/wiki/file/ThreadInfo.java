package ajie.wiki.file;

import ajie.wiki.thread.CustomThread;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ThreadInfo implements Serializable {
    @Serial
    private static final long serialVersionUID = 123456789L;
    private int PID;
    private int TID;
    private int priority;
    private int consume;
    private int arrivalTime;

    public int getPID() {
        return PID;
    }

    public int getTID() {
        return TID;
    }

    public int getPriority() {
        return priority;
    }

    public int getConsume() {
        return consume;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public void setPID(int PID) {
        this.PID = PID;
    }

    public void setTID(int TID) {
        this.TID = TID;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setConsume(int consume) {
        this.consume = consume;
    }

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public ThreadInfo(CustomThread thread) {
        this.PID = thread.getPID();
        this.TID = thread.getTID();
        this.priority = thread.getPriority();
        this.consume = thread.getTotalConsume();
        this.arrivalTime = thread.getArrivalTime();
    }

    public static List<ThreadInfo> makeInfoList(HashMap<Integer, CustomThread> threadsMap) {
        ArrayList<ThreadInfo> list = new ArrayList<>();
        threadsMap.forEach((key, value) -> {
            list.add(new ThreadInfo(value));
        });
        return list;
    }

    public String toString() {
        return "PID: " + this.priority + "  TID: " + this.TID + "  priority: " + this.priority + "  consume: " + this.consume + "  arrivalTime: " + this.arrivalTime;
    }

}
