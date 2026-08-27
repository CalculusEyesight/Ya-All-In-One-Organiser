package xv2;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import javax.imageio.ImageIO;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
public class Emb {
    ArrayList<byte []> allImages;

    HBox hBox = new HBox();
    ListView<String> listView = new ListView<>();

    int embEntries = 0;
    int i08;
    boolean isPortrait = false;

    Emb() {
        entriesActionListener();
        entriesKeysListener();
    }

    public VBox createVBox() {
        VBox.setVgrow(hBox, Priority.ALWAYS);
        return new VBox(createToolBar(), createHBox());
    }

    private Button addNewImages() {
        Button addNewImages = new Button("Add New Images");

        addNewImages.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();

            fileChooser.setTitle("Choose Image Files");
            fileChooser.getExtensionFilters().addAll(
                new ExtensionFilter("Image Files", "*.dds")
            );

            List<File> selectedFiles = fileChooser.showOpenMultipleDialog(null);

            try {
                if (selectedFiles != null) {
                    for (File file: selectedFiles) {
                        allImages.add(Files.readAllBytes(file.toPath()));
                        listView.getItems().add(file.getName() + "\0");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
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
                for (int i = 0; i < allImages.size(); i++) {
                    if(!isPortrait){
                        try(FileChannel channel = FileChannel.open(selectedDirectory.toPath().resolve(listView.getItems().get(i) + ".dds"), StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
                            ByteBuffer dynamicBuffer = ByteBuffer.allocate(allImages.get(i).length);

                            dynamicBuffer.clear();
                            dynamicBuffer.put(allImages.get(i));
                            dynamicBuffer.flip();
                            channel.write(dynamicBuffer);
                        } catch(IOException e) {
                            e.printStackTrace();
                        }
                    }
                    else {
                        try(FileChannel channel = FileChannel.open(selectedDirectory.toPath().resolve(listView.getItems().get(i).replace("\0", "").trim()), StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
                            ByteBuffer dynamicBuffer = ByteBuffer.allocate(allImages.get(i).length);

                            dynamicBuffer.clear();
                            dynamicBuffer.put(allImages.get(i));
                            dynamicBuffer.flip();
                            channel.write(dynamicBuffer);
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
            
            fileChooser.setTitle("Open Resource File");
            fileChooser.getExtensionFilters().addAll(
                new ExtensionFilter("Emb Files", "*.emb")
            );
            
            File selectedFile = fileChooser.showOpenDialog(null);

            if (selectedFile != null) {
                embReader(selectedFile.toPath(), listView.getItems().size());
            }
        });

        return mergeEmbs;
    }

    private ToolBar createToolBar() {
        return new ToolBar(addNewImages(), extractAllImages(), mergeEmbs());
    }

    private HBox createHBox() {
        this.hBox.getChildren().addAll(listView, new ImageView());
        return hBox;
    }

    private StackPane createImageView(int i) {
        ImageView imageView = new ImageView();

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(allImages.get(i));

        try {
            BufferedImage bufferedImage = ImageIO.read(byteArrayInputStream);

            Image image = SwingFXUtils.toFXImage(bufferedImage, null);

            imageView.setImage(image);
        } catch (NullPointerException | IOException e) {
            Image image = new Image(getClass().getResourceAsStream("/default question mark.png"));

            imageView.setImage(image);
            e.printStackTrace();
        }
        
        StackPane imageContainer = new StackPane(imageView);

        this.hBox.getChildren().add(imageContainer);
        HBox.setHgrow(imageContainer, Priority.ALWAYS);
        
        imageView.setFitHeight(600);
        imageView.setFitWidth(600);
        imageView.setSmooth(true);
        imageView.setCache(true);
        imageView.setPreserveRatio(true);

        return imageContainer;
    }

    private void entriesActionListener() {
        listView.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue == null) return;

            try {
                hBox.getChildren().remove(1);
                hBox.getChildren().set(1, createImageView(listView.getSelectionModel().getSelectedIndex()));
            } catch (IndexOutOfBoundsException e) {
                return;
            }
        });

        listView.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.SECONDARY) {
                ContextMenu contextMenu=new ContextMenu();
                MenuItem delete=new MenuItem("Delete Del");
                MenuItem append=new MenuItem("Append Ctrl+A");
                MenuItem insert=new MenuItem("Insert Ctrl+I");
                MenuItem replace=new MenuItem("Replace Ctrl+R");
                MenuItem extractImage = new MenuItem("Extract Image Ctrl+E");
                contextMenu.getItems().addAll(delete,append,insert,replace,extractImage);
                listView.setContextMenu(contextMenu);
                contextMenu.setOnAction(event -> {
                    if (event.getTarget() == delete) Delete();
                    else if (event.getTarget() == append) Append();
                    else if (event.getTarget() == insert) Insert();
                    else if (event.getTarget() == replace) Replace();
                    else if (event.getTarget() == extractImage) ExtractImage();
                });
            }
        });
    }

    private void entriesKeysListener() {
        listView.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.DELETE) Delete();
            else if (e.isControlDown() && e.getCode() == KeyCode.A) Append();
            else if (e.isControlDown() && e.getCode() == KeyCode.I) Insert();
            else if (e.isControlDown() && e.getCode() == KeyCode.R) Replace();
            else if (e.isControlDown() && e.getCode() == KeyCode.E) ExtractImage();
        });
    }

    private void Delete() { 
        allImages.remove(listView.getSelectionModel().getSelectedIndex());
        listView.getItems().remove(listView.getSelectionModel().getSelectedIndex());
    }

    private void Append() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
            new ExtensionFilter("Image Files", "*.dds")
        );

        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            try {
                allImages.add((listView.getSelectionModel().getSelectedIndex() + 1), Files.readAllBytes(selectedFile.toPath()));
                listView.getItems().add((listView.getSelectionModel().getSelectedIndex() + 1), selectedFile.getName().replace(".dds", ""));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void Insert() {
        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
            new ExtensionFilter("Image Files", "*.dds")
        );

        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            try {
                allImages.add((listView.getSelectionModel().getSelectedIndex()), Files.readAllBytes(selectedFile.toPath()));
                listView.getItems().add((listView.getSelectionModel().getSelectedIndex()), selectedFile.getName().replace(".dds", ""));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void Replace() {
        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
            new ExtensionFilter("Image Files", "*.dds")
        );

        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            try {
                allImages.add((listView.getSelectionModel().getSelectedIndex() + 1), Files.readAllBytes(selectedFile.toPath()));
                listView.getItems().add(listView.getSelectionModel().getSelectedIndex() + 1, selectedFile.getName().replace(".dds", ""));

                allImages.remove(listView.getSelectionModel().getSelectedIndex());
                listView.getItems().remove(listView.getSelectionModel().getSelectedIndex());

                listView.getSelectionModel().select(listView.getSelectionModel().getSelectedIndex() + 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void ExtractImage() {
        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
            new ExtensionFilter("Image Files", "*.dds")
        );
        fileChooser.setInitialFileName(listView.getSelectionModel().getSelectedItem());

        File selectedFile = fileChooser.showSaveDialog(null);

        if (selectedFile != null) {
            if (!isPortrait){
                try(FileChannel channel = FileChannel.open(selectedFile.toPath(), StandardOpenOption.WRITE, StandardOpenOption.CREATE,StandardOpenOption.TRUNCATE_EXISTING)) {
                    ByteBuffer dynamicBuffer = ByteBuffer.allocate(allImages.get(listView.getSelectionModel().getSelectedIndex()).length);

                    dynamicBuffer.clear();
                    dynamicBuffer.put(allImages.get(listView.getSelectionModel().getSelectedIndex()));
                    dynamicBuffer.flip();
                    channel.write(dynamicBuffer);
                } catch(IOException e) {
                    e.printStackTrace();
                }
            }
            else {
                try(FileChannel channel = FileChannel.open(selectedFile.toPath(), StandardOpenOption.WRITE,StandardOpenOption.CREATE,StandardOpenOption.TRUNCATE_EXISTING)) {
                    ByteBuffer dynamicBuffer = ByteBuffer.allocate(allImages.get(listView.getSelectionModel().getSelectedIndex()).length);

                    dynamicBuffer.clear();
                    dynamicBuffer.put(allImages.get(listView.getSelectionModel().getSelectedIndex()));
                    dynamicBuffer.flip();
                    channel.write(dynamicBuffer);
                } catch(IOException e) {
                    e.printStackTrace();
                }
            } 

            Platform.runLater(() -> {
                Popups.ImagesExtracted();
            });
        }
    }

    public void embReader(Path path) {
        try(FileChannel channel = FileChannel.open(path, StandardOpenOption.READ)) {
            int entriesOffset = 32;
            int filesOffset;
            int fileNamesOffset;
            int imageOffset;
            int imageSize;

            ByteBuffer byteBuffer = ByteBuffer.allocate(1).order(ByteOrder.LITTLE_ENDIAN);
            ByteBuffer intBuffer = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
            ByteBuffer dynamicStringBuffer;
            ByteBuffer dynamicImageBuffer;

            channel.position(8);
            intBuffer.clear();
            channel.read(intBuffer);
            intBuffer.flip();
            i08 = intBuffer.getInt();

            channel.position(12);
            intBuffer.clear();
            channel.read(intBuffer);
            intBuffer.flip();
            embEntries = intBuffer.getInt();
            
            channel.position(28);
            intBuffer.clear();
            channel.read(intBuffer);
            intBuffer.flip();
            filesOffset = intBuffer.getInt();

            allImages = new ArrayList<>(embEntries);

            for (int i = 0; i < embEntries; i++) {
                if (filesOffset != 0) {
                    isPortrait = true;

                    channel.position(filesOffset + i * 4);
                    intBuffer.clear();
                    channel.read(intBuffer);
                    intBuffer.flip();
                    fileNamesOffset = intBuffer.getInt();

                    int counter = 0;

                    do {
                        channel.position(fileNamesOffset + counter);
                        byteBuffer.clear();
                        channel.read(byteBuffer);
                        byteBuffer.flip();
                        counter++;
                    } while (byteBuffer.get() != 0);

                    dynamicStringBuffer = ByteBuffer.allocate(counter).order(ByteOrder.LITTLE_ENDIAN);

                    channel.position(fileNamesOffset);
                    dynamicStringBuffer.clear();
                    channel.read(dynamicStringBuffer);
                    dynamicStringBuffer.flip();

                    listView.getItems().add(new String(dynamicStringBuffer.array(), StandardCharsets.ISO_8859_1));

                    channel.position(entriesOffset + i * 8);
                    intBuffer.clear();
                    channel.read(intBuffer);
                    intBuffer.flip();
                    imageOffset = intBuffer.getInt();

                    channel.position(entriesOffset + 4 + i * 8);
                    intBuffer.clear();
                    channel.read(intBuffer);
                    intBuffer.flip();
                    imageSize = intBuffer.getInt();

                    dynamicImageBuffer = ByteBuffer.allocate(imageSize);

                    channel.position(entriesOffset + imageOffset + i * 8);
                    dynamicImageBuffer.clear();
                    channel.read(dynamicImageBuffer);
                    dynamicImageBuffer.flip();

                    allImages.add(dynamicImageBuffer.array());
                }
                else{
                    String dataString;

                    if (i < 10) { 
                        dataString = "DATA00"; 
                    } else if (i < 100) { 
                        dataString = "DATA0"; 
                    } else { 
                        dataString = "DATA"; 
                    }

                    listView.getItems().add(dataString + i);

                    channel.position(entriesOffset + i * 8);
                    intBuffer.clear();
                    channel.read(intBuffer);
                    intBuffer.flip();
                    imageOffset = intBuffer.getInt();

                    channel.position(entriesOffset + 4 + i * 8);
                    intBuffer.clear();
                    channel.read(intBuffer);
                    intBuffer.flip();
                    imageSize=intBuffer.getInt();

                    dynamicImageBuffer = ByteBuffer.allocate(imageSize);

                    channel.position(entriesOffset + imageOffset + i * 8);
                    dynamicImageBuffer.clear();
                    channel.read(dynamicImageBuffer);
                    dynamicImageBuffer.flip();

                    allImages.add(dynamicImageBuffer.array());
                }
            }
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void embReader(Path path, int initialEntries) {
        try(FileChannel channel = FileChannel.open(path, StandardOpenOption.READ)) {
            int entriesOffset = 32;
            int filesOffset;
            int fileNamesOffset;
            int imageOffset;
            int imageSize;

            ByteBuffer byteBuffer = ByteBuffer.allocate(1).order(ByteOrder.LITTLE_ENDIAN);
            ByteBuffer intBuffer = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
            ByteBuffer dynamicStringBuffer;
            ByteBuffer dynamicImageBuffer;

            channel.position(12);
            intBuffer.clear();
            channel.read(intBuffer);
            intBuffer.flip();
            embEntries = intBuffer.getInt();
            
            channel.position(28);
            intBuffer.clear();
            channel.read(intBuffer);
            intBuffer.flip();
            filesOffset = intBuffer.getInt();

            allImages.ensureCapacity(embEntries + initialEntries);

            for (int i = 0; i < embEntries; i++) {
                if (filesOffset != 0) {
                    channel.position(filesOffset + i * 4);
                    intBuffer.clear();
                    channel.read(intBuffer);
                    intBuffer.flip();
                    fileNamesOffset = intBuffer.getInt();

                    int counter = 0;

                    do {
                        channel.position(fileNamesOffset + counter);
                        byteBuffer.clear();
                        channel.read(byteBuffer);
                        byteBuffer.flip();
                        counter++;
                    } while (byteBuffer.get() != 0);

                    dynamicStringBuffer = ByteBuffer.allocate(counter).order(ByteOrder.LITTLE_ENDIAN);

                    channel.position(fileNamesOffset);
                    dynamicStringBuffer.clear();
                    channel.read(dynamicStringBuffer);
                    dynamicStringBuffer.flip();

                    listView.getItems().add(new String(dynamicStringBuffer.array(),StandardCharsets.ISO_8859_1));

                    channel.position(entriesOffset + i * 8);
                    intBuffer.clear();
                    channel.read(intBuffer);
                    intBuffer.flip();
                    imageOffset = intBuffer.getInt();

                    channel.position(entriesOffset + 4 + i * 8);
                    intBuffer.clear();
                    channel.read(intBuffer);
                    intBuffer.flip();
                    imageSize = intBuffer.getInt();

                    dynamicImageBuffer = ByteBuffer.allocate(imageSize);

                    channel.position(entriesOffset + imageOffset + i * 8);
                    dynamicImageBuffer.clear();
                    channel.read(dynamicImageBuffer);
                    dynamicImageBuffer.flip();

                    allImages.add(dynamicImageBuffer.array());
                }
                else{
                    String dataString;

                    if ((initialEntries + i) < 10) { 
                        dataString = "DATA00"; 
                    } else if ((initialEntries + i) < 100) { 
                        dataString = "DATA0"; 
                    } else { 
                        dataString = "DATA"; 
                    }

                    listView.getItems().add(dataString + (initialEntries + i));

                    channel.position(entriesOffset + i * 8);
                    intBuffer.clear();
                    channel.read(intBuffer);
                    intBuffer.flip();
                    imageOffset = intBuffer.getInt();

                    channel.position(entriesOffset + 4 + i * 8);
                    intBuffer.clear();
                    channel.read(intBuffer);
                    intBuffer.flip();
                    imageSize = intBuffer.getInt();

                    dynamicImageBuffer = ByteBuffer.allocate(imageSize);

                    channel.position(entriesOffset + imageOffset + i * 8);
                    dynamicImageBuffer.clear();
                    channel.read(dynamicImageBuffer);
                    dynamicImageBuffer.flip();

                    allImages.add(dynamicImageBuffer.array());
                }
            }
        }
        catch(IOException e ) {
            e.printStackTrace();
        }
    }

    public void embWriter(Path path) {
        try(FileChannel channel = FileChannel.open(path, StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            int entriesOffset = 32;
            int imageSizeOffset = entriesOffset + 4;
            int filesOffset= 32 + listView.getItems().size() * 8;
            int absoluteDataStart = 32 + listView.getItems().size() * 12;
            int byteBoundary64 = 64;

            ByteBuffer intBuffer = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
            ByteBuffer dynamicStringBuffer;
            ByteBuffer dynamicImageBuffer;

            while (((float) byteBoundary64 / absoluteDataStart) < 1) {
                byteBoundary64 += 64;
            }

            absoluteDataStart = byteBoundary64;
            int imageOffset = absoluteDataStart - 32;

            channel.write(ByteBuffer.wrap(new byte[]{0x23, 0x45, (byte)0x4D, 0x42}));

            channel.position(4);
            channel.write(ByteBuffer.wrap(new byte[]{(byte)0xFE, (byte)0xFF}));

            channel.position(6);
            channel.write(ByteBuffer.wrap(new byte[]{0x20, 0x00}));

            channel.position(8);
            intBuffer.clear();
            intBuffer.putInt(i08);
            intBuffer.flip();
            channel.write(intBuffer);

            channel.position(12);
            intBuffer.clear();
            intBuffer.putInt(listView.getItems().size());
            intBuffer.flip();
            channel.write(intBuffer);
            
            channel.position(24);
            intBuffer.clear();
            intBuffer.putInt(entriesOffset);
            intBuffer.flip();
            channel.write(intBuffer);

            if (isPortrait) {
                int fileNamesOffset = absoluteDataStart + allImages.stream().mapToInt(arr -> arr.length).sum();

                channel.position(28);
                intBuffer.clear();
                intBuffer.putInt(filesOffset);
                intBuffer.flip();
                channel.write(intBuffer);

                for (int i = 0; i < listView.getItems().size(); i++) {
                    channel.position(filesOffset + i * 4);
                    intBuffer.clear();
                    intBuffer.putInt(fileNamesOffset);
                    intBuffer.flip();
                    channel.write(intBuffer);
                    
                    dynamicStringBuffer = ByteBuffer.allocate(listView.getItems().get(i).length());

                    channel.position(fileNamesOffset);
                    dynamicStringBuffer.clear();
                    dynamicStringBuffer = ByteBuffer.wrap(listView.getItems().get(i).getBytes());
                    channel.write(dynamicStringBuffer);

                    channel.position(entriesOffset + i * 8);
                    intBuffer.clear();
                    intBuffer.putInt(imageOffset - i * 8);
                    intBuffer.flip();
                    channel.write(intBuffer);

                    channel.position(imageSizeOffset + i * 8);
                    intBuffer.clear();
                    intBuffer.putInt(allImages.get(i).length);
                    intBuffer.flip();
                    channel.write(intBuffer);

                    dynamicImageBuffer = ByteBuffer.allocate(allImages.get(i).length);

                    channel.position(absoluteDataStart);
                    dynamicImageBuffer.clear();
                    dynamicImageBuffer = ByteBuffer.wrap(allImages.get(i));
                    channel.write(dynamicImageBuffer);

                    fileNamesOffset += listView.getItems().get(i).length();
                    absoluteDataStart += allImages.get(i).length;
                    imageOffset += (allImages.get(i).length);
                }
            }
            else{
               for (int i = 0; i < listView.getItems().size(); i++) {
                    channel.position(entriesOffset + i * 8);
                    intBuffer.clear();
                    intBuffer.putInt(imageOffset - i * 8);
                    intBuffer.flip();
                    channel.write(intBuffer);

                    channel.position(imageSizeOffset + i * 8);
                    intBuffer.clear();
                    intBuffer.putInt(allImages.get(i).length);
                    intBuffer.flip();
                    channel.write(intBuffer);

                    dynamicImageBuffer = ByteBuffer.allocate(allImages.get(i).length);

                    channel.position(absoluteDataStart);
                    dynamicImageBuffer.clear();
                    dynamicImageBuffer = ByteBuffer.wrap(allImages.get(i));
                    channel.write(dynamicImageBuffer);

                    absoluteDataStart += allImages.get(i).length;
                    imageOffset += allImages.get(i).length;
               }
            }
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }
}
