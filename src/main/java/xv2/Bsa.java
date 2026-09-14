package xv2;
import static xv2.Unsigned.toUByte;
import static xv2.Unsigned.toUShort;
import static xv2.Unsigned.toUint32;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Spinner;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
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
import javafx.scene.paint.Color;
import xv2.Bsa.BsaType12Values.DeliveryMode;
import xv2.Bsa.BsaType13Values.ProtectAdditionalSelectorsFlags;
import xv2.Bsa.BsaType13Values.ProtectionFlags;
import xv2.Bsa.BsaType14Values.CMN_EEPK_Types;
import xv2.Bsa.BsaType14Values.PlacementModes;
import xv2.Bsa.BsaType3Values.BoundsTypes;
import xv2.Bsa.BsaType3Values.GrowMaxBoundsFlags;
import xv2.Bsa.BsaType6Values.EffectSwitchFlags;

public class Bsa {
    TreeView<String> treeView = new TreeView<>();

    HashMap<TreeItem<String>, BsaMainEntry> bsaMainHashMap = new HashMap<>();
    HashMap<TreeItem<String>, BsaCollisionEntry> bsaCollisionHashMap = new HashMap<>();
    HashMap<TreeItem<String>, BsaCollisionSoundEntry> bsaCollisionSoundHashMap = new HashMap<>();
    HashMap<TreeItem<String>, BsaType0Entry> bsaType0HashMap = new HashMap<>();
    HashMap<TreeItem<String>, BsaType1Entry> bsaType1HashMap = new HashMap<>();
    HashMap<TreeItem<String>, BsaType2Entry> bsaType2HashMap = new HashMap<>();
    HashMap<TreeItem<String>, BsaType3Entry> bsaType3HashMap = new HashMap<>();
    HashMap<TreeItem<String>, BsaType4Entry> bsaType4HashMap = new HashMap<>();
    HashMap<TreeItem<String>, BsaType6Entry> bsaType6HashMap = new HashMap<>();
    HashMap<TreeItem<String>, BsaType7Entry> bsaType7HashMap = new HashMap<>();
    HashMap<TreeItem<String>, BsaType8Entry> bsaType8HashMap = new HashMap<>();
    HashMap<TreeItem<String>, BsaType10Entry> bsaType10HashMap = new HashMap<>();
    HashMap<TreeItem<String>, BsaType12Entry> bsaType12HashMap = new HashMap<>();
    HashMap<TreeItem<String>, BsaType13Entry> bsaType13HashMap = new HashMap<>();
    HashMap<TreeItem<String>, BsaType14Entry> bsaType14HashMap = new HashMap<>();

    TreeItem<String> currentEntry = new TreeItem<>();
    TreeItem<String> grandParentEntry = new TreeItem<>();

    ContextMenu contextMenu = new ContextMenu();
    MenuItem addEntry = new MenuItem("Add Entry");
    Menu addSubEntry = new Menu("Add Sub Entry");
    MenuItem copy = new MenuItem("Copy Ctrl+C");
    MenuItem delete = new MenuItem("Delete Del");
    MenuItem addComment = new MenuItem("Add Comment  Ctrl+Q");
    MenuItem noCopiedItemFound = new MenuItem("no copied item found");
    MenuItem copiedItem = new MenuItem();
    MenuItem pasteItem = new MenuItem("dummy");
    MenuItem addItemCopy = new MenuItem();

    MenuItem collisionMenuItem = new MenuItem("Collision (After Effects)");
    MenuItem collisionSoundMenuItem = new MenuItem("Collision Sound (After Effects)");
    MenuItem type0MenuItem = new MenuItem("BSA Entry Passing");
    MenuItem type1MenuItem = new MenuItem("Movement");
    MenuItem type2MenuItem = new MenuItem("Projectile Timeline Remap");
    MenuItem type3MenuItem = new MenuItem("Hitbox");
    MenuItem type4MenuItem = new MenuItem("Deflection");
    MenuItem type6MenuItem = new MenuItem("Effect");
    MenuItem type7MenuItem = new MenuItem("Sound");
    MenuItem type8MenuItem = new MenuItem("Screen Effect");
    MenuItem type10MenuItem = new MenuItem("BSA Type 10");
    MenuItem type12MenuItem = new MenuItem("Send Projectile Signal");
    MenuItem type13MenuItem = new MenuItem("Projectile Protection");
    MenuItem type14MenuItem = new MenuItem("Effect Placement");

    TabPane tabPane = new TabPane();

    Object copyContainer = new Object();
    Object[][] copyListContainer;
    String [] copyTypesContainer;

    int allEntries;

    public Bsa() {
        entriesActionListener();
        entriesKeysListener();
    }

    public SplitPane createSplitPane() {
        SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(treeView, tabPane);
        splitPane.setDividerPositions(0.245);
        splitPane.getStylesheets().add(getClass().getResource("/style.css").toExternalForm()); 

        return splitPane;
    }

    private void createBsaMain(BsaMainEntry entry) {
        Node[] impactProperties = new Node[] {
            createLabel("A", 0),
            createSpinner(50, 0, 15, entry.i16_a, BsaMainValues.I16_A),
            createLabel("B", 0),
            createSpinner(50, 0, 15, entry.i16_b, BsaMainValues.I16_B),
        };

        VBox entryVBox = new VBox(30, 
            createHBox(0, createLabel("Impact Properties", 250), createHBox(10, impactProperties, false)),
            createHBox(0, createLabel("Lifetime", 250), createSpinner(0, 0, 65535, entry.lifetime, BsaMainValues.Lifetime)),
            createHBox(0, createLabel("Entry Pass On When Expires", 250), createSpinner(0, 0, 65535, entry.expires, BsaMainValues.Expires)),
            createHBox(0, createLabel("Entry Pass On When Impact Projectile", 250), createSpinner(0, 0, 65535, entry.impactProjectile, BsaMainValues.ImpactProjectile)),
            createHBox(0, createLabel("Entry Pass On When Impact Enemy", 250), createSpinner(0, 0, 65535, entry.impactEnemy, BsaMainValues.ImpactEnemy)), 
            createHBox(0, createLabel("Entry Pass On When Impact Ground", 250), createSpinner(0, 0, 65535, entry.impactGround, BsaMainValues.ImpactGround))
        );
        entryVBox.setPadding(new Insets(20, 0, 0, 16));

        VBox unknownVBox = new VBox(30, 
            createHBox(0, createLabel("I_00", 60), createTextField(entry.i00, BsaMainValues.I00)), 
            createHBox(0, createLabel("I_17", 60), createTextField(entry.i17, BsaMainValues.I17)), 
            createHBox(0, createLabel("I_18", 60), createTextField(entry.i18, BsaMainValues.I18)),
            createHBox(0, createLabel("I_24", 60), createTextField(entry.i24, BsaMainValues.I24)),
            createHBox(0, createLabel("I_40", 60), createTextField(entry.i40, BsaMainValues.I40)), 
            createHBox(0, createLabel("I_44", 60), createTextField(entry.i44, BsaMainValues.I44)), 
            createHBox(0, createLabel("I_48", 60), createTextField(entry.i48, BsaMainValues.I48))
        );
        unknownVBox.setPadding(new Insets(20, 0, 0, 16));

        Tab entryTab = new Tab("Entry", entryVBox);
        entryTab.setClosable(false);

        Tab unknownTab = new Tab("Unknown", unknownVBox);
        unknownTab.setClosable(false);

        tabPane.getTabs().addAll(entryTab, unknownTab);
    }

    private void createBsaCollision(BsaCollisionEntry entry) {
        ToggleGroup eepkTypeToggleGroup = new ToggleGroup();

        RadioButton[] eepkTypes = new RadioButton[] {
            createRadioButton("Common", eepkTypeToggleGroup, BsaCollisionValues.EEPK_Types.Common),
            createRadioButton("StageBG", eepkTypeToggleGroup, BsaCollisionValues.EEPK_Types.StageBG),
            createRadioButton("Character", eepkTypeToggleGroup, BsaCollisionValues.EEPK_Types.CharacterEffect),
            createRadioButton("Awoken Skill", eepkTypeToggleGroup, BsaCollisionValues.EEPK_Types.AwokenSkill),
            createRadioButton("Super Skill", eepkTypeToggleGroup, BsaCollisionValues.EEPK_Types.SuperSkill),
            createRadioButton("Ultimate Skill", eepkTypeToggleGroup, BsaCollisionValues.EEPK_Types.UltimateSkill),
            createRadioButton("Evasive Skill", eepkTypeToggleGroup, BsaCollisionValues.EEPK_Types.EvasiveSkill),
            createRadioButton("Ki Blast Skill", eepkTypeToggleGroup, BsaCollisionValues.EEPK_Types.KiBlastSkill),
            createRadioButton("Stage", eepkTypeToggleGroup, BsaCollisionValues.EEPK_Types.Stage),
        };

        VBox collisionVBox = new VBox(30, 
            createHBox(0, createLabel("EEPK Type", 100), createGridPane(3, 3, eepkTypes, true)), 
            createHBox(0, createLabel("Skill ID", 100), createSpinner(0, 65535, entry.skillId, BsaCollisionValues.Skill_ID)), 
            createHBox(0, createLabel("Effect ID", 100), createSpinner(0, 65535, entry.effectId, BsaCollisionValues.Effect_ID))
        );
        collisionVBox.setPadding(new Insets(20, 0, 0, 16));
 
        VBox unknownVBox = new VBox(30, 
            createHBox(0, createLabel("I_06", 60), createTextField(entry.i06, BsaCollisionValues.I06)), 
            createHBox(0, createLabel("I_08", 60), createTextField(entry.i08, BsaCollisionValues.I08)), 
            createHBox(0, createLabel("I_12", 60), createTextField(entry.i12, BsaCollisionValues.I12)), 
            createHBox(0, createLabel("I_16", 60), createTextField(entry.i16, BsaCollisionValues.I16)), 
            createHBox(0, createLabel("I_20", 60), createTextField(entry.i20, BsaCollisionValues.I20))
        );
        unknownVBox.setPadding(new Insets(20, 0, 0, 16));

        Tab collisionTab = new Tab("Collision", collisionVBox);
        collisionTab.setClosable(false);

        Tab unknownTab = new Tab("Unknown", unknownVBox);
        unknownTab.setClosable(false);

        tabPane.getTabs().addAll(collisionTab, unknownTab);
    }

    private void createBsaCollisionSound(BsaCollisionSoundEntry entry) {
        ToggleGroup acbTypeToggleGroup = new ToggleGroup();

        RadioButton[] acbTypes = new RadioButton[] {
            createRadioButton("Common", acbTypeToggleGroup, BsaCollisionSoundValues.ACB_Types.Common_SE), 
            createRadioButton("Character SE", acbTypeToggleGroup, BsaCollisionSoundValues.ACB_Types.Character_SE),
            createRadioButton("Character VOX", acbTypeToggleGroup, BsaCollisionSoundValues.ACB_Types.Character_VOX),
            createRadioButton("Skill SE", acbTypeToggleGroup, BsaCollisionSoundValues.ACB_Types.Skill_SE),
            createRadioButton("Skill VOX", acbTypeToggleGroup, BsaCollisionSoundValues.ACB_Types.Skill_VOX)
        };

        VBox collisionSoundVBox = new VBox(30, 
            createHBox(0, createLabel("ACB Type", 100), createHBox(15, acbTypes, true)),
            createHBox(0, createLabel("Cue ID", 100), createSpinner(0, 65535, entry.cueId, BsaCollisionSoundValues.CUE_ID))
        );
        collisionSoundVBox.setPadding(new Insets(20, 0, 0, 16));

        VBox unknownVBox = new VBox(30,
            createHBox(0, createLabel("I_02", 60), createTextField(entry.i02, BsaCollisionSoundValues.I02)), 
            createHBox(0, createLabel("I_06", 60), createTextField(entry.i06, BsaCollisionSoundValues.I06))
        );
        unknownVBox.setPadding(new Insets(20, 0, 0, 16));

        Tab collisionSoundTab = new Tab("Collision Sound", collisionSoundVBox);
        collisionSoundTab.setClosable(false);

        Tab unknownTab = new Tab("Unknown", unknownVBox);
        unknownTab.setClosable(false);

        tabPane.getTabs().addAll(collisionSoundTab, unknownTab);
    }

    private void createBsaType0(BsaType0Entry entry) {
        VBox entryPassingVBox = new VBox(35,
            createHBox(0, createLabel("Start Time", 120), createSpinner(0, 65535, entry.startTime, BsaType0Values.StartTime)), 
            createHBox(0, createLabel("Duration", 120), createSpinner(0, 65535, entry.duration, BsaType0Values.Duration)),  
            createHBox(0, createLabel("BSA Entry ID", 120), createSpinner(0, 65535, entry.bsaEntryId, BsaType0Values.BsaEntryID)),
            createHBox(0, createLabel("Main Condition", 120), createSpinner(0, 65535, entry.mainCondition, BsaType0Values.MainConditon)),
            createHBox(0, createLabel("BAC Conditon", 120), createSpinner(-Float.MAX_VALUE, Float.MAX_VALUE, entry.bacCondition, BsaType0Values.BAC_Conditon))
        );
        entryPassingVBox.setPadding(new Insets(20 ,0, 0, 16));

        VBox unknownVBox = new VBox(35,
            createHBox(0, createLabel("I_00", 60), createTextField(entry.i00, BsaType0Values.I00)),
            createHBox(0, createLabel("I_06", 60), createTextField(entry.i06, BsaType0Values.I06)),
            createHBox(0, createLabel("F_12", 60), createTextField(entry.f12, BsaType0Values.F12))
        );
        unknownVBox.setPadding(new Insets(20, 0, 0, 16));

        Tab entryPassingTab = new Tab("Entry Passing", entryPassingVBox);
        entryPassingTab.setClosable(false);

        Tab unknownTab = new Tab("Unknown", unknownVBox);
        unknownTab.setClosable(false);

        tabPane.getTabs().addAll(entryPassingTab, unknownTab);
    }

    private void createBsaType1(BsaType1Entry entry) {
        CheckBox[] option1 = new CheckBox[] {
            new CheckBox("Unknown 1"),
            new CheckBox("Unknown 2"),
            new CheckBox("Unknown 3"),
            new CheckBox("Unknown 4")
        };

        CheckBox[] option2 = new CheckBox[] {
            new CheckBox("Unknown 5"),
            new CheckBox("Unknown 6"),
            new CheckBox("Unknown 7"),
            new CheckBox("Unknown 8")
        };

        CheckBox[] option3 = new CheckBox[] {
            new CheckBox("Unknown 9"),
            new CheckBox("Unknown 10"),
            new CheckBox("Unknown 11"),
            new CheckBox("Unknown 12")
        };

        CheckBox[] option4 = new CheckBox[] {
            new CheckBox("Unknown 13"),
            new CheckBox("Unknown 14"),
            new CheckBox("Unknown 15"),
            new CheckBox("Unknown 16")
        };

        CheckBox[] option5 = new CheckBox[] {
            new CheckBox("Unknown 17"),
            new CheckBox("Opponent Tracking"),
            new CheckBox("Unknown 19"),
            new CheckBox("Unknown 20")
        };

        CheckBox[] option6 = new CheckBox[] {
            new CheckBox("Unknown 21"),
            new CheckBox("Free Movement"),
            new CheckBox("Unknown 23"),
            new CheckBox("Unknown 24")
        };

        CheckBox[] option7 = new CheckBox[] {
            new CheckBox("Unknown 25"),
            new CheckBox("Unknown 26"),
            new CheckBox("Unknown 27"),
            new CheckBox("Unknown 28")
        };

        CheckBox[] option8 = new CheckBox[] {
            new CheckBox("Unknown 29"),
            new CheckBox("Unknown 30"),
            new CheckBox("Unknown 31"),
            new CheckBox("Unknown 32")
        };

        Node[] motionFlags = new Node[] {
            createCheckBoxGroup("Options 1", option1, 1L, BsaType1Values.MotionFlags),
            createCheckBoxGroup("Options 2", option2, 16L, BsaType1Values.MotionFlags),
            createCheckBoxGroup("Options 3", option3, 256L, BsaType1Values.MotionFlags),
            createCheckBoxGroup("Options 4", option4, 4096L, BsaType1Values.MotionFlags),
            createCheckBoxGroup("Options 5", option5, 65536L, BsaType1Values.MotionFlags),
            createCheckBoxGroup("Options 6", option6, 1048576L, BsaType1Values.MotionFlags),
            createCheckBoxGroup("Options 7", option7, 16777216L, BsaType1Values.MotionFlags),
            createCheckBoxGroup("Options 8", option8, 268435456L, BsaType1Values.MotionFlags)
        };

        VBox movementVBox = new VBox(35, 
            createHBox(0, createLabel("Start Time", 120), createSpinner(0, 65535, entry.startTime, BsaType1Values.StartTime)), 
            createHBox(0, createLabel("Duration", 120), createSpinner(0, 65535, entry.duration, BsaType1Values.Duration)), 
            createHBox(0, createLabel("Motion Flags", 120), createGridPane(4, 2, motionFlags, false)), 
            createHBox(0, createLabel("Speed X", 120), createSpinner(-Float.MAX_VALUE, Float.MAX_VALUE, entry.speedX, BsaType1Values.SpeedX)),
            createHBox(0, createLabel("Speed Y", 120), createSpinner(-Float.MAX_VALUE, Float.MAX_VALUE, entry.speedY, BsaType1Values.SpeedY)), 
            createHBox(0, createLabel("Speed Z", 120), createSpinner(-Float.MAX_VALUE, Float.MAX_VALUE, entry.speedZ, BsaType1Values.SpeedZ)), 
            createHBox(0, createLabel("Acceleration X", 120), createSpinner(-Float.MAX_VALUE, Float.MAX_VALUE, entry.accelerationX, BsaType1Values.AccelerationX)),
            createHBox(0, createLabel("Acceleration Y", 120), createSpinner(-Float.MAX_VALUE, Float.MAX_VALUE, entry.accelerationY, BsaType1Values.AccelerationY)), 
            createHBox(0, createLabel("Acceleration Z", 120), createSpinner(-Float.MAX_VALUE, Float.MAX_VALUE, entry.accelerationZ, BsaType1Values.AccelerationZ)),  
            createHBox(0, createLabel("Falloff Strenght", 120), createSpinner(-Float.MAX_VALUE, Float.MAX_VALUE, entry.fallofStrength, BsaType1Values.FalloffStrength)),
            createHBox(0, createLabel("Spread Direction X", 120), createSpinner(-Float.MAX_VALUE, Float.MAX_VALUE, entry.spreadDirectionX, BsaType1Values.SpreadDirectionX)),
            createHBox(0, createLabel("Spread Direction Y", 120), createSpinner(-Float.MAX_VALUE, Float.MAX_VALUE, entry.spreadDirectionY, BsaType1Values.SpreadDirectionY)), 
            createHBox(0, createLabel("Spread Direction Z", 120), createSpinner(-Float.MAX_VALUE, Float.MAX_VALUE, entry.spreadDirectionZ, BsaType1Values.SpreadDirectionZ))
        );
        movementVBox.setPadding(new Insets(20, 0, 20, 16));

        VBox unknownVBox = new VBox(30, 
            createHBox(0, createLabel("F_16", 60), createTextField(entry.f16, BsaType1Values.F16))
        );
        unknownVBox.setPadding(new Insets(20, 0, 0, 16));

        Tab movementTab = new Tab("Movement", new ScrollPane(movementVBox));
        movementTab.setClosable(false);

        Tab unknownTab = new Tab("Unknown", unknownVBox);
        unknownTab.setClosable(false);

        tabPane.getTabs().addAll(movementTab, unknownTab);
    }

    private void createBsaType2(BsaType2Entry entry) {
        VBox projectileTimelineRemapVBox = new VBox(30,
            createHBox(0, createLabel("Start Time", 120), createSpinner(0, 65535, entry.startTime, BsaType2Values.StartTime)), 
            createHBox(0, createLabel("Duration", 120), createSpinner(0, 65535, entry.duration, BsaType2Values.Duration)),  
            createHBox(0, createLabel("Output Start Frame", 120), createSpinner(Short.MIN_VALUE, Short.MAX_VALUE, entry.outputStartFrame, BsaType2Values.OutputStartFrame)), 
            createHBox(0, createLabel("Output End Frame", 120), createSpinner(Short.MIN_VALUE, Short.MAX_VALUE, entry.outputEndFrame, BsaType2Values.OutputEndFrame)) 
        );
        projectileTimelineRemapVBox.setPadding(new Insets(20, 0, 0, 16));

        VBox unknownVBox = new VBox(30, 
            createHBox(0, createLabel("I_00", 60), createTextField(entry.i00, BsaType2Values.I00)),
            createHBox(0, createLabel("I_06", 60), createTextField(entry.i06, BsaType2Values.I06))
        );
        unknownVBox.setPadding(new Insets(20, 0, 0, 16));

        Tab projectileTimelineRemapTab = new Tab("Projectile Timeline Remap", projectileTimelineRemapVBox);
        projectileTimelineRemapTab.setClosable(false);

        Tab unknownTab = new Tab("Unknown", unknownVBox);
        unknownTab.setClosable(false);

        tabPane.getTabs().addAll(projectileTimelineRemapTab, unknownTab);
    }

    private void createBsaType3(BsaType3Entry entry) {
        ToggleGroup growMaxBoundsToggleGroup = new ToggleGroup();
        ToggleGroup boundsTypesToggleGroup = new ToggleGroup();

        RadioButton[] growMaxBoundsFlags = new RadioButton[] {
            createRadioButton("On", growMaxBoundsToggleGroup, GrowMaxBoundsFlags.On), 
            createRadioButton("Off", growMaxBoundsToggleGroup, GrowMaxBoundsFlags.Off)
        };

        RadioButton[] boundsTypes = new RadioButton[] {
            createRadioButton("Uniform", boundsTypesToggleGroup, BoundsTypes.Uniform),
            createRadioButton("MinMax", boundsTypesToggleGroup, BoundsTypes.MinMax),
            createRadioButton("Unknown 2", boundsTypesToggleGroup, BoundsTypes.Unknown2),
            createRadioButton("Unknown 3", boundsTypesToggleGroup, BoundsTypes.Unknown3),
            createRadioButton("Unknown 4", boundsTypesToggleGroup, BoundsTypes.Unknown4),
        };

        Node[] i06 = new Node[] {
            createLabel("A", 0),
            createSpinner(50, 0, 15, entry.i06_a, BsaType3Values.I06_A),
            createLabel("B", 0),
            createSpinner(50, 0, 15, entry.i06_b, BsaType3Values.I06_B),
            createLabel("C", 0),
            createSpinner(50, 0, 15, entry.i06_c, BsaType3Values.I06_C),
            createLabel("D", 0),
            createSpinner(50, 0, 15, entry.i06_d, BsaType3Values.I06_D),
        };

        VBox hitboxVBox = new VBox(40, 
            createHBox(0, createLabel("Start Time", 140), createSpinner(0, 0, 65535, entry.startTime, BsaType3Values.StartTime)), 
            createHBox(0, createLabel("Duration", 140), createSpinner(0, 0, 65535, entry.duration, BsaType3Values.Duration)),  
            createHBox(0, createLabel("Position X", 140), createSpinner(0, -Float.MAX_VALUE, Float.MAX_VALUE, entry.positionX, BsaType3Values.PositionX)),
            createHBox(0, createLabel("Position Y", 140), createSpinner(0, -Float.MAX_VALUE, Float.MAX_VALUE, entry.positionY, BsaType3Values.PositionY)), 
            createHBox(0, createLabel("Position Z", 140), createSpinner(0, -Float.MAX_VALUE, Float.MAX_VALUE, entry.positionZ, BsaType3Values.PositionZ)),
            createHBox(0, createLabel("Hitbox Scale", 140), createSpinner(0, -Float.MAX_VALUE, Float.MAX_VALUE, entry.hitboxScale, BsaType3Values.HitboxScale)),
            createHBox(0, createLabel("Hit Amount", 140), createSpinner(0, 0, 65535, entry.hitAmount, BsaType3Values.HitAmount)), 
            createHBox(0, createLabel("Hitbox Lifetime", 140), createSpinner(0, 0, 65535, entry.hitboxLifetime, BsaType3Values.HitboxLifetime)), 
            createHBox(0, createLabel("BDM ID First Hit", 140), createSpinner(0, 0, 65535, entry.firstHit, BsaType3Values.FirstHit)), 
            createHBox(0, createLabel("BDM ID Multiple Hits", 140), createSpinner(0, 0, 65535, entry.multipleHits, BsaType3Values.MultipleHits)), 
            createHBox(0, createLabel("BDM ID Last Hit", 140), createSpinner(0, 0, 65535, entry.lastHit, BsaType3Values.LastHit))
        );
        hitboxVBox.setPadding(new Insets(20, 0, 16, 16));

        VBox matrixVBox = new VBox(30,
            createHBox(0, createLabel("Grow Max Bounds", 140), createHBox(15, growMaxBoundsFlags, true)),
            createHBox(0, createLabel("Bounds Type", 140), createHBox(15, boundsTypes, true)),
            createHBox(0, createLabel("Maximum X", 140), createSpinner(0, -Float.MAX_VALUE, Float.MAX_VALUE, entry.maximumX, BsaType3Values.MaximumX)),
            createHBox(0, createLabel("Maximum Y", 140), createSpinner(0, -Float.MAX_VALUE, Float.MAX_VALUE, entry.maximumY, BsaType3Values.MaximumY)), 
            createHBox(0, createLabel("Maximum Z", 140), createSpinner(0, -Float.MAX_VALUE, Float.MAX_VALUE, entry.maximumZ, BsaType3Values.MaximumZ)), 
            createHBox(0, createLabel("Minimum X", 140), createSpinner(0, -Float.MAX_VALUE, Float.MAX_VALUE, entry.minimumX, BsaType3Values.MinimumX)),
            createHBox(0, createLabel("Minimum Y", 140), createSpinner(0, -Float.MAX_VALUE, Float.MAX_VALUE, entry.minimumY, BsaType3Values.MinimumY)), 
            createHBox(0, createLabel("Minimum Z", 140), createSpinner(0, -Float.MAX_VALUE, Float.MAX_VALUE, entry.minimumZ, BsaType3Values.MinimumZ))
        );
        matrixVBox.setPadding(new Insets(20, 0, 0, 16));

        VBox unknownVBox = new VBox(30, 
            createHBox(0, createLabel("I_02", 60), createTextField(entry.i02, BsaType3Values.I02)),
            createHBox(0, createLabel("I_06", 60), createHBox(10, i06, false)),
            createHBox(0, createLabel("I_52", 60), createTextField(entry.i52, BsaType3Values.I52)),
            createHBox(0, createLabel("I_54", 60), createTextField(entry.i54, BsaType3Values.I54)),
            createHBox(0, createLabel("I_56", 60), createTextField(entry.i56, BsaType3Values.I56))
        );
        unknownVBox.setPadding(new Insets(20, 0, 0, 16));

        Tab hitboxTab = new Tab("Hitbox", hitboxVBox);
        hitboxTab.setClosable(false);

        Tab matrixTab = new Tab("Matrix", matrixVBox);
        matrixTab.setClosable(false);

        Tab unknownTab = new Tab("Unknown", unknownVBox);
        unknownTab.setClosable(false);

        tabPane.getTabs().addAll(hitboxTab, matrixTab, unknownTab);
    }

    private void createBsaType4(BsaType4Entry entry) {
        VBox deflectionVBox = new VBox(30, 
            createHBox(0, createLabel("Start Time", 80), createSpinner(0, 65535, entry.startTime, BsaType4Values.StartTime)), 
            createHBox(0, createLabel("Duration", 80), createSpinner( 0, 65535, entry.duration, BsaType4Values.Duration)),   
            createHBox(0, createLabel("I_00", 80), createTextField(entry.i00, BsaType4Values.I00)),
            createHBox(0, createLabel("I_04", 80), createTextField(entry.i04, BsaType4Values.I04)), 
            createHBox(0, createLabel("I_08", 80), createTextField(entry.i08, BsaType4Values.I08)), 
            createHBox(0, createLabel("F_12", 80), createTextField(entry.f12, BsaType4Values.F12)), 
            createHBox(0, createLabel("F_16", 80), createTextField(entry.f16, BsaType4Values.F16)), 
            createHBox(0, createLabel("F_20", 80), createTextField(entry.f20, BsaType4Values.F20)), 
            createHBox(0, createLabel("I_24", 80), createTextField(entry.i24, BsaType4Values.I24)), 
            createHBox(0, createLabel("I_28", 80), createTextField(entry.i28, BsaType4Values.I28)), 
            createHBox(0, createLabel("I_32", 80), createTextField(entry.i32, BsaType4Values.I32)), 
            createHBox(0, createLabel("I_36", 80), createTextField(entry.i36, BsaType4Values.I36)), 
            createHBox(0, createLabel("I_40", 80), createTextField(entry.i40, BsaType4Values.I40)), 
            createHBox(0, createLabel("I_44", 80), createTextField(entry.i44, BsaType4Values.I44)), 
            createHBox(0, createLabel("I_48", 80), createTextField(entry.i48, BsaType4Values.I48)), 
            createHBox(0, createLabel("I_50", 80), createTextField(entry.i50, BsaType4Values.I50)), 
            createHBox(0, createLabel("I_52", 80), createTextField(entry.i52, BsaType4Values.I52)), 
            createHBox(0, createLabel("I_54", 80), createTextField(entry.i54, BsaType4Values.I54))
        );
        deflectionVBox.setPadding(new Insets(20, 0, 20, 16));

        Tab deflectionTab = new Tab("Deflection", new ScrollPane(deflectionVBox));
        deflectionTab.setClosable(false);

        tabPane.getTabs().add(deflectionTab);
    }

    private void createBsaType6(BsaType6Entry entry) {
        ToggleGroup effectSwitchToggleGroup = new ToggleGroup();
        ToggleGroup eepkTypeToggleGroup = new ToggleGroup();

        RadioButton[] eepkTypes = new RadioButton[] {
            createRadioButton("Common", eepkTypeToggleGroup, BsaType6Values.EEPK_Types.Common),
            createRadioButton("StageBG", eepkTypeToggleGroup, BsaType6Values.EEPK_Types.StageBG),
            createRadioButton("Character", eepkTypeToggleGroup, BsaType6Values.EEPK_Types.CharacterEffect),
            createRadioButton("Awoken Skill", eepkTypeToggleGroup, BsaType6Values.EEPK_Types.AwokenSkill),
            createRadioButton("Super Skill", eepkTypeToggleGroup, BsaType6Values.EEPK_Types.SuperSkill),
            createRadioButton("Ultimate Skill", eepkTypeToggleGroup, BsaType6Values.EEPK_Types.UltimateSkill),
            createRadioButton("Evasive Skill", eepkTypeToggleGroup, BsaType6Values.EEPK_Types.EvasiveSkill),
            createRadioButton("Ki Blast Skill", eepkTypeToggleGroup, BsaType6Values.EEPK_Types.KiBlastSkill),
            createRadioButton("Stage", eepkTypeToggleGroup, BsaType6Values.EEPK_Types.Stage),
        };

        RadioButton[] effectSwitch = new RadioButton[] {
            createRadioButton("On", effectSwitchToggleGroup, EffectSwitchFlags.On), 
            createRadioButton("Off", effectSwitchToggleGroup, EffectSwitchFlags.Off)
        };

        VBox effectVBox = new VBox(45, 
            createHBox(0, createLabel("Start Time", 100), createSpinner(0, 65535, entry.startTime, BsaType6Values.StartTime)), 
            createHBox(0, createLabel("Duration", 100), createSpinner( 0, 65535, entry.duration, BsaType6Values.Duration)),  
            createHBox(0, createLabel("EEPK Type", 100), createGridPane(3, 3, eepkTypes, true)), 
            createHBox(0, createLabel("Skill ID", 100), createSpinner( 0, 65535, entry.skillId, BsaType6Values.Skill_ID)), 
            createHBox(0, createLabel("Effect ID", 100), createSpinner( 0, 65535, entry.effectId, BsaType6Values.Effect_ID)),
            createHBox(0, createLabel("Switch", 100), createHBox(15, effectSwitch, true)), 
            createHBox(0, createLabel("Position X", 100), createSpinner(-Float.MAX_VALUE, Float.MAX_VALUE, entry.positionX, BsaType6Values.PositionX)),
            createHBox(0, createLabel("Position Y", 100), createSpinner(-Float.MAX_VALUE, Float.MAX_VALUE, entry.positionY, BsaType6Values.PositionY)), 
            createHBox(0, createLabel("Position Z", 100), createSpinner(-Float.MAX_VALUE, Float.MAX_VALUE, entry.positionZ, BsaType6Values.PositionZ))
        );

        VBox unknownVBox = new VBox(30, 
            createHBox(0, createLabel("I_06", 60), createTextField(entry.i06, BsaType6Values.I06)), 
            createHBox(0, createLabel("I_10", 60), createTextField(entry.i10, BsaType6Values.I10))
        );
        unknownVBox.setPadding(new Insets(20, 0, 0, 16));

        effectVBox.setPadding(new Insets(20, 0, 0, 16));

        Tab effectTab = new Tab("Effect", effectVBox);
        effectTab.setClosable(false);

        Tab unknownTab = new Tab("Unknown", unknownVBox);
        unknownTab.setClosable(false);

        tabPane.getTabs().addAll(effectTab, unknownTab);
    }

    private void createBsaType7(BsaType7Entry entry) {
        ToggleGroup acbTypeToggleGroup = new ToggleGroup();

        RadioButton[] acbTypes = new RadioButton[] {
            createRadioButton("Common", acbTypeToggleGroup, BsaType7Values.ACB_Types.Common_SE), 
            createRadioButton("Character SE", acbTypeToggleGroup, BsaType7Values.ACB_Types.Character_SE),
            createRadioButton("Character VOX", acbTypeToggleGroup, BsaType7Values.ACB_Types.Character_VOX),
            createRadioButton("Skill SE", acbTypeToggleGroup, BsaType7Values.ACB_Types.Skill_SE),
            createRadioButton("Skill VOX", acbTypeToggleGroup, BsaType7Values.ACB_Types.Skill_VOX)
        };

        VBox soundVBox = new VBox(30, 
            createHBox(0, createLabel("Start Time", 100), createSpinner(0, 65535, entry.startTime, BsaType7Values.StartTime)), 
            createHBox(0, createLabel("Duration", 100), createSpinner( 0, 65535, entry.duration, BsaType7Values.Duration)),
            createHBox(0, createLabel("ACB Type", 100), createHBox(15, acbTypes, true)), 
            createHBox(0, createLabel("Cue ID", 100), createSpinner( 0, 65535, entry.cueId, BsaType7Values.Cue_ID))
        );
        soundVBox.setPadding(new Insets(20, 0, 0, 16));

        Tab soundTab = new Tab("Sound",soundVBox);
        soundTab.setClosable(false);

        VBox unknownVBox = new VBox(30, 
            createHBox(0, createLabel("I_02", 60), createTextField(entry.i02, BsaType7Values.I02)),
            createHBox(0, createLabel("I_06", 60), createTextField(entry.i06, BsaType7Values.I06)) 
        );
        unknownVBox.setPadding(new Insets(20, 0, 0, 16));

        Tab unknownTab = new Tab("Unknown", unknownVBox);
        unknownTab.setClosable(false);

        tabPane.getTabs().addAll(soundTab, unknownTab);
    }

    private void createBsaType8(BsaType8Entry entry) {
        Spinner<Number> spinner = createSpinner(0, 65535, entry.bpeEffectId, BsaType8Values.BPE_Effect_ID);

        CheckBox[] screenEffectsGroup1 = new CheckBox[] {
            new CheckBox("Unknown 1"),
            new CheckBox("Disable Effect"),
            new CheckBox("Unknown 3"),
            new CheckBox("Allow Loop")
        };

        CheckBox[] screenEffectsGroup2 = new CheckBox[] {
            new CheckBox("Unknown 5"),
            new CheckBox("Unknown 6"),
            new CheckBox("Unknown 7"),
            new CheckBox("Unknown 8")
        };

        CheckBox[] screenEffectsGroup3 = new CheckBox[] {
            new CheckBox("Unknown 9"),
            new CheckBox("Unknown 10"),
            new CheckBox("Unknown 11"),
            new CheckBox("Unknown 12"),
        };

        CheckBox[] screenEffectsGroup4 = new CheckBox[] {
            new CheckBox("Unknown 13"),
            new CheckBox("Unknown 14"),
            new CheckBox("Unknown 15"),
            new CheckBox("Unknown 16"),
        };

        Node[] screenEffectsFlags = new Node[] {
            createCheckBoxGroup("Flag Group 1", screenEffectsGroup1, 1, BsaType8Values.ScreenEffectFlags),
            createCheckBoxGroup("Flag Group 2", screenEffectsGroup2, 16, BsaType8Values.ScreenEffectFlags), 
            createCheckBoxGroup("Flag Group 3", screenEffectsGroup3, 256, BsaType8Values.ScreenEffectFlags),
            createCheckBoxGroup("Flag Group 4", screenEffectsGroup4, 4096, BsaType8Values.ScreenEffectFlags)
        };

        Label label = new Label();
        label.setTextFill(Color.CRIMSON);
        label.textProperty().bind(
            Bindings.createStringBinding(() -> {
                return switch (spinner.getValue().intValue()) {
                    case 0, 255, 256 -> "White Flash";
                    case 1 -> "Strong White Flash";
                    case 2 -> "Weak White Flash";
                    case 3 -> "Weaker White Flash";
                    case 4 -> "Weak Character Illumination";
                    case 5 -> "Mild Desaturated";
                    case 6 -> "2 Consecutive Mild Flashes";
                    case 10 -> "Small Motion Blur/Shake";
                    case 11 -> "Strong Motion Blur/Shake";
                    case 12 -> "Rapid Motion Blur/Shake";
                    case 13 -> "Tiny Motion Blur/Shake";
                    case 14 -> "Side Screen Shake, Blue Filter";
                    case 15 -> "Very Quick Sideway Motion Blur/Shake 1";
                    case 16 -> "Very Quick Sideway Motion Blur/Shake 2";
                    case 17 -> "Dark, Purple Flash";
                    case 18 -> "Motion Blur/Shake Sequence";
                    case 20 -> "Underwater Blur Effect 1";
                    case 21 -> "Underwater Blur Effect 2";
                    case 22 -> "Underwater Blur Effect 3";
                    case 23 -> "Underwater Blur Effect 4";
                    case 24 -> "Underwater Blur Effect 5";
                    case 26 -> "Wave-like Blur";
                    case 27 -> "Upward-flow Liquid Blur";
                    case 30 -> "Solar Flare Screen Effects";
                    case 31 -> "Shaking And Dark Flash";
                    case 32 -> "Shadowy Flash";
                    case 33 -> "Consecutive Shadowy Flashes";
                    case 34 -> "Consecutive Shadowy Flashes, Dark Screen";
                    case 35 -> "Shadowy Flash, Longer";
                    case 36 -> "Desaturated Flash";
                    case 37 -> "Black Screen";
                    case 40 -> "Expanding Transparent Ring 1";
                    case 41 -> "Expanding Transparent Ring 2";
                    case 42 -> "Expanding Transparent Ring 3";
                    case 43 -> "Expanding Distorted Ring";
                    case 44 -> "Expanding Distorted Ring, Bigger";
                    case 45 -> "Expanding Transparent Ring 2, Quick";
                    case 46 -> "Expanding Transparent Ring 3, Quick";
                    case 50 -> "Brighten Screen 1";
                    case 51 -> "Brighten Screen 2";
                    case 52 -> "Expanding Transparent Sphere";
                    case 53 -> "Flashing Blue Hue";
                    case 54 -> "Dark Screen 1";
                    case 55 -> "Darken And Desaturate Screen 1";
                    case 56 -> "Darken And Desaturate Screen 2";
                    case 57 -> "Darken Screen 2";
                    case 59 -> "Darken And Desaturate Screen 3";
                    case 60 -> "Bright Motion Blur/Shake";
                    case 61 -> "Dark Screen Slightly (Fade) 1";
                    case 63 -> "Dark Screen Slightly (Fade) 2";
                    case 64 -> "Bright Pink Flash";
                    case 65 -> "Light Blue Filter, Fade To Normal";
                    case 66 -> "Shadowy Flash, Desaturated";
                    case 70 -> "Darken Filter For Skill Activation";
                    case 71 -> "Quick Blur/Shake";
                    case 72, 74 -> "Render A Black Void";
                    case 73 -> "Darken Screen 3";
                    case 75 -> "Flashing Dark Purple Hue";
                    case 76 -> "Gradual White Screen Blur 1";
                    case 77 -> "Gradual White Screen Blur 2";
                    case 78 -> "Invert World Colors";
                    case 79 -> "Consecutive Transparent Flashes/Shakes";
                    case 80 -> "Flashing Green Body Outline 1";
                    case 81 -> "Red Body Outline";
                    case 82 -> "Flashing Purple Body Outline";
                    case 83 -> "Flashing White Body Outline 1";
                    case 84 -> "Flashing White Body Outline 2";
                    case 85 -> "Flashing White Body Outline 3";
                    case 86 -> "Flashing Green Body Outline 2";
                    case 88 -> "Flashing Green Body Outline 3";
                    case 90 -> "Gradually Darken Background";
                    case 91 -> "Slightly Fade Out Background";
                    case 92 -> "Background Flashes Purple";
                    case 93 -> "Background Flashes Purple, Quick";
                    case 94 -> "Several Intense Blurs/Shakes";
                    case 95 -> "Gradually Fade To White";
                    case 96 -> "Background Flashes Light-Purple";
                    case 97 -> "Background Flashes Light-Purple, Slow";
                    case 98 -> "Quick White Flash";
                    case 100 -> "Distortion Bulge";
                    case 101 -> "Intense Zoom Blur";
                    case 110 -> "Light Blue Haze";
                    case 111 -> "Light Blue Haze, Intense";
                    case 112 -> "Light Purple Haze";
                    case 113 -> "Light Purple Haze, Blur";
                    case 114 -> "Light Blue Haze, Blur, Intense";
                    case 115 -> "Warm Light-Blue Hue";
                    case 116 -> "Light Green Hue";
                    case 117 -> "Flashing Green Hue";
                    case 118 -> "Saturated Light Purple Hue";
                    case 119 -> "Saturated Green Hue";
                    case 120 -> "Darken Backround 1";
                    case 121 -> "Darken Backround 2";
                    case 122 -> "Darken Backround 3";
                    case 123 -> "Black Background";
                    case 134 -> "Darken Background 4";
                    case 135, 157 -> "Light Blue Tint";
                    case 136 -> "Darken Background 5";
                    case 137 -> "Deep Blue Tint";
                    case 128 -> "Bleached Light Blue Tint";
                    case 129 -> "Blue Background";
                    case 130 -> "Fade Background";
                    case 131 -> "Fade To Pink Tint";
                    case 132 -> "Darken Background 6";
                    case 133 -> "Background Flashes Light Green";
                    case 140 -> "Thin Green Body Outline 1";
                    case 141 -> "Thin White Body Outline";
                    case 142 -> "Thin Green Body Outline 2";
                    case 150 -> "Very Dark Background";
                    case 151 -> "Warm Orange Hue 1";
                    case 152 -> "Warm Blueish Hue";
                    case 153 -> "Intense Reddish Hue";
                    case 154 -> "Slightly Darker Background";
                    case 155 -> "Gradually Darken Screen";
                    case 156 -> "Warm Orange Hue 2";
                    case 158 -> "Blue Tint";
                    case 159 -> "Blue Tint, Weaker";
                    case 160 -> "Blur";
                    case 165 -> "Various Distortion Effects (Large)";
                    case 170 -> "Motion Blur/Shakes";
                    case 175 -> "Gradually Fade Background To Dark Brown 1";
                    case 176 -> "Gradually Fade Background To Dark Brown 2";
                    case 177 -> "Quick Light Blue Tint Flash";
                    case 178 -> "Large Blue Tint Flash";
                    case 200 -> "Distortion Bulge Effects";
                    case 211 -> "Consecutive Shadowy Flashes, Intense";
                    case 212 -> "Dark Flash, Blur";
                    case 220 -> "Single Green Hue Flash 1";
                    case 221 -> "Single Dark Green Hue Flash";
                    case 222 -> "Single Green Hue Flash 2";
                    case 225 -> "Vertical Blur";
                    case 226 -> "Blue Tint Flashes, Shakes";
                    case 227 -> "Red Tint Flashes, Shakes";
                    case 230 -> "Gradually Darken Background Slightly";
                    case 231 -> "Single Pink/Purple Hue Flash";
                    case 232 -> "Lighten Background";
                    case 233 -> "Blurry Shakes";
                    case 234 -> "Light Blue Hue On Bakcground";
                    case 235 -> "Purple Hue On Bakcground";
                    case 236 -> "Intense White Flashes, Shakes";
                    case 237 -> "Intense White Flash -> Purple Flashes/Shakes";
                    case 240 -> "Light Screen";
                    case 241 -> "Flash Black Background";
                    case 245 -> "Red Body Outline";
                    case 246 -> "Red Tint, Flashing, Quaking";
                    case 250 -> "Red Body Outline, Red Background";
                    case 251 -> "Blue Body Outline, Blue Background"; 
                    case 252 -> "Green Body Outline, Green Background"; 
                    case 253 -> "Pink Body Outline, Pink Background"; 
                    case 257 -> "White Body Outline 1";
                    case 260 -> "White Body Outline 2";
                    default -> "Unknown";
                };
            }, spinner.valueProperty())
        );

        VBox screenEffectVBox = new VBox(35, 
            createHBox(0, createLabel("Start Time", 120), createSpinner(0, 65535, entry.startTime, BsaType8Values.StartTime)), 
            createHBox(0, createLabel("Duration", 120), createSpinner(0, 65535, entry.duration, BsaType8Values.Duration)),
            createHBox(0, createLabel("BPE Effect ID", 120), createHBox(15, new Node[] {spinner, label}, false)),
            createHBox(0, createLabel("Screen Effect Flags", 120), createHBox(5, screenEffectsFlags, false))
        );
        screenEffectVBox.setPadding(new Insets(20, 0, 0, 16));

        VBox unknownVBox = new VBox(30,
            createHBox(0, createLabel("I_04", 60), createTextField(entry.i04, BsaType8Values.I04)), 
            createHBox(0, createLabel("I_08", 60), createTextField(entry.i08, BsaType8Values.I08)), 
            createHBox(0, createLabel("I_12", 60), createTextField(entry.i12, BsaType8Values.I12)), 
            createHBox(0, createLabel("I_16", 60), createTextField(entry.i16, BsaType8Values.I16)),  
            createHBox(0, createLabel("I_20", 60), createTextField(entry.i20, BsaType8Values.I20))
        );
        unknownVBox.setPadding(new Insets(20, 0, 0, 16));

        Tab screenEffectTab = new Tab("Screen Effect", screenEffectVBox);
        screenEffectTab.setClosable(false);

        Tab unknownTab = new Tab("Unknown", unknownVBox);
        unknownTab.setClosable(false);

        tabPane.getTabs().addAll(screenEffectTab, unknownTab);
    }

    private void createBsaType10(BsaType10Entry entry) {
        VBox type10VBox = new VBox(30, 
            createHBox(0, createLabel("Start Time", 60), createSpinner(0, 65535, entry.startTime, BsaType10Values.StartTime)), 
            createHBox(0, createLabel("Duration", 60), createSpinner(0, 65535, entry.duration, BsaType10Values.Duration)),
            createHBox(0, createLabel("Skill ID", 60), createSpinner(Integer.MIN_VALUE, Integer.MAX_VALUE, entry.skillId, BsaType10Values.Skill_ID)),
            createHBox(0, createLabel("I_04", 60), createTextField(entry.i04, BsaType10Values.I04)), 
            createHBox(0, createLabel("I_06", 60), createTextField(entry.i06, BsaType10Values.I06))
        );
        type10VBox.setPadding(new Insets(20, 0, 0, 16));

        Tab type10Tab = new Tab("Type 10", type10VBox);
        type10Tab.setClosable(false);

        tabPane.getTabs().add(type10Tab);
    }

    private void createBsaType12(BsaType12Entry entry) {
        ToggleGroup skillTypesToggleGroup = new ToggleGroup();
        ToggleGroup deliveryModeToggleGroup = new ToggleGroup();

        RadioButton[] sikillTypes = new RadioButton[] {
            createRadioButton("Common", skillTypesToggleGroup, BsaType12Values.SkillTypes.Common),
            createRadioButton("StageBG", skillTypesToggleGroup, BsaType12Values.SkillTypes.StageBG),
            createRadioButton("Character", skillTypesToggleGroup, BsaType12Values.SkillTypes.CharacterEffect),
            createRadioButton("Awoken Skill", skillTypesToggleGroup, BsaType12Values.SkillTypes.AwokenSkill),
            createRadioButton("Super Skill", skillTypesToggleGroup, BsaType12Values.SkillTypes.SuperSkill),
            createRadioButton("Ultimate Skill", skillTypesToggleGroup, BsaType12Values.SkillTypes.UltimateSkill),
            createRadioButton("Evasive Skill", skillTypesToggleGroup, BsaType12Values.SkillTypes.EvasiveSkill),
            createRadioButton("Ki Blast Skill", skillTypesToggleGroup, BsaType12Values.SkillTypes.KiBlastSkill),
            createRadioButton("Stage", skillTypesToggleGroup, BsaType12Values.SkillTypes.Stage),
        };

        RadioButton[] deliveryMode = new RadioButton[] {
            createRadioButton("Broadcast", deliveryModeToggleGroup, DeliveryMode.Broadcast), 
            createRadioButton("Same-Context Highest Priority", deliveryModeToggleGroup, DeliveryMode.Same_ContextHighestPriority)
        };

        VBox sendProjectileSignalVBox = new VBox(40, 
            createHBox(0, createLabel("Start Time", 180), createSpinner(0, 65535, entry.startTime, BsaType12Values.StartTime)), 
            createHBox(0, createLabel("Duration", 180), createSpinner(0, 65535, entry.duration, BsaType12Values.Duration)), 
            createHBox(0, createLabel("Signal Value", 180), createSpinner(-Float.MAX_VALUE, Float.MAX_VALUE, entry.signalValue, BsaType12Values.SignalValue)), 
            createHBox(0, createLabel("Skill Type", 180), createGridPane(3, 3, sikillTypes, true)), 
            createHBox(0, createLabel("Skill ID", 180), createSpinner(Integer.MIN_VALUE, Integer.MAX_VALUE, entry.skillId, BsaType12Values.Skill_ID)), 
            createHBox(0, createLabel("Delivery Mode", 180), createHBox(15, deliveryMode, true)),
            createHBox(0, createLabel("Pause Recipient Timeline\n(One Update)", 180), createSpinner(-Float.MAX_VALUE, Float.MAX_VALUE, entry.pauseRecipientTimeline, BsaType12Values.PauseRecipientTimeline))
        );
        sendProjectileSignalVBox.setPadding(new Insets(20, 0, 0, 16));

        Tab endProjectileSignalTab = new Tab("Projectile Signal", sendProjectileSignalVBox);
        endProjectileSignalTab.setClosable(false);

        tabPane.getTabs().add(endProjectileSignalTab);
    }

    private void createBsaType13(BsaType13Entry entry) {
        ToggleGroup protectionFlagsToggleGroup = new ToggleGroup();
        ToggleGroup protectAdditionalSelectorsFlagsToggleGroup = new ToggleGroup();

        RadioButton[] protectionFlags = new RadioButton[] {
            createRadioButton("On", protectionFlagsToggleGroup, ProtectionFlags.On), 
            createRadioButton("Off", protectionFlagsToggleGroup, ProtectionFlags.Off)
        };

        RadioButton[] protectAdditionalSelectorsFlags = new RadioButton[] {
            createRadioButton("None", protectAdditionalSelectorsFlagsToggleGroup, ProtectAdditionalSelectorsFlags.None),
            createRadioButton("Selectors 4 And 5", protectAdditionalSelectorsFlagsToggleGroup, ProtectAdditionalSelectorsFlags.Selectors_4_And_5),
            createRadioButton("Selector 6", protectAdditionalSelectorsFlagsToggleGroup, ProtectAdditionalSelectorsFlags.Selector6),
            createRadioButton("Selectors 4, 5 And 6", protectAdditionalSelectorsFlagsToggleGroup, ProtectAdditionalSelectorsFlags.Selectors_4_5_And_6),
        };

        VBox projectileProtectionVBox = new VBox(35,
            createHBox(0, createLabel("Start Time", 180), createSpinner(0, 65535, entry.startTime, BsaType13Values.StartTime)), 
            createHBox(0, createLabel("Duration", 180), createSpinner(0, 65535, entry.duration, BsaType13Values.Duration)),
            createHBox(0, createLabel("Protection", 180), createHBox(15, protectionFlags, true)),
            createHBox(0, createLabel("Max Hitbox Power", 180), createSpinner(-Float.MAX_VALUE, Float.MAX_VALUE, entry.maxHitboxPower, BsaType13Values.MaxHitboxPower)),
            createHBox(0, createLabel("Protect Selectors 0-3", 180), createCheckBox(entry.protectSelectors_0_3, BsaType13Values.ProtectSelectors_0_3)),
            createHBox(0, createLabel("Protect Additional Selectors", 180), createHBox(15, protectAdditionalSelectorsFlags, true)),
            createHBox(0, createLabel("Entry Passing Signal", 180), createSpinner(-Float.MAX_VALUE, Float.MAX_VALUE, entry.entryPassingSignal, BsaType13Values.EntryPassingSignal)),
            createHBox(0, createLabel("Mark Protected Hit", 180), createCheckBox(entry.markProtectedHit, BsaType13Values.MarkProtectedHit))
        );
        projectileProtectionVBox.setPadding(new Insets(20, 0, 0, 16));
        
        VBox unknownVBox = new VBox(30,
            createHBox(0, createLabel("I_02", 60), createTextField(entry.i02, BsaType13Values.I02)), 
            createHBox(0, createLabel("I_24", 60), createTextField(entry.i24, BsaType13Values.I24)),
            createHBox(0, createLabel("I_28", 60), createTextField(entry.i28, BsaType13Values.I28))
        );
        unknownVBox.setPadding(new Insets(20, 0, 0, 16));

        Tab projectileProtectionTab = new Tab("Projectile Projection", projectileProtectionVBox);
        projectileProtectionTab.setClosable(false);

        Tab unknownTab = new Tab("Unknown", unknownVBox);
        unknownTab.setClosable(false);

        tabPane.getTabs().addAll(projectileProtectionTab, unknownTab);
    }

    private void createBsaType14(BsaType14Entry entry) {
        ToggleGroup placementModesToggleGroup = new ToggleGroup();
        ToggleGroup eepkTypeToggleGroup = new ToggleGroup();
        ToggleGroup commonEepkTypeToggleGroup = new ToggleGroup();

        RadioButton[] placementModes = new RadioButton[] {
            createRadioButton("Default Placement", placementModesToggleGroup, PlacementModes.DefaultPlacement), 
            createRadioButton("Distance-Based Placement", placementModesToggleGroup, PlacementModes.Distance_BasedPlacement),
            createRadioButton("Explicit Vector Placement", placementModesToggleGroup, PlacementModes.ExplicitVectorPlacement)
        };

        RadioButton[] eepkTypes = new RadioButton[] {
            createRadioButton("Common", eepkTypeToggleGroup, BsaType14Values.EEPK_Types.Common),
            createRadioButton("StageBG", eepkTypeToggleGroup, BsaType14Values.EEPK_Types.StageBG),
            createRadioButton("Character", eepkTypeToggleGroup, BsaType14Values.EEPK_Types.CharacterEffect),
            createRadioButton("Awoken Skill", eepkTypeToggleGroup, BsaType14Values.EEPK_Types.AwokenSkill),
            createRadioButton("Super Skill", eepkTypeToggleGroup, BsaType14Values.EEPK_Types.SuperSkill),
            createRadioButton("Ultimate Skill", eepkTypeToggleGroup, BsaType14Values.EEPK_Types.UltimateSkill),
            createRadioButton("Evasive Skill", eepkTypeToggleGroup, BsaType14Values.EEPK_Types.EvasiveSkill),
            createRadioButton("Ki Blast Skill", eepkTypeToggleGroup, BsaType14Values.EEPK_Types.KiBlastSkill),
            createRadioButton("Stage", eepkTypeToggleGroup, BsaType14Values.EEPK_Types.Stage),
        };

        RadioButton[] commonEepkTypes = new RadioButton[] {
            createRadioButton("BTL_CMN", commonEepkTypeToggleGroup, CMN_EEPK_Types.BTL_CMN),
            createRadioButton("BTL_AURA", commonEepkTypeToggleGroup, CMN_EEPK_Types.BTL_AURA),
            createRadioButton("BTL_KDN", commonEepkTypeToggleGroup, CMN_EEPK_Types.BTL_KDN),
            createRadioButton("lby_cmn/LBY_CMN", commonEepkTypeToggleGroup, CMN_EEPK_Types.lby_cmn_LBY_CMN),
            createRadioButton("TTL/TTL", commonEepkTypeToggleGroup, CMN_EEPK_Types.TTL_TTL),
            createRadioButton("ttl_lby/TTL_LBY", commonEepkTypeToggleGroup, CMN_EEPK_Types.ttl_lby_TTL_LBY),
            createRadioButton("BTL_CMN 2", commonEepkTypeToggleGroup, CMN_EEPK_Types.BTL_CMN2),
        };
    
        VBox effectPlacementVBox = new VBox(30,
            createHBox(0, createLabel("Start Time", 180), createSpinner(0, 65535, entry.startTime, BsaType14Values.StartTime)), 
            createHBox(0, createLabel("Duration", 180), createSpinner(0, 65535, entry.duration, BsaType14Values.Duration)),
            createHBox(0, createLabel("Placement Mode", 180), createHBox(15, placementModes, true)), 
            createHBox(0, createLabel("Placement Flags", 180), createSpinner(0, 4294967295L, entry.placementFlags, BsaType14Values.PlacementFlags)),
            createHBox(0, createLabel("Transform /Bone Selector", 180), createSpinner(0, 65535, entry.transform_BoneSelector, BsaType14Values.Transform_BoneSelector)),
            createHBox(0, createLabel("EEPK Type", 180), createGridPane(3, 3, eepkTypes, true)), 
            createHBox(0, createLabel("EEPK Type", 180), createHBox(15, commonEepkTypes, true)), 
            createHBox(0, createLabel("Effect Placement Flags", 180), createSpinner(0, 4294967295L, entry.effectPlacementFlags, BsaType14Values.EffectPlacementFlags))
        );
        effectPlacementVBox.setPadding(new Insets(20, 0, 20, 16));

        VBox unknownVBox = new VBox(30,
            createHBox(0, createLabel("I_02", 60), createTextField(entry.i02, BsaType14Values.I02)),
            createHBox(0, createLabel("I_08", 60), createTextField(entry.i08, BsaType14Values.I08)),
            createHBox(0, createLabel("F_12", 60), createTextField(entry.f12, BsaType14Values.F12)),
            createHBox(0, createLabel("I_16", 60), createTextField(entry.i16, BsaType14Values.I16)),
            createHBox(0, createLabel("F_20", 60), createTextField(entry.f20, BsaType14Values.F20)),
            createHBox(0, createLabel("I_24", 60), createTextField(entry.i24, BsaType14Values.I24)),
            createHBox(0, createLabel("F_28", 60), createTextField(entry.f28, BsaType14Values.F28)),
            createHBox(0, createLabel("I_32", 60), createTextField(entry.i32, BsaType14Values.I32)),
            createHBox(0, createLabel("I_36", 60), createTextField(entry.i36, BsaType14Values.I36)),
            createHBox(0, createLabel("I_40", 60), createTextField(entry.i40, BsaType14Values.I40)),
            createHBox(0, createLabel("F_44", 60), createTextField(entry.f44, BsaType14Values.F44)),
            createHBox(0, createLabel("F_60", 60), createTextField(entry.f60, BsaType14Values.F60)),
            createHBox(0, createLabel("I_64", 60), createTextField(entry.i64, BsaType14Values.I64)),
            createHBox(0, createLabel("F_68", 60), createTextField(entry.f68, BsaType14Values.F68)),
            createHBox(0, createLabel("I_72", 60), createTextField(entry.i72, BsaType14Values.I72)),
            createHBox(0, createLabel("I_76", 60), createTextField(entry.i76, BsaType14Values.I76)),
            createHBox(0, createLabel("I_80", 60), createTextField(entry.i80, BsaType14Values.I80))
        );
        unknownVBox.setPadding(new Insets(20, 0, 20, 16));

        Tab effectPlacementTab = new Tab("Effect Placement", effectPlacementVBox);
        effectPlacementTab.setClosable(false);

        Tab unknownTab = new Tab("Unknown", new ScrollPane(unknownVBox));
        unknownTab.setClosable(false);
        
        tabPane.getTabs().addAll(effectPlacementTab, unknownTab);
    }

    private Label createLabel(String text, int width) {
        Label label = new Label(text);
        if (width != 0) label.setPrefWidth(width);

        return label;
    }

    private TextField createTextField(Number value, BsaMainValues bsaMainValue) {
        TextField textField = new TextField(String.valueOf(value));
        textField.textProperty().addListener((obs, oldText, newText) -> {
            if (textField.getText().contains("-")) {
                return;
            }
            try {
                switch (bsaMainValue) {
                    case I00 -> bsaMainHashMap.get(currentEntry).i00 = Integer.parseInt(newText);
                    case I16_A -> bsaMainHashMap.get(currentEntry).i16_a = Byte.parseByte(newText);
                    case I16_B -> bsaMainHashMap.get(currentEntry).i16_b = Byte.parseByte(newText);
                    case I17 -> bsaMainHashMap.get(currentEntry).i17 = Integer.parseInt(newText);
                    case I18 -> bsaMainHashMap.get(currentEntry).i18 = Integer.parseInt(newText);
                    case I24 -> bsaMainHashMap.get(currentEntry).i24 = Integer.parseInt(newText);
                    case I40 -> bsaMainHashMap.get(currentEntry).i40 = Integer.parseInt(newText);
                    case I44 -> bsaMainHashMap.get(currentEntry).i44 = Integer.parseInt(newText);
                    case I48 -> bsaMainHashMap.get(currentEntry).i48 = Integer.parseInt(newText);
                    default -> throw new IllegalArgumentException("Unexpected value: " + bsaMainValue);
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        return textField;
    }

    private TextField createTextField(Number value, BsaCollisionValues bsaCollisionValue) {
        TextField textField = new TextField(String.valueOf(value));
        textField.textProperty().addListener((obs, oldText, newText) -> {
            if (textField.getText().contains("-")) {
                return;
            }
            try {
                switch (bsaCollisionValue) {
                    case I06 -> bsaCollisionHashMap.get(currentEntry).i06 = Integer.parseInt(newText);
                    case I08 -> bsaCollisionHashMap.get(currentEntry).i08 = Integer.parseInt(newText);
                    case I12 -> bsaCollisionHashMap.get(currentEntry).i12 = Integer.parseInt(newText);
                    case I16 -> bsaCollisionHashMap.get(currentEntry).i16 = Integer.parseInt(newText);
                    case I20 -> bsaCollisionHashMap.get(currentEntry).i20 = Integer.parseInt(newText);
                    default -> throw new IllegalArgumentException("Unexpected value: " + bsaCollisionValue);
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        return textField;
    }

    private TextField createTextField(Number value, BsaCollisionSoundValues bsaCollisionSoundValue) {
        TextField textField = new TextField(String.valueOf(value));
        textField.textProperty().addListener((obs, oldText, newText) -> {
            if (textField.getText().contains("-")) {
                return;
            }
            try {
                switch (bsaCollisionSoundValue) {
                    case I02 -> bsaCollisionSoundHashMap.get(currentEntry).i02 = Integer.parseInt(newText);
                    case I06 -> bsaCollisionSoundHashMap.get(currentEntry).i06 = Integer.parseInt(newText);
                    default -> throw new IllegalArgumentException("Unexpected value: " + bsaCollisionSoundValue);
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        return textField;
    }

    private TextField createTextField(Number value, BsaType0Values bsaType0Value) {
        TextField textField = new TextField(String.valueOf(value));
        textField.textProperty().addListener((obs, oldText, newText) -> {
            if (textField.getText().contains("-")) {
                return;
            }
            try {
                switch (bsaType0Value) {
                    case I00 -> bsaType0HashMap.get(currentEntry).i00 = Short.parseShort(newText);
                    case I06 -> bsaType0HashMap.get(currentEntry).i06 = Short.parseShort(newText);
                    case F12 -> bsaType0HashMap.get(currentEntry).f12 = Float.parseFloat(newText);
                    default -> throw new IllegalArgumentException("Unexpected value: " + bsaType0Value);
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        return textField;
    }

    private TextField createTextField(Number value, BsaType1Values bsaType1Value) {
        TextField textField = new TextField(String.valueOf(value));
        textField.textProperty().addListener((obs, oldText, newText) -> {
            if (textField.getText().contains("-")) {
                return;
            }
            try {
                switch (bsaType1Value) {
                    case F16 -> bsaType1HashMap.get(currentEntry).f16 = Float.parseFloat(newText);
                    default -> throw new IllegalArgumentException("Unexpected value: " + bsaType1Value);
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        return textField;
    }

    private TextField createTextField(Number value, BsaType2Values bsaType2Value) {
        TextField textField = new TextField(String.valueOf(value));
        textField.textProperty().addListener((obs, oldText, newText) -> {
            if (textField.getText().contains("-")) {
                return;
            }
            try {
                switch (bsaType2Value) {
                    case I00 -> bsaType2HashMap.get(currentEntry).i00 = Short.parseShort(newText);
                    case I06 -> bsaType2HashMap.get(currentEntry).i06 = Short.parseShort(newText);
                    default -> throw new IllegalArgumentException("Unexpected value: " + bsaType2Value);
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        return textField;
    }

    private TextField createTextField(Number value, BsaType3Values bsaType3Value) {
        TextField textField = new TextField(String.valueOf(value));
        textField.textProperty().addListener((obs, oldText, newText) -> {
            if (textField.getText().contains("-")) {
                return;
            }
            try {
                switch (bsaType3Value) {
                    case I02 -> bsaType3HashMap.get(currentEntry).i02 = Integer.parseInt(newText);
                    case I52 -> bsaType3HashMap.get(currentEntry).i52 = Integer.parseInt(newText);
                    case I54 -> bsaType3HashMap.get(currentEntry).i54 = Integer.parseInt(newText);
                    case I56 -> bsaType3HashMap.get(currentEntry).i56 = Integer.parseInt(newText);
                    default -> throw new IllegalArgumentException("Unexpected value: " + bsaType3Value);
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        return textField;
    }

    private TextField createTextField(Number value, BsaType4Values bsaType4Value) {
        TextField textField = new TextField(String.valueOf(value));
        textField.textProperty().addListener((obs, oldText, newText) -> {
            if (textField.getText().contains("-")) {
                return;
            }
            try {
                switch (bsaType4Value) {
                    case I00 -> bsaType4HashMap.get(currentEntry).i00 = Integer.parseInt(newText);
                    case I04 -> bsaType4HashMap.get(currentEntry).i04 = Integer.parseInt(newText);
                    case I08 -> bsaType4HashMap.get(currentEntry).i08 = Integer.parseInt(newText);
                    case F12 -> bsaType4HashMap.get(currentEntry).f12 = Float.parseFloat(newText);
                    case F16 -> bsaType4HashMap.get(currentEntry).f16 = Float.parseFloat(newText);
                    case F20 -> bsaType4HashMap.get(currentEntry).f20 = Float.parseFloat(newText);
                    case I24 -> bsaType4HashMap.get(currentEntry).i24 = Integer.parseInt(newText);
                    case I28 -> bsaType4HashMap.get(currentEntry).i28 = Integer.parseInt(newText);
                    case I32 -> bsaType4HashMap.get(currentEntry).i32 = Integer.parseInt(newText);
                    case I36 -> bsaType4HashMap.get(currentEntry).i36 = Integer.parseInt(newText);
                    case I40 -> bsaType4HashMap.get(currentEntry).i40 = Integer.parseInt(newText);
                    case I44 -> bsaType4HashMap.get(currentEntry).i44 = Integer.parseInt(newText);
                    case I48 -> bsaType4HashMap.get(currentEntry).i48 = Integer.parseInt(newText);
                    case I50 -> bsaType4HashMap.get(currentEntry).i50 = Integer.parseInt(newText);
                    case I52 -> bsaType4HashMap.get(currentEntry).i52 = Integer.parseInt(newText);
                    case I54 -> bsaType4HashMap.get(currentEntry).i54 = Integer.parseInt(newText);
                    default -> throw new IllegalArgumentException("Unexpected value: " + bsaType4Value);
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        return textField;
    }

    private TextField createTextField(Number value, BsaType6Values bsaType6Value) {
        TextField textField = new TextField(String.valueOf(value));
        textField.textProperty().addListener((obs, oldText, newText) -> {
            if (textField.getText().contains("-")) {
                return;
            }
            try {
                switch (bsaType6Value) {
                    case I06 -> bsaType6HashMap.get(currentEntry).i06 = Integer.parseInt(newText);
                    case I10 -> bsaType6HashMap.get(currentEntry).i10 = Integer.parseInt(newText);
                    default -> throw new IllegalArgumentException("Unexpected value: " + bsaType6Value);
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        return textField;
    }

    private TextField createTextField(Number value, BsaType7Values bsaType7Value) {
        TextField textField = new TextField(String.valueOf(value));
        textField.textProperty().addListener((obs, oldText, newText) -> {
            if (textField.getText().contains("-")) {
                return;
            }
            try {
                switch (bsaType7Value) {
                    case I02 -> bsaType7HashMap.get(currentEntry).i02 = Integer.parseInt(newText);
                    case I06 -> bsaType7HashMap.get(currentEntry).i06 = Integer.parseInt(newText);
                    default -> throw new IllegalArgumentException("Unexpected value: " + bsaType7Value);
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        return textField;
    }

    private TextField createTextField(Number value, BsaType8Values bsaType8Value) {
        TextField textField = new TextField(String.valueOf(value));
        textField.textProperty().addListener((obs, oldText, newText) -> {
            if (textField.getText().contains("-")) {
                return;
            }
            try {
                switch (bsaType8Value) {
                    case I04 -> bsaType8HashMap.get(currentEntry).i04 = Integer.parseInt(newText);
                    case I08 -> bsaType8HashMap.get(currentEntry).i08 = Integer.parseInt(newText);
                    case I12 -> bsaType8HashMap.get(currentEntry).i12 = Integer.parseInt(newText);
                    case I16 -> bsaType8HashMap.get(currentEntry).i16 = Integer.parseInt(newText);
                    case I20 -> bsaType8HashMap.get(currentEntry).i20 = Integer.parseInt(newText);
                    default -> throw new IllegalArgumentException("Unexpected value: " + bsaType8Value);
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        return textField;
    }

    private TextField createTextField(Number value, BsaType10Values bsaType10Value) {
        TextField textField = new TextField(String.valueOf(value));
        textField.textProperty().addListener((obs, oldText, newText) -> {
            if (textField.getText().contains("-")) {
                return;
            }
            try {
                switch (bsaType10Value) {
                    case I04 -> bsaType10HashMap.get(currentEntry).i04 = Integer.parseInt(newText);
                    case I06 -> bsaType10HashMap.get(currentEntry).i06 = Integer.parseInt(newText);
                    default -> throw new IllegalArgumentException("Unexpected value: " + bsaType10Value);
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        return textField;
    }

    private TextField createTextField(Number value, BsaType13Values bsaType13Value) {
        TextField textField = new TextField(String.valueOf(value));
        textField.textProperty().addListener((obs, oldText, newText) -> {
            if (textField.getText().contains("-")) {
                return;
            }
            try {
                switch (bsaType13Value) {
                    case I02 -> bsaType13HashMap.get(currentEntry).i02 = Integer.parseInt(newText);
                    case I24 -> bsaType13HashMap.get(currentEntry).i24 = Integer.parseInt(newText);
                    case I28 -> bsaType13HashMap.get(currentEntry).i28 = Integer.parseInt(newText);
                    default -> throw new IllegalArgumentException("Unexpected value: " + bsaType13Value);
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        return textField;
    }

    private TextField createTextField(Number value, BsaType14Values bsaType14Value) {
        TextField textField = new TextField(String.valueOf(value));
        textField.textProperty().addListener((obs, oldText, newText) -> {
            if (textField.getText().contains("-")) {
                return;
            }
            try {
                switch (bsaType14Value) {
                    case I02 -> bsaType14HashMap.get(currentEntry).i02 = Integer.parseInt(newText);
                    case I08 -> bsaType14HashMap.get(currentEntry).i08 = Long.parseLong(newText);
                    case F12 -> bsaType14HashMap.get(currentEntry).f12 = Float.parseFloat(newText);
                    case I16 -> bsaType14HashMap.get(currentEntry).i16 = Long.parseLong(newText);
                    case F20 -> bsaType14HashMap.get(currentEntry).f20 = Float.parseFloat(newText);
                    case I24 -> bsaType14HashMap.get(currentEntry).i24 = Long.parseLong(newText);
                    case F28 -> bsaType14HashMap.get(currentEntry).f28 = Float.parseFloat(newText);
                    case I32 -> bsaType14HashMap.get(currentEntry).i32 = Long.parseLong(newText);
                    case I36 -> bsaType14HashMap.get(currentEntry).i36 = Long.parseLong(newText);
                    case I40 -> bsaType14HashMap.get(currentEntry).i40 = Long.parseLong(newText);
                    case F44 -> bsaType14HashMap.get(currentEntry).f44 = Float.parseFloat(newText);
                    case F60 -> bsaType14HashMap.get(currentEntry).f60 = Float.parseFloat(newText);
                    case I64 -> bsaType14HashMap.get(currentEntry).i64 = Long.parseLong(newText);
                    case F68 -> bsaType14HashMap.get(currentEntry).f68 = Float.parseFloat(newText);
                    case I72 -> bsaType14HashMap.get(currentEntry).i72 = Long.parseLong(newText);
                    case I76 -> bsaType14HashMap.get(currentEntry).i76 = Long.parseLong(newText);
                    case I80 -> bsaType14HashMap.get(currentEntry).i80 = Long.parseLong(newText);
                    default -> throw new IllegalArgumentException("Unexpected value: " + bsaType14Value);
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        return textField;
    }

    private CheckBox createCheckBox(boolean value, BsaType13Values bsaType13Value) {
        CheckBox checkBox = new CheckBox("Enabled");
        checkBox.setSelected(value);
        checkBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
            switch (bsaType13Value) {
                case ProtectSelectors_0_3 -> bsaType13HashMap.get(currentEntry).protectSelectors_0_3 = newValue;
                case MarkProtectedHit -> bsaType13HashMap.get(currentEntry).markProtectedHit = newValue;
                default -> throw new IllegalArgumentException("Unexpected value: " + bsaType13Value);
            } 
        });

        return checkBox;
    }

    private RadioButton createRadioButton(String text, ToggleGroup toggleGroup, BsaCollisionValues.EEPK_Types effect_EEPK_Type) {
        RadioButton radioButton = new RadioButton(text);
        radioButton.setToggleGroup(toggleGroup);

        if (bsaCollisionHashMap.get(currentEntry).eepkType == effect_EEPK_Type.index) radioButton.setSelected(true);

        radioButton.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                bsaCollisionHashMap.get(currentEntry).eepkType = effect_EEPK_Type.index;
            }
        });

        return radioButton;
    }

    private RadioButton createRadioButton(String text, ToggleGroup toggleGroup, BsaCollisionSoundValues.ACB_Types ACBType) {
        RadioButton radioButton = new RadioButton(text);
        radioButton.setToggleGroup(toggleGroup);

        if (bsaCollisionSoundHashMap.get(currentEntry).acbType == ACBType.index) radioButton.setSelected(true);

        radioButton.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                bsaCollisionSoundHashMap.get(currentEntry).acbType = ACBType.index;
            }
        });

        return radioButton;
    }

    private RadioButton createRadioButton(String text, ToggleGroup toggleGroup, GrowMaxBoundsFlags growMaxBoundsFlag) {
        RadioButton radioButton = new RadioButton(text);
        radioButton.setToggleGroup(toggleGroup);

        if (bsaType3HashMap.get(currentEntry).growMaxBounds == growMaxBoundsFlag.index) radioButton.setSelected(true);

        radioButton.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                bsaType3HashMap.get(currentEntry).growMaxBounds = growMaxBoundsFlag.index;
            }
        });

        return radioButton;
    }

    private RadioButton createRadioButton(String text, ToggleGroup toggleGroup, BoundsTypes boundsType) {
        RadioButton radioButton = new RadioButton(text);
        radioButton.setToggleGroup(toggleGroup);

        if (bsaType3HashMap.get(currentEntry).boundsType == boundsType.index) radioButton.setSelected(true);

        radioButton.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                bsaType3HashMap.get(currentEntry).boundsType = boundsType.index;
            }
        });

        return radioButton;
    }

    private RadioButton createRadioButton(String text, ToggleGroup toggleGroup, EffectSwitchFlags effectSwitchFlag) {
        RadioButton radioButton = new RadioButton(text);
        radioButton.setToggleGroup(toggleGroup);

        if (bsaType6HashMap.get(currentEntry).effectSwitch == effectSwitchFlag.index) radioButton.setSelected(true);

        radioButton.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                bsaType6HashMap.get(currentEntry).effectSwitch = effectSwitchFlag.index;
            }
        });

        return radioButton;
    }

    private RadioButton createRadioButton(String text, ToggleGroup toggleGroup, BsaType6Values.EEPK_Types effect_EEPK_Type) {
        RadioButton radioButton = new RadioButton(text);
        radioButton.setToggleGroup(toggleGroup);

        if (bsaType6HashMap.get(currentEntry).eepkType == effect_EEPK_Type.index) radioButton.setSelected(true);

        radioButton.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                bsaType6HashMap.get(currentEntry).eepkType = effect_EEPK_Type.index;
            }
        });

        return radioButton;
    }

    private RadioButton createRadioButton(String text, ToggleGroup toggleGroup, BsaType7Values.ACB_Types ACBType) {
        RadioButton radioButton = new RadioButton(text);
        radioButton.setToggleGroup(toggleGroup);

        if (bsaType7HashMap.get(currentEntry).acbType == ACBType.index) radioButton.setSelected(true);

        radioButton.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                bsaType7HashMap.get(currentEntry).acbType = ACBType.index;
            }
        });

        return radioButton;
    }

    private RadioButton createRadioButton(String text, ToggleGroup toggleGroup, BsaType12Values.SkillTypes skill_Type) {
        RadioButton radioButton = new RadioButton(text);
        radioButton.setToggleGroup(toggleGroup);

        if (bsaType12HashMap.get(currentEntry).skillType == skill_Type.index) radioButton.setSelected(true);

        radioButton.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                bsaType12HashMap.get(currentEntry).skillType = skill_Type.index;
            }
        });

        return radioButton;
    }

    private RadioButton createRadioButton(String text, ToggleGroup toggleGroup, DeliveryMode deliveryMode) {
        RadioButton radioButton = new RadioButton(text);
        radioButton.setToggleGroup(toggleGroup);

        if (bsaType12HashMap.get(currentEntry).deliveryMode == deliveryMode.index) radioButton.setSelected(true);

        radioButton.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                bsaType12HashMap.get(currentEntry).deliveryMode = deliveryMode.index;
            }
        });

        return radioButton;
    }

    private RadioButton createRadioButton(String text, ToggleGroup toggleGroup, ProtectionFlags protectionFlag) {
        RadioButton radioButton = new RadioButton(text);
        radioButton.setToggleGroup(toggleGroup);

        if (bsaType13HashMap.get(currentEntry).protection == protectionFlag.index) radioButton.setSelected(true);

        radioButton.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                bsaType13HashMap.get(currentEntry).protection = protectionFlag.index;
            }
        });

        return radioButton;
    }

    private RadioButton createRadioButton(String text, ToggleGroup toggleGroup, ProtectAdditionalSelectorsFlags protectAdditionalSelectorsFlag) {
        RadioButton radioButton = new RadioButton(text);
        radioButton.setToggleGroup(toggleGroup);

        if (bsaType13HashMap.get(currentEntry).protectAdditionalSelectors == protectAdditionalSelectorsFlag.index) radioButton.setSelected(true);

        radioButton.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                bsaType13HashMap.get(currentEntry).protectAdditionalSelectors = protectAdditionalSelectorsFlag.index;
            }
        });

        return radioButton;
    }

    private RadioButton createRadioButton(String text, ToggleGroup toggleGroup, PlacementModes placementMode) {
        RadioButton radioButton = new RadioButton(text);
        radioButton.setToggleGroup(toggleGroup);

        if (bsaType14HashMap.get(currentEntry).placementMode == placementMode.index) radioButton.setSelected(true);

        radioButton.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                bsaType14HashMap.get(currentEntry).placementMode = placementMode.index;
            }
        });

        return radioButton;
    }

    private RadioButton createRadioButton(String text, ToggleGroup toggleGroup, BsaType14Values.EEPK_Types effect_EEPK_Type) {
        RadioButton radioButton = new RadioButton(text);
        radioButton.setToggleGroup(toggleGroup);

        if (bsaType14HashMap.get(currentEntry).eepkType == effect_EEPK_Type.index) radioButton.setSelected(true);

        radioButton.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                bsaType14HashMap.get(currentEntry).eepkType = effect_EEPK_Type.index;
            }
        });

        return radioButton;
    }

    private RadioButton createRadioButton(String text, ToggleGroup toggleGroup, CMN_EEPK_Types cmn_Eepk_Type) {
        RadioButton radioButton = new RadioButton(text);
        radioButton.setToggleGroup(toggleGroup);

        if (bsaType14HashMap.get(currentEntry).commonEepk == cmn_Eepk_Type.index) radioButton.setSelected(true);

        radioButton.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                bsaType14HashMap.get(currentEntry).commonEepk = cmn_Eepk_Type.index;
            }
        });

        return radioButton;
    }

    private Spinner<Number> createSpinner(int width, Number MIN_VALUE, Number MAX_VALUE, Number value, BsaMainValues bsaMainValue) {
        Spinner<Number> spinner = new Spinner<>(MIN_VALUE.intValue(), MAX_VALUE.intValue(), value.intValue());

        if (width != 0) spinner.setPrefWidth(width);
        
        spinner.setEditable(true);
        spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                switch (bsaMainValue) {
                    case I16_A -> bsaMainHashMap.get(currentEntry).i16_a = newValue.byteValue(); 
                    case I16_B -> bsaMainHashMap.get(currentEntry).i16_b = newValue.byteValue(); 
                    case Lifetime -> bsaMainHashMap.get(currentEntry).lifetime = newValue.intValue();
                    case Expires -> bsaMainHashMap.get(currentEntry).expires = newValue.intValue();
                    case ImpactProjectile -> bsaMainHashMap.get(currentEntry).impactProjectile = newValue.intValue();
                    case ImpactEnemy -> bsaMainHashMap.get(currentEntry).impactEnemy = newValue.intValue();
                    case ImpactGround -> bsaMainHashMap.get(currentEntry).impactGround = newValue.intValue();
                    default -> throw new IllegalArgumentException("Unexpected value: " + bsaMainValue);
                }   
            }
        });

        return spinner;
    }

    private Spinner<Number> createSpinner(Number MIN_VALUE, Number MAX_VALUE, Number value, BsaCollisionValues bsaCollisionValue) {
        Spinner<Number> spinner = new Spinner<>(MIN_VALUE.intValue(), MAX_VALUE.intValue(), value.intValue());

        spinner.setEditable(true);
        spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                switch (bsaCollisionValue) {
                    case Skill_ID -> bsaCollisionHashMap.get(currentEntry).skillId = newValue.intValue(); 
                    case Effect_ID -> bsaCollisionHashMap.get(currentEntry).effectId = newValue.intValue(); 
                    default -> throw new IllegalArgumentException("Unexpected value: " + bsaCollisionValue);
                }   
            }
        });

        return spinner;
    }

    private Spinner<Number> createSpinner(Number MIN_VALUE, Number MAX_VALUE, Number value, BsaCollisionSoundValues bsaCollisionSoundValue) {
        Spinner<Number> spinner = new Spinner<>(MIN_VALUE.intValue(), MAX_VALUE.intValue(), value.intValue());
        System.out.println(value);

        spinner.setEditable(true);
        spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                switch (bsaCollisionSoundValue) {
                    case CUE_ID -> bsaCollisionSoundHashMap.get(currentEntry).cueId = newValue.intValue(); 
                    default -> throw new IllegalArgumentException("Unexpected value: " + bsaCollisionSoundValue);
                } 
                System.out.println(bsaCollisionSoundHashMap.get(currentEntry).cueId);  
            }
        });

        return spinner;
    }

    private Spinner<Number> createSpinner(Number MIN_VALUE, Number MAX_VALUE, Number value, BsaType0Values bsaType0Value) {
        Spinner<Number> spinner;

        if (value instanceof Float) {
            spinner = new Spinner<>(MIN_VALUE.doubleValue(), MAX_VALUE.doubleValue(), value.doubleValue());
        }
        else {
            spinner = new Spinner<>(MIN_VALUE.intValue(), MAX_VALUE.intValue(), value.intValue());
        }

        spinner.setEditable(true);
        spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                switch (bsaType0Value) {
                    case StartTime -> bsaType0HashMap.get(currentEntry).startTime = newValue.intValue(); 
                    case Duration -> bsaType0HashMap.get(currentEntry).duration = newValue.intValue();
                    case BsaEntryID -> bsaType0HashMap.get(currentEntry).bsaEntryId = newValue.intValue();
                    case MainConditon -> bsaType0HashMap.get(currentEntry).mainCondition = newValue.intValue();
                    case BAC_Conditon-> bsaType0HashMap.get(currentEntry).bacCondition = newValue.floatValue();
                    default -> throw new IllegalArgumentException("Unexpected value: " + bsaType0Value);
                }   
            }
        });

        return spinner;
    }

    private Spinner<Number> createSpinner(Number MIN_VALUE, Number MAX_VALUE, Number value, BsaType1Values bsaType1Value) {
        Spinner<Number> spinner;

        if (value instanceof Float) {
            spinner = new Spinner<>(MIN_VALUE.doubleValue(), MAX_VALUE.doubleValue(), value.doubleValue());
        }
        else {
            spinner = new Spinner<>(MIN_VALUE.intValue(), MAX_VALUE.intValue(), value.intValue());
        }

        spinner.setEditable(true);
        spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                switch (bsaType1Value) {
                    case StartTime -> bsaType1HashMap.get(currentEntry).startTime = newValue.intValue(); 
                    case Duration -> bsaType1HashMap.get(currentEntry).duration = newValue.intValue();
                    case SpeedX -> bsaType1HashMap.get(currentEntry).speedX = newValue.floatValue();
                    case SpeedY -> bsaType1HashMap.get(currentEntry).speedY = newValue.floatValue();
                    case SpeedZ -> bsaType1HashMap.get(currentEntry).speedZ = newValue.floatValue();
                    case AccelerationX -> bsaType1HashMap.get(currentEntry).accelerationX = newValue.floatValue();
                    case AccelerationY -> bsaType1HashMap.get(currentEntry).accelerationY = newValue.floatValue();
                    case AccelerationZ -> bsaType1HashMap.get(currentEntry).accelerationZ = newValue.floatValue();
                    case SpreadDirectionX -> bsaType1HashMap.get(currentEntry).spreadDirectionX = newValue.floatValue();
                    case SpreadDirectionY -> bsaType1HashMap.get(currentEntry).spreadDirectionY = newValue.floatValue();
                    case SpreadDirectionZ -> bsaType1HashMap.get(currentEntry).spreadDirectionZ = newValue.floatValue();
                    case FalloffStrength -> bsaType1HashMap.get(currentEntry).fallofStrength = newValue.floatValue();
                    default -> throw new IllegalArgumentException("Unexpected value: " + bsaType1Value);
                }   
            }
        });

        return spinner;
    }

    private Spinner<Number> createSpinner(Number MIN_VALUE, Number MAX_VALUE, Number value, BsaType2Values bsaType2Value) {
        Spinner<Number> spinner = new Spinner<>(MIN_VALUE.intValue(), MAX_VALUE.intValue(), value.intValue());

        spinner.setEditable(true);
        spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                switch (bsaType2Value) {
                    case StartTime -> bsaType2HashMap.get(currentEntry).startTime = newValue.intValue(); 
                    case Duration -> bsaType2HashMap.get(currentEntry).duration = newValue.intValue(); 
                    case OutputStartFrame -> bsaType2HashMap.get(currentEntry).outputStartFrame = newValue.shortValue(); 
                    case OutputEndFrame -> bsaType2HashMap.get(currentEntry).outputEndFrame = newValue.shortValue(); 
                    default -> throw new IllegalArgumentException("Unexpected value: " + bsaType2Value);
                }   
            }
        });

        return spinner;
    }

    private Spinner<Number> createSpinner(int width, Number MIN_VALUE, Number MAX_VALUE, Number value, BsaType3Values bsaType3Value) {
        Spinner<Number> spinner;

        if (value instanceof Float) {
            spinner = new Spinner<>(MIN_VALUE.doubleValue(), MAX_VALUE.doubleValue(), value.doubleValue());
        }
        else {
            spinner = new Spinner<>(MIN_VALUE.intValue(), MAX_VALUE.intValue(), value.intValue());
        }

        if (width != 0) spinner.setPrefWidth(width);

        spinner.setEditable(true);
        spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                switch (bsaType3Value) {
                    case StartTime -> bsaType3HashMap.get(currentEntry).startTime = newValue.intValue(); 
                    case Duration -> bsaType3HashMap.get(currentEntry).duration = newValue.intValue();
                    case I06_A -> bsaType3HashMap.get(currentEntry).i06_a = newValue.byteValue();
                    case I06_B -> bsaType3HashMap.get(currentEntry).i06_b = newValue.byteValue();
                    case I06_C -> bsaType3HashMap.get(currentEntry).i06_c = newValue.byteValue();
                    case I06_D -> bsaType3HashMap.get(currentEntry).i06_d = newValue.byteValue();
                    case PositionX -> bsaType3HashMap.get(currentEntry).positionX = newValue.floatValue();
                    case PositionY -> bsaType3HashMap.get(currentEntry).positionY = newValue.floatValue();
                    case PositionZ -> bsaType3HashMap.get(currentEntry).positionZ = newValue.floatValue();
                    case HitboxScale -> bsaType3HashMap.get(currentEntry).hitboxScale = newValue.floatValue();
                    case MaximumX -> bsaType3HashMap.get(currentEntry).maximumX = newValue.floatValue();
                    case MaximumY -> bsaType3HashMap.get(currentEntry).maximumY = newValue.floatValue();
                    case MaximumZ -> bsaType3HashMap.get(currentEntry).maximumZ = newValue.floatValue();
                    case MinimumX -> bsaType3HashMap.get(currentEntry).minimumX = newValue.floatValue();
                    case MinimumY -> bsaType3HashMap.get(currentEntry).minimumY = newValue.floatValue();
                    case MinimumZ -> bsaType3HashMap.get(currentEntry).minimumZ = newValue.floatValue();
                    case HitAmount -> bsaType3HashMap.get(currentEntry).hitAmount = newValue.intValue(); 
                    case HitboxLifetime -> bsaType3HashMap.get(currentEntry).hitboxLifetime = newValue.intValue(); 
                    case FirstHit -> bsaType3HashMap.get(currentEntry).firstHit = newValue.intValue(); 
                    case MultipleHits -> bsaType3HashMap.get(currentEntry).multipleHits = newValue.intValue();
                    case LastHit -> bsaType3HashMap.get(currentEntry).lastHit = newValue.intValue();
                    default -> throw new IllegalArgumentException("Unexpected value: " + bsaType3Value);
                }   
            }
        });

        return spinner;
    }

    private Spinner<Number> createSpinner(Number MIN_VALUE, Number MAX_VALUE, Number value, BsaType4Values bsaType4Value) {
        Spinner<Number> spinner;

        if (value instanceof Float) {
            spinner = new Spinner<>(MIN_VALUE.doubleValue(), MAX_VALUE.doubleValue(), value.doubleValue());
        }
        else {
            spinner = new Spinner<>(MIN_VALUE.intValue(), MAX_VALUE.intValue(), value.intValue());
        }

        spinner.setEditable(true);
        spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                switch (bsaType4Value) {
                    case StartTime -> bsaType4HashMap.get(currentEntry).startTime = newValue.intValue(); 
                    case Duration -> bsaType4HashMap.get(currentEntry).duration = newValue.intValue();
                    default -> throw new IllegalArgumentException("Unexpected value: " + bsaType4Value);
                }   
            }
        });

        return spinner;
    }

    private Spinner<Number> createSpinner(Number MIN_VALUE, Number MAX_VALUE, Number value, BsaType6Values bsaType6Value) {
        Spinner<Number> spinner;

        if (value instanceof Float) {
            spinner = new Spinner<>(MIN_VALUE.doubleValue(), MAX_VALUE.doubleValue(), value.doubleValue());
        }
        else {
            spinner = new Spinner<>(MIN_VALUE.intValue(), MAX_VALUE.intValue(), value.intValue());
        }

        spinner.setEditable(true);
        spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                switch (bsaType6Value) {
                    case StartTime -> bsaType6HashMap.get(currentEntry).startTime = newValue.intValue(); 
                    case Duration -> bsaType6HashMap.get(currentEntry).duration = newValue.intValue();
                    case Skill_ID -> bsaType6HashMap.get(currentEntry).skillId = newValue.intValue();
                    case Effect_ID -> bsaType6HashMap.get(currentEntry).effectId = newValue.intValue();
                    case PositionX -> bsaType6HashMap.get(currentEntry).positionX = newValue.floatValue();
                    case PositionY -> bsaType6HashMap.get(currentEntry).positionY = newValue.floatValue();
                    case PositionZ -> bsaType6HashMap.get(currentEntry).positionZ = newValue.floatValue();
                    default -> throw new IllegalArgumentException("Unexpected value: " + bsaType6Value);
                }   
            }
        });

        return spinner;
    }

    private Spinner<Number> createSpinner(Number MIN_VALUE, Number MAX_VALUE, Number value, BsaType7Values bsaType7Value) {
        Spinner<Number> spinner = new Spinner<>(MIN_VALUE.intValue(), MAX_VALUE.intValue(), value.intValue());

        spinner.setEditable(true);
        spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                switch (bsaType7Value) {
                    case StartTime -> bsaType7HashMap.get(currentEntry).startTime = newValue.intValue(); 
                    case Duration -> bsaType7HashMap.get(currentEntry).duration = newValue.intValue();
                    case Cue_ID -> bsaType7HashMap.get(currentEntry).cueId = newValue.intValue();
                    default -> throw new IllegalArgumentException("Unexpected value: " + bsaType7Value);
                }   
            }
        });

        return spinner;
    }

    private Spinner<Number> createSpinner(Number MIN_VALUE, Number MAX_VALUE, Number value, BsaType8Values bsaType8Value) {
        Spinner<Number> spinner = new Spinner<>(MIN_VALUE.intValue(), MAX_VALUE.intValue(), value.intValue());

        spinner.setEditable(true);
        spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                switch (bsaType8Value) {
                    case StartTime -> bsaType8HashMap.get(currentEntry).startTime = newValue.intValue(); 
                    case Duration -> bsaType8HashMap.get(currentEntry).duration = newValue.intValue();
                    case BPE_Effect_ID -> bsaType8HashMap.get(currentEntry).bpeEffectId = newValue.intValue();
                    default -> throw new IllegalArgumentException("Unexpected value: " + bsaType8Value);
                }   
            }
        });

        return spinner;
    }

    private Spinner<Number> createSpinner(Number MIN_VALUE, Number MAX_VALUE, Number value, BsaType10Values bsaType10Value) {
        Spinner<Number> spinner = new Spinner<>(MIN_VALUE.intValue(), MAX_VALUE.intValue(), value.intValue());

        spinner.setEditable(true);
        spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                switch (bsaType10Value) {
                    case StartTime -> bsaType10HashMap.get(currentEntry).startTime = newValue.intValue(); 
                    case Duration -> bsaType10HashMap.get(currentEntry).duration = newValue.intValue();
                    case Skill_ID -> bsaType10HashMap.get(currentEntry).skillId = newValue.intValue();
                    default -> throw new IllegalArgumentException("Unexpected value: " + bsaType10Value);
                }   
            }
        });

        return spinner;
    }

    private Spinner<Number> createSpinner(Number MIN_VALUE, Number MAX_VALUE, Number value, BsaType12Values bsaType12Value) {
        Spinner<Number> spinner;

        if (value instanceof Float) {
            spinner = new Spinner<>(MIN_VALUE.doubleValue(), MAX_VALUE.doubleValue(), value.doubleValue());
        }
        else {
            spinner = new Spinner<>(MIN_VALUE.intValue(), MAX_VALUE.intValue(), value.intValue());
        }

        spinner.setEditable(true);
        spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                switch (bsaType12Value) {
                    case StartTime -> bsaType12HashMap.get(currentEntry).startTime = newValue.intValue(); 
                    case Duration -> bsaType12HashMap.get(currentEntry).duration = newValue.intValue();
                    case SignalValue -> bsaType12HashMap.get(currentEntry).signalValue = newValue.floatValue();
                    case PauseRecipientTimeline -> bsaType12HashMap.get(currentEntry).pauseRecipientTimeline = newValue.floatValue();
                    case Skill_ID -> bsaType12HashMap.get(currentEntry).skillId = newValue.intValue();
                    default -> throw new IllegalArgumentException("Unexpected value: " + bsaType12Value);
                }   
            }
        });

        return spinner;
    }

    private Spinner<Number> createSpinner(Number MIN_VALUE, Number MAX_VALUE, Number value, BsaType13Values bsaType13Value) {
        Spinner<Number> spinner;

        if (value instanceof Float) {
            spinner = new Spinner<>(MIN_VALUE.doubleValue(), MAX_VALUE.doubleValue(), value.doubleValue());
        }
        else {
            spinner = new Spinner<>(MIN_VALUE.intValue(), MAX_VALUE.intValue(), value.intValue());
        }

        spinner.setEditable(true);
        spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                switch (bsaType13Value) {
                    case StartTime -> bsaType13HashMap.get(currentEntry).startTime = newValue.intValue(); 
                    case Duration -> bsaType13HashMap.get(currentEntry).duration = newValue.intValue();
                    case MaxHitboxPower -> bsaType13HashMap.get(currentEntry).maxHitboxPower = newValue.floatValue();
                    case EntryPassingSignal -> bsaType13HashMap.get(currentEntry).entryPassingSignal = newValue.floatValue();
                    default -> throw new IllegalArgumentException("Unexpected value: " + bsaType13Value);
                }   
            }
        });

        return spinner;
    }

    private Spinner<Number> createSpinner(Number MIN_VALUE, Number MAX_VALUE, Number value, BsaType14Values bsaType14Value) {
        Spinner<Number> spinner;

        if (value instanceof Long) {
            spinner = new Spinner<>(MIN_VALUE.doubleValue(), MAX_VALUE.doubleValue(), value.doubleValue());
        }
        else {
            spinner = new Spinner<>(MIN_VALUE.intValue(), MAX_VALUE.intValue(), value.intValue());
        }

        spinner.setEditable(true);
        spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                switch (bsaType14Value) {
                    case StartTime -> bsaType14HashMap.get(currentEntry).startTime = newValue.intValue(); 
                    case Duration -> bsaType14HashMap.get(currentEntry).duration = newValue.intValue();
                    case PlacementFlags ->bsaType14HashMap.get(currentEntry).placementFlags = newValue.longValue();
                    case Transform_BoneSelector -> bsaType14HashMap.get(currentEntry).transform_BoneSelector = newValue.intValue();
                    case EffectPlacementFlags -> bsaType14HashMap.get(currentEntry).effectPlacementFlags = newValue.longValue();
                    default -> throw new IllegalArgumentException("Unexpected value: " + bsaType14Value);
                }   
            }
        });

        return spinner;
    }

    private StackPane createCheckBoxGroup(String text, CheckBox[] checkBoxsList, long bitMask, BsaType1Values bsaType1Value) {
        Label label = new Label(text);
        label.getStyleClass().add("titled-address-label");
        label.setTranslateY(-8); 
        label.setTranslateX(10);

        VBox vBox = new VBox(2);
        vBox.getStyleClass().add("titled-address-box");
        vBox.setPadding(new Insets(12, 0, 0, 0));
        
        for (int i = 0; i < checkBoxsList.length; i++) {
            final long bitMaskLamda = bitMask;

            checkBoxsList[i].setSelected((bsaType1HashMap.get(currentEntry).motionFlags & bitMask) != 0);

            checkBoxsList[i].selectedProperty().addListener((obs, oldValue, newValue) -> {
                if (newValue) {
                    bsaType1HashMap.get(currentEntry).motionFlags |= bitMaskLamda;
                }
                else {
                    bsaType1HashMap.get(currentEntry).motionFlags &= ~bitMaskLamda;
                }
            });

            vBox.getChildren().add(checkBoxsList[i]);

            bitMask <<= 1;
        }

        StackPane stackPane = new StackPane(vBox, label);
        StackPane.setAlignment(label, Pos.TOP_LEFT);

        return stackPane;
    }

    private StackPane createCheckBoxGroup(String text, CheckBox[] checkBoxsList, long bitMask, BsaType8Values bsaType8Value) {
        Label label = new Label(text);
        label.getStyleClass().add("titled-address-label");
        label.setTranslateY(-8); 
        label.setTranslateX(10);

        VBox vBox = new VBox(2);
        vBox.getStyleClass().add("titled-address-box");
        vBox.setPadding(new Insets(12, 0, 0, 0));
        
        for (int i = 0; i < checkBoxsList.length; i++) {
            final long bitMaskLamda = bitMask;

            checkBoxsList[i].setSelected((bsaType8HashMap.get(currentEntry).screenEffectFlags & bitMask) != 0);

            checkBoxsList[i].selectedProperty().addListener((obs, oldValue, newValue) -> {
                if (newValue) {
                    bsaType8HashMap.get(currentEntry).screenEffectFlags |= bitMaskLamda;
                }
                else {
                    bsaType8HashMap.get(currentEntry).screenEffectFlags &= ~bitMaskLamda;
                }
            });

            vBox.getChildren().add(checkBoxsList[i]);

            bitMask <<= 1;
        }

        StackPane stackPane = new StackPane(vBox, label);
        StackPane.setAlignment(label, Pos.TOP_LEFT);

        return stackPane;
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

    private void entriesActionListener() {
        addSubEntry.getItems().addAll(collisionMenuItem, collisionSoundMenuItem, type0MenuItem, type1MenuItem, type2MenuItem, type3MenuItem, type4MenuItem, type6MenuItem, type7MenuItem, type8MenuItem, type10MenuItem, type12MenuItem, type13MenuItem, type14MenuItem);

        copiedItem.setVisible(false);
        copiedItem.setDisable(true);
        pasteItem.setVisible(false);
        addItemCopy.setVisible(false);
        
        contextMenu.getItems().addAll(addEntry, addSubEntry, copy, delete, addComment, noCopiedItemFound, copiedItem, pasteItem, addItemCopy);

        treeView.setContextMenu(contextMenu);
        treeView.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue == null) return;

            currentEntry = newValue;
            grandParentEntry = newValue;
            
            try {
                while (grandParentEntry.getParent() != treeView.getRoot()) {
                    grandParentEntry = grandParentEntry.getParent();
                }
            } catch (NullPointerException e) {
                return;
            }
            
            addComment.setDisable(true);
            noCopiedItemFound.setDisable(true);
            pasteItem.setDisable(true);

            if (newValue.getValue().contains("Entry") && newValue.getParent() == treeView.getRoot()) {
                int index = tabPane.getSelectionModel().getSelectedIndex();

                tabPane.getTabs().clear();

                createBsaMain(bsaMainHashMap.get(newValue));

                tabPane.getSelectionModel().select(index);

                addComment.setDisable(false);

                if (!pasteItem.getText().contains("List") && pasteItem.getText().contains("Entry")) {
                    pasteItem.setDisable(false);
                }
            }
            else if (newValue.getParent().getValue().equals("Collision (After Effects)")) {
                int index = tabPane.getSelectionModel().getSelectedIndex();

                tabPane.getTabs().clear();

                createBsaCollision(bsaCollisionHashMap.get(newValue));

                tabPane.getSelectionModel().select(index);
                
                if (!pasteItem.getText().contains("List") && !pasteItem.getText().contains("Entry")) {
                    pasteItem.setDisable(false);
                }
            }
            else if (newValue.getParent().getValue().equals("Collision Sound (After Effects)")) {
                int index = tabPane.getSelectionModel().getSelectedIndex();
                
                tabPane.getTabs().clear();

                createBsaCollisionSound(bsaCollisionSoundHashMap.get(newValue));

                tabPane.getSelectionModel().select(index);

                if (!pasteItem.getText().contains("List") && !pasteItem.getText().contains("Entry")) {
                    pasteItem.setDisable(false);
                }
            }
            else if (newValue.getParent().getValue().equals("BSA Entry Passing")) {
                int index = tabPane.getSelectionModel().getSelectedIndex();

                tabPane.getTabs().clear();

                createBsaType0(bsaType0HashMap.get(newValue));

                tabPane.getSelectionModel().select(index);

                if (!pasteItem.getText().contains("List") && !pasteItem.getText().contains("Entry")) {
                    pasteItem.setDisable(false);
                }
            }
            else if (newValue.getParent().getValue().equals("Movement")) {
                int index = tabPane.getSelectionModel().getSelectedIndex();

                tabPane.getTabs().clear();

                createBsaType1(bsaType1HashMap.get(newValue));

                tabPane.getSelectionModel().select(index);

                if (!pasteItem.getText().contains("List") && !pasteItem.getText().contains("Entry")) {
                    pasteItem.setDisable(false);
                }
            }
            else if (newValue.getParent().getValue().equals("Projectile Timeline Remap")) {
                int index = tabPane.getSelectionModel().getSelectedIndex();

                tabPane.getTabs().clear();

                createBsaType2(bsaType2HashMap.get(newValue));

                tabPane.getSelectionModel().select(index);

                if (pasteItem.getText().contains("Projectile Timeline Remap  Ctrl+V")) {
                    pasteItem.setDisable(false);
                }
            }
            else if (newValue.getParent().getValue().equals("Hitbox")) {
                int index = tabPane.getSelectionModel().getSelectedIndex();

                tabPane.getTabs().clear();

                createBsaType3(bsaType3HashMap.get(newValue));

                tabPane.getSelectionModel().select(index);

                if (!pasteItem.getText().contains("List") && !pasteItem.getText().contains("Entry")) {
                    pasteItem.setDisable(false);
                }
            }
            else if (newValue.getParent().getValue().equals("Deflection")) {

                int index = tabPane.getSelectionModel().getSelectedIndex();

                tabPane.getTabs().clear();

                createBsaType4(bsaType4HashMap.get(newValue));

                tabPane.getSelectionModel().select(index);

                if (!pasteItem.getText().contains("List") && !pasteItem.getText().contains("Entry")) {
                    pasteItem.setDisable(false);
                }
            }
            else if (newValue.getParent().getValue().equals("Effect")) {
                int index = tabPane.getSelectionModel().getSelectedIndex();

                tabPane.getTabs().clear();

                createBsaType6(bsaType6HashMap.get(newValue));

                tabPane.getSelectionModel().select(index);

                if (!pasteItem.getText().contains("List") && !pasteItem.getText().contains("Entry")) {
                    pasteItem.setDisable(false);
                }
            }
            else if (newValue.getParent().getValue().equals("Sound")) {
                int index = tabPane.getSelectionModel().getSelectedIndex();

                tabPane.getTabs().clear();

                createBsaType7(bsaType7HashMap.get(newValue));

                tabPane.getSelectionModel().select(index);

                if (!pasteItem.getText().contains("List") && !pasteItem.getText().contains("Entry")) {
                    pasteItem.setDisable(false);
                }
            }
            else if (newValue.getParent().getValue().equals("Screen Effect")) {
                int index = tabPane.getSelectionModel().getSelectedIndex();

                tabPane.getTabs().clear();

                createBsaType8(bsaType8HashMap.get(newValue));

                tabPane.getSelectionModel().select(index);

                if (!pasteItem.getText().contains("List") && !pasteItem.getText().contains("Entry")) {
                    pasteItem.setDisable(false);
                }
            }
            else if (newValue.getParent().getValue().equals("BSA Type 10")) {
                int index = tabPane.getSelectionModel().getSelectedIndex();

                tabPane.getTabs().clear();

                createBsaType10(bsaType10HashMap.get(newValue));

                tabPane.getSelectionModel().select(index);

                if (!pasteItem.getText().contains("List") && !pasteItem.getText().contains("Entry")) {
                    pasteItem.setDisable(false);
                }
            }
            else if (newValue.getParent().getValue().equals("Send Projectile Signal")) {
                int index = tabPane.getSelectionModel().getSelectedIndex();

                tabPane.getTabs().clear();

                createBsaType12(bsaType12HashMap.get(newValue));

                tabPane.getSelectionModel().select(index);

                if (!pasteItem.getText().contains("List") && !pasteItem.getText().contains("Entry")) {
                    pasteItem.setDisable(false);
                }
            }
            else if (newValue.getParent().getValue().equals("Projectile Protection")) {
                int index = tabPane.getSelectionModel().getSelectedIndex();

                tabPane.getTabs().clear();

                createBsaType13(bsaType13HashMap.get(newValue));

                tabPane.getSelectionModel().select(index);

                if (!pasteItem.getText().contains("List") && !pasteItem.getText().contains("Entry")) {
                    pasteItem.setDisable(false);
                }
            }
            else if (newValue.getParent().getValue().equals("Effect Placement")) {
                int index = tabPane.getSelectionModel().getSelectedIndex();

                tabPane.getTabs().clear();

                createBsaType14(bsaType14HashMap.get(newValue));

                tabPane.getSelectionModel().select(index);

                if (!pasteItem.getText().contains("List") && !pasteItem.getText().contains("Entry")) {
                    pasteItem.setDisable(false);
                }
            }
            else {
                tabPane.getTabs().clear();
            } 

            if (pasteItem.getText().contains(newValue.getValue() + " List")) {
                pasteItem.setDisable(false);
            }
        });

        treeView.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.SECONDARY) {
                contextMenu.setOnAction(event -> {
                    if (event.getTarget() == addEntry) AddEntry();
                    else if (event.getTarget() == copy) Copy();
                    else if (event.getTarget() == delete) Delete();
                    else if (event.getTarget() == addComment) Popups.AddComment(currentEntry);
                    else if (event.getTarget() == pasteItem) Paste();
                    else if (event.getTarget() == addItemCopy) AddItemCopy();
                });

                addSubEntry.setOnAction(ev -> {
                    switch (addSubEntry.getItems().indexOf(ev.getTarget())) {
                        case 0 -> {
                            TreeItem<String> newItem = addSubEntryItem(addSubEntry.getItems().get(0).getText());
                            bsaCollisionHashMap.put(newItem, new BsaCollisionEntry());
                            treeView.getSelectionModel().select(newItem);
                        }
                        case 1 -> {
                            TreeItem<String> newItem = addSubEntryItem(addSubEntry.getItems().get(1).getText());
                            bsaCollisionSoundHashMap.put(newItem, new BsaCollisionSoundEntry());
                            treeView.getSelectionModel().select(newItem);
                        }
                        case 2 -> {
                            TreeItem<String> newItem = addSubEntryItem(addSubEntry.getItems().get(2).getText());
                            bsaType0HashMap.put(newItem, new BsaType0Entry());
                            treeView.getSelectionModel().select(newItem);
                        }
                        case 3 -> {
                           TreeItem<String> newItem = addSubEntryItem(addSubEntry.getItems().get(3).getText());
                            bsaType1HashMap.put(newItem, new BsaType1Entry());
                            treeView.getSelectionModel().select(newItem);
                        }
                        case 4 -> {
                            TreeItem<String> newItem = addSubEntryItem(addSubEntry.getItems().get(4).getText());
                            bsaType2HashMap.put(newItem, new BsaType2Entry());
                            treeView.getSelectionModel().select(newItem);
                        }
                        case 5 -> {
                           TreeItem<String> newItem = addSubEntryItem(addSubEntry.getItems().get(5).getText());
                            bsaType3HashMap.put(newItem, new BsaType3Entry());
                            treeView.getSelectionModel().select(newItem);
                        }
                        case 6 -> {
                            TreeItem<String> newItem = addSubEntryItem(addSubEntry.getItems().get(6).getText());
                            bsaType4HashMap.put(newItem, new BsaType4Entry());
                            treeView.getSelectionModel().select(newItem);
                        }
                        case 7 -> {
                            TreeItem<String> newItem = addSubEntryItem(addSubEntry.getItems().get(7).getText());
                            bsaType6HashMap.put(newItem, new BsaType6Entry());
                            treeView.getSelectionModel().select(newItem);
                        }
                        case 8 -> {
                            TreeItem<String> newItem = addSubEntryItem(addSubEntry.getItems().get(8).getText());
                            bsaType7HashMap.put(newItem, new BsaType7Entry());
                            treeView.getSelectionModel().select(newItem);
                        }
                        case 9 -> {
                            TreeItem<String> newItem = addSubEntryItem(addSubEntry.getItems().get(9).getText());
                            bsaType8HashMap.put(newItem, new BsaType8Entry());
                            treeView.getSelectionModel().select(newItem);
                        }
                        case 10 -> {
                            TreeItem<String> newItem = addSubEntryItem(addSubEntry.getItems().get(10).getText());
                            bsaType10HashMap.put(newItem, new BsaType10Entry());
                            treeView.getSelectionModel().select(newItem);
                        }
                        case 11 -> {
                            TreeItem<String> newItem = addSubEntryItem(addSubEntry.getItems().get(11).getText());
                            bsaType12HashMap.put(newItem, new BsaType12Entry());
                            treeView.getSelectionModel().select(newItem);
                        }
                        case 12 -> {
                            TreeItem<String> newItem = addSubEntryItem(addSubEntry.getItems().get(12).getText());
                            bsaType13HashMap.put(newItem, new BsaType13Entry());
                            treeView.getSelectionModel().select(newItem);
                        }
                        case 13 -> {
                            TreeItem<String> newItem = addSubEntryItem(addSubEntry.getItems().get(13).getText());
                            bsaType14HashMap.put(newItem, new BsaType14Entry());
                            treeView.getSelectionModel().select(newItem);
                        }
                    }
                });
            }
        });
    }

    private void entriesKeysListener() {
        treeView.setOnKeyPressed(e -> {
            if (e.isControlDown() && e.getCode() == KeyCode.C) Copy();
            else if (e.isControlDown() && e.getCode() == KeyCode.V) Paste();
            else if (e.getCode() == KeyCode.DELETE) Delete();
            else if (e.isControlDown() && e.getCode() == KeyCode.A) AddItemCopy();
            else if (e.isControlDown() && e.getCode() == KeyCode.Q) {
                if (currentEntry == grandParentEntry) {
                    Popups.AddComment(currentEntry);
                }
            }
        });
    }

    private void AddEntry() {
        if (treeView.getRoot() == null) {
            treeView.setRoot(new TreeItem<>("dummy"));
            treeView.setShowRoot(false);
        }
        else if (treeView.getRoot().getChildren().isEmpty()) {
            allEntries = 0;
        } 

        treeView.getRoot().getChildren().add(new TreeItem<>("Entry "+ allEntries));

        allEntries++;

        BsaMainEntry bsaMainEntry = new BsaMainEntry();

        bsaMainHashMap.put(treeView.getRoot().getChildren().getLast(), bsaMainEntry);
    }


    private void Copy() {
        noCopiedItemFound.setVisible(false);
        copiedItem.setVisible(true);
        pasteItem.setVisible(true);
        addItemCopy.setVisible(true);

        pasteItem.setDisable(false);

        if (currentEntry.getParent() == treeView.getRoot()) {
            setContextMenuText("Entry");

            copyTypesContainer = new String[currentEntry.getChildren().size()];
            copyContainer = new BsaMainEntry(bsaMainHashMap.get(currentEntry));
            copyListContainer = new Object[currentEntry.getChildren().size()][];

            for (int i = 0; i < currentEntry.getChildren().size(); i++) {
                copyListContainer[i] = new Object[currentEntry.getChildren().get(i).getChildren().size()];

                switch (currentEntry.getChildren().get(i).getValue()) {
                    case "Collision (After Effects)" -> {
                        copyEntryItem(bsaCollisionHashMap, currentEntry.getChildren().get(i).getValue(), i);
                    }
                    case "Collision Sound (After Effects)" -> {
                        copyEntryItem(bsaCollisionSoundHashMap, currentEntry.getChildren().get(i).getValue(), i);
                    }
                    case "BSA Entry Passing" -> {
                        copyEntryItem(bsaType0HashMap, currentEntry.getChildren().get(i).getValue(), i);
                    }
                    case "Movement" -> {
                        copyEntryItem(bsaType1HashMap, currentEntry.getChildren().get(i).getValue(), i);
                    }
                    case "Projectile Timeline Remap" -> {
                        copyEntryItem(bsaType2HashMap, currentEntry.getChildren().get(i).getValue(), i);
                    }
                    case "Hitbox" -> {
                        copyEntryItem(bsaType3HashMap, currentEntry.getChildren().get(i).getValue(), i);
                    }
                    case "Deflection" -> {
                        copyEntryItem(bsaType4HashMap, currentEntry.getChildren().get(i).getValue(), i);
                    }
                    case "Effect" -> {
                        copyEntryItem(bsaType6HashMap, currentEntry.getChildren().get(i).getValue(), i);
                    }
                    case "Sound" -> {
                        copyEntryItem(bsaType7HashMap, currentEntry.getChildren().get(i).getValue(), i);
                    }
                    case "Screen Effect" -> {
                        copyEntryItem(bsaType8HashMap, currentEntry.getChildren().get(i).getValue(), i);
                    }
                    case "BSA Type 10" -> {
                        copyEntryItem(bsaType10HashMap, currentEntry.getChildren().get(i).getValue(), i);
                    }
                    case "Send Projectile Signal" -> {
                        copyEntryItem(bsaType12HashMap, currentEntry.getChildren().get(i).getValue(), i);
                    }
                    case "Projectile Protection" -> {
                        copyEntryItem(bsaType13HashMap, currentEntry.getChildren().get(i).getValue(), i);
                    }
                    case "Effect Placement" -> {
                        copyEntryItem(bsaType14HashMap, currentEntry.getChildren().get(i).getValue(), i);
                    }
                }
            }
        }
        else if (currentEntry.getChildren().isEmpty() && currentEntry.getValue().startsWith("Entry")) {
            switch (currentEntry.getParent().getValue()) {
                case "Collision (After Effects)" -> {
                    copyChildItem(bsaCollisionHashMap);
                    setContextMenuText(currentEntry.getParent().getValue());
                }
                case "Collision Sound (After Effects)" -> {
                    copyChildItem(bsaCollisionSoundHashMap);
                    setContextMenuText(currentEntry.getParent().getValue());
                }
                case "BSA Entry Passing" -> {
                    copyChildItem(bsaType0HashMap);
                    setContextMenuText(currentEntry.getParent().getValue());
                }
                case "Movement" -> {
                    copyChildItem(bsaType1HashMap);
                    setContextMenuText(currentEntry.getParent().getValue());
                }
                case "Projectile Timeline Remap" -> {
                    copyChildItem(bsaType2HashMap);
                    setContextMenuText(currentEntry.getParent().getValue());;
                }
                case "Hitbox" -> {
                    copyChildItem(bsaType3HashMap);
                    setContextMenuText(currentEntry.getParent().getValue());
                }
                case "Deflection" -> {
                    copyChildItem(bsaType4HashMap);
                    setContextMenuText(currentEntry.getParent().getValue());
                }
                case "Effect" -> {
                    copyChildItem(bsaType6HashMap);
                    setContextMenuText(currentEntry.getParent().getValue());
                }
                case "Sound" -> {
                    copyChildItem(bsaType7HashMap);
                    setContextMenuText(currentEntry.getParent().getValue());
                }
                case "Screen Effect" -> {
                    copyChildItem(bsaType8HashMap);
                    setContextMenuText(currentEntry.getParent().getValue());
                }
                case "BSA Type 10" -> {
                    copyChildItem(bsaType10HashMap);
                    setContextMenuText(currentEntry.getParent().getValue());
                }
                case "Send Projectile Signal" -> {
                    copyChildItem(bsaType12HashMap);
                    setContextMenuText(currentEntry.getParent().getValue());
                }
                case "Projectile Protection" -> {
                    copyChildItem(bsaType13HashMap);
                    setContextMenuText(currentEntry.getParent().getValue());
                }
                case "Effect Placement" -> {
                    copyChildItem(bsaType14HashMap);
                    setContextMenuText(currentEntry.getParent().getValue());
                }
            }
        }
        else if (currentEntry.getChildren().isEmpty()) {
            setContextMenuText("Null");
        }
        else {
            copyListContainer = new Object[1][currentEntry.getChildren().size()];

            switch (currentEntry.getValue()) {
                case "Collision (After Effects)" -> {
                    copyListItem(bsaCollisionHashMap);
                    setContextMenuText(currentEntry.getValue() + " List");
                }
                case "Collision Sound (After Effects)" -> {
                    copyListItem(bsaCollisionSoundHashMap);
                    setContextMenuText(currentEntry.getValue() + " List");
                }
                case "BSA Entry Passing" -> {
                    copyListItem(bsaType0HashMap);
                    setContextMenuText(currentEntry.getValue() + " List");
                }
                case "Movement" -> {
                    copyListItem(bsaType1HashMap);
                    setContextMenuText(currentEntry.getValue() + " List");
                }
                case "Projectile Timeline Remap" -> {
                    copyListItem(bsaType2HashMap);
                    setContextMenuText(currentEntry.getValue() + " List");
                }
                case "Hitbox" -> {
                    copyListItem(bsaType3HashMap);
                    setContextMenuText(currentEntry.getValue() + " List");
                }
                case "Deflection" -> {
                    copyListItem(bsaType4HashMap);
                    setContextMenuText(currentEntry.getValue() + " List");
                }
                case "Effect" -> {
                    copyListItem(bsaType6HashMap);
                    setContextMenuText(currentEntry.getValue() + " List");
                }
                case "Sound" -> {
                    copyListItem(bsaType7HashMap);
                    setContextMenuText(currentEntry.getValue() + " List");
                }
                case "Screen Effect" -> {
                    copyListItem(bsaType8HashMap);
                    setContextMenuText(currentEntry.getValue() + " List");
                }
                case "BSA Type 10" -> {
                    copyListItem(bsaType10HashMap);
                    setContextMenuText(currentEntry.getValue() + " List");
                }
                case "Send Projectile Signal" -> {
                    copyListItem(bsaType12HashMap);
                    setContextMenuText(currentEntry.getValue() + " List");
                }
                case "Projectile Protection" -> {
                    copyListItem(bsaType13HashMap);
                    setContextMenuText(currentEntry.getValue() + " List");
                }
                case "Effect Placement" -> {
                    copyListItem(bsaType14HashMap);
                    setContextMenuText(currentEntry.getValue() + " List");
                }
            }
        }
    }

    private void Paste() {
        if (currentEntry.getParent() == treeView.getRoot()) {
            for (TreeItem<String> parent : grandParentEntry.getChildren()) {
                switch (parent.getValue()) {
                    case "Collision (After Effects)" -> {
                        for (TreeItem<String> child : parent.getChildren()) {
                            bsaCollisionHashMap.remove(child);
                        }
                    }
                    case "Collision Sound (After Effects)" -> {
                        for (TreeItem<String> child : parent.getChildren()) {
                            bsaCollisionSoundHashMap.remove(child);
                        }
                    }
                    case "BSA Entry Passing" -> {
                        for (TreeItem<String> child : parent.getChildren()) {
                            bsaType0HashMap.remove(child);
                        }
                    }
                    case "Movement" -> {
                        for (TreeItem<String> child : parent.getChildren()) {
                            bsaType1HashMap.remove(child);
                        }
                    }
                    case "Projectile Timeline Remap" -> {
                        for (TreeItem<String> child : parent.getChildren()) {
                            bsaType2HashMap.remove(child);
                        }
                    }
                    case "Hitbox" -> {
                        for (TreeItem<String> child : parent.getChildren()) {
                            bsaType3HashMap.remove(child);
                        }
                    }
                    case "Deflection" -> {
                        for (TreeItem<String> child : parent.getChildren()) {
                            bsaType4HashMap.remove(child);
                        }
                    }
                    case "Effect" -> {
                        for (TreeItem<String> child : parent.getChildren()) {
                            bsaType6HashMap.remove(child);
                        }
                    }
                    case "Sound" -> {
                        for (TreeItem<String> child : parent.getChildren()) {
                            bsaType7HashMap.remove(child);
                        }
                    }
                    case "Screen Effect" -> {
                        for (TreeItem<String> child : parent.getChildren()) {
                            bsaType8HashMap.remove(child);
                        }
                    }
                    case "BSA Type 10" -> {
                        for (TreeItem<String> child : parent.getChildren()) {
                            bsaType10HashMap.remove(child);
                        }
                    }
                    case "Send Projectile Signal" -> {
                        for (TreeItem<String> child : parent.getChildren()) {
                            bsaType12HashMap.remove(child);
                        }
                    }
                    case "Projectile Protection" -> {
                        for (TreeItem<String> child : parent.getChildren()) {
                            bsaType13HashMap.remove(child);
                        }
                    }
                    case "Effect Placement" -> {
                        for (TreeItem<String> child : parent.getChildren()) {
                            bsaType14HashMap.remove(child);
                        }
                    }
                }
            }

            currentEntry.getChildren().clear();

            bsaMainHashMap.put(currentEntry, new BsaMainEntry((BsaMainEntry) copyContainer));

            for (int i = 0; i < copyTypesContainer.length; i++) {
                switch (copyTypesContainer[i]) {
                    case "Collision (After Effects)" -> {
                        currentEntry.getChildren().add(i, new TreeItem<>("Collision (After Effects)"));

                        for (int j = 0; j < copyListContainer[i].length; j++) {
                            currentEntry.getChildren().get(i).getChildren().add(j, new TreeItem<>("Entry " +j));

                            bsaCollisionHashMap.put(currentEntry.getChildren().get(i).getChildren().get(j), new BsaCollisionEntry((BsaCollisionEntry) copyListContainer[i][j]));
                        }
                    }
                    case "Collision Sound (After Effects)" -> {
                        currentEntry.getChildren().add(i, new TreeItem<>("Collision Sound (After Effects)"));

                        for (int j = 0; j < copyListContainer[i].length; j++) {
                            currentEntry.getChildren().get(i).getChildren().add(j, new TreeItem<>("Entry " +j));

                            bsaCollisionSoundHashMap.put(currentEntry.getChildren().get(i).getChildren().get(j), new BsaCollisionSoundEntry((BsaCollisionSoundEntry) copyListContainer[i][j]));
                        }
                    }
                    case "BSA Entry Passing" -> {
                        currentEntry.getChildren().add(i, new TreeItem<>("BSA Entry Passing"));

                        for (int j = 0; j < copyListContainer[i].length; j++) {
                            currentEntry.getChildren().get(i).getChildren().add(j, new TreeItem<>("Entry " +j));

                            bsaType0HashMap.put(currentEntry.getChildren().get(i).getChildren().get(j), new BsaType0Entry((BsaType0Entry) copyListContainer[i][j]));
                        }
                    }
                    case "Movement" -> {
                        currentEntry.getChildren().add(i, new TreeItem<>("Movement"));

                        for (int j = 0; j < copyListContainer[i].length; j++) {
                            currentEntry.getChildren().get(i).getChildren().add(j, new TreeItem<>("Entry " +j));

                            bsaType1HashMap.put(currentEntry.getChildren().get(i).getChildren().get(j), new BsaType1Entry((BsaType1Entry) copyListContainer[i][j]));
                        }
                    }
                    case "Projectile Timeline Remap" -> {
                        currentEntry.getChildren().add(i, new TreeItem<>("Projectile Timeline Remap"));

                        for (int j = 0; j < copyListContainer[i].length; j++) {
                            currentEntry.getChildren().get(i).getChildren().add(j, new TreeItem<>("Entry " +j));

                            bsaType2HashMap.put(currentEntry.getChildren().get(i).getChildren().get(j), new BsaType2Entry((BsaType2Entry) copyListContainer[i][j]));
                        }
                    }
                    case "Hitbox" -> {
                        currentEntry.getChildren().add(i, new TreeItem<>("Hitbox"));

                        for (int j = 0; j < copyListContainer[i].length; j++) {
                            currentEntry.getChildren().get(i).getChildren().add(j, new TreeItem<>("Entry " +j));

                            bsaType3HashMap.put(currentEntry.getChildren().get(i).getChildren().get(j), new BsaType3Entry((BsaType3Entry) copyListContainer[i][j]));
                        }
                    }
                    case "Deflection" -> {
                        currentEntry.getChildren().add(i, new TreeItem<>("Deflection"));

                        for (int j = 0; j < copyListContainer[i].length; j++) {
                            currentEntry.getChildren().get(i).getChildren().add(j, new TreeItem<>("Entry " +j));

                            bsaType4HashMap.put(currentEntry.getChildren().get(i).getChildren().get(j), new BsaType4Entry((BsaType4Entry) copyListContainer[i][j]));
                        }
                    }
                    case "Effect" -> {
                        currentEntry.getChildren().add(i, new TreeItem<>("Effect"));

                        for (int j = 0; j < copyListContainer[i].length; j++) {
                            currentEntry.getChildren().get(i).getChildren().add(j, new TreeItem<>("Entry " +j));

                            bsaType6HashMap.put(currentEntry.getChildren().get(i).getChildren().get(j), new BsaType6Entry((BsaType6Entry) copyListContainer[i][j]));
                        }
                    }
                    case "Sound" -> {
                        currentEntry.getChildren().add(i, new TreeItem<>("Sound"));

                        for (int j = 0; j < copyListContainer[i].length; j++) {
                            currentEntry.getChildren().get(i).getChildren().add(j, new TreeItem<>("Entry " +j));

                            bsaType7HashMap.put(currentEntry.getChildren().get(i).getChildren().get(j), new BsaType7Entry((BsaType7Entry) copyListContainer[i][j]));
                        }
                    }
                    case "Screen Effect" -> {
                        currentEntry.getChildren().add(i, new TreeItem<>("Screen Effect"));

                        for (int j = 0; j < copyListContainer[i].length; j++) {
                            currentEntry.getChildren().get(i).getChildren().add(j, new TreeItem<>("Entry " +j));

                            bsaType8HashMap.put(currentEntry.getChildren().get(i).getChildren().get(j), new BsaType8Entry((BsaType8Entry) copyListContainer[i][j]));
                        }
                    }
                    case "BSA Type 10" -> {
                        currentEntry.getChildren().add(i, new TreeItem<>("BSA Type 10"));

                        for (int j = 0; j < copyListContainer[i].length; j++) {
                            currentEntry.getChildren().get(i).getChildren().add(j, new TreeItem<>("Entry " +j));

                            bsaType10HashMap.put(currentEntry.getChildren().get(i).getChildren().get(j), new BsaType10Entry((BsaType10Entry) copyListContainer[i][j]));
                        }
                    }
                    case "Send Projectile Signal" -> {
                        currentEntry.getChildren().add(i, new TreeItem<>("Send Projectile Signal"));

                        for (int j = 0; j < copyListContainer[i].length; j++) {
                            currentEntry.getChildren().get(i).getChildren().add(j, new TreeItem<>("Entry " +j));

                            bsaType12HashMap.put(currentEntry.getChildren().get(i).getChildren().get(j), new BsaType12Entry((BsaType12Entry) copyListContainer[i][j]));
                        }
                    }
                    case "Projectile Protection" -> {
                        currentEntry.getChildren().add(i, new TreeItem<>("Projectile Protection"));

                        for (int j = 0; j < copyListContainer[i].length; j++) {
                            currentEntry.getChildren().get(i).getChildren().add(j, new TreeItem<>("Entry " +j));

                            bsaType13HashMap.put(currentEntry.getChildren().get(i).getChildren().get(j), new BsaType13Entry((BsaType13Entry) copyListContainer[i][j]));
                        }
                    }
                    case "Effect Placement" -> {
                        currentEntry.getChildren().add(i, new TreeItem<>("Effect Placement"));

                        for (int j = 0; j < copyListContainer[i].length; j++) {
                            currentEntry.getChildren().get(i).getChildren().add(j, new TreeItem<>("Entry " +j));

                            bsaType14HashMap.put(currentEntry.getChildren().get(i).getChildren().get(j), new BsaType14Entry((BsaType14Entry) copyListContainer[i][j]));
                        }
                    }
                }
            }

            tabPane.getTabs().clear();
            createBsaMain(bsaMainHashMap.get(currentEntry));
            tabPane.getSelectionModel().select(tabPane.getSelectionModel().getSelectedIndex());
        }
        else if (currentEntry.getChildren().isEmpty()) {
            switch (currentEntry.getParent().getValue()) {
                case "Collision (After Effects)" -> {
                    bsaCollisionHashMap.put(currentEntry, new BsaCollisionEntry((BsaCollisionEntry) copyContainer));

                    tabPane.getTabs().clear();
                    createBsaCollision(bsaCollisionHashMap.get(currentEntry));
                    tabPane.getSelectionModel().select(tabPane.getSelectionModel().getSelectedIndex());
                }
                case "Collision Sound (After Effects)" -> {
                    bsaCollisionSoundHashMap.put(currentEntry, new BsaCollisionSoundEntry((BsaCollisionSoundEntry) copyContainer));
                    
                    tabPane.getTabs().clear();
                    createBsaCollisionSound(bsaCollisionSoundHashMap.get(currentEntry));
                    tabPane.getSelectionModel().select(tabPane.getSelectionModel().getSelectedIndex());
                }
                case "BSA Entry Passing" -> {
                    bsaType0HashMap.put(currentEntry, new BsaType0Entry((BsaType0Entry) copyContainer));
                    
                    tabPane.getTabs().clear();
                    createBsaType0(bsaType0HashMap.get(currentEntry));
                    tabPane.getSelectionModel().select(tabPane.getSelectionModel().getSelectedIndex());
                }
                case "Movement" -> {
                    bsaType1HashMap.put(currentEntry, new BsaType1Entry((BsaType1Entry) copyContainer));
                    
                    tabPane.getTabs().clear();
                    createBsaType1(bsaType1HashMap.get(currentEntry));
                    tabPane.getSelectionModel().select(tabPane.getSelectionModel().getSelectedIndex());
                }
                case "Projectile Timeline Remap" -> {
                    bsaType2HashMap.put(currentEntry, new BsaType2Entry((BsaType2Entry) copyContainer));
                    
                    tabPane.getTabs().clear();
                    createBsaType2(bsaType2HashMap.get(currentEntry));
                    tabPane.getSelectionModel().select(tabPane.getSelectionModel().getSelectedIndex());
                }
                case "Hitbox" -> {
                    bsaType3HashMap.put(currentEntry, new BsaType3Entry((BsaType3Entry) copyContainer));
                    
                    tabPane.getTabs().clear();
                    createBsaType3(bsaType3HashMap.get(currentEntry));
                    tabPane.getSelectionModel().select(tabPane.getSelectionModel().getSelectedIndex());
                }
                case "Deflection" -> {
                    bsaType4HashMap.put(currentEntry, new BsaType4Entry((BsaType4Entry) copyContainer));
                    
                    tabPane.getTabs().clear();
                    createBsaType4(bsaType4HashMap.get(currentEntry));
                    tabPane.getSelectionModel().select(tabPane.getSelectionModel().getSelectedIndex());
                }
                case "Effect" -> {
                    bsaType6HashMap.put(currentEntry, new BsaType6Entry((BsaType6Entry) copyContainer));
                    
                    tabPane.getTabs().clear();
                    createBsaType6(bsaType6HashMap.get(currentEntry));
                    tabPane.getSelectionModel().select(tabPane.getSelectionModel().getSelectedIndex());
                }
                case "Sound" -> {
                    bsaType7HashMap.put(currentEntry, new BsaType7Entry((BsaType7Entry) copyContainer));
                    
                    tabPane.getTabs().clear();
                    createBsaType7(bsaType7HashMap.get(currentEntry));
                    tabPane.getSelectionModel().select(tabPane.getSelectionModel().getSelectedIndex());
                }
                case "Screen Effect" -> {
                    bsaType8HashMap.put(currentEntry, new BsaType8Entry((BsaType8Entry) copyContainer));
                    
                    tabPane.getTabs().clear();
                    createBsaType8(bsaType8HashMap.get(currentEntry));
                    tabPane.getSelectionModel().select(tabPane.getSelectionModel().getSelectedIndex());
                }
                case "BSA Type 10" -> {
                    bsaType10HashMap.put(currentEntry, new BsaType10Entry((BsaType10Entry) copyContainer));
                    
                    tabPane.getTabs().clear();
                    createBsaType10(bsaType10HashMap.get(currentEntry));
                    tabPane.getSelectionModel().select(tabPane.getSelectionModel().getSelectedIndex());
                }
                case "Send Projectile Signal" -> {
                    bsaType12HashMap.put(currentEntry, new BsaType12Entry((BsaType12Entry) copyContainer));
                    
                    tabPane.getTabs().clear();
                    createBsaType12(bsaType12HashMap.get(currentEntry));
                    tabPane.getSelectionModel().select(tabPane.getSelectionModel().getSelectedIndex());
                }
                case "Projectile Protection" -> {
                    bsaType13HashMap.put(currentEntry, new BsaType13Entry((BsaType13Entry) copyContainer));
                    
                    tabPane.getTabs().clear();
                    createBsaType13(bsaType13HashMap.get(currentEntry));
                    tabPane.getSelectionModel().select(tabPane.getSelectionModel().getSelectedIndex());
                }
                case "Effect Placement" -> {
                    bsaType14HashMap.put(currentEntry, new BsaType14Entry((BsaType14Entry) copyContainer));
                    
                    tabPane.getTabs().clear();
                    createBsaType14(bsaType14HashMap.get(currentEntry));
                    tabPane.getSelectionModel().select(tabPane.getSelectionModel().getSelectedIndex());
                }
            }
        }
        else {
            switch (currentEntry.getValue()) {
                case "Collision (After Effects)" -> {
                    for (TreeItem<String> child : currentEntry.getChildren()) {
                        bsaCollisionHashMap.remove(child);
                    }
                }
                case "Collision Sound (After Effects)" -> {
                    for (TreeItem<String> child : currentEntry.getChildren()) {
                        bsaCollisionSoundHashMap.remove(child);
                    }
                }
                case "BSA Entry Passing" -> {
                    for (TreeItem<String> child : currentEntry.getChildren()) {
                        bsaType0HashMap.remove(child);
                    }
                }
                case "Movement" -> {
                    for (TreeItem<String> child : currentEntry.getChildren()) {
                        bsaType1HashMap.remove(child);
                    }
                }
                case "Projectile Timeline Remap" -> {
                    for (TreeItem<String> child : currentEntry.getChildren()) {
                        bsaType2HashMap.remove(child);
                    }
                }
                case "Hitbox" -> {
                    for (TreeItem<String> child : currentEntry.getChildren()) {
                        bsaType3HashMap.remove(child);
                    }
                }
                case "Deflection" -> {
                    for (TreeItem<String> child : currentEntry.getChildren()) {
                        bsaType4HashMap.remove(child);
                    }
                }
                case "Effect" -> {
                    for (TreeItem<String> child : currentEntry.getChildren()) {
                        bsaType6HashMap.remove(child);
                    }
                }
                case "Sound" -> {
                    for (TreeItem<String> child : currentEntry.getChildren()) {
                        bsaType7HashMap.remove(child);
                    }
                }
                case "Screen Effect" -> {
                    for (TreeItem<String> child : currentEntry.getChildren()) {
                        bsaType8HashMap.remove(child);
                    }
                }
                case "BSA Type 10" -> {
                    for (TreeItem<String> child : currentEntry.getChildren()) {
                        bsaType10HashMap.remove(child);
                    }
                }
                case "Send Projectile Signal" -> {
                    for (TreeItem<String> child : currentEntry.getChildren()) {
                        bsaType12HashMap.remove(child);
                    }
                }
                case "Projectile Protection" -> {
                    for (TreeItem<String> child : currentEntry.getChildren()) {
                        bsaType13HashMap.remove(child);
                    }
                }
                case "Effect Placement" -> {
                    for (TreeItem<String> child : currentEntry.getChildren()) {
                        bsaType14HashMap.remove(child);
                    }
                }
            }

            currentEntry.getChildren().clear();

            switch (currentEntry.getValue()) {
                case "Collision (After Effects)" -> {
                    for (int i = 0; i < copyListContainer[0].length; i++) {
                        currentEntry.getChildren().add(i, new TreeItem<>("Entry " +i));

                        bsaCollisionHashMap.put(currentEntry.getChildren().get(i), new BsaCollisionEntry((BsaCollisionEntry) copyListContainer[0][i]));
                    }
                }
                case "Collision Sound (After Effects)" -> {
                    for (int i = 0; i < copyListContainer[0].length; i++) {
                        currentEntry.getChildren().add(i, new TreeItem<>("Entry " +i));

                        bsaCollisionSoundHashMap.put(currentEntry.getChildren().get(i), new BsaCollisionSoundEntry((BsaCollisionSoundEntry) copyListContainer[0][i]));
                    }
                }
                case "BSA Entry Passing" -> {
                    for (int i = 0; i < copyListContainer[0].length; i++) {
                        currentEntry.getChildren().add(i, new TreeItem<>("Entry " +i));

                        bsaType0HashMap.put(currentEntry.getChildren().get(i), new BsaType0Entry((BsaType0Entry) copyListContainer[0][i]));
                    }
                }
                case "Movement" -> {
                    for (int i = 0; i < copyListContainer[0].length; i++) {
                        currentEntry.getChildren().add(i, new TreeItem<>("Entry " +i));

                        bsaType1HashMap.put(currentEntry.getChildren().get(i), new BsaType1Entry((BsaType1Entry) copyListContainer[0][i]));
                    }
                }
                case "Projectile Timeline Remap" -> {
                    for (int i = 0; i < copyListContainer[0].length; i++) {
                        currentEntry.getChildren().add(i, new TreeItem<>("Entry " +i));

                        bsaType2HashMap.put(currentEntry.getChildren().get(i), new BsaType2Entry((BsaType2Entry) copyListContainer[0][i]));
                    }
                }
                case "Hitbox" -> {
                    for (int i = 0; i < copyListContainer[0].length; i++) {
                        currentEntry.getChildren().add(i, new TreeItem<>("Entry " +i));

                        bsaType3HashMap.put(currentEntry.getChildren().get(i), new BsaType3Entry((BsaType3Entry) copyListContainer[0][i]));
                    }
                }
                case "Deflection" -> {
                    for (int i = 0; i < copyListContainer[0].length; i++) {
                        currentEntry.getChildren().add(i, new TreeItem<>("Entry " +i));

                        bsaType4HashMap.put(currentEntry.getChildren().get(i), new BsaType4Entry((BsaType4Entry) copyListContainer[0][i]));
                    }
                }
                case "Effect" -> {
                    for (int i = 0; i < copyListContainer[0].length; i++) {
                        currentEntry.getChildren().add(i, new TreeItem<>("Entry " +i));

                        bsaType6HashMap.put(currentEntry.getChildren().get(i), new BsaType6Entry((BsaType6Entry) copyListContainer[0][i]));
                    }
                }
                case "Sound" -> {
                    for (int i = 0; i < copyListContainer[0].length; i++) {
                        currentEntry.getChildren().add(i, new TreeItem<>("Entry " +i));

                        bsaType7HashMap.put(currentEntry.getChildren().get(i), new BsaType7Entry((BsaType7Entry) copyListContainer[0][i]));
                    }
                }
                case "Screen Effect" -> {
                    for (int i = 0; i < copyListContainer[0].length; i++) {
                        currentEntry.getChildren().add(i, new TreeItem<>("Entry " +i));

                        bsaType8HashMap.put(currentEntry.getChildren().get(i), new BsaType8Entry((BsaType8Entry) copyListContainer[0][i]));
                    }
                }
                case "BSA Type 10" -> {
                    for (int i = 0; i < copyListContainer[0].length; i++) {
                        currentEntry.getChildren().add(i, new TreeItem<>("Entry " +i));

                        bsaType10HashMap.put(currentEntry.getChildren().get(i), new BsaType10Entry((BsaType10Entry) copyListContainer[0][i]));
                    }
                }
                case "Send Projectile Signal" -> {
                    for (int i = 0; i < copyListContainer[0].length; i++) {
                        currentEntry.getChildren().add(i, new TreeItem<>("Entry " +i));

                        bsaType12HashMap.put(currentEntry.getChildren().get(i), new BsaType12Entry((BsaType12Entry) copyListContainer[0][i]));
                    }
                }
                case "Projectile Protection" -> {
                    for (int i = 0; i < copyListContainer[0].length; i++) {
                        currentEntry.getChildren().add(i, new TreeItem<>("Entry " +i));

                        bsaType13HashMap.put(currentEntry.getChildren().get(i), new BsaType13Entry((BsaType13Entry) copyListContainer[0][i]));
                    }
                }
                case "Effect Placement" -> {
                    for (int i = 0; i < copyListContainer[0].length; i++) {
                        currentEntry.getChildren().add(i, new TreeItem<>("Entry " +i));

                        bsaType14HashMap.put(currentEntry.getChildren().get(i), new BsaType14Entry((BsaType14Entry) copyListContainer[0][i]));
                    }
                }
            }
        }
    }

    private void AddItemCopy() {
        if (treeView.getRoot().getChildren().isEmpty()) {
            allEntries = 0;
        } 

        if (addItemCopy.getText().contains("Entry") && !addItemCopy.getText().contains("BSA")) {
            TreeItem<String> newEntry = new TreeItem<>("Entry " + allEntries);
            treeView.getRoot().getChildren().add(newEntry);
            bsaMainHashMap.put(newEntry, new BsaMainEntry((BsaMainEntry) copyContainer));
            allEntries++;

            for (int i = 0; i < copyTypesContainer.length; i++) {
                switch (copyTypesContainer[i]) {
                    case "Collision (After Effects)" -> {
                        newEntry.getChildren().add(i, new TreeItem<>("Collision (After Effects)"));

                        for (int j = 0; j < copyListContainer[i].length; j++) {
                            newEntry.getChildren().get(i).getChildren().add(j, new TreeItem<>("Entry " +j));

                            bsaCollisionHashMap.put(newEntry.getChildren().get(i).getChildren().get(j), new BsaCollisionEntry((BsaCollisionEntry) copyListContainer[i][j]));
                        }
                    }
                    case "Collision Sound (After Effects)" -> {
                        newEntry.getChildren().add(i, new TreeItem<>("Collision Sound (After Effects)"));

                        for (int j = 0; j < copyListContainer[i].length; j++) {
                            newEntry.getChildren().get(i).getChildren().add(j, new TreeItem<>("Entry " +j));

                            bsaCollisionSoundHashMap.put(newEntry.getChildren().get(i).getChildren().get(j), new BsaCollisionSoundEntry((BsaCollisionSoundEntry) copyListContainer[i][j]));
                        }
                    }
                    case "BSA Entry Passing" -> {
                        newEntry.getChildren().add(i, new TreeItem<>("BSA Entry Passing"));

                        for (int j = 0; j < copyListContainer[i].length; j++) {
                            newEntry.getChildren().get(i).getChildren().add(j, new TreeItem<>("Entry " +j));

                            bsaType0HashMap.put(newEntry.getChildren().get(i).getChildren().get(j), new BsaType0Entry((BsaType0Entry) copyListContainer[i][j]));
                        }
                    }
                    case "Movement" -> {
                        newEntry.getChildren().add(i, new TreeItem<>("Movement"));

                        for (int j = 0; j < copyListContainer[i].length; j++) {
                            newEntry.getChildren().get(i).getChildren().add(j, new TreeItem<>("Entry " +j));

                            bsaType1HashMap.put(newEntry.getChildren().get(i).getChildren().get(j), new BsaType1Entry((BsaType1Entry) copyListContainer[i][j]));
                        }
                    }
                    case "Projectile Timeline Remap" -> {
                        newEntry.getChildren().add(i, new TreeItem<>("Projectile Timeline Remap"));

                        for (int j = 0; j < copyListContainer[i].length; j++) {
                            newEntry.getChildren().get(i).getChildren().add(j, new TreeItem<>("Entry " +j));

                            bsaType2HashMap.put(newEntry.getChildren().get(i).getChildren().get(j), new BsaType2Entry((BsaType2Entry) copyListContainer[i][j]));
                        }
                    }
                    case "Hitbox" -> {
                        newEntry.getChildren().add(i, new TreeItem<>("Hitbox"));

                        for (int j = 0; j < copyListContainer[i].length; j++) {
                            newEntry.getChildren().get(i).getChildren().add(j, new TreeItem<>("Entry " +j));

                            bsaType3HashMap.put(newEntry.getChildren().get(i).getChildren().get(j), new BsaType3Entry((BsaType3Entry) copyListContainer[i][j]));
                        }
                    }
                    case "Deflection" -> {
                        newEntry.getChildren().add(i, new TreeItem<>("Deflection"));

                        for (int j = 0; j < copyListContainer[i].length; j++) {
                            newEntry.getChildren().get(i).getChildren().add(j, new TreeItem<>("Entry " +j));

                            bsaType4HashMap.put(newEntry.getChildren().get(i).getChildren().get(j), new BsaType4Entry((BsaType4Entry) copyListContainer[i][j]));
                        }
                    }
                    case "Effect" -> {
                        newEntry.getChildren().add(i, new TreeItem<>("Effect"));

                        for (int j = 0; j < copyListContainer[i].length; j++) {
                            newEntry.getChildren().get(i).getChildren().add(j, new TreeItem<>("Entry " +j));

                            bsaType6HashMap.put(newEntry.getChildren().get(i).getChildren().get(j), new BsaType6Entry((BsaType6Entry) copyListContainer[i][j]));
                        }
                    }
                    case "Sound" -> {
                        newEntry.getChildren().add(i, new TreeItem<>("Sound"));

                        for (int j = 0; j < copyListContainer[i].length; j++) {
                            newEntry.getChildren().get(i).getChildren().add(j, new TreeItem<>("Entry " +j));

                            bsaType7HashMap.put(newEntry.getChildren().get(i).getChildren().get(j), new BsaType7Entry((BsaType7Entry) copyListContainer[i][j]));
                        }
                    }
                    case "Screen Effect" -> {
                        newEntry.getChildren().add(i, new TreeItem<>("Screen Effect"));

                        for (int j = 0; j < copyListContainer[i].length; j++) {
                            newEntry.getChildren().get(i).getChildren().add(j, new TreeItem<>("Entry " +j));

                            bsaType8HashMap.put(newEntry.getChildren().get(i).getChildren().get(j), new BsaType8Entry((BsaType8Entry) copyListContainer[i][j]));
                        }
                    }
                    case "BSA Type 10" -> {
                        newEntry.getChildren().add(i, new TreeItem<>("BSA Type 10"));

                        for (int j = 0; j < copyListContainer[i].length; j++) {
                            newEntry.getChildren().get(i).getChildren().add(j, new TreeItem<>("Entry " +j));

                            bsaType10HashMap.put(newEntry.getChildren().get(i).getChildren().get(j), new BsaType10Entry((BsaType10Entry) copyListContainer[i][j]));
                        }
                    }
                    case "Send Projectile Signal" -> {
                        newEntry.getChildren().add(i, new TreeItem<>("Send Projectile Signal"));

                        for (int j = 0; j < copyListContainer[i].length; j++) {
                            newEntry.getChildren().get(i).getChildren().add(j, new TreeItem<>("Entry " +j));

                            bsaType12HashMap.put(newEntry.getChildren().get(i).getChildren().get(j), new BsaType12Entry((BsaType12Entry) copyListContainer[i][j]));
                        }
                    }
                    case "Projectile Protection" -> {
                        newEntry.getChildren().add(i, new TreeItem<>("Projectile Protection"));

                        for (int j = 0; j < copyListContainer[i].length; j++) {
                            newEntry.getChildren().get(i).getChildren().add(j, new TreeItem<>("Entry " +j));

                            bsaType13HashMap.put(newEntry.getChildren().get(i).getChildren().get(j), new BsaType13Entry((BsaType13Entry) copyListContainer[i][j]));
                        }
                    }
                    case "Effect Placement" -> {
                        newEntry.getChildren().add(i, new TreeItem<>("Effect Placement"));

                        for (int j = 0; j < copyListContainer[i].length; j++) {
                            newEntry.getChildren().get(i).getChildren().add(j, new TreeItem<>("Entry " +j));

                            bsaType14HashMap.put(newEntry.getChildren().get(i).getChildren().get(j), new BsaType14Entry((BsaType14Entry) copyListContainer[i][j]));
                        }
                    }
                }
            }
        }
        else if (addItemCopy.getText().contains("List")) {
            if (addItemCopy.getText().contains("Collision Sound")) {
                for (int i = 0; i < copyListContainer[0].length; i++) {
                    TreeItem<String> newItem = addSubEntryItem(addSubEntry.getItems().get(1).getText());
                    bsaCollisionSoundHashMap.put(newItem, new BsaCollisionSoundEntry((BsaCollisionSoundEntry) copyListContainer[0][i]));
                }
            }
            else if (addItemCopy.getText().contains("Collision")) {
                for (int i = 0; i < copyListContainer[0].length; i++) {
                    TreeItem<String> newItem = addSubEntryItem(addSubEntry.getItems().get(0).getText());
                    bsaCollisionHashMap.put(newItem, new BsaCollisionEntry((BsaCollisionEntry) copyListContainer[0][i]));
                }
            }
            else if (addItemCopy.getText().contains("Entry")) {
                for (int i = 0; i < copyListContainer[0].length; i++) {
                    TreeItem<String> newItem = addSubEntryItem(addSubEntry.getItems().get(2).getText());
                    bsaType0HashMap.put(newItem, new BsaType0Entry((BsaType0Entry) copyListContainer[0][i]));
                }
            }
            else if (addItemCopy.getText().contains("Movement")) {
                for (int i = 0; i < copyListContainer[0].length; i++) {
                    TreeItem<String> newItem = addSubEntryItem(addSubEntry.getItems().get(3).getText());
                    bsaType1HashMap.put(newItem, new BsaType1Entry((BsaType1Entry) copyListContainer[0][i]));
                }
            }
            else if (addItemCopy.getText().contains("Timeline")) {
                for (int i = 0; i < copyListContainer[0].length; i++) {
                    TreeItem<String> newItem = addSubEntryItem(addSubEntry.getItems().get(4).getText());
                    bsaType2HashMap.put(newItem, new BsaType2Entry((BsaType2Entry) copyListContainer[0][i]));
                }
            }
            else if (addItemCopy.getText().contains("Hitbox")) {
                for (int i = 0; i < copyListContainer[0].length; i++) {
                    TreeItem<String> newItem = addSubEntryItem(addSubEntry.getItems().get(5).getText());
                    bsaType3HashMap.put(newItem, new BsaType3Entry((BsaType3Entry) copyListContainer[0][i]));
                }
            }
            else if (addItemCopy.getText().contains("Deflection")) {
                for (int i = 0; i < copyListContainer[0].length; i++) {
                    TreeItem<String> newItem = addSubEntryItem(addSubEntry.getItems().get(6).getText());
                    bsaType4HashMap.put(newItem, new BsaType4Entry((BsaType4Entry) copyListContainer[0][i]));
                }
            }
            else if (addItemCopy.getText().contains("Effect Placement")) {
                for (int i = 0; i < copyListContainer[0].length; i++) {
                    TreeItem<String> newItem = addSubEntryItem(addSubEntry.getItems().get(13).getText());
                    bsaType14HashMap.put(newItem, new BsaType14Entry((BsaType14Entry) copyListContainer[0][i]));
                }
            }
            else if (addItemCopy.getText().contains("Screen Effect")) {
                for (int i = 0; i < copyListContainer[0].length; i++) {
                    TreeItem<String> newItem = addSubEntryItem(addSubEntry.getItems().get(9).getText());
                    bsaType8HashMap.put(newItem, new BsaType8Entry((BsaType8Entry) copyListContainer[0][i]));
                }
            }
            else if (addItemCopy.getText().contains("Effect")) {
                for (int i = 0; i < copyListContainer[0].length; i++) {
                    TreeItem<String> newItem = addSubEntryItem(addSubEntry.getItems().get(7).getText());
                    bsaType6HashMap.put(newItem, new BsaType6Entry((BsaType6Entry) copyListContainer[0][i]));
                }
            }
            else if (addItemCopy.getText().contains("Sound")) {
                for (int i = 0; i < copyListContainer[0].length; i++) {
                    TreeItem<String> newItem = addSubEntryItem(addSubEntry.getItems().get(8).getText());
                    bsaType7HashMap.put(newItem, new BsaType7Entry((BsaType7Entry) copyListContainer[0][i]));
                }
            }
            else if (addItemCopy.getText().contains("Type 10")) {
                for (int i = 0; i < copyListContainer[0].length; i++) {
                    TreeItem<String> newItem = addSubEntryItem(addSubEntry.getItems().get(10).getText());
                    bsaType10HashMap.put(newItem, new BsaType10Entry((BsaType10Entry) copyListContainer[0][i]));
                }
            }
            else if (addItemCopy.getText().contains("Signal")) {
                for (int i = 0; i < copyListContainer[0].length; i++) {
                    TreeItem<String> newItem = addSubEntryItem(addSubEntry.getItems().get(11).getText());
                    bsaType12HashMap.put(newItem, new BsaType12Entry((BsaType12Entry) copyListContainer[0][i]));
                }
            }
            else if (addItemCopy.getText().contains("Protection")) {
                for (int i = 0; i < copyListContainer[0].length; i++) {
                    TreeItem<String> newItem = addSubEntryItem(addSubEntry.getItems().get(12).getText());
                    bsaType13HashMap.put(newItem, new BsaType13Entry((BsaType13Entry) copyListContainer[0][i]));
                }
            }
        }
        else {
            if (addItemCopy.getText().contains("Collision Sound")) {
                TreeItem<String> newItem = addSubEntryItem(addSubEntry.getItems().get(1).getText());
                bsaCollisionSoundHashMap.put(newItem, new BsaCollisionSoundEntry((BsaCollisionSoundEntry) copyContainer));
                treeView.getSelectionModel().select(newItem);
            }
            else if (addItemCopy.getText().contains("Collision")) {
                TreeItem<String> newItem = addSubEntryItem(addSubEntry.getItems().get(0).getText());
                bsaCollisionHashMap.put(newItem, new BsaCollisionEntry((BsaCollisionEntry) copyContainer));
                treeView.getSelectionModel().select(newItem);
            }
            else if (addItemCopy.getText().contains("Entry")) {
                TreeItem<String> newItem = addSubEntryItem(addSubEntry.getItems().get(2).getText());
                bsaType0HashMap.put(newItem, new BsaType0Entry((BsaType0Entry) copyContainer));
                treeView.getSelectionModel().select(newItem);
            }
            else if (addItemCopy.getText().contains("Movement")) {
                TreeItem<String> newItem = addSubEntryItem(addSubEntry.getItems().get(3).getText());
                bsaType1HashMap.put(newItem, new BsaType1Entry((BsaType1Entry) copyContainer));
                treeView.getSelectionModel().select(newItem);
            }
            else if (addItemCopy.getText().contains("Timeline")) {
                TreeItem<String> newItem = addSubEntryItem(addSubEntry.getItems().get(4).getText());
                bsaType2HashMap.put(newItem, new BsaType2Entry((BsaType2Entry) copyContainer));
                treeView.getSelectionModel().select(newItem);
            }
            else if (addItemCopy.getText().contains("Hitbox")) {
                TreeItem<String> newItem = addSubEntryItem(addSubEntry.getItems().get(5).getText());
                bsaType3HashMap.put(newItem, new BsaType3Entry((BsaType3Entry) copyContainer));
                treeView.getSelectionModel().select(newItem);
            }
            else if (addItemCopy.getText().contains("Deflection")) {
                TreeItem<String> newItem = addSubEntryItem(addSubEntry.getItems().get(6).getText());
                bsaType4HashMap.put(newItem, new BsaType4Entry((BsaType4Entry) copyContainer));
                treeView.getSelectionModel().select(newItem);
            }
            else if (addItemCopy.getText().contains("Effect Placement")) {
                TreeItem<String> newItem = addSubEntryItem(addSubEntry.getItems().get(13).getText());
                bsaType14HashMap.put(newItem, new BsaType14Entry((BsaType14Entry) copyContainer));
                treeView.getSelectionModel().select(newItem);
            }
            else if (addItemCopy.getText().contains("Screen Effect")) {
                TreeItem<String> newItem = addSubEntryItem(addSubEntry.getItems().get(9).getText());
                bsaType8HashMap.put(newItem, new BsaType8Entry((BsaType8Entry) copyContainer));
                treeView.getSelectionModel().select(newItem);
            }
            else if (addItemCopy.getText().contains("Effect")) {
                TreeItem<String> newItem = addSubEntryItem(addSubEntry.getItems().get(7).getText());
                bsaType6HashMap.put(newItem, new BsaType6Entry((BsaType6Entry) copyContainer));
                treeView.getSelectionModel().select(newItem);
            }
            else if (addItemCopy.getText().contains("Sound")) {
                TreeItem<String> newItem = addSubEntryItem(addSubEntry.getItems().get(8).getText());
                bsaType7HashMap.put(newItem, new BsaType7Entry((BsaType7Entry) copyContainer));
                treeView.getSelectionModel().select(newItem);
            }
            else if (addItemCopy.getText().contains("Type 10")) {
                TreeItem<String> newItem = addSubEntryItem(addSubEntry.getItems().get(10).getText());
                bsaType10HashMap.put(newItem, new BsaType10Entry((BsaType10Entry) copyContainer));
                treeView.getSelectionModel().select(newItem);
            }
            else if (addItemCopy.getText().contains("Signal")) {
                TreeItem<String> newItem = addSubEntryItem(addSubEntry.getItems().get(11).getText());
                bsaType12HashMap.put(newItem, new BsaType12Entry((BsaType12Entry) copyContainer));
                treeView.getSelectionModel().select(newItem);
            }
            else if (addItemCopy.getText().contains("Protection")) {
                TreeItem<String> newItem = addSubEntryItem(addSubEntry.getItems().get(12).getText());
                bsaType13HashMap.put(newItem, new BsaType13Entry((BsaType13Entry) copyContainer));
                treeView.getSelectionModel().select(newItem);
            }
        }
    }

    private void Delete() {
        if (currentEntry.getParent() == treeView.getRoot()) {
            bsaMainHashMap.remove(currentEntry);

            for (TreeItem<String> getParent : grandParentEntry.getChildren()) {
                switch (getParent.getValue()) {
                    case "Collision (After Effects)" -> {
                        for (TreeItem<String> child : getParent.getChildren()) {
                            bsaCollisionHashMap.remove(child);
                        }
                    }
                    case "Collision Sound (After Effects)" -> {
                        for (TreeItem<String> child : getParent.getChildren()) {
                            bsaCollisionSoundHashMap.remove(child);
                        }
                    }
                    case "BSA Entry Passing" -> {
                        for (TreeItem<String> child : getParent.getChildren()) {
                            bsaType0HashMap.remove(child);
                        }
                    }
                    case "Movement" -> {
                        for (TreeItem<String> child : getParent.getChildren()) {
                            bsaType1HashMap.remove(child);
                        }
                    }
                    case "Projectile Timeline Remap" -> {
                        for (TreeItem<String> child : getParent.getChildren()) {
                            bsaType2HashMap.remove(child);
                        }
                    }
                    case "Hitbox" -> {
                        for (TreeItem<String> child : getParent.getChildren()) {
                            bsaType3HashMap.remove(child);
                        }
                    }
                    case "Deflection" -> {
                        for (TreeItem<String> child : getParent.getChildren()) {
                            bsaType4HashMap.remove(child);
                        }
                    }
                    case "Effect" -> {
                        for (TreeItem<String> child : getParent.getChildren()) {
                            bsaType6HashMap.remove(child);
                        }
                    }
                    case "Sound" -> {
                        for (TreeItem<String> child : getParent.getChildren()) {
                            bsaType7HashMap.remove(child);
                        }
                    }
                    case "Screen Effect" -> {
                        for (TreeItem<String> child : getParent.getChildren()) {
                            bsaType8HashMap.remove(child);
                        }
                    }
                    case "BSA Type 10" -> {
                        for (TreeItem<String> child : getParent.getChildren()) {
                            bsaType10HashMap.remove(child);
                        }
                    }
                    case "Send Projectile Signal" -> {
                        for (TreeItem<String> child : getParent.getChildren()) {
                            bsaType12HashMap.remove(child);
                        }
                    }
                    case "Projectile Protection" -> {
                        for (TreeItem<String> child : getParent.getChildren()) {
                            bsaType13HashMap.remove(child);
                        }
                    }
                    case "Effect Placement" -> {
                        for (TreeItem<String> child : getParent.getChildren()) {
                            bsaType14HashMap.remove(child);
                        }
                    }
                }
            }

            if (grandParentEntry.nextSibling() != null) {
                for (int i = treeView.getRoot().getChildren().indexOf(grandParentEntry.nextSibling()); i < treeView.getRoot().getChildren().size(); i++) {

                    int entryIndex = Integer.parseInt(treeView.getRoot().getChildren().get(i).getValue().toString().replaceAll("\\D+", ""));

                    treeView.getRoot().getChildren().get(i).setValue("Entry " + (entryIndex - 1));
                }
            }
            
            treeView.getRoot().getChildren().remove(grandParentEntry);

            allEntries--;
        }
        else if (currentEntry.getChildren().isEmpty() && currentEntry.getValue().startsWith("Entry")) {
            TreeItem<String> getParent = currentEntry.getParent();

            switch (currentEntry.getParent().getValue()) {
                case "Collision (After Effects)" -> {
                    bsaCollisionHashMap.remove(currentEntry);

                    getParent.getChildren().remove(currentEntry);
                    
                    for (int i = 0; i < getParent.getChildren().size(); i++) {
                        getParent.getChildren().get(i).setValue("Entry " + i);
                    }

                    if (!getParent.getChildren().isEmpty()) {
                        treeView.getSelectionModel().select(getParent.getChildren().getFirst());
                    }
                }
                case "Collision Sound (After Effects)" -> {
                    bsaCollisionSoundHashMap.remove(currentEntry);

                    getParent.getChildren().remove(currentEntry);
                    
                    for (int i = 0; i < getParent.getChildren().size(); i++) {
                        getParent.getChildren().get(i).setValue("Entry " + i);
                    }

                    if (!getParent.getChildren().isEmpty()) {
                        treeView.getSelectionModel().select(getParent.getChildren().getFirst());
                    }
                }
                case "BSA Entry Passing" -> {
                    bsaType0HashMap.remove(currentEntry);

                    getParent.getChildren().remove(currentEntry);
                    
                    for (int i = 0; i < getParent.getChildren().size(); i++) {
                        getParent.getChildren().get(i).setValue("Entry " + i);
                    }

                    if (!getParent.getChildren().isEmpty()) {
                        treeView.getSelectionModel().select(getParent.getChildren().getFirst());
                    }
                }
                case "Movement" -> {
                    bsaType1HashMap.remove(currentEntry);

                    getParent.getChildren().remove(currentEntry);
                    
                    for (int i = 0; i < getParent.getChildren().size(); i++) {
                        getParent.getChildren().get(i).setValue("Entry " + i);
                    }

                    if (!getParent.getChildren().isEmpty()) {
                        treeView.getSelectionModel().select(getParent.getChildren().getFirst());
                    }
                }
                case "Projectile Timeline Remap" -> {
                    bsaType2HashMap.remove(currentEntry);

                    getParent.getChildren().remove(currentEntry);
                    
                    for (int i = 0; i < getParent.getChildren().size(); i++) {
                        getParent.getChildren().get(i).setValue("Entry " + i);
                    }

                    if (!getParent.getChildren().isEmpty()) {
                        treeView.getSelectionModel().select(getParent.getChildren().getFirst());
                    } 
                }
                case "Hitbox" -> {
                    bsaType3HashMap.remove(currentEntry);

                    getParent.getChildren().remove(currentEntry);
                    
                    for (int i = 0; i < getParent.getChildren().size(); i++) {
                        getParent.getChildren().get(i).setValue("Entry " + i);
                    }

                    if (!getParent.getChildren().isEmpty()) {
                        treeView.getSelectionModel().select(getParent.getChildren().getFirst());
                    };
                }
                case "Deflection" -> {
                    bsaType4HashMap.remove(currentEntry);

                    getParent.getChildren().remove(currentEntry);
                    
                    for (int i = 0; i < getParent.getChildren().size(); i++) {
                        getParent.getChildren().get(i).setValue("Entry " + i);
                    }

                    if (!getParent.getChildren().isEmpty()) {
                        treeView.getSelectionModel().select(getParent.getChildren().getFirst());
                    }
                }
                case "Effect" -> {
                    bsaType6HashMap.remove(currentEntry);

                    getParent.getChildren().remove(currentEntry);
                    
                    for (int i = 0; i < getParent.getChildren().size(); i++) {
                        getParent.getChildren().get(i).setValue("Entry " + i);
                    }

                    if (!getParent.getChildren().isEmpty()) {
                        treeView.getSelectionModel().select(getParent.getChildren().getFirst());
                    }
                }
                case "Sound" -> {
                    bsaType7HashMap.remove(currentEntry);

                    getParent.getChildren().remove(currentEntry);
                    
                    for (int i = 0; i < getParent.getChildren().size(); i++) {
                        getParent.getChildren().get(i).setValue("Entry " + i);
                    }

                    if (!getParent.getChildren().isEmpty()) {
                        treeView.getSelectionModel().select(getParent.getChildren().getFirst());
                    }
                }
                case "Screen Effect" -> {
                    bsaType8HashMap.remove(currentEntry);

                    getParent.getChildren().remove(currentEntry);
                    
                    for (int i = 0; i < getParent.getChildren().size(); i++) {
                        getParent.getChildren().get(i).setValue("Entry " + i);
                    }

                    if (!getParent.getChildren().isEmpty()) {
                        treeView.getSelectionModel().select(getParent.getChildren().getFirst());
                    }
                }
                case "BSA Type 10" -> {
                    bsaType10HashMap.remove(currentEntry);

                    getParent.getChildren().remove(currentEntry);
                    
                    for (int i = 0; i < getParent.getChildren().size(); i++) {
                        getParent.getChildren().get(i).setValue("Entry " + i);
                    }

                    if (!getParent.getChildren().isEmpty()) {
                        treeView.getSelectionModel().select(getParent.getChildren().getFirst());
                    }
                }
                case "Send Projectile Signal" -> {
                    bsaType12HashMap.remove(currentEntry);

                    getParent.getChildren().remove(currentEntry);
                    
                    for (int i = 0; i < getParent.getChildren().size(); i++) {
                        getParent.getChildren().get(i).setValue("Entry " + i);
                    }

                    if (!getParent.getChildren().isEmpty()) {
                        treeView.getSelectionModel().select(getParent.getChildren().getFirst());
                    };
                }
                case "Projectile Protection" -> {
                    bsaType13HashMap.remove(currentEntry);

                    getParent.getChildren().remove(currentEntry);
                    
                    for (int i = 0; i < getParent.getChildren().size(); i++) {
                        getParent.getChildren().get(i).setValue("Entry " + i);
                    }

                    if (!getParent.getChildren().isEmpty()) {
                        treeView.getSelectionModel().select(getParent.getChildren().getFirst());
                    }
                }
                case "Effect Placement" -> {
                    bsaType14HashMap.remove(currentEntry);

                    getParent.getChildren().remove(currentEntry);
                    
                    for (int i = 0; i < getParent.getChildren().size(); i++) {
                        getParent.getChildren().get(i).setValue("Entry " + i);
                    }

                    if (!getParent.getChildren().isEmpty()) {
                        treeView.getSelectionModel().select(getParent.getChildren().getFirst());
                    }
                }
            }
        }
        else {
            TreeItem<String> getParent = currentEntry;

            switch (currentEntry.getValue()) {
                case "Collision (After Effects)" -> {
                    for (TreeItem<String> child : getParent.getChildren()) {
                        bsaCollisionHashMap.remove(child);
                    }
                    getParent.getChildren().removeAll(getParent.getChildren());
                    getParent.getParent().getChildren().remove(getParent);
                }
                case "Collision Sound (After Effects)" -> {
                    for (TreeItem<String> child : getParent.getChildren()) {
                        bsaCollisionSoundHashMap.remove(child);
                    }
                    getParent.getChildren().removeAll(getParent.getChildren());
                    getParent.getParent().getChildren().remove(getParent);
                }
                case "BSA Entry Passing" -> {
                    for (TreeItem<String> child : getParent.getChildren()) {
                        bsaType0HashMap.remove(child);
                    }
                    getParent.getChildren().removeAll(getParent.getChildren());
                    getParent.getParent().getChildren().remove(getParent);
                }
                case "Movement" -> {
                    for (TreeItem<String> child : getParent.getChildren()) {
                        bsaType1HashMap.remove(child);
                    }
                    getParent.getChildren().removeAll(getParent.getChildren());
                    getParent.getParent().getChildren().remove(getParent);
                }
                case "Projectile Timeline Remap" -> {
                    for (TreeItem<String> child : getParent.getChildren()) {
                        bsaType2HashMap.remove(child);
                    }
                    getParent.getChildren().removeAll(getParent.getChildren());
                    getParent.getParent().getChildren().remove(getParent);
                }
                case "Hitbox" -> {
                    for (TreeItem<String> child : getParent.getChildren()) {
                        bsaType3HashMap.remove(child);
                    }
                    getParent.getChildren().removeAll(getParent.getChildren());
                    getParent.getParent().getChildren().remove(getParent);
                }
                case "Deflection" -> {
                    for (TreeItem<String> child : getParent.getChildren()) {
                        bsaType4HashMap.remove(child);
                    }
                    getParent.getChildren().removeAll(getParent.getChildren());
                    getParent.getParent().getChildren().remove(getParent);
                }
                case "Effect" -> {
                    for (TreeItem<String> child : getParent.getChildren()) {
                        bsaType6HashMap.remove(child);
                    }
                    getParent.getChildren().removeAll(getParent.getChildren());
                    getParent.getParent().getChildren().remove(getParent);
                }
                case "Sound" -> {
                    for (TreeItem<String> child : getParent.getChildren()) {
                        bsaType7HashMap.remove(child);
                    }
                    getParent.getChildren().removeAll(getParent.getChildren());
                    getParent.getParent().getChildren().remove(getParent);
                }
                case "Screen Effect" -> {
                    for (TreeItem<String> child : getParent.getChildren()) {
                        bsaType8HashMap.remove(child);
                    }
                    getParent.getChildren().removeAll(getParent.getChildren());
                    getParent.getParent().getChildren().remove(getParent);
                }
                case "BSA Type 10" -> {
                    for (TreeItem<String> child : getParent.getChildren()) {
                        bsaType10HashMap.remove(child);
                    }
                    getParent.getChildren().removeAll(getParent.getChildren());
                    getParent.getParent().getChildren().remove(getParent);
                }
                case "Send Projectile Signal" -> {
                    for (TreeItem<String> child : getParent.getChildren()) {
                        bsaType12HashMap.remove(child);
                    }
                    getParent.getChildren().removeAll(getParent.getChildren());
                    getParent.getParent().getChildren().remove(getParent);
                }
                case "Projectile Protection" -> {
                    for (TreeItem<String> child : getParent.getChildren()) {
                        bsaType13HashMap.remove(child);
                    }
                    getParent.getChildren().removeAll(getParent.getChildren());
                    getParent.getParent().getChildren().remove(getParent);
                }
                case "Effect Placement" -> {
                    for (TreeItem<String> child : getParent.getChildren()) {
                        bsaType14HashMap.remove(child);
                    }
                    getParent.getChildren().removeAll(getParent.getChildren());
                    getParent.getParent().getChildren().remove(getParent);
                }
            }
        }
    }

    private void sortTreeItems(TreeItem<String> treeItem) {
        List<String> bsaTypesList = Arrays.asList(
            "Collision (After Effects)",
            "Collision Sound (After Effects)",
            "BSA Entry Passing",
            "Movement",
            "Projectile Timeline Remap",
            "Hitbox",
            "Deflection",
            "Effect",
            "Sound",
            "Screen Effect",
            "BSA Type 10",
            "Send Projectile Signal",
            "Projectile Protection",
            "Effect Placement"
        );

        treeItem.getChildren().sort((item1, item2) -> {
            int index1 = bsaTypesList.indexOf(item1.getValue());
            int index2 = bsaTypesList.indexOf(item2.getValue());
            
            return Integer.compare(index1, index2);
        });
    }

    private void copyEntryItem(HashMap<TreeItem<String>, ?> hashMap, String text, int i) {
        for (int j = 0; j < currentEntry.getChildren().get(i).getChildren().size(); j++ ) {
            copyListContainer[i][j] = hashMap.get(currentEntry.getChildren().get(i).getChildren().get(j));
        }
        copyTypesContainer[i] = text;
    }

    private void copyListItem(HashMap<TreeItem<String>, ?> hashMap) {
        for (TreeItem<String> child : currentEntry.getChildren()) {
            copyListContainer[0][currentEntry.getChildren().indexOf(child)] = hashMap.get(child);
        }
    }

    private void copyChildItem(HashMap<TreeItem<String>, ?> hashMap) {
        copyContainer = hashMap.get(currentEntry);
    }

    private TreeItem<String> addSubEntryItem(String stringTarget) {
        boolean hasItem = false;
        TreeItem<String> itemIndex = new TreeItem<>();
        TreeItem<String> newChild;
        
        for (TreeItem<String> child : grandParentEntry.getChildren()) {
            if (child.getValue().equals(stringTarget)) {
                hasItem = true;
                itemIndex = child;
            }
        }

        if (hasItem) {
            newChild = new TreeItem<>("Entry " + itemIndex.getChildren().size());

            itemIndex.getChildren().add(newChild);
        } else {
            grandParentEntry.getChildren().add(0, new TreeItem<>(stringTarget));

            newChild = new TreeItem<>("Entry " + 0);

            grandParentEntry.getChildren().get(0).getChildren().add(newChild);

            sortTreeItems(grandParentEntry);
        }

        return newChild;
    }

    private void setContextMenuText(String text) {
        copiedItem.setText("Copied " + text);
        pasteItem.setText("Paste " + text + " Ctrl+V");
        addItemCopy.setText("Add " + text + " Copy Ctrl+A");
    }

    public void bsaReader(Path path) {
        try(FileChannel channel = FileChannel.open(path, StandardOpenOption.READ)) {
            short bsaEntries;
            int offset;
            int entryOffset;
            int collisionOffset;
            int expirationOffset;
            int typesOffset;
            short typesCount;
            short type;
            short typeCount;
            int hdrOffset;
            int dataOffset;
            int collisionEntriesCount;
            int expirationEntriesCount;

            ByteBuffer byteBuffer = ByteBuffer.allocate(1).order(ByteOrder.LITTLE_ENDIAN);
            ByteBuffer shortBuffer = ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN);
            ByteBuffer intBuffer = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
            
            channel.position(18);
            shortBuffer.clear();
            channel.read(shortBuffer);
            shortBuffer.flip();
            bsaEntries = shortBuffer.getShort();
            allEntries = bsaEntries;

            if (bsaEntries > 0) {
                treeView.setRoot(new TreeItem<>("dummy"));
                treeView.setShowRoot(false);
            }

            channel.position(20);
            intBuffer.clear();
            channel.read(intBuffer);
            intBuffer.flip();
            offset = intBuffer.getInt();

            int mainIndex = 0;

            for (int i = 0; i < bsaEntries; i++) {
                int index = 0;

                channel.position(offset + i * 4);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                entryOffset = intBuffer.getInt();

                if(entryOffset != 0) {
                    treeView.getRoot().getChildren().add(new TreeItem<>("Entry " + i));

                    BsaMainEntry bsaMainEntry = new BsaMainEntry();
                    bsaMainHashMap.put(treeView.getRoot().getChildren().get(mainIndex), bsaMainEntry);

                    channel.position(entryOffset);
                    intBuffer.clear();
                    channel.read(intBuffer);
                    intBuffer.flip();
                    bsaMainHashMap.get(treeView.getRoot().getChildren().get(mainIndex)).i00 = intBuffer.getInt();

                    channel.position(entryOffset + 4);
                    shortBuffer.clear();
                    channel.read(shortBuffer);
                    shortBuffer.flip();
                    collisionEntriesCount = toUShort(shortBuffer.getShort());
                    
                    channel.position(entryOffset + 6);
                    shortBuffer.clear();
                    channel.read(shortBuffer);
                    shortBuffer.flip();
                    expirationEntriesCount = toUShort(shortBuffer.getShort());

                    channel.position(entryOffset + 8);
                    intBuffer.clear();
                    channel.read(intBuffer);
                    intBuffer.flip();
                    collisionOffset = intBuffer.getInt();
                    collisionOffset += entryOffset;

                    if (collisionEntriesCount > 0) {
                        treeView.getRoot().getChildren().get(mainIndex).getChildren().add(new TreeItem<>("Collision (After Effects)"));

                        for (int j = 0; j < collisionEntriesCount; j++) {
                            BsaCollisionEntry bsaCollisionEntry = new BsaCollisionEntry();

                            treeView.getRoot().getChildren().get(mainIndex).getChildren().get(index).getChildren().add(new TreeItem<>("Entry "+ j));

                            bsaCollisionHashMap.put(treeView.getRoot().getChildren().get(mainIndex).getChildren().get(index).getChildren().get(j), bsaCollisionEntry);

                            channel.position(collisionOffset + j * 24);
                            shortBuffer.clear();
                            channel.read(shortBuffer);
                            shortBuffer.flip();
                            bsaCollisionEntry.eepkType = toUShort(shortBuffer.getShort());

                            channel.position(collisionOffset + j * 24 + 2);
                            shortBuffer.clear();
                            channel.read(shortBuffer);
                            shortBuffer.flip();
                            bsaCollisionEntry.skillId = toUShort(shortBuffer.getShort());

                            channel.position(collisionOffset + j * 24 + 4);
                            shortBuffer.clear();
                            channel.read(shortBuffer);
                            shortBuffer.flip();
                            bsaCollisionEntry.effectId = toUShort(shortBuffer.getShort());

                            channel.position(collisionOffset + j * 24 + 6);
                            shortBuffer.clear();
                            channel.read(shortBuffer);
                            shortBuffer.flip();
                            bsaCollisionEntry.i06 = toUShort(shortBuffer.getShort());

                            channel.position(collisionOffset + j * 24 + 8);
                            intBuffer.clear();
                            channel.read(intBuffer);
                            intBuffer.flip();
                            bsaCollisionEntry.i08 = intBuffer.getInt();

                            channel.position(collisionOffset + j * 24 + 12);
                            intBuffer.clear();
                            channel.read(intBuffer);
                            intBuffer.flip();
                            bsaCollisionEntry.i12 = intBuffer.getInt();

                            channel.position(collisionOffset + j * 24 + 16);
                            intBuffer.clear();
                            channel.read(intBuffer);
                            intBuffer.flip();
                            bsaCollisionEntry.i16 = intBuffer.getInt();

                            channel.position(collisionOffset+ j * 24 + 20);
                            intBuffer.clear();
                            channel.read(intBuffer);
                            intBuffer.flip();
                            bsaCollisionEntry.i20 = intBuffer.getInt();
                        }

                        index++;
                    } 

                    channel.position(entryOffset + 12);
                    intBuffer.clear();
                    channel.read(intBuffer);
                    intBuffer.flip();
                    expirationOffset = intBuffer.getInt();
                    expirationOffset += entryOffset;

                    if (expirationEntriesCount > 0) {
                        treeView.getRoot().getChildren().get(mainIndex).getChildren().add(new TreeItem<>("Collision Sound (After Effects)"));

                        for (int j = 0; j < expirationEntriesCount; j++) {
                            BsaCollisionSoundEntry bsaCollisionSoundEntry = new BsaCollisionSoundEntry();

                            treeView.getRoot().getChildren().get(mainIndex).getChildren().get(index).getChildren().add(new TreeItem<>("Entry " + j));

                            bsaCollisionSoundHashMap.put(treeView.getRoot().getChildren().get(mainIndex).getChildren().get(index).getChildren().get(j), bsaCollisionSoundEntry);
                            
                            channel.position(expirationOffset + j * 8);
                            shortBuffer.clear();
                            channel.read(shortBuffer);
                            shortBuffer.flip();
                            bsaCollisionSoundEntry.acbType = toUShort(shortBuffer.getShort());

                            channel.position(expirationOffset + j * 8 + 2);
                            shortBuffer.clear();
                            channel.read(shortBuffer);
                            shortBuffer.flip();
                            bsaCollisionSoundEntry.i02 = toUShort(shortBuffer.getShort());

                            channel.position(expirationOffset + j * 8 + 4);
                            shortBuffer.clear();
                            channel.read(shortBuffer);
                            shortBuffer.flip();
                            bsaCollisionSoundEntry.cueId = toUShort(shortBuffer.getShort());

                            channel.position(expirationOffset + j * 8 + 6);
                            shortBuffer.clear();
                            channel.read(shortBuffer);
                            shortBuffer.flip();
                            bsaCollisionSoundEntry.i06 = toUShort(shortBuffer.getShort());
                        }

                        index++;
                    } 

                    channel.position(entryOffset + 16);
                    byteBuffer.clear();
                    channel.read(byteBuffer);
                    byteBuffer.flip();
                    bsaMainEntry.i16_a = (byte) (byteBuffer.get() & 0x0F);
                    byteBuffer.flip();
                    bsaMainEntry.i16_b = (byte)((byteBuffer.get() >> 4) & 0x0F);

                    channel.position(entryOffset + 17);
                    byteBuffer.clear();
                    channel.read(byteBuffer);
                    byteBuffer.flip();
                    bsaMainEntry.i17 = toUByte(byteBuffer.get());

                    channel.position(entryOffset + 18);
                    intBuffer.clear();
                    channel.read(intBuffer);
                    intBuffer.flip();
                    bsaMainEntry.i18 = intBuffer.getInt();

                    channel.position(entryOffset + 22);
                    shortBuffer.clear();
                    channel.read(shortBuffer);
                    shortBuffer.flip();
                    bsaMainEntry.lifetime = toUShort(shortBuffer.getShort());

                    channel.position(entryOffset + 24);
                    shortBuffer.clear();
                    channel.read(shortBuffer);
                    shortBuffer.flip();
                    bsaMainEntry.i24 = toUShort(shortBuffer.getShort());

                    channel.position(entryOffset + 26);
                    shortBuffer.clear();
                    channel.read(shortBuffer);
                    shortBuffer.flip();
                    bsaMainEntry.expires = toUShort(shortBuffer.getShort());

                    channel.position(entryOffset + 28);
                    shortBuffer.clear();
                    channel.read(shortBuffer);
                    shortBuffer.flip();
                    bsaMainEntry.impactProjectile = toUShort(shortBuffer.getShort());

                    channel.position(entryOffset + 30);
                    shortBuffer.clear();
                    channel.read(shortBuffer);
                    shortBuffer.flip();
                    bsaMainEntry.impactEnemy = toUShort(shortBuffer.getShort());

                    channel.position(entryOffset + 32);
                    shortBuffer.clear();
                    channel.read(shortBuffer);
                    shortBuffer.flip();
                    bsaMainEntry.impactGround = toUShort(shortBuffer.getShort());

                    channel.position(entryOffset + 34);
                    shortBuffer.clear();
                    channel.read(shortBuffer);
                    shortBuffer.flip();
                    typesCount = shortBuffer.getShort();

                    channel.position(entryOffset + 36);
                    intBuffer.clear();
                    channel.read(intBuffer);
                    intBuffer.flip();
                    typesOffset = intBuffer.getInt();
                    typesOffset += entryOffset;

                    channel.position(entryOffset + 40);
                    intBuffer.clear();
                    channel.read(intBuffer);
                    intBuffer.flip();
                    bsaMainEntry.i40 = intBuffer.getInt();

                    channel.position(entryOffset + 44);
                    intBuffer.clear();
                    channel.read(intBuffer);
                    intBuffer.flip();
                    bsaMainEntry.i44 = intBuffer.getInt();

                    channel.position(entryOffset + 48);
                    intBuffer.clear();
                    channel.read(intBuffer);
                    intBuffer.flip();
                    bsaMainEntry.i48 = intBuffer.getInt();

                    for (int j = 0; j < typesCount; j++) {
                        channel.position(typesOffset + j * 16);
                        shortBuffer.clear();
                        channel.read(shortBuffer);
                        shortBuffer.flip();
                        type = shortBuffer.getShort();

                        channel.position(typesOffset + j * 16 + 6);
                        shortBuffer.clear();
                        channel.read(shortBuffer);
                        shortBuffer.flip();
                        typeCount = shortBuffer.getShort();

                        channel.position(typesOffset + j * 16+ 8);
                        intBuffer.clear();
                        channel.read(intBuffer);
                        intBuffer.flip();
                        hdrOffset = intBuffer.getInt();

                        channel.position(typesOffset + j * 16 + 12);
                        intBuffer.clear();
                        channel.read(intBuffer);
                        intBuffer.flip();
                        dataOffset = intBuffer.getInt();

                        switch (type) {
                            case 0 -> {
                                treeView.getRoot().getChildren().get(mainIndex).getChildren().add(new TreeItem<>("BSA Entry Passing"));

                                for (int k = 0; k < typeCount; k++) {
                                    BsaType0Entry bsaType0Entry = new BsaType0Entry();

                                    treeView.getRoot().getChildren().get(mainIndex).getChildren().get(index).getChildren().add(new TreeItem<>("Entry " + k));

                                    bsaType0HashMap.put(treeView.getRoot().getChildren().get(mainIndex).getChildren().get(index).getChildren().get(k), bsaType0Entry);

                                    channel.position(typesOffset + hdrOffset + j * 16 + k * 4);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType0Entry.startTime = toUShort(shortBuffer.getShort());

                                    channel.position(typesOffset + hdrOffset + j * 16 + k * 4 + 2);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType0Entry.duration = toUShort((short)(shortBuffer.getShort()- bsaType0Entry.startTime));
                                    
                                    channel.position(typesOffset + dataOffset + j * 16 + k * 16);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType0Entry.i00 = shortBuffer.getShort();
                                    
                                    channel.position(typesOffset + dataOffset + j * 16 + k * 16 + 2);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType0Entry.mainCondition = toUShort(shortBuffer.getShort());

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 16 + 4);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType0Entry.bsaEntryId = toUShort(shortBuffer.getShort());

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 16 + 6);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType0Entry.i06 = shortBuffer.getShort();
    
                                    channel.position(typesOffset + dataOffset + j * 16 + k * 16 + 8);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType0Entry.bacCondition = intBuffer.getFloat();

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 16 + 12);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType0Entry.f12 = intBuffer.getFloat();
                                }

                                index++;
                            }
                            case 1 -> {
                                treeView.getRoot().getChildren().get(mainIndex).getChildren().add(new TreeItem<>("Movement"));

                                for (int k = 0; k < typeCount; k++) {
                                    BsaType1Entry bsaType1Entry = new BsaType1Entry();

                                    treeView.getRoot().getChildren().get(mainIndex).getChildren().get(index).getChildren().add(new TreeItem<>("Entry " + k));

                                    bsaType1HashMap.put(treeView.getRoot().getChildren().get(mainIndex).getChildren().get(index).getChildren().get(k), bsaType1Entry);

                                    channel.position(typesOffset + hdrOffset + j * 16 + k * 4);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType1Entry.startTime = toUShort(shortBuffer.getShort());

                                    channel.position(typesOffset + hdrOffset + j * 16 + k * 4 + 2);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType1Entry.duration = toUShort((short)(shortBuffer.getShort() - bsaType1Entry.startTime));

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 48);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType1Entry.motionFlags = toUint32(intBuffer.getInt());

                                    channel.position(typesOffset + dataOffset + j * 16+ k * 48 + 4);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType1Entry.speedZ = intBuffer.getFloat();

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 48 + 8);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType1Entry.speedX = intBuffer.getFloat();

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 48 + 12);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType1Entry.speedY = intBuffer.getFloat();

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 48 + 16);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType1Entry.f16 = intBuffer.getFloat();

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 48 + 20);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType1Entry.accelerationZ = intBuffer.getFloat();

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 48 + 24);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType1Entry.accelerationX = intBuffer.getFloat();

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 48 + 28);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType1Entry.accelerationY = intBuffer.getFloat();

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 48 + 32);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType1Entry.fallofStrength = intBuffer.getFloat();

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 48 + 36);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType1Entry.spreadDirectionX = intBuffer.getFloat();

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 48 + 40);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType1Entry.spreadDirectionY = intBuffer.getFloat();

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 48 + 44);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType1Entry.spreadDirectionZ = intBuffer.getFloat();
                                }

                                index++;
                            }
                            case 2 -> {
                                treeView.getRoot().getChildren().get(mainIndex).getChildren().add(new TreeItem<>("Projectile Timeline Remap"));

                                for (int k = 0; k < typeCount; k++) {
                                    BsaType2Entry bsaType2Entry = new BsaType2Entry();

                                    treeView.getRoot().getChildren().get(mainIndex).getChildren().get(index).getChildren().add(new TreeItem<>("Entry " + k));

                                    bsaType2HashMap.put(treeView.getRoot().getChildren().get(mainIndex).getChildren().get(index).getChildren().get(k), bsaType2Entry);
                                    
                                    channel.position(typesOffset + hdrOffset + j * 16 + k * 4);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType2Entry.startTime = toUShort(shortBuffer.getShort());

                                    channel.position(typesOffset + hdrOffset + j * 16 + k * 4 + 2);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType2Entry.duration = toUShort((short)(shortBuffer.getShort() - bsaType2Entry.startTime));

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 8);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType2Entry.i00 = shortBuffer.getShort();

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 8 + 2);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType2Entry.outputStartFrame = shortBuffer.getShort();

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 8 + 4);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType2Entry.outputEndFrame = shortBuffer.getShort();

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 8 + 6);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType2Entry.i06 = shortBuffer.getShort();
                                }

                                index++;
                            }
                            case 3 -> {
                                treeView.getRoot().getChildren().get(mainIndex).getChildren().add(new TreeItem<>("Hitbox"));

                                for (int k = 0; k < typeCount; k++) {
                                    BsaType3Entry bsaType3Entry = new BsaType3Entry();

                                    treeView.getRoot().getChildren().get(mainIndex).getChildren().get(index).getChildren().add(new TreeItem<>("Entry " + k));

                                    bsaType3HashMap.put(treeView.getRoot().getChildren().get(mainIndex).getChildren().get(index).getChildren().get(k), bsaType3Entry);

                                    channel.position(typesOffset + hdrOffset + j * 16 + k  *4);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType3Entry.startTime = toUShort(shortBuffer.getShort());

                                    channel.position(typesOffset + hdrOffset + j * 16 + k * 4 + 2);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType3Entry.duration = toUShort((short)(shortBuffer.getShort() - bsaType3Entry.startTime));

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 64);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType3Entry.boundsType = toUShort(shortBuffer.getShort());

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 64 + 2);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType3Entry.i02 = toUShort(shortBuffer.getShort());

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 64 + 4);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType3Entry.growMaxBounds = toUShort(shortBuffer.getShort());

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 64 + 6);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType3Entry.i06_a = (byte) (shortBuffer.getShort() & 0x0F);
                                    shortBuffer.flip();
                                    bsaType3Entry.i06_b = (byte)((shortBuffer.getShort() >> 4) & 0x0F);
                                    shortBuffer.flip();
                                    bsaType3Entry.i06_c = (byte)((shortBuffer.getShort() >> 8) & 0x0F);
                                    shortBuffer.flip();
                                    bsaType3Entry.i06_d = (byte)((shortBuffer.getShort() >> 12) & 0x0F);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 64 + 8);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType3Entry.positionX = intBuffer.getFloat();

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 64 + 12);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType3Entry.positionY = intBuffer.getFloat();

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 64 + 16);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType3Entry.positionZ = intBuffer.getFloat();

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 64 + 20);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType3Entry.hitboxScale = intBuffer.getFloat();

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 64 + 24);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType3Entry.maximumX = intBuffer.getFloat();

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 64 + 28);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType3Entry.maximumY = intBuffer.getFloat();
                                    
                                    channel.position(typesOffset + dataOffset + j * 16 + k * 64 + 32);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType3Entry.maximumZ = intBuffer.getFloat();

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 64 + 36);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType3Entry.minimumX = intBuffer.getFloat();

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 64 + 40);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType3Entry.minimumY = intBuffer.getFloat();

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 64 + 44);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType3Entry.minimumZ = intBuffer.getFloat();

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 64 + 48);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType3Entry.hitAmount = toUShort(shortBuffer.getShort());

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 64 + 50);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType3Entry.hitboxLifetime = toUShort(shortBuffer.getShort());

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 64 + 52);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType3Entry.i52 = toUShort(shortBuffer.getShort());

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 64 + 54);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType3Entry.i54 = toUShort(shortBuffer.getShort());

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 64 + 56);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType3Entry.i56 = toUShort(shortBuffer.getShort());

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 64 + 58);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType3Entry.firstHit = toUShort(shortBuffer.getShort());

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 64 + 60);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType3Entry.multipleHits = toUShort(shortBuffer.getShort());;

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 64 + 62);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType3Entry.lastHit = toUShort(shortBuffer.getShort());
                                }

                                index++;
                            } 
                            case 4 -> {
                                treeView.getRoot().getChildren().get(mainIndex).getChildren().add(new TreeItem<>("Deflection"));

                                for (int k = 0; k < typeCount; k++) {
                                    BsaType4Entry bsaType4Entry = new BsaType4Entry();

                                    treeView.getRoot().getChildren().get(mainIndex).getChildren().get(index).getChildren().add(new TreeItem<>("Entry " + k));

                                    bsaType4HashMap.put(treeView.getRoot().getChildren().get(mainIndex).getChildren().get(index).getChildren().get(k), bsaType4Entry);

                                    channel.position(typesOffset + hdrOffset + j * 16 + k * 4);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType4Entry.startTime = toUShort(shortBuffer.getShort());

                                    channel.position(typesOffset + hdrOffset + j * 16 + k * 4 + 2);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType4Entry.duration = toUShort((short)(shortBuffer.getShort() - bsaType4Entry.startTime));

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 56);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType4Entry.i00 = intBuffer.getInt();

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 56 + 4);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType4Entry.i04 = intBuffer.getInt();

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 56 + 8);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType4Entry.i08 = intBuffer.getInt();

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 56 + 12);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType4Entry.f12 = intBuffer.getFloat();

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 56 + 16);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType4Entry.f16 = intBuffer.getFloat();

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 56 + 20);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType4Entry.f20 = intBuffer.getFloat();

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 56 + 24);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType4Entry.i24 = intBuffer.getInt();

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 56 + 28);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType4Entry.i28 = intBuffer.getInt();

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 56 + 32);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType4Entry.i32 = intBuffer.getInt();

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 56 + 36);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType4Entry.i36 = intBuffer.getInt();

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 56 + 40);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType4Entry.i40 = intBuffer.getInt();

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 56 + 44);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType4Entry.i44 = intBuffer.getInt();

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 56 + 48);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType4Entry.i48 = toUShort(shortBuffer.getShort());

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 56 + 50);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType4Entry.i50 = toUShort(shortBuffer.getShort());

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 56 + 52);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType4Entry.i52 = toUShort(shortBuffer.getShort());

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 56 + 54);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType4Entry.i54 = toUShort(shortBuffer.getShort());
                                }

                                index++;
                            }
                            case 6 -> {
                                treeView.getRoot().getChildren().get(mainIndex).getChildren().add(new TreeItem<>("Effect"));

                                for (int k = 0; k < typeCount; k++) {
                                    BsaType6Entry bsaType6Entry = new BsaType6Entry();

                                    treeView.getRoot().getChildren().get(mainIndex).getChildren().get(index).getChildren().add(new TreeItem<>("Entry " + k));

                                    bsaType6HashMap.put(treeView.getRoot().getChildren().get(mainIndex).getChildren().get(index).getChildren().get(k), bsaType6Entry);

                                    channel.position(typesOffset + hdrOffset + j * 16 + k * 4);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType6Entry.startTime = toUShort(shortBuffer.getShort());

                                    channel.position(typesOffset + hdrOffset + j * 16 + k * 4 + 2);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType6Entry.duration = toUShort((short)(shortBuffer.getShort() - bsaType6Entry.startTime));

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 24);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType6Entry.eepkType = toUShort(shortBuffer.getShort());

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 24 + 2);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType6Entry.skillId = toUShort(shortBuffer.getShort());

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 24 + 4);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType6Entry.effectId = toUShort(shortBuffer.getShort());

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 24 + 6);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType6Entry.i06 = toUShort(shortBuffer.getShort());

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 24 + 8);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType6Entry.effectSwitch = toUShort(shortBuffer.getShort());

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 24 + 10);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType6Entry.i10 = toUShort(shortBuffer.getShort());

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 24 + 12);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType6Entry.positionX = intBuffer.getFloat();

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 24 + 16);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType6Entry.positionY = intBuffer.getFloat();

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 24 + 20);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType6Entry.positionZ = intBuffer.getFloat();
                                }

                                index++;
                            }
                            case 7 -> {
                                treeView.getRoot().getChildren().get(mainIndex).getChildren().add(new TreeItem<>("Sound"));

                                for (int k = 0; k < typeCount; k++) {
                                    BsaType7Entry bsaType7Entry = new BsaType7Entry();

                                    treeView.getRoot().getChildren().get(mainIndex).getChildren().get(index).getChildren().add(new TreeItem<>("Entry " + k));

                                    bsaType7HashMap.put(treeView.getRoot().getChildren().get(mainIndex).getChildren().get(index).getChildren().get(k), bsaType7Entry);

                                    channel.position(typesOffset + hdrOffset + j * 16 + k * 4);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType7Entry.startTime = toUShort(shortBuffer.getShort());

                                    channel.position(typesOffset + hdrOffset + j * 16 + k * 4 + 2);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType7Entry.duration = toUShort((short)(shortBuffer.getShort() - bsaType7Entry.startTime));

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 8);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType7Entry.acbType = toUShort(shortBuffer.getShort());

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 8 + 2);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType7Entry.i02 = toUShort(shortBuffer.getShort());

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 8 + 4);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType7Entry.cueId = toUShort(shortBuffer.getShort());

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 8 + 6);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType7Entry.i06 = toUShort(shortBuffer.getShort());
                                }

                                index++;
                            }
                            case 8 -> {
                                treeView.getRoot().getChildren().get(mainIndex).getChildren().add(new TreeItem<>("Screen Effect"));

                                for (int k = 0; k < typeCount; k++) {
                                    BsaType8Entry bsaType8Entry = new BsaType8Entry();

                                    treeView.getRoot().getChildren().get(mainIndex).getChildren().get(index).getChildren().add(new TreeItem<>("Entry " + k));

                                    bsaType8HashMap.put(treeView.getRoot().getChildren().get(mainIndex).getChildren().get(index).getChildren().get(k), bsaType8Entry);

                                    channel.position(typesOffset + hdrOffset + j * 16 + k * 4);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType8Entry.startTime = toUShort(shortBuffer.getShort());

                                    channel.position(typesOffset + hdrOffset + j * 16 + k * 4 + 2);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType8Entry.duration = toUShort((short)(shortBuffer.getShort() - bsaType8Entry.startTime));

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 24);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType8Entry.bpeEffectId = toUShort(shortBuffer.getShort());

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 24 + 2);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType8Entry.screenEffectFlags = toUShort(shortBuffer.getShort());

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 24 + 4);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType8Entry.i04 = toUShort(shortBuffer.getShort());

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 24 + 8);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType8Entry.i08 = intBuffer.getInt();

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 24 + 12);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType8Entry.i12 = intBuffer.getInt();

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 24 + 16);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType8Entry.i16 = intBuffer.getInt();

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 24 + 20);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType8Entry.i20 = intBuffer.getInt();
                                }

                                index++;
                            }
                            case 10 -> {
                                treeView.getRoot().getChildren().get(mainIndex).getChildren().add(new TreeItem<>("BSA Type 10"));

                                for (int k=0; k < typeCount; k++) {
                                    BsaType10Entry bsaType10Entry = new BsaType10Entry();

                                    treeView.getRoot().getChildren().get(mainIndex).getChildren().get(index).getChildren().add(new TreeItem<>("Entry " + k));

                                    bsaType10HashMap.put(treeView.getRoot().getChildren().get(mainIndex).getChildren().get(index).getChildren().get(k), bsaType10Entry);
                                    
                                    channel.position(typesOffset + hdrOffset + j * 16 + k * 4);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType10Entry.startTime = toUShort(shortBuffer.getShort());

                                    channel.position(typesOffset + hdrOffset + j * 16 + k * 4 + 2);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType10Entry.duration = toUShort((short)(shortBuffer.getShort() - bsaType10Entry.startTime));

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 8);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType10Entry.skillId = intBuffer.getInt();

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 8 + 4);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType10Entry.i04 = toUShort(shortBuffer.getShort());

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 8 + 6);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType10Entry.i06 = toUShort(shortBuffer.getShort());
                                }

                                index++;
                            }
                            case 12 -> {
                                treeView.getRoot().getChildren().get(mainIndex).getChildren().add(new TreeItem<>("Send Projectile Signal"));

                                for (int k = 0; k < typeCount; k++) {
                                    BsaType12Entry bsaType12Entry = new BsaType12Entry();

                                    treeView.getRoot().getChildren().get(mainIndex).getChildren().get(index).getChildren().add(new TreeItem<>("Entry " + k));

                                    bsaType12HashMap.put(treeView.getRoot().getChildren().get(mainIndex).getChildren().get(index).getChildren().get(k), bsaType12Entry);

                                    channel.position(typesOffset + hdrOffset + j * 16 + k * 4);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType12Entry.startTime = toUShort(shortBuffer.getShort());

                                    channel.position(typesOffset + hdrOffset + j * 16 + k * 4 + 2);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType12Entry.duration = toUShort((short)(shortBuffer.getShort() - bsaType12Entry.startTime));

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 20);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType12Entry.signalValue = intBuffer.getInt();

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 20 + 4);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType12Entry.skillType = intBuffer.getInt();

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 20 + 8);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType12Entry.skillId = intBuffer.getInt();

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 20 + 12);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType12Entry.deliveryMode = intBuffer.getInt();

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 20 +16);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType12Entry.pauseRecipientTimeline = intBuffer.getInt();
                                }

                                index++;
                            }
                            case 13 -> {
                                treeView.getRoot().getChildren().get(mainIndex).getChildren().add(new TreeItem<>("Projectile Protection"));

                                for (int k = 0; k < typeCount; k++) {
                                    BsaType13Entry bsaType13Entry = new BsaType13Entry();

                                    treeView.getRoot().getChildren().get(mainIndex).getChildren().get(index).getChildren().add(new TreeItem<>("Entry "+ k));

                                    bsaType13HashMap.put(treeView.getRoot().getChildren().get(mainIndex).getChildren().get(index).getChildren().get(k), bsaType13Entry);

                                    channel.position(typesOffset + hdrOffset + j * 16 + k * 4);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType13Entry.startTime = toUShort(shortBuffer.getShort());

                                    channel.position(typesOffset + hdrOffset + j * 16 + k * 4 + 2);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType13Entry.duration = toUShort((short)(shortBuffer.getShort() - bsaType13Entry.startTime));

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 32);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType13Entry.protection = toUShort(shortBuffer.getShort());

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 32 + 2);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType13Entry.i02 = toUShort(shortBuffer.getShort());

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 32 + 4);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType13Entry.maxHitboxPower = intBuffer.getFloat();

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 32 + 8);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType13Entry.protectSelectors_0_3 = (intBuffer.getInt() == 1);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 32 + 12);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType13Entry.protectAdditionalSelectors = intBuffer.getInt();

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 32 + 16);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType13Entry.entryPassingSignal = intBuffer.getFloat();

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 32 + 20);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType13Entry.markProtectedHit = (intBuffer.getInt() == 1);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 32 + 24);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType13Entry.i24 = intBuffer.getInt();

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 32 + 28);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType13Entry.i28 = intBuffer.getInt();
                                }

                                index++;
                            }
                            case 14 -> {
                                treeView.getRoot().getChildren().get(mainIndex).getChildren().add(new TreeItem<>("Effect Placement"));

                                for (int k = 0;k < typeCount; k++) {
                                    BsaType14Entry bsaType14Entry = new BsaType14Entry();

                                    treeView.getRoot().getChildren().get(mainIndex).getChildren().get(index).getChildren().add(new TreeItem<>("Entry " + k));

                                    bsaType14HashMap.put(treeView.getRoot().getChildren().get(mainIndex).getChildren().get(index).getChildren().get(k), bsaType14Entry);

                                    channel.position(typesOffset + hdrOffset + j * 16 + k * 4);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType14Entry.startTime = toUShort(shortBuffer.getShort());

                                    channel.position(typesOffset + hdrOffset + j * 16 + k * 4 + 2);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType14Entry.duration = toUShort((short)(shortBuffer.getShort() - bsaType14Entry.startTime));

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 88);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType14Entry.placementMode = toUShort(shortBuffer.getShort());

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 88 + 2);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType14Entry.i02 = toUShort(shortBuffer.getShort());

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 88 + 4);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType14Entry.placementFlags = toUint32(intBuffer.getInt());

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 88 + 8);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType14Entry.i08 = toUint32(intBuffer.getInt());

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 88 + 12);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType14Entry.f12 = intBuffer.getFloat();

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 88 + 16);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType14Entry.i16 = toUint32(intBuffer.getInt());

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 88 + 20);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType14Entry.f20 = intBuffer.getFloat();

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 88 + 24);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType14Entry.i24 = toUint32(intBuffer.getInt());

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 88 + 28);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType14Entry.f28 = intBuffer.getFloat();

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 88 + 32);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType14Entry.i32 = toUint32(intBuffer.getInt());

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 88 + 36);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType14Entry.i36 = toUint32(intBuffer.getInt());

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 88 + 40);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType14Entry.i40 = toUint32(intBuffer.getInt());

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 88 + 44);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType14Entry.f44 = intBuffer.getFloat();

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 88 + 48);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType14Entry.eepkType = toUint32(intBuffer.getInt());

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 88 + 52);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType14Entry.transform_BoneSelector = toUShort(shortBuffer.getShort());

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 88 + 54);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType14Entry.commonEepk = toUShort(shortBuffer.getShort());

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 88 + 56);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType14Entry.effectId = toUint32(intBuffer.getInt());

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 88 + 60);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType14Entry.f60 = intBuffer.getFloat();

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 88 + 64);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType14Entry.i64 = toUint32(intBuffer.getInt());

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 88 + 68);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType14Entry.f68 = intBuffer.getFloat();

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 88 + 72);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType14Entry.i72 = toUint32(intBuffer.getInt());

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 88 + 76);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType14Entry.i76 = toUint32(intBuffer.getInt());

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 88 + 80);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType14Entry.i80 = toUint32(intBuffer.getInt());

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 88 + 84);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType14Entry.effectPlacementFlags = toUint32(intBuffer.getInt());
                                }

                                index++;
                            }
                        }
                    }

                    mainIndex++;
                } 
            }
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void bsaWriter(Path path) {
        try(FileChannel channel = FileChannel.open(path, StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            int offset = 24;
            int entryOffset = 24 + allEntries * 4;
            int collisionOffset = 52;
            int expirationOffset = 52;
            int  typesOffset = 52;
            short type = 0;
            short typeCount;
            int hdrOffset = 192;
            int dataOffset = 0;
            int collisionEntriesCount = 0;
            int expirationEntriesCount = 0;
            int typeSize = 0;
            int entrySize = 0;
            
            ByteBuffer byteBuffer = ByteBuffer.allocate(1).order(ByteOrder.LITTLE_ENDIAN);
            ByteBuffer shortBuffer = ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN);
            ByteBuffer intBuffer = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);

            channel.position(0);
            channel.write(ByteBuffer.wrap(new byte[] {0x23, 0x42, 0x53, 0x41}));

            channel.position(4);
            channel.write(ByteBuffer.wrap(new byte[]{(byte)0xFE, (byte)0xFF}));

            channel.position(6);
            channel.write(ByteBuffer.wrap(new byte[] {0x18}));

            channel.position(18);
            shortBuffer.clear();
            shortBuffer.putShort((short) allEntries);
            shortBuffer.flip();
            channel.write(shortBuffer);

            channel.position(20);
            intBuffer.clear();
            intBuffer.putInt(offset);
            intBuffer.flip();
            channel.write(intBuffer);

            int mainIndex = 0;

            for (int i = 0; i < allEntries; i++) {
                if(Integer.parseInt(treeView.getRoot().getChildren().get(mainIndex).getValue().toString().replaceAll("\\D+", "")) == i) {
                    BsaMainEntry bsaMainEntry = bsaMainHashMap.get(treeView.getRoot().getChildren().get(mainIndex));

                    int index = 0;
                    short typesCount = 0;

                    channel.position(offset + i * 4);
                    intBuffer.clear();
                    intBuffer.putInt(entryOffset);
                    intBuffer.flip();
                    channel.write(intBuffer);

                    channel.position(entryOffset);
                    intBuffer.clear();
                    intBuffer.putInt(bsaMainEntry.i00);
                    intBuffer.flip();
                    channel.write(intBuffer);

                    if (treeView.getRoot().getChildren().get(mainIndex).getChildren().get(index).getValue().equals("Collision (After Effects)")) {
                        collisionEntriesCount = treeView.getRoot().getChildren().get(mainIndex).getChildren().get(index).getChildren().size();
                    }
                    else {
                        collisionEntriesCount = 0;
                        collisionOffset = 0;
                    }
                    
                    channel.position(entryOffset + 4);
                    shortBuffer.clear();
                    shortBuffer.putShort((short) collisionEntriesCount);
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    channel.position(entryOffset + 8);
                    intBuffer.clear();
                    intBuffer.putInt(collisionOffset);
                    intBuffer.flip();
                    channel.write(intBuffer);
                    collisionOffset += entryOffset;
                    
                    if (collisionEntriesCount > 0) {
                        for (int j = 0; j < collisionEntriesCount; j++) {
                            BsaCollisionEntry bsaCollisionEntry = bsaCollisionHashMap.get(treeView.getRoot().getChildren().get(mainIndex).getChildren().get(index).getChildren().get(j));

                            channel.position(collisionOffset + j * 24);
                            shortBuffer.clear();
                            shortBuffer.putShort((short) bsaCollisionEntry.eepkType);
                            shortBuffer.flip();
                            channel.write(shortBuffer);

                            channel.position(collisionOffset + j * 24 + 2);
                            shortBuffer.clear();
                            shortBuffer.putShort((short) bsaCollisionEntry.skillId);
                            shortBuffer.flip();
                            channel.write(shortBuffer);

                            channel.position(collisionOffset + j * 24 + 4);
                            shortBuffer.clear();
                            shortBuffer.putShort((short) bsaCollisionEntry.effectId);;
                            shortBuffer.flip();
                            channel.write(shortBuffer);

                            channel.position(collisionOffset + j * 24 + 6);
                            shortBuffer.clear();
                            shortBuffer.putShort((short) bsaCollisionEntry.i06);
                            shortBuffer.flip();
                            channel.write(shortBuffer);

                            channel.position(collisionOffset + j * 24 + 8);
                            intBuffer.clear();
                            intBuffer.putInt(bsaCollisionEntry.i08);
                            intBuffer.flip();
                            channel.write(intBuffer);

                            channel.position(collisionOffset + j * 24 + 12);
                            intBuffer.clear();
                            intBuffer.putInt(bsaCollisionEntry.i12);
                            intBuffer.flip();
                            channel.write(intBuffer);

                            channel.position(collisionOffset + j * 24 + 16);
                            intBuffer.clear();
                            intBuffer.putInt(bsaCollisionEntry.i16);
                            intBuffer.flip();
                            channel.write(intBuffer);

                            channel.position(collisionOffset + j * 24 + 20);
                            intBuffer.clear();
                            intBuffer.putInt(bsaCollisionEntry.i20);
                            intBuffer.flip();
                            channel.write(intBuffer);

                            expirationOffset += 24;
                            typesOffset += 24;
                            entrySize += 24;
                        }

                        index++;
                    } 
                    
                    if (treeView.getRoot().getChildren().get(mainIndex).getChildren().get(index).getValue().equals("Collision Sound (After Effects)")) {
                        expirationEntriesCount = treeView.getRoot().getChildren().get(mainIndex).getChildren().get(index).getChildren().size();
                    }
                    else {
                        expirationEntriesCount = 0;
                        expirationOffset = 0;
                    }

                    channel.position(entryOffset + 6);
                    shortBuffer.clear();
                    shortBuffer.putShort((short) expirationEntriesCount);
                    shortBuffer.flip();
                    channel.write(shortBuffer);
                    
                    channel.position(entryOffset + 12);
                    intBuffer.clear();
                    intBuffer.putInt(expirationOffset);
                    intBuffer.flip();
                    channel.write(intBuffer);
                    expirationOffset += entryOffset;

                    if (expirationEntriesCount > 0) {
                        for (int j = 0;j < expirationEntriesCount; j++) {
                            BsaCollisionSoundEntry bsaCollisionSoundEntry = bsaCollisionSoundHashMap.get(treeView.getRoot().getChildren().get(mainIndex).getChildren().get(index).getChildren().get(j));
                        
                            channel.position(expirationOffset + j * 8);
                            shortBuffer.clear();
                            shortBuffer.putShort((short) bsaCollisionSoundEntry.acbType);
                            shortBuffer.flip();
                            channel.write(shortBuffer);

                            channel.position(expirationOffset + j * 8 + 2);
                            shortBuffer.clear();
                            shortBuffer.putShort((short) bsaCollisionSoundEntry.i02);
                            shortBuffer.flip();
                            channel.write(shortBuffer);

                            channel.position(expirationOffset + j * 8 + 4);
                            shortBuffer.clear();
                            shortBuffer.putShort((short) bsaCollisionSoundEntry.cueId);
                            shortBuffer.flip();
                            channel.write(shortBuffer);

                            channel.position(expirationOffset + j * 8 + 6);
                            shortBuffer.clear();
                            shortBuffer.putShort((short) bsaCollisionSoundEntry.i06);
                            shortBuffer.flip();
                            channel.write(shortBuffer);

                            typesOffset += 8;
                            entrySize += 8;
                        }

                        index++;
                    } 
                    
                    channel.position(entryOffset + 16);
                    byteBuffer.clear();
                    byteBuffer.put((byte) (bsaMainEntry.i16_a | bsaMainEntry.i16_b << 4));
                    byteBuffer.flip();
                    channel.write(byteBuffer);

                    channel.position(entryOffset + 17);
                    byteBuffer.clear();
                    byteBuffer.put((byte) bsaMainEntry.i17);
                    byteBuffer.flip();
                    channel.write(byteBuffer);

                    channel.position(entryOffset + 18);
                    intBuffer.clear();
                    intBuffer.putInt(bsaMainEntry.i18);
                    intBuffer.flip();
                    channel.write(intBuffer);

                    channel.position(entryOffset + 22);
                    shortBuffer.clear();
                    shortBuffer.putShort((short) bsaMainEntry.lifetime);
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    channel.position(entryOffset + 24);
                    shortBuffer.clear();
                    shortBuffer.putShort((short) bsaMainEntry.i24);
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    channel.position(entryOffset + 26);
                    shortBuffer.clear();
                    shortBuffer.putShort((short) bsaMainEntry.expires);
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    channel.position(entryOffset + 28);
                    shortBuffer.clear();
                    shortBuffer.putShort((short) bsaMainEntry.impactProjectile);
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    channel.position(entryOffset + 30);
                    shortBuffer.clear();
                    shortBuffer.putShort((short) bsaMainEntry.impactEnemy);
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    channel.position(entryOffset + 32);
                    shortBuffer.clear();
                    shortBuffer.putShort((short) bsaMainEntry.impactGround);
                    shortBuffer.flip();
                    channel.write(shortBuffer);
                    
                    for (int j = 0; j < treeView.getRoot().getChildren().get(mainIndex).getChildren().size(); j++) {
                        if (!treeView.getRoot().getChildren().get(mainIndex).getChildren().get(j).getValue().equals("Collision (After Effects)") && !treeView.getRoot().getChildren().get(mainIndex).getChildren().get(j).getValue().equals("Collision Sound (After Effects)")) {
                            typesCount++;
                        }
                    }

                    hdrOffset = typesCount * 16;
                    
                    channel.position(entryOffset + 34);
                    shortBuffer.clear();
                    shortBuffer.putShort(typesCount);
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    channel.position(entryOffset + 36);
                    intBuffer.clear();
                    intBuffer.putInt(typesOffset);
                    intBuffer.flip();
                    channel.write(intBuffer);
                    typesOffset += entryOffset;

                    channel.position(entryOffset + 40);
                    intBuffer.clear();
                    intBuffer.putInt(bsaMainEntry.i40);
                    intBuffer.flip();
                    channel.write(intBuffer);

                    channel.position(entryOffset + 44);
                    intBuffer.clear();
                    intBuffer.putInt(bsaMainEntry.i44);
                    intBuffer.flip();
                    channel.write(intBuffer);

                    channel.position(entryOffset + 48);
                    intBuffer.clear();
                    intBuffer.putInt(bsaMainEntry.i48);
                    intBuffer.flip();
                    channel.write(intBuffer);

                    entrySize += 52;

                    for (int j = 0; j < typesCount; j++) {
                        switch(treeView.getRoot().getChildren().get(mainIndex).getChildren().get(index).getValue()) {
                            case "BSA Entry Passing" -> {
                                type = 0;
                                typeSize = 20;
                            } 
                            case "Movement" -> {
                                type = 1;
                                typeSize = 52;
                            }        
                            case "Projectile Timeline Remap" -> {
                                type = 2;
                                typeSize = 12;
                            }      
                            case "Hitbox" -> {
                                type = 3;
                                typeSize = 68;
                            }           
                            case "Deflection" -> {
                                type = 4;
                                typeSize = 60;
                            }        
                            case "Effect" -> {
                                type = 6;
                                typeSize = 28;
                            }           
                            case "Sound" -> {
                                type = 7;
                                typeSize = 12;
                            }            
                            case "Screen Effect" -> {
                                type = 8;
                                typeSize = 28;
                            }     
                            case "BSA Type 10" -> {
                                type = 10;
                                typeSize = 12;
                            }      
                            case "Send Projectile Signal" -> {
                                type = 12;
                                typeSize = 24;
                            }       
                            case "Projectile Protection" -> {
                                type = 13;
                                typeSize = 36;
                            }       
                            case "Effect Placement" -> {
                                type = 14;
                                typeSize = 92;
                            }      
                        }

                        typeCount = (short) treeView.getRoot().getChildren().get(mainIndex).getChildren().get(index).getChildren().size();
                        
                        channel.position(typesOffset + j * 16);
                        shortBuffer.clear();
                        shortBuffer.putShort(type);
                        shortBuffer.flip();
                        channel.write(shortBuffer);

                        channel.position(typesOffset + j * 16 + 6);
                        shortBuffer.clear();
                        shortBuffer.putShort(typeCount);
                        shortBuffer.flip();
                        channel.write(shortBuffer);

                        channel.position(typesOffset + j * 16 + 8);
                        intBuffer.clear();
                        intBuffer.putInt(hdrOffset);
                        intBuffer.flip();
                        channel.write(intBuffer);

                        dataOffset = hdrOffset + typeCount * 4;

                        for (int k = 0; k < typeCount; k++) {
                            switch (type) {
                                case 0 -> {
                                    BsaType0Entry bsaType0Entry = bsaType0HashMap.get(treeView.getRoot().getChildren().get(mainIndex).getChildren().get(index).getChildren().get(k));

                                    channel.position(typesOffset + hdrOffset + j * 16 + k * 4);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) bsaType0Entry.startTime);
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset + hdrOffset + j * 16 + k * 4 + 2);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) (bsaType0Entry.duration + bsaType0Entry.startTime));
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset  +dataOffset + j * 16 + k * 16);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) bsaType0Entry.i00);
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);
                                    
                                    channel.position(typesOffset + dataOffset + j * 16 + k * 16 + 2);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) bsaType0Entry.mainCondition);
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset+dataOffset + j * 16 + k * 16 + 4);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short)bsaType0Entry.bsaEntryId);
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 16 + 6);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short)bsaType0Entry.i06);
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 16 + 8);
                                    intBuffer.clear();
                                    intBuffer.putFloat(bsaType0Entry.bacCondition);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset+dataOffset + j * 16 + k * 16 + 12);
                                    intBuffer.clear();
                                    intBuffer.putFloat(bsaType0Entry.f12);
                                    intBuffer.flip();
                                    channel.write(intBuffer); 

                                    entrySize += 20;
                                }
                                case 1 -> {
                                    BsaType1Entry bsaType1Entry = bsaType1HashMap.get(treeView.getRoot().getChildren().get(mainIndex).getChildren().get(index).getChildren().get(k));

                                    channel.position(typesOffset + hdrOffset + j * 16 + k * 4);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) bsaType1Entry.startTime);
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset+hdrOffset + j * 16 + k * 4 + 2);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) (bsaType1Entry.duration + bsaType1Entry.startTime));
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 48);
                                    intBuffer.clear();
                                    intBuffer.putInt((int) bsaType1Entry.motionFlags);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 48 + 4);
                                    intBuffer.clear();
                                    intBuffer.putFloat(bsaType1Entry.speedZ);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 48 + 8);
                                    intBuffer.clear();
                                    intBuffer.putFloat(bsaType1Entry.speedX);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 48 + 12);
                                    intBuffer.clear();
                                    intBuffer.putFloat(bsaType1Entry.speedY);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 48 + 16);
                                    intBuffer.clear();
                                    intBuffer.putFloat(bsaType1Entry.f16);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 48 + 20);
                                    intBuffer.clear();
                                    intBuffer.putFloat(bsaType1Entry.accelerationZ);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 48 + 24);
                                    intBuffer.clear();
                                    intBuffer.putFloat(bsaType1Entry.accelerationX);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 48 + 28);
                                    intBuffer.clear();
                                    intBuffer.putFloat(bsaType1Entry.accelerationY);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 48 + 32);
                                    intBuffer.clear();
                                    intBuffer.putFloat(bsaType1Entry.fallofStrength);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 48 + 36);
                                    intBuffer.clear();
                                    intBuffer.putFloat(bsaType1Entry.spreadDirectionX);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 48 + 40);
                                    intBuffer.clear();
                                    intBuffer.putFloat(bsaType1Entry.spreadDirectionY);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 48 + 44);
                                    intBuffer.clear();
                                    intBuffer.putFloat(bsaType1Entry.spreadDirectionZ);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    entrySize += 52;
                                }
                                case 2 -> {
                                    BsaType2Entry bsaType2Entry = bsaType2HashMap.get(treeView.getRoot().getChildren().get(mainIndex).getChildren().get(index).getChildren().get(k));

                                    channel.position(typesOffset + hdrOffset + j * 16 + k * 4);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) bsaType2Entry.startTime);
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset + hdrOffset + j * 16 + k * 4 + 2);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) (bsaType2Entry.duration + bsaType2Entry.startTime));
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 8);
                                    shortBuffer.clear();
                                    shortBuffer.putShort(bsaType2Entry.i00);
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 8+ 2);
                                    shortBuffer.clear();
                                    shortBuffer.putShort(bsaType2Entry.outputStartFrame);
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 8 + 4);
                                    shortBuffer.clear();
                                    shortBuffer.putShort(bsaType2Entry.outputEndFrame);
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 8 + 6);
                                    shortBuffer.clear();
                                    shortBuffer.putShort(bsaType2Entry.i06);
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    entrySize += 12;
                                }
                                case 3 -> {
                                    BsaType3Entry bsaType3Entry = bsaType3HashMap.get(treeView.getRoot().getChildren().get(mainIndex).getChildren().get(index).getChildren().get(k));

                                    channel.position(typesOffset + hdrOffset + j * 16 + k * 4);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) bsaType3Entry.startTime);
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset + hdrOffset + j * 16 + k * 4 + 2);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) (bsaType3Entry.duration + bsaType3Entry.startTime));
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 64);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) bsaType3Entry.boundsType);
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 64 + 2);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) bsaType3Entry.i02);
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 64 + 4);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) bsaType3Entry.growMaxBounds);
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 64 + 6);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) (bsaType3Entry.i06_a | bsaType3Entry.i06_b << 4 | bsaType3Entry.i06_c << 8 | bsaType3Entry.i06_d << 12));
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 64 + 8);
                                    intBuffer.clear();
                                    intBuffer.putFloat(bsaType3Entry.positionX);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 64 + 12);
                                    intBuffer.clear();
                                    intBuffer.putFloat(bsaType3Entry.positionY);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 64 + 16);
                                    intBuffer.clear();
                                    intBuffer.putFloat(bsaType3Entry.positionZ);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 64 + 20);
                                    intBuffer.clear();
                                    intBuffer.putFloat(bsaType3Entry.hitboxScale);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 64 + 24);
                                    intBuffer.clear();
                                    intBuffer.putFloat(bsaType3Entry.maximumX);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 64 + 28);
                                    intBuffer.clear();
                                    intBuffer.putFloat(bsaType3Entry.maximumY);
                                    intBuffer.flip();
                                    channel.write(intBuffer);
                                    
                                    channel.position(typesOffset + dataOffset + j * 16 + k * 64 + 32);
                                    intBuffer.clear();
                                    intBuffer.putFloat(bsaType3Entry.maximumZ);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 64 + 36);
                                    intBuffer.clear();
                                    intBuffer.putFloat(bsaType3Entry.minimumX);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset  + dataOffset + j * 16 + k * 64 + 40);
                                    intBuffer.clear();
                                    intBuffer.putFloat(bsaType3Entry.minimumY);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 64 + 44);
                                    intBuffer.clear();
                                    intBuffer.putFloat(bsaType3Entry.minimumZ);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 64 + 48);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) bsaType3Entry.hitAmount);
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 64 + 50);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) bsaType3Entry.hitboxLifetime);
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 64 + 52);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) bsaType3Entry.i52);
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16+ k * 64 + 54);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) bsaType3Entry.i54);
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 64 + 56);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) bsaType3Entry.i56);
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 64 + 58);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) bsaType3Entry.firstHit);
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 64 + 60);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) bsaType3Entry.multipleHits);
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 64 + 62);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) bsaType3Entry.lastHit);
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    entrySize += 68;
                                }
                                case 4 -> {
                                    BsaType4Entry bsaType4Entry = bsaType4HashMap.get(treeView.getRoot().getChildren().get(mainIndex).getChildren().get(index).getChildren().get(k));
                                    
                                    channel.position(typesOffset + hdrOffset + j * 16 + k * 4);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) bsaType4Entry.startTime);
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset + hdrOffset + j * 16 + k * 4 + 2);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) (bsaType4Entry.duration + bsaType4Entry.startTime));
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 56);
                                    intBuffer.clear();
                                    intBuffer.putInt(bsaType4Entry.i00);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 56 + 4);
                                    intBuffer.clear();
                                    intBuffer.putInt(bsaType4Entry.i04);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 56 + 8);
                                    intBuffer.clear();
                                    intBuffer.putInt(bsaType4Entry.i08);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 56 + 12);
                                    intBuffer.clear();
                                    intBuffer.putFloat(bsaType4Entry.f12);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 56 + 16);
                                    intBuffer.clear();
                                    intBuffer.putFloat(bsaType4Entry.f16);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 56 + 20);
                                    intBuffer.clear();
                                    intBuffer.putFloat(bsaType4Entry.f20);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 56 + 24);
                                    intBuffer.clear();
                                    intBuffer.putInt(bsaType4Entry.i24);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 56 + 28);
                                    intBuffer.clear();
                                    intBuffer.putInt(bsaType4Entry.i28);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 56 + 32);
                                    intBuffer.clear();
                                    intBuffer.putInt(bsaType4Entry.i32);
                                    intBuffer.flip();
                                    channel.write(intBuffer);;

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 56 + 36);
                                    intBuffer.clear();
                                    intBuffer.putInt(bsaType4Entry.i36);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 56 + 40);
                                    intBuffer.clear();
                                    intBuffer.putInt(bsaType4Entry.i40);
                                    intBuffer.flip();
                                    channel.write(intBuffer);;

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 56 + 44);
                                    intBuffer.clear();
                                    intBuffer.putInt(bsaType4Entry.i44);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 56 + 48);
                                    intBuffer.clear();
                                    intBuffer.putInt(bsaType4Entry.i48);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 56 + 50);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) bsaType4Entry.i50);
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 56 + 52);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) bsaType4Entry.i52);
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 56 + 54);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) bsaType4Entry.i54);
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);
                                    
                                    entrySize += 60;
                                }
                                case 6 -> {
                                    BsaType6Entry bsaType6Entry = bsaType6HashMap.get(treeView.getRoot().getChildren().get(mainIndex).getChildren().get(index).getChildren().get(k));

                                    channel.position(typesOffset + hdrOffset + j * 16 + k * 4);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) bsaType6Entry.startTime);
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset + hdrOffset + j * 16 + k * 4 + 2);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) (bsaType6Entry.duration + bsaType6Entry.startTime));
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 24);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) bsaType6Entry.eepkType);
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 24 + 2);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) bsaType6Entry.skillId);
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 24 + 4);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) bsaType6Entry.effectId);
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 24 + 6);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) bsaType6Entry.i06);
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 24 + 8);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) bsaType6Entry.effectSwitch);
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 24 + 10);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) bsaType6Entry.i10);
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 24 + 12);
                                    intBuffer.clear();
                                    intBuffer.putFloat(bsaType6Entry.positionX);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 24 + 16);
                                    intBuffer.clear();
                                    intBuffer.putFloat(bsaType6Entry.positionY);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 24 + 20);
                                    intBuffer.clear();
                                    intBuffer.putFloat(bsaType6Entry.positionZ);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    entrySize += 28;
                                }
                                case 7 -> {
                                    BsaType7Entry bsaType7Entry = bsaType7HashMap.get(treeView.getRoot().getChildren().get(mainIndex).getChildren().get(index).getChildren().get(k));

                                    channel.position(typesOffset + hdrOffset + j * 16 + k * 4);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) bsaType7Entry.startTime);
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset + hdrOffset + j * 16+ k * 4 + 2);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) (bsaType7Entry.duration + bsaType7Entry.startTime));
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 8);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) bsaType7Entry.acbType);
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 8 + 2);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) bsaType7Entry.i02);
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 8 + 4);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) bsaType7Entry.cueId);
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 8 + 6);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) bsaType7Entry.i06);
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    entrySize += 12;
                                }
                                case 8 -> {
                                    BsaType8Entry bsaType8Entry = bsaType8HashMap.get(treeView.getRoot().getChildren().get(mainIndex).getChildren().get(index).getChildren().get(k));

                                    channel.position(typesOffset + hdrOffset + j * 16 + k * 4);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) bsaType8Entry.startTime);
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset + hdrOffset + j * 16+ k * 4 + 2);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) (bsaType8Entry.duration + bsaType8Entry.startTime));
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 24);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) bsaType8Entry.bpeEffectId);
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 24 + 2);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) bsaType8Entry.screenEffectFlags);
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 24 + 4);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) bsaType8Entry.i04);
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 24 + 8);
                                    intBuffer.clear();
                                    intBuffer.putInt(bsaType8Entry.i08);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 24 + 12);
                                    intBuffer.clear();
                                    intBuffer.putInt(bsaType8Entry.i12);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 24 + 16);
                                    intBuffer.clear();
                                    intBuffer.putInt(bsaType8Entry.i16);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 24+ 20);
                                    intBuffer.clear();
                                    intBuffer.putInt(bsaType8Entry.i20);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    entrySize += 28;
                                }
                                case 10 -> {
                                    BsaType10Entry bsaType10Entry = bsaType10HashMap.get(treeView.getRoot().getChildren().get(mainIndex).getChildren().get(index).getChildren().get(k));

                                    channel.position(typesOffset + hdrOffset + j * 16 + k * 4);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) bsaType10Entry.startTime);
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset + hdrOffset + j * 16+ k * 4 + 2);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) (bsaType10Entry.duration + bsaType10Entry.startTime));
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 8);
                                    intBuffer.clear();
                                    intBuffer.putInt(bsaType10Entry.skillId);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 8 + 4);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) bsaType10Entry.i04);
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16+ k * 8 + 6);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) bsaType10Entry.i06);
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    entrySize += 12;
                                }
                                case 12 -> {
                                    BsaType12Entry bsaType12Entry = bsaType12HashMap.get(treeView.getRoot().getChildren().get(mainIndex).getChildren().get(index).getChildren().get(k));

                                    channel.position(typesOffset + hdrOffset + j * 16 + k * 4);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) bsaType12Entry.startTime);
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset + hdrOffset + j * 16+ k * 4 + 2);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) (bsaType12Entry.duration + bsaType12Entry.startTime));
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 20);
                                    intBuffer.clear();
                                    intBuffer.putFloat(bsaType12Entry.signalValue);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 20 + 4);
                                    intBuffer.clear();
                                    intBuffer.putInt(bsaType12Entry.skillType);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 20 + 8);
                                    intBuffer.clear();
                                    intBuffer.putInt(bsaType12Entry.skillId);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 20 + 12);
                                    intBuffer.clear();
                                    intBuffer.putInt(bsaType12Entry.deliveryMode);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 20 +16);
                                    intBuffer.clear();
                                    intBuffer.putFloat(bsaType12Entry.pauseRecipientTimeline);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    entrySize += 24;
                                }
                                case 13 -> {
                                    BsaType13Entry bsaType13Entry = bsaType13HashMap.get(treeView.getRoot().getChildren().get(mainIndex).getChildren().get(index).getChildren().get(k));

                                    channel.position(typesOffset + hdrOffset + j * 16 + k * 4);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) bsaType13Entry.startTime);
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset + hdrOffset + j * 16+ k * 4 + 2);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) (bsaType13Entry.duration + bsaType13Entry.startTime));
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 32);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) bsaType13Entry.protection);
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 32 + 2);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) bsaType13Entry.i02);
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 32 + 4);
                                    intBuffer.clear();
                                    intBuffer.putFloat(bsaType13Entry.maxHitboxPower);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 32 + 8);
                                    intBuffer.clear();
                                    intBuffer.putInt(bsaType13Entry.protectSelectors_0_3 ? 1 : 0);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 32 + 12);
                                    intBuffer.clear();
                                    intBuffer.putInt(bsaType13Entry.protectAdditionalSelectors);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 32 + 16);
                                    intBuffer.clear();
                                    intBuffer.putFloat(bsaType13Entry.entryPassingSignal);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 32 + 20);
                                    intBuffer.clear();
                                    intBuffer.putInt(bsaType13Entry.markProtectedHit ? 1 : 0);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 32 + 24);
                                    intBuffer.clear();
                                    intBuffer.putInt(bsaType13Entry.i24);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 32 + 28);
                                    intBuffer.clear();
                                    intBuffer.putInt(bsaType13Entry.i28);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    entrySize += 36;
                                }
                                case 14 -> {
                                    BsaType14Entry bsaType14Entry = bsaType14HashMap.get(treeView.getRoot().getChildren().get(mainIndex).getChildren().get(index).getChildren().get(k));

                                    channel.position(typesOffset + hdrOffset + j * 16 + k * 4);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) bsaType14Entry.startTime);
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset + hdrOffset + j * 16+ k * 4 + 2);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) (bsaType14Entry.duration + bsaType14Entry.startTime));
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 88);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) bsaType14Entry.placementMode);
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 88 + 2);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) bsaType14Entry.i02);
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 88 + 4);
                                    intBuffer.clear();
                                    intBuffer.putFloat((int) bsaType14Entry.placementFlags);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 88 + 8);
                                    intBuffer.clear();
                                    intBuffer.putInt((int) bsaType14Entry.i08);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 88 + 12);
                                    intBuffer.clear();
                                    intBuffer.putFloat(bsaType14Entry.f12);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 88 + 16);
                                    intBuffer.clear();
                                    intBuffer.putInt((int) bsaType14Entry.i16);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 88 + 20);
                                    intBuffer.clear();
                                    intBuffer.putFloat(bsaType14Entry.f20);
                                    intBuffer.flip();
                                    channel.write(intBuffer);;

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 88 + 24);
                                    intBuffer.clear();
                                    intBuffer.putInt((int) bsaType14Entry.i24);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 88 + 28);
                                    intBuffer.clear();
                                    intBuffer.putFloat(bsaType14Entry.f28);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 88 + 32);
                                    intBuffer.clear();
                                    intBuffer.putInt((int) bsaType14Entry.i32);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 88 + 36);
                                    intBuffer.clear();
                                    intBuffer.putInt((int) bsaType14Entry.i36);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 88 + 40);
                                    intBuffer.clear();
                                    intBuffer.putInt((int) bsaType14Entry.i40);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 88 + 44);
                                    intBuffer.clear();
                                    intBuffer.putFloat(bsaType14Entry.f44);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 88 + 48);
                                    intBuffer.clear();
                                    intBuffer.putInt((int) bsaType14Entry.eepkType);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 88 + 52);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) bsaType14Entry.transform_BoneSelector);
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 88 + 54);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) bsaType14Entry.commonEepk);
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 88 + 56);
                                    intBuffer.clear();
                                    intBuffer.putInt((int) bsaType14Entry.effectId);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 88 + 60);
                                    intBuffer.clear();
                                    intBuffer.putFloat(bsaType14Entry.f60);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 88 + 64);
                                    intBuffer.clear();
                                    intBuffer.putInt((int) bsaType14Entry.i64);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 88 + 68);
                                    intBuffer.clear();
                                    intBuffer.putFloat(bsaType14Entry.f68);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 88 + 72);
                                    intBuffer.clear();
                                    intBuffer.putInt((int) bsaType14Entry.i72);
                                    intBuffer.flip();
                                    channel.write(intBuffer);;

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 88 + 76);
                                    intBuffer.clear();
                                    intBuffer.putInt((int) bsaType14Entry.i76);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 88 + 80);
                                    intBuffer.clear();
                                    intBuffer.putInt((int) bsaType14Entry.i80);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 88 + 84);
                                    intBuffer.clear();
                                    intBuffer.putInt((int) bsaType14Entry.effectPlacementFlags);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    entrySize += 92;
                                }
                            }
                        }

                        index++;
                        
                        hdrOffset += ((typeCount * typeSize) - 16);
                    
                        channel.position(typesOffset + j * 16 + 12);
                        intBuffer.clear();
                        intBuffer.putInt(dataOffset);
                        intBuffer.flip();
                        channel.write(intBuffer);

                        entrySize += 16;
                    }

                    entryOffset += entrySize;
                    entrySize = 0;
                    collisionOffset = 52;
                    expirationOffset = 52;
                    typesOffset = 52;

                    mainIndex++;
                }
            }
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    public static enum BsaMainValues {
        I00,
        I16_A,
        I16_B,
        I17,
        I18,
        Lifetime,
        I24,
        Expires,
        ImpactProjectile,
        ImpactEnemy,
        ImpactGround,
        I40,
        I44,
        I48
    }

    public static enum BsaCollisionValues {
        EEPK_Type,
        Skill_ID,
        Effect_ID,
        I06,
        I08,
        I12,
        I16,
        I20;

        public static enum EEPK_Types {
            Common(0),
            StageBG(1),
            CharacterEffect(2),
            AwokenSkill(3),
            SuperSkill(5),
            UltimateSkill(6),
            EvasiveSkill(7),
            KiBlastSkill(9),
            Stage(11);

            final int index;

            EEPK_Types(int index) {
                this.index = index;
            }
        }
    }

    public static enum BsaCollisionSoundValues {
        ACB_Type,
        I02,
        CUE_ID,
        I06;

        public static enum ACB_Types {
            Common_SE(0),
            Character_SE(1),
            Character_VOX(2),
            Skill_SE(3),
            Skill_VOX(4);

            final int index;

            ACB_Types(int index) {
                this.index = index;
            }
        }
    }

    public static enum BsaType0Values {
        StartTime,
        Duration,
        I00,
        MainConditon,
        BsaEntryID,
        I06,
        BAC_Conditon,
        F12;
    }

    public static enum BsaType1Values {
        StartTime,
        Duration,
        MotionFlags,
        SpeedX,
        SpeedY,
        SpeedZ,
        F16,
        AccelerationX,
        AccelerationY,
        AccelerationZ,
        FalloffStrength,
        SpreadDirectionX,
        SpreadDirectionY,
        SpreadDirectionZ
    }

    public static enum BsaType2Values {
        StartTime,
        Duration,
        I00,
        OutputStartFrame,
        OutputEndFrame,
        I06
    }

    public static enum BsaType3Values {
        StartTime,
        Duration,
        BoundsType,
        I02,
        GrowMaxBounds,
        I06_A,
        I06_B,
        I06_C,
        I06_D,
        PositionX,
        PositionY,
        PositionZ,
        HitboxScale,
        MaximumX,
        MaximumY,
        MaximumZ,
        MinimumX,
        MinimumY,
        MinimumZ,
        HitAmount,
        HitboxLifetime,
        I52,
        I54,
        I56,
        FirstHit,
        MultipleHits,
        LastHit;

        public static enum BoundsTypes {
            Uniform(0),
            MinMax(1),
            Unknown2(2),
            Unknown3(3),
            Unknown4(4);

            final int index;

            BoundsTypes(int index) {
                this.index = index;
            }
        }

        public static enum GrowMaxBoundsFlags {
            On(0),
            Off(1);

            final int index;

            GrowMaxBoundsFlags(int index) {
                this.index = index;
            }
        }
    }

    public static enum BsaType4Values {
        StartTime,
        Duration,
        I00,
        I04,
        I08,
        F12,
        F16,
        F20,
        I24,
        I28,
        I32,
        I36,
        I40,
        I44,
        I48,
        I50,
        I52,
        I54;
    }

    public static enum BsaType6Values {
        StartTime,
        Duration,
        EEPK_Type,
        Skill_ID,
        Effect_ID,
        I06,
        EffectSwitch,
        I10,
        PositionX,
        PositionY,
        PositionZ;

        public static enum EEPK_Types {
            Common(0),
            StageBG(1),
            CharacterEffect(2),
            AwokenSkill(3),
            SuperSkill(5),
            UltimateSkill(6),
            EvasiveSkill(7),
            KiBlastSkill(9),
            Stage(11);

            final int index;

            EEPK_Types(int index) {
                this.index = index;
            }
        }

        public static enum EffectSwitchFlags {
            On(0),
            Off(1);

            final int index;

            EffectSwitchFlags(int index) {
                this.index = index;
            }
        }
    }

    public static enum BsaType7Values {
        StartTime,
        Duration,
        ACB_Type,
        I02,
        Cue_ID,
        I06;
        
        public static enum ACB_Types {
            Common_SE(0),
            Character_SE(1),
            Character_VOX(2),
            Skill_SE(3),
            Skill_VOX(4);

            final int index;

            ACB_Types(int index) {
                this.index = index;
            }
        }
    }

    public static enum BsaType8Values {
        StartTime,
        Duration,
        BPE_Effect_ID,
        ScreenEffectFlags,
        I04,
        I08,
        I12,
        I16,
        I20;
    }

    public static enum BsaType10Values {
        StartTime,
        Duration,
        Skill_ID,
        I04,
        I06;
    }

    public static enum BsaType12Values {
        StartTime,
        Duration,
        SignalValue,
        SkillType,
        Skill_ID,
        DeliveryMode,
        PauseRecipientTimeline;

        public static enum SkillTypes {
            Common(0),
            StageBG(1),
            CharacterEffect(2),
            AwokenSkill(3),
            SuperSkill(5),
            UltimateSkill(6),
            EvasiveSkill(7),
            KiBlastSkill(9),
            Stage(11);

            final int index;

            SkillTypes(int index) {
                this.index = index;
            }
        }

        public static enum DeliveryMode {
            Broadcast(0),
            Same_ContextHighestPriority(1);

            final int index;

            DeliveryMode(int index) {
                this.index = index;
            }
        }
    }

    public static enum BsaType13Values {
        StartTime,
        Duration,
        Protection,
        I02,
        MaxHitboxPower,
        ProtectSelectors_0_3,
        ProtectAdditionalSelectors,
        EntryPassingSignal,
        MarkProtectedHit,
        I24,
        I28;

        public static enum ProtectionFlags {
            On(0),
            Off(1);

            final int index;

            ProtectionFlags(int index) {
                this.index = index;
            }
        }

        public static enum ProtectAdditionalSelectorsFlags {
            None(0),
            Selectors_4_And_5(1),
            Selector6(2),
            Selectors_4_5_And_6(3);

            final int index;

            ProtectAdditionalSelectorsFlags(int index) {
                this.index = index;
            }
        }
    }

    public static enum BsaType14Values {
        StartTime,
        Duration,
        PlacementMode,
        I02,
        PlacementFlags,
        I08,
        F12,
        I16,
        F20,
        I24,
        F28,
        I32,
        I36,
        I40,
        F44,
        EEPK_Type,
        Transform_BoneSelector,
        CMN_EEPK_Type,
        Effect_ID,
        F60,
        I64,  
        F68,
        I72,
        I76,
        I80,
        EffectPlacementFlags;

        public static enum PlacementModes {
            DefaultPlacement(0),
            Distance_BasedPlacement(1),
            ExplicitVectorPlacement(2);

            final int index;

            PlacementModes(int index) {
                this.index = index;
            }
        }

        public static enum EEPK_Types {
            Common(0),
            StageBG(1),
            CharacterEffect(2),
            AwokenSkill(3),
            SuperSkill(5),
            UltimateSkill(6),
            EvasiveSkill(7),
            KiBlastSkill(9),
            Stage(11);

            final int index;

            EEPK_Types(int index) {
                this.index = index;
            }
        }

        public static enum CMN_EEPK_Types {
            BTL_CMN(0),
            BTL_AURA(1),
            BTL_KDN(2),
            lby_cmn_LBY_CMN(3),
            TTL_TTL(4),
            ttl_lby_TTL_LBY(5),
            BTL_CMN2(6);

            final int index;

            CMN_EEPK_Types(int index) {
                this.index = index;
            }
        }
    }
}

class BsaMainEntry {
    public int i00;
    public byte i16_a;
    public byte i16_b;
    public int i17;
    public int i18;
    public int lifetime;
    public int i24;
    public int expires;
    public int impactProjectile;
    public int impactEnemy;
    public int impactGround;
    public int i40;
    public int i44;
    public int i48;

    public BsaMainEntry() {}
    public BsaMainEntry(BsaMainEntry other) {
        this.i00 = other.i00;
        this.i16_a = other.i16_a;
        this.i16_b = other.i16_b;
        this.i17 = other.i17;
        this.i18 = other.i18;
        this.lifetime = other.lifetime;
        this.i24 = other.i24;
        this.expires = other.expires;
        this.impactProjectile = other.impactProjectile;
        this.impactEnemy = other.impactEnemy;
        this.impactGround = other.impactGround;
        this.i40 = other.i40;
        this.i44 = other.i44;
        this.i48 = other.i48;
    }
}

class BsaCollisionEntry {
    public int eepkType;
    public int skillId;
    public int effectId;
    public int i06;
    public int i08;
    public int i12;
    public int i16;
    public int i20;

    public BsaCollisionEntry() {}
    public BsaCollisionEntry(BsaCollisionEntry other) {
        this.eepkType = other.eepkType;
        this.skillId = other.skillId;
        this.effectId = other.effectId;
        this.i06 = other.i06;
        this.i08 = other.i08;
        this.i12 = other.i12;
        this.i16 = other.i16;
        this.i20 = other.i20;
    }
}

class BsaCollisionSoundEntry {
    public int acbType;
    public int i02;
    public int cueId;
    public int i06;

    public BsaCollisionSoundEntry() {}
    public BsaCollisionSoundEntry(BsaCollisionSoundEntry other) {
        this.acbType = other.acbType;
        this.i02 = other.i02;
        this.cueId = other.cueId;
        this.i06 = other.i06;
    }
}

class BsaType0Entry {
    public int startTime;
    public int duration;
    public short i00;
    public int mainCondition;
    public int bsaEntryId;
    public short i06;
    public float bacCondition;
    public float f12;

    public BsaType0Entry() {}
    public BsaType0Entry(BsaType0Entry other) {
        this.startTime = other.startTime;
        this.duration = other.duration;
        this.i00 = other.i00;
        this.mainCondition = other.mainCondition;
        this.bsaEntryId = other.bsaEntryId;
        this.i06 = other.i06;
        this.bacCondition = other.bacCondition;
        this.f12 = other.f12;
    }
}

class BsaType1Entry {
    public int startTime;
    public int duration;
    public long motionFlags;
    public float speedZ;
    public float speedX;
    public float speedY;
    public float f16;
    public float accelerationZ;
    public float accelerationX;
    public float accelerationY;
    public float fallofStrength;
    public float spreadDirectionX;
    public float spreadDirectionY;
    public float spreadDirectionZ;

    public BsaType1Entry() {}
    public BsaType1Entry(BsaType1Entry other) {
        this.startTime = other.startTime;
        this.duration = other.duration;
        this.motionFlags = other.motionFlags;
        this.speedZ = other.speedZ;
        this.speedX = other.speedX;
        this.speedY = other.speedY;
        this.f16 = other.f16;
        this.accelerationZ = other.accelerationZ;
        this.accelerationX = other.accelerationX;
        this.accelerationY = other.accelerationY;
        this.fallofStrength = other.fallofStrength;
        this.spreadDirectionX = other.spreadDirectionX;
        this.spreadDirectionY = other.spreadDirectionY;
        this.spreadDirectionZ = other.spreadDirectionZ;
    }
}

class BsaType2Entry {
    public int startTime;
    public int duration;
    public short i00;
    public short outputStartFrame;
    public short outputEndFrame;
    public short i06;
    
    public BsaType2Entry () {}
    public BsaType2Entry (BsaType2Entry other) {
        this.startTime = other.startTime;
        this.duration = other.duration;
        this.i00 = other.i00;
        this.outputStartFrame = other.outputStartFrame;
        this.outputEndFrame = other.outputEndFrame;
        this.i06 = other.i06;
    }
}

class BsaType3Entry {
    public int startTime;
    public int duration;
    public int boundsType;
    public int i02;
    public int growMaxBounds;
    public byte i06_a;
    public byte i06_b;
    public byte i06_c;
    public byte i06_d;
    public float positionX;
    public float positionY;
    public float positionZ;
    public float hitboxScale;
    public float maximumX;
    public float maximumY;
    public float maximumZ;
    public float minimumX;
    public float minimumY;
    public float minimumZ;
    public int hitAmount;
    public int hitboxLifetime;
    public int i52;
    public int i54;
    public int i56;
    public int firstHit;
    public int multipleHits;
    public int lastHit;

    public BsaType3Entry() {}
    public BsaType3Entry(BsaType3Entry other) {
        this.startTime = other.startTime;
        this.duration = other.duration;
        this.boundsType = other.boundsType;
        this.i02 = other.i02;
        this.growMaxBounds = other.growMaxBounds;
        this.i06_a = other.i06_a;
        this.i06_b = other.i06_b;
        this.i06_c = other.i06_c;
        this.i06_d = other.i06_d;
        this.positionX = other.positionX;
        this.positionY = other.positionY;
        this.positionZ = other.positionZ;
        this.hitboxScale = other.hitboxScale;
        this.maximumX = other.maximumX;
        this.maximumY = other.maximumY;
        this.maximumZ = other.maximumZ;
        this.minimumX = other.minimumX;
        this.minimumY = other.minimumY;
        this.minimumZ = other.minimumZ;
        this.hitAmount = other.hitAmount;
        this.hitboxLifetime = other.hitboxLifetime;
        this.i52 = other.i52;
        this.i54 = other.i54;
        this.i56 = other.i56;
        this.firstHit = other.firstHit;
        this.multipleHits = other.multipleHits;
        this.lastHit = other.lastHit;
    }
}

class BsaType4Entry {
    int startTime;
    int duration;
    int i00;
    int i04;
    int i08;
    float f12;
    float f16;
    float f20;
    int i24;
    int i28;
    int i32;
    int i36;
    int i40;
    int i44;
    int i48;
    int i50;
    int i52;
    int i54;

    public BsaType4Entry() {}
    public BsaType4Entry(BsaType4Entry other) {
        this.startTime = other.startTime;
        this.duration = other.duration;
        this.i00 = other.i00;
        this.i04 = other.i04;
        this.i08 = other.i08;
        this.f12 = other.f12;
        this.f16 = other.f16;
        this.f20 = other.f20;
        this.i24 = other.i24;
        this.i28 = other.i28;
        this.i32 = other.i32;
        this.i36 = other.i36;
        this.i40 = other.i40;
        this.i44 = other.i44;
        this.i48 = other.i48;
        this.i50 = other.i50;
        this.i52 = other.i52;
        this.i54 = other.i54;
    }
}

class BsaType6Entry {
    int startTime;
    int duration;
    int eepkType;
    int skillId;
    int effectId;
    int i06;
    int effectSwitch;
    int i10;
    float positionX;
    float positionY;
    float positionZ;
    
    BsaType6Entry() {}
    BsaType6Entry(BsaType6Entry other) {
        this.startTime = other.startTime;
        this.duration = other.duration;
        this.eepkType = other.eepkType;
        this.skillId = other.skillId;
        this.effectId = other.effectId;
        this.i06 = other.i06;
        this.effectSwitch = other.effectSwitch;
        this.i10 = other.i10;
        this.positionX = other.positionX;
        this.positionY = other.positionY;
        this.positionZ = other.positionZ;
    }
}

class BsaType7Entry {
    int startTime;
    int duration;
    int acbType;
    int i02;
    int cueId;
    int i06;

    BsaType7Entry() {}
    BsaType7Entry(BsaType7Entry other) {
        this.startTime = other.startTime;
        this.duration = other.duration;
        this.acbType = other.acbType;
        this.i02 = other.i02;
        this.cueId = other.cueId;
        this.i06 = other.i06;
    }
}

class BsaType8Entry {
    int startTime;
    int duration;
    int bpeEffectId;
    int screenEffectFlags;
    int i04;
    int i08;
    int i12;
    int i16;
    int i20;

    BsaType8Entry() {}
    BsaType8Entry(BsaType8Entry other) {
        this.startTime = other.startTime;
        this.duration = other.duration;
        this.bpeEffectId = other.bpeEffectId;
        this.screenEffectFlags = other.screenEffectFlags;
        this.i04 = other.i04;
        this.i08 = other.i08;
        this.i12 = other.i12;
        this.i16 = other.i16;
        this.i20 = other.i20;
    }
}

class BsaType10Entry {
    int startTime;
    int duration;
    int skillId;
    int i04;
    int i06;

    BsaType10Entry() {}
    BsaType10Entry(BsaType10Entry other) {
        this.startTime = other.startTime;
        this.duration = other.duration;
        this.skillId = other.skillId;
        this.i04 = other.i04;
        this.i06 = other.i06;
    }
}

class BsaType12Entry {
    int startTime;
    int duration;
    float signalValue;
    int skillType;
    int skillId;
    int deliveryMode;
    float pauseRecipientTimeline;

    BsaType12Entry() {}
    BsaType12Entry(BsaType12Entry other) {
        this.startTime = other.startTime;
        this.duration = other.duration;
        this.signalValue = other.signalValue;
        this.skillType = other.skillType;
        this.skillId = other.skillId;
        this.deliveryMode = other.deliveryMode;
        this.pauseRecipientTimeline = other.pauseRecipientTimeline;
    }
}

class BsaType13Entry {
    int startTime;
    int duration;
    int protection;
    int i02;
    float maxHitboxPower;
    boolean protectSelectors_0_3;
    int protectAdditionalSelectors;
    float entryPassingSignal;
    boolean markProtectedHit;
    int i24;
    int i28;

    BsaType13Entry() {}
    BsaType13Entry(BsaType13Entry other) {
        this.startTime = other.startTime;
        this.duration = other.duration;
        this.protection = other.protection;
        this.i02 = other.i02;
        this.maxHitboxPower = other.maxHitboxPower;
        this.protectSelectors_0_3 = other.protectSelectors_0_3;
        this.protectAdditionalSelectors = other.protectAdditionalSelectors;
        this.entryPassingSignal = other.entryPassingSignal;
        this.markProtectedHit = other.markProtectedHit;
        this.i24 = other.i24;
        this.i28 = other.i28;
    }
}

class BsaType14Entry {
    int startTime;
    int duration;
    int placementMode;
    int i02;
    long placementFlags;
    long i08;
    float f12;
    long i16;
    float f20;
    long i24;
    float f28;
    long i32;
    long i36;
    long i40;
    float f44;
    long eepkType;
    int transform_BoneSelector;
    int commonEepk;
    long effectId;
    float f60;
    long i64;
    float f68;
    long i72;
    long i76;
    long i80;
    long effectPlacementFlags;

    BsaType14Entry() {}

    BsaType14Entry(BsaType14Entry other) {
        this.startTime = other.startTime;
        this.duration = other.duration;
        this.placementMode = other.placementMode;
        this.i02 = other.i02;
        this.placementFlags = other.placementFlags;
        this.i08 = other.i08;
        this.f12 = other.f12;
        this.i16 = other.i16;
        this.f20 = other.f20;
        this.i24 = other.i24;
        this.f28 = other.f28;
        this.i32 = other.i32;
        this.i36 = other.i36;
        this.i40 = other.i40;
        this.f44 = other.f44;
        this.eepkType = other.eepkType;
        this.transform_BoneSelector = other.transform_BoneSelector;
        this.commonEepk = other.commonEepk;
        this.effectId = other.effectId;
        this.f60 = other.f60;
        this.i64 = other.i64;
        this.f68 = other.f68;
        this.i72 = other.i72;
        this.i76 = other.i76;
        this.i80 = other.i80;
        this.effectPlacementFlags = other.effectPlacementFlags;
    }
}