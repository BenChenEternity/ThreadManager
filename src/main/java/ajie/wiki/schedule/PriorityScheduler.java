package ajie.wiki.schedule;

import ajie.wiki.Main;
import ajie.wiki.thread.CustomThread;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

public class PriorityScheduler extends DefaultScheduler{
    private final Queue<CustomThread> queue;

    public PriorityScheduler(){
        this.queue=new PriorityQueue<>(Comparator.comparingInt(CustomThread::getPriority).reversed());
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
}
