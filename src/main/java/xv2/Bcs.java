package xv2;
import static xv2.Unsigned.toUByte;
import static xv2.Unsigned.toUShort;
import static xv2.Unsigned.toUint32;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Spinner;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
public class Bcs {
    TreeItem<String> skeletonsEntries;

    BcsPartSet bcsPartSet = new BcsPartSet();
    
    HashMap<TreeItem<String>, BcsPart> bcsPartsHashMap = new HashMap<>();
    HashMap<TreeItem<String>, BcsColorSelector> bcsColorsSelectorHashMap = new HashMap<>();
    HashMap<TreeItem<String>, BcsPhysics> bcsPhysicsHashMap = new HashMap<>();
    HashMap<TreeItem<String>, BcsUnknown3> bcsUnknown3HashMap = new HashMap<>();
    HashMap<TreeItem<String>, BcsPartColor> bcsPartColorsHashMap = new HashMap<>();
    HashMap<TreeItem<String>, BcsColor> bcsColorsHashMap = new HashMap<>();
    HashMap<TreeItem<String>, BcsBoneScale> bcsBoneScalesHashMap = new HashMap<>();
    HashMap<TreeItem<String>, BcsSkeleton> bcsSkeletonsHashMap = new HashMap<>();
    HashMap<TreeItem<String>, BcsBone> bcsBonesHashMap = new HashMap<>();

    TreeView<String> partSetsTreeView = new TreeView<>();
    TreeView<String> partColorsTreeView = new TreeView<>();
    TreeView<String> bodiesTreeView = new TreeView<>();
    TreeView<String> skeletonsTreeView = new TreeView<>();

    TreeItem<String> currentPartSetEntry = new TreeItem<>();
    TreeItem<String> currentPartColorEntry = new TreeItem<>();
    TreeItem<String> currentBodyEntry = new TreeItem<>();
    TreeItem<String> currentSkeletonEntry = new TreeItem<>();
    TreeItem<String> partSetGrandParentEntry = new TreeItem<>();
    TreeItem<String> partColorGrandParentEntry = new TreeItem<>();
    TreeItem<String> bodyGrandParentEntry = new TreeItem<>();
    TreeItem<String> skeletonGrandParentEntry = new TreeItem<>();

    ObservableList<String> partColorsObservableList =  FXCollections.observableArrayList();
    List<ObservableList<String>> colorsObservableList = new ArrayList<>();
    
    TabPane mainTabPane = new TabPane();
    TabPane dynamicTabPane = new TabPane();

    ContextMenu partSetContextMenu = new ContextMenu();
    MenuItem addPartSet = new MenuItem("Add Part Set");
    Menu addPart = new Menu("Add Part");
    MenuItem addColorSelector = new MenuItem("Add Color Selector");
    MenuItem addPhysics = new MenuItem("Add Physics");
    MenuItem addUnknown3 = new MenuItem("Add Unknown 3");
    MenuItem copyPartSetItem = new MenuItem("Copy Ctrl+C");
    MenuItem deletePartSetItem = new MenuItem("Delete Del");
    MenuItem noCopiedPartSetItemFound = new MenuItem("no copied item found");
    MenuItem copiedPartSetItem = new MenuItem();
    MenuItem pastePartSetItem = new MenuItem("dummy");
    MenuItem addPartSetItemCopy = new MenuItem("dummy");

    MenuItem faceBase = new MenuItem("Face Base");
    MenuItem faceForehead = new MenuItem("Face Forehead");
    MenuItem faceEye = new MenuItem("Face Eye");
    MenuItem faceNose = new MenuItem("Face Nose");
    MenuItem faceEar = new MenuItem("Face Ear");
    MenuItem hair = new MenuItem("Hair");
    MenuItem bust = new MenuItem("Bust");
    MenuItem pants = new MenuItem("Pants");
    MenuItem rist = new MenuItem("Rist");
    MenuItem boots = new MenuItem("Boots");

    ContextMenu partColorContextMenu = new ContextMenu();
    MenuItem addPartColor = new MenuItem("Add Part Color");
    MenuItem addColor = new MenuItem("Add Color");
    MenuItem copyPartColorItem = new MenuItem("Copy Ctrl+C");
    MenuItem deletePartColorItem = new MenuItem("Delete Del");
    MenuItem noCopiedPartColorItemFound = new MenuItem("no copied item found");
    MenuItem copiedPartColorItem = new MenuItem();
    MenuItem pastePartColorItem = new MenuItem("dummy");
    MenuItem addPartColorItemCopy = new MenuItem();

    ContextMenu bodyContextMenu = new ContextMenu();
    MenuItem addBody = new MenuItem("Add Body");
    MenuItem addBoneScale = new MenuItem("Add Bone Scale");
    MenuItem copyBodyItem = new MenuItem("Copy Ctrl+C");
    MenuItem deleteBodyItem = new MenuItem("Delete Del");
    MenuItem noCopiedBodyItemFound = new MenuItem("no copied item found");
    MenuItem copiedBodyItem = new MenuItem();
    MenuItem pasteBodyItem = new MenuItem("dummy");
    MenuItem addBodyItemCopy = new MenuItem();

    ContextMenu skeletonContextMenu = new ContextMenu();
    MenuItem addSkeleton = new MenuItem("Add Skeleton");
    MenuItem addBone = new MenuItem("Add Bone");
    MenuItem copySkeletonItem = new MenuItem("Copy Ctrl+C");
    MenuItem deleteSkeletonItem = new MenuItem("Delete Del");
    MenuItem noCopiedSkeletonItemFound = new MenuItem("no copied item found");
    MenuItem copiedSkeletonItem = new MenuItem();
    MenuItem pasteSkeletonItem = new MenuItem("dummy");
    MenuItem addSkeletonItemCopy = new MenuItem("dummy");

    Object copyContainer = new Object();
    Object[] copyPartsContainer;
    String[] copySubTypesContainer;
    Object[][] copyListContainer;
    String[][] copyTypesContainer;
    Object[][][] copyPartSetContainer;

    int allPartSetEntries = 0;
    int allPartColorEntries = 0;
    int allBodyEntries = 0;
    int version = 0;

    int thisPartSetOffset = 0;
    int thisPartColorOffset = 0;
    int thisBodyOffset = 0;
    int thisSkeleton1Offset = 0;
    int thisSkeleton2Offset = 0;
    int relativeOffset = 0;

    int typesSum = 0;

    public Bcs() {
        createTabs();

        tabsActionListener();
        partSetsActionListener();
        partColorsActionListener();
        bodiesActionListener();
        skeletonsActionListener();

        partSetsKeysListener();
        partColorsKeysListener();
        bodiesKeysListener();
        skeletonsKeysListener();
    }

    public SplitPane createSplitPane() {
        createTabs();
        SplitPane splitPane = new SplitPane(createVBox(),dynamicTabPane);
        splitPane.setDividerPositions(0.3);
        splitPane.getStylesheets().add(getClass().getResource("/style.css").toExternalForm()); 

        return splitPane;
    }

    private VBox createVBox() {
        mainTabPane.getTabs().get(0).setContent(partSetsTreeView);
        VBox.setVgrow(mainTabPane, Priority.ALWAYS);

        return new VBox(createMainHBox(bcsPartSet), mainTabPane);
    }

    private HBox createMainHBox(BcsPartSet bcsMainEntry) {
        HBox hBox = new HBox(15, 
            createComboBox(FXCollections.observableArrayList("Male", "Female"), BcsPartSetValues.Gender), 
            createComboBox(FXCollections.observableArrayList("Human", "Saiyan", "Namekian", "Frieza Race", "Majin", "Other"), BcsPartSetValues.Race)
        );
        hBox.setPadding(new Insets(10, 0, 10, 16));
        
        return hBox;
    }

    private void createPart(BcsPart entry) {
        CheckBox[] dytOptions1 = new CheckBox[] {
            new CheckBox("Unknown 1"),
            new CheckBox("Use Texture DYT Path"),
            new CheckBox("Use DYT Ramps From Texture EMB"),
            new CheckBox("Green Scouter Overlay")
        };

        CheckBox[] dytOptions2 = new CheckBox[] {
            new CheckBox("Red Scouter Overlay"),
            new CheckBox("Blue Scouter Overlay"),
            new CheckBox("Purple Scouter Overlay"),
            new CheckBox("Unknown 8")
        };

        CheckBox[] dytOptions3 = new CheckBox[] {
            new CheckBox("Unknown 9"),
            new CheckBox("Orange Scouter Overlay")
        };

        CheckBox[] partHidingGroup1 = new CheckBox[] {
            new CheckBox("Face Base"),
            new CheckBox("Face Forehead"),
            new CheckBox("Face Eye"),
            new CheckBox("Face Nose")
        };

        CheckBox[] partHidingGroup2 = new CheckBox[] {
            new CheckBox("Face Ear"),
            new CheckBox("Hair"),
            new CheckBox("Bust"),
            new CheckBox("Pants")
        };

        CheckBox[] partHidingGroup3 = new CheckBox[] {
            new CheckBox("Rist"),
            new CheckBox("Boots")
        };

        CheckBox[] matHidingGroup1 = new CheckBox[] {
            new CheckBox("Face Base"),
            new CheckBox("Face Forehead"),
            new CheckBox("Face Eye"),
            new CheckBox("Face Nose")
        };

        CheckBox[] matHidingGroup2 = new CheckBox[] {
            new CheckBox("Face Ear"),
            new CheckBox("Hair"),
            new CheckBox("Bust"),
            new CheckBox("Pants")
        };

        CheckBox[] matHidingGroup3 = new CheckBox[] {
            new CheckBox("Rist"),
            new CheckBox("Boots")
        };

        Node[] dytOptons = new Node[] {
            createCheckBoxGroup(dytOptions1, 1, BcsPartValues.Flags),
            createCheckBoxGroup(dytOptions2, 16, BcsPartValues.Flags),
            createCheckBoxGroup(dytOptions3, 256, BcsPartValues.Flags)
        };

        Node[] partHiding = new Node[] {
            createCheckBoxGroup(partHidingGroup1, 1, BcsPartValues.HideFlags),
            createCheckBoxGroup(partHidingGroup2, 16, BcsPartValues.HideFlags),
            createCheckBoxGroup(partHidingGroup3, 256, BcsPartValues.HideFlags)
        };

        Node[] matHiding = new Node[] {
            createCheckBoxGroup(matHidingGroup1, 1, BcsPartValues.HideMatFlags),
            createCheckBoxGroup(matHidingGroup2, 16, BcsPartValues.HideMatFlags),
            createCheckBoxGroup(matHidingGroup3, 256, BcsPartValues.HideMatFlags)
        };

        VBox partVBox = new VBox(25, 
            createHBox(0, createLabel("Chara Code", 100), createTextField(entry.charaCode, BcsPartValues.CharaCode)),
            createHBox(0, createLabel("Model", 100), createSpinner(Short.MIN_VALUE, Short.MAX_VALUE, entry.model, BcsPartValues.Model)),
            createHBox(0, createLabel("Model 2", 100), createSpinner(Short.MIN_VALUE, Short.MAX_VALUE, entry.model2, BcsPartValues.Model2)), 
            createHBox(0, createLabel("DYT Index", 100), createSpinner(Short.MIN_VALUE, Short.MAX_VALUE, entry.texture, BcsPartValues.Texture)),
            createHBox(0, createLabel("Shader", 100), createSpinner(Short.MIN_VALUE, Short.MAX_VALUE, entry.shader, BcsPartValues.Shader)),
            createHBox(0, createLabel("EMD Name", 100), createTextField(entry.emdName, BcsPartValues.EMD_Name)),
            createHBox(0, createLabel("EMM Name", 100), createTextField(entry.emmName, BcsPartValues.EMM_Name)),
            createHBox(0, createLabel("EMB Name", 100), createTextField(entry.embName, BcsPartValues.EMB_Name)),
            createHBox(0, createLabel("EAN Name", 100), createTextField(entry.eanName, BcsPartValues.EAN_Name)), 
            createHBox(0, createLabel("DYT Options", 100), createHBox(5, dytOptons, false)),
            createHBox(0, createLabel("Part Hiding", 100), createHBox(5, partHiding, false)),
            createHBox(0, createLabel("Mat Hiding", 100), createHBox(5, matHiding, false))
        );
        partVBox.setPadding(new Insets(20, 0, 20, 16));

        VBox unknownVBox = new VBox(25, 
            createHBox(0, createLabel("F_36", 60), createTextField(entry.f36, BcsPartValues.F36)),
            createHBox(0, createLabel("F_40", 60), createTextField(entry.f40, BcsPartValues.F40)),
            createHBox(0, createLabel("I_44", 60), createTextField(entry.i44, BcsPartValues.I44)),
            createHBox(0, createLabel("I_48", 60), createTextField(entry.i48, BcsPartValues.I48))
        );
        unknownVBox.setPadding(new Insets(20, 0, 0, 16));

        Tab partTab = new Tab("Part", new ScrollPane(partVBox));
        partTab.setClosable(false);

        Tab unknownTab = new Tab("Unknown", unknownVBox);
        unknownTab.setClosable(false);

        dynamicTabPane.getTabs().addAll(partTab, unknownTab);
    }

    private void createColorSelector(BcsColorSelector entry) {
        ComboBox<String> colorComboBox = createComboBox(120, colorsObservableList.get(entry.partColorGroup), BcsColorSelectorValues.ColorIndex, null);
        
        VBox colorSelectorVBox = new VBox(25, 
            createHBox(0, createLabel("Part Colors", 80), createComboBox(120, partColorsObservableList, BcsColorSelectorValues.PartColorGroup, colorComboBox)),
            createHBox(0, createLabel("Color", 80), colorComboBox)
        );
        colorSelectorVBox.setPadding(new Insets(20, 0, 0, 16));

        Tab colorSelectorTab = new Tab("Color Selector", colorSelectorVBox);
        colorSelectorTab.setClosable(false);

        dynamicTabPane.getTabs().add(colorSelectorTab);
    }

    private void createPhysics(BcsPhysics entry) {
        CheckBox[] dytOptions1 = new CheckBox[] {
            new CheckBox("Unknown 1"),
            new CheckBox("Use Texture DYT Path"),
            new CheckBox("Use DYT Ramps From Texture EMB"),
            new CheckBox("Green Scouter Overlay")
        };

        CheckBox[] dytOptions2 = new CheckBox[] {
            new CheckBox("Red Scouter Overlay"),
            new CheckBox("Blue Scouter Overlay"),
            new CheckBox("Purple Scouter Overlay"),
            new CheckBox("Unknown 8")
        };

        CheckBox[] dytOptions3 = new CheckBox[] {
            new CheckBox("Unknown 9"),
            new CheckBox("Orange Scouter Overlay")
        };

        CheckBox[] partHidingGroup1 = new CheckBox[] {
            new CheckBox("Face Base"),
            new CheckBox("Face Forehead"),
            new CheckBox("Face Eye"),
            new CheckBox("Face Nose")
        };

        CheckBox[] partHidingGroup2 = new CheckBox[] {
            new CheckBox("Face Ear"),
            new CheckBox("Hair"),
            new CheckBox("Bust"),
            new CheckBox("Pants")
        };

        CheckBox[] partHidingGroup3 = new CheckBox[] {
            new CheckBox("Rist"),
            new CheckBox("Boots")
        };

        CheckBox[] matHidingGroup1 = new CheckBox[] {
            new CheckBox("Face Base"),
            new CheckBox("Face Forehead"),
            new CheckBox("Face Eye"),
            new CheckBox("Face Nose")
        };

        CheckBox[] matHidingGroup2 = new CheckBox[] {
            new CheckBox("Face Ear"),
            new CheckBox("Hair"),
            new CheckBox("Bust"),
            new CheckBox("Pants")
        };

        CheckBox[] matHidingGroup3 = new CheckBox[] {
            new CheckBox("Rist"),
            new CheckBox("Boots")
        };

        Node[] dytOptons = new Node[] {
            createCheckBoxGroup(dytOptions1, 1, BcsPhysicsValues.Flags),
            createCheckBoxGroup(dytOptions2, 16, BcsPhysicsValues.Flags),
            createCheckBoxGroup(dytOptions3, 256, BcsPhysicsValues.Flags)
        };

        Node[] partHiding = new Node[] {
            createCheckBoxGroup(partHidingGroup1, 1, BcsPhysicsValues.HideFlags),
            createCheckBoxGroup(partHidingGroup2, 16, BcsPhysicsValues.HideFlags),
            createCheckBoxGroup(partHidingGroup3, 256, BcsPhysicsValues.HideFlags)
        };

        Node[] matHiding = new Node[] {
            createCheckBoxGroup(matHidingGroup1, 1, BcsPhysicsValues.HideMatFlags),
            createCheckBoxGroup(matHidingGroup2, 16, BcsPhysicsValues.HideMatFlags),
            createCheckBoxGroup(matHidingGroup3, 256, BcsPhysicsValues.HideMatFlags)
        };

        VBox physicsVBox = new VBox(25, 
            createHBox(0, createLabel("Chara Code", 100), createTextField(entry.charaCode, BcsPhysicsValues.CharaCode)), 
            createHBox(0, createLabel("Model", 100), createSpinner(Short.MIN_VALUE, Short.MAX_VALUE, entry.model, BcsPhysicsValues.Model)),
            createHBox(0, createLabel("Model 2", 100), createSpinner(Short.MIN_VALUE, Short.MAX_VALUE, entry.model2, BcsPhysicsValues.Model2)), 
            createHBox(0, createLabel("DTY Index", 100), createSpinner(Short.MIN_VALUE, Short.MAX_VALUE, entry.texture, BcsPhysicsValues.Texture)),
            createHBox(0, createLabel("EMD Name", 100), createTextField(entry.emdName, BcsPhysicsValues.EMD_Name)),
            createHBox(0, createLabel("EMM Name", 100), createTextField(entry.emmName, BcsPhysicsValues.EMM_Name)),
            createHBox(0, createLabel("EMB Name", 100), createTextField(entry.embName, BcsPhysicsValues.EMB_Name)),
            createHBox(0, createLabel("ESK Name", 100), createTextField(entry.eskName, BcsPhysicsValues.ESK_Name)), 
            createHBox(0, createLabel("Bone Name", 100), createTextField(entry.boneToAttach, BcsPhysicsValues.BoneToAttatch)), 
            createHBox(0, createLabel("SCD Name", 100), createTextField(entry.scdName, BcsPhysicsValues.SCD_Name)),
            createHBox(0, createLabel("DYT Options", 100), createHBox(5, dytOptons, false)),
            createHBox(0, createLabel("Part Hiding", 100), createHBox(5, partHiding, false)),
            createHBox(0, createLabel("Mat Hiding", 100), createHBox(5, matHiding, false))
        );
        physicsVBox.setPadding(new Insets(20, 0, 20, 16));

        Tab physicsTab = new Tab("Physics", new ScrollPane(physicsVBox));
        physicsTab.setClosable(false);

        dynamicTabPane.getTabs().add(physicsTab);
    }

    private void createUnknown3(BcsUnknown3 entry) {
        VBox unknownVBox = new VBox(25,
            createHBox(0, createLabel("I_00", 60), createTextField(entry.i00, BcsUnknown3Values.I00)), 
            createHBox(0, createLabel("I_02", 60), createTextField(entry.i02, BcsUnknown3Values.I02)),
            createHBox(0, createLabel("I_04", 60), createTextField(entry.i04, BcsUnknown3Values.I04)), 
            createHBox(0, createLabel("I_06", 60), createTextField(entry.i06, BcsUnknown3Values.I06)),
            createHBox(0, createLabel("I_08", 60), createTextField(entry.i08, BcsUnknown3Values.I08)), 
            createHBox(0, createLabel("I_10", 60), createTextField(entry.i10, BcsUnknown3Values.I10))
        );
        unknownVBox.setPadding(new Insets(20, 0, 0, 16));

        Tab unknownTab = new Tab("Unknown", unknownVBox);
        unknownTab.setClosable(false);

        dynamicTabPane.getTabs().add(unknownTab);
    }

    public void createPartColor(BcsPartColor entry) {
        HBox nameHBox = createHBox(0, createLabel("Name", 60), createTextField(entry.name, BcsPartColorValues.Name));
        nameHBox.setAlignment(Pos.BASELINE_LEFT);
        nameHBox.setPadding(new Insets(20, 0, 0, 16));

        Tab partColorTab = new Tab("Part Color", nameHBox);
        partColorTab.setClosable(false);

        dynamicTabPane.getTabs().add(partColorTab);
    }

    public void createColor(BcsColor entry) {
        VBox colorVBox = new VBox(25,
            createHBox(0, createLabel("Color 1", 60), createColorPicker(entry.color1, BcsColorValues.Color1)), 
            createHBox(0, createLabel("Color 2", 60), createColorPicker(entry.color2, BcsColorValues.Color2)), 
            createHBox(0, createLabel("Color 3", 60), createColorPicker(entry.color3, BcsColorValues.Color3)),  
            createHBox(0, createLabel("Color 4", 60), createColorPicker(entry.color4, BcsColorValues.Color4))
        );
        colorVBox.setPadding(new Insets(20, 0, 0, 16));

        Tab colorTab = new Tab("Color", colorVBox);
        colorTab.setClosable(false);

        dynamicTabPane.getTabs().add(colorTab);
    }

    public void createBoneScale(BcsBoneScale entry) {
        VBox boneScaleVBox = new VBox(25, 
            createHBox(0, createLabel("Bone Name", 80), createTextField(entry.boneName, BcsBoneScaleValues.Bone_Name)), 
            createHBox(0, createLabel("Scale X", 80), createSpinner(Short.MIN_VALUE, Short.MAX_VALUE, entry.scaleX, BcsBoneScaleValues.ScaleX)),
            createHBox(0, createLabel("Scale Y", 80), createSpinner(Short.MIN_VALUE, Short.MAX_VALUE, entry.scaleY, BcsBoneScaleValues.ScaleY)), 
            createHBox(0, createLabel("Scale Z", 80), createSpinner(Short.MIN_VALUE, Short.MAX_VALUE, entry.scaleZ, BcsBoneScaleValues.ScaleZ))
        );
        boneScaleVBox.setPadding(new Insets(20, 0, 0, 16));

        Tab boneScaleTab = new Tab("Bone Scale", boneScaleVBox);
        boneScaleTab.setClosable(false);

        dynamicTabPane.getTabs().add(boneScaleTab);
    }

    private void createSkeleton(BcsSkeleton entry) {
        HBox i00HBox = createHBox(0, createLabel("I_00", 60), createTextField(entry.i00, BcsSkeletonValues.I00));
        i00HBox.setPadding(new Insets(20, 0, 0, 16));
        i00HBox.setAlignment(Pos.BASELINE_LEFT);

        Tab skeletonTab = new Tab("Skeleton", i00HBox);
        skeletonTab.setClosable(false);

        dynamicTabPane.getTabs().add(skeletonTab);
    }

    public void createBone(BcsBone entry) {
        HBox boneNameHBox = createHBox(0, createLabel("Bone Name", 60), createTextField(entry.boneName, BcsBoneValues.BoneName));
        boneNameHBox.setPadding(new Insets(20, 0, 0, 16));
        boneNameHBox.setAlignment(Pos.BASELINE_LEFT);

        VBox unknownVBox = new VBox(25, 
            createHBox(0, createLabel("I_00", 60), createTextField(entry.i00, BcsBoneValues.I00)), 
            createHBox(0, createLabel("I_04", 60), createTextField(entry.i04, BcsBoneValues.I04)),
            createHBox(0, createLabel("F_12", 60), createTextField(entry.f12, BcsBoneValues.F12)),
            createHBox(0, createLabel("F_16", 60), createTextField(entry.f16, BcsBoneValues.F16)),
            createHBox(0, createLabel("F_20", 60), createTextField(entry.f20, BcsBoneValues.F20)), 
            createHBox(0, createLabel("F_24", 60), createTextField(entry.f24, BcsBoneValues.F24)),
            createHBox(0, createLabel("F_28", 60), createTextField(entry.f28, BcsBoneValues.F28)), 
            createHBox(0, createLabel("F_32", 60), createTextField(entry.f32, BcsBoneValues.F32)),
            createHBox(0, createLabel("F_36", 60), createTextField(entry.f36, BcsBoneValues.F36)), 
            createHBox(0, createLabel("F_40", 60), createTextField(entry.f40, BcsBoneValues.F40)),
            createHBox(0, createLabel("F_44", 60), createTextField(entry.f44, BcsBoneValues.F44))
        );
        unknownVBox.setPadding(new Insets(20, 0, 0, 16));

        Tab boneTab = new Tab("Bone", boneNameHBox);
        boneTab.setClosable(false);

        Tab unknownTab = new Tab("Unknown", unknownVBox);
        unknownTab.setClosable(false);

        dynamicTabPane.getTabs().addAll(boneTab, unknownTab);
    }

    private VBox createPropertiesVBox(BcsPartSet entry) {
        VBox propertiesVBox = new VBox(30, 
            createHBox(0, createLabel("I_46", 100), createTextField(entry.i46, BcsPartSetValues.I46)), 
            createHBox(0, createLabel("I_47", 100), createTextField(entry.i47, BcsPartSetValues.I47)), 
            createHBox(0, createLabel("Position Y (CMN)", 100), createTextField(entry.positionY, BcsPartSetValues.PositionY)), 
            createHBox(0, createLabel("Camera Y", 100), createTextField(entry.cameraY, BcsPartSetValues.CameraY)),
            createHBox(0, createLabel("Tracking Offset", 100), createTextField(entry.trackingOffset, BcsPartSetValues.TrackingOffset)), 
            createHBox(0, createLabel("F_60", 100), createTextField(entry.f60, BcsPartSetValues.F60)),
            createHBox(0, createLabel("Collision Scale", 100), createTextField(entry.collisionScale, BcsPartSetValues.CollisionScale)), 
            createHBox(0, createLabel("F_68", 100), createTextField(entry.f68, BcsPartSetValues.F68)),
            createHBox(0, createLabel("F_72", 100), createTextField(entry.f72, BcsPartSetValues.F72))
        );
        propertiesVBox.setPadding(new Insets(20, 0, 0, 16));

        return propertiesVBox;
    }

    private ComboBox<String> createComboBox(ObservableList<String> observableList, BcsPartSetValues bcsPartSetValue) {
        ComboBox<String> comboBox = new ComboBox<>(observableList);

        switch (bcsPartSetValue) {
            case Gender -> {
                comboBox.getSelectionModel().select(bcsPartSet.gender);

                comboBox.getSelectionModel().selectedIndexProperty().addListener((obs, oldValue, newValue) -> {
                    if (newValue != null) {
                        bcsPartSet.gender = newValue.intValue();
                    }
                });
            }
            case Race -> {
                comboBox.getSelectionModel().select(bcsPartSet.race);

                comboBox.getSelectionModel().selectedIndexProperty().addListener((obs, oldValue, newValue) -> {
                    if (newValue != null) {
                        bcsPartSet.race = newValue.intValue();
                    }
                });
            }
            default -> throw new IllegalArgumentException("Unexpected value: " + bcsPartSetValue);
        }

        return  comboBox;
    }

    private ComboBox<String> createComboBox(int width, ObservableList<String> observableList, BcsColorSelectorValues bcsColorSelectorValue, ComboBox<String> colorsComboBox) {
        ComboBox<String> comboBox = new ComboBox<>(observableList);
        comboBox.setPrefWidth(width);

        switch (bcsColorSelectorValue) {
            case ColorIndex -> {
                comboBox.getSelectionModel().select(bcsColorsSelectorHashMap.get(currentPartSetEntry).colorIndex);

                comboBox.getSelectionModel().selectedIndexProperty().addListener((obs, oldValue, newValue) -> {
                    if (newValue != null) {
                        bcsColorsSelectorHashMap.get(currentPartSetEntry).colorIndex = newValue.intValue();
                    }
                });
            }
            case PartColorGroup -> {
                comboBox.getSelectionModel().select(bcsColorsSelectorHashMap.get(currentPartSetEntry).partColorGroup);

                comboBox.getSelectionModel().selectedIndexProperty().addListener((obs, oldValue, newValue) -> {
                    if (newValue != null) {
                        bcsColorsSelectorHashMap.get(currentPartSetEntry).partColorGroup = newValue.intValue();
                        colorsComboBox.setItems(colorsObservableList.get(bcsColorsSelectorHashMap.get(currentPartSetEntry).partColorGroup));
                    }
                });
            }
            default -> throw new IllegalArgumentException("Unexpected value: " + bcsColorSelectorValue);
        }

        return  comboBox;
    }

    private Spinner<Number> createSpinner(Number MIN_VALUE, Number MAX_VALUE, Number value, BcsPartValues bcsPartValues) {
        Spinner<Number> spinner = new Spinner<>(MIN_VALUE.intValue(), MAX_VALUE.intValue(), value.intValue());

        spinner.setEditable(true);
        spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                switch (bcsPartValues) {
                    case Model -> bcsPartsHashMap.get(currentPartSetEntry).model = newValue.shortValue();
                    case Model2 -> bcsPartsHashMap.get(currentPartSetEntry).model2 = newValue.shortValue();
                    case Texture -> bcsPartsHashMap.get(currentPartSetEntry).texture = newValue.shortValue();
                    case Shader -> bcsPartsHashMap.get(currentPartSetEntry).shader = newValue.shortValue();
                    default -> throw new IllegalArgumentException("Unexpected value: " + bcsPartValues);
                }   
            }
        });

        return spinner;
    }

    private Spinner<Number> createSpinner(Number MIN_VALUE, Number MAX_VALUE, Number value, BcsPhysicsValues bcsPhysicsValues) {
        Spinner<Number> spinner = new Spinner<>(MIN_VALUE.intValue(), MAX_VALUE.intValue(), value.intValue());

        spinner.setEditable(true);
        spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                switch (bcsPhysicsValues) {
                    case Model -> bcsPhysicsHashMap.get(currentPartSetEntry).model = newValue.shortValue();
                    case Model2 -> bcsPhysicsHashMap.get(currentPartSetEntry).model2 = newValue.shortValue();
                    case Texture -> bcsPhysicsHashMap.get(currentPartSetEntry).texture = newValue.shortValue();
                    default -> throw new IllegalArgumentException("Unexpected value: " + bcsPhysicsValues);
                }   
            }
        });

        return spinner;
    }

    private Spinner<Number> createSpinner(Number MIN_VALUE, Number MAX_VALUE, Number value, BcsBoneScaleValues bcsBoneScaleValue) {
        Spinner<Number> spinner = new Spinner<>(MIN_VALUE.doubleValue(), MAX_VALUE.doubleValue(), value.doubleValue());

        spinner.setEditable(true);
        spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                switch (bcsBoneScaleValue) {
                    case ScaleX -> bcsBoneScalesHashMap.get(currentBodyEntry).scaleX = newValue.floatValue();
                    case ScaleY -> bcsBoneScalesHashMap.get(currentBodyEntry).scaleY = newValue.floatValue();
                    case ScaleZ -> bcsBoneScalesHashMap.get(currentBodyEntry).scaleZ = newValue.floatValue();
                    default -> throw new IllegalArgumentException("Unexpected value: " + bcsBoneScaleValue);
                }   
            }
        });

        return spinner;
    }

    private TextField createTextField(Number value, BcsPartSetValues bcsPartSetValue) {
        TextField textField = new TextField(String.valueOf(value));
        textField.textProperty().addListener((obs, oldText, newText) -> {
            try {
                switch (bcsPartSetValue) {
                    case I46 -> bcsPartSet.i46 = Integer.parseInt(newText);
                    case I47 -> bcsPartSet.i47 = Integer.parseInt(newText);
                    case PositionY -> bcsPartSet.positionY = Float.parseFloat(newText);
                    case CameraY -> bcsPartSet.cameraY = Float.parseFloat(newText);
                    case TrackingOffset -> bcsPartSet.trackingOffset = Float.parseFloat(newText);
                    case F60 -> bcsPartSet.f60 = Float.parseFloat(newText);
                    case CollisionScale -> bcsPartSet.collisionScale = Float.parseFloat(newText);
                    case F68 -> bcsPartSet.f68 = Float.parseFloat(newText);
                    case F72 -> bcsPartSet.f72 = Float.parseFloat(newText);
                    default -> throw new IllegalArgumentException("Unexpected value: " + bcsPartSetValue);
                }
            } catch (NumberFormatException e) {
            }
        });

        return textField;
    }

    private TextField createTextField(Object value, BcsPartValues bcsPartValue) {
        TextField textField = new TextField(String.valueOf(value));
        textField.textProperty().addListener((obs, oldText, newText) -> {
            try {
                switch (bcsPartValue) {
                    case EMD_Name -> bcsPartsHashMap.get(currentPartSetEntry).emdName = newText;
                    case EMM_Name -> bcsPartsHashMap.get(currentPartSetEntry).emmName = newText;
                    case EMB_Name -> bcsPartsHashMap.get(currentPartSetEntry).embName = newText;
                    case EAN_Name -> bcsPartsHashMap.get(currentPartSetEntry).eanName = newText;
                    case CharaCode -> bcsPartsHashMap.get(currentPartSetEntry).charaCode = newText;
                    case F36 -> bcsPartsHashMap.get(currentPartSetEntry).f36 = Float.parseFloat(newText);
                    case F40 -> bcsPartsHashMap.get(currentPartSetEntry).f40 = Float.parseFloat(newText);
                    case I44 -> bcsPartsHashMap.get(currentPartSetEntry).i44 = Integer.parseInt(newText);
                    case I48 -> bcsPartsHashMap.get(currentPartSetEntry).i48 = Integer.parseInt(newText);
                    default -> throw new IllegalArgumentException("Unexpected value: " + bcsPartValue);
                }
            } catch (NumberFormatException e) {
            }
        });

        return textField;
    }

    private TextField createTextField(String value, BcsPhysicsValues bcsPhysicsValue) {
        TextField textField = new TextField(value);
        textField.textProperty().addListener((obs, oldText, newText) -> {
            try {
                switch (bcsPhysicsValue) {
                    case EMD_Name -> bcsPhysicsHashMap.get(currentPartSetEntry).emdName = newText;
                    case EMM_Name -> bcsPhysicsHashMap.get(currentPartSetEntry).emmName = newText;
                    case EMB_Name -> bcsPhysicsHashMap.get(currentPartSetEntry).embName = newText;
                    case ESK_Name -> bcsPhysicsHashMap.get(currentPartSetEntry).eskName = newText;
                    case CharaCode -> bcsPhysicsHashMap.get(currentPartSetEntry).charaCode = newText;
                    case BoneToAttatch -> bcsPhysicsHashMap.get(currentPartSetEntry).boneToAttach = newText;
                    case SCD_Name -> bcsPhysicsHashMap.get(currentPartSetEntry).scdName = newText;
                    default -> throw new IllegalArgumentException("Unexpected value: " + bcsPhysicsValue);
                }
            } catch (NumberFormatException e) {
            }
        });

        return textField;
    }

    private TextField createTextField(short value, BcsUnknown3Values bcsUnknown3Value) {
        TextField textField = new TextField(String.valueOf(value));
        textField.textProperty().addListener((obs, oldText, newText) -> {
            try {
                switch (bcsUnknown3Value) {
                    case I00 -> bcsUnknown3HashMap.get(currentPartSetEntry).i00 = Short.parseShort(newText);
                    case I02 -> bcsUnknown3HashMap.get(currentPartSetEntry).i02 = Short.parseShort(newText);
                    case I04 -> bcsUnknown3HashMap.get(currentPartSetEntry).i04 = Short.parseShort(newText);
                    case I06 -> bcsUnknown3HashMap.get(currentPartSetEntry).i06 = Short.parseShort(newText);
                    case I08 -> bcsUnknown3HashMap.get(currentPartSetEntry).i08 = Short.parseShort(newText);
                    case I10 -> bcsUnknown3HashMap.get(currentPartSetEntry).i10 = Short.parseShort(newText);
                    default -> throw new IllegalArgumentException("Unexpected value: " + bcsUnknown3Value);
                }
            } catch (NumberFormatException e) {
            }
        });

        return textField;
    }

    private TextField createTextField(String value, BcsPartColorValues bcsPartColorValue) {
        TextField textField = new TextField(value);
        textField.textProperty().addListener((obs, oldText, newText) -> {
            try {
                bcsPartColorsHashMap.get(currentPartColorEntry).name = newText;
                partColorsObservableList.set(Integer.parseInt(partColorGrandParentEntry.getValue().toString().replaceAll("\\D+", "")), newText);
            } catch (NumberFormatException e) {
            }
        });

        return textField;
    }

    private TextField createTextField(String value, BcsBoneScaleValues bcsBoneScaleValue) {
        TextField textField = new TextField(String.valueOf(value));
        textField.textProperty().addListener((obs, oldText, newText) -> {
            try {
                bcsBoneScalesHashMap.get(currentBodyEntry).boneName = newText;
            } catch (NumberFormatException e) {
            }
        });

        return textField;
    }

    private TextField createTextField(short value, BcsSkeletonValues bcsSkeletonValues) {
        TextField textField = new TextField(String.valueOf(value));
        textField.textProperty().addListener((obs, oldText, newText) -> {
            try {
                bcsSkeletonsHashMap.get(currentSkeletonEntry).i00 = Short.parseShort(newText);
            } catch (NumberFormatException e) {
            }
        });

        return textField;
    }

    private TextField createTextField(Object value, BcsBoneValues bcsBoneValue) {
        TextField textField = new TextField(String.valueOf(value));
        textField.textProperty().addListener((obs, oldText, newText) -> {
            try {
                switch (bcsBoneValue) {
                    case I00 -> bcsBonesHashMap.get(currentSkeletonEntry).i00 = Integer.parseInt(newText);
                    case I04 -> bcsBonesHashMap.get(currentSkeletonEntry).i04 = Integer.parseInt(newText);
                    case BoneName -> bcsBonesHashMap.get(currentSkeletonEntry).boneName = newText;
                    case F12 -> bcsBonesHashMap.get(currentSkeletonEntry).f12 = Float.parseFloat(newText);
                    case F16 -> bcsBonesHashMap.get(currentSkeletonEntry).f16 = Float.parseFloat(newText);
                    case F20 -> bcsBonesHashMap.get(currentSkeletonEntry).f20 = Float.parseFloat(newText);
                    case F24 -> bcsBonesHashMap.get(currentSkeletonEntry).f24 = Float.parseFloat(newText);
                    case F28 -> bcsBonesHashMap.get(currentSkeletonEntry).f28 = Float.parseFloat(newText);
                    case F32 -> bcsBonesHashMap.get(currentSkeletonEntry).f32 = Float.parseFloat(newText);
                    case F36 -> bcsBonesHashMap.get(currentSkeletonEntry).f36 = Float.parseFloat(newText);
                    case F40 -> bcsBonesHashMap.get(currentSkeletonEntry).f40 = Float.parseFloat(newText);
                    case F44 -> bcsBonesHashMap.get(currentSkeletonEntry).f44 = Float.parseFloat(newText);
                    default -> throw new IllegalArgumentException("Unexpected value: " + bcsBoneValue);
                }
            } catch (NumberFormatException e) {
            }
        });

        return textField;
    }

    private ColorPicker createColorPicker(Color color, BcsColorValues bcsColorValue) {
        ColorPicker colorPicker = new ColorPicker(color);
        colorPicker.setOnAction(e -> {
            switch (bcsColorValue) {
                case Color1 -> bcsColorsHashMap.get(currentPartColorEntry).color1 = colorPicker.getValue();
                case Color2 -> bcsColorsHashMap.get(currentPartColorEntry).color2 = colorPicker.getValue();
                case Color3 -> bcsColorsHashMap.get(currentPartColorEntry).color3 = colorPicker.getValue();
                case Color4 -> bcsColorsHashMap.get(currentPartColorEntry).color4 = colorPicker.getValue();
            }
        });

        return colorPicker;
    }

    private VBox createCheckBoxGroup(CheckBox[] checkBoxsList, int bitMask, BcsPartValues bcsPartValue) {
        VBox vBox = new VBox(2);
        vBox.getStyleClass().add("titled-address-box");
        vBox.setPadding(new Insets(12, 0, 0, 0));
        
        for (int i = 0; i < checkBoxsList.length; i++) {
            final int bitMaskLamda = bitMask;

            switch(bcsPartValue) {
                case Flags -> {
                    checkBoxsList[i].setSelected((bcsPartsHashMap.get(currentPartSetEntry).flags & bitMask) != 0);

                    checkBoxsList[i].selectedProperty().addListener((obs, oldValue, newValue) -> {
                        if (newValue) {
                            bcsPartsHashMap.get(currentPartSetEntry).flags |= bitMaskLamda;
                        }
                        else {
                            bcsPartsHashMap.get(currentPartSetEntry).flags &= ~bitMaskLamda;
                        }
                    });
                }
                case HideFlags -> {
                    checkBoxsList[i].setSelected((bcsPartsHashMap.get(currentPartSetEntry).hideFlags & bitMask) != 0);

                    checkBoxsList[i].selectedProperty().addListener((obs, oldValue, newValue) -> {
                        if (newValue) {
                            bcsPartsHashMap.get(currentPartSetEntry).hideFlags |= bitMaskLamda;
                        }
                        else {
                            bcsPartsHashMap.get(currentPartSetEntry).hideFlags &= ~bitMaskLamda;
                        }
                    });
                }
                case HideMatFlags -> {
                    checkBoxsList[i].setSelected((bcsPartsHashMap.get(currentPartSetEntry).hideMatFlags & bitMask) != 0);

                    checkBoxsList[i].selectedProperty().addListener((obs, oldValue, newValue) -> {
                        if (newValue) {
                            bcsPartsHashMap.get(currentPartSetEntry).hideMatFlags |= bitMaskLamda;
                        }
                        else {
                            bcsPartsHashMap.get(currentPartSetEntry).hideMatFlags &= ~bitMaskLamda;
                        }
                    });
                }
                default -> throw new IllegalArgumentException("Unexpected value: " + bcsPartValue);
            }

            vBox.getChildren().add(checkBoxsList[i]);

            bitMask <<= 1;
        }

        return vBox;
    }

    private VBox createCheckBoxGroup(CheckBox[] checkBoxsList, int bitMask, BcsPhysicsValues bcsPhysicsValue) {
        VBox vBox = new VBox(2);
        vBox.getStyleClass().add("titled-address-box");
        vBox.setPadding(new Insets(12, 0, 0, 0));
        
        for (int i = 0; i < checkBoxsList.length; i++) {
            final int bitMaskLamda = bitMask;

            switch(bcsPhysicsValue) {
                case Flags -> {
                    checkBoxsList[i].setSelected((bcsPhysicsHashMap.get(currentPartSetEntry).flags & bitMask) != 0);

                    checkBoxsList[i].selectedProperty().addListener((obs, oldValue, newValue) -> {
                        if (newValue) {
                            bcsPhysicsHashMap.get(currentPartSetEntry).flags |= bitMaskLamda;
                        }
                        else {
                            bcsPhysicsHashMap.get(currentPartSetEntry).flags &= ~bitMaskLamda;
                        }
                    });
                }
                case HideFlags -> {
                    checkBoxsList[i].setSelected((bcsPhysicsHashMap.get(currentPartSetEntry).hideFlags & bitMask) != 0);

                    checkBoxsList[i].selectedProperty().addListener((obs, oldValue, newValue) -> {
                        if (newValue) {
                            bcsPhysicsHashMap.get(currentPartSetEntry).hideFlags |= bitMaskLamda;
                        }
                        else {
                            bcsPhysicsHashMap.get(currentPartSetEntry).hideFlags &= ~bitMaskLamda;
                        }
                    });
                }
                case HideMatFlags -> {
                    checkBoxsList[i].setSelected((bcsPhysicsHashMap.get(currentPartSetEntry).hideMatFlags & bitMask) != 0);

                    checkBoxsList[i].selectedProperty().addListener((obs, oldValue, newValue) -> {
                        if (newValue) {
                            bcsPhysicsHashMap.get(currentPartSetEntry).hideMatFlags |= bitMaskLamda;
                        }
                        else {
                            bcsPhysicsHashMap.get(currentPartSetEntry).hideMatFlags &= ~bitMaskLamda;
                        }
                    });
                }
                default -> throw new IllegalArgumentException("Unexpected value: " + bcsPhysicsValue);
            }

            vBox.getChildren().add(checkBoxsList[i]);

            bitMask <<= 1;
        }

        return vBox;
    }

    private Label createLabel(String text, int width) {
        Label label = new Label(text);
        if (width != 0) label.setPrefWidth(width);

        return label;
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

    private void createTabs() {
        if (mainTabPane.getTabs().isEmpty()) {
            Tab partSetsTab = new Tab("Part Sets");
            Tab partColorsTab = new Tab("Part Colors");
            Tab bodiesTab = new Tab("Bodies");
            Tab skeletonsTab = new Tab("Skeletons");
            Tab propertiesTab = new Tab("Properties");

            partSetsTab.setClosable(false);
            partColorsTab.setClosable(false);
            bodiesTab.setClosable(false);
            skeletonsTab.setClosable(false);
            propertiesTab.setClosable(false);

            mainTabPane.getTabs().addAll(partSetsTab, partColorsTab, bodiesTab, skeletonsTab, propertiesTab);
        }
    }

    private void tabsActionListener() {
        mainTabPane.getSelectionModel().selectedItemProperty().addListener((obsevable, oldTab, newTab) -> {
            if (newTab == null) return;

            if (mainTabPane.getSelectionModel().getSelectedIndex() < 0) return;

            switch (mainTabPane.getSelectionModel().getSelectedIndex()) {
                case 0 -> {
                    partColorsTreeView.getSelectionModel().clearSelection();
                    bodiesTreeView.getSelectionModel().clearSelection();
                    skeletonsTreeView.getSelectionModel().clearSelection();

                    dynamicTabPane.getTabs().clear();

                    mainTabPane.getTabs().forEach(tab -> tab.setContent(null));
                    mainTabPane.getTabs().get(0).setContent(partSetsTreeView);

                    partSetsTreeView.getSelectionModel().select(currentPartSetEntry);
                }
                case 1 -> {
                    partSetsTreeView.getSelectionModel().clearSelection();
                    bodiesTreeView.getSelectionModel().clearSelection();
                    skeletonsTreeView.getSelectionModel().clearSelection();

                    dynamicTabPane.getTabs().clear();

                    mainTabPane.getTabs().forEach(tab -> tab.setContent(null));
                    mainTabPane.getTabs().get(1).setContent(partColorsTreeView);

                    partColorsTreeView.getSelectionModel().select(currentPartColorEntry);
                }
                case 2 -> {
                    partSetsTreeView.getSelectionModel().clearSelection();
                    partColorsTreeView.getSelectionModel().clearSelection();
                    skeletonsTreeView.getSelectionModel().clearSelection();
                    
                    dynamicTabPane.getTabs().clear();

                    mainTabPane.getTabs().forEach(tab -> tab.setContent(null));
                    mainTabPane.getTabs().get(2).setContent(bodiesTreeView);

                    bodiesTreeView.getSelectionModel().select(currentBodyEntry);
                }
                case 3 -> {
                    partSetsTreeView.getSelectionModel().clearSelection();
                    partColorsTreeView.getSelectionModel().clearSelection();
                    bodiesTreeView.getSelectionModel().clearSelection();
                    
                    dynamicTabPane.getTabs().clear();

                    mainTabPane.getTabs().forEach(tab -> tab.setContent(null));
                    mainTabPane.getTabs().get(3).setContent(skeletonsTreeView);

                    skeletonsTreeView.getSelectionModel().select(currentSkeletonEntry);
                }
                case 4 -> {
                    partSetsTreeView.getSelectionModel().clearSelection();
                    partColorsTreeView.getSelectionModel().clearSelection();
                    bodiesTreeView.getSelectionModel().clearSelection();
                    skeletonsTreeView.getSelectionModel().clearSelection();

                    dynamicTabPane.getTabs().clear();

                    mainTabPane.getTabs().forEach(tab -> tab.setContent(null));
                    mainTabPane.getTabs().get(4).setContent(createPropertiesVBox(bcsPartSet));
                }
            }
        });
    }

    private void partSetsActionListener() {
        addPart.getItems().addAll(
            faceBase, faceForehead, 
            faceEye, faceNose, 
            faceEar, hair, 
            bust, pants, 
            rist, boots
        );

        copiedPartSetItem.setVisible(false);
        copiedPartSetItem.setDisable(true);
        pastePartSetItem.setVisible(false);
        addPartSetItemCopy.setVisible(false);
        
        partSetContextMenu.getItems().addAll(
            addPartSet, addPart,
            addColorSelector, addPhysics,
            addUnknown3, 
            copyPartSetItem, deletePartSetItem, 
            noCopiedPartSetItemFound, copiedPartSetItem, 
            pastePartSetItem, addPartSetItemCopy
        );

        partSetsTreeView.setContextMenu(partSetContextMenu);
        partSetsTreeView.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue == null || newValue.getParent() == null) return;

            noCopiedPartSetItemFound.setDisable(true);
            pastePartSetItem.setDisable(true);
            addColorSelector.setDisable(true);
            addPhysics.setDisable(true);
            addUnknown3.setDisable(true);

            faceBase.setDisable(false);
            faceForehead.setDisable(false);
            faceEye.setDisable(false);
            faceNose.setDisable(false);
            faceEar.setDisable(false);
            hair.setDisable(false);
            bust.setDisable(false);
            pants.setDisable(false);
            rist.setDisable(false);
            boots.setDisable(false);

            addPartSetItemCopy.setDisable(false);

            currentPartSetEntry = newValue;
            partSetGrandParentEntry = newValue;

            try {
                while (partSetGrandParentEntry.getParent() != partSetsTreeView.getRoot()) {
                    partSetGrandParentEntry = partSetGrandParentEntry.getParent();
                }
            } catch (NullPointerException e) {
                return;
            }

            try {
                for (TreeItem<String> part : partSetGrandParentEntry.getChildren()) {
                    switch (part.getValue()) {
                        case "Face Base" -> {
                            faceBase.setDisable(true);
                            if (addPartSetItemCopy.getText().contains(part.getValue())) addPartSetItemCopy.setDisable(true);
                        }
                        case "Face Forehead" -> {
                            faceForehead.setDisable(true);
                            if (addPartSetItemCopy.getText().contains(part.getValue())) addPartSetItemCopy.setDisable(true);
                        }
                        case "Face Eye" -> {
                            faceEye.setDisable(true);
                            if (addPartSetItemCopy.getText().contains(part.getValue())) addPartSetItemCopy.setDisable(true);
                        }
                        case "Face Nose" -> {
                            faceNose.setDisable(true);
                            if (addPartSetItemCopy.getText().contains(part.getValue())) addPartSetItemCopy.setDisable(true);
                        }
                        case "Face Ear" -> {
                            faceEar.setDisable(true);
                            if (addPartSetItemCopy.getText().contains(part.getValue())) addPartSetItemCopy.setDisable(true);
                        }
                        case "Hair" -> {
                            hair.setDisable(true);
                            if (addPartSetItemCopy.getText().contains(part.getValue())) addPartSetItemCopy.setDisable(true);
                        }
                        case "Bust" -> {
                            bust.setDisable(true);
                            if (addPartSetItemCopy.getText().contains(part.getValue())) addPartSetItemCopy.setDisable(true);
                        }
                        case "Pants" -> {
                            pants.setDisable(true);
                            if (addPartSetItemCopy.getText().contains(part.getValue())) addPartSetItemCopy.setDisable(true);
                        }
                        case "Rist" -> {
                            rist.setDisable(true);
                            if (addPartSetItemCopy.getText().contains(part.getValue())) addPartSetItemCopy.setDisable(true);
                        }
                        case "Boots" -> {
                            boots.setDisable(true);
                            if (addPartSetItemCopy.getText().contains(part.getValue())) addPartSetItemCopy.setDisable(true);
                        }
                    }
                }
            } catch (Exception e) {
                return;
            }

            if (newValue.getParent().getValue().contains("Part Set")) {
                int index = dynamicTabPane.getSelectionModel().getSelectedIndex();

                dynamicTabPane.getTabs().clear();

                createPart(bcsPartsHashMap.get(newValue));

                dynamicTabPane.getSelectionModel().select(index);

                addColorSelector.setDisable(false);
                addPhysics.setDisable(false);
                if (version != 72) addUnknown3.setDisable(false);

                if (pastePartSetItem.getText().contains(newValue.getValue())) pastePartSetItem.setDisable(false);
            }
            else if (newValue.getParent().getValue().equals("Color Selectors")) {
                int index = dynamicTabPane.getSelectionModel().getSelectedIndex();

                dynamicTabPane.getTabs().clear();

                createColorSelector(bcsColorsSelectorHashMap.get(newValue));

                dynamicTabPane.getSelectionModel().select(index);

                addColorSelector.setDisable(false);

                if (!pastePartSetItem.getText().contains("List") && pastePartSetItem.getText().contains("Paste Color Selector")) pastePartSetItem.setDisable(false);
            }
            else if (newValue.getParent().getValue().equals("Physics")) {
                int index = dynamicTabPane.getSelectionModel().getSelectedIndex();

                dynamicTabPane.getTabs().clear();

                createPhysics(bcsPhysicsHashMap.get(newValue));

                dynamicTabPane.getSelectionModel().select(index);

                addPhysics.setDisable(false);

                if (!pastePartSetItem.getText().contains("List") && pastePartSetItem.getText().contains("Paste Physics")) pastePartSetItem.setDisable(false);
            }
            else if (newValue.getParent().getValue().equals("Unknown 3")) {
                int index = dynamicTabPane.getSelectionModel().getSelectedIndex();

                dynamicTabPane.getTabs().clear();

                createUnknown3(bcsUnknown3HashMap.get(newValue));

                dynamicTabPane.getSelectionModel().select(index);

                addUnknown3.setDisable(false);

                if (!pastePartSetItem.getText().contains("List") && pastePartSetItem.getText().contains("Paste Unknown 3")) pastePartSetItem.setDisable(false);
            }
            else {
                dynamicTabPane.getTabs().clear();

                switch (newValue.getValue()) {
                    case "Color Selectors" -> {
                        addColorSelector.setDisable(false);
                    }
                    case "Physics" -> {
                        addPhysics.setDisable(false);
                    }
                    case "Unknown 3" -> {
                        addUnknown3.setDisable(false);
                    }
                }
                
                if (newValue.getValue().contains("Part Set")){
                    if (pastePartSetItem.getText().contains("Paste Part Set")) pastePartSetItem.setDisable(false);

                    else if (addPartSetItemCopy.getText().contains("Color Selector") || addPartSetItemCopy.getText().contains("Physics") || addPartSetItemCopy.getText().contains("Unknown 3")) {
                        addPartSetItemCopy.setDisable(true);
                    }
                }
                else if (pastePartSetItem.getText().contains(newValue.getValue() + " List")) {
                    pastePartSetItem.setDisable(false);
                }
            }
        });

        partSetsTreeView.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.SECONDARY) {
                partSetContextMenu.setOnAction(event -> {
                    if (event.getTarget() == addPartSet) AddPartSet();
                    else if (event.getTarget() == copyPartSetItem) CopyPartSetItem();
                    else if (event.getTarget() == deletePartSetItem) DeletePartSetItem();
                    else if (event.getTarget() == addColorSelector) AddColorSelector();
                    else if (event.getTarget() == addPhysics) AddPhysics();
                    else if (event.getTarget() == addUnknown3) AddUnknown3();
                    else if (event.getTarget() == pastePartSetItem) PastePartSetItem();
                    else if (event.getTarget() == addPartSetItemCopy) AddPartSetItemCopy();
                });
            }
            addPart.setOnAction(ev -> {
                switch (addPart.getItems().indexOf(ev.getTarget())) {
                    case 0 -> {
                        TreeItem<String> faceBaseTreeItem = new TreeItem<>("Face Base");

                        partSetGrandParentEntry.getChildren().add(faceBaseTreeItem);
                        bcsPartsHashMap.put(faceBaseTreeItem, new BcsPart());
                 
                        sortPartSetItems(partSetGrandParentEntry);
                        faceBase.setDisable(true);

                        partSetsTreeView.getSelectionModel().select(faceBaseTreeItem);
                    }
                    case 1 -> {
                        TreeItem<String> faceForeheadTreeItem = new TreeItem<>("Face Forehead");

                        partSetGrandParentEntry.getChildren().add(faceForeheadTreeItem);
                        bcsPartsHashMap.put(faceForeheadTreeItem, new BcsPart());

                        sortPartSetItems(partSetGrandParentEntry);
                        faceForehead.setDisable(true);

                        partSetsTreeView.getSelectionModel().select(faceForeheadTreeItem);
                    }
                    case 2 -> {
                        TreeItem<String> faceEyeTreeItem = new TreeItem<>("Face Eye");

                        partSetGrandParentEntry.getChildren().add(faceEyeTreeItem);
                        bcsPartsHashMap.put(faceEyeTreeItem, new BcsPart());

                        sortPartSetItems(partSetGrandParentEntry);
                        faceEye.setDisable(true);

                        partSetsTreeView.getSelectionModel().select(faceEyeTreeItem);
                    }
                    case 3 -> {
                        TreeItem<String> faceNoseTreeItem = new TreeItem<>("Face Nose");

                        partSetGrandParentEntry.getChildren().add(faceNoseTreeItem);
                        bcsPartsHashMap.put(faceNoseTreeItem, new BcsPart());

                        sortPartSetItems(partSetGrandParentEntry);
                        faceNose.setDisable(true);

                        partSetsTreeView.getSelectionModel().select(faceNoseTreeItem);
                    }
                    case 4 -> {
                        TreeItem<String> faceEarTreeItem = new TreeItem<>("Face Ear");

                        partSetGrandParentEntry.getChildren().add(faceEarTreeItem);
                        bcsPartsHashMap.put(faceEarTreeItem, new BcsPart());

                        sortPartSetItems(partSetGrandParentEntry);
                        faceEar.setDisable(true);

                        partSetsTreeView.getSelectionModel().select(faceEarTreeItem);
                    }
                    case 5 -> {
                        TreeItem<String> hairTreeItem = new TreeItem<>("Hair");

                        partSetGrandParentEntry.getChildren().add(hairTreeItem);
                        bcsPartsHashMap.put(hairTreeItem, new BcsPart());

                        sortPartSetItems(partSetGrandParentEntry);
                        hair.setDisable(true);

                        partSetsTreeView.getSelectionModel().select(hairTreeItem);
                    }
                    case 6 -> {
                        TreeItem<String> bustTreeItem = new TreeItem<>("Bust");

                        partSetGrandParentEntry.getChildren().add(bustTreeItem);
                        bcsPartsHashMap.put(bustTreeItem, new BcsPart());

                        sortPartSetItems(partSetGrandParentEntry);
                        bust.setDisable(true);

                        partSetsTreeView.getSelectionModel().select(bustTreeItem);
                    }
                    case 7 -> {
                        TreeItem<String> pantsTreeItem = new TreeItem<>("Pants");

                        partSetGrandParentEntry.getChildren().add(pantsTreeItem);
                        bcsPartsHashMap.put(pantsTreeItem, new BcsPart());

                        sortPartSetItems(partSetGrandParentEntry);
                        pants.setDisable(true);

                        partSetsTreeView.getSelectionModel().select(pantsTreeItem);
                    }
                    case 8 -> {
                        TreeItem<String> ristTreeItem = new TreeItem<>("Rist");

                        partSetGrandParentEntry.getChildren().add(ristTreeItem);
                        bcsPartsHashMap.put(ristTreeItem, new BcsPart());

                        sortPartSetItems(partSetGrandParentEntry);
                        rist.setDisable(true);

                        partSetsTreeView.getSelectionModel().select(ristTreeItem);
                    }
                    case 9 -> {
                        TreeItem<String> bootsTreeItem = new TreeItem<>("Boots");

                        partSetGrandParentEntry.getChildren().add(bootsTreeItem);
                        bcsPartsHashMap.put(bootsTreeItem, new BcsPart());

                        sortPartSetItems(partSetGrandParentEntry);
                        boots.setDisable(true);

                        partSetsTreeView.getSelectionModel().select(bootsTreeItem);
                    }
                }
            });
        });
    }

    private void partSetsKeysListener() {
        partSetsTreeView.setOnKeyPressed(e -> {
            if (e.isControlDown() && e.getCode() == KeyCode.C) CopyPartSetItem();
            else if (e.isControlDown() && e.getCode() == KeyCode.V) PastePartSetItem();
            else if (e.getCode() == KeyCode.DELETE) DeletePartSetItem();
            else if (e.isControlDown() && e.getCode() == KeyCode.A) AddPartSetItemCopy();
        });
    }

    private void AddPartSet() {
        if (partSetsTreeView.getRoot() == null) {
            partSetsTreeView.setRoot(new TreeItem<>("dummy"));
            partSetsTreeView.setShowRoot(false);
        }
        else if (partSetsTreeView.getRoot().getChildren().isEmpty()) {
            allPartSetEntries = 0;
        } 

        TreeItem<String> partSet  = new TreeItem<>("Part Set " + allPartSetEntries);

        partSetsTreeView.getRoot().getChildren().add(partSet); 

        allPartSetEntries++;
    }

    private void AddColorSelector() {
        TreeItem<String> getGrandParent = currentPartSetEntry;

        while (!getGrandParent.getParent().getValue().contains("Part Set")) {
            getGrandParent = getGrandParent.getParent();
        }

        boolean hasColorSelector = false;

        for (TreeItem<String> child : getGrandParent.getChildren()) {
            if (child.getValue().equals("Color Selectors")) {
                hasColorSelector = true;
            }
        }

        if (hasColorSelector) {
            TreeItem<String> getParent = getGrandParent.getChildren().get(0);
            TreeItem<String> newChild = new TreeItem<>("Entry " + getParent.getChildren().size());

            getParent.getChildren().add(newChild);

            bcsColorsSelectorHashMap.put(newChild, new BcsColorSelector());

            partSetsTreeView.getSelectionModel().select(newChild);
        } 
        else {
            getGrandParent.getChildren().add(0, new TreeItem<>("Color Selectors"));

            TreeItem<String> newChild = new TreeItem<>("Entry " + 0);

            getGrandParent.getChildren().get(0).getChildren().add(newChild);

            bcsColorsSelectorHashMap.put(newChild, new BcsColorSelector());

            partSetsTreeView.getSelectionModel().select(newChild);
        }
    }

    private void AddPhysics() {
        TreeItem<String> getGrandParent = currentPartSetEntry;

        while (!getGrandParent.getParent().getValue().contains("Part Set")) {
            getGrandParent = getGrandParent.getParent();
        }

        boolean hasPhysics = false;
        TreeItem<String> physicsIndex = new TreeItem<>();

        for (TreeItem<String> child : getGrandParent.getChildren()) {
            if (child.getValue().equals("Physics")) {
                hasPhysics = true;
                physicsIndex = child;
            }
        }

        if (hasPhysics) {
            TreeItem<String> getParent = physicsIndex;
            TreeItem<String> newChild = new TreeItem<>("Entry " + getParent.getChildren().size());

            getParent.getChildren().add(newChild);

            bcsPhysicsHashMap.put(newChild, new BcsPhysics());

            partSetsTreeView.getSelectionModel().select(newChild);
        } 
        else {
            getGrandParent.getChildren().add(0, new TreeItem<>("Physics"));

            TreeItem<String> newChild = new TreeItem<>("Entry " + 0);

            getGrandParent.getChildren().get(0).getChildren().add(newChild);

            bcsPhysicsHashMap.put(newChild, new BcsPhysics());

            sortPartSetSubItems(getGrandParent);

            partSetsTreeView.getSelectionModel().select(newChild);
        }
    }

    private void AddUnknown3() {
        TreeItem<String> getGrandParent = currentPartSetEntry;

        while (!getGrandParent.getParent().getValue().contains("Part Set")) {
            getGrandParent = getGrandParent.getParent();
        }

        boolean hasUnknown3 = false;
        TreeItem<String> unknown3Index = new TreeItem<>();

        for (TreeItem<String> child : getGrandParent.getChildren()) {
            if (child.getValue().equals("Unknown 3")) {
                hasUnknown3 = true;
                unknown3Index = child;
            }
        }

        if (hasUnknown3) {
            TreeItem<String> getParent = unknown3Index;
            TreeItem<String> newChild = new TreeItem<>("Entry " + getParent.getChildren().size());

            getParent.getChildren().add(newChild);

            bcsUnknown3HashMap.put(newChild, new BcsUnknown3());

            partSetsTreeView.getSelectionModel().select(newChild);
        } 
        else {
            getGrandParent.getChildren().add(0, new TreeItem<>("Unknown 3"));

            TreeItem<String> newChild = new TreeItem<>("Entry " + 0);

            getGrandParent.getChildren().get(0).getChildren().add(newChild);

            bcsUnknown3HashMap.put(newChild, new BcsUnknown3());

            sortPartSetSubItems(getGrandParent);

            partSetsTreeView.getSelectionModel().select(newChild);
        }
    }

    private void CopyPartSetItem() {
        noCopiedPartSetItemFound.setVisible(false);
        copiedPartSetItem.setVisible(true);
        pastePartSetItem.setVisible(true);
        addPartSetItemCopy.setVisible(true);

        pastePartSetItem.setDisable(false);

        if (currentPartSetEntry.getParent() == partSetsTreeView.getRoot()) {
            setPartSetContextMenuText("Part Set");

            copyTypesContainer = new String[currentPartSetEntry.getChildren().size()][];
            copySubTypesContainer = new String[currentPartSetEntry.getChildren().size()];
            copyPartsContainer = new Object[currentPartSetEntry.getChildren().size()];
            copyPartSetContainer = new Object[currentPartSetEntry.getChildren().size()][][];

            for (int i = 0; i < currentPartSetEntry.getChildren().size(); i++) {
                copyPartSetContainer[i] = new Object[currentPartSetEntry.getChildren().get(i).getChildren().size()][];
                
                switch (currentPartSetEntry.getChildren().get(i).getValue()) {
                    case "Face Base" -> {
                        copyPartsContainer[i] = new BcsPart(bcsPartsHashMap.get(currentPartSetEntry.getChildren().get(i)));
                        copyTypesContainer[i] = new String[currentPartSetEntry.getChildren().get(i).getChildren().size()];
                        copySubTypesContainer[i] = "Face Base";
                        
                        for (int j = 0; j < currentPartSetEntry.getChildren().get(i).getChildren().size(); j++) {
                            copyPartSetContainer[i][j] = new Object[currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().size()];

                            switch (currentPartSetEntry.getChildren().get(i).getChildren().get(j).getValue()) {
                                case "Color Selectors" -> {
                                    for (int k = 0; k < currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().size(); k++) {
                                        copyPartSetContainer[i][j][k] = new BcsColorSelector(bcsColorsSelectorHashMap.get(currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().get(k)));
                                    }
                                    copyTypesContainer[i][j] = "Color Selectors";
                                }
                                case "Physics" -> {
                                    for (int k = 0; k < currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().size(); k++) {
                                        copyPartSetContainer[i][j][k] = new BcsPhysics(bcsPhysicsHashMap.get(currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().get(k)));
                                    }
                                    copyTypesContainer[i][j] = "Physics";
                                }
                                case "Unknown 3" -> {
                                    for (int k = 0; k < currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().size(); k++) {
                                        copyPartSetContainer[i][j][k] = new BcsUnknown3(bcsUnknown3HashMap.get(currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().get(k)));
                                    }
                                    copyTypesContainer[i][j] = "Unknown 3";
                                }
                            }
                        }
                    }
                    case "Face Forehead" -> {
                        copyPartsContainer[i] = new BcsPart(bcsPartsHashMap.get(currentPartSetEntry.getChildren().get(i)));
                        copyTypesContainer[i] = new String[currentPartSetEntry.getChildren().get(i).getChildren().size()];
                        copySubTypesContainer[i] = "Face Forehead";
                        
                        for (int j = 0; j < currentPartSetEntry.getChildren().get(i).getChildren().size(); j++) {
                            copyPartSetContainer[i][j] = new Object[currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().size()];

                            switch (currentPartSetEntry.getChildren().get(i).getChildren().get(j).getValue()) {
                                case "Color Selectors" -> {
                                    for (int k = 0; k < currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().size(); k++) {
                                        copyPartSetContainer[i][j][k] = new BcsColorSelector(bcsColorsSelectorHashMap.get(currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().get(k)));
                                    }
                                    copyTypesContainer[i][j] = "Color Selectors";
                                }
                                case "Physics" -> {
                                    for (int k = 0; k < currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().size(); k++) {
                                        copyPartSetContainer[i][j][k] = new BcsPhysics(bcsPhysicsHashMap.get(currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().get(k)));
                                    }
                                    copyTypesContainer[i][j] = "Physics";
                                }
                                case "Unknown 3" -> {
                                    for (int k = 0; k < currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().size(); k++) {
                                        copyPartSetContainer[i][j][k] = new BcsUnknown3(bcsUnknown3HashMap.get(currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().get(k)));
                                    }
                                    copyTypesContainer[i][j] = "Unknown 3";
                                }
                            }
                        }
                    }
                    case "Face Eye" -> {
                        copyPartsContainer[i] = new BcsPart(bcsPartsHashMap.get(currentPartSetEntry.getChildren().get(i)));
                        copyTypesContainer[i] = new String[currentPartSetEntry.getChildren().get(i).getChildren().size()];
                        copySubTypesContainer[i] = "Face Eye";
                        
                        for (int j = 0; j < currentPartSetEntry.getChildren().get(i).getChildren().size(); j++) {
                            copyPartSetContainer[i][j] = new Object[currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().size()];

                            switch (currentPartSetEntry.getChildren().get(i).getChildren().get(j).getValue()) {
                                case "Color Selectors" -> {
                                    for (int k = 0; k < currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().size(); k++) {
                                        copyPartSetContainer[i][j][k] = new BcsColorSelector(bcsColorsSelectorHashMap.get(currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().get(k)));
                                    }
                                    copyTypesContainer[i][j] = "Color Selectors";
                                }
                                case "Physics" -> {
                                    for (int k = 0; k < currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().size(); k++) {
                                        copyPartSetContainer[i][j][k] = new BcsPhysics(bcsPhysicsHashMap.get(currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().get(k)));
                                    }
                                    copyTypesContainer[i][j] = "Physics";
                                }
                                case "Unknown 3" -> {
                                    for (int k = 0; k < currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().size(); k++) {
                                        copyPartSetContainer[i][j][k] = new BcsUnknown3(bcsUnknown3HashMap.get(currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().get(k)));
                                    }
                                    copyTypesContainer[i][j] = "Unknown 3";
                                }
                            }
                        }
                    }
                    case "Face Nose" -> {
                        copyPartsContainer[i] = new BcsPart(bcsPartsHashMap.get(currentPartSetEntry.getChildren().get(i)));
                        copyTypesContainer[i] = new String[currentPartSetEntry.getChildren().get(i).getChildren().size()];
                        copySubTypesContainer[i] = "Face Nose";
                        
                        for (int j = 0; j < currentPartSetEntry.getChildren().get(i).getChildren().size(); j++) {
                            copyPartSetContainer[i][j] = new Object[currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().size()];

                            switch (currentPartSetEntry.getChildren().get(i).getChildren().get(j).getValue()) {
                                case "Color Selectors" -> {
                                    for (int k = 0; k < currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().size(); k++) {
                                        copyPartSetContainer[i][j][k] = new BcsColorSelector(bcsColorsSelectorHashMap.get(currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().get(k)));
                                    }
                                    copyTypesContainer[i][j] = "Color Selectors";
                                }
                                case "Physics" -> {
                                    for (int k = 0; k < currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().size(); k++) {
                                        copyPartSetContainer[i][j][k] = new BcsPhysics(bcsPhysicsHashMap.get(currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().get(k)));
                                    }
                                    copyTypesContainer[i][j] = "Physics";
                                }
                                case "Unknown 3" -> {
                                    for (int k = 0; k < currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().size(); k++) {
                                        copyPartSetContainer[i][j][k] = new BcsUnknown3(bcsUnknown3HashMap.get(currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().get(k)));
                                    }
                                    copyTypesContainer[i][j] = "Unknown 3";
                                }
                            }
                        }
                    }
                    case "Face Ear" -> {
                        copyPartsContainer[i] = new BcsPart(bcsPartsHashMap.get(currentPartSetEntry.getChildren().get(i)));
                        copyTypesContainer[i] = new String[currentPartSetEntry.getChildren().get(i).getChildren().size()];
                        copySubTypesContainer[i] = "Face Ear";
                        
                        for (int j = 0; j < currentPartSetEntry.getChildren().get(i).getChildren().size(); j++) {
                            copyPartSetContainer[i][j] = new Object[currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().size()];

                            switch (currentPartSetEntry.getChildren().get(i).getChildren().get(j).getValue()) {
                                case "Color Selectors" -> {
                                    for (int k = 0; k < currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().size(); k++) {
                                        copyPartSetContainer[i][j][k] = new BcsColorSelector(bcsColorsSelectorHashMap.get(currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().get(k)));
                                    }
                                    copyTypesContainer[i][j] = "Color Selectors";
                                }
                                case "Physics" -> {
                                    for (int k = 0; k < currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().size(); k++) {
                                        copyPartSetContainer[i][j][k] = new BcsPhysics(bcsPhysicsHashMap.get(currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().get(k)));
                                    }
                                    copyTypesContainer[i][j] = "Physics";
                                }
                                case "Unknown 3" -> {
                                    for (int k = 0; k < currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().size(); k++) {
                                        copyPartSetContainer[i][j][k] = new BcsUnknown3(bcsUnknown3HashMap.get(currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().get(k)));
                                    }
                                    copyTypesContainer[i][j] = "Unknown 3";
                                }
                            }
                        }
                    }
                    case "Hair" -> {
                        copyPartsContainer[i] = new BcsPart(bcsPartsHashMap.get(currentPartSetEntry.getChildren().get(i)));
                        copyTypesContainer[i] = new String[currentPartSetEntry.getChildren().get(i).getChildren().size()];
                        copySubTypesContainer[i] = "Hair";
                        
                        for (int j = 0; j < currentPartSetEntry.getChildren().get(i).getChildren().size(); j++) {
                            copyPartSetContainer[i][j] = new Object[currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().size()];

                            switch (currentPartSetEntry.getChildren().get(i).getChildren().get(j).getValue()) {
                                case "Color Selectors" -> {
                                    for (int k = 0; k < currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().size(); k++) {
                                        copyPartSetContainer[i][j][k] = new BcsColorSelector(bcsColorsSelectorHashMap.get(currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().get(k)));
                                    }
                                    copyTypesContainer[i][j] = "Color Selectors";
                                }
                                case "Physics" -> {
                                    for (int k = 0; k < currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().size(); k++) {
                                        copyPartSetContainer[i][j][k] = new BcsPhysics(bcsPhysicsHashMap.get(currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().get(k)));
                                    }
                                    copyTypesContainer[i][j] = "Physics";
                                }
                                case "Unknown 3" -> {
                                    for (int k = 0; k < currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().size(); k++) {
                                        copyPartSetContainer[i][j][k] = new BcsUnknown3(bcsUnknown3HashMap.get(currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().get(k)));
                                    }
                                    copyTypesContainer[i][j] = "Unknown 3";
                                }
                            }
                        }
                    }
                    case "Bust" -> {
                        copyPartsContainer[i] = new BcsPart(bcsPartsHashMap.get(currentPartSetEntry.getChildren().get(i)));
                        copyTypesContainer[i] = new String[currentPartSetEntry.getChildren().get(i).getChildren().size()];
                        copySubTypesContainer[i] = "Bust";
                        
                        for (int j = 0; j < currentPartSetEntry.getChildren().get(i).getChildren().size(); j++) {
                            copyPartSetContainer[i][j] = new Object[currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().size()];

                            switch (currentPartSetEntry.getChildren().get(i).getChildren().get(j).getValue()) {
                                case "Color Selectors" -> {
                                    for (int k = 0; k < currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().size(); k++) {
                                        copyPartSetContainer[i][j][k] = new BcsColorSelector(bcsColorsSelectorHashMap.get(currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().get(k)));
                                    }
                                    copyTypesContainer[i][j] = "Color Selectors";
                                }
                                case "Physics" -> {
                                    for (int k = 0; k < currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().size(); k++) {
                                        copyPartSetContainer[i][j][k] = new BcsPhysics(bcsPhysicsHashMap.get(currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().get(k)));
                                    }
                                    copyTypesContainer[i][j] = "Physics";
                                }
                                case "Unknown 3" -> {
                                    for (int k = 0; k < currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().size(); k++) {
                                        copyPartSetContainer[i][j][k] = new BcsUnknown3(bcsUnknown3HashMap.get(currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().get(k)));
                                    }
                                    copyTypesContainer[i][j] = "Unknown 3";
                                }
                            }
                        }
                    }
                    case "Pants" -> {
                        copyPartsContainer[i] = new BcsPart(bcsPartsHashMap.get(currentPartSetEntry.getChildren().get(i)));
                        copyTypesContainer[i] = new String[currentPartSetEntry.getChildren().get(i).getChildren().size()];
                        copySubTypesContainer[i] = "Pants";
                        
                        for (int j = 0; j < currentPartSetEntry.getChildren().get(i).getChildren().size(); j++) {
                            copyPartSetContainer[i][j] = new Object[currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().size()];

                            switch (currentPartSetEntry.getChildren().get(i).getChildren().get(j).getValue()) {
                                case "Color Selectors" -> {
                                    for (int k = 0; k < currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().size(); k++) {
                                        copyPartSetContainer[i][j][k] = new BcsColorSelector(bcsColorsSelectorHashMap.get(currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().get(k)));
                                    }
                                    copyTypesContainer[i][j] = "Color Selectors";
                                }
                                case "Physics" -> {
                                    for (int k = 0; k < currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().size(); k++) {
                                        copyPartSetContainer[i][j][k] = new BcsPhysics(bcsPhysicsHashMap.get(currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().get(k)));
                                    }
                                    copyTypesContainer[i][j] = "Physics";
                                }
                                case "Unknown 3" -> {
                                    for (int k = 0; k < currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().size(); k++) {
                                        copyPartSetContainer[i][j][k] = new BcsUnknown3(bcsUnknown3HashMap.get(currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().get(k)));
                                    }
                                    copyTypesContainer[i][j] = "Unknown 3";
                                }
                            }
                        }
                    }
                    case "Rist" -> {
                        copyPartsContainer[i] = new BcsPart(bcsPartsHashMap.get(currentPartSetEntry.getChildren().get(i)));
                        copyTypesContainer[i] = new String[currentPartSetEntry.getChildren().get(i).getChildren().size()];
                        copySubTypesContainer[i] = "Rist";
                        
                        for (int j = 0; j < currentPartSetEntry.getChildren().get(i).getChildren().size(); j++) {
                            copyPartSetContainer[i][j] = new Object[currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().size()];

                            switch (currentPartSetEntry.getChildren().get(i).getChildren().get(j).getValue()) {
                                case "Color Selectors" -> {
                                    for (int k = 0; k < currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().size(); k++) {
                                        copyPartSetContainer[i][j][k] = new BcsColorSelector(bcsColorsSelectorHashMap.get(currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().get(k)));
                                    }
                                    copyTypesContainer[i][j] = "Color Selectors";
                                }
                                case "Physics" -> {
                                    for (int k = 0; k < currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().size(); k++) {
                                        copyPartSetContainer[i][j][k] = new BcsPhysics(bcsPhysicsHashMap.get(currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().get(k)));
                                    }
                                    copyTypesContainer[i][j] = "Physics";
                                }
                                case "Unknown 3" -> {
                                    for (int k = 0; k < currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().size(); k++) {
                                        copyPartSetContainer[i][j][k] = new BcsUnknown3(bcsUnknown3HashMap.get(currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().get(k)));
                                    }
                                    copyTypesContainer[i][j] = "Unknown 3";
                                }
                            }
                        }
                    }
                    case "Boots" -> {
                        copyPartsContainer[i] = new BcsPart(bcsPartsHashMap.get(currentPartSetEntry.getChildren().get(i)));
                        copyTypesContainer[i] = new String[currentPartSetEntry.getChildren().get(i).getChildren().size()];
                        copySubTypesContainer[i] = "Boots";
                        
                        for (int j = 0; j < currentPartSetEntry.getChildren().get(i).getChildren().size(); j++) {
                            copyPartSetContainer[i][j] = new Object[currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().size()];

                            switch (currentPartSetEntry.getChildren().get(i).getChildren().get(j).getValue()) {
                                case "Color Selectors" -> {
                                    for (int k = 0; k < currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().size(); k++) {
                                        copyPartSetContainer[i][j][k] = new BcsColorSelector(bcsColorsSelectorHashMap.get(currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().get(k)));
                                    }
                                    copyTypesContainer[i][j] = "Color Selectors";
                                }
                                case "Physics" -> {
                                    for (int k = 0; k < currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().size(); k++) {
                                        copyPartSetContainer[i][j][k] = new BcsPhysics(bcsPhysicsHashMap.get(currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().get(k)));
                                    }
                                    copyTypesContainer[i][j] = "Physics";
                                }
                                case "Unknown 3" -> {
                                    for (int k = 0; k < currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().size(); k++) {
                                        copyPartSetContainer[i][j][k] = new BcsUnknown3(bcsUnknown3HashMap.get(currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().get(k)));
                                    }
                                    copyTypesContainer[i][j] = "Unknown 3";
                                }
                            }
                        }
                    }
                }
            }
        }
        else if (currentPartSetEntry.getParent().getValue().contains("Part Set")) {
            setPartSetContextMenuText(currentPartSetEntry.getValue());

            copySubTypesContainer = new String[currentPartSetEntry.getChildren().size()];
            copyContainer = new BcsPart(bcsPartsHashMap.get(currentPartSetEntry));
            copyListContainer = new Object[currentPartSetEntry.getChildren().size()][];

            for (int i = 0; i < currentPartSetEntry.getChildren().size(); i++) {
                copyListContainer[i] = new Object[currentPartSetEntry.getChildren().get(i).getChildren().size()];

                switch (currentPartSetEntry.getChildren().get(i).getValue()) {
                    case "Color Selectors" -> {
                        for (int j = 0; j < currentPartSetEntry.getChildren().get(i).getChildren().size(); j++ ) {
                            copyListContainer[i][j] = new BcsColorSelector(bcsColorsSelectorHashMap.get(currentPartSetEntry.getChildren().get(i).getChildren().get(j)));
                        }
                        copySubTypesContainer[i] = "Color Selectors";
                    }
                    case "Physics" -> {
                        for (int j = 0; j < currentPartSetEntry.getChildren().get(i).getChildren().size(); j++ ) {
                            copyListContainer[i][j] = new BcsPhysics(bcsPhysicsHashMap.get(currentPartSetEntry.getChildren().get(i).getChildren().get(j)));
                        }
                        copySubTypesContainer[i] = "Physics";
                    }
                    case "Unknown 3" -> {
                        for (int j = 0; j < currentPartSetEntry.getChildren().get(i).getChildren().size(); j++ ) {
                            copyListContainer[i][j] = new BcsUnknown3(bcsUnknown3HashMap.get(currentPartSetEntry.getChildren().get(i).getChildren().get(j)));
                        }
                        copySubTypesContainer[i] = "Unknown 3";
                    }
                }
            }
        }
        else if (currentPartSetEntry.getChildren().isEmpty()) {
            switch (currentPartSetEntry.getParent().getValue()) {
                case "Color Selectors" -> {
                    copyContainer = new BcsColorSelector(bcsColorsSelectorHashMap.get(currentPartSetEntry));
                    setPartSetContextMenuText("Color Selector");
                }
                case "Physics" -> {
                    copyContainer = new BcsPhysics(bcsPhysicsHashMap.get(currentPartSetEntry));
                    setPartSetContextMenuText("Physics");
                }
                case "Unknown 3" -> {
                    copyContainer = new BcsUnknown3(bcsUnknown3HashMap.get(currentPartSetEntry));
                    setPartSetContextMenuText("Unknown 3");
                }
            }
        }
        else if (currentPartSetEntry.getChildren().isEmpty()) {
            setPartSetContextMenuText("Null");
        }
        else {
            copyListContainer = new Object[1][currentPartSetEntry.getChildren().size()];

            switch (currentPartSetEntry.getValue()) {
                case "Color Selectors" -> {
                    for (TreeItem<String> child : currentPartSetEntry.getChildren()) {
                        copyListContainer[0][currentPartSetEntry.getChildren().indexOf(child)] = new BcsColorSelector(bcsColorsSelectorHashMap.get(child));
                    }

                    setPartSetContextMenuText("Color Selector List");
                }
                case "Physics" -> {
                    for (TreeItem<String> child : currentPartSetEntry.getChildren()) {
                        copyListContainer[0][currentPartSetEntry.getChildren().indexOf(child)] = new BcsPhysics(bcsPhysicsHashMap.get(child));
                    }

                    setPartSetContextMenuText("Physics List");
                }
                case "Unknown 3" -> {
                    for (TreeItem<String> child : currentPartSetEntry.getChildren()) {
                        copyListContainer[0][currentPartSetEntry.getChildren().indexOf(child)] = new BcsUnknown3(bcsUnknown3HashMap.get(child));
                    }

                    setPartSetContextMenuText("Unknown 3 List");
                }
            }
        }
    }

    private void DeletePartSetItem() {
        if (currentPartSetEntry.getParent() == partSetsTreeView.getRoot()) {
            for (TreeItem<String> getParent : partSetGrandParentEntry.getChildren()) {
                bcsPartsHashMap.remove(getParent);

                switch (getParent.getValue()) {
                    case "Face Base" -> {
                        for(TreeItem<String> getChildren : getParent.getChildren()) {
                            switch (getChildren.getValue()) {
                                case "Color Selectors" -> {
                                    for (TreeItem<String> grandChild : getChildren.getChildren()) {
                                        bcsColorsSelectorHashMap.remove(grandChild);
                                    }
                                }
                                case "Physics" -> {
                                    for (TreeItem<String> grandChild : getChildren.getChildren()) {
                                        bcsPhysicsHashMap.remove(grandChild);
                                    }
                                }
                                case "Unknown 3" -> {
                                    for (TreeItem<String> grandChild : getChildren.getChildren()) {
                                        bcsUnknown3HashMap.remove(grandChild);
                                    }
                                }
                            }
                        }
                    }
                    case "Face Forehead" -> {
                        for(TreeItem<String> getChildren : getParent.getChildren()) {
                            switch (getChildren.getValue()) {
                                case "Color Selectors" -> {
                                    for (TreeItem<String> grandChild : getChildren.getChildren()) {
                                        bcsColorsSelectorHashMap.remove(grandChild);
                                    }
                                }
                                case "Physics" -> {
                                    for (TreeItem<String> grandChild : getChildren.getChildren()) {
                                        bcsPhysicsHashMap.remove(grandChild);
                                    }
                                }
                                case "Unknown 3" -> {
                                    for (TreeItem<String> grandChild : getChildren.getChildren()) {
                                        bcsUnknown3HashMap.remove(grandChild);
                                    }
                                }
                            }
                        }
                    }
                    case "Face Eye" -> {
                        for(TreeItem<String> getChildren : getParent.getChildren()) {
                            switch (getChildren.getValue()) {
                                case "Color Selectors" -> {
                                    for (TreeItem<String> grandChild : getChildren.getChildren()) {
                                        bcsColorsSelectorHashMap.remove(grandChild);
                                    }
                                }
                                case "Physics" -> {
                                    for (TreeItem<String> grandChild : getChildren.getChildren()) {
                                        bcsPhysicsHashMap.remove(grandChild);
                                    }
                                }
                                case "Unknown 3" -> {
                                    for (TreeItem<String> grandChild : getChildren.getChildren()) {
                                        bcsUnknown3HashMap.remove(grandChild);
                                    }
                                }
                            }
                        }
                    }
                    case "Face Nose" -> {
                        for(TreeItem<String> getChildren : getParent.getChildren()) {
                            switch (getChildren.getValue()) {
                                case "Color Selectors" -> {
                                    for (TreeItem<String> grandChild : getChildren.getChildren()) {
                                        bcsColorsSelectorHashMap.remove(grandChild);
                                    }
                                }
                                case "Physics" -> {
                                    for (TreeItem<String> grandChild : getChildren.getChildren()) {
                                        bcsPhysicsHashMap.remove(grandChild);
                                    }
                                }
                                case "Unknown 3" -> {
                                    for (TreeItem<String> grandChild : getChildren.getChildren()) {
                                        bcsUnknown3HashMap.remove(grandChild);
                                    }
                                }
                            }
                        }
                    }
                    case "Face Ear" -> {
                        for(TreeItem<String> getChildren : getParent.getChildren()) {
                            switch (getChildren.getValue()) {
                                case "Color Selectors" -> {
                                    for (TreeItem<String> grandChild : getChildren.getChildren()) {
                                        bcsColorsSelectorHashMap.remove(grandChild);
                                    }
                                }
                                case "Physics" -> {
                                    for (TreeItem<String> grandChild : getChildren.getChildren()) {
                                        bcsPhysicsHashMap.remove(grandChild);
                                    }
                                }
                                case "Unknown 3" -> {
                                    for (TreeItem<String> grandChild : getChildren.getChildren()) {
                                        bcsUnknown3HashMap.remove(grandChild);
                                    }
                                }
                            }
                        }
                    }
                    case "Hair" -> {
                        for(TreeItem<String> getChildren : getParent.getChildren()) {
                            switch (getChildren.getValue()) {
                                case "Color Selectors" -> {
                                    for (TreeItem<String> grandChild : getChildren.getChildren()) {
                                        bcsColorsSelectorHashMap.remove(grandChild);
                                    }
                                }
                                case "Physics" -> {
                                    for (TreeItem<String> grandChild : getChildren.getChildren()) {
                                        bcsPhysicsHashMap.remove(grandChild);
                                    }
                                }
                                case "Unknown 3" -> {
                                    for (TreeItem<String> grandChild : getChildren.getChildren()) {
                                        bcsUnknown3HashMap.remove(grandChild);
                                    }
                                }
                            }
                        }
                    }
                    case "Bust" -> {
                        for(TreeItem<String> getChildren : getParent.getChildren()) {
                            switch (getChildren.getValue()) {
                                case "Color Selectors" -> {
                                    for (TreeItem<String> grandChild : getChildren.getChildren()) {
                                        bcsColorsSelectorHashMap.remove(grandChild);
                                    }
                                }
                                case "Physics" -> {
                                    for (TreeItem<String> grandChild : getChildren.getChildren()) {
                                        bcsPhysicsHashMap.remove(grandChild);
                                    }
                                }
                                case "Unknown 3" -> {
                                    for (TreeItem<String> grandChild : getChildren.getChildren()) {
                                        bcsUnknown3HashMap.remove(grandChild);
                                    }
                                }
                            }
                        }
                    }
                    case "Pants" -> {
                        for(TreeItem<String> getChildren : getParent.getChildren()) {
                            switch (getChildren.getValue()) {
                                case "Color Selectors" -> {
                                    for (TreeItem<String> grandChild : getChildren.getChildren()) {
                                        bcsColorsSelectorHashMap.remove(grandChild);
                                    }
                                }
                                case "Physics" -> {
                                    for (TreeItem<String> grandChild : getChildren.getChildren()) {
                                        bcsPhysicsHashMap.remove(grandChild);
                                    }
                                }
                                case "Unknown 3" -> {
                                    for (TreeItem<String> grandChild : getChildren.getChildren()) {
                                        bcsUnknown3HashMap.remove(grandChild);
                                    }
                                }
                            }
                        }
                    }
                    case "Rist" -> {
                        for(TreeItem<String> getChildren : getParent.getChildren()) {
                            switch (getChildren.getValue()) {
                                case "Color Selectors" -> {
                                    for (TreeItem<String> grandChild : getChildren.getChildren()) {
                                        bcsColorsSelectorHashMap.remove(grandChild);
                                    }
                                }
                                case "Physics" -> {
                                    for (TreeItem<String> grandChild : getChildren.getChildren()) {
                                        bcsPhysicsHashMap.remove(grandChild);
                                    }
                                }
                                case "Unknown 3" -> {
                                    for (TreeItem<String> grandChild : getChildren.getChildren()) {
                                        bcsUnknown3HashMap.remove(grandChild);
                                    }
                                }
                            }
                        }
                    }
                    case "Boots" -> {
                        for(TreeItem<String> getChildren : getParent.getChildren()) {
                            switch (getChildren.getValue()) {
                                case "Color Selectors" -> {
                                    for (TreeItem<String> grandChild : getChildren.getChildren()) {
                                        bcsColorsSelectorHashMap.remove(grandChild);
                                    }
                                }
                                case "Physics" -> {
                                    for (TreeItem<String> grandChild : getChildren.getChildren()) {
                                        bcsPhysicsHashMap.remove(grandChild);
                                    }
                                }
                                case "Unknown 3" -> {
                                    for (TreeItem<String> grandChild : getChildren.getChildren()) {
                                        bcsUnknown3HashMap.remove(grandChild);
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (partSetGrandParentEntry.nextSibling() != null) {
                for (int i = partSetsTreeView.getRoot().getChildren().indexOf(partSetGrandParentEntry.nextSibling()); i < partSetsTreeView.getRoot().getChildren().size(); i++) {

                    int entryIndex = Integer.parseInt(partSetsTreeView.getRoot().getChildren().get(i).getValue().toString().replaceAll("\\D+", ""));

                    partSetsTreeView.getRoot().getChildren().get(i).setValue("Part Set " + (entryIndex - 1));
                }
            }

            partSetsTreeView.getRoot().getChildren().remove(partSetGrandParentEntry);

            allPartSetEntries--;
        }
        else if (currentPartSetEntry.getParent().getValue().contains("Part Set")) {
            bcsPartsHashMap.remove(currentPartSetEntry);

            for (TreeItem<String> getParent : currentPartSetEntry.getChildren()) {
                switch (getParent.getValue()) {
                    case "Color Selectors" -> {
                        for (TreeItem<String> child : getParent.getChildren()) {
                            bcsColorsSelectorHashMap.remove(child);
                        }
                    }
                    case "Physics" -> {
                        for (TreeItem<String> child : getParent.getChildren()) {
                            bcsPhysicsHashMap.remove(child);
                        }
                    }
                    case "Unknown 3" -> {
                        for (TreeItem<String> child : getParent.getChildren()) {
                            bcsUnknown3HashMap.remove(child);
                        }
                    }
                }
            }
            
            partSetGrandParentEntry.getChildren().remove(partSetGrandParentEntry.getChildren().indexOf(currentPartSetEntry));
        }
        else if (currentPartSetEntry.getChildren().isEmpty() && currentPartSetEntry.getValue().startsWith("Entry")) {
            TreeItem<String> getParent = currentPartSetEntry.getParent();

            switch (currentPartSetEntry.getParent().getValue()) {
                case "Color Selectors" -> {
                    bcsColorsSelectorHashMap.remove(currentPartSetEntry);

                    getParent.getChildren().remove(currentPartSetEntry);
                    
                    for (int i = 0; i < getParent.getChildren().size(); i++) {
                        getParent.getChildren().get(i).setValue("Entry " + i);
                    }

                    if (!getParent.getChildren().isEmpty()) {
                        partSetsTreeView.getSelectionModel().select(getParent.getChildren().getFirst());
                    }
                }
                case "Physics" -> {
                    bcsPhysicsHashMap.remove(currentPartSetEntry);

                    getParent.getChildren().remove(currentPartSetEntry);
                    
                    for (int i = 0; i < getParent.getChildren().size(); i++) {
                        getParent.getChildren().get(i).setValue("Entry " + i);
                    }

                    if (!getParent.getChildren().isEmpty()) {
                        partSetsTreeView.getSelectionModel().select(getParent.getChildren().getFirst());
                    }
                }
                case "Unknown 3" -> {
                    bcsUnknown3HashMap.remove(currentPartSetEntry);

                    getParent.getChildren().remove(currentPartSetEntry);
                    
                    for (int i = 0; i < getParent.getChildren().size(); i++) {
                        getParent.getChildren().get(i).setValue("Entry " + i);
                    }

                    if (!getParent.getChildren().isEmpty()) {
                        partSetsTreeView.getSelectionModel().select(getParent.getChildren().getFirst());
                    }
                }
            }
        }
        else {
            TreeItem<String> getParent = currentPartSetEntry;

            switch (currentPartSetEntry.getValue()) {
                case "Color Selectors" -> {
                    for (TreeItem<String> child : getParent.getChildren()) {
                        bcsColorsSelectorHashMap.remove(child);
                    }
                    getParent.getChildren().removeAll(getParent.getChildren());
                    getParent.getParent().getChildren().remove(getParent);
                }
                case "Physics" -> {
                    for (TreeItem<String> child : getParent.getChildren()) {
                        bcsPhysicsHashMap.remove(child);
                    }
                    getParent.getChildren().removeAll(getParent.getChildren());
                    getParent.getParent().getChildren().remove(getParent);
                }
                case "Unknown 3" -> {
                    for (TreeItem<String> child : getParent.getChildren()) {
                        bcsUnknown3HashMap.remove(child);
                    }
                    getParent.getChildren().removeAll(getParent.getChildren());
                    getParent.getParent().getChildren().remove(getParent);
                }
            }
        }
    }

    private void PastePartSetItem() {
        if (currentPartSetEntry.getParent() == partSetsTreeView.getRoot()) {
            for (TreeItem<String> parent : partSetGrandParentEntry.getChildren()) {
                switch (parent.getValue()) {
                    case "Face Base" -> {
                        bcsPartsHashMap.remove(parent);

                        for (TreeItem<String> child : parent.getChildren()) {
                            switch (parent.getValue()) {
                                case "Color Selectors" -> {
                                    for (TreeItem<String> grandChild : child.getChildren()) {
                                        bcsColorsSelectorHashMap.remove(grandChild);
                                    }
                                }
                                case "Physics" -> {
                                    for (TreeItem<String> grandChild : child.getChildren()) {
                                        bcsPhysicsHashMap.remove(grandChild);
                                    }
                                }
                                case "Unknown 3" -> {
                                    for (TreeItem<String> grandChild : child.getChildren()) {
                                        bcsUnknown3HashMap.remove(grandChild);
                                    }
                                }
                            }
                        }
                    }
                    case "Face Forehead" -> {
                        bcsPartsHashMap.remove(parent);

                        for (TreeItem<String> child : parent.getChildren()) {
                            switch (parent.getValue()) {
                                case "Color Selectors" -> {
                                    for (TreeItem<String> grandChild : child.getChildren()) {
                                        bcsColorsSelectorHashMap.remove(grandChild);
                                    }
                                }
                                case "Physics" -> {
                                    for (TreeItem<String> grandChild : child.getChildren()) {
                                        bcsPhysicsHashMap.remove(grandChild);
                                    }
                                }
                                case "Unknown 3" -> {
                                    for (TreeItem<String> grandChild : child.getChildren()) {
                                        bcsUnknown3HashMap.remove(grandChild);
                                    }
                                }
                            }
                        }
                    }
                    case "Face Eye" -> {
                        bcsPartsHashMap.remove(parent);

                        for (TreeItem<String> child : parent.getChildren()) {
                            switch (parent.getValue()) {
                                case "Color Selectors" -> {
                                    for (TreeItem<String> grandChild : child.getChildren()) {
                                        bcsColorsSelectorHashMap.remove(grandChild);
                                    }
                                }
                                case "Physics" -> {
                                    for (TreeItem<String> grandChild : child.getChildren()) {
                                        bcsPhysicsHashMap.remove(grandChild);
                                    }
                                }
                                case "Unknown 3" -> {
                                    for (TreeItem<String> grandChild : child.getChildren()) {
                                        bcsUnknown3HashMap.remove(grandChild);
                                    }
                                }
                            }
                        }
                    }
                    case "Face Nose" -> {
                        bcsPartsHashMap.remove(parent);

                        for (TreeItem<String> child : parent.getChildren()) {
                            switch (parent.getValue()) {
                                case "Color Selectors" -> {
                                    for (TreeItem<String> grandChild : child.getChildren()) {
                                        bcsColorsSelectorHashMap.remove(grandChild);
                                    }
                                }
                                case "Physics" -> {
                                    for (TreeItem<String> grandChild : child.getChildren()) {
                                        bcsPhysicsHashMap.remove(grandChild);
                                    }
                                }
                                case "Unknown 3" -> {
                                    for (TreeItem<String> grandChild : child.getChildren()) {
                                        bcsUnknown3HashMap.remove(grandChild);
                                    }
                                }
                            }
                        }
                    }
                    case "Face Ear" -> {
                        bcsPartsHashMap.remove(parent);

                        for (TreeItem<String> child : parent.getChildren()) {
                            switch (parent.getValue()) {
                                case "Color Selectors" -> {
                                    for (TreeItem<String> grandChild : child.getChildren()) {
                                        bcsColorsSelectorHashMap.remove(grandChild);
                                    }
                                }
                                case "Physics" -> {
                                    for (TreeItem<String> grandChild : child.getChildren()) {
                                        bcsPhysicsHashMap.remove(grandChild);
                                    }
                                }
                                case "Unknown 3" -> {
                                    for (TreeItem<String> grandChild : child.getChildren()) {
                                        bcsUnknown3HashMap.remove(grandChild);
                                    }
                                }
                            }
                        }
                    }
                    case "Hair" -> {
                        bcsPartsHashMap.remove(parent);

                        for (TreeItem<String> child : parent.getChildren()) {
                            switch (parent.getValue()) {
                                case "Color Selectors" -> {
                                    for (TreeItem<String> grandChild : child.getChildren()) {
                                        bcsColorsSelectorHashMap.remove(grandChild);
                                    }
                                }
                                case "Physics" -> {
                                    for (TreeItem<String> grandChild : child.getChildren()) {
                                        bcsPhysicsHashMap.remove(grandChild);
                                    }
                                }
                                case "Unknown 3" -> {
                                    for (TreeItem<String> grandChild : child.getChildren()) {
                                        bcsUnknown3HashMap.remove(grandChild);
                                    }
                                }
                            }
                        }
                    }
                    case "Bust" -> {
                        bcsPartsHashMap.remove(parent);

                        for (TreeItem<String> child : parent.getChildren()) {
                            switch (parent.getValue()) {
                                case "Color Selectors" -> {
                                    for (TreeItem<String> grandChild : child.getChildren()) {
                                        bcsColorsSelectorHashMap.remove(grandChild);
                                    }
                                }
                                case "Physics" -> {
                                    for (TreeItem<String> grandChild : child.getChildren()) {
                                        bcsPhysicsHashMap.remove(grandChild);
                                    }
                                }
                                case "Unknown 3" -> {
                                    for (TreeItem<String> grandChild : child.getChildren()) {
                                        bcsUnknown3HashMap.remove(grandChild);
                                    }
                                }
                            }
                        }
                    }
                    case "Pants" -> {
                        bcsPartsHashMap.remove(parent);

                        for (TreeItem<String> child : parent.getChildren()) {
                            switch (parent.getValue()) {
                                case "Color Selectors" -> {
                                    for (TreeItem<String> grandChild : child.getChildren()) {
                                        bcsColorsSelectorHashMap.remove(grandChild);
                                    }
                                }
                                case "Physics" -> {
                                    for (TreeItem<String> grandChild : child.getChildren()) {
                                        bcsPhysicsHashMap.remove(grandChild);
                                    }
                                }
                                case "Unknown 3" -> {
                                    for (TreeItem<String> grandChild : child.getChildren()) {
                                        bcsUnknown3HashMap.remove(grandChild);
                                    }
                                }
                            }
                        }
                    }
                    case "Rist" -> {
                        bcsPartsHashMap.remove(parent);

                        for (TreeItem<String> child : parent.getChildren()) {
                            switch (parent.getValue()) {
                                case "Color Selectors" -> {
                                    for (TreeItem<String> grandChild : child.getChildren()) {
                                        bcsColorsSelectorHashMap.remove(grandChild);
                                    }
                                }
                                case "Physics" -> {
                                    for (TreeItem<String> grandChild : child.getChildren()) {
                                        bcsPhysicsHashMap.remove(grandChild);
                                    }
                                }
                                case "Unknown 3" -> {
                                    for (TreeItem<String> grandChild : child.getChildren()) {
                                        bcsUnknown3HashMap.remove(grandChild);
                                    }
                                }
                            }
                        }
                    }
                    case "Boots" -> {
                        bcsPartsHashMap.remove(parent);

                        for (TreeItem<String> child : parent.getChildren()) {
                            switch (parent.getValue()) {
                                case "Color Selectors" -> {
                                    for (TreeItem<String> grandChild : child.getChildren()) {
                                        bcsColorsSelectorHashMap.remove(grandChild);
                                    }
                                }
                                case "Physics" -> {
                                    for (TreeItem<String> grandChild : child.getChildren()) {
                                        bcsPhysicsHashMap.remove(grandChild);
                                    }
                                }
                                case "Unknown 3" -> {
                                    for (TreeItem<String> grandChild : child.getChildren()) {
                                        bcsUnknown3HashMap.remove(grandChild);
                                    }
                                }
                            }
                        }
                    }
                }
            }

            currentPartSetEntry.getChildren().clear();
            
            for (int i = 0; i < copySubTypesContainer.length; i++) {
                switch (copySubTypesContainer[i]) {
                    case "Face Base" -> {
                        currentPartSetEntry.getChildren().add(i, new TreeItem<>("Face Base"));
                        bcsPartsHashMap.put(currentPartSetEntry.getChildren().get(i), new BcsPart((BcsPart) copyPartsContainer[i]));

                        for (int j = 0; j < copyTypesContainer[i].length; j++) {
                            switch (copyTypesContainer[i][j]) {
                                case "Color Selectors" -> {
                                    currentPartSetEntry.getChildren().get(i).getChildren().add(j, new TreeItem<>("Color Selectors"));

                                    for (int k = 0; k < copyPartSetContainer[i][j].length; k++) {
                                        currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().add(k, new TreeItem<>("Entry " + k));

                                        bcsColorsSelectorHashMap.put(currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().get(k), new BcsColorSelector((BcsColorSelector) copyPartSetContainer[i][j][k]));
                                    }
                                }
                                case "Physics" -> {
                                    currentPartSetEntry.getChildren().get(i).getChildren().add(j, new TreeItem<>("Physics"));

                                    for (int k = 0; k < copyPartSetContainer[i][j].length; k++) {
                                        currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().add(k, new TreeItem<>("Entry " + k));

                                        bcsPhysicsHashMap.put(currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().get(k), new BcsPhysics((BcsPhysics) copyPartSetContainer[i][j][k]));
                                    }
                                }
                                case "Unknown 3" -> {
                                    currentPartSetEntry.getChildren().get(i).getChildren().add(j, new TreeItem<>("Unknown 3"));

                                    for (int k = 0; k < copyPartSetContainer[i][j].length; k++) {
                                        currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().add(k, new TreeItem<>("Entry " + k));

                                        bcsUnknown3HashMap.put(currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().get(k), new BcsUnknown3((BcsUnknown3) copyPartSetContainer[i][j][k]));
                                    }
                                }
                            }
                        }
                    }
                    case "Face Forehead" -> {
                        currentPartSetEntry.getChildren().add(i, new TreeItem<>("Face Forehead"));
                        bcsPartsHashMap.put(currentPartSetEntry.getChildren().get(i), new BcsPart((BcsPart) copyPartsContainer[i]));

                        for (int j = 0; j < copyTypesContainer[i].length; j++) {
                            switch (copyTypesContainer[i][j]) {
                                case "Color Selectors" -> {
                                    currentPartSetEntry.getChildren().get(i).getChildren().add(j, new TreeItem<>("Color Selectors"));

                                    for (int k = 0; k < copyPartSetContainer[i][j].length; k++) {
                                        currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().add(k, new TreeItem<>("Entry " + k));

                                        bcsColorsSelectorHashMap.put(currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().get(k), new BcsColorSelector((BcsColorSelector) copyPartSetContainer[i][j][k]));
                                    }
                                }
                                case "Physics" -> {
                                    currentPartSetEntry.getChildren().get(i).getChildren().add(j, new TreeItem<>("Physics"));

                                    for (int k = 0; k < copyPartSetContainer[i][j].length; k++) {
                                        currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().add(k, new TreeItem<>("Entry " + k));

                                        bcsPhysicsHashMap.put(currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().get(k), new BcsPhysics((BcsPhysics) copyPartSetContainer[i][j][k]));
                                    }
                                }
                                case "Unknown 3" -> {
                                    currentPartSetEntry.getChildren().get(i).getChildren().add(j, new TreeItem<>("Unknown 3"));

                                    for (int k = 0; k < copyPartSetContainer[i][j].length; k++) {
                                        currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().add(k, new TreeItem<>("Entry " + k));

                                        bcsUnknown3HashMap.put(currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().get(k), new BcsUnknown3((BcsUnknown3) copyPartSetContainer[i][j][k]));
                                    }
                                }
                            }
                        }
                    }
                    case "Face Eye" -> {
                        currentPartSetEntry.getChildren().add(i, new TreeItem<>("Face Eye"));
                        bcsPartsHashMap.put(currentPartSetEntry.getChildren().get(i), new BcsPart((BcsPart) copyPartsContainer[i]));

                        for (int j = 0; j < copyTypesContainer[i].length; j++) {
                            switch (copyTypesContainer[i][j]) {
                                case "Color Selectors" -> {
                                    currentPartSetEntry.getChildren().get(i).getChildren().add(j, new TreeItem<>("Color Selectors"));

                                    for (int k = 0; k < copyPartSetContainer[i][j].length; k++) {
                                        currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().add(k, new TreeItem<>("Entry " + k));

                                        bcsColorsSelectorHashMap.put(currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().get(k), new BcsColorSelector((BcsColorSelector) copyPartSetContainer[i][j][k]));
                                    }
                                }
                                case "Physics" -> {
                                    currentPartSetEntry.getChildren().get(i).getChildren().add(j, new TreeItem<>("Physics"));

                                    for (int k = 0; k < copyPartSetContainer[i][j].length; k++) {
                                        currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().add(k, new TreeItem<>("Entry " + k));

                                        bcsPhysicsHashMap.put(currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().get(k), new BcsPhysics((BcsPhysics) copyPartSetContainer[i][j][k]));
                                    }
                                }
                                case "Unknown 3" -> {
                                    currentPartSetEntry.getChildren().get(i).getChildren().add(j, new TreeItem<>("Unknown 3"));

                                    for (int k = 0; k < copyPartSetContainer[i][j].length; k++) {
                                        currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().add(k, new TreeItem<>("Entry " + k));

                                        bcsUnknown3HashMap.put(currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().get(k), new BcsUnknown3((BcsUnknown3) copyPartSetContainer[i][j][k]));
                                    }
                                }
                            }
                        }
                    }
                    case "Face Nose" -> {
                        currentPartSetEntry.getChildren().add(i, new TreeItem<>("Face Nose"));
                        bcsPartsHashMap.put(currentPartSetEntry.getChildren().get(i), new BcsPart((BcsPart) copyPartsContainer[i]));

                        for (int j = 0; j < copyTypesContainer[i].length; j++) {
                            switch (copyTypesContainer[i][j]) {
                                case "Color Selectors" -> {
                                    currentPartSetEntry.getChildren().get(i).getChildren().add(j, new TreeItem<>("Color Selectors"));

                                    for (int k = 0; k < copyPartSetContainer[i][j].length; k++) {
                                        currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().add(k, new TreeItem<>("Entry " + k));

                                        bcsColorsSelectorHashMap.put(currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().get(k), new BcsColorSelector((BcsColorSelector) copyPartSetContainer[i][j][k]));
                                    }
                                }
                                case "Physics" -> {
                                    currentPartSetEntry.getChildren().get(i).getChildren().add(j, new TreeItem<>("Physics"));

                                    for (int k = 0; k < copyPartSetContainer[i][j].length; k++) {
                                        currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().add(k, new TreeItem<>("Entry " + k));

                                        bcsPhysicsHashMap.put(currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().get(k), new BcsPhysics((BcsPhysics) copyPartSetContainer[i][j][k]));
                                    }
                                }
                                case "Unknown 3" -> {
                                    currentPartSetEntry.getChildren().get(i).getChildren().add(j, new TreeItem<>("Unknown 3"));

                                    for (int k = 0; k < copyPartSetContainer[i][j].length; k++) {
                                        currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().add(k, new TreeItem<>("Entry " + k));

                                        bcsUnknown3HashMap.put(currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().get(k), new BcsUnknown3((BcsUnknown3) copyPartSetContainer[i][j][k]));
                                    }
                                }
                            }
                        }
                    }
                    case "Face Ear" -> {
                        currentPartSetEntry.getChildren().add(i, new TreeItem<>("Face Ear"));
                        bcsPartsHashMap.put(currentPartSetEntry.getChildren().get(i), new BcsPart((BcsPart) copyPartsContainer[i]));

                        for (int j = 0; j < copyTypesContainer[i].length; j++) {
                            switch (copyTypesContainer[i][j]) {
                                case "Color Selectors" -> {
                                    currentPartSetEntry.getChildren().get(i).getChildren().add(j, new TreeItem<>("Color Selectors"));

                                    for (int k = 0; k < copyPartSetContainer[i][j].length; k++) {
                                        currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().add(k, new TreeItem<>("Entry " + k));

                                        bcsColorsSelectorHashMap.put(currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().get(k), new BcsColorSelector((BcsColorSelector) copyPartSetContainer[i][j][k]));
                                    }
                                }
                                case "Physics" -> {
                                    currentPartSetEntry.getChildren().get(i).getChildren().add(j, new TreeItem<>("Physics"));

                                    for (int k = 0; k < copyPartSetContainer[i][j].length; k++) {
                                        currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().add(k, new TreeItem<>("Entry " + k));

                                        bcsPhysicsHashMap.put(currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().get(k), new BcsPhysics((BcsPhysics) copyPartSetContainer[i][j][k]));
                                    }
                                }
                                case "Unknown 3" -> {
                                    currentPartSetEntry.getChildren().get(i).getChildren().add(j, new TreeItem<>("Unknown 3"));

                                    for (int k = 0; k < copyPartSetContainer[i][j].length; k++) {
                                        currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().add(k, new TreeItem<>("Entry " + k));

                                        bcsUnknown3HashMap.put(currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().get(k), new BcsUnknown3((BcsUnknown3) copyPartSetContainer[i][j][k]));
                                    }
                                }
                            }
                        }
                    }
                    case "Hair" -> {
                        currentPartSetEntry.getChildren().add(i, new TreeItem<>("Hair"));
                        bcsPartsHashMap.put(currentPartSetEntry.getChildren().get(i), new BcsPart((BcsPart) copyPartsContainer[i]));

                        for (int j = 0; j < copyTypesContainer[i].length; j++) {
                            switch (copyTypesContainer[i][j]) {
                                case "Color Selectors" -> {
                                    currentPartSetEntry.getChildren().get(i).getChildren().add(j, new TreeItem<>("Color Selectors"));

                                    for (int k = 0; k < copyPartSetContainer[i][j].length; k++) {
                                        currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().add(k, new TreeItem<>("Entry " + k));

                                        bcsColorsSelectorHashMap.put(currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().get(k), new BcsColorSelector((BcsColorSelector) copyPartSetContainer[i][j][k]));
                                    }
                                }
                                case "Physics" -> {
                                    currentPartSetEntry.getChildren().get(i).getChildren().add(j, new TreeItem<>("Physics"));

                                    for (int k = 0; k < copyPartSetContainer[i][j].length; k++) {
                                        currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().add(k, new TreeItem<>("Entry " + k));

                                        bcsPhysicsHashMap.put(currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().get(k), new BcsPhysics((BcsPhysics) copyPartSetContainer[i][j][k]));
                                    }
                                }
                                case "Unknown 3" -> {
                                    currentPartSetEntry.getChildren().get(i).getChildren().add(j, new TreeItem<>("Unknown 3"));

                                    for (int k = 0; k < copyPartSetContainer[i][j].length; k++) {
                                        currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().add(k, new TreeItem<>("Entry " + k));

                                        bcsUnknown3HashMap.put(currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().get(k), new BcsUnknown3((BcsUnknown3) copyPartSetContainer[i][j][k]));
                                    }
                                }
                            }
                        }
                    }
                    case "Bust" -> {
                        currentPartSetEntry.getChildren().add(i, new TreeItem<>("Bust"));
                        bcsPartsHashMap.put(currentPartSetEntry.getChildren().get(i), new BcsPart((BcsPart) copyPartsContainer[i]));

                        for (int j = 0; j < copyTypesContainer[i].length; j++) {
                            switch (copyTypesContainer[i][j]) {
                                case "Color Selectors" -> {
                                    currentPartSetEntry.getChildren().get(i).getChildren().add(j, new TreeItem<>("Color Selectors"));

                                    for (int k = 0; k < copyPartSetContainer[i][j].length; k++) {
                                        currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().add(k, new TreeItem<>("Entry " + k));

                                        bcsColorsSelectorHashMap.put(currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().get(k), new BcsColorSelector((BcsColorSelector) copyPartSetContainer[i][j][k]));
                                    }
                                }
                                case "Physics" -> {
                                    currentPartSetEntry.getChildren().get(i).getChildren().add(j, new TreeItem<>("Physics"));

                                    for (int k = 0; k < copyPartSetContainer[i][j].length; k++) {
                                        currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().add(k, new TreeItem<>("Entry " + k));

                                        bcsPhysicsHashMap.put(currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().get(k), new BcsPhysics((BcsPhysics) copyPartSetContainer[i][j][k]));
                                    }
                                }
                                case "Unknown 3" -> {
                                    currentPartSetEntry.getChildren().get(i).getChildren().add(j, new TreeItem<>("Unknown 3"));

                                    for (int k = 0; k < copyPartSetContainer[i][j].length; k++) {
                                        currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().add(k, new TreeItem<>("Entry " + k));

                                        bcsUnknown3HashMap.put(currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().get(k), new BcsUnknown3((BcsUnknown3) copyPartSetContainer[i][j][k]));
                                    }
                                }
                            }
                        }
                    }
                    case "Pants" -> {
                        currentPartSetEntry.getChildren().add(i, new TreeItem<>("Pants"));
                        bcsPartsHashMap.put(currentPartSetEntry.getChildren().get(i), new BcsPart((BcsPart) copyPartsContainer[i]));

                        for (int j = 0; j < copyTypesContainer[i].length; j++) {
                            switch (copyTypesContainer[i][j]) {
                                case "Color Selectors" -> {
                                    currentPartSetEntry.getChildren().get(i).getChildren().add(j, new TreeItem<>("Color Selectors"));

                                    for (int k = 0; k < copyPartSetContainer[i][j].length; k++) {
                                        currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().add(k, new TreeItem<>("Entry " + k));

                                        bcsColorsSelectorHashMap.put(currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().get(k), new BcsColorSelector((BcsColorSelector) copyPartSetContainer[i][j][k]));
                                    }
                                }
                                case "Physics" -> {
                                    currentPartSetEntry.getChildren().get(i).getChildren().add(j, new TreeItem<>("Physics"));

                                    for (int k = 0; k < copyPartSetContainer[i][j].length; k++) {
                                        currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().add(k, new TreeItem<>("Entry " + k));

                                        bcsPhysicsHashMap.put(currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().get(k), new BcsPhysics((BcsPhysics) copyPartSetContainer[i][j][k]));
                                    }
                                }
                                case "Unknown 3" -> {
                                    currentPartSetEntry.getChildren().get(i).getChildren().add(j, new TreeItem<>("Unknown 3"));

                                    for (int k = 0; k < copyPartSetContainer[i][j].length; k++) {
                                        currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().add(k, new TreeItem<>("Entry " + k));

                                        bcsUnknown3HashMap.put(currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().get(k), new BcsUnknown3((BcsUnknown3) copyPartSetContainer[i][j][k]));
                                    }
                                }
                            }
                        }
                    }
                    case "Rist" -> {
                        currentPartSetEntry.getChildren().add(i, new TreeItem<>("Rist"));
                        bcsPartsHashMap.put(currentPartSetEntry.getChildren().get(i), new BcsPart((BcsPart) copyPartsContainer[i]));

                        for (int j = 0; j < copyTypesContainer[i].length; j++) {
                            switch (copyTypesContainer[i][j]) {
                                case "Color Selectors" -> {
                                    currentPartSetEntry.getChildren().get(i).getChildren().add(j, new TreeItem<>("Color Selectors"));

                                    for (int k = 0; k < copyPartSetContainer[i][j].length; k++) {
                                        currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().add(k, new TreeItem<>("Entry " + k));

                                        bcsColorsSelectorHashMap.put(currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().get(k), new BcsColorSelector((BcsColorSelector) copyPartSetContainer[i][j][k]));
                                    }
                                }
                                case "Physics" -> {
                                    currentPartSetEntry.getChildren().get(i).getChildren().add(j, new TreeItem<>("Physics"));

                                    for (int k = 0; k < copyPartSetContainer[i][j].length; k++) {
                                        currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().add(k, new TreeItem<>("Entry " + k));

                                        bcsPhysicsHashMap.put(currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().get(k), new BcsPhysics((BcsPhysics) copyPartSetContainer[i][j][k]));
                                    }
                                }
                                case "Unknown 3" -> {
                                    currentPartSetEntry.getChildren().get(i).getChildren().add(j, new TreeItem<>("Unknown 3"));

                                    for (int k = 0; k < copyPartSetContainer[i][j].length; k++) {
                                        currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().add(k, new TreeItem<>("Entry " + k));

                                        bcsUnknown3HashMap.put(currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().get(k), new BcsUnknown3((BcsUnknown3) copyPartSetContainer[i][j][k]));
                                    }
                                }
                            }
                        }
                    }
                    case "Boots" -> {
                        currentPartSetEntry.getChildren().add(i, new TreeItem<>("Boots"));
                        bcsPartsHashMap.put(currentPartSetEntry.getChildren().get(i), new BcsPart((BcsPart) copyPartsContainer[i]));

                        for (int j = 0; j < copyTypesContainer[i].length; j++) {
                            switch (copyTypesContainer[i][j]) {
                                case "Color Selectors" -> {
                                    currentPartSetEntry.getChildren().get(i).getChildren().add(j, new TreeItem<>("Color Selectors"));

                                    for (int k = 0; k < copyPartSetContainer[i][j].length; k++) {
                                        currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().add(k, new TreeItem<>("Entry " + k));

                                        bcsColorsSelectorHashMap.put(currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().get(k), new BcsColorSelector((BcsColorSelector) copyPartSetContainer[i][j][k]));
                                    }
                                }
                                case "Physics" -> {
                                    currentPartSetEntry.getChildren().get(i).getChildren().add(j, new TreeItem<>("Physics"));

                                    for (int k = 0; k < copyPartSetContainer[i][j].length; k++) {
                                        currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().add(k, new TreeItem<>("Entry " + k));

                                        bcsPhysicsHashMap.put(currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().get(k), new BcsPhysics((BcsPhysics) copyPartSetContainer[i][j][k]));
                                    }
                                }
                                case "Unknown 3" -> {
                                    currentPartSetEntry.getChildren().get(i).getChildren().add(j, new TreeItem<>("Unknown 3"));

                                    for (int k = 0; k < copyPartSetContainer[i][j].length; k++) {
                                        currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().add(k, new TreeItem<>("Entry " + k));

                                        bcsUnknown3HashMap.put(currentPartSetEntry.getChildren().get(i).getChildren().get(j).getChildren().get(k), new BcsUnknown3((BcsUnknown3) copyPartSetContainer[i][j][k]));
                                    }
                                }
                            }
                        }
                    }

                }
            }
        }
        else if (currentPartSetEntry.getParent().getValue().contains("Part Set")) {
            for (TreeItem<String> parent : currentPartSetEntry.getChildren()) {
                switch (parent.getValue()) {
                    case "Color Selectors" -> {
                        for (TreeItem<String> child : parent.getChildren()) {
                            bcsColorsSelectorHashMap.remove(child);
                        }
                    }
                    case "Physics" -> {
                        for (TreeItem<String> child : parent.getChildren()) {
                            bcsPhysicsHashMap.remove(child);
                        }
                    }
                    case "Unknown 3" -> {
                        for (TreeItem<String> child : parent.getChildren()) {
                            bcsUnknown3HashMap.remove(child);
                        }
                    }
                }
            }

            currentPartSetEntry.getChildren().clear();

            bcsPartsHashMap.put(currentPartSetEntry, new BcsPart((BcsPart) copyContainer));

            for (int i = 0; i < copySubTypesContainer.length; i++) {
                switch (copySubTypesContainer[i]) {
                    case "Color Selectors" -> {
                        currentPartSetEntry.getChildren().add(i, new TreeItem<>("Color Selectors"));

                        for (int j = 0; j < copyListContainer[i].length; j++) {
                            currentPartSetEntry.getChildren().get(i).getChildren().add(j, new TreeItem<>("Entry " +j));

                            bcsColorsSelectorHashMap.put(currentPartSetEntry.getChildren().get(i).getChildren().get(j), new BcsColorSelector((BcsColorSelector) copyListContainer[i][j]));
                        }
                    }
                    case "Physics" -> {
                        currentPartSetEntry.getChildren().add(i, new TreeItem<>("Physics"));

                        for (int j = 0; j < copyListContainer[i].length; j++) {
                            currentPartSetEntry.getChildren().get(i).getChildren().add(j, new TreeItem<>("Entry " +j));

                            bcsPhysicsHashMap.put(currentPartSetEntry.getChildren().get(i).getChildren().get(j), new BcsPhysics((BcsPhysics) copyListContainer[i][j]));
                        }
                    }
                    case "Unknown 3" -> {
                        currentPartSetEntry.getChildren().add(i, new TreeItem<>("Unknown 3"));

                        for (int j = 0; j < copyListContainer[i].length; j++) {
                            currentPartSetEntry.getChildren().get(i).getChildren().add(j, new TreeItem<>("Entry " +j));

                            bcsUnknown3HashMap.put(currentPartSetEntry.getChildren().get(i).getChildren().get(j), new BcsUnknown3((BcsUnknown3) copyListContainer[i][j]));
                        }
                    }
                }
            }

            dynamicTabPane.getTabs().clear();
            createPart(bcsPartsHashMap.get(currentPartSetEntry));
            dynamicTabPane.getSelectionModel().select(dynamicTabPane.getSelectionModel().getSelectedIndex());
        }
        else if (currentPartSetEntry.getChildren().isEmpty()) {
            switch (currentPartSetEntry.getParent().getValue()) {
                case "Color Selectors" -> {
                    bcsColorsSelectorHashMap.put(currentPartSetEntry, new BcsColorSelector((BcsColorSelector) copyContainer));

                    dynamicTabPane.getTabs().clear();
                    createColorSelector(bcsColorsSelectorHashMap.get(currentPartSetEntry));
                    dynamicTabPane.getSelectionModel().select(dynamicTabPane.getSelectionModel().getSelectedIndex());
                }
                case "Physics" -> {
                    bcsPhysicsHashMap.put(currentPartSetEntry, new BcsPhysics((BcsPhysics) copyContainer));
                    
                    dynamicTabPane.getTabs().clear();
                    createPhysics(bcsPhysicsHashMap.get(currentPartSetEntry));
                    dynamicTabPane.getSelectionModel().select(dynamicTabPane.getSelectionModel().getSelectedIndex());
                }
                case "Unknown 3" -> {
                    bcsUnknown3HashMap.put(currentPartSetEntry, new BcsUnknown3((BcsUnknown3) copyContainer));
                    
                    dynamicTabPane.getTabs().clear();
                    createUnknown3(bcsUnknown3HashMap.get(currentPartSetEntry));
                    dynamicTabPane.getSelectionModel().select(dynamicTabPane.getSelectionModel().getSelectedIndex());
                }
            }
        }
        else {
            switch (currentPartSetEntry.getValue()) {
                case "Color Selectors" -> {
                    for (TreeItem<String> child : currentPartSetEntry.getChildren()) {
                        bcsColorsSelectorHashMap.remove(child);
                    }
                }
                case "Physics" -> {
                    for (TreeItem<String> child : currentPartSetEntry.getChildren()) {
                        bcsPhysicsHashMap.remove(child);
                    }
                }
                case "Unknown 3" -> {
                    for (TreeItem<String> child : currentPartSetEntry.getChildren()) {
                        bcsUnknown3HashMap.remove(child);
                    }
                }
            }

            currentPartSetEntry.getChildren().clear();

            switch (currentPartSetEntry.getValue()) {
                case "Color Selectors" -> {
                    for (int i = 0; i < copyListContainer[0].length; i++) {
                        currentPartSetEntry.getChildren().add(i, new TreeItem<>("Entry " +i));

                        bcsColorsSelectorHashMap.put(currentPartSetEntry.getChildren().get(i), new BcsColorSelector((BcsColorSelector) copyListContainer[0][i]));
                    }
                }
                case "Physics" -> {
                    for (int i = 0; i < copyListContainer[0].length; i++) {
                        currentPartSetEntry.getChildren().add(i, new TreeItem<>("Entry " +i));

                        bcsPhysicsHashMap.put(currentPartSetEntry.getChildren().get(i), new BcsPhysics((BcsPhysics) copyListContainer[0][i]));
                    }
                }
                case "Unknown 3" -> {
                    for (int i = 0; i < copyListContainer[0].length; i++) {
                        currentPartSetEntry.getChildren().add(i, new TreeItem<>("Entry " +i));

                        bcsUnknown3HashMap.put(currentPartSetEntry.getChildren().get(i), new BcsUnknown3((BcsUnknown3) copyListContainer[0][i]));
                    }
                }
            }
        }
    }

    private void AddPartSetItemCopy() {
        if (partSetsTreeView.getRoot().getChildren().isEmpty()) {
            allPartSetEntries = 0;
        } 

        if(addPartSetItemCopy.getText().contains("Part Set")) {
            partSetsTreeView.getRoot().getChildren().add(new TreeItem<>("Part Set " + allPartSetEntries));

            allPartSetEntries++;

            for (int i = 0; i < copySubTypesContainer.length; i++) {
                switch (copySubTypesContainer[i]) {
                    case "Face Base" -> {
                        partSetsTreeView.getRoot().getChildren().getLast().getChildren().add(i, new TreeItem<>("Face Base"));

                        bcsPartsHashMap.put(partSetsTreeView.getRoot().getChildren().getLast().getChildren().get(i), (BcsPart) copyPartsContainer[i]);

                        for (int j = 0; j < copyTypesContainer[i].length; j++) {
                            switch (copyTypesContainer[i][j]) {
                                case "Color Selectors" -> {
                                    partSetsTreeView.getRoot().getChildren().getLast().getChildren().get(i).getChildren().add(j, new TreeItem<>("Color Selectors"));

                                    for (int k = 0; k < copyPartSetContainer[i][j].length; k++) {
                                        partSetsTreeView.getRoot().getChildren().getLast().getChildren().get(i).getChildren().get(j).getChildren().add(new TreeItem<>("Entry " + k));

                                        bcsColorsSelectorHashMap.put(partSetsTreeView.getRoot().getChildren().getLast().getChildren().get(i).getChildren().get(j).getChildren().get(k), new BcsColorSelector((BcsColorSelector) copyPartSetContainer[i][j][k]));
                                    }
                                }
                                case "Physics" -> {
                                    partSetsTreeView.getRoot().getChildren().getLast().getChildren().get(i).getChildren().add(j, new TreeItem<>("Physics"));

                                    for (int k = 0; k < copyPartSetContainer[i][j].length; k++) {
                                        partSetsTreeView.getRoot().getChildren().getLast().getChildren().get(i).getChildren().get(j).getChildren().add(new TreeItem<>("Entry " + k));

                                        bcsPhysicsHashMap.put(partSetsTreeView.getRoot().getChildren().getLast().getChildren().get(i).getChildren().get(j).getChildren().get(k), new BcsPhysics((BcsPhysics) copyPartSetContainer[i][j][k]));
                                    }
                                }
                                case "Unknown 3" -> {
                                    partSetsTreeView.getRoot().getChildren().getLast().getChildren().get(i).getChildren().add(j, new TreeItem<>("Unknown 3"));

                                    for (int k = 0; k < copyPartSetContainer[i][j].length; k++) {
                                        partSetsTreeView.getRoot().getChildren().getLast().getChildren().get(i).getChildren().get(j).getChildren().add(new TreeItem<>("Entry " + k));

                                        bcsUnknown3HashMap.put(partSetsTreeView.getRoot().getChildren().getLast().getChildren().get(i).getChildren().get(j).getChildren().get(k), new BcsUnknown3((BcsUnknown3) copyPartSetContainer[i][j][k]));
                                    }
                                }
                            }
                        }
                    }
                    case "Face Forehead" -> {
                        partSetsTreeView.getRoot().getChildren().getLast().getChildren().add(i, new TreeItem<>("Face Forehead"));

                        bcsPartsHashMap.put(partSetsTreeView.getRoot().getChildren().getLast().getChildren().get(i), (BcsPart) copyPartsContainer[i]);

                        for (int j = 0; j < copyTypesContainer[i].length; j++) {
                            switch (copyTypesContainer[i][j]) {
                                case "Color Selectors" -> {
                                    partSetsTreeView.getRoot().getChildren().getLast().getChildren().get(i).getChildren().add(j, new TreeItem<>("Color Selectors"));

                                    for (int k = 0; k < copyPartSetContainer[i][j].length; k++) {
                                        partSetsTreeView.getRoot().getChildren().getLast().getChildren().get(i).getChildren().get(j).getChildren().add(new TreeItem<>("Entry " + k));

                                        bcsColorsSelectorHashMap.put(partSetsTreeView.getRoot().getChildren().getLast().getChildren().get(i).getChildren().get(j).getChildren().get(k), new BcsColorSelector((BcsColorSelector) copyPartSetContainer[i][j][k]));
                                    }
                                }
                                case "Physics" -> {
                                    partSetsTreeView.getRoot().getChildren().getLast().getChildren().get(i).getChildren().add(j, new TreeItem<>("Physics"));

                                    for (int k = 0; k < copyPartSetContainer[i][j].length; k++) {
                                        partSetsTreeView.getRoot().getChildren().getLast().getChildren().get(i).getChildren().get(j).getChildren().add(new TreeItem<>("Entry " + k));

                                        bcsPhysicsHashMap.put(partSetsTreeView.getRoot().getChildren().getLast().getChildren().get(i).getChildren().get(j).getChildren().get(k), new BcsPhysics((BcsPhysics) copyPartSetContainer[i][j][k]));
                                    }
                                }
                                case "Unknown 3" -> {
                                    partSetsTreeView.getRoot().getChildren().getLast().getChildren().get(i).getChildren().add(j, new TreeItem<>("Unknown 3"));

                                    for (int k = 0; k < copyPartSetContainer[i][j].length; k++) {
                                        partSetsTreeView.getRoot().getChildren().getLast().getChildren().get(i).getChildren().get(j).getChildren().add(new TreeItem<>("Entry " + k));

                                        bcsUnknown3HashMap.put(partSetsTreeView.getRoot().getChildren().getLast().getChildren().get(i).getChildren().get(j).getChildren().get(k), new BcsUnknown3((BcsUnknown3) copyPartSetContainer[i][j][k]));
                                    }
                                }
                            }
                        }
                    }
                    case "Face Eye" -> {
                        partSetsTreeView.getRoot().getChildren().getLast().getChildren().add(i, new TreeItem<>("Face Eye"));

                        bcsPartsHashMap.put(partSetsTreeView.getRoot().getChildren().getLast().getChildren().get(i), (BcsPart) copyPartsContainer[i]);

                        for (int j = 0; j < copyTypesContainer[i].length; j++) {
                            switch (copyTypesContainer[i][j]) {
                                case "Color Selectors" -> {
                                    partSetsTreeView.getRoot().getChildren().getLast().getChildren().get(i).getChildren().add(j, new TreeItem<>("Color Selectors"));

                                    for (int k = 0; k < copyPartSetContainer[i][j].length; k++) {
                                        partSetsTreeView.getRoot().getChildren().getLast().getChildren().get(i).getChildren().get(j).getChildren().add(new TreeItem<>("Entry " + k));

                                        bcsColorsSelectorHashMap.put(partSetsTreeView.getRoot().getChildren().getLast().getChildren().get(i).getChildren().get(j).getChildren().get(k), new BcsColorSelector((BcsColorSelector) copyPartSetContainer[i][j][k]));
                                    }
                                }
                                case "Physics" -> {
                                    partSetsTreeView.getRoot().getChildren().getLast().getChildren().get(i).getChildren().add(j, new TreeItem<>("Physics"));

                                    for (int k = 0; k < copyPartSetContainer[i][j].length; k++) {
                                        partSetsTreeView.getRoot().getChildren().getLast().getChildren().get(i).getChildren().get(j).getChildren().add(new TreeItem<>("Entry " + k));

                                        bcsPhysicsHashMap.put(partSetsTreeView.getRoot().getChildren().getLast().getChildren().get(i).getChildren().get(j).getChildren().get(k), new BcsPhysics((BcsPhysics) copyPartSetContainer[i][j][k]));
                                    }
                                }
                                case "Unknown 3" -> {
                                    partSetsTreeView.getRoot().getChildren().getLast().getChildren().get(i).getChildren().add(j, new TreeItem<>("Unknown 3"));

                                    for (int k = 0; k < copyPartSetContainer[i][j].length; k++) {
                                        partSetsTreeView.getRoot().getChildren().getLast().getChildren().get(i).getChildren().get(j).getChildren().add(new TreeItem<>("Entry " + k));

                                        bcsUnknown3HashMap.put(partSetsTreeView.getRoot().getChildren().getLast().getChildren().get(i).getChildren().get(j).getChildren().get(k), new BcsUnknown3((BcsUnknown3) copyPartSetContainer[i][j][k]));
                                    }
                                }
                            }
                        }
                    }
                    case "Face Nose" -> {
                        partSetsTreeView.getRoot().getChildren().getLast().getChildren().add(i, new TreeItem<>("Face Nose"));

                        bcsPartsHashMap.put(partSetsTreeView.getRoot().getChildren().getLast().getChildren().get(i), (BcsPart) copyPartsContainer[i]);

                        for (int j = 0; j < copyTypesContainer[i].length; j++) {
                            switch (copyTypesContainer[i][j]) {
                                case "Color Selectors" -> {
                                    partSetsTreeView.getRoot().getChildren().getLast().getChildren().get(i).getChildren().add(j, new TreeItem<>("Color Selectors"));

                                    for (int k = 0; k < copyPartSetContainer[i][j].length; k++) {
                                        partSetsTreeView.getRoot().getChildren().getLast().getChildren().get(i).getChildren().get(j).getChildren().add(new TreeItem<>("Entry " + k));

                                        bcsColorsSelectorHashMap.put(partSetsTreeView.getRoot().getChildren().getLast().getChildren().get(i).getChildren().get(j).getChildren().get(k), new BcsColorSelector((BcsColorSelector) copyPartSetContainer[i][j][k]));
                                    }
                                }
                                case "Physics" -> {
                                    partSetsTreeView.getRoot().getChildren().getLast().getChildren().get(i).getChildren().add(j, new TreeItem<>("Physics"));

                                    for (int k = 0; k < copyPartSetContainer[i][j].length; k++) {
                                        partSetsTreeView.getRoot().getChildren().getLast().getChildren().get(i).getChildren().get(j).getChildren().add(new TreeItem<>("Entry " + k));

                                        bcsPhysicsHashMap.put(partSetsTreeView.getRoot().getChildren().getLast().getChildren().get(i).getChildren().get(j).getChildren().get(k), new BcsPhysics((BcsPhysics) copyPartSetContainer[i][j][k]));
                                    }
                                }
                                case "Unknown 3" -> {
                                    partSetsTreeView.getRoot().getChildren().getLast().getChildren().get(i).getChildren().add(j, new TreeItem<>("Unknown 3"));

                                    for (int k = 0; k < copyPartSetContainer[i][j].length; k++) {
                                        partSetsTreeView.getRoot().getChildren().getLast().getChildren().get(i).getChildren().get(j).getChildren().add(new TreeItem<>("Entry " + k));

                                        bcsUnknown3HashMap.put(partSetsTreeView.getRoot().getChildren().getLast().getChildren().get(i).getChildren().get(j).getChildren().get(k), new BcsUnknown3((BcsUnknown3) copyPartSetContainer[i][j][k]));
                                    }
                                }
                            }
                        }
                    }
                    case "Face Ear" -> {
                        partSetsTreeView.getRoot().getChildren().getLast().getChildren().add(i, new TreeItem<>("Face Ear"));

                        bcsPartsHashMap.put(partSetsTreeView.getRoot().getChildren().getLast().getChildren().get(i), (BcsPart) copyPartsContainer[i]);

                        for (int j = 0; j < copyTypesContainer[i].length; j++) {
                            switch (copyTypesContainer[i][j]) {
                                case "Color Selectors" -> {
                                    partSetsTreeView.getRoot().getChildren().getLast().getChildren().get(i).getChildren().add(j, new TreeItem<>("Color Selectors"));

                                    for (int k = 0; k < copyPartSetContainer[i][j].length; k++) {
                                        partSetsTreeView.getRoot().getChildren().getLast().getChildren().get(i).getChildren().get(j).getChildren().add(new TreeItem<>("Entry " + k));

                                        bcsColorsSelectorHashMap.put(partSetsTreeView.getRoot().getChildren().getLast().getChildren().get(i).getChildren().get(j).getChildren().get(k), new BcsColorSelector((BcsColorSelector) copyPartSetContainer[i][j][k]));
                                    }
                                }
                                case "Physics" -> {
                                    partSetsTreeView.getRoot().getChildren().getLast().getChildren().get(i).getChildren().add(j, new TreeItem<>("Physics"));

                                    for (int k = 0; k < copyPartSetContainer[i][j].length; k++) {
                                        partSetsTreeView.getRoot().getChildren().getLast().getChildren().get(i).getChildren().get(j).getChildren().add(new TreeItem<>("Entry " + k));

                                        bcsPhysicsHashMap.put(partSetsTreeView.getRoot().getChildren().getLast().getChildren().get(i).getChildren().get(j).getChildren().get(k), new BcsPhysics((BcsPhysics) copyPartSetContainer[i][j][k]));
                                    }
                                }
                                case "Unknown 3" -> {
                                    partSetsTreeView.getRoot().getChildren().getLast().getChildren().get(i).getChildren().add(j, new TreeItem<>("Unknown 3"));

                                    for (int k = 0; k < copyPartSetContainer[i][j].length; k++) {
                                        partSetsTreeView.getRoot().getChildren().getLast().getChildren().get(i).getChildren().get(j).getChildren().add(new TreeItem<>("Entry " + k));

                                        bcsUnknown3HashMap.put(partSetsTreeView.getRoot().getChildren().getLast().getChildren().get(i).getChildren().get(j).getChildren().get(k), new BcsUnknown3((BcsUnknown3) copyPartSetContainer[i][j][k]));
                                    }
                                }
                            }
                        }
                    }
                    case "Hair" -> {
                        partSetsTreeView.getRoot().getChildren().getLast().getChildren().add(i, new TreeItem<>("Hair"));

                        bcsPartsHashMap.put(partSetsTreeView.getRoot().getChildren().getLast().getChildren().get(i), (BcsPart) copyPartsContainer[i]);

                        for (int j = 0; j < copyTypesContainer[i].length; j++) {
                            switch (copyTypesContainer[i][j]) {
                                case "Color Selectors" -> {
                                    partSetsTreeView.getRoot().getChildren().getLast().getChildren().get(i).getChildren().add(j, new TreeItem<>("Color Selectors"));

                                    for (int k = 0; k < copyPartSetContainer[i][j].length; k++) {
                                        partSetsTreeView.getRoot().getChildren().getLast().getChildren().get(i).getChildren().get(j).getChildren().add(new TreeItem<>("Entry " + k));

                                        bcsColorsSelectorHashMap.put(partSetsTreeView.getRoot().getChildren().getLast().getChildren().get(i).getChildren().get(j).getChildren().get(k), new BcsColorSelector((BcsColorSelector) copyPartSetContainer[i][j][k]));
                                    }
                                }
                                case "Physics" -> {
                                    partSetsTreeView.getRoot().getChildren().getLast().getChildren().get(i).getChildren().add(j, new TreeItem<>("Physics"));

                                    for (int k = 0; k < copyPartSetContainer[i][j].length; k++) {
                                        partSetsTreeView.getRoot().getChildren().getLast().getChildren().get(i).getChildren().get(j).getChildren().add(new TreeItem<>("Entry " + k));

                                        bcsPhysicsHashMap.put(partSetsTreeView.getRoot().getChildren().getLast().getChildren().get(i).getChildren().get(j).getChildren().get(k), new BcsPhysics((BcsPhysics) copyPartSetContainer[i][j][k]));
                                    }
                                }
                                case "Unknown 3" -> {
                                    partSetsTreeView.getRoot().getChildren().getLast().getChildren().get(i).getChildren().add(j, new TreeItem<>("Unknown 3"));

                                    for (int k = 0; k < copyPartSetContainer[i][j].length; k++) {
                                        partSetsTreeView.getRoot().getChildren().getLast().getChildren().get(i).getChildren().get(j).getChildren().add(new TreeItem<>("Entry " + k));

                                        bcsUnknown3HashMap.put(partSetsTreeView.getRoot().getChildren().getLast().getChildren().get(i).getChildren().get(j).getChildren().get(k), new BcsUnknown3((BcsUnknown3) copyPartSetContainer[i][j][k]));
                                    }
                                }
                            }
                        }
                    }
                    case "Bust" -> {
                        partSetsTreeView.getRoot().getChildren().getLast().getChildren().add(i, new TreeItem<>("Bust"));

                        bcsPartsHashMap.put(partSetsTreeView.getRoot().getChildren().getLast().getChildren().get(i), (BcsPart) copyPartsContainer[i]);

                        for (int j = 0; j < copyTypesContainer[i].length; j++) {
                            switch (copyTypesContainer[i][j]) {
                                case "Color Selectors" -> {
                                    partSetsTreeView.getRoot().getChildren().getLast().getChildren().get(i).getChildren().add(j, new TreeItem<>("Color Selectors"));

                                    for (int k = 0; k < copyPartSetContainer[i][j].length; k++) {
                                        partSetsTreeView.getRoot().getChildren().getLast().getChildren().get(i).getChildren().get(j).getChildren().add(new TreeItem<>("Entry " + k));

                                        bcsColorsSelectorHashMap.put(partSetsTreeView.getRoot().getChildren().getLast().getChildren().get(i).getChildren().get(j).getChildren().get(k), new BcsColorSelector((BcsColorSelector) copyPartSetContainer[i][j][k]));
                                    }
                                }
                                case "Physics" -> {
                                    partSetsTreeView.getRoot().getChildren().getLast().getChildren().get(i).getChildren().add(j, new TreeItem<>("Physics"));

                                    for (int k = 0; k < copyPartSetContainer[i][j].length; k++) {
                                        partSetsTreeView.getRoot().getChildren().getLast().getChildren().get(i).getChildren().get(j).getChildren().add(new TreeItem<>("Entry " + k));

                                        bcsPhysicsHashMap.put(partSetsTreeView.getRoot().getChildren().getLast().getChildren().get(i).getChildren().get(j).getChildren().get(k), new BcsPhysics((BcsPhysics) copyPartSetContainer[i][j][k]));
                                    }
                                }
                                case "Unknown 3" -> {
                                    partSetsTreeView.getRoot().getChildren().getLast().getChildren().get(i).getChildren().add(j, new TreeItem<>("Unknown 3"));

                                    for (int k = 0; k < copyPartSetContainer[i][j].length; k++) {
                                        partSetsTreeView.getRoot().getChildren().getLast().getChildren().get(i).getChildren().get(j).getChildren().add(new TreeItem<>("Entry " + k));

                                        bcsUnknown3HashMap.put(partSetsTreeView.getRoot().getChildren().getLast().getChildren().get(i).getChildren().get(j).getChildren().get(k), new BcsUnknown3((BcsUnknown3) copyPartSetContainer[i][j][k]));
                                    }
                                }
                            }
                        }
                    }
                    case "Pants" -> {
                        partSetsTreeView.getRoot().getChildren().getLast().getChildren().add(i, new TreeItem<>("Pants"));

                        bcsPartsHashMap.put(partSetsTreeView.getRoot().getChildren().getLast().getChildren().get(i), (BcsPart) copyPartsContainer[i]);

                        for (int j = 0; j < copyTypesContainer[i].length; j++) {
                            switch (copyTypesContainer[i][j]) {
                                case "Color Selectors" -> {
                                    partSetsTreeView.getRoot().getChildren().getLast().getChildren().get(i).getChildren().add(j, new TreeItem<>("Color Selectors"));

                                    for (int k = 0; k < copyPartSetContainer[i][j].length; k++) {
                                        partSetsTreeView.getRoot().getChildren().getLast().getChildren().get(i).getChildren().get(j).getChildren().add(new TreeItem<>("Entry " + k));

                                        bcsColorsSelectorHashMap.put(partSetsTreeView.getRoot().getChildren().getLast().getChildren().get(i).getChildren().get(j).getChildren().get(k), new BcsColorSelector((BcsColorSelector) copyPartSetContainer[i][j][k]));
                                    }
                                }
                                case "Physics" -> {
                                    partSetsTreeView.getRoot().getChildren().getLast().getChildren().get(i).getChildren().add(j, new TreeItem<>("Physics"));

                                    for (int k = 0; k < copyPartSetContainer[i][j].length; k++) {
                                        partSetsTreeView.getRoot().getChildren().getLast().getChildren().get(i).getChildren().get(j).getChildren().add(new TreeItem<>("Entry " + k));

                                        bcsPhysicsHashMap.put(partSetsTreeView.getRoot().getChildren().getLast().getChildren().get(i).getChildren().get(j).getChildren().get(k), new BcsPhysics((BcsPhysics) copyPartSetContainer[i][j][k]));
                                    }
                                }
                                case "Unknown 3" -> {
                                    partSetsTreeView.getRoot().getChildren().getLast().getChildren().get(i).getChildren().add(j, new TreeItem<>("Unknown 3"));

                                    for (int k = 0; k < copyPartSetContainer[i][j].length; k++) {
                                        partSetsTreeView.getRoot().getChildren().getLast().getChildren().get(i).getChildren().get(j).getChildren().add(new TreeItem<>("Entry " + k));

                                        bcsUnknown3HashMap.put(partSetsTreeView.getRoot().getChildren().getLast().getChildren().get(i).getChildren().get(j).getChildren().get(k), new BcsUnknown3((BcsUnknown3) copyPartSetContainer[i][j][k]));
                                    }
                                }
                            }
                        }
                    }
                    case "Rist" -> {
                        partSetsTreeView.getRoot().getChildren().getLast().getChildren().add(i, new TreeItem<>("Rist"));

                        bcsPartsHashMap.put(partSetsTreeView.getRoot().getChildren().getLast().getChildren().get(i), (BcsPart) copyPartsContainer[i]);

                        for (int j = 0; j < copyTypesContainer[i].length; j++) {
                            switch (copyTypesContainer[i][j]) {
                                case "Color Selectors" -> {
                                    partSetsTreeView.getRoot().getChildren().getLast().getChildren().get(i).getChildren().add(j, new TreeItem<>("Color Selectors"));

                                    for (int k = 0; k < copyPartSetContainer[i][j].length; k++) {
                                        partSetsTreeView.getRoot().getChildren().getLast().getChildren().get(i).getChildren().get(j).getChildren().add(new TreeItem<>("Entry " + k));

                                        bcsColorsSelectorHashMap.put(partSetsTreeView.getRoot().getChildren().getLast().getChildren().get(i).getChildren().get(j).getChildren().get(k), new BcsColorSelector((BcsColorSelector) copyPartSetContainer[i][j][k]));
                                    }
                                }
                                case "Physics" -> {
                                    partSetsTreeView.getRoot().getChildren().getLast().getChildren().get(i).getChildren().add(j, new TreeItem<>("Physics"));

                                    for (int k = 0; k < copyPartSetContainer[i][j].length; k++) {
                                        partSetsTreeView.getRoot().getChildren().getLast().getChildren().get(i).getChildren().get(j).getChildren().add(new TreeItem<>("Entry " + k));

                                        bcsPhysicsHashMap.put(partSetsTreeView.getRoot().getChildren().getLast().getChildren().get(i).getChildren().get(j).getChildren().get(k), new BcsPhysics((BcsPhysics) copyPartSetContainer[i][j][k]));
                                    }
                                }
                                case "Unknown 3" -> {
                                    partSetsTreeView.getRoot().getChildren().getLast().getChildren().get(i).getChildren().add(j, new TreeItem<>("Unknown 3"));

                                    for (int k = 0; k < copyPartSetContainer[i][j].length; k++) {
                                        partSetsTreeView.getRoot().getChildren().getLast().getChildren().get(i).getChildren().get(j).getChildren().add(new TreeItem<>("Entry " + k));

                                        bcsUnknown3HashMap.put(partSetsTreeView.getRoot().getChildren().getLast().getChildren().get(i).getChildren().get(j).getChildren().get(k), new BcsUnknown3((BcsUnknown3) copyPartSetContainer[i][j][k]));
                                    }
                                }
                            }
                        }
                    }
                    case "Boots" -> {
                        partSetsTreeView.getRoot().getChildren().getLast().getChildren().add(i, new TreeItem<>("Boots"));

                        bcsPartsHashMap.put(partSetsTreeView.getRoot().getChildren().getLast().getChildren().get(i), (BcsPart) copyPartsContainer[i]);

                        for (int j = 0; j < copyTypesContainer[i].length; j++) {
                            switch (copyTypesContainer[i][j]) {
                                case "Color Selectors" -> {
                                    partSetsTreeView.getRoot().getChildren().getLast().getChildren().get(i).getChildren().add(j, new TreeItem<>("Color Selectors"));

                                    for (int k = 0; k < copyPartSetContainer[i][j].length; k++) {
                                        partSetsTreeView.getRoot().getChildren().getLast().getChildren().get(i).getChildren().get(j).getChildren().add(new TreeItem<>("Entry " + k));

                                        bcsColorsSelectorHashMap.put(partSetsTreeView.getRoot().getChildren().getLast().getChildren().get(i).getChildren().get(j).getChildren().get(k), new BcsColorSelector((BcsColorSelector) copyPartSetContainer[i][j][k]));
                                    }
                                }
                                case "Physics" -> {
                                    partSetsTreeView.getRoot().getChildren().getLast().getChildren().get(i).getChildren().add(j, new TreeItem<>("Physics"));

                                    for (int k = 0; k < copyPartSetContainer[i][j].length; k++) {
                                        partSetsTreeView.getRoot().getChildren().getLast().getChildren().get(i).getChildren().get(j).getChildren().add(new TreeItem<>("Entry " + k));

                                        bcsPhysicsHashMap.put(partSetsTreeView.getRoot().getChildren().getLast().getChildren().get(i).getChildren().get(j).getChildren().get(k), new BcsPhysics((BcsPhysics) copyPartSetContainer[i][j][k]));
                                    }
                                }
                                case "Unknown 3" -> {
                                    partSetsTreeView.getRoot().getChildren().getLast().getChildren().get(i).getChildren().add(j, new TreeItem<>("Unknown 3"));

                                    for (int k = 0; k < copyPartSetContainer[i][j].length; k++) {
                                        partSetsTreeView.getRoot().getChildren().getLast().getChildren().get(i).getChildren().get(j).getChildren().add(new TreeItem<>("Entry " + k));

                                        bcsUnknown3HashMap.put(partSetsTreeView.getRoot().getChildren().getLast().getChildren().get(i).getChildren().get(j).getChildren().get(k), new BcsUnknown3((BcsUnknown3) copyPartSetContainer[i][j][k]));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        else if (addPartSetItemCopy.getText().contains("List")) {
            if (addPartSetItemCopy.getText().contains("Color")) {
                boolean hasColorSelector = false;
                TreeItem<String> getGrandParent = currentPartSetEntry;
                
                while (!getGrandParent.getParent().getValue().toString().contains("Part Set")) {
                    getGrandParent = getGrandParent.getParent();
                }

                for (TreeItem<String> child : getGrandParent.getChildren()) {
                    if (child.getValue().equals("Color Selectors")) {
                        hasColorSelector = true;
                    }
                }
                if (hasColorSelector) {
                    TreeItem<String> getParent = getGrandParent.getChildren().get(0);

                    for (int i = 0; i < copyListContainer[0].length; i++) {
                        TreeItem<String> newChild = new TreeItem<>("Entry " + getParent.getChildren().size());

                        getParent.getChildren().add(newChild);

                        bcsColorsSelectorHashMap.put(newChild, new BcsColorSelector((BcsColorSelector) copyListContainer[0][i]));
                    }
                } 
                else {
                    getGrandParent.getChildren().add(0, new TreeItem<>("Color Selectors"));

                    for (int i = 0; i < copyListContainer[0].length; i++) {
                        TreeItem<String> newChild = new TreeItem<>("Entry " + i);

                        getGrandParent.getChildren().get(0).getChildren().add(newChild);

                        bcsColorsSelectorHashMap.put(newChild, new BcsColorSelector((BcsColorSelector) copyListContainer[0][i]));
                    }
                }
            }
            else if (addPartSetItemCopy.getText().contains("Physics")) {
                boolean hasPhysics = false;
                TreeItem<String> physicsIndex = new TreeItem<>();
                TreeItem<String> getGrandParent = currentPartSetEntry;
                
                while (!getGrandParent.getParent().getValue().toString().contains("Part Set")) {
                    getGrandParent = getGrandParent.getParent();
                }

                for (TreeItem<String> child : getGrandParent.getChildren()) {
                    if (child.getValue().equals("Physics")) {
                        hasPhysics = true;
                        physicsIndex = child;
                    }
                }

                if (hasPhysics) {
                    for (int i = 0; i < copyListContainer[0].length; i++) {
                        TreeItem<String> newChild = new TreeItem<>("Entry " + physicsIndex.getChildren().size());

                        physicsIndex.getChildren().add(newChild);

                        bcsPhysicsHashMap.put(newChild, new BcsPhysics((BcsPhysics) copyListContainer[0][i]));
                    }
                } 
                else {
                    getGrandParent.getChildren().add(0, new TreeItem<>("Physics"));

                    for (int i = 0; i < copyListContainer[0].length; i++) {
                        TreeItem<String> newChild = new TreeItem<>("Entry " + i);

                        getGrandParent.getChildren().get(0).getChildren().add(newChild);

                        bcsPhysicsHashMap.put(newChild, new BcsPhysics((BcsPhysics) copyListContainer[0][i]));
                    }

                    sortPartSetSubItems(getGrandParent);
                }
            }
            else if (addPartSetItemCopy.getText().contains("Unknown")) {
                boolean hasUnknown3 = false;
                TreeItem<String> unknown3Index = new TreeItem<>();
                TreeItem<String> getGrandParent = currentPartSetEntry;
                
                while (!getGrandParent.getParent().getValue().toString().contains("Part Set")) {
                    getGrandParent = getGrandParent.getParent();
                }

                for (TreeItem<String> child : getGrandParent.getChildren()) {
                    if (child.getValue().equals("Unknown 3")) {
                        hasUnknown3 = true;
                        unknown3Index = child;
                    }
                }
                if (hasUnknown3) {
                    for (int i = 0; i < copyListContainer[0].length; i++) {
                        TreeItem<String> newChild = new TreeItem<>("Entry " + unknown3Index.getChildren().size());

                        unknown3Index.getChildren().add(newChild);

                        bcsUnknown3HashMap.put(newChild, new BcsUnknown3((BcsUnknown3) copyListContainer[0][i]));
                    }
                } 
                else {
                    getGrandParent.getChildren().add(0, new TreeItem<>("Unknown 3"));

                    for (int i = 0; i < copyListContainer[0].length; i++) {
                        TreeItem<String> newChild = new TreeItem<>("Entry " + i);

                        getGrandParent.getChildren().get(0).getChildren().add(newChild);

                        bcsUnknown3HashMap.put(newChild, new BcsUnknown3((BcsUnknown3) copyListContainer[0][i]));
                    }

                    sortPartSetSubItems(getGrandParent);
                }
            }
        }
        else if(addPartSetItemCopy.getText().contains("Color") || addPartSetItemCopy.getText().contains("Physics") || addPartSetItemCopy.getText().contains("Unknown 3")) {
            if (addPartSetItemCopy.getText().contains("Color Selector")) {
                boolean hasColorSelector = false;
                TreeItem<String> getGrandParent = currentPartSetEntry;
                
                while (!getGrandParent.getParent().getValue().toString().contains("Part Set")) {
                    getGrandParent = getGrandParent.getParent();
                }

                for (TreeItem<String> child : getGrandParent.getChildren()) {
                    if (child.getValue().equals("Color Selectors")) {
                        hasColorSelector = true;
                    }
                }
                if (hasColorSelector) {
                    TreeItem<String> getParent = getGrandParent.getChildren().get(0);
                    TreeItem<String> newChild = new TreeItem<>("Entry " + getParent.getChildren().size());

                    getParent.getChildren().add(newChild);

                    bcsColorsSelectorHashMap.put(newChild, new BcsColorSelector((BcsColorSelector) copyContainer));

                    partSetsTreeView.getSelectionModel().select(newChild);
                }
                else {
                    getGrandParent.getChildren().add(0, new TreeItem<>("Color Selectors"));

                    TreeItem<String> newChild = new TreeItem<>("Entry " + 0);

                    getGrandParent.getChildren().get(0).getChildren().add(newChild);

                    bcsColorsSelectorHashMap.put(newChild, new BcsColorSelector((BcsColorSelector) copyContainer));

                    partSetsTreeView.getSelectionModel().select(newChild);
                }
            }
            else if (addPartSetItemCopy.getText().contains("Physics")) {
                boolean hasPhysics = false;
                TreeItem<String> physicsIndex = new TreeItem<>();
                TreeItem<String> getGrandParent = currentPartSetEntry;
                
                while (!getGrandParent.getParent().getValue().toString().contains("Part Set")) {
                    getGrandParent = getGrandParent.getParent();
                }

                for (TreeItem<String> child : getGrandParent.getChildren()) {
                    if (child.getValue().equals("Physics")) {
                        hasPhysics = true;
                        physicsIndex = child;
                    }
                }
                if (hasPhysics) {
                    TreeItem<String> newChild = new TreeItem<>("Entry " + physicsIndex.getChildren().size());

                    physicsIndex.getChildren().add(newChild);

                    bcsPhysicsHashMap.put(newChild, new BcsPhysics((BcsPhysics) copyContainer));

                    partSetsTreeView.getSelectionModel().select(newChild);
                } 
                else {
                    getGrandParent.getChildren().add(0, new TreeItem<>("Physics"));

                    TreeItem<String> newChild = new TreeItem<>("Entry " + 0);

                    getGrandParent.getChildren().get(0).getChildren().add(newChild);

                    bcsPhysicsHashMap.put(newChild, new BcsPhysics((BcsPhysics) copyContainer));

                    sortPartSetSubItems(getGrandParent);

                    partSetsTreeView.getSelectionModel().select(newChild);
                }
            }
            else {
                boolean hasUnknown3 = false;
                TreeItem<String> unknown3Index = new TreeItem<>();
                TreeItem<String> getGrandParent = currentPartSetEntry;
                
                while (!getGrandParent.getParent().getValue().toString().contains("Part Set")) {
                    getGrandParent = getGrandParent.getParent();
                }

                for (TreeItem<String> child : getGrandParent.getChildren()) {
                    if (child.getValue().equals("Unknown 3")) {
                        hasUnknown3 = true;
                        unknown3Index = child;
                    }
                }
                if (hasUnknown3) {
                    TreeItem<String> newChild = new TreeItem<>("Entry " + unknown3Index.getChildren().size());

                    unknown3Index.getChildren().add(newChild);

                    bcsUnknown3HashMap.put(newChild, new BcsUnknown3((BcsUnknown3) copyContainer));

                    partSetsTreeView.getSelectionModel().select(newChild);
                } 
                else {
                    getGrandParent.getChildren().add(0, new TreeItem<>("Unknown 3"));

                    TreeItem<String> newChild = new TreeItem<>("Entry " + 0);

                    getGrandParent.getChildren().get(0).getChildren().add(newChild);

                    bcsUnknown3HashMap.put(newChild, new BcsUnknown3((BcsUnknown3) copyContainer));

                    sortPartSetSubItems(getGrandParent);

                    partSetsTreeView.getSelectionModel().select(newChild);
                }
            }
        }
        else {
            if (addPartSetItemCopy.getText().contains("Face Base")) {
                partSetGrandParentEntry.getChildren().add(0, new TreeItem<>("Face Base"));

                bcsPartsHashMap.put(partSetGrandParentEntry.getChildren().get(0), new BcsPart((BcsPart) copyContainer));
                
                for (int i = 0; i < copySubTypesContainer.length; i++) {
                    switch (copySubTypesContainer[i]) {
                        case "Color Selectors" -> {
                            partSetGrandParentEntry.getChildren().get(0).getChildren().add(i, new TreeItem<>("Color Selectors"));

                            for (int j = 0; j < copyListContainer[i].length; j++) {
                                partSetGrandParentEntry.getChildren().get(0).getChildren().get(i).getChildren().add(j, new TreeItem<>("Entry " +j));

                                bcsColorsSelectorHashMap.put(partSetGrandParentEntry.getChildren().get(0).getChildren().get(i).getChildren().get(j), new BcsColorSelector((BcsColorSelector) copyListContainer[i][j]));
                            }
                        }
                        case "Physics" -> {
                            partSetGrandParentEntry.getChildren().get(0).getChildren().add(i, new TreeItem<>("Physics"));

                            for (int j = 0; j < copyListContainer[i].length; j++) {
                                partSetGrandParentEntry.getChildren().get(0).getChildren().get(i).getChildren().add(j, new TreeItem<>("Entry " +j));

                                bcsPhysicsHashMap.put(partSetGrandParentEntry.getChildren().get(0).getChildren().get(i).getChildren().get(j), new BcsPhysics((BcsPhysics) copyListContainer[i][j]));
                            }
                        }
                        case "Unknown 3" -> {
                            partSetGrandParentEntry.getChildren().get(0).getChildren().add(i, new TreeItem<>("Unknown 3"));

                            for (int j = 0; j < copyListContainer[i].length; j++) {
                                partSetGrandParentEntry.getChildren().get(0).getChildren().get(i).getChildren().add(j, new TreeItem<>("Entry " +j));

                                bcsUnknown3HashMap.put(partSetGrandParentEntry.getChildren().get(0).getChildren().get(i).getChildren().get(j), new BcsUnknown3((BcsUnknown3) copyListContainer[i][j]));
                            }
                        }
                    }
                }
            }
            else if (addPartSetItemCopy.getText().contains("Face Forehead")) {
                partSetGrandParentEntry.getChildren().add(0, new TreeItem<>("Face Forehead"));

                bcsPartsHashMap.put(partSetGrandParentEntry.getChildren().get(0), new BcsPart((BcsPart) copyContainer));
                
                for (int i = 0; i < copySubTypesContainer.length; i++) {
                    switch (copySubTypesContainer[i]) {
                        case "Color Selectors" -> {
                            partSetGrandParentEntry.getChildren().get(0).getChildren().add(i, new TreeItem<>("Color Selectors"));

                            for (int j = 0; j < copyListContainer[i].length; j++) {
                                partSetGrandParentEntry.getChildren().get(0).getChildren().get(i).getChildren().add(j, new TreeItem<>("Entry " +j));

                                bcsColorsSelectorHashMap.put(partSetGrandParentEntry.getChildren().get(0).getChildren().get(i).getChildren().get(j), new BcsColorSelector((BcsColorSelector) copyListContainer[i][j]));
                            }
                        }
                        case "Physics" -> {
                            partSetGrandParentEntry.getChildren().get(0).getChildren().add(i, new TreeItem<>("Physics"));

                            for (int j = 0; j < copyListContainer[i].length; j++) {
                                partSetGrandParentEntry.getChildren().get(0).getChildren().get(i).getChildren().add(j, new TreeItem<>("Entry " +j));

                                bcsPhysicsHashMap.put(partSetGrandParentEntry.getChildren().get(0).getChildren().get(i).getChildren().get(j), new BcsPhysics((BcsPhysics) copyListContainer[i][j]));
                            }
                        }
                        case "Unknown 3" -> {
                            partSetGrandParentEntry.getChildren().get(0).getChildren().add(i, new TreeItem<>("Unknown 3"));

                            for (int j = 0; j < copyListContainer[i].length; j++) {
                                partSetGrandParentEntry.getChildren().get(0).getChildren().get(i).getChildren().add(j, new TreeItem<>("Entry " +j));

                                bcsUnknown3HashMap.put(partSetGrandParentEntry.getChildren().get(0).getChildren().get(i).getChildren().get(j), new BcsUnknown3((BcsUnknown3) copyListContainer[i][j]));
                            }
                        }
                    }
                }

                sortPartSetItems(partSetGrandParentEntry);
            }
            else if (addPartSetItemCopy.getText().contains("Face Eye")) {
                 partSetGrandParentEntry.getChildren().add(0, new TreeItem<>("Face Eye"));

                bcsPartsHashMap.put(partSetGrandParentEntry.getChildren().get(0), new BcsPart((BcsPart) copyContainer));
                
                for (int i = 0; i < copySubTypesContainer.length; i++) {
                    switch (copySubTypesContainer[i]) {
                        case "Color Selectors" -> {
                            partSetGrandParentEntry.getChildren().get(0).getChildren().add(i, new TreeItem<>("Color Selectors"));

                            for (int j = 0; j < copyListContainer[i].length; j++) {
                                partSetGrandParentEntry.getChildren().get(0).getChildren().get(i).getChildren().add(j, new TreeItem<>("Entry " +j));

                                bcsColorsSelectorHashMap.put(partSetGrandParentEntry.getChildren().get(0).getChildren().get(i).getChildren().get(j), new BcsColorSelector((BcsColorSelector) copyListContainer[i][j]));
                            }
                        }
                        case "Physics" -> {
                            partSetGrandParentEntry.getChildren().get(0).getChildren().add(i, new TreeItem<>("Physics"));

                            for (int j = 0; j < copyListContainer[i].length; j++) {
                                partSetGrandParentEntry.getChildren().get(0).getChildren().get(i).getChildren().add(j, new TreeItem<>("Entry " +j));

                                bcsPhysicsHashMap.put(partSetGrandParentEntry.getChildren().get(0).getChildren().get(i).getChildren().get(j), new BcsPhysics((BcsPhysics) copyListContainer[i][j]));
                            }
                        }
                        case "Unknown 3" -> {
                            partSetGrandParentEntry.getChildren().get(0).getChildren().add(i, new TreeItem<>("Unknown 3"));

                            for (int j = 0; j < copyListContainer[i].length; j++) {
                                partSetGrandParentEntry.getChildren().get(0).getChildren().get(i).getChildren().add(j, new TreeItem<>("Entry " +j));

                                bcsUnknown3HashMap.put(partSetGrandParentEntry.getChildren().get(0).getChildren().get(i).getChildren().get(j), new BcsUnknown3((BcsUnknown3) copyListContainer[i][j]));
                            }
                        }
                    }
                }

                sortPartSetItems(partSetGrandParentEntry);
            }
            else if (addPartSetItemCopy.getText().contains("Face Nose")) {
                partSetGrandParentEntry.getChildren().add(0, new TreeItem<>("Face Nose"));

                bcsPartsHashMap.put(partSetGrandParentEntry.getChildren().get(0), new BcsPart((BcsPart) copyContainer));
                
                for (int i = 0; i < copySubTypesContainer.length; i++) {
                    switch (copySubTypesContainer[i]) {
                        case "Color Selectors" -> {
                            partSetGrandParentEntry.getChildren().get(0).getChildren().add(i, new TreeItem<>("Color Selectors"));

                            for (int j = 0; j < copyListContainer[i].length; j++) {
                                partSetGrandParentEntry.getChildren().get(0).getChildren().get(i).getChildren().add(j, new TreeItem<>("Entry " +j));

                                bcsColorsSelectorHashMap.put(partSetGrandParentEntry.getChildren().get(0).getChildren().get(i).getChildren().get(j), new BcsColorSelector((BcsColorSelector) copyListContainer[i][j]));
                            }
                        }
                        case "Physics" -> {
                            partSetGrandParentEntry.getChildren().get(0).getChildren().add(i, new TreeItem<>("Physics"));

                            for (int j = 0; j < copyListContainer[i].length; j++) {
                                partSetGrandParentEntry.getChildren().get(0).getChildren().get(i).getChildren().add(j, new TreeItem<>("Entry " +j));

                                bcsPhysicsHashMap.put(partSetGrandParentEntry.getChildren().get(0).getChildren().get(i).getChildren().get(j), new BcsPhysics((BcsPhysics) copyListContainer[i][j]));
                            }
                        }
                        case "Unknown 3" -> {
                            partSetGrandParentEntry.getChildren().get(0).getChildren().add(i, new TreeItem<>("Unknown 3"));

                            for (int j = 0; j < copyListContainer[i].length; j++) {
                                partSetGrandParentEntry.getChildren().get(0).getChildren().get(i).getChildren().add(j, new TreeItem<>("Entry " +j));

                                bcsUnknown3HashMap.put(partSetGrandParentEntry.getChildren().get(0).getChildren().get(i).getChildren().get(j), new BcsUnknown3((BcsUnknown3) copyListContainer[i][j]));
                            }
                        }
                    }
                }

                sortPartSetItems(partSetGrandParentEntry);
            }
            else if (addPartSetItemCopy.getText().contains("Face Ear")) {
                partSetGrandParentEntry.getChildren().add(0, new TreeItem<>("Face Ear"));

                bcsPartsHashMap.put(partSetGrandParentEntry.getChildren().get(0), new BcsPart((BcsPart) copyContainer));
                
                for (int i = 0; i < copySubTypesContainer.length; i++) {
                    switch (copySubTypesContainer[i]) {
                        case "Color Selectors" -> {
                            partSetGrandParentEntry.getChildren().get(0).getChildren().add(i, new TreeItem<>("Color Selectors"));

                            for (int j = 0; j < copyListContainer[i].length; j++) {
                                partSetGrandParentEntry.getChildren().get(0).getChildren().get(i).getChildren().add(j, new TreeItem<>("Entry " +j));

                                bcsColorsSelectorHashMap.put(partSetGrandParentEntry.getChildren().get(0).getChildren().get(i).getChildren().get(j), new BcsColorSelector((BcsColorSelector) copyListContainer[i][j]));
                            }
                        }
                        case "Physics" -> {
                            partSetGrandParentEntry.getChildren().get(0).getChildren().add(i, new TreeItem<>("Physics"));

                            for (int j = 0; j < copyListContainer[i].length; j++) {
                                partSetGrandParentEntry.getChildren().get(0).getChildren().get(i).getChildren().add(j, new TreeItem<>("Entry " +j));

                                bcsPhysicsHashMap.put(partSetGrandParentEntry.getChildren().get(0).getChildren().get(i).getChildren().get(j), new BcsPhysics((BcsPhysics) copyListContainer[i][j]));
                            }
                        }
                        case "Unknown 3" -> {
                            partSetGrandParentEntry.getChildren().get(0).getChildren().add(i, new TreeItem<>("Unknown 3"));

                            for (int j = 0; j < copyListContainer[i].length; j++) {
                                partSetGrandParentEntry.getChildren().get(0).getChildren().get(i).getChildren().add(j, new TreeItem<>("Entry " +j));

                                bcsUnknown3HashMap.put(partSetGrandParentEntry.getChildren().get(0).getChildren().get(i).getChildren().get(j), new BcsUnknown3((BcsUnknown3) copyListContainer[i][j]));
                            }
                        }
                    }
                }

                sortPartSetItems(partSetGrandParentEntry);
            }
            else if (addPartSetItemCopy.getText().contains("Hair")) {
                partSetGrandParentEntry.getChildren().add(0, new TreeItem<>("Hair"));

                bcsPartsHashMap.put(partSetGrandParentEntry.getChildren().get(0), new BcsPart((BcsPart) copyContainer));
                
                for (int i = 0; i < copySubTypesContainer.length; i++) {
                    switch (copySubTypesContainer[i]) {
                        case "Color Selectors" -> {
                            partSetGrandParentEntry.getChildren().get(0).getChildren().add(i, new TreeItem<>("Color Selectors"));

                            for (int j = 0; j < copyListContainer[i].length; j++) {
                                partSetGrandParentEntry.getChildren().get(0).getChildren().get(i).getChildren().add(j, new TreeItem<>("Entry " +j));

                                bcsColorsSelectorHashMap.put(partSetGrandParentEntry.getChildren().get(0).getChildren().get(i).getChildren().get(j), new BcsColorSelector((BcsColorSelector) copyListContainer[i][j]));
                            }
                        }
                        case "Physics" -> {
                            partSetGrandParentEntry.getChildren().get(0).getChildren().add(i, new TreeItem<>("Physics"));

                            for (int j = 0; j < copyListContainer[i].length; j++) {
                                partSetGrandParentEntry.getChildren().get(0).getChildren().get(i).getChildren().add(j, new TreeItem<>("Entry " +j));

                                bcsPhysicsHashMap.put(partSetGrandParentEntry.getChildren().get(0).getChildren().get(i).getChildren().get(j), new BcsPhysics((BcsPhysics) copyListContainer[i][j]));
                            }
                        }
                        case "Unknown 3" -> {
                            partSetGrandParentEntry.getChildren().get(0).getChildren().add(i, new TreeItem<>("Unknown 3"));

                            for (int j = 0; j < copyListContainer[i].length; j++) {
                                partSetGrandParentEntry.getChildren().get(0).getChildren().get(i).getChildren().add(j, new TreeItem<>("Entry " +j));

                                bcsUnknown3HashMap.put(partSetGrandParentEntry.getChildren().get(0).getChildren().get(i).getChildren().get(j), new BcsUnknown3((BcsUnknown3) copyListContainer[i][j]));
                            }
                        }
                    }
                }

                sortPartSetItems(partSetGrandParentEntry);
            }
            else if (addPartSetItemCopy.getText().contains("Bust")) {
                partSetGrandParentEntry.getChildren().add(0, new TreeItem<>("Bust"));

                bcsPartsHashMap.put(partSetGrandParentEntry.getChildren().get(0), new BcsPart((BcsPart) copyContainer));
                
                for (int i = 0; i < copySubTypesContainer.length; i++) {
                    switch (copySubTypesContainer[i]) {
                        case "Color Selectors" -> {
                            partSetGrandParentEntry.getChildren().get(0).getChildren().add(i, new TreeItem<>("Color Selectors"));

                            for (int j = 0; j < copyListContainer[i].length; j++) {
                                partSetGrandParentEntry.getChildren().get(0).getChildren().get(i).getChildren().add(j, new TreeItem<>("Entry " +j));

                                bcsColorsSelectorHashMap.put(partSetGrandParentEntry.getChildren().get(0).getChildren().get(i).getChildren().get(j), new BcsColorSelector((BcsColorSelector) copyListContainer[i][j]));
                            }
                        }
                        case "Physics" -> {
                            partSetGrandParentEntry.getChildren().get(0).getChildren().add(i, new TreeItem<>("Physics"));

                            for (int j = 0; j < copyListContainer[i].length; j++) {
                                partSetGrandParentEntry.getChildren().get(0).getChildren().get(i).getChildren().add(j, new TreeItem<>("Entry " +j));

                                bcsPhysicsHashMap.put(partSetGrandParentEntry.getChildren().get(0).getChildren().get(i).getChildren().get(j), new BcsPhysics((BcsPhysics) copyListContainer[i][j]));
                            }
                        }
                        case "Unknown 3" -> {
                            partSetGrandParentEntry.getChildren().get(0).getChildren().add(i, new TreeItem<>("Unknown 3"));

                            for (int j = 0; j < copyListContainer[i].length; j++) {
                                partSetGrandParentEntry.getChildren().get(0).getChildren().get(i).getChildren().add(j, new TreeItem<>("Entry " +j));

                                bcsUnknown3HashMap.put(partSetGrandParentEntry.getChildren().get(0).getChildren().get(i).getChildren().get(j), new BcsUnknown3((BcsUnknown3) copyListContainer[i][j]));
                            }
                        }
                    }
                }

                sortPartSetItems(partSetGrandParentEntry);
            }
            else if (addPartSetItemCopy.getText().contains("Pants")) {
                partSetGrandParentEntry.getChildren().add(0, new TreeItem<>("Pants"));

                bcsPartsHashMap.put(partSetGrandParentEntry.getChildren().get(0), new BcsPart((BcsPart) copyContainer));
                
                for (int i = 0; i < copySubTypesContainer.length; i++) {
                    switch (copySubTypesContainer[i]) {
                        case "Color Selectors" -> {
                            partSetGrandParentEntry.getChildren().get(0).getChildren().add(i, new TreeItem<>("Color Selectors"));

                            for (int j = 0; j < copyListContainer[i].length; j++) {
                                partSetGrandParentEntry.getChildren().get(0).getChildren().get(i).getChildren().add(j, new TreeItem<>("Entry " +j));

                                bcsColorsSelectorHashMap.put(partSetGrandParentEntry.getChildren().get(0).getChildren().get(i).getChildren().get(j), new BcsColorSelector((BcsColorSelector) copyListContainer[i][j]));
                            }
                        }
                        case "Physics" -> {
                            partSetGrandParentEntry.getChildren().get(0).getChildren().add(i, new TreeItem<>("Physics"));

                            for (int j = 0; j < copyListContainer[i].length; j++) {
                                partSetGrandParentEntry.getChildren().get(0).getChildren().get(i).getChildren().add(j, new TreeItem<>("Entry " +j));

                                bcsPhysicsHashMap.put(partSetGrandParentEntry.getChildren().get(0).getChildren().get(i).getChildren().get(j), new BcsPhysics((BcsPhysics) copyListContainer[i][j]));
                            }
                        }
                        case "Unknown 3" -> {
                            partSetGrandParentEntry.getChildren().get(0).getChildren().add(i, new TreeItem<>("Unknown 3"));

                            for (int j = 0; j < copyListContainer[i].length; j++) {
                                partSetGrandParentEntry.getChildren().get(0).getChildren().get(i).getChildren().add(j, new TreeItem<>("Entry " +j));

                                bcsUnknown3HashMap.put(partSetGrandParentEntry.getChildren().get(0).getChildren().get(i).getChildren().get(j), new BcsUnknown3((BcsUnknown3) copyListContainer[i][j]));
                            }
                        }
                    }
                }

                sortPartSetItems(partSetGrandParentEntry);
            }
            else if (addPartSetItemCopy.getText().contains("Rist")) {
                partSetGrandParentEntry.getChildren().add(0, new TreeItem<>("Rist"));

                bcsPartsHashMap.put(partSetGrandParentEntry.getChildren().get(0), new BcsPart((BcsPart) copyContainer));
                
                for (int i = 0; i < copySubTypesContainer.length; i++) {
                    switch (copySubTypesContainer[i]) {
                        case "Color Selectors" -> {
                            partSetGrandParentEntry.getChildren().get(0).getChildren().add(i, new TreeItem<>("Color Selectors"));

                            for (int j = 0; j < copyListContainer[i].length; j++) {
                                partSetGrandParentEntry.getChildren().get(0).getChildren().get(i).getChildren().add(j, new TreeItem<>("Entry " +j));

                                bcsColorsSelectorHashMap.put(partSetGrandParentEntry.getChildren().get(0).getChildren().get(i).getChildren().get(j), new BcsColorSelector((BcsColorSelector) copyListContainer[i][j]));
                            }
                        }
                        case "Physics" -> {
                            partSetGrandParentEntry.getChildren().get(0).getChildren().add(i, new TreeItem<>("Physics"));

                            for (int j = 0; j < copyListContainer[i].length; j++) {
                                partSetGrandParentEntry.getChildren().get(0).getChildren().get(i).getChildren().add(j, new TreeItem<>("Entry " +j));

                                bcsPhysicsHashMap.put(partSetGrandParentEntry.getChildren().get(0).getChildren().get(i).getChildren().get(j), new BcsPhysics((BcsPhysics) copyListContainer[i][j]));
                            }
                        }
                        case "Unknown 3" -> {
                            partSetGrandParentEntry.getChildren().get(0).getChildren().add(i, new TreeItem<>("Unknown 3"));

                            for (int j = 0; j < copyListContainer[i].length; j++) {
                                partSetGrandParentEntry.getChildren().get(0).getChildren().get(i).getChildren().add(j, new TreeItem<>("Entry " +j));

                                bcsUnknown3HashMap.put(partSetGrandParentEntry.getChildren().get(0).getChildren().get(i).getChildren().get(j), new BcsUnknown3((BcsUnknown3) copyListContainer[i][j]));
                            }
                        }
                    }
                }

                sortPartSetItems(partSetGrandParentEntry);
            }
            else {
                partSetGrandParentEntry.getChildren().add(0, new TreeItem<>("Boots"));

                bcsPartsHashMap.put(partSetGrandParentEntry.getChildren().get(0), new BcsPart((BcsPart) copyContainer));
                
                for (int i = 0; i < copySubTypesContainer.length; i++) {
                    switch (copySubTypesContainer[i]) {
                        case "Color Selectors" -> {
                            partSetGrandParentEntry.getChildren().get(0).getChildren().add(i, new TreeItem<>("Color Selectors"));

                            for (int j = 0; j < copyListContainer[i].length; j++) {
                                partSetGrandParentEntry.getChildren().get(0).getChildren().get(i).getChildren().add(j, new TreeItem<>("Entry " +j));

                                bcsColorsSelectorHashMap.put(partSetGrandParentEntry.getChildren().get(0).getChildren().get(i).getChildren().get(j), new BcsColorSelector((BcsColorSelector) copyListContainer[i][j]));
                            }
                        }
                        case "Physics" -> {
                            partSetGrandParentEntry.getChildren().get(0).getChildren().add(i, new TreeItem<>("Physics"));

                            for (int j = 0; j < copyListContainer[i].length; j++) {
                                partSetGrandParentEntry.getChildren().get(0).getChildren().get(i).getChildren().add(j, new TreeItem<>("Entry " +j));

                                bcsPhysicsHashMap.put(partSetGrandParentEntry.getChildren().get(0).getChildren().get(i).getChildren().get(j), new BcsPhysics((BcsPhysics) copyListContainer[i][j]));
                            }
                        }
                        case "Unknown 3" -> {
                            partSetGrandParentEntry.getChildren().get(0).getChildren().add(i, new TreeItem<>("Unknown 3"));

                            for (int j = 0; j < copyListContainer[i].length; j++) {
                                partSetGrandParentEntry.getChildren().get(0).getChildren().get(i).getChildren().add(j, new TreeItem<>("Entry " +j));

                                bcsUnknown3HashMap.put(partSetGrandParentEntry.getChildren().get(0).getChildren().get(i).getChildren().get(j), new BcsUnknown3((BcsUnknown3) copyListContainer[i][j]));
                            }
                        }
                    }
                }

                sortPartSetItems(partSetGrandParentEntry);
            }
        }
    }

    private void partColorsActionListener() {
        copiedPartColorItem.setVisible(false);
        copiedPartColorItem.setDisable(true);
        pastePartColorItem.setVisible(false);
        addPartColorItemCopy.setVisible(false);

        partColorContextMenu.getItems().addAll(
            addPartColor, addColor, 
            copyPartColorItem, deletePartColorItem, 
            noCopiedPartColorItemFound, copiedPartColorItem, 
            pastePartColorItem, addPartColorItemCopy
        );

        partColorsTreeView.setContextMenu(partColorContextMenu);
        partColorsTreeView.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue == null || newValue.getParent() == null) return;

            noCopiedPartColorItemFound.setDisable(true);
            pastePartColorItem.setDisable(true);

            currentPartColorEntry = newValue;
            partColorGrandParentEntry = newValue;
            
            try {
                while (partColorGrandParentEntry.getParent() != partColorsTreeView.getRoot()) {
                    partColorGrandParentEntry = partColorGrandParentEntry.getParent();
                }
            } catch (NullPointerException e) {
                return;
            }

            if (newValue.getValue().contains("Part Color")) {
                int index = dynamicTabPane.getSelectionModel().getSelectedIndex();

                dynamicTabPane.getTabs().clear();

                createPartColor(bcsPartColorsHashMap.get(newValue));

                dynamicTabPane.getSelectionModel().select(index);

                if (pastePartColorItem.getText().contains("Paste Part Color")) pastePartColorItem.setDisable(false);
            }
            else if (newValue.getValue().contains("Color")) {
                int index = dynamicTabPane.getSelectionModel().getSelectedIndex();

                dynamicTabPane.getTabs().clear();

                createColor(bcsColorsHashMap.get(newValue));

                dynamicTabPane.getSelectionModel().select(index);

                if (pastePartColorItem.getText().contains("Paste Color")) pastePartColorItem.setDisable(false);
            }
            else {
                dynamicTabPane.getTabs().clear();
            }
        });
        partColorsTreeView.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.SECONDARY) {
                partColorContextMenu.setOnAction(event -> {
                    if (event.getTarget() == addPartColor) AddPartColor();
                    else if (event.getTarget() == copyPartColorItem) CopyPartColorItem();
                    else if (event.getTarget() == deletePartColorItem) DeletePartColorItem();
                    else if (event.getTarget() == addColor) AddColor();
                    else if (event.getTarget() == pastePartColorItem) PastePartColorItem();
                    else if (event.getTarget() == addPartColorItemCopy) AddPartColorItemCopy();
                });
            }
        });
    }

    private void partColorsKeysListener() {
        partColorsTreeView.setOnKeyPressed(e -> {
            if (e.isControlDown() && e.getCode() == KeyCode.C) CopyPartColorItem();
            else if (e.isControlDown() && e.getCode() == KeyCode.V) PastePartColorItem();
            else if (e.getCode() == KeyCode.DELETE) DeletePartColorItem();
            else if (e.isControlDown() && e.getCode() == KeyCode.A) AddPartColorItemCopy();
        });
    }

    private void AddPartColor() {
        if (partColorsTreeView.getRoot() == null) {
            partColorsTreeView.setRoot(new TreeItem<>("dummy"));
            partColorsTreeView.setShowRoot(false);
        }
        else if (partColorsTreeView.getRoot().getChildren().isEmpty()) {
            allPartColorEntries = 0;
        } 

        TreeItem<String> partColor  = new TreeItem<>("Part Color " + allPartColorEntries);

        bcsPartColorsHashMap.put(partColor, new BcsPartColor());

        partColorsTreeView.getRoot().getChildren().add(partColor);

        partColorsObservableList.add(new String("null"));

        allPartColorEntries++;
    }

    private void AddColor() {
        if (partColorsTreeView.getSelectionModel().getSelectedIndex() < 0) return;

        TreeItem<String> parent = !currentPartColorEntry.getValue().contains("Part") ? currentPartColorEntry.getParent() : currentPartColorEntry;

        TreeItem<String> color = new TreeItem<>("Color " +  parent.getChildren().size());

        bcsColorsHashMap.put(color, new BcsColor());

        parent.getChildren().add(color);

        colorsObservableList.get(Integer.parseInt(partColorGrandParentEntry.getValue().toString().replaceAll("\\D+", ""))).add("Color " + (parent.getChildren().size() - 1));

        partColorsTreeView.getSelectionModel().select(color);
    }

    private void CopyPartColorItem() {
        noCopiedPartColorItemFound.setVisible(false);
        copiedPartColorItem.setVisible(true);
        pastePartColorItem.setVisible(true);
        addPartColorItemCopy.setVisible(true);

        pastePartColorItem.setDisable(false);
        if (partColorsTreeView.getSelectionModel().getSelectedIndex() < 0) return;

        if (currentPartColorEntry.getValue().contains("Part")) {
            copyContainer = new BcsPartColor(bcsPartColorsHashMap.get(currentPartColorEntry));
            copyListContainer = new Object[1][currentPartColorEntry.getChildren().size()];

            for (int i = 0; i < currentPartColorEntry.getChildren().size(); i++) {
                copyListContainer[0][i] = new BcsColor(bcsColorsHashMap.get(currentPartColorEntry.getChildren().get(i)));
            }

            setPartColorContextMenuText("Part Color");
        }
        else {
            copyContainer = new BcsColor(bcsColorsHashMap.get(currentPartColorEntry));

            setPartColorContextMenuText("Color");
        }
    }

    public void DeletePartColorItem() {
        if (currentPartColorEntry.getValue().contains("Part")) {
            BcsPartColor bcsPartColor = bcsPartColorsHashMap.get(currentPartColorEntry);
            partColorsObservableList.remove(bcsPartColor.name);

            for (TreeItem<String> color : currentPartColorEntry.getChildren()) {
                bcsColorsHashMap.remove(color);
                colorsObservableList.get(currentPartColorEntry.getParent().getChildren().indexOf(currentPartColorEntry)).remove(color.getValue());
            }

            if (partColorGrandParentEntry.nextSibling() != null) {
                for (int i = partColorsTreeView.getRoot().getChildren().indexOf(partColorGrandParentEntry.nextSibling()); i < partColorsTreeView.getRoot().getChildren().size(); i++) {

                    int entryIndex = Integer.parseInt(partColorsTreeView.getRoot().getChildren().get(i).getValue().toString().replaceAll("\\D+", ""));

                    partColorsTreeView.getRoot().getChildren().get(i).setValue("Part Color " + (entryIndex - 1));
                }
            }

            bcsPartColorsHashMap.remove(partColorGrandParentEntry);

            partColorsTreeView.getRoot().getChildren().remove(partColorGrandParentEntry);

            allPartColorEntries--;
        }
        else {
            bcsColorsHashMap.remove(currentPartColorEntry);
            colorsObservableList.remove(currentPartColorEntry.getParent().getChildren().indexOf(currentPartColorEntry));

            TreeItem<String> parent = currentPartColorEntry.getParent();

            parent.getChildren().remove(currentPartColorEntry);

            for (int i = 0; i < parent.getChildren().size(); i++) {
                parent.getChildren().get(i).setValue("Color " + i);
            }
        }
    }

    private void PastePartColorItem() {
        if (currentPartColorEntry.getValue().contains("Part")) {
            bcsPartColorsHashMap.put(currentPartColorEntry, new BcsPartColor((BcsPartColor) copyContainer));

            for (TreeItem<String> color : currentPartColorEntry.getChildren()) {
                bcsColorsHashMap.remove(color);
            }

            currentPartColorEntry.getChildren().clear();

            for (int i = 0; i < copyListContainer[0].length; i++) {
                currentPartColorEntry.getChildren().add(i, new TreeItem<>("Color " + i));

                bcsColorsHashMap.put(currentPartColorEntry.getChildren().get(i), new BcsColor((BcsColor) copyListContainer[0][i]));
            }

            dynamicTabPane.getTabs().clear();
            createPartColor(bcsPartColorsHashMap.get(currentPartColorEntry));
            dynamicTabPane.getSelectionModel().select(dynamicTabPane.getSelectionModel().getSelectedIndex());
        }
        else {
            bcsColorsHashMap.put(currentPartColorEntry, new BcsColor((BcsColor) copyContainer));

            dynamicTabPane.getTabs().clear();
            createColor(bcsColorsHashMap.get(currentPartColorEntry));
            dynamicTabPane.getSelectionModel().select(dynamicTabPane.getSelectionModel().getSelectedIndex());
        }
    }

    private void AddPartColorItemCopy() {
        if (partColorsTreeView.getRoot().getChildren().isEmpty()) {
            allPartColorEntries = 0;
        } 

        if (addPartColorItemCopy.getText().contains("Part")) {
            TreeItem<String> partColor = new TreeItem<>("Part Color " + allPartColorEntries);

            partColorsTreeView.getRoot().getChildren().add(partColor);

            BcsPartColor bcsPartColor = new BcsPartColor((BcsPartColor) copyContainer);

            bcsPartColorsHashMap.put(partColor, bcsPartColor);

            partColorsObservableList.add(bcsPartColor.name);

            for (int i = 0; i < copyListContainer[0].length; i++) {
                partColor.getChildren().add(i, new TreeItem<>("Color " + i));

                bcsColorsHashMap.put(partColor.getChildren().get(i), new BcsColor((BcsColor) copyListContainer[0][i]));
            }

            allPartColorEntries++;

        }
        else {
            TreeItem<String> color = new TreeItem<>("Color " + partColorGrandParentEntry.getChildren().size());

            partColorGrandParentEntry.getChildren().add(color);

            bcsColorsHashMap.put(color, new BcsColor((BcsColor) copyContainer));

            colorsObservableList.get(Integer.parseInt(partColorGrandParentEntry.getValue().toString().replaceAll("\\D+", ""))).add("Color " + (partColorGrandParentEntry.getChildren().size() - 1));

            partColorsTreeView.getSelectionModel().select(color);
        }
    }

    private void bodiesActionListener() {
        copiedBodyItem.setVisible(false);
        copiedBodyItem.setDisable(true);
        pasteBodyItem.setVisible(false);
        addBodyItemCopy.setVisible(false);
        
        bodyContextMenu.getItems().addAll(
            addBody, addBoneScale, 
            copyBodyItem, deleteBodyItem, 
            noCopiedBodyItemFound, copiedBodyItem, 
            pasteBodyItem, addBodyItemCopy
        );

        bodiesTreeView.setContextMenu(bodyContextMenu);
        bodiesTreeView.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue == null || newValue.getParent() == null) return;

            noCopiedBodyItemFound.setDisable(true);
            pasteBodyItem.setDisable(true);

            currentBodyEntry = newValue;
            bodyGrandParentEntry = newValue;
            
            try {
                while (bodyGrandParentEntry.getParent() != bodiesTreeView.getRoot()) {
                    bodyGrandParentEntry = bodyGrandParentEntry.getParent();
                }
            } catch (NullPointerException e) {
                return;
            }

            if (newValue.getValue().contains("Bone Scale")) {
                int index = dynamicTabPane.getSelectionModel().getSelectedIndex();

                dynamicTabPane.getTabs().clear();

                createBoneScale(bcsBoneScalesHashMap.get(newValue));

                dynamicTabPane.getSelectionModel().select(index);

                if (pasteBodyItem.getText().contains("Paste Bone Scale")) pasteBodyItem.setDisable(false);
            }
            else {
                dynamicTabPane.getTabs().clear();

                if (pasteBodyItem.getText().contains("Paste Body")) pasteBodyItem.setDisable(false);
            }
        });
        bodiesTreeView.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.SECONDARY) {
                bodyContextMenu.setOnAction(event -> {
                    if (event.getTarget() == addBody) AddBody();
                    else if (event.getTarget() == copyBodyItem) CopyBodyItem();
                    else if (event.getTarget() == deleteBodyItem) DeleteBodyItem();
                    else if (event.getTarget() == addBoneScale) AddBoneScale();
                    else if (event.getTarget() == pasteBodyItem) PasteBodyItem();
                    else if (event.getTarget() == addBodyItemCopy) AddBodyItemCopy();
                });
            }
        });
    }

    private void bodiesKeysListener() {
        bodiesTreeView.setOnKeyPressed(e -> {
            if (e.isControlDown() && e.getCode() == KeyCode.C) CopyBodyItem();
            else if (e.isControlDown() && e.getCode() == KeyCode.V) PasteBodyItem();
            else if (e.getCode() == KeyCode.DELETE) DeleteBodyItem();
            else if (e.isControlDown() && e.getCode() == KeyCode.A) AddBodyItemCopy();
        });
    }

    private void AddBody() {
        if (bodiesTreeView.getRoot() == null) {
            bodiesTreeView.setRoot(new TreeItem<>("dummy"));
            bodiesTreeView.setShowRoot(false);
        }
        else if (bodiesTreeView.getRoot().getChildren().isEmpty()) {
            allBodyEntries = 0;
        } 

        TreeItem<String> body = new TreeItem<>("Body " + allBodyEntries);

        bodiesTreeView.getRoot().getChildren().add(body); 

        allBodyEntries++;
    }

    private void AddBoneScale() {
        if (bodiesTreeView.getSelectionModel().getSelectedIndex() < 0) return;

        TreeItem<String> parent = currentBodyEntry.getValue().contains("Bone Scale") ? currentBodyEntry.getParent() : currentBodyEntry;

        TreeItem<String> boneScale = new TreeItem<>("Bone Scale " +  parent.getChildren().size());

        bcsBoneScalesHashMap.put(boneScale, new BcsBoneScale());

        parent.getChildren().add(boneScale);

        bodiesTreeView.getSelectionModel().select(boneScale);
    }

    private void CopyBodyItem() {
        noCopiedBodyItemFound.setVisible(false);
        copiedBodyItem.setVisible(true);
        pasteBodyItem.setVisible(true);
        addBodyItemCopy.setVisible(true);

        pasteBodyItem.setDisable(false);
        if (bodiesTreeView.getSelectionModel().getSelectedIndex() < 0) return;

        if (currentBodyEntry.getValue().contains("Body")) {
            copyListContainer = new Object[1][currentBodyEntry.getChildren().size()];

            for (int i = 0; i < currentBodyEntry.getChildren().size(); i++) {
                copyListContainer[0][i] = new BcsBoneScale(bcsBoneScalesHashMap.get(currentBodyEntry.getChildren().get(i)));
            }

            setBodyContextMenuText("Body");
        }
        else {
            copyContainer = new BcsBoneScale(bcsBoneScalesHashMap.get(currentBodyEntry));

            setBodyContextMenuText("Bone Scale");
        }
    }

    private void DeleteBodyItem() {
        if (currentBodyEntry.getValue().contains("Body")) {

            for (TreeItem<String> boneScale : currentBodyEntry.getChildren()) {
                bcsBoneScalesHashMap.remove(boneScale);
            }

            if (bodyGrandParentEntry.nextSibling() != null) {
                for (int i = bodiesTreeView.getRoot().getChildren().indexOf(bodyGrandParentEntry.nextSibling()); i < bodiesTreeView.getRoot().getChildren().size(); i++) {

                    int entryIndex = Integer.parseInt(bodiesTreeView.getRoot().getChildren().get(i).getValue().toString().replaceAll("\\D+", ""));

                    bodiesTreeView.getRoot().getChildren().get(i).setValue("Body " + (entryIndex - 1));
                }
            }

            bodiesTreeView.getRoot().getChildren().remove(bodyGrandParentEntry);

            allBodyEntries--;
        }
        else {
            bcsBoneScalesHashMap.remove(currentBodyEntry);

            TreeItem<String> parent = currentBodyEntry.getParent();

            parent.getChildren().remove(currentBodyEntry);

            for (int i = 0; i < parent.getChildren().size(); i++) {
                parent.getChildren().get(i).setValue("Bone Scale " + i);
            }
        }
    }

    private void PasteBodyItem() {
        if (currentBodyEntry.getValue().contains("Body")) {

            for (TreeItem<String> boneScale : currentBodyEntry.getChildren()) {
                bcsBoneScalesHashMap.remove(boneScale);
            }

            currentBodyEntry.getChildren().clear();

            for (int i = 0; i < copyListContainer[0].length; i++) {
                currentBodyEntry.getChildren().add(i, new TreeItem<>("Bone Scale " + i));

                bcsBoneScalesHashMap.put(currentBodyEntry.getChildren().get(i), new BcsBoneScale((BcsBoneScale) copyListContainer[0][i]));
            }

            dynamicTabPane.getTabs().clear();
            
            dynamicTabPane.getSelectionModel().select(dynamicTabPane.getSelectionModel().getSelectedIndex());
        }
        else {
            bcsBoneScalesHashMap.put(currentBodyEntry, new BcsBoneScale((BcsBoneScale) copyContainer));

            dynamicTabPane.getTabs().clear();
            createBoneScale(bcsBoneScalesHashMap.get(currentBodyEntry));
            dynamicTabPane.getSelectionModel().select(dynamicTabPane.getSelectionModel().getSelectedIndex());
        }
    }

    private void AddBodyItemCopy() {
        if (bodiesTreeView.getRoot().getChildren().isEmpty()) {
            allBodyEntries = 0;
        } 

        if (addBodyItemCopy.getText().contains("Body")) {
            TreeItem<String> body = new TreeItem<>("Body " + allBodyEntries);

            bodiesTreeView.getRoot().getChildren().add(body);

            for (int i = 0; i < copyListContainer[0].length; i++) {
                body.getChildren().add(i, new TreeItem<>("Bone Scale " + i));

                bcsBoneScalesHashMap.put(body.getChildren().get(i), new BcsBoneScale((BcsBoneScale) copyListContainer[0][i]));
            }

            allBodyEntries++;
        }
        else if (addBodyItemCopy.getText().contains("Bone")) {
            TreeItem<String> boneScale = new TreeItem<>("Bone Scale " + bodyGrandParentEntry.getChildren().size());

            bodyGrandParentEntry.getChildren().add(boneScale);

            bcsBoneScalesHashMap.put(boneScale, new BcsBoneScale((BcsBoneScale) copyContainer));

            bodiesTreeView.getSelectionModel().select(boneScale);
        }
    }

    private void skeletonsActionListener() {
        copiedSkeletonItem.setVisible(false);
        copiedSkeletonItem.setDisable(true);
        pasteSkeletonItem.setVisible(false);
        addSkeletonItemCopy.setVisible(false);
        
        skeletonContextMenu.getItems().addAll(
            addSkeleton, addBone, 
            copySkeletonItem, deleteSkeletonItem, 
            noCopiedSkeletonItemFound, copiedSkeletonItem, 
            pasteSkeletonItem, addSkeletonItemCopy
        );

        skeletonsTreeView.setContextMenu(skeletonContextMenu);
        skeletonsTreeView.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue == null || newValue.getParent() == null) return;

            noCopiedSkeletonItemFound.setDisable(true);
            pasteSkeletonItem.setDisable(true);

            currentSkeletonEntry = newValue;
            skeletonGrandParentEntry = newValue;
            
            try {
                while (skeletonGrandParentEntry.getParent() != skeletonsTreeView.getRoot()) {
                    skeletonGrandParentEntry = skeletonGrandParentEntry.getParent();
                }
            } catch (NullPointerException e) {
                return;
            }

            if (newValue.getValue().contains("Skeleton")) {
                int index = dynamicTabPane.getSelectionModel().getSelectedIndex();

                dynamicTabPane.getTabs().clear();

                createSkeleton(bcsSkeletonsHashMap.get(newValue));

                dynamicTabPane.getSelectionModel().select(index);

                if (pasteSkeletonItem.getText().contains("Paste Skeleton")) pasteSkeletonItem.setDisable(false);
            }
            else if (newValue.getValue().contains("Bone")) {
                int index = dynamicTabPane.getSelectionModel().getSelectedIndex();

                dynamicTabPane.getTabs().clear();

                createBone(bcsBonesHashMap.get(newValue));

                dynamicTabPane.getSelectionModel().select(index);

                if (pasteSkeletonItem.getText().contains("Paste Bone")) pasteSkeletonItem.setDisable(false);
            }
            else {
                dynamicTabPane.getTabs().clear();
            }
        });
        skeletonsTreeView.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.SECONDARY) {
                skeletonContextMenu.setOnAction(event -> {
                    if (event.getTarget() == addSkeleton) AddSkeleton();
                    else if (event.getTarget() == copySkeletonItem) CopySkeletonItem();
                    else if (event.getTarget() == deleteSkeletonItem) DeleteSkeletonItem();
                    else if (event.getTarget() == addBone) AddBone();
                    else if (event.getTarget() == pasteSkeletonItem) PasteSkeletonItem();
                    else if (event.getTarget() == addSkeletonItemCopy) AddSkeletonItemCopy();
                });
            }
        });
    }

    private void skeletonsKeysListener() {
        skeletonsTreeView.setOnKeyPressed(e -> {
            if (e.isControlDown() && e.getCode() == KeyCode.C) CopySkeletonItem();
            else if (e.isControlDown() && e.getCode() == KeyCode.V) PasteSkeletonItem();
            else if (e.getCode() == KeyCode.DELETE) DeleteSkeletonItem();
            else if (e.isControlDown() && e.getCode() == KeyCode.A) AddSkeletonItemCopy();
        });
    }

    private void AddSkeleton() {
        if (skeletonsTreeView.getRoot() == null) {
            skeletonsTreeView.setRoot(new TreeItem<>("dummy"));
            skeletonsTreeView.setShowRoot(false);
        }

        if (skeletonsTreeView.getRoot().getChildren().size() < 2) {
            TreeItem<String> skeleton  = new TreeItem<>("Skeleton " + (skeletonsTreeView.getRoot().getChildren().size() + 1));

            bcsSkeletonsHashMap.put(skeleton, new BcsSkeleton());

            skeletonsTreeView.getRoot().getChildren().add(skeleton);

            if (skeletonsTreeView.getRoot().getChildren().size() == 2){
                addSkeleton.setDisable(true);
                if (addSkeletonItemCopy.getText().contains("Skeleton")){
                    addSkeletonItemCopy.setDisable(true);
                }
            } 
        } 
    }

    private void AddBone() {
        if (skeletonsTreeView.getSelectionModel().getSelectedIndex() < 0) return;

        TreeItem<String> parent = currentSkeletonEntry.getValue().contains("Bone") ? currentSkeletonEntry.getParent() : currentSkeletonEntry;

        TreeItem<String> bone = new TreeItem<>("Bone " +  parent.getChildren().size());

        bcsBonesHashMap.put(bone, new BcsBone());

        parent.getChildren().add(bone);

        skeletonsTreeView.getSelectionModel().select(bone);
    }

    private void CopySkeletonItem() {
        noCopiedSkeletonItemFound.setVisible(false);
        copiedSkeletonItem.setVisible(true);
        pasteSkeletonItem.setVisible(true);
        addSkeletonItemCopy.setVisible(true);

        pasteSkeletonItem.setDisable(false);
        if (skeletonsTreeView.getSelectionModel().getSelectedIndex() < 0) return;

        if (currentSkeletonEntry.getValue().contains("Skeleton")) {
            if (skeletonsTreeView.getRoot().getChildren().size() == 2){
                addSkeletonItemCopy.setDisable(true);
            } 

            copyContainer = new BcsSkeleton(bcsSkeletonsHashMap.get(currentSkeletonEntry));
            copyListContainer = new Object[1][currentSkeletonEntry.getChildren().size()];

            for (int i = 0; i < currentSkeletonEntry.getChildren().size(); i++) {
                copyListContainer[0][i] = new BcsBone(bcsBonesHashMap.get(currentSkeletonEntry.getChildren().get(i)));
            }

            setSkeletonContextMenuText("Skeleton");
        }
        else {
            addSkeletonItemCopy.setDisable(false);

            copyContainer = new BcsBone(bcsBonesHashMap.get(currentSkeletonEntry));

            setSkeletonContextMenuText("Bone");
        }
    }

    private void DeleteSkeletonItem() {
        if (currentSkeletonEntry.getValue().contains("Skeleton")) {
            bcsSkeletonsHashMap.remove(currentSkeletonEntry);

            for (TreeItem<String> bone : currentSkeletonEntry.getChildren()) {
                bcsBonesHashMap.remove(bone);
            }

            skeletonsTreeView.getRoot().getChildren().remove(currentSkeletonEntry);

            for (int i = 0; i < skeletonsTreeView.getRoot().getChildren().size(); i++) {
                skeletonsTreeView.getRoot().getChildren().get(i).setValue("Skeleton " + (i + 1));
            }

            addSkeleton.setDisable(false);
            addSkeletonItemCopy.setDisable(false);
        }
        else {
            bcsBonesHashMap.remove(currentSkeletonEntry);

            TreeItem<String> parent = new TreeItem<>();
            parent = currentSkeletonEntry.getParent();

            parent.getChildren().remove(currentSkeletonEntry);

            for (int i = 0; i < parent.getChildren().size(); i++) {
                parent.getChildren().get(i).setValue("Bone " + i);
            }
        }
    }

    private void PasteSkeletonItem() {
        if (currentSkeletonEntry.getValue().contains("Skeleton")) {
            bcsSkeletonsHashMap.put(currentSkeletonEntry, new BcsSkeleton((BcsSkeleton) copyContainer));

            for (TreeItem<String> bone : currentSkeletonEntry.getChildren()) {
                bcsBonesHashMap.remove(bone);
            }

            currentSkeletonEntry.getChildren().clear();

            for (int i = 0; i < copyListContainer[0].length; i++) {
                currentSkeletonEntry.getChildren().add(i, new TreeItem<>("Bone " + i));

                bcsBonesHashMap.put(currentSkeletonEntry.getChildren().get(i), new BcsBone((BcsBone) copyListContainer[0][i]));
            }

            dynamicTabPane.getTabs().clear();
            createSkeleton(bcsSkeletonsHashMap.get(currentSkeletonEntry));
            dynamicTabPane.getSelectionModel().select(dynamicTabPane.getSelectionModel().getSelectedIndex());
        }
        else {
            bcsBonesHashMap.put(currentSkeletonEntry, new BcsBone((BcsBone) copyContainer));

            dynamicTabPane.getTabs().clear();
            createBone(bcsBonesHashMap.get(currentSkeletonEntry));
            dynamicTabPane.getSelectionModel().select(dynamicTabPane.getSelectionModel().getSelectedIndex());
        }
    }

    private void AddSkeletonItemCopy() {
        if (addSkeletonItemCopy.getText().contains("Skeleton")) {
            TreeItem<String> skeleton = new TreeItem<>("Skeleton " + (skeletonsTreeView.getRoot().getChildren().size() + 1));

            skeletonsTreeView.getRoot().getChildren().add(skeleton);

            bcsSkeletonsHashMap.put(skeleton, new BcsSkeleton((BcsSkeleton) copyContainer));

            for (int i = 0; i < copyListContainer[0].length; i++) {
                skeleton.getChildren().add(i, new TreeItem<>("Bone " + i));

                bcsBonesHashMap.put(skeleton.getChildren().get(i), new BcsBone((BcsBone) copyListContainer[0][i]));
            }
        }
        else {
            TreeItem<String> bone = new TreeItem<>("Bone " + skeletonGrandParentEntry.getChildren().size());

            skeletonGrandParentEntry.getChildren().add(bone);

            bcsBonesHashMap.put(bone, new BcsBone((BcsBone) copyContainer));

            skeletonsTreeView.getSelectionModel().select(bone);
        }

        if (skeletonsTreeView.getRoot().getChildren().size() == 2) {
            addSkeleton.setDisable(true);
            if (addSkeletonItemCopy.getText().contains("Skeleton")) {
                addSkeletonItemCopy.setDisable(true);
            }
        }
    }

    private void sortPartSetItems(TreeItem<String> treeItem) {
        List<String> partTypesList = Arrays.asList(
            "Face Base",
            "Face Forehead",
            "Face Eye",
            "Face Nose",
            "Face Ear",
            "Hair",
            "Bust",
            "Pants",
            "Rist",
            "Boots"
        );

        treeItem.getChildren().sort((item1, item2) -> {
            int index1 = partTypesList.indexOf(item1.getValue());
            int index2 = partTypesList.indexOf(item2.getValue());
            
            return Integer.compare(index1, index2);
        });
    }

    private void sortPartSetSubItems(TreeItem<String> treeItem) {
        List<String> partTypesList = Arrays.asList(
            "Color Selectors",
            "Physics",
            "Unknown 3"
        );

        treeItem.getChildren().sort((item1, item2) -> {
            int index1 = partTypesList.indexOf(item1.getValue());
            int index2 = partTypesList.indexOf(item2.getValue());
            
            return Integer.compare(index1, index2);
        });
    }

    private void setPartSetContextMenuText(String text) {
        copiedPartSetItem.setText("Copied " + text);
        pastePartSetItem.setText("Paste " + text + " Ctrl+V");
        addPartSetItemCopy.setText("Add " + text + " Copy Ctrl+A");
    }

    private void setPartColorContextMenuText(String text) {
        copiedPartColorItem.setText("Copied " + text);
        pastePartColorItem.setText("Paste " + text + " Ctrl+V");
        addPartColorItemCopy.setText("Add " + text + " Copy Ctrl+A");
    }

    private void setBodyContextMenuText(String text) {
        copiedBodyItem.setText("Copied " + text);
        pasteBodyItem.setText("Paste " + text + " Ctrl+V");
        addBodyItemCopy.setText("Add " + text + " Copy Ctrl+A");
    }

    private void setSkeletonContextMenuText(String text) {
        copiedSkeletonItem.setText("Copied " + text);
        pasteSkeletonItem.setText("Paste " + text + " Ctrl+V");
        addSkeletonItemCopy.setText("Add " + text + " Copy Ctrl+A");
    }

    public void bcsReader(Path path) {
        try(FileChannel channel = FileChannel.open(path, StandardOpenOption.READ)) {
            int partSetCount;
            int partColorsCount;
            int bodyCount;

            int partSetOffset = 0;
            int partColorsOffset = 0;
            int bodyOffset = 0;
            int skeleton2Offset = 0;
            int skeleton1Offset = 0;

            ByteBuffer byteBuffer = ByteBuffer.allocate(1).order(ByteOrder.LITTLE_ENDIAN);
            ByteBuffer shortBuffer = ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN);
            ByteBuffer intBuffer = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
            ByteBuffer dynamicStringBuffer;

            channel.position(6);
            shortBuffer.clear();
            channel.read(shortBuffer);
            shortBuffer.flip();
            version = shortBuffer.getShort();

            channel.position(12);
            shortBuffer.clear();
            channel.read(shortBuffer);
            shortBuffer.flip();
            partSetCount = toUShort(shortBuffer.getShort());
            allPartSetEntries = partSetCount;

            channel.position(14);
            shortBuffer.clear();
            channel.read(shortBuffer);
            shortBuffer.flip();
            partColorsCount = toUShort(shortBuffer.getShort());
            allPartColorEntries = partColorsCount;

            channel.position(16);
            shortBuffer.clear();
            channel.read(shortBuffer);
            shortBuffer.flip();
            bodyCount = toUShort(shortBuffer.getShort());
            allBodyEntries = bodyCount;

            switch (version) {
                case 72 -> {
                    channel.position(20);
                    intBuffer.clear();
                    channel.read(intBuffer);
                    intBuffer.flip();
                    partSetOffset = intBuffer.getInt();

                    channel.position(24);
                    intBuffer.clear();
                    channel.read(intBuffer);
                    intBuffer.flip();
                    partColorsOffset = intBuffer.getInt();

                    channel.position(28);
                    intBuffer.clear();
                    channel.read(intBuffer);
                    intBuffer.flip();
                    bodyOffset = intBuffer.getInt();

                    skeleton1Offset = 64;

                    skeleton2Offset = 0;

                    channel.position(32);
                    byteBuffer.clear();
                    channel.read(byteBuffer);
                    byteBuffer.flip();
                    bcsPartSet.race = toUByte(byteBuffer.get());

                    channel.position(33);
                    byteBuffer.clear();
                    channel.read(byteBuffer);
                    byteBuffer.flip();
                    bcsPartSet.gender = toUByte(byteBuffer.get());

                    channel.position(34);
                    byteBuffer.clear();
                    channel.read(byteBuffer);
                    byteBuffer.flip();
                    bcsPartSet.i46 = toUByte(byteBuffer.get());

                    channel.position(35);
                    byteBuffer.clear();
                    channel.read(byteBuffer);
                    byteBuffer.flip();
                    bcsPartSet.i47 = toUByte(byteBuffer.get());

                    channel.position(36);
                    intBuffer.clear();
                    channel.read(intBuffer);
                    intBuffer.flip();
                    bcsPartSet.positionY = intBuffer.getFloat();

                    channel.position(40);
                    intBuffer.clear();
                    channel.read(intBuffer);
                    intBuffer.flip();
                    bcsPartSet.cameraY = intBuffer.getFloat();

                    channel.position(44);
                    intBuffer.clear();
                    channel.read(intBuffer);
                    intBuffer.flip();
                    bcsPartSet.trackingOffset = intBuffer.getFloat();

                    channel.position(48);
                    intBuffer.clear();
                    channel.read(intBuffer);
                    intBuffer.flip();
                    bcsPartSet.f60 = intBuffer.getFloat();

                    channel.position(52);
                    intBuffer.clear();
                    channel.read(intBuffer);
                    intBuffer.flip();
                    bcsPartSet.collisionScale = intBuffer.getFloat();

                    channel.position(56);
                    intBuffer.clear();
                    channel.read(intBuffer);
                    intBuffer.flip();
                    bcsPartSet.f68 = intBuffer.getFloat();

                    channel.position(60);
                    intBuffer.clear();
                    channel.read(intBuffer);
                    intBuffer.flip();
                    bcsPartSet.f72 = intBuffer.getFloat();
                }
                case 76, 0 -> {
                    channel.position(24);
                    intBuffer.clear();
                    channel.read(intBuffer);
                    intBuffer.flip();
                    partSetOffset = intBuffer.getInt();

                    channel.position(28);
                    intBuffer.clear();
                    channel.read(intBuffer);
                    intBuffer.flip();
                    partColorsOffset = intBuffer.getInt();

                    channel.position(32);
                    intBuffer.clear();
                    channel.read(intBuffer);
                    intBuffer.flip();
                    bodyOffset = intBuffer.getInt();

                    channel.position(36);
                    intBuffer.clear();
                    channel.read(intBuffer);
                    intBuffer.flip();
                    skeleton2Offset = intBuffer.getInt();

                    channel.position(40);
                    intBuffer.clear();
                    channel.read(intBuffer);
                    intBuffer.flip();
                    skeleton1Offset = intBuffer.getInt();

                    channel.position(44);
                    byteBuffer.clear();
                    channel.read(byteBuffer);
                    byteBuffer.flip();
                    bcsPartSet.race = toUByte(byteBuffer.get());

                    channel.position(45);
                    byteBuffer.clear();
                    channel.read(byteBuffer);
                    byteBuffer.flip();
                    bcsPartSet.gender = toUByte(byteBuffer.get());

                    channel.position(46);
                    byteBuffer.clear();
                    channel.read(byteBuffer);
                    byteBuffer.flip();
                    bcsPartSet.i46 = toUByte(byteBuffer.get());

                    channel.position(47);
                    byteBuffer.clear();
                    channel.read(byteBuffer);
                    byteBuffer.flip();
                    bcsPartSet.i47 = toUByte(byteBuffer.get());

                    channel.position(48);
                    intBuffer.clear();
                    channel.read(intBuffer);
                    intBuffer.flip();
                    bcsPartSet.positionY = intBuffer.getFloat();

                    channel.position(52);
                    intBuffer.clear();
                    channel.read(intBuffer);
                    intBuffer.flip();
                    bcsPartSet.cameraY = intBuffer.getFloat();

                    channel.position(56);
                    intBuffer.clear();
                    channel.read(intBuffer);
                    intBuffer.flip();
                    bcsPartSet.trackingOffset = intBuffer.getFloat();

                    channel.position(60);
                    intBuffer.clear();
                    channel.read(intBuffer);
                    intBuffer.flip();
                    bcsPartSet.f60 = intBuffer.getFloat();

                    channel.position(64);
                    intBuffer.clear();
                    channel.read(intBuffer);
                    intBuffer.flip();
                    bcsPartSet.collisionScale = intBuffer.getFloat();

                    channel.position(68);
                    intBuffer.clear();
                    channel.read(intBuffer);
                    intBuffer.flip();
                    bcsPartSet.f68 = intBuffer.getFloat();

                    channel.position(72);
                    intBuffer.clear();
                    channel.read(intBuffer);
                    intBuffer.flip();
                    bcsPartSet.f72 = intBuffer.getFloat();
                }
                default -> {
                    Popups.LegacyFormat();
                }
            }
            if (partSetCount > 0) {
                partSetsTreeView.setRoot(new TreeItem<>("dummy"));
                partSetsTreeView.setShowRoot(false);

                int mainIndex = 0;

                for (int i = 0; i < partSetCount; i++) {
                    int partIndex = 0;
                    int subPartIndex = 0;

                    channel.position(partSetOffset + i * 4);
                    intBuffer.clear();
                    channel.read(intBuffer);
                    intBuffer.flip();
                    int thisPartSetOffset = intBuffer.getInt();

                    if (thisPartSetOffset != 0) {
                        partSetsTreeView.getRoot().getChildren().add(new TreeItem<>("Part Set " + i));

                        channel.position(thisPartSetOffset + 24);
                        intBuffer.clear();
                        channel.read(intBuffer);
                        intBuffer.flip();
                        int tableOffset = thisPartSetOffset + intBuffer.getInt();

                        channel.position(tableOffset);
                        intBuffer.clear();
                        channel.read(intBuffer);
                        intBuffer.flip();
                        int offset = intBuffer.getInt();

                        if (offset != 0) {
                            partsReader(version, offset, thisPartSetOffset, mainIndex, partIndex, subPartIndex, channel, "Face Base");
                            partIndex++;
                        }

                        channel.position(tableOffset + 4);
                        intBuffer.clear();
                        channel.read(intBuffer);
                        intBuffer.flip();
                        offset = intBuffer.getInt();

                        if(offset != 0) {
                            partsReader(version, offset, thisPartSetOffset, mainIndex, partIndex, subPartIndex, channel, "Face Forehead");
                            partIndex++;
                        }

                        channel.position(tableOffset + 8);
                        intBuffer.clear();
                        channel.read(intBuffer);
                        intBuffer.flip();
                        offset = intBuffer.getInt();

                        if(offset != 0) {
                            partsReader(version, offset, thisPartSetOffset, mainIndex, partIndex, subPartIndex, channel, "Face Eye");
                            partIndex++;
                        }

                        channel.position(tableOffset + 12);
                        intBuffer.clear();
                        channel.read(intBuffer);
                        intBuffer.flip();
                        offset = intBuffer.getInt();

                        if(offset != 0) {
                            partsReader(version, offset, thisPartSetOffset, mainIndex, partIndex, subPartIndex, channel, "Face Nose");
                            partIndex++;
                        }

                        channel.position(tableOffset + 16);
                        intBuffer.clear();
                        channel.read(intBuffer);
                        intBuffer.flip();
                        offset = intBuffer.getInt();

                        if(offset != 0) {
                            partsReader(version, offset, thisPartSetOffset, mainIndex, partIndex, subPartIndex, channel, "Face Ear");
                            partIndex++;
                        }

                        channel.position(tableOffset + 20);
                        intBuffer.clear();
                        channel.read(intBuffer);
                        intBuffer.flip();
                        offset = intBuffer.getInt();

                        if(offset != 0) {
                            partsReader(version, offset, thisPartSetOffset, mainIndex, partIndex, subPartIndex, channel, "Hair");
                            partIndex++;
                        }

                        channel.position(tableOffset + 24);
                        intBuffer.clear();
                        channel.read(intBuffer);
                        intBuffer.flip();
                        offset = intBuffer.getInt();

                        if(offset != 0) {
                            partsReader(version, offset, thisPartSetOffset, mainIndex, partIndex, subPartIndex, channel, "Bust");
                            partIndex++;
                        }

                        channel.position(tableOffset + 28);
                        intBuffer.clear();
                        channel.read(intBuffer);
                        intBuffer.flip();
                        offset = intBuffer.getInt();

                        if(offset != 0) {
                            partsReader(version, offset, thisPartSetOffset, mainIndex, partIndex, subPartIndex, channel, "Pants");
                            partIndex++;
                        }

                        channel.position(tableOffset + 32);
                        intBuffer.clear();
                        channel.read(intBuffer);
                        intBuffer.flip();
                        offset = intBuffer.getInt();

                        if(offset != 0) {
                            partsReader(version, offset, thisPartSetOffset, mainIndex, partIndex, subPartIndex, channel, "Rist");
                            partIndex++;
                        }

                        channel.position(tableOffset + 36);
                        intBuffer.clear();
                        channel.read(intBuffer);
                        intBuffer.flip();
                        offset = intBuffer.getInt();

                        if(offset != 0) {
                            partsReader(version, offset, thisPartSetOffset, mainIndex, partIndex, subPartIndex, channel, "Boots");
                            partIndex++;
                        }

                        mainIndex++;
                    }
                }
            }
            if (partColorsCount > 0) {
                partColorsTreeView.setRoot(new TreeItem<>("dummy"));
                partColorsTreeView.setShowRoot(false);

                int mainIndex = 0;

                for (int i = 0; i < partColorsCount; i++) {
                    channel.position(partColorsOffset + i * 4);
                    intBuffer.clear();
                    channel.read(intBuffer);
                    intBuffer.flip();
                    int thisPartColorOffset = intBuffer.getInt();

                    if (thisPartColorOffset != 0) {
                        partColorsTreeView.getRoot().getChildren().add(new TreeItem<>("Part Color " + i));

                        BcsPartColor bcsPartColor = new BcsPartColor();
            
                        bcsPartColorsHashMap.put(partColorsTreeView.getRoot().getChildren().get(mainIndex), bcsPartColor);

                        channel.position(thisPartColorOffset);
                        intBuffer.clear();
                        channel.read(intBuffer);
                        intBuffer.flip();
                        int partColorNameOffset = intBuffer.getInt();

                        if (partColorNameOffset != 0) {
                            int counter = 0;

                            do {
                                channel.position(thisPartColorOffset + partColorNameOffset + counter);
                                byteBuffer.clear();
                                channel.read(byteBuffer);
                                byteBuffer.flip();
                                counter++;
                            } while (byteBuffer.get() != 0);

                            dynamicStringBuffer = ByteBuffer.allocate(counter);

                            channel.position(thisPartColorOffset + partColorNameOffset);
                            dynamicStringBuffer.clear();
                            channel.read(dynamicStringBuffer);
                            dynamicStringBuffer.flip();
                            bcsPartColor.name = new String(dynamicStringBuffer.array(), StandardCharsets.ISO_8859_1);

                            partColorsObservableList.add(bcsPartColor.name);
                        }

                        channel.position(thisPartColorOffset + 10);
                        shortBuffer.clear();
                        channel.read(shortBuffer);
                        shortBuffer.flip();
                        short colorCount = shortBuffer.getShort();

                        if (colorCount > 0) {
                            channel.position(thisPartColorOffset + 12);
                            intBuffer.clear();
                            channel.read(intBuffer);
                            intBuffer.flip();
                            int colorOffset = intBuffer.getInt();

                            for (int j = 0; j < colorCount; j++) {
                                BcsColor bcsColor = new BcsColor();

                                partColorsTreeView.getRoot().getChildren().get(mainIndex).getChildren().add(new TreeItem<>("Color " + j));

                                colorsObservableList.add(FXCollections.observableArrayList());
                                colorsObservableList.get(i).add("Color " + j);

                                bcsColorsHashMap.put(partColorsTreeView.getRoot().getChildren().get(mainIndex).getChildren().get(j), bcsColor);
                                
                                channel.position(thisPartColorOffset + colorOffset + j * 80);
                                intBuffer.clear();
                                channel.read(intBuffer);
                                intBuffer.flip();
                                float red1 = intBuffer.getFloat();

                                channel.position(thisPartColorOffset + colorOffset + 4 + j * 80);
                                intBuffer.clear();
                                channel.read(intBuffer);
                                intBuffer.flip();
                                float green1 = intBuffer.getFloat();

                                channel.position(thisPartColorOffset + colorOffset + 8 + j * 80);
                                intBuffer.clear();
                                channel.read(intBuffer);
                                intBuffer.flip();
                                float blue1 = intBuffer.getFloat();

                                channel.position(thisPartColorOffset + colorOffset + 12 + j * 80);
                                intBuffer.clear();
                                channel.read(intBuffer);
                                intBuffer.flip();
                                float alpha1 = intBuffer.getFloat();

                                channel.position(thisPartColorOffset + colorOffset + 16 + j * 80);
                                intBuffer.clear();
                                channel.read(intBuffer);
                                intBuffer.flip();
                                float red2 = intBuffer.getFloat();

                                channel.position(thisPartColorOffset + colorOffset + 20 + j * 80);
                                intBuffer.clear();
                                channel.read(intBuffer);
                                intBuffer.flip();
                                float green2 = intBuffer.getFloat();

                                channel.position(thisPartColorOffset + colorOffset + 24 + j * 80);
                                intBuffer.clear();
                                channel.read(intBuffer);
                                intBuffer.flip();
                                float blue2 = intBuffer.getFloat();

                                channel.position(thisPartColorOffset + colorOffset + 28 + j * 80);
                                intBuffer.clear();
                                channel.read(intBuffer);
                                intBuffer.flip();
                                float alpha2 = intBuffer.getFloat();

                                channel.position(thisPartColorOffset + colorOffset + 32 + j * 80);
                                intBuffer.clear();
                                channel.read(intBuffer);
                                intBuffer.flip();
                                float red3 = intBuffer.getFloat();

                                channel.position(thisPartColorOffset + colorOffset + 36 + j * 80);
                                intBuffer.clear();
                                channel.read(intBuffer);
                                intBuffer.flip();
                                float green3 = intBuffer.getFloat();

                                channel.position(thisPartColorOffset + colorOffset + 40 + j * 80);
                                intBuffer.clear();
                                channel.read(intBuffer);
                                intBuffer.flip();
                                float blue3 = intBuffer.getFloat();

                                channel.position(thisPartColorOffset + colorOffset + 44 + j * 80);
                                intBuffer.clear();
                                channel.read(intBuffer);
                                intBuffer.flip();
                                float alpha3 = intBuffer.getFloat();

                                channel.position(thisPartColorOffset + colorOffset + 48 + j * 80);
                                intBuffer.clear();
                                channel.read(intBuffer);
                                intBuffer.flip();
                                float red4 = intBuffer.getFloat();

                                channel.position(thisPartColorOffset + colorOffset + 52 + j * 80);
                                intBuffer.clear();
                                channel.read(intBuffer);
                                intBuffer.flip();
                                float green4 = intBuffer.getFloat();

                                channel.position(thisPartColorOffset + colorOffset + 56 + j * 80);
                                intBuffer.clear();
                                channel.read(intBuffer);
                                intBuffer.flip();
                                float blue4 = intBuffer.getFloat();

                                channel.position(thisPartColorOffset + colorOffset + 60 + j * 80);
                                intBuffer.clear();
                                channel.read(intBuffer);
                                intBuffer.flip();
                                float alpha4 = intBuffer.getFloat();

                                bcsColor.color1 = new Color(red1, green1, blue1, alpha1);
                                bcsColor.color2 = new Color(red2, green2, blue2, alpha2);
                                bcsColor.color3 = new Color(red3, green3, blue3, alpha3);
                                bcsColor.color4 = new Color(red4, green4, blue4, alpha4);
                            }
                        }

                        mainIndex++;
                    }
                    else {
                        partColorsObservableList.add("null");
                    }
                }
            }
            if (bodyCount > 0) {
                bodiesTreeView.setRoot(new TreeItem<>("dummy"));
                bodiesTreeView.setShowRoot(false);

                int mainIndex = 0;

                for (int i = 0; i < bodyCount; i++) {
                    channel.position(bodyOffset + i * 4);
                    intBuffer.clear();
                    channel.read(intBuffer);
                    intBuffer.flip();
                    int thisBodyScaleOffset = intBuffer.getInt();

                    if (thisBodyScaleOffset != 0) {
                        bodiesTreeView.getRoot().getChildren().add(new TreeItem<>("Body " + i));

                        channel.position(thisBodyScaleOffset + 2);
                        shortBuffer.clear();
                        channel.read(shortBuffer);
                        shortBuffer.flip();
                        short boneScaleCount = shortBuffer.getShort();

                        if (boneScaleCount > 0) {
                            channel.position(thisBodyScaleOffset + 4);
                            intBuffer.clear();
                            channel.read(intBuffer);
                            intBuffer.flip();
                            int boneScaleOffset = intBuffer.getInt();

                            for (int j = 0; j < boneScaleCount; j++) {
                                BcsBoneScale bcsBoneScale = new BcsBoneScale();

                                bodiesTreeView.getRoot().getChildren().get(mainIndex).getChildren().add(new TreeItem<>("Bone Scale " + j));

                                bcsBoneScalesHashMap.put(bodiesTreeView.getRoot().getChildren().get(mainIndex).getChildren().get(j), bcsBoneScale);

                                channel.position(thisBodyScaleOffset + boneScaleOffset + j * 16);
                                intBuffer.clear();
                                channel.read(intBuffer);
                                intBuffer.flip();
                                bcsBoneScale.scaleX = intBuffer.getFloat();

                                channel.position(thisBodyScaleOffset + boneScaleOffset + 4 + j * 16);
                                intBuffer.clear();
                                channel.read(intBuffer);
                                intBuffer.flip();
                                bcsBoneScale.scaleY = intBuffer.getFloat();

                                channel.position(thisBodyScaleOffset + boneScaleOffset + 8 + j * 16);
                                intBuffer.clear();
                                channel.read(intBuffer);
                                intBuffer.flip();
                                bcsBoneScale.scaleZ = intBuffer.getFloat();

                                channel.position(thisBodyScaleOffset + boneScaleOffset + 12 + j * 16);
                                intBuffer.clear();
                                channel.read(intBuffer);
                                intBuffer.flip();
                                int boneNameOffset = intBuffer.getInt();

                                if (boneNameOffset != 0) {
                                    int counter = 0;

                                    do {
                                        channel.position(thisBodyScaleOffset + boneScaleOffset + boneNameOffset + counter + j * 16);
                                        byteBuffer.clear();
                                        channel.read(byteBuffer);
                                        byteBuffer.flip();
                                        counter++;
                                    } while (byteBuffer.get() != 0);

                                    dynamicStringBuffer = ByteBuffer.allocate(counter);

                                    channel.position(thisBodyScaleOffset + boneScaleOffset + boneNameOffset + j * 16);
                                    dynamicStringBuffer.clear();
                                    channel.read(dynamicStringBuffer);
                                    dynamicStringBuffer.flip();
                                    bcsBoneScale.boneName = new String(dynamicStringBuffer.array(), StandardCharsets.ISO_8859_1);
                                }
                            }
                        }

                        mainIndex++;
                    }
                }
            }
            if (skeleton1Offset != 0) {
                skeletonsTreeView.setRoot(new TreeItem<>("dummy"));
                skeletonsTreeView.setShowRoot(false);

                int thisSkeleton1Offset;

                if (version == 72) {
                    thisSkeleton1Offset = skeleton1Offset;
                }
                else {
                    channel.position(skeleton1Offset);
                    intBuffer.clear();
                    channel.read(intBuffer);
                    intBuffer.flip();
                    thisSkeleton1Offset = intBuffer.getInt();
                }

                skeletonsReader(version, thisSkeleton1Offset, channel, "Skeleton 1");
            }
            else if (skeleton2Offset != 0 && skeleton1Offset == 0){
                skeletonsTreeView.setRoot(new TreeItem<>("dummy"));
                skeletonsTreeView.setShowRoot(false);

                skeletonsTreeView.getRoot().getChildren().add(new TreeItem<>("Skeleton 1"));

                BcsSkeleton bcsSkeleton = new BcsSkeleton();
                bcsSkeleton.i00 = 0;

                bcsSkeletonsHashMap.put(skeletonsTreeView.getRoot().getChildren().get(0), bcsSkeleton);
            }
            if (skeleton2Offset != 0) {
                addSkeleton.setDisable(true);

                channel.position(skeleton2Offset);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                int thisSkeleton2Offset = intBuffer.getInt();

                skeletonsReader(version, thisSkeleton2Offset, channel, "Skeleton 2");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void partsReader(int version, int relativeOffset, int mainOffset, int mainIndex, int partIndex, int subPartIndex, FileChannel channel, String part) {
        try {
            ByteBuffer byteBuffer = ByteBuffer.allocate(1).order(ByteOrder.LITTLE_ENDIAN);
            ByteBuffer shortBuffer = ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN);
            ByteBuffer intBuffer = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
            ByteBuffer dynamicStringBuffer;

            mainOffset += relativeOffset;

            partSetsTreeView.getRoot().getChildren().get(mainIndex).getChildren().add(new TreeItem<>(part));

            BcsPart bcsPart = new BcsPart();
            
            bcsPartsHashMap.put(partSetsTreeView.getRoot().getChildren().get(mainIndex).getChildren().get(partIndex), bcsPart);

            channel.position(mainOffset);
            shortBuffer.clear();
            channel.read(shortBuffer);
            shortBuffer.flip();
            bcsPart.model = shortBuffer.getShort();

            channel.position(mainOffset + 2);
            shortBuffer.clear();
            channel.read(shortBuffer);
            shortBuffer.flip();
            bcsPart.model2 = shortBuffer.getShort();

            channel.position(mainOffset + 4);
            shortBuffer.clear();
            channel.read(shortBuffer);
            shortBuffer.flip();
            bcsPart.texture = shortBuffer.getShort();

            channel.position(mainOffset + 16);
            shortBuffer.clear();
            channel.read(shortBuffer);
            shortBuffer.flip();
            bcsPart.shader = shortBuffer.getShort();

            channel.position(mainOffset + 18);
            shortBuffer.clear();
            channel.read(shortBuffer);
            shortBuffer.flip();
            short colorSelectorCount = shortBuffer.getShort();

            if (colorSelectorCount > 0) {
                partSetsTreeView.getRoot().getChildren().get(mainIndex).getChildren().get(partIndex).getChildren().add(new TreeItem<>("Color Selectors"));

                channel.position(mainOffset + 20);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                int colorSelectorOffset = intBuffer.getInt();

                for (int j = 0; j < colorSelectorCount; j++) {
                    BcsColorSelector bcsColorSelector = new BcsColorSelector();

                    partSetsTreeView.getRoot().getChildren().get(mainIndex).getChildren().get(partIndex).getChildren().get(subPartIndex).getChildren().add(new TreeItem<>("Entry " + j));

                    bcsColorsSelectorHashMap.put(partSetsTreeView.getRoot().getChildren().get(mainIndex).getChildren().get(partIndex).getChildren().get(subPartIndex).getChildren().get(j), bcsColorSelector);

                    channel.position(mainOffset + colorSelectorOffset + j * 4);
                    shortBuffer.clear();
                    channel.read(shortBuffer);
                    shortBuffer.flip();
                    bcsColorSelector.partColorGroup = toUShort(shortBuffer.getShort());

                    channel.position(mainOffset + colorSelectorOffset + j * 4 + 2);
                    shortBuffer.clear();
                    channel.read(shortBuffer);
                    shortBuffer.flip();
                    bcsColorSelector.colorIndex = toUShort(shortBuffer.getShort());
                }

                subPartIndex++;
            }

            channel.position(mainOffset + 24);
            intBuffer.clear();
            channel.read(intBuffer);
            intBuffer.flip();
            bcsPart.flags = toUint32(intBuffer.getInt());

            channel.position(mainOffset + 28);
            intBuffer.clear();
            channel.read(intBuffer);
            intBuffer.flip();
            bcsPart.hideFlags = intBuffer.getInt();

            channel.position(mainOffset + 32);
            intBuffer.clear();
            channel.read(intBuffer);
            intBuffer.flip();
            bcsPart.hideMatFlags = intBuffer.getInt();

            channel.position(mainOffset + 36);
            intBuffer.clear();
            channel.read(intBuffer);
            intBuffer.flip();
            bcsPart.f36 = intBuffer.getFloat();

            channel.position(mainOffset + 40);
            intBuffer.clear();
            channel.read(intBuffer);
            intBuffer.flip();
            bcsPart.f40 = intBuffer.getFloat();

            channel.position(mainOffset + 44);
            intBuffer.clear();
            channel.read(intBuffer);
            intBuffer.flip();
            bcsPart.i44 = intBuffer.getInt();

            channel.position(mainOffset + 48);
            intBuffer.clear();
            channel.read(intBuffer);
            intBuffer.flip();
            bcsPart.i48 = intBuffer.getInt();

            channel.position(mainOffset + 52);
            intBuffer.clear();
            channel.read(intBuffer);
            intBuffer.flip();
            bcsPart.charaCode = StandardCharsets.ISO_8859_1.decode(intBuffer).toString().trim();

            channel.position(mainOffset + 56);
            intBuffer.clear();
            channel.read(intBuffer);
            intBuffer.flip();
            int emdNameOffset = intBuffer.getInt();

            if (emdNameOffset != 0) {
                int counter = 0;

                do {
                    channel.position(mainOffset + emdNameOffset + counter);
                    byteBuffer.clear();
                    channel.read(byteBuffer);
                    byteBuffer.flip();
                    counter++;
                } while (byteBuffer.get() != 0);

                dynamicStringBuffer = ByteBuffer.allocate(counter);

                channel.position(mainOffset + emdNameOffset);
                dynamicStringBuffer.clear();
                channel.read(dynamicStringBuffer);
                dynamicStringBuffer.flip();
                bcsPart.emdName = new String(dynamicStringBuffer.array(), StandardCharsets.ISO_8859_1);
            }

            channel.position(mainOffset + 60);
            intBuffer.clear();
            channel.read(intBuffer);
            intBuffer.flip();
            int emmNameOffset = intBuffer.getInt();

            if (emmNameOffset != 0) {
                int counter = 0;

                do {
                    channel.position(mainOffset + emmNameOffset + counter);
                    byteBuffer.clear();
                    channel.read(byteBuffer);
                    byteBuffer.flip();
                    counter++;
                } while (byteBuffer.get() != 0);

                dynamicStringBuffer = ByteBuffer.allocate(counter);

                channel.position(mainOffset + emmNameOffset);
                dynamicStringBuffer.clear();
                channel.read(dynamicStringBuffer);
                dynamicStringBuffer.flip();
                bcsPart.emmName = new String(dynamicStringBuffer.array(), StandardCharsets.ISO_8859_1);
            }

            channel.position(mainOffset + 64);
            intBuffer.clear();
            channel.read(intBuffer);
            intBuffer.flip();
            int embNameOffset = intBuffer.getInt();

            if (embNameOffset != 0) {
                int counter = 0;

                do {
                    channel.position(mainOffset + embNameOffset + counter);
                    byteBuffer.clear();
                    channel.read(byteBuffer);
                    byteBuffer.flip();
                    counter++;
                } while (byteBuffer.get() != 0);

                dynamicStringBuffer = ByteBuffer.allocate(counter);

                channel.position(mainOffset + embNameOffset);
                dynamicStringBuffer.clear();
                channel.read(dynamicStringBuffer);
                dynamicStringBuffer.flip();
                bcsPart.embName = new String(dynamicStringBuffer.array(), StandardCharsets.ISO_8859_1);
            }

            channel.position(mainOffset + 68);
            intBuffer.clear();
            channel.read(intBuffer);
            intBuffer.flip();
            int eanNameOffset = intBuffer.getInt();

            if (eanNameOffset != 0) {
                int counter = 0;

                do {
                    channel.position(mainOffset + eanNameOffset + counter);
                    byteBuffer.clear();
                    channel.read(byteBuffer);
                    byteBuffer.flip();
                    counter++;
                } while (byteBuffer.get() != 0);

                dynamicStringBuffer = ByteBuffer.allocate(counter);

                channel.position(mainOffset + eanNameOffset);
                dynamicStringBuffer.clear();
                channel.read(dynamicStringBuffer);
                dynamicStringBuffer.flip();
                bcsPart.eanName = new String(dynamicStringBuffer.array(), StandardCharsets.ISO_8859_1);
            }

            channel.position(mainOffset + 74);
            shortBuffer.clear();
            channel.read(shortBuffer);
            shortBuffer.flip();
            short physicsPartsCount = shortBuffer.getShort();

            if (physicsPartsCount > 0) {
                partSetsTreeView.getRoot().getChildren().get(mainIndex).getChildren().get(partIndex).getChildren().add(new TreeItem<>("Physics"));

                channel.position(mainOffset + 76);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                int physicsOffset = intBuffer.getInt();

                for (int j = 0; j < physicsPartsCount; j++) {
                    BcsPhysics bcsPhysics = new BcsPhysics();

                    partSetsTreeView.getRoot().getChildren().get(mainIndex).getChildren().get(partIndex).getChildren().get(subPartIndex).getChildren().add(new TreeItem<>("Entry " + j));

                    bcsPhysicsHashMap.put(partSetsTreeView.getRoot().getChildren().get(mainIndex).getChildren().get(partIndex).getChildren().get(subPartIndex).getChildren().get(j), bcsPhysics);

                    channel.position(mainOffset + physicsOffset + j * 72);
                    shortBuffer.clear();
                    channel.read(shortBuffer);
                    shortBuffer.flip();
                    bcsPhysics.model = shortBuffer.getShort();

                    channel.position(mainOffset + physicsOffset + j * 72 + 2);
                    shortBuffer.clear();
                    channel.read(shortBuffer);
                    shortBuffer.flip();
                    bcsPhysics.model2 = shortBuffer.getShort();

                    channel.position(mainOffset + physicsOffset + j * 72 + 4);
                    shortBuffer.clear();
                    channel.read(shortBuffer);
                    shortBuffer.flip();
                    bcsPhysics.texture = shortBuffer.getShort();

                    channel.position(mainOffset + physicsOffset + j * 72 + 24);
                    intBuffer.clear();
                    channel.read(intBuffer);
                    intBuffer.flip();
                    bcsPhysics.flags = toUint32(intBuffer.getInt());

                    channel.position(mainOffset + physicsOffset + j * 72 + 28);
                    intBuffer.clear();
                    channel.read(intBuffer);
                    intBuffer.flip();
                    bcsPhysics.hideFlags = intBuffer.getInt();

                    channel.position(mainOffset + physicsOffset + j * 72 + 32);
                    intBuffer.clear();
                    channel.read(intBuffer);
                    intBuffer.flip();
                    bcsPhysics.hideMatFlags = intBuffer.getInt();

                    channel.position(mainOffset + physicsOffset + j * 72 + 36);
                    intBuffer.clear();
                    channel.read(intBuffer);
                    intBuffer.flip();
                    bcsPhysics.charaCode = StandardCharsets.ISO_8859_1.decode(intBuffer).toString().trim();

                    channel.position(mainOffset + physicsOffset + j * 72 + 40);
                    intBuffer.clear();
                    channel.read(intBuffer);
                    intBuffer.flip();
                    int emdNameOffsetPhysics = intBuffer.getInt();

                    if (emdNameOffsetPhysics != 0) {
                        int counter = 0;

                        do {
                            channel.position(mainOffset + physicsOffset + emdNameOffsetPhysics + counter + j * 72);
                            byteBuffer.clear();
                            channel.read(byteBuffer);
                            byteBuffer.flip();
                            counter++;
                        } while (byteBuffer.get() != 0);

                        dynamicStringBuffer = ByteBuffer.allocate(counter);

                        channel.position(mainOffset + physicsOffset + emdNameOffsetPhysics + j * 72);
                        dynamicStringBuffer.clear();
                        channel.read(dynamicStringBuffer);
                        dynamicStringBuffer.flip();
                        bcsPhysics.emdName = new String(dynamicStringBuffer.array(), StandardCharsets.ISO_8859_1);
                    }

                    channel.position(mainOffset + physicsOffset + j * 72 + 44);
                    intBuffer.clear();
                    channel.read(intBuffer);
                    intBuffer.flip();
                    int emmNameOffsetPhysics = intBuffer.getInt();

                    if (emmNameOffsetPhysics != 0) {
                        int counter = 0;

                        do {
                            channel.position(mainOffset + physicsOffset + emmNameOffsetPhysics + counter + j * 72);
                            byteBuffer.clear();
                            channel.read(byteBuffer);
                            byteBuffer.flip();
                            counter++;
                        } while (byteBuffer.get() != 0);

                        dynamicStringBuffer = ByteBuffer.allocate(counter);

                        channel.position(mainOffset + physicsOffset + emmNameOffsetPhysics + j * 72);
                        dynamicStringBuffer.clear();
                        channel.read(dynamicStringBuffer);
                        dynamicStringBuffer.flip();
                        bcsPhysics.emmName = new String(dynamicStringBuffer.array(), StandardCharsets.ISO_8859_1);
                    }

                    channel.position(mainOffset + physicsOffset + 48 + j * 72);
                    intBuffer.clear();
                    channel.read(intBuffer);
                    intBuffer.flip();
                    int embNameOffsetPhysics = intBuffer.getInt();

                    if (embNameOffsetPhysics != 0) {
                        int counter = 0;

                        do {
                            channel.position(mainOffset + physicsOffset + embNameOffsetPhysics + counter + j * 72);
                            byteBuffer.clear();
                            channel.read(byteBuffer);
                            byteBuffer.flip();
                            counter++;
                        } while (byteBuffer.get() != 0);

                        dynamicStringBuffer = ByteBuffer.allocate(counter);

                        channel.position(mainOffset + physicsOffset + embNameOffsetPhysics + j * 72);
                        dynamicStringBuffer.clear();
                        channel.read(dynamicStringBuffer);
                        dynamicStringBuffer.flip();
                        bcsPhysics.embName = new String(dynamicStringBuffer.array(), StandardCharsets.ISO_8859_1);
                    }

                    channel.position(mainOffset + physicsOffset + j * 72 + 52);
                    intBuffer.clear();
                    channel.read(intBuffer);
                    intBuffer.flip();
                    int eskNameOffsetPhysics = intBuffer.getInt();

                    if (eskNameOffsetPhysics != 0) {
                        int counter = 0;

                        do {
                            channel.position(mainOffset + physicsOffset + eskNameOffsetPhysics + counter + j * 72);
                            byteBuffer.clear();
                            channel.read(byteBuffer);
                            byteBuffer.flip();
                            counter++;
                        } while (byteBuffer.get() != 0);

                        dynamicStringBuffer = ByteBuffer.allocate(counter);

                        channel.position(mainOffset + physicsOffset + eskNameOffsetPhysics + j * 72);
                        dynamicStringBuffer.clear();
                        channel.read(dynamicStringBuffer);
                        dynamicStringBuffer.flip();
                        bcsPhysics.eskName = new String(dynamicStringBuffer.array(), StandardCharsets.ISO_8859_1);
                    }

                    channel.position(mainOffset + physicsOffset + j * 72 + 56);
                    intBuffer.clear();
                    channel.read(intBuffer);
                    intBuffer.flip();
                    int boneToAttatchOffset = intBuffer.getInt();

                    if (boneToAttatchOffset != 0) {
                        int counter = 0;

                        do {
                            channel.position(mainOffset + physicsOffset + boneToAttatchOffset + counter + j * 72);
                            byteBuffer.clear();
                            channel.read(byteBuffer);
                            byteBuffer.flip();
                            counter++;
                        } while (byteBuffer.get() != 0);

                        dynamicStringBuffer = ByteBuffer.allocate(counter);

                        channel.position(mainOffset + physicsOffset + boneToAttatchOffset + j * 72);
                        dynamicStringBuffer.clear();
                        channel.read(dynamicStringBuffer);
                        dynamicStringBuffer.flip();
                        bcsPhysics.boneToAttach = new String(dynamicStringBuffer.array(), StandardCharsets.ISO_8859_1);
                    }

                    channel.position(mainOffset + physicsOffset + j * 72 + 60);
                    intBuffer.clear();
                    channel.read(intBuffer);
                    intBuffer.flip();
                    int scdNameOffset = intBuffer.getInt();

                    if (scdNameOffset != 0) {
                        int counter = 0;

                        do {
                            channel.position(mainOffset + physicsOffset + scdNameOffset + counter + j * 72);
                            byteBuffer.clear();
                            channel.read(byteBuffer);
                            byteBuffer.flip();
                            counter++;
                        } while (byteBuffer.get() != 0);

                        dynamicStringBuffer = ByteBuffer.allocate(counter);

                        channel.position(mainOffset + physicsOffset + scdNameOffset + j * 72);
                        dynamicStringBuffer.clear();
                        channel.read(dynamicStringBuffer);
                        dynamicStringBuffer.flip();
                        bcsPhysics.scdName = new String(dynamicStringBuffer.array(), StandardCharsets.ISO_8859_1);
                    }
                }

                subPartIndex++;
            }

            if (version != 72) {
                channel.position(mainOffset + 82);
                shortBuffer.clear();
                channel.read(shortBuffer);
                shortBuffer.flip();
                short unknown3Count = shortBuffer.getShort();

                if (unknown3Count > 0) {
                    partSetsTreeView.getRoot().getChildren().get(mainIndex).getChildren().get(partIndex).getChildren().add(new TreeItem<>("Unknown 3"));

                    channel.position(mainOffset + 84);
                    intBuffer.clear();
                    channel.read(intBuffer);
                    intBuffer.flip();
                    int unknown3Offset = intBuffer.getInt();

                    for (int j = 0; j < unknown3Count; j++) {
                        BcsUnknown3 bcsUnknown3 = new BcsUnknown3();

                        partSetsTreeView.getRoot().getChildren().get(mainIndex).getChildren().get(partIndex).getChildren().get(subPartIndex).getChildren().add(new TreeItem<>("Entry " + j));

                        bcsUnknown3HashMap.put(partSetsTreeView.getRoot().getChildren().get(mainIndex).getChildren().get(partIndex).getChildren().get(subPartIndex).getChildren().get(j), bcsUnknown3);

                        channel.position(mainOffset + unknown3Offset + j * 12);
                        shortBuffer.clear();
                        channel.read(shortBuffer);
                        shortBuffer.flip();
                        bcsUnknown3.i00 = shortBuffer.getShort();
                        
                        channel.position(mainOffset + unknown3Offset + j * 12 + 2);
                        shortBuffer.clear();
                        channel.read(shortBuffer);
                        shortBuffer.flip();
                        bcsUnknown3.i02 = shortBuffer.getShort();

                        channel.position(mainOffset + unknown3Offset + j * 12 + 4);
                        shortBuffer.clear();
                        channel.read(shortBuffer);
                        shortBuffer.flip();
                        bcsUnknown3.i04 = shortBuffer.getShort();

                        channel.position(mainOffset + unknown3Offset + j * 12 + 6);
                        shortBuffer.clear();
                        channel.read(shortBuffer);
                        shortBuffer.flip();
                        bcsUnknown3.i06 = shortBuffer.getShort();

                        channel.position(mainOffset + unknown3Offset + j * 12 + 8);
                        shortBuffer.clear();
                        channel.read(shortBuffer);
                        shortBuffer.flip();
                        bcsUnknown3.i08 = shortBuffer.getShort();

                        channel.position(mainOffset + unknown3Offset + j * 12 + 10);
                        shortBuffer.clear();
                        channel.read(shortBuffer);
                        shortBuffer.flip();
                        bcsUnknown3.i10 = shortBuffer.getShort();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void skeletonsReader(int version, int mainOffset, FileChannel channel, String skeleton) {
        try {
            ByteBuffer byteBuffer = ByteBuffer.allocate(1).order(ByteOrder.LITTLE_ENDIAN);
            ByteBuffer shortBuffer = ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN);
            ByteBuffer intBuffer = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
            ByteBuffer dynamicStringBuffer;

            if (mainOffset != 0) {  
                int relativeOffset;

                if (version == 72) {
                    relativeOffset = 32;
                }
                else {
                    relativeOffset = mainOffset;
                }

                skeletonsEntries = new TreeItem<>(skeleton);
                skeletonsTreeView.getRoot().getChildren().add(skeletonsEntries);
            
                BcsSkeleton bcsSkeleton = new BcsSkeleton(); 
            
                bcsSkeletonsHashMap.put(skeletonsEntries, bcsSkeleton);

                channel.position(mainOffset);
                shortBuffer.clear();
                channel.read(shortBuffer);
                shortBuffer.flip();
                bcsSkeleton.i00 = shortBuffer.getShort();

                channel.position(mainOffset + 2);
                shortBuffer.clear();
                channel.read(shortBuffer);
                shortBuffer.flip();
                int boneCount = shortBuffer.getShort();

                if (boneCount > 0) {
                    channel.position(mainOffset + 4);
                    intBuffer.clear();
                    channel.read(intBuffer);
                    intBuffer.flip();
                    int boneOffset = intBuffer.getInt();

                    for (int j = 0; j < boneCount; j++) {
                        BcsBone bcsBone = new BcsBone();
                        
                        skeletonsEntries.getChildren().add(new TreeItem<>("Bone " + j));

                        bcsBonesHashMap.put(skeletonsEntries.getChildren().get(j), bcsBone);

                        if  (version == 72) {
                            channel.position(boneOffset + relativeOffset + j * 52);
                            intBuffer.clear();
                            channel.read(intBuffer);
                            intBuffer.flip();
                            bcsBone.i00 = intBuffer.getInt();

                            channel.position(boneOffset + relativeOffset + j * 52 + 4);
                            intBuffer.clear();
                            channel.read(intBuffer);
                            intBuffer.flip();
                            bcsBone.i04 = intBuffer.getInt();

                            channel.position(boneOffset + relativeOffset + j * 52 + 12);
                            intBuffer.clear();
                            channel.read(intBuffer);
                            intBuffer.flip();
                            int boneNameOffset = intBuffer.getInt();

                            if (boneNameOffset != 0) {
                                int counter = 0;

                                do {
                                    channel.position(boneOffset + relativeOffset + boneNameOffset + j * 52 + counter);
                                    byteBuffer.clear();
                                    channel.read(byteBuffer);
                                    byteBuffer.flip();
                                    counter++;
                                } while (byteBuffer.get() != 0);

                                dynamicStringBuffer = ByteBuffer.allocate(counter);

                                channel.position(boneOffset + relativeOffset + boneNameOffset + j * 52);
                                dynamicStringBuffer.clear();
                                channel.read(dynamicStringBuffer);
                                dynamicStringBuffer.flip();
                                bcsBone.boneName = new String(dynamicStringBuffer.array(), StandardCharsets.ISO_8859_1);
                            }

                            channel.position(boneOffset + relativeOffset + j * 52 + 16);
                            intBuffer.clear();
                            channel.read(intBuffer);
                            intBuffer.flip();
                            bcsBone.f12 = intBuffer.getFloat();

                            channel.position(boneOffset + relativeOffset + j * 52 + 20);
                            intBuffer.clear();
                            channel.read(intBuffer);
                            intBuffer.flip();
                            bcsBone.f16 = intBuffer.getFloat();

                            channel.position(boneOffset + relativeOffset + j * 52 + 24);
                            intBuffer.clear();
                            channel.read(intBuffer);
                            intBuffer.flip();
                            bcsBone.f20 = intBuffer.getFloat();

                            channel.position(boneOffset + relativeOffset + j * 52 + 28);
                            intBuffer.clear();
                            channel.read(intBuffer);
                            intBuffer.flip();
                            bcsBone.f24 = intBuffer.getFloat();

                            channel.position(boneOffset + relativeOffset + j * 52 + 32);
                            intBuffer.clear();
                            channel.read(intBuffer);
                            intBuffer.flip();
                            bcsBone.f28 = intBuffer.getFloat();

                            channel.position(boneOffset + relativeOffset + j * 52 + 36);
                            intBuffer.clear();
                            channel.read(intBuffer);
                            intBuffer.flip();
                            bcsBone.f32 = intBuffer.getFloat();

                            channel.position(boneOffset + relativeOffset + j * 52 + 40);
                            intBuffer.clear();
                            channel.read(intBuffer);
                            intBuffer.flip();
                            bcsBone.f36 = intBuffer.getFloat();

                            channel.position(boneOffset + relativeOffset + j * 52 + 44);
                            intBuffer.clear();
                            channel.read(intBuffer);
                            intBuffer.flip();
                            bcsBone.f40 = intBuffer.getFloat();

                            channel.position(boneOffset + relativeOffset + j * 52 + 48);
                            intBuffer.clear();
                            channel.read(intBuffer);
                            intBuffer.flip();
                            bcsBone.f44 = intBuffer.getFloat();
                        }
                        else {
                            channel.position(boneOffset + relativeOffset + j * 52);
                            intBuffer.clear();
                            channel.read(intBuffer);
                            intBuffer.flip();
                            bcsBone.i00 = intBuffer.getInt();

                            channel.position(boneOffset + relativeOffset + j * 52 + 4);
                            intBuffer.clear();
                            channel.read(intBuffer);
                            intBuffer.flip();
                            bcsBone.i04 = intBuffer.getInt();

                            channel.position(boneOffset + relativeOffset + j * 52 + 12);
                            intBuffer.clear();
                            channel.read(intBuffer);
                            intBuffer.flip();
                            bcsBone.f12 = intBuffer.getFloat();

                            channel.position(boneOffset + relativeOffset + j * 52 + 16);
                            intBuffer.clear();
                            channel.read(intBuffer);
                            intBuffer.flip();
                            bcsBone.f16 = intBuffer.getFloat();

                            channel.position(boneOffset + relativeOffset + j * 52 + 20);
                            intBuffer.clear();
                            channel.read(intBuffer);
                            intBuffer.flip();
                            bcsBone.f20 = intBuffer.getFloat();

                            channel.position(boneOffset + relativeOffset + j * 52 + 24);
                            intBuffer.clear();
                            channel.read(intBuffer);
                            intBuffer.flip();
                            bcsBone.f24 = intBuffer.getFloat();

                            channel.position(boneOffset + relativeOffset + j * 52 + 28);
                            intBuffer.clear();
                            channel.read(intBuffer);
                            intBuffer.flip();
                            bcsBone.f28 = intBuffer.getFloat();

                            channel.position(boneOffset + relativeOffset + j * 52 + 32);
                            intBuffer.clear();
                            channel.read(intBuffer);
                            intBuffer.flip();
                            bcsBone.f32 = intBuffer.getFloat();

                            channel.position(boneOffset + relativeOffset + j * 52 + 36);
                            intBuffer.clear();
                            channel.read(intBuffer);
                            intBuffer.flip();
                            bcsBone.f36 = intBuffer.getFloat();

                            channel.position(boneOffset + relativeOffset + j * 52 + 40);
                            intBuffer.clear();
                            channel.read(intBuffer);
                            intBuffer.flip();
                            bcsBone.f40 = intBuffer.getFloat();

                            channel.position(boneOffset + relativeOffset + j * 52 + 44);
                            intBuffer.clear();
                            channel.read(intBuffer);
                            intBuffer.flip();
                            bcsBone.f44 = intBuffer.getFloat();

                            channel.position(boneOffset + relativeOffset + j * 52 + 48);
                            intBuffer.clear();
                            channel.read(intBuffer);
                            intBuffer.flip();
                            int boneNameOffset = intBuffer.getInt();
                            
                            if (boneNameOffset != 0) {
                                int counter = 0;

                                do {
                                    channel.position(boneOffset + relativeOffset + boneNameOffset + j * 52 + counter);
                                    byteBuffer.clear();
                                    channel.read(byteBuffer);
                                    byteBuffer.flip();
                                    counter++;
                                } while (byteBuffer.get() != 0);

                                dynamicStringBuffer = ByteBuffer.allocate(counter);

                                channel.position(boneOffset + relativeOffset + boneNameOffset + j * 52);
                                dynamicStringBuffer.clear();
                                channel.read(dynamicStringBuffer);
                                dynamicStringBuffer.flip();
                                bcsBone.boneName = new String(dynamicStringBuffer.array(), StandardCharsets.ISO_8859_1);
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void bcsWriter(Path path) {
        try(FileChannel channel = FileChannel.open(path, StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            int partSetOffset = 0;
            int partColorOffset = 0;
            int bodyOffset = 0;
            int skeleton1Offset = 0;
            int skeleton2Offset = 0;

            int tableOffset = 32;

            int headerSum = version == 72 ? 72 : 76;
            int partSetOffsetSum = allPartSetEntries * 4;
            int partColorOffsetSum = allPartColorEntries * 4;
            int bodyOffsetSum = allBodyEntries * 4;
            int skeletonOffsetSum = (skeletonsTreeView.getRoot() != null && !skeletonsTreeView.getRoot().getChildren().isEmpty() && version != 72) ? skeletonsTreeView.getRoot().getChildren().size() * 4 : 0;
            int partOffsetData = (partSetsTreeView.getRoot() != null && !partSetsTreeView.getRoot().getChildren().isEmpty()) ? partSetsTreeView.getRoot().getChildren().size() * 72 : 0; //offsets for where each part is in the file and some other stuff
            int partColorOffsetData = (partColorsTreeView.getRoot() != null && !partColorsTreeView.getRoot().getChildren().isEmpty()) ? partColorsTreeView.getRoot().getChildren().size() * 16 : 0;
            int bodyOffsetData = (bodiesTreeView.getRoot() != null && !bodiesTreeView.getRoot().getChildren().isEmpty()) ? bodiesTreeView.getRoot().getChildren().size() * 8 : 0;

            int partSum = version == 72 ? bcsPartsHashMap.size() * 80 : bcsPartsHashMap.size() * 88;
            int colorSelectorSum = bcsColorsSelectorHashMap.size() * 4;
            int physicsSum = bcsPhysicsHashMap.size() * 72;
            int unknown3Sum = version == 72 ? 0 : bcsUnknown3HashMap.size() * 12;
            int colorSum = bcsColorsHashMap.size() * 80;
            int boneScaleSum = bcsBoneScalesHashMap.size() * 16;
            int skeletonSum = (skeletonsTreeView.getRoot() != null && !skeletonsTreeView.getRoot().getChildren().isEmpty() && version != 72) ? skeletonsTreeView.getRoot().getChildren().size() * 8 : 0;
            int boneSum = bcsBonesHashMap.size() * 52;

            typesSum = headerSum 
                + partSetOffsetSum 
                + partColorOffsetSum 
                + bodyOffsetSum 
                + skeletonOffsetSum
                + partOffsetData 
                + partColorOffsetData
                + bodyOffsetData 
                + partSum
                + colorSelectorSum 
                + physicsSum 
                + unknown3Sum
                + colorSum
                + boneScaleSum
                + skeletonSum
                + boneSum;

            ByteBuffer byteBuffer = ByteBuffer.allocate(1).order(ByteOrder.LITTLE_ENDIAN);
            ByteBuffer shortBuffer = ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN);
            ByteBuffer intBuffer = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
            ByteBuffer dynamicStringBuffer;

            channel.position(0);
            channel.write(ByteBuffer.wrap(new byte[] {0x23, 0x42, 0x43, 0x53}));

            channel.position(4);
            channel.write(ByteBuffer.wrap(new byte[]{(byte)0xFE, (byte)0xFF}));

            channel.position(6);
            intBuffer.clear();
            intBuffer.putInt(version);
            intBuffer.flip();
            channel.write(intBuffer);

            channel.position(12);
            shortBuffer.clear();
            shortBuffer.putShort((short) allPartSetEntries);
            shortBuffer.flip();
            channel.write(shortBuffer);

            channel.position(14);
            shortBuffer.clear();
            shortBuffer.putShort((short) allPartColorEntries);
            shortBuffer.flip();
            channel.write(shortBuffer);

            channel.position(16);
            shortBuffer.clear();
            shortBuffer.putShort((short) allBodyEntries);
            shortBuffer.flip();
            channel.write(shortBuffer);

            channel.position(18);
            shortBuffer.clear();
            shortBuffer.putShort((short) (skeletonsTreeView.getRoot() != null && skeletonsTreeView.getRoot().getChildren().size() > 1 ? 1 : 0));
            shortBuffer.flip();
            channel.write(shortBuffer);

            switch (version) {
                case 72 -> {
                    if (allPartSetEntries > 0) {
                        partSetOffset = 72;
                        thisPartSetOffset = partSetOffset + 4;
                    }
                    if (allPartColorEntries > 0) {
                        partColorOffset = 72  + allPartSetEntries * 4;
                        thisPartSetOffset = allPartSetEntries > 0 ? partColorOffset + 4 : 0; 
                        thisPartColorOffset = partColorOffset + 4;
                    }
                    if (allBodyEntries > 0) {
                        bodyOffset = 72 + allPartSetEntries * 4 + allPartColorEntries * 4;
                        thisPartSetOffset = allPartSetEntries > 0 ? bodyOffset + 4 : 0;
                        thisPartColorOffset = allPartColorEntries > 0 ? bodyOffset + 4 : 0;
                        thisBodyOffset = bodyOffset + 4;
                    }
                    if (skeletonsTreeView.getRoot() != null && !skeletonsTreeView.getRoot().getChildren().isEmpty()) {
                        thisPartSetOffset = allPartSetEntries > 0 ? 72 + allPartSetEntries * 4 + allPartColorEntries * 4 + allBodyEntries * 4 : 0;
                        thisPartColorOffset = allPartColorEntries > 0 ? 72 + allPartColorEntries * 4 + allPartColorEntries * 4 + allBodyEntries * 4 : 0;
                        thisBodyOffset = allBodyEntries > 0 ? 72 + allBodyEntries * 4 + allPartColorEntries * 4 + allBodyEntries * 4 : 0;
                        thisSkeleton1Offset = 64;
                        skeleton1Offset = 64;
                    }

                    channel.position(20);
                    intBuffer.clear();
                    intBuffer.putInt(partSetOffset);
                    intBuffer.flip();
                    channel.write(intBuffer);

                    channel.position(24);
                    intBuffer.clear();
                    intBuffer.putInt(partColorOffset);
                    intBuffer.flip();
                    channel.write(intBuffer);

                    channel.position(28);
                    intBuffer.clear();
                    intBuffer.putInt(bodyOffset);
                    intBuffer.flip();
                    channel.write(intBuffer);

                    channel.position(32);
                    byteBuffer.clear();
                    byteBuffer.put((byte) bcsPartSet.race);
                    byteBuffer.flip();
                    channel.write(byteBuffer);

                    channel.position(33);
                    byteBuffer.clear();
                    byteBuffer.put((byte) bcsPartSet.gender);
                    byteBuffer.flip();
                    channel.write(byteBuffer);

                    channel.position(34);
                    byteBuffer.clear();
                    byteBuffer.put((byte) bcsPartSet.i46);
                    byteBuffer.flip();
                    channel.write(byteBuffer);

                    channel.position(35);
                    byteBuffer.clear();
                    byteBuffer.put((byte) bcsPartSet.i47);
                    byteBuffer.flip();
                    channel.write(byteBuffer);

                    channel.position(36);
                    intBuffer.clear();
                    intBuffer.putFloat(bcsPartSet.positionY);
                    intBuffer.flip();
                    channel.write(intBuffer);

                    channel.position(40);
                    intBuffer.clear();
                    intBuffer.putFloat(bcsPartSet.cameraY);
                    intBuffer.flip();
                    channel.write(intBuffer);

                    channel.position(44);
                    intBuffer.clear();
                    intBuffer.putFloat(bcsPartSet.trackingOffset);
                    intBuffer.flip();
                    channel.write(intBuffer);

                    channel.position(48);
                    intBuffer.clear();
                    intBuffer.putFloat(bcsPartSet.f60);
                    intBuffer.flip();
                    channel.write(intBuffer);

                    channel.position(52);
                    intBuffer.clear();
                    intBuffer.putFloat(bcsPartSet.collisionScale);
                    intBuffer.flip();
                    channel.write(intBuffer);

                    channel.position(56);
                    intBuffer.clear();
                    intBuffer.putFloat(bcsPartSet.f68);
                    intBuffer.flip();
                    channel.write(intBuffer);

                    channel.position(60);
                    intBuffer.clear();
                    intBuffer.putFloat(bcsPartSet.f72);
                    intBuffer.flip();
                    channel.write(intBuffer);
                }
                case 76, 0 -> {
                    if (allPartSetEntries > 0) {
                        partSetOffset = 76;
                        thisPartSetOffset = partSetOffset + 4;
                    }
                    if (allPartColorEntries > 0) {
                        partColorOffset = 76 + allPartSetEntries * 4;
                        thisPartSetOffset = allPartSetEntries > 0 ? partColorOffset + 4 : 0;
                        thisPartColorOffset = allPartColorEntries * 4 + partColorOffset;
                    }
                    if (allBodyEntries > 0) {
                        bodyOffset = 76 + allPartSetEntries * 4 + allPartColorEntries * 4;
                        thisPartSetOffset = allPartSetEntries > 0 ? bodyOffset + 4 : 0;
                        thisPartColorOffset = allPartColorEntries > 0 ? bodyOffset + 4 : 0;
                        thisBodyOffset = bodyOffset + 4;
                    }
                    if (skeletonsTreeView.getRoot() != null && skeletonsTreeView.getRoot().getChildren().size() > 1) {
                        skeleton1Offset = 76 + allPartSetEntries * 4 + allPartColorEntries * 4 + allBodyEntries * 4;
                        skeleton2Offset = skeleton1Offset + 4;
                        thisPartSetOffset = allPartSetEntries > 0 ? skeleton2Offset + 4 : 0;
                        thisPartColorOffset = allPartColorEntries > 0 ? skeleton2Offset + 4 : 0;
                        thisBodyOffset = allBodyEntries > 0 ? skeleton2Offset + 4 : 0;
                    }
                    else if (skeletonsTreeView.getRoot() != null && !skeletonsTreeView.getRoot().getChildren().isEmpty()) {
                        skeleton1Offset = 76 + allPartSetEntries * 4 + allPartColorEntries * 4 + allBodyEntries * 4;
                        thisPartSetOffset = allPartSetEntries > 0 ? skeleton1Offset + 4 : 0;
                        thisPartColorOffset = allPartColorEntries > 0 ? skeleton1Offset + 4 : 0;
                        thisBodyOffset = allBodyEntries > 0 ? skeleton1Offset + 4 : 0;
                    }

                    channel.position(24);
                    intBuffer.clear();
                    intBuffer.putInt(partSetOffset);
                    intBuffer.flip();
                    channel.write(intBuffer);

                    channel.position(28);
                    intBuffer.clear();
                    intBuffer.putInt(partColorOffset);
                    intBuffer.flip();
                    channel.write(intBuffer);

                    channel.position(32);
                    intBuffer.clear();
                    intBuffer.putInt(bodyOffset);
                    intBuffer.flip();
                    channel.write(intBuffer);

                    channel.position(36);
                    intBuffer.clear();
                    intBuffer.putInt(skeleton2Offset);
                    intBuffer.flip();
                    channel.write(intBuffer);

                    channel.position(40);
                    intBuffer.clear();
                    intBuffer.putInt(skeleton1Offset);
                    intBuffer.flip();
                    channel.write(intBuffer);
                    
                    channel.position(44);
                    byteBuffer.clear();
                    byteBuffer.put((byte) bcsPartSet.race);
                    byteBuffer.flip();
                    channel.write(byteBuffer);

                    channel.position(45);
                    byteBuffer.clear();
                    byteBuffer.put((byte) bcsPartSet.gender);
                    byteBuffer.flip();
                    channel.write(byteBuffer);

                    channel.position(46);
                    byteBuffer.clear();
                    byteBuffer.put((byte) bcsPartSet.i46);
                    byteBuffer.flip();
                    channel.write(byteBuffer);

                    channel.position(47);
                    byteBuffer.clear();
                    byteBuffer.put((byte) bcsPartSet.i47);
                    byteBuffer.flip();
                    channel.write(byteBuffer);

                    channel.position(48);
                    intBuffer.clear();
                    intBuffer.putFloat(bcsPartSet.positionY);
                    intBuffer.flip();
                    channel.write(intBuffer);

                    channel.position(52);
                    intBuffer.clear();
                    intBuffer.putFloat(bcsPartSet.cameraY);
                    intBuffer.flip();
                    channel.write(intBuffer);

                    channel.position(56);
                    intBuffer.clear();
                    intBuffer.putFloat(bcsPartSet.trackingOffset);
                    intBuffer.flip();
                    channel.write(intBuffer);

                    channel.position(60);
                    intBuffer.clear();
                    intBuffer.putFloat(bcsPartSet.f60);
                    intBuffer.flip();
                    channel.write(intBuffer);

                    channel.position(64);
                    intBuffer.clear();
                    intBuffer.putFloat(bcsPartSet.collisionScale);
                    intBuffer.flip();
                    channel.write(intBuffer);

                    channel.position(68);
                    intBuffer.clear();
                    intBuffer.putFloat(bcsPartSet.f68);
                    intBuffer.flip();
                    channel.write(intBuffer);

                    channel.position(72);
                    intBuffer.clear();
                    intBuffer.putFloat(bcsPartSet.f72);
                    intBuffer.flip();
                    channel.write(intBuffer);
                }
            }

            if (allPartSetEntries > 0) {
                int mainIndex = 0;

                for (int i = 0; i < allPartSetEntries; i++) {
                    int partIndex = 0;
                    int subPartIndex = 0;

                    if (thisPartSetOffset != 0) {
                        channel.position(thisPartSetOffset + 20);
                        channel.write(ByteBuffer.wrap(new byte[] {(byte) 0x0A}));

                        if(Integer.parseInt(partSetsTreeView.getRoot().getChildren().get(mainIndex).getValue().toString().replaceAll("\\D+", "")) == i) {
                            relativeOffset = 72;
                            int offset = thisPartSetOffset;

                            channel.position(partSetOffset + i * 4);
                            intBuffer.clear();
                            intBuffer.putInt(thisPartSetOffset);
                            intBuffer.flip();
                            channel.write(intBuffer);

                            channel.position(thisPartSetOffset + 24);
                            intBuffer.clear();
                            intBuffer.putInt(tableOffset);
                            intBuffer.flip();
                            channel.write(intBuffer);

                            channel.position(offset + tableOffset);
                            intBuffer.clear();
                            if (partIndex < partSetsTreeView.getRoot().getChildren().get(mainIndex).getChildren().size()) {
                                if (partSetsTreeView.getRoot().getChildren().get(mainIndex).getChildren().get(partIndex).getValue().equals("Face Base")) {
                                    intBuffer.putInt(relativeOffset);
                                    intBuffer.flip();
                                    channel.write(intBuffer);
                                    
                                    partsWriter(relativeOffset, offset, mainIndex, partIndex, subPartIndex, channel);
                                    partIndex++;
                                    relativeOffset += (version == 72 ? 80 : 88);
                                    thisPartSetOffset += (version == 72 ? 80 : 88);
                                }
                            }
                            
                            channel.position(offset + tableOffset + 4);
                            intBuffer.clear();
                            if (partIndex < partSetsTreeView.getRoot().getChildren().get(mainIndex).getChildren().size()) {
                                if (partSetsTreeView.getRoot().getChildren().get(mainIndex).getChildren().get(partIndex).getValue().equals("Face Forehead")) {
                                    intBuffer.putInt(relativeOffset);
                                    intBuffer.flip();
                                    channel.write(intBuffer);
                                    
                                    partsWriter(relativeOffset, offset, mainIndex, partIndex, subPartIndex, channel);
                                    partIndex++;
                                    relativeOffset += (version == 72 ? 80 : 88);
                                    thisPartSetOffset += (version == 72 ? 80 : 88);
                                }
                            }
                            
                            channel.position(offset + tableOffset + 8);
                            intBuffer.clear();
                            if (partIndex < partSetsTreeView.getRoot().getChildren().get(mainIndex).getChildren().size()) {
                                if (partSetsTreeView.getRoot().getChildren().get(mainIndex).getChildren().get(partIndex).getValue().equals("Face Eye")) {
                                    intBuffer.putInt(relativeOffset);
                                    intBuffer.flip();
                                    channel.write(intBuffer);
                                    
                                    partsWriter(relativeOffset, offset, mainIndex, partIndex, subPartIndex, channel);
                                    partIndex++;
                                    relativeOffset += (version == 72 ? 80 : 88);
                                    thisPartSetOffset += (version == 72 ? 80 : 88);
                                }
                            }
                            
                            channel.position(offset + tableOffset + 12);
                            intBuffer.clear();
                            if (partIndex < partSetsTreeView.getRoot().getChildren().get(mainIndex).getChildren().size()) {
                                if (partSetsTreeView.getRoot().getChildren().get(mainIndex).getChildren().get(partIndex).getValue().equals("Face Nose")) {
                                    intBuffer.putInt(relativeOffset);
                                    intBuffer.flip();
                                    channel.write(intBuffer);
                                    
                                    partsWriter(relativeOffset, offset, mainIndex, partIndex, subPartIndex, channel);
                                    partIndex++;
                                    relativeOffset += (version == 72 ? 80 : 88);
                                    thisPartSetOffset += (version == 72 ? 80 : 88);
                                }
                            }
                            
                            channel.position(offset + tableOffset + 16);
                            intBuffer.clear();
                            if (partIndex < partSetsTreeView.getRoot().getChildren().get(mainIndex).getChildren().size()) {
                                if (partSetsTreeView.getRoot().getChildren().get(mainIndex).getChildren().get(partIndex).getValue().equals("Face Ear")) {
                                    intBuffer.putInt(relativeOffset);
                                    intBuffer.flip();
                                    channel.write(intBuffer);
                                    
                                    partsWriter(relativeOffset, offset, mainIndex, partIndex, subPartIndex, channel);
                                    partIndex++;
                                    relativeOffset += (version == 72 ? 80 : 88);
                                    thisPartSetOffset += (version == 72 ? 80 : 88);
                                }
                            }

                            channel.position(offset + tableOffset + 20);
                            intBuffer.clear();
                            if (partIndex < partSetsTreeView.getRoot().getChildren().get(mainIndex).getChildren().size()) {
                                if (partSetsTreeView.getRoot().getChildren().get(mainIndex).getChildren().get(partIndex).getValue().equals("Hair")) {
                                    intBuffer.putInt(relativeOffset);
                                    intBuffer.flip();
                                    channel.write(intBuffer);
                                    
                                    partsWriter(relativeOffset, offset, mainIndex, partIndex, subPartIndex, channel);
                                    partIndex++;
                                    relativeOffset += (version == 72 ? 80 : 88);
                                    thisPartSetOffset += (version == 72 ? 80 : 88);
                                }
                            }

                            channel.position(offset + tableOffset + 24);
                            intBuffer.clear();
                            if (partIndex < partSetsTreeView.getRoot().getChildren().get(mainIndex).getChildren().size()) {
                                if (partSetsTreeView.getRoot().getChildren().get(mainIndex).getChildren().get(partIndex).getValue().equals("Bust")) {
                                    intBuffer.putInt(relativeOffset);
                                    intBuffer.flip();
                                    channel.write(intBuffer);
                                    
                                    partsWriter(relativeOffset, offset, mainIndex, partIndex, subPartIndex, channel);
                                    partIndex++;
                                    relativeOffset += (version == 72 ? 80 : 88);
                                    thisPartSetOffset += (version == 72 ? 80 : 88);
                                }
                            }
                            
                            channel.position(offset + tableOffset + 28);
                            intBuffer.clear();
                            if (partIndex < partSetsTreeView.getRoot().getChildren().get(mainIndex).getChildren().size()) {
                                if (partSetsTreeView.getRoot().getChildren().get(mainIndex).getChildren().get(partIndex).getValue().equals("Pants")) {
                                    intBuffer.putInt(relativeOffset);
                                    intBuffer.flip();
                                    channel.write(intBuffer);
                                    
                                    partsWriter(relativeOffset, offset, mainIndex, partIndex, subPartIndex, channel);
                                    partIndex++;
                                    relativeOffset += (version == 72 ? 80 : 88);
                                    thisPartSetOffset += (version == 72 ? 80 : 88);
                                }
                            }

                            channel.position(offset + tableOffset + 32);
                            intBuffer.clear();
                            if (partIndex < partSetsTreeView.getRoot().getChildren().get(mainIndex).getChildren().size()) {
                                if (partSetsTreeView.getRoot().getChildren().get(mainIndex).getChildren().get(partIndex).getValue().equals("Rist")) {
                                    intBuffer.putInt(relativeOffset);
                                    intBuffer.flip();
                                    channel.write(intBuffer);
                                    
                                    partsWriter(relativeOffset, offset, mainIndex, partIndex, subPartIndex, channel);
                                    partIndex++;
                                    relativeOffset += (version == 72 ? 80 : 88);
                                    thisPartSetOffset += (version == 72 ? 80 : 88);
                                }
                            }

                            channel.position(offset + tableOffset + 36);
                            intBuffer.clear();
                            if (partIndex < partSetsTreeView.getRoot().getChildren().get(mainIndex).getChildren().size()) {
                                if (partSetsTreeView.getRoot().getChildren().get(mainIndex).getChildren().get(partIndex).getValue().equals("Boots")) {
                                    intBuffer.putInt(relativeOffset);
                                    intBuffer.flip();
                                    channel.write(intBuffer);
                                    
                                    partsWriter(relativeOffset, offset, mainIndex, partIndex, subPartIndex, channel);
                                    partIndex++;
                                    relativeOffset += (version == 72 ? 80 : 88);
                                    thisPartSetOffset += (version == 72 ? 80 : 88);
                                }
                            }
                            
                            thisPartSetOffset += 72;
                            mainIndex++;
                        }
                    }
                }

                thisPartColorOffset = thisPartSetOffset;
                thisBodyOffset = thisPartSetOffset;
                thisSkeleton1Offset = thisPartSetOffset;
            }

            if (allPartColorEntries > 0) {
                int mainIndex = 0;
                int colorOffset = partColorsTreeView.getRoot().getChildren().size() * 16;
                int offset = thisPartColorOffset;
                int stableOffset = offset;

                for (int i = 0; i < allPartColorEntries; i++) {
                    if (thisPartColorOffset != 0) {
                        if(Integer.parseInt(partColorsTreeView.getRoot().getChildren().get(mainIndex).getValue().toString().replaceAll("\\D+", "")) == i) {
                            BcsPartColor bcsPartColor = bcsPartColorsHashMap.get(partColorsTreeView.getRoot().getChildren().get(mainIndex));

                            channel.position(partColorOffset + i * 4);
                            intBuffer.clear();
                            intBuffer.putInt(thisPartColorOffset);
                            intBuffer.flip();
                            channel.write(intBuffer);

                            channel.position(offset);
                            intBuffer.clear();
                            intBuffer.putInt(typesSum - offset);
                            intBuffer.flip();
                            channel.write(intBuffer);

                            dynamicStringBuffer = ByteBuffer.allocate(bcsPartColor.name.getBytes().length);

                            channel.position(typesSum);
                            dynamicStringBuffer.clear();
                            dynamicStringBuffer = ByteBuffer.wrap(bcsPartColor.name.getBytes());
                            channel.write(dynamicStringBuffer);
                            typesSum += bcsPartColor.name.getBytes().length;

                            short colorCount = (short) partColorsTreeView.getRoot().getChildren().get(mainIndex).getChildren().size();
                            if (colorCount > 0) {
                                channel.position(offset + 10);
                                shortBuffer.clear();
                                shortBuffer.putShort(colorCount);
                                shortBuffer.flip();
                                channel.write(shortBuffer);

                                channel.position(offset + 12);
                                intBuffer.clear();
                                intBuffer.putInt(colorOffset - mainIndex * 16);
                                intBuffer.flip();
                                channel.write(intBuffer);
                                
                                for (int j = 0; j < colorCount; j++) {
                                    BcsColor bcsColor = bcsColorsHashMap.get(partColorsTreeView.getRoot().getChildren().get(mainIndex).getChildren().get(j));

                                    channel.position(stableOffset + colorOffset + 80 * j);
                                    intBuffer.clear();
                                    intBuffer.putFloat((float) bcsColor.color1.getRed());
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(stableOffset + colorOffset + 80 * j + 4);
                                    intBuffer.clear();
                                    intBuffer.putFloat((float) bcsColor.color1.getGreen());
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(stableOffset + colorOffset + 80 * j + 8);
                                    intBuffer.clear();
                                    intBuffer.putFloat((float) bcsColor.color1.getBlue());
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(stableOffset + colorOffset + 80 * j + 12);
                                    intBuffer.clear();
                                    intBuffer.putFloat((float) bcsColor.color1.getOpacity());
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(stableOffset + colorOffset + 80 * j + 16);
                                    intBuffer.clear();
                                    intBuffer.putFloat((float) bcsColor.color2.getRed());
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(stableOffset + colorOffset + 80 * j + 20);
                                    intBuffer.clear();
                                    intBuffer.putFloat((float) bcsColor.color2.getGreen());
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(stableOffset + colorOffset + 80 * j + 24);
                                    intBuffer.clear();
                                    intBuffer.putFloat((float) bcsColor.color2.getBlue());
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(stableOffset + colorOffset + 80 * j + 28);
                                    intBuffer.clear();
                                    intBuffer.putFloat((float) bcsColor.color2.getOpacity());
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(stableOffset + colorOffset + 80 * j + 32);
                                    intBuffer.clear();
                                    intBuffer.putFloat((float) bcsColor.color3.getRed());
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(stableOffset + colorOffset + 80 * j + 36);
                                    intBuffer.clear();
                                    intBuffer.putFloat((float) bcsColor.color3.getGreen());
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(stableOffset + colorOffset + 80 * j + 40);
                                    intBuffer.clear();
                                    intBuffer.putFloat((float) bcsColor.color3.getBlue());
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(stableOffset + colorOffset + 80 * j + 44);
                                    intBuffer.clear();
                                    intBuffer.putFloat((float) bcsColor.color3.getOpacity());
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(stableOffset + colorOffset + 80 * j + 48);
                                    intBuffer.clear();
                                    intBuffer.putFloat((float) bcsColor.color4.getRed());
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(stableOffset + colorOffset + 80 * j + 52);
                                    intBuffer.clear();
                                    intBuffer.putFloat((float) bcsColor.color4.getGreen());
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(stableOffset + colorOffset + 80 * j + 56);
                                    intBuffer.clear();
                                    intBuffer.putFloat((float) bcsColor.color4.getBlue());
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(stableOffset + colorOffset + 80 * j + 60);
                                    intBuffer.clear();
                                    intBuffer.putFloat((float) bcsColor.color4.getOpacity());
                                    intBuffer.flip();
                                    channel.write(intBuffer);
                                }
                                
                                colorOffset += colorCount * 80;
                            }
                            offset += 16;
                            thisPartColorOffset += 16;
                            mainIndex++;
                        }
                    }
                }

                thisBodyOffset = thisPartColorOffset;
                thisSkeleton1Offset = thisPartColorOffset;

                thisBodyOffset += (colorOffset - partColorsTreeView.getRoot().getChildren().size() * 16);
                thisSkeleton1Offset += (colorOffset - partColorsTreeView.getRoot().getChildren().size() * 16);
            }
            if (allBodyEntries > 0) {
                int mainIndex = 0;
                int boneScaleOffset = (bodiesTreeView.getRoot() != null && !bodiesTreeView.getRoot().getChildren().isEmpty()) ? bodiesTreeView.getRoot().getChildren().size() * 8 : 0;
                int offset = thisBodyOffset;
                int stableOffset = offset;

                for (int i = 0; i < allBodyEntries; i++) {
                    if (thisBodyOffset != 0) {
                        if(Integer.parseInt(bodiesTreeView.getRoot().getChildren().get(mainIndex).getValue().toString().replaceAll("\\D+", "")) == i) {
                            channel.position(bodyOffset + i * 4);
                            intBuffer.clear();
                            intBuffer.putInt(thisBodyOffset);
                            intBuffer.flip();
                            channel.write(intBuffer);

                            short boneScaleCount = (short) bodiesTreeView.getRoot().getChildren().get(mainIndex).getChildren().size();
                            if (boneScaleCount > 0) {
                                channel.position(offset + 2);
                                shortBuffer.clear();
                                shortBuffer.putShort(boneScaleCount);
                                shortBuffer.flip();
                                channel.write(shortBuffer);

                                channel.position(offset + 4);
                                intBuffer.clear();
                                intBuffer.putInt(boneScaleOffset - mainIndex * 8);
                                intBuffer.flip();
                                channel.write(intBuffer);

                                for (int j = 0; j < boneScaleCount; j++) {
                                    BcsBoneScale bcsBoneScale = bcsBoneScalesHashMap.get(bodiesTreeView.getRoot().getChildren().get(mainIndex).getChildren().get(j));

                                    channel.position(stableOffset + boneScaleOffset + j * 16);
                                    intBuffer.clear();
                                    intBuffer.putFloat(bcsBoneScale.scaleX);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(stableOffset + boneScaleOffset + j * 16 + 4);
                                    intBuffer.clear();
                                    intBuffer.putFloat(bcsBoneScale.scaleY);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(stableOffset + boneScaleOffset + j * 16 + 8);
                                    intBuffer.clear();
                                    intBuffer.putFloat(bcsBoneScale.scaleZ);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    if (bcsBoneScale.boneName != null) {
                                        channel.position(stableOffset + boneScaleOffset + j * 16 + 12);
                                        intBuffer.clear();
                                        intBuffer.putInt(typesSum - stableOffset - boneScaleOffset - j * 16);
                                        intBuffer.flip();
                                        channel.write(intBuffer);

                                        dynamicStringBuffer = ByteBuffer.allocate(bcsBoneScale.boneName.getBytes().length);

                                        channel.position(typesSum);
                                        dynamicStringBuffer.clear();
                                        dynamicStringBuffer = ByteBuffer.wrap(bcsBoneScale.boneName.getBytes());
                                        channel.write(dynamicStringBuffer);
                                        typesSum += bcsBoneScale.boneName.getBytes().length;
                                    } 
                                }

                                boneScaleOffset += boneScaleCount * 16;
                            }

                            offset += 8;
                            thisBodyOffset += 8;
                            mainIndex++;
                        }
                    } 
                }

                thisSkeleton1Offset = thisBodyOffset;
                thisSkeleton1Offset += (boneScaleOffset - bodiesTreeView.getRoot().getChildren().size() * 8);
            }
            if (skeleton1Offset != 0) {
                if (version != 72) {
                    channel.position(skeleton1Offset);
                    intBuffer.clear();
                    intBuffer.putInt(thisSkeleton1Offset);
                    intBuffer.flip();
                    channel.write(intBuffer);
                }
                
                BcsSkeleton bcsSkeleton = bcsSkeletonsHashMap.get(skeletonsTreeView.getRoot().getChildren().get(0));
                int offset = version == 72 ? 64 : thisSkeleton1Offset; 
                int boneOffset = version == 72 ? thisSkeleton1Offset - 32 : 8;

                channel.position(offset);
                shortBuffer.clear();
                shortBuffer.putShort(bcsSkeleton.i00);
                shortBuffer.flip();
                channel.write(shortBuffer);

                short boneCount = (short) skeletonsTreeView.getRoot().getChildren().get(0).getChildren().size();

                channel.position(offset + 2);
                shortBuffer.clear();
                shortBuffer.putShort(boneCount);
                shortBuffer.flip();
                channel.write(shortBuffer);

                channel.position(offset + 4);
                intBuffer.clear();
                intBuffer.putInt(boneOffset);
                intBuffer.flip();
                channel.write(intBuffer);

                if (version == 72) {
                    boneOffset -= 32;
                }

                for (int i = 0; i < boneCount; i++) {
                    BcsBone bcsBone = bcsBonesHashMap.get(skeletonsTreeView.getRoot().getChildren().get(0).getChildren().get(i));

                    if (version == 72) {
                        channel.position(offset + boneOffset + i * 52);
                        intBuffer.clear();
                        intBuffer.putInt(bcsBone.i00);
                        intBuffer.flip();
                        channel.write(intBuffer);

                        channel.position(offset + boneOffset + i * 52 + 4);
                        intBuffer.clear();
                        intBuffer.putInt(bcsBone.i04);
                        intBuffer.flip();
                        channel.write(intBuffer);

                        
                        channel.position(offset + boneOffset + i * 52 + 12);
                        intBuffer.clear();
                        intBuffer.putInt(typesSum - offset - boneOffset - i * 52);
                        intBuffer.flip();
                        channel.write(intBuffer);

                        dynamicStringBuffer = ByteBuffer.allocate(bcsBone.boneName.getBytes().length);

                        channel.position(typesSum);
                        dynamicStringBuffer.clear();
                        dynamicStringBuffer = ByteBuffer.wrap(bcsBone.boneName.getBytes());
                        channel.write(dynamicStringBuffer);

                        typesSum += bcsBone.boneName.getBytes().length;

                        channel.position(offset + boneOffset + i * 52 + 16);
                        intBuffer.clear();
                        intBuffer.putFloat(bcsBone.f12);
                        intBuffer.flip();
                        channel.write(intBuffer);

                        channel.position(offset + boneOffset + i * 52 + 20);
                        intBuffer.clear();
                        intBuffer.putFloat(bcsBone.f16);
                        intBuffer.flip();
                        channel.write(intBuffer);

                        channel.position(offset + boneOffset + i * 52 + 24);
                        intBuffer.clear();
                        intBuffer.putFloat(bcsBone.f20);
                        intBuffer.flip();
                        channel.write(intBuffer);

                        channel.position(offset + boneOffset + i * 52 + 28);
                        intBuffer.clear();
                        intBuffer.putFloat(bcsBone.f24);
                        intBuffer.flip();
                        channel.write(intBuffer);

                        channel.position(offset + boneOffset + i * 52 + 32);
                        intBuffer.clear();
                        intBuffer.putFloat(bcsBone.f28);
                        intBuffer.flip();
                        channel.write(intBuffer);

                        channel.position(offset + boneOffset + i * 52 + 36);
                        intBuffer.clear();
                        intBuffer.putFloat(bcsBone.f32);
                        intBuffer.flip();
                        channel.write(intBuffer);

                        channel.position(offset + boneOffset + i * 52 + 40);
                        intBuffer.clear();
                        intBuffer.putFloat(bcsBone.f36);
                        intBuffer.flip();
                        channel.write(intBuffer);

                        channel.position(offset + boneOffset + i * 52 + 44);
                        intBuffer.clear();
                        intBuffer.putFloat(bcsBone.f40);
                        intBuffer.flip();
                        channel.write(intBuffer);

                        channel.position(offset + boneOffset + i * 52 + 48);
                        intBuffer.clear();
                        intBuffer.putFloat(bcsBone.f44);
                        intBuffer.flip();
                        channel.write(intBuffer);  
                    }
                    else {
                        channel.position(offset + boneOffset + i * 52);
                        intBuffer.clear();
                        intBuffer.putInt(bcsBone.i00);
                        intBuffer.flip();
                        channel.write(intBuffer);

                        channel.position(offset + boneOffset + i * 52 + 4);
                        intBuffer.clear();
                        intBuffer.putInt(bcsBone.i04);
                        intBuffer.flip();
                        channel.write(intBuffer);

                        channel.position(offset + boneOffset + i * 52 + 12);
                        intBuffer.clear();
                        intBuffer.putFloat(bcsBone.f12);
                        intBuffer.flip();
                        channel.write(intBuffer);

                        channel.position(offset + boneOffset + i * 52 + 16);
                        intBuffer.clear();
                        intBuffer.putFloat(bcsBone.f16);
                        intBuffer.flip();
                        channel.write(intBuffer);

                        channel.position(offset + boneOffset + i * 52 + 20);
                        intBuffer.clear();
                        intBuffer.putFloat(bcsBone.f20);
                        intBuffer.flip();
                        channel.write(intBuffer);

                        channel.position(offset + boneOffset + i * 52 + 24);
                        intBuffer.clear();
                        intBuffer.putFloat(bcsBone.f24);
                        intBuffer.flip();
                        channel.write(intBuffer);

                        channel.position(offset + boneOffset + i * 52 + 28);
                        intBuffer.clear();
                        intBuffer.putFloat(bcsBone.f28);
                        intBuffer.flip();
                        channel.write(intBuffer);

                        channel.position(offset + boneOffset + i * 52 + 32);
                        intBuffer.clear();
                        intBuffer.putFloat(bcsBone.f32);
                        intBuffer.flip();
                        channel.write(intBuffer);

                        channel.position(offset + boneOffset + i * 52 + 36);
                        intBuffer.clear();
                        intBuffer.putFloat(bcsBone.f36);
                        intBuffer.flip();
                        channel.write(intBuffer);

                        channel.position(offset + boneOffset + i * 52 + 40);
                        intBuffer.clear();
                        intBuffer.putFloat(bcsBone.f40);
                        intBuffer.flip();
                        channel.write(intBuffer);

                        channel.position(offset + boneOffset + i * 52 + 44);
                        intBuffer.clear();
                        intBuffer.putFloat(bcsBone.f44);
                        intBuffer.flip();
                        channel.write(intBuffer);  

                        channel.position(offset + boneOffset + i * 52 + 48);
                        intBuffer.clear();
                        intBuffer.putInt(typesSum - offset - boneOffset - i * 52);
                        intBuffer.flip();
                        channel.write(intBuffer);

                        dynamicStringBuffer = ByteBuffer.allocate(bcsBone.boneName.getBytes().length);

                        channel.position(typesSum);
                        dynamicStringBuffer.clear();
                        dynamicStringBuffer = ByteBuffer.wrap(bcsBone.boneName.getBytes());
                        channel.write(dynamicStringBuffer);

                        typesSum += bcsBone.boneName.getBytes().length;
                    }
                }

                thisSkeleton2Offset = thisSkeleton1Offset + 268;
            }
            if (skeleton2Offset != 0) {
                channel.position(skeleton2Offset);
                intBuffer.clear();
                intBuffer.putInt(thisSkeleton2Offset);
                intBuffer.flip();
                channel.write(intBuffer);

                BcsSkeleton bcsSkeleton = bcsSkeletonsHashMap.get(skeletonsTreeView.getRoot().getChildren().get(1));
                int offset = thisSkeleton2Offset; 
                int boneOffset = 8;

                channel.position(offset);
                shortBuffer.clear();
                shortBuffer.putShort(bcsSkeleton.i00);
                shortBuffer.flip();
                channel.write(shortBuffer);

                short boneCount = (short) skeletonsTreeView.getRoot().getChildren().get(0).getChildren().size();

                channel.position(offset + 2);
                shortBuffer.clear();
                shortBuffer.putShort(boneCount);
                shortBuffer.flip();
                channel.write(shortBuffer);

                channel.position(offset + 4);
                intBuffer.clear();
                intBuffer.putInt(boneOffset);
                intBuffer.flip();
                channel.write(intBuffer);

                for (int i = 0; i < boneCount; i++) {
                    BcsBone bcsBone = bcsBonesHashMap.get(skeletonsTreeView.getRoot().getChildren().get(0).getChildren().get(i));

                    channel.position(offset + boneOffset + i * 52);
                    intBuffer.clear();
                    intBuffer.putInt(bcsBone.i00);
                    intBuffer.flip();
                    channel.write(intBuffer);

                    channel.position(offset + boneOffset + i * 52 + 4);
                    intBuffer.clear();
                    intBuffer.putInt(bcsBone.i04);
                    intBuffer.flip();
                    channel.write(intBuffer);

                    channel.position(offset + boneOffset + i * 52 + 12);
                    intBuffer.clear();
                    intBuffer.putFloat(bcsBone.f12);
                    intBuffer.flip();
                    channel.write(intBuffer);

                    channel.position(offset + boneOffset + i * 52 + 16);
                    intBuffer.clear();
                    intBuffer.putFloat(bcsBone.f16);
                    intBuffer.flip();
                    channel.write(intBuffer);

                    channel.position(offset + boneOffset + i * 52 + 20);
                    intBuffer.clear();
                    intBuffer.putFloat(bcsBone.f20);
                    intBuffer.flip();
                    channel.write(intBuffer);

                    channel.position(offset + boneOffset + i * 52 + 24);
                    intBuffer.clear();
                    intBuffer.putFloat(bcsBone.f24);
                    intBuffer.flip();
                    channel.write(intBuffer);

                    channel.position(offset + boneOffset + i * 52 + 28);
                    intBuffer.clear();
                    intBuffer.putFloat(bcsBone.f28);
                    intBuffer.flip();
                    channel.write(intBuffer);

                    channel.position(offset + boneOffset + i * 52 + 32);
                    intBuffer.clear();
                    intBuffer.putFloat(bcsBone.f32);
                    intBuffer.flip();
                    channel.write(intBuffer);

                    channel.position(offset + boneOffset + i * 52 + 36);
                    intBuffer.clear();
                    intBuffer.putFloat(bcsBone.f36);
                    intBuffer.flip();
                    channel.write(intBuffer);

                    channel.position(offset + boneOffset + i * 52 + 40);
                    intBuffer.clear();
                    intBuffer.putFloat(bcsBone.f40);
                    intBuffer.flip();
                    channel.write(intBuffer);

                    channel.position(offset + boneOffset + i * 52 + 44);
                    intBuffer.clear();
                    intBuffer.putFloat(bcsBone.f44);
                    intBuffer.flip();
                    channel.write(intBuffer);  

                    channel.position(offset + boneOffset + i * 52 + 48);
                    intBuffer.clear();
                    intBuffer.putInt(typesSum - offset - boneOffset - i * 52);
                    intBuffer.flip();
                    channel.write(intBuffer);

                    dynamicStringBuffer = ByteBuffer.allocate(bcsBone.boneName.getBytes().length);

                    channel.position(typesSum);
                    dynamicStringBuffer.clear();
                    dynamicStringBuffer = ByteBuffer.wrap(bcsBone.boneName.getBytes());
                    channel.write(dynamicStringBuffer);

                    typesSum += bcsBone.boneName.getBytes().length;
                }
            }
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    private void partsWriter(int relativeOffset, int mainOffset, int mainIndex, int partIndex, int subPartIndex, FileChannel channel) {
        try {
            ByteBuffer shortBuffer = ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN);
            ByteBuffer intBuffer = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
            ByteBuffer dynamicStringBuffer;

            short colorSelectorCount = 0;
            short physicsCount = 0;
            short unknown3Count = 0;
            
            mainOffset += relativeOffset;

            BcsPart bcsPart = bcsPartsHashMap.get(partSetsTreeView.getRoot().getChildren().get(mainIndex).getChildren().get(partIndex));
            
            channel.position(mainOffset);
            shortBuffer.clear();
            shortBuffer.putShort(bcsPart.model);
            shortBuffer.flip();
            channel.write(shortBuffer);

            channel.position(mainOffset + 2);
            shortBuffer.clear();
            shortBuffer.putShort(bcsPart.model2);
            shortBuffer.flip();
            channel.write(shortBuffer);

            channel.position(mainOffset + 4);
            shortBuffer.clear();
            shortBuffer.putShort(bcsPart.texture);
            shortBuffer.flip();
            channel.write(shortBuffer);

            channel.position(mainOffset + 16);
            shortBuffer.clear();
            shortBuffer.putShort(bcsPart.shader);
            shortBuffer.flip();
            channel.write(shortBuffer);

            if (subPartIndex < partSetsTreeView.getRoot().getChildren().get(mainIndex).getChildren().get(partIndex).getChildren().size() && partSetsTreeView.getRoot().getChildren().get(mainIndex).getChildren().get(partIndex).getChildren().get(subPartIndex).getValue().equals("Color Selectors")) {
                colorSelectorCount = (short) partSetsTreeView.getRoot().getChildren().get(mainIndex).getChildren().get(partIndex).getChildren().get(subPartIndex).getChildren().size();
            }
           
            channel.position(mainOffset + 18);
            shortBuffer.clear();
            shortBuffer.putShort(colorSelectorCount);
            shortBuffer.flip();
            channel.write(shortBuffer);

            if (colorSelectorCount > 0) {
                int colorSelectorOffset = (version == 72 ? 80 : 88);

                channel.position(mainOffset + 20);
                intBuffer.clear();
                intBuffer.putInt(colorSelectorOffset);
                intBuffer.flip();
                channel.write(intBuffer);
                
                for (int j = 0; j < colorSelectorCount; j++) {
                    BcsColorSelector bcsColorSelector = bcsColorsSelectorHashMap.get(partSetsTreeView.getRoot().getChildren().get(mainIndex).getChildren().get(partIndex).getChildren().get(subPartIndex).getChildren().get(j));

                    channel.position(mainOffset + colorSelectorOffset + j * 4);
                    shortBuffer.clear();
                    shortBuffer.putShort((short) bcsColorSelector.partColorGroup);
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    channel.position(mainOffset + colorSelectorOffset + j * 4 + 2);
                    shortBuffer.clear();
                    shortBuffer.putShort((short) bcsColorSelector.colorIndex);
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    thisPartSetOffset += 4;
                    this.relativeOffset += 4;
                }

                subPartIndex++;
            }

            channel.position(mainOffset + 24);
            intBuffer.clear();
            intBuffer.putInt((int) bcsPart.flags);
            intBuffer.flip();
            channel.write(intBuffer);

            channel.position(mainOffset + 28);
            intBuffer.clear();
            intBuffer.putInt(bcsPart.hideFlags);
            intBuffer.flip();
            channel.write(intBuffer);

            channel.position(mainOffset + 32);
            intBuffer.clear();
            intBuffer.putInt(bcsPart.hideMatFlags);
            intBuffer.flip();
            channel.write(intBuffer);

            channel.position(mainOffset + 36);
            intBuffer.clear();
            intBuffer.putFloat(bcsPart.f36);
            intBuffer.flip();
            channel.write(intBuffer);

            channel.position(mainOffset + 40);
            intBuffer.clear();
            intBuffer.putFloat(bcsPart.f40);
            intBuffer.flip();
            channel.write(intBuffer);

            channel.position(mainOffset + 44);
            intBuffer.clear();
            intBuffer.putInt(bcsPart.i44);
            intBuffer.flip();
            channel.write(intBuffer);

            channel.position(mainOffset + 48);
            intBuffer.clear();
            intBuffer.putInt(bcsPart.i48);
            intBuffer.flip();
            channel.write(intBuffer);

            channel.position(mainOffset + 52);
            intBuffer.clear();
            intBuffer.put(bcsPart.charaCode.getBytes(StandardCharsets.ISO_8859_1));
            intBuffer.flip();
            channel.write(intBuffer);

            if (bcsPart.emdName != null) {
                channel.position(mainOffset + 56);
                intBuffer.clear();
                intBuffer.putInt(typesSum - mainOffset);
                intBuffer.flip();
                channel.write(intBuffer);
                
                dynamicStringBuffer = ByteBuffer.allocate(bcsPart.emdName.getBytes().length);

                channel.position(typesSum);
                dynamicStringBuffer.clear();
                dynamicStringBuffer = ByteBuffer.wrap(bcsPart.emdName.getBytes());
                channel.write(dynamicStringBuffer);

                typesSum += bcsPart.emdName.getBytes().length;
            }

            if (bcsPart.emmName != null) {
                channel.position(mainOffset + 60);
                intBuffer.clear();
                intBuffer.putInt(typesSum - mainOffset);
                intBuffer.flip();
                channel.write(intBuffer);
                
                dynamicStringBuffer = ByteBuffer.allocate(bcsPart.emmName.getBytes().length);

                channel.position(typesSum);
                dynamicStringBuffer.clear();
                dynamicStringBuffer = ByteBuffer.wrap(bcsPart.emmName.getBytes());
                channel.write(dynamicStringBuffer);

                typesSum += bcsPart.emmName.getBytes().length;
            }

            if (bcsPart.embName != null) {
                channel.position(mainOffset + 64);
                intBuffer.clear();
                intBuffer.putInt(typesSum - mainOffset);
                intBuffer.flip();
                channel.write(intBuffer);
                
                dynamicStringBuffer = ByteBuffer.allocate(bcsPart.embName.getBytes().length);

                channel.position(typesSum);
                dynamicStringBuffer.clear();
                dynamicStringBuffer = ByteBuffer.wrap(bcsPart.embName.getBytes());
                channel.write(dynamicStringBuffer);

                typesSum += bcsPart.embName.getBytes().length;
            }

            if (bcsPart.eanName != null) {
                channel.position(mainOffset + 68);
                intBuffer.clear();
                intBuffer.putInt(typesSum - mainOffset);
                intBuffer.flip();
                channel.write(intBuffer);
                
                dynamicStringBuffer = ByteBuffer.allocate(bcsPart.eanName.getBytes().length);

                channel.position(typesSum);
                dynamicStringBuffer.clear();
                dynamicStringBuffer = ByteBuffer.wrap(bcsPart.eanName.getBytes());
                channel.write(dynamicStringBuffer);

                typesSum += bcsPart.eanName.getBytes().length;
            }

            if (subPartIndex < (short) partSetsTreeView.getRoot().getChildren().get(mainIndex).getChildren().get(partIndex).getChildren().size() && partSetsTreeView.getRoot().getChildren().get(mainIndex).getChildren().get(partIndex).getChildren().get(subPartIndex).getValue().equals("Physics")) {
                physicsCount = (short) partSetsTreeView.getRoot().getChildren().get(mainIndex).getChildren().get(partIndex).getChildren().get(subPartIndex).getChildren().size();
            }
             
            channel.position(mainOffset + 74);
            shortBuffer.clear();
            shortBuffer.putShort(physicsCount);
            shortBuffer.flip();
            channel.write(shortBuffer);

            if (physicsCount > 0) {
                int physicsOffset = (version == 72 ? 80 + colorSelectorCount * 4 : 88 + colorSelectorCount * 4);

                channel.position(mainOffset + 76);
                intBuffer.clear();
                intBuffer.putInt(physicsOffset);
                intBuffer.flip();
                channel.write(intBuffer);

                for (int j = 0; j < physicsCount; j++) {
                    BcsPhysics bcsPhysics = bcsPhysicsHashMap.get(partSetsTreeView.getRoot().getChildren().get(mainIndex).getChildren().get(partIndex).getChildren().get(subPartIndex).getChildren().get(j));

                    channel.position(mainOffset + physicsOffset + j * 72);
                    shortBuffer.clear();
                    shortBuffer.putShort(bcsPhysics.model);
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    channel.position(mainOffset + physicsOffset + j * 72 + 2);
                    shortBuffer.clear();
                    shortBuffer.putShort(bcsPhysics.model2);
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    channel.position(mainOffset + physicsOffset + j * 72 + 4);
                    shortBuffer.clear();
                    shortBuffer.putShort(bcsPhysics.texture);
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    channel.position(mainOffset + physicsOffset + j * 72 + 24);
                    intBuffer.clear();
                    intBuffer.putInt((int) bcsPhysics.flags);
                    intBuffer.flip();
                    channel.write(intBuffer);

                    channel.position(mainOffset + physicsOffset + j * 72 + 28);
                    intBuffer.clear();
                    intBuffer.putInt(bcsPhysics.hideFlags);
                    intBuffer.flip();
                    channel.write(intBuffer);

                    channel.position(mainOffset + physicsOffset + j * 72 + 32);
                    intBuffer.clear();
                    intBuffer.putInt(bcsPhysics.hideMatFlags);
                    intBuffer.flip();
                    channel.write(intBuffer);

                    channel.position(mainOffset + physicsOffset + j * 72 + 36);
                    intBuffer.clear();
                    intBuffer.put(bcsPhysics.charaCode.getBytes(StandardCharsets.ISO_8859_1));
                    intBuffer.flip();
                    channel.write(intBuffer);

                    if (bcsPhysics.emdName != null) {
                        channel.position(mainOffset + physicsOffset + j * 72 + 40);
                        intBuffer.clear();
                        intBuffer.putInt(typesSum - mainOffset - physicsOffset - j * 72);
                        intBuffer.flip();
                        channel.write(intBuffer);
                        
                        dynamicStringBuffer = ByteBuffer.allocate(bcsPhysics.emdName.getBytes().length);

                        channel.position(typesSum);
                        dynamicStringBuffer.clear();
                        dynamicStringBuffer = ByteBuffer.wrap(bcsPhysics.emdName.getBytes());
                        channel.write(dynamicStringBuffer);

                        typesSum += bcsPhysics.emdName.getBytes().length;
                    }

                    if (bcsPhysics.emmName != null) {
                        channel.position(mainOffset + physicsOffset + j * 72 + 44);
                        intBuffer.clear();
                        intBuffer.putInt(typesSum - mainOffset - physicsOffset - j * 72);
                        intBuffer.flip();
                        channel.write(intBuffer);
                        
                        dynamicStringBuffer = ByteBuffer.allocate(bcsPhysics.emmName.getBytes().length);

                        channel.position(typesSum);
                        dynamicStringBuffer.clear();
                        dynamicStringBuffer = ByteBuffer.wrap(bcsPhysics.emmName.getBytes());
                        channel.write(dynamicStringBuffer);

                        typesSum += bcsPhysics.emmName.getBytes().length;
                    }

                    if (bcsPhysics.embName != null) {
                        channel.position(mainOffset + physicsOffset + j * 72 + 48);
                        intBuffer.clear();
                        intBuffer.putInt(typesSum - mainOffset - physicsOffset - j * 72);
                        intBuffer.flip();
                        channel.write(intBuffer);
                        
                        dynamicStringBuffer = ByteBuffer.allocate(bcsPhysics.embName.getBytes().length);

                        channel.position(typesSum);
                        dynamicStringBuffer.clear();
                        dynamicStringBuffer = ByteBuffer.wrap(bcsPhysics.embName.getBytes());
                        channel.write(dynamicStringBuffer);

                        typesSum += bcsPhysics.embName.getBytes().length;
                    }

                    if (bcsPhysics.eskName != null) {
                        channel.position(mainOffset + physicsOffset + j * 72 + 52);
                        intBuffer.clear();
                        intBuffer.putInt(typesSum - mainOffset - physicsOffset - j * 72);
                        intBuffer.flip();
                        channel.write(intBuffer);
                        
                        dynamicStringBuffer = ByteBuffer.allocate(bcsPhysics.eskName.getBytes().length);

                        channel.position(typesSum);
                        dynamicStringBuffer.clear();
                        dynamicStringBuffer = ByteBuffer.wrap(bcsPhysics.eskName.getBytes());
                        channel.write(dynamicStringBuffer);

                        typesSum += bcsPhysics.eskName.getBytes().length;
                    }

                    if (bcsPhysics.boneToAttach != null) {
                        channel.position(mainOffset + physicsOffset + j * 72 + 56);
                        intBuffer.clear();
                        intBuffer.putInt(typesSum - mainOffset  - physicsOffset - j * 72);
                        intBuffer.flip();
                        channel.write(intBuffer);
                        
                        dynamicStringBuffer = ByteBuffer.allocate(bcsPhysics.boneToAttach.getBytes().length);

                        channel.position(typesSum);
                        dynamicStringBuffer.clear();
                        dynamicStringBuffer = ByteBuffer.wrap(bcsPhysics.boneToAttach.getBytes());
                        channel.write(dynamicStringBuffer);

                        typesSum += bcsPhysics.boneToAttach.getBytes().length;
                    }

                    if (bcsPhysics.scdName != null) {
                        channel.position(mainOffset + physicsOffset + j * 72 + 60);
                        intBuffer.clear();
                        intBuffer.putInt(typesSum - mainOffset - physicsOffset - j * 72);
                        intBuffer.flip();
                        channel.write(intBuffer);
                        
                        dynamicStringBuffer = ByteBuffer.allocate(bcsPhysics.scdName.getBytes().length);

                        channel.position(typesSum);
                        dynamicStringBuffer.clear();
                        dynamicStringBuffer = ByteBuffer.wrap(bcsPhysics.scdName.getBytes());
                        channel.write(dynamicStringBuffer);

                        typesSum += bcsPhysics.scdName.getBytes().length;
                    }

                    thisPartSetOffset +=  72;
                    this.relativeOffset +=  72;
                }

                subPartIndex++;
            }

            if (version != 72) {
                if (subPartIndex < (short) partSetsTreeView.getRoot().getChildren().get(mainIndex).getChildren().get(partIndex).getChildren().size() && partSetsTreeView.getRoot().getChildren().get(mainIndex).getChildren().get(partIndex).getChildren().get(subPartIndex).getValue().equals("Unknown 3")) {
                    unknown3Count = (short) partSetsTreeView.getRoot().getChildren().get(mainIndex).getChildren().get(partIndex).getChildren().get(subPartIndex).getChildren().size();
                }
                    
                channel.position(mainOffset + 82);
                shortBuffer.clear();
                shortBuffer.putShort(unknown3Count);
                shortBuffer.flip();
                channel.write(shortBuffer);

                if (unknown3Count > 0) {
                    int unknown3Offset = 88 + colorSelectorCount * 4 + physicsCount * 72;

                    channel.position(mainOffset + 84);
                    intBuffer.clear();
                    intBuffer.putInt(unknown3Offset);
                    intBuffer.flip();
                    channel.write(intBuffer);

                    for (int j = 0; j < unknown3Count; j++) {
                        BcsUnknown3 bcsUnknown3 =  bcsUnknown3HashMap.get(partSetsTreeView.getRoot().getChildren().get(mainIndex).getChildren().get(partIndex).getChildren().get(subPartIndex).getChildren().get(j));

                        channel.position(mainOffset + unknown3Offset + j * 12);
                        shortBuffer.clear();
                        shortBuffer.putShort(bcsUnknown3.i00);
                        shortBuffer.flip();
                        channel.write(shortBuffer);
                        
                        channel.position(mainOffset + unknown3Offset + j * 12 + 2);
                        shortBuffer.clear();
                        shortBuffer.putShort(bcsUnknown3.i02);
                        shortBuffer.flip();
                        channel.write(shortBuffer);

                        channel.position(mainOffset + unknown3Offset + j * 12 + 4);
                        shortBuffer.clear();
                        shortBuffer.putShort(bcsUnknown3.i04);
                        shortBuffer.flip();
                        channel.write(shortBuffer);

                        channel.position(mainOffset + unknown3Offset + j * 12 + 6);
                        shortBuffer.clear();
                        shortBuffer.putShort(bcsUnknown3.i06);
                        shortBuffer.flip();
                        channel.write(shortBuffer);

                        channel.position(mainOffset + unknown3Offset + j * 12 + 8);
                        shortBuffer.clear();
                        shortBuffer.putShort(bcsUnknown3.i08);
                        shortBuffer.flip();
                        channel.write(shortBuffer);

                        channel.position(mainOffset + unknown3Offset + j * 12 + 10);
                        shortBuffer.clear();
                        shortBuffer.putShort(bcsUnknown3.i10);
                        shortBuffer.flip();
                        channel.write(shortBuffer);

                        thisPartSetOffset += 12;
                        this.relativeOffset += 12;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static enum BcsPartSetValues {
        Race,
        Gender,
        I46,
        I47,
        PositionY,
        CameraY,
        TrackingOffset,
        F60,
        CollisionScale,
        F68,
        F72;
    }

    public static enum BcsPartValues {
        Model,
        Model2,
        Texture,
        Shader,
        Flags,
        HideFlags,
        HideMatFlags,
        F36,
        F40,
        I44,
        I48,
        CharaCode,
        EMD_Name,
        EMM_Name,
        EMB_Name,
        EAN_Name;
    }

    public static enum BcsColorSelectorValues {
        PartColorGroup,
        ColorIndex;
    }

    public static enum BcsPhysicsValues {
        Model,
        Model2,
        Texture,
        Flags,
        HideFlags,
        HideMatFlags,
        CharaCode,
        EMD_Name,
        EMM_Name,
        EMB_Name,
        ESK_Name,
        BoneToAttatch,
        SCD_Name;
    }

    public static enum BcsUnknown3Values {
        I00,
        I02,
        I04,
        I06,
        I08,
        I10;
    }

    public static enum BcsPartColorValues {
        Name;
    }

    public static enum BcsColorValues {
        Color1,
        Color2,
        Color3,
        Color4;
    }

    public static enum BcsBoneScaleValues {
        ScaleX,
        ScaleY,
        ScaleZ,
        Bone_Name;
    }

    public static enum BcsSkeletonValues {
        I00;
    }

    public static enum BcsBoneValues {
        I00,
        I04,
        BoneName,
        F12,
        F16,
        F20,
        F24,
        F28,
        F32,
        F36,
        F40,
        F44;
    }
}

class BcsPartSet {
    public int race;
    public int gender;
    public int i46;
    public int i47;
    public float positionY;
    public float cameraY;
    public float trackingOffset;
    public float f60;
    public float collisionScale;
    public float f68;
    public float f72;

    public BcsPartSet() {}
    public BcsPartSet(BcsPartSet other) {
        this.race = other.race;
        this.gender = other.gender;
        this.i46 = other.i46;
        this.i47 = other.i47;
        this.positionY = other.positionY;
        this.cameraY = other.cameraY;
        this.trackingOffset = other.trackingOffset;
        this.f60 = other.f60;
        this.collisionScale = other.collisionScale;
        this.f68 = other.f68;
        this.f72 = other.f72;
    }
}

class BcsPart {
    public short model;
    public short model2;
    public short texture;
    public short shader;
    public long flags;
    public int hideFlags;
    public int hideMatFlags;
    public float f36;
    public float f40;
    public int i44;
    public int i48;
    public String charaCode = "";
    public String emdName = "";
    public String emmName = "";
    public String embName = "";
    public String eanName = "";

    public BcsPart() {}
    public BcsPart(BcsPart other) {
        this.model = other.model;
        this.model2 = other.model2;
        this.texture = other.texture;
        this.shader = other.shader;
        this.flags = other.flags;
        this.hideFlags = other.hideFlags;
        this.hideMatFlags = other.hideMatFlags;
        this.f36 = other.f36;
        this.f40 = other.f40;
        this.i44 = other.i44;
        this.i48 = other.i48;
        this.charaCode = other.charaCode;
        this.emdName = other.emdName;
        this.emmName = other.emmName;
        this.embName = other.embName;
        this.eanName = other.eanName;
    }
}

class BcsColorSelector {
    int partColorGroup;
    int colorIndex;

    public BcsColorSelector() {}
    public BcsColorSelector(BcsColorSelector other) {
        this.partColorGroup = other.partColorGroup;
        this.colorIndex = other.colorIndex;
    }
}

class BcsPhysics {
    short model;
    short model2;
    short texture;
    long flags;
    int hideFlags;
    int hideMatFlags;
    String charaCode = "";
    String emdName = "";
    String emmName = "";
    String embName = "";
    String eskName = "";
    String boneToAttach = "";
    String scdName = "";

    public BcsPhysics() {}
    public BcsPhysics(BcsPhysics other) {
        this.model = other.model;
        this.model2 = other.model2;
        this.texture = other.texture;
        this.flags = other.flags;
        this.hideFlags = other.hideFlags;
        this.hideMatFlags = other.hideMatFlags;
        this.charaCode = other.charaCode;
        this.emdName = other.emdName;
        this.emmName = other.emmName;
        this.embName = other.embName;
        this.eskName = other.eskName;
        this.boneToAttach = other.boneToAttach;
        this.scdName = other.scdName;
    }
}

class BcsUnknown3 {
    short i00;
    short i02;
    short i04;
    short i06;
    short i08;
    short i10;

    public BcsUnknown3() {}
    public BcsUnknown3(BcsUnknown3 other) {
        this.i00 = other.i00;
        this.i02 = other.i02;
        this.i04 = other.i04;
        this.i06 = other.i06;
        this.i08 = other.i08;
        this.i10 = other.i10;
    }
}

class BcsPartColor {
    String name = "";

    BcsPartColor() {}
    BcsPartColor(BcsPartColor other) {
        this.name = other.name;
        
    }
}

class BcsColor {
    Color color1;
    Color color2;
    Color color3;
    Color color4;

    BcsColor() {}
    BcsColor(BcsColor other) {
        this.color1 = other.color1;
        this.color2 = other.color2;
        this.color3 = other.color3;
        this.color4 = other.color4;
    }
}

class BcsBoneScale {
    float scaleX;
    float scaleY;
    float scaleZ;
    String boneName;

    BcsBoneScale() {}
    BcsBoneScale(BcsBoneScale other) {
        this.scaleX = other.scaleX;
        this.scaleY = other.scaleY;
        this.scaleZ = other.scaleZ;
        this.boneName = other.boneName;
    }
}

class BcsSkeleton {
    short i00;
    
    BcsSkeleton() {}
    BcsSkeleton(BcsSkeleton other) {
        this.i00 = other.i00;
    }
}

class BcsBone {
    int i00;
    int i04;
    String boneName;
    float f12;
    float f16;
    float f20;
    float f24;
    float f28;
    float f32;
    float f36;
    float f40;
    float f44;

    BcsBone() {}
    BcsBone(BcsBone other) {
        this.i00 = other.i00;
        this.i04 = other.i04;
        this.boneName = other.boneName;
        this.f12 = other.f12;
        this.f16 = other.f16;
        this.f20 = other.f20;
        this.f24 = other.f24;
        this.f28 = other.f28;
        this.f32 = other.f32;
        this.f36 = other.f36;
        this.f40 = other.f40;
        this.f44 = other.f44;
    }
}