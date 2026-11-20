package xv2.BDM.UI;
import UiComponents.Boxes.HBoxFactory;
import UiComponents.Boxes.VBoxFactory;
import UiComponents.Buttons.RadioButtonFactory;
import UiComponents.Labels.LabelFactory;
import UiComponents.Panes.GridPaneFactory;
import UiComponents.Panes.StackPaneFactory;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import xv2.BDM.Bdm.BdmSubEntry;

public class Main implements VBoxFactory, StackPaneFactory, RadioButtonFactory, GridPaneFactory, LabelFactory, HBoxFactory {
    BdmSubEntry bdmSubEntry;
    ToggleGroup damageTypeTG = createDamageTypeTG();
    Spinner<Integer> damageAmountSP = createDamageAmountSP();
    Spinner<Integer> damageSpecialSP = createDamageSpecialSP();
    Spinner<Integer> damageSpecial2SP = createDamageSpecial2SP();
    Spinner<Integer> damageSpecial3SP = createDamageSpecial3SP();

    CheckBox[] healthProperties = new CheckBox[] {
        new CheckBox("Restore Health"),
        new CheckBox("Unknown 2"),
        new CheckBox("Unknown 3"),
        new CheckBox("Unknown 4"),
    };

    CheckBox[] unknown = new CheckBox[] {
        new CheckBox("Unknown 5"),
        new CheckBox("Unknown 6"),
        new CheckBox("Unknown 7"),
        new CheckBox("Unknown 8")
    };

    CheckBox[] damageProperties = new CheckBox[] {
        new CheckBox("Disable Evasive Usage"),
        new CheckBox("Unknown 10"),
        new CheckBox("Bypass Time Stop Damage"),
        new CheckBox("Bypass Super Armor")
    };

    CheckBox[] damageOrientation = new CheckBox[] {
        new CheckBox("Face Opponent Always"),
        new CheckBox("Unknown 14"),
        new CheckBox("Unknown 15"),
        new CheckBox("Unknown 16")
    };

    RadioButton[] damageTypes = new RadioButton[] {
        createRadioButton("No Effect", damageTypeTG),
        createRadioButton("Block", damageTypeTG),
        createRadioButton("Guard Break", damageTypeTG),
        createRadioButton("Standard", damageTypeTG),
        createRadioButton("Heavy", damageTypeTG),
        createRadioButton("Knockback", damageTypeTG),
        createRadioButton("Knockback 1", damageTypeTG),
        createRadioButton("Knockback 2", damageTypeTG),
        createRadioButton("Knockback 3", damageTypeTG),
        createRadioButton("Knockback 4", damageTypeTG),
        createRadioButton("Grab", damageTypeTG),
        createRadioButton("Hold Stomach", damageTypeTG),
        createRadioButton("Hold Eyes", damageTypeTG),
        createRadioButton("Knockback 5", damageTypeTG),
        createRadioButton("Electric", damageTypeTG),
        createRadioButton("Dazed", damageTypeTG),
        createRadioButton("Paralysis", damageTypeTG),
        createRadioButton("Freeze", damageTypeTG),
        createRadioButton("Wild-Card", damageTypeTG),
        createRadioButton("Unused", damageTypeTG),
        createRadioButton("Heavy Stamina Break", damageTypeTG),
        createRadioButton("Light Stamina Break", damageTypeTG),
        createRadioButton("Gigantic Ki Blast Push", damageTypeTG),
        createRadioButton("Brain Wash", damageTypeTG),
        createRadioButton("Gigantic Ki Blast Return", damageTypeTG),
        createRadioButton("Knockback 6", damageTypeTG),
        createRadioButton("Knockback 7", damageTypeTG),
        createRadioButton("Knockback 8", damageTypeTG),
        createRadioButton("Knockback 9", damageTypeTG),
        createRadioButton("Slow Opponent", damageTypeTG),
        createRadioButton("Brain Wash 2", damageTypeTG),
        createRadioButton("Time Stop", damageTypeTG),
    };

    StackPane[] secondaryType = new StackPane[] {
        createStackPane("Health Properties", createSecondaryTypeGroup(healthProperties, 1)),
        createStackPane("Unknown", createSecondaryTypeGroup(unknown, 16)),
        createStackPane("Damage Properties", createSecondaryTypeGroup(damageProperties, 256)),
        createStackPane("Damage Orientation", createSecondaryTypeGroup(damageOrientation, 4096)),
    };

    HBox[] hBoxs = new HBox[] {
        createHBox(0, Pos.CENTER_LEFT, createLabel("Damage Type", 160), createGridPane(4, 8, damageTypes, true)), 
        createHBox(0, Pos.CENTER_LEFT, createLabel("Secondary Type", 160), createHBox(5, Pos.CENTER_LEFT, secondaryType, false)),
        createHBox(0, Pos.CENTER_LEFT, createLabel("Damage Amount", 160), damageAmountSP), 
        createHBox(0, Pos.CENTER_LEFT, createLabel("Damage Special", 160), damageSpecialSP),
        createHBox(0, Pos.CENTER_LEFT, createLabel("Damage Special 2", 160), damageSpecial2SP), 
        createHBox(0, Pos.CENTER_LEFT, createLabel("Damage Special 3", 160), damageSpecial3SP)
    };

    private VBox createSecondaryTypeGroup(CheckBox[] checkBoxs, int bitMask) {
        VBox vBox = createVBox(2, new Insets(12, 0, 0, 0));
        vBox.getStyleClass().add("titled-address-box");

        for (int i = 0; i < checkBoxs.length; i++) {
            final int thisBitmask = bitMask;
            checkBoxs[i].selectedProperty().addListener((obs, oldValue, newValue) -> {
                if (newValue) {
                    bdmSubEntry.setSecondaryType(bdmSubEntry.getSecondaryType() | thisBitmask);
                }
                else {
                    bdmSubEntry.setSecondaryType(bdmSubEntry.getSecondaryType() & ~thisBitmask);
                }
            });

            vBox.getChildren().add(checkBoxs[i]);
            bitMask <<= 1;
        }

        return vBox;
    }

    private Spinner<Integer> createDamageAmountSP() {
        Spinner<Integer> spinner = new Spinner<>(0, 65535, 0);
        spinner.setEditable(true);
        spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            bdmSubEntry.setDamageAmount(newValue.intValue());
        });

        return spinner;
    }

    private Spinner<Integer> createDamageSpecialSP() {
        Spinner<Integer> spinner = new Spinner<>(0, 65535, 0);
        spinner.setEditable(true);
        spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            bdmSubEntry.setDamageSpecial(newValue.intValue());
        });

        return spinner;
    }

    private Spinner<Integer> createDamageSpecial2SP() {
        Spinner<Integer> spinner = new Spinner<>(0, 65535, 0);
        spinner.setEditable(true);
        spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            bdmSubEntry.setDamageSpecial2(newValue.intValue());
        });

        return spinner;
    }

    private Spinner<Integer> createDamageSpecial3SP() {
        Spinner<Integer> spinner = new Spinner<>(0, 65535, 0);
        spinner.setEditable(true);
        spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            bdmSubEntry.setDamageSpecial3(newValue.intValue());
        });

        return spinner;
    }

    private ToggleGroup createDamageTypeTG() {
        ToggleGroup toggleGroup = new ToggleGroup();
        toggleGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            int selectedIndex = toggleGroup.getToggles().indexOf(newToggle);
            bdmSubEntry.setDamageType(selectedIndex);
        });

        return toggleGroup;
    }

    public void setBdmEntry(BdmSubEntry bdmSubEntry) {
        this.bdmSubEntry = bdmSubEntry;
        updateItems();
    }

    private void updateItems() {
        damageAmountSP.getValueFactory().setValue(bdmSubEntry.getDamageAmount());
        damageSpecialSP.getValueFactory().setValue(bdmSubEntry.getDamageSpecial());
        damageSpecial2SP.getValueFactory().setValue(bdmSubEntry.getDamageSpecial2());
        damageSpecial3SP.getValueFactory().setValue(bdmSubEntry.getDamageSpecial3());
        damageTypeTG.selectToggle(damageTypeTG.getToggles().get(bdmSubEntry.getDamageType()));

        for (int i = 0; i < healthProperties.length; i++) {
            healthProperties[i].setSelected((bdmSubEntry.getSecondaryType() & (1 << i)) != 0);
        }

        for (int i = 0; i < unknown.length; i++) {
            unknown[i].setSelected((bdmSubEntry.getSecondaryType() & (16 << i)) != 0);
        }

        for (int i = 0; i < damageProperties.length; i++) {
            damageProperties[i].setSelected((bdmSubEntry.getSecondaryType() & (256 << i)) != 0);
        }

        for (int i = 0; i < damageOrientation.length; i++) {
            damageOrientation[i].setSelected((bdmSubEntry.getSecondaryType() & (4096 << i)) != 0);
        } 
    }

    @Override
    public VBox createVBox() {
        return createVBox(45, new Insets(20, 0, 0, 8), hBoxs);
    }
}