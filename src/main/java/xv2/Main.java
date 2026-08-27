package xv2;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.TransferMode;
public class Main extends Application {
    public static void main(String[] args) throws Exception {
        launch(args);
    }

    HashMap<String,Object> FileTypeRecall = new HashMap<>();

    ArrayList<ArrayList<Tab>> groupedEmbTabs = new ArrayList<>();
    ArrayList<ArrayList<Tab>> groupedEmbDytTabs = new ArrayList<>();

    ExecutorService executorService = Executors.newFixedThreadPool(4);
    
    BorderPane borderPane = new BorderPane();

    TabPane tabPane = new TabPane();
        
    Scene scene = new Scene(borderPane, 1280, 800);

    ToggleGroup performanceGroup = new ToggleGroup();

    ComboBox<String> embComboBox = new ComboBox<>();
    ComboBox<String> embDytComboBox = new ComboBox<>();

    MenuBar menuBar = new MenuBar();

    final Menu File = new Menu("File");
    final Menu Options = new Menu("Options");
    final Menu Help = new Menu("Help");
    final Menu Performance = new Menu("Performance");
        
    MenuItem loadFolder = new MenuItem("Load Folder   Ctrl+O");
    MenuItem saveFolder = new MenuItem("Save Folder   Ctrl+S");

    RadioMenuItem efficiencyMode = new RadioMenuItem("Effiency Mode");
    RadioMenuItem defaultMode = new RadioMenuItem("Default");
    RadioMenuItem performanceMode = new RadioMenuItem(" High Performance Mode");

    MenuItem idList = new MenuItem("ID List");
    MenuItem commonBdmIdList = new MenuItem("Common BDM ID List");
    MenuItem skillMovesetManual = new MenuItem("Skill/Movset Manual");
    
    CheckMenuItem autoGroup = new CheckMenuItem("Auto Group EMBs");

    int embTabIndex = -1;
    int embDytTabIndex = -1;

    @Override
    public void start(Stage primaryStage) throws Exception {
        borderPane.setTop(menuBar);
        borderPane.setCenter(tabPane);

        efficiencyMode.setToggleGroup(performanceGroup);
        defaultMode.setToggleGroup(performanceGroup);
        performanceMode.setToggleGroup(performanceGroup);
        defaultMode.setSelected(true);

        File.getItems().addAll(loadFolder, saveFolder);

        Options.getItems().addAll(Performance, autoGroup);

        Performance.getItems().addAll(efficiencyMode, defaultMode, performanceMode);

        Help.getItems().addAll(idList, commonBdmIdList, skillMovesetManual);
       
        menuBar.getMenus().addAll(File, Options, Help);

        autoGroup.setDisable(true);

        autoGroup.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                executorService.submit(() -> {
                    Platform.runLater(() -> {
                        int compareTabIndex = 0;
                        int saveTabGroupIndex = 0;

                        groupedEmbTabs.add(new ArrayList<>());
                        
                        boolean hasdotFound = false;
                        boolean hasUnderscoreFound = false;

                        outerLoop:
                        while (compareTabIndex < tabPane.getTabs().size()) {
                            String tabText = tabPane.getTabs().get(compareTabIndex).getText();
                            while (!tabText.substring(Math.max(0, tabText.length() - 3)).equals("emb") || tabText.contains("dyt.emb")) {
                                try {
                                    compareTabIndex++;
                                    tabText = tabPane.getTabs().get(compareTabIndex).getText();
                                } catch (IndexOutOfBoundsException e) {
                                    break outerLoop;
                                }
                            }
                            int count = tabText.length() - tabText.replace("_", "").length();
                            
                            for (int i = compareTabIndex + 1; i < tabPane.getTabs().size(); i++) {

                                String compareTabString = tabPane.getTabs().get(i).getText();

                                if (count > 0 && !compareTabString.contains("dyt.emb")) {
                                    if (tabText.substring(0, Math.min(tabText.length(), 7)).equals(compareTabString.substring(0, Math.min(compareTabString.length(), 7)))) {
                                        groupedEmbTabs.get(saveTabGroupIndex).add(tabPane.getTabs().get(i));

                                        tabPane.getTabs().remove(i);
                                        i -= 1;

                                        hasUnderscoreFound = true;
                                    }
                                }
                                else if (compareTabString.contains("dyt.emb")) {
                                    if (tabText.substring(0, tabText.indexOf(".")).equals(compareTabString.substring(0, compareTabString.indexOf("."))) ) {

                                        groupedEmbTabs.get(saveTabGroupIndex).add(tabPane.getTabs().get(i));
                                        
                                        tabPane.getTabs().remove(i);
                                        i -= 1;

                                        hasdotFound = true;
                                    }
                                }
                            }
                            if (hasdotFound) {
                                groupedEmbTabs.get(saveTabGroupIndex).add(0, tabPane.getTabs().get(compareTabIndex));

                                tabPane.getTabs().remove(compareTabIndex);

                                embComboBox.getItems().add(tabText.substring(0, tabText.indexOf(".")));

                                groupedEmbTabs.add(new ArrayList<>());

                                saveTabGroupIndex++;
                                hasdotFound = false;
                                compareTabIndex --;
                            }
                            else if (hasUnderscoreFound) {
                                groupedEmbTabs.get(saveTabGroupIndex).add(0, tabPane.getTabs().get(compareTabIndex));

                                tabPane.getTabs().remove(compareTabIndex);

                                embComboBox.getItems().add(tabText.substring(0, tabText.lastIndexOf("_")));

                                groupedEmbTabs.add(new ArrayList<>());

                                saveTabGroupIndex++;
                                hasUnderscoreFound= false;
                                compareTabIndex--;
                            }
                            compareTabIndex++;
                        }
                        if (!embComboBox.getItems().isEmpty()) {
                            embTabIndex = 0;

                            tabPane.getTabs().add(0, new Tab());
                            tabPane.getTabs().getFirst().setClosable(false);
                            tabPane.getTabs().getFirst().setGraphic(embComboBox);
                            tabPane.getTabs().getFirst().setText(".emb " + "Group" );
                            tabPane.getSelectionModel().select(0);
                        }
                    });
                });
                executorService.submit(() -> {
                    Platform.runLater(() -> {
                        int compareTabIndex = 0;
                        int saveTabGroupIndex = 0;

                        groupedEmbDytTabs.add(new ArrayList<>());
                        
                        boolean hasdotFound = false;
                        boolean hasUnderscoreFound = false;

                        outerLoop:
                        while (compareTabIndex < tabPane.getTabs().size()) {
                            String tabText = tabPane.getTabs().get(compareTabIndex).getText();
                            while (!tabText.endsWith("dyt.emb")) {
                                try {
                                    compareTabIndex++;
                                    tabText = tabPane.getTabs().get(compareTabIndex).getText();
                                } catch (IndexOutOfBoundsException e) {
                                    break outerLoop;
                                }
                            }

                            int count = tabText.length() - tabText.replace("_", "").length();
                            
                            for (int i = compareTabIndex + 1; i < tabPane.getTabs().size(); i++) {

                                String compareTabString = tabPane.getTabs().get(i).getText();
                                
                                if (count > 0) {
                                    if (tabText.substring(0, Math.min(tabText.length(), 7)).equals(compareTabString.substring(0, Math.min(compareTabString.length(), 7)))) {
                                        groupedEmbDytTabs.get(saveTabGroupIndex).add(tabPane.getTabs().get(i));

                                        tabPane.getTabs().remove(i);
                                        i -= 1;

                                        hasUnderscoreFound = true;
                                    }
                                }
                                else if (!tabText.endsWith("emb")) {
                                    if (tabText.substring(0, tabText.indexOf(".")).equals(compareTabString.substring(0, compareTabString.indexOf("."))) ) {

                                        groupedEmbDytTabs.get(saveTabGroupIndex).add(tabPane.getTabs().get(i));
                                        
                                        tabPane.getTabs().remove(i);
                                        i -= 1;

                                        hasdotFound = true;
                                    }
                                }
                            }
                            if (hasdotFound) {
                                groupedEmbDytTabs.get(saveTabGroupIndex).add(0, tabPane.getTabs().get(compareTabIndex));

                                tabPane.getTabs().remove(compareTabIndex);

                                embDytComboBox.getItems().add(tabText.substring(0, tabText.indexOf(".")));

                                groupedEmbDytTabs.add(new ArrayList<>());

                                saveTabGroupIndex++;
                                hasdotFound = false;
                                compareTabIndex --;
                            }
                            else if (hasUnderscoreFound) {
                                groupedEmbDytTabs.get(saveTabGroupIndex).add(0, tabPane.getTabs().get(compareTabIndex));

                                tabPane.getTabs().remove(compareTabIndex);

                                embDytComboBox.getItems().add(tabText.substring(0, tabText.lastIndexOf("_")));

                                groupedEmbDytTabs.add(new ArrayList<>());

                                saveTabGroupIndex++;
                                hasUnderscoreFound = false;
                                compareTabIndex--;
                            }
                            compareTabIndex++;
                        }
                        if (!embDytComboBox.getItems().isEmpty()) {
                            embTabIndex = 1;
                            embDytTabIndex = 0;

                            tabPane.getTabs().add(0, new Tab());
                            tabPane.getTabs().getFirst().setClosable(false);
                            tabPane.getTabs().getFirst().setGraphic(embDytComboBox);
                            tabPane.getTabs().getFirst().setText(".dyt.emb " + "Group" );
                            tabPane.getSelectionModel().select(0);
                        }
                    });
                });
            }
            else {
                embComboBox.getItems().clear();
                embDytComboBox.getItems().clear();
                if (tabPane.getTabs().size() > 0) {
                    tabPane.getTabs().remove(0, 2);
                }
                for (int i = 0; i < groupedEmbTabs.size(); i++) {
                    for (int j = 0; j < groupedEmbTabs.get(i).size(); j++) {
                        tabPane.getTabs().add(groupedEmbTabs.get(i).get(j));
                    }
                }
                for (int i = 0; i < groupedEmbDytTabs.size(); i++) {
                    for (int j = 0; j < groupedEmbDytTabs.get(i).size(); j++) {
                        tabPane.getTabs().add(groupedEmbDytTabs.get(i).get(j));
                    }
                }
            }

            groupedEmbDytTabs.clear();
            groupedEmbTabs.clear();
        });
        
        embComboBox.getSelectionModel().selectedIndexProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue.intValue() != -1) {
                TabPane comboBoxTabPane = new TabPane();
                comboBoxTabPane.getTabs().addAll(groupedEmbTabs.get(newValue.intValue()));
                tabPane.getTabs().get(embTabIndex).setContent(comboBoxTabPane);  

            }
        });

        embDytComboBox.getSelectionModel().selectedIndexProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue.intValue() != -1) {
                TabPane comboBoxTabPane = new TabPane();
                comboBoxTabPane.getTabs().addAll(groupedEmbDytTabs.get(newValue.intValue()));
                tabPane.getTabs().get(embDytTabIndex).setContent(comboBoxTabPane);  
            }
        });

        loadFolder.setOnAction(e -> {
            LoadFolder(primaryStage, false, null);
        });
        
        saveFolder.setOnAction(e -> {
            SaveFolder(primaryStage);
        });

        borderPane.setOnKeyPressed(e -> {
            if (e.isControlDown()&& e.getCode() == KeyCode.O) {
                LoadFolder(primaryStage, false, null);
            }
            if (e.isControlDown()&& e.getCode() == KeyCode.S) {
                SaveFolder(primaryStage);
            }
        });
        
        borderPane.setOnDragOver(new EventHandler<DragEvent>() {
            @Override public void handle(DragEvent event) {
                Dragboard db = event.getDragboard();
                if(db.hasFiles()) {
                    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                }
                event.consume();
            }
        });
        borderPane.setOnDragDropped(new EventHandler<DragEvent>() {
            @Override public void handle(DragEvent event) {
                Dragboard db = event.getDragboard();
                boolean success = false;
                if (db.hasFiles()) {
                    LoadFolder(primaryStage, true, db);
                    success = true;
                }
                event.setDropCompleted(success);
                event.consume();
            }
        });
        
        performanceGroup.selectedToggleProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null && newValue.isSelected()) {
                if ((RadioMenuItem) newValue == efficiencyMode) { 
                    ((ThreadPoolExecutor) executorService).setCorePoolSize(2);
                    ((ThreadPoolExecutor) executorService).setMaximumPoolSize(2);
                    
                }
                if ((RadioMenuItem) newValue == defaultMode) { 
                    if (((ThreadPoolExecutor) executorService).getMaximumPoolSize() > 4) {
                        ((ThreadPoolExecutor) executorService).setCorePoolSize(4);
                        ((ThreadPoolExecutor) executorService).setMaximumPoolSize(4);
                    }
                    else {
                        ((ThreadPoolExecutor) executorService).setMaximumPoolSize(4);
                        ((ThreadPoolExecutor) executorService).setCorePoolSize(4);
                    }
                }
                if ((RadioMenuItem) newValue == performanceMode) { 
                    ((ThreadPoolExecutor) executorService).setMaximumPoolSize(8);
                    ((ThreadPoolExecutor) executorService).setCorePoolSize(8);
                }
            }
        });

        idList.setOnAction(e -> getHostServices().showDocument("https://docs.google.com/spreadsheets/d/1SyHP2fns9w_ovq96eiLejxZBngo2cYbsNxC9fo9YH5w/edit?gid=291233767#gid=291233767"));
        skillMovesetManual.setOnAction(e -> getHostServices().showDocument("https://docs.google.com/document/d/1OQGaZhRJ26KgtSRIyRyamrx1Bw_P1xBfCyYmqnvdbY0/edit?tab=t.0"));
        commonBdmIdList.setOnAction(e -> getHostServices().showDocument("https://docs.google.com/document/d/1onRNymSXB91eypgAF7kfcmLax9hOdXj37N9Vox9Hl5Y/edit?tab=t.0#heading=h.x17265uzei3s"));

        Image image = new Image("/xv2ins_256x256_8bit.png");
        primaryStage.getIcons().add(image);
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setTitle("All-In-One Organiser");
        
        primaryStage.setOnCloseRequest(event -> {
            executorService.shutdown();
        });  
    }

    private void LoadFolder(Stage primaryStage, boolean hasDraggedAndDropped, Dragboard dragboard) {
        File selectedDirectory = null;

        if(!hasDraggedAndDropped) {
            DirectoryChooser directoryChooser = new DirectoryChooser();

            selectedDirectory = directoryChooser.showDialog(primaryStage);
        }
        
        if (selectedDirectory != null || hasDraggedAndDropped) {

            resetRoutine();

            File[] files;

            if (hasDraggedAndDropped) {
                files =  dragboard.getFiles().toArray(new File [0]);
            }
            else {
                files = selectedDirectory.listFiles();
            }

            for (File xv2File : files) {
                if (xv2File.isFile()) {
                    switch (getFileExtension(xv2File.getAbsolutePath())) {
                        case "agd" -> {
                            Tab tabAgd = new Tab(xv2File.getName());
                            tabAgd.setClosable(false);

                            tabPane.getTabs().add(tabAgd);

                            Agd agd = new Agd();
                            FileTypeRecall.put(xv2File.getAbsolutePath(),agd);
                            
                            executorService.submit(() -> {
                                try {
                                    agd.agdReader(xv2File.toPath());
                                    Platform.runLater(()->{
                                        tabAgd.setContent(agd.createVbox());
                                    });
                                } catch (Exception er) {
                                    er.printStackTrace();
                                }
                            });
                        }
                        case "aur" -> {
                            Tab tabAur = new Tab(xv2File.getName());
                            tabAur.setClosable(false);

                            tabPane.getTabs().add(tabAur);

                            Aur aur = new Aur();
                            FileTypeRecall.put(xv2File.getAbsolutePath(),aur);

                            executorService.submit(() -> {
                                try {
                                    aur.aurReader(xv2File.toPath());
                                    Platform.runLater(() -> {
                                        tabAur.setContent(aur.createSplitPane());
                                    });
                                    
                                } catch (Exception er) {
                                    er.printStackTrace();
                                }
                            });
                        }
                        case "bcm" -> {
                            Tab tabBcm = new Tab(xv2File.getName());
                            tabBcm.setClosable(false);

                            tabPane.getTabs().add(tabBcm); 

                            Bcm bcm = new Bcm();
                            FileTypeRecall.put(xv2File.getAbsolutePath(), bcm);

                            executorService.submit(() -> {
                                try {
                                    bcm.bcmReader(xv2File.toPath()); 
                                    
                                    Platform.runLater(() -> {
                                        tabBcm.setContent(bcm.createSplitPane());
                                    });

                                } catch (Exception er) {
                                    er.printStackTrace();
                                }
                            });
                        }
                        case "bcs" -> {
                            Tab tabBcs = new Tab(xv2File.getName());
                            tabBcs.setClosable(false);

                            tabPane.getTabs().add(tabBcs); 

                            Bcs bcs = new Bcs();
                            FileTypeRecall.put(xv2File.getAbsolutePath(), bcs);

                            executorService.submit(() -> {
                                try {
                                    bcs.bcsReader(xv2File.toPath()); 
                                    
                                    Platform.runLater(() -> {
                                        tabBcs.setContent(bcs.createSplitPane());
                                    });

                                } catch (Exception er) {
                                    er.printStackTrace();
                                }
                            });

                        }
                        case "bdm" -> {
                            Tab tabBdm = new Tab(xv2File.getName());
                            tabBdm.setClosable(false);

                            tabPane.getTabs().add(tabBdm); 

                            Bdm bdm = new Bdm();
                            FileTypeRecall.put(xv2File.getAbsolutePath(), bdm);

                            executorService.submit(() -> {
                                try {
                                    bdm.bdmReader(xv2File.toPath()); 
                                    
                                    Platform.runLater(() -> {
                                        tabBdm.setContent(bdm.createHBox());
                                    });

                                } catch (Exception er) {
                                    er.printStackTrace();
                                }
                            });
                        }
                        case "bsa" -> {
                            Tab tabBsa = new Tab(xv2File.getName());
                            tabBsa.setClosable(false);

                            tabPane.getTabs().add(tabBsa); 

                            Bsa bsa = new Bsa();
                            FileTypeRecall.put(xv2File.getAbsolutePath(), bsa);

                            executorService.submit(() -> {
                                try {
                                    bsa.bsaReader(xv2File.toPath()); 
                                    
                                    Platform.runLater(() -> {
                                        tabBsa.setContent(bsa.createSplitPane());
                                    });

                                } catch (Exception er) {
                                    er.printStackTrace();
                                }
                            });
                        }
                        case "cat" -> {
                            Tab tabCat = new Tab(xv2File.getName());
                            tabCat.setClosable(false);

                            tabPane.getTabs().add(tabCat); 

                            Cat cat = new Cat();
                            FileTypeRecall.put(xv2File.getAbsolutePath(), cat);

                            executorService.submit(() -> {
                                try {
                                    cat.catReader(xv2File.toPath()); 
                                    
                                    Platform.runLater(() -> {
            
                                        tabCat.setContent(cat.createHBoxOuter());
                                    });

                                } catch (Exception er) {
                                    er.printStackTrace();
                                }
                            });
                        }
                        case "emb","EMB" -> {
                            autoGroup.setDisable(false);
                            
                            Tab tabEmb = new Tab(xv2File.getName());
                            tabEmb.setClosable(false);

                            tabPane.getTabs().add(tabEmb);

                            Emb emb = new Emb();
                            FileTypeRecall.put(xv2File.getAbsolutePath(),emb);
                            
                            executorService.submit(() -> {
                                try {
                                    emb.embReader(xv2File.toPath());
                                    Platform.runLater(() -> {
                                        tabEmb.setContent(emb.createVBox());
                                    });
                                } catch (Exception er) {
                                    er.printStackTrace();
                                }
                            });
                        }
                    }
                }
            }
        }
    }

    private void SaveFolder(Stage primaryStage) {
        DirectoryChooser directoryChooser = new DirectoryChooser();

        File selectedDirectory = directoryChooser.showDialog(primaryStage);

        boolean hasSaved = false;
        
        for (String originalPath : FileTypeRecall.keySet()) {
        File originalFile = new File(originalPath);
            switch (getFileExtension(originalFile.getName())) {
                case "agd" -> {
                    Agd agd = (Agd) FileTypeRecall.get(originalPath);

                    executorService.submit(() -> {
                        agd.agdWriter(selectedDirectory.toPath().resolve(originalFile.getName()));
                    });

                    hasSaved = true;
                }
                case "aur" -> {
                    Aur aur = (Aur) FileTypeRecall.get(originalPath);

                    executorService.submit(() -> {
                        aur.aurWriter(selectedDirectory.toPath().resolve(originalFile.getName()));
                    });

                    hasSaved = true;
                }
                case "bcm" -> {
                    Bcm bcm = (Bcm) FileTypeRecall.get(originalPath);

                    executorService.submit(() -> {
                        bcm.bcmWriter(selectedDirectory.toPath().resolve(originalFile.getName()));
                    });

                    hasSaved = true;
                }
                case "bcs" -> {
                    Bcs bcs = (Bcs) FileTypeRecall.get(originalPath);

                    executorService.submit(() -> {
                        bcs.bcsWriter(selectedDirectory.toPath().resolve(originalFile.getName()));
                    });

                    hasSaved = true;
                }
                case "bdm" -> {
                    Bdm bdm = (Bdm) FileTypeRecall.get(originalPath);

                    executorService.submit(() -> {
                        bdm.bdmWriter(selectedDirectory.toPath().resolve(originalFile.getName()));
                    });

                    hasSaved = true;
                }
                case "bsa" -> {
                    Bsa bsa = (Bsa) FileTypeRecall.get(originalPath);

                    executorService.submit(() -> {
                        bsa.bsaWriter(selectedDirectory.toPath().resolve(originalFile.getName()));
                    });

                    hasSaved = true;
                }
                case "cat" -> {
                    Cat cat = (Cat) FileTypeRecall.get(originalPath);

                    executorService.submit(() -> {
                        cat.catWriter(selectedDirectory.toPath().resolve(originalFile.getName()));
                    });

                    hasSaved = true;
                }
                case "emb","EMB" -> {
                    Emb emb = (Emb) FileTypeRecall.get(originalPath);

                    executorService.submit(() -> {
                        emb.embWriter(selectedDirectory.toPath().resolve(originalFile.getName()));
                    });

                    hasSaved = true;
                }
                default -> {
                    hasSaved=false;
                    break;
                }     
            }
        }
        if (hasSaved && selectedDirectory != null) {
            Popups.SuccessSave();
        }
        else if (selectedDirectory == null) {
            Popups.NoSave();
        }
    }

    String getFileExtension(String filename) {
        if (filename == null) {
            return null;
        }

        int dotIndex = filename.lastIndexOf(".");

        if (dotIndex >= 0) {
            return filename.substring(dotIndex + 1);
        }

        return "";
    }

    public void resetRoutine() {
        autoGroup.setSelected(false);
        tabPane.getTabs().clear();   
        FileTypeRecall.clear();
    }
}