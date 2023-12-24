package ajie.wiki.schedule;

import ajie.wiki.Main;
import ajie.wiki.thread.CustomThread;

import java.util.*;

public class FCFSScheduler extends DefaultScheduler {
    private final Queue<CustomThread> threadQueue;

    public FCFSScheduler() {
        this.threadQueue = new LinkedList<>();
        Main.clockModule.addTimeable(this);
    }

    @Override
    void blockedOperation() {
        this.currentThread.addSpinning();
    }

    @Override
    public void addReadyThread(CustomThread thread) {
        threadQueue.add(thread);
    }

    @Override
    public CustomThread getNextThread() {
        return threadQueue.poll();
    }

    @Override
    public boolean hasThreadUncompleted() {
        return !threadQueue.isEmpty();
    }


}
