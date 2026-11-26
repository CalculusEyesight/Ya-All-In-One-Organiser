package xv2;
import java.io.File;
import javafx.scene.control.Tab;
import xv2.agd.AgdFile;
import xv2.aur.AurFile;
import xv2.bcm.BcmFile;
import xv2.bdm.BdmFile;
import xv2.bsa.BsaFile;
import xv2.cat.CatFile;
import xv2.emb.EmbFile;

public abstract class Xv2File extends File {
    public Xv2File(File file) {
        super(file.getName());
    }

    public abstract void loadFile(File file, Tab tab);
    public abstract void saveFile(File selectedDirectory, Tab tab);
    public static Xv2File getXv2File(File file) {
        return switch (getFileExtension(file.getName())) {
            case "agd" -> new AgdFile(file);
            case "aur" -> new AurFile(file);
            case "bcm" -> new BcmFile(file);
            case "bdm" -> new BdmFile(file);
            case "bsa" -> new BsaFile(file);
            case "cat" -> new CatFile(file);
            case "emb" -> new EmbFile(file);
            default -> new UnknownXv2File(file);
        };
    }

    public String getFileName() {
        return this.getName();
    }

    public boolean isXv2File() {
        return switch (getFileExtension(this.getName())) {
            case "agd", "aur", "bcm", "bdm", "bsa", "cat", "emb" -> true;
            default -> false;
        };
    }

    public static String getFileExtension(String filename) {
        if (filename == null) {
            return null;
        }

        int dotIndex = filename.lastIndexOf(".");

        if (dotIndex >= 0) {
            return filename.substring(dotIndex + 1);
        }

        return "";
    }
}