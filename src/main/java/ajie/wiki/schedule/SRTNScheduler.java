package ajie.wiki.schedule;

import ajie.wiki.Main;
import ajie.wiki.thread.CustomThread;

import java.util.*;

public class SRTNScheduler extends DefaultScheduler {
    private final Queue<CustomThread> queue;

    public SRTNScheduler() {
        this.queue = new PriorityQueue<>(Comparator.comparingInt(thread -> thread.getTotalConsume() - thread.getProgress()));
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
        CustomThread shortestThread = queue.peek();
        if (currentThread != null && shortestThread != null && shortestThread.getTotalConsume() - shortestThread.getProgress() < currentThread.getTotalConsume() - currentThread.getProgress()) {
            queue.add(currentThread);
            currentThread.setReady();
            currentThread = queue.poll();;
        }
    }
}
