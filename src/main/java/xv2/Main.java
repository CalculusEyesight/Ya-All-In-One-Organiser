package xv2;
import java.io.File;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;

public class Main extends Application {
    public static void main(String[] args) throws Exception {
        launch(args);
    }

    HashMap<String,Object> FileTypeRecall = new HashMap<>();

    ExecutorService executorService = Executors.newFixedThreadPool(4);
    
    BorderPane borderPane = new BorderPane();

    TabPane tabPane = new TabPane();
        
    Scene scene = new Scene(borderPane, 1280, 800);

    MenuBar menuBar = new MenuBar();

    ToggleGroup performanceGroup = new ToggleGroup();
        
    MenuItem loadFolder = new MenuItem("Load Folder");
    MenuItem saveFolder = new MenuItem("Save Folder");

    MenuItem idList = new MenuItem("ID List");
    MenuItem commonBdmIdList = new MenuItem("Common BDM ID List");
    MenuItem skillMovesetManual = new MenuItem("Skill/Movset Manual");

    RadioMenuItem efficiencyMode = new RadioMenuItem("Effiency Mode");
    RadioMenuItem defaultMode = new RadioMenuItem("Default");
    RadioMenuItem performanceMode = new RadioMenuItem("Performance Mode");
    
    final Menu File = new Menu("File");
    final Menu Options = new Menu("Options");
    final Menu Help = new Menu("Help");
    final Menu Performance = new Menu("Performance");

    @Override
    public void start(Stage primaryStage) throws Exception {
        //bpane
        borderPane.setTop(menuBar);
        borderPane.setCenter(tabPane);

        //radio menu
        efficiencyMode.setToggleGroup(performanceGroup);
        defaultMode.setToggleGroup(performanceGroup);
        performanceMode.setToggleGroup(performanceGroup);
        defaultMode.setSelected(true);

        //file
        File.getItems().add(loadFolder);
        File.getItems().add(saveFolder);

        //option
        Options.getItems().addAll(Performance);

        //performance
        Performance.getItems().addAll(efficiencyMode, defaultMode, performanceMode);

        //help
        Help.getItems().addAll(idList, commonBdmIdList, skillMovesetManual);
       
        //menubar
        menuBar.getMenus().addAll(File, Options, Help);

        //loadfolder
        loadFolder.setOnAction(e -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();

            File selectedDirectory = directoryChooser.showDialog(primaryStage);
            
            if (selectedDirectory != null) {

                resetRoutine();

                File[] files = selectedDirectory.listFiles();

                for (File xv2File : files) {
                    if (xv2File.isFile()) {
                        switch (getFileExtension(xv2File.getAbsolutePath())) {
                            case "agd" -> {
                                Tab tabAgd = new Tab();
                                tabAgd.setClosable(false);
                                tabAgd.setText(xv2File.getName());
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
                                Tab tabAur = new Tab();
                                tabAur.setClosable(false);
                                tabAur.setText(xv2File.getName());
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
                                Tab tabBcm = new Tab();
                                tabBcm.setClosable(false);
                                tabBcm.setText(xv2File.getName());
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
                            case "bdm" -> {
                                Tab tabBdm = new Tab();
                                tabBdm.setClosable(false);
                                tabBdm.setText(xv2File.getName());
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
                                Tab tabBsa = new Tab();
                                tabBsa.setClosable(false);
                                tabBsa.setText(xv2File.getName());
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
                                Tab tabCat = new Tab();
                                tabCat.setClosable(false);
                                tabCat.setText(xv2File.getName());
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
                                Tab tabEmb = new Tab();
                                tabEmb.setClosable(false);
                                tabEmb.setText(xv2File.getName());
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
        });
        
        //savefolder
        saveFolder.setOnAction(e -> {
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
            event.consume();
            Platform.exit();
        });  
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
        tabPane.getTabs().clear();   
        FileTypeRecall.clear();
    }
}