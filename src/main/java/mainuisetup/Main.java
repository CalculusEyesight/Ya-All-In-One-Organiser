package mainuisetup;
import xv2.Popups;
import xv2.Xv2File;
import java.io.File;
import javafx.application.Application;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

public class Main extends Application {
    public static void main(String[] args) throws Exception {
        launch(args);
    }

    private final BorderPane borderPane = new BorderPane();
    private final TabPane tabPane = new TabPane();
    private final Scene scene = new Scene(borderPane, 1280, 800);

    @Override
    public void start(Stage primaryStage) throws Exception {
        setupScene();
        Menus.setupMenus();
        AutoGrouping.setupAutoGrouping(tabPane);
        AutoGrouping.setupEmbComboBoxes(tabPane);
        Threading.setupPerformanceMode();
        setupDarkMode();
        setupFileActions(primaryStage);
        setupDragAndDrop(primaryStage);
        setupHelpActions();
        setupStage(primaryStage);
    }

    private void setupStage(Stage primaryStage) {
        Image image = new Image("/xv2ins_256x256_8bit.png");

        primaryStage.getIcons().add(image);
        primaryStage.setScene(scene);
        primaryStage.setTitle("All-In-One Organiser");
        primaryStage.show();

        primaryStage.setOnCloseRequest(event -> {
            Threading.executorService.shutdown();
        });
    }

    private void setupScene() {
        scene.getStylesheets().add("style.css");
        borderPane.setTop(Menus.menuBar);
        borderPane.setCenter(tabPane);
    }

    private void setupDarkMode() {
        Menus.toggleDarkMode.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                scene.getStylesheets().set(0, "darkTheme.css");
            } else {
                scene.getStylesheets().set(0, "style.css");
            }
        });
    }

    private void setupFileActions(Stage primaryStage) {
        Menus.loadFolder.setOnAction(e -> {
            loadFolder(primaryStage, false, null);
        });

        Menus.saveFolder.setOnAction(e -> {
            saveFolder(primaryStage);
        });
    }

    private void setupDragAndDrop(Stage primaryStage) {
        borderPane.setOnDragOver(event -> {
            Dragboard dragboard = event.getDragboard();

            if (dragboard.hasFiles()) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }

            event.consume();
        });

        borderPane.setOnDragDropped(event -> {
            Dragboard dragboard = event.getDragboard();
            boolean success = false;

            if (dragboard.hasFiles()) {
                loadFolder(primaryStage, true, dragboard);
                success = true;
            }

            event.setDropCompleted(success);
            event.consume();
        });
    }

    private void setupHelpActions() {
        Menus.idList.setOnAction(e ->
            getHostServices().showDocument("https://docs.google.com/spreadsheets/d/1SyHP2fns9w_ovq96eiLejxZBngo2cYbsNxC9fo9YH5w/edit?gid=291233767#gid=291233767")
        );

        Menus.skillMovesetManual.setOnAction(e ->
            getHostServices().showDocument("https://docs.google.com/document/d/1OQGaZhRJ26KgtSRIyRyamrx1Bw_P1xBfCyYmqnvdbY0/edit?tab=t.0")
        );

        Menus.commonBdmIdList.setOnAction(e ->
            getHostServices().showDocument("https://docs.google.com/document/d/1onRNymSXB91eypgAF7kfcmLax9hOdXj37N9Vox9Hl5Y/edit?tab=t.0#heading=h.x17265uzei3s")
        );
    }

    private void loadFolder(Stage primaryStage, boolean hasDraggedAndDropped, Dragboard dragboard) {
        File[] files;

        if (hasDraggedAndDropped) {
            files = dragboard.getFiles().toArray(new File[0]);
        } else {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            File selectedDirectory = directoryChooser.showDialog(primaryStage);

            if (selectedDirectory == null) {
                return;
            }

            files = selectedDirectory.listFiles();
        }

        Menus.autoGroup.setSelected(false);
        tabPane.getTabs().clear();

        for (File file : files) {
            if (!file.isFile()) {
                continue;
            }

            Xv2File xv2File = Xv2File.getXv2File(file);
            
            if (xv2File.isXv2File()) {
                Tab tab = new Tab(xv2File.getFileName());
                tab.setClosable(false);
                tab.setUserData(xv2File);
                tabPane.getTabs().add(tab);
                xv2File.loadFile(file, tab);
            } 
        }
    }

    private void saveFolder(Stage primaryStage) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(primaryStage);

        if (selectedDirectory == null) {
            Popups.NoSave();
            return;
        }
        
        for (Tab tab : tabPane.getTabs()) {
            Xv2File xv2File = (Xv2File) tab.getUserData();
            xv2File.saveFile(selectedDirectory, tab);
        }

        Popups.SuccessSave();
    }
}