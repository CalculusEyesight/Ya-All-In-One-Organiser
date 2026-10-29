package xv2.AGD;
import UiComponents.Boxes.HBoxFactory;
import UiComponents.Boxes.VBoxFactory;
import UiComponents.Labels.LabelFactory;
import UiComponents.Panes.ScrollPaneFactory;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import xv2.Popups;

public class AgdUi extends AgdWriter implements LabelFactory, VBoxFactory, HBoxFactory {
    private VBox vBox = createVBox(10, new Insets(20, 0, 20, 60));

    private TextField levelTField(int i) {
        TextField textField = new TextField(String.valueOf(agdEntries.get(i).getLevel()));
        textField.textProperty().addListener((obs, oldText, newText) -> {
            agdEntries.get(i).setLevel(Integer.parseInt(newText));
        });

        return textField;
    }

    private TextField xpToNextLevelTField(int i) {
        TextField textField = new TextField(String.valueOf(agdEntries.get(i).getXpToNextLevel()));
        textField.textProperty().addListener((obs, oldText, newText) -> {
            agdEntries.get(i).setXpToNextLevel(Integer.parseInt(newText));
        });

        return textField;
    }

    private TextField xpToThisLevelTField(int i) {
        TextField textField = new TextField(String.valueOf(agdEntries.get(i).getXpToThisLevel()));
        textField.textProperty().addListener((obs, oldText, newText) -> {
            agdEntries.get(i).setXpToThisLevel(Integer.parseInt(newText));
        });

        return textField;
    }

    private TextField attributePointsGainedTField(int i) {
        TextField textField = new TextField(String.valueOf(agdEntries.get(i).getAttributePointsGained()));
        textField.textProperty().addListener((obs, oldText, newText) -> {
            agdEntries.get(i).setAttributePointsGained(Integer.parseInt(newText));
        });

        return textField;
    }

    private Button insertEntry() {
        Button insertEntry = new Button("Insert Entry");

        insertEntry.setOnAction(event -> {
            agdEntries.add(new Agd());
            addChildren(agdEntries.size() - 1);
        });

        return insertEntry;
    }

    private Button removeEntry() {
        Button removeEntry = new Button("Remove Entry");

        removeEntry.setOnAction(event -> {
            try {
                this.vBox.getChildren().remove(agdEntries.size() - 1);
                agdEntries.remove(agdEntries.getLast());
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
        HBox hBox = createHBox(30, Pos.CENTER_LEFT);
        hBox.getChildren().addAll(
            createLabel("Level", 0), levelTField(i),
            createLabel("Xp To Next Level", 0), xpToNextLevelTField(i),
            createLabel("Xp To This Level", 0), xpToThisLevelTField(i),
            createLabel("Attribute Points Gained", 0), attributePointsGainedTField(i)
        );

        this.vBox.getChildren().add(hBox);
    }
}