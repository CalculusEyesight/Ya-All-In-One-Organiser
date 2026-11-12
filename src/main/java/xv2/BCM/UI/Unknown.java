package xv2.BCM.UI;
import UiComponents.Boxes.HBoxFactory;
import UiComponents.Boxes.VBoxFactory;
import UiComponents.Labels.LabelFactory;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import xv2.BCM.Bcm;

public class Unknown implements HBoxFactory, LabelFactory, VBoxFactory {
    private Bcm bcm;
    private TextField i36TF = createI36TF();
    private TextField i68TF = createI68TF();
    private TextField i72TF = createI72TF();
    private TextField i80TF = createI80TF();
    private TextField i88TF = createI88TF();
    private TextField i104TF = createI104TF();

    private HBox[] hBoxs = new HBox[] {
        createHBox(0, Pos.CENTER_LEFT, createLabel("I_36", 130), i36TF),
        createHBox(0, Pos.CENTER_LEFT, createLabel("I_68", 130), i68TF),
        createHBox(0, Pos.CENTER_LEFT, createLabel("I_72", 130), i72TF),
        createHBox(0, Pos.CENTER_LEFT, createLabel("I_80", 130), i80TF),
        createHBox(0, Pos.CENTER_LEFT, createLabel("I_88", 130), i88TF),
        createHBox(0, Pos.CENTER_LEFT, createLabel("Skill Upgrade Value?", 130), i104TF)
    };

    public void setBcmEntry(Bcm bcm) {
        this.bcm = bcm;
        updateItems();
    }

    private TextField createI36TF() throws NumberFormatException {
        TextField textField = new TextField();
        textField.textProperty().addListener((obs, oldText, newText) -> {
            bcm.setI36(Short.parseShort(newText));
        });

        return textField;
    }

    private TextField createI68TF() throws NumberFormatException {
        TextField textField = new TextField();
        textField.textProperty().addListener((obs, oldText, newText) -> {
            bcm.setI68(Long.parseLong(newText));
        });

        return textField;
    }

    private TextField createI72TF() throws NumberFormatException {
        TextField textField = new TextField();
        textField.textProperty().addListener((obs, oldText, newText) -> {
            bcm.setI72(Long.parseLong(newText));
        });

        return textField;
    }

    private TextField createI80TF() throws NumberFormatException {
        TextField textField = new TextField();
        textField.textProperty().addListener((obs, oldText, newText) -> {
            bcm.setI80(Long.parseLong(newText));
        });

        return textField;
    }

    private TextField createI88TF() throws NumberFormatException {
        TextField textField = new TextField();
        textField.textProperty().addListener((obs, oldText, newText) -> {
            bcm.setI88(Long.parseLong(newText));
        });

        return textField;
    }

    private TextField createI104TF() throws NumberFormatException {
        TextField textField = new TextField();
        textField.textProperty().addListener((obs, oldText, newText) -> {
            bcm.setI104(Long.parseLong(newText));
        });

        return textField;
    }

    public void updateItems() {
        i36TF.setText(String.valueOf(bcm.getI36()));
        i68TF.setText(String.valueOf(bcm.getI68()));
        i72TF.setText(String.valueOf(bcm.getI72()));
        i80TF.setText(String.valueOf(bcm.getI80()));
        i88TF.setText(String.valueOf(bcm.getI88()));
        i104TF.setText(String.valueOf(bcm.getI104()));
    }

    @Override
    public VBox createVBox() {
        return createVBox(30, new Insets(20, 0, 0, 8), hBoxs);
    }
}