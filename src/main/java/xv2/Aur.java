package xv2;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Aur {
    VBox vBox = new VBox(10);
    HBox hBox = new HBox(10);
    ListView<String> listView = new ListView<>();

    ArrayList<AuraEntry> auraEntries = new ArrayList<>();
    ArrayList<CharaEntry> charaEntries = new ArrayList<>();

    AuraEntry copyContainer = null;

    ContextMenu contextMenu = new ContextMenu();
    MenuItem copy = new MenuItem("Copy Ctrl+C");
    MenuItem paste = new MenuItem("Paste Ctrl+V");
    MenuItem delete = new MenuItem("Delete Del");
    MenuItem append = new MenuItem("Append Ctrl+A");
    MenuItem insert = new MenuItem("Insert Ctrl+I");

    int findIndex = 0;
    String findText = null;
    Object [] indexList = new Object[] {findIndex, findText};

    public Aur() {
        vBox.setPadding(new Insets(5, 5, 5, 5));

        entriesActionListener();
        entriesKeysListener();
        vBoxListener();
    }

    public SplitPane createSplitPane() {
       SplitPane splitPane = new SplitPane(createAuraIDHBox(), createCharaIDVBox());
       splitPane.setDividerPositions(0.43);

       return splitPane;
    }

    private HBox createAuraIDHBox() {
        this.hBox.getChildren().addAll(listView, new VBox());

        return hBox;
    }

    private VBox createAuraIdVBox(AuraEntry entry) {
        VBox auraIdVBox = new VBox(70, 
            createHBox(createLabel("I_04", 120), createAuraTextField(entry.i04 ,AuraValues.I04)), 
            createHBox(createLabel("BoostStart", 120), createAuraTextField(entry.boostStart, AuraValues.BoostStart)),
            createHBox(createLabel("BoostLoop", 120), createAuraTextField(entry.boostLoop ,AuraValues.BoostLoop)), 
            createHBox(createLabel("BoostEnd", 120), createAuraTextField(entry.boostEnd ,AuraValues.BoostEnd)),
            createHBox(createLabel("KiaiCharge", 120), createAuraTextField(entry.kiaiCharge, AuraValues.KiaiCharge)), 
            createHBox(createLabel("KiryokuMax", 120), createAuraTextField(entry.kiryokuMax, AuraValues.KiryokuMax)),
            createHBox(createLabel("HenshinStart", 120), createAuraTextField(entry.henshinStart, AuraValues.HenshinStart)), 
            createHBox(createLabel("HenshinEnd", 120), createAuraTextField(entry.henshinEnd, AuraValues.HenshinEnd))
        );
        auraIdVBox.setPadding(new Insets(20, 0, 0, 0));

        return auraIdVBox;
    }

    private ScrollPane createScrollPane() {
        return new ScrollPane(vBox);
    }

    private VBox createCharaIDVBox() {
        return new VBox(createToolBarRight(), createScrollPane());
    }

    private Label createLabel(String text, int width) {
        Label label = new Label(text);
        if (width != 0) label.setPrefWidth(width);

        return label;
    }

    private TextField createAuraTextField(int value, AuraValues auraValue) {
        TextField textField = new TextField(String.valueOf(value));
        textField.textProperty().addListener((obs, oldText, newText) -> {
            if (textField.getText().contains("-")) {
                return;
            }
            try {
                switch (auraValue) {
                    case AuraValues.I04 -> auraEntries.get(listView.getSelectionModel().getSelectedIndex()).i04 = Integer.parseInt(newText);
                    case AuraValues.BoostStart -> auraEntries.get(listView.getSelectionModel().getSelectedIndex()).boostStart = Integer.parseInt(newText);
                    case AuraValues.BoostLoop -> auraEntries.get(listView.getSelectionModel().getSelectedIndex()).boostLoop = Integer.parseInt(newText);
                    case AuraValues.BoostEnd -> auraEntries.get(listView.getSelectionModel().getSelectedIndex()).boostEnd = Integer.parseInt(newText);
                    case AuraValues.KiaiCharge -> auraEntries.get(listView.getSelectionModel().getSelectedIndex()).kiaiCharge = Integer.parseInt(newText);
                    case AuraValues.KiryokuMax -> auraEntries.get(listView.getSelectionModel().getSelectedIndex()).kiryokuMax = Integer.parseInt(newText);
                    case AuraValues.HenshinStart -> auraEntries.get(listView.getSelectionModel().getSelectedIndex()).henshinStart = Integer.parseInt(newText);
                    case AuraValues.HenshinEnd -> auraEntries.get(listView.getSelectionModel().getSelectedIndex()).henshinEnd = Integer.parseInt(newText);
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        return textField;
    }

    private TextField createCharaTextField(int i, int value, CharaValues charaValue) {
        TextField textField = new TextField(String.valueOf(value));
        textField.textProperty().addListener((obs, oldText, newText) -> {
            if (textField.getText().contains("-")) {
                return;
            }
            try {
                switch (charaValue) {
                    case CharaValues.CharaID -> charaEntries.get(i).charaId = Integer.parseInt(newText);
                    case CharaValues.Costume -> charaEntries.get(i).costume = Integer.parseInt(newText);
                    case CharaValues.AuraID -> charaEntries.get(i).auraId = Integer.parseInt(newText);
                    default -> throw new IllegalArgumentException("Unexpected value: " + charaValue);
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        return textField;
    }

    private CheckBox createCharaChekcBox(int i, boolean value, CharaValues charaValue) {
        CheckBox checkBox = new CheckBox("Glare");
        checkBox.setSelected(value);
        checkBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
            switch (charaValue) {
                case CharaValues.Glare -> charaEntries.get(i).glare = newValue;
                default -> throw new IllegalArgumentException("Unexpected value: " + charaValue);
            } 
        });

        return checkBox;
    }

    private HBox createHBox(Label label, TextField textField) {
        HBox hBox = new HBox(label, textField);
        hBox.setAlignment(Pos.CENTER_LEFT);

        return hBox;
    }

    private Button insertEntry() {
        Button insertEntry = new Button("Insert Chara ID");

        insertEntry.setOnAction(event -> {
            charaEntries.add(new CharaEntry());
            createCharaIdVBox(charaEntries.getLast());
        });

        return insertEntry;
    }

    private Button removeEntry() {
        Button removeEntry = new Button("Remove Chara ID");

        removeEntry.setOnAction(event -> {
            try {
                vBox.getChildren().remove(charaEntries.size() - 1);
                charaEntries.remove(charaEntries.size() - 1);
            } catch (IndexOutOfBoundsException e) {
                Popups.ErrorOutOfBounds();
                e.printStackTrace();
            }
        });

        return removeEntry;
    }

    private ToolBar createToolBarRight() {
        return new ToolBar(insertEntry(), removeEntry());
    }
    
    private VBox createCharaIdVBox(CharaEntry entry) {
        HBox hBox = new HBox(10,
            createLabel("Chara ID", 0), createCharaTextField(charaEntries.indexOf(entry), entry.charaId, CharaValues.CharaID),
            createLabel("Costume", 0), createCharaTextField(charaEntries.indexOf(entry), entry.costume, CharaValues.Costume),
            createLabel("Aura ID", 0), createCharaTextField(charaEntries.indexOf(entry), entry.auraId, CharaValues.AuraID) ,
                                                    createCharaChekcBox(charaEntries.indexOf(entry), entry.glare, CharaValues.Glare)
        );
        hBox.setAlignment(Pos.CENTER_LEFT);

        vBox.getChildren().add(hBox);

        return vBox;
    }

    private void entriesActionListener() {
        paste.setDisable(true);

        contextMenu.getItems().addAll(copy, paste, delete, append, insert);

        listView.setContextMenu(contextMenu);

        listView.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue == null) return;

            hBox.getChildren().remove(1);
            hBox.getChildren().add(1, createAuraIdVBox(auraEntries.get(listView.getSelectionModel().getSelectedIndex())));
        });

        listView.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.SECONDARY) {
                contextMenu.setOnAction(event -> {
                    if (event.getTarget() == copy) Copy();
                    else if (event.getTarget() == paste) Paste();
                    else if (event.getTarget() == delete) Delete();
                    else if (event.getTarget() == append) Append();
                    else if (event.getTarget() == insert) Insert();
                });
            }
        });
    }

    private void vBoxListener() {
        vBox.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {});
    }

    private void listViewSearch(AuraValues auraValue) {
        int counter = 0;
        int value = 0;
        int listIndex = listView.getSelectionModel().getSelectedIndex();
        int textFieldIndex = auraValue.index;
        boolean found = false;

        do {

            switch (auraValue) {
                case AuraValues.I04 -> value = auraEntries.get(listIndex).i04;
                case AuraValues.BoostStart -> value = auraEntries.get(listIndex).boostStart;
                case AuraValues.BoostLoop -> value = auraEntries.get(listIndex).boostLoop;
                case AuraValues.BoostEnd -> value = auraEntries.get(listIndex).boostEnd;
                case AuraValues.KiaiCharge -> value = auraEntries.get(listIndex).kiaiCharge;
                case AuraValues.KiryokuMax -> value = auraEntries.get(listIndex).kiryokuMax;
                case AuraValues.HenshinStart -> value = auraEntries.get(listIndex).henshinStart;
                case AuraValues.HenshinEnd -> value = auraEntries.get(listIndex).henshinEnd;
            }

            if (indexList[1] != null && value == Integer.parseInt((String) indexList[1]) && listView.getSelectionModel().getSelectedIndex() != listIndex) {
                listView.getSelectionModel().select(listIndex);
                ((TextField) ((HBox) ((VBox) hBox.getChildren().get(1)).getChildren().get(textFieldIndex)).getChildren().get(1)).requestFocus();
                ((TextField) ((HBox) ((VBox) hBox.getChildren().get(1)).getChildren().get(textFieldIndex)).getChildren().get(1)).selectAll();
                found = true;
                break;
            }

            listIndex++;
            counter++;

            if (listIndex == listView.getItems().size()) listIndex = 0;
        } while (counter != listView.getItems().size());

        if (!found) {
            Popups.ItemNotFound();
        }
    }

    private void entriesKeysListener() {
        listView.setOnKeyPressed(e -> {
            if (e.isControlDown() && e.getCode() == KeyCode.C) Copy();
            else if (e.isControlDown() && e.getCode() == KeyCode.V) Paste();
            else if (e.getCode() == KeyCode.DELETE) Delete();
            else if (e.isControlDown() && e.getCode() == KeyCode.A) Append();
            else if (e.isControlDown() && e.getCode() == KeyCode.I) Insert();
            else if (e.isControlDown() && e.getCode() == KeyCode.F) {
                ButtonType findNextButtonType = new ButtonType("Find Next", ButtonData.NEXT_FORWARD);
                ButtonType cancelButtonType = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

                Dialog<String> dialog = new Dialog<>();
                dialog.setTitle("Find");
                dialog.getDialogPane().getButtonTypes().addAll(findNextButtonType, cancelButtonType);
                dialog.getDialogPane().setContent(Popups.createFindDialog("Aura Entry: ", indexList, 
                    FXCollections.observableArrayList(
                        "I_04", 
                        "BoostStart", 
                        "BoostLoop", 
                        "BoostEnd", 
                        "KiaiCharge", 
                        "KiryokuMax", 
                        "HenshinStart", 
                        "HenshinEnd"
                    )));

                final Button findbt = (Button) dialog.getDialogPane().lookupButton(findNextButtonType);
                findbt.addEventFilter(ActionEvent.ACTION, event -> {
                    if (!findbt.isPressed()) {
                        switch ((int) indexList[0]) {
                            case 0 -> listViewSearch(AuraValues.I04);
                            case 1 -> listViewSearch(AuraValues.BoostStart);
                            case 2 -> listViewSearch(AuraValues.BoostLoop);
                            case 3 -> listViewSearch(AuraValues.BoostEnd);
                            case 4 -> listViewSearch(AuraValues.KiaiCharge);
                            case 5 -> listViewSearch(AuraValues.KiryokuMax);
                            case 6 -> listViewSearch(AuraValues.HenshinStart);
                            case 7 -> listViewSearch(AuraValues.HenshinEnd);
                        }
                        
                        event.consume();
                    }
                });
                dialog.showAndWait();
            }
        });
    }

    private void Copy() {
        paste.setDisable(false);
        copyContainer = new AuraEntry(auraEntries.get(listView.getSelectionModel().getSelectedIndex()));
    }

    private void Paste() {
        if (copyContainer == null) return;

        auraEntries.set(listView.getSelectionModel().getSelectedIndex(), new AuraEntry(copyContainer));
        hBox.getChildren().remove(1);
        hBox.getChildren().add(1, createAuraIdVBox(auraEntries.get(listView.getSelectionModel().getSelectedIndex())));
    }

    private void Delete() {
        if (listView.getSelectionModel().getSelectedIndex() == 0) return;
        
        auraEntries.remove(listView.getSelectionModel().getSelectedIndex());
        listView.getItems().remove(listView.getSelectionModel().getSelectedIndex());

        for (int i = 0; i < listView.getItems().size(); i++) {
            listView.getItems().set(i, "Aura ID " + i);
        }
    }

    private void Append() {
        auraEntries.add(listView.getSelectionModel().getSelectedIndex() + 1, new AuraEntry());

        listView.getItems().add("Aura ID " + listView.getItems().size());
    }

    private void Insert() {
        if (listView.getSelectionModel().getSelectedIndex() > 0) {
            auraEntries.add(listView.getSelectionModel().getSelectedIndex() - 1, new AuraEntry());

            listView.getItems().add("Aura ID " + listView.getItems().size());
        }
        else if (listView.getSelectionModel().getSelectedIndex() == 0) {
            auraEntries.add(listView.getSelectionModel().getSelectedIndex(), new AuraEntry());

            listView.getItems().add("Aura ID " + listView.getItems().size());
        }
    }

    public void aurReader(Path path) {
        try(FileChannel channel = FileChannel.open(path, StandardOpenOption.READ)) {
            int auraoffset = 32;
            int charaOffset = 0;
            int effectsOffset = 0;
            int auraEntriesCount;
            
            ByteBuffer intBuffer = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
            
            channel.position(8);
            intBuffer.clear();
            channel.read(intBuffer);
            intBuffer.flip();
            auraEntriesCount = intBuffer.getInt();

            for (int i = 0; i < auraEntriesCount; i++) {
                listView.getItems().add("Aura ID " + i);
            }

            channel.position(24);
            intBuffer.clear();
            channel.read(intBuffer);
            intBuffer.flip();
            int characterEntries = intBuffer.getInt();

            channel.position(28);
            intBuffer.clear();
            channel.read(intBuffer);
            intBuffer.flip();
            charaOffset = intBuffer.getInt();

            for (int i = 0; i < auraEntriesCount; i++) {
                auraEntries.add(new AuraEntry());

                channel.position(auraoffset + i * 16 + 4);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                auraEntries.get(i).i04 = intBuffer.getInt();

                channel.position(auraoffset + i * 16 + 12);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                effectsOffset = intBuffer.getInt();

                channel.position(effectsOffset + 4);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                auraEntries.get(i).boostStart = intBuffer.getInt();

                channel.position(effectsOffset + 12);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                auraEntries.get(i).boostLoop = intBuffer.getInt();

                channel.position(effectsOffset + 20);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                auraEntries.get(i).boostEnd = intBuffer.getInt();

                channel.position(effectsOffset + 28);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                auraEntries.get(i).kiaiCharge = intBuffer.getInt();

                channel.position(effectsOffset + 36);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                auraEntries.get(i).kiryokuMax = intBuffer.getInt();

                channel.position(effectsOffset + 44);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                auraEntries.get(i).henshinStart = intBuffer.getInt();

                channel.position(effectsOffset + 52);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                auraEntries.get(i).henshinEnd = intBuffer.getInt();
            }

            for (int i = 0; i < characterEntries; i++) {
                charaEntries.add(new CharaEntry());

                channel.position(charaOffset + i * 16);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                charaEntries.get(i).charaId = intBuffer.getInt();
    
                channel.position(charaOffset + i * 16 + 4);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                charaEntries.get(i).costume = intBuffer.getInt();

                channel.position(charaOffset + i * 16 + 8);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                charaEntries.get(i).auraId = intBuffer.getInt();
                
                channel.position(charaOffset + i * 16 + 12);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                charaEntries.get(i).glare = (intBuffer.getInt() ==  1);

                createCharaIdVBox(charaEntries.get(i));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void aurWriter(Path path) {
        try(FileChannel channel = FileChannel.open(path, StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            int effectCount = 7;
            int auraOffset = 32;
            int auraTypeOffset = 32 + listView.getItems().size() * 72;
            int charaOffset = 32 + listView.getItems().size() * 72 + 120;
            String auraTypes =  
            "BoostStart\0" + 
            "BoostLoop\0" + 
            "BoostEnd\0" + 
            "KiaiCharge\0" + 
            "KiryokuMax\0" + 
            "HenshinStart\0" + 
            "HenshinEnd\0";

            ByteBuffer intBuffer = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
            
            channel.write(ByteBuffer.wrap(new byte[]{0x23, 0x41, 0x55, 0x52}));

            channel.position(4);
            channel.write(ByteBuffer.wrap(new byte[]{(byte)0xFE, (byte)0xFF}));
            
            channel.position(6);
            channel.write(ByteBuffer.wrap(new byte[]{0x20, 0x00}));

            channel.position(8);
            intBuffer.clear();
            intBuffer.putInt(listView.getItems().size());
            intBuffer.flip();
            channel.write(intBuffer);

            channel.position(12);
            intBuffer.clear();
            intBuffer.putInt(auraOffset);
            intBuffer.flip();
            channel.write(intBuffer);

            channel.position(16);
            intBuffer.clear();
            intBuffer.putInt(effectCount);
            intBuffer.flip();
            channel.write(intBuffer);

            channel.position(20);
            intBuffer.clear();
            intBuffer.putInt(auraTypeOffset);
            intBuffer.flip();
            channel.write(intBuffer);

            channel.position(24);
            intBuffer.clear();
            intBuffer.putInt(charaEntries.size());
            intBuffer.flip();
            channel.write(intBuffer);

            channel.position(28);
            intBuffer.clear();
            intBuffer.putInt(charaOffset);
            intBuffer.flip();
            channel.write(intBuffer);

            channel.position(auraTypeOffset);
            intBuffer.clear();
            intBuffer.putInt(auraTypeOffset + 28);
            intBuffer.flip();
            channel.write(intBuffer);

            channel.position(auraTypeOffset + 4);
            intBuffer.clear();
            intBuffer.putInt(auraTypeOffset + 39);
            intBuffer.flip();
            channel.write(intBuffer);

            channel.position(auraTypeOffset + 8);
            intBuffer.clear();
            intBuffer.putInt(auraTypeOffset + 49);
            intBuffer.flip();
            channel.write(intBuffer);

            channel.position(auraTypeOffset + 12);
            intBuffer.clear();
            intBuffer.putInt(auraTypeOffset + 58);
            intBuffer.flip();
            channel.write(intBuffer);

            channel.position(auraTypeOffset + 16);
            intBuffer.clear();
            intBuffer.putInt(auraTypeOffset + 69);
            intBuffer.flip();
            channel.write(intBuffer);

            channel.position(auraTypeOffset + 20);
            intBuffer.clear();
            intBuffer.putInt(auraTypeOffset + 80);
            intBuffer.flip();
            channel.write(intBuffer);

            channel.position(auraTypeOffset + 24);
            intBuffer.clear();
            intBuffer.putInt(auraTypeOffset + 93);
            intBuffer.flip();
            channel.write(intBuffer);

            channel.position(auraTypeOffset + 28);
            channel.write(ByteBuffer.wrap(auraTypes.getBytes(StandardCharsets.ISO_8859_1)));

            for (int i = 0; i < listView.getItems().size(); i++) {
                AuraEntry auraEntry = auraEntries.get(i);
   
                channel.position(auraOffset + i * 16);
                intBuffer.clear();
                intBuffer.putInt(listView.getItems().indexOf(listView.getItems().get(i)));
                intBuffer.flip();
                channel.write(intBuffer);

                channel.position(auraOffset + i * 16 + 4);
                intBuffer.clear();
                intBuffer.putInt(auraEntry.i04);
                intBuffer.flip();
                channel.write(intBuffer);

                channel.position(auraOffset + i * 16 + 8);
                intBuffer.clear();
                intBuffer.putInt(effectCount);
                intBuffer.flip();
                channel.write(intBuffer);
                
                channel.position(auraOffset+ i * 16 + 12);
                intBuffer.clear();
                intBuffer.putInt(listView.getItems().size() * 16 + 32 + i * 56);
                intBuffer.flip();
                channel.write(intBuffer);

                channel.position(listView.getItems().size() * 16 + 32 + i * 56 + 4);
                intBuffer.clear();
                intBuffer.putInt(auraEntry.boostStart);
                intBuffer.flip();
                channel.write(intBuffer);

                channel.position(listView.getItems().size() * 16 + 32 + i * 56 + 8);
                channel.write(ByteBuffer.wrap(new byte[]{0x01, 0x00, 0x00, 0x00}));

                channel.position(listView.getItems().size() * 16 + 32 + i * 56 + 12);
                intBuffer.clear();
                intBuffer.putInt(auraEntry.boostLoop);
                intBuffer.flip();
                channel.write(intBuffer);

                channel.position(listView.getItems().size() * 16 + 32 + i * 56 + 16);
                channel.write(ByteBuffer.wrap(new byte[]{0x02, 0x00, 0x00, 0x00}));
                
                channel.position(listView.getItems().size() * 16 + 32 + i * 56 + 20);
                intBuffer.clear();
                intBuffer.putInt(auraEntry.boostEnd);
                intBuffer.flip();
                channel.write(intBuffer);

                channel.position(listView.getItems().size() * 16 + 32 + i * 56 + 24);
                channel.write(ByteBuffer.wrap(new byte[]{0x03, 0x00, 0x00, 0x00}));

                channel.position(listView.getItems().size() * 16 + 32 + i * 56 + 28);
                intBuffer.clear();
                intBuffer.putInt(auraEntry.kiaiCharge);
                intBuffer.flip();
                channel.write(intBuffer);

                channel.position(listView.getItems().size() * 16 + 32 + i * 56 + 32);
                channel.write(ByteBuffer.wrap(new byte[]{0x04, 0x00, 0x00, 0x00}));
                
                channel.position(listView.getItems().size() * 16 + 32 + i * 56 + 36);
                intBuffer.clear();
                intBuffer.putInt(auraEntry.kiryokuMax);
                intBuffer.flip();
                channel.write(intBuffer);

                channel.position(listView.getItems().size() * 16 + 32 + i * 56 + 40);
                channel.write(ByteBuffer.wrap(new byte[]{0x05 ,0x00,0x00,0x00}));

                channel.position(listView.getItems().size() * 16 + 32 + i * 56 + 44);
                intBuffer.clear();
                intBuffer.putInt(auraEntry.henshinStart);
                intBuffer.flip();
                channel.write(intBuffer);

                channel.position(listView.getItems().size() * 16 + 32 + i * 56 + 48);
                channel.write(ByteBuffer.wrap(new byte[]{0x06, 0x00, 0x00, 0x00}));

                channel.position(listView.getItems().size() * 16 + 32 + i * 56 + 52);
                intBuffer.clear();
                intBuffer.putInt(auraEntry.henshinEnd);
                intBuffer.flip();
                channel.write(intBuffer);
            }

            for (int i = 0; i < charaEntries.size(); i++) {
                CharaEntry charaEntry = charaEntries.get(i);
            
                channel.position(charaOffset + i * 16);
                intBuffer.clear();
                intBuffer.putInt(charaEntry.charaId);
                intBuffer.flip();
                channel.write(intBuffer);
    
                channel.position(charaOffset + i * 16 + 4);
                intBuffer.clear();
                intBuffer.putInt(charaEntry.costume);
                intBuffer.flip();
                channel.write(intBuffer);

                channel.position(charaOffset + i * 16 + 8);
                intBuffer.clear();
                intBuffer.putInt(charaEntry.auraId);
                intBuffer.flip();
                channel.write(intBuffer);

                channel.position(charaOffset + i * 16 + 12);
                intBuffer.clear();
                intBuffer.putInt(charaEntry.glare ? 1 : 0);
                intBuffer.flip();
                channel.write(intBuffer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static enum AuraValues {
        I04(0),
        BoostStart(1),
        BoostLoop(2),
        BoostEnd(3),
        KiaiCharge(4),
        KiryokuMax(5),
        HenshinStart(6),
        HenshinEnd(7);

        final int index;

        AuraValues(int index) {
            this.index = index;
        }
    }

    public static enum CharaValues {
        CharaID,
        Costume,
        AuraID,
        Glare;
    }
}
class AuraEntry {
    public int i04 = 0;
    public int boostStart = 0;
    public int boostLoop = 0;
    public int boostEnd = 0;
    public int kiaiCharge = 0;
    public int kiryokuMax = 0;
    public int henshinStart = 0;
    public int henshinEnd = 0;

    public AuraEntry() {}

    public AuraEntry(AuraEntry other) {
        this.i04 = other.i04;
        this.boostStart = other.boostStart;
        this.boostLoop = other.boostLoop;
        this.boostEnd = other.boostEnd;
        this.kiaiCharge = other.kiaiCharge;
        this.kiryokuMax = other.kiryokuMax;
        this.henshinStart = other.henshinStart;
        this.henshinEnd = other.henshinEnd;
    }
}

class CharaEntry {
    public int charaId = 0;
    public int costume = 0;
    public int auraId = 0;
    public boolean glare = false;
}