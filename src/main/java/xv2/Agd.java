package xv2;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
class Agd {

    VBox vBox= new VBox(5);

    ArrayList <AgdEntry> agdEntries = new ArrayList<>();

    private int entries=0;

    public Agd(){
        this.vBox.setPadding(new Insets(0,0,0,60));
        vBoxListener();
    }
    public VBox createVbox(){
        VBox vBox=new VBox();
        vBox.getChildren().addAll(createToolBar(),createScrollPane());
        return vBox;
    }

    private ScrollPane createScrollPane(){
        ScrollPane scrollPane=new ScrollPane();
        scrollPane.setContent(vBox);
        return scrollPane;
    }

    private ToolBar createToolBar(){
        Button insertEntry=new Button("Insert Entry");
        insertEntry.setOnAction(event->{
            AgdEntry newEntry = new AgdEntry();
            agdEntries.add(newEntry);
            createVBoxAgdData(newEntry);
            entries+=1;
        });
        Button removeEntry=new Button("Remove Entry");
        removeEntry.setOnAction(event->{
            try {
                agdEntries.remove(entries-1);
                this.vBox.getChildren().remove(entries-1);
                entries-=1;
            } catch (IndexOutOfBoundsException e) {
                System.out.println(removeEntry);
                Popups.ErrorOutOfBounds();
            }
        });
        ToolBar toolBar=new ToolBar(
            insertEntry,
            removeEntry
        );
        return toolBar;
    }

    private VBox createVBoxAgdData(AgdEntry entry){
        HBox hBox=new HBox(30);
        Label lblLevel=new Label("Level");
        TextField txtLevel=new TextField(String.valueOf(entry.level));
        txtLevel.textProperty().addListener((obs,oldText,newText)->{
            if (txtLevel.getText().contains("-")) {
                return;
            }
            try {
                entry.level= Integer.parseInt(newText);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });


        Label lblXpToNextLevel=new Label("Xp To Next Level");
        TextField txtXpToNextLevel=new TextField(String.valueOf(entry.xpToNextLevel));
        txtXpToNextLevel.textProperty().addListener((obs,oldText,newText)->{
            if (txtXpToNextLevel.getText().contains("-")) {
                return;
            }
            try {
                entry.xpToNextLevel= Integer.parseInt(newText);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });
        Label lblXpToThisLevel=new Label("Xp To This Level");
        TextField txtXpToThisLevel=new TextField(String.valueOf(entry.xpToThisLevel));
        txtXpToThisLevel.textProperty().addListener((obs,oldText,newText)->{
            if (txtXpToThisLevel.getText().contains("-")) {
                return;
            }
            try {
                entry.xpToThisLevel = Integer.parseInt(newText);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        Label lblAttributePointsGained=new Label("Attribute Points Gained");
        TextField txtAttributePointsGained=new TextField(String.valueOf(entry.attributePointsGained));
        txtAttributePointsGained.textProperty().addListener((obs,oldText,newText)->{
            if (txtAttributePointsGained.getText().contains("-")) {
                return;
            }
            try {
                entry.attributePointsGained= Integer.parseInt(newText);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

       hBox.getChildren().addAll(lblLevel,txtLevel,lblXpToNextLevel,txtXpToNextLevel,lblXpToThisLevel,txtXpToThisLevel,lblAttributePointsGained,txtAttributePointsGained);
       vBox.getChildren().add(hBox);
       
       return vBox;
    }
    private void vBoxListener(){
        vBox.addEventFilter(MouseEvent.ANY, event -> {});
    }

    public void agdReader(Path path){
        try(FileChannel channel=FileChannel.open(path, StandardOpenOption.READ)) {
            int offset=16;
            ByteBuffer intBuffer=ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
            ByteBuffer shortBuffer=ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN);

            channel.position(8);
            shortBuffer.clear();
            channel.read(shortBuffer);
            shortBuffer.flip();
            entries=shortBuffer.getShort();
            
            for(int i=0;i<entries;i++){
                AgdEntry agdEntry = new AgdEntry();
                //reading level
                channel.position(offset*(i+1));
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                agdEntry.level = intBuffer.getInt();
                
                //reading xptonextlevel
                channel.position(offset*(i+1)+4);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                agdEntry.xpToNextLevel = intBuffer.getInt();
                
                //reading xptothislevel
                channel.position(offset*(i+1)+8);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                agdEntry.xpToThisLevel = intBuffer.getInt();

                //reading attributepointsgained
                channel.position(offset*(i+1)+12);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                agdEntry.attributePointsGained = intBuffer.getInt();

                agdEntries.add(agdEntry);

                createVBoxAgdData(agdEntry);
            }
        } catch (IOException e) {
            System.err.println(e);
            Popups.ErrorLoad(path.toFile().getName());
        }
    }

    public void agdWriter(Path path){
        try(FileChannel channel=FileChannel.open(path, StandardOpenOption.WRITE,StandardOpenOption.CREATE,StandardOpenOption.TRUNCATE_EXISTING)) {
            int offset=16;
            ByteBuffer intBuffer=ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
            
            channel.position(0);
            channel.write(ByteBuffer.wrap(new byte[]{0x23,0x41,0x47,0x44}));
          
            channel.position(4);
            channel.write(ByteBuffer.wrap(new byte[]{(byte)0xFE,(byte)0xFF}));

            channel.position(6);
            channel.write(ByteBuffer.wrap(new byte[]{0x10,0x00}));

            channel.position(8);
            intBuffer.clear();
            intBuffer.putInt(entries);
            intBuffer.flip();
            channel.write(intBuffer);

            channel.position(12);
            channel.write(ByteBuffer.wrap(new byte[]{0x10,0x00,0x00,0x00}));

            for(int i=0;i<entries;i++){
                AgdEntry agdEntry = agdEntries.get(i);
                //writing level
                channel.position(offset*(i+1));
                intBuffer.clear();
                intBuffer.putInt(agdEntry.level);
                intBuffer.flip();
                channel.write(intBuffer);

                //writing xptonextlevel
                channel.position(offset*(i+1)+4);
                intBuffer.clear();
                intBuffer.putInt(agdEntry.xpToNextLevel);
                intBuffer.flip();
                channel.write(intBuffer);

                //writing xptothislevel
                channel.position(offset*(i+1)+8);
                intBuffer.clear();
                intBuffer.putInt(agdEntry.xpToThisLevel);
                intBuffer.flip();
                channel.write(intBuffer);
                
                //writing attributepointsgained
                channel.position(offset*(i+1)+12);
                intBuffer.clear();
                intBuffer.putInt(agdEntry.attributePointsGained);
                intBuffer.flip();
                channel.write(intBuffer);
            }
            
        } catch (IOException e) {
            System.err.println(e);
            Popups.ErrorSave(path.toFile().getName());
        }
    }
}

class AgdEntry {
    public int level;
    public int xpToNextLevel;
    public int xpToThisLevel;
    public int attributePointsGained;
}