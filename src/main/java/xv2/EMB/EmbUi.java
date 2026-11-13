package xv2.EMB;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.List;
import javax.imageio.ImageIO;
import UiComponents.Bars.ToolBarFactory;
import UiComponents.Boxes.HBoxFactory;
import UiComponents.Boxes.VBoxFactory;
import UiComponents.Menus.ContextMenuFactory;
import Writers.ByteWriter;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import xv2.Popups;

public class EmbUi implements ContextMenuFactory, VBoxFactory, ToolBarFactory, HBoxFactory {
    private Emb.Image currentEntry;
    public ListView<Emb.Image> listView = createListView();
    private Emb emb;
    private ImageView imageView = createImageView();

    public EmbUi(Emb emb) {
        this.emb = emb;
    }

    public Emb getEmb() {
        return emb;
    }

    private ImageView createImageView() {
        ImageView imageView = new ImageView();
        imageView.setFitHeight(600);
        imageView.setFitWidth(600);
        imageView.setSmooth(true);
        imageView.setCache(true);
        imageView.setPreserveRatio(true);

        return imageView;
    }

    private StackPane createStackPane() {
        StackPane stackPane = new StackPane(imageView);
        HBox.setHgrow(stackPane, Priority.ALWAYS);
        
        return stackPane;
    }

    private ListView<Emb.Image> createListView() {
        ListView<Emb.Image> listView = new ListView<>();
        keysListener(listView);

        listView.setContextMenu(getDefaultContextMenu());
        listView.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue == null) return;

            currentEntry = newValue;
            updateItems(currentEntry);
        });

        return listView;
    }

    private Button addNewImages() {
        Button addNewImages = new Button("Add New Images");
        addNewImages.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choose Image Files");
            fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Image Files", "*.dds"));
            List<File> selectedFiles = fileChooser.showOpenMultipleDialog(null);
           
            try {
                if (selectedFiles != null) {
                    for (File file: selectedFiles) {
                        Emb.Image embImage = new Emb.Image(file.getName() + "\0");
                        embImage.setImage(Files.readAllBytes(file.toPath()));
                        listView.getItems().add(embImage);
                    }
                }
            } catch (IOException e) {
            }
        });

        return addNewImages;
    }

    private Button extractAllImages() {
        Button extractAllImages = new Button("Extract All Images");
        extractAllImages.setOnAction(event -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            File selectedDirectory = directoryChooser.showDialog(null);
            if(selectedDirectory != null) {
                for (int i = 0; i < listView.getItems().size(); i++) {
                    if(!emb.isPortrait()){
                        try(FileChannel channel = FileChannel.open(selectedDirectory.toPath().resolve(listView.getItems().get(i) + ".dds"), StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
                            ByteWriter byteWriter = new ByteWriter(channel);
                            byteWriter.writeByteArray(listView.getItems().get(i).getImage().length, listView.getItems().get(i).getImage());
                        } catch(IOException e) {
                            e.printStackTrace();
                        }
                    }
                    else {
                        try(FileChannel channel = FileChannel.open(selectedDirectory.toPath().resolve(listView.getItems().get(i).getName().replace("\0", "").trim()), StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
                            ByteWriter byteWriter = new ByteWriter(channel);
                            byteWriter.writeByteArray(listView.getItems().get(i).getImage().length, listView.getItems().get(i).getImage());
                        } catch(IOException e) {
                            e.printStackTrace();
                        }
                    } 
                }
                
                Platform.runLater(() -> {
                    Popups.ImagesExtracted();
                });
            }
        });

        return extractAllImages;
    }

    private Button mergeEmbs() {
        Button mergeEmbs = new Button("Merge EMBs");
        mergeEmbs.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choose EMB File");
            fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Emb Files", "*.emb"));
            File selectedFile = fileChooser.showOpenDialog(null);
            EmbParser embParser = new EmbParser();
            if (selectedFile != null) {
                try {
                    listView.getItems().addAll(embParser.parseEmb(selectedFile.toPath()).listView.getItems());  
                } catch (IOException e) {
                }
            }
        });

        return mergeEmbs;
    }

    @Override
    public void copy() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Image Files", "*.dds"));
        fileChooser.setInitialFileName(currentEntry.getName());
        File selectedFile = fileChooser.showSaveDialog(null);
        if(selectedFile != null) {
            try(FileChannel channel = FileChannel.open(selectedFile.toPath(), StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
                ByteWriter byteWriter = new ByteWriter(channel);
                byteWriter.writeByteArray(currentEntry.getImage().length, currentEntry.getImage());
            } catch(IOException e) {
                e.printStackTrace();
            }
            
            Platform.runLater(() -> {
                Popups.ImagesExtracted();
            });
        }
    }

    @Override
    public void paste() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Image Files", "*.dds"));
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            try {
                Emb.Image embImage = new Emb.Image(selectedFile.getName().replace(".dds", ""));
                embImage.setImage(Files.readAllBytes(selectedFile.toPath()));
                listView.getItems().set(listView.getSelectionModel().getSelectedIndex(), embImage);
            } catch (IOException e) {
            }
        }
    }

    @Override
    public void delete() {
        listView.getItems().remove(currentEntry);
    }

    @Override
    public void append() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Image Files", "*.dds"));
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            try {
                Emb.Image embImage = new Emb.Image(selectedFile.getName().replace(".dds", ""));
                embImage.setImage(Files.readAllBytes(selectedFile.toPath()));
                listView.getItems().add((listView.getSelectionModel().getSelectedIndex() + 1), embImage);
            } catch (IOException e) {
            }
        }
    }

    @Override
    public void insert() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Image Files", "*.dds"));
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            try {
                Emb.Image embImage = new Emb.Image(selectedFile.getName().replace(".dds", ""));
                embImage.setImage(Files.readAllBytes(selectedFile.toPath()));
                listView.getItems().add(listView.getSelectionModel().getSelectedIndex(), embImage);
            } catch (IOException e) {
            }
        }
    }

    private void updateItems(Emb.Image embImage) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(embImage.getImage());

        try {
            BufferedImage bufferedImage = ImageIO.read(byteArrayInputStream);
            Image image = SwingFXUtils.toFXImage(bufferedImage, null);
            imageView.setImage(image);
        } catch (NullPointerException | IOException e) {
            Image image = new Image(getClass().getResourceAsStream("/default question mark.png"));
            imageView.setImage(image);
            e.printStackTrace();
        }
    }

    @Override
    public ContextMenu getDefaultContextMenu() {
        ContextMenu menu =  ContextMenuFactory.super.getDefaultContextMenu();
        menu.getItems().get(1).setDisable(false);
        menu.getItems().get(0).setText("Extract Image Ctrl+E");
        menu.getItems().get(1).setText("Replace Ctrl+R");

        return menu;
    }

    @Override
    public void keysListener(Node node) {
        node.setOnKeyPressed(e -> {
            if (e.isControlDown() && e.getCode() == KeyCode.E) copy();
            else if (e.isControlDown() && e.getCode() == KeyCode.R) paste();
            else if (e.getCode() == KeyCode.DELETE) delete();
            else if (e.isControlDown() && e.getCode() == KeyCode.A) append();
            else if (e.isControlDown() && e.getCode() == KeyCode.I) insert();
        });
    }

    @Override
    public VBox createVBox() {
        HBox hBox = createHBox(0, new Node[] {listView, createStackPane()});
        VBox.setVgrow(hBox, Priority.ALWAYS);
        return new VBox(createToolBar(), hBox);
    }

    @Override
    public ToolBar createToolBar() {
        return new ToolBar(addNewImages(), extractAllImages(), mergeEmbs());
    }
}