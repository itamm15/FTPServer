package FTPServer.file;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CustomFile {
    private File file;

    public CustomFile(String pathname) {
        this.file = new File(pathname);
    }

    public boolean exists() {
        return file.exists();
    }

    public boolean isDirectory() {
        return file.isDirectory();
    }

    public boolean isFile() {
        return file.isFile();
    }

    public String getName() {
        return file.getName();
    }

    public List<CustomFile> listFiles() {
        List<CustomFile> customFiles = new ArrayList<>();
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File f : files) {
                    customFiles.add(new CustomFile(f.getPath()));
                }
            }
        }
        return customFiles;
    }

    public boolean createNewFile() {
        try {
            return file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean mkdir() {
        return file.mkdir();
    }

    public boolean delete() {
        return file.delete();
    }
}
