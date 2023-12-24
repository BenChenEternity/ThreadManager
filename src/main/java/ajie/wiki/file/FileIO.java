package ajie.wiki.file;

import java.io.*;
import java.util.List;

public class FileIO {
    public static String FILE_READ_DEFAULT_PATH = null;
    public static String FILE_SAVE_PATH = null;

    public static void save(List<ThreadInfo> infoList, String path) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path))) {
            oos.writeObject(infoList);
            System.out.println("Thread information saved successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<ThreadInfo> read(String path) {
        List<ThreadInfo> infoList = null;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path))) {
            infoList = (List<ThreadInfo>) ois.readObject();
            System.out.println("Thread information loaded successfully.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return infoList;
    }
}
