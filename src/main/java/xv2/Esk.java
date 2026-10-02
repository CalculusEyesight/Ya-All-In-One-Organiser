package xv2;
import static xv2.Unsigned.toUByte;
import static xv2.Unsigned.toUShort;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Spinner;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Esk {
    TreeView<String> bonesTreeView = new TreeView<>();
    TreeView<String> ikTreeView = new TreeView<>();
    TabPane eskTabPane = new TabPane();
    TabPane dynamicTabPane = new TabPane();
    TreeItem<String> currentBoneEntry = new TreeItem<>();
    TreeItem<String> currentIkEntry = new TreeItem<>();

    ArrayList<TreeItem<String>> allBones;
    HashMap<TreeItem<String>, Bone> bonesHashMap;
    HashMap<TreeItem<String>, IK_Relations> ikHashMap;
    HashMap<TreeItem<String>, IK_Bone> ikBoneHashMap = new HashMap<>();
    
    EskMain eskMain = new EskMain();
    Skeleton skeleton = new Skeleton();

    boolean hasAbsoluteTransform = false;

    Esk() {
        tabsActionListener();
        bonesActionListener();
        ikActionListener();
    }

    public SplitPane createSplitPane() {
        createTabs(eskTabPane);
        return new SplitPane(eskTabPane, dynamicTabPane);
    }

    protected void createTabs(TabPane tabPane) {
        if (tabPane.getTabs().isEmpty()) {
            Tab boneListTab = new Tab("Bone List");
            Tab ikRelationsTab = new Tab("IK Relations");
            Tab miscellaneousTab = new Tab("Miscellaneous");

            boneListTab.setClosable(false);
            ikRelationsTab.setClosable(false);
            miscellaneousTab.setClosable(false);
            
            tabPane.getTabs().addAll(boneListTab, ikRelationsTab, miscellaneousTab);
        }
    }

    protected void createBones(Bone bone) {
        VBox bonePropertiesVBox = new VBox(30,
            createHBox(0, createLabel("Bone Name", 80), createTextField(bone.boneName, BoneValues.BoneName)),
            createHBox(0, createLabel("IK Flag", 80), createTextField(bone.ikFlag, BoneValues.IK_Flag))
        );
        bonePropertiesVBox.setPadding(new Insets(20, 0, 0, 16));

        VBox relativeTransformVBox = new VBox(30,
            createHBox(0, createLabel("Position X", 80), createSpinner(-Float.MAX_VALUE, Float.MAX_VALUE, bone.positionX, BoneValues.PositionX)),
            createHBox(0, createLabel("Position Y", 80), createSpinner(-Float.MAX_VALUE, Float.MAX_VALUE, bone.positionY, BoneValues.PositionY)),
            createHBox(0, createLabel("Position Z", 80), createSpinner(-Float.MAX_VALUE, Float.MAX_VALUE, bone.positionZ, BoneValues.PositionZ)),
            createHBox(0, createLabel("Position W", 80), createSpinner(-Float.MAX_VALUE, Float.MAX_VALUE, bone.positionW, BoneValues.PositionW)),
            createHBox(0, createLabel("Rotation X", 80), createSpinner(-Float.MAX_VALUE, Float.MAX_VALUE, bone.rotationX, BoneValues.RotationX)),
            createHBox(0, createLabel("Rotation Y", 80), createSpinner(-Float.MAX_VALUE, Float.MAX_VALUE, bone.rotationY, BoneValues.RotationY)),
            createHBox(0, createLabel("Rotation Z", 80), createSpinner(-Float.MAX_VALUE, Float.MAX_VALUE, bone.rotationZ, BoneValues.RotationZ)),
            createHBox(0, createLabel("Rotation W", 80), createSpinner(-Float.MAX_VALUE, Float.MAX_VALUE, bone.rotationW, BoneValues.RotationW)),
            createHBox(0, createLabel("Scale X", 80), createSpinner(-Float.MAX_VALUE, Float.MAX_VALUE, bone.scaleX, BoneValues.ScaleX)),
            createHBox(0, createLabel("Scale Y", 80), createSpinner(-Float.MAX_VALUE, Float.MAX_VALUE, bone.scaleY, BoneValues.ScaleY)),
            createHBox(0, createLabel("Scale Z", 80), createSpinner(-Float.MAX_VALUE, Float.MAX_VALUE, bone.scaleZ, BoneValues.ScaleZ)),
            createHBox(0, createLabel("Scale W", 80), createSpinner(-Float.MAX_VALUE, Float.MAX_VALUE, bone.scaleW, BoneValues.ScaleW))
        );
        relativeTransformVBox.setPadding(new Insets(20, 0, 0, 16));

        VBox absoluteTransformVBox = new VBox(30,
            createHBox(0, createLabel("Line1 X", 80), createSpinner(-Float.MAX_VALUE, Float.MAX_VALUE, bone.line1X, BoneValues.Line1X)),
            createHBox(0, createLabel("Line1 Y", 80), createSpinner(-Float.MAX_VALUE, Float.MAX_VALUE, bone.line1Y, BoneValues.Line1Y)),
            createHBox(0, createLabel("Line1 Z", 80), createSpinner(-Float.MAX_VALUE, Float.MAX_VALUE, bone.line1Z, BoneValues.Line1Z)),
            createHBox(0, createLabel("Line1 W", 80), createSpinner(-Float.MAX_VALUE, Float.MAX_VALUE, bone.line1W, BoneValues.Line1W)),
            createHBox(0, createLabel("Line2 X", 80), createSpinner(-Float.MAX_VALUE, Float.MAX_VALUE, bone.line2X, BoneValues.Line2X)),
            createHBox(0, createLabel("Line2 Y", 80), createSpinner(-Float.MAX_VALUE, Float.MAX_VALUE, bone.line2Y, BoneValues.Line2Y)),
            createHBox(0, createLabel("Line2 Z", 80), createSpinner(-Float.MAX_VALUE, Float.MAX_VALUE, bone.line2Z, BoneValues.Line2Z)),
            createHBox(0, createLabel("Line2 W", 80), createSpinner(-Float.MAX_VALUE, Float.MAX_VALUE, bone.line2W, BoneValues.Line2W)),
            createHBox(0, createLabel("Line3 X", 80), createSpinner(-Float.MAX_VALUE, Float.MAX_VALUE, bone.line3X, BoneValues.Line3X)),
            createHBox(0, createLabel("Line3 Y", 80), createSpinner(-Float.MAX_VALUE, Float.MAX_VALUE, bone.line3Y, BoneValues.Line3Y)),
            createHBox(0, createLabel("Line3 Z", 80), createSpinner(-Float.MAX_VALUE, Float.MAX_VALUE, bone.line3Z, BoneValues.Line3Z)),
            createHBox(0, createLabel("Line3 W", 80), createSpinner(-Float.MAX_VALUE, Float.MAX_VALUE, bone.line3W, BoneValues.Line3W)),
            createHBox(0, createLabel("Line4 X", 80), createSpinner(-Float.MAX_VALUE, Float.MAX_VALUE, bone.line4X, BoneValues.Line4X)),
            createHBox(0, createLabel("Line4 Y", 80), createSpinner(-Float.MAX_VALUE, Float.MAX_VALUE, bone.line4Y, BoneValues.Line4Y)),
            createHBox(0, createLabel("Line4 Z", 80), createSpinner(-Float.MAX_VALUE, Float.MAX_VALUE, bone.line4Z, BoneValues.Line4Z)),
            createHBox(0, createLabel("Line4 W", 80), createSpinner(-Float.MAX_VALUE, Float.MAX_VALUE, bone.line4W, BoneValues.Line4W))
        );
        absoluteTransformVBox.setPadding(new Insets(20, 0, 20, 16));
        
        VBox extraValuesVBox = new VBox(30,
            createHBox(0, createLabel("Extra Value 1", 80), createSpinner(-Float.MAX_VALUE, Float.MAX_VALUE, bone.extraValue1, BoneValues.ExtraValue1)),
            createHBox(0, createLabel("Extra Value 2", 80), createSpinner(-Float.MAX_VALUE, Float.MAX_VALUE, bone.extraValue2, BoneValues.ExtraValue2)),
            createHBox(0, createLabel("Extra Value 3", 80), createSpinner(-Float.MAX_VALUE, Float.MAX_VALUE, bone.extraValue3, BoneValues.ExtraValue3)),
            createHBox(0, createLabel("Extra Value 4", 80), createSpinner(-Float.MAX_VALUE, Float.MAX_VALUE, bone.extraValue4, BoneValues.ExtraValue4))
        );
        extraValuesVBox.setPadding(new Insets(20, 0, 0, 16));

        Tab bonePropertiesTab = new Tab("Bone Properties", bonePropertiesVBox);
        bonePropertiesTab.setClosable(false);

        Tab relativeTransformTab = new Tab("Relative Transform", relativeTransformVBox);
        relativeTransformTab.setClosable(false);

        Tab absoluteTransformTab = new Tab("Absolute Transform", new ScrollPane(absoluteTransformVBox));
        absoluteTransformTab.setClosable(false);

        Tab extraTab = new Tab("Extra", extraValuesVBox);
        extraTab.setClosable(false);

        dynamicTabPane.getTabs().addAll(bonePropertiesTab, relativeTransformTab, extraTab);
        if (hasAbsoluteTransform) dynamicTabPane.getTabs().add(2, absoluteTransformTab);
    }

    protected void createIkRelation(IK_Relations ik_Relation) {
        HBox hBox = createHBox(0, createLabel("I_04", 60), createTextField(ik_Relation.i04, IK_Values.I04));
        hBox.setAlignment(Pos.BASELINE_LEFT);
        hBox.setPadding(new Insets(20, 0, 0, 16));

        Tab tab = new Tab("Properties", hBox);
        tab.setClosable(false);

        dynamicTabPane.getTabs().add(tab);
    }

    protected void createIkBone(IK_Bone ik_bone) {
        VBox vBox = new VBox(30,
            createHBox(0, createLabel("Bone Index", 100), createTextField(ik_bone.boneIndex, IK_BoneValues.BoneIndex)), 
            createHBox(0, createLabel("Bone Name", 100), createTextField(ik_bone.boneName, IK_BoneValues.BoneName)), 
            createHBox(0, createLabel("Weight", 100), createTextField(ik_bone.weight, IK_BoneValues.Weight)) 
        );
        vBox.setPadding(new Insets(20, 0, 0, 16));

        Tab tab = new Tab("Bone Properties", vBox);
        tab.setClosable(false);

        dynamicTabPane.getTabs().add(tab);
    }

    private VBox createMiscellaneousVBox() {
        VBox vBox = new VBox(30, 
            createHBox(0, createLabel("Version", 100), createTextField(eskMain.version, EskMainValues.Version)),
            createHBox(0, createLabel("I_10", 100), createTextField(eskMain.i10, EskMainValues.I10)),
            createHBox(0, createLabel("I_12", 100), createTextField(eskMain.i12, EskMainValues.I12)),
            createHBox(0, createLabel("I_24", 100) , createTextField(eskMain.i24, EskMainValues.I24)),
            createSkeletonProperties()[0],
            createSkeletonProperties()[1],
            createSkeletonProperties()[2]
        );
        vBox.setPadding(new Insets(20, 0, 0, 16));

        return  vBox;
    }

    protected HBox[] createSkeletonProperties() {
        return new HBox[] {
            createHBox(0, createLabel("Skeleton Flag", 100) , createTextField(skeleton.flag, SkeletonValues.Flag)),
            createHBox(0, createLabel("Skeleton ID", 100), createTextField(skeleton.skeletonId, SkeletonValues.Skeleton_ID)),
            createHBox(0, createLabel("Use Unknown 2", 100), createCheckBox(SkeletonValues.UseExtraValues))
        };
    }

    private TextField createTextField(int value, EskMainValues eskMainValue) {
        TextField textField = new TextField(String.valueOf(value));
        textField.textProperty().addListener((obs, oldText, newText) -> {
            try {
                switch (eskMainValue) {
                    case Version -> eskMain.version = Integer.parseInt(newText);
                    case I10 -> eskMain.i10 = Integer.parseInt(newText);
                    case I12 -> eskMain.i12 = Integer.parseInt(newText);
                    case I24 -> eskMain.i24 = Integer.parseInt(newText);
                    default -> throw new IllegalArgumentException("Unexpected value: " + eskMainValue);
                }
            } catch (NumberFormatException e) {
            }
        });

        return textField;
    }

    private TextField createTextField(Number value, SkeletonValues skeletonValue) {
        TextField textField = new TextField(String.valueOf(value));
        textField.textProperty().addListener((obs, oldText, newText) -> {
            try {
                switch (skeletonValue) {
                    case Flag -> skeleton.flag = Short.parseShort(newText);
                    case Skeleton_ID -> skeleton.skeletonId = BigInteger.valueOf(Long.parseLong(newText));
                    default -> throw new IllegalArgumentException("Unexpected value: " + skeletonValue);
                }  
            } catch (NumberFormatException e) {
            }
        });

        return textField;
    }

    private TextField createTextField(Number value, IK_Values ikValue) {
        TextField textField = new TextField(String.valueOf(value));
        textField.textProperty().addListener((obs, oldText, newText) -> {
            try {
                ikHashMap.get(currentIkEntry).i04 = Integer.parseInt(newText);
            } catch (NumberFormatException e) {
            }
        });

        return textField;
    }

    private TextField createTextField(Object value, IK_BoneValues ik_BoneValue) {
        TextField textField = new TextField(String.valueOf(value));
        textField.textProperty().addListener((obs, oldText, newText) -> {
            try {
                switch (ik_BoneValue) {
                    case BoneIndex -> ikBoneHashMap.get(currentIkEntry).boneIndex = Integer.parseInt(newText);
                    case BoneName -> ikBoneHashMap.get(currentIkEntry).boneName = newText;
                    case Weight -> ikBoneHashMap.get(currentIkEntry).weight = Float.parseFloat(newText);
                }  
            } catch (NumberFormatException e) {
            }
        });

        return textField;
    }

    private TextField createTextField(Object value, BoneValues boneValue) {
        TextField textField = new TextField(String.valueOf(value));
        textField.textProperty().addListener((obs, oldText, newText) -> {
            try {
                switch (boneValue) {
                    case BoneName -> {
                        bonesHashMap.get(currentBoneEntry).boneName = newText;
                        currentBoneEntry.setValue(newText);
                    }
                    case IK_Flag -> bonesHashMap.get(currentBoneEntry).ikFlag = Short.parseShort(newText);
                    default -> throw new IllegalArgumentException("Unexpected value: " + boneValue);
                }  
            } catch (NumberFormatException e) {
            }
        });

        return textField;
    }

    private Spinner<Number> createSpinner(Number MIN_VALUE, Number MAX_VALUE, Number value, BoneValues boneValue) {
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
                switch (boneValue) {
                    case PositionX -> bonesHashMap.get(currentBoneEntry).positionX = newValue.floatValue();
                    case PositionY -> bonesHashMap.get(currentBoneEntry).positionY = newValue.floatValue();
                    case PositionZ -> bonesHashMap.get(currentBoneEntry).positionZ = newValue.floatValue();
                    case PositionW -> bonesHashMap.get(currentBoneEntry).positionW = newValue.floatValue();
                    case RotationX -> bonesHashMap.get(currentBoneEntry).rotationX = newValue.floatValue();
                    case RotationY -> bonesHashMap.get(currentBoneEntry).rotationY = newValue.floatValue();
                    case RotationZ -> bonesHashMap.get(currentBoneEntry).rotationZ = newValue.floatValue();
                    case RotationW -> bonesHashMap.get(currentBoneEntry).rotationW = newValue.floatValue();
                    case ScaleX -> bonesHashMap.get(currentBoneEntry).scaleX = newValue.floatValue();
                    case ScaleY -> bonesHashMap.get(currentBoneEntry).scaleY = newValue.floatValue();
                    case ScaleZ -> bonesHashMap.get(currentBoneEntry).scaleZ = newValue.floatValue();
                    case ScaleW -> bonesHashMap.get(currentBoneEntry).scaleW = newValue.floatValue();
                    case Line1X -> bonesHashMap.get(currentBoneEntry).line1X = newValue.floatValue();
                    case Line1Y -> bonesHashMap.get(currentBoneEntry).line1Y = newValue.floatValue();
                    case Line1Z -> bonesHashMap.get(currentBoneEntry).line1Z = newValue.floatValue();
                    case Line1W -> bonesHashMap.get(currentBoneEntry).line1W = newValue.floatValue();
                    case Line2X -> bonesHashMap.get(currentBoneEntry).line2X = newValue.floatValue();
                    case Line2Y -> bonesHashMap.get(currentBoneEntry).line2Y = newValue.floatValue();
                    case Line2Z -> bonesHashMap.get(currentBoneEntry).line2Z = newValue.floatValue();
                    case Line2W -> bonesHashMap.get(currentBoneEntry).line2W = newValue.floatValue();
                    case Line3X -> bonesHashMap.get(currentBoneEntry).line3X = newValue.floatValue();
                    case Line3Y -> bonesHashMap.get(currentBoneEntry).line3Y = newValue.floatValue();
                    case Line3Z -> bonesHashMap.get(currentBoneEntry).line3Z = newValue.floatValue();
                    case Line3W -> bonesHashMap.get(currentBoneEntry).line3W = newValue.floatValue();
                    case Line4X -> bonesHashMap.get(currentBoneEntry).line4X = newValue.floatValue();
                    case Line4Y -> bonesHashMap.get(currentBoneEntry).line4Y = newValue.floatValue();
                    case Line4Z -> bonesHashMap.get(currentBoneEntry).line4Z = newValue.floatValue();
                    case Line4W -> bonesHashMap.get(currentBoneEntry).line4W = newValue.floatValue();
                    case ExtraValue1 -> bonesHashMap.get(currentBoneEntry).extraValue1 = newValue.intValue();
                    case ExtraValue2 -> bonesHashMap.get(currentBoneEntry).extraValue2 = newValue.intValue();
                    case ExtraValue3 -> bonesHashMap.get(currentBoneEntry).extraValue3 = newValue.intValue();
                    case ExtraValue4 -> bonesHashMap.get(currentBoneEntry).extraValue4 = newValue.intValue();
                    default -> throw new IllegalArgumentException("Unexpected value: " + boneValue);
                }
            }
        });

        return spinner;
    }

    private CheckBox createCheckBox(SkeletonValues skeletonValue) {
        CheckBox checkBox = new CheckBox();
        checkBox.setSelected(skeleton.useExtraValues);
        checkBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
            skeleton.useExtraValues = newValue;
        });

        return checkBox;
    }

    protected Label createLabel(String text, int width) {
        Label label = new Label(text);
        if (width != 0) label.setPrefWidth(width);

        return label;
    }

    protected HBox createHBox(int width, Label label, Node node) {
        HBox hBox = new HBox(width, label, node);
        hBox.setAlignment(Pos.CENTER_LEFT);

        return hBox;
    }

    public void bonesActionListener() {
        bonesTreeView.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue == null || newValue.getValue() == null) return;

            currentBoneEntry = newValue;

            int index = dynamicTabPane.getSelectionModel().getSelectedIndex();
            dynamicTabPane.getTabs().clear();
            createBones(bonesHashMap.get(newValue));
            dynamicTabPane.getSelectionModel().select(index);
        });
    }

    public void ikActionListener() {
        ikTreeView.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue == null || newValue.getParent() == null) return;

            currentIkEntry = newValue;

            if (newValue.getValue().contains("Relation")) {
                dynamicTabPane.getTabs().clear();
                createIkRelation(ikHashMap.get(newValue));
            }
            else {
                dynamicTabPane.getTabs().clear();
                createIkBone(ikBoneHashMap.get(newValue));
            }
        });
    }

    private void tabsActionListener() {
        eskTabPane.getSelectionModel().selectedItemProperty().addListener((obsevable, oldTab, newTab) -> {
            if (newTab == null) return;

            if (eskTabPane.getSelectionModel().getSelectedIndex() < 0) return;

            switch (eskTabPane.getSelectionModel().getSelectedIndex()) {
                case 0 -> {
                    ikTreeView.getSelectionModel().clearSelection();
                    dynamicTabPane.getTabs().clear();

                    eskTabPane.getTabs().forEach(tab -> tab.setContent(null));
                    eskTabPane.getTabs().get(0).setContent(bonesTreeView);

                    bonesTreeView.getSelectionModel().select(currentBoneEntry);
                }
                case 1 -> {
                    bonesTreeView.getSelectionModel().clearSelection();
                    dynamicTabPane.getTabs().clear();

                    eskTabPane.getTabs().forEach(tab -> tab.setContent(null));
                    eskTabPane.getTabs().get(1).setContent(ikTreeView);

                    ikTreeView.getSelectionModel().select(currentIkEntry);

                }
                case 2 -> {
                    bonesTreeView.getSelectionModel().clearSelection();
                    ikTreeView.getSelectionModel().clearSelection();
                    dynamicTabPane.getTabs().clear();

                    eskTabPane.getTabs().forEach(tab -> tab.setContent(null));
                    eskTabPane.getTabs().get(2).setContent(createMiscellaneousVBox());
                }
            }
        });
    }
    
    public void eskReader(Path path) {
        try(FileChannel channel = FileChannel.open(path, StandardOpenOption.READ)) {
            ByteBuffer shortBuffer = ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN);
            ByteBuffer intBuffer = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);

            channel.position(8);
            channel.read(shortBuffer);
            shortBuffer.flip();
            eskMain.version = toUShort(shortBuffer.getShort());

            channel.position(10);
            shortBuffer.clear();
            channel.read(shortBuffer);
            shortBuffer.flip();
            eskMain.i10 = toUShort(shortBuffer.getShort());

            channel.position(12);
            channel.read(intBuffer);
            intBuffer.flip();
            eskMain.i12 = intBuffer.getInt();

            channel.position(16);
            intBuffer.clear();
            channel.read(intBuffer);
            intBuffer.flip();
            int skeletonOffset = intBuffer.getInt();

            eskBoneReader(channel, skeletonOffset);

            channel.position(24);
            intBuffer.clear();
            channel.read(intBuffer);
            intBuffer.flip();
            eskMain.i24 = intBuffer.getInt();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    protected void eskBoneReader(FileChannel channel, int skeletonOffset) {
        try {
            ByteBuffer byteBuffer = ByteBuffer.allocate(1).order(ByteOrder.LITTLE_ENDIAN);
            ByteBuffer shortBuffer = ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN);
            ByteBuffer intBuffer = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
            ByteBuffer longBuffer = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN);
            ByteBuffer dynamicStringBuffer;

            channel.position(skeletonOffset);
            shortBuffer.clear();
            channel.read(shortBuffer);
            shortBuffer.flip();
            short boneCount = shortBuffer.getShort();

            channel.position(skeletonOffset + 2);
            shortBuffer.clear();
            channel.read(shortBuffer);
            shortBuffer.flip();
            skeleton.flag = shortBuffer.getShort();

            channel.position(skeletonOffset + 4);
            intBuffer.clear();
            channel.read(intBuffer);
            intBuffer.flip();
            int boneIndexOffset = intBuffer.getInt() + skeletonOffset;

            channel.position(skeletonOffset + 8);
            intBuffer.clear();
            channel.read(intBuffer);
            intBuffer.flip();
            int nameTableOffset = intBuffer.getInt() + skeletonOffset;

            channel.position(skeletonOffset + 12);
            intBuffer.clear();
            channel.read(intBuffer);
            intBuffer.flip();
            int skinningMatrixOffset = intBuffer.getInt() + skeletonOffset;

            channel.position(skeletonOffset + 16);
            intBuffer.clear();
            channel.read(intBuffer);
            intBuffer.flip();
            int transformMatrixTableOffset = intBuffer.getInt();
            int transformMatrixOffset = (transformMatrixTableOffset != 0) ? transformMatrixTableOffset + skeletonOffset : 0;

            channel.position(skeletonOffset + 20);
            intBuffer.clear();
            channel.read(intBuffer);
            intBuffer.flip();
            int ikOffset = intBuffer.getInt();

            channel.position(skeletonOffset + 24);
            intBuffer.clear();
            channel.read(intBuffer);
            intBuffer.flip();
            int boneExtraInfoOffset = intBuffer.getInt() + skeletonOffset;

            channel.position(skeletonOffset + 28);
            longBuffer.clear();
            channel.read(longBuffer);
            longBuffer.flip();
            skeleton.skeletonId = BigInteger.valueOf(longBuffer.getLong());

            skeleton.useExtraValues = boneExtraInfoOffset - skeletonOffset != 0;

            allBones = new ArrayList<>(boneCount);
            bonesHashMap = new HashMap<>(boneCount);

            for (int i = 0; i < boneCount; i++) {
                allBones.add(new TreeItem<>("dummy"));
            }

            if (boneCount > 0) {
                bonesTreeView.setRoot(allBones.get(0));
            }

            for (int i = 0; i < boneCount; i++) {
                bonesHashMap.put(allBones.get(i), new Bone());

                channel.position(boneIndexOffset + i * 8 + 2);
                shortBuffer.clear();
                channel.read(shortBuffer);
                shortBuffer.flip();
                short childIndex = shortBuffer.getShort();

                if (childIndex == -1 && i == 0 && boneCount > 1) childIndex = 1;

                if (childIndex != -1) {
                    allBones.get(i).getChildren().add(allBones.get(childIndex));
                }

                channel.position(boneIndexOffset + i * 8 + 4);
                shortBuffer.clear();
                channel.read(shortBuffer);
                shortBuffer.flip();
                short siblingIndex = shortBuffer.getShort();

                if (siblingIndex != -1) {
                    allBones.get(i).getParent().getChildren().add(allBones.get(siblingIndex));
                }

                channel.position(boneIndexOffset + i * 8 + 6);
                shortBuffer.clear();
                channel.read(shortBuffer);
                shortBuffer.flip();
                bonesHashMap.get(allBones.get(i)).ikFlag = shortBuffer.getShort();

                channel.position(nameTableOffset + i * 4);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                int boneNameOffset = intBuffer.getInt() + skeletonOffset;

                if (boneNameOffset != 0) {
                    int counter = 0;

                    do {
                        channel.position(boneNameOffset + counter);
                        byteBuffer.clear();
                        channel.read(byteBuffer);
                        byteBuffer.flip();
                        counter++;
                    } while (byteBuffer.get() != 0);

                    dynamicStringBuffer = ByteBuffer.allocate(counter);

                    channel.position(boneNameOffset);
                    dynamicStringBuffer.clear();
                    channel.read(dynamicStringBuffer);
                    dynamicStringBuffer.flip();
                    allBones.get(i).setValue(new String(dynamicStringBuffer.array(), StandardCharsets.ISO_8859_1));
                    bonesHashMap.get(allBones.get(i)).boneName = allBones.get(i).getValue();
                }

                channel.position(skinningMatrixOffset + i * 48);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                bonesHashMap.get(allBones.get(i)).positionX = intBuffer.getFloat();

                channel.position(skinningMatrixOffset + i * 48 + 4);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                bonesHashMap.get(allBones.get(i)).positionY = intBuffer.getFloat();

                channel.position(skinningMatrixOffset + i * 48 + 8);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                bonesHashMap.get(allBones.get(i)).positionZ = intBuffer.getFloat();

                channel.position(skinningMatrixOffset + i * 48 + 12);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                bonesHashMap.get(allBones.get(i)).positionW = intBuffer.getFloat();

                channel.position(skinningMatrixOffset + i * 48 + 16);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                bonesHashMap.get(allBones.get(i)).rotationX = intBuffer.getFloat();

                channel.position(skinningMatrixOffset + i * 48 + 20);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                bonesHashMap.get(allBones.get(i)).rotationY = intBuffer.getFloat();

                channel.position(skinningMatrixOffset + i * 48 + 24);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                bonesHashMap.get(allBones.get(i)).rotationZ = intBuffer.getFloat();

                channel.position(skinningMatrixOffset + i * 48 + 28);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                bonesHashMap.get(allBones.get(i)).rotationW = intBuffer.getFloat();

                channel.position(skinningMatrixOffset + i * 48 + 32);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                bonesHashMap.get(allBones.get(i)).scaleX = intBuffer.getFloat();

                channel.position(skinningMatrixOffset + i * 48 + 36);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                bonesHashMap.get(allBones.get(i)).scaleY = intBuffer.getFloat();

                channel.position(skinningMatrixOffset + i * 48 + 40);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                bonesHashMap.get(allBones.get(i)).scaleZ = intBuffer.getFloat();

                channel.position(skinningMatrixOffset + i * 48 + 44);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                bonesHashMap.get(allBones.get(i)).scaleW = intBuffer.getFloat();

                if (transformMatrixOffset != 0) {
                    hasAbsoluteTransform = true;

                    channel.position(transformMatrixOffset + i * 64);
                    intBuffer.clear();
                    channel.read(intBuffer);
                    intBuffer.flip();
                    bonesHashMap.get(allBones.get(i)).line1X = intBuffer.getFloat();

                    channel.position(transformMatrixOffset + i * 64 + 4);
                    intBuffer.clear();
                    channel.read(intBuffer);
                    intBuffer.flip();
                    bonesHashMap.get(allBones.get(i)).line1Y = intBuffer.getFloat();

                    channel.position(transformMatrixOffset + i * 64 + 8);
                    intBuffer.clear();
                    channel.read(intBuffer);
                    intBuffer.flip();
                    bonesHashMap.get(allBones.get(i)).line1Z = intBuffer.getFloat();

                    channel.position(transformMatrixOffset + i * 64 + 12);
                    intBuffer.clear();
                    channel.read(intBuffer);
                    intBuffer.flip();
                    bonesHashMap.get(allBones.get(i)).line1W = intBuffer.getFloat();

                    channel.position(transformMatrixOffset + i * 64 + 16);
                    intBuffer.clear();
                    channel.read(intBuffer);
                    intBuffer.flip();
                    bonesHashMap.get(allBones.get(i)).line2X = intBuffer.getFloat();

                    channel.position(transformMatrixOffset + i * 64 + 20);
                    intBuffer.clear();
                    channel.read(intBuffer);
                    intBuffer.flip();
                    bonesHashMap.get(allBones.get(i)).line2Y = intBuffer.getFloat();

                    channel.position(transformMatrixOffset + i * 64 + 24);
                    intBuffer.clear();
                    channel.read(intBuffer);
                    intBuffer.flip();
                    bonesHashMap.get(allBones.get(i)).line2Z = intBuffer.getFloat();

                    channel.position(transformMatrixOffset + i * 64 + 28);
                    intBuffer.clear();
                    channel.read(intBuffer);
                    intBuffer.flip();
                    bonesHashMap.get(allBones.get(i)).line2W = intBuffer.getFloat();

                    channel.position(transformMatrixOffset + i * 64 + 32);
                    intBuffer.clear();
                    channel.read(intBuffer);
                    intBuffer.flip();
                    bonesHashMap.get(allBones.get(i)).line3X = intBuffer.getFloat();

                    channel.position(transformMatrixOffset + i * 64 + 36);
                    intBuffer.clear();
                    channel.read(intBuffer);
                    intBuffer.flip();
                    bonesHashMap.get(allBones.get(i)).line3Y = intBuffer.getFloat();

                    channel.position(transformMatrixOffset + i * 64 + 40);
                    intBuffer.clear();
                    channel.read(intBuffer);
                    intBuffer.flip();
                    bonesHashMap.get(allBones.get(i)).line3Z = intBuffer.getFloat();

                    channel.position(transformMatrixOffset + i * 64 + 44);
                    intBuffer.clear();
                    channel.read(intBuffer);
                    intBuffer.flip();
                    bonesHashMap.get(allBones.get(i)).line3W = intBuffer.getFloat();

                    channel.position(transformMatrixOffset + i * 64 + 48);
                    intBuffer.clear();
                    channel.read(intBuffer);
                    intBuffer.flip();
                    bonesHashMap.get(allBones.get(i)).line4X = intBuffer.getFloat();

                    channel.position(transformMatrixOffset + i * 64 + 52);
                    intBuffer.clear();
                    channel.read(intBuffer);
                    intBuffer.flip();
                    bonesHashMap.get(allBones.get(i)).line4Y = intBuffer.getFloat();

                    channel.position(transformMatrixOffset + i * 64 + 56);
                    intBuffer.clear();
                    channel.read(intBuffer);
                    intBuffer.flip();
                    bonesHashMap.get(allBones.get(i)).line4Z = intBuffer.getFloat();

                    channel.position(transformMatrixOffset + i * 64 + 60);
                    intBuffer.clear();
                    channel.read(intBuffer);
                    intBuffer.flip();
                    bonesHashMap.get(allBones.get(i)).line4W = intBuffer.getFloat();
                }
                
                channel.position(boneExtraInfoOffset + i * 8);
                shortBuffer.clear();
                channel.read(shortBuffer);
                shortBuffer.flip();
                bonesHashMap.get(allBones.get(i)).extraValue1 = toUShort(shortBuffer.getShort());

                channel.position(boneExtraInfoOffset + i * 8 + 2);
                shortBuffer.clear();
                channel.read(shortBuffer);
                shortBuffer.flip();
                bonesHashMap.get(allBones.get(i)).extraValue2 = toUShort(shortBuffer.getShort());

                channel.position(boneExtraInfoOffset + i * 8 + 4);
                shortBuffer.clear();
                channel.read(shortBuffer);
                shortBuffer.flip();
                bonesHashMap.get(allBones.get(i)).extraValue3 = toUShort(shortBuffer.getShort());

                channel.position(boneExtraInfoOffset + i * 8 + 6);
                shortBuffer.clear();
                channel.read(shortBuffer);
                shortBuffer.flip();
                bonesHashMap.get(allBones.get(i)).extraValue4 = toUShort(shortBuffer.getShort());
            }

            if (ikOffset > 0) {
                ikOffset += skeletonOffset;

                ikTreeView.setRoot(new TreeItem<>("dummy"));
                ikTreeView.setShowRoot(false);

                channel.position(ikOffset);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                int ikCount =  intBuffer.getInt();

                ikHashMap = HashMap.newHashMap(ikCount);

                ikOffset += 4;

                for (int i = 0; i < ikCount; i++) {
                    IK_Relations ik_Relations = new IK_Relations();
                    ikTreeView.getRoot().getChildren().add(new TreeItem<>("IK Relation " + i));
                    ikHashMap.put(ikTreeView.getRoot().getChildren().get(i), ik_Relations);

                    channel.position(ikOffset + i * 18);
                    shortBuffer.clear();
                    channel.read(shortBuffer);
                    shortBuffer.flip();
                    int type = toUShort(shortBuffer.getShort());

                    if (type != 1) {
                        Platform.runLater(() -> {
                            Popups.UnknownIK(type);
                        });
                        System.exit(0);
                    }

                    channel.position(ikOffset + i * 18 + 4);
                    byteBuffer.clear();
                    channel.read(byteBuffer);
                    byteBuffer.flip();
                    ik_Relations.i04 = toUByte(byteBuffer.get());

                    channel.position(ikOffset + i * 18 + 5);
                    byteBuffer.clear();
                    channel.read(byteBuffer);
                    byteBuffer.flip();
                    int ikBoneCount = toUByte(byteBuffer.get());

                    ikOffset += 6;

                    for (int j = 0; j < ikBoneCount + 1; j++) {
                        IK_Bone ik_Bone = new IK_Bone();
                        ikTreeView.getRoot().getChildren().get(i).getChildren().add(new TreeItem<>("IK Bone " + j));
                        ikBoneHashMap.put(ikTreeView.getRoot().getChildren().get(i).getChildren().get(j), ik_Bone);

                        channel.position(ikOffset + i * 18 + j * 2);
                        shortBuffer.clear();
                        channel.read(shortBuffer);
                        shortBuffer.flip();
                        ik_Bone.boneIndex = toUShort(shortBuffer.getShort());
                        ik_Bone.boneName = allBones.get(ik_Bone.boneIndex).getValue();

                        channel.position(ikOffset + i * 18 + j * 4 + 6);
                        intBuffer.clear();
                        channel.read(intBuffer);
                        intBuffer.flip();
                        ik_Bone.weight = intBuffer.getFloat();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void eskWriter(Path path) {
        try(FileChannel channel = FileChannel.open(path, StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            ByteBuffer shortBuffer = ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN);
            ByteBuffer intBuffer = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);

            int skeletonOffset = 32;

            channel.position(0);
            channel.write(ByteBuffer.wrap(new byte[]{0x23, 0x45, 0x53, (byte)0x4B}));

            channel.position(4);
            channel.write(ByteBuffer.wrap(new byte[]{(byte)0xFE, (byte)0xFF}));

            //header size (28)
            channel.position(6);
            channel.write(ByteBuffer.wrap(new byte[]{(byte)0x1C, 0x00}));

            channel.position(8);
            shortBuffer.putShort((short) eskMain.version);
            shortBuffer.flip();
            channel.write(shortBuffer);

            channel.position(10);
            shortBuffer.clear();
            shortBuffer.putShort((short) eskMain.i10);
            shortBuffer.flip();
            channel.write(shortBuffer);

            channel.position(12);
            intBuffer.putInt(eskMain.i12);
            intBuffer.flip();
            channel.write(intBuffer);

            channel.position(16);
            intBuffer.clear();
            intBuffer.putInt(skeletonOffset);
            intBuffer.flip();
            channel.write(intBuffer);

            channel.position(24);
            intBuffer.clear();
            intBuffer.putInt(eskMain.i24);
            intBuffer.flip();
            channel.write(intBuffer);

            eskBoneWriter(channel, skeletonOffset);
        } catch(IOException e) {
            e.printStackTrace();
        }

    }

    protected void eskBoneWriter(FileChannel channel, int skeletonOffset) {
        try {
            ByteBuffer byteBuffer = ByteBuffer.allocate(1).order(ByteOrder.LITTLE_ENDIAN);
            ByteBuffer shortBuffer = ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN);
            ByteBuffer intBuffer = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
            ByteBuffer longBuffer = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN);
            ByteBuffer dynamicStringBuffer;

            int headerSize = 28;
            int offsetDataSize = 40;
            int boneIndexOffset = headerSize + offsetDataSize - skeletonOffset;
            int nameTableOffset = 8 * allBones.size() + boneIndexOffset;
            int boneNameOffset = 4 * allBones.size() + nameTableOffset;
            int skinningMatrixOffset = 4 * allBones.size() + allBones.stream().mapToInt(arr -> arr.getValue().length()).sum() + nameTableOffset;
            int transformMatrixOffset = eskTabPane.getTabs().size() == 4 ? 48 * allBones.size() + skinningMatrixOffset : 0;
            int ikOffset = 64 * allBones.size() + transformMatrixOffset;
            
            if ((skinningMatrixOffset + skeletonOffset) % 16 != 0) {
                skinningMatrixOffset += (16 - (skinningMatrixOffset + skeletonOffset) % 16);
            }

            if((transformMatrixOffset + skeletonOffset) % 16 != 0) {
                transformMatrixOffset += (16 - (transformMatrixOffset + skeletonOffset) % 16);
            }

            if((ikOffset + skeletonOffset) % 16 != 0) {
                ikOffset += (16 - (ikOffset + skeletonOffset) % 16);
            }

            int boneExtraInfoOffset = 4 + 6 * ikHashMap.size() + 6 * ikBoneHashMap.size() + ikOffset;

            channel.position(skeletonOffset);
            shortBuffer.putShort((short) allBones.size());
            shortBuffer.flip();
            channel.write(shortBuffer);

            channel.position(skeletonOffset + 2);
            shortBuffer.clear();
            shortBuffer.putShort(skeleton.flag);
            shortBuffer.flip();
            channel.write(shortBuffer);

            channel.position(skeletonOffset + 4);
            intBuffer.clear();
            intBuffer.putInt(boneIndexOffset);
            intBuffer.flip();
            channel.write(intBuffer);

            channel.position(skeletonOffset + 8);
            intBuffer.clear();
            intBuffer.putInt(nameTableOffset);
            intBuffer.flip();
            channel.write(intBuffer);

            channel.position(skeletonOffset + 12);
            intBuffer.clear();
            intBuffer.putInt(skinningMatrixOffset);
            intBuffer.flip();
            channel.write(intBuffer);

            channel.position(skeletonOffset + 16);
            intBuffer.clear();
            intBuffer.putInt(transformMatrixOffset);
            intBuffer.flip();
            channel.write(intBuffer);

            channel.position(skeletonOffset + 20);
            intBuffer.clear();
            intBuffer.putInt(ikOffset);
            intBuffer.flip();
            channel.write(intBuffer);

            channel.position(skeletonOffset + 24);
            intBuffer.clear();
            intBuffer.putInt(boneExtraInfoOffset);
            intBuffer.flip();
            channel.write(intBuffer);

            channel.position(skeletonOffset + 28);
            longBuffer.putLong(skeleton.skeletonId.longValue());
            longBuffer.flip();
            channel.write(longBuffer);

            for (int i = 0; i < allBones.size(); i++) {
                short parentIndex;
                short siblingIndex;
                short childIndex;

                if (allBones.get(i).getParent() != null) {
                    parentIndex = (short) allBones.indexOf(allBones.get(i).getParent());
                }
                else {
                    parentIndex = -1;
                }

                if (!allBones.get(i).getChildren().isEmpty()) {
                    childIndex = (short) allBones.indexOf(allBones.get(i).getChildren().get(0));
                }
                else {
                    childIndex = -1;
                }

                if (allBones.get(i).nextSibling() != null) {
                    siblingIndex = (short) allBones.indexOf(allBones.get(i).nextSibling());
                }
                else {
                    siblingIndex = -1;
                }

                channel.position(boneIndexOffset + skeletonOffset + i * 8);
                shortBuffer.clear();
                shortBuffer.putShort(parentIndex);
                shortBuffer.flip();
                channel.write(shortBuffer);

                channel.position(boneIndexOffset + skeletonOffset + i * 8 + 2);
                shortBuffer.clear();
                shortBuffer.putShort(childIndex);
                shortBuffer.flip();
                channel.write(shortBuffer);

                channel.position(boneIndexOffset + skeletonOffset + i * 8 + 4);
                shortBuffer.clear();
                shortBuffer.putShort(siblingIndex);
                shortBuffer.flip();
                channel.write(shortBuffer);  

                channel.position(boneIndexOffset + skeletonOffset + i * 8 + 6);
                shortBuffer.clear();
                shortBuffer.putShort(bonesHashMap.get(allBones.get(i)).ikFlag);
                shortBuffer.flip();
                channel.write(shortBuffer);

                channel.position(nameTableOffset + skeletonOffset + i * 4);
                intBuffer.clear();
                intBuffer.putInt(boneNameOffset);
                intBuffer.flip();
                channel.write(intBuffer);

                if (boneNameOffset != 0) {
                    dynamicStringBuffer = ByteBuffer.allocate(allBones.get(i).getValue().length());

                    channel.position(boneNameOffset + skeletonOffset);
                    dynamicStringBuffer = ByteBuffer.wrap(allBones.get(i).getValue().getBytes());
                    channel.write(dynamicStringBuffer);
                    boneNameOffset += allBones.get(i).getValue().length();
                }

                channel.position(skinningMatrixOffset + skeletonOffset + i * 48);
                intBuffer.clear();
                intBuffer.putFloat(bonesHashMap.get(allBones.get(i)).positionX);
                intBuffer.flip();
                channel.write(intBuffer);

                channel.position(skinningMatrixOffset + skeletonOffset + i * 48 + 4);
                intBuffer.clear();
                intBuffer.putFloat(bonesHashMap.get(allBones.get(i)).positionY);
                intBuffer.flip();
                channel.write(intBuffer);

                channel.position(skinningMatrixOffset + skeletonOffset + i * 48 + 8);
                intBuffer.clear();
                intBuffer.putFloat(bonesHashMap.get(allBones.get(i)).positionZ);
                intBuffer.flip();
                channel.write(intBuffer);

                channel.position(skinningMatrixOffset + skeletonOffset + i * 48 + 12);
                intBuffer.clear();
                intBuffer.putFloat(bonesHashMap.get(allBones.get(i)).positionW);
                intBuffer.flip();
                channel.write(intBuffer);

                channel.position(skinningMatrixOffset + skeletonOffset + i * 48 + 16);
                intBuffer.clear();
                intBuffer.putFloat(bonesHashMap.get(allBones.get(i)).rotationX);
                intBuffer.flip();
                channel.write(intBuffer);

                channel.position(skinningMatrixOffset + skeletonOffset + i * 48 + 20);
                intBuffer.clear();
                intBuffer.putFloat(bonesHashMap.get(allBones.get(i)).rotationY);
                intBuffer.flip();
                channel.write(intBuffer);

                channel.position(skinningMatrixOffset + skeletonOffset + i * 48 + 24);
                intBuffer.clear();
                intBuffer.putFloat(bonesHashMap.get(allBones.get(i)).rotationZ);
                intBuffer.flip();
                channel.write(intBuffer);

                channel.position(skinningMatrixOffset + skeletonOffset + i * 48 + 28);
                intBuffer.clear();
                intBuffer.putFloat(bonesHashMap.get(allBones.get(i)).rotationW);
                intBuffer.flip();
                channel.write(intBuffer);

                channel.position(skinningMatrixOffset + skeletonOffset + i * 48 + 32);
                intBuffer.clear();
                intBuffer.putFloat(bonesHashMap.get(allBones.get(i)).scaleX);
                intBuffer.flip();
                channel.write(intBuffer);

                channel.position(skinningMatrixOffset + skeletonOffset + i * 48 + 36);
                intBuffer.clear();
                intBuffer.putFloat(bonesHashMap.get(allBones.get(i)).scaleY);
                intBuffer.flip();
                channel.write(intBuffer);

                channel.position(skinningMatrixOffset + skeletonOffset + i * 48 + 40);
                intBuffer.clear();
                intBuffer.putFloat(bonesHashMap.get(allBones.get(i)).scaleZ);
                intBuffer.flip();
                channel.write(intBuffer);

                channel.position(skinningMatrixOffset + skeletonOffset + i * 48 + 44);
                intBuffer.clear();
                intBuffer.putFloat(bonesHashMap.get(allBones.get(i)).scaleW);
                intBuffer.flip();
                channel.write(intBuffer);

                if (transformMatrixOffset != 0) {
                    channel.position(transformMatrixOffset + skeletonOffset + i * 64);
                    intBuffer.clear();
                    intBuffer.putFloat(bonesHashMap.get(allBones.get(i)).line1X);
                    intBuffer.flip();
                    channel.write(intBuffer);

                    channel.position(transformMatrixOffset + skeletonOffset + i * 64 + 4);
                    intBuffer.clear();
                    intBuffer.putFloat(bonesHashMap.get(allBones.get(i)).line1Y);
                    intBuffer.flip();
                    channel.write(intBuffer);

                    channel.position(transformMatrixOffset + skeletonOffset + i * 64 + 8);
                    intBuffer.clear();
                    intBuffer.putFloat(bonesHashMap.get(allBones.get(i)).line1Z);
                    intBuffer.flip();
                    channel.write(intBuffer);

                    channel.position(transformMatrixOffset + skeletonOffset + i * 64 + 12);
                    intBuffer.clear();
                    intBuffer.putFloat(bonesHashMap.get(allBones.get(i)).line1W);
                    intBuffer.flip();
                    channel.write(intBuffer);

                    channel.position(transformMatrixOffset + skeletonOffset + i * 64 + 16);
                    intBuffer.clear();
                    intBuffer.putFloat(bonesHashMap.get(allBones.get(i)).line2X);
                    intBuffer.flip();
                    channel.write(intBuffer);

                    channel.position(transformMatrixOffset + skeletonOffset + i * 64 + 20);
                    intBuffer.clear();
                    intBuffer.putFloat(bonesHashMap.get(allBones.get(i)).line2Y);
                    intBuffer.flip();
                    channel.write(intBuffer);

                    channel.position(transformMatrixOffset + skeletonOffset + i * 64 + 24);
                    intBuffer.clear();
                    intBuffer.putFloat(bonesHashMap.get(allBones.get(i)).line2Z);
                    intBuffer.flip();
                    channel.write(intBuffer);

                    channel.position(transformMatrixOffset + skeletonOffset + i * 64 + 28);
                    intBuffer.clear();
                    intBuffer.putFloat(bonesHashMap.get(allBones.get(i)).line2W);
                    intBuffer.flip();
                    channel.write(intBuffer);

                    channel.position(transformMatrixOffset + skeletonOffset + i * 64 + 32);
                    intBuffer.clear();
                    intBuffer.putFloat(bonesHashMap.get(allBones.get(i)).line3X);
                    intBuffer.flip();
                    channel.write(intBuffer);

                    channel.position(transformMatrixOffset + skeletonOffset + i * 64 + 36);
                    intBuffer.clear();
                    intBuffer.putFloat(bonesHashMap.get(allBones.get(i)).line3Y);
                    intBuffer.flip();
                    channel.write(intBuffer);

                    channel.position(transformMatrixOffset + skeletonOffset + i * 64 + 40);
                    intBuffer.clear();
                    intBuffer.putFloat(bonesHashMap.get(allBones.get(i)).line3Z);
                    intBuffer.flip();
                    channel.write(intBuffer);

                    channel.position(transformMatrixOffset + skeletonOffset + i * 64 + 44);
                    intBuffer.clear();
                    intBuffer.putFloat(bonesHashMap.get(allBones.get(i)).line3W);
                    intBuffer.flip();
                    channel.write(intBuffer);

                    channel.position(transformMatrixOffset + skeletonOffset + i * 64 + 48);
                    intBuffer.clear();
                    intBuffer.putFloat(bonesHashMap.get(allBones.get(i)).line4X);
                    intBuffer.flip();
                    channel.write(intBuffer);

                    channel.position(transformMatrixOffset + skeletonOffset + i * 64 + 52);
                    intBuffer.clear();
                    intBuffer.putFloat(bonesHashMap.get(allBones.get(i)).line4Y);
                    intBuffer.flip();
                    channel.write(intBuffer);

                    channel.position(transformMatrixOffset + skeletonOffset + i * 64 + 56);
                    intBuffer.clear();
                    intBuffer.putFloat(bonesHashMap.get(allBones.get(i)).line4Z);
                    intBuffer.flip();
                    channel.write(intBuffer);

                    channel.position(transformMatrixOffset + skeletonOffset + i * 64 + 60);
                    intBuffer.clear();
                    intBuffer.putFloat(bonesHashMap.get(allBones.get(i)).line4W);
                    intBuffer.flip();
                    channel.write(intBuffer);
                }
                
                channel.position(boneExtraInfoOffset + skeletonOffset + i * 8);
                shortBuffer.clear();
                shortBuffer.putShort((short) bonesHashMap.get(allBones.get(i)).extraValue1);
                shortBuffer.flip();
                channel.write(shortBuffer);

                channel.position(boneExtraInfoOffset + skeletonOffset + i * 8 + 2);
                shortBuffer.clear();
                shortBuffer.putShort((short) bonesHashMap.get(allBones.get(i)).extraValue2);
                shortBuffer.flip();
                channel.write(shortBuffer);

                channel.position(boneExtraInfoOffset + skeletonOffset + i * 8 + 4);
                shortBuffer.clear();
                shortBuffer.putShort((short) bonesHashMap.get(allBones.get(i)).extraValue3);
                shortBuffer.flip();
                channel.write(shortBuffer);

                channel.position(boneExtraInfoOffset + skeletonOffset + i * 8 + 6);
                shortBuffer.clear();
                shortBuffer.putShort((short) bonesHashMap.get(allBones.get(i)).extraValue4);
                shortBuffer.flip();
                channel.write(shortBuffer);
            }  
            
            if (!ikHashMap.isEmpty()) {
                channel.position(ikOffset + skeletonOffset);
                intBuffer.clear();
                intBuffer.putInt(ikTreeView.getRoot().getChildren().size());
                intBuffer.flip();
                channel.write(intBuffer);

                ikOffset += 4;

                for (int i = 0; i < ikTreeView.getRoot().getChildren().size(); i++) {
                    IK_Relations ik_Relations = ikHashMap.get(ikTreeView.getRoot().getChildren().get(i));

                    channel.position(ikOffset + skeletonOffset + i * 18);
                    channel.write(ByteBuffer.wrap(new byte[] {0x01, 0x00}));

                    channel.position(ikOffset + skeletonOffset + i * 18 + 2);
                    shortBuffer.clear();
                    shortBuffer.putShort((short) (6 + 6 * ikTreeView.getRoot().getChildren().get(i).getChildren().size()));
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    channel.position(ikOffset + skeletonOffset + i * 18 + 4);
                    byteBuffer.clear();
                    byteBuffer.put((byte) ik_Relations.i04);
                    byteBuffer.flip();
                    channel.write(byteBuffer);

                    channel.position(ikOffset + skeletonOffset + i * 18 + 5);
                    byteBuffer.clear();
                    byteBuffer.put((byte) (ikTreeView.getRoot().getChildren().get(i).getChildren().size() - 1));
                    byteBuffer.flip();
                    channel.write(byteBuffer);

                    ikOffset += 6;

                    for (int j = 0; j < ikTreeView.getRoot().getChildren().get(i).getChildren().size(); j++) {
                        IK_Bone ik_Bone = ikBoneHashMap.get(ikTreeView.getRoot().getChildren().get(i).getChildren().get(j));

                        channel.position(ikOffset + skeletonOffset + i * 18 + j * 2);
                        shortBuffer.clear();
                        shortBuffer.putShort((short) ik_Bone.boneIndex);
                        shortBuffer.flip();
                        channel.write(shortBuffer);

                        channel.position(ikOffset + skeletonOffset + i * 18 + j * 4 + 6);
                        intBuffer.clear();
                        intBuffer.putFloat(ik_Bone.weight);
                        intBuffer.flip();
                        channel.write(intBuffer);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static enum EskMainValues {
        Version,
        I10,
        I12,
        I24;
    }

    private static enum SkeletonValues {
        Flag,
        UseExtraValues,
        Skeleton_ID;
    }

    private enum BoneValues {
        BoneName,
        IK_Flag,
        PositionX,
        PositionY,
        PositionZ,
        PositionW,
        RotationX,
        RotationY,
        RotationZ,
        RotationW,
        ScaleX,
        ScaleY,
        ScaleZ,
        ScaleW,
        Line1X,
        Line1Y,
        Line1Z,
        Line1W,
        Line2X,
        Line2Y,
        Line2Z,
        Line2W,
        Line3X,
        Line3Y,
        Line3Z,
        Line3W,
        Line4X,
        Line4Y,
        Line4Z,
        Line4W,
        ExtraValue1,
        ExtraValue2,
        ExtraValue3,
        ExtraValue4
    }

    private static enum IK_Values {
        I04,
    }

    private static enum IK_BoneValues {
        BoneIndex,
        BoneName,
        Weight;
    }
}

class EskMain {
    int version;
    int i10;
    int i12;
    int i24;
}

class Skeleton {
    short flag;
    boolean useExtraValues;
    BigInteger skeletonId;
}

class Bone {
    String boneName = "";
    short ikFlag;
    float positionX;
    float positionY;
    float positionZ;
    float positionW;
    float rotationX;
    float rotationY;
    float rotationZ;
    float rotationW;
    float scaleX;
    float scaleY;
    float scaleZ;
    float scaleW;
    float line1X;
    float line1Y;
    float line1Z;
    float line1W;
    float line2X;
    float line2Y;
    float line2Z;
    float line2W;
    float line3X;
    float line3Y;
    float line3Z;
    float line3W;
    float line4X;
    float line4Y;
    float line4Z;
    float line4W;
    int extraValue1;
    int extraValue2;
    int extraValue3;
    int extraValue4;
}

class IK_Relations {
    int i04;
}

class IK_Bone {
    String boneName;
    int boneIndex;
    float weight;
}
