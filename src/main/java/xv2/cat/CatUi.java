package xv2.cat;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import uicomponents.boxes.HBoxFactory;
import uicomponents.boxes.VBoxFactory;
import uicomponents.labels.LabelFactory;
import uicomponents.menus.ContextMenuFactory;

public class CatUi implements ContextMenuFactory, LabelFactory, VBoxFactory, HBoxFactory {
    private Cat currentEntry;
    private Cat container;
    private int currentIndex;
    private TextField charaIdTF = createCharaIdTF();
    private TextField costumeTF = createCostumeTF();
    private TextField i04TF = createI04TF();
    private TextField skillId2TF = createSkillId2TF();
    private TextField charaCodeTF = createCharaCodeTF();
    private TextField i12TF = createI12TF();
    private TextField i16TF = createI16TF();
    private TextField i20TF = createI20TF();
    private TextField transformationEntryTF = createTransformationEntryTF();
    private TextField i22TF = createI22TF();
    public ListView<Cat> listView = createListView();

    private HBox[] hBoxs = new HBox[] {
        createHBox(0, Pos.CENTER_LEFT, createLabel("Chara ID", 120), this.charaIdTF),
        createHBox(0, Pos.CENTER_LEFT, createLabel("Costume", 120), this.costumeTF),
        createHBox(0, Pos.CENTER_LEFT, createLabel("I04", 120), this.i04TF),
        createHBox(0, Pos.CENTER_LEFT, createLabel("Skill ID 2", 120), this.skillId2TF),
        createHBox(0, Pos.CENTER_LEFT, createLabel("Chara Code", 120), this.charaCodeTF),
        createHBox(0, Pos.CENTER_LEFT, createLabel("I12", 120), this.i12TF),
        createHBox(0, Pos.CENTER_LEFT, createLabel("I16", 120), this.i16TF),
        createHBox(0, Pos.CENTER_LEFT, createLabel("I20", 120), this.i20TF),
        createHBox(0, Pos.CENTER_LEFT, createLabel("Tranformation Entry", 120), this.transformationEntryTF),
        createHBox(0, Pos.CENTER_LEFT, createLabel("I22", 120), this.i22TF)
    };

    public HBox createHBox() {
        return createHBox(10, new Node[]{listView, createVBox()});
    }

    private ListView<Cat> createListView() {
        ListView<Cat> listView = new ListView<>();
        keysListener(listView);

        listView.setContextMenu(getDefaultContextMenu());
        listView.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue == null) return;

            currentEntry = newValue;
            currentIndex = listView.getItems().indexOf(currentEntry);
            updateItems();
        });

        return listView;
    }

    private TextField createCharaIdTF() throws NumberFormatException {
        TextField textField = new TextField();
        textField.textProperty().addListener((obs, oldText, newText) -> {
            currentEntry.setCharaId(Integer.parseInt(newText));
        });

        return textField;
    }

    private TextField createCostumeTF() throws NumberFormatException {
        TextField textField = new TextField();
        textField.textProperty().addListener((obs, oldText, newText) -> {
            currentEntry.setCostume(Integer.parseInt(newText));
        });

        return textField;
    }

    private TextField createI04TF() throws NumberFormatException {
        TextField textField = new TextField();
        textField.textProperty().addListener((obs, oldText, newText) -> {
            currentEntry.setI04(Integer.parseInt(newText));
        });

        return textField;
    }

    private TextField createSkillId2TF() throws NumberFormatException {
        TextField textField = new TextField();
        textField.textProperty().addListener((obs, oldText, newText) -> {
            currentEntry.setSkillId2(Integer.parseInt(newText));
        });

        return textField;
    }

    private TextField createCharaCodeTF() throws NumberFormatException {
        TextField textField = new TextField();
        textField.textProperty().addListener((obs, oldText, newText) -> {
            currentEntry.setCharaCode(newText);
        });

        return textField;
    }

    private TextField createI12TF() throws NumberFormatException {
        TextField textField = new TextField();
        textField.textProperty().addListener((obs, oldText, newText) -> {
            currentEntry.setI12(Integer.parseInt(newText));
        });

        return textField;
    }

    private TextField createI16TF() throws NumberFormatException {
        TextField textField = new TextField();
        textField.textProperty().addListener((obs, oldText, newText) -> {
            currentEntry.setI16(Integer.parseInt(newText));
        });

        return textField;
    }

    private TextField createI20TF() throws NumberFormatException {
        TextField textField = new TextField();
        textField.textProperty().addListener((obs, oldText, newText) -> {
            currentEntry.setI20(Integer.parseInt(newText));
        });

        return textField;
    }

    private TextField createTransformationEntryTF() throws NumberFormatException {
        TextField textField = new TextField();
        textField.textProperty().addListener((obs, oldText, newText) -> {
            currentEntry.setTranformationEntry(Integer.parseInt(newText));
        });

        return textField;
    }

    private TextField createI22TF() throws NumberFormatException {
        TextField textField = new TextField();
        textField.textProperty().addListener((obs, oldText, newText) -> {
            currentEntry.setI22(Integer.parseInt(newText));
        });

        return textField;
    }

    @Override
    public void copy() {
        listView.getContextMenu().getItems().get(1).setDisable(false);
        container = new Cat(currentEntry);
    }

    @Override
    public void paste() {
        container.setEntry(currentEntry.getEntry());
        listView.getItems().set(currentIndex, new Cat(container));
        listView.refresh();
    }

    @Override
    public void delete() {
        if (listView.getItems().size() == 1) return;
        
        listView.getItems().remove(currentEntry);
        for (int i = 0; i < listView.getItems().size(); i++) {
            listView.getItems().get(i).setEntry("Entry " + i);
        }
    }

    @Override
    public void append() {
        listView.getItems().add((currentIndex + 1), new Cat());
        for (int i = 0; i < listView.getItems().size(); i++) {
            listView.getItems().get(i).setEntry("Entry " + i);
        }
        listView.refresh();
    }

    @Override
    public void insert() {
        listView.getItems().add(currentIndex, new Cat());
        for (int i = 0; i < listView.getItems().size(); i++) {
            listView.getItems().get(i).setEntry("Entry " + i);
        }
        listView.refresh();
    }

    private void updateItems() {
        charaIdTF.setText(String.valueOf(currentEntry.getCharaId()));
        costumeTF.setText(String.valueOf(currentEntry.getCostume()));
        i04TF.setText(String.valueOf(currentEntry.getI04()));
        skillId2TF.setText(String.valueOf(currentEntry.getSkillId2()));
        charaCodeTF.setText(currentEntry.getCharaCode());
        i12TF.setText(String.valueOf(currentEntry.getI12()));
        i16TF.setText(String.valueOf(currentEntry.getI16()));
        i20TF.setText(String.valueOf(currentEntry.getI20()));
        transformationEntryTF.setText(String.valueOf(currentEntry.getTranformationEntry()));
        i22TF.setText(String.valueOf(currentEntry.getI22()));
    }

    @Override
    public VBox createVBox() {
        return createVBox(50, new Insets(20, 0, 0, 0), this.hBoxs);
    }
}