package xv2;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.awt.Toolkit;

public class Popups {

    public static void NoFilesSupported(){
        Toolkit.getDefaultToolkit().beep();
        Alert alert=new Alert(AlertType.INFORMATION);
        alert.setContentText("Files in directory are not supported");
        alert.showAndWait();
    }

    public static void SuccessSave(){
        Toolkit.getDefaultToolkit().beep();
        Alert alert=new Alert(AlertType.INFORMATION);
        alert.setContentText("File has been saved successfully");
        alert.showAndWait();
    }

    public static void NoSave(){
        Toolkit.getDefaultToolkit().beep();
        Alert alert=new Alert(AlertType.INFORMATION);
        alert.setContentText("Nothing has been saved");
        alert.showAndWait();
    }

    public static  void ErrorOutOfBounds(){
        Toolkit.getDefaultToolkit().beep();
        Alert alert=new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText("Can't remove entries");
        alert.showAndWait();
    }

    public static  void LegacyFormat(){
        Toolkit.getDefaultToolkit().beep();
        Alert alert=new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText("Bdm file format is a legacy format not supported by the tool");
        alert.showAndWait();
    }

    public static void ImageDataNotSupported (){
        Toolkit.getDefaultToolkit().beep();
        Alert alert=new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText("Image file type not supported");
        alert.showAndWait();
    }
}
