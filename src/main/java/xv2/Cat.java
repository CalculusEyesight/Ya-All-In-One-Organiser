package xv2;
import static xv2.Unsigned.toUByte;
import static xv2.Unsigned.toUShort;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Cat {
    ArrayList <CatEntry> catEntries = new ArrayList<>();

    ListView <String> listView = new ListView<>();
    HBox hBox = new HBox(10);

    CatEntry copyContainer = null;

    ContextMenu contextMenu = new ContextMenu();
    MenuItem copy = new MenuItem("Copy Ctrl+C");
    MenuItem paste = new MenuItem("Paste Ctrl+V");
    MenuItem delete = new MenuItem("Delete Del");
    MenuItem append = new MenuItem("Append Ctrl+A");
    MenuItem insert = new MenuItem("Insert Ctrl+I");

    int findIndex = 0;
    String findText = null;
    String replaceText = null;
    Object [] indexList = new Object[] {findIndex, findText, replaceText};
    boolean found = false;

    public Cat() {
        entriesActionListener();
        entriesKeysListener();
    }

    public HBox createHBoxOuter() {
        hBox.getChildren().addAll(listView, new VBox());
        return this.hBox;
    }

    private VBox createCatVBox(CatEntry entry) {
        VBox catVBox = new VBox(30,
            createHBox(createLabel("Chara ID", 170), createTextField(catEntries.indexOf(entry), entry.charaId, CatValues.CharaID)),
            createHBox(createLabel("Costume", 170), createTextField(catEntries.indexOf(entry), entry.costume, CatValues.Costume)),
            createHBox(createLabel("I_04", 170), createTextField(catEntries.indexOf(entry), entry.i04, CatValues.I04)), 
            createHBox(createLabel("Skill ID 2", 170), createTextField(catEntries.indexOf(entry), entry.skillId2, CatValues.SkillID2)),
            createHBox(createLabel("Chara Code", 170), createTextField(catEntries.indexOf(entry), entry.charaCode, CatValues.CharaCode)), 
            createHBox(createLabel("I_12", 170), createTextField(catEntries.indexOf(entry), entry.i12, CatValues.I12)),
            createHBox(createLabel("I_16", 170), createTextField(catEntries.indexOf(entry), entry.i16, CatValues.I16)), 
            createHBox(createLabel("I_20", 170), createTextField(catEntries.indexOf(entry), entry.i20, CatValues.I20)),
            createHBox(createLabel("Transformation Entry", 170), createTextField(catEntries.indexOf(entry), entry.transformationEntry, CatValues.TransformationEntry)), 
            createHBox(createLabel("I_22", 170), createTextField(catEntries.indexOf(entry), entry.i22, CatValues.I22))
        );
        catVBox.setPadding(new Insets(20, 0, 0, 0));
        
        return catVBox;
    }

    private Label createLabel(String value, int width) {
        Label label = new Label(value);
        if (width != 0) label.setPrefWidth(width);

        return label;
    }

    private TextField createTextField(int i, Object value, CatValues catValue) {
        TextField textField = new TextField(String.valueOf(value));
        textField.textProperty().addListener((obs, oldText, newText) -> {
            try {
                switch (catValue) {
                    case CharaID -> catEntries.get(i).charaId = Integer.parseInt(newText);
                    case Costume -> catEntries.get(i).costume = Integer.parseInt(newText);
                    case I04 -> catEntries.get(i).i04 = Integer.parseInt(newText);
                    case SkillID2 -> catEntries.get(i).skillId2 = Integer.parseInt(newText);
                    case CharaCode -> catEntries.get(i).charaCode = newText;
                    case I12 -> catEntries.get(i).i12 = Integer.parseInt(newText);
                    case I16 -> catEntries.get(i).i16 = Integer.parseInt(newText);
                    case I20 -> catEntries.get(i).i20 = Integer.parseInt(newText);
                    case TransformationEntry -> catEntries.get(i).transformationEntry = Integer.parseInt(newText);
                    case I22 -> catEntries.get(i).i22 = Integer.parseInt(newText);
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        return textField;
    }

    private HBox createHBox(Label label, TextField textField) {
        HBox hBox = new HBox(label, textField);
        hBox.setAlignment(Pos.CENTER_LEFT);

        return hBox;
    }

    private void entriesActionListener() {
        paste.setDisable(true);

        contextMenu.getItems().addAll(copy, paste, delete, append, insert);

        listView.setContextMenu(contextMenu);

        listView.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue == null) return;

            hBox.getChildren().remove(1);
            hBox.getChildren().add(1, createCatVBox(catEntries.get(listView.getSelectionModel().getSelectedIndex())));
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

    private void entriesKeysListener() {
        listView.setOnKeyPressed(e -> {
            if (e.isControlDown() && e.getCode() == KeyCode.C) Copy();
            else if (e.isControlDown() && e.getCode()==KeyCode.V) Paste();
            else if (e.getCode() == KeyCode.DELETE) Delete();
            else if (e.isControlDown() && e.getCode() == KeyCode.A) Append();
            else if (e.isControlDown() && e.getCode() == KeyCode.I) Insert();
            else if (e.isControlDown() && e.getCode() == KeyCode.F) {
                ButtonType findNextButtonType = new ButtonType("Find Next", ButtonData.NEXT_FORWARD);
                ButtonType cancelButtonType = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

                Dialog<String> dialog = new Dialog<>();
                dialog.setTitle("Find");
                dialog.getDialogPane().getButtonTypes().addAll(findNextButtonType, cancelButtonType);
                dialog.getDialogPane().setContent(Popups.createFindDialog("Cat Entry: ", indexList, 
                    FXCollections.observableArrayList(
                        "Chara ID", 
                        "Costume", 
                        "I_04", 
                        "Skill ID 2", 
                        "Chara Code", 
                        "I_12", 
                        "I_16", 
                        "I_20",
                        "Transformation Entry",
                        "I22"
                    ))
                );

                final Button findbt = (Button) dialog.getDialogPane().lookupButton(findNextButtonType);
                findbt.addEventFilter(ActionEvent.ACTION, event -> {
                    if (!findbt.isPressed()) {
                        switch ((int) indexList[0]) {
                            case 0 -> listViewSearch(CatValues.CharaID);
                            case 1 -> listViewSearch(CatValues.Costume);
                            case 2 -> listViewSearch(CatValues.I04);
                            case 3 -> listViewSearch(CatValues.SkillID2);
                            case 4 -> listViewSearch(CatValues.CharaCode);
                            case 5 -> listViewSearch(CatValues.I12);
                            case 6 -> listViewSearch(CatValues.I16);
                            case 7 -> listViewSearch(CatValues.I20);
                            case 8 -> listViewSearch(CatValues.TransformationEntry);
                            case 9 -> listViewSearch(CatValues.I22);
                        }
                        
                        event.consume();
                    }
                });

                dialog.showAndWait();
            }
            else if (e.isControlDown() && e.getCode() == KeyCode.R) {
                ButtonType replaceNextButtonType = new ButtonType("Replace Next", ButtonData.NEXT_FORWARD);
                ButtonType replaceAllButtonType = new ButtonType("Replace All");
                ButtonType cancelButtonType = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

                Dialog<String> dialog = new Dialog<>();
                dialog.setTitle("Repplace");
                dialog.getDialogPane().getButtonTypes().addAll(replaceNextButtonType, replaceAllButtonType, cancelButtonType);
                dialog.getDialogPane().setContent(Popups.createReplaceDialog("Aura Entry: ", indexList, 
                    FXCollections.observableArrayList(
                        "Chara ID", 
                        "Costume", 
                        "I_04", 
                        "Skill ID 2", 
                        "Chara Code", 
                        "I_12", 
                        "I_16", 
                        "I_20",
                        "Transformation Entry",
                        "I22"
                    ))
                );

                final Button replacebt = (Button) dialog.getDialogPane().lookupButton(replaceNextButtonType);
                final Button replaceAllbt = (Button) dialog.getDialogPane().lookupButton(replaceAllButtonType);

                replacebt.addEventFilter(ActionEvent.ACTION, event -> {
                    if (!replacebt.isPressed()) {
                        found = false;

                        switch ((int) indexList[0]) {
                            case 0 -> listViewReplace(CatValues.CharaID, false);
                            case 1 -> listViewReplace(CatValues.Costume, false);
                            case 2 -> listViewReplace(CatValues.I04, false);
                            case 3 -> listViewReplace(CatValues.SkillID2, false);
                            case 4 -> listViewReplace(CatValues.CharaCode, false);
                            case 5 -> listViewReplace(CatValues.I12, false);
                            case 6 -> listViewReplace(CatValues.I16, false);
                            case 7 -> listViewReplace(CatValues.I20, false);
                            case 8 -> listViewReplace(CatValues.TransformationEntry, false);
                            case 9 -> listViewReplace(CatValues.I22, false);
                        }

                        event.consume();
                    }
                });

                replaceAllbt.addEventFilter(ActionEvent.ACTION, event -> {
                    if (!replaceAllbt.isPressed()) {
                        found = false;

                        switch ((int) indexList[0]) {
                            case 0 -> listViewReplace(CatValues.CharaID, true);
                            case 1 -> listViewReplace(CatValues.Costume, true);
                            case 2 -> listViewReplace(CatValues.I04, true);
                            case 3 -> listViewReplace(CatValues.SkillID2, true);
                            case 4 -> listViewReplace(CatValues.CharaCode, true);
                            case 5 -> listViewReplace(CatValues.I12, true);
                            case 6 -> listViewReplace(CatValues.I16, true);
                            case 7 -> listViewReplace(CatValues.I20, true);
                            case 8 -> listViewReplace(CatValues.TransformationEntry, true);
                            case 9 -> listViewReplace(CatValues.I22, true);
                        }

                        event.consume();
                    }
                });

                dialog.showAndWait();
            }
        });
    }

    private int listViewSearch(CatValues catValue) {
        int counter = 0;
        String value = null;
        int listIndex = listView.getSelectionModel().getSelectedIndex();
        int textFieldIndex = catValue.index;

        do {
            switch (catValue) {
                case CharaID -> value = String.valueOf(catEntries.get(listIndex).charaId);
                case Costume -> value = String.valueOf(catEntries.get(listIndex).costume);
                case I04 -> value = String.valueOf(catEntries.get(listIndex).i04);
                case SkillID2 -> value = String.valueOf(catEntries.get(listIndex).skillId2);
                case CharaCode -> value = catEntries.get(listIndex).charaCode;
                case I12 -> value = String.valueOf(catEntries.get(listIndex).i12);
                case I16 -> value = String.valueOf(catEntries.get(listIndex).i16);
                case I20 -> value = String.valueOf(catEntries.get(listIndex).i20);
                case TransformationEntry -> value = String.valueOf(catEntries.get(listIndex).transformationEntry);
                case I22 -> value = String.valueOf(catEntries.get(listIndex).i22);
            }

            if (indexList[1] != null && value.equals(indexList[1]) && listView.getSelectionModel().getSelectedIndex() != listIndex) {
                listView.getSelectionModel().select(listIndex);
                ((TextField) ((HBox) ((VBox) hBox.getChildren().get(1)).getChildren().get(textFieldIndex)).getChildren().get(1)).requestFocus();
                ((TextField) ((HBox) ((VBox) hBox.getChildren().get(1)).getChildren().get(textFieldIndex)).getChildren().get(1)).selectAll();
                found = true;
                
                return listIndex;
            }

            listIndex++;
            counter++;

            if (listIndex == listView.getItems().size()) listIndex = 0;
        } while (counter != listView.getItems().size());

        if (!found) {
            Popups.ItemNotFound();
        }
        else {
            Popups.ItemsReplaced();
        }

        return - 1;
    }

    private void listViewReplace(CatValues catValue, boolean continueLooping) {
        int searchedItemIndex = - 1;

        do {
            searchedItemIndex = listViewSearch(catValue);

            if (searchedItemIndex != -1) {
                switch (catValue) {
                    case CharaID -> catEntries.get(searchedItemIndex).charaId = Integer.parseInt((String) indexList[2]);
                    case Costume -> catEntries.get(searchedItemIndex).costume = Integer.parseInt((String) indexList[2]);
                    case I04 -> catEntries.get(searchedItemIndex).i04 = Integer.parseInt((String) indexList[2]);
                    case SkillID2 -> catEntries.get(searchedItemIndex).skillId2 = Integer.parseInt((String) indexList[2]);
                    case CharaCode -> catEntries.get(searchedItemIndex).charaCode = (String) indexList[2];
                    case I12 -> catEntries.get(searchedItemIndex).i12 = Integer.parseInt((String) indexList[2]);
                    case I16 -> catEntries.get(searchedItemIndex).i16 = Integer.parseInt((String) indexList[2]);
                    case I20 -> catEntries.get(searchedItemIndex).i20 = Integer.parseInt((String) indexList[2]);
                    case TransformationEntry -> catEntries.get(searchedItemIndex).transformationEntry = Integer.parseInt((String) indexList[2]);
                    case I22 -> catEntries.get(searchedItemIndex).i22 = Integer.parseInt((String) indexList[2]);
                }

                hBox.getChildren().remove(1);
                hBox.getChildren().add(1, createCatVBox(catEntries.get(listView.getSelectionModel().getSelectedIndex())));
            } 
        } while (continueLooping && searchedItemIndex != -1);
    }

    private void Copy() {
        copyContainer = new CatEntry(catEntries.get(listView.getSelectionModel().getSelectedIndex()));
    }

    private void Paste() {
        if (copyContainer == null) return;
        
        catEntries.set(listView.getSelectionModel().getSelectedIndex(), new CatEntry(copyContainer));
        hBox.getChildren().remove(1);
        hBox.getChildren().add(1, createCatVBox(catEntries.get(listView.getSelectionModel().getSelectedIndex())));
    }

    private void Delete() {
        if (listView.getSelectionModel().getSelectedIndex() == 0) return;

        catEntries.remove(listView.getSelectionModel().getSelectedIndex());
        for (int i = 0; i < listView.getItems().size(); i++) {
            listView.getItems().set(i, "Entry " + i);
        }
    }

    private void Append() {
        catEntries.add(listView.getSelectionModel().getSelectedIndex() + 1, new CatEntry());
        listView.getItems().add("Entry " + listView.getItems().size());
    }

    private void Insert() {
        if (listView.getSelectionModel().getSelectedIndex() > 0) {
            catEntries.add(listView.getSelectionModel().getSelectedIndex() , new CatEntry());
            listView.getItems().add("Entry " + listView.getItems().size());
            hBox.getChildren().remove(1);
            hBox.getChildren().add(1, createCatVBox(catEntries.get(listView.getSelectionModel().getSelectedIndex())));
        }
    }

    public void catReader(Path path) {
        try(FileChannel channel = FileChannel.open(path, StandardOpenOption.READ)) {
            ByteBuffer byteBuffer = ByteBuffer.allocate(1).order(ByteOrder.LITTLE_ENDIAN);
            ByteBuffer shortBuffer = ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN);
            ByteBuffer intBuffer = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
            
            short catEntriesCount;
            int entryOffset = 12;

            channel.position(6);
            shortBuffer.clear();
            channel.read(shortBuffer);
            shortBuffer.flip();
            catEntriesCount = shortBuffer.getShort();

            for (int i = 0; i < catEntriesCount; i++) {
                listView.getItems().add("Entry " + i);
            }

            for (int i = 0; i < catEntriesCount; i++) {
                catEntries.add(new CatEntry());

                channel.position(entryOffset + i * 24);
                shortBuffer.clear();
                channel.read(shortBuffer);
                shortBuffer.flip();
                catEntries.get(i).charaId = toUShort(shortBuffer.getShort());
                
                channel.position(entryOffset +  i * 24 + 2);
                shortBuffer.clear();
                channel.read(shortBuffer);
                shortBuffer.flip();
                catEntries.get(i).costume = toUShort(shortBuffer.getShort());

                channel.position(entryOffset + i * 24 + 4);
                shortBuffer.clear();
                channel.read(shortBuffer);
                shortBuffer.flip();
                catEntries.get(i).i04 = toUShort(shortBuffer.getShort());

                channel.position(entryOffset + i * 24 + 6);
                shortBuffer.clear();
                channel.read(shortBuffer);
                shortBuffer.flip();
                catEntries.get(i).skillId2 = toUShort(shortBuffer.getShort());

                channel.position(entryOffset + i * 24 + 8);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                catEntries.get(i).charaCode = StandardCharsets.ISO_8859_1.decode(intBuffer).toString().trim();

                channel.position(entryOffset + i * 24 + 12);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                catEntries.get(i).i12 = intBuffer.getInt();
                
                channel.position(entryOffset + i * 24 + 16);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                catEntries.get(i).i16 = intBuffer.getInt();

                channel.position(entryOffset + i * 24 + 20);
                byteBuffer.clear();
                channel.read(byteBuffer);
                byteBuffer.flip();
                catEntries.get(i).i20 = toUByte(byteBuffer.get());
                
                channel.position(entryOffset + i * 24 + 21);
                byteBuffer.clear();
                channel.read(byteBuffer);
                byteBuffer.flip();
                catEntries.get(i).transformationEntry = toUByte(byteBuffer.get());

                channel.position(entryOffset + i * 24 + 22);
                shortBuffer.clear();
                channel.read(shortBuffer);
                shortBuffer.flip();
                catEntries.get(i).i22 = toUShort(shortBuffer.getShort());
            }    
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void catWriter(Path path) {
        try(FileChannel channel = FileChannel.open(path, StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            int entriesOffset = 12;

            ByteBuffer byteBuffer = ByteBuffer.allocate(1).order(ByteOrder.LITTLE_ENDIAN);
            ByteBuffer shortBuffer = ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN);
            ByteBuffer intBuffer = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
            
            channel.position(0);
            channel.write(ByteBuffer.wrap(new byte[]{0x23, 0x43, 0x41, 0x54}));
            
            channel.position(4);
            channel.write(ByteBuffer.wrap(new byte[]{(byte)0xFE, (byte)0xFF}));
        
            channel.position(6);
            shortBuffer.clear();
            shortBuffer.putShort((short) listView.getItems().size());
            shortBuffer.flip();
            channel.write(shortBuffer);

            channel.position(8);
            intBuffer.clear();
            intBuffer.putInt(entriesOffset);
            intBuffer.flip();
            channel.write(intBuffer);

            for (int i = 0; i < listView.getItems().size(); i++) {
                CatEntry catEntry = catEntries.get(i);

                channel.position(entriesOffset + i * 24);
                shortBuffer.clear();
                shortBuffer.putShort((short)catEntry.charaId);
                shortBuffer.flip();
                channel.write(shortBuffer);

                channel.position(entriesOffset + i * 24 + 2);
                shortBuffer.clear();
                shortBuffer.putShort((short)catEntry.costume);
                shortBuffer.flip();
                channel.write(shortBuffer);

                channel.position(entriesOffset + i * 24 + 4);
                shortBuffer.clear();
                shortBuffer.putShort((short)catEntry.i04);
                shortBuffer.flip();
                channel.write(shortBuffer);

                channel.position(entriesOffset + i * 24 + 6);
                shortBuffer.clear();
                shortBuffer.putShort((short)catEntry.skillId2);
                shortBuffer.flip();
                channel.write(shortBuffer);
                
                channel.position(entriesOffset + i * 24 + 8);
                intBuffer.clear();
                intBuffer.put(catEntry.charaCode.getBytes(StandardCharsets.ISO_8859_1));
                intBuffer.flip();
                channel.write(intBuffer);

                channel.position(entriesOffset + i * 24 + 12);
                intBuffer.clear();
                intBuffer.putInt(catEntry.i12);
                intBuffer.flip();
                channel.write(intBuffer);
            
                channel.position(entriesOffset + i * 24 + 16);
                intBuffer.clear();
                intBuffer.putInt(catEntry.i16);
                intBuffer.flip();
                channel.write(intBuffer);
        
                channel.position(entriesOffset + i * 24 + 20);
                byteBuffer.clear();
                byteBuffer.put((byte)catEntry.i20);
                byteBuffer.flip();
                channel.write(byteBuffer);

                channel.position(entriesOffset + i * 24 + 21);
                byteBuffer.clear();
                byteBuffer.put((byte)catEntry.transformationEntry);
                byteBuffer.flip();
                channel.write(byteBuffer);

                channel.position(entriesOffset + i * 24 + 22);
                shortBuffer.clear();
                shortBuffer.putShort((short)catEntry.i22);
                shortBuffer.flip();
                channel.write(shortBuffer);
            }
        } catch(IOException e) {
            e.printStackTrace();
        } 
    }

    public static enum CatValues {
        CharaID(0),
        Costume(1),
        I04(2),
        SkillID2(3),
        CharaCode(4),
        I12(5),
        I16(6),
        I20(7),
        TransformationEntry(8),
        I22(9);

        final int index;

        CatValues(int index) {
            this.index = index;
        }
    }
}
class CatEntry{
    public int charaId;
    public int costume;
    public int i04;
    public int skillId2;
    public String charaCode = "";
    public int i12;
    public int i16;
    public int i20;
    public int transformationEntry;
    public int i22;

    public CatEntry() {}
    public CatEntry(CatEntry other) {
        this.charaId = other.charaId;
        this.costume = other.costume;
        this.i04 = other.i04;
        this.skillId2 = other.skillId2;
        this.charaCode = other.charaCode;
        this.i12 = other.i12;
        this.i16 = other.i16;
        this.i20 = other.i20;
        this.transformationEntry = other.transformationEntry;
        this.i22 = other.i22;
    }
}