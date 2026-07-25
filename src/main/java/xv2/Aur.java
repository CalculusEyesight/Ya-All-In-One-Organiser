package xv2;
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
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
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
    VBox vBox = new VBox(5);
    HBox hBox = new HBox(10);
    ListView<String> listView = new ListView<>();

    ArrayList<String> allEntries;
    ArrayList<AurAuraEntry> auraEntries = new ArrayList<>();
    ArrayList<AurCharaEntry> charaEntries = new ArrayList<>();

    AurAuraEntry copyContainer = null;
    int characterEntries;
    
    public Aur() {
        vBox.setPadding(new Insets(5, 5, 5, 5));
        entriesActionListener();
        entriesKeysListener();
        vBoxListener();
    }

    public SplitPane createSplitPane() {
       SplitPane splitPane = new SplitPane(createHBoxLeft(), createVBoxRight());
       splitPane.setDividerPositions(0.43);
       return splitPane;
    }

    private HBox createHBoxLeft() {
        this.hBox.getChildren().addAll(listView, new VBox());
        return hBox;
    }

    private VBox createAuraIdVBox(AurAuraEntry entry) {
        //i04
        Label i04Label = new Label("I_04");
        i04Label.setPrefWidth(80);

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

        HBox i04HBox = new HBox(40, i04Label, i04TextField);
        i04HBox.setAlignment(Pos.CENTER_LEFT);
        //i04

        //boostStart
        Label boostStartLabel = new Label("BoostStart");
        boostStartLabel.setPrefWidth(80);

        TextField boostStartTextField = new TextField(String.valueOf(entry.boostStart));
        boostStartTextField.textProperty().addListener((obs, oldText, newText) -> {
            if (boostStartTextField.getText().contains("-")) {
                return;
            }
            try {
                entry.boostStart = Integer.parseInt(newText); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox boostStartHBox = new HBox(40, boostStartLabel, boostStartTextField);
        boostStartHBox.setAlignment(Pos.CENTER_LEFT);
        //boostStart

        //boostLoop
        Label boostLoopLabel = new Label("BoostLoop");
        boostLoopLabel.setPrefWidth(80);

        TextField boostLoopTextField=new TextField(String.valueOf(entry.boostLoop));
        boostLoopTextField.textProperty().addListener((obs, oldText, newText) -> {
            if (boostLoopTextField.getText().contains("-")) {
                return;
            }
            try {
                entry.boostLoop = Integer.parseInt(newText); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox boostLoopHBox = new HBox(40, boostLoopLabel, boostLoopTextField);
        boostLoopHBox.setAlignment(Pos.CENTER_LEFT);
        //boostLoop

        //boostEnd
        Label boostEndLabel = new Label("BoostEnd");
        boostEndLabel.setPrefWidth(80);

        TextField boostEndTextField = new TextField(String.valueOf(entry.boostEnd));
        boostEndTextField.textProperty().addListener((obs, oldText, newText) -> {
            if (boostEndTextField.getText().contains("-")) {
                return;
            }
            try {
                entry.boostEnd = Integer.parseInt(newText); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox boostEndHBox = new HBox(40,boostEndLabel, boostEndTextField);
        boostEndHBox.setAlignment(Pos.CENTER_LEFT);

        //kiaiCharge
        Label kiaiChargeLabel = new Label("KiaiCharge");
        kiaiChargeLabel.setPrefWidth(80);

        TextField kiaiChargeTextField = new TextField(String.valueOf(entry.kiaiCharge));
        kiaiChargeTextField.textProperty().addListener((obs, oldText, newText) -> {
            if (kiaiChargeTextField.getText().contains("-")) {
                return;
            }
            try {
                entry.kiaiCharge = Integer.parseInt(newText); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox kiaiChargeHBox = new HBox(40, kiaiChargeLabel, kiaiChargeTextField);
        kiaiChargeHBox.setAlignment(Pos.CENTER_LEFT);

        //kiryokuMax
        Label kiryokuMaxLabel = new Label("KiryokuMax");
        kiryokuMaxLabel.setPrefWidth(80);

        TextField txtKiryokuMax=new TextField(String.valueOf(entry.kiryokuMax));
        txtKiryokuMax.textProperty().addListener((obs, oldText, newText) -> {
            if (txtKiryokuMax.getText().contains("-")) {
                return;
            }
            try {
                entry.kiryokuMax = Integer.parseInt(newText); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox kiryokuMaxHBox = new HBox(40, kiryokuMaxLabel, txtKiryokuMax);
        kiryokuMaxHBox.setAlignment(Pos.CENTER_LEFT);
        //kiryokuMax

        //henshinStart
        Label henshinStartLabel = new Label("HenshinStart");
        henshinStartLabel.setPrefWidth(80);

        TextField henshinStartTextField = new TextField(String.valueOf(entry.henshinStart));
        henshinStartTextField.textProperty().addListener((obs, oldText, newText) -> {
            if (henshinStartTextField.getText().contains("-")) {
                return;
            }
            try {
                entry.henshinStart = Integer.parseInt(newText); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox henshinStartHBox = new HBox(40, henshinStartLabel, henshinStartTextField);
        henshinStartHBox.setAlignment(Pos.CENTER_LEFT);
        //henshinStart

        //henshinEnd
        Label henshinEndLabel = new Label("HenshinEnd");
        henshinEndLabel.setPrefWidth(80);

        TextField henshinEndTextField = new TextField(String.valueOf(entry.henshinEnd));
        henshinEndTextField.textProperty().addListener((obs, oldText, newText) -> {
            if (henshinEndTextField.getText().contains("-")) {
                return;
            }
            try {
                entry.henshinEnd = Integer.parseInt(newText); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox henshinEndHBox = new HBox(40, henshinEndLabel, henshinEndTextField);
        henshinEndHBox.setAlignment(Pos.CENTER_LEFT);
        //henshinEnd

        VBox auraIdVBox = new VBox(60, 
            i04HBox, boostStartHBox,
            boostLoopHBox, boostEndHBox,
            kiaiChargeHBox, kiryokuMaxHBox,
            henshinStartHBox, henshinEndHBox
        );
        auraIdVBox.setPadding(new Insets(30, 0, 0, 0));

        return auraIdVBox;
    }

    private ScrollPane createScrollPane() {
        return new ScrollPane(vBox);
    }

    private VBox createVBoxRight() {
        return new VBox(createToolBarRight(), createScrollPane());
    }

    private ToolBar createToolBarRight() {
        Button insertEntry = new Button("Insert ID");
        insertEntry.setOnAction(event -> {
            AurCharaEntry newCharaEntry = new AurCharaEntry();
            charaEntries.add(newCharaEntry);
            createVBoxRightCharaId(newCharaEntry);
            characterEntries += 1;
        });

        Button removeEntry = new Button("Remove Chara ID");
        removeEntry.setOnAction(event -> {
            try {
                charaEntries.remove(characterEntries - 1);
                vBox.getChildren().remove(characterEntries - 1);
                characterEntries -= 1;
            } catch (IndexOutOfBoundsException e) {
                Popups.ErrorOutOfBounds();
                e.printStackTrace();
            }
        });

        return new ToolBar(insertEntry, removeEntry);
    }
    
    private VBox createVBoxRightCharaId(AurCharaEntry entry) {
        HBox hBox = new HBox(10);

        //charaId
        Label charaIdLabel = new Label("Chara ID");
        TextField txtCharaId = new TextField(String.valueOf(entry.charaId));
        txtCharaId.textProperty().addListener((obs, oldText, newText) -> {
            if (txtCharaId.getText().contains("-")) {
                return;
            }
            try {
                entry.charaId = Integer.parseInt(newText);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });
        //charaId

        //costume
        Label costumeLabel = new Label("Costume");
        TextField costumeTextField = new TextField(String.valueOf(entry.costume));
        costumeTextField.textProperty().addListener((obs, oldText, newText) -> {
            if (txtCharaId.getText().contains("-")) {
                return;
            }
            try {
                entry.costume = Integer.parseInt(newText);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });
        //costume

        //auraId
        Label auraIdLabel = new Label("Aura ID");
        TextField txtAuraId = new TextField(String.valueOf(entry.auraId));
        txtAuraId.textProperty().addListener((obs, oldText, newText) -> {
            if (txtAuraId.getText().contains("-")) {
                return;
            }
            try {
                entry.auraId = Integer.parseInt(newText);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });
        //auraId

        //glare
        CheckBox glareCheckBox = new CheckBox("Glare");
        glareCheckBox.setSelected(entry.glare);
        glareCheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
            entry.glare = newValue; 
        });
        //glare

        hBox.getChildren().addAll(
            charaIdLabel, txtCharaId,
            costumeLabel, costumeTextField,
            auraIdLabel, txtAuraId ,glareCheckBox
        );
        hBox.setAlignment(Pos.CENTER_LEFT);
        vBox.getChildren().add(hBox);

        return vBox;
    }

    private void entriesActionListener() {
        listView.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if(newValue == null) return;

            hBox.getChildren().remove(1);
            hBox.getChildren().add(1, createAuraIdVBox(auraEntries.get(listView.getSelectionModel().getSelectedIndex())));
        });
        listView.setOnMouseClicked(e->{
            if(e.getButton()==MouseButton.SECONDARY) {
                ContextMenu contextMenu=new ContextMenu();
                MenuItem copy=new MenuItem("Copy Ctrl+C");
                MenuItem paste=new MenuItem("Paste Ctrl+V");
                MenuItem delete=new MenuItem("Delete Delete");
                MenuItem append=new MenuItem("Append Ctrl+A");
                MenuItem insert=new MenuItem("Insert Ctrl+I");
                contextMenu.getItems().addAll(copy,paste,delete,append,insert);
                listView.setContextMenu(contextMenu);
                contextMenu.setOnAction(event->{
                    if(event.getTarget() == copy) Copy();
                    if(event.getTarget() == paste) Paste();
                    if(event.getTarget() == delete) Delete();
                    if(event.getTarget() == append) Append();
                    if(event.getTarget() == insert) Insert();
                });
            }
        });
    }

    private void vBoxListener() {
        vBox.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {});
    }

    private void entriesKeysListener() {
        listView.setOnKeyPressed(e->{
            if(e.isControlDown() && e.getCode() == KeyCode.C) Copy();
            if(e.isControlDown() && e.getCode() == KeyCode.V) Paste();
            if(e.getCode() == KeyCode.DELETE) Delete();
            if(e.isControlDown() && e.getCode() == KeyCode.A) Append();
            if(e.isControlDown() && e.getCode() == KeyCode.I) Insert();
        });
    }

    private void Copy() {
        copyContainer = new AurAuraEntry(auraEntries.get(listView.getSelectionModel().getSelectedIndex()));
    }

    private void Paste() {
        if(copyContainer == null) return;

        auraEntries.set(listView.getSelectionModel().getSelectedIndex(), new AurAuraEntry(copyContainer));
        hBox.getChildren().remove(1);
        hBox.getChildren().add(1, createAuraIdVBox(auraEntries.get(listView.getSelectionModel().getSelectedIndex())));
    }

    private void Delete() {
        if(listView.getSelectionModel().getSelectedIndex() == 0) return;
        
        auraEntries.remove(listView.getSelectionModel().getSelectedIndex());
        allEntries.remove(listView.getSelectionModel().getSelectedIndex());
        listView.getItems().remove(listView.getSelectionModel().getSelectedIndex());

        for(int i = 0; i < listView.getItems().size(); i++) {
            allEntries.set(i, new String("Aura ID " + i));
            listView.getItems().set(i,allEntries.get(i));
        }
    }

    private void Append() {
        auraEntries.add(listView.getSelectionModel().getSelectedIndex()+1,new AurAuraEntry());
        allEntries.add(new String("Aura ID "+ listView.getItems().size()));
        listView.getItems().add(allEntries.getLast());
    }

    private void Insert() {
        if(listView.getSelectionModel().getSelectedIndex() > 0) {
            auraEntries.add(listView.getSelectionModel().getSelectedIndex() - 1, new AurAuraEntry());
            allEntries.add(new String("Aura ID " + listView.getItems().size()));
            listView.getItems().add(allEntries.getLast());
        }
        else if(listView.getSelectionModel().getSelectedIndex() == 0) {
            auraEntries.add(listView.getSelectionModel().getSelectedIndex(), new AurAuraEntry());
            allEntries.add(new String("Aura ID " + listView.getItems().size()));
            listView.getItems().add(allEntries.getLast());
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

            allEntries = new ArrayList<>(auraEntriesCount);

            for(int i = 0; i < auraEntriesCount; i++) {
                allEntries.add(new String("Aura ID " + i));
                listView.getItems().add(allEntries.get(i));
            }

            channel.position(24);
            intBuffer.clear();
            channel.read(intBuffer);
            intBuffer.flip();
            characterEntries = intBuffer.getInt();

            channel.position(28);
            intBuffer.clear();
            channel.read(intBuffer);
            intBuffer.flip();
            charaOffset = intBuffer.getInt();

            for(int i = 0; i < auraEntriesCount; i++) {
                AurAuraEntry auraEntry = new AurAuraEntry();
                auraEntries.add(auraEntry);

                channel.position(auraoffset + i * 16 + 4);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                auraEntry.i04 = intBuffer.getInt();

                channel.position(auraoffset + i * 16 + 12);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                effectsOffset = intBuffer.getInt();

                channel.position(effectsOffset + 4);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                auraEntry.boostStart = intBuffer.getInt();

                channel.position(effectsOffset + 12);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                auraEntry.boostLoop = intBuffer.getInt();

                channel.position(effectsOffset + 20);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                auraEntry.boostEnd = intBuffer.getInt();

                channel.position(effectsOffset + 28);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                auraEntry.kiaiCharge = intBuffer.getInt();

                channel.position(effectsOffset + 36);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                auraEntry.kiryokuMax = intBuffer.getInt();

                channel.position(effectsOffset + 44);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                auraEntry.henshinStart = intBuffer.getInt();

                channel.position(effectsOffset + 52);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                auraEntry.henshinEnd = intBuffer.getInt();
            }

            for(int i = 0; i < characterEntries; i++) {
                AurCharaEntry charaEntry = new AurCharaEntry();
                charaEntries.add(charaEntry);

                channel.position(charaOffset + i * 16);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                charaEntry.charaId = intBuffer.getInt();
    
                channel.position(charaOffset + i * 16 + 4);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                charaEntry.costume = intBuffer.getInt();

                channel.position(charaOffset + i * 16 + 8);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                charaEntry.auraId = intBuffer.getInt();
                
                channel.position(charaOffset + i * 16 + 12);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                charaEntry.glare = (intBuffer.getInt() ==  1);

                createVBoxRightCharaId(charaEntries.get(i));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void aurWriter(Path path) {
        try(FileChannel channel = FileChannel.open(path, StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            int effectCount = 7;
            int auraOffset = 32;
            int auraTypeOffset = 32 + allEntries.size() * 72;
            int charaOffset = 32 + allEntries.size() * 72 + 120;
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
            intBuffer.putInt(allEntries.size());
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
            intBuffer.putInt(characterEntries);
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

            for(int i = 0; i < allEntries.size(); i++) {
                AurAuraEntry auraEntry = auraEntries.get(i);
   
                channel.position(auraOffset + i * 16);
                intBuffer.clear();
                intBuffer.putInt(allEntries.indexOf(allEntries.get(i)));
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
                intBuffer.putInt(allEntries.size() * 16 + 32 + i * 56);
                intBuffer.flip();
                channel.write(intBuffer);

                channel.position(allEntries.size() * 16 + 32 + i * 56 + 4);
                intBuffer.clear();
                intBuffer.putInt(auraEntry.boostStart);
                intBuffer.flip();
                channel.write(intBuffer);

                channel.position(allEntries.size() * 16 + 32 + i * 56 + 8);
                channel.write(ByteBuffer.wrap(new byte[]{0x01, 0x00, 0x00, 0x00}));

                channel.position(allEntries.size() * 16 + 32 + i * 56 + 12);
                intBuffer.clear();
                intBuffer.putInt(auraEntry.boostLoop);
                intBuffer.flip();
                channel.write(intBuffer);

                channel.position(allEntries.size() * 16 + 32 + i * 56 + 16);
                channel.write(ByteBuffer.wrap(new byte[]{0x02, 0x00, 0x00, 0x00}));
                
                channel.position(allEntries.size() * 16 + 32 + i * 56 + 20);
                intBuffer.clear();
                intBuffer.putInt(auraEntry.boostEnd);
                intBuffer.flip();
                channel.write(intBuffer);

                channel.position(allEntries.size() * 16 + 32 + i * 56 + 24);
                channel.write(ByteBuffer.wrap(new byte[]{0x03, 0x00, 0x00, 0x00}));

                channel.position(allEntries.size() * 16 + 32 + i * 56 + 28);
                intBuffer.clear();
                intBuffer.putInt(auraEntry.kiaiCharge);
                intBuffer.flip();
                channel.write(intBuffer);

                channel.position(allEntries.size() * 16 + 32 + i * 56 + 32);
                channel.write(ByteBuffer.wrap(new byte[]{0x04, 0x00, 0x00, 0x00}));
                
                channel.position(allEntries.size() * 16 + 32 + i * 56 + 36);
                intBuffer.clear();
                intBuffer.putInt(auraEntry.kiryokuMax);
                intBuffer.flip();
                channel.write(intBuffer);

                channel.position(allEntries.size() * 16 + 32 + i * 56 + 40);
                channel.write(ByteBuffer.wrap(new byte[]{0x05 ,0x00,0x00,0x00}));

                channel.position(allEntries.size() * 16 + 32 + i * 56 + 44);
                intBuffer.clear();
                intBuffer.putInt(auraEntry.henshinStart);
                intBuffer.flip();
                channel.write(intBuffer);

                channel.position(allEntries.size() * 16 + 32 + i * 56 + 48);
                channel.write(ByteBuffer.wrap(new byte[]{0x06, 0x00, 0x00, 0x00}));

                channel.position(allEntries.size() * 16 + 32 + i * 56 + 52);
                intBuffer.clear();
                intBuffer.putInt(auraEntry.henshinEnd);
                intBuffer.flip();
                channel.write(intBuffer);
            }

            for(int i = 0; i < characterEntries; i++) {
                AurCharaEntry charaEntry = charaEntries.get(i);
            
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
}
class AurAuraEntry {
    public int i04 = 0;
    public int boostStart = 0;
    public int boostLoop = 0;
    public int boostEnd = 0;
    public int kiaiCharge = 0;
    public int kiryokuMax = 0;
    public int henshinStart = 0;
    public int henshinEnd = 0;

    public AurAuraEntry() {}

    public AurAuraEntry(AurAuraEntry other) {
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

class AurCharaEntry {
    public int charaId = 0;
    public int costume = 0;
    public int auraId = 0;
    public boolean glare = false;
}