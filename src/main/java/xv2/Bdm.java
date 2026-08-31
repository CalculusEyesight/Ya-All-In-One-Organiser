package xv2;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import static xv2.BinaryUtilities.toUShort;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Spinner;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class Bdm {
    ArrayList<BdmEntry> bdmEntries = new ArrayList<>();

    ListView <String> listView = new ListView<>();
    TabPane mainTabPane = new TabPane();

    ArrayList<BdmEntry> copyContainer = new ArrayList<>();

    ContextMenu contextMenu = new ContextMenu();
    MenuItem copy = new MenuItem("Copy Ctrl+C");
    MenuItem paste = new MenuItem("Paste Ctrl+V");
    MenuItem delete = new MenuItem("Delete Del");
    MenuItem append = new MenuItem("Append Ctrl+A");
    MenuItem insert = new MenuItem("Insert Ctrl+I");

    public Bdm() {
        entriesActionListener();
        entriesKeysListener();
        tabActionListener();
    }

    public HBox createMainHBox() {
        createMainTabPane();
        createSubTabPane();

        HBox hBox = new HBox(listView, mainTabPane);
        HBox.setHgrow(mainTabPane, Priority.ALWAYS);
        hBox.getStylesheets().add(getClass().getResource("/style.css").toExternalForm()); 

        return hBox;
    }

    public void createMainTabPane() {
        if (mainTabPane.getTabs().isEmpty()) {
            Tab defaultTab = new Tab("0: Default");
            Tab counterHitFrontTab = new Tab("1: Counter Hit (Front)");
            Tab primaryKnockbackTab = new Tab("2: Primary Knockback");
            Tab backTab = new Tab("3: Back");
            Tab groundImpactTab = new Tab("4: Ground Impact");
            Tab guardingTab = new Tab("5: Guarding");
            Tab stumbleTab = new Tab("6: Stumble");
            Tab counterHitBackTab = new Tab("7: Counter Hit (Back)");
            Tab floatingKnockbackTab = new Tab("8: Floating Knockback");
            Tab lyingOnGroundTab = new Tab("9: Lying On Ground");

            defaultTab.setClosable(false);
            counterHitFrontTab.setClosable(false);
            primaryKnockbackTab.setClosable(false);
            backTab.setClosable(false);
            groundImpactTab.setClosable(false);
            guardingTab.setClosable(false);
            stumbleTab.setClosable(false);
            counterHitBackTab.setClosable(false);
            floatingKnockbackTab.setClosable(false);
            lyingOnGroundTab.setClosable(false);

            defaultTab.setContent(createSubTabPane());
            counterHitFrontTab.setContent(createSubTabPane());
            primaryKnockbackTab.setContent(createSubTabPane());
            backTab.setContent(createSubTabPane());
            groundImpactTab.setContent(createSubTabPane());
            guardingTab.setContent(createSubTabPane());
            stumbleTab.setContent(createSubTabPane());
            counterHitBackTab.setContent(createSubTabPane());
            floatingKnockbackTab.setContent(createSubTabPane());
            lyingOnGroundTab.setContent(createSubTabPane());

            mainTabPane.getTabs().addAll(
                defaultTab, counterHitFrontTab, 
                primaryKnockbackTab, backTab, 
                groundImpactTab, guardingTab,
                stumbleTab, counterHitBackTab,
                floatingKnockbackTab, lyingOnGroundTab
            );
        }
    }

    public TabPane createSubTabPane() {
        TabPane subTabPane = new TabPane();
    
        Tab mainTab = new Tab("Main");
        Tab animationTab = new Tab("Animation");
        Tab soundTab = new Tab("Sound");
        Tab effectsTab = new Tab("Effects");
        Tab pushbackStunKnockbacTab = new Tab("Pushback/Stun/Knockback");
        Tab cameraTab = new Tab("Camera");
        Tab miscTab = new Tab("Misc");
        Tab unknownTab = new Tab("Unknown");

        mainTab.setClosable(false);
        animationTab.setClosable(false);
        soundTab.setClosable(false);
        effectsTab.setClosable(false);
        pushbackStunKnockbacTab.setClosable(false);
        cameraTab.setClosable(false);
        miscTab.setClosable(false);
        unknownTab.setClosable(false);
    
        subTabPane.getTabs().addAll(
            mainTab, animationTab,
            soundTab, effectsTab,
            pushbackStunKnockbacTab, cameraTab,
            miscTab, unknownTab
        );
        return subTabPane;
    }

    private VBox createMainVBox(BdmSubEntry subEntry) {
        CheckBox[] healthPropertiesList = new CheckBox[] {
            new CheckBox("Restore Health"),
            new CheckBox("Unknown 2"),
            new CheckBox("Unknown 3"),
            new CheckBox("Unknown 4"),
        };

        CheckBox[] unknownList = new CheckBox[] {
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

        Node[] secondaryType = new Node[] {
            createCheckBoxGroup("Health Properties", healthPropertiesList, 1, BdmGroups.SecondaryType),
            createCheckBoxGroup("Unknown", unknownList, 16, BdmGroups.SecondaryType), 
            createCheckBoxGroup("Damage Priorities", damageProperties, 256, BdmGroups.SecondaryType),
            createCheckBoxGroup("Damage Orientation", damageOrientation, 4096, BdmGroups.SecondaryType)
        };

        ToggleGroup damageTypeToggleGroup = new ToggleGroup();

        RadioButton[] damageTypeRadioButtonsList = new RadioButton[] {
            createRadioButton("No Effect", damageTypeToggleGroup, subEntry.damageType, DamageTypes.NoEffect),
            createRadioButton("Block", damageTypeToggleGroup, subEntry.damageType, DamageTypes.Block),
            createRadioButton("Guard Break", damageTypeToggleGroup, subEntry.damageType, DamageTypes.GuardBreak),
            createRadioButton("Standard", damageTypeToggleGroup, subEntry.damageType, DamageTypes.Standard),
            createRadioButton("Heavy", damageTypeToggleGroup, subEntry.damageType, DamageTypes.Heavy),
            createRadioButton("Knockback", damageTypeToggleGroup, subEntry.damageType, DamageTypes.Knockback),
            createRadioButton("Knockback 1", damageTypeToggleGroup, subEntry.damageType, DamageTypes.Knockback1),
            createRadioButton("Knockback 2", damageTypeToggleGroup, subEntry.damageType, DamageTypes.Knockback2),
            createRadioButton("Knockback 3", damageTypeToggleGroup, subEntry.damageType, DamageTypes.Knockback3),
            createRadioButton("Knockback 4", damageTypeToggleGroup, subEntry.damageType, DamageTypes.Knockback4),
            createRadioButton("Grab", damageTypeToggleGroup, subEntry.damageType, DamageTypes.Grab),
            createRadioButton("Hold Stomach", damageTypeToggleGroup, subEntry.damageType, DamageTypes.HoldStomach),
            createRadioButton("Hold Eyes", damageTypeToggleGroup, subEntry.damageType, DamageTypes.HoldEyes),
            createRadioButton("Knockback 5", damageTypeToggleGroup, subEntry.damageType, DamageTypes.Knockback5),
            createRadioButton("Electric", damageTypeToggleGroup, subEntry.damageType, DamageTypes.Electric),
            createRadioButton("Dazed", damageTypeToggleGroup, subEntry.damageType, DamageTypes.Dazed),
            createRadioButton("Paralysis", damageTypeToggleGroup, subEntry.damageType, DamageTypes.Paralysis),
            createRadioButton("Freeze", damageTypeToggleGroup, subEntry.damageType, DamageTypes.Freeze),
            createRadioButton("Wild-Card", damageTypeToggleGroup, subEntry.damageType, DamageTypes.WildCard),
            createRadioButton("Unused", damageTypeToggleGroup, subEntry.damageType, DamageTypes.Unused),
            createRadioButton("Heavy Stamina Break", damageTypeToggleGroup, subEntry.damageType, DamageTypes.HeavyStaminaBreak),
            createRadioButton("Light Stamina Break", damageTypeToggleGroup, subEntry.damageType, DamageTypes.LightStaminaBreak),
            createRadioButton("Gigantic Ki Blast Push", damageTypeToggleGroup, subEntry.damageType, DamageTypes.GiganticKiBlastPush),
            createRadioButton("Brain Wash", damageTypeToggleGroup, subEntry.damageType, DamageTypes.BrainWash),
            createRadioButton("Gigantic Ki Blast Return", damageTypeToggleGroup, subEntry.damageType, DamageTypes.GiganticKiBlastReturn),
            createRadioButton("Knockback 6", damageTypeToggleGroup, subEntry.damageType, DamageTypes.Knockback6),
            createRadioButton("Knockback 7", damageTypeToggleGroup, subEntry.damageType, DamageTypes.Knockback7),
            createRadioButton("Knockback 8", damageTypeToggleGroup, subEntry.damageType, DamageTypes.Knockback8),
            createRadioButton("Knockback 9", damageTypeToggleGroup, subEntry.damageType, DamageTypes.Knockback9),
            createRadioButton("Slow Opponent", damageTypeToggleGroup, subEntry.damageType, DamageTypes.SlowOpponent),
            createRadioButton("Brain Wash 2", damageTypeToggleGroup, subEntry.damageType, DamageTypes.BrainWash2),
            createRadioButton("Time Stop", damageTypeToggleGroup, subEntry.damageType, DamageTypes.TimeStop),
        };

        VBox mainVBox = new VBox(45,
            createHBox(0, createLabel("Damage Type", 160), createGridPane(4, 8, damageTypeRadioButtonsList)), 
            createHBox(0, createLabel("Secondary Type", 160), createHBox(5, secondaryType, false)),
            createHBox(0, createLabel("Damage Amount", 160), createSpinner(0, 65535, subEntry.damageAmount, BdmValues.DamageAmount)), 
            createHBox(0, createLabel("Damage Special", 160), createSpinner(0, 65535, subEntry.damageSpecial, BdmValues.DamageSpecial)),
            createHBox(0, createLabel("Damage Special 2", 160), createSpinner(0, 65535, subEntry.damageSpecial2, BdmValues.DamageSpecial2)), 
            createHBox(0, createLabel("Damage Special 3", 160), createSpinner(0, 65535, subEntry.damageSpecial3, BdmValues.DamageSpecial3))
        );
        mainVBox.setPadding(new Insets(20, 0, 0, 8));

        return mainVBox;
    }

    private VBox createAnimationVBox(BdmSubEntry subEntry) {
        VBox animationVBox = new VBox(30, 
            createHBox(0, createLabel("User Animation Time", 160), createSpinner(0, 65535, subEntry.userAnimationTime, BdmValues.UserAnimationTime)), 
            createHBox(0, createLabel("User Animation Speed", 160), createSpinner(Float.MIN_VALUE, Float.MAX_VALUE, (double)subEntry.userAnimationSpeed, BdmValues.UserAnimationSpeed)),
            createHBox(0, createLabel("Victim Animation Time", 160), createSpinner(0, 65535, subEntry.victimAnimationTime, BdmValues.VictimAnimationTime)), 
            createHBox(0, createLabel("Victim Animation Speed", 160), createSpinner(Float.MIN_VALUE, Float.MAX_VALUE, (double)subEntry.victimAnimationSpeed, BdmValues.VictimAnimationSpeed))
        );
        animationVBox.setPadding(new Insets(20, 0, 0, 8));

        return animationVBox;
    }

    private VBox createSoundVBox(BdmSubEntry subEntry) {
        ToggleGroup acbTypeToggleGroup = new ToggleGroup();

        RadioButton[] radioButtonsList = new RadioButton[] {
            createRadioButton("Common", acbTypeToggleGroup, subEntry.acbType, ACBTypes.Common), 
            createRadioButton("Character SE", acbTypeToggleGroup, subEntry.acbType, ACBTypes.CharacterSE),
            createRadioButton("Character VOX", acbTypeToggleGroup, subEntry.acbType, ACBTypes.CharacterVOX),
            createRadioButton("Skill SE", acbTypeToggleGroup, subEntry.acbType, ACBTypes.SkillSE),
            createRadioButton("Skill VOX", acbTypeToggleGroup, subEntry.acbType, ACBTypes.SkillVOX)
        };

        VBox soundVBox = new VBox(30, 
            createHBox(0, createLabel("ACB Type", 100), createHBox(15, radioButtonsList, true)), 
            createHBox(0, createLabel("Cue ID", 100), createSpinner(Short.MIN_VALUE, Short.MAX_VALUE, subEntry.cueId, BdmValues.Cue_ID))
        );
        soundVBox.setPadding(new Insets(20, 0, 0, 8));

        return soundVBox;
    }

    private ScrollPane createEffectsScrollPane(BdmSubEntry subEntry) {
        ToggleGroup effect1EepkTypeToggleGroup = new ToggleGroup();

        RadioButton[] effect1List = new RadioButton[] {
            createRadioButton("Common", effect1EepkTypeToggleGroup, subEntry.effect1EepkType, Effect_EEPK_Types.Common, 1),
            createRadioButton("StageBG", effect1EepkTypeToggleGroup, subEntry.effect1EepkType, Effect_EEPK_Types.StageBG, 1),
            createRadioButton("Character", effect1EepkTypeToggleGroup, subEntry.effect1EepkType, Effect_EEPK_Types.CharacterEffect, 1),
            createRadioButton("Awoken Skill", effect1EepkTypeToggleGroup, subEntry.effect1EepkType, Effect_EEPK_Types.AwokenSkill, 1),
            createRadioButton("Super Skill", effect1EepkTypeToggleGroup, subEntry.effect1EepkType, Effect_EEPK_Types.SuperSkill, 1),
            createRadioButton("Ultimate Skill", effect1EepkTypeToggleGroup, subEntry.effect1EepkType, Effect_EEPK_Types.UltimateSkill, 1),
            createRadioButton("Evasive Skill", effect1EepkTypeToggleGroup, subEntry.effect1EepkType, Effect_EEPK_Types.EvasiveSkill, 1),
            createRadioButton("Ki Blast Skill", effect1EepkTypeToggleGroup, subEntry.effect1EepkType, Effect_EEPK_Types.KiBlastSkill, 1),
            createRadioButton("Stage", effect1EepkTypeToggleGroup, subEntry.effect1EepkType, Effect_EEPK_Types.StageEffect, 1),
        };

        ToggleGroup effect2EepkTypeToggleGroup = new ToggleGroup();

        RadioButton[] effect2List = new RadioButton[] {
            createRadioButton("Common", effect2EepkTypeToggleGroup, subEntry.effect2EepkType, Effect_EEPK_Types.Common, 2),
            createRadioButton("StageBG", effect2EepkTypeToggleGroup, subEntry.effect2EepkType, Effect_EEPK_Types.StageBG, 2),
            createRadioButton("Character", effect2EepkTypeToggleGroup, subEntry.effect2EepkType, Effect_EEPK_Types.CharacterEffect, 2),
            createRadioButton("Awoken Skill", effect2EepkTypeToggleGroup, subEntry.effect2EepkType, Effect_EEPK_Types.AwokenSkill, 2),
            createRadioButton("Super Skill", effect2EepkTypeToggleGroup, subEntry.effect2EepkType, Effect_EEPK_Types.SuperSkill, 2),
            createRadioButton("Ultimate Skill", effect2EepkTypeToggleGroup, subEntry.effect2EepkType, Effect_EEPK_Types.UltimateSkill, 2),
            createRadioButton("Evasive Skill", effect2EepkTypeToggleGroup, subEntry.effect2EepkType, Effect_EEPK_Types.EvasiveSkill, 2),
            createRadioButton("Ki Blast Skill", effect2EepkTypeToggleGroup, subEntry.effect2EepkType, Effect_EEPK_Types.KiBlastSkill, 2),
            createRadioButton("Stage", effect2EepkTypeToggleGroup, subEntry.effect2EepkType, Effect_EEPK_Types.StageEffect, 2),
        };

        ToggleGroup effect3EepkTypeToggleGroup = new ToggleGroup();

        RadioButton[] effect3List = new RadioButton[] {
            createRadioButton("Common", effect3EepkTypeToggleGroup, subEntry.effect3EepkType, Effect_EEPK_Types.Common, 3),
            createRadioButton("StageBG", effect3EepkTypeToggleGroup, subEntry.effect3EepkType, Effect_EEPK_Types.StageBG, 3),
            createRadioButton("Character", effect3EepkTypeToggleGroup, subEntry.effect3EepkType, Effect_EEPK_Types.CharacterEffect, 3),
            createRadioButton("Awoken Skill", effect3EepkTypeToggleGroup, subEntry.effect3EepkType, Effect_EEPK_Types.AwokenSkill, 3),
            createRadioButton("Super Skill", effect3EepkTypeToggleGroup, subEntry.effect3EepkType, Effect_EEPK_Types.SuperSkill, 3),
            createRadioButton("Ultimate Skill", effect3EepkTypeToggleGroup, subEntry.effect3EepkType, Effect_EEPK_Types.UltimateSkill, 3),
            createRadioButton("Evasive Skill", effect3EepkTypeToggleGroup, subEntry.effect3EepkType, Effect_EEPK_Types.EvasiveSkill, 3),
            createRadioButton("Ki Blast Skill", effect3EepkTypeToggleGroup, subEntry.effect3EepkType, Effect_EEPK_Types.KiBlastSkill, 3),
            createRadioButton("Stage", effect3EepkTypeToggleGroup, subEntry.effect3EepkType, Effect_EEPK_Types.StageEffect, 3),
        };

        VBox effectsVBox = new VBox(35, 
            createHBox(0, createLabel("Effect 1 ID", 160), createSpinner(Short.MIN_VALUE, Short.MAX_VALUE, subEntry.effect1Id, BdmValues.Effect1_ID)), 
            createHBox(0, createLabel("Effect 1 Skill ID", 160), createSpinner(0, 65535, subEntry.effect1SkillId, BdmValues.Effect1_Skill_ID)),
            createHBox(0, createLabel("Effect 1 EEPK Type", 160), createGridPane(3, 3, effect1List)),
            createHBox(0, createLabel("Effect 2 ID", 160), createSpinner(Short.MIN_VALUE, Short.MAX_VALUE, subEntry.effect2Id, BdmValues.Effect2_ID)),
            createHBox(0, createLabel("Effect 2 Skill ID", 160), createSpinner(0, 65535, subEntry.effect2SkillId, BdmValues.Effect2_Skill_ID)), 
            createHBox(0, createLabel("Effect 2 EEPK Type", 160), createGridPane(3, 3, effect2List)),
            createHBox(0, createLabel("Effect 3 ID", 160), createSpinner(Short.MIN_VALUE, Short.MAX_VALUE, subEntry.effect3Id, BdmValues.Effect3_ID)),
            createHBox(0, createLabel("Effect 3 Skill ID", 160), createSpinner(0, 65535, subEntry.effect3SkillId, BdmValues.Effect3_Skill_ID)), 
            createHBox(0, createLabel("Effect 3 EEPK Type", 160), createGridPane(3, 3, effect3List))
        );
        effectsVBox.setPadding(new Insets(20, 0, 20, 8));

        return new ScrollPane(effectsVBox);
    }

    private VBox createPushbackVBox(BdmSubEntry subEntry) {
        VBox pushBackVBox = new VBox(27, 
            createHBox(0, createLabel("Pushback Strength", 230), createSpinner(Float.MIN_VALUE, Float.MAX_VALUE, (double)subEntry.pushbackStrength, BdmValues.PushbackStrength)), 
            createHBox(0, createLabel("Pushback Acceleration", 230), createSpinner(Float.MIN_VALUE, Float.MAX_VALUE, (double)subEntry.pushbackAcceleration, BdmValues.PushbackAcceleration)),
            createHBox(0, createLabel("User Stunt", 230), createSpinner(0, 65535, subEntry.userStunt, BdmValues.UserStunt)), 
            createHBox(0, createLabel("Victim Stunt", 230), createSpinner(0, 65535, subEntry.victimStunt, BdmValues.VictimStunt)),
            createHBox(0, createLabel("Knockback Duration", 230), createSpinner(0, 65535, subEntry.knockbackDuration, BdmValues.KnockbackDuration)),  
            createHBox(0, createLabel("Knockback Ground Impact Time", 230), createSpinner(0, 65535, subEntry.knockbackGroundImpactTime, BdmValues.KnockbackGroundImpactTime)),  
            createHBox(0, createLabel("Knockback Recovery After Impact Time", 230), createSpinner(0, 65535, subEntry.knockbackRecoveryAfterImpactTime, BdmValues.KnockbackRecoveryAfterImpactTime)),
            createHBox(0, createLabel("Knockback Strength X", 230), createSpinner(Float.MIN_VALUE, Float.MAX_VALUE, (double)subEntry.knockbackStrengthX, BdmValues.KnockbackStrengthX)), 
            createHBox(0, createLabel("Knockback Strength Y", 230), createSpinner(Float.MIN_VALUE, Float.MAX_VALUE, (double)subEntry.knockbackStrengthY, BdmValues.KnockbackStrengthY)),
            createHBox(0, createLabel("Knockback Strength Z", 230), createSpinner(Float.MIN_VALUE, Float.MAX_VALUE, (double)subEntry.knockbackStrengthZ, BdmValues.KnockbackStrengthZ)), 
            createHBox(0, createLabel("Knockback Drag Y", 230), createSpinner(Float.MIN_VALUE, Float.MAX_VALUE, (double)subEntry.knockbackDragY, BdmValues.KnockbackDragY)),
            createHBox(0, createLabel("Knockback Gravity Time", 230), createSpinner(0, 65535, subEntry.knockbackGravityTime, BdmValues.KnockbackGravityTime)), 
            createHBox(0, createLabel("Victim Invincibility Time", 230), createSpinner(Short.MIN_VALUE, Short.MAX_VALUE, subEntry.victimInvincibilityTime, BdmValues.VictimInvincibilityTime))
        );
        pushBackVBox.setPadding(new Insets(20, 0, 0, 8));

        return pushBackVBox;
    }

    private VBox createCameraVBox(BdmSubEntry subEntry) {
        Spinner<Number> spinner = createSpinner(Short.MIN_VALUE, Short.MAX_VALUE, subEntry.cameraShakeType, BdmValues.CameraShakeType);

        Label label = new Label();
        label.setTextFill(Color.CRIMSON);
        label.textProperty().bind(
            Bindings.createStringBinding(() -> {
                return switch (spinner.getValue().intValue()) {
                    case -1, 3, 4, 5, 9 -> "None";
                    case 0 -> "Rumble";
                    case 1 -> "Heavy Rumble";
                    case 2 -> "Extreme Rumble";
                    case 6, 10 -> "Camera Zoom";
                    case 7 -> "Static Camera";
                    case 8 -> "Camera Focus";
                    default -> "Unknown";
                };
            }, spinner.valueProperty())
        );

        VBox cameraVBox = new VBox(25, 
            createHBox(0, createLabel("Camera Shake Type", 130), createHBox(15, new Node[] {spinner, label}, false)), 
            createHBox(0, createLabel("Camera Shake Time", 130), createSpinner(0, 65535, subEntry.cameraShakeTime, BdmValues.CameraShakeTime)), 
            createHBox(0, createLabel("User BPE ID", 130), createSpinner(Short.MIN_VALUE, Short.MAX_VALUE, subEntry.userBpeID, BdmValues.User_BPE_ID)), 
            createHBox(0, createLabel("Victim BPE ID", 130), createSpinner(Short.MIN_VALUE, Short.MAX_VALUE, subEntry.victimBpeID, BdmValues.Victim_BPE_ID))
        );
        cameraVBox.setPadding(new Insets(20, 0, 0, 8));

        return cameraVBox;
    }

    private VBox createMiscVBox(BdmSubEntry subEntry) {
        Spinner<Number> spinner = createSpinner(0, 65535, subEntry.transformationType, BdmValues.TransformationType);

        Label label = new Label();
        label.setTextFill(Color.CRIMSON);
        label.textProperty().bind(
            Bindings.createStringBinding(() -> {
                return switch (spinner.getValue().intValue()) {
                    case 0 -> "None";
                    case 1 -> "Candy";
                    default -> "Unknown";
                };
            }, spinner.valueProperty())
        );

        CheckBox[] alimentGroup1 = new CheckBox[] {
            new CheckBox("Unknown1"),
            new CheckBox("HP/DEF"),
            new CheckBox("SPD"),
            new CheckBox("Target")
        };
 
        CheckBox[] alimentGroup2 = new CheckBox[] {
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

        Node[] alimentTypes = new Node[] {
            createCheckBoxGroup("Stumble Group 1", alimentGroup1, 1, BdmGroups.AlimentType),
            createCheckBoxGroup("Properties #2", alimentGroup2, 16, BdmGroups.AlimentType),
        };

        Node[] stumbleTypes = new Node[] {
            createCheckBoxGroup("Stumble Group 1", stumbleGroup1, 1, BdmGroups.StumbleType),
            createCheckBoxGroup("Stumble Group 2", stumbleGroup2, 16, BdmGroups.StumbleType), 
            createCheckBoxGroup("Stumble Group 3", stumbleGroup3, 256, BdmGroups.StumbleType),
            createCheckBoxGroup("Stumble Group 4", stumbleGroup4, 4096, BdmGroups.StumbleType)
        };
        
        VBox miscVBox = new VBox(35, 
            createHBox(0, createLabel("Transformation Type", 200), createHBox(15, new Node[] {spinner, label}, false)), 
            createHBox(0, createLabel("Aliment Type", 200), createHBox(5, alimentTypes, false)), 
            createHBox(0, createLabel("Stumble Type", 200), createHBox(5, stumbleTypes, false)), 
            createHBox(0, createLabel("Stamina Broken Override BDM ID", 200), createSpinner(Short.MIN_VALUE, Short.MAX_VALUE, subEntry.staminaBrokenOverrideBdmId, BdmValues.StaminaBrokenOverride_BDM_ID)), 
            createHBox(0, createLabel("Z Vanish Enable Time", 200), createSpinner(0, 65535, subEntry.zVanishEnableTime, BdmValues.ZVanishEnableTime))
        );
        miscVBox.setPadding(new Insets(20, 0, 0, 8));

        return miscVBox;
    }

    private VBox createUnknownVBox (BdmSubEntry subEntry) {
        VBox unknownVBox = new VBox(32, 
            createHBox(0, createLabel("I_02", 60), createTextField(subEntry.i02 ,BdmValues.I02)), 
            createHBox(0, createLabel("I_06", 60), createTextField(subEntry.i06 ,BdmValues.I06)),
            createHBox(0, createLabel("F_08", 60), createTextField(subEntry.f08 ,BdmValues.F08)), 
            createHBox(0, createLabel("I_22", 60), createTextField(subEntry.i22 ,BdmValues.I22)),
            createHBox(0, createLabel("I_30", 60), createTextField(subEntry.i30 ,BdmValues.I30)), 
            createHBox(0, createLabel("I_38", 60), createTextField(subEntry.i38 ,BdmValues.I38)),
            createHBox(0, createLabel("I_58", 60), createTextField(subEntry.i58 ,BdmValues.I58)), 
            createHBox(0, createLabel("I_76", 60), createTextField(subEntry.i76 ,BdmValues.I76)),
            createHBox(0, createLabel("I_82", 60), createTextField(subEntry.i82 ,BdmValues.I82)), 
            createHBox(0, createLabel("I_88", 60), createTextField(subEntry.i88 ,BdmValues.I88)),
            createHBox(0, createLabel("I_90", 60), createTextField(subEntry.i90 ,BdmValues.I90)), 
            createHBox(0, createLabel("I_92", 60), createTextField(subEntry.i92, BdmValues.I92)) 
        );
        unknownVBox.setPadding(new Insets(20,0,0,8));

        return unknownVBox;
    }

    private Label createLabel(String text, int width) {
        Label label = new Label(text);
        if (width != 0) label.setPrefWidth(width);

        return label;
    }

    private RadioButton createRadioButton(String text, ToggleGroup toggleGroup, int value, DamageTypes damageType) {
        RadioButton radioButton = new RadioButton(text);
        radioButton.setToggleGroup(toggleGroup);

        if (value == damageType.index) radioButton.setSelected(true);

        BdmSubEntry bdmSubEntry = bdmEntries.get(listView.getSelectionModel().getSelectedIndex()).subEntries[mainTabPane.getSelectionModel().getSelectedIndex()];
        radioButton.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                switch (damageType) {
                    case NoEffect -> bdmSubEntry.damageType = 0;
                    case Block -> bdmSubEntry.damageType = 1;
                    case GuardBreak -> bdmSubEntry.damageType = 2;
                    case Standard -> bdmSubEntry.damageType = 3;
                    case Heavy -> bdmSubEntry.damageType = 4;
                    case Knockback -> bdmSubEntry.damageType = 5;
                    case Knockback1 -> bdmSubEntry.damageType = 6;
                    case Knockback2 -> bdmSubEntry.damageType = 7;
                    case Knockback3 -> bdmSubEntry.damageType = 8;
                    case Knockback4 -> bdmSubEntry.damageType = 9;
                    case Grab -> bdmSubEntry.damageType = 10;
                    case HoldStomach -> bdmSubEntry.damageType = 11;
                    case HoldEyes -> bdmSubEntry.damageType = 12;
                    case Knockback5 -> bdmSubEntry.damageType = 13;
                    case Electric -> bdmSubEntry.damageType = 14;
                    case Dazed -> bdmSubEntry.damageType = 15;
                    case Paralysis -> bdmSubEntry.damageType = 16;
                    case Freeze -> bdmSubEntry.damageType = 17;
                    case WildCard -> bdmSubEntry.damageType = 18;
                    case Unused -> bdmSubEntry.damageType = 19;
                    case HeavyStaminaBreak -> bdmSubEntry.damageType = 20;
                    case LightStaminaBreak -> bdmSubEntry.damageType = 21;
                    case GiganticKiBlastPush -> bdmSubEntry.damageType = 22;
                    case BrainWash -> bdmSubEntry.damageType = 23;
                    case GiganticKiBlastReturn -> bdmSubEntry.damageType = 24;
                    case Knockback6 -> bdmSubEntry.damageType = 25;
                    case Knockback7 -> bdmSubEntry.damageType = 26;
                    case Knockback8 -> bdmSubEntry.damageType = 27;
                    case Knockback9 -> bdmSubEntry.damageType = 28;
                    case SlowOpponent -> bdmSubEntry.damageType = 29;
                    case BrainWash2 -> bdmSubEntry.damageType = 30;
                    case TimeStop -> bdmSubEntry.damageType = 31;
                }
            }
        });

        return radioButton;
    }

    private RadioButton createRadioButton(String text, ToggleGroup toggleGroup, int value, ACBTypes ACBType) {
        RadioButton radioButton = new RadioButton(text);
        radioButton.setToggleGroup(toggleGroup);

        if (value == ACBType.index) radioButton.setSelected(true);

        BdmSubEntry bdmSubEntry = bdmEntries.get(listView.getSelectionModel().getSelectedIndex()).subEntries[mainTabPane.getSelectionModel().getSelectedIndex()];
        radioButton.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                switch (ACBType) {
                    case Common -> bdmSubEntry.acbType = 0;
                    case CharacterSE -> bdmSubEntry.acbType = 2;
                    case CharacterVOX -> bdmSubEntry.acbType = 3;
                    case SkillSE -> bdmSubEntry.acbType = 10;
                    case SkillVOX -> bdmSubEntry.acbType = 11;
                }
            }
        });

        return radioButton;
    }

    private RadioButton createRadioButton(String text, ToggleGroup toggleGroup, int value, Effect_EEPK_Types effect_EEPK_Type, int type) {
        RadioButton radioButton = new RadioButton(text);
        radioButton.setToggleGroup(toggleGroup);

        if (value == effect_EEPK_Type.index) radioButton.setSelected(true);

        BdmSubEntry bdmSubEntry = bdmEntries.get(listView.getSelectionModel().getSelectedIndex()).subEntries[mainTabPane.getSelectionModel().getSelectedIndex()];
        radioButton.selectedProperty().addListener((obs, oldValue, newValue) -> {
            switch (type) {
                case 1 -> {
                    if (newValue) {
                        switch (effect_EEPK_Type) {
                            case Common -> bdmSubEntry.effect1EepkType = 0;
                            case StageBG -> bdmSubEntry.effect1EepkType = 1;
                            case CharacterEffect -> bdmSubEntry.effect1EepkType = 2;
                            case AwokenSkill -> bdmSubEntry.effect1EepkType = 3;
                            case SuperSkill -> bdmSubEntry.effect1EepkType = 5;
                            case UltimateSkill -> bdmSubEntry.effect1EepkType = 6;
                            case EvasiveSkill -> bdmSubEntry.effect1EepkType = 7;
                            case KiBlastSkill -> bdmSubEntry.effect1EepkType = 9;
                            case StageEffect -> bdmSubEntry.effect1EepkType = 11;
                        }
                    }
                }
                case 2 -> {
                    if (newValue) {
                        switch (effect_EEPK_Type) {
                            case Common -> bdmSubEntry.effect2EepkType = 0;
                            case StageBG -> bdmSubEntry.effect2EepkType = 1;
                            case CharacterEffect -> bdmSubEntry.effect2EepkType = 2;
                            case AwokenSkill -> bdmSubEntry.effect2EepkType = 3;
                            case SuperSkill -> bdmSubEntry.effect2EepkType = 5;
                            case UltimateSkill -> bdmSubEntry.effect2EepkType = 6;
                            case EvasiveSkill -> bdmSubEntry.effect2EepkType = 7;
                            case KiBlastSkill -> bdmSubEntry.effect2EepkType = 9;
                            case StageEffect -> bdmSubEntry.effect2EepkType = 11;
                        }
                    }
                }
                case 3 -> {
                    if (newValue) {
                        switch (effect_EEPK_Type) {
                            case Common -> bdmSubEntry.effect3EepkType = 0;
                            case StageBG -> bdmSubEntry.effect3EepkType = 1;
                            case CharacterEffect -> bdmSubEntry.effect3EepkType = 2;
                            case AwokenSkill -> bdmSubEntry.effect3EepkType = 3;
                            case SuperSkill -> bdmSubEntry.effect3EepkType = 5;
                            case UltimateSkill -> bdmSubEntry.effect3EepkType = 6;
                            case EvasiveSkill -> bdmSubEntry.effect3EepkType = 7;
                            case KiBlastSkill -> bdmSubEntry.effect3EepkType = 9;
                            case StageEffect -> bdmSubEntry.effect3EepkType = 11;
                        }
                    }
                }
            }
        });

        return radioButton;
    }

    private TextField createTextField(Number value, BdmValues bdmValue) {
        TextField textField = new TextField(String.valueOf(value));
        textField.textProperty().addListener((obs, oldText, newText) -> {
            if (textField.getText().contains("-")) {
                return;
            }
            try {
                switch (bdmValue) {
                    case BdmValues.I02 -> bdmEntries.get(listView.getSelectionModel().getSelectedIndex()).subEntries[mainTabPane.getSelectionModel().getSelectedIndex()].i02 = Integer.parseInt(newText);
                    case BdmValues.I06 -> bdmEntries.get(listView.getSelectionModel().getSelectedIndex()).subEntries[mainTabPane.getSelectionModel().getSelectedIndex()].i06 = Integer.parseInt(newText);
                    case BdmValues.F08 -> bdmEntries.get(listView.getSelectionModel().getSelectedIndex()).subEntries[mainTabPane.getSelectionModel().getSelectedIndex()].f08 = Float.parseFloat(newText);
                    case BdmValues.I22 -> bdmEntries.get(listView.getSelectionModel().getSelectedIndex()).subEntries[mainTabPane.getSelectionModel().getSelectedIndex()].i22 = Integer.parseInt(newText);
                    case BdmValues.I30 -> bdmEntries.get(listView.getSelectionModel().getSelectedIndex()).subEntries[mainTabPane.getSelectionModel().getSelectedIndex()].i30 = Integer.parseInt(newText);
                    case BdmValues.I38 -> bdmEntries.get(listView.getSelectionModel().getSelectedIndex()).subEntries[mainTabPane.getSelectionModel().getSelectedIndex()].i38 = Integer.parseInt(newText);
                    case BdmValues.I58 -> bdmEntries.get(listView.getSelectionModel().getSelectedIndex()).subEntries[mainTabPane.getSelectionModel().getSelectedIndex()].i58 = Integer.parseInt(newText);
                    case BdmValues.I76 -> bdmEntries.get(listView.getSelectionModel().getSelectedIndex()).subEntries[mainTabPane.getSelectionModel().getSelectedIndex()].i76 = Integer.parseInt(newText);
                    case BdmValues.I82 -> bdmEntries.get(listView.getSelectionModel().getSelectedIndex()).subEntries[mainTabPane.getSelectionModel().getSelectedIndex()].i82 = Integer.parseInt(newText);
                    case BdmValues.I88 -> bdmEntries.get(listView.getSelectionModel().getSelectedIndex()).subEntries[mainTabPane.getSelectionModel().getSelectedIndex()].i88 = Integer.parseInt(newText);
                    case BdmValues.I90 -> bdmEntries.get(listView.getSelectionModel().getSelectedIndex()).subEntries[mainTabPane.getSelectionModel().getSelectedIndex()].i90 = Integer.parseInt(newText);
                    case BdmValues.I92 -> bdmEntries.get(listView.getSelectionModel().getSelectedIndex()).subEntries[mainTabPane.getSelectionModel().getSelectedIndex()].i92 = Integer.parseInt(newText);
                    default -> throw new IllegalArgumentException("Unexpected value: " + bdmValue);
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        return textField;
    }

    private Spinner<Number> createSpinner(Number MIN_VALUE, Number MAX_VALUE, Number value, BdmValues bdmValue) {
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
                switch (bdmValue) {
                    case DamageAmount -> bdmEntries.get(listView.getSelectionModel().getSelectedIndex()).subEntries[mainTabPane.getSelectionModel().getSelectedIndex()].damageAmount = newValue.intValue();
                    case DamageSpecial -> bdmEntries.get(listView.getSelectionModel().getSelectedIndex()).subEntries[mainTabPane.getSelectionModel().getSelectedIndex()].damageSpecial = newValue.intValue();
                    case DamageSpecial2 -> bdmEntries.get(listView.getSelectionModel().getSelectedIndex()).subEntries[mainTabPane.getSelectionModel().getSelectedIndex()].damageSpecial2 = newValue.intValue();
                    case DamageSpecial3 -> bdmEntries.get(listView.getSelectionModel().getSelectedIndex()).subEntries[mainTabPane.getSelectionModel().getSelectedIndex()].damageSpecial3 = newValue.intValue();
                    case Cue_ID -> bdmEntries.get(listView.getSelectionModel().getSelectedIndex()).subEntries[mainTabPane.getSelectionModel().getSelectedIndex()].cueId = newValue.shortValue();
                    case Effect1_ID -> bdmEntries.get(listView.getSelectionModel().getSelectedIndex()).subEntries[mainTabPane.getSelectionModel().getSelectedIndex()].effect1Id = newValue.shortValue();
                    case Effect1_Skill_ID -> bdmEntries.get(listView.getSelectionModel().getSelectedIndex()).subEntries[mainTabPane.getSelectionModel().getSelectedIndex()].effect1SkillId = newValue.intValue();
                    case Effect2_ID -> bdmEntries.get(listView.getSelectionModel().getSelectedIndex()).subEntries[mainTabPane.getSelectionModel().getSelectedIndex()].effect2Id = newValue.shortValue();
                    case Effect2_Skill_ID -> bdmEntries.get(listView.getSelectionModel().getSelectedIndex()).subEntries[mainTabPane.getSelectionModel().getSelectedIndex()].effect2SkillId = newValue.intValue();
                    case Effect3_ID -> bdmEntries.get(listView.getSelectionModel().getSelectedIndex()).subEntries[mainTabPane.getSelectionModel().getSelectedIndex()].effect3Id = newValue.shortValue();
                    case Effect3_Skill_ID -> bdmEntries.get(listView.getSelectionModel().getSelectedIndex()).subEntries[mainTabPane.getSelectionModel().getSelectedIndex()].effect3SkillId = newValue.intValue();
                    case PushbackStrength -> bdmEntries.get(listView.getSelectionModel().getSelectedIndex()).subEntries[mainTabPane.getSelectionModel().getSelectedIndex()].pushbackStrength = newValue.floatValue();
                    case PushbackAcceleration -> bdmEntries.get(listView.getSelectionModel().getSelectedIndex()).subEntries[mainTabPane.getSelectionModel().getSelectedIndex()].pushbackAcceleration = newValue.floatValue();
                    case UserStunt -> bdmEntries.get(listView.getSelectionModel().getSelectedIndex()).subEntries[mainTabPane.getSelectionModel().getSelectedIndex()].userStunt = newValue.intValue();
                    case VictimStunt -> bdmEntries.get(listView.getSelectionModel().getSelectedIndex()).subEntries[mainTabPane.getSelectionModel().getSelectedIndex()].victimStunt = newValue.intValue();
                    case KnockbackDuration -> bdmEntries.get(listView.getSelectionModel().getSelectedIndex()).subEntries[mainTabPane.getSelectionModel().getSelectedIndex()].knockbackDuration = newValue.intValue();
                    case KnockbackGroundImpactTime -> bdmEntries.get(listView.getSelectionModel().getSelectedIndex()).subEntries[mainTabPane.getSelectionModel().getSelectedIndex()].knockbackGroundImpactTime = newValue.intValue();
                    case KnockbackRecoveryAfterImpactTime -> bdmEntries.get(listView.getSelectionModel().getSelectedIndex()).subEntries[mainTabPane.getSelectionModel().getSelectedIndex()].knockbackRecoveryAfterImpactTime = newValue.intValue();
                    case KnockbackStrengthX -> bdmEntries.get(listView.getSelectionModel().getSelectedIndex()).subEntries[mainTabPane.getSelectionModel().getSelectedIndex()].knockbackStrengthX = newValue.floatValue();
                    case KnockbackStrengthY -> bdmEntries.get(listView.getSelectionModel().getSelectedIndex()).subEntries[mainTabPane.getSelectionModel().getSelectedIndex()].knockbackStrengthY = newValue.floatValue();
                    case KnockbackStrengthZ -> bdmEntries.get(listView.getSelectionModel().getSelectedIndex()).subEntries[mainTabPane.getSelectionModel().getSelectedIndex()].knockbackStrengthZ = newValue.floatValue();
                    case KnockbackDragY -> bdmEntries.get(listView.getSelectionModel().getSelectedIndex()).subEntries[mainTabPane.getSelectionModel().getSelectedIndex()].knockbackDragY = newValue.floatValue();
                    case KnockbackGravityTime -> bdmEntries.get(listView.getSelectionModel().getSelectedIndex()).subEntries[mainTabPane.getSelectionModel().getSelectedIndex()].knockbackGravityTime = newValue.intValue();
                    case VictimInvincibilityTime -> bdmEntries.get(listView.getSelectionModel().getSelectedIndex()).subEntries[mainTabPane.getSelectionModel().getSelectedIndex()].victimInvincibilityTime = newValue.shortValue();
                    case CameraShakeType -> bdmEntries.get(listView.getSelectionModel().getSelectedIndex()).subEntries[mainTabPane.getSelectionModel().getSelectedIndex()].cameraShakeType = newValue.shortValue();
                    case CameraShakeTime -> bdmEntries.get(listView.getSelectionModel().getSelectedIndex()).subEntries[mainTabPane.getSelectionModel().getSelectedIndex()].cameraShakeTime = newValue.intValue();
                    case User_BPE_ID -> bdmEntries.get(listView.getSelectionModel().getSelectedIndex()).subEntries[mainTabPane.getSelectionModel().getSelectedIndex()].userBpeID = newValue.shortValue();
                    case Victim_BPE_ID -> bdmEntries.get(listView.getSelectionModel().getSelectedIndex()).subEntries[mainTabPane.getSelectionModel().getSelectedIndex()].victimBpeID = newValue.shortValue();
                    case TransformationType -> bdmEntries.get(listView.getSelectionModel().getSelectedIndex()).subEntries[mainTabPane.getSelectionModel().getSelectedIndex()].transformationType = newValue.intValue();
                    case StaminaBrokenOverride_BDM_ID -> bdmEntries.get(listView.getSelectionModel().getSelectedIndex()).subEntries[mainTabPane.getSelectionModel().getSelectedIndex()].staminaBrokenOverrideBdmId = newValue.shortValue();
                    case ZVanishEnableTime -> bdmEntries.get(listView.getSelectionModel().getSelectedIndex()).subEntries[mainTabPane.getSelectionModel().getSelectedIndex()].zVanishEnableTime = newValue.intValue();
                    case UserAnimationTime -> bdmEntries.get(listView.getSelectionModel().getSelectedIndex()).subEntries[mainTabPane.getSelectionModel().getSelectedIndex()].userAnimationTime = newValue.intValue();
                    case UserAnimationSpeed -> bdmEntries.get(listView.getSelectionModel().getSelectedIndex()).subEntries[mainTabPane.getSelectionModel().getSelectedIndex()].userAnimationSpeed = newValue.floatValue();
                    case VictimAnimationTime -> bdmEntries.get(listView.getSelectionModel().getSelectedIndex()).subEntries[mainTabPane.getSelectionModel().getSelectedIndex()].victimAnimationTime = newValue.intValue();
                    case VictimAnimationSpeed -> bdmEntries.get(listView.getSelectionModel().getSelectedIndex()).subEntries[mainTabPane.getSelectionModel().getSelectedIndex()].victimAnimationSpeed = newValue.floatValue();
                    default -> throw new IllegalArgumentException("Unexpected value: " + bdmValue);
                }   
            }
        });

        return spinner;
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

    private GridPane createGridPane(int columns ,int rows, Node[] nodeList) {
        GridPane gridPane = new GridPane(10, 10);
        gridPane.getStyleClass().add("titled-address-box");

        int index = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                gridPane.add(nodeList[index], j, i);
                index++;
            }
        }

        return gridPane;
    }

    private StackPane createCheckBoxGroup(String text, CheckBox[] checkBoxsList, int increment, BdmGroups bdmGroup) {
        Label label = new Label(text);
        label.getStyleClass().add("titled-address-label");
        label.setTranslateY(-8); 
        label.setTranslateX(10);

        VBox vBox = new VBox(2);
        vBox.getStyleClass().add("titled-address-box");
        vBox.setPadding(new Insets(12, 0, 0, 0));
        
        for (int i = 0; i < checkBoxsList.length; i++) {
            final int incrementLamda = increment;

            switch(bdmGroup) {
                case SecondaryType -> {
                    checkBoxsList[i].setSelected((bdmEntries.get(listView.getSelectionModel().getSelectedIndex()).subEntries[mainTabPane.getSelectionModel().getSelectedIndex()].secondaryType & increment) != 0);

                    checkBoxsList[i].selectedProperty().addListener((obs, oldValue, newValue) -> {
                        if (newValue) {
                            bdmEntries.get(listView.getSelectionModel().getSelectedIndex()).subEntries[mainTabPane.getSelectionModel().getSelectedIndex()].secondaryType |= incrementLamda;
                        }
                        else {
                            bdmEntries.get(listView.getSelectionModel().getSelectedIndex()).subEntries[mainTabPane.getSelectionModel().getSelectedIndex()].secondaryType &= ~incrementLamda;
                        }
                    });
                }
                case AlimentType -> {
                    checkBoxsList[i].setSelected((bdmEntries.get(listView.getSelectionModel().getSelectedIndex()).subEntries[mainTabPane.getSelectionModel().getSelectedIndex()].alimentType & increment) != 0);

                    checkBoxsList[i].selectedProperty().addListener((obs, oldValue, newValue) -> {
                        if (newValue) {
                            bdmEntries.get(listView.getSelectionModel().getSelectedIndex()).subEntries[mainTabPane.getSelectionModel().getSelectedIndex()].alimentType |= incrementLamda;
                        }
                        else {
                            bdmEntries.get(listView.getSelectionModel().getSelectedIndex()).subEntries[mainTabPane.getSelectionModel().getSelectedIndex()].alimentType &= ~incrementLamda;
                        }
                    });
                }
                case StumbleType -> {
                    checkBoxsList[i].setSelected((bdmEntries.get(listView.getSelectionModel().getSelectedIndex()).subEntries[mainTabPane.getSelectionModel().getSelectedIndex()].stumbleType & increment) != 0);

                    checkBoxsList[i].selectedProperty().addListener((obs, oldValue, newValue) -> {
                        if (newValue) {
                            bdmEntries.get(listView.getSelectionModel().getSelectedIndex()).subEntries[mainTabPane.getSelectionModel().getSelectedIndex()].stumbleType |= incrementLamda;
                        }
                        else {
                            bdmEntries.get(listView.getSelectionModel().getSelectedIndex()).subEntries[mainTabPane.getSelectionModel().getSelectedIndex()].stumbleType &= ~incrementLamda;
                        }
                    });
                }
            }

            vBox.getChildren().add(checkBoxsList[i]);

            increment <<= 1;
        }

        StackPane stackPane = new StackPane(vBox, label);
        StackPane.setAlignment(label, Pos.TOP_LEFT);

        return stackPane;
    }

    public void entriesActionListener() {
        paste.setDisable(true);

        contextMenu.getItems().addAll(copy, paste, delete, append, insert);

        listView.setContextMenu(contextMenu);
        listView.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue == null) return;

            if (listView.getSelectionModel().getSelectedIndex() < 0 || mainTabPane.getSelectionModel().getSelectedIndex() < 0) return;
          
            ((TabPane) mainTabPane.getTabs().get(mainTabPane.getSelectionModel().getSelectedIndex()).getContent()).getTabs().get(0).setContent(createMainVBox(bdmEntries.get(listView.getSelectionModel().getSelectedIndex()).subEntries[mainTabPane.getSelectionModel().getSelectedIndex()]));
            ((TabPane) mainTabPane.getTabs().get(mainTabPane.getSelectionModel().getSelectedIndex()).getContent()).getTabs().get(1).setContent(createAnimationVBox(bdmEntries.get(listView.getSelectionModel().getSelectedIndex()).subEntries[mainTabPane.getSelectionModel().getSelectedIndex()]));
            ((TabPane) mainTabPane.getTabs().get(mainTabPane.getSelectionModel().getSelectedIndex()).getContent()).getTabs().get(2).setContent(createSoundVBox(bdmEntries.get(listView.getSelectionModel().getSelectedIndex()).subEntries[mainTabPane.getSelectionModel().getSelectedIndex()]));
            ((TabPane) mainTabPane.getTabs().get(mainTabPane.getSelectionModel().getSelectedIndex()).getContent()).getTabs().get(3).setContent(createEffectsScrollPane(bdmEntries.get(listView.getSelectionModel().getSelectedIndex()).subEntries[mainTabPane.getSelectionModel().getSelectedIndex()]));
            ((TabPane) mainTabPane.getTabs().get(mainTabPane.getSelectionModel().getSelectedIndex()).getContent()).getTabs().get(4).setContent(createPushbackVBox(bdmEntries.get(listView.getSelectionModel().getSelectedIndex()).subEntries[mainTabPane.getSelectionModel().getSelectedIndex()]));
            ((TabPane) mainTabPane.getTabs().get(mainTabPane.getSelectionModel().getSelectedIndex()).getContent()).getTabs().get(5).setContent(createCameraVBox(bdmEntries.get(listView.getSelectionModel().getSelectedIndex()).subEntries[mainTabPane.getSelectionModel().getSelectedIndex()]));
            ((TabPane) mainTabPane.getTabs().get(mainTabPane.getSelectionModel().getSelectedIndex()).getContent()).getTabs().get(6).setContent(createMiscVBox(bdmEntries.get(listView.getSelectionModel().getSelectedIndex()).subEntries[mainTabPane.getSelectionModel().getSelectedIndex()]));
            ((TabPane) mainTabPane.getTabs().get(mainTabPane.getSelectionModel().getSelectedIndex()).getContent()).getTabs().get(7).setContent(createUnknownVBox(bdmEntries.get(listView.getSelectionModel().getSelectedIndex()).subEntries[mainTabPane.getSelectionModel().getSelectedIndex()]));
        });
        listView.setOnMouseClicked(e -> {
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
                });
            }
        });
    }

    public void tabActionListener() {
        mainTabPane.getSelectionModel().selectedItemProperty().addListener((obsevable, oldTab, newTab) -> {
            if (newTab == null) return;

            if (listView.getSelectionModel().getSelectedIndex() < 0 || mainTabPane.getSelectionModel().getSelectedIndex() < 0) return;

            ((TabPane) mainTabPane.getTabs().get(mainTabPane.getSelectionModel().getSelectedIndex()).getContent()).getTabs().get(0).setContent(createMainVBox(bdmEntries.get(listView.getSelectionModel().getSelectedIndex()).subEntries[mainTabPane.getTabs().indexOf(newTab)]));
            ((TabPane) mainTabPane.getTabs().get(mainTabPane.getSelectionModel().getSelectedIndex()).getContent()).getTabs().get(1).setContent(createAnimationVBox(bdmEntries.get(listView.getSelectionModel().getSelectedIndex()).subEntries[mainTabPane.getTabs().indexOf(newTab)]));
            ((TabPane) mainTabPane.getTabs().get(mainTabPane.getSelectionModel().getSelectedIndex()).getContent()).getTabs().get(2).setContent(createSoundVBox(bdmEntries.get(listView.getSelectionModel().getSelectedIndex()).subEntries[mainTabPane.getTabs().indexOf(newTab)]));
            ((TabPane) mainTabPane.getTabs().get(mainTabPane.getSelectionModel().getSelectedIndex()).getContent()).getTabs().get(3).setContent(createEffectsScrollPane(bdmEntries.get(listView.getSelectionModel().getSelectedIndex()).subEntries[mainTabPane.getTabs().indexOf(newTab)]));
            ((TabPane) mainTabPane.getTabs().get(mainTabPane.getSelectionModel().getSelectedIndex()).getContent()).getTabs().get(4).setContent(createPushbackVBox(bdmEntries.get(listView.getSelectionModel().getSelectedIndex()).subEntries[mainTabPane.getTabs().indexOf(newTab)]));
            ((TabPane) mainTabPane.getTabs().get(mainTabPane.getSelectionModel().getSelectedIndex()).getContent()).getTabs().get(5).setContent(createCameraVBox(bdmEntries.get(listView.getSelectionModel().getSelectedIndex()).subEntries[mainTabPane.getTabs().indexOf(newTab)]));
            ((TabPane) mainTabPane.getTabs().get(mainTabPane.getSelectionModel().getSelectedIndex()).getContent()).getTabs().get(6).setContent(createMiscVBox(bdmEntries.get(listView.getSelectionModel().getSelectedIndex()).subEntries[mainTabPane.getTabs().indexOf(newTab)]));
            ((TabPane) mainTabPane.getTabs().get(mainTabPane.getSelectionModel().getSelectedIndex()).getContent()).getTabs().get(7).setContent(createUnknownVBox(bdmEntries.get(listView.getSelectionModel().getSelectedIndex()).subEntries[mainTabPane.getTabs().indexOf(newTab)]));
            ((TabPane) newTab.getContent()).getSelectionModel().select(((TabPane) oldTab.getContent()).getSelectionModel().getSelectedIndex());
        });
    }

    private void entriesKeysListener() {
        listView.setOnKeyPressed(e -> {
            if (e.isControlDown() && e.getCode() == KeyCode.C) {
                Copy();
                paste.setDisable(false);
            }
            else if (e.isControlDown() && e.getCode() == KeyCode.V) Paste();
            else if (e.getCode() == KeyCode.DELETE) Delete();
            else if (e.isControlDown() && e.getCode() == KeyCode.A) Append();
            else if (e.isControlDown() && e.getCode() == KeyCode.I) Insert();
        });
    }

    private void Copy() {
        if (listView.getSelectionModel().getSelectedIndex() < 0) return; 

        copyContainer.clear(); 
        copyContainer.add(new BdmEntry(bdmEntries.get(listView.getSelectionModel().getSelectedIndex()))); 
    }

    private void Paste() {
        if (copyContainer.isEmpty()) return;

        bdmEntries.set(listView.getSelectionModel().getSelectedIndex(), new BdmEntry(copyContainer.get(0)));

        ((TabPane) mainTabPane.getTabs().get(mainTabPane.getSelectionModel().getSelectedIndex()).getContent()).getTabs().get(0).setContent(createMainVBox(bdmEntries.get(listView.getSelectionModel().getSelectedIndex()).subEntries[mainTabPane.getSelectionModel().getSelectedIndex()]));
        ((TabPane) mainTabPane.getTabs().get(mainTabPane.getSelectionModel().getSelectedIndex()).getContent()).getTabs().get(1).setContent(createAnimationVBox(bdmEntries.get(listView.getSelectionModel().getSelectedIndex()).subEntries[mainTabPane.getSelectionModel().getSelectedIndex()]));
        ((TabPane) mainTabPane.getTabs().get(mainTabPane.getSelectionModel().getSelectedIndex()).getContent()).getTabs().get(2).setContent(createSoundVBox(bdmEntries.get(listView.getSelectionModel().getSelectedIndex()).subEntries[mainTabPane.getSelectionModel().getSelectedIndex()]));
        ((TabPane) mainTabPane.getTabs().get(mainTabPane.getSelectionModel().getSelectedIndex()).getContent()).getTabs().get(3).setContent(createEffectsScrollPane(bdmEntries.get(listView.getSelectionModel().getSelectedIndex()).subEntries[mainTabPane.getSelectionModel().getSelectedIndex()]));
        ((TabPane) mainTabPane.getTabs().get(mainTabPane.getSelectionModel().getSelectedIndex()).getContent()).getTabs().get(4).setContent(createPushbackVBox(bdmEntries.get(listView.getSelectionModel().getSelectedIndex()).subEntries[mainTabPane.getSelectionModel().getSelectedIndex()]));
        ((TabPane) mainTabPane.getTabs().get(mainTabPane.getSelectionModel().getSelectedIndex()).getContent()).getTabs().get(5).setContent(createCameraVBox(bdmEntries.get(listView.getSelectionModel().getSelectedIndex()).subEntries[mainTabPane.getSelectionModel().getSelectedIndex()]));
        ((TabPane) mainTabPane.getTabs().get(mainTabPane.getSelectionModel().getSelectedIndex()).getContent()).getTabs().get(6).setContent(createMiscVBox(bdmEntries.get(listView.getSelectionModel().getSelectedIndex()).subEntries[mainTabPane.getSelectionModel().getSelectedIndex()]));
        ((TabPane) mainTabPane.getTabs().get(mainTabPane.getSelectionModel().getSelectedIndex()).getContent()).getTabs().get(7).setContent(createUnknownVBox(bdmEntries.get(listView.getSelectionModel().getSelectedIndex()).subEntries[mainTabPane.getSelectionModel().getSelectedIndex()]));
    }

    private void Delete() {
        if (listView.getSelectionModel().getSelectedIndex() == 0) return;

        bdmEntries.remove(listView.getSelectionModel().getSelectedIndex());
        listView.getItems().remove(listView.getSelectionModel().getSelectedIndex());

        for (int i = 0; i < listView.getItems().size(); i++) {
            listView.getItems().set(i, "Entry: " + i);
        }
    }

   private void Append() {
        if (listView.getSelectionModel().getSelectedIndex() < 0) return;
        
        bdmEntries.add(listView.getSelectionModel().getSelectedIndex() + 1, new BdmEntry());
        listView.getItems().add("Entry " + listView.getItems().size());
    }

    private void Insert() {
        if (listView.getSelectionModel().getSelectedIndex() > 0) {
            bdmEntries.add(listView.getSelectionModel().getSelectedIndex() - 1, new BdmEntry());
            listView.getItems().add("Entry " + listView.getItems().size());
        } 
        else if (listView.getSelectionModel().getSelectedIndex() == 0) {
            bdmEntries.add(listView.getSelectionModel().getSelectedIndex(), new BdmEntry());
            listView.getItems().add("Entry " + listView.getItems().size());
        }
    }

    public void bdmReader(Path path) {
        try(FileChannel channel = FileChannel.open(path, StandardOpenOption.READ)) {
            int bdmEntriesCount;
            int entryOffset = 16;

            ByteBuffer shortBuffer = ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN);
            ByteBuffer intBuffer = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
            
            channel.position(8);
            intBuffer.clear();
            channel.read(intBuffer);
            intBuffer.flip();
            bdmEntriesCount = intBuffer.getInt();

            switch ((int)((path.toFile().length() - 16) / bdmEntriesCount)) {
                case 1284 -> {
                    for (int i = 0; i < bdmEntriesCount; i++) {
                        bdmEntries.add(new BdmEntry());

                        listView.getItems().add("Entry " + i);

                        for (int j = 0; j < 10; j++) {
                            channel.position(entryOffset + 4 + j * 128 + i * 1284);
                            shortBuffer.clear();
                            channel.read(shortBuffer);
                            shortBuffer.flip();
                            bdmEntries.get(i).subEntries[j].damageType = toUShort(shortBuffer.getShort());

                            channel.position(entryOffset + 6 + j * 128 + i * 1284);
                            shortBuffer.clear();
                            channel.read(shortBuffer);
                            shortBuffer.flip();
                            bdmEntries.get(i).subEntries[j].i02 = toUShort(shortBuffer.getShort());

                            channel.position(entryOffset + 8 + j * 128 + i * 1284);
                            shortBuffer.clear();
                            channel.read(shortBuffer);
                            shortBuffer.flip();
                            bdmEntries.get(i).subEntries[j].damageAmount = toUShort(shortBuffer.getShort());

                            channel.position(entryOffset + 10 + j * 128 + i * 1284);
                            shortBuffer.clear();
                            channel.read(shortBuffer);
                            shortBuffer.flip();
                            bdmEntries.get(i).subEntries[j].i06 = toUShort(shortBuffer.getShort());

                            channel.position(entryOffset + 12 + j * 128 + i * 1284);
                            intBuffer.clear();
                            channel.read(intBuffer);
                            intBuffer.flip();
                            bdmEntries.get(i).subEntries[j].f08 = intBuffer.getFloat();

                            channel.position(entryOffset + 16 + j * 128 + i * 1284);
                            shortBuffer.clear();
                            channel.read(shortBuffer);
                            shortBuffer.flip();
                            bdmEntries.get(i).subEntries[j].acbType = toUShort(shortBuffer.getShort());

                            channel.position(entryOffset + 18 + j * 128 + i * 1284);
                            shortBuffer.clear();
                            channel.read(shortBuffer);
                            shortBuffer.flip();
                            bdmEntries.get(i).subEntries[j].cueId = shortBuffer.getShort();

                            channel.position(entryOffset + 20 + j * 128 + i * 1284);
                            shortBuffer.clear();
                            channel.read(shortBuffer);
                            shortBuffer.flip();
                            bdmEntries.get(i).subEntries[j].effect1Id = shortBuffer.getShort();

                            channel.position(entryOffset + 22 + j * 128 + i * 1284);
                            shortBuffer.clear();
                            channel.read(shortBuffer);
                            shortBuffer.flip();
                            bdmEntries.get(i).subEntries[j].effect1SkillId = toUShort(shortBuffer.getShort());

                            channel.position(entryOffset + 24 + j * 128 + i * 1284);
                            shortBuffer.clear();
                            channel.read(shortBuffer);
                            shortBuffer.flip();
                            bdmEntries.get(i).subEntries[j].effect1EepkType = toUShort(shortBuffer.getShort());

                            channel.position(entryOffset + 26 + j * 128 + i * 1284);
                            shortBuffer.clear();
                            channel.read(shortBuffer);
                            shortBuffer.flip();
                            bdmEntries.get(i).subEntries[j].i22 = toUShort(shortBuffer.getShort());

                            channel.position(entryOffset + 28 + j * 128 + i * 1284);
                            shortBuffer.clear();
                            channel.read(shortBuffer);
                            shortBuffer.flip();
                            bdmEntries.get(i).subEntries[j].effect2Id = shortBuffer.getShort();

                            channel.position(entryOffset + 30 + j * 128 + i * 1284);
                            shortBuffer.clear();
                            channel.read(shortBuffer);
                            shortBuffer.flip();
                            bdmEntries.get(i).subEntries[j].effect2SkillId = toUShort(shortBuffer.getShort());

                            channel.position(entryOffset + 32 + j * 128 + i * 1284);
                            shortBuffer.clear();
                            channel.read(shortBuffer);
                            shortBuffer.flip();
                            bdmEntries.get(i).subEntries[j].effect2EepkType = toUShort(shortBuffer.getShort());

                            channel.position(entryOffset + 34 + j * 128 + i * 1284);
                            shortBuffer.clear();
                            channel.read(shortBuffer);
                            shortBuffer.flip();
                            bdmEntries.get(i).subEntries[j].i30 = toUShort(shortBuffer.getShort());

                            channel.position(entryOffset + 36 + j * 128 + i * 1284);
                            shortBuffer.clear();
                            channel.read(shortBuffer);
                            shortBuffer.flip();
                            bdmEntries.get(i).subEntries[j].effect3Id = shortBuffer.getShort();

                            channel.position(entryOffset + 38 + j * 128 + i * 1284);
                            shortBuffer.clear();
                            channel.read(shortBuffer);
                            shortBuffer.flip();
                            bdmEntries.get(i).subEntries[j].effect3SkillId = toUShort(shortBuffer.getShort());

                            channel.position(entryOffset + 40 + j * 128 + i * 1284);
                            shortBuffer.clear();
                            channel.read(shortBuffer);
                            shortBuffer.flip();
                            bdmEntries.get(i).subEntries[j].effect3EepkType = toUShort(shortBuffer.getShort());

                            channel.position(entryOffset + 42 + j * 128 + i * 1284);
                            shortBuffer.clear();
                            channel.read(shortBuffer);
                            shortBuffer.flip();
                            bdmEntries.get(i).subEntries[j].i38 = toUShort(shortBuffer.getShort());

                            channel.position(entryOffset + 44 + j * 128 + i * 1284);
                            intBuffer.clear();
                            channel.read(intBuffer);
                            intBuffer.flip();
                            bdmEntries.get(i).subEntries[j].pushbackStrength = intBuffer.getFloat();

                            channel.position(entryOffset + 48 + j * 128 + i * 1284);
                            intBuffer.clear();
                            channel.read(intBuffer);
                            intBuffer.flip();
                            bdmEntries.get(i).subEntries[j].pushbackAcceleration = intBuffer.getFloat();

                            channel.position(entryOffset + 52 + j * 128 + i * 1284);
                            shortBuffer.clear();
                            channel.read(shortBuffer);
                            shortBuffer.flip();
                            bdmEntries.get(i).subEntries[j].userStunt = toUShort(shortBuffer.getShort());

                            channel.position(entryOffset + 54 + j * 128 + i * 1284);
                            shortBuffer.clear();
                            channel.read(shortBuffer);
                            shortBuffer.flip();
                            bdmEntries.get(i).subEntries[j].victimStunt = toUShort(shortBuffer.getShort());

                            channel.position(entryOffset + 56 + j * 128 + i * 1284);
                            shortBuffer.clear();
                            channel.read(shortBuffer);
                            shortBuffer.flip();
                            bdmEntries.get(i).subEntries[j].knockbackDuration = toUShort(shortBuffer.getShort());

                            channel.position(entryOffset + 58 + j * 128 + i * 1284);
                            shortBuffer.clear();
                            channel.read(shortBuffer);
                            shortBuffer.flip();
                            bdmEntries.get(i).subEntries[j].knockbackRecoveryAfterImpactTime = toUShort(shortBuffer.getShort());

                            channel.position(entryOffset + 60 + j * 128 + i * 1284);
                            shortBuffer.clear();
                            channel.read(shortBuffer);
                            shortBuffer.flip();
                            bdmEntries.get(i).subEntries[j].knockbackGroundImpactTime = toUShort(shortBuffer.getShort());

                            channel.position(entryOffset + 62 + j * 128 + i * 1284);
                            shortBuffer.clear();
                            channel.read(shortBuffer);
                            shortBuffer.flip();
                            bdmEntries.get(i).subEntries[j].i58 = toUShort(shortBuffer.getShort());

                            channel.position(entryOffset + 64 + j * 128 + i * 1284);
                            intBuffer.clear();
                            channel.read(intBuffer);
                            intBuffer.flip();
                            bdmEntries.get(i).subEntries[j].knockbackStrengthX = intBuffer.getFloat();

                            channel.position(entryOffset + 68 + j * 128 + i * 1284);
                            intBuffer.clear();
                            channel.read(intBuffer);
                            intBuffer.flip();
                            bdmEntries.get(i).subEntries[j].knockbackStrengthY = intBuffer.getFloat();

                            channel.position(entryOffset + 72 + j * 128 + i * 1284);
                            intBuffer.clear();
                            channel.read(intBuffer);
                            intBuffer.flip();
                            bdmEntries.get(i).subEntries[j].knockbackStrengthZ = intBuffer.getFloat();

                            channel.position(entryOffset + 76 + j * 128 + i * 1284);
                            intBuffer.clear();
                            channel.read(intBuffer);
                            intBuffer.flip();
                            bdmEntries.get(i).subEntries[j].knockbackDragY = intBuffer.getFloat();

                            channel.position(entryOffset + 80 + j * 128 + i * 1284);
                            shortBuffer.clear();
                            channel.read(shortBuffer);
                            shortBuffer.flip();
                            bdmEntries.get(i).subEntries[j].i76 = toUShort(shortBuffer.getShort());

                            channel.position(entryOffset + 82 + j * 128 + i * 1284);
                            shortBuffer.clear();
                            channel.read(shortBuffer);
                            shortBuffer.flip();
                            bdmEntries.get(i).subEntries[j].knockbackGravityTime = toUShort(shortBuffer.getShort());

                            channel.position(entryOffset + 84 + j * 128 + i * 1284);
                            shortBuffer.clear();
                            channel.read(shortBuffer);
                            shortBuffer.flip();
                            bdmEntries.get(i).subEntries[j].victimInvincibilityTime = shortBuffer.getShort();

                            channel.position(entryOffset + 86 + j * 128 + i * 1284);
                            shortBuffer.clear();
                            channel.read(shortBuffer);
                            shortBuffer.flip();
                            bdmEntries.get(i).subEntries[j].i82 = shortBuffer.getShort();

                            channel.position(entryOffset + 88 + j * 128 + i * 1284);
                            shortBuffer.clear();
                            channel.read(shortBuffer);
                            shortBuffer.flip();
                            bdmEntries.get(i).subEntries[j].transformationType = shortBuffer.getShort();

                            channel.position(entryOffset + 90 + j * 128 + i * 1284);
                            shortBuffer.clear();
                            channel.read(shortBuffer);
                            shortBuffer.flip();
                            bdmEntries.get(i).subEntries[j].alimentType = shortBuffer.getShort();

                            channel.position(entryOffset + 92 + j * 128 + i * 1284);
                            shortBuffer.clear();
                            channel.read(shortBuffer);
                            shortBuffer.flip();
                            bdmEntries.get(i).subEntries[j].i88 = toUShort(shortBuffer.getShort());

                            channel.position(entryOffset + 94 + j * 128 + i * 1284);
                            shortBuffer.clear();
                            channel.read(shortBuffer);
                            shortBuffer.flip();
                            bdmEntries.get(i).subEntries[j].i90 = toUShort(shortBuffer.getShort());

                            channel.position(entryOffset + 96 + j * 128 + i * 1284);
                            shortBuffer.clear();
                            channel.read(shortBuffer);
                            shortBuffer.flip();
                            bdmEntries.get(i).subEntries[j].i92 = toUShort(shortBuffer.getShort());

                            channel.position(entryOffset + 98 + j * 128 + i * 1284);
                            shortBuffer.clear();
                            channel.read(shortBuffer);
                            shortBuffer.flip();
                            bdmEntries.get(i).subEntries[j].damageSpecial = toUShort(shortBuffer.getShort());

                            channel.position(entryOffset + 100 + j * 128 + i * 1284);
                            shortBuffer.clear();
                            channel.read(shortBuffer);
                            shortBuffer.flip();
                            bdmEntries.get(i).subEntries[j].damageSpecial2 = toUShort(shortBuffer.getShort());

                            channel.position(entryOffset + 102 + j * 128 + i * 1284);
                            shortBuffer.clear();
                            channel.read(shortBuffer);
                            shortBuffer.flip();
                            bdmEntries.get(i).subEntries[j].damageSpecial3 = toUShort(shortBuffer.getShort());

                            channel.position(entryOffset + 104 + j * 128 + i * 1284);
                            shortBuffer.clear();
                            channel.read(shortBuffer);
                            shortBuffer.flip();
                            bdmEntries.get(i).subEntries[j].stumbleType = toUShort(shortBuffer.getShort());

                            channel.position(entryOffset + 106 + j * 128 + i * 1284);
                            shortBuffer.clear();
                            channel.read(shortBuffer);
                            shortBuffer.flip();
                            bdmEntries.get(i).subEntries[j].secondaryType = toUShort(shortBuffer.getShort());

                            channel.position(entryOffset + 108 + j * 128 + i * 1284);
                            shortBuffer.clear();
                            channel.read(shortBuffer);
                            shortBuffer.flip();
                            bdmEntries.get(i).subEntries[j].cameraShakeType = shortBuffer.getShort();

                            channel.position(entryOffset + 110 + j * 128 + i * 1284);
                            shortBuffer.clear();
                            channel.read(shortBuffer);
                            shortBuffer.flip();
                            bdmEntries.get(i).subEntries[j].cameraShakeTime = toUShort(shortBuffer.getShort());

                            channel.position(entryOffset + 112 + j * 128 + i * 1284);
                            shortBuffer.clear();
                            channel.read(shortBuffer);
                            shortBuffer.flip();
                            bdmEntries.get(i).subEntries[j].userBpeID = shortBuffer.getShort();

                            channel.position(entryOffset + 114 + j * 128 + i * 1284);
                            shortBuffer.clear();
                            channel.read(shortBuffer);
                            shortBuffer.flip();
                            bdmEntries.get(i).subEntries[j].victimBpeID = shortBuffer.getShort();

                            channel.position(entryOffset + 116 + j * 128 + i * 1284);
                            shortBuffer.clear();
                            channel.read(shortBuffer);
                            shortBuffer.flip();
                            bdmEntries.get(i).subEntries[j].staminaBrokenOverrideBdmId = shortBuffer.getShort();

                            channel.position(entryOffset + 118 + j * 128 + i * 1284);
                            shortBuffer.clear();
                            channel.read(shortBuffer);
                            shortBuffer.flip();
                            bdmEntries.get(i).subEntries[j].zVanishEnableTime = toUShort(shortBuffer.getShort());

                            channel.position(entryOffset + 120 + j * 128 + i * 1284);
                            shortBuffer.clear();
                            channel.read(shortBuffer);
                            shortBuffer.flip();
                            bdmEntries.get(i).subEntries[j].userAnimationTime = toUShort(shortBuffer.getShort());

                            channel.position(entryOffset + 122 + j * 128 + i * 1284);
                            shortBuffer.clear();
                            channel.read(shortBuffer);
                            shortBuffer.flip();
                            bdmEntries.get(i).subEntries[j].victimAnimationTime = toUShort(shortBuffer.getShort());

                            channel.position(entryOffset + 124 + j * 128 + i * 1284);
                            intBuffer.clear();
                            channel.read(intBuffer);
                            intBuffer.flip();
                            bdmEntries.get(i).subEntries[j].userAnimationSpeed = intBuffer.getFloat();

                            channel.position(entryOffset + 128 + j * 128 + i * 1284);
                            intBuffer.clear();
                            channel.read(intBuffer);
                            intBuffer.flip();
                            bdmEntries.get(i).subEntries[j].victimAnimationSpeed = intBuffer.getFloat();
                        }
                    }
                }
                default -> {
                    Platform.runLater(() -> {
                        Popups.LegacyFormat();
                    });
                }   
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void bdmWriter(Path path) {
        try(FileChannel channel = FileChannel.open(path, StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            int entryOffset = 16;

            ByteBuffer shortBuffer = ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN);
            ByteBuffer intBuffer = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
            
            channel.write(ByteBuffer.wrap(new byte[]{0x23, 0x42, 0x44, (byte)0x4D}));

            channel.position(4);
            channel.write(ByteBuffer.wrap(new byte[]{(byte)0xFE, (byte)0xFF}));

            channel.position(8);
            intBuffer.clear();
            intBuffer.putInt(listView.getItems().size());
            intBuffer.flip();
            channel.write(intBuffer);

            channel.position(12);
            intBuffer.clear();
            intBuffer.putInt(entryOffset);
            intBuffer.flip();
            channel.write(intBuffer);

            for (int i = 0; i < listView.getItems().size(); i++) {
                BdmEntry bdmEntry = bdmEntries.get(i);

                channel.position(entryOffset + i * 1284);
                intBuffer.clear();
                intBuffer.putInt(i);
                intBuffer.flip();
                channel.write(intBuffer);

                for (int j = 0; j < 10; j++) {
                    BdmSubEntry subEntry = bdmEntry.subEntries[j];
                    
                    channel.position(entryOffset + 4 + j * 128 + i * 1284);
                    shortBuffer.clear();
                    shortBuffer.putShort((short)subEntry.damageType);
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    channel.position(entryOffset + 6 + j * 128 + i * 1284);
                    shortBuffer.clear();
                    shortBuffer.putShort((short)subEntry.i02);
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    channel.position(entryOffset + 8 + j * 128 + i * 1284);
                    shortBuffer.clear();
                    shortBuffer.putShort((short)subEntry.damageAmount);
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    channel.position(entryOffset+10+j * 128 + i * 1284);
                    shortBuffer.clear();
                    shortBuffer.putShort((short)subEntry.i06);
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    channel.position(entryOffset + 12 + j * 128 + i * 1284);
                    intBuffer.clear();
                    intBuffer.putFloat((float)subEntry.f08);
                    intBuffer.flip();
                    channel.write(intBuffer);

                    channel.position(entryOffset + 16 + j * 128 + i * 1284);
                    shortBuffer.clear();
                    shortBuffer.putShort((short)subEntry.acbType);
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    channel.position(entryOffset + 18 + j * 128 + i * 1284);
                    shortBuffer.clear();
                    shortBuffer.putShort(subEntry.cueId);
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    channel.position(entryOffset + 20 + j * 128 + i * 1284);
                    shortBuffer.clear();
                    shortBuffer.putShort(subEntry.effect1Id);
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    channel.position(entryOffset + 22 + j * 128 + i * 1284);
                    shortBuffer.clear();
                    shortBuffer.putShort((short)subEntry.effect1SkillId);
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    channel.position(entryOffset + 24 + j * 128 + i * 1284);
                    shortBuffer.clear();
                    shortBuffer.putShort((short)subEntry.effect1EepkType);
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    channel.position(entryOffset + 26 + j * 128 + i * 1284);
                    shortBuffer.clear();
                    shortBuffer.putShort((short)subEntry.i22);
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    channel.position(entryOffset + 28 + j * 128 + i * 1284);
                    shortBuffer.clear();
                    shortBuffer.putShort(subEntry.effect2Id);
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    channel.position(entryOffset+30 + j * 128 + i * 1284);
                    shortBuffer.clear();
                    shortBuffer.putShort((short)subEntry.effect2SkillId);
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    channel.position(entryOffset + 32 + j * 128 + i * 1284);
                    shortBuffer.clear();
                    shortBuffer.putShort((short)subEntry.effect2EepkType);
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    channel.position(entryOffset + 34 + j * 128 + i * 1284);
                    shortBuffer.clear();
                    shortBuffer.putShort((short)subEntry.i30);
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    channel.position(entryOffset + 36 + j * 128 + i * 1284);
                    shortBuffer.clear();
                    shortBuffer.putShort(subEntry.effect3Id);
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    channel.position(entryOffset + 38 + j * 128 + i * 1284);
                    shortBuffer.clear();
                    shortBuffer.putShort((short)subEntry.effect3SkillId);
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    channel.position(entryOffset + 40 + j * 128 + i * 1284);
                    shortBuffer.clear();
                    shortBuffer.putShort((short)subEntry.effect3EepkType);
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    channel.position(entryOffset + 42 + j * 128 + i * 1284);
                    shortBuffer.clear();
                    shortBuffer.putShort((short)subEntry.i38);
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    channel.position(entryOffset+44+128*j+1284*i);
                    intBuffer.clear();
                    intBuffer.putFloat((float)subEntry.pushbackStrength);
                    intBuffer.flip();
                    channel.write(intBuffer);

                    channel.position(entryOffset + 48 + j * 128 + i * 1284);
                    intBuffer.clear();
                    intBuffer.putFloat((float)subEntry.pushbackAcceleration);
                    intBuffer.flip();
                    channel.write(intBuffer);

                    channel.position(entryOffset + 52 + j * 128 + i * 1284);
                    shortBuffer.clear();
                    shortBuffer.putShort((short)subEntry.userStunt);
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    channel.position(entryOffset + 54 + j * 128 + i * 1284);
                    shortBuffer.clear();
                    shortBuffer.putShort((short)subEntry.victimStunt);
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    channel.position(entryOffset + 56 + j * 128 + i * 1284);
                    shortBuffer.clear();
                    shortBuffer.putShort((short)subEntry.knockbackDuration);
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    channel.position(entryOffset + 58 + j * 128 + i * 1284);
                    shortBuffer.clear();
                    shortBuffer.putShort((short)subEntry.knockbackRecoveryAfterImpactTime);
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    channel.position(entryOffset + 60 + j * 128 + i * 1284);
                    shortBuffer.clear();
                    shortBuffer.putShort((short)subEntry.knockbackGroundImpactTime);
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    channel.position(entryOffset + 62 + j * 128 + i * 1284);
                    shortBuffer.clear();
                    shortBuffer.putShort((short)subEntry.i58);
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    channel.position(entryOffset + 64 + j * 128 + i * 1284);
                    intBuffer.clear();
                    intBuffer.putFloat((float)subEntry.knockbackStrengthX);
                    intBuffer.flip();
                    channel.write(intBuffer);

                    channel.position(entryOffset + 68 + j * 128 + i * 1284);
                    intBuffer.clear();
                    intBuffer.putFloat((float)subEntry.knockbackStrengthY);
                    intBuffer.flip();
                    channel.write(intBuffer);

                    channel.position(entryOffset + 72 + j * 128 + i * 1284);
                    intBuffer.clear();
                    intBuffer.putFloat((float)subEntry.knockbackStrengthZ);
                    intBuffer.flip();
                    channel.write(intBuffer);

                    channel.position(entryOffset + 76 + j * 128 + i * 1284);
                    intBuffer.clear();
                    intBuffer.putFloat((float)subEntry.knockbackDragY);
                    intBuffer.flip();
                    channel.write(intBuffer);

                    channel.position(entryOffset + 80 + j * 128 + i * 1284);
                    shortBuffer.clear();
                    shortBuffer.putShort((short)subEntry.i76);
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    channel.position(entryOffset + 82 + j * 128 + i * 1284);
                    shortBuffer.clear();
                    shortBuffer.putShort((short)subEntry.knockbackGravityTime);
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    channel.position(entryOffset + 84 + j * 128 + i * 1284);
                    shortBuffer.clear();
                    shortBuffer.putShort(subEntry.victimInvincibilityTime);
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    channel.position(entryOffset + 86 + j * 128 + i * 1284);
                    shortBuffer.clear();
                    shortBuffer.putShort((short)subEntry.i82);
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    channel.position(entryOffset + 88 + j * 128 + i * 1284);
                    shortBuffer.clear();
                    shortBuffer.putShort((short)subEntry.transformationType);
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    channel.position(entryOffset + 90 + j * 128 + i * 1284);
                    shortBuffer.clear();
                    shortBuffer.putShort(subEntry.alimentType);
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    channel.position(entryOffset + 92 + j * 128 + i * 1284);
                    shortBuffer.clear();
                    shortBuffer.putShort((short)subEntry.i88);
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    channel.position(entryOffset + 94 + j * 128 + i * 1284);
                    shortBuffer.clear();
                    shortBuffer.putShort((short)subEntry.i90);
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    channel.position(entryOffset + 96 + j * 128 + i * 1284);
                    shortBuffer.clear();
                    shortBuffer.putShort((short)subEntry.i92);
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    channel.position(entryOffset + 98 + j * 128 + i * 1284);
                    shortBuffer.clear();
                    shortBuffer.putShort((short)subEntry.damageSpecial);
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    channel.position(entryOffset + 100 + j * 128 + i * 1284);
                    shortBuffer.clear();
                    shortBuffer.putShort((short)subEntry.damageSpecial2);
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    channel.position(entryOffset + 102 + j * 128 + i * 1284);
                    shortBuffer.clear();
                    shortBuffer.putShort((short)subEntry.damageSpecial3);
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    channel.position(entryOffset + 104 + j * 128 + i * 1284);
                    shortBuffer.clear();
                    shortBuffer.putShort((short)subEntry.stumbleType);
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    channel.position(entryOffset + 106 + j * 128 + i * 1284);
                    shortBuffer.clear();
                    shortBuffer.putShort((short)subEntry.secondaryType);
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    channel.position(entryOffset + 108 + j * 128 + i * 1284);
                    shortBuffer.clear();
                    shortBuffer.putShort(subEntry.cameraShakeType);
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    channel.position(entryOffset + 110 + j * 128 + i * 1284);
                    shortBuffer.clear();
                    shortBuffer.putShort((short)subEntry.cameraShakeTime);
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    channel.position(entryOffset + 112 + j * 128 + i * 1284);
                    shortBuffer.clear();
                    shortBuffer.putShort(subEntry.userBpeID);
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    channel.position(entryOffset + 114 + j * 128 + i * 1284);
                    shortBuffer.clear();
                    shortBuffer.putShort(subEntry.victimBpeID);
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    channel.position(entryOffset + 116 + j * 128 + i * 1284);
                    shortBuffer.clear();
                    shortBuffer.putShort(subEntry.staminaBrokenOverrideBdmId);
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    channel.position(entryOffset + 118 + j * 128 + i * 1284);
                    shortBuffer.clear();
                    shortBuffer.putShort((short)subEntry.zVanishEnableTime);
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    channel.position(entryOffset + 120 + j * 128 + i * 1284);
                    shortBuffer.clear();
                    shortBuffer.putShort((short)subEntry.userAnimationTime);
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    channel.position(entryOffset + 122 + j * 128 + i * 1284);
                    shortBuffer.clear();
                    shortBuffer.putShort((short)subEntry.victimAnimationTime);
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    channel.position(entryOffset + 124 + j * 128 + i * 1284);
                    intBuffer.clear();
                    intBuffer.putFloat((float)subEntry.userAnimationSpeed);
                    intBuffer.flip();
                    channel.write(intBuffer);

                    channel.position(entryOffset + 128 + j * 128 + i * 1284);
                    intBuffer.clear();
                    intBuffer.putFloat((float)subEntry.victimAnimationSpeed);
                    intBuffer.flip();
                    channel.write(intBuffer);
                }
            }
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    public static enum DamageTypes {
        NoEffect(0),
        Block(1),
        GuardBreak(2),
        Standard(3),
        Heavy(4),
        Knockback(5),
        Knockback1(6),
        Knockback2(7),
        Knockback3(8),
        Knockback4(9),
        Grab(10),
        HoldStomach(11),
        HoldEyes(12),
        Knockback5(13),
        Electric(14),
        Dazed(15),
        Paralysis(16),
        Freeze(17),
        WildCard(18),
        Unused(19),
        HeavyStaminaBreak(20),
        LightStaminaBreak(21),
        GiganticKiBlastPush(22),
        BrainWash(23),
        GiganticKiBlastReturn(24),
        Knockback6(25),
        Knockback7(26),
        Knockback8(27),
        Knockback9(28),
        SlowOpponent(29),
        BrainWash2(30),
        TimeStop(31);

        final int index;

        DamageTypes(int index) {
            this.index = index;
        }
    }

    public static enum BdmGroups {
        SecondaryType,
        AlimentType,
        StumbleType
    }

    public static enum ACBTypes {
        Common(0),
        CharacterSE(2),
        CharacterVOX(3),
        SkillSE(10),
        SkillVOX(11);

        final int index;

        ACBTypes(int index) {
            this.index = index;
        }
    }

    public static enum Effect_EEPK_Types {
        Common(0),
        StageBG(1),
        CharacterEffect(2),
        AwokenSkill(3),
        SuperSkill(5),
        UltimateSkill(6),
        EvasiveSkill(7),
        KiBlastSkill(9),
        StageEffect(11);

        final int index;

        Effect_EEPK_Types(int index) {
            this.index = index;
        }
    }

    public static enum BdmValues {
        DamageType(0),
        I02(1),
        DamageAmount(2),
        I06(3),
        F08(4),
        ACB_Type(5),
        Cue_ID(6),
        Effect1_ID(7),
        Effect1_Skill_ID(8),
        Effect1_EEPK_Type(9),
        I22(10),
        Effect2_ID(11),
        Effect2_Skill_ID(12),
        Effect2_EEPK_Type(13),
        I30(14),
        Effect3_ID(15),
        Effect3_Skill_ID(16),
        Effect3_EEPK_Type(17),
        I38(18),
        PushbackStrength(19),
        PushbackAcceleration(20),
        UserStunt(21),
        KnockbackDuration(22),
        KnockbackRecoveryAfterImpactTime(23),
        KnockbackGroundImpactTime(24),
        I58(25),
        VictimStunt(26),
        KnockbackStrengthX(27),
        KnockbackStrengthY(28),
        KnockbackStrengthZ(29),
        KnockbackDragY(30),
        I76(31),
        KnockbackGravityTime(32),
        VictimInvincibilityTime(33),
        I82(34),
        TransformationType(35),
        AlimentType(36),
        I88(37),
        I90(38),
        I92(39),
        DamageSpecial(40),
        DamageSpecial2(41),
        DamageSpecial3(42),
        StumbleType(43),
        SecondaryType(44),
        CameraShakeType(45),
        CameraShakeTime(46),
        User_BPE_ID(47),
        Victim_BPE_ID(48),
        StaminaBrokenOverride_BDM_ID(49),
        ZVanishEnableTime(50), 
        UserAnimationTime(51),     
        VictimAnimationTime(52),    
        UserAnimationSpeed(53),         
        VictimAnimationSpeed(54);

        final int index;

        BdmValues(int index) {
            this.index = index;
        }
    }
}

class BdmEntry {
    public final BdmSubEntry[] subEntries = new BdmSubEntry[10];

    public BdmEntry() {
        for (int i = 0; i < 10; i++) {
            subEntries[i] = new BdmSubEntry();
        }
    }

    public BdmEntry(BdmEntry other) {
        for (int i = 0; i < 10; i++) {
            this.subEntries[i] = new BdmSubEntry(other.subEntries[i]);
        }
    }
}

class BdmSubEntry {
    public int damageType = 0;
    public int i02 = 0;
    public int damageAmount = 0;
    public int i06 = 0;
    public float f08 = 0.0f;
    public int acbType = 0;
    public short cueId = 0;
    public short effect1Id = 0;
    public int effect1SkillId = 0;
    public int effect1EepkType = 0;
    public int i22 = 0;
    public short effect2Id = 0;
    public int effect2SkillId = 0;
    public int effect2EepkType = 0;
    public int i30 = 0;
    public short effect3Id = 0;
    public int effect3SkillId = 0;
    public int effect3EepkType = 0;
    public int i38 = 0;
    public float pushbackStrength = 0.0f;
    public float pushbackAcceleration = 0.0f;
    public int userStunt = 0;
    public int knockbackDuration = 0;
    public int knockbackRecoveryAfterImpactTime = 0;
    public int knockbackGroundImpactTime = 0;
    public int i58 = 0;
    public int victimStunt = 0;
    public float knockbackStrengthX = 0.0f;
    public float knockbackStrengthY = 0.0f;
    public float knockbackStrengthZ = 0.0f;
    public float knockbackDragY = 0.0f;
    public int i76 = 0;
    public int knockbackGravityTime = 0;
    public short victimInvincibilityTime = 0;
    public int i82 = 0;
    public int transformationType = 0;
    public short alimentType = 0;
    public int i88 = 0;
    public int i90 = 0;
    public int i92 = 0;
    public int damageSpecial = 0;
    public int damageSpecial2 = 0;
    public int damageSpecial3 = 0;
    public int stumbleType = 0;
    public int secondaryType = 0;
    public short cameraShakeType = 0;
    public int cameraShakeTime = 0;
    public short userBpeID = 0;
    public short victimBpeID = 0;
    public short staminaBrokenOverrideBdmId = 0;
    public int zVanishEnableTime = 0;
    public int userAnimationTime = 0;
    public int victimAnimationTime = 0;
    public float userAnimationSpeed = 0.0f;
    public float victimAnimationSpeed = 0.0f;

    public BdmSubEntry() {}
    public BdmSubEntry(BdmSubEntry other) {
        this.damageType = other.damageType;
        this.i02 = other.i02;
        this.damageAmount = other.damageAmount;
        this.i06 = other.i06;
        this.f08 = other.f08;
        this.acbType = other.acbType;
        this.cueId = other.cueId;
        this.effect1Id = other.effect1Id;
        this.effect1SkillId = other.effect1SkillId;
        this.effect1EepkType = other.effect1EepkType;
        this.i22 = other.i22;
        this.effect2Id = other.effect2Id;
        this.effect2SkillId = other.effect2SkillId;
        this.effect2EepkType = other.effect2EepkType;
        this.i30 = other.i30;
        this.effect3Id = other.effect3Id;
        this.effect3SkillId = other.effect3SkillId;
        this.effect3EepkType = other.effect3EepkType;
        this.i38 = other.i38;
        this.pushbackStrength = other.pushbackStrength;
        this.pushbackAcceleration = other.pushbackAcceleration;
        this.userStunt = other.userStunt;
        this.knockbackDuration = other.knockbackDuration;
        this.knockbackRecoveryAfterImpactTime = other.knockbackRecoveryAfterImpactTime;
        this.knockbackGroundImpactTime = other.knockbackGroundImpactTime;
        this.i58 = other.i58;
        this.victimStunt = other.victimStunt;
        this.knockbackStrengthX = other.knockbackStrengthX;
        this.knockbackStrengthY = other.knockbackStrengthY;
        this.knockbackStrengthZ = other.knockbackStrengthZ;
        this.knockbackDragY = other.knockbackDragY;
        this.i76 = other.i76;
        this.knockbackGravityTime = other.knockbackGravityTime;
        this.victimInvincibilityTime = other.victimInvincibilityTime;
        this.i82 = other.i82;
        this.transformationType = other.transformationType;
        this.alimentType = other.alimentType;
        this.i88 = other.i88;
        this.i90 = other.i90;
        this.i92 = other.i92;
        this.damageSpecial = other.damageSpecial;
        this.damageSpecial2 = other.damageSpecial2;
        this.damageSpecial3 = other.damageSpecial3;
        this.stumbleType = other.stumbleType;
        this.secondaryType = other.secondaryType;
        this.cameraShakeType = other.cameraShakeType;
        this.cameraShakeTime = other.cameraShakeTime;
        this.userBpeID = other.userBpeID;
        this.victimBpeID = other.victimBpeID;
        this.staminaBrokenOverrideBdmId = other.staminaBrokenOverrideBdmId;
        this.zVanishEnableTime = other.zVanishEnableTime;
        this.userAnimationTime = other.userAnimationTime;
        this.victimAnimationTime = other.victimAnimationTime;
        this.userAnimationSpeed = other.userAnimationSpeed;
        this.victimAnimationSpeed = other.victimAnimationSpeed;
    }
}