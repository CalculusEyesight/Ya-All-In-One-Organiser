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
    VBox vBox = new VBox(5);
    ArrayList<AgdEntry> agdEntries = new ArrayList<>();
    int entries = 0;

    public Agd() {
        this.vBox.setPadding(new Insets(0, 0, 0, 60));
        vBoxListener();
    }

    public VBox createVbox() {
        return new VBox(createToolBar(), createScrollPane());
    }

    private ScrollPane createScrollPane() {
        return new ScrollPane(this.vBox);
    }

    private ToolBar createToolBar() {
        Button insertEntry = new Button("Insert Entry");

        insertEntry.setOnAction(event -> {
            AgdEntry newEntry = new AgdEntry();
            agdEntries.add(newEntry);
            createAgdVBox(newEntry);
            entries += 1;
        });

        Button removeEntry = new Button("Remove Entry");

        removeEntry.setOnAction(event -> {
            try {
                agdEntries.remove(entries - 1);
                this.vBox.getChildren().remove(entries - 1);
                entries -= 1;
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
                Popups.ErrorOutOfBounds();
            }
        });

        return new ToolBar(insertEntry, removeEntry);
    }

    private void createAgdVBox(AgdEntry entry) {
        //level
        Label levelLabel = new Label("Level");
        TextField levelTextField = new TextField(String.valueOf(entry.level));
        levelTextField.textProperty().addListener((obs, oldText, newText) -> {
            if (levelTextField.getText().contains("-")) {
                return;
            }
            try {
                entry.level = Integer.parseInt(newText);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });
        //level

        //xptonextlevel
        Label xpToNextLevelLabel = new Label("Xp To Next Level");
        TextField xpToNextLevelTextField = new TextField(String.valueOf(entry.xpToNextLevel));
        xpToNextLevelTextField.textProperty().addListener((obs, oldText, newText) -> {
            if (xpToNextLevelTextField.getText().contains("-")) {
                return;
            }
            try {
                entry.xpToNextLevel = Integer.parseInt(newText);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });
        //xptonextlevel
        
        //xptothislevel
        Label xpToThisLevelLabel = new Label("Xp To This Level");
        TextField xpToThisLevelTextField = new TextField(String.valueOf(entry.xpToThisLevel));
        xpToThisLevelTextField.textProperty().addListener((obs, oldText, newText) -> {
            if (xpToThisLevelTextField.getText().contains("-")) {
                return;
            }
            try {
                entry.xpToThisLevel = Integer.parseInt(newText);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });
        //xptothislevel

        //attributepointsgained
        Label attributePointsGainedLabel = new Label("Attribute Points Gained");
        TextField attributePointsGainedTextField = new TextField(String.valueOf(entry.attributePointsGained));
        attributePointsGainedTextField.textProperty().addListener((obs, oldText, newText) -> {
            if (attributePointsGainedTextField.getText().contains("-")) {
                return;
            }
            try {
                entry.attributePointsGained = Integer.parseInt(newText);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });
        //attributepointsgained

        HBox agdHBox = new HBox(30, 
            levelLabel, levelTextField, 
            xpToNextLevelLabel, xpToNextLevelTextField, 
            xpToThisLevelLabel, xpToThisLevelTextField, 
            attributePointsGainedLabel, attributePointsGainedTextField
        );

        this.vBox.getChildren().add(agdHBox);
    }

    private void vBoxListener() {
        this.vBox.addEventFilter(MouseEvent.ANY, event -> {});
    }

    public void agdReader(Path path) {
        try (FileChannel channel = FileChannel.open(path, StandardOpenOption.READ)) {
            int offset = 16;

            ByteBuffer intBuffer = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
            
            channel.position(8);
            intBuffer.clear();
            channel.read(intBuffer);
            intBuffer.flip();
            entries = intBuffer.getInt();
            
            for (int i = 0; i < entries; i++) {
                AgdEntry agdEntry = new AgdEntry();
                agdEntries.add(agdEntry);

                channel.position(offset * (i + 1));
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                agdEntry.level = intBuffer.getInt();
                
                channel.position(offset * (i + 1) + 4);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                agdEntry.xpToNextLevel = intBuffer.getInt();
                
                channel.position(offset * (i + 1) + 8);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                agdEntry.xpToThisLevel = intBuffer.getInt();

                channel.position(offset * (i + 1) + 12);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                agdEntry.attributePointsGained = intBuffer.getInt();

                createAgdVBox(agdEntry);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Popups.ErrorLoad(path.toFile().getName());
        }
    }

    public void agdWriter(Path path) {
        try (FileChannel channel = FileChannel.open(path, StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            int offset = 16;

            ByteBuffer intBuffer = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
            
            channel.position(0);
            channel.write(ByteBuffer.wrap(new byte[]{0x23, 0x41, 0x47, 0x44}));
          
            channel.position(4);
            channel.write(ByteBuffer.wrap(new byte[]{(byte)0xFE, (byte)0xFF}));

            channel.position(6);
            channel.write(ByteBuffer.wrap(new byte[]{0x10, 0x00}));

            channel.position(8);
            intBuffer.clear();
            intBuffer.putInt(entries);
            intBuffer.flip();
            channel.write(intBuffer);

            channel.position(12);
            channel.write(ByteBuffer.wrap(new byte[]{0x10, 0x00, 0x00, 0x00}));

            for (int i = 0; i < entries; i++) {
                AgdEntry agdEntry = agdEntries.get(i);

                channel.position(offset * (i + 1));
                intBuffer.clear();
                intBuffer.putInt(agdEntry.level);
                intBuffer.flip();
                channel.write(intBuffer);

                channel.position(offset * (i + 1) + 4);
                intBuffer.clear();
                intBuffer.putInt(agdEntry.xpToNextLevel);
                intBuffer.flip();
                channel.write(intBuffer);

                channel.position(offset * (i + 1) + 8);
                intBuffer.clear();
                intBuffer.putInt(agdEntry.xpToThisLevel);
                intBuffer.flip();
                channel.write(intBuffer);

                channel.position(offset * (i + 1) + 12);
                intBuffer.clear();
                intBuffer.putInt(agdEntry.attributePointsGained);
                intBuffer.flip();
                channel.write(intBuffer);
            }
        } catch (IOException e) {
            e.printStackTrace();
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