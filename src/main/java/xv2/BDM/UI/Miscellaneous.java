package xv2.BDM.UI;
import UiComponents.Boxes.HBoxFactory;
import UiComponents.Boxes.VBoxFactory;
import UiComponents.Labels.LabelFactory;
import UiComponents.Panes.StackPaneFactory;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import xv2.BDM.Bdm.BdmSubEntry;

public class Miscellaneous implements HBoxFactory, LabelFactory, VBoxFactory, StackPaneFactory {
    BdmSubEntry bdmSubEntry;
    Label label = new Label();
    Spinner<Integer> staminaBrokenOverrideBdmIdSP = createStaminaBrokenOverrideBdmIdSP();
    Spinner<Integer> zVanishEnableTimeSP = createZVanishEnableTimeSP();
    Spinner<Integer> transformationTypeSP = createTransformationTypeSP();

    CheckBox[] ailmentGroup1 = new CheckBox[] {
        new CheckBox("Unknown1"),
        new CheckBox("HP/DEF"),
        new CheckBox("SPD"),
        new CheckBox("Target")
    };

    CheckBox[] ailmentGroup2 = new CheckBox[] {
        new CheckBox("Seal Awoken Skill"),
        new CheckBox("Unknown 6"),
        new CheckBox("Unknown 7"),
        new CheckBox("Unknown 8")
    };

    CheckBox[] stumbleGroup1 = new CheckBox[] {
        new CheckBox("Stumble Set 1"),
        new CheckBox("Stumble Set 2"),
        new CheckBox("Stumble Set 3"),
        new CheckBox("Stumble Set 4")
    };

    CheckBox[] stumbleGroup2 = new CheckBox[] {
        new CheckBox("Stumble Set 5"),
        new CheckBox("Stumble Set 6"),
        new CheckBox("All Stumble Sets"),
        new CheckBox("Unknown 8")
    };

    CheckBox[] stumbleGroup3 = new CheckBox[] {
        new CheckBox("Unknown 9"),
        new CheckBox("Unknown 10"),
        new CheckBox("Unknown 11"),
        new CheckBox("Unknown 12"),
    };

    CheckBox[] stumbleGroup4 = new CheckBox[] {
        new CheckBox("Unknown 13"),
        new CheckBox("Unknown 14"),
        new CheckBox("Unknown 15"),
        new CheckBox("Unknown 16"),
    };

    StackPane[] ailmentTypes = new StackPane[] {
        createStackPane("Properties #1", createAilmentTypeGroup(ailmentGroup1, (short) 1)),
        createStackPane("Properties #2", createAilmentTypeGroup(ailmentGroup2, (short) 16)),
    };

    StackPane[] stumbleTypes = new StackPane[] {
        createStackPane("Stumble Group 1", createStumbleTypeGroup(stumbleGroup1, 1)),
        createStackPane("Stumble Group 2", createStumbleTypeGroup(stumbleGroup2, 6)),
        createStackPane("Stumble Group 3", createStumbleTypeGroup(stumbleGroup3, 256)),
        createStackPane("Stumble Group 4", createStumbleTypeGroup(stumbleGroup4, 4096)),
    };
    
    HBox[] hBoxs = new HBox[] {
        createHBox(0, Pos.CENTER_LEFT, createLabel("Transformation Type", 200), createHBox(15, Pos.CENTER_LEFT, new Node[] {transformationTypeSP, label}, false)), 
        createHBox(0, Pos.CENTER_LEFT, createLabel("Ailment Type", 200), createHBox(5, Pos.CENTER_LEFT, ailmentTypes, false)), 
        createHBox(0, Pos.CENTER_LEFT, createLabel("Stumble Type", 200), createHBox(5, Pos.CENTER_LEFT, stumbleTypes, false)), 
        createHBox(0, Pos.CENTER_LEFT, createLabel("Stamina Broken Override BDM ID", 200), staminaBrokenOverrideBdmIdSP), 
        createHBox(0, Pos.CENTER_LEFT, createLabel("Z Vanish Enable Time", 200), zVanishEnableTimeSP)
    };

    private Spinner<Integer> createStaminaBrokenOverrideBdmIdSP() {
        Spinner<Integer> spinner = new Spinner<>(Short.MIN_VALUE, Short.MAX_VALUE, 0);
        spinner.setEditable(true);
        spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            bdmSubEntry.setStaminaBrokenOverrideBdmId(newValue.shortValue());
        });

        return spinner;
    }

    private Spinner<Integer> createZVanishEnableTimeSP() {
        Spinner<Integer> spinner = new Spinner<>(0, 65535, 0);
        spinner.setEditable(true);
        spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            bdmSubEntry.setZVanishEnableTime(newValue.intValue());
        });

        return spinner;
    }

    private VBox createAilmentTypeGroup(CheckBox[] checkBoxs, short bitMask) {
        VBox vBox = createVBox(2, new Insets(12, 0, 0, 0));
        vBox.getStyleClass().add("titled-address-box");

        for (int i = 0; i < checkBoxs.length; i++) {
            final short thisBitmask = bitMask;
            checkBoxs[i].selectedProperty().addListener((obs, oldValue, newValue) -> {
                if (newValue) {
                    bdmSubEntry.setAilmentType((short) (bdmSubEntry.getAilmentType() | thisBitmask));
                }
                else {
                    bdmSubEntry.setAilmentType((short) (bdmSubEntry.getAilmentType() & ~thisBitmask));
                }
            });

            vBox.getChildren().add(checkBoxs[i]);
            bitMask <<= 1;
        }

        return vBox;
    }

    private VBox createStumbleTypeGroup(CheckBox[] checkBoxs, int bitMask) {
        VBox vBox = createVBox(2, new Insets(12, 0, 0, 0));
        vBox.getStyleClass().add("titled-address-box");

        for (int i = 0; i < checkBoxs.length; i++) {
            final int thisBitmask = bitMask;
            checkBoxs[i].selectedProperty().addListener((obs, oldValue, newValue) -> {
                if (newValue) {
                    bdmSubEntry.setStumbleType(bdmSubEntry.getStumbleType() | thisBitmask);
                }
                else {
                    bdmSubEntry.setStumbleType(bdmSubEntry.getStumbleType() & ~thisBitmask);
                }
            });

            vBox.getChildren().add(checkBoxs[i]);
            bitMask <<= 1;
        }

        return vBox;
    }

    private Spinner<Integer> createTransformationTypeSP() {
        Spinner<Integer> spinner = new Spinner<>(0, 65535, 0);
        spinner.setEditable(true);
        spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            bdmSubEntry.setTransformationType(newValue.intValue());
        });

        label.getStyleClass().add("custom-label");
        label.textProperty().bind(
            Bindings.createStringBinding(() -> {
                return switch (spinner.getValue().intValue()) {
                    case 0 -> "None";
                    case 1 -> "Candy";
                    default -> "Unknown";
                };
            }, spinner.valueProperty())
        );

        return spinner;
    }

    @Override
    public VBox createVBox() {
        return createVBox(35, new Insets(20, 0, 0, 8), hBoxs);
    }

    public void setBdmEntry(BdmSubEntry bdmSubEntry) {
        this.bdmSubEntry = bdmSubEntry;
        updateItems();
    }

    private void updateItems() {
        staminaBrokenOverrideBdmIdSP.getValueFactory().setValue((int) bdmSubEntry.getStaminaBrokenOverrideBdmId());
        zVanishEnableTimeSP.getValueFactory().setValue(bdmSubEntry.getZVanishEnableTime());
        transformationTypeSP.getValueFactory().setValue(bdmSubEntry.getTransformationType());

        for (int i = 0; i < ailmentGroup1.length; i++) {
            ailmentGroup1[i].setSelected((bdmSubEntry.getAilmentType() & (1 << i)) != 0);
        }

        for (int i = 0; i < ailmentGroup2.length; i++) {
            ailmentGroup2[i].setSelected((bdmSubEntry.getAilmentType() & (16 << i)) != 0);
        }

        for (int i = 0; i < stumbleGroup1.length; i++) {
            stumbleGroup1[i].setSelected((bdmSubEntry.getStumbleType() & (1 << i)) != 0);
        }

        for (int i = 0; i < stumbleGroup2.length; i++) {
            stumbleGroup2[i].setSelected((bdmSubEntry.getStumbleType() & (16 << i)) != 0);
        }

        for (int i = 0; i < stumbleGroup3.length; i++) {
            stumbleGroup3[i].setSelected((bdmSubEntry.getStumbleType() & (256 << i)) != 0);
        }

        for (int i = 0; i < stumbleGroup4.length; i++) {
            stumbleGroup4[i].setSelected((bdmSubEntry.getStumbleType() & (4096 << i)) != 0);
        } 
    }
}