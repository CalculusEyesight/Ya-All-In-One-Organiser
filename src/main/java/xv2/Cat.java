package xv2;

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
    ArrayList <Integer> getCharaId = new ArrayList<>();
    ArrayList <Integer> getCostume = new ArrayList<>();
    ArrayList <Integer> getI04 = new ArrayList<>();
    ArrayList <Integer> getSkillId2 = new ArrayList<>();
    ArrayList <String> getCharaCode = new ArrayList<>();
    ArrayList <Integer> getI12 = new ArrayList<>();
    ArrayList <Integer> getI16 = new ArrayList<>();
    ArrayList <Byte> getI20 = new ArrayList<>();
    ArrayList <Byte> getTransformationEntry = new ArrayList<>();
    ArrayList <Integer> getI22 = new ArrayList<>();

    ListView <String> listView = new ListView<>();

    HBox outerHBox = new HBox(2);

    int copyCharaId;
    int copyCostume;
    int copyI04;
    int copySkillId2;
    String copyCharaCode;
    int copyI12;
    int copyI16;
    byte copyI20;
    byte copyTransformationEntry;
    int copyI22;
    public Cat(){
        entriesActionListener();
        entriesKeysListener();
    }
    public HBox createHBoxOuter(){
        VBox vBox = new VBox();
        outerHBox.getChildren().addAll(listView,vBox);
        return this.outerHBox;
    }

    private VBox createVBox(int i){
        VBox catVBox = new VBox(30);
        catVBox.setPadding(new Insets(20,0,0,5));

        HBox charaIdHBox=new HBox(40);
        Label lblCharaId=new Label("Chara Id: ");
        lblCharaId.setPrefWidth(130);
        TextField txtCharaId=new TextField(String.valueOf(getCharaId.get(i)));
        txtCharaId.textProperty().addListener((obs,oldText,newText)->{
            if (txtCharaId.getText().contains("-")) {
                return;
            }
            try {
                
                getCharaId.set(i, Integer.parseInt(newText)); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });
        charaIdHBox.setAlignment(Pos.CENTER_LEFT);
        charaIdHBox.getChildren().addAll(lblCharaId,txtCharaId);

        HBox costumeHBox=new HBox(40);
        Label lblCostume=new Label("Costume: ");
        lblCostume.setPrefWidth(130);
        TextField txtCostume=new TextField(String.valueOf(getCostume.get(i)));
        txtCostume.textProperty().addListener((obs,oldText,newText)->{
            if (txtCostume.getText().contains("-")) {
                return;
            }
            try {
                
                getCostume.set(i, Integer.parseInt(newText)); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });
        costumeHBox.setAlignment(Pos.CENTER_LEFT);
        costumeHBox.getChildren().addAll(lblCostume,txtCostume);

        HBox i04HBox=new HBox(40);
        Label lblI04=new Label("I_04: ");
        lblI04.setPrefWidth(130);
        TextField txtI04=new TextField(String.valueOf(getI04.get(i)));
        txtI04.textProperty().addListener((obs,oldText,newText)->{
            if (txtI04.getText().contains("-")) {
                return;
            }
            try {
                getI04.set(i, Integer.parseInt(newText)); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });
        i04HBox.setAlignment(Pos.CENTER_LEFT);
        i04HBox.getChildren().addAll(lblI04,txtI04);

        HBox skillId2HBox=new HBox(40);
        Label lblSkillId2=new Label("Skill Id 2: ");
        lblSkillId2.setPrefWidth(130);
        TextField txtSkillId2=new TextField(String.valueOf(getSkillId2.get(i)));
        txtSkillId2.textProperty().addListener((obs,oldText,newText)->{
            if (txtSkillId2.getText().contains("-")) {
                return;
            }
            try {
                getSkillId2.set(i, Integer.parseInt(newText)); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });
        skillId2HBox.setAlignment(Pos.CENTER_LEFT);
        skillId2HBox.getChildren().addAll(lblSkillId2,txtSkillId2);

        HBox charaCodeHBox=new HBox(40);
        Label lblCharaCode=new Label("Chara Code: ");
        lblCharaCode.setPrefWidth(130);
        TextField txtCharaCode=new TextField(getCharaCode.get(i).toString());
        txtCharaCode.textProperty().addListener((obs,oldText,newText)->{
            if (txtCharaCode.getText().contains("-")) {
                return;
            }
            try {
                getCharaCode.set(i, newText); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });
        charaCodeHBox.setAlignment(Pos.CENTER_LEFT);
        charaCodeHBox.getChildren().addAll(lblCharaCode,txtCharaCode);

        HBox i12HBox=new HBox(40);
        Label lblBI12=new Label("I_12: ");
        lblBI12.setPrefWidth(130);
        TextField txtI12=new TextField(String.valueOf(getI12.get(i)));
        txtI12.textProperty().addListener((obs,oldText,newText)->{
            if (txtI12.getText().contains("-")) {
                return;
            }
            try {
                getI12.set(i, Integer.parseInt(newText)); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });
        i12HBox.setAlignment(Pos.CENTER_LEFT);
        i12HBox.getChildren().addAll(lblBI12,txtI12);

        HBox i16HBox=new HBox(40);
        Label lblI16=new Label("Loading Screen Value?: ");
        lblI16.setPrefWidth(130);
        TextField txtI16=new TextField(String.valueOf(getI16.get(i)));
        txtI16.textProperty().addListener((obs,oldText,newText)->{
            if (txtI16.getText().contains("-")) {
                return;
            }
            try {
                getI16.set(i, Integer.parseInt(newText)); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });
        i16HBox.setAlignment(Pos.CENTER_LEFT);
        i16HBox.getChildren().addAll(lblI16,txtI16);

        HBox i20HBox=new HBox(40);
        Label lblI20=new Label("I_20: ");
        lblI20.setPrefWidth(130);
        TextField txtI20=new TextField(String.valueOf(getI20.get(i)));
        txtI20.textProperty().addListener((obs,oldText,newText)->{
            if (txtI20.getText().contains("-")) {
                return;
            }
            try {
                getI20.set(i, Byte.parseByte(newText)); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });
        i20HBox.setAlignment(Pos.CENTER_LEFT);
        i20HBox.getChildren().addAll(lblI20,txtI20);

        HBox transformationEntryHBox=new HBox(40);
        Label lblTransformationEntry=new Label("Trasnformation Entry: ");
        lblTransformationEntry.setPrefWidth(130);
        TextField txtTransformationEntry=new TextField(String.valueOf(getTransformationEntry.get(i)));
        txtTransformationEntry.textProperty().addListener((obs,oldText,newText)->{
            if (txtTransformationEntry.getText().contains("-")) {
                return;
            }
            try {
                getTransformationEntry.set(i, Byte.parseByte(newText)); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });
        transformationEntryHBox.setAlignment(Pos.CENTER_LEFT);
        transformationEntryHBox.getChildren().addAll(lblTransformationEntry,txtTransformationEntry);

        HBox i22HBox=new HBox(40);
        Label lblI22=new Label("I_22: ");
        lblI22.setPrefWidth(130);
        TextField txtI22=new TextField(String.valueOf(getI22.get(i)));
        txtI22.textProperty().addListener((obs,oldText,newText)->{
            if (txtI22.getText().contains("-")) {
                return;
            }
            try {
                getI22.set(i, Integer.parseInt(newText)); 
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
         listView.getSelectionModel().selectedItemProperty().addListener((obs,oldValue,newValue)->{
            if(newValue==null){
                return;
            }
            //System.out.println("entry clicked: "+listView.getSelectionModel().getSelectedIndex());
            outerHBox.getChildren().remove(1);
            outerHBox.getChildren().set(1, createVBox(listView.getSelectionModel().getSelectedIndex()));
        });
        listView.setOnMouseClicked(e->{
            if(e.getButton()==MouseButton.SECONDARY){
                ContextMenu contextMenu=new ContextMenu();
                MenuItem copy=new MenuItem("Copy Ctrl+C");
                MenuItem paste=new MenuItem("Paste Ctrl+V");
                MenuItem delete=new MenuItem("Delete Delete");
                MenuItem append=new MenuItem("Append Ctrl+A");
                MenuItem insert=new MenuItem("Insert Ctrl+I");
                contextMenu.getItems().addAll(copy,paste,delete,append,insert);
                listView.setContextMenu(contextMenu);
                contextMenu.setOnAction(event->{
                    if(event.getTarget()==copy){
                        Copy();
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
        copyCharaId = getCharaId.get(listView.getSelectionModel().getSelectedIndex());
        copyCostume = getCostume.get(listView.getSelectionModel().getSelectedIndex());
        copyI04 = getI04.get(listView.getSelectionModel().getSelectedIndex());
        copySkillId2 = getSkillId2.get(listView.getSelectionModel().getSelectedIndex());
        copyCharaCode = getCharaCode.get(listView.getSelectionModel().getSelectedIndex());
        copyI12 = getI12.get(listView.getSelectionModel().getSelectedIndex());
        copyI16 = getI16.get(listView.getSelectionModel().getSelectedIndex());
        copyI20 = getI20.get(listView.getSelectionModel().getSelectedIndex());
        copyTransformationEntry = getTransformationEntry.get(listView.getSelectionModel().getSelectedIndex());
        copyI22 = getI22.get(listView.getSelectionModel().getSelectedIndex());
    }

    private void Paste(){
        getCharaId.set(listView.getSelectionModel().getSelectedIndex(), copyCharaId);
        getCostume.set(listView.getSelectionModel().getSelectedIndex(), copyCostume);
        getI04.set(listView.getSelectionModel().getSelectedIndex(), copyI04);
        getSkillId2.set(listView.getSelectionModel().getSelectedIndex(), copySkillId2);
        getCharaCode.set(listView.getSelectionModel().getSelectedIndex(), copyCharaCode);
        getI12.set(listView.getSelectionModel().getSelectedIndex(), copyI12);
        getI16.set(listView.getSelectionModel().getSelectedIndex(), copyI16);
        getI20.set(listView.getSelectionModel().getSelectedIndex(), copyI20);
        getTransformationEntry.set(listView.getSelectionModel().getSelectedIndex(), copyTransformationEntry);
        getI22.set(listView.getSelectionModel().getSelectedIndex(), copyI22);
        outerHBox.getChildren().remove(1);
        outerHBox.getChildren().set(1, createVBox(listView.getSelectionModel().getSelectedIndex()));
    }

    private void Delete(){
        if(listView.getSelectionModel().getSelectedIndex() == 0) return;
        getCharaId.remove(listView.getSelectionModel().getSelectedIndex());
        getCostume.remove(listView.getSelectionModel().getSelectedIndex());
        getI04.remove(listView.getSelectionModel().getSelectedIndex());
        getSkillId2.remove(listView.getSelectionModel().getSelectedIndex());
        getCharaCode.remove(listView.getSelectionModel().getSelectedIndex());
        getI12.remove(listView.getSelectionModel().getSelectedIndex());
        getI16.remove(listView.getSelectionModel().getSelectedIndex());
        getI20.remove(listView.getSelectionModel().getSelectedIndex());
        getTransformationEntry.remove(listView.getSelectionModel().getSelectedIndex());
        getI22.remove(listView.getSelectionModel().getSelectedIndex());
        allEntries.remove(listView.getSelectionModel().getSelectedIndex());
        listView.getItems().remove(listView.getSelectionModel().getSelectedIndex());
        
        for(int i=0;i<listView.getItems().size();i++){
            allEntries.set(i,new String("Entry: "+i));
            listView.getItems().set(i,allEntries.get(i));
        }
    }

    private void Append(){
        getCharaId.add(listView.getSelectionModel().getSelectedIndex()+1,0);
        getCostume.add(listView.getSelectionModel().getSelectedIndex()+1,0);
        getI04.add(listView.getSelectionModel().getSelectedIndex()+1,0);
        getSkillId2.add(listView.getSelectionModel().getSelectedIndex()+1,0);
        getCharaCode.add(listView.getSelectionModel().getSelectedIndex()+1,"");
        getI12.add(listView.getSelectionModel().getSelectedIndex()+1,0);
        getI16.add(listView.getSelectionModel().getSelectedIndex()+1,0);
        getI20.add(listView.getSelectionModel().getSelectedIndex()+1,(byte)0);
        getTransformationEntry.add(listView.getSelectionModel().getSelectedIndex()+1,(byte)0);
        getI22.add(listView.getSelectionModel().getSelectedIndex()+1,0);
        allEntries.add(new String("Entry: "+listView.getItems().size()));
        listView.getItems().add(allEntries.getLast());
    }

    private void Insert(){
        if(listView.getSelectionModel().getSelectedIndex()>0){
            getCharaId.add(listView.getSelectionModel().getSelectedIndex()-1,0);
            getCostume.add(listView.getSelectionModel().getSelectedIndex()-1,0);
            getI04.add(listView.getSelectionModel().getSelectedIndex()-1,0);
            getSkillId2.add(listView.getSelectionModel().getSelectedIndex()-1,0);
            getCharaCode.add(listView.getSelectionModel().getSelectedIndex()-1,"");
            getI12.add(listView.getSelectionModel().getSelectedIndex()-1,0);
            getI16.add(listView.getSelectionModel().getSelectedIndex()-1,0);
            getI20.add(listView.getSelectionModel().getSelectedIndex()-1,(byte)0);
            getTransformationEntry.add(listView.getSelectionModel().getSelectedIndex()-1,(byte)0);
            getI22.add(listView.getSelectionModel().getSelectedIndex()-1,0);
            allEntries.add(new String("Entry: "+listView.getItems().size()));
            listView.getItems().add(allEntries.getLast());
        }
        else if(listView.getSelectionModel().getSelectedIndex()==0){
            getCharaId.add(listView.getSelectionModel().getSelectedIndex(),0);
            getCostume.add(listView.getSelectionModel().getSelectedIndex(),0);
            getI04.add(listView.getSelectionModel().getSelectedIndex(),0);
            getSkillId2.add(listView.getSelectionModel().getSelectedIndex(),0);
            getCharaCode.add(listView.getSelectionModel().getSelectedIndex(),"");
            getI12.add(listView.getSelectionModel().getSelectedIndex(),0);
            getI16.add(listView.getSelectionModel().getSelectedIndex(),0);
            getI20.add(listView.getSelectionModel().getSelectedIndex(),(byte)0);
            getTransformationEntry.add(listView.getSelectionModel().getSelectedIndex(),(byte)0);
            getI22.add(listView.getSelectionModel().getSelectedIndex(),0);
            allEntries.add(new String("Entry: "+listView.getItems().size()));
            listView.getItems().add(allEntries.getLast());
        }
    }

    public void catReader(Path path){
        try(FileChannel channel = FileChannel.open(path,StandardOpenOption.READ)) {
            ByteBuffer byteBuffer=ByteBuffer.allocate(1).order(ByteOrder.LITTLE_ENDIAN);
            ByteBuffer shortBuffer=ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN);
            ByteBuffer intBuffer=ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
            
            short catEntries;
            int entryOffset=12;

            //reading entries
            channel.position(6);
            shortBuffer.clear();
            channel.read(shortBuffer);
            shortBuffer.flip();
            catEntries=shortBuffer.getShort();

            allEntries=new ArrayList<>(catEntries);
            for(int i=0;i<catEntries;i++){
                allEntries.add(new String("Entry "+i));
                listView.getItems().add(allEntries.get(i));
            }

            for(int i=0;i<catEntries;i++){
                //reading charaid
                channel.position(entryOffset+i*24);
                shortBuffer.clear();
                channel.read(shortBuffer);
                shortBuffer.flip();
                getCharaId.add(toUShort(shortBuffer.getShort()));
                
                //reading costume
                channel.position(entryOffset+i*24+2);
                shortBuffer.clear();
                channel.read(shortBuffer);
                shortBuffer.flip();
                getCostume.add(toUShort(shortBuffer.getShort()));

                //reding I_04
                channel.position(entryOffset+i*24+4);
                shortBuffer.clear();
                channel.read(shortBuffer);
                shortBuffer.flip();
                getI04.add(toUShort(shortBuffer.getShort()));

                //reading skillid2
                channel.position(entryOffset+i*24+6);
                shortBuffer.clear();
                channel.read(shortBuffer);
                shortBuffer.flip();
                getSkillId2.add(toUShort(shortBuffer.getShort()));

                //reading characode
                channel.position(entryOffset+i*24+8);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                getCharaCode.add(StandardCharsets.ISO_8859_1.decode(intBuffer).toString().trim());

                //reading I_12
                channel.position(entryOffset+i*24+12);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                getI12.add(intBuffer.getInt());
                
                //reading I_16
                channel.position(entryOffset+i*24+16);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                getI16.add(intBuffer.getInt());

                //reading I_20
                channel.position(entryOffset+i*24+20);
                byteBuffer.clear();
                channel.read(byteBuffer);
                byteBuffer.flip();
                getI20.add(byteBuffer.get());

                //reading Transformation Entry
                channel.position(entryOffset+i*24+21);
                byteBuffer.clear();
                channel.read(byteBuffer);
                byteBuffer.flip();
                getTransformationEntry.add(byteBuffer.get());

                //reading I_22
                channel.position(entryOffset+i*24+22);
                shortBuffer.clear();
                channel.read(shortBuffer);
                shortBuffer.flip();
                getI22.add(toUShort(shortBuffer.getShort()));

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
                    //writing charaid
                    channel.position(entriesOffset+i*24);
                    shortBuffer.clear();
                    shortBuffer.putShort(getCharaId.get(i).shortValue());
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    //writing costume
                    channel.position(entriesOffset+i*24+2);
                    shortBuffer.clear();
                    shortBuffer.putShort(getCostume.get(i).shortValue());
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    //writing I_04
                    channel.position(entriesOffset+i*24+4);
                    shortBuffer.clear();
                    shortBuffer.putShort(getI04.get(i).shortValue());
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    //writing skillid2
                    channel.position(entriesOffset+i*24+6);
                    shortBuffer.clear();
                    shortBuffer.putShort(getSkillId2.get(i).shortValue());
                    shortBuffer.flip();
                    channel.write(shortBuffer);
                    
                    //writing characode
                    channel.position(entriesOffset+i*24+8);
                    intBuffer.clear();
                    intBuffer.put((getCharaCode.get(i).getBytes(StandardCharsets.ISO_8859_1)));
                    intBuffer.flip();
                    channel.write(intBuffer);
    
                    //witing I_12
                    channel.position(entriesOffset+i*24+12);
                    intBuffer.clear();
                    intBuffer.putInt(getI12.get(i));
                    intBuffer.flip();
                    channel.write(intBuffer);
                
                    //writing I_16
                    channel.position(entriesOffset+i*24+16);
                    intBuffer.clear();
                    intBuffer.putInt(getI16.get(i));
                    intBuffer.flip();
                    channel.write(intBuffer);
            
                    //writing I_20
                    channel.position(entriesOffset+i*24+20);
                    byteBuffer.clear();
                    byteBuffer.put(getI20.get(i));
                    byteBuffer.flip();
                    channel.write(byteBuffer);

                    //writing TransformationEntry
                    channel.position(entriesOffset+i*24+21);
                    byteBuffer.clear();
                    byteBuffer.put(getTransformationEntry.get(i));
                    byteBuffer.flip();
                    channel.write(byteBuffer);

                    //writing I_22
                    channel.position(entriesOffset+i*24+22);
                    shortBuffer.clear();
                    shortBuffer.putShort(getI22.get(i).shortValue());
                    shortBuffer.flip();
                    channel.write(shortBuffer);
                }
                
            }catch(IOException e){
                e.printStackTrace();
            } 
    }
}
