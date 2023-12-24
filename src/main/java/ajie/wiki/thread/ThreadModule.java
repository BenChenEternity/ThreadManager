package ajie.wiki.thread;

import ajie.wiki.CustomEvent;
import ajie.wiki.Main;
import ajie.wiki.file.ThreadInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class ThreadModule {
    private final List<ThreadInfo> threadInfos;
    private final HashMap<Integer, CustomThread> threadsMap;

    public ArrayList<CustomThread> threadsList() {
        ArrayList<CustomThread> list = new ArrayList<>();
        threadsMap.forEach((key, value) -> {
            list.add(value);
        });
        return list;
    }

    public void addThread(CustomThread thread) {
        this.threadsMap.put(thread.getTID(), thread);
        this.threadInfos.add(new ThreadInfo(thread));
        HashMap<String, Object> params = new HashMap<>();
        params.put("PID", thread.getPID());
        params.put("TID", thread.getTID());
        params.put("status", thread.getStatus());
        params.put("priority", thread.getPriority());
        params.put("idColor", thread.getIdColor());
        params.put("totalConsume", thread.getTotalConsume());
        params.put("arrivalTime", thread.getArrivalTime());
        Main.eventHandler.add(new CustomEvent(CustomEvent.EventType.THREAD_ADD, params));
    }

    public void modifyThread(int oldTid,int pid,int tid,int priority,int consume,int arrivalTime){
        HashMap<String, Object> params = new HashMap<>();
        for(ThreadInfo threadInfo:this.threadInfos){
            if(threadInfo.getTID()==oldTid){
                threadInfo.setPID(pid);
                threadInfo.setTID(tid);
                threadInfo.setPriority(priority);
                params.put("originalConsume",consume);
                threadInfo.setConsume(consume);
                threadInfo.setArrivalTime(arrivalTime);
                this.threadsMap.remove(oldTid);
                CustomThread newThread=new CustomThread(threadInfo);
                this.threadsMap.put(tid,newThread);
                params.put("OldTID", oldTid);
                params.put("PID", pid);
                params.put("TID", tid);
                params.put("priority", priority);
                params.put("totalConsume", consume);
                params.put("arrivalTime", arrivalTime);
                params.put("color", newThread.getIdColor());
                break;
            }
        }
        Main.eventHandler.add(new CustomEvent(CustomEvent.EventType.THREAD_MODIFY, params));
    }

    public void removeThread(int TID) {
        ThreadInfo tInfo = null;
        for (ThreadInfo threadInfo : threadInfos) {
            if (threadInfo.getTID() == TID) {
                tInfo = threadInfo;
            }
        }
        threadInfos.remove(tInfo);
        CustomThread thread = threadsMap.get(TID);
        HashMap<String, Object> params = new HashMap<>();
        params.put("tid", TID);
        params.put("consume", thread.getTotalConsume());
        this.threadsMap.remove(TID);
        Main.eventHandler.add(new CustomEvent(CustomEvent.EventType.THREAD_REMOVE, params));
    }

    public List<ThreadInfo> getThreadInfos() {
        return threadInfos;
    }

    public ThreadModule() {
        threadsMap = new HashMap<>();
        threadInfos = new ArrayList<>();
    }

    public void create(int count, int totalConsumeBase, int totalConsumeOffset, int priorityBase, int priorityOffset, int arrivalTimeBase, int arrivalTimeOffset) {
        Random r = new Random();
        for (int i = 0; i < count; i++) {
            CustomThread t = new CustomThread(
                    r.nextInt(priorityOffset + 1) + priorityBase,
                    r.nextInt(totalConsumeOffset + 1) + totalConsumeBase,
                    r.nextInt(arrivalTimeOffset + 1) + arrivalTimeBase
            );
            addThread(t);
        }
    }

    public HashMap<Integer, CustomThread> getThreadsMap() {
        return threadsMap;
    }

    private void loadInfo() {
        ArrayList<ThreadInfo> threadInfoTemp = new ArrayList<>(threadInfos);
        threadInfos.clear();
        for (ThreadInfo threadInfo : threadInfoTemp) {
            addThread(new CustomThread(threadInfo));
        }
    }

    public void reset() {
        threadsMap.clear();
        this.loadInfo();
    }

    public void createSRTN() {
        addThread(new CustomThread(0, 12, 0));//
        addThread(new CustomThread(0, 9, 1));
        addThread(new CustomThread(0, 6, 3));
        addThread(new CustomThread(0, 3, 5));
    }

    public void createPriority() {
        addThread(new CustomThread(1, 7, 0));
        addThread(new CustomThread(2, 4, 2));
        addThread(new CustomThread(3, 1, 4));
        addThread(new CustomThread(2, 4, 5));
    }

    public void createPPriority() {
        addThread(new CustomThread(1, 7, 0));
        addThread(new CustomThread(2, 4, 2));
        addThread(new CustomThread(3, 1, 4));
        addThread(new CustomThread(2, 4, 5));
    }

    public void createSJF() {
        addThread(new CustomThread(1, 3, 0));
        addThread(new CustomThread(2, 5, 1));
        addThread(new CustomThread(3, 2, 2));
        addThread(new CustomThread(4, 4, 3));
        addThread(new CustomThread(5, 6, 4));
        addThread(new CustomThread(6, 2, 5));
        addThread(new CustomThread(7, 1, 6));
        addThread(new CustomThread(8, 4, 7));
        addThread(new CustomThread(9, 3, 8));
        addThread(new CustomThread(10, 5, 9));
    }

    public void createRR() {
        addThread(new CustomThread(0, 4, 0));
        addThread(new CustomThread(0, 3, 1));
        addThread(new CustomThread(0, 5, 2));
        addThread(new CustomThread(0, 2, 3));
        addThread(new CustomThread(0, 4, 4));
    }

    public void createFCFS() {
        addThread(new CustomThread(1, 3, 0));
        addThread(new CustomThread(2, 5, 2));
        addThread(new CustomThread(3, 2, 1));
        addThread(new CustomThread(4, 4, 5));
        addThread(new CustomThread(5, 6, 3));
        addThread(new CustomThread(6, 2, 7));
        addThread(new CustomThread(7, 1, 4));
        addThread(new CustomThread(8, 4, 8));
        addThread(new CustomThread(9, 3, 6));
        addThread(new CustomThread(10, 5, 9));
    }
}
