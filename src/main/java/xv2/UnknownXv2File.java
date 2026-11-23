package xv2;
import java.io.File;
import javafx.scene.control.Tab;

class UnknownXv2File extends Xv2File {
    public UnknownXv2File(File file) {
        super(file);
        IO.println("Unsupported file type " + file.getName());
    }

    @Override
    public void loadFile(File file, Tab tab) {}

    @Override
    public void saveFile(File selectedDirectory, Tab tab) {}
}