package xv2;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.awt.Toolkit;

public class Popups {
    public static void NoFilesSupported() {
        Toolkit.getDefaultToolkit().beep();
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setContentText("Files in directory are not supported");
        alert.showAndWait();
    }

    public static void SuccessSave() {
        Toolkit.getDefaultToolkit().beep();
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setContentText("File has been saved successfully");
        alert.showAndWait();
    }

    public static void NoSave() {
        Toolkit.getDefaultToolkit().beep();
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setContentText("Nothing has been saved");
        alert.showAndWait();
    }

    public static  void ErrorOutOfBounds() {
        Toolkit.getDefaultToolkit().beep();
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText("Can't remove entries");
        alert.showAndWait();
    }

    public static  void LegacyFormat() {
        Toolkit.getDefaultToolkit().beep();
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText("Bdm file format is a legacy format not supported by the tool");
        alert.showAndWait();
    }

    public static void ImageDataNotSupported() {
        Toolkit.getDefaultToolkit().beep();
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText("Image file type not supported");
        alert.showAndWait();
    }

    public static void ImagesExtracted() {
        Toolkit.getDefaultToolkit().beep();
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setContentText("Files have been extracted successfully");
        alert.showAndWait();
    }

    public static void ItemNotFound() {
        Toolkit.getDefaultToolkit().beep();
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setContentText("Item Not Found");
        alert.showAndWait();
    }

    public static void AddComment(TreeItem<String> currentEntry) {
        if (currentEntry == null) return;

        TextInputDialog textInputDialog=new TextInputDialog();
        textInputDialog.setTitle("Comment");
        textInputDialog.getDialogPane().setContentText("New Comment: ");

        textInputDialog.showAndWait().ifPresent(updatedText -> {
            currentEntry.setValue(currentEntry.getValue()+" - "+ updatedText);
        });
    }

    public static VBox createFindDialog(String EntryType, Object[] indexList, ObservableList<String> entriesList) {
        Label auraEntryLabel = new Label(EntryType);
        auraEntryLabel.setPrefWidth(70);
        ComboBox<String> auraEntriesComboBox = new ComboBox<>(entriesList);
        auraEntriesComboBox.setPrefWidth(150);
        auraEntriesComboBox.getSelectionModel().select(0);
        auraEntriesComboBox.getSelectionModel().selectedIndexProperty().addListener((obs, oldValue, newValue) -> {
            indexList[0] = newValue.intValue();
        });
        HBox auraEntriesHBox = new HBox(auraEntryLabel, auraEntriesComboBox);
        auraEntriesHBox.setAlignment(Pos.CENTER_LEFT);

        Label findNextLabel = new Label("Find Next: ");
        findNextLabel.setPrefWidth(70);
        TextField findNextTextField = new TextField();
        findNextTextField.textProperty().addListener((obs, oldText, newText) -> {
            try {
                indexList[1] = newText;
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });
        HBox findNextHBox = new HBox(findNextLabel, findNextTextField);
        findNextHBox.setAlignment(Pos.CENTER_LEFT);

        return new VBox(8, auraEntriesHBox, findNextHBox);
    }
}
