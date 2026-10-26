package xv2.AUR;
import UiComponents.Menus.ContextMenuFactory;
import UiComponents.Panes.SplitPaneFactory;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class AurAuraUi extends AurCharaUi implements SplitPaneFactory, ContextMenuFactory {
    private AurAura currentEntry;
    private AurAura container;

    protected ListView<AurAura> listView = createListView();
    private TextField i04TF = createI04TF();
    private TextField boostStartTF = createBoostStartTF();
    private TextField boostLoopTF = createBoostLoopTF();
    private TextField boostEndTF = createBoostEndTF();
    private TextField kiaiChargeTF = createKiaiChargeTF();
    private TextField kiryokuMaxTF = createKiryokuMaxTF();
    private TextField henshinStartTF = createHenshinStartTF();
    private TextField henshinEndTF = createHenshinEndTF();

    private HBox[] hBoxs = new HBox[] {
        createHBox(0, Pos.CENTER_LEFT, createLabel("I04", 120), this.i04TF),
        createHBox(0, Pos.CENTER_LEFT, createLabel("Boost Start", 120), this.boostStartTF),
        createHBox(0, Pos.CENTER_LEFT, createLabel("Boost Loop", 120), this.boostLoopTF),
        createHBox(0, Pos.CENTER_LEFT, createLabel("Boost End", 120), this.boostEndTF),
        createHBox(0, Pos.CENTER_LEFT, createLabel("Kiai Charge", 120), this.kiaiChargeTF),
        createHBox(0, Pos.CENTER_LEFT, createLabel("Kiryoku Max", 120), this.kiryokuMaxTF),
        createHBox(0, Pos.CENTER_LEFT, createLabel("Henshin Start", 120), this.henshinStartTF),
        createHBox(0, Pos.CENTER_LEFT, createLabel("Henshin End", 120), this.henshinEndTF)
    };

    private VBox vBox = createVBox(70, new Insets(20, 0, 0, 0), this.hBoxs);
    private HBox hBox = createHBox(10, new Node[]{listView, this.vBox});
    
    @Override 
    public SplitPane createSplitPane() {
       SplitPane splitPane = new SplitPane(this.hBox, super.createVBox());
       splitPane.setDividerPositions(0.43);
       splitPane.getStyleClass().add("vbox");

       return splitPane;
    }

    public ListView<AurAura> createListView() {
        ListView<AurAura> listView = new ListView<>();
        keysListener(listView);

        listView.setContextMenu(getDefaultContextMenu());
        listView.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue == null) return;

            currentEntry = newValue;
            updateItems();
        });

        return listView;
    }

    private TextField createI04TF() throws NumberFormatException {
        TextField textField = new TextField();
        textField.textProperty().addListener((obs, oldText, newText) -> {
            currentEntry.setI04(Integer.parseInt(newText));
        });

        return textField;
    }

    private TextField createBoostStartTF() throws NumberFormatException {
        TextField textField = new TextField();
        textField.textProperty().addListener((obs, oldText, newText) -> {
            currentEntry.setBoostStart(Integer.parseInt(newText));
        });

        return textField;
    }

    private TextField createBoostLoopTF() throws NumberFormatException {
        TextField textField = new TextField();
        textField.textProperty().addListener((obs, oldText, newText) -> {
           currentEntry.setBoostLoop(Integer.parseInt(newText));
        });

        return textField;
    }

    private TextField createBoostEndTF() throws NumberFormatException {
        TextField textField = new TextField();
        textField.textProperty().addListener((obs, oldText, newText) -> {
            currentEntry.setBoostEnd(Integer.parseInt(newText));
        });

        return textField;
    }

    private TextField createKiaiChargeTF() throws NumberFormatException {
        TextField textField = new TextField();
        textField.textProperty().addListener((obs, oldText, newText) -> {
            currentEntry.setKiaiCharge(Integer.parseInt(newText));
        });

        return textField;
    }

    private TextField createKiryokuMaxTF() throws NumberFormatException {
        TextField textField = new TextField();
        textField.textProperty().addListener((obs, oldText, newText) -> {
            currentEntry.setKiryokuMax(Integer.parseInt(newText));
        });

        return textField;
    }

    private TextField createHenshinStartTF() throws NumberFormatException {
        TextField textField = new TextField();
        textField.textProperty().addListener((obs, oldText, newText) -> {
            currentEntry.setHenshinStart(Integer.parseInt(newText));
        });

        return textField;
    }

    private TextField createHenshinEndTF() throws NumberFormatException {
        TextField textField = new TextField();
        textField.textProperty().addListener((obs, oldText, newText) -> {
            currentEntry.setHenshinEnd(Integer.parseInt(newText));
        });

        return textField;
    }

    @Override
    public void copy() {
        listView.getContextMenu().getItems().get(1).setDisable(false);
        container = new AurAura(currentEntry);
    }

    @Override
    public void paste() {
        container.setAuraId(currentEntry.getAuraId());
        listView.getItems().set(listView.getItems().indexOf(currentEntry), new AurAura(container));
        listView.refresh();
    }

    @Override
    public void delete() {
        listView.getItems().remove(currentEntry);

        for (int i = 0; i < listView.getItems().size(); i++) {
            listView.getItems().get(i).setAuraId("Aura ID " + i);
        }
    }

    @Override
    public void append() {
        listView.getItems().add((listView.getItems().indexOf(currentEntry) + 1), new AurAura());
        for (int i = 0; i < listView.getItems().size(); i++) {
            listView.getItems().get(i).setAuraId("Aura ID " + i);
        }
        listView.refresh();
    }

    @Override
    public void insert() {
        listView.getItems().add(listView.getItems().indexOf(currentEntry), new AurAura());
        for (int i = 0; i < listView.getItems().size(); i++) {
            listView.getItems().get(i).setAuraId("Aura ID " + i);
        }
        listView.refresh();
    }

    private void updateItems() {
        i04TF.setText(String.valueOf(currentEntry.getI04()));
        boostStartTF.setText(String.valueOf(currentEntry.getBoostStart()));
        boostLoopTF.setText(String.valueOf(currentEntry.getBoostLoop()));
        boostEndTF.setText(String.valueOf(currentEntry.getBoostEnd()));
        kiaiChargeTF.setText(String.valueOf(currentEntry.getKiaiCharge()));
        kiryokuMaxTF.setText(String.valueOf(currentEntry.getKiryokuMax()));
        henshinStartTF.setText(String.valueOf(currentEntry.getHenshinStart()));
        henshinEndTF.setText(String.valueOf(currentEntry.getHenshinEnd()));
    }
}