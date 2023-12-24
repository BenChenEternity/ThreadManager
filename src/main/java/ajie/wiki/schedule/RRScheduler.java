package ajie.wiki.schedule;

import ajie.wiki.Main;
import ajie.wiki.thread.CustomThread;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

public class RRScheduler extends DefaultScheduler {
    public static int occupiedTimeSlice = 2;
    int timeSliceCounter = 0;
    private final Queue<CustomThread> queue;

    public RRScheduler() {
        this.queue = new LinkedList<>();
        Main.clockModule.addTimeable(this);
    }

    @Override
    void blockedOperation() {
        this.currentThread.addSpinning();
        this.timeSliceCounter+=1;
    }

    @Override
    public void addReadyThread(CustomThread thread) {
        this.queue.add(thread);
    }

    @Override
    public CustomThread getNextThread() {
        CustomThread thread = queue.poll();
        if (thread != null) {
            timeSliceCounter = 0;
        }
        return thread;
    }

    @Override
    public boolean hasThreadUncompleted() {
        return !queue.isEmpty();
    }

    @Override
    public void checkStatus() {
        super.checkStatus();
        if (timeSliceCounter >= occupiedTimeSlice) {
            timeSliceCounter = 0;
            if (currentThread != null) {
                queue.add(currentThread);
                if(currentThread.getStatus()== CustomThread.ThreadStatus.RUNNING){
                    currentThread.setReady();
                }
                currentThread = queue.poll();
            }
        }
    }

    @Override
    protected void addProgress(CustomThread thread) {
        super.addProgress(thread);
        timeSliceCounter += 1;
    }
}
