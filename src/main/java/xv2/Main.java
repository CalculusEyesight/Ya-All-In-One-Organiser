package xv2;
import xv2.AGD.*;
import xv2.AUR.AurAuraUi;

import java.io.File;
import java.io.IOException;
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

    HashMap<String, Object> fileTypesContainer = new HashMap<>();

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
    CheckMenuItem toggleDarkMode = new CheckMenuItem("Dark Mode");

    @Override
    public void start(Stage primaryStage) throws Exception {
        borderPane.setTop(menuBar);
        borderPane.setCenter(tabPane);

        efficiencyMode.setToggleGroup(performanceGroup);
        defaultMode.setToggleGroup(performanceGroup);
        performanceMode.setToggleGroup(performanceGroup);
        defaultMode.setSelected(true);

        File.getItems().addAll(loadFolder, saveFolder);
        Options.getItems().addAll(Performance, autoGroup, toggleDarkMode);
        Performance.getItems().addAll(efficiencyMode, defaultMode, performanceMode);
        Help.getItems().addAll(idList, commonBdmIdList, skillMovesetManual);
        menuBar.getMenus().addAll(File, Options, Help);

        autoGroup.setDisable(true);
        autoGroup.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                boolean hasEmb = true;
                
                while (hasEmb) {
                    String compareEmbDytText = null;
                    String compareEmbText = null;

                    outerLoop:
                    for (Tab tab : tabPane.getTabs()) {
                        if (tab.getText().endsWith("dyt.emb")) {
                            compareEmbDytText = tab.getText().substring(0, Math.min(tab.getText().length(), 7)); 
                            embDytComboBox.getItems().add(compareEmbDytText);
                            groupedEmbDytTabs.add(new ArrayList<>());
                            hasEmb = true;
                            break outerLoop;
                        } else if (tab.getText().endsWith("emb")) {
                            compareEmbText = tab.getText().substring(0, Math.min(tab.getText().length(), 7)); 
                            embComboBox.getItems().add(compareEmbText);
                            groupedEmbTabs.add(new ArrayList<>());
                            hasEmb = true;
                            break outerLoop;
                        } else {
                            hasEmb = false;
                        }
                    }
                    
                    if (hasEmb) {
                        for (Tab tab : tabPane.getTabs()) {
                            if (tab.getText().endsWith("dyt.emb") && compareEmbDytText != null && tab.getText().contains(compareEmbDytText)) {
                                groupedEmbDytTabs.getLast().add(tab);
                            } else if (tab.getText().endsWith("emb") && compareEmbText != null && tab.getText().contains(compareEmbText)) {
                                groupedEmbTabs.getLast().add(tab);
                            } 
                        }

                        if (compareEmbDytText != null) tabPane.getTabs().removeAll(groupedEmbDytTabs.getLast());
                        else if (compareEmbText != null) tabPane.getTabs().removeAll(groupedEmbTabs.getLast());
                    }
                }

                Tab embGroupTab = new Tab();
                Tab embDytGroupTab = new Tab();
                embGroupTab.setGraphic(embComboBox);
                embDytGroupTab.setGraphic(embDytComboBox);
                embGroupTab.setText(".emb");
                embDytGroupTab.setText(".dyt.emb");
                tabPane.getTabs().add(0, embGroupTab);
                tabPane.getTabs().add(1, embDytGroupTab);
            } else {
                embComboBox.getItems().clear();
                embDytComboBox.getItems().clear();

                if (tabPane.getTabs().size() > 0) {
                    tabPane.getTabs().remove(0, 2);
                }

                for (ArrayList<Tab> tabs : groupedEmbTabs) {
                    tabPane.getTabs().addAll(tabs);
                }

                for (ArrayList<Tab> tabs : groupedEmbDytTabs) {
                    tabPane.getTabs().addAll(tabs);
                }

                groupedEmbDytTabs.clear();
                groupedEmbTabs.clear();
            }
        });

        toggleDarkMode.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) scene.getStylesheets().add("darkTheme.css");
            else scene.getStylesheets().remove(0);
        });
        
        embComboBox.getSelectionModel().selectedIndexProperty().addListener((obs, oldValue, newValue) -> {
            TabPane comboBoxTabPane = new TabPane();
            if (newValue.intValue() != -1) {
                comboBoxTabPane.getTabs().addAll(groupedEmbTabs.get(newValue.intValue()));
                tabPane.getTabs().get(0).setContent(comboBoxTabPane);  
            }
        });

        embDytComboBox.getSelectionModel().selectedIndexProperty().addListener((obs, oldValue, newValue) -> {
            TabPane comboBoxTabPane = new TabPane();
            if (newValue.intValue() != -1) {
                comboBoxTabPane.getTabs().addAll(groupedEmbDytTabs.get(newValue.intValue()));
                tabPane.getTabs().get(1).setContent(comboBoxTabPane);  
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

                            AgdUi agd_UI = new AgdUi();
                            
                            fileTypesContainer.put(xv2File.getAbsolutePath(), agd_UI);
                            
                            executorService.submit(() -> {
                                try {
                                    agd_UI.parseAgd(xv2File.toPath(), agd_UI);   
     
                                    Platform.runLater(() -> {
                                        tabAgd.setContent(agd_UI.createVBox());
                                    });
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            });
                        }
                        case "aur" -> {
                            Tab tabAur = new Tab(xv2File.getName());
                            tabAur.setClosable(false);

                            tabPane.getTabs().add(tabAur);

                            AurAuraUi aurAuraUi = new AurAuraUi();
                            fileTypesContainer.put(xv2File.getAbsolutePath(), aurAuraUi);

                            executorService.submit(() -> {
                                try {
                                    aurAuraUi.parseAur(xv2File.toPath(), aurAuraUi);
                                    Platform.runLater(() -> {
                                        tabAur.setContent(aurAuraUi.createSplitPane());
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
                            fileTypesContainer.put(xv2File.getAbsolutePath(), bcm);

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
                            fileTypesContainer.put(xv2File.getAbsolutePath(), bcs);

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
                            fileTypesContainer.put(xv2File.getAbsolutePath(), bdm);

                            executorService.submit(() -> {
                                try {
                                    bdm.bdmReader(xv2File.toPath()); 
                                    
                                    Platform.runLater(() -> {
                                        tabBdm.setContent(bdm.createMainHBox());
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
                            fileTypesContainer.put(xv2File.getAbsolutePath(), bsa);

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
                            fileTypesContainer.put(xv2File.getAbsolutePath(), cat);

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
                        case "ean" -> {
                            Tab tabEan = new Tab(xv2File.getName());
                            tabEan.setClosable(false);

                            tabPane.getTabs().add(tabEan); 

                            Ean ean = new Ean();
                            fileTypesContainer.put(xv2File.getAbsolutePath(), ean);

                            executorService.submit(() -> {
                                try {
                                    ean.eanReader(xv2File.toPath()); 
                                    
                                    Platform.runLater(() -> {
                                        tabEan.setContent(ean.createSplitPane());
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
                            fileTypesContainer.put(xv2File.getAbsolutePath(), emb);
                            
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
                        case "esk" -> {
                            Tab tabEsk = new Tab(xv2File.getName());
                            tabEsk.setClosable(false);

                            tabPane.getTabs().add(tabEsk);

                            Esk esk = new Esk();
                            fileTypesContainer.put(xv2File.getAbsolutePath(), esk);

                            executorService.submit(() -> {
                                try {
                                    esk.eskReader(xv2File.toPath());
                                    Platform.runLater(() -> {
                                        tabEsk.setContent(esk.createSplitPane());
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
        
        for (String originalPath : fileTypesContainer.keySet()) {
            File originalFile = new File(originalPath);

            switch (getFileExtension(originalFile.getName())) {
                case "agd" -> {
                    AgdUi agd_UI = (AgdUi) fileTypesContainer.get(originalPath);
                    AgdWriter agdWriter = new AgdWriter();
                    agdWriter.agdEntries = agd_UI.agdEntries;

                    executorService.submit(() -> {
                        try {
                            agdWriter.writeAgd(selectedDirectory.toPath().resolve(originalFile.getName()));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });

                    hasSaved = true;
                }
                case "aur" -> {

                    executorService.submit(() -> {
                        //aur.aurWriter(selectedDirectory.toPath().resolve(originalFile.getName()));
                    });

                    hasSaved = true;
                }
                case "bcm" -> {
                    

                    executorService.submit(() -> {
                        //bcm.bcmWriter(selectedDirectory.toPath().resolve(originalFile.getName()));
                    });

                    hasSaved = true;
                }
                case "bcs" -> {
                   
                    executorService.submit(() -> {
                        //bcs.bcsWriter(selectedDirectory.toPath().resolve(originalFile.getName()));
                    });

                    hasSaved = true;
                }
                case "bdm" -> {
                   
                    executorService.submit(() -> {
                        //bdm.bdmWriter(selectedDirectory.toPath().resolve(originalFile.getName()));
                    });

                    hasSaved = true;
                }
                case "bsa" -> {

                    executorService.submit(() -> {
                        //bsa.bsaWriter(selectedDirectory.toPath().resolve(originalFile.getName()));
                    });

                    hasSaved = true;
                }
                case "cat" -> {
                
                    executorService.submit(() -> {
                        //cat.catWriter(selectedDirectory.toPath().resolve(originalFile.getName()));
                    });

                    hasSaved = true;
                }
                case "ean" -> {
                    

                    executorService.submit(() -> {
                        //ean.eanWriter(selectedDirectory.toPath().resolve(originalFile.getName()));
                    });

                    hasSaved = true;
                }
                case "emb","EMB" -> {

                    executorService.submit(() -> {
                        //emb.embWriter(selectedDirectory.toPath().resolve(originalFile.getName()));
                    });

                    hasSaved = true;
                }
                case "esk" -> {
                    

                    executorService.submit(() -> {
                        //esk.eskWriter(selectedDirectory.toPath().resolve(originalFile.getName()));
                    });

                    hasSaved = true;
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
        fileTypesContainer.clear();
    }
}