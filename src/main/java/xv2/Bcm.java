package xv2;
import static xv2.BinaryUtilities.*;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Spinner;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Tab;

public class Bcm {
    ArrayList<TreeItem<String>> allEntries;
    HashMap<TreeItem<String> ,BcmEntry> bcmHashMap = new HashMap<>();

    TreeView<String> treeView = new TreeView<>();
    TreeItem<String> currentEntry = new TreeItem<>();
    TabPane tabPane = new TabPane();

    BcmEntry copyContainer = null;

    ContextMenu contextMenu = new ContextMenu();
    MenuItem copy = new MenuItem("Copy Ctrl+C");
    MenuItem paste = new MenuItem("Paste Ctrl+V");
    MenuItem delete = new MenuItem("Delete Del");
    MenuItem append = new MenuItem("Append Ctrl+A");
    MenuItem insert = new MenuItem("Insert Ctrl+I");
    MenuItem addNewChild = new MenuItem("Add New Child Ctrl+N");
    MenuItem addComment = new MenuItem("Add Comment Ctrl+Q");

    int findIndex = 0;
    String findText = null;
    Object[] indexList = new Object[] {findIndex, findText};
  
    public Bcm() {
        entriesActionListener();
        entriesKeysListener();
    }

    public SplitPane createSplitPane() {
        createTabs();
        SplitPane splitPane = new SplitPane(treeView, tabPane);
        splitPane.setDividerPositions(0.245);
        splitPane.getStylesheets().add(getClass().getResource("/style.css").toExternalForm()); 

        return splitPane;
    }

    public void createTabs() {
        if (tabPane.getTabs().isEmpty()) {
            Tab inputsTab = new Tab("Inputs");
            Tab activatorTab = new Tab("Activator");
            Tab bacTab = new Tab("BAC");
            Tab miscTab = new Tab("Misc");
            Tab unknownTab = new Tab("Unknown");

            inputsTab.setClosable(false);
            activatorTab.setClosable(false);
            bacTab.setClosable(false);
            miscTab.setClosable(false);
            unknownTab.setClosable(false);

            tabPane.getTabs().addAll(inputsTab, activatorTab, bacTab, miscTab, unknownTab);
        }
    }
    
    public VBox createInputsVBox(BcmEntry entry) {
        ToggleGroup behaviorToggleGroup = new ToggleGroup();
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

        RadioButton[] holdDownConditonsGroup1 = new RadioButton[] {
            new RadioButton("Continue Until Released"),
            new RadioButton("Delay Until Released"),
            new RadioButton("Unknown 2"),
            new RadioButton("Stop Skill From Activating")
        };

        RadioButton[] holdDownConditonsGroup2 = new RadioButton[] {
            new RadioButton("Unknown 5"),
            new RadioButton("Unknown 6"),
            new RadioButton("Unknown 7"),
            new RadioButton("Unknown 8")
        };

        RadioButton[] holdDownConditonsGroup3 = new RadioButton[] {
            new RadioButton("Unknown 9"),
            new RadioButton("Unknown 10"),
            new RadioButton("Unknown 11"),
            new RadioButton("Unknown 12")
        };

        RadioButton[] holdDownConditonsGroup4 = new RadioButton[] {
            new RadioButton("Unknown 13"),
            new RadioButton("Unknown 14"),
            new RadioButton("Unknown 15"),
            new RadioButton("Unknown 16")
        };

        RadioButton[] holdDownConditonsGroup5 = new RadioButton[] {
            new RadioButton("Automatic"),
            new RadioButton("Manual"),
            new RadioButton("Hold Down To Loop"),
            new RadioButton("Unknown 20")
        };

        Node[] directionalInput = new Node[] {
            createCheckBoxGroup("Relative Direction", relativeDirection, 1L, BcmValues.DirectionalInput),
            createCheckBoxGroup("User Direction", userDirection, 16L, BcmValues.DirectionalInput),
        };

        Node[] buttonInput = new Node[] {
            createCheckBoxGroup("0x1", buttonInputGroup1, 1L, BcmValues.ButtonInput),
            createCheckBoxGroup("0x10", buttonInputGroup2, 16L, BcmValues.ButtonInput),
            createCheckBoxGroup("0x100", buttonInputGroup3, 256L, BcmValues.ButtonInput),
            createCheckBoxGroup("0x1000", buttonInputGroup4, 4096L, BcmValues.ButtonInput),
            createCheckBoxGroup("0x10000", buttonInputGroup5, 65536L, BcmValues.ButtonInput),
            createCheckBoxGroup("0x100000", buttonInputGroup6, 1048576L, BcmValues.ButtonInput),
            createCheckBoxGroup("0x1000000", buttonInputGroup7, 16777216L, BcmValues.ButtonInput),
            createCheckBoxGroup("0x10000000", buttonInputGroup8, 268435456L, BcmValues.ButtonInput)
        };

        Node[] holdDownConditions = new Node[] {
            createRadioButtonGroup("Behaviour", behaviorToggleGroup, holdDownConditonsGroup1, 1L, new float[] {2, 2}, BcmValues.HoldDownConditions),
            createRadioButtonGroup("Option 2", option2ToggleGroup, holdDownConditonsGroup2, 16L, new float[] {2, 1.5f}, BcmValues.HoldDownConditions),
            createRadioButtonGroup("Option 3", option3ToggleGroup, holdDownConditonsGroup3, 256L, new float[] {2, 1.5f}, BcmValues.HoldDownConditions),
            createRadioButtonGroup("Option 4", option4ToggleGroup, holdDownConditonsGroup4, 4096L, new float[] {2, 1.5f}, BcmValues.HoldDownConditions),
            createRadioButtonGroup("Charge Type", chargeTypeToggleGroup, holdDownConditonsGroup5, 65536L, new float[] {2, 1.5f}, BcmValues.HoldDownConditions)
        };

        VBox inputsVBox = new VBox(100, 
            createHBox(0, createLabel("Directional Input", 100), createHBox(5, directionalInput, false)), 
            createHBox(0, createLabel("Button Input", 100), createGridPane(4, 2, buttonInput, false)), 
            createHBox(0, createLabel("Hold Down\nConditions", 100), createHBox(5, holdDownConditions, false))
        );
        inputsVBox.setPadding(new Insets(20, 0, 0, 8));
        inputsVBox.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

        return inputsVBox;
    }

    private ScrollPane createActivatorScrollPane(BcmEntry entry) {
        ToggleGroup skillConditionsToggleGroup = new ToggleGroup();

        ObservableList<String> opponentSizes = FXCollections.observableArrayList(
            "All Sizes",
            "Unknown 1",
            "Unknown 2",
            "Small Characters",
            "Default Size",
            "Medium",
            "Medium Large",
            "Large",
            "Great Ape"
        );

        RadioButton[] skillConditions = new RadioButton[] {
            createRadioButton("None", skillConditionsToggleGroup, entry.skillConditions, SkillConditions.None),
            createRadioButton("Use Skill Upgrades", skillConditionsToggleGroup, entry.skillConditions, SkillConditions.UseSkillUpgrades),
            createRadioButton("Unknown 2", skillConditionsToggleGroup, entry.skillConditions, SkillConditions.Unknown2),
            createRadioButton("Unknown 3", skillConditionsToggleGroup, entry.skillConditions, SkillConditions.Unknown3),
            createRadioButton("Unknown 4", skillConditionsToggleGroup, entry.skillConditions, SkillConditions.Unknown4),
            createRadioButton("Opponent Reached Ground", skillConditionsToggleGroup, entry.skillConditions, SkillConditions.OpponentRachedGround),
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
            new CheckBox("Running BAC Entry Attact Hits"),
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

        Node[] primaryActivatorConditions = new Node[] {
            createCheckBoxGroup("Group 1", primaryConditionsGroup1, 1L, BcmValues.PrimaryActivatorConditions),
            createCheckBoxGroup("Group 2", primaryConditionsGroup2, 16L, BcmValues.PrimaryActivatorConditions),
            createCheckBoxGroup("Group 3", primaryConditionsGroup3, 256L, BcmValues.PrimaryActivatorConditions),
            createCheckBoxGroup("Group 4", primaryConditionsGroup4, 4096L, BcmValues.PrimaryActivatorConditions),
            createCheckBoxGroup("Group 5", primaryConditionsGroup5, 65536L, BcmValues.PrimaryActivatorConditions),
            createCheckBoxGroup("Group 6", primaryConditionsGroup6, 1048576L, BcmValues.PrimaryActivatorConditions),
            createCheckBoxGroup("Group 7", primaryConditionsGroup7, 16777216L, BcmValues.PrimaryActivatorConditions),
            createCheckBoxGroup("Group 8", primaryConditionsGroup8, 268435456L, BcmValues.PrimaryActivatorConditions)
        };

        Node[] activatorState = new Node[] {
            createCheckBoxGroup("State", activatorStateGroup1, 1L, BcmValues.ActivatorState),
            createCheckBoxGroup("State", activatorStateGroup2, 16L, BcmValues.ActivatorState)
        };

        Node[] opponentSizeConditions = new Node[] {
            createComboBox(opponentSizes, BcmValues.OpponentSizeConditions),
            createLabel("Upgrade Level", 0),
            createSpinner(0, 255, entry.opponentSizeConditions / 16777216, BcmValues.OpponentSizeConditions)
        };

        VBox activatorVBox = new VBox(35, 
            createHBox(0, createLabel("Opponent Size", 120), createHBox(15, opponentSizeConditions, false)),
            createHBox(0, createLabel("Skill Conditions", 120), createGridPane(1, 6, skillConditions, true)),
            createHBox(0, createLabel("Minimum Loop\nDuration", 120), createSpinner(0, 65535, entry.maximumLoopDuration, BcmValues.MaximumLoopDuration)),
            createHBox(0, createLabel("Maximum Loop\nDuration", 120), createSpinner(0, 65535, entry.minimumLoopDuration, BcmValues.MinimumLoopDuration)), 
            createHBox(0, createLabel("Primary Activator\nConditions", 120), createGridPane(4, 2, primaryActivatorConditions, false)), 
            createHBox(0, createLabel("Activator State", 120), createHBox(5, activatorState, false))
        );
        activatorVBox.setPadding(new Insets(20, 0, 20, 8));
        activatorVBox.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

        ScrollPane activatorScrollPane = new ScrollPane(activatorVBox);
        activatorScrollPane.setFitToWidth(true);

        return activatorScrollPane;
    }

    private VBox createBACVBox(BcmEntry entry) {
        ToggleGroup randomFlagToggleGroup = new ToggleGroup();

        RadioButton[] randomFlagsList = new RadioButton[] {
            createRadioButton("None/Default", randomFlagToggleGroup, entry.characterCondition, RandomFlags.None),
            createRadioButton("Random BAC Entry", randomFlagToggleGroup, entry.characterCondition, RandomFlags.Random_BAC_Entry),
            createRadioButton("No Target Correction", randomFlagToggleGroup, entry.characterCondition, RandomFlags.NoTargetCorrection),
            createRadioButton("3 Instance Setup", randomFlagToggleGroup, entry.characterCondition, RandomFlags.ThreeInstanceSetup),
            createRadioButton("Unknown 4", randomFlagToggleGroup, entry.characterCondition, RandomFlags.Unknown4),
            createRadioButton("Unknown 6", randomFlagToggleGroup, entry.characterCondition, RandomFlags.Unknown6),
        };

        VBox BACVBox = new VBox(30, 
            createHBox(0, createLabel("BAC Entry Primary", 200), createSpinner(Short.MIN_VALUE, Short.MAX_VALUE, entry.bacEntryPrimary, BcmValues.BAC_EntryPrimary)), 
            createHBox(0, createLabel("BAC Entry Charge", 200), createSpinner(Short.MIN_VALUE, Short.MAX_VALUE, entry.bacEntryCharge, BcmValues.BAC_EntryCharge)),
            createHBox(0, createLabel("BAC Entry User Connect", 200), createSpinner(Short.MIN_VALUE, Short.MAX_VALUE, entry.bacEntryUserConnect, BcmValues.BAC_EntryUserConnect)), 
            createHBox(0, createLabel("BAC Entry Victim Connect", 200), createSpinner(Short.MIN_VALUE, Short.MAX_VALUE, entry.bacEntryVictimConnect, BcmValues.BAC_EntryVictimConnect)), 
            createHBox(0, createLabel("BAC Entry Airborne", 200), createSpinner(Short.MIN_VALUE, Short.MAX_VALUE, entry.bacEntryAirborne, BcmValues.BAC_EntryAirborne)), 
            createHBox(0, createLabel("BAC Entry Targeting Override", 200), createSpinner(0, 65535, entry.bacEntryTargetingOverride, BcmValues.BAC_EntryTargetingOverride)),
            createHBox(0, createLabel("Random Flag", 200), createGridPane(2, 3, randomFlagsList, true))
        );
        BACVBox.setPadding(new Insets(20, 0, 0, 8));

        return BACVBox;
    }

    private VBox createMiscVBox(BcmEntry entry) {
        ToggleGroup characterConditonToggleGroup = new ToggleGroup();

        ObservableList<String> receiverLinkIds = FXCollections.observableArrayList(
            "None",
            "Combos",
            "Supers",
            "Ultimate / Awoken / Evasive",
            "Z-Vanish",
            "Ki Blasts",
            "Jump",
            "Guard",
            "Flying / Step Dash"
        );

        RadioButton[] characterConditonsList = new RadioButton[] {
            createRadioButton("None/Default", characterConditonToggleGroup, entry.characterCondition, CharacterConditions.None),
            createRadioButton("Custom Character (CAC)", characterConditonToggleGroup, entry.characterCondition, CharacterConditions.CustomCharacter),
            createRadioButton("Human Male (HUM)", characterConditonToggleGroup, entry.characterCondition, CharacterConditions.HumanMale),
            createRadioButton("Human Female (HUF)", characterConditonToggleGroup, entry.characterCondition, CharacterConditions.HumanFemale),
            createRadioButton("Saiyan Male (SYM)", characterConditonToggleGroup, entry.characterCondition, CharacterConditions.SaiyanMale),
            createRadioButton("Saiyan Female (SYF)", characterConditonToggleGroup, entry.characterCondition, CharacterConditions.SaiyanFemale),
            createRadioButton("Namekian (NMC)", characterConditonToggleGroup, entry.characterCondition, CharacterConditions.Namekian),
            createRadioButton("Frieza Race (FRI)", characterConditonToggleGroup, entry.characterCondition, CharacterConditions.FriezaRace),
            createRadioButton("Majin Male (MAM)", characterConditonToggleGroup, entry.characterCondition, CharacterConditions.MajinMale),
            createRadioButton("Majin Female (MAF)", characterConditonToggleGroup, entry.characterCondition, CharacterConditions.MajinFemale),
        };
        
        VBox miscVBox = new VBox(30, 
            createHBox(0, createLabel("Ki Cost", 180), createSpinner(0, 4294967295.0, (double) entry.kiCost, BcmValues.KiCost)), 
            createHBox(0, createLabel("Receiver Link ID", 180), createComboBox(receiverLinkIds, BcmValues.ReceiverLinkID)),
            createHBox(0, createLabel("Stamina Cost", 180), createSpinner(0, 4294967295.0, (double) entry.staminaCost, BcmValues.StaminaCost)), 
            createHBox(0, createLabel("Ki Required", 180), createSpinner(0, 4294967295.0, (double) entry.kiRequired, BcmValues.KiRequired)),
            createHBox(0, createLabel("Health Required", 180), createSpinner(Float.MIN_VALUE, Float.MAX_VALUE, (double) entry.healthRequired, BcmValues.HealthRequired)),
            createHBox(0, createLabel("Transformation Stage", 180), createSpinner(Short.MIN_VALUE, Short.MAX_VALUE, entry.transformationStage, BcmValues.TransformationStage)),
            createHBox(0, createLabel("CUS Aura", 180), createSpinner(Short.MIN_VALUE, Short.MAX_VALUE, entry.cusAura, BcmValues.CUS_Aura)), 
            createHBox(0, createLabel("Character Condition", 180), createGridPane(2, 5, characterConditonsList, true))
        );
        miscVBox.setPadding(new Insets(20, 0, 0, 8));

        return miscVBox;
    }

    private VBox createUnknownVBox(BcmEntry entry) {
        VBox unknownVBox = new VBox(30,
            createHBox(0, createLabel("I_36", 130), createTextField(entry.i36, BcmValues.I36)),
            createHBox(0, createLabel("I_68", 130), createTextField(entry.i68, BcmValues.I68)), 
            createHBox(0, createLabel("I_72", 130), createTextField(entry.i72, BcmValues.I72)),
            createHBox(0, createLabel("I_80", 130), createTextField(entry.i80, BcmValues.I80)), 
            createHBox(0, createLabel("I_88", 130), createTextField(entry.i88, BcmValues.I88)),
            createHBox(0, createLabel("Skill Upgrade Value?", 130), createTextField(entry.i104, BcmValues.I104))
        );
        unknownVBox.setPadding(new Insets(20, 0, 0, 8));

        return unknownVBox;
    }

    private Label createLabel(String text, int width) {
        Label label = new Label(text);
        if (width != 0) label.setPrefWidth(width);

        return label;
    }

    private TextField createTextField(Number value, BcmValues bcmValue) {
        TextField textField = new TextField(String.valueOf(value));
        textField.textProperty().addListener((obs, oldText, newText) -> {
            if (textField.getText().contains("-")) {
                return;
            }
            try {
                switch (bcmValue) {
                    case BcmValues.I36 -> bcmHashMap.get(currentEntry).i36 = Short.parseShort(newText);
                    case BcmValues.I68 -> bcmHashMap.get(currentEntry).i68 = Long.parseLong(newText);
                    case BcmValues.I72 -> bcmHashMap.get(currentEntry).i72 = Long.parseLong(newText);
                    case BcmValues.I80 -> bcmHashMap.get(currentEntry).i80 = Long.parseLong(newText);
                    case BcmValues.I88 -> bcmHashMap.get(currentEntry).i88 = Long.parseLong(newText);
                    case BcmValues.I104 -> bcmHashMap.get(currentEntry).i104 = Long.parseLong(newText);
                    default -> throw new IllegalArgumentException("Unexpected value: " + bcmValue);
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        return textField;
    }

    private ComboBox<String> createComboBox(ObservableList<String> observableList, BcmValues bcmValue) {
        ComboBox<String> comboBox = new ComboBox<>(observableList);

        switch (bcmValue) {
            case OpponentSizeConditions -> {
                outerLoop:
                for (int i = 1; i < observableList.size(); i++) {
                    if (bcmHashMap.get(currentEntry).opponentSizeConditions != 0 && (bcmHashMap.get(currentEntry).opponentSizeConditions & OpponentSizeConditions.values()[i].index) == (bcmHashMap.get(currentEntry).opponentSizeConditions % 16777216)) {
                        comboBox.getSelectionModel().select(i);
                        break outerLoop;
                    }
                    else {
                        comboBox.getSelectionModel().select(0);
                    }
                }

                comboBox.getSelectionModel().selectedIndexProperty().addListener((obs, oldValue, newValue) -> {
                    final long upgradeLevel = bcmHashMap.get(currentEntry).opponentSizeConditions - bcmHashMap.get(currentEntry).opponentSizeConditions % 16777216;
                    bcmHashMap.get(currentEntry).opponentSizeConditions = upgradeLevel + OpponentSizeConditions.values()[newValue.intValue()].index;
                });
            }
            case ReceiverLinkID -> {
                if (bcmHashMap.get(currentEntry).receiverLinkId != 0) {
                    comboBox.getSelectionModel().select((int) (Math.log(bcmHashMap.get(currentEntry).receiverLinkId) / Math.log(2)) + 1);
                }
                else {
                    comboBox.getSelectionModel().select(0);
                }

                comboBox.getSelectionModel().selectedIndexProperty().addListener((obs, oldValue, newValue) -> {
                    bcmHashMap.get(currentEntry).receiverLinkId = newValue.intValue() != 0 ? 1 << (newValue.intValue() - 1) : 0;
                });
            }
            default -> throw new IllegalArgumentException("Unexpected value: " + bcmValue);
        }
        
        return comboBox;
    }

    private Spinner<Number> createSpinner(Number MIN_VALUE, Number MAX_VALUE, Number value, BcmValues bcmValue) {
        Spinner<Number> spinner;

        if (value instanceof Double) {
            spinner = new Spinner<>(MIN_VALUE.doubleValue(), MAX_VALUE.doubleValue(), value.doubleValue());
        }
        else {
            spinner = new Spinner<>(MIN_VALUE.intValue(), MAX_VALUE.intValue(), value.intValue());
        }
        
        spinner.setEditable(true);
        spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                switch (bcmValue) {
                    case OpponentSizeConditions -> {
                        final long remainingBits = ((bcmHashMap.get(currentEntry).opponentSizeConditions + bcmHashMap.get(currentEntry).opponentSizeConditions * 16777216) % 16777216);
                        bcmHashMap.get(currentEntry).opponentSizeConditions = remainingBits +  newValue.longValue()  * 16777216;
                    }
                    case MinimumLoopDuration -> bcmHashMap.get(currentEntry).minimumLoopDuration = newValue.intValue();
                    case MaximumLoopDuration -> bcmHashMap.get(currentEntry).maximumLoopDuration = newValue.intValue();
                    case BAC_EntryPrimary -> bcmHashMap.get(currentEntry).bacEntryPrimary = newValue.shortValue();
                    case BAC_EntryCharge -> bcmHashMap.get(currentEntry).bacEntryCharge = newValue.shortValue();
                    case BAC_EntryUserConnect -> bcmHashMap.get(currentEntry).bacEntryUserConnect = newValue.shortValue();
                    case BAC_EntryVictimConnect -> bcmHashMap.get(currentEntry).bacEntryVictimConnect = newValue.shortValue();
                    case BAC_EntryAirborne -> bcmHashMap.get(currentEntry).bacEntryAirborne = newValue.shortValue();
                    case BAC_EntryTargetingOverride -> bcmHashMap.get(currentEntry).bacEntryTargetingOverride = newValue.intValue();
                    case KiCost -> bcmHashMap.get(currentEntry).kiCost = newValue.longValue();
                    case StaminaCost -> bcmHashMap.get(currentEntry).staminaCost = newValue.longValue();
                    case KiRequired -> bcmHashMap.get(currentEntry).kiRequired = newValue.longValue();
                    case HealthRequired -> bcmHashMap.get(currentEntry).healthRequired = newValue.floatValue();
                    case TransformationStage -> bcmHashMap.get(currentEntry).transformationStage = newValue.shortValue();
                    case CUS_Aura -> bcmHashMap.get(currentEntry).cusAura = newValue.shortValue();
                    default -> throw new IllegalArgumentException("Unexpected value: " + bcmValue);
                }   
            }
        });

        return spinner;
    }

    private RadioButton createRadioButton(String text, ToggleGroup toggleGroup, long value, SkillConditions skillConditions) {
        RadioButton radioButton = new RadioButton(text);
        radioButton.setToggleGroup(toggleGroup);

        if (value == skillConditions.index) radioButton.setSelected(true);

        radioButton.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                switch (skillConditions) {
                    case None -> bcmHashMap.get(currentEntry).skillConditions = 0;
                    case UseSkillUpgrades -> bcmHashMap.get(currentEntry).skillConditions = 1;
                    case Unknown2 -> bcmHashMap.get(currentEntry).skillConditions = 2;
                    case Unknown3 -> bcmHashMap.get(currentEntry).skillConditions = 4;
                    case Unknown4 -> bcmHashMap.get(currentEntry).skillConditions = 8;
                    case OpponentRachedGround -> bcmHashMap.get(currentEntry).skillConditions = 6;
                }
            }
        });

        return radioButton;
    }

    private RadioButton createRadioButton(String text, ToggleGroup toggleGroup, long value, RandomFlags randomFlag) {
        RadioButton radioButton = new RadioButton(text);
        radioButton.setToggleGroup(toggleGroup);

        if (value == randomFlag.index) radioButton.setSelected(true);

        radioButton.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                switch (randomFlag) {
                    case None -> bcmHashMap.get(currentEntry).bacRandomFlags = 0;
                    case Random_BAC_Entry -> bcmHashMap.get(currentEntry).bacRandomFlags = 1;
                    case NoTargetCorrection -> bcmHashMap.get(currentEntry).bacRandomFlags = 2;
                    case ThreeInstanceSetup -> bcmHashMap.get(currentEntry).bacRandomFlags = 3;
                    case Unknown4 -> bcmHashMap.get(currentEntry).bacRandomFlags = 4;
                    case Unknown6 -> bcmHashMap.get(currentEntry).bacRandomFlags = 6;
                }
            }
        });

        return radioButton;
    }

    private RadioButton createRadioButton(String text, ToggleGroup toggleGroup, long value, CharacterConditions characterCondition) {
        RadioButton radioButton = new RadioButton(text);
        radioButton.setToggleGroup(toggleGroup);

        if (value == characterCondition.index) radioButton.setSelected(true);

        radioButton.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                switch (characterCondition) {
                    case None -> bcmHashMap.get(currentEntry).characterCondition = 0;
                    case CustomCharacter -> bcmHashMap.get(currentEntry).characterCondition = 1;
                    case HumanMale -> bcmHashMap.get(currentEntry).characterCondition = 2;
                    case HumanFemale -> bcmHashMap.get(currentEntry).characterCondition = 3;
                    case SaiyanMale -> bcmHashMap.get(currentEntry).characterCondition = 4;
                    case SaiyanFemale -> bcmHashMap.get(currentEntry).characterCondition = 5;
                    case Namekian -> bcmHashMap.get(currentEntry).characterCondition = 6;
                    case FriezaRace -> bcmHashMap.get(currentEntry).characterCondition = 7;
                    case MajinMale -> bcmHashMap.get(currentEntry).characterCondition = 8;
                    case MajinFemale -> bcmHashMap.get(currentEntry).characterCondition = 9;
                }
            }
        });

        return radioButton;
    }
    
    private StackPane createCheckBoxGroup(String text, CheckBox[] checkBoxsList, long bitMask, BcmValues bcmValue) {
        Label label = new Label(text);
        label.getStyleClass().add("titled-address-label");
        label.setTranslateY(-8); 
        label.setTranslateX(10);

        VBox vBox = new VBox(2);
        vBox.getStyleClass().add("titled-address-box");
        vBox.setPadding(new Insets(12, 0, 0, 0));
        
        for (int i = 0; i < checkBoxsList.length; i++) {
            final long bitMaskLamda = bitMask;

            switch(bcmValue) {
                case DirectionalInput -> {
                    checkBoxsList[i].setSelected((bcmHashMap.get(currentEntry).directionalInputs & bitMask) != 0);

                    checkBoxsList[i].selectedProperty().addListener((obs, oldValue, newValue) -> {
                        if (newValue) {
                            bcmHashMap.get(currentEntry).directionalInputs |= bitMaskLamda;
                        }
                        else {
                            bcmHashMap.get(currentEntry).directionalInputs &= ~bitMaskLamda;
                        }
                    });
                }
                case ButtonInput -> {
                    checkBoxsList[i].setSelected((bcmHashMap.get(currentEntry).buttonInputs & bitMask) != 0);

                    checkBoxsList[i].selectedProperty().addListener((obs, oldValue, newValue) -> {
                        if (newValue) {
                            bcmHashMap.get(currentEntry).buttonInputs |= bitMaskLamda;
                        }
                        else {
                            bcmHashMap.get(currentEntry).buttonInputs &= ~bitMaskLamda;
                        }
                    });
                }
                case PrimaryActivatorConditions -> {
                    checkBoxsList[i].setSelected((bcmHashMap.get(currentEntry).primaryActivatorConditions & bitMask) != 0);

                    checkBoxsList[i].selectedProperty().addListener((obs, oldValue, newValue) -> {
                        if (newValue) {
                            bcmHashMap.get(currentEntry).primaryActivatorConditions |= bitMaskLamda;
                        }
                        else {
                            bcmHashMap.get(currentEntry).primaryActivatorConditions &= ~bitMaskLamda;
                        }
                    });
                }
                case ActivatorState -> {
                    checkBoxsList[i].setSelected((bcmHashMap.get(currentEntry).activatorState & bitMask) != 0);

                    checkBoxsList[i].selectedProperty().addListener((obs, oldValue, newValue) -> {
                        if (newValue) {
                            bcmHashMap.get(currentEntry).activatorState |= bitMaskLamda;
                        }
                        else {
                            bcmHashMap.get(currentEntry).activatorState &= ~bitMaskLamda;
                        }
                    });
                }
                default -> throw new IllegalArgumentException("Unexpected value: " + bcmValue);
            }

            vBox.getChildren().add(checkBoxsList[i]);

            bitMask <<= 1;
        }

        StackPane stackPane = new StackPane(vBox, label);
        StackPane.setAlignment(label, Pos.TOP_LEFT);

        return stackPane;
    }

    private StackPane createRadioButtonGroup(String text, ToggleGroup toggleGroup, RadioButton[] radioButtonsList, double bitMask, float[] increment, BcmValues bcmValue) {
        Label label = new Label(text);
        label.getStyleClass().add("titled-address-label");
        label.setTranslateY(-8); 
        label.setTranslateX(10);

        VBox vBox = new VBox(2, radioButtonsList[0]);
        vBox.getStyleClass().add("titled-address-box");
        vBox.setPadding(new Insets(12, 0, 0, 0));

        radioButtonsList[0].setSelected(true);
        radioButtonsList[0].setToggleGroup(toggleGroup);

        double currentBitMask = bitMask;

        for (int i = 1; i < radioButtonsList.length; i++) {
            switch(bcmValue) {
                case HoldDownConditions -> {
                    radioButtonsList[i].setToggleGroup(toggleGroup);
                    radioButtonsList[i].setSelected((bcmHashMap.get(currentEntry).holdDownConditions & (long) currentBitMask) == (long) currentBitMask); 
                }
                default -> throw new IllegalArgumentException("Unexpected value: " + bcmValue);
            }

            vBox.getChildren().add(radioButtonsList[i]);

            if (i != radioButtonsList.length - 1) currentBitMask *= increment[i - 1];
        }

        currentBitMask = bitMask;

        for (int i = 1; i < radioButtonsList.length; i++) {
            final long bitMaskLamda = (long) currentBitMask;

            radioButtonsList[i].selectedProperty().addListener((obs, oldValue, newValue) -> {
                if (newValue) {
                    bcmHashMap.get(currentEntry).holdDownConditions |= bitMaskLamda;
                }
                else {
                    bcmHashMap.get(currentEntry).holdDownConditions &= ~bitMaskLamda;
                    
                }
            });

            if (i != radioButtonsList.length - 1) currentBitMask *= increment[i - 1];
        }

        StackPane stackPane = new StackPane(vBox, label);
        StackPane.setAlignment(label, Pos.TOP_LEFT);

        return stackPane;
    }

    private HBox createHBox(int width, Label label, Node node) {
        HBox hBox = new HBox(width, label, node);
        hBox.setAlignment(Pos.CENTER_LEFT);

        return hBox;
    }

    private HBox createHBox(int width, Node[] nodeList, boolean enableStyle) {
        HBox hBox = new HBox(width);

        if (enableStyle) hBox.getStyleClass().add("titled-address-box");

        for (int i = 0; i < nodeList.length; i++) {
            hBox.getChildren().add(nodeList[i]);
        }
        hBox.setAlignment(Pos.CENTER_LEFT);

        return hBox;
    }

    private GridPane createGridPane(int columns ,int rows, Node[] nodeList, boolean enableStyle) {
        GridPane gridPane = new GridPane(10, 10);
        if (enableStyle) gridPane.getStyleClass().add("titled-address-box");

        int index = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                gridPane.add(nodeList[index], j, i);
                index++;
            }
        }

        return gridPane;
    }
     
    public void entriesActionListener() {
        paste.setDisable(true);

        contextMenu.getItems().addAll(copy, paste, delete, append, insert, addNewChild, addComment);

        treeView.setContextMenu(contextMenu);
        treeView.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue == null)  return;

            currentEntry = newValue;

            tabPane.getTabs().get(0).setContent(createInputsVBox(bcmHashMap.get(currentEntry)));
            tabPane.getTabs().get(1).setContent(createActivatorScrollPane(bcmHashMap.get(currentEntry)));
            tabPane.getTabs().get(2).setContent(createBACVBox(bcmHashMap.get(currentEntry)));
            tabPane.getTabs().get(3).setContent(createMiscVBox(bcmHashMap.get(currentEntry)));
            tabPane.getTabs().get(4).setContent(createUnknownVBox(bcmHashMap.get(currentEntry)));
        });
        treeView.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.SECONDARY) {
                contextMenu.setOnAction(event -> {
                    if (event.getTarget() == copy) {
                        Copy();
                        paste.setDisable(false);
                    }
                    else if (event.getTarget() == paste) Paste();
                    else if (event.getTarget() == delete) Delete();
                    else if (event.getTarget() == append) Append();
                    else if (event.getTarget() == insert) Insert();
                    else if (event.getTarget() == addNewChild) AddNewChild();
                    else if (event.getTarget() == addComment) Popups.AddComment(currentEntry);
                });
            }
        });
    }

    public void entriesKeysListener() {
        treeView.setOnKeyPressed(e -> {
            if (e.isControlDown() && e.getCode() == KeyCode.C) {
                Copy();
                paste.setDisable(false);
            }
            else if (e.isControlDown() && e.getCode() == KeyCode.V) Paste();
            else if (e.getCode() == KeyCode.DELETE) Delete();
            else if (e.isControlDown() && e.getCode() == KeyCode.A) Append();
            else if (e.isControlDown() && e.getCode() == KeyCode.I) Insert();
            else if (e.isControlDown() && e.getCode() == KeyCode.N) AddNewChild();
            else if (e.isControlDown() && e.getCode() == KeyCode.Q) Popups.AddComment(currentEntry);
            else if (e.isControlDown() && e.getCode() == KeyCode.F) {
                ButtonType findNextButtonType = new ButtonType("Find Next", ButtonData.NEXT_FORWARD);
                ButtonType cancelButtonType = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
                
                Dialog<String> dialog = new Dialog<>();

                dialog.setTitle("Find");
                dialog.getDialogPane().getButtonTypes().addAll(findNextButtonType, cancelButtonType);
                dialog.getDialogPane().setContent(Popups.createFindDialog("Entry: ", indexList, 
                    FXCollections.observableArrayList(
                        "Directional Input", 
                        "Button Input", 
                        "Hold Down Conditions", 
                        "BoostEnd", 
                        "KiaiCharge", 
                        "KiryokuMax", 
                        "HenshinStart", 
                        "HenshinEnd"
                    )));

                final Button findbt = (Button) dialog.getDialogPane().lookupButton(findNextButtonType);
                findbt.addEventFilter(ActionEvent.ACTION, event -> {
                    if (!findbt.isPressed()) {

            
                        event.consume();
                    }
                });
                dialog.showAndWait();
            }
        });
    }

    private void Copy() {
        if (currentEntry == null) return;
        
        copyContainer = new BcmEntry(bcmHashMap.get(currentEntry));
    }

    private void Paste() {
        if (currentEntry == null || copyContainer == null) return;
        
        bcmHashMap.put(currentEntry, new BcmEntry(copyContainer));

        if (treeView.getSelectionModel().getSelectedItem() != null) {
            tabPane.getTabs().get(0).setContent(createInputsVBox(bcmHashMap.get(currentEntry)));
            tabPane.getTabs().get(1).setContent(createActivatorScrollPane(bcmHashMap.get(currentEntry)));
            tabPane.getTabs().get(2).setContent(createBACVBox(bcmHashMap.get(currentEntry)));
            tabPane.getTabs().get(3).setContent(createMiscVBox(bcmHashMap.get(currentEntry)));
            tabPane.getTabs().get(4).setContent(createUnknownVBox(bcmHashMap.get(currentEntry)));
        }
    }

    private void Delete() {
        if (currentEntry == null || currentEntry.getParent() == null) return;
        
        bcmHashMap.remove(currentEntry);
        allEntries.remove(currentEntry);
        currentEntry.getParent().getChildren().remove(currentEntry);
    
        int[] index = {0};
        renameTreeItems(treeView.getRoot(), index);
    }

    private void Append() {
        TreeItem<String> parent = currentEntry.getParent();
        if (parent != null) {
            TreeItem<String> newEntry = new TreeItem<>("New Entry");

            int currentPos = parent.getChildren().indexOf(currentEntry);

            parent.getChildren().add(currentPos + 1, newEntry);

            allEntries.add(treeView.getRow(currentEntry) + 1, newEntry);
            bcmHashMap.put(newEntry, new BcmEntry());

            int[] index = {0};
            renameTreeItems(treeView.getRoot(), index);
        }
    }

   private void Insert() {
        TreeItem<String> parent = currentEntry.getParent();

        if (parent != null) {
            TreeItem<String> newEntry = new TreeItem<>("New Entry");

            int currentPos = parent.getChildren().indexOf(currentEntry);

            parent.getChildren().add(currentPos, newEntry);

            allEntries.add(treeView.getRow(currentEntry) - 1, newEntry);
            bcmHashMap.put(newEntry, new BcmEntry());

            int[] index = {0};
            renameTreeItems(treeView.getRoot(), index);
        }
    }

    public void AddNewChild() {
        if (currentEntry == null) return;

        TreeItem<String> newChild = new TreeItem<>("New Entry");

        currentEntry.getChildren().add(newChild);
        currentEntry.setExpanded(true);

        allEntries.add(treeView.getRow(currentEntry) + 1, newChild);
        bcmHashMap.put(newChild, new BcmEntry());

        int[] index = {0};
        renameTreeItems(treeView.getRoot(), index);
    }

    public void renameTreeItems(TreeItem<String> root, int[] index) {
        if (root == null) return;

        root.setValue("Entry " + index[0]);

        allEntries.set(index[0], root);

        index[0]++;
        
        for (TreeItem<String> child : root.getChildren()) {
            renameTreeItems(child, index);
        }
    }

    public void bcmReader(Path path) {
        try(FileChannel channel = FileChannel.open(path, StandardOpenOption.READ)) {
            int bcmEntryCount;

            ByteBuffer shortBuffer = ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN);
            ByteBuffer intBuffer = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);

            channel.position(8);
            intBuffer.clear();
            channel.read(intBuffer);
            intBuffer.flip();
            bcmEntryCount = intBuffer.getInt();
            
            allEntries = new ArrayList<>(bcmEntryCount);

            for (int i = 0; i < bcmEntryCount; i++) {
                allEntries.add(new TreeItem<>("Entry " + i));
            }

            if (bcmEntryCount > 0) {
                treeView.setRoot(allEntries.get(0));
            }

            for (int i = 0; i < bcmEntryCount; i++) {
                bcmHashMap.put(allEntries.get(i), new BcmEntry());

                int entryStartOffset = 16 + (i * 112);
                int siblingOffset = entryStartOffset + 48;
                int childOffset = entryStartOffset + 52;
                
                channel.position(entryStartOffset);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                bcmHashMap.get(allEntries.get(i)).skillConditions = toUint32(intBuffer.getInt());

                channel.position(entryStartOffset + 4);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                bcmHashMap.get(allEntries.get(i)).directionalInputs = toUint32(intBuffer.getInt());
                
                channel.position(entryStartOffset + 8);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                bcmHashMap.get(allEntries.get(i)).buttonInputs = toUint32(intBuffer.getInt());
            
                channel.position(entryStartOffset + 12);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                bcmHashMap.get(allEntries.get(i)).holdDownConditions = toUint32(intBuffer.getInt());
                
                channel.position(entryStartOffset + 16);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                bcmHashMap.get(allEntries.get(i)).opponentSizeConditions = toUint32(intBuffer.getInt());
                
                channel.position(entryStartOffset + 20);
                shortBuffer.clear();
                channel.read(shortBuffer);
                shortBuffer.flip();
                bcmHashMap.get(allEntries.get(i)).minimumLoopDuration = toUShort(shortBuffer.getShort());

                channel.position(entryStartOffset + 22);
                shortBuffer.clear();
                channel.read(shortBuffer);
                shortBuffer.flip();
                bcmHashMap.get(allEntries.get(i)).maximumLoopDuration = toUShort(shortBuffer.getShort());

                channel.position(entryStartOffset + 24);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                bcmHashMap.get(allEntries.get(i)).primaryActivatorConditions = toUint32(intBuffer.getInt());
                
                channel.position(entryStartOffset + 28);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                bcmHashMap.get(allEntries.get(i)).activatorState = toUint32(intBuffer.getInt());
              
                channel.position(entryStartOffset + 32);
                shortBuffer.clear();
                channel.read(shortBuffer);
                shortBuffer.flip();
                bcmHashMap.get(allEntries.get(i)).bacEntryPrimary = shortBuffer.getShort();

                channel.position(entryStartOffset + 34);
                shortBuffer.clear();
                channel.read(shortBuffer);
                shortBuffer.flip();
                bcmHashMap.get(allEntries.get(i)).bacEntryCharge = shortBuffer.getShort();

                channel.position(entryStartOffset + 36);
                shortBuffer.clear();
                channel.read(shortBuffer);
                shortBuffer.flip();
                bcmHashMap.get(allEntries.get(i)).i36 = shortBuffer.getShort();

                channel.position(entryStartOffset + 38);
                shortBuffer.clear();
                channel.read(shortBuffer);
                shortBuffer.flip();
                bcmHashMap.get(allEntries.get(i)).bacEntryUserConnect = shortBuffer.getShort();

                channel.position(entryStartOffset + 40);
                shortBuffer.clear();
                channel.read(shortBuffer);
                shortBuffer.flip();
                bcmHashMap.get(allEntries.get(i)).bacEntryVictimConnect = shortBuffer.getShort();

                channel.position(entryStartOffset + 42);
                shortBuffer.clear();
                channel.read(shortBuffer);
                shortBuffer.flip();
                bcmHashMap.get(allEntries.get(i)).bacEntryAirborne = shortBuffer.getShort();

                channel.position(entryStartOffset + 44);
                shortBuffer.clear();
                channel.read(shortBuffer);
                shortBuffer.flip();
                bcmHashMap.get(allEntries.get(i)).bacEntryTargetingOverride = toUShort(shortBuffer.getShort());
                
                channel.position(entryStartOffset + 46);
                shortBuffer.clear();
                channel.read(shortBuffer);
                shortBuffer.flip();
                bcmHashMap.get(allEntries.get(i)).bacRandomFlags = toUShort(shortBuffer.getShort());
               
                channel.position(siblingOffset);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                int siblingPointer = intBuffer.getInt();

                if (siblingPointer != 0) {
                    TreeItem<String> newSiblingEntry = allEntries.get(i).getParent();

                    if (newSiblingEntry != null) {
                        int siblingEntry = (siblingPointer - 16) / 112;

                        allEntries.get(i).getParent().getChildren().add(allEntries.get(siblingEntry));
                    }
                }
                   
                channel.position(childOffset);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                int childPointer = intBuffer.getInt();

                if (childPointer != 0) {
                    int childEntry = (childPointer - 16) / 112;
                    allEntries.get(i).getChildren().add(allEntries.get(childEntry));
                }

                channel.position(entryStartOffset + 64);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                bcmHashMap.get(allEntries.get(i)).kiCost = toUint32(intBuffer.getInt());

                channel.position(entryStartOffset + 68);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                bcmHashMap.get(allEntries.get(i)).i68 = toUint32(intBuffer.getInt());

                channel.position(entryStartOffset + 72);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                bcmHashMap.get(allEntries.get(i)).i72 = toUint32(intBuffer.getInt());

                channel.position(entryStartOffset + 76);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                bcmHashMap.get(allEntries.get(i)).receiverLinkId = toUint32(intBuffer.getInt());

                channel.position(entryStartOffset + 80);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                bcmHashMap.get(allEntries.get(i)).i80 = toUint32(intBuffer.getInt());

                channel.position(entryStartOffset + 84);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                bcmHashMap.get(allEntries.get(i)).staminaCost = toUint32(intBuffer.getInt());

                channel.position(entryStartOffset + 88);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                bcmHashMap.get(allEntries.get(i)).i88 = toUint32(intBuffer.getInt());

                channel.position(entryStartOffset + 92);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                bcmHashMap.get(allEntries.get(i)).kiRequired = toUint32(intBuffer.getInt());

                channel.position(entryStartOffset + 96);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                bcmHashMap.get(allEntries.get(i)).healthRequired = intBuffer.getFloat();

                channel.position(entryStartOffset + 100);
                shortBuffer.clear();
                channel.read(shortBuffer);
                shortBuffer.flip();
                bcmHashMap.get(allEntries.get(i)).transformationStage = shortBuffer.getShort();

                channel.position(entryStartOffset + 102);
                shortBuffer.clear();
                channel.read(shortBuffer);
                shortBuffer.flip();
                bcmHashMap.get(allEntries.get(i)).cusAura = shortBuffer.getShort();

                channel.position(entryStartOffset + 104);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                bcmHashMap.get(allEntries.get(i)).i104 = toUint32(intBuffer.getInt());
                
                channel.position(entryStartOffset + 108);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                bcmHashMap.get(allEntries.get(i)).characterCondition = toUint32(intBuffer.getInt());
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void bcmWriter(Path path) {
        try(FileChannel channel = FileChannel.open(path, StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            int zeroEntryOffset = 16;
            int currentParent = 0;

            ByteBuffer shortBuffer = ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN);
            ByteBuffer intBuffer = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
        
            channel.position(0);
            channel.write(ByteBuffer.wrap(new byte[]{0x23, 0x42, 0x43, (byte)0x4D}));

            channel.position(4);
            channel.write(ByteBuffer.wrap(new byte[]{(byte)0xFE, (byte)0xFF}));

            channel.position(6);
            channel.write(ByteBuffer.wrap(new byte[]{0x00, 0x00}));

            channel.position(8);
            intBuffer.clear();
            intBuffer.putInt(allEntries.size());
            intBuffer.flip();
            channel.write(intBuffer);

            channel.position(12);
            intBuffer.clear();
            intBuffer.putInt(zeroEntryOffset);
            intBuffer.flip();
            channel.write(intBuffer);
            
            for (int i = 0; i < allEntries.size(); i++) {
                int entryStartOffset = 16 + (112 * i);
                int siblingOffset = entryStartOffset + 48;
                int childOffset = entryStartOffset + 52;
                int rootParentOffset = entryStartOffset + 56;
                int parentOffset = entryStartOffset + 60;
                
                channel.position(entryStartOffset);
                intBuffer.clear();
                intBuffer.putInt(((int) bcmHashMap.get(allEntries.get(i)).skillConditions));
                intBuffer.flip();
                channel.write(intBuffer);
                    
                channel.position(entryStartOffset + 4);
                intBuffer.clear();
                intBuffer.putInt((int) bcmHashMap.get(allEntries.get(i)).directionalInputs);
                intBuffer.flip();
                channel.write(intBuffer);

                channel.position(entryStartOffset + 8);
                intBuffer.clear();
                intBuffer.putInt((int) bcmHashMap.get(allEntries.get(i)).buttonInputs);
                intBuffer.flip();
                channel.write(intBuffer);

                channel.position(entryStartOffset + 12);
                intBuffer.clear();
                intBuffer.putInt((int) bcmHashMap.get(allEntries.get(i)).holdDownConditions);
                intBuffer.flip();
                channel.write(intBuffer);

                channel.position(entryStartOffset + 16);
                intBuffer.clear();
                intBuffer.putInt((int)bcmHashMap.get(allEntries.get(i)).opponentSizeConditions);
                intBuffer.flip();
                channel.write(intBuffer);

                channel.position(entryStartOffset + 20);
                shortBuffer.clear();
                shortBuffer.putShort((short)bcmHashMap.get(allEntries.get(i)).minimumLoopDuration);
                shortBuffer.flip();
                channel.write(shortBuffer);

                channel.position(entryStartOffset + 22);
                shortBuffer.clear();
                shortBuffer.putShort((short)bcmHashMap.get(allEntries.get(i)).maximumLoopDuration);
                shortBuffer.flip();
                channel.write(shortBuffer);

                channel.position(entryStartOffset + 24);
                intBuffer.clear();
                intBuffer.putInt((int)bcmHashMap.get(allEntries.get(i)).primaryActivatorConditions);
                intBuffer.flip();
                channel.write(intBuffer);

                channel.position(entryStartOffset + 28);
                intBuffer.clear();
                intBuffer.putInt((int)bcmHashMap.get(allEntries.get(i)).activatorState);
                intBuffer.flip();
                channel.write(intBuffer);

                channel.position(entryStartOffset + 32);
                shortBuffer.clear();
                shortBuffer.putShort(bcmHashMap.get(allEntries.get(i)).bacEntryPrimary);
                shortBuffer.flip();
                channel.write(shortBuffer);

                channel.position(entryStartOffset + 34);
                shortBuffer.clear();
                shortBuffer.putShort(bcmHashMap.get(allEntries.get(i)).bacEntryCharge);
                shortBuffer.flip();
                channel.write(shortBuffer);

                channel.position(entryStartOffset + 36);
                shortBuffer.clear();
                shortBuffer.putShort(bcmHashMap.get(allEntries.get(i)).i36);
                shortBuffer.flip();
                channel.write(shortBuffer);

                channel.position(entryStartOffset + 38);
                shortBuffer.clear();
                shortBuffer.putShort(bcmHashMap.get(allEntries.get(i)).bacEntryUserConnect);
                shortBuffer.flip();
                channel.write(shortBuffer);

                channel.position(entryStartOffset + 40);
                shortBuffer.clear();
                shortBuffer.putShort(bcmHashMap.get(allEntries.get(i)).bacEntryVictimConnect);
                shortBuffer.flip();
                channel.write(shortBuffer);

                channel.position(entryStartOffset + 42);
                shortBuffer.clear();
                shortBuffer.putShort(bcmHashMap.get(allEntries.get(i)).bacEntryAirborne);
                shortBuffer.flip();
                channel.write(shortBuffer);

                channel.position(entryStartOffset + 44);
                shortBuffer.clear();
                shortBuffer.putShort((short)bcmHashMap.get(allEntries.get(i)).bacEntryTargetingOverride);
                shortBuffer.flip();
                channel.write(shortBuffer);

                channel.position(entryStartOffset + 46);
                shortBuffer.clear();
                shortBuffer.putShort((short)bcmHashMap.get(allEntries.get(i)).bacRandomFlags);
                shortBuffer.flip();
                channel.write(shortBuffer);

                channel.position(siblingOffset);
                intBuffer.clear();
                if (allEntries.get(i).nextSibling() != null) {
                    intBuffer.putInt(allEntries.indexOf(allEntries.get(i).nextSibling()) * 112 + 16);
                }
                else {
                    intBuffer.putInt(0);
                }
                intBuffer.flip();
                channel.write(intBuffer);

                channel.position(childOffset);
                intBuffer.clear();
                if (!allEntries.get(i).getChildren().isEmpty()) {
                    intBuffer.putInt((allEntries.indexOf(allEntries.get(i).getChildren().get(0))) * 112 + 16);
                }
                else {
                    intBuffer.putInt(0);
                }
                intBuffer.flip();
                channel.write(intBuffer);

                channel.position(parentOffset);
                intBuffer.clear();
                if (allEntries.indexOf(allEntries.get(i).getParent()) == 0 && i != 0) {
                    intBuffer.putInt(allEntries.indexOf(allEntries.get(i)) * 112 + 16);
                    currentParent = allEntries.indexOf(allEntries.get(i))* 112 + 16;
                    intBuffer.flip();
                    channel.write(intBuffer);
                }
                else if (allEntries.indexOf(allEntries.get(i).getParent()) != 0 && i != 0) {
                    intBuffer.putInt(allEntries.indexOf(allEntries.get(i).getParent()) * 112 + 16);
                    intBuffer.flip();
                    channel.write(intBuffer);

                    channel.position(rootParentOffset);
                    intBuffer.clear();
                    intBuffer.putInt(currentParent);
                    intBuffer.flip();
                    channel.write(intBuffer);
                }
                else {
                    intBuffer.putInt(0);
                    intBuffer.flip();
                    channel.write(intBuffer);
                }
             
                channel.position(entryStartOffset + 64);
                intBuffer.clear();
                intBuffer.putInt((int) bcmHashMap.get(allEntries.get(i)).kiCost);
                intBuffer.flip();
                channel.write(intBuffer);

                channel.position(entryStartOffset + 68);
                intBuffer.clear();
                intBuffer.putInt((int) bcmHashMap.get(allEntries.get(i)).i68);
                intBuffer.flip();
                channel.write(intBuffer);

                channel.position(entryStartOffset + 72);
                intBuffer.clear();
                intBuffer.putInt((int) bcmHashMap.get(allEntries.get(i)).i72);
                intBuffer.flip();
                channel.write(intBuffer);

                channel.position(entryStartOffset + 76);
                intBuffer.clear();
                intBuffer.putInt((int) bcmHashMap.get(allEntries.get(i)).receiverLinkId);
                intBuffer.flip();
                channel.write(intBuffer);

                channel.position(entryStartOffset + 80);
                intBuffer.clear();
                intBuffer.putInt((int) bcmHashMap.get(allEntries.get(i)).i80);
                intBuffer.flip();
                channel.write(intBuffer);

                channel.position(entryStartOffset + 84);
                intBuffer.clear();
                intBuffer.putInt((int) bcmHashMap.get(allEntries.get(i)).staminaCost);
                intBuffer.flip();
                channel.write(intBuffer);

                channel.position(entryStartOffset + 88);
                intBuffer.clear();
                intBuffer.putInt((int) bcmHashMap.get(allEntries.get(i)).i88);
                intBuffer.flip();
                channel.write(intBuffer);

                channel.position(entryStartOffset + 92);
                intBuffer.clear();
                intBuffer.putInt((int) bcmHashMap.get(allEntries.get(i)).kiRequired);
                intBuffer.flip();
                channel.write(intBuffer);

                channel.position(entryStartOffset + 96);
                intBuffer.clear();
                intBuffer.putFloat(bcmHashMap.get(allEntries.get(i)).healthRequired);
                intBuffer.flip();
                channel.write(intBuffer);

                channel.position(entryStartOffset + 100);
                intBuffer.clear();
                intBuffer.putInt(bcmHashMap.get(allEntries.get(i)).transformationStage);
                intBuffer.flip();
                channel.write(intBuffer);

                channel.position(entryStartOffset + 102);
                intBuffer.clear();
                intBuffer.putInt(bcmHashMap.get(allEntries.get(i)).cusAura);
                intBuffer.flip();
                channel.write(intBuffer);

                channel.position(entryStartOffset + 104);
                intBuffer.clear();
                intBuffer.putInt((int) bcmHashMap.get(allEntries.get(i)).i104);
                intBuffer.flip();
                channel.write(intBuffer);

                channel.position(entryStartOffset + 108);
                intBuffer.clear();
                intBuffer.putInt((int) bcmHashMap.get(allEntries.get(i)).characterCondition);
                intBuffer.flip();
                channel.write(intBuffer);
            }
        }
         catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static enum SkillConditions {
        None(0),
        UseSkillUpgrades(1),
        Unknown2(2),
        Unknown3(4),
        Unknown4(8),
        OpponentRachedGround(16);

        final int index;

        SkillConditions(int index) {
            this.index = index;
        }
    }

    public static enum OpponentSizeConditions {
        AllSizes(0),
        Unknown1(1),
        Unknown2(3),
        SmallCharacters(131072),
        DefaultSize(262144),
        Medium(327680),
        MeduimLarge(393216),
        Large(458752),
        GreatApe(524288);

        final long index;

        OpponentSizeConditions(long index) {
            this.index = index;
        }
    }

    public static enum RandomFlags {
        None(0),
        Random_BAC_Entry(1),
        NoTargetCorrection(2),
        ThreeInstanceSetup(3),
        Unknown4(4),
        Unknown6(6);

        final int index;

        RandomFlags(int index) {
            this.index = index;
        }
    }

    public static enum CharacterConditions {
        None(0),
        CustomCharacter(1),
        HumanMale(2),
        HumanFemale(3),
        SaiyanMale(4),
        SaiyanFemale(5),
        Namekian(6),
        FriezaRace(7),
        MajinMale(8),
        MajinFemale(9);

        final int index;

        CharacterConditions(int index) {
            this.index = index;
        }
    }

    public static enum BcmValues {
        Mode,
        DirectionalInput,
        ButtonInput,
        HoldDownConditions,
        OpponentSizeConditions,
        MinimumLoopDuration,
        MaximumLoopDuration,
        PrimaryActivatorConditions,
        ActivatorState,
        BAC_EntryPrimary,
        BAC_EntryCharge,
        I36,
        BAC_EntryUserConnect,
        BAC_EntryVictimConnect,
        BAC_EntryAirborne,
        BAC_EntryTargetingOverride,
        BAC_RandomFlags,
        KiCost,
        I68,
        I72,
        ReceiverLinkID,
        I80,
        StaminaCost,
        I88,
        KiRequired,
        HealthRequired,
        TransformationStage,
        CUS_Aura,
        I104,
        CharacterCondition;
    }
}

class BcmEntry {
    public long skillConditions;
    public long directionalInputs;
    public long buttonInputs;
    public long holdDownConditions;
    public long opponentSizeConditions;
    public int minimumLoopDuration;
    public int maximumLoopDuration;
    public long primaryActivatorConditions;
    public long activatorState;
    public short bacEntryPrimary;
    public short bacEntryCharge;
    public short i36;
    public short bacEntryUserConnect;
    public short bacEntryVictimConnect;
    public short bacEntryAirborne;
    public int bacEntryTargetingOverride;
    public int bacRandomFlags;
    public long kiCost;
    public long i68;
    public long i72;
    public long receiverLinkId;
    public long i80;
    public long staminaCost;
    public long i88;
    public long kiRequired;
    public float healthRequired;
    public short transformationStage;
    public short cusAura;
    public long i104;
    public long characterCondition;
    
    public BcmEntry() {}

    public BcmEntry(BcmEntry other) {
        this.skillConditions = other.skillConditions;
        this.directionalInputs = other.directionalInputs;
        this.buttonInputs = other.buttonInputs;
        this.holdDownConditions = other.holdDownConditions;
        this.opponentSizeConditions = other.opponentSizeConditions;
        this.minimumLoopDuration = other.minimumLoopDuration;
        this.maximumLoopDuration = other.maximumLoopDuration;
        this.primaryActivatorConditions = other.primaryActivatorConditions;
        this.activatorState = other.activatorState;
        this.bacEntryPrimary = other.bacEntryPrimary;
        this.bacEntryCharge = other.bacEntryCharge;
        this.i36 = other.i36;
        this.bacEntryUserConnect = other.bacEntryUserConnect;
        this.bacEntryVictimConnect = other.bacEntryVictimConnect;
        this.bacEntryAirborne = other.bacEntryAirborne;
        this.bacEntryTargetingOverride = other.bacEntryTargetingOverride;
        this.bacRandomFlags = other.bacRandomFlags;
        this.kiCost = other.kiCost;
        this.i68 = other.i68;
        this.i72 = other.i72;
        this.receiverLinkId = other.receiverLinkId;
        this.i80 = other.i80;
        this.staminaCost = other.staminaCost;
        this.i88 = other.i88;
        this.kiRequired = other.kiRequired;
        this.healthRequired = other.healthRequired;
        this.transformationStage = other.transformationStage;
        this.cusAura = other.cusAura;
        this.i104 = other.i104;
        this.characterCondition = other.characterCondition;
    }
}