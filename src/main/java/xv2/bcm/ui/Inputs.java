package xv2.bcm.ui;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import uicomponents.boxes.HBoxFactory;
import uicomponents.boxes.VBoxFactory;
import uicomponents.labels.LabelFactory;
import uicomponents.panes.GridPaneFactory;
import uicomponents.panes.StackPaneFactory;
import xv2.bcm.Bcm;

public class Inputs implements VBoxFactory, StackPaneFactory, LabelFactory, HBoxFactory, GridPaneFactory {
    Bcm bcm;
    ToggleGroup behaviourToggleGroup = new ToggleGroup();
    ToggleGroup option2ToggleGroup = new ToggleGroup();
    ToggleGroup option3ToggleGroup = new ToggleGroup();
    ToggleGroup option4ToggleGroup = new ToggleGroup();
    ToggleGroup chargeTypeToggleGroup = new ToggleGroup();

    CheckBox[] relativeDirection = new CheckBox[] {
        new CheckBox("Forwards"),
        new CheckBox("Backwards"),
        new CheckBox("Left"),
        new CheckBox("Right")
    };

    CheckBox[] userDirection = new CheckBox[] {
        new CheckBox("Input Activated Once"),
        new CheckBox("Up"),
        new CheckBox("Down"),
        new CheckBox("Right"),
        new CheckBox("Left")
    };

    CheckBox[] buttonInputGroup1 = new CheckBox[] {
        new CheckBox("Light"),
        new CheckBox("Heavy"),
        new CheckBox("Blast"),
        new CheckBox("Jump")
    };

    CheckBox[] buttonInputGroup2 = new CheckBox[] {
        new CheckBox("Skill Menu"),
        new CheckBox("Boost"),
        new CheckBox("Guard"),
        new CheckBox("Unknown 8")
    };

    CheckBox[] buttonInputGroup3 = new CheckBox[] {
        new CheckBox("Super Skill 1"),
        new CheckBox("Super Skill 2"),
        new CheckBox("Super Skill 3"),
        new CheckBox("Super Skill 4")
    };

    CheckBox[] buttonInputGroup4 = new CheckBox[] {
        new CheckBox("Ultimate Skill 1"),
        new CheckBox("Ultimate Skill 2"),
        new CheckBox("Awoken Skill"),
        new CheckBox("Evasive Sklill")
    };

    CheckBox[] buttonInputGroup5 = new CheckBox[] {
        new CheckBox("Skill Input"),
        new CheckBox("Super Menu + Skill Input"),
        new CheckBox("Ultimate Menu + Skill Input"),
        new CheckBox("Unknown 20")
    };

    CheckBox[] buttonInputGroup6 = new CheckBox[] {
        new CheckBox("Locked On"),
        new CheckBox("Descend"),
        new CheckBox("Dragon Radar"),
        new CheckBox("Jump 2")
    };

    CheckBox[] buttonInputGroup7 = new CheckBox[] {
        new CheckBox("Ultimate Menu"),
        new CheckBox("Unknown 26"),
        new CheckBox("Unknown 27"),
        new CheckBox("Unknown 28")
    };

    CheckBox[] buttonInputGroup8 = new CheckBox[] {
        new CheckBox("Ultimate Menu 2"),
        new CheckBox("Unknown 30"),
        new CheckBox("Unknown 31"),
        new CheckBox("Unknown 32")
    };

    RadioButton[] holdDownConditionsGroup1 = new RadioButton[] {
        new RadioButton("Continue Until Released"),
        new RadioButton("Delay Until Released"),
        new RadioButton("Unknown 2"),
        new RadioButton("Stop Skill From Activating")
    };

    RadioButton[] holdDownConditionsGroup2 = new RadioButton[] {
        new RadioButton("Unknown 4"),
        new RadioButton("Unknown 5"),
        new RadioButton("Unknown 6"),
        new RadioButton("Unknown 7")
    };

    RadioButton[] holdDownConditionsGroup3 = new RadioButton[] {
        new RadioButton("Unknown 8"),
        new RadioButton("Unknown 9"),
        new RadioButton("Unknown 10"),
        new RadioButton("Unknown 11")
    };

    RadioButton[] holdDownConditionsGroup4 = new RadioButton[] {
        new RadioButton("Unknown 12"),
        new RadioButton("Unknown 13"),
        new RadioButton("Unknown 14"),
        new RadioButton("Unknown 15")
    };

    RadioButton[] holdDownConditionsGroup5 = new RadioButton[] {
        new RadioButton("Automatic"),
        new RadioButton("Manual"),
        new RadioButton("Hold Down To Loop"),
        new RadioButton("Unknown 19")
    };

    StackPane[] directionalInputs = new StackPane[] {
        createStackPane("Relative Direction", createDirectionalInputsGroup(relativeDirection, 1L)),
        createStackPane("User Direction", createDirectionalInputsGroup(userDirection, 16L)),
    };

    StackPane[] buttonInputs = new StackPane[] {
        createStackPane("0x1", createButtonInputsGroup(buttonInputGroup1, 1L)),
        createStackPane("0x10", createButtonInputsGroup(buttonInputGroup2, 16L)),
        createStackPane("0x100", createButtonInputsGroup(buttonInputGroup3, 256L)),
        createStackPane("0x1000", createButtonInputsGroup(buttonInputGroup4, 4096L)),
        createStackPane("0x10000", createButtonInputsGroup(buttonInputGroup5, 65536L)),
        createStackPane("0x100000", createButtonInputsGroup(buttonInputGroup6, 1048576L)),
        createStackPane("0x1000000", createButtonInputsGroup(buttonInputGroup7, 16777216L)),
        createStackPane("0x10000000", createButtonInputsGroup(buttonInputGroup8, 268435456L)),
    };

    StackPane[] holdDownConditions = new StackPane[] {
        createStackPane("Behaviour", createBehaviourGroup(behaviourToggleGroup, holdDownConditionsGroup1, 1L)),
        createStackPane("Options #2", createOtherGroup(option2ToggleGroup, holdDownConditionsGroup2, 16L)),
        createStackPane("Options #3", createOtherGroup(option3ToggleGroup, holdDownConditionsGroup3, 256L)),
        createStackPane("Options #4", createOtherGroup(option4ToggleGroup, holdDownConditionsGroup4, 4096L)),
        createStackPane("Charge Type", createOtherGroup(chargeTypeToggleGroup, holdDownConditionsGroup5, 65536L)),
    };

    HBox[] hBoxs = new HBox[] {
        createHBox(0, Pos.CENTER_LEFT, createLabel("Directional Input", 100), createHBox(5, Pos.CENTER_LEFT, directionalInputs, false)),
        createHBox(0, Pos.CENTER_LEFT, createLabel("Button Input", 100), createGridPane(4, 2, buttonInputs, false)),
        createHBox(0, Pos.CENTER_LEFT, createLabel("Hold Down\nConditions", 100), createHBox(5, Pos.CENTER_LEFT, holdDownConditions, false)),
    };

    private VBox createDirectionalInputsGroup(CheckBox[] checkBoxs, long bitMask) {
        VBox vBox = createVBox(2, new Insets(12, 0, 0, 0));
        vBox.getStyleClass().add("titled-address-box");

        for (int i = 0; i < checkBoxs.length; i++) {
            final long thisBitmask = bitMask;
            checkBoxs[i].selectedProperty().addListener((obs, oldValue, newValue) -> {
                if (newValue) {
                    bcm.setDirectionalInputs(bcm.getDirectionalInputs() | thisBitmask);
                }
                else {
                    bcm.setDirectionalInputs(bcm.getDirectionalInputs() & ~thisBitmask);
                }
            });

            vBox.getChildren().add(checkBoxs[i]);
            bitMask <<= 1;
        }

        return vBox;
    }

    private VBox createButtonInputsGroup(CheckBox[] checkBoxs, long bitMask) {
        VBox vBox = createVBox(2, new Insets(12, 0, 0, 0));
        vBox.getStyleClass().add("titled-address-box");

        for (int i = 0; i < checkBoxs.length; i++) {
            final long thisBitmask = bitMask;
            checkBoxs[i].selectedProperty().addListener((obs, oldValue, newValue) -> {
                if (newValue) {
                    bcm.setButtonInputs(bcm.getButtonInputs() | thisBitmask);
                }
                else {
                    bcm.setButtonInputs(bcm.getButtonInputs() & ~thisBitmask);
                }
            });

            vBox.getChildren().add(checkBoxs[i]);
            bitMask <<= 1;
        }

        return vBox;
    }

    private VBox createBehaviourGroup(ToggleGroup toggleGroup, RadioButton[] radioButtons, long bitMask) {
        radioButtons[0].setSelected(true);
        VBox vBox = createVBox(5, new Insets(12, 0, 0, 0), radioButtons);
        vBox.getStyleClass().add("titled-address-box");

        for (int i = 0; i < radioButtons.length; i++) {
            radioButtons[i].setToggleGroup(toggleGroup);

            if (i != 0) {
                final long thisBitmask = bitMask << (i - 1);
                radioButtons[i].selectedProperty().addListener((obs, oldValue, newValue) -> {
                    if (newValue) {
                        bcm.setHoldDownConditions(bcm.getHoldDownConditions() | thisBitmask);
                    }
                    else {
                        bcm.setHoldDownConditions(bcm.getHoldDownConditions() & ~thisBitmask);
                    }
                });
            }
        }

        return vBox;
    }

    private VBox createOtherGroup(ToggleGroup toggleGroup, RadioButton[] radioButtons, long bitMask) {
        radioButtons[0].setSelected(true);
        VBox vBox = createVBox(5, new Insets(12, 0, 0, 0), radioButtons);
        vBox.getStyleClass().add("titled-address-box");

        for (int i = 0; i < radioButtons.length; i++) {
            radioButtons[i].setToggleGroup(toggleGroup);

            if (i != 0) {
                final long thisBitmask = bitMask * i;
                radioButtons[i].selectedProperty().addListener((obs, oldValue, newValue) -> {
                    if (newValue) {
                        bcm.setHoldDownConditions(bcm.getHoldDownConditions() | thisBitmask);
                    }
                    else {
                        bcm.setHoldDownConditions(bcm.getHoldDownConditions() & ~thisBitmask);
                    }
                });
            }
        }

        return vBox;
    }

    public void setBcmEntry(Bcm bcm) {
        this.bcm = bcm;
        updateItems();
    }

    private void updateItems() {
        for (int i = 0; i < relativeDirection.length; i++) {
            relativeDirection[i].setSelected((bcm.getDirectionalInputs() & (1L << i)) != 0);
        }

        for (int i = 0; i < userDirection.length; i++) {
            userDirection[i].setSelected((bcm.getDirectionalInputs() & (16L << i)) != 0);
        }

        for (int i = 0; i < buttonInputGroup1.length; i++) {
            buttonInputGroup1[i].setSelected((bcm.getButtonInputs() & (1L << i)) != 0);
        }

        for (int i = 0; i < buttonInputGroup2.length; i++) {
            buttonInputGroup2[i].setSelected((bcm.getButtonInputs() & (16L << i)) != 0);
        }

        for (int i = 0; i < buttonInputGroup3.length; i++) {
            buttonInputGroup3[i].setSelected((bcm.getButtonInputs() & (256L << i)) != 0);
        }

        for (int i = 0; i < buttonInputGroup4.length; i++) {
            buttonInputGroup4[i].setSelected((bcm.getButtonInputs() & (4096L << i)) != 0);
        }

        for (int i = 0; i < buttonInputGroup5.length; i++) {
            buttonInputGroup5[i].setSelected((bcm.getButtonInputs() & (65536L << i)) != 0);
        }

        for (int i = 0; i < buttonInputGroup6.length; i++) {
            buttonInputGroup6[i].setSelected((bcm.getButtonInputs() & (1048576L << i)) != 0);
        }

        for (int i = 0; i < buttonInputGroup7.length; i++) {
            buttonInputGroup7[i].setSelected((bcm.getButtonInputs() & (16777216L << i)) != 0);
        }

        for (int i = 0; i < buttonInputGroup8.length; i++) {
            buttonInputGroup8[i].setSelected((bcm.getButtonInputs() & (268435456L << i)) != 0);
        }

        holdDownConditionsGroup1[0].setSelected(true);
        for (int i = 1; i < holdDownConditionsGroup1.length; i++) {
            holdDownConditionsGroup1[i].setSelected(((bcm.getHoldDownConditions() & 1L << (i - 1)) != 0));
            if (holdDownConditionsGroup1[i].isSelected()) break;
        }
        
        for (int i = holdDownConditionsGroup2.length - 1; i > - 1; i--) {
            holdDownConditionsGroup2[i].setSelected((bcm.getHoldDownConditions() & (16L * i)) == (16L * i));
            if (holdDownConditionsGroup2[i].isSelected()) break;
        }

        for (int i = holdDownConditionsGroup3.length - 1; i > -1; i--) {
            holdDownConditionsGroup3[i].setSelected((bcm.getHoldDownConditions() & (256L * i)) == 256L * i);
            if (holdDownConditionsGroup3[i].isSelected()) break;
        }   

        for (int i = holdDownConditionsGroup4.length - 1; i > -1; i--) {
            holdDownConditionsGroup4[i].setSelected((bcm.getHoldDownConditions() & (4096L * i)) == 4096L * i);
            if (holdDownConditionsGroup4[i].isSelected()) break;
        }

        for (int i = holdDownConditionsGroup5.length - 1; i > -1; i--) {
            holdDownConditionsGroup5[i].setSelected((bcm.getHoldDownConditions() & (65536L * i)) == 65536L * i);
            if (holdDownConditionsGroup5[i].isSelected()) break;
        }
    }

    @Override
    public VBox createVBox() {
        VBox vBox = createVBox(100, new Insets(20, 0, 0, 8), hBoxs);
        vBox.getStyleClass().add("inputs-vbox");
        return vBox;
    }
}