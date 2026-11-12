package xv2.BCM.UI;
import UiComponents.Boxes.HBoxFactory;
import UiComponents.Boxes.VBoxFactory;
import UiComponents.Buttons.RadioButtonFactory;
import UiComponents.Labels.LabelFactory;
import UiComponents.Panes.GridPaneFactory;
import UiComponents.Panes.StackPaneFactory;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Spinner;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import xv2.BCM.Bcm;

public class Activator implements HBoxFactory, LabelFactory, VBoxFactory, RadioButtonFactory, StackPaneFactory, GridPaneFactory {
    private Bcm bcm;
    private Spinner<Integer> priorityLayerSP = createPriorityLayerSP();
    private Spinner<Integer> minimumLoopDurationSP = createMinimumLoopDurationSP();
    private Spinner<Integer> maximumLoopDurationSP = createMaximumLoopDurationSP();
    private ComboBox<String> opponentSizeCB = createOpponentSizeCB();
    private Spinner<Integer> upgradeLevelSP = createUpgradeLevelSP();
    private ToggleGroup skillConditionsToggleGroup = createSkillConditonsTG();
    
    private Node[] opponentSizeConditions = new Node[] {
        opponentSizeCB,
        createLabel("Upgrade Level", 0),
        upgradeLevelSP
    };

    CheckBox[] primaryConditionsGroup1 = new CheckBox[] {
        new CheckBox("Standing"),
        new CheckBox("Floating"),
        new CheckBox("Touching Ground"), 
        new CheckBox("When Attack Hits")
    };

    CheckBox[] primaryConditionsGroup2 = new CheckBox[] {
        new CheckBox("Pass When Guarding"),
        new CheckBox("Close To Opponent"),
        new CheckBox("Far From Opponent"), 
        new CheckBox("Base Form")
    };

    CheckBox[] primaryConditionsGroup3 = new CheckBox[] {
        new CheckBox("Transformed"),
        new CheckBox("Flash On/Off Unless Targeting"),
        new CheckBox("Unknown 11"), 
        new CheckBox("Not Moving")
    };

    CheckBox[] primaryConditionsGroup4 = new CheckBox[] {
        new CheckBox("Counter All"),
        new CheckBox("Pass When Stamina Reaches 0"),
        new CheckBox("Ki < 100%"), 
        new CheckBox("Ki > 0%")
    };

    CheckBox[] primaryConditionsGroup5 = new CheckBox[] {
        new CheckBox("Counter Melee"),
        new CheckBox("Counter Projectile"),
        new CheckBox("Ground"),
        new CheckBox("Opponent")
    };

    CheckBox[] primaryConditionsGroup6 = new CheckBox[] {
        new CheckBox("Opponent In Knockback"),
        new CheckBox("Unknown 22"),
        new CheckBox("Opponent Being Targeted"),
        new CheckBox("Unknown 24")
    };

    CheckBox[] primaryConditionsGroup7 = new CheckBox[] {
        new CheckBox("Active Projectile"),
        new CheckBox("Stamina > 0%"),
        new CheckBox("Not Near Map Ceiling"),
        new CheckBox("Not Near Certain Objects")
    };

    CheckBox[] primaryConditionsGroup8 = new CheckBox[] {
        new CheckBox("User Health < 25% (One Use)"),
        new CheckBox("Opponent Health < 25%"),
        new CheckBox("Running BAC Entry Attack Hits"),
        new CheckBox("User Health < 25%")
    };

    CheckBox[] activatorStateGroup1 = new CheckBox[] {
        new CheckBox("Idle"),
        new CheckBox("Combo/Skill"),
        new CheckBox("Boosting"), 
        new CheckBox("Guarding")
    };

    CheckBox[] activatorStateGroup2 = new CheckBox[] {
        new CheckBox("Receiving Damage"),
        new CheckBox("Jumping"),
        new CheckBox("Not Being Damaged"), 
        new CheckBox("Target Attacking Player")
    };

    StackPane[] activatorState = new StackPane[] {
        createStackPane("State 1", createActivatorStateGroup(activatorStateGroup1, 1L)),
        createStackPane("State 2", createActivatorStateGroup(activatorStateGroup2, 16L))
    };

    StackPane[] primaryActivatorConditons = new StackPane[] {
        createStackPane("Group 1", createPrimaryActivatorGroup(primaryConditionsGroup1, 1L)),
        createStackPane("Group 2", createPrimaryActivatorGroup(primaryConditionsGroup2, 16L)),
        createStackPane("Group 3", createPrimaryActivatorGroup(primaryConditionsGroup3, 256L)),
        createStackPane("Group 4", createPrimaryActivatorGroup(primaryConditionsGroup4, 4096L)),
        createStackPane("Group 5", createPrimaryActivatorGroup(primaryConditionsGroup5, 65536L)),
        createStackPane("Group 6", createPrimaryActivatorGroup(primaryConditionsGroup6, 1048576L)),
        createStackPane("Group 7", createPrimaryActivatorGroup(primaryConditionsGroup7, 16777216L)),
        createStackPane("Group 8", createPrimaryActivatorGroup(primaryConditionsGroup8, 268435456L)),
    };

    private RadioButton[] skillConditions = new RadioButton[] {
        createRadioButton("None", skillConditionsToggleGroup),
        createRadioButton("Use Skill Upgrades", skillConditionsToggleGroup),
        createRadioButton("Unknown 2", skillConditionsToggleGroup),
        createRadioButton("Unknown 3", skillConditionsToggleGroup),
        createRadioButton("Unknown 4", skillConditionsToggleGroup),
        createRadioButton("Opponent Reached Ground", skillConditionsToggleGroup)
    };

    private VBox skilConditionsVBox = createSkillConditonsVBox();

    private HBox[] hBoxs = new HBox[] {
        createHBox(0, Pos.CENTER_LEFT, createLabel("Priority Layer", 120), priorityLayerSP),
        createHBox(0, Pos.CENTER_LEFT, createLabel("Opponent Size", 120), createHBox(15, Pos.CENTER_LEFT, opponentSizeConditions, false)),
        createHBox(0, Pos.CENTER_LEFT, createLabel("Skill Conditions", 120), skilConditionsVBox),
        createHBox(0, Pos.CENTER_LEFT, createLabel("Minimum Loop\nDuration", 120), minimumLoopDurationSP),
        createHBox(0, Pos.CENTER_LEFT, createLabel("Maximum Loop\nDuration", 120), maximumLoopDurationSP),
        createHBox(0, Pos.CENTER_LEFT, createLabel("Primary Activator\nConditions", 120), createGridPane(4, 2, primaryActivatorConditons, false)),
        createHBox(0, Pos.CENTER_LEFT, createLabel("Activator State", 120), createHBox(5, Pos.CENTER_LEFT, activatorState, false))
    };

    public void setBcmEntry(Bcm bcm) {
        this.bcm = bcm;
        updateItems();
    }

    private VBox createSkillConditonsVBox() {
        VBox vBox = new VBox(5, skillConditions);
        vBox.getStyleClass().add("titled-address-box");
        return vBox;
    }

    private Spinner<Integer> createPriorityLayerSP() {
        Spinner<Integer> spinner = new Spinner<>(0, 65535, 0);
        spinner.setEditable(true);
        spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            bcm.setPriorityLayer(newValue.intValue());
        });

        return spinner;
    }

    private ComboBox<String> createOpponentSizeCB() {
        ComboBox<String> comboBox = new ComboBox<>(FXCollections.observableArrayList(
            "All Sizes",
            "Unknown 1",
            "Small Characters",
            "Unknown 3",
            "Default Size",
            "Medium",
            "Medium Large",
            "Large",
            "Great Ape"
        ));

        comboBox.getSelectionModel().selectedIndexProperty().addListener((obs, oldValue, newValue) -> {
            bcm.setOpponentSizeConditions(newValue.intValue());
        });

        return comboBox;
    }

    private Spinner<Integer> createUpgradeLevelSP() {
        Spinner<Integer> spinner = new Spinner<>(0, 255, 0);
        spinner.setEditable(true);
        spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            bcm.setUpgradeLevel(newValue.intValue());
        });

        return spinner;
    }

    private Spinner<Integer> createMinimumLoopDurationSP() {
        Spinner<Integer> spinner = new Spinner<>(0, 65535, 0);
        spinner.setEditable(true);
        spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            bcm.setMinimumLoopDuration(newValue.intValue());
        });

        return spinner;
    }

    private Spinner<Integer> createMaximumLoopDurationSP() {
        Spinner<Integer> spinner = new Spinner<>(0, 65535, 0);
        spinner.setEditable(true);
        spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            bcm.setMaximumLoopDuration(newValue.intValue());
        });

        return spinner;
    }

    private ToggleGroup createSkillConditonsTG() {
        ToggleGroup toggleGroup = new ToggleGroup();
        toggleGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            int selectedIndex = toggleGroup.getToggles().indexOf(newToggle);
            bcm.setSkillConditons(selectedIndex == 0 ? 0 : 1 << selectedIndex);
        });

        return toggleGroup;
    }

    private VBox createActivatorStateGroup(CheckBox[] checkBoxs, long bitMask) {
        VBox vBox = createVBox(2, new Insets(12, 0, 0, 0));
        vBox.getStyleClass().add("titled-address-box");

        for (int i = 0; i < checkBoxs.length; i++) {
            final long thisBitmask = bitMask;
            checkBoxs[i].selectedProperty().addListener((obs, oldValue, newValue) -> {
                if (newValue) {
                    bcm.setActivatorState(bcm.getActivatorState() | thisBitmask);
                }
                else {
                    bcm.setActivatorState(bcm.getActivatorState() & ~thisBitmask);
                }
            });

            vBox.getChildren().add(checkBoxs[i]);
            bitMask <<= 1;
        }

        return vBox;
    }

    private VBox createPrimaryActivatorGroup(CheckBox[] checkBoxsList, long bitMask) {
        VBox vBox = createVBox(2, new Insets(12, 0, 0, 0));
        vBox.getStyleClass().add("titled-address-box");

        for (int i = 0; i < checkBoxsList.length; i++) {
            final long thisBitmask = bitMask;
            checkBoxsList[i].selectedProperty().addListener((obs, oldValue, newValue) -> {
                if (newValue) {
                    bcm.setPrimaryActivatorConditions(bcm.getPrimaryActivatorConditions() | thisBitmask);
                }
                else {
                    bcm.setPrimaryActivatorConditions(bcm.getPrimaryActivatorConditions() & ~thisBitmask);
                }
            });

            vBox.getChildren().add(checkBoxsList[i]);
            bitMask <<= 1;
        }

        return vBox;
    }

    public void updateItems() {
        priorityLayerSP.getValueFactory().setValue(bcm.getPriorityLayer());
        upgradeLevelSP.getValueFactory().setValue(bcm.getUpgradeLevel());
        opponentSizeCB.getSelectionModel().select(bcm.getOpponentSizeConditions());
        skillConditionsToggleGroup.selectToggle(bcm.getSkillConditions() == 0 ? skillConditionsToggleGroup.getToggles().get(0) : skillConditionsToggleGroup.getToggles().get(Integer.numberOfTrailingZeros((int) bcm.getSkillConditions())));
        minimumLoopDurationSP.getValueFactory().setValue(bcm.getMinimumLoopDuration());
        maximumLoopDurationSP.getValueFactory().setValue(bcm.getMaximumLoopDuration());

        for (int i = 0; i < activatorStateGroup1.length; i++) {
            activatorStateGroup1[i].setSelected((bcm.getActivatorState() & (1L << i)) != 0);
        }

        for (int i = 0; i < activatorStateGroup2.length; i++) {
            activatorStateGroup2[i].setSelected((bcm.getActivatorState() & (16L << i)) != 0);
        }

        for (int i = 0; i < primaryConditionsGroup1.length; i++) {
            primaryConditionsGroup1[i].setSelected((bcm.getPrimaryActivatorConditions() & (1L << i)) != 0);
        }

        for (int i = 0; i < primaryConditionsGroup2.length; i++) {
            primaryConditionsGroup2[i].setSelected((bcm.getPrimaryActivatorConditions() & (16L << i)) != 0);
        }

        for (int i = 0; i < primaryConditionsGroup3.length; i++) {
            primaryConditionsGroup3[i].setSelected((bcm.getPrimaryActivatorConditions() & (256L << i)) != 0);
        }

        for (int i = 0; i < primaryConditionsGroup4.length; i++) {
            primaryConditionsGroup4[i].setSelected((bcm.getPrimaryActivatorConditions() & (4096L << i)) != 0);
        }

        for (int i = 0; i < primaryConditionsGroup5.length; i++) {
            primaryConditionsGroup5[i].setSelected((bcm.getPrimaryActivatorConditions() & (65536L << i)) != 0);
        }

        for (int i = 0; i < primaryConditionsGroup6.length; i++) {
            primaryConditionsGroup6[i].setSelected((bcm.getPrimaryActivatorConditions() & (1048576L << i)) != 0);
        }

        for (int i = 0; i < primaryConditionsGroup7.length; i++) {
            primaryConditionsGroup7[i].setSelected((bcm.getPrimaryActivatorConditions() & (16777216L << i)) != 0);
        }

        for (int i = 0; i < primaryConditionsGroup8.length; i++) {
            primaryConditionsGroup8[i].setSelected((bcm.getPrimaryActivatorConditions() & (268435456L << i)) != 0);
        }
    }
    
    @Override
    public VBox createVBox() {
        return createVBox(35, new Insets(20, 0, 20, 8), hBoxs);
    }

    public ScrollPane createScrollPane() {
        return new ScrollPane(createVBox());
    }
}