package xv2.BCM.UI;
import UiComponents.Boxes.HBoxFactory;
import UiComponents.Boxes.VBoxFactory;
import UiComponents.Buttons.RadioButtonFactory;
import UiComponents.Labels.LabelFactory;
import UiComponents.Panes.GridPaneFactory;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import xv2.BCM.Bcm;

public class BAC implements LabelFactory, HBoxFactory, VBoxFactory, RadioButtonFactory, GridPaneFactory {
    private Bcm bcm;
    private Spinner<Integer> bacEntryPrimarySP = createBACEntryPrimarySP();
    private Spinner<Integer> bacEntryChargeSP = createBACEntryChargeSP();
    private Spinner<Integer> bacEntryUserConnectSP = createBACEntryUserConnectSP();
    private Spinner<Integer> bacEntryVictimConnectSP = createBACEntryVictimConnectSP();
    private Spinner<Integer> bacEntryAirborneSP = createBACEntryAirborneSP();
    private Spinner<Integer> bacEntryTargetingOverrideSP = createBACEntryTargetingOverrideSP();
    private ToggleGroup randomFlagTG = createBACRandomFlagsTG();
    
    RadioButton[] randomFlags = new RadioButton[] {
        createRadioButton("None/Default", randomFlagTG),
        createRadioButton("Random BAC Entry", randomFlagTG),
        createRadioButton("No Target Correction", randomFlagTG),
        createRadioButton("3 Instance Setup", randomFlagTG),
        createRadioButton("Unknown 4", randomFlagTG),
        createRadioButton("Unknown 6", randomFlagTG)
    };

    private HBox[] hBoxs = new HBox[] {
        createHBox(0, Pos.CENTER_LEFT, createLabel("Primary BAC", 200), bacEntryPrimarySP),
        createHBox(0, Pos.CENTER_LEFT, createLabel("Charge BAC", 200), bacEntryChargeSP),
        createHBox(0, Pos.CENTER_LEFT, createLabel("User Connect BAC", 200), bacEntryUserConnectSP),
        createHBox(0, Pos.CENTER_LEFT, createLabel("Victim Connect BAC", 200), bacEntryVictimConnectSP),
        createHBox(0, Pos.CENTER_LEFT, createLabel("Airborne BAC", 200), bacEntryAirborneSP),
        createHBox(0, Pos.CENTER_LEFT, createLabel("Targeting Override BAC", 200), bacEntryTargetingOverrideSP),
        createHBox(0, Pos.CENTER_LEFT, createLabel("Random Flag", 200), createGridPane(2, 3, randomFlags, true))
    };

    public void setBcmEntry(Bcm bcm) {
        this.bcm = bcm;
        updateItems();
    }

    private Spinner<Integer> createBACEntryPrimarySP() {
        Spinner<Integer> spinner = new Spinner<>(Short.MIN_VALUE, Short.MAX_VALUE, 0);
        spinner.setEditable(true);
        spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            bcm.setBACEntryPrimary(newValue.shortValue());
        });

        return spinner;
    }

    private Spinner<Integer> createBACEntryChargeSP() {
        Spinner<Integer> spinner = new Spinner<>(Short.MIN_VALUE, Short.MAX_VALUE, 0);
        spinner.setEditable(true);
        spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            bcm.setBACEntryCharge(newValue.shortValue());
        });

        return spinner;
    }

    private Spinner<Integer> createBACEntryUserConnectSP() {
        Spinner<Integer> spinner = new Spinner<>(Short.MIN_VALUE, Short.MAX_VALUE, 0);
        spinner.setEditable(true);
        spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            bcm.setBACEntryUserConnect(newValue.shortValue());
        });

        return spinner;
    }

    private Spinner<Integer> createBACEntryVictimConnectSP() {
        Spinner<Integer> spinner = new Spinner<>(Short.MIN_VALUE, Short.MAX_VALUE, 0);
        spinner.setEditable(true);
        spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            bcm.setBACEntryVictimConnect(newValue.shortValue());
        });

        return spinner;
    }

    private Spinner<Integer> createBACEntryAirborneSP() {
        Spinner<Integer> spinner = new Spinner<>(Short.MIN_VALUE, Short.MAX_VALUE, 0);
        spinner.setEditable(true);
        spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            bcm.setBACEntryAirborne(newValue.shortValue());
        });

        return spinner;
    }

    private Spinner<Integer> createBACEntryTargetingOverrideSP() {
        Spinner<Integer> spinner = new Spinner<>(0, 65535, 0);
        spinner.setEditable(true);
        spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            bcm.setBACEntryTargetingOverride(newValue.intValue());
        });

        return spinner;
    }

    private ToggleGroup createBACRandomFlagsTG() {
        ToggleGroup toggleGroup = new ToggleGroup();
        toggleGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            int selectedIndex = toggleGroup.getToggles().indexOf(newToggle);
            bcm.setBACRandomFlags(selectedIndex == 5 ? 6 : selectedIndex);
        });

        return toggleGroup;
    }

    public void updateItems() {
        bacEntryPrimarySP.getValueFactory().setValue((int) bcm.getBACEntryPrimary());
        bacEntryChargeSP.getValueFactory().setValue((int) bcm.getBACEntryCharge());
        bacEntryUserConnectSP.getValueFactory().setValue((int) bcm.getBACEntryUserConnect());
        bacEntryVictimConnectSP.getValueFactory().setValue((int) bcm.getBACEntryVictimConnect());
        bacEntryAirborneSP.getValueFactory().setValue((int) bcm.getBACEntryAirborne());
        bacEntryTargetingOverrideSP.getValueFactory().setValue(bcm.getBACEntryTargetingOverride());
        randomFlagTG.selectToggle(bcm.getBACRandomFlags() == 6 ? randomFlagTG.getToggles().get(5) : randomFlagTG.getToggles().get(bcm.getBACRandomFlags()));
    }

    @Override
    public VBox createVBox() {
        return createVBox(30, new Insets(20, 0, 0, 8), hBoxs);
    }
}