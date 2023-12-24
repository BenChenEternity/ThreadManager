package ajie.wiki.thread;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadIdPool {
    private final Set<Integer> allocatedIds;
    private final AtomicInteger nextId;

    public ThreadIdPool() {
        allocatedIds = new HashSet<>();
        nextId = new AtomicInteger(1);
    }

    public int get() {
        // 生成新的线程 ID
        int newId;
        do {
            newId = generateNextId();
        } while (!allocatedIds.add(newId)); // 如果ID已存在，则重新生成

        return newId;
    }

    public boolean remove(int threadId) {
        return allocatedIds.remove(threadId);
    }

    private int generateNextId() {
        return nextId.getAndIncrement();
    }

    public Set<Integer> getAllocatedIds() {
        return allocatedIds;
    }
    public Integer getRandomAllocatedId() {
        ArrayList<Integer> list = new ArrayList<>(this.allocatedIds);
        int randomIndex = new Random().nextInt(list.size());
        return list.get(randomIndex);
    }
}

