package xv2;
import static xv2.BinaryUtilities.toUByte;
import static xv2.BinaryUtilities.toUShort;
import static xv2.BinaryUtilities.toUint32;
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
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
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

        return new VBox(createHBox(bcsPartSet), mainTabPane);
    }

    private HBox createHBox(BcsPartSet bcsMainEntry) {
        ComboBox<String> genderComboBox = new ComboBox<>();
        genderComboBox.getItems().addAll("Male", "Female");
        genderComboBox.getSelectionModel().selectedIndexProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                bcsMainEntry.gender = newValue.intValue();
            }
        });
        switch (bcsMainEntry.gender) {
            case 1 -> {
                genderComboBox.getSelectionModel().select(1);
            }
            default -> {
                genderComboBox.getSelectionModel().select(0);
            }
        }

        ComboBox<String> raceComboBox = new ComboBox<>();
        raceComboBox.getItems().addAll("Human", "Saiyan", "Namekian", "Frieza Race", "Majin", "Other");
        raceComboBox.getSelectionModel().selectedIndexProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                bcsMainEntry.race = newValue.intValue();
            }
        });
        switch (bcsMainEntry.race) {
            case 1 -> {
                raceComboBox.getSelectionModel().select(1);
            }
            case 2 -> {
                raceComboBox.getSelectionModel().select(2);
            }
            case 3 -> {
                raceComboBox.getSelectionModel().select(3);
            }
            case 4 -> {
                raceComboBox.getSelectionModel().select(4);
            }
            case 5 -> {
                raceComboBox.getSelectionModel().select(5);
            }
            default -> {
                raceComboBox.getSelectionModel().select(0);
            }
        }

        HBox hBox = new HBox(15, genderComboBox, raceComboBox);
        hBox.setPadding(new Insets(10, 0, 10, 16));
        
        return hBox;
    }

    private void createPart(BcsPart entry) {
        //model
        Label modelLabel = new Label("Model");
        modelLabel.setPrefWidth(100);

        Spinner<Integer> modelSpinner = new Spinner<>(Short.MIN_VALUE, Short.MAX_VALUE, entry.model);
        modelSpinner.setEditable(true);
        modelSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                entry.model = newValue.byteValue();
            }
        });

        HBox modelHBox = new HBox(5, modelLabel, modelSpinner);
        modelLabel.setAlignment(Pos.CENTER_LEFT);
        //model

        //model2
        Label model2Label = new Label("Model 2");
        model2Label.setPrefWidth(100);

        Spinner<Integer> model2Spinner = new Spinner<>(Short.MIN_VALUE, Short.MAX_VALUE, entry.model2);
        model2Spinner.setEditable(true);
        model2Spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                entry.model2 = newValue.byteValue();
            }
        });

        HBox model2HBox = new HBox(5, model2Label, model2Spinner);
        model2Label.setAlignment(Pos.CENTER_LEFT);
        //model2

        //texture
        Label textureLabel = new Label("Texture");
        textureLabel.setPrefWidth(100);

        Spinner<Integer> textureSpinner = new Spinner<>(Short.MIN_VALUE, Short.MAX_VALUE, entry.texture);
        textureSpinner.setEditable(true);
        textureSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                entry.texture = newValue.byteValue();
            }
        });

        HBox textureHBox = new HBox(5, textureLabel, textureSpinner);
        textureLabel.setAlignment(Pos.CENTER_LEFT);
        //texture

        //shader
        Label shaderLabel = new Label("Shader");
        shaderLabel.setPrefWidth(100);

        Spinner<Integer> shaderSpinner = new Spinner<>(Short.MIN_VALUE, Short.MAX_VALUE, entry.shader);
        shaderSpinner.setEditable(true);
        shaderSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                entry.shader = newValue.byteValue();
            }
        });

        HBox shaderHBox = new HBox(5, shaderLabel, shaderSpinner);
        shaderLabel.setAlignment(Pos.CENTER_LEFT);
        //shader

        //dytOptions
        Label dytOptionsLabel = new Label("DYT Options");
        dytOptionsLabel.setPrefWidth(100);

        ToggleGroup dytOptionsToggleGroup = new ToggleGroup();

        ToggleButton standard = new ToggleButton("Standard");
        standard.setPrefWidth(150);
        standard.setToggleGroup(dytOptionsToggleGroup);

        ToggleButton seeminglyNothing = new ToggleButton("Seemingly Nothing");
        seeminglyNothing.setPrefWidth(150);
        seeminglyNothing.setToggleGroup(dytOptionsToggleGroup);

        ToggleButton model2Dyt = new ToggleButton("Model 2 DYT");
        model2Dyt.setPrefWidth(150);
        model2Dyt.setToggleGroup(dytOptionsToggleGroup);

        ToggleButton accessories = new ToggleButton("Accessories");
        accessories.setPrefWidth(150);
        accessories.setToggleGroup(dytOptionsToggleGroup);

        ToggleButton greenScouterOverlay = new ToggleButton("Green Scouter Overlay");
        greenScouterOverlay.setPrefWidth(150);
        greenScouterOverlay.setToggleGroup(dytOptionsToggleGroup);

        ToggleButton redScouterOverlay = new ToggleButton("Red Scouter Overlay");
        redScouterOverlay.setPrefWidth(150);
        redScouterOverlay.setToggleGroup(dytOptionsToggleGroup);

        ToggleButton blueScouterOverlay = new ToggleButton("Blue Scouter Overlay");
        blueScouterOverlay.setPrefWidth(150);
        blueScouterOverlay.setToggleGroup(dytOptionsToggleGroup);

        ToggleButton purpleScouterOverlay = new ToggleButton("Purple Scouter Overlay");
        purpleScouterOverlay.setPrefWidth(150);
        purpleScouterOverlay.setToggleGroup(dytOptionsToggleGroup);

        ToggleButton unknown8 = new ToggleButton("Unknown 8");
        unknown8.setPrefWidth(150);
        unknown8.setToggleGroup(dytOptionsToggleGroup);

        ToggleButton unknown9 = new ToggleButton("Unknown 9");
        unknown9.setPrefWidth(150);
        unknown9.setToggleGroup(dytOptionsToggleGroup);

        ToggleButton orangeScouterOverlay = new ToggleButton("Orange Scouter Overlay");
        orangeScouterOverlay.setPrefWidth(150);
        orangeScouterOverlay.setToggleGroup(dytOptionsToggleGroup);

        switch ((int) entry.flags) {
            case 1 -> seeminglyNothing.setSelected(true);
            case 2 -> model2Dyt.setSelected(true);
            case 4 -> accessories.setSelected(true);
            case 8 -> greenScouterOverlay.setSelected(true);
            case 16 -> redScouterOverlay.setSelected(true);
            case 32 -> blueScouterOverlay.setSelected(true);
            case 64 -> purpleScouterOverlay.setSelected(true);
            case 128 -> unknown8.setSelected(true);
            case 256 -> unknown9.setSelected(true);
            case 512 -> orangeScouterOverlay.setSelected(true);
            default -> standard.setSelected(true);
        }

        dytOptionsToggleGroup.selectedToggleProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue.isSelected()) {
                if ((ToggleButton) newValue == standard) { 
                    entry.flags = 0;
                }
                else if ((ToggleButton) newValue == seeminglyNothing) { 
                    entry.flags = 1;
                }
                else if ((ToggleButton) newValue == model2Dyt) { 
                    entry.flags = 2;
                }
                else if ((ToggleButton) newValue == accessories) { 
                    entry.flags = 4;
                }
                else if ((ToggleButton) newValue == greenScouterOverlay) { 
                    entry.flags = 8;
                }
                else if ((ToggleButton) newValue == redScouterOverlay) { 
                    entry.flags = 16;
                }
                else if ((ToggleButton) newValue == blueScouterOverlay) { 
                    entry.flags = 32;
                }
                else if ((ToggleButton) newValue == purpleScouterOverlay) { 
                    entry.flags = 64;
                }
                else if ((ToggleButton) newValue == unknown8) {
                    entry.flags = 128;
                }
                else if ((ToggleButton) newValue == unknown9) {
                    entry.flags = 256;
                }
                else if ((ToggleButton) newValue == orangeScouterOverlay) {
                    entry.flags = 512;
                }
            }
        });

        GridPane dytOptionsGridPane = new GridPane(10, 10);
        dytOptionsGridPane.getStyleClass().add("titled-address-box");
        dytOptionsGridPane.add(standard, 0, 0);   
        dytOptionsGridPane.add(seeminglyNothing, 1, 0);          
        dytOptionsGridPane.add(model2Dyt, 2, 0);          
        dytOptionsGridPane.add(accessories, 0, 1);          
        dytOptionsGridPane.add(greenScouterOverlay, 1, 1);          
        dytOptionsGridPane.add(redScouterOverlay, 2, 1);          
        dytOptionsGridPane.add(blueScouterOverlay, 0, 2);          
        dytOptionsGridPane.add(purpleScouterOverlay, 1, 2);          
        dytOptionsGridPane.add(unknown8, 2, 2);  
        dytOptionsGridPane.add(unknown9, 0, 3); 
        dytOptionsGridPane.add(orangeScouterOverlay, 1, 3);        

        HBox dytOptionsHBox=new HBox(5, dytOptionsLabel, dytOptionsGridPane);
        dytOptionsHBox.setAlignment(Pos.CENTER_LEFT);
        //dytOptions

        //partHiding
        Label partHidingLabel = new Label("Part Hiding");
        partHidingLabel.setPrefWidth(100);

        //box1
        CheckBox faceBase = new CheckBox("Face Base");
        CheckBox faceForehead = new CheckBox("Face Forehead");
        CheckBox faceEye = new CheckBox("Face Eye");
        CheckBox faceNose = new CheckBox("Face Nose");

        faceBase.setSelected((entry.hideFlags & 1) != 0);       
        faceForehead.setSelected((entry.hideFlags & 2) != 0);              
        faceEye.setSelected((entry.hideFlags & 4) != 0);  
        faceNose.setSelected((entry.hideFlags & 8) != 0);

        faceBase.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.hideFlags |= 1;
            } else {
                entry.hideFlags &= ~1;
            }
        });
        faceForehead.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.hideFlags |= 2;
            } else {
                entry.hideFlags &= ~2;
            }
        });
        faceEye.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.hideFlags |= 4;
            } else {
                entry.hideFlags &= ~4;
            }
        });
        faceNose.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.hideFlags |= 8;
            } else {
                entry.hideFlags &= ~8;
            }
        });

        VBox box1 = new VBox(2, faceBase, faceForehead, faceEye, faceNose);

        VBox borderContainerBox1 = new VBox(box1);
        borderContainerBox1.getStyleClass().add("titled-address-box");
        borderContainerBox1.setPadding(new Insets(12, 0, 0, 0));

        StackPane box1StackPane = new StackPane(borderContainerBox1);
        //box1

        //box2
        CheckBox faceEar = new CheckBox("Face Ear");
        CheckBox hair = new CheckBox("Hair");
        CheckBox bust = new CheckBox("Bust");
        CheckBox pants = new CheckBox("Pants");

        faceEar.setSelected((entry.hideFlags & 16L) != 0);   
        hair.setSelected((entry.hideFlags & 32L) != 0);      
        bust.setSelected((entry.hideFlags & 64L) != 0);       
        pants.setSelected((entry.hideFlags & 128L) != 0);

        faceEar.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.hideFlags |= 16;
            } else {
                entry.hideFlags &= ~16;
            }
        });
        hair.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.hideFlags |= 32;
            } else {
                entry.hideFlags &= ~32;
            }
        });
        bust.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.hideFlags |= 64;
            } else {
                entry.hideFlags &= ~64;
            }
        });
        pants.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.hideFlags |= 128;
            } else {
                entry.hideFlags &= ~128;
            }
        });

        VBox box2 = new VBox(2, faceEar, hair, bust, pants);

        VBox borderContainerBox2 = new VBox(box2);
        borderContainerBox2.getStyleClass().add("titled-address-box");
        borderContainerBox2.setPadding(new Insets(12, 0, 0, 0));

        StackPane box2StackPane = new StackPane(borderContainerBox2);
        //box2

        //box3
        CheckBox rist = new CheckBox("Rist");
        CheckBox boots = new CheckBox("Boots");

        rist.setSelected((entry.hideFlags & 256L) != 0);   
        boots.setSelected((entry.hideFlags & 512L) != 0); 

        rist.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.hideFlags |= 256;
            } else {
                entry.hideFlags &= ~256;
            }
        });
        boots.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.hideFlags |= 512;
            } else {
                entry.hideFlags &= 512;
            }
        });

        VBox box3 = new VBox(2, rist, boots);

        VBox borderContainerBox3 = new VBox(box3);
        borderContainerBox3.getStyleClass().add("titled-address-box");
        borderContainerBox3.setPadding(new Insets(12, 0, 0, 0));

        StackPane box3StackPane = new StackPane(borderContainerBox3);
        //box3

        HBox partHidingHBox = new HBox(5, partHidingLabel ,box1StackPane, box2StackPane, box3StackPane);
        partHidingHBox.setAlignment(Pos.CENTER_LEFT);
        //partHiding

        //matHiding
        Label matHidingLabel = new Label("Mat Hiding");
        matHidingLabel.setPrefWidth(100);

        //box1
        CheckBox faceBaseMat = new CheckBox("Face Base");
        CheckBox faceForeheadMat = new CheckBox("Face Forehead");
        CheckBox faceEyeMat = new CheckBox("Face Eye");
        CheckBox faceNoseMat = new CheckBox("Face Nose");

        faceBaseMat.setSelected((entry.hideMatFlags & 1) != 0);       
        faceForeheadMat.setSelected((entry.hideMatFlags & 2) != 0);              
        faceEyeMat.setSelected((entry.hideMatFlags & 4) != 0);  
        faceNoseMat.setSelected((entry.hideMatFlags & 8) != 0);

        faceBaseMat.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.hideMatFlags |= 1;
            } else {
                entry.hideMatFlags &= ~1;
            }
        });
        faceForeheadMat.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.hideMatFlags |= 2;
            } else {
                entry.hideMatFlags &= ~2;
            }
        });
        faceEyeMat.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.hideMatFlags |= 4;
            } else {
                entry.hideMatFlags &= ~4;
            }
        });
        faceNoseMat.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.hideMatFlags |= 8;
            } else {
                entry.hideMatFlags &= ~8;
            }
        });

        VBox box1Mat = new VBox(2, faceBaseMat, faceForeheadMat, faceEyeMat, faceNoseMat);

        VBox borderContainerBox1Mat = new VBox(box1Mat);
        borderContainerBox1Mat.getStyleClass().add("titled-address-box");
        borderContainerBox1Mat.setPadding(new Insets(12, 0, 0, 0));

        StackPane box1StackPaneMat = new StackPane(borderContainerBox1Mat);
        //box1

        //box2
        CheckBox faceEarMat = new CheckBox("Face Ear");
        CheckBox hairMat = new CheckBox("Hair");
        CheckBox bustMat = new CheckBox("Bust");
        CheckBox pantsMat = new CheckBox("Pants");

        faceEarMat.setSelected((entry.hideMatFlags & 16L) != 0);   
        hairMat.setSelected((entry.hideMatFlags & 32L) != 0);      
        bustMat.setSelected((entry.hideMatFlags & 64L) != 0);       
        pantsMat.setSelected((entry.hideMatFlags & 128L) != 0);

        faceEarMat.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.hideMatFlags |= 16;
            } else {
                entry.hideMatFlags &= ~16;
            }
        });
        hairMat.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.hideMatFlags |= 32;
            } else {
                entry.hideMatFlags &= ~32;
            }
        });
        bustMat.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.hideMatFlags |= 64;
            } else {
                entry.hideMatFlags &= ~64;
            }
        });
        pantsMat.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.hideMatFlags |= 128;
            } else {
                entry.hideMatFlags &= ~128;
            }
        });

        VBox box2Mat = new VBox(2, faceEarMat, hairMat, bustMat, pantsMat);

        VBox borderContainerBox2Mat = new VBox(box2Mat);
        borderContainerBox2Mat.getStyleClass().add("titled-address-box");
        borderContainerBox2Mat.setPadding(new Insets(12, 0, 0, 0));

        StackPane box2StackPaneMat = new StackPane(borderContainerBox2Mat);
        //box2

        //box3
        CheckBox ristMat = new CheckBox("Rist");
        CheckBox bootsMat = new CheckBox("Boots");

        ristMat.setSelected((entry.hideMatFlags & 256L) != 0);   
        bootsMat.setSelected((entry.hideMatFlags & 512L) != 0);

        ristMat.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.hideMatFlags |= 256;
            } else {
                entry.hideMatFlags &= ~256;
            }
        });
        bootsMat.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.hideMatFlags |= 512;
            } else {
                entry.hideMatFlags &= 512;
            }
        });

        VBox box3Mat = new VBox(2, ristMat, bootsMat);

        VBox borderContainerBox3Mat = new VBox(box3Mat);
        borderContainerBox3Mat.getStyleClass().add("titled-address-box");
        borderContainerBox3Mat.setPadding(new Insets(12, 0, 0, 0));

        StackPane box3StackPaneMat = new StackPane(borderContainerBox3Mat);
        //box3

        HBox matHidingHBox = new HBox(5, matHidingLabel ,box1StackPaneMat, box2StackPaneMat, box3StackPaneMat);
        matHidingHBox.setAlignment(Pos.CENTER_LEFT);
        //matHiding

        //f36
        Label f36Label = new Label("F_36");
        f36Label.setPrefWidth(60);
        
        TextField f36TextField = new TextField(String.valueOf(entry.f36));
        f36TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (f36TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.f36 = Float.parseFloat(newText); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox f36HBox = new HBox(f36Label, f36TextField);
        f36HBox.setAlignment(Pos.CENTER_LEFT);
        //f36

        //f40
        Label f40Label = new Label("F_40");
        f40Label.setPrefWidth(60);
        
        TextField f40TextField = new TextField(String.valueOf(entry.f40));
        f40TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (f40TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.f40 = Float.parseFloat(newText); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox f40HBox = new HBox(f40Label, f40TextField);
        f40HBox.setAlignment(Pos.CENTER_LEFT);
        //f40

        //i44
        Label i44Label = new Label("I_44");
        i44Label.setPrefWidth(60);

        TextField i44TextField = new TextField(String.valueOf(entry.i44));
        i44TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (i44TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.i44 = Integer.parseInt(newText); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox i44HBox = new HBox(i44Label, i44TextField);
        i44HBox.setAlignment(Pos.CENTER_LEFT);
        //i44

        //i48
        Label i48Label = new Label("I_48");
        i48Label.setPrefWidth(60);

        TextField i48TextField = new TextField(String.valueOf(entry.i48));
        i48TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (i48TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.i48 = Integer.parseInt(newText); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox i48HBox=new HBox(i48Label, i48TextField);
        i48HBox.setAlignment(Pos.CENTER_LEFT);
        //i48

        //charaCode
        Label charaCodeLabel = new Label("Chara Code");
        charaCodeLabel.setPrefWidth(100);

        TextField charaCodeTextField = new TextField(entry.charaCode);
        charaCodeTextField.textProperty().addListener((obs, oldText, newText) -> {
            if (charaCodeTextField.getText().contains("-")) {
                return;
            }
            try {
                entry.charaCode = newText; 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox charaCodeHBox = new HBox(5, charaCodeLabel, charaCodeTextField);
        charaCodeHBox.setAlignment(Pos.CENTER_LEFT);
        //charaCode

        //emdName
        Label emdNameLabel = new Label("EMD Name");
        emdNameLabel.setPrefWidth(100);

        TextField emdNameTextField = new TextField(entry.emdName);
        emdNameTextField.textProperty().addListener((obs, oldText, newText) -> {
            if (emdNameTextField.getText().contains("-")) {
                return;
            }
            try {
                entry.emdName = newText; 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox emdNameHBox = new HBox(5, emdNameLabel, emdNameTextField);
        emdNameHBox.setAlignment(Pos.CENTER_LEFT);
        //emdName

        //emmName
        Label emmNameLabel = new Label("EMM Name");
        emmNameLabel.setPrefWidth(100);

        TextField emmNameTextField = new TextField(entry.emmName);
        emmNameTextField.textProperty().addListener((obs, oldText, newText) -> {
            if (emmNameTextField.getText().contains("-")) {
                return;
            }
            try {
                entry.emmName = newText; 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox emmNameHBox = new HBox(5, emmNameLabel, emmNameTextField);
        emmNameHBox.setAlignment(Pos.CENTER_LEFT);
        //emmName

        //embName
        Label embNameLabel = new Label("EMB Name");
        embNameLabel.setPrefWidth(100);

        TextField embNameTextField = new TextField(entry.embName);
        embNameTextField.textProperty().addListener((obs, oldText, newText) -> {
            if (embNameTextField.getText().contains("-")) {
                return;
            }
            try {
                entry.embName = newText; 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox embNameHBox = new HBox(5, embNameLabel, embNameTextField);
        embNameHBox.setAlignment(Pos.CENTER_LEFT);
        //embName

        //eanName
        Label eanNameLabel = new Label("EAN Name");
        eanNameLabel.setPrefWidth(100);

        TextField eanNameTextField = new TextField(entry.eanName);
        eanNameTextField.textProperty().addListener((obs, oldText, newText) -> {
            if (eanNameTextField.getText().contains("-")) {
                return;
            }
            try {
                entry.eanName = newText; 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox eanNameHBox = new HBox(5, eanNameLabel, eanNameTextField);
        eanNameHBox.setAlignment(Pos.CENTER_LEFT);
        //eanName

        //part
        VBox partVBox = new VBox(25, 
            charaCodeHBox, modelHBox,
            model2HBox, textureHBox,
            shaderHBox, emdNameHBox,
            emmNameHBox, embNameHBox,
            eanNameHBox, dytOptionsHBox,
            partHidingHBox, matHidingHBox
        );
        partVBox.setPadding(new Insets(20, 0, 20, 16));

        Tab partTab = new Tab("Part", new ScrollPane(partVBox));
        partTab.setClosable(false);
        //part

        //unknown
        VBox unknownVBox = new VBox(25, 
            f36HBox, f40HBox,
            i44HBox, i48HBox
        );
        unknownVBox.setPadding(new Insets(20, 0, 0, 16));

        Tab unknownTab = new Tab("Unknown", unknownVBox);
        unknownTab.setClosable(false);
        //unknown

        dynamicTabPane.getTabs().addAll(partTab, unknownTab);
    }

    private void createColorSelector(BcsColorSelector entry) {
        //partColors
        Label partColorsLabel = new Label("Part Colors");
        partColorsLabel.setPrefWidth(80);

        ComboBox<String> partColorsComboBox = new ComboBox<>(partColorsObservableList);
        partColorsComboBox.getSelectionModel().select(entry.partColorGroup);
        partColorsComboBox.setPrefWidth(120);
        partColorsComboBox.getSelectionModel().selectedIndexProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                entry.partColorGroup = newValue.intValue();
            }
        });

        HBox partColorsHBox = new HBox(partColorsLabel, partColorsComboBox);
        partColorsHBox.setAlignment(Pos.CENTER_LEFT);
        //partColors

        //color
        Label colorLabel = new Label("Color");
        colorLabel.setPrefWidth(80);

        ComboBox<String> colorsComboBox = new ComboBox<>(colorsObservableList.get(0));
        colorsComboBox.getSelectionModel().select(entry.colorIndex);
        colorsComboBox.setPrefWidth(120);
        colorsComboBox.getSelectionModel().selectedIndexProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                entry.colorIndex = newValue.intValue();
            }
        });

        HBox colorsHBox = new HBox(colorLabel, colorsComboBox);
        colorsHBox.setAlignment(Pos.CENTER_LEFT);

        VBox colorSelectorVBox = new VBox(25, partColorsHBox, colorsHBox);
        colorSelectorVBox.setPadding(new Insets(20, 0, 0, 16));

        Tab colorSelectorTab = new Tab("Color Selector", colorSelectorVBox);
        colorSelectorTab.setClosable(false);

        dynamicTabPane.getTabs().add(colorSelectorTab);
    }

    private void createPhysics(BcsPhysics entry) {
        //model1
        Label modelLabel = new Label("Model");
        modelLabel.setPrefWidth(100);

        Spinner<Integer> model1Spinner = new Spinner<>(Short.MIN_VALUE, Short.MAX_VALUE, entry.model1);
        model1Spinner.setEditable(true);
        model1Spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                entry.model1 = newValue.byteValue();
            }
        });

        HBox model1HBox = new HBox(5, modelLabel, model1Spinner);
        modelLabel.setAlignment(Pos.CENTER_LEFT);
        //model1

        //model2
        Label model2Label = new Label("Model 2");
        model2Label.setPrefWidth(100);

        Spinner<Integer> model2Spinner = new Spinner<>(Short.MIN_VALUE, Short.MAX_VALUE, entry.model2);
        model2Spinner.setEditable(true);
        model2Spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                entry.model2 = newValue.byteValue();
            }
        });

        HBox model2HBox = new HBox(5, model2Label, model2Spinner);
        model2Label.setAlignment(Pos.CENTER_LEFT);
        //model2

        //texture
        Label textureLabel = new Label("Texture");
        textureLabel.setPrefWidth(100);

        Spinner<Integer> textureSpinner = new Spinner<>(Short.MIN_VALUE, Short.MAX_VALUE, entry.texture);
        textureSpinner.setEditable(true);
        textureSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                entry.texture = newValue.byteValue();
            }
        });

        HBox textureHBox = new HBox(5, textureLabel, textureSpinner);
        textureLabel.setAlignment(Pos.CENTER_LEFT);
        //texture

        //dytOptions
        Label dytOptionsLabel = new Label("DYT Options");
        dytOptionsLabel.setPrefWidth(100);

        ToggleGroup dytOptionsToggleGroup = new ToggleGroup();

        ToggleButton standard = new ToggleButton("Standard");
        standard.setPrefWidth(150);
        standard.setToggleGroup(dytOptionsToggleGroup);

        ToggleButton partDyt = new ToggleButton("Part DYT");
        partDyt.setPrefWidth(150);
        partDyt.setToggleGroup(dytOptionsToggleGroup);

        ToggleButton model2Dyt = new ToggleButton("Physics DYT");
        model2Dyt.setPrefWidth(150);
        model2Dyt.setToggleGroup(dytOptionsToggleGroup);

        ToggleButton accessories = new ToggleButton("Accessories");
        accessories.setPrefWidth(150);
        accessories.setToggleGroup(dytOptionsToggleGroup);

        ToggleButton greenScouterOverlay = new ToggleButton("Green Scouter Overlay");
        greenScouterOverlay.setPrefWidth(150);
        greenScouterOverlay.setToggleGroup(dytOptionsToggleGroup);

        ToggleButton redScouterOverlay = new ToggleButton("Red Scouter Overlay");
        redScouterOverlay.setPrefWidth(150);
        redScouterOverlay.setToggleGroup(dytOptionsToggleGroup);

        ToggleButton blueScouterOverlay = new ToggleButton("Blue Scouter Overlay");
        blueScouterOverlay.setPrefWidth(150);
        blueScouterOverlay.setToggleGroup(dytOptionsToggleGroup);

        ToggleButton purpleScouterOverlay = new ToggleButton("Purple Scouter Overlay");
        purpleScouterOverlay.setPrefWidth(150);
        purpleScouterOverlay.setToggleGroup(dytOptionsToggleGroup);

        ToggleButton unknown8 = new ToggleButton("Unknown 8");
        unknown8.setPrefWidth(150);
        unknown8.setToggleGroup(dytOptionsToggleGroup);

        ToggleButton unknown9 = new ToggleButton("Unknown 9");
        unknown9.setPrefWidth(150);
        unknown9.setToggleGroup(dytOptionsToggleGroup);

        ToggleButton orangeScouterOverlay = new ToggleButton("Orange Scouter Overlay");
        orangeScouterOverlay.setPrefWidth(150);
        orangeScouterOverlay.setToggleGroup(dytOptionsToggleGroup);

        switch ((int) entry.flags) {
            case 1 -> partDyt.setSelected(true);
            case 2 -> model2Dyt.setSelected(true);
            case 4 -> accessories.setSelected(true);
            case 8 -> greenScouterOverlay.setSelected(true);
            case 16 -> redScouterOverlay.setSelected(true);
            case 32 -> blueScouterOverlay.setSelected(true);
            case 64 -> purpleScouterOverlay.setSelected(true);
            case 128 -> unknown8.setSelected(true);
            case 256 -> unknown9.setSelected(true);
            case 512 -> orangeScouterOverlay.setSelected(true);
            default -> standard.setSelected(true);
        }

        dytOptionsToggleGroup.selectedToggleProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue.isSelected()) {
                if ((ToggleButton) newValue == standard) { 
                    entry.flags = 0;
                }
                else if ((ToggleButton) newValue == partDyt) { 
                    entry.flags = 1;
                }
                else if ((ToggleButton) newValue == model2Dyt) { 
                    entry.flags = 2;
                }
                else if ((ToggleButton) newValue == accessories) { 
                    entry.flags = 4;
                }
                else if ((ToggleButton) newValue == greenScouterOverlay) { 
                    entry.flags = 8;
                }
                else if ((ToggleButton) newValue == redScouterOverlay) { 
                    entry.flags = 16;
                }
                else if ((ToggleButton) newValue == blueScouterOverlay) { 
                    entry.flags = 32;
                }
                else if ((ToggleButton) newValue == purpleScouterOverlay) { 
                    entry.flags = 64;
                }
                else if ((ToggleButton) newValue == unknown8) {
                    entry.flags = 128;
                }
                else if ((ToggleButton) newValue == unknown9) {
                    entry.flags = 256;
                }
                else if ((ToggleButton) newValue == orangeScouterOverlay) {
                    entry.flags = 512;
                }
            }
        });

        GridPane dytOptionsGridPane = new GridPane(10, 10);
        dytOptionsGridPane.getStyleClass().add("titled-address-box");
        dytOptionsGridPane.add(standard, 0, 0);   
        dytOptionsGridPane.add(partDyt, 1, 0);          
        dytOptionsGridPane.add(model2Dyt, 2, 0);          
        dytOptionsGridPane.add(accessories, 0, 1);          
        dytOptionsGridPane.add(greenScouterOverlay, 1, 1);          
        dytOptionsGridPane.add(redScouterOverlay, 2, 1);          
        dytOptionsGridPane.add(blueScouterOverlay, 0, 2);          
        dytOptionsGridPane.add(purpleScouterOverlay, 1, 2);          
        dytOptionsGridPane.add(unknown8, 2, 2);  
        dytOptionsGridPane.add(unknown9, 0, 3); 
        dytOptionsGridPane.add(orangeScouterOverlay, 1, 3);        

        HBox dytOptionsHBox=new HBox(5, dytOptionsLabel, dytOptionsGridPane);
        dytOptionsHBox.setAlignment(Pos.CENTER_LEFT);
        //dytOptions

        //partHiding
        Label partHidingLabel = new Label("Part Hiding");
        partHidingLabel.setPrefWidth(100);

        //box1
        CheckBox faceBase = new CheckBox("Face Base");
        CheckBox faceForehead = new CheckBox("Face Forehead");
        CheckBox faceEye = new CheckBox("Face Eye");
        CheckBox faceNose = new CheckBox("Face Nose");

        faceBase.setSelected((entry.hideFlags & 1) != 0);       
        faceForehead.setSelected((entry.hideFlags & 2) != 0);              
        faceEye.setSelected((entry.hideFlags & 4) != 0);  
        faceNose.setSelected((entry.hideFlags & 8) != 0);

        faceBase.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.hideFlags |= 1;
            } else {
                entry.hideFlags &= ~1;
            }
        });
        faceForehead.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.hideFlags |= 2;
            } else {
                entry.hideFlags &= ~2;
            }
        });
        faceEye.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.hideFlags |= 4;
            } else {
                entry.hideFlags &= ~4;
            }
        });
        faceNose.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.hideFlags |= 8;
            } else {
                entry.hideFlags &= ~8;
            }
        });

        VBox box1 = new VBox(2, faceBase, faceForehead, faceEye, faceNose);

        VBox borderContainerBox1 = new VBox(box1);
        borderContainerBox1.getStyleClass().add("titled-address-box");
        borderContainerBox1.setPadding(new Insets(12, 0, 0, 0));

        StackPane box1StackPane = new StackPane(borderContainerBox1);
        //box1

        //box2
        CheckBox faceEar = new CheckBox("Face Ear");
        CheckBox hair = new CheckBox("Hair");
        CheckBox bust = new CheckBox("Bust");
        CheckBox pants = new CheckBox("Pants");

        faceEar.setSelected((entry.hideFlags & 16L) != 0);   
        hair.setSelected((entry.hideFlags & 32L) != 0);      
        bust.setSelected((entry.hideFlags & 64L) != 0);       
        pants.setSelected((entry.hideFlags & 128L) != 0);

        faceEar.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.hideFlags |= 16;
            } else {
                entry.hideFlags &= ~16;
            }
        });
        hair.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.hideFlags |= 32;
            } else {
                entry.hideFlags &= ~32;
            }
        });
        bust.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.hideFlags |= 64;
            } else {
                entry.hideFlags &= ~64;
            }
        });
        pants.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.hideFlags |= 128;
            } else {
                entry.hideFlags &= ~128;
            }
        });

        VBox box2 = new VBox(2, faceEar, hair, bust, pants);

        VBox borderContainerBox2 = new VBox(box2);
        borderContainerBox2.getStyleClass().add("titled-address-box");
        borderContainerBox2.setPadding(new Insets(12, 0, 0, 0));

        StackPane box2StackPane = new StackPane(borderContainerBox2);
        //box2

        //box3
        CheckBox rist = new CheckBox("Rist");
        CheckBox boots = new CheckBox("Boots");

        rist.setSelected((entry.hideFlags & 256L) != 0);   
        boots.setSelected((entry.hideFlags & 512L) != 0); 

        rist.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.hideFlags |= 256;
            } else {
                entry.hideFlags &= ~256;
            }
        });
        boots.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.hideFlags |= 512;
            } else {
                entry.hideFlags &= 512;
            }
        });

        VBox box3 = new VBox(2, rist, boots);

        VBox borderContainerBox3 = new VBox(box3);
        borderContainerBox3.getStyleClass().add("titled-address-box");
        borderContainerBox3.setPadding(new Insets(12, 0, 0, 0));

        StackPane box3StackPane = new StackPane(borderContainerBox3);
        //box3

        HBox partHidingHBox = new HBox(5, partHidingLabel ,box1StackPane, box2StackPane, box3StackPane);
        partHidingHBox.setAlignment(Pos.CENTER_LEFT);
        //partHiding

        //matHiding
        Label matHidingLabel = new Label("Mat Hiding");
        matHidingLabel.setPrefWidth(100);

        //box1
        CheckBox faceBaseMat = new CheckBox("Face Base");
        CheckBox faceForeheadMat = new CheckBox("Face Forehead");
        CheckBox faceEyeMat = new CheckBox("Face Eye");
        CheckBox faceNoseMat = new CheckBox("Face Nose");

        faceBaseMat.setSelected((entry.hideMatFlags & 1) != 0);       
        faceForeheadMat.setSelected((entry.hideMatFlags & 2) != 0);              
        faceEyeMat.setSelected((entry.hideMatFlags & 4) != 0);  
        faceNoseMat.setSelected((entry.hideMatFlags & 8) != 0);

        faceBaseMat.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.hideMatFlags |= 1;
            } else {
                entry.hideMatFlags &= ~1;
            }
        });
        faceForeheadMat.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.hideMatFlags |= 2;
            } else {
                entry.hideMatFlags &= ~2;
            }
        });
        faceEyeMat.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.hideMatFlags |= 4;
            } else {
                entry.hideMatFlags &= ~4;
            }
        });
        faceNoseMat.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.hideMatFlags |= 8;
            } else {
                entry.hideMatFlags &= ~8;
            }
        });

        VBox box1Mat = new VBox(2, faceBaseMat, faceForeheadMat, faceEyeMat, faceNoseMat);

        VBox borderContainerBox1Mat = new VBox(box1Mat);
        borderContainerBox1Mat.getStyleClass().add("titled-address-box");
        borderContainerBox1Mat.setPadding(new Insets(12, 0, 0, 0));

        StackPane box1StackPaneMat = new StackPane(borderContainerBox1Mat);
        //box1

        //box2
        CheckBox faceEarMat = new CheckBox("Face Ear");
        CheckBox hairMat = new CheckBox("Hair");
        CheckBox bustMat = new CheckBox("Bust");
        CheckBox pantsMat = new CheckBox("Pants");

        faceEarMat.setSelected((entry.hideMatFlags & 16L) != 0);   
        hairMat.setSelected((entry.hideMatFlags & 32L) != 0);      
        bustMat.setSelected((entry.hideMatFlags & 64L) != 0);       
        pantsMat.setSelected((entry.hideMatFlags & 128L) != 0);

        faceEarMat.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.hideMatFlags |= 16;
            } else {
                entry.hideMatFlags &= ~16;
            }
        });
        hairMat.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.hideMatFlags |= 32;
            } else {
                entry.hideMatFlags &= ~32;
            }
        });
        bustMat.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.hideMatFlags |= 64;
            } else {
                entry.hideMatFlags &= ~64;
            }
        });
        pantsMat.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.hideMatFlags |= 128;
            } else {
                entry.hideMatFlags &= ~128;
            }
        });

        VBox box2Mat = new VBox(2, faceEarMat, hairMat, bustMat, pantsMat);

        VBox borderContainerBox2Mat = new VBox(box2Mat);
        borderContainerBox2Mat.getStyleClass().add("titled-address-box");
        borderContainerBox2Mat.setPadding(new Insets(12, 0, 0, 0));

        StackPane box2StackPaneMat = new StackPane(borderContainerBox2Mat);
        //box2

        //box3
        CheckBox ristMat = new CheckBox("Rist");
        CheckBox bootsMat = new CheckBox("Boots");

        ristMat.setSelected((entry.hideMatFlags & 256L) != 0);   
        bootsMat.setSelected((entry.hideMatFlags & 512L) != 0);

        ristMat.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.hideMatFlags |= 256;
            } else {
                entry.hideMatFlags &= ~256;
            }
        });
        bootsMat.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.hideMatFlags |= 512;
            } else {
                entry.hideMatFlags &= 512;
            }
        });

        VBox box3Mat = new VBox(2, ristMat, bootsMat);

        VBox borderContainerBox3Mat = new VBox(box3Mat);
        borderContainerBox3Mat.getStyleClass().add("titled-address-box");
        borderContainerBox3Mat.setPadding(new Insets(12, 0, 0, 0));

        StackPane box3StackPaneMat = new StackPane(borderContainerBox3Mat);
        //box3

        HBox matHidingHBox = new HBox(5, matHidingLabel ,box1StackPaneMat, box2StackPaneMat, box3StackPaneMat);
        matHidingHBox.setAlignment(Pos.CENTER_LEFT);
        //matHiding

        //charaCode
        Label charaCodeLabel = new Label("Chara Code");
        charaCodeLabel.setPrefWidth(100);

        TextField charaCodeTextField = new TextField(entry.charaCode);
        charaCodeTextField.textProperty().addListener((obs, oldText, newText) -> {
            if (charaCodeTextField.getText().contains("-")) {
                return;
            }
            try {
                entry.charaCode = newText; 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox charaCodeHBox = new HBox(5, charaCodeLabel, charaCodeTextField);
        charaCodeHBox.setAlignment(Pos.CENTER_LEFT);
        //charaCode

        //emdName
        Label emdNameLabel = new Label("EMD Name");
        emdNameLabel.setPrefWidth(100);

        TextField emdNameTextField = new TextField(entry.emdName);
        emdNameTextField.textProperty().addListener((obs, oldText, newText) -> {
            if (emdNameTextField.getText().contains("-")) {
                return;
            }
            try {
                entry.emdName = newText; 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox emdNameHBox = new HBox(5, emdNameLabel, emdNameTextField);
        emdNameHBox.setAlignment(Pos.CENTER_LEFT);
        //emdName

        //emmName
        Label emmNameLabel = new Label("EMM Name");
        emmNameLabel.setPrefWidth(100);

        TextField emmNameTextField = new TextField(entry.emmName);
        emmNameTextField.textProperty().addListener((obs, oldText, newText) -> {
            if (emmNameTextField.getText().contains("-")) {
                return;
            }
            try {
                entry.emmName = newText; 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox emmNameHBox = new HBox(5, emmNameLabel, emmNameTextField);
        emmNameHBox.setAlignment(Pos.CENTER_LEFT);
        //emmName

        //embName
        Label embNameLabel = new Label("EMB Name");
        embNameLabel.setPrefWidth(100);

        TextField embNameTextField = new TextField(entry.embName);
        embNameTextField.textProperty().addListener((obs, oldText, newText) -> {
            if (embNameTextField.getText().contains("-")) {
                return;
            }
            try {
                entry.embName = newText; 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox embNameHBox = new HBox(5, embNameLabel, embNameTextField);
        embNameHBox.setAlignment(Pos.CENTER_LEFT);
        //embName

        //eskName
        Label eskNameLabel = new Label("ESK Name");
        eskNameLabel.setPrefWidth(100);

        TextField eskNameTextField = new TextField(entry.eskName);
        eskNameTextField.textProperty().addListener((obs, oldText, newText) -> {
            if (eskNameTextField.getText().contains("-")) {
                return;
            }
            try {
                entry.eskName = newText; 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox eskNameHBox = new HBox(5, eskNameLabel, eskNameTextField);
        eskNameHBox.setAlignment(Pos.CENTER_LEFT);
        //eskName

        //boneName
        Label boneNameLabel = new Label("Bone Name");
        boneNameLabel.setPrefWidth(100);

        TextField boneNameTextField = new TextField(entry.boneToAttach);
        boneNameTextField.textProperty().addListener((obs, oldText, newText) -> {
            if (boneNameTextField.getText().contains("-")) {
                return;
            }
            try {
                entry.boneToAttach = newText;
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox boneNameHBox = new HBox(5, boneNameLabel, boneNameTextField);
        boneNameHBox.setAlignment(Pos.CENTER_LEFT);
        //boneName

        //scdName
        Label scdNameLabel = new Label("SCD Name");
        scdNameLabel.setPrefWidth(100);

        TextField scdNameTextField = new TextField(entry.scdName);
        scdNameTextField.textProperty().addListener((obs, oldText, newText) -> {
            if (scdNameTextField.getText().contains("-")) {
                return;
            }
            try {
                entry.scdName = newText;
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox scdNameHBox = new HBox(5, scdNameLabel, scdNameTextField);
        scdNameHBox.setAlignment(Pos.CENTER_LEFT);
        //scdName

        VBox physicsVBox = new VBox(25, 
            charaCodeHBox, model1HBox,
            model2HBox, textureHBox,
            emdNameHBox, emmNameHBox,
            embNameHBox, eskNameHBox,
            boneNameHBox, scdNameHBox,
            dytOptionsHBox, partHidingHBox,
            matHidingHBox
        );
        physicsVBox.setPadding(new Insets(20, 0, 20, 16));

        Tab physicsTab = new Tab("Physics", new ScrollPane(physicsVBox));
        physicsTab.setClosable(false);

        dynamicTabPane.getTabs().add(physicsTab);
    }

    private void createUnknown3(BcsUnknown3 entry) {
        //i00
        Label i00Label=new Label("I_00");
        i00Label.setPrefWidth(60);

        TextField i00TextField = new TextField(String.valueOf(entry.i00));
        i00TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (i00TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.i00 = Short.parseShort(newText);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });
        
        HBox i00HBox = new HBox(i00Label, i00TextField);
        i00HBox.setAlignment(Pos.CENTER_LEFT);
        //i00

        //i02
        Label i02Label = new Label("I_02");
        i02Label.setPrefWidth(60);

        TextField i02TextField = new TextField(String.valueOf(entry.i02));
        i02TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (i02TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.i02 = Short.parseShort(newText); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox i02HBox = new HBox(i02Label, i02TextField);
        i02HBox.setAlignment(Pos.CENTER_LEFT);
        //i02

        //i04
        Label i04Label = new Label("I_04");
        i04Label.setPrefWidth(60);

        TextField i04TextField = new TextField(String.valueOf(entry.i04));
        i04TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (i04TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.i04 = Short.parseShort(newText);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox i04HBox = new HBox(i04Label, i04TextField);
        i04HBox.setAlignment(Pos.CENTER_LEFT);
        //i04

        //i06
        Label i06Label = new Label("I_06");
        i06Label.setPrefWidth(60);

        TextField i06TextField = new TextField(String.valueOf(entry.i06));
        i06TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (i06TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.i06 = Short.parseShort(newText);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox i06HBox = new HBox(i06Label, i06TextField);
        i06HBox.setAlignment(Pos.CENTER_LEFT);
        //i06

        //i08
        Label i08Label = new Label("I_08");
        i08Label.setPrefWidth(60);

        TextField i08TextField = new TextField(String.valueOf(entry.i08));
        i08TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (i08TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.i08 = Short.parseShort(newText); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox i08HBox = new HBox(i08Label, i08TextField);
        i08HBox.setAlignment(Pos.CENTER_LEFT);
        //i08

        //i10
        Label i10Label = new Label("I_10");
        i10Label.setPrefWidth(60);

        TextField i10TextField = new TextField(String.valueOf(entry.i10));
        i10TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (i10TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.i10 = Short.parseShort(newText);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox i10HBox = new HBox(i10Label, i10TextField);
        i10HBox.setAlignment(Pos.CENTER_LEFT);
        //i10

        VBox unknownVBox = new VBox(25,
            i00HBox, i02HBox,
            i04HBox, i06HBox,
            i08HBox, i10HBox
        );
        unknownVBox.setPadding(new Insets(20, 0, 0, 16));

        Tab unknownTab = new Tab("Unknown", unknownVBox);
        unknownTab.setClosable(false);

        dynamicTabPane.getTabs().add(unknownTab);
    }

    public void createPartColor(BcsPartColor entry) {
        //name
        Label nameLabel = new Label("Name");
        nameLabel.setPrefWidth(60);

        TextField nameTextField = new TextField(entry.name);
        nameTextField.textProperty().addListener((obs, oldText, newText) -> {
            if (nameTextField.getText().contains("-")) {
                return;
            }
            try {
                entry.name = newText; 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox nameHBox = new HBox(nameLabel, nameTextField);
        nameHBox.setAlignment(Pos.BASELINE_LEFT);
        nameHBox.setPadding(new Insets(20, 0, 0, 16));
        //name

        Tab partColorTab = new Tab("Part Color", nameHBox);
        partColorTab.setClosable(false);

        dynamicTabPane.getTabs().add(partColorTab);
    }

    public void createColor(BcsColor entry) {
        //color1
        Label color1Label = new Label("Color 1");
        color1Label.setPrefWidth(60);

        ColorPicker colorPicker1 = new ColorPicker(entry.color1);
        colorPicker1.setOnAction(e -> {
            entry.color1 = colorPicker1.getValue();
        });

        HBox color1HBox = new HBox(color1Label, colorPicker1);
        color1HBox.setAlignment(Pos.CENTER_LEFT);
        //color1

        //color2
        Label color2Label = new Label("Color 2");
        color2Label.setPrefWidth(60);

        ColorPicker colorPicker2 = new ColorPicker(entry.color2);
        colorPicker2.setOnAction(e -> {
            entry.color2 = colorPicker2.getValue();
        });

        HBox color2HBox = new HBox(color2Label, colorPicker2);
        color2HBox.setAlignment(Pos.CENTER_LEFT);
        //color2

        //color3
        Label color3Label = new Label("Color 3");
        color3Label.setPrefWidth(60);

        ColorPicker colorPicker3 = new ColorPicker(entry.color3);
        colorPicker3.setOnAction(e -> {
            entry.color3 = colorPicker3.getValue();
        });

        HBox color3HBox = new HBox(color3Label, colorPicker3);
        color3HBox.setAlignment(Pos.CENTER_LEFT);
        //color3

        //color4
        Label color4Label = new Label("Color 4");
        color4Label.setPrefWidth(60);

        ColorPicker colorPicker4 = new ColorPicker(entry.color4);
        colorPicker4.setOnAction(e -> {
            entry.color4 = colorPicker4.getValue();
        });

        HBox color4HBox = new HBox(color4Label, colorPicker4);
        color4HBox.setAlignment(Pos.CENTER_LEFT);
        //color4

        VBox colorVBox = new VBox(25,
            color1HBox, color2HBox,
            color3HBox, color4HBox
        );
        colorVBox.setPadding(new Insets(20, 0, 0, 16));

        Tab colorTab = new Tab("Color", colorVBox);
        colorTab.setClosable(false);

        dynamicTabPane.getTabs().add(colorTab);
    }

    public void createBoneScale(BcsBoneScale entry) {
        //scaleX
        Label speedXLabel = new Label("Scale X");
        speedXLabel.setPrefWidth(80);
        
        Spinner <Double> scaleXSpinner = new Spinner<>(Float.MIN_VALUE, Float.MAX_VALUE, entry.scaleX);
        scaleXSpinner.setEditable(true);
        scaleXSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                entry.scaleX = newValue.floatValue();
            }
        });

        HBox scaleXHBox = new HBox(speedXLabel, scaleXSpinner);
        scaleXHBox.setAlignment(Pos.CENTER_LEFT);
        //scaleX

        //scaleY
        Label speedYLabel = new Label("Scale Y");
        speedYLabel.setPrefWidth(80);
        
        Spinner <Double> scaleYSpinner = new Spinner<>(Float.MIN_VALUE, Float.MAX_VALUE, entry.scaleY);
        scaleYSpinner.setEditable(true);
        scaleYSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                entry.scaleY = newValue.floatValue();
            }
        });

        HBox scaleYHBox = new HBox(speedYLabel, scaleYSpinner);
        scaleYHBox.setAlignment(Pos.CENTER_LEFT);
        //scaleY

        //scaleZ
        Label speedZLabel = new Label("Scale Z");
        speedZLabel.setPrefWidth(80);
        
        Spinner <Double> scaleZSpinner = new Spinner<>(Float.MIN_VALUE, Float.MAX_VALUE, entry.scaleZ);
        scaleZSpinner.setEditable(true);
        scaleZSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                entry.scaleZ = newValue.floatValue();
            }
        });

        HBox scaleZHBox = new HBox(speedZLabel, scaleZSpinner);
        scaleZHBox.setAlignment(Pos.CENTER_LEFT);
        //scaleZ

        //boneName
        Label boneNameLabel = new Label("Bone Name");
        boneNameLabel.setPrefWidth(80);

        TextField boneNameTextField = new TextField(entry.boneName);
        boneNameTextField.textProperty().addListener((obs, oldText, newText) -> {
            if (boneNameTextField.getText().contains("-")) {
                return;
            }
            try {
                entry.boneName = newText; 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox boneNameHBox = new HBox(boneNameLabel, boneNameTextField);
        boneNameHBox.setAlignment(Pos.CENTER_LEFT);
        //boneName

        VBox boneScaleVBox = new VBox(25, 
            boneNameHBox, scaleXHBox,
            scaleYHBox, scaleZHBox
        );
        boneScaleVBox.setPadding(new Insets(20, 0, 0, 16));

        Tab boneScaleTab = new Tab("Bone Scale", boneScaleVBox);
        boneScaleTab.setClosable(false);

        dynamicTabPane.getTabs().add(boneScaleTab);
    }

    private void createSkeleton(BcsSkeleton entry) {
        //i00
        Label i00Label=new Label("I_00");
        i00Label.setPrefWidth(60);

        TextField i00TextField = new TextField(String.valueOf(entry.i00));
        i00TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (i00TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.i00 = Short.parseShort(newText);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });
        
        HBox i00HBox = new HBox(i00Label, i00TextField);
        i00HBox.setPadding(new Insets(20, 0, 0, 16));
        i00HBox.setAlignment(Pos.BASELINE_LEFT);
        //i00

        Tab skeletonTab = new Tab("Skeleton", i00HBox);
        skeletonTab.setClosable(false);

        dynamicTabPane.getTabs().add(skeletonTab);
    }

    public void createBone(BcsBone entry) {
        //i00
        Label i00Label=new Label("I_00");
        i00Label.setPrefWidth(60);

        TextField i00TextField = new TextField(String.valueOf(entry.i00));
        i00TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (i00TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.i00 = Integer.parseInt(newText);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });
        
        HBox i00HBox = new HBox(i00Label, i00TextField);
        i00HBox.setAlignment(Pos.CENTER_LEFT);
        //i00

        //i04
        Label i04Label = new Label("I_04");
        i04Label.setPrefWidth(60);

        TextField i04TextField = new TextField(String.valueOf(entry.i04));
        i04TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (i04TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.i04 = Integer.parseInt(newText);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox i04HBox = new HBox(i04Label, i04TextField);
        i04HBox.setAlignment(Pos.CENTER_LEFT);
        //i04

        //f12
        Label f12Label = new Label("F_12");
        f12Label.setPrefWidth(60);

        TextField f12TextField = new TextField(String.valueOf(entry.f12));
        f12TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (f12TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.f12 = Float.parseFloat(newText); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox f12HBox = new HBox(f12Label, f12TextField);
        f12HBox.setAlignment(Pos.CENTER_LEFT);
        //f12

        //f16
        Label f16Label = new Label("F_16");
        f16Label.setPrefWidth(60);
        
        TextField f16TextField = new TextField(String.valueOf(entry.f16));
        f16TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (f16TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.f16 = Float.parseFloat(newText);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox f16HBox = new HBox(f16Label, f16TextField);
        f16HBox.setAlignment(Pos.CENTER_LEFT);
        //f16

        //f20
        Label f20Label = new Label("F_20");
        f20Label.setPrefWidth(60);

        TextField f20TextField = new TextField(String.valueOf(entry.f20));
        f20TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (f20TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.f20 = Float.parseFloat(newText); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox f20HBox = new HBox(f20Label ,f20TextField);
        f20HBox.setAlignment(Pos.CENTER_LEFT);
        //f20

        //f24
        Label f24Label = new Label("F_24");
        f24Label.setPrefWidth(60);

        TextField f24TextField = new TextField(String.valueOf(entry.f24));
        f24TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (f24TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.f24 = Float.parseFloat(newText); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox f24HBox = new HBox(f24Label ,f24TextField);
        f24HBox.setAlignment(Pos.CENTER_LEFT);
        //f24

        //f28
        Label f28Label = new Label("F_28");
        f28Label.setPrefWidth(60);
        
        TextField f28TextField = new TextField(String.valueOf(entry.f28));
        f28TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (f28TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.f28 = Float.parseFloat(newText); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox f28HBox = new HBox(f28Label, f28TextField);
        f28HBox.setAlignment(Pos.CENTER_LEFT);
        //f28

        //f32
        Label f32Label = new Label("F_32");
        f32Label.setPrefWidth(60);
        
        TextField f32TextField = new TextField(String.valueOf(entry.f32));
        f32TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (f32TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.f32 = Float.parseFloat(newText); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox f32HBox = new HBox(f32Label, f32TextField);
        f32HBox.setAlignment(Pos.CENTER_LEFT);
        //f32

        //f36
        Label f36Label = new Label("F_36");
        f36Label.setPrefWidth(60);
        
        TextField f36TextField = new TextField(String.valueOf(entry.f36));
        f36TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (f36TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.f36 = Float.parseFloat(newText); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox f36HBox = new HBox(f36Label, f36TextField);
        f36HBox.setAlignment(Pos.CENTER_LEFT);
        //f36

        //f40
        Label f40Label = new Label("F_40");
        f40Label.setPrefWidth(60);
        
        TextField f40TextField = new TextField(String.valueOf(entry.f40));
        f40TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (f40TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.f40 = Float.parseFloat(newText); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox f40HBox = new HBox(f40Label, f40TextField);
        f40HBox.setAlignment(Pos.CENTER_LEFT);
        //f40

        //f44
        Label f44Label = new Label("F_44");
        f44Label.setPrefWidth(60);
        
        TextField f44TextField = new TextField(String.valueOf(entry.f44));
        f44TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (f44TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.f44 = Float.parseFloat(newText); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox f44HBox = new HBox(f44Label, f44TextField);
        f44HBox.setAlignment(Pos.CENTER_LEFT);
        //f44

        //boneName
        Label boneNameLabel = new Label("Bone Name");
        boneNameLabel.setPrefWidth(80);

        TextField boneNameTextField = new TextField(entry.boneName);
        boneNameTextField.textProperty().addListener((obs, oldText, newText) -> {
            if (boneNameTextField.getText().contains("-")) {
                return;
            }
            try {
                entry.boneName = newText;
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox boneNameHBox = new HBox(boneNameLabel, boneNameTextField);
        boneNameHBox.setPadding(new Insets(20, 0, 0, 16));
        boneNameHBox.setAlignment(Pos.BASELINE_LEFT);
        //name

        //bone
        Tab boneTab = new Tab("Bone", boneNameHBox);
        boneTab.setClosable(false);
        //bone

        //unknown
        VBox unknownVBox = new VBox(25, 
            i00HBox, i04HBox,
            f12HBox, f16HBox,
            f20HBox, f24HBox,
            f28HBox, f32HBox,
            f36HBox, f40HBox,
            f44HBox
        );
        unknownVBox.setPadding(new Insets(20, 0, 0, 16));

        Tab unknownTab = new Tab("Unknown", unknownVBox);
        unknownTab.setClosable(false);
        //unknown

        dynamicTabPane.getTabs().addAll(boneTab, unknownTab);
    }

    private VBox createUnknownVBox(BcsPartSet entry) {
        //i46
        Label i46Label = new Label("I_46");
        i46Label.setPrefWidth(60);

        TextField i46TextField = new TextField(String.valueOf(entry.i46));
        i46TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (i46TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.i46 = Integer.parseInt(newText);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox i46HBox = new HBox(i46Label, i46TextField);
        i46HBox.setAlignment(Pos.CENTER_LEFT);
        //i46

        //i47
        Label i47Label = new Label("I_47");
        i47Label.setPrefWidth(60);

        TextField i47TextField = new TextField(String.valueOf(entry.i47));
        i47TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (i47TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.i47 = Integer.parseInt(newText);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox i47HBox = new HBox(i47Label, i47TextField);
        i47HBox.setAlignment(Pos.CENTER_LEFT);
        //i47

        //f48
        Label f48Label = new Label("F_48");
        f48Label.setPrefWidth(60);
        
        TextField f48TextField = new TextField(String.valueOf(entry.f48));
        f48TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (f48TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.f48 = Float.parseFloat(newText); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox f48HBox = new HBox(f48Label, f48TextField);
        f48HBox.setAlignment(Pos.CENTER_LEFT);
        //f48

        //f52
        Label f52Label = new Label("F_52");
        f52Label.setPrefWidth(60);
        
        TextField f52TextField = new TextField(String.valueOf(entry.f52));
        f52TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (f52TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.f52 = Float.parseFloat(newText); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox f52HBox = new HBox(f52Label, f52TextField);
        f52HBox.setAlignment(Pos.CENTER_LEFT);
        //f52

        //f56
        Label f56Label = new Label("F_56");
        f56Label.setPrefWidth(60);

        TextField f56TextField = new TextField(String.valueOf(entry.f56));
        f56TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (f56TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.f56 = Float.parseFloat(newText);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox f56HBox = new HBox(f56Label, f56TextField);
        f56HBox.setAlignment(Pos.CENTER_LEFT);
        //f56

        //f60
        Label f60Label = new Label("F_60");
        f60Label.setPrefWidth(60);
        
        TextField f60TextField = new TextField(String.valueOf(entry.f60));
        f60TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (f60TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.f60 = Float.parseFloat(newText); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox f60HBox = new HBox(f60Label, f60TextField);
        f60HBox.setAlignment(Pos.CENTER_LEFT);
        //f60

        //f64
        Label f64Label = new Label("F_64");
        f64Label.setPrefWidth(60);

        TextField i64TextField = new TextField(String.valueOf(entry.f64));
        i64TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (i64TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.f64 = Float.parseFloat(newText);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox f64HBox = new HBox(f64Label, i64TextField);
        f64HBox.setAlignment(Pos.CENTER_LEFT);
        //i64

        //f68
        Label f68Label = new Label("F_68");
        f68Label.setPrefWidth(60);
        
        TextField f68TextField = new TextField(String.valueOf(entry.f68));
        f68TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (f68TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.f68 = Float.parseFloat(newText); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox f68HBox = new HBox(f68Label, f68TextField);
        f68HBox.setAlignment(Pos.CENTER_LEFT);
        //f68

        //f72
        Label f72Label = new Label("F_72");
        f72Label.setPrefWidth(60);

        TextField f72TextField = new TextField(String.valueOf(entry.f72));
        f72TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (f72TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.f72 = Float.parseFloat(newText);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox f72HBox = new HBox(f72Label, f72TextField);
        f72HBox.setAlignment(Pos.CENTER_LEFT);
        //f72

        VBox unknownVBox = new VBox(30, 
            i46HBox, i47HBox, 
            f48HBox, f52HBox,
            f56HBox, f60HBox,
            f64HBox, f68HBox,
            f72HBox
        );
        unknownVBox.setPadding(new Insets(20, 0, 0, 16));

        return unknownVBox;
    }

    private void createTabs() {
        if (mainTabPane.getTabs().isEmpty()) {
            Tab partSetsTab = new Tab("Part Sets");
            Tab partColorsTab = new Tab("Part Colors");
            Tab bodiesTab = new Tab("Bodies");
            Tab skeletonsTab = new Tab("Skeletons");
            Tab unknownTab = new Tab("Unknown");

            partSetsTab.setClosable(false);
            partColorsTab.setClosable(false);
            bodiesTab.setClosable(false);
            skeletonsTab.setClosable(false);
            unknownTab.setClosable(false);

            mainTabPane.getTabs().addAll(partSetsTab, partColorsTab, bodiesTab, skeletonsTab, unknownTab);
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
                    mainTabPane.getTabs().get(4).setContent(createUnknownVBox(bcsPartSet));
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
                addUnknown3.setDisable(false);

                if (pastePartSetItem.getText().contains(newValue.getValue())) pastePartSetItem.setDisable(false);
            }
            else if (newValue.getParent().getValue().equals("Color Selectors")) {
                int index = dynamicTabPane.getSelectionModel().getSelectedIndex();

                dynamicTabPane.getTabs().clear();

                createColorSelector(bcsColorsSelectorHashMap.get(newValue));

                dynamicTabPane.getSelectionModel().select(index);

                addColorSelector.setDisable(false);

                if (pastePartSetItem.getText().equals("Paste Color Selector  Ctrl+V")) pastePartSetItem.setDisable(false);
            }
            else if (newValue.getParent().getValue().equals("Physics")) {
                int index = dynamicTabPane.getSelectionModel().getSelectedIndex();

                dynamicTabPane.getTabs().clear();

                createPhysics(bcsPhysicsHashMap.get(newValue));

                dynamicTabPane.getSelectionModel().select(index);

                addPhysics.setDisable(false);

                if (pastePartSetItem.getText().equals("Paste Physics  Ctrl+V")) pastePartSetItem.setDisable(false);
            }
            else if (newValue.getParent().getValue().equals("Unknown 3")) {
                int index = dynamicTabPane.getSelectionModel().getSelectedIndex();

                dynamicTabPane.getTabs().clear();

                createUnknown3(bcsUnknown3HashMap.get(newValue));

                dynamicTabPane.getSelectionModel().select(index);

                addUnknown3.setDisable(false);

                if (pastePartSetItem.getText().equals("Paste Unknown 3  Ctrl+V")) pastePartSetItem.setDisable(false);
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
                    if (pastePartSetItem.getText().equals("Paste Part Set  Ctrl+V")) pastePartSetItem.setDisable(false);

                    else if (addPartSetItemCopy.getText().contains("Color Selector") || addPartSetItemCopy.getText().contains("Physics") || addPartSetItemCopy.getText().contains("Unknown 3")) {
                        addPartSetItemCopy.setDisable(true);
                    }
                }
            }
        });
        partSetsTreeView.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.SECONDARY) {
                partSetContextMenu.setOnAction(event -> {
                    if (event.getTarget() == addPartSet) {
                        AddPartSet();
                    }
                    if (event.getTarget() == copyPartSetItem) {
                        CopyPartSetItem();
                    }
                    if (event.getTarget() == deletePartSetItem) {
                        DeletePartSetItem();
                    }
                    if (event.getTarget() == addColorSelector) {
                        AddColorSelector();
                    }
                    if (event.getTarget() == addPhysics) {
                        AddPhysics();
                    }
                    if (event.getTarget() == addUnknown3) {
                        AddUnknown3();
                    }
                    if (event.getTarget() == pastePartSetItem) {
                        PastePartSetItem();
                    }
                    if (event.getTarget() == addPartSetItemCopy) {
                        AddPartSetItemCopy();
                    }
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
            if (e.isControlDown() && e.getCode() == KeyCode.C) {
                CopyPartSetItem();
            }
            if (e.isControlDown() && e.getCode() == KeyCode.V) {
                PastePartSetItem();
            }
            if (e.getCode() == KeyCode.DELETE) {
                DeletePartSetItem();
            }
            if (e.isControlDown() && e.getCode() == KeyCode.A) {
                AddPartSetItemCopy();
            }
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
        copiedPartSetItem.setText("Copied %s");
        pastePartSetItem.setText("Paste %s  Ctrl+V");
        addPartSetItemCopy.setText("Add %s Copy  Ctrl+A");

        noCopiedPartSetItemFound.setVisible(false);
        copiedPartSetItem.setVisible(true);
        pastePartSetItem.setVisible(true);
        addPartSetItemCopy.setVisible(true);

        pastePartSetItem.setDisable(false);

        if (currentPartSetEntry.getParent() == partSetsTreeView.getRoot()) {
            copiedPartSetItem.setText(String.format(copiedPartSetItem.getText(), "Part Set"));
            pastePartSetItem.setText(String.format(pastePartSetItem.getText(), "Part Set"));
            addPartSetItemCopy.setText(String.format(addPartSetItemCopy.getText(), "Part Set"));

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
            copiedPartSetItem.setText(String.format(copiedPartSetItem.getText(), currentPartSetEntry.getValue()));
            pastePartSetItem.setText(String.format(pastePartSetItem.getText(), currentPartSetEntry.getValue()));
            addPartSetItemCopy.setText(String.format(addPartSetItemCopy.getText(), currentPartSetEntry.getValue()));

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

                    copiedPartSetItem.setText(String.format(copiedPartSetItem.getText(), "Color Selector"));
                    pastePartSetItem.setText(String.format(pastePartSetItem.getText(), "Color Selector"));
                    addPartSetItemCopy.setText(String.format(addPartSetItemCopy.getText(), "Color Selector"));
                }
                case "Physics" -> {
                    copyContainer = new BcsPhysics(bcsPhysicsHashMap.get(currentPartSetEntry));

                    copiedPartSetItem.setText(String.format(copiedPartSetItem.getText(), "Physics"));
                    pastePartSetItem.setText(String.format(pastePartSetItem.getText(), "Physics"));
                    addPartSetItemCopy.setText(String.format(addPartSetItemCopy.getText(), "Physics"));
                }
                case "Unknown 3" -> {
                    copyContainer = new BcsUnknown3(bcsUnknown3HashMap.get(currentPartSetEntry));

                    copiedPartSetItem.setText(String.format(copiedPartSetItem.getText(), "Unknown 3"));
                    pastePartSetItem.setText(String.format(pastePartSetItem.getText(), "Unknown 3"));
                    addPartSetItemCopy.setText(String.format(addPartSetItemCopy.getText(), "Unknown 3"));
                }
            }
        }
        else if (currentPartSetEntry.getChildren().isEmpty()) {
            copiedPartSetItem.setText(String.format(copiedPartSetItem.getText(), "Null"));
            pastePartSetItem.setText(String.format(pastePartSetItem.getText(), "Null"));
            addPartSetItemCopy.setText(String.format(addPartSetItemCopy.getText(), "Null"));
        }
        else {
            copyListContainer = new Object[1][currentPartSetEntry.getChildren().size()];

            switch (currentPartSetEntry.getValue()) {
                case "Color Selectors" -> {
                    for (TreeItem<String> child : currentPartSetEntry.getChildren()) {
                        copyListContainer[0][currentPartSetEntry.getChildren().indexOf(child)] = new BcsColorSelector(bcsColorsSelectorHashMap.get(child));
                    }

                    copiedPartSetItem.setText(String.format(copiedPartSetItem.getText(), "Color Selector List"));
                    pastePartSetItem.setText(String.format(pastePartSetItem.getText(), "Color Selector List"));
                    addPartSetItemCopy.setText(String.format(addPartSetItemCopy.getText(), "Color Selector List"));
                }
                case "Physics" -> {
                    for (TreeItem<String> child : currentPartSetEntry.getChildren()) {
                        copyListContainer[0][currentPartSetEntry.getChildren().indexOf(child)] = new BcsPhysics(bcsPhysicsHashMap.get(child));
                    }

                    copiedPartSetItem.setText(String.format(copiedPartSetItem.getText(), "Physics List"));
                    pastePartSetItem.setText(String.format(pastePartSetItem.getText(), "Physics List"));
                    addPartSetItemCopy.setText(String.format(addPartSetItemCopy.getText(), "Physics List"));
                }
                case "Unknown 3" -> {
                    for (TreeItem<String> child : currentPartSetEntry.getChildren()) {
                        copyListContainer[0][currentPartSetEntry.getChildren().indexOf(child)] = new BcsUnknown3(bcsUnknown3HashMap.get(child));
                    }

                    copiedPartSetItem.setText(String.format(copiedPartSetItem.getText(), "Unknown 3 List"));
                    pastePartSetItem.setText(String.format(pastePartSetItem.getText(), "Unknown 3 List"));
                    addPartSetItemCopy.setText(String.format(addPartSetItemCopy.getText(), "Unknown 3 List"));
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
        switch (addPartSetItemCopy.getText()) {
            case "Add Part Set Copy  Ctrl+A" -> {
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
            case "Add Face Base Copy  Ctrl+A" -> {
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
            case "Add Face Forehead Copy  Ctrl+A" -> {
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
            case "Add Face Eye Copy  Ctrl+A" -> {
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
            case "Add Face Nose Copy  Ctrl+A" -> {
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
            case "Add Face Ear Copy  Ctrl+A" -> {
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
            case "Add Hair Copy  Ctrl+A" -> {
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
            case "Add Bust Copy  Ctrl+A" -> {
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
            case "Add Pants Copy  Ctrl+A" -> {
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
            case "Add Rist Copy  Ctrl+A" -> {
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
            case "Add Boots Copy  Ctrl+A" -> {
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
            case "Add Color Selector Copy  Ctrl+A" -> {
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
                } else {
                    getGrandParent.getChildren().add(0, new TreeItem<>("Color Selectors"));

                    TreeItem<String> newChild = new TreeItem<>("Entry " + 0);

                    getGrandParent.getChildren().get(0).getChildren().add(newChild);

                    bcsColorsSelectorHashMap.put(newChild, new BcsColorSelector((BcsColorSelector) copyContainer));

                    partSetsTreeView.getSelectionModel().select(newChild);
                }
            }
            case "Add Color Selector List Copy  Ctrl+A" -> {
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
            case "Add Physics Copy  Ctrl+A" -> {
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
            case "Add Physics List Copy  Ctrl+A" -> {
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
            case "Add Unknown 3 Copy  Ctrl+A" -> {
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
            case "Add Unknown 3 List Copy  Ctrl+A" -> {
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

                if (pastePartColorItem.getText().equals("Paste Part Color  Ctrl+V")) pastePartColorItem.setDisable(false);
            }
            else if (newValue.getValue().contains("Color")) {
                int index = dynamicTabPane.getSelectionModel().getSelectedIndex();

                dynamicTabPane.getTabs().clear();

                createColor(bcsColorsHashMap.get(newValue));

                dynamicTabPane.getSelectionModel().select(index);

                if (pastePartColorItem.getText().equals("Paste Color  Ctrl+V")) pastePartColorItem.setDisable(false);
            }
            else {
                dynamicTabPane.getTabs().clear();
            }
        });
        partColorsTreeView.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.SECONDARY) {
                partColorContextMenu.setOnAction(event -> {
                    if (event.getTarget() == addPartColor) {
                        AddPartColor();
                    }
                    if (event.getTarget() == copyPartColorItem) {
                        CopyPartColorItem();
                    }
                    if (event.getTarget() == deletePartColorItem) {
                        DeletePartColorItem();
                    }
                    if (event.getTarget() == addColor) {
                        AddColor();
                    }
                    if (event.getTarget() == pastePartColorItem) {
                        PastePartColorItem();
                    }
                    if (event.getTarget() == addPartColorItemCopy) {
                        AddPartColorItemCopy();
                    }
                });
            }
        });
    }

    private void partColorsKeysListener() {
        partColorsTreeView.setOnKeyPressed(e -> {
            if (e.isControlDown() && e.getCode() == KeyCode.C) {
                CopyPartColorItem();
            }
            if (e.isControlDown() && e.getCode() == KeyCode.V) {
                PastePartColorItem();
            }
            if (e.getCode() == KeyCode.DELETE) {
                DeletePartColorItem();
            }
            if (e.isControlDown() && e.getCode() == KeyCode.A) {
                AddPartColorItemCopy();
            }
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

        allPartColorEntries++;
    }

    private void AddColor() {
        if (partColorsTreeView.getSelectionModel().getSelectedIndex() < 0) return;

        TreeItem<String> parent = !currentPartColorEntry.getValue().contains("Part") ? currentPartColorEntry.getParent() : currentPartColorEntry;

        TreeItem<String> color = new TreeItem<>("Color " +  parent.getChildren().size());

        bcsColorsHashMap.put(color, new BcsColor());

        parent.getChildren().add(color);

        partColorsTreeView.getSelectionModel().select(color);
    }

    private void CopyPartColorItem() {
        copiedPartColorItem.setText("Copied %s");
        pastePartColorItem.setText("Paste %s  Ctrl+V");
        addPartColorItemCopy.setText("Add %s Copy  Ctrl+A");

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

            copiedPartColorItem.setText(String.format(copiedPartColorItem.getText(), "Part Color"));
            pastePartColorItem.setText(String.format(pastePartColorItem.getText(), "Part Color"));
            addPartColorItemCopy.setText(String.format(addPartColorItemCopy.getText(), "Part Color"));
        }
        else {
            copyContainer = new BcsColor(bcsColorsHashMap.get(currentPartColorEntry));

            copiedPartColorItem.setText(String.format(copiedPartColorItem.getText(), "Color"));
            pastePartColorItem.setText(String.format(pastePartColorItem.getText(), "Color"));
            addPartColorItemCopy.setText(String.format(addPartColorItemCopy.getText(), "Color"));
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
        switch (addPartColorItemCopy.getText()) {
            case "Add Part Color Copy  Ctrl+A" -> {
                TreeItem<String> partColor = new TreeItem<>("Part Color " + allPartColorEntries);

                partColorsTreeView.getRoot().getChildren().add(partColor);

                bcsPartColorsHashMap.put(partColor, new BcsPartColor((BcsPartColor) copyContainer));

                for (int i = 0; i < copyListContainer[0].length; i++) {
                    partColor.getChildren().add(i, new TreeItem<>("Color " + i));

                    bcsColorsHashMap.put(partColor.getChildren().get(i), new BcsColor((BcsColor) copyListContainer[0][i]));
                }

                allPartColorEntries++;
            }
            case "Add Color Copy  Ctrl+A" -> {
                TreeItem<String> color = new TreeItem<>("Color " + partColorGrandParentEntry.getChildren().size());

                partColorGrandParentEntry.getChildren().add(color);

                bcsColorsHashMap.put(color, new BcsColor((BcsColor) copyContainer));

                partColorsTreeView.getSelectionModel().select(color);
            }
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

                if (pasteBodyItem.getText().equals("Paste Bone Scale  Ctrl+V")) pasteBodyItem.setDisable(false);
            }
            else {
                dynamicTabPane.getTabs().clear();

                if (pasteBodyItem.getText().equals("Paste Body  Ctrl+V")) pasteBodyItem.setDisable(false);
            }
        });
        bodiesTreeView.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.SECONDARY) {
                bodyContextMenu.setOnAction(event -> {
                    if (event.getTarget() == addBody) {
                        AddBody();
                    }
                    if (event.getTarget() == copyBodyItem) {
                        CopyBodyItem();
                    }
                    if (event.getTarget() == deleteBodyItem) {
                        DeleteBodyItem();
                    }
                    if (event.getTarget() == addBoneScale) {
                        AddBoneScale();
                    }
                    if (event.getTarget() == pasteBodyItem) {
                        PasteBodyItem();
                    }
                    if (event.getTarget() == addBodyItemCopy) {
                        AddBodyItemCopy();
                    }
                });
            }
        });
    }

    private void bodiesKeysListener() {
        bodiesTreeView.setOnKeyPressed(e -> {
            if (e.isControlDown() && e.getCode() == KeyCode.C) {
                CopyBodyItem();
            }
            if (e.isControlDown() && e.getCode() == KeyCode.V) {
                PasteBodyItem();
            }
            if (e.getCode() == KeyCode.DELETE) {
                DeleteBodyItem();
            }
            if (e.isControlDown() && e.getCode() == KeyCode.A) {
                AddBodyItemCopy();
            }
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
        copiedBodyItem.setText("Copied %s");
        pasteBodyItem.setText("Paste %s  Ctrl+V");
        addBodyItemCopy.setText("Add %s Copy  Ctrl+A");

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

            copiedBodyItem.setText(String.format(copiedBodyItem.getText(), "Body"));
            pasteBodyItem.setText(String.format(pasteBodyItem.getText(), "Body"));
            addBodyItemCopy.setText(String.format(addBodyItemCopy.getText(), "Body"));
        }
        else {
            copyContainer = new BcsBoneScale(bcsBoneScalesHashMap.get(currentBodyEntry));

            copiedBodyItem.setText(String.format(copiedBodyItem.getText(), "Bone Scale"));
            pasteBodyItem.setText(String.format(pasteBodyItem.getText(), "Bone Scale"));
            addBodyItemCopy.setText(String.format(addBodyItemCopy.getText(), "Bone Scale"));
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
        switch (addBodyItemCopy.getText()) {
            case "Add Body Copy  Ctrl+A" -> {
                TreeItem<String> body = new TreeItem<>("Body " + allBodyEntries);

                bodiesTreeView.getRoot().getChildren().add(body);

                for (int i = 0; i < copyListContainer[0].length; i++) {
                    body.getChildren().add(i, new TreeItem<>("Bone Scale " + i));

                    bcsBoneScalesHashMap.put(body.getChildren().get(i), new BcsBoneScale((BcsBoneScale) copyListContainer[0][i]));
                }

                allBodyEntries++;
            }
            case "Add Bone Scale Copy  Ctrl+A" -> {
                TreeItem<String> boneScale = new TreeItem<>("Bone Scale " + bodyGrandParentEntry.getChildren().size());

                bodyGrandParentEntry.getChildren().add(boneScale);

                bcsBoneScalesHashMap.put(boneScale, new BcsBoneScale((BcsBoneScale) copyContainer));

                bodiesTreeView.getSelectionModel().select(boneScale);
            }
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

                if (pasteSkeletonItem.getText().equals("Paste Skeleton  Ctrl+V")) pasteSkeletonItem.setDisable(false);
            }
            else if (newValue.getValue().contains("Bone")) {
                int index = dynamicTabPane.getSelectionModel().getSelectedIndex();

                dynamicTabPane.getTabs().clear();

                createBone(bcsBonesHashMap.get(newValue));

                dynamicTabPane.getSelectionModel().select(index);

                if (pasteSkeletonItem.getText().equals("Paste Bone  Ctrl+V")) pasteSkeletonItem.setDisable(false);
            }
            else {
                dynamicTabPane.getTabs().clear();
            }
        });
        skeletonsTreeView.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.SECONDARY) {
                skeletonContextMenu.setOnAction(event -> {
                    if (event.getTarget() == addSkeleton) {
                        AddSkeleton();
                    }
                    if (event.getTarget() == copySkeletonItem) {
                        CopySkeletonItem();
                    }
                    if (event.getTarget() == deleteSkeletonItem) {
                        DeleteSkeletonItem();
                    }
                    if (event.getTarget() == addBone) {
                        AddBone();
                    }
                    if (event.getTarget() == pasteSkeletonItem) {
                        PasteSkeletonItem();
                    }
                    if (event.getTarget() == addSkeletonItemCopy) {
                        AddSkeletonItemCopy();
                    }
                });
            }
        });
    }

    private void skeletonsKeysListener() {
        skeletonsTreeView.setOnKeyPressed(e -> {
            if (e.isControlDown() && e.getCode() == KeyCode.C) {
                CopySkeletonItem();
            }
            if (e.isControlDown() && e.getCode() == KeyCode.V) {
                PasteSkeletonItem();
            }
            if (e.getCode() == KeyCode.DELETE) {
                DeleteSkeletonItem();
            }
            if (e.isControlDown() && e.getCode() == KeyCode.A) {
                AddSkeletonItemCopy();
            }
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
        copiedSkeletonItem.setText("Copied %s");
        pasteSkeletonItem.setText("Paste %s  Ctrl+V");
        addSkeletonItemCopy.setText("Add %s Copy  Ctrl+A");

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

            copiedSkeletonItem.setText(String.format(copiedSkeletonItem.getText(), "Skeleton"));
            pasteSkeletonItem.setText(String.format(pasteSkeletonItem.getText(), "Skeleton"));
            addSkeletonItemCopy.setText(String.format(addSkeletonItemCopy.getText(), "Skeleton"));
        }
        else {
            addSkeletonItemCopy.setDisable(false);

            copyContainer = new BcsBone(bcsBonesHashMap.get(currentSkeletonEntry));

            copiedSkeletonItem.setText(String.format(copiedSkeletonItem.getText(), "Bone"));
            pasteSkeletonItem.setText(String.format(pasteSkeletonItem.getText(), "Bone"));
            addSkeletonItemCopy.setText(String.format(addSkeletonItemCopy.getText(), "Bone"));
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
        switch (addSkeletonItemCopy.getText()) {
            case "Add Skeleton Copy  Ctrl+A" -> {
                TreeItem<String> skeleton = new TreeItem<>("Skeleton " + (skeletonsTreeView.getRoot().getChildren().size() + 1));

                skeletonsTreeView.getRoot().getChildren().add(skeleton);

                bcsSkeletonsHashMap.put(skeleton, new BcsSkeleton((BcsSkeleton) copyContainer));

                for (int i = 0; i < copyListContainer[0].length; i++) {
                    skeleton.getChildren().add(i, new TreeItem<>("Bone " + i));

                    bcsBonesHashMap.put(skeleton.getChildren().get(i), new BcsBone((BcsBone) copyListContainer[0][i]));
                }
            }
            case "Add Bone Copy  Ctrl+A" -> {
                TreeItem<String> bone = new TreeItem<>("Bone " + skeletonGrandParentEntry.getChildren().size());

                skeletonGrandParentEntry.getChildren().add(bone);

                bcsBonesHashMap.put(bone, new BcsBone((BcsBone) copyContainer));

                skeletonsTreeView.getSelectionModel().select(bone);
            }
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
                    bcsPartSet.f48 = intBuffer.getFloat();

                    channel.position(40);
                    intBuffer.clear();
                    channel.read(intBuffer);
                    intBuffer.flip();
                    bcsPartSet.f52 = intBuffer.getFloat();

                    channel.position(44);
                    intBuffer.clear();
                    channel.read(intBuffer);
                    intBuffer.flip();
                    bcsPartSet.f56 = intBuffer.getFloat();

                    channel.position(48);
                    intBuffer.clear();
                    channel.read(intBuffer);
                    intBuffer.flip();
                    bcsPartSet.f60 = intBuffer.getFloat();

                    channel.position(52);
                    intBuffer.clear();
                    channel.read(intBuffer);
                    intBuffer.flip();
                    bcsPartSet.f64 = intBuffer.getFloat();

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
                    bcsPartSet.f48 = intBuffer.getFloat();

                    channel.position(52);
                    intBuffer.clear();
                    channel.read(intBuffer);
                    intBuffer.flip();
                    bcsPartSet.f52 = intBuffer.getFloat();

                    channel.position(56);
                    intBuffer.clear();
                    channel.read(intBuffer);
                    intBuffer.flip();
                    bcsPartSet.f56 = intBuffer.getFloat();

                    channel.position(60);
                    intBuffer.clear();
                    channel.read(intBuffer);
                    intBuffer.flip();
                    bcsPartSet.f60 = intBuffer.getFloat();

                    channel.position(64);
                    intBuffer.clear();
                    channel.read(intBuffer);
                    intBuffer.flip();
                    bcsPartSet.f64 = intBuffer.getFloat();

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
                    bcsPhysics.model1 = shortBuffer.getShort();

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
                    intBuffer.putFloat(bcsPartSet.f48);
                    intBuffer.flip();
                    channel.write(intBuffer);

                    channel.position(40);
                    intBuffer.clear();
                    intBuffer.putFloat(bcsPartSet.f52);
                    intBuffer.flip();
                    channel.write(intBuffer);

                    channel.position(44);
                    intBuffer.clear();
                    intBuffer.putFloat(bcsPartSet.f56);
                    intBuffer.flip();
                    channel.write(intBuffer);

                    channel.position(48);
                    intBuffer.clear();
                    intBuffer.putFloat(bcsPartSet.f60);
                    intBuffer.flip();
                    channel.write(intBuffer);

                    channel.position(52);
                    intBuffer.clear();
                    intBuffer.putFloat(bcsPartSet.f64);
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
                    intBuffer.putFloat(bcsPartSet.f48);
                    intBuffer.flip();
                    channel.write(intBuffer);

                    channel.position(52);
                    intBuffer.clear();
                    intBuffer.putFloat(bcsPartSet.f52);
                    intBuffer.flip();
                    channel.write(intBuffer);

                    channel.position(56);
                    intBuffer.clear();
                    intBuffer.putFloat(bcsPartSet.f56);
                    intBuffer.flip();
                    channel.write(intBuffer);

                    channel.position(60);
                    intBuffer.clear();
                    intBuffer.putFloat(bcsPartSet.f60);
                    intBuffer.flip();
                    channel.write(intBuffer);

                    channel.position(64);
                    intBuffer.clear();
                    intBuffer.putFloat(bcsPartSet.f64);
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
                                intBuffer.putInt(boneScaleOffset);
                                intBuffer.flip();
                                channel.write(intBuffer);

                                for (int j = 0; j < boneScaleCount; j++) {
                                    BcsBoneScale bcsBoneScale = bcsBoneScalesHashMap.get(bodiesTreeView.getRoot().getChildren().get(mainIndex).getChildren().get(j));

                                    channel.position(offset + boneScaleOffset + j * 16);
                                    intBuffer.clear();
                                    intBuffer.putFloat(bcsBoneScale.scaleX);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(offset + boneScaleOffset + j * 16 + 4);
                                    intBuffer.clear();
                                    intBuffer.putFloat(bcsBoneScale.scaleY);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(offset + boneScaleOffset + j * 16 + 8);
                                    intBuffer.clear();
                                    intBuffer.putFloat(bcsBoneScale.scaleZ);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(offset + boneScaleOffset + j * 16 + 12);
                                    intBuffer.clear();
                                    intBuffer.putInt(typesSum - offset - boneScaleOffset - j * 16);
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

                            offset += 8;
                            boneScaleOffset += ((boneScaleCount + 1) * 8);
                            thisBodyOffset += 8;
                            mainIndex++;
                        }
                    } 
                }

                thisSkeleton1Offset = thisBodyOffset;
                thisSkeleton1Offset += boneScaleOffset;
            }
            if (skeleton1Offset != 0) {
                if (version != 72) {
                    channel.position(skeleton1Offset);
                    intBuffer.clear();
                    intBuffer.putInt(thisSkeleton1Offset);
                    intBuffer.flip();
                    channel.write(intBuffer);
                }
                else {
                    if (bodiesTreeView.getRoot() != null) {
                        thisSkeleton1Offset -= bodiesTreeView.getRoot().getChildren().size() * 16;
                    }
                    else if (partColorsTreeView.getRoot() != null) {
                        thisSkeleton1Offset -= partColorsTreeView.getRoot().getChildren().size() * 16;
                    }
                    else if (partSetsTreeView.getRoot() != null) {
                        thisSkeleton1Offset -= partSetsTreeView.getRoot().getChildren().size() * 16;
                    }
                }
                
                BcsSkeleton bcsSkeleton = bcsSkeletonsHashMap.get(skeletonsTreeView.getRoot().getChildren().get(0));
                int offset = version == 72 ? 64 : thisSkeleton1Offset; 
                int boneOffset = version == 72 ? thisSkeleton1Offset : 8;

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
                    if (bodiesTreeView.getRoot() != null) {
                        boneOffset -= bodiesTreeView.getRoot().getChildren().size() * 16;
                    }
                    else if (partColorsTreeView.getRoot() != null) {
                        boneOffset -= partColorsTreeView.getRoot().getChildren().size() * 16;
                    }
                    else if (partSetsTreeView.getRoot() != null) {
                        boneOffset -= partSetsTreeView.getRoot().getChildren().size() * 16;
                    }
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
                int physicsOffset = (version == 72 ? 80 : 88);

                channel.position(mainOffset + 76);
                intBuffer.clear();
                intBuffer.putInt(physicsOffset);
                intBuffer.flip();
                channel.write(intBuffer);

                for (int j = 0; j < physicsCount; j++) {
                    BcsPhysics bcsPhysics = bcsPhysicsHashMap.get(partSetsTreeView.getRoot().getChildren().get(mainIndex).getChildren().get(partIndex).getChildren().get(subPartIndex).getChildren().get(j));

                    channel.position(mainOffset + physicsOffset + j * 72);
                    shortBuffer.clear();
                    shortBuffer.putShort(bcsPhysics.model1);
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
                        intBuffer.putInt(typesSum - mainOffset - physicsOffset);
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
                        intBuffer.putInt(typesSum - mainOffset - physicsOffset);
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
                        intBuffer.putInt(typesSum - mainOffset - physicsOffset);
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
                        intBuffer.putInt(typesSum - mainOffset - physicsOffset);
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
                        intBuffer.putInt(typesSum - mainOffset  - physicsOffset);
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
                        intBuffer.putInt(typesSum - mainOffset - physicsOffset);
                        intBuffer.flip();
                        channel.write(intBuffer);
                        
                        dynamicStringBuffer = ByteBuffer.allocate(bcsPhysics.scdName.getBytes().length);

                        channel.position(typesSum);
                        dynamicStringBuffer.clear();
                        dynamicStringBuffer = ByteBuffer.wrap(bcsPhysics.scdName.getBytes());
                        channel.write(dynamicStringBuffer);

                        typesSum += bcsPhysics.scdName.getBytes().length;
                    }

                    thisPartSetOffset += 72;
                    this.relativeOffset += 72;
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
                    int unknown3Offset = 88;

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
}

class BcsPartSet {
    public int race;
    public int gender;
    public int i46;
    public int i47;
    public float f48;
    public float f52;
    public float f56;
    public float f60;
    public float f64;
    public float f68;
    public float f72;

    public BcsPartSet() {}
    public BcsPartSet(BcsPartSet other) {
        this.race = other.race;
        this.gender = other.gender;
        this.i46 = other.i46;
        this.i47 = other.i47;
        this.f48 = other.f48;
        this.f52 = other.f52;
        this.f56 = other.f56;
        this.f60 = other.f60;
        this.f64 = other.f64;
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
    public String charaCode;
    public String emdName;
    public String emmName;
    public String embName;
    public String eanName;

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
    short model1;
    short model2;
    short texture;
    long flags;
    int hideFlags;
    int hideMatFlags;
    String charaCode;
    String emdName;
    String emmName;
    String embName;
    String eskName;
    String boneToAttach;
    String scdName;

    public BcsPhysics() {}
    public BcsPhysics(BcsPhysics other) {
        this.model1 = other.model1;
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
    String name;

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