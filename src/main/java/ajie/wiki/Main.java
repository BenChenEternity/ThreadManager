package ajie.wiki;

import ajie.wiki.file.FileIO;
import ajie.wiki.schedule.FCFSScheduler;
import ajie.wiki.schedule.Scheduler;
import ajie.wiki.thread.ThreadModule;
import ajie.wiki.UI.UIModule;
import ajie.wiki.clock.ClockModule;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;

public class Main {
    public static ClockModule clockModule;
    public static UIModule uiModule;
    public static EventHandler eventHandler;
    public static ThreadModule threadModule;
    public static Scheduler scheduler;

    public static void main(String[] args) {
        eventHandler = new EventHandler();
        Thread eventHandlerThread = new Thread(eventHandler::run);
        eventHandlerThread.start();

        init();
        clockModule.start();
    }

    public static void reset() {
        clockModule.stop();
        uiModule.getFrame().dispose();
        clockModule = new ClockModule();
        uiModule = new UIModule();
        threadModule.reset();
        scheduler = new FCFSScheduler();
        clockModule.start();
        FileIO.FILE_SAVE_PATH=null;
    }

    public static void reset(String tileName) {
        reset();
        uiModule.getFrame().setTitle(tileName);
    }

    public static void reset(int comboboxSelectedIndex) {
        reset();
        uiModule.getThreadControlBar().getSchedulerChoosePanel().getSchedulerComboBox().setSelectedIndex(comboboxSelectedIndex);
    }

    public static void init() {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            throw new RuntimeException(e);
        }
        loadModule();
    }

    private static void loadModule() {
        clockModule = new ClockModule();
        uiModule = new UIModule();
        threadModule = new ThreadModule();

        scheduler = new FCFSScheduler();

        //threadModule.create(10, 5, 20, 0, 6, 0, 20);
    }
}
