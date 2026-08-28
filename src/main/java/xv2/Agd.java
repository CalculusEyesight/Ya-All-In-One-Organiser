package xv2;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
class Agd {
    VBox vBox = new VBox(10);
    ArrayList<AgdEntry> agdEntries = new ArrayList<>();

    public Agd() {
        this.vBox.setPadding(new Insets(20, 0, 20, 60));
        vBoxListener();
    }

    public VBox createVbox() {
        return new VBox(createToolBar(), createScrollPane());
    }

    private ScrollPane createScrollPane() {
        return new ScrollPane(this.vBox);
    }

    private Button insertEntry() {
        Button insertEntry = new Button("Insert Entry");

        insertEntry.setOnAction(event -> {
            agdEntries.add(new AgdEntry());
            createAgdVBox(agdEntries.getLast());
        });

        return insertEntry;
    }

    private Button removeEntry() {
        Button removeEntry = new Button("Remove Entry");

        removeEntry.setOnAction(event -> {
            try {
                this.vBox.getChildren().remove(agdEntries.size() - 1);
                agdEntries.remove(agdEntries.size() - 1);
            } catch (IndexOutOfBoundsException e) {
                Popups.ErrorOutOfBounds();
            }
        });

        return removeEntry;
    }

    private ToolBar createToolBar() {
        return new ToolBar(insertEntry(), removeEntry());
    }

    private void createAgdVBox(AgdEntry entry) {
        HBox agdHBox = new HBox(30, 
            createLabel("Level"), createTextField(agdEntries.indexOf(entry), AgdValues.Level), 
            createLabel("Xp To Next Level"), createTextField(agdEntries.indexOf(entry), AgdValues.XpToNextLevel), 
            createLabel("Xp To This Level"), createTextField(agdEntries.indexOf(entry), AgdValues.XpToThisLevel), 
            createLabel("Attribute Points Gained"),createTextField(agdEntries.indexOf(entry), AgdValues.AttributePointsGained)
        );
        agdHBox.setAlignment(Pos.CENTER_LEFT);

        this.vBox.getChildren().add(agdHBox);
    }

    private Label createLabel(String text) {
        return new Label(text);
    }

    private TextField createTextField(int i, AgdValues index) {
        int value = 0;
        switch (index) {
            case AgdValues.Level -> value = agdEntries.get(i).level;
            case AgdValues.XpToNextLevel -> value = agdEntries.get(i).xpToNextLevel;
            case AgdValues.XpToThisLevel -> value = agdEntries.get(i).xpToThisLevel;
            case AgdValues.AttributePointsGained -> value = agdEntries.get(i).attributePointsGained;
        }

        TextField textField = new TextField(String.valueOf(value));
        textField.textProperty().addListener((obs, oldText, newText) -> {
            if (textField.getText().contains("-")) {
                return;
            }
            try {
                switch (index) {
                    case AgdValues.Level -> agdEntries.get(i).level = Integer.parseInt(newText);
                    case AgdValues.XpToNextLevel -> agdEntries.get(i).xpToNextLevel = Integer.parseInt(newText);
                    case AgdValues.XpToThisLevel -> agdEntries.get(i).xpToThisLevel = Integer.parseInt(newText);
                    case AgdValues.AttributePointsGained -> agdEntries.get(i).attributePointsGained = Integer.parseInt(newText);
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        return textField;
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
            int entries = intBuffer.getInt();
            
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

                createAgdVBox(agdEntries.get(i));
            }
        } catch (IOException e) {
            e.printStackTrace();
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
            intBuffer.putInt(agdEntries.size());
            intBuffer.flip();
            channel.write(intBuffer);

            channel.position(12);
            channel.write(ByteBuffer.wrap(new byte[]{0x10, 0x00, 0x00, 0x00}));

            for (int i = 0; i < agdEntries.size(); i++) {
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
        }
    }

    public static enum AgdValues {
        Level,
        XpToNextLevel,
        XpToThisLevel,
        AttributePointsGained;
    }
}

class AgdEntry {
    public int level;
    public int xpToNextLevel;
    public int xpToThisLevel;
    public int attributePointsGained;
}