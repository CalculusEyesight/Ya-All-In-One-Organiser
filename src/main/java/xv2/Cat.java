package xv2;
import static xv2.BinaryUtilities.toUByte;
import static xv2.BinaryUtilities.toUShort;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
public class Cat {
    ArrayList<String> allEntries;
    ArrayList <CatEntry> catEntries = new ArrayList<>();

    ListView <String> listView = new ListView<>();
    HBox outerHBox = new HBox(2);

    CatEntry copyContainer = null;

    ContextMenu contextMenu = new ContextMenu();
    MenuItem copy = new MenuItem("Copy Ctrl+C");
    MenuItem paste = new MenuItem("Paste Ctrl+V");
    MenuItem delete = new MenuItem("Delete Del");
    MenuItem append = new MenuItem("Append Ctrl+A");
    MenuItem insert = new MenuItem("Insert Ctrl+I");

    public Cat() {
        entriesActionListener();
        entriesKeysListener();
    }

    public HBox createHBoxOuter() {
        outerHBox.getChildren().addAll(listView, new VBox());
        return this.outerHBox;
    }

    private VBox createCatVBox(CatEntry entry) {
        //charaId
        Label charaIdLabel = new Label("Chara ID");
        charaIdLabel.setPrefWidth(170);

        TextField charaIdTextField = new TextField(String.valueOf(entry.charaId));
        charaIdTextField.textProperty().addListener((obs, oldText, newText) -> {
            if (charaIdTextField.getText().contains("-")) {
                return;
            }
            try {
                entry.charaId = Integer.parseInt(newText); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox charaIdHBox = new HBox(charaIdLabel, charaIdTextField);
        charaIdHBox.setAlignment(Pos.CENTER_LEFT);
        //charaId

        //costume
        Label costumeLabel = new Label("Costume");
        costumeLabel.setPrefWidth(170);

        TextField costumeTextField=new TextField(String.valueOf(entry.costume));
        costumeTextField.textProperty().addListener((obs, oldText, newText) -> {
            if (costumeTextField.getText().contains("-")) {
                return;
            }
            try {
                entry.costume = Integer.parseInt(newText); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox costumeHBox = new HBox(costumeLabel, costumeTextField);
        costumeHBox.setAlignment(Pos.CENTER_LEFT);

        //i04
        Label i04Label = new Label("I_04");
        i04Label.setPrefWidth(170);

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

        //skillId2
        Label skillId2Label = new Label("Skill Id 2");
        skillId2Label.setPrefWidth(170);

        TextField skillId2TextField = new TextField(String.valueOf(entry.skillId2));
        skillId2TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (skillId2TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.skillId2 = Integer.parseInt(newText); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox skillId2HBox = new HBox(skillId2Label, skillId2TextField);
        skillId2HBox.setAlignment(Pos.CENTER_LEFT);
        //skillId2

        //charaCode
        Label charaCodeLabel = new Label("Chara Code");
        charaCodeLabel.setPrefWidth(170);

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

        HBox charaCodeHBox = new HBox(charaCodeLabel, charaCodeTextField);
        charaCodeHBox.setAlignment(Pos.CENTER_LEFT);
        //charaCode

        //i12
        Label i12Label = new Label("I_12");
        i12Label.setPrefWidth(170);

        TextField i12TextField = new TextField(String.valueOf(entry.i12));
        i12TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (i12TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.i12 = Integer.parseInt(newText); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox i12HBox=new HBox(i12Label, i12TextField);
        i12HBox.setAlignment(Pos.CENTER_LEFT);
        //i12

        //i16
        Label i16Label = new Label("Loading Screen Value?");
        i16Label.setPrefWidth(170);

        TextField i16TextField = new TextField(String.valueOf(entry.i16));
        i16TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (i16TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.i16 = Integer.parseInt(newText); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox i16HBox = new HBox(i16Label, i16TextField);
        i16HBox.setAlignment(Pos.CENTER_LEFT);
        //i16

        //i20
        Label i20Label = new Label("I_20");
        i20Label.setPrefWidth(170);

        TextField i20TextField = new TextField(String.valueOf(entry.i20));
        i20TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (i20TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.i20 = Integer.parseInt(newText); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox i20HBox = new HBox(i20Label, i20TextField);
        i20HBox.setAlignment(Pos.CENTER_LEFT);
        //i20

        //transformationEntry
        Label transformationEntryLabel = new Label("Trasnformation Entry");
        transformationEntryLabel.setPrefWidth(170);

        TextField transformationEntryTextField = new TextField(String.valueOf(entry.transformationEntry));
        transformationEntryTextField.textProperty().addListener((obs, oldText, newText) -> {
            if (transformationEntryTextField.getText().contains("-")) {
                return;
            }
            try {
                entry.transformationEntry = Integer.parseInt(newText); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox transformationEntryHBox = new HBox(transformationEntryLabel, transformationEntryTextField);
        transformationEntryHBox.setAlignment(Pos.CENTER_LEFT);
        //transformationEntry

        //i22
        Label i22Label = new Label("I_22");
        i22Label.setPrefWidth(170);

        TextField i22TextField = new TextField(String.valueOf(entry.i22));
        i22TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (i22TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.i22 = Integer.parseInt(newText); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox i22HBox = new HBox(i22Label, i22TextField);
        i22HBox.setAlignment(Pos.CENTER_LEFT);
        //i22

        VBox catVBox = new VBox(30,
            charaIdHBox, costumeHBox,
            i04HBox, skillId2HBox,
            charaCodeHBox, i12HBox,
            i16HBox, i20HBox,
            transformationEntryHBox, i22HBox
        );
        catVBox.setPadding(new Insets(20, 0, 0, 5));
        return catVBox;
    }

    private void entriesActionListener() {
        paste.setDisable(true);

        contextMenu.getItems().addAll(copy,paste,delete,append,insert);

        listView.setContextMenu(contextMenu);
        listView.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue == null) return;

            outerHBox.getChildren().remove(1);
            outerHBox.getChildren().add(1, createCatVBox(catEntries.get(listView.getSelectionModel().getSelectedIndex())));
        });
        listView.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.SECONDARY) {
                contextMenu.setOnAction(event -> {
                    if (event.getTarget() == copy) {
                        Copy();
                        paste.setDisable(false);
                    }
                    if (event.getTarget() == paste) {
                        Paste();
                    }
                    if (event.getTarget() == delete) {
                       Delete();
                    }
                    if (event.getTarget() == append) {
                        Append();
                    }
                    if (event.getTarget() == insert) {
                        Insert();
                    }
                });
            }
        });
    }

    private void entriesKeysListener() {
        listView.setOnKeyPressed(e -> {
            if (e.isControlDown() && e.getCode() == KeyCode.C) {
                Copy();
            }
            if (e.isControlDown() && e.getCode()==KeyCode.V) {
                Paste();
            }
            if (e.getCode() == KeyCode.DELETE) {
                Delete();
            }
            if (e.isControlDown() && e.getCode() == KeyCode.A) {
                Append();
            }
            if (e.isControlDown() && e.getCode() == KeyCode.I) {
                Insert();
            }
        });
    }

    private void Copy() {
        copyContainer = new CatEntry(catEntries.get(listView.getSelectionModel().getSelectedIndex()));
    }

    private void Paste() {
        if (copyContainer == null) return;
        
        catEntries.set(listView.getSelectionModel().getSelectedIndex(), new CatEntry(copyContainer));

        outerHBox.getChildren().remove(1);
        outerHBox.getChildren().add(1, createCatVBox(catEntries.get(listView.getSelectionModel().getSelectedIndex())));
    }

    private void Delete() {
        if (listView.getSelectionModel().getSelectedIndex() == 0) return;

        catEntries.remove(listView.getSelectionModel().getSelectedIndex());

        for (int i = 0; i < listView.getItems().size(); i++) {
            allEntries.set(i,new String("Entry: " + i));
            listView.getItems().set(i,allEntries.get(i));
        }
    }

    private void Append() {
        catEntries.add(listView.getSelectionModel().getSelectedIndex() + 1, new CatEntry());
        allEntries.add(new String("Entry " + listView.getItems().size()));
        listView.getItems().add(allEntries.getLast());
    }

    private void Insert() {
        if (listView.getSelectionModel().getSelectedIndex() > 0) {
            catEntries.add(listView.getSelectionModel().getSelectedIndex() - 1, new CatEntry());
            allEntries.add(new String("Entry " + listView.getItems().size()));
            listView.getItems().add(allEntries.getLast());
        }
        else if (listView.getSelectionModel().getSelectedIndex() == 0) {
            catEntries.add(listView.getSelectionModel().getSelectedIndex(), new CatEntry());
            allEntries.add(new String("Entry " + listView.getItems().size()));
            listView.getItems().add(allEntries.getLast());
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

            allEntries = new ArrayList<>(catEntriesCount);

            for (int i = 0; i < catEntriesCount; i++) {
                allEntries.add(new String("Entry " + i));
                listView.getItems().add(allEntries.get(i));
            }

            for (int i = 0; i < catEntriesCount; i++) {
                CatEntry catEntry = new CatEntry();
                catEntries.add(catEntry);

                channel.position(entryOffset + i * 24);
                shortBuffer.clear();
                channel.read(shortBuffer);
                shortBuffer.flip();
                catEntry.charaId = toUShort(shortBuffer.getShort());
                
                channel.position(entryOffset +  i * 24 + 2);
                shortBuffer.clear();
                channel.read(shortBuffer);
                shortBuffer.flip();
                catEntry.costume = toUShort(shortBuffer.getShort());

                channel.position(entryOffset + i * 24 + 4);
                shortBuffer.clear();
                channel.read(shortBuffer);
                shortBuffer.flip();
                catEntry.i04 = toUShort(shortBuffer.getShort());

                channel.position(entryOffset + i * 24 + 6);
                shortBuffer.clear();
                channel.read(shortBuffer);
                shortBuffer.flip();
                catEntry.skillId2 = toUShort(shortBuffer.getShort());

                channel.position(entryOffset + i * 24 + 8);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                catEntry.charaCode = StandardCharsets.ISO_8859_1.decode(intBuffer).toString().trim();

                channel.position(entryOffset + i * 24 + 12);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                catEntry.i12 = intBuffer.getInt();
                
                channel.position(entryOffset + i * 24 + 16);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                catEntry.i16 = intBuffer.getInt();

                channel.position(entryOffset + i * 24 + 20);
                byteBuffer.clear();
                channel.read(byteBuffer);
                byteBuffer.flip();
                catEntry.i20 = toUByte(byteBuffer.get());
                
                channel.position(entryOffset + i * 24 + 21);
                byteBuffer.clear();
                channel.read(byteBuffer);
                byteBuffer.flip();
                catEntry.transformationEntry = toUByte(byteBuffer.get());

                channel.position(entryOffset + i * 24 + 22);
                shortBuffer.clear();
                channel.read(shortBuffer);
                shortBuffer.flip();
                catEntry.i22 = toUShort(shortBuffer.getShort());
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
            shortBuffer.putShort((short)allEntries.size());
            shortBuffer.flip();
            channel.write(shortBuffer);

            channel.position(8);
            intBuffer.clear();
            intBuffer.putInt(entriesOffset);
            intBuffer.flip();
            channel.write(intBuffer);

            for (int i = 0; i < allEntries.size(); i++) {
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
        }catch(IOException e) {
            e.printStackTrace();
        } 
    }
}
class CatEntry{
    public int charaId;
    public int costume;
    public int i04;
    public int skillId2;
    public String charaCode = new String();
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