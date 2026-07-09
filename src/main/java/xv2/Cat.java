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

    private CatEntry copyContainer = null;

    ContextMenu contextMenu=new ContextMenu();
    MenuItem copy=new MenuItem("Copy Ctrl+C");
    MenuItem paste=new MenuItem("Paste Ctrl+V");
    MenuItem delete=new MenuItem("Delete Delete");
    MenuItem append=new MenuItem("Append Ctrl+A");
    MenuItem insert=new MenuItem("Insert Ctrl+I");

    public Cat(){
        entriesActionListener();
        entriesKeysListener();
    }
    public HBox createHBoxOuter(){
        VBox vBox = new VBox();
        outerHBox.getChildren().addAll(listView,vBox);
        return this.outerHBox;
    }

    private VBox createVBox(CatEntry entry){
        VBox catVBox = new VBox(30);
        catVBox.setPadding(new Insets(20,0,0,5));

        HBox charaIdHBox=new HBox(40);
        Label lblCharaId=new Label("Chara Id: ");
        lblCharaId.setPrefWidth(130);
        TextField txtCharaId=new TextField(String.valueOf(entry.charaId));
        txtCharaId.textProperty().addListener((obs,oldText,newText)->{
            if (txtCharaId.getText().contains("-")) {
                return;
            }
            try {
                entry.charaId = Integer.parseInt(newText); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });
        charaIdHBox.setAlignment(Pos.CENTER_LEFT);
        charaIdHBox.getChildren().addAll(lblCharaId,txtCharaId);

        HBox costumeHBox=new HBox(40);
        Label lblCostume=new Label("Costume: ");
        lblCostume.setPrefWidth(130);
        TextField txtCostume=new TextField(String.valueOf(entry.costume));
        txtCostume.textProperty().addListener((obs,oldText,newText)->{
            if (txtCostume.getText().contains("-")) {
                return;
            }
            try {
                entry.costume = Integer.parseInt(newText); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });
        costumeHBox.setAlignment(Pos.CENTER_LEFT);
        costumeHBox.getChildren().addAll(lblCostume,txtCostume);

        HBox i04HBox=new HBox(40);
        Label lblI04=new Label("I_04: ");
        lblI04.setPrefWidth(130);
        TextField txtI04=new TextField(String.valueOf(entry.i04));
        txtI04.textProperty().addListener((obs,oldText,newText)->{
            if (txtI04.getText().contains("-")) {
                return;
            }
            try {
                entry.i04 = Integer.parseInt(newText); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });
        i04HBox.setAlignment(Pos.CENTER_LEFT);
        i04HBox.getChildren().addAll(lblI04,txtI04);

        HBox skillId2HBox=new HBox(40);
        Label lblSkillId2=new Label("Skill Id 2: ");
        lblSkillId2.setPrefWidth(130);
        TextField txtSkillId2=new TextField(String.valueOf(entry.skillId2));
        txtSkillId2.textProperty().addListener((obs,oldText,newText)->{
            if (txtSkillId2.getText().contains("-")) {
                return;
            }
            try {
                entry.skillId2 = Integer.parseInt(newText); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });
        skillId2HBox.setAlignment(Pos.CENTER_LEFT);
        skillId2HBox.getChildren().addAll(lblSkillId2,txtSkillId2);

        HBox charaCodeHBox=new HBox(40);
        Label lblCharaCode=new Label("Chara Code: ");
        lblCharaCode.setPrefWidth(130);
        TextField txtCharaCode=new TextField(entry.charaCode);
        txtCharaCode.textProperty().addListener((obs,oldText,newText)->{
            if (txtCharaCode.getText().contains("-")) {
                return;
            }
            try {
                entry.charaCode = newText; 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });
        charaCodeHBox.setAlignment(Pos.CENTER_LEFT);
        charaCodeHBox.getChildren().addAll(lblCharaCode,txtCharaCode);

        HBox i12HBox=new HBox(40);
        Label lblBI12=new Label("I_12: ");
        lblBI12.setPrefWidth(130);
        TextField txtI12=new TextField(String.valueOf(entry.i12));
        txtI12.textProperty().addListener((obs,oldText,newText)->{
            if (txtI12.getText().contains("-")) {
                return;
            }
            try {
                entry.i12 = Integer.parseInt(newText); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });
        i12HBox.setAlignment(Pos.CENTER_LEFT);
        i12HBox.getChildren().addAll(lblBI12,txtI12);

        HBox i16HBox=new HBox(40);
        Label lblI16=new Label("Loading Screen Value?: ");
        lblI16.setPrefWidth(130);
        TextField txtI16=new TextField(String.valueOf(entry.i16));
        txtI16.textProperty().addListener((obs,oldText,newText)->{
            if (txtI16.getText().contains("-")) {
                return;
            }
            try {
                entry.i16 = Integer.parseInt(newText); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });
        i16HBox.setAlignment(Pos.CENTER_LEFT);
        i16HBox.getChildren().addAll(lblI16,txtI16);

        HBox i20HBox=new HBox(40);
        Label lblI20=new Label("I_20: ");
        lblI20.setPrefWidth(130);
        TextField txtI20=new TextField(String.valueOf(entry.i20));
        txtI20.textProperty().addListener((obs,oldText,newText)->{
            if (txtI20.getText().contains("-")) {
                return;
            }
            try {
                entry.i20 = Integer.parseInt(newText); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });
        i20HBox.setAlignment(Pos.CENTER_LEFT);
        i20HBox.getChildren().addAll(lblI20,txtI20);

        HBox transformationEntryHBox=new HBox(40);
        Label lblTransformationEntry=new Label("Trasnformation Entry: ");
        lblTransformationEntry.setPrefWidth(130);
        TextField txtTransformationEntry=new TextField(String.valueOf(entry.transformationEntry));
        txtTransformationEntry.textProperty().addListener((obs,oldText,newText)->{
            if (txtTransformationEntry.getText().contains("-")) {
                return;
            }
            try {
                entry.transformationEntry = Integer.parseInt(newText); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });
        transformationEntryHBox.setAlignment(Pos.CENTER_LEFT);
        transformationEntryHBox.getChildren().addAll(lblTransformationEntry,txtTransformationEntry);

        HBox i22HBox=new HBox(40);
        Label lblI22=new Label("I_22: ");
        lblI22.setPrefWidth(130);
        TextField txtI22=new TextField(String.valueOf(entry.i22));
        txtI22.textProperty().addListener((obs,oldText,newText)->{
            if (txtI22.getText().contains("-")) {
                return;
            }
            try {
                entry.i22 = Integer.parseInt(newText); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });
        i22HBox.setAlignment(Pos.CENTER_LEFT);
        i22HBox.getChildren().addAll(lblI22,txtI22);
        
        catVBox.getChildren().addAll(charaIdHBox,costumeHBox,i04HBox,skillId2HBox,charaCodeHBox,i12HBox,i16HBox,i20HBox,transformationEntryHBox,i22HBox);
        this.outerHBox.getChildren().add(catVBox);
        return catVBox;

    }

    private void entriesActionListener(){
        paste.setDisable(true);
        contextMenu.getItems().addAll(copy,paste,delete,append,insert);
        listView.setContextMenu(contextMenu);
        listView.getSelectionModel().selectedItemProperty().addListener((obs,oldValue,newValue)->{
            if(newValue==null){
                return;
            }

            outerHBox.getChildren().remove(1);
            outerHBox.getChildren().set(1, createVBox(catEntries.get(listView.getSelectionModel().getSelectedIndex())));
        });
        listView.setOnMouseClicked(e->{
            if(e.getButton()==MouseButton.SECONDARY){
                contextMenu.setOnAction(event->{
                    if(event.getTarget()==copy){
                        Copy();
                        paste.setDisable(false);
                    }
                    if(event.getTarget()==paste){
                        Paste();
                    }
                    if(event.getTarget()==delete){
                       Delete();
                    }
                    if(event.getTarget()==append){
                        Append();
                    }
                    if(event.getTarget()==insert){
                        Insert();
                    }
                });
            }
        });
    }

    private void entriesKeysListener(){
        listView.setOnKeyPressed(e->{
            if(e.isControlDown()&&e.getCode()==KeyCode.C){
                Copy();
            }
            if(e.isControlDown()&&e.getCode()==KeyCode.V){
                Paste();
            }
            if(e.getCode()==KeyCode.DELETE){
                Delete();
            }
            if(e.isControlDown()&&e.getCode()==KeyCode.A){
                Append();
                
            }
            if(e.isControlDown()&&e.getCode()==KeyCode.I){
                Insert();
            }
        });
    }

    private void Copy() {
        copyContainer = new CatEntry(catEntries.get(listView.getSelectionModel().getSelectedIndex()));
    }

    private void Paste(){
        if(copyContainer == null) return;
        
        catEntries.set(listView.getSelectionModel().getSelectedIndex(), new CatEntry(copyContainer));
        outerHBox.getChildren().remove(1);
        outerHBox.getChildren().set(1, createVBox(catEntries.get(listView.getSelectionModel().getSelectedIndex())));
    }

    private void Delete(){
        if(listView.getSelectionModel().getSelectedIndex() == 0) return;

        catEntries.remove(listView.getSelectionModel().getSelectedIndex());
        for(int i=0;i<listView.getItems().size();i++){
            allEntries.set(i,new String("Entry: "+i));
            listView.getItems().set(i,allEntries.get(i));
        }
    }

    private void Append(){
        catEntries.add(listView.getSelectionModel().getSelectedIndex()+1,new CatEntry());
        allEntries.add(new String("Entry "+listView.getItems().size()));
        listView.getItems().add(allEntries.getLast());
    }

    private void Insert(){
        if(listView.getSelectionModel().getSelectedIndex()>0){
            catEntries.add(listView.getSelectionModel().getSelectedIndex()-1,new CatEntry());
            allEntries.add(new String("Entry: "+listView.getItems().size()));
            listView.getItems().add(allEntries.getLast());
        }
        else if(listView.getSelectionModel().getSelectedIndex()==0){
            catEntries.add(listView.getSelectionModel().getSelectedIndex(),new CatEntry());
            allEntries.add(new String("Entry: "+listView.getItems().size()));
            listView.getItems().add(allEntries.getLast());
        }
    }

    public void catReader(Path path){
        try(FileChannel channel = FileChannel.open(path,StandardOpenOption.READ)) {
            ByteBuffer byteBuffer=ByteBuffer.allocate(1).order(ByteOrder.LITTLE_ENDIAN);
            ByteBuffer shortBuffer=ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN);
            ByteBuffer intBuffer=ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
            
            short catEntriesCount;
            int entryOffset=12;

            //reading entries
            channel.position(6);
            shortBuffer.clear();
            channel.read(shortBuffer);
            shortBuffer.flip();
            catEntriesCount=shortBuffer.getShort();

            allEntries=new ArrayList<>(catEntriesCount);
            for(int i=0;i<catEntriesCount;i++){
                allEntries.add(new String("Entry "+i));
                listView.getItems().add(allEntries.get(i));
            }

            for(int i=0;i<catEntriesCount;i++){
                CatEntry catEntry = new CatEntry();
                //reading charaid
                channel.position(entryOffset+i*24);
                shortBuffer.clear();
                channel.read(shortBuffer);
                shortBuffer.flip();
                catEntry.charaId = toUShort(shortBuffer.getShort());
                
                //reading costume
                channel.position(entryOffset+i*24+2);
                shortBuffer.clear();
                channel.read(shortBuffer);
                shortBuffer.flip();
                catEntry.costume = toUShort(shortBuffer.getShort());

                //reding I_04
                channel.position(entryOffset+i*24+4);
                shortBuffer.clear();
                channel.read(shortBuffer);
                shortBuffer.flip();
                catEntry.i04 = toUShort(shortBuffer.getShort());

                //reading skillid2
                channel.position(entryOffset+i*24+6);
                shortBuffer.clear();
                channel.read(shortBuffer);
                shortBuffer.flip();
                catEntry.skillId2 = toUShort(shortBuffer.getShort());

                //reading characode
                channel.position(entryOffset+i*24+8);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                catEntry.charaCode = StandardCharsets.ISO_8859_1.decode(intBuffer).toString().trim();

                //reading I_12
                channel.position(entryOffset+i*24+12);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                catEntry.i12 = intBuffer.getInt();
                
                //reading I_16
                channel.position(entryOffset+i*24+16);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                catEntry.i16 = intBuffer.getInt();

                //reading I_20
                channel.position(entryOffset+i*24+20);
                byteBuffer.clear();
                channel.read(byteBuffer);
                byteBuffer.flip();
                catEntry.i20 = toUByte(byteBuffer.get());
                
                //reading Transformation Entry
                channel.position(entryOffset+i*24+21);
                byteBuffer.clear();
                channel.read(byteBuffer);
                byteBuffer.flip();
                catEntry.transformationEntry = toUByte(byteBuffer.get());

                //reading I_22
                channel.position(entryOffset+i*24+22);
                shortBuffer.clear();
                channel.read(shortBuffer);
                shortBuffer.flip();
                catEntry.i22 = toUShort(shortBuffer.getShort());
                catEntries.add(catEntry);
            }    
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void catWriter(Path path){
        try(FileChannel channel = FileChannel.open(path, StandardOpenOption.WRITE,StandardOpenOption.CREATE,StandardOpenOption.TRUNCATE_EXISTING)) {
            int entriesOffset=12;
            ByteBuffer byteBuffer=ByteBuffer.allocate(1).order(ByteOrder.LITTLE_ENDIAN);
            ByteBuffer shortBuffer=ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN);
            ByteBuffer intBuffer=ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
            
            //write magic
            channel.position(0);
            channel.write(ByteBuffer.wrap(new byte[]{0x23,0x43,0x41,0x54}));
            
            //write endiannes
            channel.position(4);
            channel.write(ByteBuffer.wrap(new byte[]{(byte)0xFE,(byte)0xFF}));
        
            //write entry
            channel.position(6);
            shortBuffer.clear();
            shortBuffer.putShort((short)allEntries.size());
            shortBuffer.flip();
            channel.write(shortBuffer);

            //write entry offset
            channel.position(8);
            intBuffer.clear();
            intBuffer.putInt(entriesOffset);
            intBuffer.flip();
            channel.write(intBuffer);

            for(int i=0;i<allEntries.size();i++){
                CatEntry catEntry = catEntries.get(i);
                //writing charaid
                channel.position(entriesOffset+i*24);
                shortBuffer.clear();
                shortBuffer.putShort((short)catEntry.charaId);
                shortBuffer.flip();
                channel.write(shortBuffer);

                //writing costume
                channel.position(entriesOffset+i*24+2);
                shortBuffer.clear();
                shortBuffer.putShort((short)catEntry.costume);
                shortBuffer.flip();
                channel.write(shortBuffer);

                //writing I_04
                channel.position(entriesOffset+i*24+4);
                shortBuffer.clear();
                shortBuffer.putShort((short)catEntry.i04);
                shortBuffer.flip();
                channel.write(shortBuffer);

                //writing skillid2
                channel.position(entriesOffset+i*24+6);
                shortBuffer.clear();
                shortBuffer.putShort((short)catEntry.skillId2);
                shortBuffer.flip();
                channel.write(shortBuffer);
                
                //writing characode
                channel.position(entriesOffset+i*24+8);
                intBuffer.clear();
                intBuffer.put(catEntry.charaCode.getBytes(StandardCharsets.ISO_8859_1));
                intBuffer.flip();
                channel.write(intBuffer);

                //witing I_12
                channel.position(entriesOffset+i*24+12);
                intBuffer.clear();
                intBuffer.putInt(catEntry.i12);
                intBuffer.flip();
                channel.write(intBuffer);
            
                //writing I_16
                channel.position(entriesOffset+i*24+16);
                intBuffer.clear();
                intBuffer.putInt(catEntry.i16);
                intBuffer.flip();
                channel.write(intBuffer);
        
                //writing I_20
                channel.position(entriesOffset+i*24+20);
                byteBuffer.clear();
                byteBuffer.put((byte)catEntry.i20);
                byteBuffer.flip();
                channel.write(byteBuffer);

                //writing TransformationEntry
                channel.position(entriesOffset+i*24+21);
                byteBuffer.clear();
                byteBuffer.put((byte)catEntry.transformationEntry);
                byteBuffer.flip();
                channel.write(byteBuffer);

                //writing I_22
                channel.position(entriesOffset+i*24+22);
                shortBuffer.clear();
                shortBuffer.putShort((short)catEntry.i22);
                shortBuffer.flip();
                channel.write(shortBuffer);
            }
        }catch(IOException e){
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

    public CatEntry(CatEntry other){
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