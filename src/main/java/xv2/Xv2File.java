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
        switch (getFileExtension(file.getName())) {
            case "agd" -> {return new AgdFile(file);}
            case "aur" -> {return new AurFile(file);}
            case "bcm" -> {return new BcmFile(file);}
            case "bdm" -> {return new BdmFile(file);}
            case "bsa" -> {return new BsaFile(file);}
            case "cat" -> {return new CatFile(file);}
            case "emb" -> {return new EmbFile(file);}
            default -> {return new UnknownXv2File(file);}
        }
    }

    public String getFileName() {
        return this.getName();
    }

    public boolean isXv2File() {
        switch (getFileExtension(this.getName())) {
            case "agd" -> {return true;}
            case "aur" -> {return true;}
            case "bcm" -> {return true;}
            case "bdm" -> {return true;}
            case "bsa" -> {return true;}
            case "cat" -> {return true;}
            case "emb" -> {return true;}
            default -> {return false;}
        }
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