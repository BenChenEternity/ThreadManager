package ajie.wiki.clock;

import ajie.wiki.Main;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ClockModule {
    private final ArrayList<Timeable> timeables;
    private int globalTime;
    private volatile boolean pause;
    private ScheduledExecutorService scheduler;

    public ArrayList<Timeable> getTimeables() {
        return timeables;
    }

    public int getGlobalTime() {
        return globalTime;
    }

    public ClockModule() {
        this.globalTime = 0;
        this.timeables = new ArrayList<>();
        this.pause = true;
    }

    public void start() {
        this.scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {
            if (!pause) {
                cycle();
            }
        }, 0, 100, TimeUnit.MILLISECONDS);
    }

    public void cycle(){
        System.out.println(globalTime + " - 调度器容器大小:" + timeables.size());
        try {
            for (Timeable timeable : timeables) {
                System.out.println(globalTime + " - 调度器执行");
                timeable.run();
                System.out.println(globalTime + " - 调度器执行结束");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(timeables.size());
        }
        globalTime += 1;
        Main.uiModule.clockControlPanel.setTime();
    }

    public void stop() {
        pause = true; // 设置标志以停止新的任务
        scheduler.shutdown(); // 关闭调度器
        try {
            scheduler.awaitTermination(5, TimeUnit.SECONDS); // 等待任务结束最多5秒
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            scheduler.shutdownNow(); // 立即中断未完成的任务
        }
    }

    public void pause() {
        this.pause = true;
    }

    public void continueRunning() {
        this.pause = false;
    }

    public void addTimeable(Timeable timeable) {
        this.timeables.add(timeable);
    }
}

