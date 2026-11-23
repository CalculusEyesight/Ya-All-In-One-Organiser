package xv2.bdm.ui;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import uicomponents.boxes.HBoxFactory;
import uicomponents.boxes.VBoxFactory;
import uicomponents.labels.LabelFactory;
import xv2.bdm.Bdm.BdmSubEntry;

public class Unknown implements LabelFactory, VBoxFactory, HBoxFactory {
    private BdmSubEntry bdmSubEntry;
    private TextField i02TF = createI02TF();
    private TextField i06TF = createI06TF();
    private TextField f08TF = createF08TF();
    private TextField i22TF = createI22TF();
    private TextField i30TF = createI30TF();
    private TextField i38TF = createI38TF();
    private TextField i58TF = createI58TF();
    private TextField i76TF = createI76TF();
    private TextField i82TF = createI82TF();
    private TextField i88TF = createI88TF();
    private TextField i90TF = createI90TF();
    private TextField i92TF = createI92TF();

    private HBox[] hBoxs = new HBox[] {
        createHBox(0, Pos.CENTER_LEFT, createLabel("I_02", 60), i02TF),
        createHBox(0, Pos.CENTER_LEFT, createLabel("I_06", 60), i06TF),
        createHBox(0, Pos.CENTER_LEFT, createLabel("F_08", 60), f08TF),
        createHBox(0, Pos.CENTER_LEFT, createLabel("I_22", 60), i22TF),
        createHBox(0, Pos.CENTER_LEFT, createLabel("I_30", 60), i30TF),
        createHBox(0, Pos.CENTER_LEFT, createLabel("I_58", 60), i58TF),
        createHBox(0, Pos.CENTER_LEFT, createLabel("I_76", 60), i76TF),
        createHBox(0, Pos.CENTER_LEFT, createLabel("I_82", 60), i82TF),
        createHBox(0, Pos.CENTER_LEFT, createLabel("I_88", 60), i88TF),
        createHBox(0, Pos.CENTER_LEFT, createLabel("I_90", 60), i90TF),
        createHBox(0, Pos.CENTER_LEFT, createLabel("I_92", 60), i92TF)
    };

    private TextField createI02TF() throws NumberFormatException {
        TextField textField = new TextField();
        textField.textProperty().addListener((obs, oldText, newText) -> {
            bdmSubEntry.setI02(Integer.parseInt(newText));
        });

        return textField;
    }

    private TextField createI06TF() throws NumberFormatException {
        TextField textField = new TextField();
        textField.textProperty().addListener((obs, oldText, newText) -> {
            bdmSubEntry.setI06(Integer.parseInt(newText));
        });

        return textField;
    }

    private TextField createF08TF() throws NumberFormatException {
        TextField textField = new TextField();
        textField.textProperty().addListener((obs, oldText, newText) -> {
            bdmSubEntry.setF08(Float.parseFloat(newText));
        });

        return textField;
    }

    private TextField createI22TF() throws NumberFormatException {
        TextField textField = new TextField();
        textField.textProperty().addListener((obs, oldText, newText) -> {
            bdmSubEntry.setI22(Integer.parseInt(newText));
        });

        return textField;
    }

    private TextField createI30TF() throws NumberFormatException {
        TextField textField = new TextField();
        textField.textProperty().addListener((obs, oldText, newText) -> {
            bdmSubEntry.setI30(Integer.parseInt(newText));
        });

        return textField;
    }

    private TextField createI38TF() throws NumberFormatException {
        TextField textField = new TextField();
        textField.textProperty().addListener((obs, oldText, newText) -> {
            bdmSubEntry.setI38(Integer.parseInt(newText));
        });

        return textField;
    }

    private TextField createI58TF() throws NumberFormatException {
        TextField textField = new TextField();
        textField.textProperty().addListener((obs, oldText, newText) -> {
            bdmSubEntry.setI58(Integer.parseInt(newText));
        });

        return textField;
    }

    private TextField createI76TF() throws NumberFormatException {
        TextField textField = new TextField();
        textField.textProperty().addListener((obs, oldText, newText) -> {
            bdmSubEntry.setI76(Integer.parseInt(newText));
        });

        return textField;
    }

    private TextField createI82TF() throws NumberFormatException {
        TextField textField = new TextField();
        textField.textProperty().addListener((obs, oldText, newText) -> {
            bdmSubEntry.setI82(Integer.parseInt(newText));
        });

        return textField;
    }

    private TextField createI88TF() throws NumberFormatException {
        TextField textField = new TextField();
        textField.textProperty().addListener((obs, oldText, newText) -> {
            bdmSubEntry.setI88(Integer.parseInt(newText));
        });

        return textField;
    }

    private TextField createI90TF() throws NumberFormatException {
        TextField textField = new TextField();
        textField.textProperty().addListener((obs, oldText, newText) -> {
            bdmSubEntry.setI90(Integer.parseInt(newText));
        });

        return textField;
    }

    private TextField createI92TF() throws NumberFormatException {
        TextField textField = new TextField();
        textField.textProperty().addListener((obs, oldText, newText) -> {
            bdmSubEntry.setI92(Integer.parseInt(newText));
        });

        return textField;
    }

    public void setBdmEntry(BdmSubEntry bdmSubEntry) {
        this.bdmSubEntry = bdmSubEntry;
        updateItems();
    }

    @Override
    public VBox createVBox() {
        return createVBox(32, new Insets(20, 0, 0, 8), hBoxs);
    }

    private void updateItems() {
        i02TF.setText(String.valueOf(bdmSubEntry.getI02()));
        i06TF.setText(String.valueOf(bdmSubEntry.getI06()));
        f08TF.setText(String.valueOf(bdmSubEntry.getF08()));
        i22TF.setText(String.valueOf(bdmSubEntry.getI22()));
        i30TF.setText(String.valueOf(bdmSubEntry.getI30()));
        i38TF.setText(String.valueOf(bdmSubEntry.getI38()));
        i58TF.setText(String.valueOf(bdmSubEntry.getI58()));
        i76TF.setText(String.valueOf(bdmSubEntry.getI76()));
        i82TF.setText(String.valueOf(bdmSubEntry.getI82()));
        i88TF.setText(String.valueOf(bdmSubEntry.getI88()));
        i90TF.setText(String.valueOf(bdmSubEntry.getI90()));
        i92TF.setText(String.valueOf(bdmSubEntry.getI92()));
    }
}