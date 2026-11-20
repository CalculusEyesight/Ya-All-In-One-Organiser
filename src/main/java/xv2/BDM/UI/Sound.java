package xv2.BDM.UI;
import UiComponents.Boxes.HBoxFactory;
import UiComponents.Boxes.VBoxFactory;
import UiComponents.Buttons.RadioButtonFactory;
import UiComponents.Labels.LabelFactory;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import xv2.BDM.Bdm.BdmSubEntry;

public class Sound implements VBoxFactory, HBoxFactory, LabelFactory, RadioButtonFactory {
    BdmSubEntry bdmSubEntry;
    ToggleGroup acbTypeTG = createAcbTypeTG();
    Spinner<Integer> cueIdSP = createCueIdSP();

    private RadioButton[] acbTypes = new RadioButton[] {
        createRadioButton("Common", acbTypeTG), 
        createRadioButton("Character SE", acbTypeTG),
        createRadioButton("Character VOX", acbTypeTG),
        createRadioButton("Skill SE", acbTypeTG),
        createRadioButton("Skill VOX", acbTypeTG)
    };

    private HBox[] hBoxs = new HBox[] {
        createHBox(0, Pos.CENTER_LEFT, createLabel("ACB Type", 100), createHBox(15, Pos.CENTER_LEFT, acbTypes, true)), 
        createHBox(0, Pos.CENTER_LEFT, createLabel("CUE ID", 100), cueIdSP)
    };

    private ToggleGroup createAcbTypeTG() {
        ToggleGroup toggleGroup = new ToggleGroup();
        toggleGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            int selectedIndex = toggleGroup.getToggles().indexOf(newToggle);
            switch (selectedIndex) {
                case 0 -> bdmSubEntry.setAcbType(0);
                case 1 -> bdmSubEntry.setAcbType(2);
                case 2 -> bdmSubEntry.setAcbType(3);
                case 3 -> bdmSubEntry.setAcbType(10);
                case 4 -> bdmSubEntry.setAcbType(11);
            }
        });

        return toggleGroup;
    }

    private Spinner<Integer> createCueIdSP() {
        Spinner<Integer> spinner = new Spinner<>(Short.MIN_VALUE, Short.MAX_VALUE, 0);
        spinner.setEditable(true);
        spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            bdmSubEntry.setCueId(newValue.shortValue());
        });

        return spinner;
    }

    private Toggle acbToggleSelection() {
        switch (bdmSubEntry.getAcbType()) {
            case 0 -> {return acbTypeTG.getToggles().get(0);}
            case 2 -> {return acbTypeTG.getToggles().get(1);}
            case 3 -> {return acbTypeTG.getToggles().get(2);}
            case 10 -> {return acbTypeTG.getToggles().get(3);}
            case 11 -> {return acbTypeTG.getToggles().get(4);}
            default -> {return acbTypeTG.getToggles().get(0); }
        }
    }

    public void setBdmEntry(BdmSubEntry bdmSubEntry) {
        this.bdmSubEntry = bdmSubEntry;
        updateItems();
    }

    private void updateItems() {
        acbTypeTG.selectToggle(acbToggleSelection());
        cueIdSP.getValueFactory().setValue((int) bdmSubEntry.getCueId());
    }
        
    @Override
    public VBox createVBox() {
        return createVBox(30, new Insets(20, 0, 0, 8), hBoxs);
    }
}