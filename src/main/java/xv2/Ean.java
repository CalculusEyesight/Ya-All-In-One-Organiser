package xv2;
import static xv2.Unsigned.*;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import javafx.geometry.Insets;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;

public class Ean extends Esk {
    EanMain eanMainEntry = new EanMain();

    TabPane eanTabPane = new TabPane();
    TreeView<String> nodesTreeView = new TreeView<>();

    HashMap<TreeItem<String>, Animation> animationsHashMap = new HashMap<>();
    HashMap<TreeItem<String> ,AnimationNode> animationNodesHashMap = new HashMap<>();
    HashMap<TreeItem<String>, KeyframeType> keyframeTypesHashMap = new HashMap<>();
    HashMap<TreeItem<String>, Keyframe> keyframesHashMap = new HashMap<>();
    TreeItem<String> currentNodeEntry = new TreeItem<>();

    static String[] copyAnimationsValue;
    static String[][] copyNodesValue;
    static String[][][] copyKeyframeTypesValue;
    static String[][][][] copyKeyframesValue;

    static Animation[] copyAnimations;
    static AnimationNode[][] copyAnimationNodes;
    static KeyframeType[][][] copyKeyframeTypes;
    static Keyframe[][][][] copyKeyframes;

    Ean() {
        bonesActionListener();
        tabsActionListener();
        nodesActionListener();
        entriesKeysListener();
        nodesTreeView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    public SplitPane createSplitPane() {
        createTabs(eanTabPane);
        Tab nodesTab = new Tab("Animation Nodes");
        nodesTab.setClosable(false);

        eanTabPane.getTabs().add(0, nodesTab);
        eanTabPane.getSelectionModel().select(0);

        return new SplitPane(eanTabPane, dynamicTabPane);
    }

    private void createAnimation(Animation animation) {
        VBox vBox = new VBox(30,
            createHBox(0, createLabel("Name", 60), createTextField(animation.name, AnimationValues.Name)),
            createHBox(0, createLabel("Index Size:", 60), createLabel(String.valueOf(animation.indexSize), 0)),
            createHBox(0, createLabel("Float Size:", 60), createLabel(String.valueOf(animation.floatSize), 0))
        );
        vBox.setPadding(new Insets(20, 0, 0 ,16));

        Tab tab = new Tab("Animation", vBox);
        tab.setClosable(false);

        dynamicTabPane.getTabs().add(tab);
    }

    private void createKeyframe(Keyframe keyframe) {
        VBox vBox = new VBox(30,
            createHBox(0, createLabel("X", 60), createTextField(keyframe.x, KeyframeValues.X)),
            createHBox(0, createLabel("Y", 60), createTextField(keyframe.y, KeyframeValues.Y)),
            createHBox(0, createLabel("Z", 60), createTextField(keyframe.z, KeyframeValues.Z)),
            createHBox(0, createLabel("W", 60), createTextField(keyframe.w, KeyframeValues.W))
        );
        vBox.setPadding(new Insets(20, 0, 0 ,16));

        Tab tab = new Tab("Keframe", vBox);
        tab.setClosable(false);

        dynamicTabPane.getTabs().add(tab);
    }

    private VBox createMiscellaneousVBox() {
        VBox vBox = new VBox(30,
            createHBox(0, createLabel("I_08", 100), createTextField(eanMainEntry.i08, EanMainValues.I08)),
            createHBox(0, createLabel("Is Camera", 100), createCheckBox(EanMainValues.IsCamera)),
            createHBox(0, createLabel("I_17", 100), createTextField(eanMainEntry.i17, EanMainValues.I17)),
            createSkeletonProperties()[0],
            createSkeletonProperties()[1],
            createSkeletonProperties()[2]
        );
        vBox.setPadding(new Insets(20, 0, 0, 16));

        return vBox;
    }

    private TextField createTextField(Object value, AnimationValues animationValue) {
        TextField textField = new TextField(String.valueOf(value));
        textField.textProperty().addListener((obs, oldText, newText) -> {
            try {
                if (animationValue.equals(AnimationValues.Name)) {
                    animationsHashMap.get(currentNodeEntry).name = newText;
                    currentNodeEntry.setValue(newText);
                }  
            } catch (NumberFormatException e) {
            }
        });

        return textField;
    }

    private TextField createTextField(int value, EanMainValues eanMainEntryValues) {
        TextField textField = new TextField(String.valueOf(value));
        textField.textProperty().addListener((obs, oldText, newText) -> {
            try {
                switch (eanMainEntryValues) {
                    case I08 -> eanMainEntry.i08 = Integer.parseInt(newText);
                    case I17 -> eanMainEntry.i17 = Integer.parseInt(newText);
                    default -> throw new IllegalArgumentException("Unexpected value: " + eanMainEntryValues);
                }
            } catch (NumberFormatException e) {
            }
        });

        return textField;
    }

    private TextField createTextField(float value, KeyframeValues keyframeValue) {
        TextField textField = new TextField(String.valueOf(value));
        textField.textProperty().addListener((obs, oldText, newText) -> {
            try {
                switch (keyframeValue) {
                    case X -> keyframesHashMap.get(currentNodeEntry).x = Float.parseFloat(newText);
                    case Y -> keyframesHashMap.get(currentNodeEntry).y = Float.parseFloat(newText);
                    case Z -> keyframesHashMap.get(currentNodeEntry).z = Float.parseFloat(newText);
                    case W -> keyframesHashMap.get(currentNodeEntry).w = Float.parseFloat(newText);
                }
            } catch (NumberFormatException e) {
            }
        });

        return textField;
    }

    private CheckBox createCheckBox(EanMainValues eanMainEntryValue) {
        CheckBox checkBox = new CheckBox();
        checkBox.setSelected(eanMainEntry.isCamera);
        checkBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
            eanMainEntry.isCamera = newValue;
        });

        return checkBox;
    }

    private void nodesActionListener() {
        SharedContextMenu.getContextMenu().getItems().get(1).setDisable(true);

        nodesTreeView.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue == null || newValue.getValue() == null) return;

            currentNodeEntry = newValue;

            switch (nodesTreeView.getTreeItemLevel(newValue)) {
                case 1 -> {
                    int index = dynamicTabPane.getSelectionModel().getSelectedIndex();
                    dynamicTabPane.getTabs().clear();
                    createAnimation(animationsHashMap.get(newValue));
                    dynamicTabPane.getSelectionModel().select(index);
                    nodesTreeView.setContextMenu(SharedContextMenu.getContextMenu());
                }
                case 4 -> {
                    int index = dynamicTabPane.getSelectionModel().getSelectedIndex();
                    dynamicTabPane.getTabs().clear();
                    createKeyframe(keyframesHashMap.get(newValue));
                    dynamicTabPane.getSelectionModel().select(index);
                    nodesTreeView.setContextMenu(null);
                }
                default -> {
                    dynamicTabPane.getTabs().clear();
                    nodesTreeView.setContextMenu(null);
                } 
            }
        });

        nodesTreeView.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.SECONDARY && nodesTreeView.getTreeItemLevel(currentNodeEntry) == 1) {
                nodesTreeView.getContextMenu().setOnAction(event -> {
                    if (event.getTarget() == nodesTreeView.getContextMenu().getItems().get(0)) {
                        Copy();
                    } else if (event.getTarget() == nodesTreeView.getContextMenu().getItems().get(1)) {
                        Paste();
                    } else if (event.getTarget() == nodesTreeView.getContextMenu().getItems().get(2)) {
                        Delete();
                    } else if (event.getTarget() == nodesTreeView.getContextMenu().getItems().get(3)) {
                        Append();
                    } else if (event.getTarget() == nodesTreeView.getContextMenu().getItems().get(4)) {
                        Insert();
                    }
                });
            }
        });
    }

    private void tabsActionListener() {
        eanTabPane.getSelectionModel().selectedItemProperty().addListener((obsevable, oldTab, newTab) -> {
            if (newTab == null) return;

            if (eanTabPane.getSelectionModel().getSelectedIndex() < 0) return;

            switch (eanTabPane.getSelectionModel().getSelectedIndex()) {
                case 0 -> {
                    bonesTreeView.getSelectionModel().clearSelection();
                    ikTreeView.getSelectionModel().clearSelection();
                    dynamicTabPane.getTabs().clear();

                    eanTabPane.getTabs().forEach(tab -> tab.setContent(null));
                    eanTabPane.getTabs().get(0).setContent(nodesTreeView);

                    nodesTreeView.getSelectionModel().select(currentNodeEntry);
                    
                }
                case 1 -> {
                    nodesTreeView.getSelectionModel().clearSelection();
                    ikTreeView.getSelectionModel().clearSelection();
                    dynamicTabPane.getTabs().clear();

                    eanTabPane.getTabs().forEach(tab -> tab.setContent(null));
                    eanTabPane.getTabs().get(1).setContent(bonesTreeView);

                    bonesTreeView.getSelectionModel().select(currentBoneEntry);
                }
                case 2 -> {
                    bonesTreeView.getSelectionModel().clearSelection();
                    dynamicTabPane.getTabs().clear();

                    eanTabPane.getTabs().forEach(tab -> tab.setContent(null));
                    eanTabPane.getTabs().get(2).setContent(ikTreeView);

                    ikTreeView.getSelectionModel().select(currentIkEntry);
                }
                case 3 -> {
                    bonesTreeView.getSelectionModel().clearSelection();
                    ikTreeView.getSelectionModel().clearSelection();
                    dynamicTabPane.getTabs().clear();

                    eanTabPane.getTabs().forEach(tab -> tab.setContent(null));
                    eanTabPane.getTabs().get(3).setContent(createMiscellaneousVBox());
                }
            }
        });
    }
    
    private void entriesKeysListener() {
        nodesTreeView.setOnKeyPressed(e -> {
            if (e.isControlDown() && e.getCode() == KeyCode.C)  Copy();
            else if (e.isControlDown() && e.getCode() == KeyCode.V) Paste();
            else if (e.getCode() == KeyCode.DELETE) Delete();
            else if (e.isControlDown() && e.getCode() == KeyCode.A) Append();
            else if (e.isControlDown() && e.getCode() == KeyCode.I) Insert();
        });
    }

    private void Copy() {
        SharedContextMenu.getContextMenu().getItems().get(1).setDisable(false);
        SharedContextMenu.getContextMenu().getItems().get(3).setDisable(false);
        SharedContextMenu.getContextMenu().getItems().get(4).setDisable(false);

        copyAnimationsValue = new String[nodesTreeView.getSelectionModel().getSelectedItems().size()];
        copyNodesValue = new String[nodesTreeView.getSelectionModel().getSelectedItems().size()][];
        copyKeyframeTypesValue = new String[nodesTreeView.getSelectionModel().getSelectedItems().size()][][];
        copyKeyframesValue = new String[nodesTreeView.getSelectionModel().getSelectedItems().size()][][][];

        copyAnimations = new Animation[nodesTreeView.getSelectionModel().getSelectedItems().size()];
        copyAnimationNodes = new AnimationNode[nodesTreeView.getSelectionModel().getSelectedItems().size()][];
        copyKeyframeTypes = new KeyframeType[nodesTreeView.getSelectionModel().getSelectedItems().size()][][];
        copyKeyframes = new Keyframe[nodesTreeView.getSelectionModel().getSelectedItems().size()][][][];

        for (int i = 0; i < nodesTreeView.getSelectionModel().getSelectedItems().size(); i++) {
            TreeItem<String> animation = nodesTreeView.getSelectionModel().getSelectedItems().get(i);
            copyAnimationsValue[i] = animation.getValue();
            copyNodesValue[i] = new String[animation.getChildren().size()];
            copyKeyframeTypesValue[i] = new String[animation.getChildren().size()][];
            copyKeyframesValue[i] = new String[animation.getChildren().size()][][];

            copyAnimations[i] = new Animation(animationsHashMap.get(animation));
            copyAnimationNodes[i] = new AnimationNode[animation.getChildren().size()];
            copyKeyframeTypes[i] = new KeyframeType[animation.getChildren().size()][];
            copyKeyframes[i] = new Keyframe[animation.getChildren().size()][][];

            for (int j = 0; j < animation.getChildren().size(); j++) {
                TreeItem<String> node = animation.getChildren().get(j);
                copyNodesValue[i][j] = node.getValue();
                copyKeyframeTypesValue[i][j] = new String[node.getChildren().size()];
                copyKeyframesValue[i][j] = new String[node.getChildren().size()][];

                copyAnimationNodes[i][j] = new AnimationNode(animationNodesHashMap.get(node));
                copyKeyframeTypes[i][j] = new KeyframeType[node.getChildren().size()];
                copyKeyframes[i][j] = new Keyframe[node.getChildren().size()][];

                for (int k = 0; k < node.getChildren().size(); k++) {
                    TreeItem<String> keyframeType = node.getChildren().get(k);
                    copyKeyframeTypesValue[i][j][k] = keyframeType.getValue();
                    copyKeyframesValue[i][j][k] = new String[keyframeType.getChildren().size()];

                    copyKeyframeTypes[i][j][k] = new KeyframeType(keyframeTypesHashMap.get(keyframeType));
                    copyKeyframes[i][j][k] = new Keyframe[keyframeType.getChildren().size()];

                    for (int l = 0; l < keyframeType.getChildren().size(); l++) {
                        TreeItem<String> keyframe = keyframeType.getChildren().get(l);
                        copyKeyframesValue[i][j][k][l] = keyframe.getValue();
                        copyKeyframes[i][j][k][l] = new Keyframe(keyframesHashMap.get(keyframe));
                    }
                }
            }
        }
    }

    private void Paste() {
        if (nodesTreeView.getSelectionModel().getSelectedIndices().size() == copyAnimations.length) {
            for (int i = 0; i < nodesTreeView.getSelectionModel().getSelectedItems().size(); i++) {
                TreeItem<String> item = nodesTreeView.getSelectionModel().getSelectedItems().get(i);
                deleteTreeItemKeys(item);
                item.getChildren().clear();
                item.setValue(copyAnimationsValue[i]);
                animationsHashMap.put(item, new Animation(copyAnimations[i]));
            }

            for (int i = 0; i < nodesTreeView.getSelectionModel().getSelectedItems().size(); i++) {
                TreeItem<String> animationTreeItem = nodesTreeView.getSelectionModel().getSelectedItems().get(i);

                for (int j = 0; j < copyNodesValue[i].length; j++) {
                    animationTreeItem.getChildren().add(new TreeItem<>(copyNodesValue[i][j]));
                    TreeItem<String> nodeTreeItem = animationTreeItem.getChildren().get(j);
                    animationNodesHashMap.put(animationTreeItem.getChildren().get(j), new AnimationNode(copyAnimationNodes[i][j]));

                    for (int k = 0; k < copyKeyframeTypesValue[i][j].length; k++) {
                        nodeTreeItem.getChildren().add(new TreeItem<>(copyKeyframeTypesValue[i][j][k]));
                        TreeItem<String> keyframeTypeTree = nodeTreeItem.getChildren().get(k);
                        keyframeTypesHashMap.put(nodeTreeItem.getChildren().get(k), new KeyframeType(copyKeyframeTypes[i][j][k]));

                        for (int l = 0; l < copyKeyframesValue[i][j][k].length; l++) {
                            keyframeTypeTree.getChildren().add(new TreeItem<>(copyKeyframesValue[i][j][k][l]));
                            keyframesHashMap.put(keyframeTypeTree.getChildren().get(l), new Keyframe(copyKeyframes[i][j][k][l]));
                        }
                    }
                }
            }

            dynamicTabPane.getTabs().clear();
            createAnimation(animationsHashMap.get(nodesTreeView.getSelectionModel().getSelectedItem()));
            
        } else {
            Popups.IndicesNotMatched();
        }
    }

    private void Delete() {
        for (TreeItem<String> treeItem : nodesTreeView.getSelectionModel().getSelectedItems()) {
            deleteTreeItemKeys(treeItem);
        }

        nodesTreeView.getRoot().getChildren().removeAll(nodesTreeView.getSelectionModel().getSelectedItems());
        nodesTreeView.getSelectionModel().clearSelection();
        dynamicTabPane.getTabs().clear();
        nodesTreeView.setContextMenu(null);
    }

    private void Append() {
        for (int i = copyAnimationsValue.length - 1; i > -1; i--) {
            TreeItem<String> animationTreeItem = new TreeItem<>(copyAnimationsValue[i]);
            animationsHashMap.put(animationTreeItem, new Animation(copyAnimations[i]));
            int index = nodesTreeView.getSelectionModel().getSelectedIndices().getLast();
            nodesTreeView.getRoot().getChildren().add(index + 1, animationTreeItem);

            for (int j = 0; j < copyNodesValue[i].length; j++) {
                animationTreeItem.getChildren().add(new TreeItem<>(copyNodesValue[i][j]));
                TreeItem<String> nodeTreeItem = animationTreeItem.getChildren().get(j);
                animationNodesHashMap.put(animationTreeItem.getChildren().get(j), new AnimationNode(copyAnimationNodes[i][j]));

                for (int k = 0; k < copyKeyframeTypesValue[i][j].length; k++) {
                    nodeTreeItem.getChildren().add(new TreeItem<>(copyKeyframeTypesValue[i][j][k]));
                    TreeItem<String> keyframeTypeTree = nodeTreeItem.getChildren().get(k);
                    keyframeTypesHashMap.put(nodeTreeItem.getChildren().get(k), new KeyframeType(copyKeyframeTypes[i][j][k]));

                    for (int l = 0; l < copyKeyframesValue[i][j][k].length; l++) {
                        keyframeTypeTree.getChildren().add(new TreeItem<>(copyKeyframesValue[i][j][k][l]));
                        keyframesHashMap.put(keyframeTypeTree.getChildren().get(l), new Keyframe(copyKeyframes[i][j][k][l]));
                    }
                }
            }
        }

        dynamicTabPane.getTabs().clear();
        createAnimation(animationsHashMap.get(nodesTreeView.getSelectionModel().getSelectedItem()));
    }

    private void Insert() {
        for (int i = copyAnimationsValue.length - 1; i > -1; i--) {
            TreeItem<String> animationTreeItem = new TreeItem<>(copyAnimationsValue[i]);
            animationsHashMap.put(animationTreeItem, new Animation(copyAnimations[i]));
            int index = nodesTreeView.getSelectionModel().getSelectedIndices().getFirst();
            nodesTreeView.getRoot().getChildren().add(index, animationTreeItem);

            for (int j = 0; j < copyNodesValue[i].length; j++) {
                animationTreeItem.getChildren().add(new TreeItem<>(copyNodesValue[i][j]));
                TreeItem<String> nodeTreeItem = animationTreeItem.getChildren().get(j);
                animationNodesHashMap.put(animationTreeItem.getChildren().get(j), new AnimationNode(copyAnimationNodes[i][j]));

                for (int k = 0; k < copyKeyframeTypesValue[i][j].length; k++) {
                    nodeTreeItem.getChildren().add(new TreeItem<>(copyKeyframeTypesValue[i][j][k]));
                    TreeItem<String> keyframeTypeTree = nodeTreeItem.getChildren().get(k);
                    keyframeTypesHashMap.put(nodeTreeItem.getChildren().get(k), new KeyframeType(copyKeyframeTypes[i][j][k]));

                    for (int l = 0; l < copyKeyframesValue[i][j][k].length; l++) {
                        keyframeTypeTree.getChildren().add(new TreeItem<>(copyKeyframesValue[i][j][k][l]));
                        keyframesHashMap.put(keyframeTypeTree.getChildren().get(l), new Keyframe(copyKeyframes[i][j][k][l]));
                    }
                }
            }
        }

        dynamicTabPane.getTabs().clear();
        createAnimation(animationsHashMap.get(nodesTreeView.getSelectionModel().getSelectedItem()));
    }

    private void deleteTreeItemKeys(TreeItem<String> item) {
        animationsHashMap.remove(item);
        keyframesHashMap.remove(item);
        
        for (TreeItem<String> child : item.getChildren()) {
            deleteTreeItemKeys(child);
        }
    }
    
    public void eanReader(Path path) {
        try(FileChannel channel = FileChannel.open(path, StandardOpenOption.READ)) {
            ByteBuffer byteBuffer = ByteBuffer.allocate(1).order(ByteOrder.LITTLE_ENDIAN);
            ByteBuffer shortBuffer = ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN);
            ByteBuffer intBuffer = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
            ByteBuffer dynamicStringBuffer;

            channel.position(8);
            intBuffer.clear();
            channel.read(intBuffer);
            intBuffer.flip();
            eanMainEntry.i08 = intBuffer.getInt();

            channel.position(16);
            byteBuffer.clear();
            channel.read(byteBuffer);
            byteBuffer.flip();
            eanMainEntry.isCamera = byteBuffer.get() == 1;

            channel.position(17);
            byteBuffer.clear();
            channel.read(byteBuffer);
            byteBuffer.flip();
            eanMainEntry.i17 = byteBuffer.get();

            channel.position(18);
            shortBuffer.clear();
            channel.read(shortBuffer);
            shortBuffer.flip();
            short animationCount = shortBuffer.getShort();

            channel.position(20);
            intBuffer.clear();
            channel.read(intBuffer);
            intBuffer.flip();
            int skeletonOffset = intBuffer.getInt();

            channel.position(24);
            intBuffer.clear();
            channel.read(intBuffer);
            intBuffer.flip();
            int animationOffset = intBuffer.getInt();

            channel.position(28);
            intBuffer.clear();
            channel.read(intBuffer);
            intBuffer.flip();
            int animationNameOffset = intBuffer.getInt();

            eskBoneReader(channel, skeletonOffset);

            if (animationCount > 0) {
                nodesTreeView.setRoot(new TreeItem<>("dummy"));
                nodesTreeView.setShowRoot(false);

                for (int i = 0; i < animationCount; i++) {
                    Animation animation = new Animation();
                    
                    channel.position(animationOffset + i * 4);
                    intBuffer.clear();
                    channel.read(intBuffer);
                    intBuffer.flip();
                    int offset = intBuffer.getInt();

                    if (offset != 0) {
                        channel.position(animationNameOffset + i * 4);
                        intBuffer.clear();
                        channel.read(intBuffer);
                        intBuffer.flip();
                        int nameOffset = intBuffer.getInt();

                        if (nameOffset != 0) {
                            int counter = 0;

                            do {
                                channel.position(nameOffset + counter);
                                byteBuffer.clear();
                                channel.read(byteBuffer);
                                byteBuffer.flip();
                                counter++;
                            } while (byteBuffer.get() != 0);

                            dynamicStringBuffer = ByteBuffer.allocate(counter);

                            channel.position(nameOffset);
                            dynamicStringBuffer.clear();
                            channel.read(dynamicStringBuffer);
                            dynamicStringBuffer.flip();
                            animation.name = new String(dynamicStringBuffer.array(), StandardCharsets.ISO_8859_1);
                            nodesTreeView.getRoot().getChildren().add(new TreeItem<>(animation.name));
                            animationsHashMap.put(nodesTreeView.getRoot().getChildren().get(i), animation);
                        }

                        channel.position(offset + 2);
                        byteBuffer.clear();
                        channel.read(byteBuffer);   
                        byteBuffer.flip();
                        animation.indexSize = toUByte(byteBuffer.get());

                        channel.position(offset + 3);
                        byteBuffer.clear();
                        channel.read(byteBuffer);
                        byteBuffer.flip();
                        animation.floatSize = toUByte(byteBuffer.get());

                        channel.position(offset + 4);
                        intBuffer.clear();
                        channel.read(intBuffer);
                        intBuffer.flip();
                        animation.frameCount = intBuffer.getInt();

                        channel.position(offset + 8);
                        intBuffer.clear();
                        channel.read(intBuffer);
                        intBuffer.flip();
                        int nodeCount = intBuffer.getInt();

                        channel.position(offset + 12);
                        intBuffer.clear();
                        channel.read(intBuffer);
                        intBuffer.flip();
                        int nodeOffset = intBuffer.getInt() + offset;

                        if (nodeCount > 0) {
                            for (int j = 0; j < nodeCount; j++) {
                                AnimationNode animationNode = new AnimationNode();
                                
                                channel.position(nodeOffset + j * 4);
                                intBuffer.clear();
                                channel.read(intBuffer);
                                intBuffer.flip();
                                int thisNodeOffset = intBuffer.getInt() + offset;
                                
                                channel.position(thisNodeOffset);
                                shortBuffer.clear();
                                channel.read(shortBuffer);
                                shortBuffer.flip();
                                animationNode.boneIndex = shortBuffer.getShort();
                                animationNode.boneName = bonesHashMap.get(allBones.get(animationNode.boneIndex)).boneName;
                                nodesTreeView.getRoot().getChildren().get(i).getChildren().add(new TreeItem<>(animationNode.boneName));
                                animationNodesHashMap.put(nodesTreeView.getRoot().getChildren().get(i).getChildren().get(j), animationNode);

                                channel.position(thisNodeOffset + 2);
                                shortBuffer.clear();
                                channel.read(shortBuffer);
                                shortBuffer.flip();
                                short keyframedAnimationsCount = shortBuffer.getShort();

                                channel.position(thisNodeOffset + 4);
                                intBuffer.clear();
                                channel.read(intBuffer);
                                intBuffer.flip();
                                int keyframedAnimationsOffset = intBuffer.getInt() + thisNodeOffset;

                                if (keyframedAnimationsCount > 0) {
                                    for (int k = 0; k < keyframedAnimationsCount; k++) {
                                        KeyframeType keyframeType = new KeyframeType();
                                        
                                        channel.position(keyframedAnimationsOffset + k * 4);
                                        intBuffer.clear();
                                        channel.read(intBuffer);
                                        intBuffer.flip();
                                        int thisKeyframedAnimationOffset = intBuffer.getInt() + thisNodeOffset;

                                        channel.position(thisKeyframedAnimationOffset);
                                        byteBuffer.clear();
                                        channel.read(byteBuffer);
                                        byteBuffer.flip();
                                        keyframeType.type = toUByte(byteBuffer.get());
                                        nodesTreeView.getRoot().getChildren().get(i).getChildren().get(j).getChildren().add(new TreeItem<>(KeyframeType.getComponentType(keyframeType.type)));
                                        keyframeTypesHashMap.put(nodesTreeView.getRoot().getChildren().get(i).getChildren().get(j).getChildren().get(k), keyframeType);

                                        channel.position(thisKeyframedAnimationOffset + 1);
                                        byteBuffer.clear();
                                        channel.read(byteBuffer);
                                        byteBuffer.flip();
                                        keyframeType.i01 = toUByte(byteBuffer.get());

                                        channel.position(thisKeyframedAnimationOffset + 2);
                                        shortBuffer.clear();
                                        channel.read(shortBuffer);
                                        shortBuffer.flip();
                                        keyframeType.i02 = shortBuffer.getShort();

                                        channel.position(thisKeyframedAnimationOffset + 4);
                                        intBuffer.clear();
                                        channel.read(intBuffer);
                                        intBuffer.flip();
                                        int keyframeCount = intBuffer.getInt();
                                        
                                        channel.position(thisKeyframedAnimationOffset + 8);
                                        intBuffer.clear();
                                        channel.read(intBuffer);
                                        intBuffer.flip();
                                        int indexListOffset = intBuffer.getInt() + thisKeyframedAnimationOffset;

                                        channel.position(thisKeyframedAnimationOffset + 12);
                                        intBuffer.clear();
                                        channel.read(intBuffer);
                                        intBuffer.flip();
                                        int matrixOffset = intBuffer.getInt() + thisKeyframedAnimationOffset;

                                        for (int l = 0; l < keyframeCount; l++) {
                                            Keyframe keyframe = new Keyframe();

                                            if (animation.indexSize == 0) {
                                                channel.position(indexListOffset + l);
                                                byteBuffer.clear();
                                                channel.read(byteBuffer);
                                                byteBuffer.flip();
                                                nodesTreeView.getRoot().getChildren().get(i).getChildren().get(j).getChildren().get(k).getChildren().add(new TreeItem<>("Keyframe " + toUByte(byteBuffer.get())));
                                                keyframesHashMap.put(nodesTreeView.getRoot().getChildren().get(i).getChildren().get(j).getChildren().get(k).getChildren().get(l), keyframe);
                                            }
                                            else if (animation.indexSize == 1) {
                                                channel.position(indexListOffset + l * 2);
                                                shortBuffer.clear();
                                                channel.read(shortBuffer);
                                                shortBuffer.flip();
                                                nodesTreeView.getRoot().getChildren().get(i).getChildren().get(j).getChildren().get(k).getChildren().add(new TreeItem<>("Keyframe " + shortBuffer.getShort()));
                                                keyframesHashMap.put(nodesTreeView.getRoot().getChildren().get(i).getChildren().get(j).getChildren().get(k).getChildren().get(l), keyframe);
                                            } 

                                            if (animation.floatSize == 1) {
                                                channel.position(matrixOffset + l * 8);
                                                shortBuffer.clear();
                                                channel.read(shortBuffer);
                                                shortBuffer.flip();
                                                keyframe.x = Float.float16ToFloat(shortBuffer.getShort());

                                                channel.position(matrixOffset + l * 8 + 2);
                                                shortBuffer.clear();
                                                channel.read(shortBuffer);
                                                shortBuffer.flip();
                                                keyframe.y = Float.float16ToFloat(shortBuffer.getShort());

                                                channel.position(matrixOffset + l * 8 + 4);
                                                shortBuffer.clear();
                                                channel.read(shortBuffer);
                                                shortBuffer.flip();
                                                keyframe.z = Float.float16ToFloat(shortBuffer.getShort());

                                                channel.position(matrixOffset + l * 8 + 6);
                                                shortBuffer.clear();
                                                channel.read(shortBuffer);
                                                shortBuffer.flip();
                                                keyframe.w = Float.float16ToFloat(shortBuffer.getShort());
                                            }
                                            else if (animation.floatSize == 2) {
                                                channel.position(matrixOffset + l * 16);
                                                intBuffer.clear();
                                                channel.read(intBuffer);
                                                intBuffer.flip();
                                                keyframe.x = intBuffer.getFloat();

                                                channel.position(matrixOffset + l * 16 + 4);
                                                shortBuffer.clear();
                                                channel.read(shortBuffer);
                                                shortBuffer.flip();
                                                keyframe.y = intBuffer.getFloat();

                                                channel.position(matrixOffset + l * 16 + 8);
                                                shortBuffer.clear();
                                                channel.read(shortBuffer);
                                                shortBuffer.flip();
                                                keyframe.z = intBuffer.getFloat();

                                                channel.position(matrixOffset + l * 16 + 12);
                                                shortBuffer.clear();
                                                channel.read(shortBuffer);
                                                shortBuffer.flip();
                                                keyframe.w = intBuffer.getFloat();
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void eanWriter(Path path) {
        try(FileChannel channel = FileChannel.open(path, StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            ByteBuffer byteBuffer = ByteBuffer.allocate(1).order(ByteOrder.LITTLE_ENDIAN);
            ByteBuffer shortBuffer = ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN);
            ByteBuffer intBuffer = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
            ByteBuffer dynamicStringBuffer;

            int skeletonOffset = 32;
            int animationNameOffset;
            
            channel.position(0);
            channel.write(ByteBuffer.wrap(new byte[]{0x23, 0x45, 0x41, (byte)0x4E}));

            channel.position(4);
            channel.write(ByteBuffer.wrap(new byte[]{(byte)0xFE, (byte)0xFF}));

            //header size (32)
            channel.position(6);
            channel.write(ByteBuffer.wrap(new byte[]{0x20, 0x00}));

            channel.position(8);
            intBuffer.putInt(eanMainEntry.i08);
            intBuffer.flip();
            channel.write(intBuffer);

            channel.position(16);
            byteBuffer.put((byte) (eanMainEntry.isCamera ? 1 : 0));
            byteBuffer.flip();
            channel.write(byteBuffer);

            channel.position(17);
            byteBuffer.clear();
            byteBuffer.put((byte) eanMainEntry.i17);
            byteBuffer.flip();
            channel.write(byteBuffer);

            channel.position(18);
            shortBuffer.clear();
            shortBuffer.putShort((short) (!nodesTreeView.getRoot().getChildren().isEmpty() ? nodesTreeView.getRoot().getChildren().size() : 0));
            shortBuffer.flip();
            channel.write(shortBuffer);

            channel.position(20);
            intBuffer.clear();
            intBuffer.putInt(skeletonOffset);
            intBuffer.flip();
            channel.write(intBuffer);

            int animationOffset = eskBoneWriter(channel, skeletonOffset);

            channel.position(24);
            intBuffer.clear();
            intBuffer.putInt(animationOffset);
            intBuffer.flip();
            channel.write(intBuffer);

            int animationCount = nodesTreeView.getRoot().getChildren().size();
            int offset = animationOffset + animationCount * 4;
            int nodeOffset = 16;
            int indexListOffset = 16;

            for (int i = 0; i < animationCount; i++) {
                Animation animation = animationsHashMap.get(nodesTreeView.getRoot().getChildren().get(i));

                if (offset % 16 != 0) {
                    offset += (16 - (offset % 16));
                }
                
                channel.position(animationOffset + i * 4);
                intBuffer.clear();
                intBuffer.putInt(offset);
                intBuffer.flip();
                channel.write(intBuffer);

                channel.position(offset + 2);
                byteBuffer.clear();
                byteBuffer.put((byte) animation.indexSize);
                byteBuffer.flip();
                channel.write(byteBuffer);

                channel.position(offset + 3);
                byteBuffer.clear();
                byteBuffer.put((byte) animation.floatSize);
                byteBuffer.flip();
                channel.write(byteBuffer);

                channel.position(offset + 4);
                intBuffer.clear();
                intBuffer.putInt(animation.frameCount);
                intBuffer.flip();
                channel.write(intBuffer);

                int nodeCount = nodesTreeView.getRoot().getChildren().get(i).getChildren().size();
                channel.position(offset + 8);
                intBuffer.clear();
                intBuffer.putInt(nodeCount);
                intBuffer.flip();
                channel.write(intBuffer);

                channel.position(offset + 12);
                intBuffer.clear();
                intBuffer.putInt(nodeOffset);
                intBuffer.flip();
                channel.write(intBuffer);

                int thisNodeOffset = nodeCount * 4 + nodeOffset;

                for (int j = 0; j < nodeCount; j++) {
                    AnimationNode animationNode = animationNodesHashMap.get(nodesTreeView.getRoot().getChildren().get(i).getChildren().get(j));

                    channel.position(offset + nodeOffset + j * 4);
                    intBuffer.clear();
                    intBuffer.putInt(thisNodeOffset);
                    intBuffer.flip();
                    channel.write(intBuffer);

                    channel.position(offset + thisNodeOffset);
                    shortBuffer.clear();
                    shortBuffer.putShort(animationNode.boneIndex);
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    short keyframedAnimationsCount = (short) nodesTreeView.getRoot().getChildren().get(i).getChildren().get(j).getChildren().size();
                    channel.position(offset + thisNodeOffset + 2);
                    shortBuffer.clear();
                    shortBuffer.putShort(keyframedAnimationsCount);
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    int keyframedAnimationsOffset = 8;
                    channel.position(offset + thisNodeOffset + 4);
                    intBuffer.clear();
                    intBuffer.putInt(keyframedAnimationsOffset);
                    intBuffer.flip();
                    channel.write(intBuffer);

                    int thisKeyframedAnimationOffset = keyframedAnimationsCount * 4 + 8;

                    for (int k = 0; k < keyframedAnimationsCount; k++) {
                        KeyframeType keyframeType = keyframeTypesHashMap.get(nodesTreeView.getRoot().getChildren().get(i).getChildren().get(j).getChildren().get(k));

                        channel.position(offset + thisNodeOffset + keyframedAnimationsOffset + k * 4);
                        intBuffer.flip();
                        intBuffer.putInt(thisKeyframedAnimationOffset);
                        intBuffer.clear();
                        channel.write(intBuffer);

                        channel.position(offset + thisNodeOffset + thisKeyframedAnimationOffset);
                        byteBuffer.clear();
                        byteBuffer.put((byte) keyframeType.type);
                        byteBuffer.flip();
                        channel.write(byteBuffer);

                        channel.position(offset + thisNodeOffset + thisKeyframedAnimationOffset + 1);
                        byteBuffer.clear();
                        byteBuffer.put((byte) keyframeType.i01);
                        byteBuffer.flip();
                        channel.write(byteBuffer);

                        channel.position(offset + thisNodeOffset + thisKeyframedAnimationOffset + 2);
                        shortBuffer.clear();
                        shortBuffer.putShort(keyframeType.i02);
                        shortBuffer.flip();
                        channel.write(shortBuffer);

                        int keyframeCount = nodesTreeView.getRoot().getChildren().get(i).getChildren().get(j).getChildren().get(k).getChildren().size();
                        channel.position(offset + thisNodeOffset + thisKeyframedAnimationOffset + 4);
                        intBuffer.clear();
                        intBuffer.putInt(keyframeCount);
                        intBuffer.flip();
                        channel.write(intBuffer);
                        
                        channel.position(offset + thisNodeOffset + thisKeyframedAnimationOffset + 8);
                        intBuffer.clear();
                        intBuffer.putInt(indexListOffset);
                        intBuffer.flip();
                        channel.write(intBuffer);

                        int matrixOffset =  animation.indexSize == 0 ? keyframeCount + indexListOffset : keyframeCount * 2 + indexListOffset;

                        if ((offset + thisNodeOffset + thisKeyframedAnimationOffset + matrixOffset) % 16 != 0) {
                            matrixOffset += 16 - ((offset + thisNodeOffset + thisKeyframedAnimationOffset + matrixOffset) % 16);
                        }

                        channel.position(offset + thisNodeOffset + thisKeyframedAnimationOffset + 12);
                        intBuffer.clear();
                        intBuffer.putInt(matrixOffset);
                        intBuffer.flip();
                        channel.write(intBuffer);

                        for (int l = 0; l < keyframeCount; l++) {
                            Keyframe keyframe = keyframesHashMap.get(nodesTreeView.getRoot().getChildren().get(i).getChildren().get(j).getChildren().get(k).getChildren().get(l));

                            if (animation.indexSize == 0) {
                                channel.position(offset + thisNodeOffset + thisKeyframedAnimationOffset + indexListOffset + l);
                                byteBuffer.clear();
                                byteBuffer.put((byte) Integer.parseInt(nodesTreeView.getRoot().getChildren().get(i).getChildren().get(j).getChildren().get(k).getChildren().get(l).getValue().replaceAll("\\D+", "")));
                                byteBuffer.flip();
                                channel.write(byteBuffer);
                            }
                            else if (animation.indexSize == 1) {
                                channel.position(offset + thisNodeOffset + thisKeyframedAnimationOffset + indexListOffset + l * 2);
                                shortBuffer.clear();
                                shortBuffer.putShort((short) Integer.parseInt(nodesTreeView.getRoot().getChildren().get(i).getChildren().get(j).getChildren().get(k).getChildren().get(l).getValue().replaceAll("\\D+", "")));
                                shortBuffer.flip();
                                channel.write(shortBuffer);
                            }

                            if (animation.floatSize == 1) {
                                channel.position(offset + thisNodeOffset + thisKeyframedAnimationOffset + matrixOffset + l * 8);
                                shortBuffer.clear();
                                shortBuffer.putShort(Float.floatToFloat16(keyframe.x));
                                shortBuffer.flip();
                                channel.write(shortBuffer);

                                channel.position(offset + thisNodeOffset + thisKeyframedAnimationOffset + matrixOffset + l * 8 + 2);
                                shortBuffer.clear();
                                shortBuffer.putShort(Float.floatToFloat16(keyframe.y));
                                shortBuffer.flip();
                                channel.write(shortBuffer);

                                channel.position(offset + thisNodeOffset + thisKeyframedAnimationOffset + matrixOffset + l * 8 + 4);
                                shortBuffer.clear();
                                shortBuffer.putShort(Float.floatToFloat16(keyframe.z));
                                shortBuffer.flip();
                                channel.write(shortBuffer);

                                channel.position(offset + thisNodeOffset + thisKeyframedAnimationOffset + matrixOffset + l * 8 + + 6);
                                shortBuffer.clear();
                                shortBuffer.putShort(Float.floatToFloat16(keyframe.w));
                                shortBuffer.flip();
                                channel.write(shortBuffer);

                                if (l == keyframeCount - 1) thisKeyframedAnimationOffset += (matrixOffset + 8 * keyframeCount);
                            }
                            else if (animation.floatSize == 2) {
                                channel.position(offset + thisNodeOffset + thisKeyframedAnimationOffset + matrixOffset + l * 16);
                                intBuffer.clear();
                                intBuffer.putFloat(keyframe.x);
                                intBuffer.flip();
                                channel.write(intBuffer);

                                channel.position(offset + thisNodeOffset + thisKeyframedAnimationOffset + matrixOffset + l * 16 + 4);
                                intBuffer.clear();
                                intBuffer.putFloat(keyframe.y);
                                intBuffer.flip();
                                channel.write(intBuffer);

                                channel.position(offset + thisNodeOffset + thisKeyframedAnimationOffset + matrixOffset + l * 16 + 8);
                                intBuffer.clear();
                                intBuffer.putFloat(keyframe.z);
                                intBuffer.flip();
                                channel.write(intBuffer);

                                channel.position(offset + thisNodeOffset + thisKeyframedAnimationOffset + matrixOffset + l * 16 + 12);
                                intBuffer.clear();
                                intBuffer.putFloat(keyframe.w);
                                intBuffer.flip();
                                channel.write(intBuffer);

                                if (l == keyframeCount - 1)  thisKeyframedAnimationOffset += (matrixOffset + 16 * keyframeCount);
                            }
                        }
                    }

                    thisNodeOffset += thisKeyframedAnimationOffset;
                }

                offset += eanMainEntry.isCamera ? thisNodeOffset : (thisNodeOffset + 16);
            }
            
            if (!eanMainEntry.isCamera) offset -= 16;
            animationNameOffset = eanMainEntry.isCamera ? offset : offset + 12;

            channel.position(28);
            intBuffer.clear();
            intBuffer.putInt(animationNameOffset);
            intBuffer.flip();
            channel.write(intBuffer);

            int nameOffset = animationNameOffset + animationCount * 4;

            for (int i = 0; i < animationCount; i++) {
                Animation animation = animationsHashMap.get(nodesTreeView.getRoot().getChildren().get(i));

                channel.position(animationNameOffset + i * 4);
                intBuffer.clear();
                intBuffer.putInt(nameOffset);
                intBuffer.flip();
                channel.write(intBuffer);

                dynamicStringBuffer = ByteBuffer.allocate(animation.name.getBytes().length);

                channel.position(nameOffset);
                dynamicStringBuffer = ByteBuffer.wrap(animation.name.getBytes());
                channel.write(dynamicStringBuffer);

                nameOffset += animation.name.getBytes().length;
            }
        } catch (IOException e) {
        }
    }

    private static enum EanMainValues {
        I08,
        IsCamera,
        I17
    }

    private static enum AnimationValues {
        IndexSize,
        FloatSize,
        Name
    }

    private static enum KeyframeValues {
        X,
        Y,
        Z,
        W
    }
}

class EanMain {
    int i08;
    boolean isCamera = false;
    int i17;
}

class Animation {
    int indexSize;
    int floatSize;
    String name = "";
    int frameCount;

    Animation() {}
    Animation(Animation other) {
        this.indexSize = other.indexSize;
        this.floatSize = other.floatSize;
        this.name = other.name;
        this.frameCount = other.frameCount;
    }
}

class AnimationNode {
    String boneName = "";
    short boneIndex;

    AnimationNode() {}
    AnimationNode(AnimationNode other) {
        this.boneName = other.boneName;
        this.boneIndex = other.boneIndex;
    }
}

class KeyframeType {
    int type;
    int i01;
    short i02;

    KeyframeType() {}
    KeyframeType(KeyframeType other) {
        this.type = other.type;
        this.i01 = other.i01;
        this.i02 = other.i02;
    }

    public static String getComponentType(int type) {
        switch (type) {
            case 0 -> {
                return new String("Position");
            }
            case 1 -> {
                return new String("Rotation");
            }
            case 2 -> {
                return new String("Scale");
            }
            default -> {
                return new String("Unknown Type");
            }
        }
    }
}

class Keyframe {
    float x;
    float y;
    float z;
    float w;

    Keyframe() {}
    Keyframe(Keyframe other) {
       this.x = other.x;
       this.y = other.y;
       this.z = other.z;
       this.w = other.w;
    }
}

class SharedContextMenu {
    static ContextMenu contextMenu = new ContextMenu(new MenuItem[] {
        new MenuItem("Copy  Ctrl+C"), 
        new MenuItem("Paste  Ctrl+V"),
        new MenuItem("Delete  Del"),
        new MenuItem("Append  Ctrl+A"),
        new MenuItem("Insert  Ctrl+I")
    });

    public static ContextMenu getContextMenu() {
        return contextMenu;
    }
}


