package xv2;
import java.io.File;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;

public class Main extends Application {
    public static void main(String[] args) throws Exception {
        launch(args);
    }
    HashMap<String,Object> FileTypeRecall=new HashMap<>();

    ExecutorService executorService=Executors.newFixedThreadPool(4);
    
    BorderPane bPane=new BorderPane();

    TabPane tabPane = new TabPane();
        
    Scene scene=new Scene(bPane,1280,800);

    MenuBar menuBar = new MenuBar();
        
    MenuItem loadFolder=new MenuItem("Load Folder");
    MenuItem saveFolder=new MenuItem("Save Folder");

    final Menu File = new Menu("File");
    final Menu Options = new Menu("Options");
    final Menu Help = new Menu("Help");

    @Override
    public void start(Stage primaryStage) throws Exception {
        //bpane
        bPane.setTop(menuBar);
        bPane.setCenter(tabPane);

        //file
        File.getItems().add(loadFolder);
        File.getItems().add(saveFolder);
       
        //menubar
        menuBar.getMenus().addAll(File, Options, Help);

        //loadfolder
        loadFolder.setOnAction(e ->{
            DirectoryChooser directoryChooser=new DirectoryChooser();
            File selectedDirectory = directoryChooser.showDialog(primaryStage);
            
            if (selectedDirectory != null) {
                resetRoutine();
                File[] files = selectedDirectory.listFiles();
                for(File xv2File: files){
                    if(xv2File.isFile()){
                        switch (getFileExtension(xv2File.getAbsolutePath())) {
                            case "agd":
                                Tab tabAgd=new Tab();
                                tabAgd.setClosable(false);
                                tabAgd.setText(xv2File.getName());
                                tabPane.getTabs().add(tabAgd);

                                Agd agd=new Agd();
                                FileTypeRecall.put(xv2File.getAbsolutePath(),agd);
                                
                                    executorService.submit(()->{
                                        try {
                                            agd.agdReader(xv2File.toPath());
                                            Platform.runLater(()->{
                                                tabAgd.setContent(agd.createVbox());
                                            });
                                        } catch (Exception er) {
                                            er.printStackTrace();
                                        }
                                    });
                                break;
                            case "aur":
                                Tab tabAur=new Tab();
                                tabAur.setClosable(false);
                                tabAur.setText(xv2File.getName());
                                tabPane.getTabs().add(tabAur);

                                Aur aur=new Aur();
                                FileTypeRecall.put(xv2File.getAbsolutePath(),aur);

                                executorService.submit(()->{
                                    try {
                                        aur.aurReader(xv2File.toPath());
                                        Platform.runLater(()->{
                                            tabAur.setContent(aur.createSplitPane());
                                        });
                                        
                                    } catch (Exception er) {
                                        er.printStackTrace();
                                    }
                                });
                                break;
                            case "bcm":
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
                                break;

                            case "bdm":
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
                                break;
                            case "bsa":
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
                                break;
                            case "cat":
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
                                break;

                            case "emb","EMB":
                                Tab tabEmb=new Tab();
                                tabEmb.setClosable(false);
                                tabEmb.setText(xv2File.getName());
                                tabPane.getTabs().add(tabEmb);

                                Emb emb=new Emb();
                                FileTypeRecall.put(xv2File.getAbsolutePath(),emb);
                                
                                    executorService.submit(()->{
                                        try {
                                            emb.embReader(xv2File.toPath());
                                            Platform.runLater(()->{
                                                tabEmb.setContent(emb.createVBox());
                                            });
                                        } catch (Exception er) {
                                            er.printStackTrace();
                                        }
                                    });
                                break;
                            default:
                                break;
                        }
                    }
                }
            }
        });
        
        //savefolder
        saveFolder.setOnAction(e ->{
            DirectoryChooser directoryChooser=new DirectoryChooser();
            File selectedDirectory = directoryChooser.showDialog(primaryStage);
            boolean hasSaved=false;
            
            for(String originalPath : FileTypeRecall.keySet()){
            File originalFile = new File(originalPath);
                switch (getFileExtension(originalFile.getName())) {
                    case "agd":
                        Agd agd = (Agd) FileTypeRecall.get(originalPath);
                        executorService.submit(()->{
                            agd.agdWriter(selectedDirectory.toPath().resolve(originalFile.getName()));
                        });
                        hasSaved=true;
                        break;
                    case "aur":
                        Aur aur = (Aur) FileTypeRecall.get(originalPath);
                        executorService.submit(()->{
                            aur.aurWriter(selectedDirectory.toPath().resolve(originalFile.getName()));
                        });
                        hasSaved=true;
                        break;
                    case "bcm":
                        Bcm bcm=(Bcm) FileTypeRecall.get(originalPath);
                        executorService.submit(()->{
                            bcm.bcmWriter(selectedDirectory.toPath().resolve(originalFile.getName()));
                        });
                        hasSaved=true;
                        break;
                    case "bdm":
                        Bdm bdm=(Bdm) FileTypeRecall.get(originalPath);
                        executorService.submit(()->{
                            bdm.bdmWriter(selectedDirectory.toPath().resolve(originalFile.getName()));
                        });
                        hasSaved=true;
                        break;
                    case "bsa":
                        Bsa bsa=(Bsa) FileTypeRecall.get(originalPath);
                        executorService.submit(()->{
                            bsa.bsaWriter(selectedDirectory.toPath().resolve(originalFile.getName()));
                        });
                        hasSaved=true;
                        break;
                    case "cat":
                        Cat cat=(Cat) FileTypeRecall.get(originalPath);
                        executorService.submit(()->{
                            cat.catWriter(selectedDirectory.toPath().resolve(originalFile.getName()));
                        });
                        hasSaved=true;
                        break;
                    case "emb","EMB":
                        Emb emb=(Emb) FileTypeRecall.get(originalPath);
                        executorService.submit(()->{
                            emb.embWriter(selectedDirectory.toPath().resolve(originalFile.getName()));
                        });
                        hasSaved=true;
                        break;
                    default:
                        hasSaved=false;
                        break;
                }
            }
            if(hasSaved && selectedDirectory!=null){
                Popups.SuccessSave();
            }
            else if(selectedDirectory==null){
                Popups.NoSave();
                
            }
        });


        Image image=new Image("/xv2ins_256x256_8bit.png");
        primaryStage.getIcons().add(image);
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setTitle("All-In-One Organiser");
        
        primaryStage.setOnCloseRequest(event->{
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

    public void resetRoutine(){
        tabPane.getTabs().clear();   
        FileTypeRecall.clear();
    }
}
