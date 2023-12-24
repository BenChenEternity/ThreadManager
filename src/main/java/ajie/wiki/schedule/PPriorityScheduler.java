package ajie.wiki.schedule;

import ajie.wiki.Main;
import ajie.wiki.thread.CustomThread;

import java.util.*;

public class PPriorityScheduler extends DefaultScheduler {
    private final Queue<CustomThread> queue;

    public PPriorityScheduler() {
        this.queue = new PriorityQueue<>(Comparator.comparingInt(CustomThread::getPriority).reversed());
        Main.clockModule.addTimeable(this);
    }

    @Override
    void blockedOperation() {
        this.currentThread.addSpinning();
    }

    @Override
    public void addReadyThread(CustomThread thread) {
        this.queue.add(thread);
    }

    @Override
    public CustomThread getNextThread() {
        return queue.poll();
    }

    @Override
    public boolean hasThreadUncompleted() {
        return !queue.isEmpty();
    }

    @Override
    public void checkStatus() {
        super.checkStatus();
        CustomThread priorThread = queue.peek();
        if (currentThread != null && priorThread != null && priorThread.getPriority() > currentThread.getPriority()) {
            currentThread.setReady();
            queue.add(currentThread);
            currentThread = queue.poll();
        }
    }
}

