package xv2.BCM.UI;
import UiComponents.Boxes.HBoxFactory;
import UiComponents.Boxes.VBoxFactory;
import UiComponents.Buttons.RadioButtonFactory;
import UiComponents.Labels.LabelFactory;
import UiComponents.Panes.GridPaneFactory;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import xv2.BCM.Bcm;

public class Miscellaneous implements VBoxFactory, HBoxFactory, LabelFactory, RadioButtonFactory, GridPaneFactory {
    private Bcm bcm;
    private ComboBox<String> movesCategoryCB = createMovesCategoryCB();
    private Spinner<Double> kiCostSP = createKiCostSP();
    private Spinner<Double> staminaCostSP = createStaminaCostSP();
    private Spinner<Double> kiRequiredSP = createKiRequiredSP();
    private Spinner<Double> healthRequiredSP = createHealthRequiredSP();
    private Spinner<Integer> transformationStageSP = createTransformationStageSP();
    private Spinner<Integer> CUSAuraSP = createCUSAuraSP();
    private ToggleGroup characterConditionTG = createCharacterConditionTG();

    RadioButton[] characterConditions = new RadioButton[] {
        createRadioButton("None/Default", characterConditionTG),
        createRadioButton("Custom Character (CAC)", characterConditionTG),
        createRadioButton("Human Male (HUM)", characterConditionTG),
        createRadioButton("Human Female (HUF)", characterConditionTG),
        createRadioButton("Saiyan Male (SYM)", characterConditionTG),
        createRadioButton("Saiyan Female (SYF)", characterConditionTG),
        createRadioButton("Namekian (NMC)", characterConditionTG),
        createRadioButton("Frieza Race (FRI)", characterConditionTG),
        createRadioButton("Majin Male (MAM)", characterConditionTG),
        createRadioButton("Majin Female (MAF)", characterConditionTG),
    };

    HBox[] hBoxs = new HBox[] {
        createHBox(0, Pos.CENTER_LEFT, createLabel("Ki Cost", 180), kiCostSP),
        createHBox(0, Pos.CENTER_LEFT, createLabel("Moves Category", 180), movesCategoryCB),
        createHBox(0, Pos.CENTER_LEFT, createLabel("Stamina Cost", 180), staminaCostSP),
        createHBox(0, Pos.CENTER_LEFT, createLabel("Ki Required", 180), kiRequiredSP),
        createHBox(0, Pos.CENTER_LEFT, createLabel("Health Required", 180), healthRequiredSP),
        createHBox(0, Pos.CENTER_LEFT, createLabel("Transformation Stage", 180), transformationStageSP),
        createHBox(0, Pos.CENTER_LEFT, createLabel("CUS Aura", 180), CUSAuraSP),
        createHBox(0, Pos.CENTER_LEFT, createLabel("Character Conditon", 180), createGridPane(2, 5, characterConditions, true))
    };

    private ComboBox<String> createMovesCategoryCB() {
        ComboBox<String> comboBox = new ComboBox<>(FXCollections.observableArrayList(
            "None",
            "Combos",
            "Supers",
            "Ultimate / Awoken / Evasive",
            "Z-Vanish",
            "Ki Blasts",
            "Jump",
            "Guard",
            "Flying / Step Dash"
        ));

        comboBox.getSelectionModel().selectedIndexProperty().addListener((obs, oldValue, newValue) -> {
           bcm.setMoveCategory(newValue.intValue() == 0 ? 0 : 1 << (newValue.intValue() - 1));
        });

        return comboBox;
    }

    private Spinner<Double> createKiCostSP() {
        Spinner<Double> spinner = new Spinner<>(0, 4294967295L, 0);
        spinner.setEditable(true);
        spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            bcm.setKiCost(newValue.longValue());
        });

        return spinner;
    }

    private Spinner<Double> createStaminaCostSP() {
        Spinner<Double> spinner = new Spinner<>(0, 4294967295L, 0);
        spinner.setEditable(true);
        spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            bcm.setStaminaCost(newValue.longValue());
        });

        return spinner;
    }

    private Spinner<Double> createKiRequiredSP() {
        Spinner<Double> spinner = new Spinner<>(0, 4294967295L, 0);
        spinner.setEditable(true);
        spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            bcm.setKiRequired(newValue.longValue());
        });

        return spinner;
    }

    private Spinner<Double> createHealthRequiredSP() {
        Spinner<Double> spinner = new Spinner<>(-Float.MAX_VALUE, Float.MAX_VALUE, 0);
        spinner.setEditable(true);
        spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            bcm.setHealthRequired(newValue.floatValue());
        });

        return spinner;
    }

    private Spinner<Integer> createTransformationStageSP() {
        Spinner<Integer> spinner = new Spinner<>(Short.MIN_VALUE, Short.MAX_VALUE, 0);
        spinner.setEditable(true);
        spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            bcm.setTranformationStage(newValue.shortValue());
        });

        return spinner;
    }

    private Spinner<Integer> createCUSAuraSP() {
        Spinner<Integer> spinner = new Spinner<>(Short.MIN_VALUE, Short.MAX_VALUE, 0);
        spinner.setEditable(true);
        spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            bcm.setCUSAura(newValue.shortValue());
        });

        return spinner;
    }

    private ToggleGroup createCharacterConditionTG() {
        ToggleGroup toggleGroup = new ToggleGroup();
        toggleGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            int selectedIndex = toggleGroup.getToggles().indexOf(newToggle);
            bcm.setCharacterCondition(selectedIndex);
        });

        return toggleGroup;
    }

    public void setBcmEntry(Bcm bcm) {
        this.bcm = bcm;
        updateItems();
    }

    private void updateItems() {
        kiCostSP.getValueFactory().setValue((double) bcm.getKiCost());
        movesCategoryCB.getSelectionModel().select((int) bcm.getMoveCategory() == 0 ? 0 : (Long.numberOfTrailingZeros(bcm.getMoveCategory()) + 1));
        staminaCostSP.getValueFactory().setValue((double) bcm.getStaminaCost());
        kiRequiredSP.getValueFactory().setValue((double) bcm.getKiRequired());
        healthRequiredSP.getValueFactory().setValue((double) bcm.getHealthRequired());
        transformationStageSP.getValueFactory().setValue((int) bcm.getTranformationStage());
        CUSAuraSP.getValueFactory().setValue((int) bcm.getCUSAura());
        characterConditionTG.selectToggle(characterConditionTG.getToggles().get((int) bcm.getCharacterCondition()));
    }

    @Override
    public VBox createVBox() {
        return createVBox(30, new Insets(20, 0, 0, 8), hBoxs);
    }
}