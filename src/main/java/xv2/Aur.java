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
    ArrayList<String> allEntries;
    
    ArrayList<AurAuraEntry> auraEntries = new ArrayList<>();
    ArrayList<AurCharaEntry> charaEntries = new ArrayList<>();

    ListView<String> listView=new ListView<>();
    HBox hBox=new HBox(10);

    private AurAuraEntry copyContainer = null;
    
    int characterEntries;
    
    public Aur(){
        vBox.setPadding(new Insets(5,5,5,5));
        entriesActionListener();
        entriesKeysListener();
        vBoxListener();
    }
    public SplitPane createSplitPane(){
       SplitPane splitPane =new SplitPane(createHBoxLeft(),createVBoxRight());
       splitPane.setDividerPositions(0.43);
       return splitPane;
    }

    private HBox createHBoxLeft(){
        VBox vBox=new VBox();
        this.hBox.getChildren().addAll(listView,vBox);
        return hBox;
    }

    private VBox createVBoxAuraId(AurAuraEntry entry){
        VBox auraIdVBox=new VBox(60);
        auraIdVBox.setPadding(new Insets(30,0,0,0));

        HBox i04HBox=new HBox(40);
        Label lblI04=new Label("I_04: ");
        lblI04.setPrefWidth(80);
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

        HBox boostStartHBox=new HBox(40);
        Label lblBoostStart=new Label("BoostStart: ");
        lblBoostStart.setPrefWidth(80);
        TextField txtBoostStart=new TextField(String.valueOf(entry.boostStart));
        txtBoostStart.textProperty().addListener((obs,oldText,newText)->{
            if (txtBoostStart.getText().contains("-")) {
                return;
            }
            try {
                entry.boostStart = Integer.parseInt(newText); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });
        boostStartHBox.setAlignment(Pos.CENTER_LEFT);
        boostStartHBox.getChildren().addAll(lblBoostStart,txtBoostStart);

        HBox boostLoopHBox=new HBox(40);
        Label lblBoostLoop=new Label("BoostLoop: ");
        lblBoostLoop.setPrefWidth(80);
        TextField txtBoostLoop=new TextField(String.valueOf(entry.boostLoop));
        txtBoostLoop.textProperty().addListener((obs,oldText,newText)->{
            if (txtBoostLoop.getText().contains("-")) {
                return;
            }
            try {
                entry.boostLoop = Integer.parseInt(newText); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });
        boostLoopHBox.setAlignment(Pos.CENTER_LEFT);
        boostLoopHBox.getChildren().addAll(lblBoostLoop,txtBoostLoop);

        HBox boostEndHBox=new HBox(40);
        Label lblBoostEnd=new Label("BoostEnd: ");
        lblBoostEnd.setPrefWidth(80);
        TextField txtBoostEnd=new TextField(String.valueOf(entry.boostEnd));
        txtBoostEnd.textProperty().addListener((obs,oldText,newText)->{
            if (txtBoostEnd.getText().contains("-")) {
                return;
            }
            try {
                entry.boostEnd = Integer.parseInt(newText); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });
        boostEndHBox.setAlignment(Pos.CENTER_LEFT);
        boostEndHBox.getChildren().addAll(lblBoostEnd,txtBoostEnd);

        HBox kiaiChargeHBox=new HBox(40);
        Label lblKiaiCharge=new Label("KiaiCharge: ");
        lblKiaiCharge.setPrefWidth(80);
        TextField txtKiaiCharge=new TextField(String.valueOf(entry.kiaiCharge));
        txtKiaiCharge.textProperty().addListener((obs,oldText,newText)->{
            if (txtKiaiCharge.getText().contains("-")) {
                return;
            }
            try {
                entry.kiaiCharge = Integer.parseInt(newText); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });
        kiaiChargeHBox.setAlignment(Pos.CENTER_LEFT);
        kiaiChargeHBox.getChildren().addAll(lblKiaiCharge,txtKiaiCharge);

        HBox kiryokuMaxHBox=new HBox(40);
        Label lblKiryokuMax=new Label("KiryokuMax: ");
        lblKiryokuMax.setPrefWidth(80);
        TextField txtKiryokuMax=new TextField(String.valueOf(entry.kiaiCharge));
        txtKiryokuMax.textProperty().addListener((obs,oldText,newText)->{
            if (txtKiryokuMax.getText().contains("-")) {
                return;
            }
            try {
                entry.kiaiCharge = Integer.parseInt(newText); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });
        kiryokuMaxHBox.setAlignment(Pos.CENTER_LEFT);
        kiryokuMaxHBox.getChildren().addAll(lblKiryokuMax,txtKiryokuMax);

        HBox henshinStartHBox=new HBox(40);
        Label lblHenshinStart=new Label("HenshinStart: ");
        lblHenshinStart.setPrefWidth(80);
        TextField txtHenshinStart=new TextField(String.valueOf(entry.henshinStart));
        txtHenshinStart.textProperty().addListener((obs,oldText,newText)->{
            if (txtHenshinStart.getText().contains("-")) {
                return;
            }
            try {
                entry.henshinStart = Integer.parseInt(newText); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });
        henshinStartHBox.setAlignment(Pos.CENTER_LEFT);
        henshinStartHBox.getChildren().addAll(lblHenshinStart,txtHenshinStart);

        HBox henshinEndHBox=new HBox(40);
        Label lblHenshinEnd=new Label("HenshinEnd: ");
        lblHenshinEnd.setPrefWidth(80);
        TextField txtHenshinEnd=new TextField(String.valueOf(entry.henshinEnd));
        txtHenshinEnd.textProperty().addListener((obs,oldText,newText)->{
            if (txtHenshinEnd.getText().contains("-")) {
                return;
            }
            try {
                entry.henshinEnd = Integer.parseInt(newText); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });
        henshinEndHBox.setAlignment(Pos.CENTER_LEFT);
        henshinEndHBox.getChildren().addAll(lblHenshinEnd,txtHenshinEnd);

        auraIdVBox.getChildren().addAll(i04HBox,boostStartHBox,boostLoopHBox,boostEndHBox,kiaiChargeHBox,kiryokuMaxHBox,henshinStartHBox,henshinEndHBox);
        this.hBox.getChildren().add(auraIdVBox);
        
        return auraIdVBox;

    }
    private ScrollPane createScrollPane(){
        ScrollPane scrollPane=new ScrollPane();
        scrollPane.setContent(vBox);
        return scrollPane;
        
    }
    private VBox createVBoxRight(){
        VBox vBox=new VBox();
        vBox.getChildren().addAll(createToolBarRight(),createScrollPane());
        return vBox;
    }

    private ToolBar createToolBarRight(){
        Button insertEntry=new Button("Insert Chara Id");
        insertEntry.setOnAction(event->{
            AurCharaEntry newCharaEntry = new AurCharaEntry();
            charaEntries.add(newCharaEntry);
            createVBoxRightCharaId(newCharaEntry);
            characterEntries+=1;
        });

        Button removeEntry=new Button("Remove Chara Id");
        removeEntry.setOnAction(event->{
            try {
                charaEntries.remove(characterEntries-1);
                vBox.getChildren().remove(characterEntries-1);
                characterEntries-=1;
            } catch (IndexOutOfBoundsException e) {
                Popups.ErrorOutOfBounds();
                e.printStackTrace();
            }

        });
        ToolBar toolBar=new ToolBar(
            insertEntry,
            removeEntry
        );
        return toolBar;
    }
    
    private VBox createVBoxRightCharaId(AurCharaEntry entry){
        HBox hBox=new HBox(10);

        Label lblCharaId=new Label("Chara Id");
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

        Label lblCostume=new Label("Costume");
        TextField txtCostume=new TextField(String.valueOf(entry.costume));
        txtCostume.textProperty().addListener((obs,oldText,newText)->{
            if (txtCharaId.getText().contains("-")) {
                return;
            }
            try {
                entry.costume = Integer.parseInt(newText);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        Label lblAuraId=new Label("Aura Id");
        TextField txtAuraId=new TextField(String.valueOf(entry.auraId));
        txtAuraId.textProperty().addListener((obs,oldText,newText)->{
            if (txtAuraId.getText().contains("-")) {
                return;
            }
            try {
                entry.auraId = Integer.parseInt(newText);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        Label lblGlare=new Label("Glare");
        CheckBox glareCheckBox=new CheckBox();
        glareCheckBox.setSelected(entry.glare);
        glareCheckBox.selectedProperty().addListener((obs,oldValue,newValue)->{
            try {
                entry.glare = newValue;
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

        });

        hBox.getChildren().addAll(lblCharaId,txtCharaId,lblCostume,txtCostume,lblAuraId,txtAuraId,lblGlare,glareCheckBox);
        hBox.setAlignment(Pos.CENTER_LEFT);
        vBox.getChildren().add(hBox);

        return vBox;
    }

    private void entriesActionListener(){
        listView.getSelectionModel().selectedItemProperty().addListener((obs,oldValue,newValue)->{
            if(newValue==null){
                return;
            }
            //System.out.println("entry clicked: "+listView.getSelectionModel().getSelectedIndex());
            hBox.getChildren().remove(1);
            hBox.getChildren().set(1, createVBoxAuraId(auraEntries.get(listView.getSelectionModel().getSelectedIndex())));
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
                    if(event.getTarget() == copy) Copy();
                    if(event.getTarget() == paste) Paste();
                    if(event.getTarget() == delete) Delete();
                    if(event.getTarget() == append) Append();
                    if(event.getTarget() == insert) Insert();
                });
            }
        });
    }

    private void vBoxListener(){
        vBox.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
        });
    }

    private void entriesKeysListener(){
        listView.setOnKeyPressed(e->{
            if(e.isControlDown() && e.getCode() == KeyCode.C) Copy();
            if(e.isControlDown() && e.getCode() == KeyCode.V) Paste();
            if(e.getCode() == KeyCode.DELETE) Delete();
            if(e.isControlDown() && e.getCode() == KeyCode.A) Append();
            if(e.isControlDown() && e.getCode() == KeyCode.I) Insert();
        });
    }

    private void Copy(){
        copyContainer = new AurAuraEntry(auraEntries.get(listView.getSelectionModel().getSelectedIndex()));
    }

    private void Paste(){
        auraEntries.set(listView.getSelectionModel().getSelectedIndex(), new AurAuraEntry(copyContainer));
        hBox.getChildren().remove(1);
        hBox.getChildren().set(1, createVBoxAuraId(auraEntries.get(listView.getSelectionModel().getSelectedIndex())));
    }

    private void Delete(){
        if(listView.getSelectionModel().getSelectedIndex() == 0) return;
        auraEntries.remove(listView.getSelectionModel().getSelectedIndex());
        allEntries.remove(listView.getSelectionModel().getSelectedIndex());
        listView.getItems().remove(listView.getSelectionModel().getSelectedIndex());

        for(int i=0;i<listView.getItems().size();i++){
            allEntries.set(i,new String("Aura Id: "+i));
            listView.getItems().set(i,allEntries.get(i));
        }
    }

    private void Append(){
        auraEntries.add(listView.getSelectionModel().getSelectedIndex()+1,new AurAuraEntry());
        allEntries.add(new String("Aura Id: "+listView.getItems().size()));
        listView.getItems().add(allEntries.getLast());
    }
    private void Insert(){
        if(listView.getSelectionModel().getSelectedIndex()>0){
            auraEntries.add(listView.getSelectionModel().getSelectedIndex()-1,new AurAuraEntry());
            allEntries.add(new String("Aura Id: "+listView.getItems().size()));
            listView.getItems().add(allEntries.getLast());
        }
        else if(listView.getSelectionModel().getSelectedIndex()==0){
            auraEntries.add(listView.getSelectionModel().getSelectedIndex(),new AurAuraEntry());
            allEntries.add(new String("Aura Id: "+listView.getItems().size()));
            listView.getItems().add(allEntries.getLast());
        }
    }

    public void aurReader(Path path){
        try(FileChannel channel=FileChannel.open(path, StandardOpenOption.READ)) {
            int auraoffset=32;
            int charaOffset=0;
            int effectsOffset=0;
            int auraEntriesCount;
            
            ByteBuffer intBuffer=ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
            
            //reading aura entries
            channel.position(8);
            intBuffer.clear();
            channel.read(intBuffer);
            intBuffer.flip();
            auraEntriesCount=intBuffer.getInt();

            allEntries=new ArrayList<>(auraEntriesCount);
            for(int i=0;i<auraEntriesCount;i++){
                allEntries.add(new String("Aura Id: "+i));
                listView.getItems().add(allEntries.get(i));
            }

            //reading character entries
            channel.position(24);
            intBuffer.clear();
            channel.read(intBuffer);
            intBuffer.flip();
            characterEntries=intBuffer.getInt();

            //reading character entries offstet
            channel.position(28);
            intBuffer.clear();
            channel.read(intBuffer);
            intBuffer.flip();
            charaOffset=intBuffer.getInt();

            for(int i=0;i<auraEntriesCount;i++){
                AurAuraEntry auraEntry = new AurAuraEntry();
                //reading i04
                channel.position(auraoffset+16*i+4);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                auraEntry.i04 = intBuffer.getInt();

                //reading effectOffset
                channel.position(auraoffset+16*i+12);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                effectsOffset=intBuffer.getInt();

                //reading boostStart
                channel.position(effectsOffset+4);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                auraEntry.boostStart = intBuffer.getInt();

                //reading boostLoop
                channel.position(effectsOffset+12);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                auraEntry.boostLoop = intBuffer.getInt();

                //reading boostEnd
                channel.position(effectsOffset+20);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                auraEntry.boostEnd = intBuffer.getInt();

                //reading kiaiCharge
                channel.position(effectsOffset+28);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                auraEntry.kiaiCharge = intBuffer.getInt();

                //reading kiryokuMax
                channel.position(effectsOffset+36);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                auraEntry.kiryokuMax = intBuffer.getInt();

                //reading HenshinStart
                channel.position(effectsOffset+44);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                auraEntry.henshinStart = intBuffer.getInt();

                //reading HenshinEnd
                channel.position(effectsOffset+52);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                auraEntry.henshinEnd = intBuffer.getInt();
                auraEntries.add(auraEntry);
                //System.out.println("looking at: "+(effectsOffset+52));
            }
            for(int i=0;i<characterEntries;i++){
                AurCharaEntry charaEntry = new AurCharaEntry();
                channel.position(charaOffset+16*i);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                charaEntry.charaId = intBuffer.getInt();
    
                channel.position(charaOffset+16*i+4);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                charaEntry.costume = intBuffer.getInt();

                channel.position(charaOffset+16*i+8);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                charaEntry.auraId = intBuffer.getInt();
                
                channel.position(charaOffset+16*i+12);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                charaEntry.glare = (intBuffer.getInt() ==  1);

                charaEntries.add(charaEntry);
                createVBoxRightCharaId(charaEntries.get(i));
            }
            
        } catch (IOException e) {
            System.err.println(e);
            
        }
    }

    public void aurWriter(Path path){
        try(FileChannel channel=FileChannel.open(path,StandardOpenOption.WRITE,StandardOpenOption.CREATE,StandardOpenOption.TRUNCATE_EXISTING)) {
            int effectCount=7;
            int auraOffset=32;
            int auraTypeOffset=32+allEntries.size()*16+allEntries.size()*56;
            int charaOffset=32+allEntries.size()*16+allEntries.size()*56+120;
            String auraTypes =  
            "BoostStart\0" + 
            "BoostLoop\0" + 
            "BoostEnd\0" + 
            "KiaiCharge\0" + 
            "KiryokuMax\0" + 
            "HenshinStart\0" + 
            "HenshinEnd\0";

            ByteBuffer intBuffer=ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
            
            //writing magic
            channel.write(ByteBuffer.wrap(new byte[]{0x23,0x41,0x55,0x52}));
          
            //writing endinanes
            channel.position(4);
            channel.write(ByteBuffer.wrap(new byte[]{(byte)0xFE,(byte)0xFF}));
            
            //writing header size 
            channel.position(6);
            channel.write(ByteBuffer.wrap(new byte[]{0x20,0x00}));

            //writing total aura entries
            channel.position(8);
            intBuffer.clear();
            intBuffer.putInt(allEntries.size());
            intBuffer.flip();
            channel.write(intBuffer);

            //writing auraoffset 
            channel.position(12);
            intBuffer.clear();
            intBuffer.putInt(auraOffset);
            intBuffer.flip();
            channel.write(intBuffer);
            
            //writing effect count
            channel.position(16);
            intBuffer.clear();
            intBuffer.putInt(effectCount);
            intBuffer.flip();
            channel.write(intBuffer);
            
            //writing aura type offset
            channel.position(20);
            intBuffer.clear();
            intBuffer.putInt(auraTypeOffset);
            intBuffer.flip();
            channel.write(intBuffer);

            //writing character entries
            channel.position(24);
            intBuffer.clear();
            intBuffer.putInt(characterEntries);
            intBuffer.flip();
            channel.write(intBuffer);

            //writing character id offset
            channel.position(28);
            intBuffer.clear();
            intBuffer.putInt(charaOffset);
            intBuffer.flip();
            channel.write(intBuffer);

            //writing boostStart textString offset
            channel.position(auraTypeOffset);
            intBuffer.clear();
            intBuffer.putInt(auraTypeOffset+28);
            intBuffer.flip();
            channel.write(intBuffer);

            //writing boostLoop textString offset
            channel.position(auraTypeOffset+4);
            intBuffer.clear();
            intBuffer.putInt(auraTypeOffset+39);
            intBuffer.flip();
            channel.write(intBuffer);

            //writing boostEnd textString offset
            channel.position(auraTypeOffset+8);
            intBuffer.clear();
            intBuffer.putInt(auraTypeOffset+49);
            intBuffer.flip();
            channel.write(intBuffer);

            //writing kiaiCharge textString offset
            channel.position(auraTypeOffset+12);
            intBuffer.clear();
            intBuffer.putInt(auraTypeOffset+58);
            intBuffer.flip();
            channel.write(intBuffer);

            //writing kiryokuMax textString offset
            channel.position(auraTypeOffset+16);
            intBuffer.clear();
            intBuffer.putInt(auraTypeOffset+69);
            intBuffer.flip();
            channel.write(intBuffer);

            //writing henshinStart textString offset
            channel.position(auraTypeOffset+20);
            intBuffer.clear();
            intBuffer.putInt(auraTypeOffset+80);
            intBuffer.flip();
            channel.write(intBuffer);

            //writing henshinEnd textString offset
            channel.position(auraTypeOffset+24);
            intBuffer.clear();
            intBuffer.putInt(auraTypeOffset+93);
            intBuffer.flip();
            channel.write(intBuffer);

            channel.position(auraTypeOffset+28);
            channel.write(ByteBuffer.wrap(auraTypes.getBytes(StandardCharsets.ISO_8859_1)));

            for(int i=0;i<allEntries.size();i++){
                AurAuraEntry entry = auraEntries.get(i);
                //writing aura id initial
                channel.position(auraOffset+(16*i));
                intBuffer.clear();
                intBuffer.putInt(allEntries.indexOf(allEntries.get(i)));
                intBuffer.flip();
                channel.write(intBuffer);
                
                //writing i04
                channel.position(auraOffset+(16*i)+4);
                intBuffer.clear();
                intBuffer.putInt(entry.i04);
                intBuffer.flip();
                channel.write(intBuffer);

                //writing effectCount
                channel.position(auraOffset+(16*i)+8);
                intBuffer.clear();
                intBuffer.putInt(effectCount);
                intBuffer.flip();
                channel.write(intBuffer);
                
                //writing effectsOffset
                channel.position(auraOffset+(16*i)+12);
                intBuffer.clear();
                intBuffer.putInt(allEntries.size()*16+32+56*i);
                intBuffer.flip();
                channel.write(intBuffer);

                //writing boostStart
                channel.position(allEntries.size()*16+32+56*i+4);
                intBuffer.clear();
                intBuffer.putInt(entry.boostStart);
                intBuffer.flip();
                channel.write(intBuffer);

                //write 1
                channel.position(allEntries.size()*16+32+56*i+8);
                channel.write(ByteBuffer.wrap(new byte[]{0x01,0x00,0x00,0x00}));

                //write boostLoop
                channel.position(allEntries.size()*16+32+56*i+12);
                intBuffer.clear();
                intBuffer.putInt(entry.boostLoop);
                intBuffer.flip();
                channel.write(intBuffer);

                //write 2
                channel.position(allEntries.size()*16+32+56*i+16);
                channel.write(ByteBuffer.wrap(new byte[]{0x02,0x00,0x00,0x00}));
                
                //write boostEnd
                channel.position(allEntries.size()*16+32+56*i+20);
                intBuffer.clear();
                intBuffer.putInt(entry.boostEnd);
                intBuffer.flip();
                channel.write(intBuffer);

                //write 3
                channel.position(allEntries.size()*16+32+56*i+24);
                channel.write(ByteBuffer.wrap(new byte[]{0x03,0x00,0x00,0x00}));

                //write kiai charge
                channel.position(allEntries.size()*16+32+56*i+28);
                intBuffer.clear();
                intBuffer.putInt(entry.kiaiCharge);
                intBuffer.flip();
                channel.write(intBuffer);

                //write 4
                channel.position(allEntries.size()*16+32+56*i+32);
                channel.write(ByteBuffer.wrap(new byte[]{0x04,0x00,0x00,0x00}));
                
                //write kiryoku max
                channel.position(allEntries.size()*16+32+56*i+36);
                intBuffer.clear();
                intBuffer.putInt(entry.kiryokuMax);
                intBuffer.flip();
                channel.write(intBuffer);

                //write 5
                channel.position(allEntries.size()*16+32+56*i+40);
                channel.write(ByteBuffer.wrap(new byte[]{0x05,0x00,0x00,0x00}));

                //werite henshin start
                channel.position(allEntries.size()*16+32+56*i+44);
                intBuffer.clear();
                intBuffer.putInt(entry.henshinStart);
                intBuffer.flip();
                channel.write(intBuffer);

                //write 6
                channel.position(allEntries.size()*16+32+56*i+48);
                channel.write(ByteBuffer.wrap(new byte[]{0x06,0x00,0x00,0x00}));

                //writing henshin end
                channel.position(allEntries.size()*16+32+56*i+52);
                intBuffer.clear();
                intBuffer.putInt(entry.henshinEnd);
                intBuffer.flip();
                channel.write(intBuffer);
            }
            for(int i=0;i<characterEntries;i++){
                AurCharaEntry chara = charaEntries.get(i);
            
                channel.position(charaOffset+(16*i));
                intBuffer.clear();
                intBuffer.putInt(chara.charaId);
                intBuffer.flip();
                channel.write(intBuffer);
    
                channel.position(charaOffset+(16*i)+4);
                intBuffer.clear();
                intBuffer.putInt(chara.costume);
                intBuffer.flip();
                channel.write(intBuffer);

                channel.position(charaOffset+(16*i)+8);
                intBuffer.clear();
                intBuffer.putInt(chara.auraId);
                intBuffer.flip();
                channel.write(intBuffer);

                channel.position(charaOffset+(16*i)+12);
                intBuffer.clear();
                intBuffer.putInt(chara.glare ? 1 : 0);
                intBuffer.flip();
                channel.write(intBuffer);
            }
        } catch (IOException e) {
            System.err.println(e);
            Popups.ErrorSave(path.toFile().getName());   
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