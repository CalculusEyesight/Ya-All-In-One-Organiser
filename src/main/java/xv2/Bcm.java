package xv2;
import static xv2.BinaryUtilities.*;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Spinner;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Tab;

public class Bcm {
    TreeView<String> treeView=new TreeView<>();
    TreeItem<String> currentEntry=new TreeItem<>();

    ArrayList<TreeItem<String>> allEntries;
    
    TabPane tabPane=new TabPane();

    
    int entryIndex;

    //arraylists for input
    ArrayList<Long> getDirectionalInputs = new ArrayList<>(); 
    ArrayList<Long> getButtonInputs = new ArrayList<>(); 
    ArrayList<Long> getHoldDownConditions= new ArrayList<>();


    //arraylists for activator
    ArrayList<Long> getOpponentSizeConditions = new ArrayList<>();       
    ArrayList<Integer> getMinimumLoopDuration = new ArrayList<>();       
    ArrayList<Integer> getMaximumLoopDuration = new ArrayList<>();       
    ArrayList<Long> getPrimaryActivatorConditions = new ArrayList<>();  
    ArrayList<Long> getActivatorState = new ArrayList<>();

    //arraylists for BAC
    ArrayList<Integer> getBACEntryPrimary = new ArrayList<>();
    ArrayList<Integer> getBACEntryCharge = new ArrayList<>();
    ArrayList<Integer> getBACEntryUserConnect = new ArrayList<>();
    ArrayList<Integer> getBACEntryVictimConnect = new ArrayList<>();
    ArrayList<Integer> getBACEntryAirborne = new ArrayList<>();
    ArrayList<Integer> getBACEntryTargetingOverride = new ArrayList<>();
    ArrayList<Short> getBACRandomFlags = new ArrayList<>();

    //arraylists for misc
    ArrayList<Double> getKiCost = new ArrayList<>();                   
    ArrayList<Double> getReceiverLinkId = new ArrayList<>();            
    ArrayList<Double> getStaminaCost = new ArrayList<>();               
    ArrayList<Double> getKiRequired = new ArrayList<>();                 
    ArrayList<Double> getHealthRequired = new ArrayList<>();            
    ArrayList<Integer> getTransformationStage = new ArrayList<>();       
    ArrayList<Integer> getCusAura = new ArrayList<>();                   
    ArrayList<Long> getRaceGender = new ArrayList<>();
    
    //arraylist for unknown
    ArrayList<Long> getUnknown0 = new ArrayList<>();
    ArrayList<Short>getUnknown36=new ArrayList<>();
    ArrayList<Long>getUnknown68=new ArrayList<>();
    ArrayList<Long>getUnknown72=new ArrayList<>();
    ArrayList<Long>getUnknown80=new ArrayList<>();
    ArrayList<Long>getUnknown88=new ArrayList<>();
    ArrayList<Long>getUnknown104=new ArrayList<>();

    //copycontainers
    // Input variables
    private long copyDirectionalInputs;
    private long copyButtonInputs;
    private long copyHoldDownConditions;
    

    // Activator variables
    private long copyOpponentSizeConditions;
    private int copyMinimumLoopDuration;
    private int copyMaximumLoopDuration;
    private long copyPrimaryActivatorConditions;
    private long copyActivatorState;

    // BAC variables
    private int copyBACEntryPrimary;
    private int copyBACEntryCharge;
    private int copyBACEntryUserConnect;
    private int copyBACEntryVictimConnect;
    private int copyBACEntryAirborne;
    private int copyBACEntryTargetingOverride;
    private short copyBACRandomFlags;

    // Misc variables
    private double copyKiCost;
    private double copyReceiverLinkId;
    private double copyStaminaCost;
    private double copyKiRequired;
    private double copyHealthRequired;
    private int copyTransformationStage;
    private int copyCusAura;
    private long copyRaceGender;

    // Unknown variables
    private long copyUnknown0;
    private short copyUnknown36;
    private long copyUnknown68;
    private long copyUnknown72;
    private long copyUnknown80;
    private long copyUnknown88;
    private long copyUnknown104;
  

    public Bcm(){
        entriesActionListener();
        entriesKeysListener();
    }

    public SplitPane createSplitPane(){
        SplitPane splitPane =new SplitPane();
        createTabs();
  
        splitPane.getItems().addAll(treeView, tabPane);
        splitPane.setDividerPositions(0.245);
        String css = getClass().getResource("/style.css").toExternalForm();
        splitPane.getStylesheets().add(css); 
        return splitPane;
    }

  

    public void createTabs(){
        if (tabPane.getTabs().isEmpty()) {
            Tab inputsTab = new Tab("Inputs");
            Tab activatorTab = new Tab("Activator");
            Tab bacTab = new Tab("BAC");
            Tab miscTab = new Tab("Misc");
            Tab unknownTab = new Tab("Unknown");

            inputsTab.setClosable(false);
            activatorTab.setClosable(false);
            bacTab.setClosable(false);
            miscTab.setClosable(false);
            unknownTab.setClosable(false);

            tabPane.getTabs().addAll(inputsTab, activatorTab, bacTab, miscTab, unknownTab);
        }
    }
    
    public VBox createInputsVBox(int i){
        VBox inputsVBox=new VBox(20);
        inputsVBox.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
 
        //directional input
        HBox directionalInputHBox=new HBox(2);
        directionalInputHBox.setPadding(new Insets(20,0,0,8));
        
        Label directionalInputLabel=new Label("Directional input ");
        directionalInputLabel.setPrefWidth(100);

        //relative direction
        Label relativeDirectionLabel = new Label("Relative Direction");
        relativeDirectionLabel.getStyleClass().add("titled-address-label");

        CheckBox forwardsRD=new CheckBox("Forwards");
        CheckBox backwardsRD=new CheckBox("Backwards");
        CheckBox leftRD=new CheckBox("Left");
        CheckBox rightRD= new CheckBox("Right");

        forwardsRD.setSelected((getDirectionalInputs.get(i) & 1L) != 0);
        backwardsRD.setSelected((getDirectionalInputs.get(i) & 2L) != 0);
        leftRD.setSelected((getDirectionalInputs.get(i) & 4L) != 0);
        rightRD.setSelected((getDirectionalInputs.get(i) & 8L) != 0);

        forwardsRD.selectedProperty().addListener((obs,oldValue,newValue)->{
            if(newValue){
                getDirectionalInputs.set(i,getDirectionalInputs.get(i)|1L);
            }
            else{
                getDirectionalInputs.set(i,getDirectionalInputs.get(i)&~1L);
            }
        });
        backwardsRD.selectedProperty().addListener((obs,oldValue,newValue)->{
            if(newValue){
                getDirectionalInputs.set(i,getDirectionalInputs.get(i)|2L);
            }
            else{
                getDirectionalInputs.set(i,getDirectionalInputs.get(i)&~2L);
            }
        });
        leftRD.selectedProperty().addListener((obs,oldValue,newValue)->{
            if(newValue){
                getDirectionalInputs.set(i,getDirectionalInputs.get(i)|4L);
            }
            else{
                getDirectionalInputs.set(i,getDirectionalInputs.get(i)&~4L);
            }
        });
        rightRD.selectedProperty().addListener((obs,oldValue,newValue)->{
            if(newValue){
                getDirectionalInputs.set(i,getDirectionalInputs.get(i)|8L);
            }
            else{
                getDirectionalInputs.set(i,getDirectionalInputs.get(i)&~8L);
            }
        });
        VBox relativeDirectionBox = new VBox(2,forwardsRD,backwardsRD,leftRD,rightRD);

        VBox borderContainerRD=new VBox(relativeDirectionBox);
        borderContainerRD.getStyleClass().add("titled-address-box");
        borderContainerRD.setPadding(new Insets(12,0,0,0));

        StackPane relativeDirectionBoxStackPane = new StackPane();
        relativeDirectionBoxStackPane.getChildren().addAll(borderContainerRD,relativeDirectionLabel);

        StackPane.setAlignment(relativeDirectionLabel, Pos.TOP_LEFT);
        relativeDirectionLabel.setTranslateY(-8); 
        relativeDirectionLabel.setTranslateX(10);
        //relative direction
        

        //user direction
        Label userDirectionLabel = new Label("User Direction");
        userDirectionLabel.getStyleClass().add("titled-address-label");

        CheckBox inputActivatedOnceDI=new CheckBox("Input Activated Once");
        CheckBox upDI=new CheckBox("Up");
        CheckBox downDI=new CheckBox("Down");
        CheckBox rightDI=new CheckBox("Right");
        CheckBox leftDI=new CheckBox("Left");

        inputActivatedOnceDI.setSelected((getDirectionalInputs.get(i) & 16L) != 0);
        upDI.setSelected((getDirectionalInputs.get(i) & 32L) != 0);
        downDI.setSelected((getDirectionalInputs.get(i) & 64L) != 0);
        rightDI.setSelected((getDirectionalInputs.get(i) & 128L) != 0);
        leftDI.setSelected((getDirectionalInputs.get(i) & 256L) != 0);

        inputActivatedOnceDI.selectedProperty().addListener((obs,oldValue,newValue)->{
            if(newValue){
                getDirectionalInputs.set(i,getDirectionalInputs.get(i)|16L);
            }
            else{
                getDirectionalInputs.set(i,getDirectionalInputs.get(i)&~16L);
            }
        });
        upDI.selectedProperty().addListener((obs,oldValue,newValue)->{
            if(newValue){
                getDirectionalInputs.set(i,getDirectionalInputs.get(i)|32L);
            }
            else{
                getDirectionalInputs.set(i,getDirectionalInputs.get(i)&~32L);
            }
        });
        downDI.selectedProperty().addListener((obs,oldValue,newValue)->{
            if(newValue){
                getDirectionalInputs.set(i,getDirectionalInputs.get(i)|64L);
            }
            else{
                getDirectionalInputs.set(i,getDirectionalInputs.get(i)&~64L);
            }
        });
        rightDI.selectedProperty().addListener((obs,oldValue,newValue)->{
            if(newValue){
                getDirectionalInputs.set(i,getDirectionalInputs.get(i)|128L);
            }
            else{
                getDirectionalInputs.set(i,getDirectionalInputs.get(i)&~128L);
            }
        });
        leftDI.selectedProperty().addListener((obs,oldValue,newValue)->{
            if(newValue){
                getDirectionalInputs.set(i,getDirectionalInputs.get(i)|256L);
            }
            else{
                getDirectionalInputs.set(i,getDirectionalInputs.get(i)&~256L);
            }
        });
        VBox userDirectionVBox = new VBox(2,inputActivatedOnceDI,upDI,downDI,rightDI,leftDI);

        VBox borderContainerUD=new VBox(userDirectionVBox);
        borderContainerUD.getStyleClass().add("titled-address-box");
        borderContainerUD.setPadding(new Insets(12,0,0,0));

        StackPane userDirectionBoxStackPane = new StackPane();
        userDirectionBoxStackPane.getChildren().addAll(borderContainerUD,userDirectionLabel);

        StackPane.setAlignment(userDirectionLabel, Pos.TOP_LEFT);
        userDirectionLabel.setTranslateY(-8); 
        userDirectionLabel.setTranslateX(10);
        //user direction

        
        //unknown1
        Label unknownDir1Label = new Label("Unknown 1");
        unknownDir1Label.getStyleClass().add("titled-address-label");

        CheckBox unknown10=new CheckBox("Unknown 10");
        CheckBox unknown11=new CheckBox("Unknown 11");
        CheckBox unknown12=new CheckBox("Unknown 12");
        CheckBox unknown13=new CheckBox("Unknown 13");
        CheckBox unknown14=new CheckBox("Unknown 14");

        unknown10.setSelected((getDirectionalInputs.get(i) & 512L) != 0);
        unknown11.setSelected((getDirectionalInputs.get(i) & 1024L) != 0);
        unknown12.setSelected((getDirectionalInputs.get(i) & 2048L) != 0);
        unknown13.setSelected((getDirectionalInputs.get(i) & 4096L) != 0);
        unknown14.setSelected((getDirectionalInputs.get(i) & 8192L) != 0);

        unknown10.selectedProperty().addListener((obs,oldValue,newValue)->{
            if(newValue){
                getDirectionalInputs.set(i,getDirectionalInputs.get(i)|512L);
            }
            else{
                getDirectionalInputs.set(i,getDirectionalInputs.get(i)&~512L);
            }
        });
        unknown11.selectedProperty().addListener((obs,oldValue,newValue)->{
            if(newValue){
                getDirectionalInputs.set(i,getDirectionalInputs.get(i)|1024L);
            }
            else{
                getDirectionalInputs.set(i,getDirectionalInputs.get(i)&~1024L);
            }
        });
        unknown12.selectedProperty().addListener((obs,oldValue,newValue)->{
            if(newValue){
                getDirectionalInputs.set(i,getDirectionalInputs.get(i)|2048L);
            }
            else{
                getDirectionalInputs.set(i,getDirectionalInputs.get(i)&~2048L);
            }
        });
        unknown13.selectedProperty().addListener((obs,oldValue,newValue)->{
            if(newValue){
                getDirectionalInputs.set(i,getDirectionalInputs.get(i)|4096L);
            }
            else{
                getDirectionalInputs.set(i,getDirectionalInputs.get(i)&~4096L);
            }
        });
        unknown14.selectedProperty().addListener((obs,oldValue,newValue)->{
            if(newValue){
                getDirectionalInputs.set(i,getDirectionalInputs.get(i)|8192L);
            }
            else{
                getDirectionalInputs.set(i,getDirectionalInputs.get(i)&~8192L);
            }
        });
        VBox unknownGroup1Box = new VBox(2,unknown10,unknown11,unknown12,unknown13,unknown14);

        VBox borderContainerUG1=new VBox(unknownGroup1Box);
        borderContainerUG1.getStyleClass().add("titled-address-box");
        borderContainerUG1.setPadding(new Insets(12,0,0,0));

        StackPane unknownGroup1BoxStackPane = new StackPane();
        unknownGroup1BoxStackPane.getChildren().addAll(borderContainerUG1,unknownDir1Label);

        StackPane.setAlignment(unknownDir1Label, Pos.TOP_LEFT);
        unknownDir1Label.setTranslateY(-8); 
        unknownDir1Label.setTranslateX(10);
        //unknown1

        //unknown2
        Label unknownDir2Label = new Label("Unknown 2");
        unknownDir2Label.getStyleClass().add("titled-address-label");

        CheckBox unknown15=new CheckBox("Unknown 15");
        CheckBox unknown16=new CheckBox("Unknown 16");
        CheckBox unknown17=new CheckBox("Unknown 17");
        CheckBox unknown18=new CheckBox("Unknown 18");
        CheckBox unknown19=new CheckBox("Unknown 19");

        unknown15.setSelected((getDirectionalInputs.get(i) & 16384L) != 0);
        unknown16.setSelected((getDirectionalInputs.get(i) & 32768L) != 0);
        unknown17.setSelected((getDirectionalInputs.get(i) & 65536L) != 0);
        unknown18.setSelected((getDirectionalInputs.get(i) & 131072L) != 0);
        unknown19.setSelected((getDirectionalInputs.get(i) & 262144L) != 0);
        unknown15.selectedProperty().addListener((obs,oldValue,newValue)->{
            if(newValue){
                getDirectionalInputs.set(i,getDirectionalInputs.get(i)|16384L);
            }
            else{
                getDirectionalInputs.set(i,getDirectionalInputs.get(i)&~16384L);
            }
        });
        unknown16.selectedProperty().addListener((obs,oldValue,newValue)->{
            if(newValue){
                getDirectionalInputs.set(i,getDirectionalInputs.get(i)|32768L);
            }
            else{
                getDirectionalInputs.set(i,getDirectionalInputs.get(i)&~32768L);
            }
        });
        unknown17.selectedProperty().addListener((obs,oldValue,newValue)->{
            if(newValue){
                getDirectionalInputs.set(i,getDirectionalInputs.get(i)|65536L);
            }
            else{
                getDirectionalInputs.set(i,getDirectionalInputs.get(i)&~65536L);
            }
        });
        unknown18.selectedProperty().addListener((obs,oldValue,newValue)->{
            if(newValue){
                getDirectionalInputs.set(i,getDirectionalInputs.get(i)|131072L);
            }
            else{
                getDirectionalInputs.set(i,getDirectionalInputs.get(i)&~131072L);
            }
        });
        unknown19.selectedProperty().addListener((obs,oldValue,newValue)->{
            if(newValue){
                getDirectionalInputs.set(i,getDirectionalInputs.get(i)|262144L);
            }
            else{
                getDirectionalInputs.set(i,getDirectionalInputs.get(i)&~262144L);
            }
        });
        VBox unknownGroup2Box = new VBox(2,unknown15,unknown16,unknown17,unknown18,unknown19);

        VBox borderContainerUG2=new VBox(unknownGroup2Box);
        borderContainerUG2.getStyleClass().add("titled-address-box");
        borderContainerUG2.setPadding(new Insets(12,0,0,0));

        StackPane unknownGroup2BoxStackPane = new StackPane();
        unknownGroup2BoxStackPane.getChildren().addAll(borderContainerUG2,unknownDir2Label);

        StackPane.setAlignment(unknownDir2Label, Pos.TOP_LEFT);
        unknownDir2Label.setTranslateY(-8); 
        unknownDir2Label.setTranslateX(10);
        //unknown2

        //unknown3
        Label unknownDir3Label = new Label("Unknown 3");
        unknownDir3Label.getStyleClass().add("titled-address-label");

        CheckBox unknown20=new CheckBox("Unknown 20");
        CheckBox unknown21=new CheckBox("Unknown 21");
        CheckBox unknown22=new CheckBox("Unknown 22");
        CheckBox unknown23=new CheckBox("Unknown 23");
        CheckBox unknown24=new CheckBox("Unknown 24");

        unknown20.setSelected((getDirectionalInputs.get(i) & 524288L) != 0);
        unknown21.setSelected((getDirectionalInputs.get(i) & 1048576L) != 0);
        unknown22.setSelected((getDirectionalInputs.get(i) & 2097152L) != 0);
        unknown23.setSelected((getDirectionalInputs.get(i) & 4194304L) != 0);
        unknown24.setSelected((getDirectionalInputs.get(i) & 8388608L) != 0);

        unknown20.selectedProperty().addListener((obs,oldValue,newValue)->{
            if(newValue){
                getDirectionalInputs.set(i,getDirectionalInputs.get(i)|524288L);
            }
            else{
                getDirectionalInputs.set(i,getDirectionalInputs.get(i)&~524288L);
            }
        });
        unknown21.selectedProperty().addListener((obs,oldValue,newValue)->{
            if(newValue){
                getDirectionalInputs.set(i,getDirectionalInputs.get(i)|1048576L);
            }
            else{
                getDirectionalInputs.set(i,getDirectionalInputs.get(i)&~1048576L);
            }
        });
        unknown22.selectedProperty().addListener((obs,oldValue,newValue)->{
            if(newValue){
                getDirectionalInputs.set(i,getDirectionalInputs.get(i)|2097152L);
            }
            else{
                getDirectionalInputs.set(i,getDirectionalInputs.get(i)&~2097152L);
            }
        });
        unknown23.selectedProperty().addListener((obs,oldValue,newValue)->{
            if(newValue){
                getDirectionalInputs.set(i,getDirectionalInputs.get(i)|4194304L);
            }
            else{
                getDirectionalInputs.set(i,getDirectionalInputs.get(i)&~4194304L);
            }
        });
        unknown24.selectedProperty().addListener((obs,oldValue,newValue)->{
            if(newValue){
                getDirectionalInputs.set(i,getDirectionalInputs.get(i)|8388608L);
            }
            else{
                getDirectionalInputs.set(i,getDirectionalInputs.get(i)&~8388608L);
            }
        });
        
        VBox unknownGroup3Box = new VBox(2,unknown20,unknown21,unknown22,unknown23,unknown24);

        VBox borderContainerUG3=new VBox(unknownGroup3Box);
        borderContainerUG3.getStyleClass().add("titled-address-box");
        borderContainerUG3.setPadding(new Insets(12,0,0,0));

        StackPane unknownGroup3BoxStackPane = new StackPane();
        unknownGroup3BoxStackPane.getChildren().addAll(borderContainerUG3,unknownDir3Label);

        StackPane.setAlignment(unknownDir3Label, Pos.TOP_LEFT);
        unknownDir3Label.setTranslateY(-8); 
        unknownDir3Label.setTranslateX(10);
        //unknown3

        //unknown4
        Label unknownDir4Label = new Label("Unknown 4");
        unknownDir4Label.getStyleClass().add("titled-address-label");

        CheckBox unknown25=new CheckBox("Unknown 25");
        CheckBox unknown26=new CheckBox("Unknown 26");
        CheckBox unknown27=new CheckBox("Unknown 27");
        CheckBox unknown28=new CheckBox("Unknown 28");
        CheckBox unknown29=new CheckBox("Unknown 29");

        unknown25.setSelected((getDirectionalInputs.get(i) & 16777216L) != 0);
        unknown26.setSelected((getDirectionalInputs.get(i) & 33554432L) != 0);
        unknown27.setSelected((getDirectionalInputs.get(i) & 67108864L) != 0);
        unknown28.setSelected((getDirectionalInputs.get(i) & 134217728L) != 0);
        unknown29.setSelected((getDirectionalInputs.get(i) & 268435456L) != 0);

        unknown25.selectedProperty().addListener((obs,oldValue,newValue)->{
            if(newValue){
                getDirectionalInputs.set(i,getDirectionalInputs.get(i)|16777216L);
            }
            else{
                getDirectionalInputs.set(i,getDirectionalInputs.get(i)&~16777216L);
            }
        });
        unknown26.selectedProperty().addListener((obs,oldValue,newValue)->{
            if(newValue){
                getDirectionalInputs.set(i,getDirectionalInputs.get(i)|33554432L);
            }
            else{
                getDirectionalInputs.set(i,getDirectionalInputs.get(i)&~33554432L);
            }
        });
        unknown27.selectedProperty().addListener((obs,oldValue,newValue)->{
            if(newValue){
                getDirectionalInputs.set(i,getDirectionalInputs.get(i)|67108864L);
            }
            else{
                getDirectionalInputs.set(i,getDirectionalInputs.get(i)&~67108864L);
            }
        });
        unknown28.selectedProperty().addListener((obs,oldValue,newValue)->{
            if(newValue){
                getDirectionalInputs.set(i,getDirectionalInputs.get(i)|134217728L);
            }
            else{
                getDirectionalInputs.set(i,getDirectionalInputs.get(i)&~134217728L);
            }
        });
        unknown29.selectedProperty().addListener((obs,oldValue,newValue)->{
            if(newValue){
                getDirectionalInputs.set(i,getDirectionalInputs.get(i)|268435456L);
            }
            else{
                getDirectionalInputs.set(i,getDirectionalInputs.get(i)&~268435456L);
            }
        });
        VBox unknownGroup4Box = new VBox(2,unknown25,unknown26,unknown27,unknown28,unknown29);

        VBox borderContainerUG4=new VBox(unknownGroup4Box);
        borderContainerUG4.getStyleClass().add("titled-address-box");
        borderContainerUG4.setPadding(new Insets(12,0,0,0));

        StackPane unknownGroup4BoxStackPane = new StackPane();
        unknownGroup4BoxStackPane.getChildren().addAll(borderContainerUG4,unknownDir4Label);

        StackPane.setAlignment(unknownDir4Label, Pos.TOP_LEFT);
        unknownDir4Label.setTranslateY(-8); 
        unknownDir4Label.setTranslateX(10);
        //unknown4

        //unknown5
        Label unknownDir5Label = new Label("Unknown 5");
        unknownDir5Label.getStyleClass().add("titled-address-label");

        CheckBox unknown30=new CheckBox("Unknown 30");
        CheckBox unknown31=new CheckBox("Unknown 31");
        CheckBox unknown32=new CheckBox("Unknown 32");

        unknown30.setSelected((getDirectionalInputs.get(i) & 536870912L) != 0);
        unknown31.setSelected((getDirectionalInputs.get(i) & 1073741824L) != 0);
        unknown32.setSelected((getDirectionalInputs.get(i) & 2147483648L) != 0);

        unknown30.selectedProperty().addListener((obs,oldValue,newValue)->{
            if(newValue){
                getDirectionalInputs.set(i,getDirectionalInputs.get(i)|536870912L);
            }
            else{
                getDirectionalInputs.set(i,getDirectionalInputs.get(i)&~536870912L);
            }
        });
        unknown31.selectedProperty().addListener((obs,oldValue,newValue)->{
            if(newValue){
                getDirectionalInputs.set(i,getDirectionalInputs.get(i)|1073741824L);
            }
            else{
                getDirectionalInputs.set(i,getDirectionalInputs.get(i)&~1073741824L);
            }
        });
        unknown32.selectedProperty().addListener((obs,oldValue,newValue)->{
            if(newValue){
                getDirectionalInputs.set(i,getDirectionalInputs.get(i)|2147483648L);
            }
            else{
                getDirectionalInputs.set(i,getDirectionalInputs.get(i)&~2147483648L);
            }
        });
        VBox unknownGroup5Box = new VBox(2,unknown30,unknown31,unknown32);

        VBox borderContainerUG5=new VBox(unknownGroup5Box);
        borderContainerUG5.getStyleClass().add("titled-address-box");
        borderContainerUG5.setPadding(new Insets(12,0,0,0));

        StackPane unknownGroup5BoxStackPane = new StackPane();
        unknownGroup5BoxStackPane.getChildren().addAll(borderContainerUG5,unknownDir5Label);

        StackPane.setAlignment(unknownDir5Label, Pos.TOP_LEFT);
        unknownDir5Label.setTranslateY(-8); 
        unknownDir5Label.setTranslateX(10);
        
        //unknown5
        directionalInputHBox.setAlignment(Pos.CENTER_LEFT);
        directionalInputHBox.getChildren().addAll(directionalInputLabel,relativeDirectionBoxStackPane,userDirectionBoxStackPane,unknownGroup1BoxStackPane,unknownGroup2BoxStackPane,unknownGroup3BoxStackPane,unknownGroup4BoxStackPane,unknownGroup5BoxStackPane);
        //directional input 
        
        //button input
        HBox buttonInputHBox=new HBox(40);
        buttonInputHBox.setPadding(new Insets(20,0,0,8));

        Label buttonInputLabel = new Label("Button Input");

        GridPane buttonInputGridPane=new GridPane();
        buttonInputGridPane.setVgap(10);
        buttonInputGridPane.setHgap(10);

        //0x1
        Label hexDigit0Label = new Label("0x1");
        hexDigit0Label.getStyleClass().add("titled-address-label");

        CheckBox light = new CheckBox("Light");
        CheckBox heavy = new CheckBox("Heavy");
        CheckBox blast = new CheckBox("Blast"); 
        CheckBox jump = new CheckBox("Jump");

        light.setSelected((getButtonInputs.get(i) & 1L) != 0);
        heavy.setSelected((getButtonInputs.get(i) & 2L) != 0);
        blast.setSelected((getButtonInputs.get(i) & 4L) != 0);
        jump.setSelected((getButtonInputs.get(i) & 8L) != 0);

        // 0x1 Group
        light.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) { getButtonInputs.set(i, getButtonInputs.get(i) | 1L); }
            else { getButtonInputs.set(i, getButtonInputs.get(i) & ~1L); }
        });
        heavy.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) { getButtonInputs.set(i, getButtonInputs.get(i) | 2L); }
            else { getButtonInputs.set(i, getButtonInputs.get(i) & ~2L); }
        });
        blast.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) { getButtonInputs.set(i, getButtonInputs.get(i) | 4L); }
            else { getButtonInputs.set(i, getButtonInputs.get(i) & ~4L); }
        });
        jump.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) { getButtonInputs.set(i, getButtonInputs.get(i) | 8L); }
            else { getButtonInputs.set(i, getButtonInputs.get(i) & ~8L); }
        });
        VBox hexDigit0Box = new VBox(2, light, heavy, blast, jump);

        VBox borderContainerHexDigit0 = new VBox(hexDigit0Box);
        borderContainerHexDigit0.getStyleClass().add("titled-address-box");
        borderContainerHexDigit0.setPadding(new Insets(12, 0, 0, 0));

        StackPane hexDigit0StackPane = new StackPane();
        hexDigit0StackPane.getChildren().addAll(borderContainerHexDigit0, hexDigit0Label);

        StackPane.setAlignment(hexDigit0Label, Pos.TOP_LEFT);
        hexDigit0Label.setTranslateY(-8); 
        hexDigit0Label.setTranslateX(10);
        buttonInputGridPane.add(hexDigit0StackPane, 0, 0); // Position 0
        //0x1

        //0x10
        Label hexDigit1Label = new Label("0x10");
        hexDigit1Label.getStyleClass().add("titled-address-label");

        CheckBox skillMenu = new CheckBox("Skill Menu");
        CheckBox boost = new CheckBox("Boost");
        CheckBox guard = new CheckBox("Guard");
        CheckBox unknown8 = new CheckBox("Unknown 8");

        skillMenu.setSelected((getButtonInputs.get(i) & 16L) != 0);
        boost.setSelected((getButtonInputs.get(i) & 32L) != 0);
        guard.setSelected((getButtonInputs.get(i) & 64L) != 0);
        unknown8.setSelected((getButtonInputs.get(i) & 128L) != 0);

        skillMenu.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getButtonInputs.set(i, getButtonInputs.get(i) | 16L);
            } else {
                getButtonInputs.set(i, getButtonInputs.get(i) & ~16L);
            }
        });
        boost.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getButtonInputs.set(i, getButtonInputs.get(i) | 32L);
            } else {
                getButtonInputs.set(i, getButtonInputs.get(i) & ~32L);
            }
        });
        guard.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getButtonInputs.set(i, getButtonInputs.get(i) | 64L);
            } else {
                getButtonInputs.set(i, getButtonInputs.get(i) & ~64L);
            }
        });
        unknown8.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getButtonInputs.set(i, getButtonInputs.get(i) | 128L);
            } else {
                getButtonInputs.set(i, getButtonInputs.get(i) & ~128L);
            }
        });
        VBox hexDigit1Box = new VBox(2, skillMenu, boost, guard, unknown8);

        VBox borderContainerHexDigit1 = new VBox(hexDigit1Box);
        borderContainerHexDigit1.getStyleClass().add("titled-address-box");
        borderContainerHexDigit1.setPadding(new Insets(12, 0, 0, 0));

        StackPane hexDigit1StackPane = new StackPane();
        hexDigit1StackPane.getChildren().addAll(borderContainerHexDigit1, hexDigit1Label);

        StackPane.setAlignment(hexDigit1Label, Pos.TOP_LEFT);
        hexDigit1Label.setTranslateY(-8); 
        hexDigit1Label.setTranslateX(10);
        buttonInputGridPane.add(hexDigit1StackPane, 1, 0); // Position 1
        //0x10

        //0x100
        Label hexDigit2Label = new Label("0x100");
        hexDigit2Label.getStyleClass().add("titled-address-label");

        CheckBox superSkill1 = new CheckBox("Super Skill 1");
        CheckBox superSkill2 = new CheckBox("Super Skill 2");
        CheckBox superSkill3 = new CheckBox("Super Skill 3");
        CheckBox superSkill4 = new CheckBox("Super Skill 4");

        superSkill1.setSelected((getButtonInputs.get(i) & 256L) != 0);
        superSkill2.setSelected((getButtonInputs.get(i) & 512L) != 0);
        superSkill3.setSelected((getButtonInputs.get(i) & 1024L) != 0);
        superSkill4.setSelected((getButtonInputs.get(i) & 2048L) != 0);

        superSkill1.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getButtonInputs.set(i, getButtonInputs.get(i) | 256L);
            } else {
                getButtonInputs.set(i, getButtonInputs.get(i) & ~256L);
            }
        });
        superSkill2.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getButtonInputs.set(i, getButtonInputs.get(i) | 512L);
            } else {
                getButtonInputs.set(i, getButtonInputs.get(i) & ~512L);
            }
        });
        superSkill3.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getButtonInputs.set(i, getButtonInputs.get(i) | 1024L);
            } else {
                getButtonInputs.set(i, getButtonInputs.get(i) & ~1024L);
            }
        });
        superSkill4.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getButtonInputs.set(i, getButtonInputs.get(i) | 2048L);
            } else {
                getButtonInputs.set(i, getButtonInputs.get(i) & ~2048L);
            }
        });
        VBox hexDigit2Box = new VBox(2, superSkill1, superSkill2, superSkill3, superSkill4);

        VBox borderContainerHexDigit2 = new VBox(hexDigit2Box);
        borderContainerHexDigit2.getStyleClass().add("titled-address-box");
        borderContainerHexDigit2.setPadding(new Insets(12, 0, 0, 0));

        StackPane hexDigit2StackPane = new StackPane();
        hexDigit2StackPane.getChildren().addAll(borderContainerHexDigit2, hexDigit2Label);

        StackPane.setAlignment(hexDigit2Label, Pos.TOP_LEFT);
        hexDigit2Label.setTranslateY(-8); 
        hexDigit2Label.setTranslateX(10);
        buttonInputGridPane.add(hexDigit2StackPane, 2, 0); // Position 2
        //0x100

        //0x1000
        Label hexDigit3Label = new Label("0x1000");
        hexDigit3Label.getStyleClass().add("titled-address-label");

        CheckBox ultimateSkill1 = new CheckBox("Ultimate Skill 1");
        CheckBox ultimateSkill2 = new CheckBox("Ultimate Skill 2");
        CheckBox awokenSkill = new CheckBox("Awoken Skill");
        CheckBox evasiveSkill = new CheckBox("Evasive Skill");

        ultimateSkill1.setSelected((getButtonInputs.get(i) & 4096L) != 0);
        ultimateSkill2.setSelected((getButtonInputs.get(i) & 8192L) != 0);
        awokenSkill.setSelected((getButtonInputs.get(i) & 16384L) != 0);
        evasiveSkill.setSelected((getButtonInputs.get(i) & 32768L) != 0);

        ultimateSkill1.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getButtonInputs.set(i, getButtonInputs.get(i) | 4096L);
            } else {
                getButtonInputs.set(i, getButtonInputs.get(i) & ~4096L);
            }
        });
        ultimateSkill2.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getButtonInputs.set(i, getButtonInputs.get(i) | 8192L);
            } else {
                getButtonInputs.set(i, getButtonInputs.get(i) & ~8192L);
            }
        });
        awokenSkill.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getButtonInputs.set(i, getButtonInputs.get(i) | 16384L);
            } else {
                getButtonInputs.set(i, getButtonInputs.get(i) & ~16384L);
            }
        });
        evasiveSkill.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getButtonInputs.set(i, getButtonInputs.get(i) | 32768L);
            } else {
                getButtonInputs.set(i, getButtonInputs.get(i) & ~32768L);
            }
        });
        VBox hexDigit3Box = new VBox(2, ultimateSkill1, ultimateSkill2, awokenSkill, evasiveSkill);

        VBox borderContainerHexDigit3 = new VBox(hexDigit3Box);
        borderContainerHexDigit3.getStyleClass().add("titled-address-box");
        borderContainerHexDigit3.setPadding(new Insets(12, 0, 0, 0));

        StackPane hexDigit3StackPane = new StackPane();
        hexDigit3StackPane.getChildren().addAll(borderContainerHexDigit3, hexDigit3Label);

        StackPane.setAlignment(hexDigit3Label, Pos.TOP_LEFT);
        hexDigit3Label.setTranslateY(-8); 
        hexDigit3Label.setTranslateX(10);
        buttonInputGridPane.add(hexDigit3StackPane, 3, 0); // Position 3
        //0x1000

        //0x10000
        Label hexDigit4Label = new Label("0x10000");
        hexDigit4Label.getStyleClass().add("titled-address-label");

        CheckBox skillInput = new CheckBox("Skill Input");
        CheckBox superMenuPlusSkillInput = new CheckBox("Super Menu + Skill Input");
        CheckBox ultimateMenuPlusSkillInput = new CheckBox("Ultimate Menu+Skill Input");
        CheckBox unknown20Button = new CheckBox("Unknown 20");

        skillInput.setSelected((getButtonInputs.get(i) & 65536L) != 0);
        superMenuPlusSkillInput.setSelected((getButtonInputs.get(i) & 131072L) != 0);
        ultimateMenuPlusSkillInput.setSelected((getButtonInputs.get(i) & 262144L) != 0);
        unknown20Button.setSelected((getButtonInputs.get(i) & 524288L) != 0);

        skillInput.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getButtonInputs.set(i, getButtonInputs.get(i) | 65536L);
            } else {
                getButtonInputs.set(i, getButtonInputs.get(i) & ~65536L);
            }
        });
        superMenuPlusSkillInput.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getButtonInputs.set(i, getButtonInputs.get(i) | 131072L);
            } else {
                getButtonInputs.set(i, getButtonInputs.get(i) & ~131072L);
            }
        });
        ultimateMenuPlusSkillInput.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getButtonInputs.set(i, getButtonInputs.get(i) | 262144L);
            } else {
                getButtonInputs.set(i, getButtonInputs.get(i) & ~262144L);
            }
        });
        unknown20Button.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getButtonInputs.set(i, getButtonInputs.get(i) | 524288L);
            } else {
                getButtonInputs.set(i, getButtonInputs.get(i) & ~524288L);
            }
        });
        VBox hexDigit4Box = new VBox(2, skillInput, superMenuPlusSkillInput, ultimateMenuPlusSkillInput, unknown20Button);

        VBox borderContainerHexDigit4 = new VBox(hexDigit4Box);
        borderContainerHexDigit4.getStyleClass().add("titled-address-box");
        borderContainerHexDigit4.setPadding(new Insets(12, 0, 0, 0));

        StackPane hexDigit4StackPane = new StackPane();
        hexDigit4StackPane.getChildren().addAll(borderContainerHexDigit4, hexDigit4Label);

        StackPane.setAlignment(hexDigit4Label, Pos.TOP_LEFT);
        hexDigit4Label.setTranslateY(-8); 
        hexDigit4Label.setTranslateX(10);
        buttonInputGridPane.add(hexDigit4StackPane, 0, 1); // Row 2, Position 0
        //0x10000

        //0x100000
        Label hexDigit5Label = new Label("0x100000");
        hexDigit5Label.getStyleClass().add("titled-address-label");

        CheckBox lockedON=new CheckBox("Locked On");
        CheckBox descend=new CheckBox("Descend");
        CheckBox dragonRadar=new CheckBox("Dragon Radar");
        CheckBox jump2=new CheckBox("Jump 2");

        lockedON.setSelected((getButtonInputs.get(i) & 1048576L) != 0);
        descend.setSelected((getButtonInputs.get(i) & 2097152L) != 0);
        dragonRadar.setSelected((getButtonInputs.get(i) & 4194304L) != 0);
        jump2.setSelected((getButtonInputs.get(i) & 8388608L) != 0);

        lockedON.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getButtonInputs.set(i, getButtonInputs.get(i) | 1048576L);
            } else {
                getButtonInputs.set(i, getButtonInputs.get(i) & ~1048576L);
            }
        });
        descend.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getButtonInputs.set(i, getButtonInputs.get(i) | 2097152L);
            } else {
                getButtonInputs.set(i, getButtonInputs.get(i) & ~2097152L);
            }
        });
        dragonRadar.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getButtonInputs.set(i, getButtonInputs.get(i) | 4194304L);
            } else {
                getButtonInputs.set(i, getButtonInputs.get(i) & ~4194304L);
            }
        });
        jump2.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getButtonInputs.set(i, getButtonInputs.get(i) | 8388608L);
            } else {
                getButtonInputs.set(i, getButtonInputs.get(i) & ~8388608L);
            }
        });
        VBox hexDigit5Box = new VBox(2, lockedON,descend,dragonRadar,jump2);

        VBox borderContainerHexDigit5 = new VBox(hexDigit5Box);
        borderContainerHexDigit5.getStyleClass().add("titled-address-box");
        borderContainerHexDigit5.setPadding(new Insets(12, 0, 0, 0));

        StackPane hexDigit5StackPane = new StackPane();
        hexDigit5StackPane.getChildren().addAll(borderContainerHexDigit5, hexDigit5Label);

        StackPane.setAlignment(hexDigit5Label, Pos.TOP_LEFT);
        hexDigit5Label.setTranslateY(-8); 
        hexDigit5Label.setTranslateX(10);
        buttonInputGridPane.add(hexDigit5StackPane, 1, 1); 
        //0x100000

        //0x1000000
        Label hexDigit6Label = new Label("0x1000000");
        hexDigit6Label.getStyleClass().add("titled-address-label");
        CheckBox ultimateMenu=new CheckBox("Ultimate Menu");
        CheckBox unknown26ButtonInput=new CheckBox("Unknown 26");
        CheckBox unknown27ButtonInput=new CheckBox("Unknown 27");
        CheckBox unknown28ButtonInput=new CheckBox("Unknown 28");

        ultimateMenu.setSelected((getButtonInputs.get(i) & 16777216L) != 0);
        unknown26ButtonInput.setSelected((getButtonInputs.get(i) & 33554432L) != 0);
        unknown27ButtonInput.setSelected((getButtonInputs.get(i) & 67108864L) != 0);
        unknown28ButtonInput.setSelected((getButtonInputs.get(i) & 134217728L) != 0);

        ultimateMenu.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getButtonInputs.set(i, getButtonInputs.get(i) | 16777216L);
            } else {
                getButtonInputs.set(i, getButtonInputs.get(i) & ~16777216L);
            }
        });
        unknown26ButtonInput.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getButtonInputs.set(i, getButtonInputs.get(i) | 33554432L);
            } else {
                getButtonInputs.set(i, getButtonInputs.get(i) & ~33554432L);
            }
        });
        unknown27ButtonInput.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getButtonInputs.set(i, getButtonInputs.get(i) | 67108864L);
            } else {
                getButtonInputs.set(i, getButtonInputs.get(i) & ~67108864L);
            }
        });
        unknown28ButtonInput.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getButtonInputs.set(i, getButtonInputs.get(i) | 134217728L);
            } else {
                getButtonInputs.set(i, getButtonInputs.get(i) & ~134217728L);
            }
        });

        VBox hexDigit6Box = new VBox(2,ultimateMenu,unknown26ButtonInput,unknown27ButtonInput,unknown28ButtonInput);

        VBox borderContainerHexDigit6=new VBox(hexDigit6Box);
        borderContainerHexDigit6.getStyleClass().add("titled-address-box");
        borderContainerHexDigit6.setPadding(new Insets(12,0,0,0));

        StackPane hexDigit6StackPane = new StackPane();
        hexDigit6StackPane.getChildren().addAll(borderContainerHexDigit6,hexDigit6Label);

        StackPane.setAlignment(hexDigit6Label, Pos.TOP_LEFT);
        hexDigit6Label.setTranslateY(-8); 
        hexDigit6Label.setTranslateX(10);
        buttonInputGridPane.add(hexDigit6StackPane, 2,1);
        //0x1000000

        //0x10000000
        Label hexDigit7Label = new Label("0x10000000");
        hexDigit7Label.getStyleClass().add("titled-address-label");

        CheckBox ultimateMenu2=new CheckBox("Ultimate Menu 2");
        CheckBox unknown30ButtonInput=new CheckBox("Unknown 30");
        CheckBox unknown31ButtonInput=new CheckBox("Unknown 31");
        CheckBox unknown32ButtonInput=new CheckBox("Unknown 32");

        ultimateMenu2.setSelected((getButtonInputs.get(i) & 268435456L) != 0);
        unknown30ButtonInput.setSelected((getButtonInputs.get(i) & 536870912L) != 0);
        unknown31ButtonInput.setSelected((getButtonInputs.get(i) & 1073741824L) != 0);
        unknown32ButtonInput.setSelected((getButtonInputs.get(i) & 2147483648L) != 0);

        ultimateMenu2.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getButtonInputs.set(i, getButtonInputs.get(i) | 268435456L);
            } else {
                getButtonInputs.set(i, getButtonInputs.get(i) & ~268435456L);
            }
        });
        unknown30ButtonInput.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getButtonInputs.set(i, getButtonInputs.get(i) | 536870912L);
            } else {
                getButtonInputs.set(i, getButtonInputs.get(i) & ~536870912L);
            }
        });
        unknown31ButtonInput.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getButtonInputs.set(i, getButtonInputs.get(i) | 1073741824L);
            } else {
                getButtonInputs.set(i, getButtonInputs.get(i) & ~1073741824L);
            }
        });
        unknown32ButtonInput.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getButtonInputs.set(i, getButtonInputs.get(i) | 2147483648L);
            } else {
                getButtonInputs.set(i, getButtonInputs.get(i) & ~2147483648L);
            }
        });

        VBox hexDigit7Box = new VBox(2,ultimateMenu2,unknown30ButtonInput,unknown31ButtonInput,unknown32ButtonInput);

        VBox borderContainerhexDigit7=new VBox(hexDigit7Box);
        borderContainerhexDigit7.getStyleClass().add("titled-address-box");
        borderContainerhexDigit7.setPadding(new Insets(12,0,0,0));

        StackPane hexDigit7BoxStackPane = new StackPane();
        hexDigit7BoxStackPane.getChildren().addAll(borderContainerhexDigit7,hexDigit7Label);

        StackPane.setAlignment(hexDigit7Label, Pos.TOP_LEFT);
        hexDigit7Label.setTranslateY(-8); 
        hexDigit7Label.setTranslateX(10);
        buttonInputGridPane.add(hexDigit7BoxStackPane, 3, 1);
        //0x10000000
        buttonInputHBox.setAlignment(Pos.CENTER_LEFT);;
        buttonInputHBox.getChildren().addAll(buttonInputLabel,buttonInputGridPane);
        //button input

        //hold down conditions
        HBox holdDownConditionsHBox =new HBox(40);
        holdDownConditionsHBox.setPadding(new Insets(20,0,0,8));
        

        GridPane holdDownConditionsGridPane=new GridPane();
        holdDownConditionsGridPane.setHgap(10);
        holdDownConditionsGridPane.setVgap(8);
  
        Label holdDownConditionslabel = new Label("Hold Down \nConditions");
        
        //Action
        Label actionLabel = new Label("Action");
        actionLabel.getStyleClass().add("titled-address-label");

        CheckBox continueUntilReleased = new CheckBox("Continue Until Released");
        CheckBox delayUntilReleased = new CheckBox("Delay Until Released");
        CheckBox unknown2HDC = new CheckBox("Unknown 2");
        CheckBox stopSkillFromActivating = new CheckBox("Stop Skill From Activating");
        CheckBox unknown4HDC = new CheckBox("Unknown 4");

        continueUntilReleased.setSelected(true); 
        delayUntilReleased.setSelected((getHoldDownConditions.get(i) & 1L) != 0);       
        unknown2HDC.setSelected((getHoldDownConditions.get(i) & 2L) != 0);              
        stopSkillFromActivating.setSelected((getHoldDownConditions.get(i) & 4L) != 0);  
        unknown4HDC.setSelected((getHoldDownConditions.get(i) & 8L) != 0);

        if(delayUntilReleased.isSelected()||unknown2HDC.isSelected()||stopSkillFromActivating.isSelected()||unknown4HDC.isSelected()){
            continueUntilReleased.setSelected(false);

        }

        continueUntilReleased.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if(delayUntilReleased.isSelected()||unknown2HDC.isSelected()||stopSkillFromActivating.isSelected()||unknown4HDC.isSelected()){
                continueUntilReleased.setSelected(false);

            }
            else{
                continueUntilReleased.setSelected(true);
            }
            
        });
        delayUntilReleased.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getHoldDownConditions.set(i, getHoldDownConditions.get(i) | 1L);
            } else {
                getHoldDownConditions.set(i, getHoldDownConditions.get(i) & ~1L);
            }
        });
        unknown2HDC.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getHoldDownConditions.set(i, getHoldDownConditions.get(i) | 2L);
            } else {
                getHoldDownConditions.set(i, getHoldDownConditions.get(i) & ~2L);
            }
        });
        
        stopSkillFromActivating.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getHoldDownConditions.set(i, getHoldDownConditions.get(i) | 4L);
            } else {
                getHoldDownConditions.set(i, getHoldDownConditions.get(i) & ~4L);
            }
        });
        unknown4HDC.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getHoldDownConditions.set(i, getHoldDownConditions.get(i) | 8L);
            } else {
                getHoldDownConditions.set(i, getHoldDownConditions.get(i) & ~8L);
            }
        });

        VBox actionBox = new VBox(2, continueUntilReleased, delayUntilReleased, unknown2HDC, stopSkillFromActivating,unknown4HDC);

        VBox borderContainerAction = new VBox(actionBox);
        borderContainerAction.getStyleClass().add("titled-address-box");
        borderContainerAction.setPadding(new Insets(12, 0, 0, 0));

        StackPane actionBoxStackPane = new StackPane();
        actionBoxStackPane.getChildren().addAll(borderContainerAction, actionLabel);
        
        actionBox.addEventHandler(ActionEvent.ACTION,event->{
            if(delayUntilReleased.isSelected()|| unknown2HDC.isSelected()||stopSkillFromActivating.isSelected()||unknown4HDC.isSelected()){
                continueUntilReleased.setSelected(false);
            }
            else{
                continueUntilReleased.setSelected(true);
            }
        });

        StackPane.setAlignment(actionLabel, Pos.TOP_LEFT);
        actionLabel.setTranslateY(-8); 
        actionLabel.setTranslateX(10);
        holdDownConditionsGridPane.add(actionBoxStackPane,0,0);
        //Action
        //Option 2
        Label option2Label = new Label("Option 2");
        option2Label.getStyleClass().add("titled-address-label");

        CheckBox unknown5HDC = new CheckBox("Unknown 5");
        CheckBox unknown6HDC = new CheckBox("Unknown 6");
        CheckBox unknown7HDC = new CheckBox("Unknown 7");
        CheckBox unknown8HDC = new CheckBox("Unknown 8");

        unknown5HDC.setSelected((getHoldDownConditions.get(i) & 16L) != 0);   
        unknown6HDC.setSelected((getHoldDownConditions.get(i) & 32L) != 0);      
        unknown7HDC.setSelected((getHoldDownConditions.get(i) & 64L) != 0);       
        unknown8HDC.setSelected((getHoldDownConditions.get(i) & 128L) != 0);     

        unknown5HDC.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getHoldDownConditions.set(i, getHoldDownConditions.get(i) | 16L);
            } else {
                getHoldDownConditions.set(i, getHoldDownConditions.get(i) & ~16L);
            }
        });

        unknown6HDC.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getHoldDownConditions.set(i, getHoldDownConditions.get(i) | 32L);
            } else {
                getHoldDownConditions.set(i, getHoldDownConditions.get(i) & ~32L);
            }
        });

        unknown7HDC.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getHoldDownConditions.set(i, getHoldDownConditions.get(i) | 64L);
            } else {
                getHoldDownConditions.set(i, getHoldDownConditions.get(i) & ~64L);
            }
        });

        unknown8HDC.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getHoldDownConditions.set(i, getHoldDownConditions.get(i) | 128L);
            } else {
                getHoldDownConditions.set(i, getHoldDownConditions.get(i) & ~128L);
            }
        });

        VBox option2Box = new VBox(2, unknown5HDC,unknown6HDC, unknown7HDC, unknown8HDC);

        VBox borderContainerOption2 = new VBox(option2Box);
        borderContainerOption2.getStyleClass().add("titled-address-box");
        borderContainerOption2.setPadding(new Insets(12, 0, 0, 0));

        StackPane option2BoxStackPane = new StackPane();
        option2BoxStackPane.getChildren().addAll(borderContainerOption2, option2Label);

        StackPane.setAlignment(option2Label, Pos.TOP_LEFT);
        option2Label.setTranslateY(-8); 
        option2Label.setTranslateX(10);
        holdDownConditionsGridPane.add(option2BoxStackPane,1,0);
        //Options 2

        //Options 3
        Label option3Label = new Label("Option 3");
        option3Label.getStyleClass().add("titled-address-label");

        CheckBox unknown9HDC = new CheckBox("Unknown 9");
        CheckBox unknown10HDC = new CheckBox("Unknown 10");
        CheckBox unknown11HDC = new CheckBox("Unknown 11");
        CheckBox unknown12HDC = new CheckBox("Unknown 12");

        unknown9HDC.setSelected((getHoldDownConditions.get(i) & 256L) != 0);      // Bit 8  (256)
        unknown10HDC.setSelected((getHoldDownConditions.get(i) & 512L) != 0);     // Bit 9  (512)
        unknown11HDC.setSelected((getHoldDownConditions.get(i) & 1024L) != 0);    // Bit 10 (1024)
        unknown12HDC.setSelected((getHoldDownConditions.get(i) & 2048L) != 0);    
   

        unknown9HDC.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getHoldDownConditions.set(i, getHoldDownConditions.get(i) | 256L);
            } else {
                getHoldDownConditions.set(i, getHoldDownConditions.get(i) & ~256L);
            }
        });
        unknown10HDC.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getHoldDownConditions.set(i, getHoldDownConditions.get(i) | 512L);
            } else {
                getHoldDownConditions.set(i, getHoldDownConditions.get(i) & ~512L);
            }
        });
        unknown11HDC.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getHoldDownConditions.set(i, getHoldDownConditions.get(i) | 1024L);
            } else {
                getHoldDownConditions.set(i, getHoldDownConditions.get(i) & ~1024L);
            }
        });
        unknown12HDC.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getHoldDownConditions.set(i, getHoldDownConditions.get(i) | 2048L);
            } else {
                getHoldDownConditions.set(i, getHoldDownConditions.get(i) & ~2048L);
            }
        });

        VBox option3Box = new VBox(2,unknown9HDC, unknown10HDC,unknown11HDC,unknown12HDC);

        VBox borderContainerOption3 = new VBox(option3Box);
        borderContainerOption3.getStyleClass().add("titled-address-box");
        borderContainerOption3.setPadding(new Insets(12, 0, 0, 0));

        StackPane option3BoxStackPane = new StackPane();
        option3BoxStackPane.getChildren().addAll(borderContainerOption3, option3Label);

        StackPane.setAlignment(option3Label, Pos.TOP_LEFT);
        option3Label.setTranslateY(-8); 
        option3Label.setTranslateX(10);
        holdDownConditionsGridPane.add(option3BoxStackPane,2,0);
        //Options 3

        //Options 4
        Label option4Label = new Label("Option 4");
        option4Label.getStyleClass().add("titled-address-label");

        CheckBox unknown13HDC = new CheckBox("Unknown 13");
        CheckBox unknown14HDC = new CheckBox("Unknown 14");
        CheckBox unknown15HDC = new CheckBox("Unknown 15");
        CheckBox unknown16HDC = new CheckBox("Unknown 16");

        unknown13HDC.setSelected((getHoldDownConditions.get(i) & 4096L) != 0);    
        unknown14HDC.setSelected((getHoldDownConditions.get(i) & 8192L) != 0);   
        unknown15HDC.setSelected((getHoldDownConditions.get(i) & 16384L) != 0);   
        unknown16HDC.setSelected((getHoldDownConditions.get(i) & 32768L) != 0);   

        unknown13HDC.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getHoldDownConditions.set(i, getHoldDownConditions.get(i) | 4096L);
            } else {
                getHoldDownConditions.set(i, getHoldDownConditions.get(i) & ~4096L);
            }
        });
        unknown14HDC.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getHoldDownConditions.set(i, getHoldDownConditions.get(i) | 8192L);
            } else {
                getHoldDownConditions.set(i, getHoldDownConditions.get(i) & ~8192L);
            }
        });
        unknown15HDC.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getHoldDownConditions.set(i, getHoldDownConditions.get(i) | 16384L);
            } else {
                getHoldDownConditions.set(i, getHoldDownConditions.get(i) & ~16384L);
            }
        });
        unknown16HDC.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getHoldDownConditions.set(i, getHoldDownConditions.get(i) | 32768L);
            } else {
                getHoldDownConditions.set(i, getHoldDownConditions.get(i) & ~32768L);
            }
        });
        VBox option4Box = new VBox(2,unknown13HDC,unknown14HDC,unknown15HDC,unknown16HDC);

        VBox borderContainerOption4 = new VBox(option4Box);
        borderContainerOption4.getStyleClass().add("titled-address-box");
        borderContainerOption4.setPadding(new Insets(12, 0, 0, 0));

        StackPane option4BoxStackPane = new StackPane();
        option4BoxStackPane.getChildren().addAll(borderContainerOption4, option4Label);

        StackPane.setAlignment(option4Label, Pos.TOP_LEFT);
        option4Label.setTranslateY(-8); 
        option4Label.setTranslateX(10);
        holdDownConditionsGridPane.add(option4BoxStackPane,3,0);
        //Options 4

        //ChargeType
        Label chargeTypeLabel = new Label("Charge Type");
        chargeTypeLabel.getStyleClass().add("titled-address-label");

        CheckBox automatic = new CheckBox("Automatic");
        CheckBox manual = new CheckBox("Manual");
        CheckBox unknown18HDC = new CheckBox("Unknown 18");
        CheckBox unknown19HDC = new CheckBox("Unknown 19");
        CheckBox unknown20HDC = new CheckBox("Unknown 20");
        automatic.setSelected(true);

        manual.setSelected((getHoldDownConditions.get(i) & 65536L) != 0);   
        unknown18HDC.setSelected((getHoldDownConditions.get(i) & 131072L) != 0);  
        unknown19HDC.setSelected((getHoldDownConditions.get(i) & 262144L) != 0);  
        unknown20HDC.setSelected((getHoldDownConditions.get(i) & 524288L) != 0);  

        if(manual.isSelected()||unknown18HDC.isSelected()||unknown19HDC.isSelected()||unknown20HDC.isSelected()){
            automatic.setSelected(false);

        }

        automatic.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if(manual.isSelected()||unknown18HDC.isSelected()||unknown19HDC.isSelected()||unknown20HDC.isSelected()){
                continueUntilReleased.setSelected(false);
            }
            else{
                continueUntilReleased.setSelected(true);
            }
        });

        manual.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getHoldDownConditions.set(i, getHoldDownConditions.get(i) | 65536L);
            } else {
                getHoldDownConditions.set(i, getHoldDownConditions.get(i) & ~65536L);
            }
        });
        unknown18HDC.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getHoldDownConditions.set(i, getHoldDownConditions.get(i) | 131072L);
            } else {
                getHoldDownConditions.set(i, getHoldDownConditions.get(i) & ~131072L);
            }
        });
        unknown19HDC.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getHoldDownConditions.set(i, getHoldDownConditions.get(i) | 262144L);
            } else {
                getHoldDownConditions.set(i, getHoldDownConditions.get(i) & ~262144L);
            }
        });
        unknown20HDC.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getHoldDownConditions.set(i, getHoldDownConditions.get(i) | 524288L);
            } else {
                getHoldDownConditions.set(i, getHoldDownConditions.get(i) & ~524288L);
            }
        });


        VBox chargeTypeBox = new VBox(2, automatic, manual, unknown18HDC,unknown19HDC,unknown20HDC );

        VBox borderContainerChargeType = new VBox(chargeTypeBox);
        borderContainerChargeType.getStyleClass().add("titled-address-box");
        borderContainerChargeType.setPadding(new Insets(12, 0, 0, 0));

        StackPane chargeTypeBoxStackPane = new StackPane();
        chargeTypeBoxStackPane.getChildren().addAll(borderContainerChargeType, chargeTypeLabel);

        chargeTypeBox.addEventHandler(ActionEvent.ACTION,event->{
            if(manual.isSelected()|| unknown18HDC.isSelected()||unknown19HDC.isSelected()||unknown20HDC.isSelected()){
                automatic.setSelected(false);
            }
            else{
                automatic.setSelected(true);
            }
        
        });

        StackPane.setAlignment(chargeTypeLabel, Pos.TOP_LEFT);
        chargeTypeLabel.setTranslateY(-8); 
        chargeTypeLabel.setTranslateX(10);
        holdDownConditionsGridPane.add(chargeTypeBoxStackPane,0,1);
        //ChargeType

        //Options 5
        Label option6Label = new Label("Option 5");
        option6Label.getStyleClass().add("titled-address-label");


        CheckBox unknown21HDC = new CheckBox("Unknown 21");
        CheckBox unknown22HDC = new CheckBox("Unknown 22");
        CheckBox unknown23HDC = new CheckBox("Unknown 23");
        CheckBox unknown24HDC = new CheckBox("Unknown 24");

        unknown21HDC.setSelected((getHoldDownConditions.get(i) & 1048576L) != 0); 
        unknown22HDC.setSelected((getHoldDownConditions.get(i) & 2097152L) != 0); 
        unknown23HDC.setSelected((getHoldDownConditions.get(i) & 4194304L) != 0); 
        unknown24HDC.setSelected((getHoldDownConditions.get(i) & 8388608L) != 0); 

        unknown21HDC.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getHoldDownConditions.set(i, getHoldDownConditions.get(i) | 1048576L);
            } else {
                getHoldDownConditions.set(i, getHoldDownConditions.get(i) & ~1048576L);
            }
        });
        unknown22HDC.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getHoldDownConditions.set(i, getHoldDownConditions.get(i) | 2097152L);
            } else {
                getHoldDownConditions.set(i, getHoldDownConditions.get(i) & ~2097152L);
            }
        });
        unknown23HDC.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getHoldDownConditions.set(i, getHoldDownConditions.get(i) | 4194304L);
            } else {
                getHoldDownConditions.set(i, getHoldDownConditions.get(i) & ~4194304L);
            }
        });
        unknown24HDC.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getHoldDownConditions.set(i, getHoldDownConditions.get(i) | 8388608L);
            } else {
                getHoldDownConditions.set(i, getHoldDownConditions.get(i) & ~8388608L);
            }
        });


        VBox option6Box = new VBox(2,unknown21HDC,unknown22HDC,unknown23HDC,unknown24HDC);

        VBox borderContainerOption6 = new VBox(option6Box);
        borderContainerOption6.getStyleClass().add("titled-address-box");
        borderContainerOption6.setPadding(new Insets(12, 0, 0, 0));

        StackPane option6BoxStackPane = new StackPane();
        option6BoxStackPane.getChildren().addAll(borderContainerOption6, option6Label);

        StackPane.setAlignment(option6Label, Pos.TOP_LEFT);
        option6Label.setTranslateY(-8); 
        option6Label.setTranslateX(10);
        holdDownConditionsGridPane.add(option6BoxStackPane,1,1);
        //Options 6

        //Options 7
        Label option7Label = new Label("Option 7");
        option7Label.getStyleClass().add("titled-address-label");


        CheckBox unknown25HDC = new CheckBox("Unknown 25");
        CheckBox unknown26HDC = new CheckBox("Unknown 26");
        CheckBox unknown27HDC = new CheckBox("Unknown 27");
        CheckBox unknown28HDC = new CheckBox("Unknown 28");

        unknown25HDC.setSelected((getHoldDownConditions.get(i) & 16777216L) != 0);
        unknown26HDC.setSelected((getHoldDownConditions.get(i) & 33554432L) != 0);
        unknown27HDC.setSelected((getHoldDownConditions.get(i) & 67108864L) != 0);
        unknown28HDC.setSelected((getHoldDownConditions.get(i) & 134217728L) != 0);

        unknown25HDC.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getHoldDownConditions.set(i, getHoldDownConditions.get(i) | 16777216L);
            } else {
                getHoldDownConditions.set(i, getHoldDownConditions.get(i) & ~16777216L);
            }
        });
        unknown26HDC.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getHoldDownConditions.set(i, getHoldDownConditions.get(i) | 33554432L);
            } else {
                getHoldDownConditions.set(i, getHoldDownConditions.get(i) & ~33554432L);
            }
        });
        unknown27HDC.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getHoldDownConditions.set(i, getHoldDownConditions.get(i) | 67108864L);
            } else {
                getHoldDownConditions.set(i, getHoldDownConditions.get(i) & ~67108864L);
            }
        });
        unknown28HDC.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getHoldDownConditions.set(i, getHoldDownConditions.get(i) | 134217728L);
            } else {
                getHoldDownConditions.set(i, getHoldDownConditions.get(i) & ~134217728L);
            }
        });

        VBox option7Box = new VBox(2,unknown25HDC,unknown26HDC,unknown27HDC,unknown28HDC);

        VBox borderContainerOption7 = new VBox(option7Box);
        borderContainerOption7.getStyleClass().add("titled-address-box");
        borderContainerOption7.setPadding(new Insets(12, 0, 0, 0));

        StackPane option7BoxStackPane = new StackPane();
        option7BoxStackPane.getChildren().addAll(borderContainerOption7, option7Label);

        StackPane.setAlignment(option7Label, Pos.TOP_LEFT);
        option7Label.setTranslateY(-8); 
        option7Label.setTranslateX(10);
        holdDownConditionsGridPane.add(option7BoxStackPane,2,1);
        //Options 7

        //Options 8
        Label option8Label = new Label("Option 8");
        option8Label.getStyleClass().add("titled-address-label");

        CheckBox unknown29HDC = new CheckBox("Unknown 29");
        CheckBox unknown30HDC = new CheckBox("Unknown 30");
        CheckBox unknown31HDC = new CheckBox("Unknown 31");
        CheckBox unknown32HDC = new CheckBox("Unknown 32");

        unknown29HDC.setSelected((getHoldDownConditions.get(i) & 268435456L) != 0);
        unknown30HDC.setSelected((getHoldDownConditions.get(i) & 536870912L) != 0);
        unknown31HDC.setSelected((getHoldDownConditions.get(i) & 1073741824L) != 0);
        unknown32HDC.setSelected((getHoldDownConditions.get(i) & 2147483648L) != 0);

        unknown29HDC.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getHoldDownConditions.set(i, getHoldDownConditions.get(i) | 268435456L);
            } else {
                getHoldDownConditions.set(i, getHoldDownConditions.get(i) & ~268435456L);
            }
        });
        unknown30HDC.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getHoldDownConditions.set(i, getHoldDownConditions.get(i) | 536870912L);
            } else {
                getHoldDownConditions.set(i, getHoldDownConditions.get(i) & ~536870912L);
            }
        });
        unknown31HDC.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getHoldDownConditions.set(i, getHoldDownConditions.get(i) | 1073741824L);
            } else {
                getHoldDownConditions.set(i, getHoldDownConditions.get(i) & ~1073741824L);
            }
        });
        unknown32HDC.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getHoldDownConditions.set(i, getHoldDownConditions.get(i) | 2147483648L);
            } else {
                getHoldDownConditions.set(i, getHoldDownConditions.get(i) & ~2147483648L);
            }
        });

        VBox option8Box = new VBox(2,unknown29HDC,unknown30HDC,unknown31HDC,unknown32HDC);

        VBox borderContainerOption8 = new VBox(option8Box);
        borderContainerOption8.getStyleClass().add("titled-address-box");
        borderContainerOption8.setPadding(new Insets(12, 0, 0, 0));

        StackPane option8BoxStackPane = new StackPane();
        option8BoxStackPane.getChildren().addAll(borderContainerOption8, option8Label);

        StackPane.setAlignment(option8Label, Pos.TOP_LEFT);
        option8Label.setTranslateY(-8); 
        option8Label.setTranslateX(10);
        holdDownConditionsGridPane.add(option8BoxStackPane,3,1);
        //Options 8

        holdDownConditionsHBox.setAlignment(Pos.CENTER_LEFT);
        holdDownConditionsHBox.getChildren().addAll(holdDownConditionslabel,holdDownConditionsGridPane);
        //hold down conditions

        inputsVBox.getChildren().addAll(directionalInputHBox,buttonInputHBox,holdDownConditionsHBox);
   
        return inputsVBox;
    }
    private ScrollPane createActivatorScrollPane(int i){
        Tab activator=new Tab("Activator");
        activator.setClosable(false);

        VBox activatorVBox=new VBox(20);
        activatorVBox.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

        //Opponent Size Conditions
        HBox opponentsSizeConditionsHBox=new HBox(2);
        opponentsSizeConditionsHBox.setPadding(new Insets(20,0,0,8));
        Label opponentSizeConditionsLabel = new Label("Opponent Size\nConditions");
        opponentSizeConditionsLabel.setPrefWidth(120);

        GridPane opponentSizeConditionsGridPane=new GridPane();
        opponentSizeConditionsGridPane.setVgap(10);
        opponentSizeConditionsGridPane.setHgap(10);

        //Unk Size 1
        Label unknownSize1Label = new Label("Unk Size 1");
        unknownSize1Label.getStyleClass().add("titled-address-label");

        CheckBox unknown1OpponentSizeConditions = new CheckBox("Unknown 1");
        CheckBox unknown2OpponentSizeConditions = new CheckBox("Unknown 2");
        CheckBox unknown3OpponentSizeConditions = new CheckBox("Unknown 3"); 
        CheckBox unknown4OpponentSizeConditions = new CheckBox("Unknown 4");

        unknown1OpponentSizeConditions.setSelected((getOpponentSizeConditions.get(i) & 1L) != 0);          
        unknown2OpponentSizeConditions.setSelected((getOpponentSizeConditions.get(i) & 2L) != 0);          
        unknown3OpponentSizeConditions.setSelected((getOpponentSizeConditions.get(i) & 4L) != 0);         
        unknown4OpponentSizeConditions.setSelected((getOpponentSizeConditions.get(i) & 8L) != 0);     
        
        unknown1OpponentSizeConditions.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getOpponentSizeConditions.set(i, getOpponentSizeConditions.get(i) | 1L);
            } else {
                getOpponentSizeConditions.set(i, getOpponentSizeConditions.get(i) & ~1L);
            }
        });
        unknown2OpponentSizeConditions.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getOpponentSizeConditions.set(i, getOpponentSizeConditions.get(i) | 2L);
            } else {
                getOpponentSizeConditions.set(i, getOpponentSizeConditions.get(i) & ~2L);
            }
        });
        unknown3OpponentSizeConditions.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getOpponentSizeConditions.set(i, getOpponentSizeConditions.get(i) | 4L);
            } else {
                getOpponentSizeConditions.set(i, getOpponentSizeConditions.get(i) & ~4L);
            }
        });
        unknown4OpponentSizeConditions.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getOpponentSizeConditions.set(i, getOpponentSizeConditions.get(i) | 8L);
            } else {
                getOpponentSizeConditions.set(i, getOpponentSizeConditions.get(i) & ~8L);
            }
        });

        VBox unknownSize1Box = new VBox(2, unknown1OpponentSizeConditions, unknown2OpponentSizeConditions, unknown3OpponentSizeConditions, unknown4OpponentSizeConditions);

        VBox borderContainerUnknownSize1Conditions = new VBox(unknownSize1Box);
        borderContainerUnknownSize1Conditions.getStyleClass().add("titled-address-box");
        borderContainerUnknownSize1Conditions.setPadding(new Insets(12, 0, 0, 0));

        StackPane unknownSize1StackPane = new StackPane();
        unknownSize1StackPane.getChildren().addAll(borderContainerUnknownSize1Conditions, unknownSize1Label);

        StackPane.setAlignment(unknownSize1Label, Pos.TOP_LEFT);
        unknownSize1Label.setTranslateY(-8); 
        unknownSize1Label.setTranslateX(10);
        opponentSizeConditionsGridPane.add(unknownSize1StackPane, 0, 0); 
        //Unk Size 1

        //Unk Size 2
        Label unknownSize2Label = new Label("Unk Size 2");
        unknownSize2Label.getStyleClass().add("titled-address-label");

        CheckBox unknown5OpponentSizeConditions = new CheckBox("Unknown 5");
        CheckBox unknown6OpponentSizeConditions = new CheckBox("Unknown 6");
        CheckBox unknown7OpponentSizeConditions = new CheckBox("Unknown 7"); 
        CheckBox unknown8OpponentSizeConditions = new CheckBox("Unknown 8");

        unknown5OpponentSizeConditions.setSelected((getOpponentSizeConditions.get(i) & 16L) != 0);       
        unknown6OpponentSizeConditions.setSelected((getOpponentSizeConditions.get(i) & 32L) != 0);         
        unknown7OpponentSizeConditions.setSelected((getOpponentSizeConditions.get(i) & 64L) != 0);         
        unknown8OpponentSizeConditions.setSelected((getOpponentSizeConditions.get(i) & 128L) != 0);        

        unknown5OpponentSizeConditions.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getOpponentSizeConditions.set(i, getOpponentSizeConditions.get(i) | 16L);
            } else {
                getOpponentSizeConditions.set(i, getOpponentSizeConditions.get(i) & ~16L);
            }
        });
        unknown6OpponentSizeConditions.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getOpponentSizeConditions.set(i, getOpponentSizeConditions.get(i) | 32L);
            } else {
                getOpponentSizeConditions.set(i, getOpponentSizeConditions.get(i) & ~32L);
            }
        });
        unknown7OpponentSizeConditions.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getOpponentSizeConditions.set(i, getOpponentSizeConditions.get(i) | 64L);
            } else {
                getOpponentSizeConditions.set(i, getOpponentSizeConditions.get(i) & ~64L);
            }
        });
        unknown8OpponentSizeConditions.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getOpponentSizeConditions.set(i, getOpponentSizeConditions.get(i) | 128L);
            } else {
                getOpponentSizeConditions.set(i, getOpponentSizeConditions.get(i) & ~128L);
            }
        });

        VBox unknownSize2Box = new VBox(2, unknown5OpponentSizeConditions, unknown6OpponentSizeConditions, unknown7OpponentSizeConditions, unknown8OpponentSizeConditions);

        VBox borderContainerUnknownSize2Conditions = new VBox(unknownSize2Box);
        borderContainerUnknownSize2Conditions.getStyleClass().add("titled-address-box");
        borderContainerUnknownSize2Conditions.setPadding(new Insets(12, 0, 0, 0));

        StackPane unknownSize2StackPane = new StackPane();
        unknownSize2StackPane.getChildren().addAll(borderContainerUnknownSize2Conditions, unknownSize2Label);

        StackPane.setAlignment(unknownSize2Label, Pos.TOP_LEFT);
        unknownSize2Label.setTranslateY(-8); 
        unknownSize2Label.setTranslateX(10);
        opponentSizeConditionsGridPane.add(unknownSize2StackPane, 1, 0); 
        //Unk Size 2

        //Unk Size 3
        Label unknownSize3Label = new Label("Unk Size 3");
        unknownSize3Label.getStyleClass().add("titled-address-label");

        CheckBox unknown9OpponentSizeConditions = new CheckBox("Unknown 9");
        CheckBox unknown10OpponentSizeConditions = new CheckBox("Unknown 10");
        CheckBox unknown11OpponentSizeConditions = new CheckBox("Unknown 11"); 
        CheckBox unknown12OpponentSizeConditions = new CheckBox("Unknown 12");

        unknown9OpponentSizeConditions.setSelected((getOpponentSizeConditions.get(i) & 256L) != 0);        
        unknown10OpponentSizeConditions.setSelected((getOpponentSizeConditions.get(i) & 512L) != 0);       
        unknown11OpponentSizeConditions.setSelected((getOpponentSizeConditions.get(i) & 1024L) != 0);     
        unknown12OpponentSizeConditions.setSelected((getOpponentSizeConditions.get(i) & 2048L) != 0);    
        
        unknown9OpponentSizeConditions.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getOpponentSizeConditions.set(i, getOpponentSizeConditions.get(i) | 256L);
            } else {
                getOpponentSizeConditions.set(i, getOpponentSizeConditions.get(i) & ~256L);
            }
        });
        unknown10OpponentSizeConditions.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getOpponentSizeConditions.set(i, getOpponentSizeConditions.get(i) | 512L);
            } else {
                getOpponentSizeConditions.set(i, getOpponentSizeConditions.get(i) & ~512L);
            }
        });
        unknown11OpponentSizeConditions.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getOpponentSizeConditions.set(i, getOpponentSizeConditions.get(i) | 1024L);
            } else {
                getOpponentSizeConditions.set(i, getOpponentSizeConditions.get(i) & ~1024L);
            }
        });
        unknown12OpponentSizeConditions.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getOpponentSizeConditions.set(i, getOpponentSizeConditions.get(i) | 2048L);
            } else {
                getOpponentSizeConditions.set(i, getOpponentSizeConditions.get(i) & ~2048L);
            }
        });

        VBox unknownSize3Box = new VBox(2, unknown9OpponentSizeConditions, unknown10OpponentSizeConditions, unknown11OpponentSizeConditions, unknown12OpponentSizeConditions);

        VBox borderContainerUnknownSize3Conditions = new VBox(unknownSize3Box);
        borderContainerUnknownSize3Conditions.getStyleClass().add("titled-address-box");
        borderContainerUnknownSize3Conditions.setPadding(new Insets(12, 0, 0, 0));

        StackPane unknownSize3StackPane = new StackPane();
        unknownSize3StackPane.getChildren().addAll(borderContainerUnknownSize3Conditions, unknownSize3Label);

        StackPane.setAlignment(unknownSize3Label, Pos.TOP_LEFT);
        unknownSize3Label.setTranslateY(-8); 
        unknownSize3Label.setTranslateX(10);
        opponentSizeConditionsGridPane.add(unknownSize3StackPane, 2, 0); 
        //Unk Size 3

        //Unk Size 4
        Label unknownSize4Label = new Label("Unk Size 4");
        unknownSize4Label.getStyleClass().add("titled-address-label");

        CheckBox unknown13OpponentSizeConditions = new CheckBox("Unknown 13");
        CheckBox unknown14OpponentSizeConditions = new CheckBox("Unknown 14");
        CheckBox unknown15OpponentSizeConditions = new CheckBox("Unknown 15"); 
        CheckBox unknown16OpponentSizeConditions = new CheckBox("Unknown 16");

        unknown13OpponentSizeConditions.setSelected((getOpponentSizeConditions.get(i) & 4096L) != 0);      
        unknown14OpponentSizeConditions.setSelected((getOpponentSizeConditions.get(i) & 8192L) != 0);      
        unknown15OpponentSizeConditions.setSelected((getOpponentSizeConditions.get(i) & 16384L) != 0);     
        unknown16OpponentSizeConditions.setSelected((getOpponentSizeConditions.get(i) & 32768L) != 0);         

        unknown13OpponentSizeConditions.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getOpponentSizeConditions.set(i, getOpponentSizeConditions.get(i) | 4096L);
            } else {
                getOpponentSizeConditions.set(i, getOpponentSizeConditions.get(i) & ~4096L);
            }
        });
        unknown14OpponentSizeConditions.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getOpponentSizeConditions.set(i, getOpponentSizeConditions.get(i) | 8192L);
            } else {
                getOpponentSizeConditions.set(i, getOpponentSizeConditions.get(i) & ~8192L);
            }
        });
        unknown15OpponentSizeConditions.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getOpponentSizeConditions.set(i, getOpponentSizeConditions.get(i) | 16384L);
            } else {
                getOpponentSizeConditions.set(i, getOpponentSizeConditions.get(i) & ~16384L);
            }
        });
        unknown16OpponentSizeConditions.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getOpponentSizeConditions.set(i, getOpponentSizeConditions.get(i) | 32768L);
            } else {
                getOpponentSizeConditions.set(i, getOpponentSizeConditions.get(i) & ~32768L);
            }
        });

        VBox unknownSize4Box = new VBox(2, unknown13OpponentSizeConditions, unknown14OpponentSizeConditions, unknown15OpponentSizeConditions, unknown16OpponentSizeConditions);

        VBox borderContainerUnknownSize4Conditions = new VBox(unknownSize4Box);
        borderContainerUnknownSize4Conditions.getStyleClass().add("titled-address-box");
        borderContainerUnknownSize4Conditions.setPadding(new Insets(12, 0, 0, 0));

        StackPane unknownSize4StackPane = new StackPane();
        unknownSize4StackPane.getChildren().addAll(borderContainerUnknownSize4Conditions, unknownSize4Label);

        StackPane.setAlignment(unknownSize4Label, Pos.TOP_LEFT);
        unknownSize4Label.setTranslateY(-8); 
        unknownSize4Label.setTranslateX(10);
        opponentSizeConditionsGridPane.add(unknownSize4StackPane, 3, 0); 
        //Unk Size 4

        //Opponent Size 1
        Label opponentSize1Label = new Label("Opponent Size 1");
        opponentSize1Label.getStyleClass().add("titled-address-label");

        CheckBox unknown17OpponentSizeConditions = new CheckBox("Unknown 17");
        CheckBox unknown18OpponentSizeConditions = new CheckBox("Unknown 18");
        CheckBox unknown19OpponentSizeConditions = new CheckBox("Unknown 19"); 
        CheckBox unknown20OpponentSizeConditions = new CheckBox("Unknown 20");

        unknown17OpponentSizeConditions.setSelected((getOpponentSizeConditions.get(i) & 65536L) != 0);   
        unknown18OpponentSizeConditions.setSelected((getOpponentSizeConditions.get(i) & 131072L) != 0);    
        unknown19OpponentSizeConditions.setSelected((getOpponentSizeConditions.get(i) & 262144L) != 0);   
        unknown20OpponentSizeConditions.setSelected((getOpponentSizeConditions.get(i) & 524288L) != 0); 

        unknown17OpponentSizeConditions.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getOpponentSizeConditions.set(i, getOpponentSizeConditions.get(i) | 65536L);
            } else {
                getOpponentSizeConditions.set(i, getOpponentSizeConditions.get(i) & ~65536L);
            }
        });
        unknown18OpponentSizeConditions.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getOpponentSizeConditions.set(i, getOpponentSizeConditions.get(i) | 131072L);
            } else {
                getOpponentSizeConditions.set(i, getOpponentSizeConditions.get(i) & ~131072L);
            }
        });
        unknown19OpponentSizeConditions.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getOpponentSizeConditions.set(i, getOpponentSizeConditions.get(i) | 262144L);
            } else {
                getOpponentSizeConditions.set(i, getOpponentSizeConditions.get(i) & ~262144L);
            }
        });
        unknown20OpponentSizeConditions.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getOpponentSizeConditions.set(i, getOpponentSizeConditions.get(i) | 524288L);
            } else {
                getOpponentSizeConditions.set(i, getOpponentSizeConditions.get(i) & ~524288L);
            }
        });

        VBox opponentSize1Box = new VBox(2, unknown17OpponentSizeConditions, unknown18OpponentSizeConditions, unknown19OpponentSizeConditions, unknown20OpponentSizeConditions);

        VBox borderContainerOpponentSize1Conditions = new VBox(opponentSize1Box);
        borderContainerOpponentSize1Conditions.getStyleClass().add("titled-address-box");
        borderContainerOpponentSize1Conditions.setPadding(new Insets(12, 0, 0, 0));

        StackPane opponentSize1StackPane = new StackPane();
        opponentSize1StackPane.getChildren().addAll(borderContainerOpponentSize1Conditions, opponentSize1Label);

        StackPane.setAlignment(opponentSize1Label, Pos.TOP_LEFT);
        opponentSize1Label.setTranslateY(-8); 
        opponentSize1Label.setTranslateX(10);
        opponentSizeConditionsGridPane.add(opponentSize1StackPane, 0, 1); 
        //Opponent Size 1

        //Opponent Size 2
        Label opponentSize2Label = new Label("Opponent Size 2");
        opponentSize2Label.getStyleClass().add("titled-address-label");

        CheckBox unknown21OpponentSizeConditions = new CheckBox("Unknown 21");
        CheckBox unknown22OpponentSizeConditions = new CheckBox("Unknown 22");
        CheckBox unknown23OpponentSizeConditions = new CheckBox("Unknown 23"); 
        CheckBox unknown24OpponentSizeConditions = new CheckBox("Unknown 24");

        unknown21OpponentSizeConditions.setSelected((getOpponentSizeConditions.get(i) & 1048576L) != 0);   // Bit 20 (1048576)
        unknown22OpponentSizeConditions.setSelected((getOpponentSizeConditions.get(i) & 2097152L) != 0);   // Bit 21 (2097152)
        unknown23OpponentSizeConditions.setSelected((getOpponentSizeConditions.get(i) & 4194304L) != 0);   // Bit 22 (4194304)
        unknown24OpponentSizeConditions.setSelected((getOpponentSizeConditions.get(i) & 8388608L) != 0);   // Bit 23 (8388608)

        unknown21OpponentSizeConditions.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getOpponentSizeConditions.set(i, getOpponentSizeConditions.get(i) | 1048576L);
            } else {
                getOpponentSizeConditions.set(i, getOpponentSizeConditions.get(i) & ~1048576L);
            }
        });
        unknown22OpponentSizeConditions.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getOpponentSizeConditions.set(i, getOpponentSizeConditions.get(i) | 2097152L);
            } else {
                getOpponentSizeConditions.set(i, getOpponentSizeConditions.get(i) & ~2097152L);
            }
        });
        unknown23OpponentSizeConditions.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getOpponentSizeConditions.set(i, getOpponentSizeConditions.get(i) | 4194304L);
            } else {
                getOpponentSizeConditions.set(i, getOpponentSizeConditions.get(i) & ~4194304L);
            }
        });
        unknown24OpponentSizeConditions.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getOpponentSizeConditions.set(i, getOpponentSizeConditions.get(i) | 8388608L);
            } else {
                getOpponentSizeConditions.set(i, getOpponentSizeConditions.get(i) & ~8388608L);
            }
        });

        VBox opponentSize2Box = new VBox(2, unknown21OpponentSizeConditions, unknown22OpponentSizeConditions, unknown23OpponentSizeConditions, unknown24OpponentSizeConditions);

        VBox borderContainerOpponentSize2Conditions = new VBox(opponentSize2Box);
        borderContainerOpponentSize2Conditions.getStyleClass().add("titled-address-box");
        borderContainerOpponentSize2Conditions.setPadding(new Insets(12, 0, 0, 0));

        StackPane opponentSize2StackPane = new StackPane();
        opponentSize2StackPane.getChildren().addAll(borderContainerOpponentSize2Conditions, opponentSize2Label);

        StackPane.setAlignment(opponentSize2Label, Pos.TOP_LEFT);
        opponentSize2Label.setTranslateY(-8); 
        opponentSize2Label.setTranslateX(10);
        opponentSizeConditionsGridPane.add(opponentSize2StackPane, 1, 1); 
        //Opponent Size 2

        //Skill Upgrade 1
        Label skillUpgrade1Label = new Label("Skill Upgrade 1");
        skillUpgrade1Label.getStyleClass().add("titled-address-label");

        CheckBox unknown25OpponentSizeConditions = new CheckBox("Unknown 25");
        CheckBox unknown26OpponentSizeConditions = new CheckBox("Unknown 26");
        CheckBox unknown27OpponentSizeConditions = new CheckBox("Unknown 27"); 
        CheckBox unknown28OpponentSizeConditions = new CheckBox("Unknown 28");

        unknown25OpponentSizeConditions.setSelected((getOpponentSizeConditions.get(i) & 16777216L) != 0); 
        unknown26OpponentSizeConditions.setSelected((getOpponentSizeConditions.get(i) & 33554432L) != 0);  
        unknown27OpponentSizeConditions.setSelected((getOpponentSizeConditions.get(i) & 67108864L) != 0);  
        unknown28OpponentSizeConditions.setSelected((getOpponentSizeConditions.get(i) & 134217728L) != 0); 

        unknown25OpponentSizeConditions.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getOpponentSizeConditions.set(i, getOpponentSizeConditions.get(i) | 16777216L);
            } else {
                getOpponentSizeConditions.set(i, getOpponentSizeConditions.get(i) & ~16777216L);
            }
        });
        unknown26OpponentSizeConditions.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getOpponentSizeConditions.set(i, getOpponentSizeConditions.get(i) | 33554432L);
            } else {
                getOpponentSizeConditions.set(i, getOpponentSizeConditions.get(i) & ~33554432L);
            }
        });
        unknown27OpponentSizeConditions.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getOpponentSizeConditions.set(i, getOpponentSizeConditions.get(i) | 67108864L);
            } else {
                getOpponentSizeConditions.set(i, getOpponentSizeConditions.get(i) & ~67108864L);
            }
        });
        unknown28OpponentSizeConditions.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getOpponentSizeConditions.set(i, getOpponentSizeConditions.get(i) | 134217728L);
            } else {
                getOpponentSizeConditions.set(i, getOpponentSizeConditions.get(i) & ~134217728L);
            }
        });

        VBox skillUpgrade1Box = new VBox(2, unknown25OpponentSizeConditions, unknown26OpponentSizeConditions, unknown27OpponentSizeConditions, unknown28OpponentSizeConditions);

        VBox borderContainerSkillUpgrade1Conditions = new VBox(skillUpgrade1Box);
        borderContainerSkillUpgrade1Conditions.getStyleClass().add("titled-address-box");
        borderContainerSkillUpgrade1Conditions.setPadding(new Insets(12, 0, 0, 0));

        StackPane skillUpgrade1StackPane = new StackPane();
        skillUpgrade1StackPane.getChildren().addAll(borderContainerSkillUpgrade1Conditions, skillUpgrade1Label);

        StackPane.setAlignment(skillUpgrade1Label, Pos.TOP_LEFT);
        skillUpgrade1Label.setTranslateY(-8); 
        skillUpgrade1Label.setTranslateX(10);
        opponentSizeConditionsGridPane.add(skillUpgrade1StackPane, 2, 1); 
        //Skill Upgrade 1

        //Skill Upgrade 2
        Label skillUpgrade2Label = new Label("Skill Upgrade 2");
        skillUpgrade2Label.getStyleClass().add("titled-address-label");

        CheckBox unknown29OpponentSizeConditions = new CheckBox("Unknown 29");
        CheckBox unknown30OpponentSizeConditions = new CheckBox("Unknown 30");
        CheckBox unknown31OpponentSizeConditions = new CheckBox("Unknown 31"); 
        CheckBox unknown32OpponentSizeConditions = new CheckBox("Unknown 32");

        unknown29OpponentSizeConditions.setSelected((getOpponentSizeConditions.get(i) & 268435456L) != 0); 
        unknown30OpponentSizeConditions.setSelected((getOpponentSizeConditions.get(i) & 536870912L) != 0);
        unknown31OpponentSizeConditions.setSelected((getOpponentSizeConditions.get(i) & 1073741824L) != 0);
        unknown32OpponentSizeConditions.setSelected((getOpponentSizeConditions.get(i) & 2147483648L) != 0);

        unknown29OpponentSizeConditions.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getOpponentSizeConditions.set(i, getOpponentSizeConditions.get(i) | 268435456L);
            } else {
                getOpponentSizeConditions.set(i, getOpponentSizeConditions.get(i) & ~268435456L);
            }
        });
        unknown30OpponentSizeConditions.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getOpponentSizeConditions.set(i, getOpponentSizeConditions.get(i) | 536870912L);
            } else {
                getOpponentSizeConditions.set(i, getOpponentSizeConditions.get(i) & ~536870912L);
            }
        });
        unknown31OpponentSizeConditions.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getOpponentSizeConditions.set(i, getOpponentSizeConditions.get(i) | 1073741824L);
            } else {
                getOpponentSizeConditions.set(i, getOpponentSizeConditions.get(i) & ~1073741824L);
            }
        });
        unknown32OpponentSizeConditions.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getOpponentSizeConditions.set(i, getOpponentSizeConditions.get(i) | 2147483648L);
            } else {
                getOpponentSizeConditions.set(i, getOpponentSizeConditions.get(i) & ~2147483648L);
            }
        });

        VBox skillUpgrade2Box = new VBox(2, unknown29OpponentSizeConditions, unknown30OpponentSizeConditions, unknown31OpponentSizeConditions, unknown32OpponentSizeConditions);

        VBox borderContainerSkillUpgrade2Conditions = new VBox(skillUpgrade2Box);
        borderContainerSkillUpgrade2Conditions.getStyleClass().add("titled-address-box");
        borderContainerSkillUpgrade2Conditions.setPadding(new Insets(12, 0, 0, 0));

        StackPane skillUpgrade2StackPane = new StackPane();
        skillUpgrade2StackPane.getChildren().addAll(borderContainerSkillUpgrade2Conditions, skillUpgrade2Label);

        StackPane.setAlignment(skillUpgrade2Label, Pos.TOP_LEFT);
        skillUpgrade2Label.setTranslateY(-8); 
        skillUpgrade2Label.setTranslateX(10);
        opponentSizeConditionsGridPane.add(skillUpgrade2StackPane, 3, 1); 
        //Skill Upgrade 2

        opponentsSizeConditionsHBox.setAlignment(Pos.CENTER_LEFT);
        opponentsSizeConditionsHBox.getChildren().addAll(opponentSizeConditionsLabel,opponentSizeConditionsGridPane);
        //Opponents Size Conditons

        //Minimum Loop Conditions
        HBox minimumLoopDurationHBox=new HBox(2);
        minimumLoopDurationHBox.setPadding(new Insets(20,0,0,8));
        Label minimumLoopDurationLabel=new Label("Minimum Loop\nConditions");
        minimumLoopDurationLabel.setPrefWidth(120);;

        Spinner<Integer> minimumLoopDurationSpinner=new Spinner<>(0,65535,0);
        minimumLoopDurationSpinner.setEditable(true);
        minimumLoopDurationSpinner.getValueFactory().setValue(getMinimumLoopDuration.get(i));
        minimumLoopDurationSpinner.valueProperty().addListener((obs,oldValue,newValue)->{
            if(newValue!=null){
                getMinimumLoopDuration.set(i, newValue);
            }

        });

        minimumLoopDurationHBox.getChildren().addAll(minimumLoopDurationLabel,minimumLoopDurationSpinner);
        minimumLoopDurationHBox.setAlignment(Pos.CENTER_LEFT);
        //Minimum Loop Conditions

        //Maximum Loop Conditions
        HBox maximumLoopDurationHBox=new HBox(2);
        maximumLoopDurationHBox.setPadding(new Insets(20,0,0,8));
        Label maximumLoopDurationLabel=new Label("Maximum Loop\nConditions");
        maximumLoopDurationLabel.setPrefWidth(120);
        
        Spinner<Integer> maximumLoopDurationSpinner=new Spinner<>(0,65535,0);
        maximumLoopDurationSpinner.setEditable(true);
        maximumLoopDurationSpinner.getValueFactory().setValue(getMaximumLoopDuration.get(i));
        maximumLoopDurationSpinner.valueProperty().addListener((obs,oldValue,newValue)->{
            if(newValue!=null){
                getMaximumLoopDuration.set(i, newValue);
            }

        });
        maximumLoopDurationHBox.getChildren().addAll(maximumLoopDurationLabel,maximumLoopDurationSpinner);
        maximumLoopDurationHBox.setAlignment(Pos.CENTER_LEFT);
        //Maximum Loop Conditions

        //Primary Activator Conditions
        HBox primaryActivatorConditionsHBox=new HBox(2);
        primaryActivatorConditionsHBox.setPadding(new Insets(20,0,0,8));
        Label primaryActivatorConditionsLabel = new Label("Primary Activator\nConditions");
        primaryActivatorConditionsLabel.setPrefWidth(120);
        
        GridPane primaryActivatorConditionsGridPane=new GridPane();
        primaryActivatorConditionsGridPane.setVgap(10);
        primaryActivatorConditionsGridPane.setHgap(10);

        //Position
        Label positionLabel = new Label("Position");
        positionLabel.getStyleClass().add("titled-address-label");

        CheckBox standing = new CheckBox("Standing");
        CheckBox floating = new CheckBox("Floating");
        CheckBox touchingGround = new CheckBox("Touching Ground"); 
        CheckBox onAttackHit = new CheckBox("On Attack Hit");

        standing.setSelected((getPrimaryActivatorConditions.get(i) & 1L) != 0);
        floating.setSelected((getPrimaryActivatorConditions.get(i) & 2L) != 0);
        touchingGround.setSelected((getPrimaryActivatorConditions.get(i) & 4L) != 0);
        onAttackHit.setSelected((getPrimaryActivatorConditions.get(i) & 8L) != 0);

        standing.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getPrimaryActivatorConditions.set(i, getPrimaryActivatorConditions.get(i) | 1L);
            } else {
                getPrimaryActivatorConditions.set(i, getPrimaryActivatorConditions.get(i) & ~1L);
            }
        });
        floating.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getPrimaryActivatorConditions.set(i, getPrimaryActivatorConditions.get(i) | 2L);
            } else {
                getPrimaryActivatorConditions.set(i, getPrimaryActivatorConditions.get(i) & ~2L);
            }
        });
        touchingGround.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getPrimaryActivatorConditions.set(i, getPrimaryActivatorConditions.get(i) | 4L);
            } else {
                getPrimaryActivatorConditions.set(i, getPrimaryActivatorConditions.get(i) & ~4L);
            }
        });
        onAttackHit.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getPrimaryActivatorConditions.set(i, getPrimaryActivatorConditions.get(i) | 8L);
            } else {
                getPrimaryActivatorConditions.set(i, getPrimaryActivatorConditions.get(i) & ~8L);
            }
        });

        VBox positionBox = new VBox(2, standing, floating, touchingGround, onAttackHit);

        VBox borderContainerPositionConditions = new VBox(positionBox);
        borderContainerPositionConditions.getStyleClass().add("titled-address-box");
        borderContainerPositionConditions.setPadding(new Insets(12, 0, 0, 0));

        StackPane positionStackPane = new StackPane();
        positionStackPane.getChildren().addAll(borderContainerPositionConditions, positionLabel);

        StackPane.setAlignment(positionLabel, Pos.TOP_LEFT);
        positionLabel.setTranslateY(-8); 
        positionLabel.setTranslateX(10);
        primaryActivatorConditionsGridPane.add(positionStackPane, 0, 0); 
        //Position

        //Distance and Transformation
        Label distanceTransformationLabel = new Label("Distance And Transformation");
        distanceTransformationLabel.getStyleClass().add("titled-address-label");

        CheckBox attackBlocked = new CheckBox("Attack Blocked");
        CheckBox closeToTarget = new CheckBox("Close To Target");
        CheckBox farFromTarget = new CheckBox("Far From Target"); 
        CheckBox inBaseForm = new CheckBox("In Base Form");

        attackBlocked.setSelected((getPrimaryActivatorConditions.get(i) & 16L) != 0);
        closeToTarget.setSelected((getPrimaryActivatorConditions.get(i) & 32L) != 0);
        farFromTarget.setSelected((getPrimaryActivatorConditions.get(i) & 64L) != 0);
        inBaseForm.setSelected((getPrimaryActivatorConditions.get(i) & 128L) != 0);

        attackBlocked.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getPrimaryActivatorConditions.set(i, getPrimaryActivatorConditions.get(i) | 16L);
            } else {
                getPrimaryActivatorConditions.set(i, getPrimaryActivatorConditions.get(i) & ~16L);
            }
        });
        closeToTarget.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getPrimaryActivatorConditions.set(i, getPrimaryActivatorConditions.get(i) | 32L);
            } else {
                getPrimaryActivatorConditions.set(i, getPrimaryActivatorConditions.get(i) & ~32L);
            }
        });
        farFromTarget.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getPrimaryActivatorConditions.set(i, getPrimaryActivatorConditions.get(i) | 64L);
            } else {
                getPrimaryActivatorConditions.set(i, getPrimaryActivatorConditions.get(i) & ~64L);
            }
        });
        inBaseForm.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getPrimaryActivatorConditions.set(i, getPrimaryActivatorConditions.get(i) | 128L);
            } else {
                getPrimaryActivatorConditions.set(i, getPrimaryActivatorConditions.get(i) & ~128L);
            }
        });

        VBox distanceTransformationBox = new VBox(2, attackBlocked, closeToTarget, farFromTarget, inBaseForm);

        VBox borderContainerdistanceTransformationConditions = new VBox(distanceTransformationBox);
        borderContainerdistanceTransformationConditions.getStyleClass().add("titled-address-box");
        borderContainerdistanceTransformationConditions.setPadding(new Insets(12, 0, 0, 0));

        StackPane distanceTransformationStackPane = new StackPane();
        distanceTransformationStackPane.getChildren().addAll(borderContainerdistanceTransformationConditions, distanceTransformationLabel);

        StackPane.setAlignment(distanceTransformationLabel, Pos.TOP_LEFT);
        distanceTransformationLabel.setTranslateY(-8); 
        distanceTransformationLabel.setTranslateX(10);
        primaryActivatorConditionsGridPane.add(distanceTransformationStackPane, 1, 0); 
        //Distance and Transformation

        //Primary Activator
        Label primaryActivatorLabel=new Label("Primary Activator");
        primaryActivatorLabel.getStyleClass().add("titled-address-label");

        CheckBox inTransformedState = new CheckBox("In Transformed State");
        CheckBox flashOnOffUnlessTargeting = new CheckBox("Flash On/Off Unless Targeting");
        CheckBox unknown11PrimaryActivatorConditions = new CheckBox("Unknown 11"); 
        CheckBox idle = new CheckBox("Idle");

        inTransformedState.setSelected((getPrimaryActivatorConditions.get(i) & 256L) != 0);
        flashOnOffUnlessTargeting.setSelected((getPrimaryActivatorConditions.get(i) & 512L) != 0);
        unknown11PrimaryActivatorConditions.setSelected((getPrimaryActivatorConditions.get(i) & 1024L) != 0);
        idle.setSelected((getPrimaryActivatorConditions.get(i) & 2048L) != 0);

        inTransformedState.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getPrimaryActivatorConditions.set(i, getPrimaryActivatorConditions.get(i) | 256L);
            } else {
                getPrimaryActivatorConditions.set(i, getPrimaryActivatorConditions.get(i) & ~256L);
            }
        });
        flashOnOffUnlessTargeting.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getPrimaryActivatorConditions.set(i, getPrimaryActivatorConditions.get(i) | 512L);
            } else {
                getPrimaryActivatorConditions.set(i, getPrimaryActivatorConditions.get(i) & ~512L);
            }
        });
        unknown11PrimaryActivatorConditions.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getPrimaryActivatorConditions.set(i, getPrimaryActivatorConditions.get(i) | 1024L);
            } else {
                getPrimaryActivatorConditions.set(i, getPrimaryActivatorConditions.get(i) & ~1024L);
            }
        });
        idle.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getPrimaryActivatorConditions.set(i, getPrimaryActivatorConditions.get(i) | 2048L);
            } else {
                getPrimaryActivatorConditions.set(i, getPrimaryActivatorConditions.get(i) & ~2048L);
            }
        });

        VBox primaryActivatorBox = new VBox(2, inTransformedState, flashOnOffUnlessTargeting, unknown11PrimaryActivatorConditions, idle);

        VBox borderContainerPrimaryActivator = new VBox(primaryActivatorBox);
        borderContainerPrimaryActivator.getStyleClass().add("titled-address-box");
        borderContainerPrimaryActivator.setPadding(new Insets(12, 0, 0, 0));

        StackPane primaryActivatorStackPane = new StackPane();
        primaryActivatorStackPane.getChildren().addAll(borderContainerPrimaryActivator, primaryActivatorLabel);

        StackPane.setAlignment(primaryActivatorLabel, Pos.TOP_LEFT);
        primaryActivatorLabel.setTranslateY(-8); 
        primaryActivatorLabel.setTranslateX(10);
        primaryActivatorConditionsGridPane.add(primaryActivatorStackPane, 2, 0); 
        //Primary Activator

        //Counter and Ki Amount
        Label counterKiAmountLabel = new Label("Counter And Ki Amount");
        counterKiAmountLabel.getStyleClass().add("titled-address-label");

        CheckBox counterMelee = new CheckBox("Counter Melee");
        CheckBox counterProjectile = new CheckBox("Counter Projectile");
        CheckBox kiBelow100 = new CheckBox("Ki < 100%"); 
        CheckBox kiAbove0 = new CheckBox("Ki > 0%");

        counterMelee.setSelected((getPrimaryActivatorConditions.get(i) & 4096L) != 0);
        counterProjectile.setSelected((getPrimaryActivatorConditions.get(i) & 8192L) != 0);
        kiBelow100.setSelected((getPrimaryActivatorConditions.get(i) & 16384L) != 0);
        kiAbove0.setSelected((getPrimaryActivatorConditions.get(i) & 32768L) != 0);

        counterMelee.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getPrimaryActivatorConditions.set(i, getPrimaryActivatorConditions.get(i) | 4096L);
            } else {
                getPrimaryActivatorConditions.set(i, getPrimaryActivatorConditions.get(i) & ~4096L);
            }
        });
        counterProjectile.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getPrimaryActivatorConditions.set(i, getPrimaryActivatorConditions.get(i) | 8192L);
            } else {
                getPrimaryActivatorConditions.set(i, getPrimaryActivatorConditions.get(i) & ~8192L);
            }
        });
        kiBelow100.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getPrimaryActivatorConditions.set(i, getPrimaryActivatorConditions.get(i) | 16384L);
            } else {
                getPrimaryActivatorConditions.set(i, getPrimaryActivatorConditions.get(i) & ~16384L);
            }
        });
        kiAbove0.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getPrimaryActivatorConditions.set(i, getPrimaryActivatorConditions.get(i) | 32768L);
            } else {
                getPrimaryActivatorConditions.set(i, getPrimaryActivatorConditions.get(i) & ~32768L);
            }
        });

        

        VBox counterKiAmountBox = new VBox(2, counterMelee, counterProjectile, kiBelow100, kiAbove0);

        VBox borderContainercounterKiAmountConditions = new VBox(counterKiAmountBox);
        borderContainercounterKiAmountConditions.getStyleClass().add("titled-address-box");
        borderContainercounterKiAmountConditions.setPadding(new Insets(12, 0, 0, 0));

        StackPane counterKiAmountStackPane = new StackPane();
        counterKiAmountStackPane.getChildren().addAll(borderContainercounterKiAmountConditions, counterKiAmountLabel);

        StackPane.setAlignment(counterKiAmountLabel, Pos.TOP_LEFT);
        counterKiAmountLabel.setTranslateY(-8); 
        counterKiAmountLabel.setTranslateX(10);
        primaryActivatorConditionsGridPane.add(counterKiAmountStackPane, 3, 0); 
        //Counter and Ki Amount

        //Touching
        Label touchingLabel = new Label("Touching");
        touchingLabel.getStyleClass().add("titled-address-label");

        CheckBox unknown17PrimaryActivatorConditions = new CheckBox("Unknown 17");
        CheckBox unknown18PrimaryActivatorConditions = new CheckBox("Unknown 18");
        CheckBox ground = new CheckBox("Ground"); 
        CheckBox opponent = new CheckBox("Opponent");

        unknown17PrimaryActivatorConditions.setSelected((getPrimaryActivatorConditions.get(i) & 65536L) != 0);
        unknown18PrimaryActivatorConditions.setSelected((getPrimaryActivatorConditions.get(i) & 131072L) != 0);
        ground.setSelected((getPrimaryActivatorConditions.get(i) & 262144L) != 0);
        opponent.setSelected((getPrimaryActivatorConditions.get(i) & 524288L) != 0);

        unknown17PrimaryActivatorConditions.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getPrimaryActivatorConditions.set(i, getPrimaryActivatorConditions.get(i) | 65536L);
            } else {
                getPrimaryActivatorConditions.set(i, getPrimaryActivatorConditions.get(i) & ~65536L);
            }
        });
        unknown18PrimaryActivatorConditions.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getPrimaryActivatorConditions.set(i, getPrimaryActivatorConditions.get(i) | 131072L);
            } else {
                getPrimaryActivatorConditions.set(i, getPrimaryActivatorConditions.get(i) & ~131072L);
            }
        });
        ground.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getPrimaryActivatorConditions.set(i, getPrimaryActivatorConditions.get(i) | 262144L);
            } else {
                getPrimaryActivatorConditions.set(i, getPrimaryActivatorConditions.get(i) & ~262144L);
            }
        });
        opponent.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getPrimaryActivatorConditions.set(i, getPrimaryActivatorConditions.get(i) | 524288L);
            } else {
                getPrimaryActivatorConditions.set(i, getPrimaryActivatorConditions.get(i) & ~524288L);
            }
        });

        VBox touchingBox = new VBox(2, unknown17PrimaryActivatorConditions, unknown18PrimaryActivatorConditions, ground, opponent);

        VBox borderContainertouchingConditions = new VBox(touchingBox);
        borderContainertouchingConditions.getStyleClass().add("titled-address-box");
        borderContainertouchingConditions.setPadding(new Insets(12, 0, 0, 0));

        StackPane touchingStackPane = new StackPane();
        touchingStackPane.getChildren().addAll(borderContainertouchingConditions, touchingLabel);

        StackPane.setAlignment(touchingLabel, Pos.TOP_LEFT);
        touchingLabel.setTranslateY(-8); 
        touchingLabel.setTranslateX(10);
        primaryActivatorConditionsGridPane.add(touchingStackPane, 0, 1); 
        //Touching

        //Targeting
        Label targetingLabel = new Label("Targeting");
        targetingLabel.getStyleClass().add("titled-address-label");

        CheckBox opponentKnockback = new CheckBox("Opponent Knockback");
        CheckBox unknown22PrimaryActivatorConditions = new CheckBox("Unknown 22");
        CheckBox targetingOpponent = new CheckBox("Targeting Opponent"); 
        CheckBox unknown24PrimaryActivatorConditions = new CheckBox("Unknown 24");

        opponentKnockback.setSelected((getPrimaryActivatorConditions.get(i) & 1048576L) != 0);
        unknown22PrimaryActivatorConditions.setSelected((getPrimaryActivatorConditions.get(i) & 2097152L) != 0);
        targetingOpponent.setSelected((getPrimaryActivatorConditions.get(i) & 4194304L) != 0);
        unknown24PrimaryActivatorConditions.setSelected((getPrimaryActivatorConditions.get(i) & 8388608L) != 0);

        opponentKnockback.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getPrimaryActivatorConditions.set(i, getPrimaryActivatorConditions.get(i) | 1048576L);
            } else {
                getPrimaryActivatorConditions.set(i, getPrimaryActivatorConditions.get(i) & ~1048576L);
            }
        });
        unknown22PrimaryActivatorConditions.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getPrimaryActivatorConditions.set(i, getPrimaryActivatorConditions.get(i) | 2097152L);
            } else {
                getPrimaryActivatorConditions.set(i, getPrimaryActivatorConditions.get(i) & ~2097152L);
            }
        });
        targetingOpponent.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getPrimaryActivatorConditions.set(i, getPrimaryActivatorConditions.get(i) | 4194304L);
            } else {
                getPrimaryActivatorConditions.set(i, getPrimaryActivatorConditions.get(i) & ~4194304L);
            }
        });
        unknown24PrimaryActivatorConditions.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getPrimaryActivatorConditions.set(i, getPrimaryActivatorConditions.get(i) | 8388608L);
            } else {
                getPrimaryActivatorConditions.set(i, getPrimaryActivatorConditions.get(i) & ~8388608L);
            }
        });

        VBox targetingBox = new VBox(2, opponentKnockback, unknown22PrimaryActivatorConditions, targetingOpponent, unknown24PrimaryActivatorConditions);

        VBox borderContainerTargetingConditions = new VBox(targetingBox);
        borderContainerTargetingConditions.getStyleClass().add("titled-address-box");
        borderContainerTargetingConditions.setPadding(new Insets(12, 0, 0, 0));

        StackPane targetingStackPane = new StackPane();
        targetingStackPane.getChildren().addAll(borderContainerTargetingConditions, targetingLabel);

        StackPane.setAlignment(targetingLabel, Pos.TOP_LEFT);
        targetingLabel.setTranslateY(-8); 
        targetingLabel.setTranslateX(10);
        primaryActivatorConditionsGridPane.add(targetingStackPane, 1, 1); 
        //Targeting

        //Collision and Stamina
        Label collisionStaminaLabel = new Label("Collision/Stamina");
        collisionStaminaLabel.getStyleClass().add("titled-address-label");

        CheckBox activateProjectile = new CheckBox("Activate Projectile");
        CheckBox staminaAboveZero = new CheckBox("Stamina > 0%");
        CheckBox notNearStageCeiling = new CheckBox("Not Near Stage Ceiling"); 
        CheckBox notNearCertainObjects = new CheckBox("Not Near Certain Objects");

        activateProjectile.setSelected((getPrimaryActivatorConditions.get(i) & 16777216L) != 0);
        staminaAboveZero.setSelected((getPrimaryActivatorConditions.get(i) & 33554432L) != 0);
        notNearStageCeiling.setSelected((getPrimaryActivatorConditions.get(i) & 67108864L) != 0);
        notNearCertainObjects.setSelected((getPrimaryActivatorConditions.get(i) & 134217728L) != 0);

        activateProjectile.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getPrimaryActivatorConditions.set(i, getPrimaryActivatorConditions.get(i) | 16777216L);
            } else {
                getPrimaryActivatorConditions.set(i, getPrimaryActivatorConditions.get(i) & ~16777216L);
            }
        });
        staminaAboveZero.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getPrimaryActivatorConditions.set(i, getPrimaryActivatorConditions.get(i) | 33554432L);
            } else {
                getPrimaryActivatorConditions.set(i, getPrimaryActivatorConditions.get(i) & ~33554432L);
            }
        });
        notNearStageCeiling.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getPrimaryActivatorConditions.set(i, getPrimaryActivatorConditions.get(i) | 67108864L);
            } else {
                getPrimaryActivatorConditions.set(i, getPrimaryActivatorConditions.get(i) & ~67108864L);
            }
        });
        notNearCertainObjects.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getPrimaryActivatorConditions.set(i, getPrimaryActivatorConditions.get(i) | 134217728L);
            } else {
                getPrimaryActivatorConditions.set(i, getPrimaryActivatorConditions.get(i) & ~134217728L);
            }
        });

        VBox collisionStaminaBox = new VBox(2, activateProjectile, staminaAboveZero, notNearStageCeiling, notNearCertainObjects);

        VBox borderContainerCollisionStaminaConditions = new VBox(collisionStaminaBox);
        borderContainerCollisionStaminaConditions.getStyleClass().add("titled-address-box");
        borderContainerCollisionStaminaConditions.setPadding(new Insets(12, 0, 0, 0));

        StackPane collisionStaminaStackPane = new StackPane();
        collisionStaminaStackPane.getChildren().addAll(borderContainerCollisionStaminaConditions, collisionStaminaLabel);

        StackPane.setAlignment(collisionStaminaLabel, Pos.TOP_LEFT);
        collisionStaminaLabel.setTranslateY(-8); 
        collisionStaminaLabel.setTranslateX(10);
        primaryActivatorConditionsGridPane.add(collisionStaminaStackPane, 2, 1); 
        //Collision and Stamina

        //Health
        Label healthLabel = new Label("Users Health (One Use)");
        healthLabel.getStyleClass().add("titled-address-label");

        CheckBox usersHealth_OneUse = new CheckBox("Users Health (One Use)");
        CheckBox targetsHealthLessThan25 = new CheckBox("Target's Health < 25%");
        CheckBox currentBacEntryHits = new CheckBox("Current BAC Entry Hits"); 
        CheckBox usersHealth = new CheckBox("Users Health");

        usersHealth_OneUse.setSelected((getPrimaryActivatorConditions.get(i) & 268435456L) != 0);
        targetsHealthLessThan25.setSelected((getPrimaryActivatorConditions.get(i) & 536870912L) != 0);
        currentBacEntryHits.setSelected((getPrimaryActivatorConditions.get(i) & 1073741824L) != 0);
        usersHealth.setSelected((getPrimaryActivatorConditions.get(i) & 2147483648L) != 0);

        usersHealth_OneUse.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getPrimaryActivatorConditions.set(i, getPrimaryActivatorConditions.get(i) | 268435456L);
            } else {
                getPrimaryActivatorConditions.set(i, getPrimaryActivatorConditions.get(i) & ~268435456L);
            }
        });
        targetsHealthLessThan25.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getPrimaryActivatorConditions.set(i, getPrimaryActivatorConditions.get(i) | 536870912L);
            } else {
                getPrimaryActivatorConditions.set(i, getPrimaryActivatorConditions.get(i) & ~536870912L);
            }
        });
        currentBacEntryHits.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getPrimaryActivatorConditions.set(i, getPrimaryActivatorConditions.get(i) | 1073741824L);
            } else {
                getPrimaryActivatorConditions.set(i, getPrimaryActivatorConditions.get(i) & ~1073741824L);
            }
        });
        usersHealth.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getPrimaryActivatorConditions.set(i, getPrimaryActivatorConditions.get(i) | 2147483648L);
            } else {
                getPrimaryActivatorConditions.set(i, getPrimaryActivatorConditions.get(i) & ~2147483648L);
            }
        });

        VBox healthBox = new VBox(2, usersHealth_OneUse, targetsHealthLessThan25, currentBacEntryHits, usersHealth);

        VBox borderContainerhealthConditions = new VBox(healthBox);
        borderContainerhealthConditions.getStyleClass().add("titled-address-box");
        borderContainerhealthConditions.setPadding(new Insets(12, 0, 0, 0));

        StackPane healthStackPane = new StackPane();
        healthStackPane.getChildren().addAll(borderContainerhealthConditions, healthLabel);

        StackPane.setAlignment(healthLabel, Pos.TOP_LEFT);
        healthLabel.setTranslateY(-8); 
        healthLabel.setTranslateX(10);
        primaryActivatorConditionsGridPane.add(healthStackPane, 3, 1); 
        //Health

        primaryActivatorConditionsHBox.getChildren().addAll(primaryActivatorConditionsLabel,primaryActivatorConditionsGridPane);
        primaryActivatorConditionsHBox.setAlignment(Pos.CENTER_LEFT);
        //Primary Activator Conditions

        //Activator State
        HBox activatorStateHBox=new HBox(2);
        activatorStateHBox.setPadding(new Insets(20,0,0,8));
        Label activatorStateLabel = new Label("Activator State");
        activatorStateLabel.setPrefWidth(120);
        
        GridPane activatorStateGridPane=new GridPane();
        activatorStateGridPane.setVgap(10);
        activatorStateGridPane.setHgap(10);
        
        //activator state box 1
        CheckBox idleActivatorState = new CheckBox("Idle");
        CheckBox comboSkill = new CheckBox("Combo/Skill");
        CheckBox boosting = new CheckBox("Boosting"); 
        CheckBox guarding = new CheckBox("Guarding");

        idleActivatorState.setSelected((getActivatorState.get(i) & 1L) != 0);
        comboSkill.setSelected((getActivatorState.get(i) & 2L) != 0);
        boosting.setSelected((getActivatorState.get(i) & 4L) != 0);
        guarding.setSelected((getActivatorState.get(i) & 8L) != 0);

        idleActivatorState.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getActivatorState.set(i, getActivatorState.get(i) | 1L);
            } else {
                getActivatorState.set(i, getActivatorState.get(i) & ~1L);
            }
        });
        comboSkill.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getActivatorState.set(i, getActivatorState.get(i) | 2L);
            } else {
                getActivatorState.set(i, getActivatorState.get(i) & ~2L);
            }
        });
        boosting.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getActivatorState.set(i, getActivatorState.get(i) | 4L);
            } else {
                getActivatorState.set(i, getActivatorState.get(i) & ~4L);
            }
        });
        guarding.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getActivatorState.set(i, getActivatorState.get(i) | 8L);
            } else {
                getActivatorState.set(i, getActivatorState.get(i) & ~8L);
            }
        });

        VBox activatorState1Box = new VBox(2, idleActivatorState, comboSkill, boosting, guarding);

        VBox borderContainerActivatorState1 = new VBox(activatorState1Box);
        borderContainerActivatorState1.getStyleClass().add("titled-address-box");
        borderContainerActivatorState1.setPadding(new Insets(12, 0, 0, 0));

        activatorStateGridPane.add(borderContainerActivatorState1, 0, 0); 
        //activator state box 1

        //activator state box 2
        CheckBox receivingDamage = new CheckBox("Receiving Damage");
        CheckBox jumping = new CheckBox("Jumping");
        CheckBox notBeingDamaged = new CheckBox("Not Being Damaged"); 
        CheckBox targetAttackingPlayer = new CheckBox("Target Attacking Player");

        receivingDamage.setSelected((getActivatorState.get(i) & 16L) != 0);
        jumping.setSelected((getActivatorState.get(i) & 32L) != 0);
        notBeingDamaged.setSelected((getActivatorState.get(i) & 64L) != 0);
        targetAttackingPlayer.setSelected((getActivatorState.get(i) & 128L) != 0);

        receivingDamage.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getActivatorState.set(i, getActivatorState.get(i) | 16L);
            } else {
                getActivatorState.set(i, getActivatorState.get(i) & ~16L);
            }
        });
        jumping.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getActivatorState.set(i, getActivatorState.get(i) | 32L);
            } else {
                getActivatorState.set(i, getActivatorState.get(i) & ~32L);
            }
        });
        notBeingDamaged.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getActivatorState.set(i, getActivatorState.get(i) | 64L);
            } else {
                getActivatorState.set(i, getActivatorState.get(i) & ~64L);
            }
        });
        targetAttackingPlayer.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getActivatorState.set(i, getActivatorState.get(i) | 128L);
            } else {
                getActivatorState.set(i, getActivatorState.get(i) & ~128L);
            }
        });

        VBox activatorState2Box = new VBox(2, receivingDamage,jumping,notBeingDamaged,targetAttackingPlayer);

        VBox borderContainerActivatorState2 = new VBox(activatorState2Box);
        borderContainerActivatorState2.getStyleClass().add("titled-address-box");
        borderContainerActivatorState2.setPadding(new Insets(12, 0, 0, 0));

        activatorStateGridPane.add(borderContainerActivatorState2, 1, 0); 
        //activator state box 2

        //activator state box 3
        CheckBox forwards = new CheckBox("Forwards");
        CheckBox backwards = new CheckBox("Backwards");
        CheckBox left = new CheckBox("Left"); 
        CheckBox right = new CheckBox("Right");

        forwards.setSelected((getActivatorState.get(i) & 256L) != 0);
        backwards.setSelected((getActivatorState.get(i) & 512L) != 0);
        left.setSelected((getActivatorState.get(i) & 1024L) != 0);
        right.setSelected((getActivatorState.get(i) & 2048L) != 0);

        forwards.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getActivatorState.set(i, getActivatorState.get(i) | 256L);
            } else {
                getActivatorState.set(i, getActivatorState.get(i) & ~256L);
            }
        });
        backwards.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getActivatorState.set(i, getActivatorState.get(i) | 512L);
            } else {
                getActivatorState.set(i, getActivatorState.get(i) & ~512L);
            }
        });
        left.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getActivatorState.set(i, getActivatorState.get(i) | 1024L);
            } else {
                getActivatorState.set(i, getActivatorState.get(i) & ~1024L);
            }
        });
        right.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getActivatorState.set(i, getActivatorState.get(i) | 2048L);
            } else {
                getActivatorState.set(i, getActivatorState.get(i) & ~2048L);
            }
        });

        VBox activatorState3Box = new VBox(2, forwards,backwards,left,right);

        VBox borderContainerActivatorState3 = new VBox(activatorState3Box);
        borderContainerActivatorState3.getStyleClass().add("titled-address-box");
        borderContainerActivatorState3.setPadding(new Insets(12, 0, 0, 0));

        activatorStateGridPane.add(borderContainerActivatorState3, 2, 0); 
        //activator state box 3

        //activator state box 4
        Label unknownState4Label = new Label("Unk State 4");
        unknownState4Label.getStyleClass().add("titled-address-label");
        CheckBox unknown13ActivatorState = new CheckBox("Unknown 13");
        CheckBox unknown14ActivatorState = new CheckBox("Unknown 14");
        CheckBox unknown15ActivatorState = new CheckBox("Unknown 15"); 
        CheckBox unknown16ActivatorState = new CheckBox("Unknown 16");

        unknown13ActivatorState.setSelected((getActivatorState.get(i) & 4096L) != 0);
        unknown14ActivatorState.setSelected((getActivatorState.get(i) & 8192L) != 0);
        unknown15ActivatorState.setSelected((getActivatorState.get(i) & 16384L) != 0);
        unknown16ActivatorState.setSelected((getActivatorState.get(i) & 32768L) != 0);

        unknown13ActivatorState.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getActivatorState.set(i, getActivatorState.get(i) | 4096L);
            } else {
                getActivatorState.set(i, getActivatorState.get(i) & ~4096L);
            }
        });
        unknown14ActivatorState.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getActivatorState.set(i, getActivatorState.get(i) | 8192L);
            } else {
                getActivatorState.set(i, getActivatorState.get(i) & ~8192L);
            }
        });
        unknown15ActivatorState.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getActivatorState.set(i, getActivatorState.get(i) | 16384L);
            } else {
                getActivatorState.set(i, getActivatorState.get(i) & ~16384L);
            }
        });
        unknown16ActivatorState.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getActivatorState.set(i, getActivatorState.get(i) | 32768L);
            } else {
                getActivatorState.set(i, getActivatorState.get(i) & ~32768L);
            }
        });

        VBox activatorState4Box = new VBox(2, unknown13ActivatorState,unknown14ActivatorState,unknown15ActivatorState,unknown16ActivatorState);

        VBox borderContainerActivatorState4 = new VBox(activatorState4Box);
        borderContainerActivatorState4.getStyleClass().add("titled-address-box");
        borderContainerActivatorState4.setPadding(new Insets(12, 0, 0, 0));

        StackPane activatorState4StackPane = new StackPane();
        activatorState4StackPane.getChildren().addAll(borderContainerActivatorState4, unknownState4Label);

        StackPane.setAlignment(unknownState4Label, Pos.TOP_LEFT);
        unknownState4Label.setTranslateY(-8); 
        unknownState4Label.setTranslateX(10);

        activatorStateGridPane.add(activatorState4StackPane, 3, 0); 
        //activator state box 4

        //activator state box 5
        Label unknownState5Label = new Label("Unk State 5");
        unknownState5Label.getStyleClass().add("titled-address-label");

        CheckBox unknown17ActivatorState = new CheckBox("Unknown 17");
        CheckBox unknown18ActivatorState = new CheckBox("Unknown 18");
        CheckBox unknown19ActivatorState = new CheckBox("Unknown 19"); 
        CheckBox unknown20ActivatorState = new CheckBox("Unknown 20");

        unknown17ActivatorState.setSelected((getActivatorState.get(i) & 65536L) != 0);
        unknown18ActivatorState.setSelected((getActivatorState.get(i) & 131072L) != 0);
        unknown19ActivatorState.setSelected((getActivatorState.get(i) & 262144L) != 0);
        unknown20ActivatorState.setSelected((getActivatorState.get(i) & 524288L) != 0);

        unknown17ActivatorState.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getActivatorState.set(i, getActivatorState.get(i) | 65536L);
            } else {
                getActivatorState.set(i, getActivatorState.get(i) & ~65536L);
            }
        });
        unknown18ActivatorState.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getActivatorState.set(i, getActivatorState.get(i) | 131072L);
            } else {
                getActivatorState.set(i, getActivatorState.get(i) & ~131072L);
            }
        });
        unknown19ActivatorState.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getActivatorState.set(i, getActivatorState.get(i) | 262144L);
            } else {
                getActivatorState.set(i, getActivatorState.get(i) & ~262144L);
            }
        });
        unknown20ActivatorState.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getActivatorState.set(i, getActivatorState.get(i) | 524288L);
            } else {
                getActivatorState.set(i, getActivatorState.get(i) & ~524288L);
            }
        });

        VBox activatorState5Box = new VBox(2, unknown17ActivatorState, unknown18ActivatorState, unknown19ActivatorState, unknown20ActivatorState);

        VBox borderContainerActivatorState5 = new VBox(activatorState5Box);
        borderContainerActivatorState5.getStyleClass().add("titled-address-box");
        borderContainerActivatorState5.setPadding(new Insets(12, 0, 0, 0));

        StackPane activatorState5StackPane = new StackPane();
        activatorState5StackPane.getChildren().addAll(borderContainerActivatorState5, unknownState5Label);

        StackPane.setAlignment(unknownState5Label, Pos.TOP_LEFT);
        unknownState5Label.setTranslateY(-8); 
        unknownState5Label.setTranslateX(10);
        
        // Placed at Column 0, Row 1
        activatorStateGridPane.add(activatorState5StackPane, 0, 1); 
        //activator state box 5

        //activator state box 6
        Label unknownState6Label = new Label("Unk State 6");
        unknownState6Label.getStyleClass().add("titled-address-label");

        CheckBox unknown21ActivatorState = new CheckBox("Unknown 21");
        CheckBox unknown22ActivatorState = new CheckBox("Unknown 22");
        CheckBox unknown23ActivatorState = new CheckBox("Unknown 23"); 
        CheckBox unknown24ActivatorState = new CheckBox("Unknown 24");

        unknown21ActivatorState.setSelected((getActivatorState.get(i) & 1048576L) != 0);
        unknown22ActivatorState.setSelected((getActivatorState.get(i) & 2097152L) != 0);
        unknown23ActivatorState.setSelected((getActivatorState.get(i) & 4194304L) != 0);
        unknown24ActivatorState.setSelected((getActivatorState.get(i) & 8388608L) != 0);

        unknown21ActivatorState.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getActivatorState.set(i, getActivatorState.get(i) | 1048576L);
            } else {
                getActivatorState.set(i, getActivatorState.get(i) & ~1048576L);
            }
        });
        unknown22ActivatorState.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getActivatorState.set(i, getActivatorState.get(i) | 2097152L);
            } else {
                getActivatorState.set(i, getActivatorState.get(i) & ~2097152L);
            }
        });
        unknown23ActivatorState.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getActivatorState.set(i, getActivatorState.get(i) | 4194304L);
            } else {
                getActivatorState.set(i, getActivatorState.get(i) & ~4194304L);
            }
        });
        unknown24ActivatorState.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getActivatorState.set(i, getActivatorState.get(i) | 8388608L);
            } else {
                getActivatorState.set(i, getActivatorState.get(i) & ~8388608L);
            }
        });
        VBox activatorState6Box = new VBox(2, unknown21ActivatorState, unknown22ActivatorState, unknown23ActivatorState, unknown24ActivatorState);

        VBox borderContainerActivatorState6 = new VBox(activatorState6Box);
        borderContainerActivatorState6.getStyleClass().add("titled-address-box");
        borderContainerActivatorState6.setPadding(new Insets(12, 0, 0, 0));

        StackPane activatorState6StackPane = new StackPane();
        activatorState6StackPane.getChildren().addAll(borderContainerActivatorState6, unknownState6Label);

        StackPane.setAlignment(unknownState6Label, Pos.TOP_LEFT);
        unknownState6Label.setTranslateY(-8); 
        unknownState6Label.setTranslateX(10);
        
        // Placed at Column 1, Row 1
        activatorStateGridPane.add(activatorState6StackPane, 1, 1); 
        //activator state box 6

        //activator state box 7
        Label unknownState7Label = new Label("Unk State 7");
        unknownState7Label.getStyleClass().add("titled-address-label");

        CheckBox unknown25ActivatorState = new CheckBox("Unknown 25");
        CheckBox unknown26ActivatorState = new CheckBox("Unknown 26");
        CheckBox unknown27ActivatorState = new CheckBox("Unknown 27"); 
        CheckBox unknown28ActivatorState = new CheckBox("Unknown 28");

        unknown25ActivatorState.setSelected((getActivatorState.get(i) & 16777216L) != 0);
        unknown26ActivatorState.setSelected((getActivatorState.get(i) & 33554432L) != 0);
        unknown27ActivatorState.setSelected((getActivatorState.get(i) & 67108864L) != 0);
        unknown28ActivatorState.setSelected((getActivatorState.get(i) & 134217728L) != 0);

        unknown25ActivatorState.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getActivatorState.set(i, getActivatorState.get(i) | 16777216L);
            } else {
                getActivatorState.set(i, getActivatorState.get(i) & ~16777216L);
            }
        });
        unknown26ActivatorState.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getActivatorState.set(i, getActivatorState.get(i) | 33554432L);
            } else {
                getActivatorState.set(i, getActivatorState.get(i) & ~33554432L);
            }
        });
        unknown27ActivatorState.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getActivatorState.set(i, getActivatorState.get(i) | 67108864L);
            } else {
                getActivatorState.set(i, getActivatorState.get(i) & ~67108864L);
            }
        });
        unknown28ActivatorState.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getActivatorState.set(i, getActivatorState.get(i) | 134217728L);
            } else {
                getActivatorState.set(i, getActivatorState.get(i) & ~134217728L);
            }
        });

        VBox activatorState7Box = new VBox(2, unknown25ActivatorState, unknown26ActivatorState, unknown27ActivatorState, unknown28ActivatorState);

        VBox borderContainerActivatorState7 = new VBox(activatorState7Box);
        borderContainerActivatorState7.getStyleClass().add("titled-address-box");
        borderContainerActivatorState7.setPadding(new Insets(12, 0, 0, 0));

        StackPane activatorState7StackPane = new StackPane();
        activatorState7StackPane.getChildren().addAll(borderContainerActivatorState7, unknownState7Label);

        StackPane.setAlignment(unknownState7Label, Pos.TOP_LEFT);
        unknownState7Label.setTranslateY(-8); 
        unknownState7Label.setTranslateX(10);
        
        // Placed at Column 2, Row 1
        activatorStateGridPane.add(activatorState7StackPane, 2, 1); 
        //activator state box 7

        //activator state box 8
        Label unknownState8Label = new Label("Unk State 8");
        unknownState8Label.getStyleClass().add("titled-address-label");

        CheckBox unknown29ActivatorState = new CheckBox("Unknown 29");
        CheckBox unknown30ActivatorState = new CheckBox("Unknown 30");
        CheckBox unknown31ActivatorState = new CheckBox("Unknown 31"); 
        CheckBox unknown32ActivatorState = new CheckBox("Unknown 32");

        unknown29ActivatorState.setSelected((getActivatorState.get(i) & 268435456L) != 0);
        unknown30ActivatorState.setSelected((getActivatorState.get(i) & 536870912L) != 0);
        unknown31ActivatorState.setSelected((getActivatorState.get(i) & 1073741824L) != 0);
        unknown32ActivatorState.setSelected((getActivatorState.get(i) & 2147483648L) != 0);

        unknown28ActivatorState.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getActivatorState.set(i, getActivatorState.get(i) | 134217728L);
            } else {
                getActivatorState.set(i, getActivatorState.get(i) & ~134217728L);
            }
        });
        unknown29ActivatorState.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getActivatorState.set(i, getActivatorState.get(i) | 268435456L);
            } else {
                getActivatorState.set(i, getActivatorState.get(i) & ~268435456L);
            }
        });
        unknown30ActivatorState.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getActivatorState.set(i, getActivatorState.get(i) | 536870912L);
            } else {
                getActivatorState.set(i, getActivatorState.get(i) & ~536870912L);
            }
        });
        unknown31ActivatorState.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getActivatorState.set(i, getActivatorState.get(i) | 1073741824L);
            } else {
                getActivatorState.set(i, getActivatorState.get(i) & ~1073741824L);
            }
        });
        unknown32ActivatorState.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                getActivatorState.set(i, getActivatorState.get(i) | 2147483648L);
            } else {
                getActivatorState.set(i, getActivatorState.get(i) & ~2147483648L);
            }
        });

        VBox activatorState8Box = new VBox(2, unknown29ActivatorState, unknown30ActivatorState, unknown31ActivatorState, unknown32ActivatorState);

        VBox borderContainerActivatorState8 = new VBox(activatorState8Box);
        borderContainerActivatorState8.getStyleClass().add("titled-address-box");
        borderContainerActivatorState8.setPadding(new Insets(12, 0, 0, 0));

        StackPane activatorState8StackPane = new StackPane();
        activatorState8StackPane.getChildren().addAll(borderContainerActivatorState8, unknownState8Label);

        StackPane.setAlignment(unknownState8Label, Pos.TOP_LEFT);
        unknownState8Label.setTranslateY(-8); 
        unknownState8Label.setTranslateX(10);
        
        // Placed at Column 3, Row 1
        activatorStateGridPane.add(activatorState8StackPane, 3, 1); 
        //activator state box 8


        activatorStateHBox.getChildren().addAll(activatorStateLabel,activatorStateGridPane);
        activatorStateHBox.setAlignment(Pos.CENTER_LEFT);

        activatorVBox.getChildren().addAll(opponentsSizeConditionsHBox,minimumLoopDurationHBox,maximumLoopDurationHBox,primaryActivatorConditionsHBox,activatorStateHBox);
        
        ScrollPane scrollPane = new ScrollPane(activatorVBox);
        scrollPane.setFitToWidth(true);
        activator.setContent(scrollPane);

        return scrollPane;
    
    }

    private VBox createBACVBox(int i){

        VBox BACVBox=new VBox(12);
        BACVBox.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

        //BAC entry primary
        HBox BACEntryPrimaryHBox=new HBox(40);
        BACEntryPrimaryHBox.setPadding(new Insets(20,0,0,8));
        Label BACEntryPrimaryLabel=new Label("BAC Entry Primary");
        BACEntryPrimaryLabel.setPrefWidth(160);
        
        Spinner<Integer> BACEntryPrimarySpinner=new Spinner<>(Short.MIN_VALUE,Short.MAX_VALUE,0);
        BACEntryPrimarySpinner.setEditable(true);
        BACEntryPrimarySpinner.getValueFactory().setValue(getBACEntryPrimary.get(i));
        BACEntryPrimarySpinner.valueProperty().addListener((obs,oldValue,newValue)->{
            if(newValue!=null){
                getBACEntryPrimary.set(i, newValue);
            }

        });
        
        BACEntryPrimaryHBox.getChildren().addAll(BACEntryPrimaryLabel,BACEntryPrimarySpinner);
        BACEntryPrimaryHBox.setAlignment(Pos.CENTER_LEFT);
        //BAC entry primary

        //BAC entry charge
        HBox BACEntryChargeHBox=new HBox(40);
        BACEntryChargeHBox.setPadding(new Insets(20,0,0,8));
        Label BACEntryChargeLabel=new Label("BAC Entry Charge");
        BACEntryChargeLabel.setPrefWidth(160);

        Spinner<Integer> BACEntryChargeSpinner=new Spinner<>(Short.MIN_VALUE,Short.MAX_VALUE,0);
        BACEntryChargeSpinner.setEditable(true);
        BACEntryChargeSpinner.getValueFactory().setValue(getBACEntryCharge.get(i));
        BACEntryChargeSpinner.valueProperty().addListener((obs,oldValue,newValue)->{
            if(newValue!=null){
                getBACEntryCharge.set(i, newValue);
            }

        });

        BACEntryChargeHBox.getChildren().addAll(BACEntryChargeLabel,BACEntryChargeSpinner);
        BACEntryChargeHBox.setAlignment(Pos.CENTER_LEFT);
        //BAC entry charge

        //BAC entry user connect
        HBox BACEntryUserConnectHBox=new HBox(40);
        BACEntryUserConnectHBox.setPadding(new Insets(20,0,0,8));
        Label BACEntryUserConnectLabel=new Label("BAC Entry User Connect");
        BACEntryUserConnectLabel.setPrefWidth(160);

        Spinner<Integer> BACEntryUserConnectSpinner=new Spinner<>(Short.MIN_VALUE,Short.MAX_VALUE,0);
        BACEntryUserConnectSpinner.setEditable(true);
        BACEntryUserConnectSpinner.getValueFactory().setValue(getBACEntryUserConnect.get(i));
        BACEntryUserConnectSpinner.valueProperty().addListener((obs,oldValue,newValue)->{
            if(newValue!=null){
                getBACEntryUserConnect.set(i, newValue);
            }

        });

        BACEntryUserConnectHBox.getChildren().addAll(BACEntryUserConnectLabel,BACEntryUserConnectSpinner);
        BACEntryUserConnectHBox.setAlignment(Pos.CENTER_LEFT);
        //BAC entry user connect

        //BAC entry victim connect
        HBox BACEntryVictimConnectHBox=new HBox(40);
        BACEntryVictimConnectHBox.setPadding(new Insets(20,0,0,8));
        Label BACEntryVictimConnectLabel=new Label("BAC Entry Victim Connect");
        BACEntryVictimConnectLabel.setPrefWidth(160);
        

        Spinner<Integer> BACEntryVictimConnectSpinner=new Spinner<>(Short.MIN_VALUE,Short.MAX_VALUE,0);
        BACEntryVictimConnectSpinner.setEditable(true);
        BACEntryVictimConnectSpinner.getValueFactory().setValue(getBACEntryVictimConnect.get(i));
        BACEntryVictimConnectSpinner.valueProperty().addListener((obs,oldValue,newValue)->{
            if(newValue!=null){
                getBACEntryVictimConnect.set(i, newValue);
            }

        });

        BACEntryVictimConnectHBox.getChildren().addAll(BACEntryVictimConnectLabel,BACEntryVictimConnectSpinner);
        BACEntryVictimConnectHBox.setAlignment(Pos.CENTER_LEFT);
        //BAC entry victim connect

        //BAC entry airborne
        HBox BACEntryAirborneHBox=new HBox(40);
        BACEntryAirborneHBox.setPadding(new Insets(20,0,0,8));
        Label BACEntryAirborneLabel=new Label("BAC Entry Airborne");
        BACEntryAirborneLabel.setPrefWidth(160);

        Spinner<Integer> BACEntryAirborneSpinner=new Spinner<>(Short.MIN_VALUE,Short.MAX_VALUE,0);
        BACEntryAirborneSpinner.setEditable(true);
        BACEntryAirborneSpinner.getValueFactory().setValue(getBACEntryAirborne.get(i));
        BACEntryAirborneSpinner.valueProperty().addListener((obs,oldValue,newValue)->{
            if(newValue!=null){
                getBACEntryAirborne.set(i, newValue);
            }

        });

        BACEntryAirborneHBox.getChildren().addAll(BACEntryAirborneLabel,BACEntryAirborneSpinner);
        BACEntryAirborneHBox.setAlignment(Pos.CENTER_LEFT);
        //BAC entry airborne

        //BAC entry targeting override
        HBox BACEntryTargetingOverrideHBox=new HBox(40);
        BACEntryTargetingOverrideHBox.setPadding(new Insets(20,0,0,8));
        Label BACEntryTargetingOverrideLabel=new Label("BAC Entry Targeting Override");
        BACEntryTargetingOverrideLabel.setPrefWidth(160);

        Spinner<Integer> BACEntryTargetingOverrideSpinner=new Spinner<>(0,65535,0);
        BACEntryTargetingOverrideSpinner.setEditable(true);
        BACEntryTargetingOverrideSpinner.getValueFactory().setValue(getBACEntryTargetingOverride.get(i));
        BACEntryTargetingOverrideSpinner.valueProperty().addListener((obs,oldValue,newValue)->{
            if(newValue!=null){
                getBACEntryTargetingOverride.set(i, newValue);
            }

        });
        
        BACEntryTargetingOverrideHBox.getChildren().addAll(BACEntryTargetingOverrideLabel,BACEntryTargetingOverrideSpinner);
        BACEntryTargetingOverrideHBox.setAlignment(Pos.CENTER_LEFT);
        //BAC entry targeting override

        //random flags
        HBox randomFlagHBox=new HBox(40);
        randomFlagHBox.setPadding(new Insets(20,0,0,8));
        Label randomFlagLabel=new Label("Random Flag");
        randomFlagLabel.setPrefWidth(160);

        GridPane randomFlagGridPane=new GridPane();
        randomFlagGridPane.getStyleClass().add("titled-address-box");
        randomFlagGridPane.setHgap(10);
        randomFlagGridPane.setVgap(10);

        ToggleGroup randomFlagToggleGroup=new ToggleGroup();
        RadioButton none=new RadioButton("None");
        none.setToggleGroup(randomFlagToggleGroup);
        RadioButton randomBACEntry=new RadioButton("Random BAC Entry");
        randomBACEntry.setToggleGroup(randomFlagToggleGroup);
        RadioButton noTargetCorrection=new RadioButton("No Target Correction");
        noTargetCorrection.setToggleGroup(randomFlagToggleGroup);
        RadioButton threeInstanceSetup =new RadioButton("3 Instance Setup");
        threeInstanceSetup.setToggleGroup(randomFlagToggleGroup);
        RadioButton unknown4=new RadioButton("Unknown 4");
        unknown4.setToggleGroup(randomFlagToggleGroup);
        RadioButton unknown5 = new RadioButton("Unknown 5");
        unknown5.setToggleGroup(randomFlagToggleGroup);
        RadioButton unknown6 = new RadioButton("Unknown 6");
        unknown6.setToggleGroup(randomFlagToggleGroup);
        RadioButton unknown7 = new RadioButton("Unknown 7");
        unknown7.setToggleGroup(randomFlagToggleGroup);
        RadioButton unknown8 = new RadioButton("Unknown 8");
        unknown8.setToggleGroup(randomFlagToggleGroup);
        RadioButton unknown9 = new RadioButton("Unknown 9");
        unknown9.setToggleGroup(randomFlagToggleGroup);
        none.setSelected(true);

        if(getBACRandomFlags.get(i)==1){
            randomBACEntry.setSelected(true);
        }
        else if(getBACRandomFlags.get(i)==2){
            noTargetCorrection.setSelected(true);
        }
        else if(getBACRandomFlags.get(i)==3){
            threeInstanceSetup.setSelected(true);
        }
        else if(getBACRandomFlags.get(i)==4){
            unknown4.setSelected(true);
        }
        else if(getBACRandomFlags.get(i)==5){
            unknown5.setSelected(true);
        }
        else if(getBACRandomFlags.get(i)==6){
            unknown6.setSelected(true);
        }
        else if(getBACRandomFlags.get(i)==7){
            unknown7.setSelected(true);
        }
        else if(getBACRandomFlags.get(i)==8){
            unknown8.setSelected(true);
        }
        else if(getBACRandomFlags.get(i)==9){
            unknown9.setSelected(true);
        }

        randomFlagToggleGroup.selectedToggleProperty().addListener((obs,oldValue,newValue)->{
            if(newValue.isSelected()){
                RadioButton selectedRadio = (RadioButton) newValue;
                if (selectedRadio == none) { 
                    getBACRandomFlags.set(i, (short)0);
                }
                else if (selectedRadio == randomBACEntry) { 
                    getBACRandomFlags.set(i, (short)1);
                }
                else if (selectedRadio == noTargetCorrection) { 
                    getBACRandomFlags.set(i, (short)2);
                }
                else if (selectedRadio == threeInstanceSetup) { 
                    getBACRandomFlags.set(i, (short)3);
                }
                else if (selectedRadio == unknown4) { 
                    getBACRandomFlags.set(i, (short)4);
                }
                else if (selectedRadio == unknown5) { 
                    getBACRandomFlags.set(i, (short)5);
                }
                else if (selectedRadio == unknown6) { 
                    getBACRandomFlags.set(i, (short)6);
                }
                else if (selectedRadio == unknown7) { 
                    getBACRandomFlags.set(i, (short)7);
                }
                else if (selectedRadio == unknown8) { 
                    getBACRandomFlags.set(i, (short)8);
                }
                else if (selectedRadio == unknown9) { 
                    getBACRandomFlags.set(i, (short)9);
                }
            }
        });

        randomFlagGridPane.add(none, 0, 0);   
        randomFlagGridPane.add(randomBACEntry, 1, 0);          
        randomFlagGridPane.add(noTargetCorrection, 0, 1);          
        randomFlagGridPane.add(threeInstanceSetup, 1, 1);          
        randomFlagGridPane.add(unknown4, 0, 2);          
        randomFlagGridPane.add(unknown5, 1, 2);          
        randomFlagGridPane.add(unknown6, 0, 3);          
        randomFlagGridPane.add(unknown7, 1, 3);          
        randomFlagGridPane.add(unknown8, 0, 4);          
        randomFlagGridPane.add(unknown9, 1, 4);   
        
        randomFlagHBox.getChildren().addAll(randomFlagLabel,randomFlagGridPane);
        randomFlagHBox.setAlignment(Pos.CENTER_LEFT);
        //random flangs

        //BAC

        BACVBox.getChildren().addAll(BACEntryPrimaryHBox,BACEntryChargeHBox,BACEntryUserConnectHBox,BACEntryVictimConnectHBox,BACEntryAirborneHBox,BACEntryTargetingOverrideHBox,randomFlagHBox);
        
        return BACVBox;

    }

    private VBox createMiscVBox(int i){
        VBox miscVBox=new VBox(12);
        miscVBox.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

        //ki cost
        HBox kiCostHBox=new HBox(40);
        kiCostHBox.setPadding(new Insets(20,0,0,8));
        Label kiCostLabel=new Label("Ki Cost");
        kiCostLabel.setPrefWidth(160);

        Spinner<Double> kiCostSpinner=new Spinner<>(0,4294967295.0,0);
        kiCostSpinner.setEditable(true);
        kiCostSpinner.getValueFactory().setValue(getKiCost.get(i));
        kiCostSpinner.valueProperty().addListener((obs,oldValue,newValue)->{
            if(newValue!=null){
                getKiCost.set(i, newValue);
            }

        });

        kiCostHBox.getChildren().addAll(kiCostLabel,kiCostSpinner);
        kiCostHBox.setAlignment(Pos.CENTER_LEFT);
        //ki cost

        //receiver link id
        HBox receiverIdLinkHBox=new HBox(40);
        receiverIdLinkHBox.setPadding(new Insets(20,0,0,8));
        Label receiverLinkIdLabel=new Label("Receiver Link Id");
        receiverLinkIdLabel.setPrefWidth(160);

        Spinner<Double> receiverLinkIdSpinner=new Spinner<>(0,4294967295.0,0);
        receiverLinkIdSpinner.setEditable(true);
        receiverLinkIdSpinner.getValueFactory().setValue(getReceiverLinkId.get(i));
        receiverLinkIdSpinner.valueProperty().addListener((obs,oldValue,newValue)->{
            if(newValue!=null){
                getReceiverLinkId.set(i, newValue);
            }
        });

        receiverIdLinkHBox.getChildren().addAll(receiverLinkIdLabel,receiverLinkIdSpinner);
        receiverIdLinkHBox.setAlignment(Pos.CENTER_LEFT);
        //receiver link id

        //stamina cost
        HBox staminaCostHBox=new HBox(40);
        staminaCostHBox.setPadding(new Insets(20,0,0,8));
        Label staminaCostLabel=new Label("Stamina Cost");
        staminaCostLabel.setPrefWidth(160);

        Spinner<Double> staminaCostSpinner=new Spinner<>(0,4294967295.0,0);
        staminaCostSpinner.setEditable(true);
        staminaCostSpinner.getValueFactory().setValue(getStaminaCost.get(i));
        staminaCostSpinner.valueProperty().addListener((obs,oldValue,newValue)->{
            if(newValue!=null){
                getStaminaCost.set(i, newValue);
            }
        });
        
        staminaCostHBox.getChildren().addAll(staminaCostLabel,staminaCostSpinner);
        staminaCostHBox.setAlignment(Pos.CENTER_LEFT);
        //stamina cost

        //ki required
        HBox kiRequiredHBox=new HBox(40);
        kiRequiredHBox.setPadding(new Insets(20,0,0,8));
        Label kiRequiredLabel=new Label("Ki Required");
        kiRequiredLabel.setPrefWidth(160);

        Spinner<Double> kiRequiredSpinner=new Spinner<>(0,4294967295.0,0);
        kiRequiredSpinner.setEditable(true);
        kiRequiredSpinner.getValueFactory().setValue(getKiRequired.get(i));
        kiRequiredSpinner.valueProperty().addListener((obs,oldValue,newValue)->{
            if(newValue!=null){
                getKiRequired.set(i, newValue);
            }
        });

        kiRequiredHBox.getChildren().addAll(kiRequiredLabel,kiRequiredSpinner);
        kiRequiredHBox.setAlignment(Pos.CENTER_LEFT);
        //ki required

        //health required
        HBox healthRequiredHBox=new HBox(40);
        healthRequiredHBox.setPadding(new Insets(20,0,0,8));
        Label healthRequiredLabel=new Label("Health Required");
        healthRequiredLabel.setPrefWidth(160);

        Spinner<Double> healthRequiredSpinner=new Spinner<>(Float.MIN_VALUE,Float.MAX_VALUE,0.0);
        healthRequiredSpinner.setEditable(true);
        healthRequiredSpinner.getValueFactory().setValue(getHealthRequired.get(i));
        healthRequiredSpinner.valueProperty().addListener((obs,oldValue,newValue)->{
            if(newValue!=null){
                getHealthRequired.set(i, newValue);
            }
        });

        healthRequiredHBox.getChildren().addAll(healthRequiredLabel,healthRequiredSpinner);
        healthRequiredHBox.setAlignment(Pos.CENTER_LEFT);
        //health required

        //transformation stage
        HBox transformationStageHBox=new HBox(40);
        transformationStageHBox.setPadding(new Insets(20,0,0,8));
        Label transformationStageLabel=new Label("Transformation Stage");
        transformationStageLabel.setPrefWidth(160);

        Spinner<Integer> transformationStageSpinner=new Spinner<>(Short.MIN_VALUE,Short.MAX_VALUE,0);
        transformationStageSpinner.setEditable(true);
        transformationStageSpinner.getValueFactory().setValue(getTransformationStage.get(i));
        transformationStageSpinner.valueProperty().addListener((obs,oldValue,newValue)->{
            if(newValue!=null){
                getTransformationStage.set(i, newValue);
            }
        });

        transformationStageHBox.getChildren().addAll(transformationStageLabel,transformationStageSpinner);
        transformationStageHBox.setAlignment(Pos.CENTER_LEFT);
        //transformation stage

        //cus aura
        HBox cusAuraHBox=new HBox(40);
        cusAuraHBox.setPadding(new Insets(20,0,0,8));
        Label cusAuraLabel=new Label("CUS Aura");
        cusAuraLabel.setPrefWidth(160);

        Spinner<Integer> cusAuraSpinner=new Spinner<>(Short.MIN_VALUE,Short.MAX_VALUE,0);
        cusAuraSpinner.setEditable(true);
        cusAuraSpinner.getValueFactory().setValue(getTransformationStage.get(i));
        cusAuraSpinner.valueProperty().addListener((obs,oldValue,newValue)->{
            if(newValue!=null){
                getTransformationStage.set(i, newValue);
            }
        });
        cusAuraHBox.getChildren().addAll(cusAuraLabel,cusAuraSpinner);
        cusAuraHBox.setAlignment(Pos.CENTER_LEFT);
        //cus aura

        //race and gender
        HBox raceGenderHBox=new HBox(40);
        raceGenderHBox.setPadding(new Insets(20,0,0,8));
        Label raceGenderLabel=new Label("Race/Gender");
        raceGenderLabel.setPrefWidth(160);

        GridPane raceGenderGridPane=new GridPane();
        raceGenderGridPane.getStyleClass().add("titled-address-box");
        raceGenderGridPane.setHgap(10);
        raceGenderGridPane.setVgap(10);

        ToggleGroup raceGenderToggleGroup=new ToggleGroup();
        RadioButton allCharactersDefault=new RadioButton("All Characters/Default");
        allCharactersDefault.setToggleGroup(raceGenderToggleGroup);
        RadioButton rosterCharactersOnly=new RadioButton("Roster Characters Only");
        rosterCharactersOnly.setToggleGroup(raceGenderToggleGroup);
        RadioButton maleHumansOnly=new RadioButton("Male Humans Only");
        maleHumansOnly.setToggleGroup(raceGenderToggleGroup);
        RadioButton femaleHumansOnly =new RadioButton("Female Humans Only");
        femaleHumansOnly.setToggleGroup(raceGenderToggleGroup);
        RadioButton maleSaiyansOnly=new RadioButton("Male Saiyans Only");
        maleSaiyansOnly.setToggleGroup(raceGenderToggleGroup);
        RadioButton femaleSaiyansOnly = new RadioButton("Female Saiyans Only");
        femaleSaiyansOnly.setToggleGroup(raceGenderToggleGroup);
        RadioButton namekiansOnly = new RadioButton("Namekians Only");
        namekiansOnly.setToggleGroup(raceGenderToggleGroup);
        RadioButton friezaRaceOnly = new RadioButton("Frieza Race Only");
        friezaRaceOnly.setToggleGroup(raceGenderToggleGroup);
        RadioButton maleMajinsOnly = new RadioButton("Male Majins Only");
        maleMajinsOnly.setToggleGroup(raceGenderToggleGroup);
        RadioButton femaleMajinsOnly = new RadioButton("Female Majins Only");
        femaleMajinsOnly.setToggleGroup(raceGenderToggleGroup);
        allCharactersDefault.setSelected(true);

        if(getRaceGender.get(i)==1){
            rosterCharactersOnly.setSelected(true);
        }
        else if(getRaceGender.get(i)==2){
            maleHumansOnly.setSelected(true);
        }
        else if(getRaceGender.get(i)==3){
            femaleHumansOnly.setSelected(true);
        }
        else if(getRaceGender.get(i)==4){
            maleSaiyansOnly.setSelected(true);
        }
        else if(getRaceGender.get(i)==5){
            femaleSaiyansOnly.setSelected(true);
        }
        else if(getRaceGender.get(i)==6){
            namekiansOnly.setSelected(true);
        }
        else if(getRaceGender.get(i)==7){
           friezaRaceOnly.setSelected(true);
        }
        else if(getRaceGender.get(i)==8){
            maleMajinsOnly.setSelected(true);
        }
        else if(getRaceGender.get(i)==9){
            femaleMajinsOnly.setSelected(true);
        }
        raceGenderToggleGroup.selectedToggleProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue.isSelected()) {
                RadioButton selectedRadio = (RadioButton) newValue;
                if (selectedRadio == allCharactersDefault) {
                    getRaceGender.set(i, 0L);
                }
                else if (selectedRadio == rosterCharactersOnly) {
                    getRaceGender.set(i, 1L);
                }
                else if (selectedRadio == maleHumansOnly) {
                    getRaceGender.set(i, 2L);
                }
                else if (selectedRadio == femaleHumansOnly) {
                    getRaceGender.set(i, 3L);
                }
                else if (selectedRadio == maleSaiyansOnly) {
                    getRaceGender.set(i, 4L);
                }
                else if (selectedRadio == femaleSaiyansOnly) {
                    getRaceGender.set(i, 5L);
                }
                else if (selectedRadio == namekiansOnly) {
                    getRaceGender.set(i, 6L);
                }
                else if (selectedRadio == friezaRaceOnly) {
                    getRaceGender.set(i, 7L);
                }
                else if (selectedRadio == maleMajinsOnly) {
                    getRaceGender.set(i, 8L);
                }
                else if (selectedRadio == femaleMajinsOnly) {
                    getRaceGender.set(i, 9L);
                }
            }
        });
        raceGenderHBox.getChildren().addAll(raceGenderLabel,raceGenderGridPane);
        raceGenderHBox.setAlignment(Pos.CENTER_LEFT);

        raceGenderGridPane.add(allCharactersDefault, 0, 0);   
        raceGenderGridPane.add(rosterCharactersOnly, 1, 0);          
        raceGenderGridPane.add(maleHumansOnly, 0, 1);          
        raceGenderGridPane.add(femaleHumansOnly, 1, 1);          
        raceGenderGridPane.add(maleSaiyansOnly, 0, 2);          
        raceGenderGridPane.add(femaleSaiyansOnly, 1, 2);          
        raceGenderGridPane.add(namekiansOnly, 0, 3);          
        raceGenderGridPane.add(friezaRaceOnly, 1, 3);          
        raceGenderGridPane.add(maleMajinsOnly, 0, 4);          
        raceGenderGridPane.add(femaleMajinsOnly, 1, 4);
        
        miscVBox.getChildren().addAll(kiCostHBox,receiverIdLinkHBox,staminaCostHBox,kiRequiredHBox,healthRequiredHBox,transformationStageHBox,cusAuraHBox,raceGenderHBox);
   
        return miscVBox;
    }

    private VBox createUnknownVBox(int i){
        // Tab unknown=new Tab("Unknown");
        // unknown.setClosable(false);

        VBox unknownVBox=new VBox(20);
        unknownVBox.setPadding(new Insets(20,0,0,8));

        
        

        //I_00
        HBox unknown00HBox=new HBox(2);
        unknown00HBox.setPadding(new Insets(20,0,0,8));
        Label lblI00=new Label("I_00: ");
        lblI00.setPrefWidth(120);
        TextField txtI00=new TextField(String.valueOf(getUnknown0.get(i)));
        txtI00.textProperty().addListener((obs, oldText, newText) -> {
            if (txtI00.getText().contains("-")) {
                return;
            }
            try {
                
                getUnknown0.set(i, Long.parseLong(newText)); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });
        
        unknown00HBox.getChildren().addAll(lblI00,txtI00);
        unknown00HBox.setAlignment(Pos.CENTER_LEFT);
        //I_00

        //I_36
        HBox unknown36HBox=new HBox(2);
        unknown36HBox.setPadding(new Insets(20,0,0,8));
        Label lblI36=new Label("I_36: ");
        lblI36.setPrefWidth(120);
        TextField txtI36=new TextField(String.valueOf(getUnknown36.get(i)));
        txtI36.textProperty().addListener((obs, oldText, newText) -> {
            if (txtI36.getText().contains("-")) {
                return;
            }
            try {
                getUnknown36.set(i, Short.parseShort(newText)); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });
        
        unknown36HBox.getChildren().addAll(lblI36,txtI36);
        unknown36HBox.setAlignment(Pos.CENTER_LEFT);
        //I_68
        HBox unknown68HBox=new HBox(2);
        unknown68HBox.setPadding(new Insets(20,0,0,8));
        Label lblI68=new Label("I_68: ");
        lblI68.setPrefWidth(120);
        TextField txtI68=new TextField(String.valueOf(getUnknown68.get(i)));
        txtI68.textProperty().addListener((obs, oldText, newText) -> {
            if (txtI68.getText().contains("-")) {
                return;
            }
            try {
                getUnknown68.set(i, Long.parseLong(newText)); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
                
            }
        });
    
        unknown68HBox.getChildren().addAll(lblI68,txtI68);
        unknown68HBox.setAlignment(Pos.CENTER_LEFT);
        //I_68

        //I_72
        HBox unknown72HBox=new HBox(2);
        unknown72HBox.setPadding(new Insets(20,0,0,8));
        Label lblI72=new Label("I_72: ");
        lblI72.setPrefWidth(120);
        TextField txtI72=new TextField(String.valueOf(getUnknown72.get(i)));
        txtI72.textProperty().addListener((obs, oldText, newText) -> {
            if (txtI72.getText().contains("-")) {
                return;
            }
            try {
                getUnknown72.set(i, Long.parseLong(newText)); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
                
            }
        });
        unknown72HBox.getChildren().addAll(lblI72,txtI72);
        unknown72HBox.setAlignment(Pos.CENTER_LEFT);
        //I_72

        //I_80
        HBox unknown80HBox=new HBox(2);
        unknown80HBox.setPadding(new Insets(20,0,0,8));
        Label lblI80=new Label("I_80: ");
        lblI80.setPrefWidth(120);
        TextField txtI80=new TextField(String.valueOf(getUnknown80.get(i)));
        txtI80.textProperty().addListener((obs, oldText, newText) -> {
            if (txtI80.getText().contains("-")) {
                return;
            }
            try {
                getUnknown80.set(i, Long.parseLong(newText)); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
                
            }
        });
        
        unknown80HBox.getChildren().addAll(lblI80,txtI80);
        unknown80HBox.setAlignment(Pos.CENTER_LEFT);
        //I_80

        //I_88
        HBox unknown88HBox=new HBox(2);
        unknown88HBox.setPadding(new Insets(20,0,0,8));
        Label lblI88=new Label("I_88: ");
        lblI88.setPrefWidth(120);
        TextField txtI88=new TextField(String.valueOf(getUnknown88.get(i)));
        txtI88.textProperty().addListener((obs, oldText, newText) -> {
            if (txtI88.getText().contains("-")) {
                return;
            }
            try {
                getUnknown88.set(i, Long.parseLong(newText)); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
                
            }
        });
        unknown88HBox.getChildren().addAll(lblI88,txtI88);
        unknown88HBox.setAlignment(Pos.CENTER_LEFT);
        //I_88

        //I_104
        HBox unknown104HBox=new HBox(2);
        unknown104HBox.setPadding(new Insets(20,0,0,8));
        Label lblI104=new Label("Skill Upgrade Value?: ");
        lblI104.setPrefWidth(120);
        TextField txtI104=new TextField(String.valueOf(getUnknown104.get(i)));
        txtI104.textProperty().addListener((obs, oldText, newText) -> {
            if (txtI104.getText().contains("-")) {
                return;
            }
            try {
                
                getUnknown104.set(i, Long.parseLong(newText)); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });
        
        unknown104HBox.getChildren().addAll(lblI104,txtI104);
        unknown104HBox.setAlignment(Pos.CENTER_LEFT);
        //I_104

        unknownVBox.getChildren().addAll(unknown00HBox,unknown36HBox,unknown68HBox,unknown72HBox,unknown80HBox,unknown88HBox,unknown104HBox);
        return unknownVBox;
    }
     
    public void entriesActionListener(){
        treeView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue)->{

            if (newValue == null) {
                return;
            }
            //currentEntry=treeView.getSelectionModel().getSelectedItem();
            currentEntry=newValue;
            //System.out.println(treeView.getRow(currentEntry));
            //System.out.println(getGridPanes.get(treeView.getRow(currentEntry)).getChildren()) ;


            // //input tab
            tabPane.getTabs().get(0).setContent(createInputsVBox(treeView.getRow(currentEntry)));
   
            // //activator tab
            tabPane.getTabs().get(1).setContent(createActivatorScrollPane(treeView.getRow(currentEntry)));

            // //BAC tab
            tabPane.getTabs().get(2).setContent(createBACVBox(treeView.getRow(currentEntry)));
       
            // //misc tab
            tabPane.getTabs().get(3).setContent(createMiscVBox(treeView.getRow(currentEntry)));
            
            //unknown tab
            tabPane.getTabs().get(4).setContent(createUnknownVBox(treeView.getRow(currentEntry)));
        });
        treeView.setOnMouseClicked(e->{
            if(e.getButton()==MouseButton.SECONDARY){
                ContextMenu contextMenu=new ContextMenu();
                MenuItem copy=new MenuItem("Copy Ctrl+C");
                MenuItem paste=new MenuItem("Paste Ctrl+V");
                MenuItem delete=new MenuItem("Delete Delete");
                MenuItem append=new MenuItem("Append Ctrl+A");
                MenuItem insert=new MenuItem("Insert Ctrl+I");
                MenuItem addNewChild=new MenuItem("Add New Child Ctrl+N");
                MenuItem addComment =new MenuItem("Add Comment Ctrl+Q");
                contextMenu.getItems().addAll(copy,paste,delete,append,insert,addNewChild,addComment);
                treeView.setContextMenu(contextMenu);
               
                contextMenu.setOnAction(event->{
                    if(event.getTarget()==copy){
                        Copy();
                    }
                    if(event.getTarget()==paste){
                        Paste();
                    }
                    if(event.getTarget()==delete){
                       Delete();
                    }
                    if(event.getTarget()==append){
                        Append();
                    }
                    if(event.getTarget()==insert){
                        Insert();
                    }
                    if(event.getTarget()==addNewChild){
                        AddNewChild();
                    }
                    if(event.getTarget()==addComment){
                        AddComment();

                    }
                });
            }
        });
    }

    public void entriesKeysListener(){
        treeView.setOnKeyPressed(e->{
            if(e.isControlDown()&&e.getCode()==KeyCode.C){
                Copy();
            }
            if(e.isControlDown()&&e.getCode()==KeyCode.V){
                Paste();
            }
            if(e.getCode()==KeyCode.DELETE){
                Delete();
            }
            if(e.isControlDown()&&e.getCode()==KeyCode.A){
                Append();
                
            }
            if(e.isControlDown()&&e.getCode()==KeyCode.I){
                Insert();
            }
            if(e.isControlDown()&&e.getCode()==KeyCode.N){
                AddNewChild();
            }
            if(e.isControlDown()&&e.getCode()==KeyCode.Q){
                AddComment();
                
            }
        });
    }

    private void Copy() {
        if (currentEntry == null) {
            return;
        }

        // Copy Inputs
        copyDirectionalInputs = getDirectionalInputs.get(treeView.getRow(currentEntry));
        copyButtonInputs = getButtonInputs.get(treeView.getRow(currentEntry));
        copyHoldDownConditions = getHoldDownConditions.get(treeView.getRow(currentEntry));

        // Copy Activator configurations
        copyOpponentSizeConditions = getOpponentSizeConditions.get(treeView.getRow(currentEntry));
        copyMinimumLoopDuration = getMinimumLoopDuration.get(treeView.getRow(currentEntry));
        copyMaximumLoopDuration = getMaximumLoopDuration.get(treeView.getRow(currentEntry));
        copyPrimaryActivatorConditions = getPrimaryActivatorConditions.get(treeView.getRow(currentEntry));
        copyActivatorState = getActivatorState.get(treeView.getRow(currentEntry));

        // Copy BAC parameters
        copyBACEntryPrimary = getBACEntryPrimary.get(treeView.getRow(currentEntry));
        copyBACEntryCharge = getBACEntryCharge.get(treeView.getRow(currentEntry));
        copyBACEntryUserConnect = getBACEntryUserConnect.get(treeView.getRow(currentEntry));
        copyBACEntryVictimConnect = getBACEntryVictimConnect.get(treeView.getRow(currentEntry));
        copyBACEntryAirborne = getBACEntryAirborne.get(treeView.getRow(currentEntry));
        copyBACEntryTargetingOverride = getBACEntryTargetingOverride.get(treeView.getRow(currentEntry));
        copyBACRandomFlags = getBACRandomFlags.get(treeView.getRow(currentEntry));

        // Copy Misc parameters
        copyKiCost = getKiCost.get(treeView.getRow(currentEntry));
        copyReceiverLinkId = getReceiverLinkId.get(treeView.getRow(currentEntry));
        copyStaminaCost = getStaminaCost.get(treeView.getRow(currentEntry));
        copyKiRequired = getKiRequired.get(treeView.getRow(currentEntry));
        copyHealthRequired = getHealthRequired.get(treeView.getRow(currentEntry));
        copyTransformationStage = getTransformationStage.get(treeView.getRow(currentEntry));
        copyCusAura = getCusAura.get(treeView.getRow(currentEntry));
        copyRaceGender = getRaceGender.get(treeView.getRow(currentEntry));

        // Copy Unknown fields
        copyUnknown0 = getUnknown0.get(treeView.getRow(currentEntry));
        copyUnknown36 = getUnknown36.get(treeView.getRow(currentEntry));
        copyUnknown68 = getUnknown68.get(treeView.getRow(currentEntry));
        copyUnknown72 = getUnknown72.get(treeView.getRow(currentEntry));
        copyUnknown80 = getUnknown80.get(treeView.getRow(currentEntry));
        copyUnknown88 = getUnknown88.get(treeView.getRow(currentEntry));
        copyUnknown104 = getUnknown104.get(treeView.getRow(currentEntry));
    }
    private void Paste() {
        if (currentEntry == null) {
            return;
        }
        getDirectionalInputs.set(treeView.getRow(currentEntry), copyDirectionalInputs);
        getButtonInputs.set(treeView.getRow(currentEntry), copyButtonInputs);
        getHoldDownConditions.set(treeView.getRow(currentEntry), copyHoldDownConditions);
        getOpponentSizeConditions.set(treeView.getRow(currentEntry), copyOpponentSizeConditions);
        getMinimumLoopDuration.set(treeView.getRow(currentEntry), copyMinimumLoopDuration);
        getMaximumLoopDuration.set(treeView.getRow(currentEntry), copyMaximumLoopDuration);
        getPrimaryActivatorConditions.set(treeView.getRow(currentEntry), copyPrimaryActivatorConditions);
        getActivatorState.set(treeView.getRow(currentEntry), copyActivatorState);
        getBACEntryPrimary.set(treeView.getRow(currentEntry), copyBACEntryPrimary);
        getBACEntryCharge.set(treeView.getRow(currentEntry), copyBACEntryCharge);
        getBACEntryUserConnect.set(treeView.getRow(currentEntry), copyBACEntryUserConnect);
        getBACEntryVictimConnect.set(treeView.getRow(currentEntry), copyBACEntryVictimConnect);
        getBACEntryAirborne.set(treeView.getRow(currentEntry), copyBACEntryAirborne);
        getBACEntryTargetingOverride.set(treeView.getRow(currentEntry), copyBACEntryTargetingOverride);
        getBACRandomFlags.set(treeView.getRow(currentEntry), copyBACRandomFlags);
        getKiCost.set(treeView.getRow(currentEntry), copyKiCost);
        getReceiverLinkId.set(treeView.getRow(currentEntry), copyReceiverLinkId);
        getStaminaCost.set(treeView.getRow(currentEntry), copyStaminaCost);
        getKiRequired.set(treeView.getRow(currentEntry), copyKiRequired);
        getHealthRequired.set(treeView.getRow(currentEntry), copyHealthRequired);
        getTransformationStage.set(treeView.getRow(currentEntry), copyTransformationStage);
        getCusAura.set(treeView.getRow(currentEntry), copyCusAura);
        getRaceGender.set(treeView.getRow(currentEntry), copyRaceGender);
        getUnknown0.set(treeView.getRow(currentEntry), copyUnknown0);
        getUnknown36.set(treeView.getRow(currentEntry), copyUnknown36);
        getUnknown68.set(treeView.getRow(currentEntry), copyUnknown68);
        getUnknown72.set(treeView.getRow(currentEntry), copyUnknown72);
        getUnknown80.set(treeView.getRow(currentEntry), copyUnknown80);
        getUnknown88.set(treeView.getRow(currentEntry), copyUnknown88);
        getUnknown104.set(treeView.getRow(currentEntry), copyUnknown104);

        if (treeView.getSelectionModel().getSelectedItem() != null) {
            tabPane.getTabs().get(0).setContent(createInputsVBox(treeView.getSelectionModel().getSelectedIndex()));
            tabPane.getTabs().get(1).setContent(createActivatorScrollPane(treeView.getSelectionModel().getSelectedIndex()));
            tabPane.getTabs().get(2).setContent(createBACVBox(treeView.getSelectionModel().getSelectedIndex()));
            tabPane.getTabs().get(3).setContent(createMiscVBox(treeView.getSelectionModel().getSelectedIndex()));
            tabPane.getTabs().get(4).setContent(createUnknownVBox(treeView.getSelectionModel().getSelectedIndex()));
        }
        
    }
    private void Delete() {
        if (currentEntry == null || currentEntry.getParent() == null) {
            return;
        }
        getDirectionalInputs.remove(treeView.getRow(currentEntry));
        getButtonInputs.remove(treeView.getRow(currentEntry));
        getHoldDownConditions.remove(treeView.getRow(currentEntry));
        getOpponentSizeConditions.remove(treeView.getRow(currentEntry));
        getMinimumLoopDuration.remove(treeView.getRow(currentEntry));
        getMaximumLoopDuration.remove(treeView.getRow(currentEntry));
        getPrimaryActivatorConditions.remove(treeView.getRow(currentEntry));
        getActivatorState.remove(treeView.getRow(currentEntry));
        getBACEntryPrimary.remove(treeView.getRow(currentEntry));
        getBACEntryCharge.remove(treeView.getRow(currentEntry));
        getBACEntryUserConnect.remove(treeView.getRow(currentEntry));
        getBACEntryVictimConnect.remove(treeView.getRow(currentEntry));
        getBACEntryAirborne.remove(treeView.getRow(currentEntry));
        getBACEntryTargetingOverride.remove(treeView.getRow(currentEntry));
        getBACRandomFlags.remove(treeView.getRow(currentEntry));
        getKiCost.remove(treeView.getRow(currentEntry));
        getReceiverLinkId.remove(treeView.getRow(currentEntry));
        getStaminaCost.remove(treeView.getRow(currentEntry));
        getKiRequired.remove(treeView.getRow(currentEntry));
        getHealthRequired.remove(treeView.getRow(currentEntry));
        getTransformationStage.remove(treeView.getRow(currentEntry));
        getCusAura.remove(treeView.getRow(currentEntry));
        getRaceGender.remove(treeView.getRow(currentEntry));
        getUnknown0.remove(treeView.getRow(currentEntry));
        getUnknown36.remove(treeView.getRow(currentEntry));
        getUnknown68.remove(treeView.getRow(currentEntry));
        getUnknown72.remove(treeView.getRow(currentEntry));
        getUnknown80.remove(treeView.getRow(currentEntry));
        getUnknown88.remove(treeView.getRow(currentEntry));
        getUnknown104.remove(treeView.getRow(currentEntry));

        currentEntry.getParent().getChildren().remove(currentEntry);
        allEntries.remove(treeView.getSelectionModel().getSelectedIndex());

        int[] index = {0};
        renameTreeItems(treeView.getRoot(), index);

    }
    private void Append() {
        TreeItem<String> parent = currentEntry.getParent();
        if (parent != null) {
            TreeItem<String> newEntry = new TreeItem<>("New Entry");
            int currentPos = parent.getChildren().indexOf(currentEntry);
            
            parent.getChildren().add(currentPos + 1, newEntry);

            getDirectionalInputs.add(treeView.getRow(currentEntry) + 1, 0L);
            getButtonInputs.add(treeView.getRow(currentEntry) + 1, 0L);
            getHoldDownConditions.add(treeView.getRow(currentEntry) + 1, 0L);
            getOpponentSizeConditions.add(treeView.getRow(currentEntry) + 1, 0L);
            getMinimumLoopDuration.add(treeView.getRow(currentEntry) + 1, 0);
            getMaximumLoopDuration.add(treeView.getRow(currentEntry) + 1, 0);
            getPrimaryActivatorConditions.add(treeView.getRow(currentEntry) + 1, 0L);
            getActivatorState.add(treeView.getRow(currentEntry) + 1, 0L);
            getBACEntryPrimary.add(treeView.getRow(currentEntry) + 1, 0);
            getBACEntryCharge.add(treeView.getRow(currentEntry) + 1, 0);
            getBACEntryUserConnect.add(treeView.getRow(currentEntry) + 1, 0);
            getBACEntryVictimConnect.add(treeView.getRow(currentEntry) + 1, 0);
            getBACEntryAirborne.add(treeView.getRow(currentEntry) + 1, 0);
            getBACEntryTargetingOverride.add(treeView.getRow(currentEntry) + 1, 0);
            getBACRandomFlags.add(treeView.getRow(currentEntry) + 1, (short)0);
            getKiCost.add(treeView.getRow(currentEntry) + 1, 0.0);
            getReceiverLinkId.add(treeView.getRow(currentEntry) + 1, 0.0);
            getStaminaCost.add(treeView.getRow(currentEntry) + 1, 0.0);
            getKiRequired.add(treeView.getRow(currentEntry) + 1, 0.0);
            getHealthRequired.add(treeView.getRow(currentEntry) + 1, 0.0);
            getTransformationStage.add(treeView.getRow(currentEntry) + 1, 0);
            getCusAura.add(treeView.getRow(currentEntry) + 1, 0);
            getRaceGender.add(treeView.getRow(currentEntry) + 1, 0L);
            getUnknown0.add(treeView.getRow(currentEntry) + 1, 0L);
            getUnknown36.add(treeView.getRow(currentEntry) + 1, (short)0);
            getUnknown68.add(treeView.getRow(currentEntry) + 1, 0L);
            getUnknown72.add(treeView.getRow(currentEntry) + 1, 0L);
            getUnknown80.add(treeView.getRow(currentEntry) + 1, 0L);
            getUnknown88.add(treeView.getRow(currentEntry) + 1, 0L);
            getUnknown104.add(treeView.getRow(currentEntry) + 1, 0L);

            allEntries.add(treeView.getRow(currentEntry) + 1, newEntry);
            int[] index = {0};
            renameTreeItems(treeView.getRoot(), index);
        }
    }
   private void Insert() {
        TreeItem<String> parent = currentEntry.getParent();
        if (parent != null) {
            TreeItem<String> newEntry = new TreeItem<>("New Entry");
            int currentPos = parent.getChildren().indexOf(currentEntry);
            
            parent.getChildren().add(currentPos, newEntry);

            getDirectionalInputs.add(treeView.getRow(currentEntry) - 1, 0L);
            getButtonInputs.add(treeView.getRow(currentEntry) - 1, 0L);
            getHoldDownConditions.add(treeView.getRow(currentEntry) - 1, 0L);
            getOpponentSizeConditions.add(treeView.getRow(currentEntry) - 1, 0L);
            getMinimumLoopDuration.add(treeView.getRow(currentEntry) - 1, 0);
            getMaximumLoopDuration.add(treeView.getRow(currentEntry) - 1, 0);
            getPrimaryActivatorConditions.add(treeView.getRow(currentEntry) - 1, 0L);
            getActivatorState.add(treeView.getRow(currentEntry) - 1, 0L);
            getBACEntryPrimary.add(treeView.getRow(currentEntry) - 1, 0);
            getBACEntryCharge.add(treeView.getRow(currentEntry) - 1, 0);
            getBACEntryUserConnect.add(treeView.getRow(currentEntry) - 1, 0);
            getBACEntryVictimConnect.add(treeView.getRow(currentEntry) - 1, 0);
            getBACEntryAirborne.add(treeView.getRow(currentEntry) - 1, 0);
            getBACEntryTargetingOverride.add(treeView.getRow(currentEntry) - 1, 0);
            getBACRandomFlags.add(treeView.getRow(currentEntry) - 1, (short)0);
            getKiCost.add(treeView.getRow(currentEntry) - 1, 0.0);
            getReceiverLinkId.add(treeView.getRow(currentEntry) - 1, 0.0);
            getStaminaCost.add(treeView.getRow(currentEntry) - 1, 0.0);
            getKiRequired.add(treeView.getRow(currentEntry) - 1, 0.0);
            getHealthRequired.add(treeView.getRow(currentEntry) - 1, 0.0);
            getTransformationStage.add(treeView.getRow(currentEntry) - 1, 0);
            getCusAura.add(treeView.getRow(currentEntry) - 1, 0);
            getRaceGender.add(treeView.getRow(currentEntry) - 1, 0L);
            getUnknown0.add(treeView.getRow(currentEntry) - 1, 0L);
            getUnknown36.add(treeView.getRow(currentEntry) - 1, (short)0);
            getUnknown68.add(treeView.getRow(currentEntry) - 1, 0L);
            getUnknown72.add(treeView.getRow(currentEntry) - 1, 0L);
            getUnknown80.add(treeView.getRow(currentEntry) - 1, 0L);
            getUnknown88.add(treeView.getRow(currentEntry) - 1, 0L);
            getUnknown104.add(treeView.getRow(currentEntry) - 1, 0L);

            allEntries.add(treeView.getRow(currentEntry) - 1, newEntry);

            int[] index = {0};
            renameTreeItems(treeView.getRoot(), index);
        }
    }
    public void AddNewChild() {
        if (currentEntry == null) {
            return;
        }

        TreeItem<String> newChild = new TreeItem<>("New Entry");
        currentEntry.getChildren().add(newChild);
        currentEntry.setExpanded(true);

        // 1. Insert default data into the Inputs lists
        getDirectionalInputs.add(treeView.getRow(currentEntry) + 1, 0L);
        getButtonInputs.add(treeView.getRow(currentEntry) + 1, 0L);
        getHoldDownConditions.add(treeView.getRow(currentEntry) + 1, 0L);

        // 2. Insert default data into the Activator lists
        getOpponentSizeConditions.add(treeView.getRow(currentEntry) + 1, 0L);
        getMinimumLoopDuration.add(treeView.getRow(currentEntry) + 1, 0);
        getMaximumLoopDuration.add(treeView.getRow(currentEntry) + 1, 0);
        getPrimaryActivatorConditions.add(treeView.getRow(currentEntry) + 1, 0L);
        getActivatorState.add(treeView.getRow(currentEntry) + 1, 0L);

        // 3. Insert default data into the BAC lists
        getBACEntryPrimary.add(treeView.getRow(currentEntry) + 1, 0);
        getBACEntryCharge.add(treeView.getRow(currentEntry) + 1, 0);
        getBACEntryUserConnect.add(treeView.getRow(currentEntry) + 1, 0);
        getBACEntryVictimConnect.add(treeView.getRow(currentEntry) + 1, 0);
        getBACEntryAirborne.add(treeView.getRow(currentEntry) + 1, 0);
        getBACEntryTargetingOverride.add(treeView.getRow(currentEntry) + 1, 0);
        getBACRandomFlags.add(treeView.getRow(currentEntry) + 1, (short)0);

        // 4. Insert default data into the Misc lists
        getKiCost.add(treeView.getRow(currentEntry) + 1, 0.0);
        getReceiverLinkId.add(treeView.getRow(currentEntry) + 1, 0.0);
        getStaminaCost.add(treeView.getRow(currentEntry) + 1, 0.0);
        getKiRequired.add(treeView.getRow(currentEntry) + 1, 0.0);
        getHealthRequired.add(treeView.getRow(currentEntry) + 1, 0.0);
        getTransformationStage.add(treeView.getRow(currentEntry) + 1, 0);
        getCusAura.add(treeView.getRow(currentEntry) + 1, 0);
        getRaceGender.add(treeView.getRow(currentEntry) + 1, 0L);

        // 5. Insert default data into the Unknown lists
        getUnknown0.add(treeView.getRow(currentEntry) + 1, 0L);
        getUnknown36.add(treeView.getRow(currentEntry) + 1, (short)0);
        getUnknown68.add(treeView.getRow(currentEntry) + 1, 0L);
        getUnknown72.add(treeView.getRow(currentEntry) + 1, 0L);
        getUnknown80.add(treeView.getRow(currentEntry) + 1, 0L);
        getUnknown88.add(treeView.getRow(currentEntry) + 1, 0L);
        getUnknown104.add(treeView.getRow(currentEntry) + 1, 0L);

        allEntries.add(treeView.getRow(currentEntry) + 1, newChild);

       
        int[] index = {0};
        renameTreeItems(treeView.getRoot(), index);

        
        tabPane.getTabs().get(0).setContent(null);
        tabPane.getTabs().get(1).setContent(null);
        tabPane.getTabs().get(2).setContent(null);
        tabPane.getTabs().get(3).setContent(null);
        tabPane.getTabs().get(4).setContent(null);

        tabPane.getTabs().get(0).setContent(createInputsVBox(treeView.getRow(currentEntry)));
        tabPane.getTabs().get(1).setContent(createActivatorScrollPane(treeView.getRow(currentEntry)));
        tabPane.getTabs().get(2).setContent(createBACVBox(treeView.getRow(currentEntry)));
        tabPane.getTabs().get(3).setContent(createMiscVBox(treeView.getRow(currentEntry)));
        tabPane.getTabs().get(4).setContent(createUnknownVBox(treeView.getRow(currentEntry)));
    }

    public void AddComment(){
        if(currentEntry==null){
            return;
        }

        TextInputDialog textInputDialog=new TextInputDialog();
        textInputDialog.setTitle("Comment");
        textInputDialog.getDialogPane().setContentText("New Comment: ");

        System.out.println(textInputDialog.getEditor().getText());

        textInputDialog.showAndWait().ifPresent(updatedText->{
            currentEntry.setValue(currentEntry.getValue()+" - "+ updatedText);
        });
    }

    public void renameTreeItems(TreeItem<String> item, int[] index) {
        if (item == null) return;

        item.setValue("Entry " + index[0]);
        allEntries.set(index[0], item);
        System.out.println("this index is: "+index[0]);
        System.out.println("array size: "+allEntries.size());
        index[0]++;
        
        for (TreeItem<String> child : item.getChildren()) {
            renameTreeItems(child, index);
        }
    }

    public void bcmReader(Path path){
        try(FileChannel channel=FileChannel.open(path, StandardOpenOption.READ)){
            ByteBuffer intBuffer=ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
            ByteBuffer shortBuffer=ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN);
            int entries;

            channel.position(8);
            intBuffer.clear();
            channel.read(intBuffer);
            intBuffer.flip();
            entries=intBuffer.getInt();
            
            allEntries=new ArrayList<>(entries);
            for(int i=0;i<entries;i++){
                allEntries.add(new TreeItem<>("Entry "+i));
            }

            if(entries>0){
                treeView.setRoot(allEntries.get(0));
            }

            for(int i=0;i<entries;i++){
                
                int entryStartOffset = 16 + (112 * i);
                int siblingOffset = entryStartOffset + 48;
                int childOffset = entryStartOffset + 52;
                
                channel.position(entryStartOffset);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                getUnknown0.add(toUint32(intBuffer.getInt()));

                channel.position(entryStartOffset+4);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                getDirectionalInputs.add(toUint32(intBuffer.getInt()));
                
                channel.position(entryStartOffset+8);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                getButtonInputs.add(toUint32(intBuffer.getInt()));
            
                channel.position(entryStartOffset+12);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                getHoldDownConditions.add(toUint32(intBuffer.getInt()));
                
                
                channel.position(entryStartOffset+16);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                getOpponentSizeConditions.add(toUint32(intBuffer.getInt()));
                
                channel.position(entryStartOffset+20);
                shortBuffer.clear();
                channel.read(shortBuffer);
                shortBuffer.flip();
                getMinimumLoopDuration.add(toUShort(shortBuffer.getShort()));

                channel.position(entryStartOffset+22);
                shortBuffer.clear();
                channel.read(shortBuffer);
                shortBuffer.flip();
                getMaximumLoopDuration.add(toUShort(shortBuffer.getShort()));

                channel.position(entryStartOffset+24);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                getPrimaryActivatorConditions.add(toUint32(intBuffer.getInt()));
                
                channel.position(entryStartOffset+28);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                getActivatorState.add(toUint32(intBuffer.getInt()));
              
                channel.position(entryStartOffset+32);
                shortBuffer.clear();
                channel.read(shortBuffer);
                shortBuffer.flip();
                getBACEntryPrimary.add((int)shortBuffer.getShort());

                channel.position(entryStartOffset+34);
                shortBuffer.clear();
                channel.read(shortBuffer);
                shortBuffer.flip();
                getBACEntryCharge.add((int)shortBuffer.getShort());

                channel.position(entryStartOffset+36);
                shortBuffer.clear();
                channel.read(shortBuffer);
                shortBuffer.flip();
                getUnknown36.add(shortBuffer.getShort());

                channel.position(entryStartOffset+38);
                shortBuffer.clear();
                channel.read(shortBuffer);
                shortBuffer.flip();
                getBACEntryUserConnect.add((int)shortBuffer.getShort());

                channel.position(entryStartOffset+40);
                shortBuffer.clear();
                channel.read(shortBuffer);
                shortBuffer.flip();
                getBACEntryVictimConnect.add((int)shortBuffer.getShort());

                channel.position(entryStartOffset+42);
                shortBuffer.clear();
                channel.read(shortBuffer);
                shortBuffer.flip();
                getBACEntryAirborne.add((int)shortBuffer.getShort());

                channel.position(entryStartOffset+44);
                shortBuffer.clear();
                channel.read(shortBuffer);
                shortBuffer.flip();
                getBACEntryTargetingOverride.add((int)shortBuffer.getShort());
                
                channel.position(entryStartOffset+46);
                shortBuffer.clear();
                channel.read(shortBuffer);
                shortBuffer.flip();
                getBACRandomFlags.add(shortBuffer.getShort());
               
                channel.position(siblingOffset);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                int siblingPointer=intBuffer.getInt();
                if(siblingPointer!=0){
                    TreeItem<String> newSiblingEntry=allEntries.get(i).getParent();
                    if(newSiblingEntry!=null){
                        int siblingEntry=(siblingPointer-16)/112;
                        if(allEntries.get(i).getParent()!=null){
                            allEntries.get(i).getParent().getChildren().add(allEntries.get(siblingEntry));
                        }
                    }
                }
                   
                channel.position(childOffset);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                int childPointer=intBuffer.getInt();
                if(childPointer!=0){
                    int childEntry=(childPointer - 16) / 112;
                    allEntries.get(i).getChildren().add(allEntries.get(childEntry));
                }

                channel.position(entryStartOffset+64);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                getKiCost.add((double)intBuffer.getInt());

                channel.position(entryStartOffset+68);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                getUnknown68.add(toUint32(intBuffer.getInt()));

                channel.position(entryStartOffset+72);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                getUnknown72.add(toUint32(intBuffer.getInt()));

                channel.position(entryStartOffset+76);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                getReceiverLinkId.add((double)toUint32(intBuffer.getInt()));

                channel.position(entryStartOffset+80);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                getUnknown80.add(toUint32(intBuffer.getInt()));

                channel.position(entryStartOffset+84);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                getStaminaCost.add((double)toUint32(intBuffer.getInt()));

                channel.position(entryStartOffset+88);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                getUnknown88.add(toUint32(intBuffer.getInt()));

                channel.position(entryStartOffset+92);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                getKiRequired.add((double)toUint32(intBuffer.getInt()));

                channel.position(entryStartOffset+96);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                getHealthRequired.add((double)intBuffer.getFloat());

                channel.position(entryStartOffset+100);
                shortBuffer.clear();
                channel.read(shortBuffer);
                shortBuffer.flip();
                getTransformationStage.add((int)shortBuffer.getShort());

                channel.position(entryStartOffset+102);
                shortBuffer.clear();
                channel.read(shortBuffer);
                shortBuffer.flip();
                getCusAura.add((int)shortBuffer.getShort());

                channel.position(entryStartOffset+104);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                getUnknown104.add((toUint32(intBuffer.getInt())));
                
                channel.position(entryStartOffset+108);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                getRaceGender.add(toUint32(intBuffer.getInt()));
                
            }
        }
        catch (IOException e) {
            System.err.println(e);
            
        }
    }
    public void bcmWriter(Path path){
        try(FileChannel channel=FileChannel.open(path, StandardOpenOption.WRITE,StandardOpenOption.CREATE,StandardOpenOption.TRUNCATE_EXISTING)){
            ByteBuffer intBuffer=ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
            ByteBuffer shortBuffer=ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN);
            int zeroEntryOffset=16;
            int currentParent=0;
        
            channel.position(0);
            channel.write(ByteBuffer.wrap(new byte[]{0x23,0x42,0x43,(byte)0x4D}));

            channel.position(4);
            channel.write(ByteBuffer.wrap(new byte[]{(byte)0xFE,(byte)0xFF}));

            channel.position(6);
            channel.write(ByteBuffer.wrap(new byte[]{0x00,0x00}));

            channel.position(8);
            intBuffer.clear();
            intBuffer.putInt(allEntries.size());
            intBuffer.flip();
            channel.write(intBuffer);

            channel.position(12);
            intBuffer.clear();
            intBuffer.putInt(zeroEntryOffset);
            intBuffer.flip();
            channel.write(intBuffer);
            
            for(int i=0;i<allEntries.size();i++){
                int entryStartOffset = 16 + (112 * i);
                int siblingOffset = entryStartOffset + 48;
                int childOffset = entryStartOffset + 52;
                int rootParentOffset=entryStartOffset+56;
                int parentOffset=entryStartOffset+60;
                try {
                    channel.position(entryStartOffset);
                    intBuffer.clear();
                    intBuffer.putInt((getUnknown0.get(i).intValue()));
                    intBuffer.flip();
                    channel.write(intBuffer);
                    
                } catch (Exception e) {
                    e.printStackTrace();
                }
                
                channel.position(entryStartOffset+4);
                intBuffer.clear();
                intBuffer.putInt(getDirectionalInputs.get(i).intValue());
                intBuffer.flip();
                channel.write(intBuffer);

                channel.position(entryStartOffset+8);
                intBuffer.clear();
                intBuffer.putInt(getButtonInputs.get(i).intValue());
                intBuffer.flip();
                channel.write(intBuffer);

                channel.position(entryStartOffset+12);
                intBuffer.clear();
                intBuffer.putInt(getHoldDownConditions.get(i).intValue());
                intBuffer.flip();
                channel.write(intBuffer);

                channel.position(entryStartOffset+16);
                intBuffer.clear();
                intBuffer.putInt(getOpponentSizeConditions.get(i).intValue());
                intBuffer.flip();
                channel.write(intBuffer);

                channel.position(entryStartOffset+20);
                shortBuffer.clear();
                shortBuffer.putShort(getMinimumLoopDuration.get(i).shortValue());
                shortBuffer.flip();
                channel.write(shortBuffer);

                channel.position(entryStartOffset+22);
                shortBuffer.clear();
                shortBuffer.putShort(getMaximumLoopDuration.get(i).shortValue());
                shortBuffer.flip();
                channel.write(shortBuffer);

                channel.position(entryStartOffset+24);
                intBuffer.clear();
                intBuffer.putInt(getPrimaryActivatorConditions.get(i).intValue());
                intBuffer.flip();
                channel.write(intBuffer);

                channel.position(entryStartOffset+28);
                intBuffer.clear();
                intBuffer.putInt(getActivatorState.get(i).intValue());
                intBuffer.flip();
                channel.write(intBuffer);

                channel.position(entryStartOffset+32);
                shortBuffer.clear();
                shortBuffer.putShort(getBACEntryPrimary.get(i).shortValue());
                shortBuffer.flip();
                channel.write(shortBuffer);

                channel.position(entryStartOffset+34);
                shortBuffer.clear();
                shortBuffer.putShort(getBACEntryCharge.get(i).shortValue());
                shortBuffer.flip();
                channel.write(shortBuffer);

                channel.position(entryStartOffset+36);
                shortBuffer.clear();
                shortBuffer.putShort((getUnknown36.get(i)));
                shortBuffer.flip();
                channel.write(shortBuffer);

                channel.position(entryStartOffset+38);
                shortBuffer.clear();
                shortBuffer.putShort(getBACEntryUserConnect.get(i).shortValue());
                shortBuffer.flip();
                channel.write(shortBuffer);

                channel.position(entryStartOffset+40);
                shortBuffer.clear();
                shortBuffer.putShort(getBACEntryVictimConnect.get(i).shortValue());
                shortBuffer.flip();
                channel.write(shortBuffer);

                channel.position(entryStartOffset+42);
                shortBuffer.clear();
                shortBuffer.putShort(getBACEntryAirborne.get(i).shortValue());
                shortBuffer.flip();
                channel.write(shortBuffer);

                channel.position(entryStartOffset+44);
                shortBuffer.clear();
                shortBuffer.putShort(getBACEntryTargetingOverride.get(i).shortValue());
                shortBuffer.flip();
                channel.write(shortBuffer);

                channel.position(entryStartOffset+46);
                shortBuffer.clear();
                shortBuffer.putShort(getBACRandomFlags.get(i).shortValue());
                shortBuffer.flip();
                channel.write(shortBuffer);

                channel.position(siblingOffset);
                intBuffer.clear();
                if(allEntries.get(i).nextSibling()!=null){
                    intBuffer.putInt(allEntries.indexOf(allEntries.get(i).nextSibling())*112+16);
                    //System.out.println("the sibling: "+(allEntries.indexOf(allEntries.get(i).nextSibling())*112+16));
                }
                else{
                    intBuffer.putInt(0);
                }
                intBuffer.flip();
                channel.write(intBuffer);

                channel.position(childOffset);
                intBuffer.clear();
                if(!allEntries.get(i).getChildren().isEmpty()){
                    //System.out.println("the children:"+((allEntries.indexOf(allEntries.get(i).getChildren().get(0)))*112+16));
                    intBuffer.putInt((allEntries.indexOf(allEntries.get(i).getChildren().get(0)))*112+16);
                }
                else{
                    intBuffer.putInt(0);
                }
                intBuffer.flip();
                channel.write(intBuffer);

                channel.position(parentOffset);
                intBuffer.clear();
                if(allEntries.indexOf(allEntries.get(i).getParent())==0&&i!=0){
                    intBuffer.putInt(allEntries.indexOf(allEntries.get(i))*112+16);
                    currentParent=allEntries.indexOf(allEntries.get(i))*112+16;
                    intBuffer.flip();
                    channel.write(intBuffer);
                }
                else if(allEntries.indexOf(allEntries.get(i).getParent())!=0&&i!=0){
                    intBuffer.putInt(allEntries.indexOf(allEntries.get(i).getParent())*112+16);
                    intBuffer.flip();
                    channel.write(intBuffer);
                    channel.position(rootParentOffset);
                    intBuffer.clear();
                    intBuffer.putInt(currentParent);
                    intBuffer.flip();
                    channel.write(intBuffer);
                }
                else{
                    intBuffer.putInt(0);
                    intBuffer.flip();
                    channel.write(intBuffer);
                }
             
                channel.position(entryStartOffset+64);
                intBuffer.clear();
                intBuffer.putInt(getKiCost.get(i).intValue());
                intBuffer.flip();
                channel.write(intBuffer);

                channel.position(entryStartOffset+68);
                intBuffer.clear();
                intBuffer.putInt(getUnknown68.get(i).intValue());
                intBuffer.flip();
                channel.write(intBuffer);

                channel.position(entryStartOffset+72);
                intBuffer.clear();
                intBuffer.putInt(getUnknown72.get(i).intValue());
                intBuffer.flip();
                channel.write(intBuffer);

                channel.position(entryStartOffset+76);
                intBuffer.clear();
                intBuffer.putInt(getReceiverLinkId.get(i).intValue());
                intBuffer.flip();
                channel.write(intBuffer);

                channel.position(entryStartOffset+80);
                intBuffer.clear();
                intBuffer.putInt(getUnknown80.get(i).intValue());
                intBuffer.flip();
                channel.write(intBuffer);

                channel.position(entryStartOffset+84);
                intBuffer.clear();
                intBuffer.putInt(getStaminaCost.get(i).intValue());
                intBuffer.flip();
                channel.write(intBuffer);

                channel.position(entryStartOffset+88);
                intBuffer.clear();
                intBuffer.putInt(getUnknown88.get(i).intValue());
                intBuffer.flip();
                channel.write(intBuffer);

                channel.position(entryStartOffset+92);
                intBuffer.clear();
                intBuffer.putInt(getKiRequired.get(i).intValue());
                intBuffer.flip();
                channel.write(intBuffer);

                channel.position(entryStartOffset+96);
                intBuffer.clear();
                intBuffer.putFloat(getHealthRequired.get(i).floatValue());
                intBuffer.flip();
                channel.write(intBuffer);

                channel.position(entryStartOffset+100);
                intBuffer.clear();
                intBuffer.putInt(getTransformationStage.get(i).intValue());
                intBuffer.flip();
                channel.write(intBuffer);

                channel.position(entryStartOffset+102);
                intBuffer.clear();
                intBuffer.putInt(getCusAura.get(i).intValue());
                intBuffer.flip();
                channel.write(intBuffer);

                channel.position(entryStartOffset+104);
                intBuffer.clear();
                intBuffer.putInt(getUnknown104.get(i).intValue());
                intBuffer.flip();
                channel.write(intBuffer);

                channel.position(entryStartOffset+108);
                intBuffer.clear();
                intBuffer.putInt(getRaceGender.get(i).intValue());
                intBuffer.flip();
                channel.write(intBuffer);
            }
        }
         catch (IOException e) {
            System.err.println(e);
        }
    }
}