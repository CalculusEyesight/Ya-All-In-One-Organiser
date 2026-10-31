package xv2.AUR;
import UiComponents.Boxes.HBoxFactory;
import UiComponents.Boxes.VBoxFactory;
import UiComponents.Labels.LabelFactory;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import xv2.Popups;

public class AurCharaUi implements LabelFactory, VBoxFactory, HBoxFactory {
    protected VBox vBox = createVBox(10, new Insets(5));
    private AurChara aurChara;

    public AurCharaUi(AurChara aurChara) {
        this.aurChara = aurChara;
    }

    public AurChara getAurChara() {
        return this.aurChara;
    }

    private TextField charaIdTField(int i) {
        TextField textField = new TextField(String.valueOf(aurChara.aurCharaEntries.get(i).getCharaId()));
        textField.textProperty().addListener((obs, oldText, newText) -> {
            aurChara.aurCharaEntries.get(i).setCharaId(Integer.parseInt(newText));
        });

        return textField;
    }

    private TextField costumeTField(int i) {
        TextField textField = new TextField(String.valueOf(aurChara.aurCharaEntries.get(i).getCostume()));
        textField.textProperty().addListener((obs, oldText, newText) -> {
            aurChara.aurCharaEntries.get(i).setCostume(Integer.parseInt(newText));
        });

        return textField;
    }

    private TextField auraIdTField(int i) {
        TextField textField = new TextField(String.valueOf(aurChara.aurCharaEntries.get(i).getCharaAuraId()));
        textField.textProperty().addListener((obs, oldText, newText) -> {
            aurChara.aurCharaEntries.get(i).setAuraId(Integer.parseInt(newText));
        });

        return textField;
    }

    private CheckBox glareCB(int i) {
        CheckBox checkBox = new CheckBox();
        checkBox.setSelected(aurChara.aurCharaEntries.get(i).getGlare());
        checkBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
            aurChara.aurCharaEntries.get(i).setGlare(newValue);
        });

        return checkBox;
    }

    private Button insertEntry() {
        Button insertEntry = new Button("Insert Entry");

        insertEntry.setOnAction(event -> {
            aurChara.aurCharaEntries.add(new AurChara());
            addChildren(aurChara.aurCharaEntries.size() - 1);
        });

        return insertEntry;
    }

    private Button removeEntry() {
        Button removeEntry = new Button("Remove Entry");

        removeEntry.setOnAction(event -> {
            try {
                this.vBox.getChildren().remove(aurChara.aurCharaEntries.size() - 1);
                aurChara.aurCharaEntries.remove(aurChara.aurCharaEntries.getLast());
            } catch (IndexOutOfBoundsException e) {
                Popups.ErrorOutOfBounds();
            }
        });

        return removeEntry;
    }

    private ToolBar createToolBar() {
        return new ToolBar(insertEntry(), removeEntry());
    }

    @Override
    public VBox createVBox() {
        ScrollPane scrollPane = new ScrollPane(this.vBox);
        
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        scrollPane.getStyleClass().add("scroll-pane");
        return new VBox(createToolBar(), scrollPane);
    }

    public void addChildren(int i) {
        HBox hBox = createHBox(10, Pos.CENTER_LEFT);
        hBox.getChildren().addAll(
            createLabel("Chara ID", 0), charaIdTField(i),
            createLabel("Costume", 0), costumeTField(i),
            createLabel("Aura ID", 0), auraIdTField(i),
            createLabel("Glare", 0), glareCB(i)
        );

        this.vBox.getChildren().add(hBox);
    }
}