package ajie.wiki.schedule;

import ajie.wiki.CustomEvent;
import ajie.wiki.Main;
import ajie.wiki.clock.Timeable;
import ajie.wiki.thread.CustomThread;

import java.util.HashMap;

import static ajie.wiki.CustomEvent.EventType.IDLE_STATE;

public interface Scheduler extends Timeable {
    void addThread(CustomThread thread);

    void checkStatus();

    void setCompleted();
}
