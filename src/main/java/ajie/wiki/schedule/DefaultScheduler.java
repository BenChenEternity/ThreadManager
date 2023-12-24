package ajie.wiki.schedule;

import ajie.wiki.CustomEvent;
import ajie.wiki.Main;
import ajie.wiki.thread.CustomThread;

import java.util.*;

import static ajie.wiki.CustomEvent.EventType.*;

public abstract class DefaultScheduler implements Scheduler {
    private ArrayList<CustomThread> threadList;
    protected CustomThread currentThread;
    private boolean completed;

    public DefaultScheduler() {
        this.threadList = new ArrayList<>();
        currentThread = null;
        completed = false;
    }

    @Override
    public void run() {
        System.out.println(Main.clockModule.getGlobalTime() + " - run() - 开始run()");
        if (Main.clockModule.getGlobalTime() == 0) {
            System.out.println(Main.clockModule.getGlobalTime() + " - run() - 时间是0，把线程都拿过来");
            Main.eventHandler.add(new CustomEvent(CALCULATE_TOTAL_CONSUME, null));//计算总开销
            Main.eventHandler.add(new CustomEvent(CustomEvent.EventType.CLOCK_START, null));
            for (CustomThread thread : Main.threadModule.threadsList()) {
                addThread(thread);
            }
            System.out.println(Main.clockModule.getGlobalTime() + " - run() - 总共拿过来线程个数：" + threadList.size());
        }
        if (completed) {
            System.out.println(Main.clockModule.getGlobalTime() + " - run() - 检查到completed，直接退出");
            return;
        }
        System.out.println(Main.clockModule.getGlobalTime() + " - run() - 开始检查到达的线程");
        checkStatus();
        if (currentThread == null) {
            if (hasThreadUncompleted()) {
                currentThread = getNextThread();
                if (currentThread == null) {
                    this.idleState();//没有不在阻塞的线程了，进入空闲状态
                    System.out.println(Main.clockModule.getGlobalTime() + " - run() - 没有非阻塞线程了");
                }
//                System.out.println(Main.clockModule.getGlobalTime() + " - run() - 当前线程：" + currentThread.getTID());
            } else if (threadList.isEmpty()) {
                this.setCompleted();
                System.out.println(Main.clockModule.getGlobalTime() + " - run() - 调度完成");
                //return;
            } else {
                this.idleState();//目前没有线程到达，进入空闲状态
                System.out.println(Main.clockModule.getGlobalTime() + " - run() - CPU空闲");
            }
        }
        if (currentThread != null) {//不要和上面的合并else 如果获得了线程就直接开始（不然会有1的线程切换开销）
            if (currentThread.getStatus() != CustomThread.ThreadStatus.BLOCKED) {
                if (currentThread.getStatus() == CustomThread.ThreadStatus.READY) {
                    System.out.println(Main.clockModule.getGlobalTime() + " - run() - " + currentThread.getTID() + "线程设为running");
                    currentThread.setRunning();
                }
                if (currentThread.getStatus() == CustomThread.ThreadStatus.RUNNING) {
                    System.out.println(Main.clockModule.getGlobalTime() + " - run() - " + currentThread.getTID() + "线程进度 +1");
                    this.addProgress(currentThread);
                    if (currentThread.getStatus() == CustomThread.ThreadStatus.TERMINATED) {//当前线程结束
                        System.out.println(Main.clockModule.getGlobalTime() + " - run() - " + currentThread.getTID() + "线程结束");
                        currentThread = null;
                    }
                }
            } else {
                System.out.println(Main.clockModule.getGlobalTime() + " - run() - " + currentThread.getTID() + "线程阻塞处理");
                blockedOperation();
            }
        }
        System.out.println(Main.clockModule.getGlobalTime() + "  - run() - 结束run");
    }

    protected void addProgress(CustomThread thread) {
        thread.addProgress(1);
    }
    abstract void blockedOperation();
    //currentThread.addSpinning();

    public abstract void addReadyThread(CustomThread thread);
    //threadQueue.add(thread);

    public abstract CustomThread getNextThread();
    //threadQueue.poll();

    public abstract boolean hasThreadUncompleted();
        //return !threadQueue.isEmpty();

    @Override
    public void checkStatus() {
        ArrayList<CustomThread> currentReadyThread = new ArrayList<>();
        for (CustomThread thread : threadList) {
            if (thread.getArrivalTime() <= Main.clockModule.getGlobalTime() && thread.getStatus() == CustomThread.ThreadStatus.NEW) {
                thread.setReady();
                addReadyThread(thread);
                currentReadyThread.add(thread);
            }
        }
        System.out.println(Main.clockModule.getGlobalTime() + " - run() - 检查完成，共发现 " + currentReadyThread.size() + " 个线程变为READY");
        this.threadList.removeAll(currentReadyThread);
    }

    @Override
    public void setCompleted() {
        this.completed = true;
        HashMap<String, Object> params = new HashMap<>();
        params.put("actualConsume", Main.clockModule.getGlobalTime());
        Main.eventHandler.add(new CustomEvent(SCHEDULER_DONE, params));
    }

    @Override
    public void addThread(CustomThread thread) {
        this.threadList.add(thread);
    }

    void idleState() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("time", Main.clockModule.getGlobalTime());
        Main.eventHandler.add(new CustomEvent(IDLE_STATE, params));
    }
}
