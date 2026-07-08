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

   ArrayList <BcmEntry> bcmEntries = new ArrayList<>();

    private BcmEntry copyContainer = null;
  
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
    
    public VBox createInputsVBox(BcmEntry entry){
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

        forwardsRD.setSelected((entry.directionalInputs & 1L) != 0);
        backwardsRD.setSelected((entry.directionalInputs & 2L) != 0);
        leftRD.setSelected((entry.directionalInputs & 4L) != 0);
        rightRD.setSelected((entry.directionalInputs & 8L) != 0);

        forwardsRD.selectedProperty().addListener((obs,oldValue,newValue)->{
            if(newValue){
                entry.directionalInputs|=1L;
            }
            else{
                entry.directionalInputs&=~1L;
            }
        });
        backwardsRD.selectedProperty().addListener((obs,oldValue,newValue)->{
            if(newValue){
                entry.directionalInputs|=2L;
            }
            else{
                entry.directionalInputs&=~2L;
            }
        });
        leftRD.selectedProperty().addListener((obs,oldValue,newValue)->{
            if(newValue){
                entry.directionalInputs|=4L;
            }
            else{
                entry.directionalInputs&=~4L;
            }
        });
        rightRD.selectedProperty().addListener((obs,oldValue,newValue)->{
            if(newValue){
                entry.directionalInputs|=8L;
            }
            else{
                entry.directionalInputs&=~8L;
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

        inputActivatedOnceDI.setSelected((entry.directionalInputs & 16L) != 0);
        upDI.setSelected((entry.directionalInputs & 32L) != 0);
        downDI.setSelected((entry.directionalInputs & 64L) != 0);
        rightDI.setSelected((entry.directionalInputs & 128L) != 0);
        leftDI.setSelected((entry.directionalInputs & 256L) != 0);

        inputActivatedOnceDI.selectedProperty().addListener((obs,oldValue,newValue)->{
            if(newValue){
                entry.directionalInputs|=16L;
            }
            else{
                entry.directionalInputs&=~16L;
            }
        });
        upDI.selectedProperty().addListener((obs,oldValue,newValue)->{
            if(newValue){
                entry.directionalInputs|=32L;
            }
            else{
                entry.directionalInputs&=~32L;
            }
        });
        downDI.selectedProperty().addListener((obs,oldValue,newValue)->{
            if(newValue){
                entry.directionalInputs|=64L;
            }
            else{
                entry.directionalInputs&=~64L;
            }
        });
        rightDI.selectedProperty().addListener((obs,oldValue,newValue)->{
            if(newValue){
                entry.directionalInputs|=128L;
            }
            else{
                entry.directionalInputs&=~128L;
            }
        });
        leftDI.selectedProperty().addListener((obs,oldValue,newValue)->{
            if(newValue){
                entry.directionalInputs|=256L;
            }
            else{
                entry.directionalInputs&=~256L;
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

        unknown10.setSelected((entry.directionalInputs & 512L) != 0);
        unknown11.setSelected((entry.directionalInputs & 1024L) != 0);
        unknown12.setSelected((entry.directionalInputs & 2048L) != 0);
        unknown13.setSelected((entry.directionalInputs & 4096L) != 0);
        unknown14.setSelected((entry.directionalInputs & 8192L) != 0);

        unknown10.selectedProperty().addListener((obs,oldValue,newValue)->{
            if(newValue){
                entry.directionalInputs|=512L;
            }
            else{
                entry.directionalInputs&=~512L;
            }
        });
        unknown11.selectedProperty().addListener((obs,oldValue,newValue)->{
            if(newValue){
                entry.directionalInputs|=1024L;
            }
            else{
                entry.directionalInputs&=~1024L;
            }
        });
        unknown12.selectedProperty().addListener((obs,oldValue,newValue)->{
            if(newValue){
                entry.directionalInputs|=2048L;
            }
            else{
                entry.directionalInputs&=~2048L;
            }
        });
        unknown13.selectedProperty().addListener((obs,oldValue,newValue)->{
            if(newValue){
                entry.directionalInputs|=4096L;
            }
            else{
                entry.directionalInputs&=~4096L;
            }
        });
        unknown14.selectedProperty().addListener((obs,oldValue,newValue)->{
            if(newValue){
                entry.directionalInputs|=8192L;
            }
            else{
                entry.directionalInputs&=~8192L;
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

        unknown15.setSelected((entry.directionalInputs & 16384L) != 0);
        unknown16.setSelected((entry.directionalInputs & 32768L) != 0);
        unknown17.setSelected((entry.directionalInputs & 65536L) != 0);
        unknown18.setSelected((entry.directionalInputs & 131072L) != 0);
        unknown19.setSelected((entry.directionalInputs & 262144L) != 0);
        unknown15.selectedProperty().addListener((obs,oldValue,newValue)->{
            if(newValue){
                entry.directionalInputs|=16384L;
            }
            else{
                entry.directionalInputs&=~16384L;
            }
        });
        unknown16.selectedProperty().addListener((obs,oldValue,newValue)->{
            if(newValue){
                entry.directionalInputs|=32768L;
            }
            else{
                entry.directionalInputs&=~32768L;
            }
        });
        unknown17.selectedProperty().addListener((obs,oldValue,newValue)->{
            if(newValue){
                entry.directionalInputs|=65536L;
            }
            else{
                entry.directionalInputs&=~65536L;
            }
        });
        unknown18.selectedProperty().addListener((obs,oldValue,newValue)->{
            if(newValue){
                entry.directionalInputs|=131072L;
            }
            else{
                entry.directionalInputs&=~131072L;
            }
        });
        unknown19.selectedProperty().addListener((obs,oldValue,newValue)->{
            if(newValue){
                entry.directionalInputs|=262144L;
            }
            else{
                entry.directionalInputs&=~262144L;
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

        unknown20.setSelected((entry.directionalInputs & 524288L) != 0);
        unknown21.setSelected((entry.directionalInputs & 1048576L) != 0);
        unknown22.setSelected((entry.directionalInputs & 2097152L) != 0);
        unknown23.setSelected((entry.directionalInputs & 4194304L) != 0);
        unknown24.setSelected((entry.directionalInputs & 8388608L) != 0);

        unknown20.selectedProperty().addListener((obs,oldValue,newValue)->{
            if(newValue){
                entry.directionalInputs|=524288L;
            }
            else{
                entry.directionalInputs&=~524288L;
            }
        });
        unknown21.selectedProperty().addListener((obs,oldValue,newValue)->{
            if(newValue){
                entry.directionalInputs|=1048576L;
            }
            else{
                entry.directionalInputs&=~1048576L;
            }
        });
        unknown22.selectedProperty().addListener((obs,oldValue,newValue)->{
            if(newValue){
                entry.directionalInputs|=2097152L;
            }
            else{
                entry.directionalInputs&=~2097152L;
            }
        });
        unknown23.selectedProperty().addListener((obs,oldValue,newValue)->{
            if(newValue){
                entry.directionalInputs|=4194304L;
            }
            else{
                entry.directionalInputs&=~4194304L;
            }
        });
        unknown24.selectedProperty().addListener((obs,oldValue,newValue)->{
            if(newValue){
                entry.directionalInputs|=8388608L;
            }
            else{
                entry.directionalInputs&=~8388608L;
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

        unknown25.setSelected((entry.directionalInputs & 16777216L) != 0);
        unknown26.setSelected((entry.directionalInputs & 33554432L) != 0);
        unknown27.setSelected((entry.directionalInputs & 67108864L) != 0);
        unknown28.setSelected((entry.directionalInputs & 134217728L) != 0);
        unknown29.setSelected((entry.directionalInputs & 268435456L) != 0);

        unknown25.selectedProperty().addListener((obs,oldValue,newValue)->{
            if(newValue){
                entry.directionalInputs|=16777216L;
            }
            else{
                entry.directionalInputs&=~16777216L;
            }
        });
        unknown26.selectedProperty().addListener((obs,oldValue,newValue)->{
            if(newValue){
                entry.directionalInputs|=33554432L;
            }
            else{
                entry.directionalInputs&=~33554432L;
            }
        });
        unknown27.selectedProperty().addListener((obs,oldValue,newValue)->{
            if(newValue){
                entry.directionalInputs|=67108864L;
            }
            else{
                entry.directionalInputs&=~67108864L;
            }
        });
        unknown28.selectedProperty().addListener((obs,oldValue,newValue)->{
            if(newValue){
                entry.directionalInputs|=134217728L;
            }
            else{
                entry.directionalInputs&=~134217728L;
            }
        });
        unknown29.selectedProperty().addListener((obs,oldValue,newValue)->{
            if(newValue){
                entry.directionalInputs|=268435456L;
            }
            else{
                entry.directionalInputs&=~268435456L;
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

        unknown30.setSelected((entry.directionalInputs & 536870912L) != 0);
        unknown31.setSelected((entry.directionalInputs & 1073741824L) != 0);
        unknown32.setSelected((entry.directionalInputs & 2147483648L) != 0);

        unknown30.selectedProperty().addListener((obs,oldValue,newValue)->{
            if(newValue){
                entry.directionalInputs|=536870912L;
            }
            else{
                entry.directionalInputs&=~536870912L;
            }
        });
        unknown31.selectedProperty().addListener((obs,oldValue,newValue)->{
            if(newValue){
                entry.directionalInputs|=1073741824L;
            }
            else{
                entry.directionalInputs&=~1073741824L;
            }
        });
        unknown32.selectedProperty().addListener((obs,oldValue,newValue)->{
            if(newValue){
                entry.directionalInputs|=2147483648L;
            }
            else{
                entry.directionalInputs&=~2147483648L;
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

        light.setSelected((entry.buttonInputs & 1L) != 0);
        heavy.setSelected((entry.buttonInputs & 2L) != 0);
        blast.setSelected((entry.buttonInputs& 4L) != 0);
        jump.setSelected((entry.buttonInputs & 8L) != 0);

        // 0x1 Group
        light.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) { 
                entry.buttonInputs |= 1L; 
            }
            else { 
                entry.buttonInputs &= ~1L; 
            }
        });
        heavy.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) { 
                entry.buttonInputs |= 2L; 
            }
            else { 
                entry.buttonInputs &= ~2L; 
            }
        });
        blast.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) { 
                entry.buttonInputs |= 4L; 
            }
            else { 
                entry.buttonInputs &= ~4L; 
            }
        });
        jump.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) { 
                entry.buttonInputs |= 8L; 
            }
            else { 
                entry.buttonInputs &= ~8L; 
            }
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

        skillMenu.setSelected((entry.buttonInputs & 16L) != 0);
        boost.setSelected((entry.buttonInputs & 32L) != 0);
        guard.setSelected((entry.buttonInputs & 64L) != 0);
        unknown8.setSelected((entry.buttonInputs & 128L) != 0);

        skillMenu.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.buttonInputs |= 16L;
            } else {
                entry.buttonInputs &= ~16L;
            }
        });
        boost.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.buttonInputs |= 32L;
            } else {
                entry.buttonInputs &= ~32L;
            }
        });
        guard.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.buttonInputs |= 64L;
            } else {
                entry.buttonInputs &= ~64L;
            }
        });
        unknown8.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.buttonInputs |= 128L;
            } else {
                entry.buttonInputs &= ~128L;
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

        superSkill1.setSelected((entry.buttonInputs & 256L) != 0);
        superSkill2.setSelected((entry.buttonInputs & 512L) != 0);
        superSkill3.setSelected((entry.buttonInputs & 1024L) != 0);
        superSkill4.setSelected((entry.buttonInputs & 2048L) != 0);

        superSkill1.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.buttonInputs |= 256L;
            } else {
                entry.buttonInputs &= ~256L;
            }
        });
        superSkill2.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.buttonInputs |= 512L;
            } else {
                entry.buttonInputs &= ~512L;
            }
        });
        superSkill3.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.buttonInputs |= 1024L;
            } else {
                entry.buttonInputs &= ~1024L;
            }
        });
        superSkill4.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.buttonInputs |= 2048L;
            } else {
                entry.buttonInputs &= ~2048L;
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

        ultimateSkill1.setSelected((entry.buttonInputs & 4096L) != 0);
        ultimateSkill2.setSelected((entry.buttonInputs & 8192L) != 0);
        awokenSkill.setSelected((entry.buttonInputs & 16384L) != 0);
        evasiveSkill.setSelected((entry.buttonInputs & 32768L) != 0);

        ultimateSkill1.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.buttonInputs |= 4096L;
            } else {
                entry.buttonInputs &= ~4096L;
            }
        });
        ultimateSkill2.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.buttonInputs |= 8192L;
            } else {
                entry.buttonInputs &= ~8192L;
            }
        });
        awokenSkill.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.buttonInputs |= 16384L;
            } else {
                entry.buttonInputs &= ~16384L;
            }
        });
        evasiveSkill.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.buttonInputs |= 32768L;
            } else {
                entry.buttonInputs &= ~32768L;
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

        skillInput.setSelected((entry.buttonInputs & 65536L) != 0);
        superMenuPlusSkillInput.setSelected((entry.buttonInputs & 131072L) != 0);
        ultimateMenuPlusSkillInput.setSelected((entry.buttonInputs & 262144L) != 0);
        unknown20Button.setSelected((entry.buttonInputs & 524288L) != 0);

        skillInput.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.buttonInputs |= 65536L;
            } else {
                entry.buttonInputs &= ~65536L;
            }
        });
        superMenuPlusSkillInput.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.buttonInputs |= 131072L;
            } else {
                entry.buttonInputs &= ~131072L;
            }
        });
        ultimateMenuPlusSkillInput.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.buttonInputs |= 262144L;
            } else {
                entry.buttonInputs &= ~262144L;
            }
        });
        unknown20Button.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.buttonInputs |= 524288L;
            } else {
                entry.buttonInputs &= ~524288L;
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

        lockedON.setSelected((entry.buttonInputs & 1048576L) != 0);
        descend.setSelected((entry.buttonInputs & 2097152L) != 0);
        dragonRadar.setSelected((entry.buttonInputs & 4194304L) != 0);
        jump2.setSelected((entry.buttonInputs & 8388608L) != 0);

        lockedON.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.buttonInputs |= 1048576L;
            } else {
                entry.buttonInputs &= ~1048576L;
            }
        });
        descend.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.buttonInputs |= 2097152L;
            } else {
                entry.buttonInputs &= ~2097152L;
            }
        });
        dragonRadar.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.buttonInputs |= 4194304L;
            } else {
                entry.buttonInputs &= ~4194304L;
            }
        });
        jump2.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.buttonInputs |= 8388608L;
            } else {
                entry.buttonInputs &= ~8388608L;
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

        ultimateMenu.setSelected((entry.buttonInputs & 16777216L) != 0);
        unknown26ButtonInput.setSelected((entry.buttonInputs & 33554432L) != 0);
        unknown27ButtonInput.setSelected((entry.buttonInputs & 67108864L) != 0);
        unknown28ButtonInput.setSelected((entry.buttonInputs & 134217728L) != 0);

        ultimateMenu.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.buttonInputs |= 16777216L;
            } else {
                entry.buttonInputs &= ~16777216L;
            }
        });
        unknown26ButtonInput.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.buttonInputs |= 33554432L;
            } else {
                entry.buttonInputs &= ~33554432L;
            }
        });
        unknown27ButtonInput.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.buttonInputs |= 67108864L;
            } else {
                entry.buttonInputs &= ~67108864L;
            }
        });
        unknown28ButtonInput.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.buttonInputs |= 134217728L;
            } else {
                entry.buttonInputs &= ~134217728L;
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

        ultimateMenu2.setSelected((entry.buttonInputs & 268435456L) != 0);
        unknown30ButtonInput.setSelected((entry.buttonInputs & 536870912L) != 0);
        unknown31ButtonInput.setSelected((entry.buttonInputs & 1073741824L) != 0);
        unknown32ButtonInput.setSelected((entry.buttonInputs & 2147483648L) != 0);

        ultimateMenu2.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.buttonInputs |= 268435456L;
            } else {
                entry.buttonInputs &= ~268435456L;
            }
        });
        unknown30ButtonInput.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.buttonInputs |= 536870912L;
            } else {
                entry.buttonInputs &= ~536870912L;
            }
        });
        unknown31ButtonInput.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.buttonInputs |= 1073741824L;
            } else {
                entry.buttonInputs &= ~1073741824L;
            }
        });
        unknown32ButtonInput.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.buttonInputs |= 2147483648L;
            } else {
                entry.buttonInputs &= ~2147483648L;
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
        delayUntilReleased.setSelected((entry.holdDownConditions & 1L) != 0);       
        unknown2HDC.setSelected((entry.holdDownConditions & 2L) != 0);              
        stopSkillFromActivating.setSelected((entry.holdDownConditions& 4L) != 0);  
        unknown4HDC.setSelected((entry.holdDownConditions & 8L) != 0);

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
                entry.holdDownConditions |= 1L;
            } else {
                entry.holdDownConditions &= ~1L;
            }
        });
        unknown2HDC.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.holdDownConditions |= 2L;
            } else {
                entry.holdDownConditions &= ~2L;
            }
        });
        stopSkillFromActivating.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.holdDownConditions |= 4L;
            } else {
                entry.holdDownConditions &= ~4L;
            }
        });
        unknown4HDC.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.holdDownConditions |= 8L;
            } else {
                entry.holdDownConditions &= ~8L;
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

        unknown5HDC.setSelected((entry.holdDownConditions & 16L) != 0);   
        unknown6HDC.setSelected((entry.holdDownConditions & 32L) != 0);      
        unknown7HDC.setSelected((entry.holdDownConditions & 64L) != 0);       
        unknown8HDC.setSelected((entry.holdDownConditions & 128L) != 0);     

        unknown5HDC.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.holdDownConditions |= 16L;
            } else {
                entry.holdDownConditions &= ~16L;
            }
        });
        unknown6HDC.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.holdDownConditions |= 32L;
            } else {
                entry.holdDownConditions &= ~32L;
            }
        });
        unknown7HDC.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.holdDownConditions |= 64L;
            } else {
                entry.holdDownConditions &= ~64L;
            }
        });
        unknown8HDC.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.holdDownConditions |= 128L;
            } else {
                entry.holdDownConditions &= ~128L;
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

        unknown9HDC.setSelected((entry.holdDownConditions & 256L) != 0);   
        unknown10HDC.setSelected((entry.holdDownConditions & 512L) != 0);     
        unknown11HDC.setSelected((entry.holdDownConditions & 1024L) != 0);    
        unknown12HDC.setSelected((entry.holdDownConditions & 2048L) != 0);    
   

        unknown9HDC.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.holdDownConditions |= 256L;
            } else {
                entry.holdDownConditions &= ~256L;
            }
        });
        unknown10HDC.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.holdDownConditions |= 512L;
            } else {
                entry.holdDownConditions &= ~512L;
            }
        });
        unknown11HDC.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.holdDownConditions |= 1024L;
            } else {
                entry.holdDownConditions &= ~1024L;
            }
        });
        unknown12HDC.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.holdDownConditions |= 2048L;
            } else {
                entry.holdDownConditions &= ~2048L;
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

        unknown13HDC.setSelected((entry.holdDownConditions & 4096L) != 0);    
        unknown14HDC.setSelected((entry.holdDownConditions & 8192L) != 0);   
        unknown15HDC.setSelected((entry.holdDownConditions & 16384L) != 0);   
        unknown16HDC.setSelected((entry.holdDownConditions & 32768L) != 0);   

        unknown13HDC.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.holdDownConditions |= 4096L;
            } else {
                entry.holdDownConditions &= ~4096L;
            }
        });
        unknown14HDC.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.holdDownConditions |= 8192L;
            } else {
                entry.holdDownConditions &= ~8192L;
            }
        });
        unknown15HDC.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.holdDownConditions |= 16384L;
            } else {
                entry.holdDownConditions &= ~16384L;
            }
        });
        unknown16HDC.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.holdDownConditions |= 32768L;
            } else {
                entry.holdDownConditions &= ~32768L;
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

        manual.setSelected((entry.holdDownConditions & 65536L) != 0);   
        unknown18HDC.setSelected((entry.holdDownConditions & 131072L) != 0);  
        unknown19HDC.setSelected((entry.holdDownConditions & 262144L) != 0);  
        unknown20HDC.setSelected((entry.holdDownConditions & 524288L) != 0);  

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
                entry.holdDownConditions |= 65536L;
            } else {
                entry.holdDownConditions &= ~65536L;
            }
        });
        unknown18HDC.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.holdDownConditions |= 131072L;
            } else {
                entry.holdDownConditions &= ~131072L;
            }
        });
        unknown19HDC.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.holdDownConditions |= 262144L;
            } else {
                entry.holdDownConditions &= ~262144L;
            }
        });
        unknown20HDC.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.holdDownConditions |= 524288L;
            } else {
                entry.holdDownConditions &= ~524288L;
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

        //Options 6
        Label option6Label = new Label("Option 6");
        option6Label.getStyleClass().add("titled-address-label");


        CheckBox unknown21HDC = new CheckBox("Unknown 21");
        CheckBox unknown22HDC = new CheckBox("Unknown 22");
        CheckBox unknown23HDC = new CheckBox("Unknown 23");
        CheckBox unknown24HDC = new CheckBox("Unknown 24");

        unknown21HDC.setSelected((entry.holdDownConditions & 1048576L) != 0); 
        unknown22HDC.setSelected((entry.holdDownConditions & 2097152L) != 0); 
        unknown23HDC.setSelected((entry.holdDownConditions & 4194304L) != 0); 
        unknown24HDC.setSelected((entry.holdDownConditions & 8388608L) != 0); 

        unknown21HDC.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.holdDownConditions |= 1048576L;
            } else {
                entry.holdDownConditions &= ~1048576L;
            }
        });
        unknown22HDC.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.holdDownConditions |= 2097152L;
            } else {
                entry.holdDownConditions &= ~2097152L;
            }
        });
        unknown23HDC.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.holdDownConditions |= 4194304L;
            } else {
                entry.holdDownConditions &= ~4194304L;
            }
        });
        unknown24HDC.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.holdDownConditions |= 8388608L;
            } else {
                entry.holdDownConditions &= ~8388608L;
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

        unknown25HDC.setSelected((entry.holdDownConditions & 16777216L) != 0);
        unknown26HDC.setSelected((entry.holdDownConditions & 33554432L) != 0);
        unknown27HDC.setSelected((entry.holdDownConditions & 67108864L) != 0);
        unknown28HDC.setSelected((entry.holdDownConditions & 134217728L) != 0);

        unknown25HDC.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.holdDownConditions |= 16777216L;
            } else {
                entry.holdDownConditions &= ~16777216L;
            }
        });
        unknown26HDC.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.holdDownConditions |= 33554432L;
            } else {
                entry.holdDownConditions &= ~33554432L;
            }
        });
        unknown27HDC.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.holdDownConditions |= 67108864L;
            } else {
                entry.holdDownConditions &= ~67108864L;
            }
        });
        unknown28HDC.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.holdDownConditions |= 134217728L;
            } else {
                entry.holdDownConditions &= ~134217728L;
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

        unknown29HDC.setSelected((entry.holdDownConditions & 268435456L) != 0);
        unknown30HDC.setSelected((entry.holdDownConditions & 536870912L) != 0);
        unknown31HDC.setSelected((entry.holdDownConditions & 1073741824L) != 0);
        unknown32HDC.setSelected((entry.holdDownConditions & 2147483648L) != 0);

        unknown29HDC.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.holdDownConditions |= 268435456L;
            } else {
                entry.holdDownConditions &= ~268435456L;
            }
        });
        unknown30HDC.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.holdDownConditions |= 536870912L;
            } else {
                entry.holdDownConditions &= ~536870912L;
            }
        });
        unknown31HDC.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.holdDownConditions |= 1073741824L;
            } else {
                entry.holdDownConditions &= ~1073741824L;
            }
        });
        unknown32HDC.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.holdDownConditions |= 2147483648L;
            } else {
                entry.holdDownConditions &= ~2147483648L;
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

    private ScrollPane createActivatorScrollPane(BcmEntry entry){
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

        unknown1OpponentSizeConditions.setSelected((entry.opponentSizeConditions & 1L) != 0);          
        unknown2OpponentSizeConditions.setSelected((entry.opponentSizeConditions & 2L) != 0);          
        unknown3OpponentSizeConditions.setSelected((entry.opponentSizeConditions & 4L) != 0);         
        unknown4OpponentSizeConditions.setSelected((entry.opponentSizeConditions & 8L) != 0);     
        
        unknown1OpponentSizeConditions.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.opponentSizeConditions |= 1L;
            } else {
                entry.opponentSizeConditions &= ~1L;
            }
        });
        unknown2OpponentSizeConditions.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.opponentSizeConditions |= 2L;
            } else {
                entry.opponentSizeConditions &= ~2L;
            }
        });
        unknown3OpponentSizeConditions.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.opponentSizeConditions |= 4L;
            } else {
                entry.opponentSizeConditions &= ~4L;
            }
        });
        unknown4OpponentSizeConditions.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.opponentSizeConditions |= 8L;
            } else {
                entry.opponentSizeConditions &= ~8L;
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

        unknown5OpponentSizeConditions.setSelected((entry.opponentSizeConditions & 16L) != 0);       
        unknown6OpponentSizeConditions.setSelected((entry.opponentSizeConditions & 32L) != 0);         
        unknown7OpponentSizeConditions.setSelected((entry.opponentSizeConditions & 64L) != 0);         
        unknown8OpponentSizeConditions.setSelected((entry.opponentSizeConditions & 128L) != 0);        

        unknown5OpponentSizeConditions.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.opponentSizeConditions |= 16L;
            } else {
                entry.opponentSizeConditions &= ~16L;
            }
        });
        unknown6OpponentSizeConditions.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.opponentSizeConditions |= 32L;
            } else {
                entry.opponentSizeConditions &= ~32L;
            }
        });
        unknown7OpponentSizeConditions.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.opponentSizeConditions |= 64L;
            } else {
                entry.opponentSizeConditions &= ~64L;
            }
        });
        unknown8OpponentSizeConditions.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.opponentSizeConditions|= 128L;
            } else {
                entry.opponentSizeConditions &= ~128L;
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

        unknown9OpponentSizeConditions.setSelected((entry.opponentSizeConditions & 256L) != 0);        
        unknown10OpponentSizeConditions.setSelected((entry.opponentSizeConditions & 512L) != 0);       
        unknown11OpponentSizeConditions.setSelected((entry.opponentSizeConditions & 1024L) != 0);     
        unknown12OpponentSizeConditions.setSelected((entry.opponentSizeConditions & 2048L) != 0);    
        
        unknown9OpponentSizeConditions.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.opponentSizeConditions |= 256L;
            } else {
                entry.opponentSizeConditions &= ~256L;
            }
        });
        unknown10OpponentSizeConditions.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.opponentSizeConditions |= 512L;
            } else {
                entry.opponentSizeConditions &= ~512L;
            }
        });
        unknown11OpponentSizeConditions.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.opponentSizeConditions |= 1024L;
            } else {
                entry.opponentSizeConditions &= ~1024L;
            }
        });
        unknown12OpponentSizeConditions.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.opponentSizeConditions |= 2048L;
            } else {
                entry.opponentSizeConditions &= ~2048L;
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

        unknown13OpponentSizeConditions.setSelected((entry.opponentSizeConditions & 4096L) != 0);      
        unknown14OpponentSizeConditions.setSelected((entry.opponentSizeConditions & 8192L) != 0);      
        unknown15OpponentSizeConditions.setSelected((entry.opponentSizeConditions & 16384L) != 0);     
        unknown16OpponentSizeConditions.setSelected((entry.opponentSizeConditions & 32768L) != 0);         

        unknown13OpponentSizeConditions.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.opponentSizeConditions |= 4096L;
            } else {
                entry.opponentSizeConditions &= ~4096L;
            }
        });
        unknown14OpponentSizeConditions.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.opponentSizeConditions |= 8192L;
            } else {
                entry.opponentSizeConditions &= ~8192L;
            }
        });
        unknown15OpponentSizeConditions.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.opponentSizeConditions |= 16384L;
            } else {
                entry.opponentSizeConditions &= ~16384L;
            }
        });
        unknown16OpponentSizeConditions.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.opponentSizeConditions |= 32768L;
            } else {
                entry.opponentSizeConditions &= ~32768L;
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

        unknown17OpponentSizeConditions.setSelected((entry.opponentSizeConditions & 65536L) != 0);   
        unknown18OpponentSizeConditions.setSelected((entry.opponentSizeConditions & 131072L) != 0);    
        unknown19OpponentSizeConditions.setSelected((entry.opponentSizeConditions & 262144L) != 0);   
        unknown20OpponentSizeConditions.setSelected((entry.opponentSizeConditions & 524288L) != 0); 

        unknown17OpponentSizeConditions.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.opponentSizeConditions |= 65536L;
            } else {
                entry.opponentSizeConditions &= ~65536L;
            }
        });
        unknown18OpponentSizeConditions.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.opponentSizeConditions |= 131072L;
            } else {
                entry.opponentSizeConditions &= ~131072L;
            }
        });
        unknown19OpponentSizeConditions.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.opponentSizeConditions |= 262144L;
            } else {
               entry.opponentSizeConditions &= ~262144L;
            }
        });
        unknown20OpponentSizeConditions.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.opponentSizeConditions |= 524288L;
            } else {
                entry.opponentSizeConditions &= ~524288L;
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

        unknown21OpponentSizeConditions.setSelected((entry.opponentSizeConditions & 1048576L) != 0);
        unknown22OpponentSizeConditions.setSelected((entry.opponentSizeConditions & 2097152L) != 0);  
        unknown23OpponentSizeConditions.setSelected((entry.opponentSizeConditions & 4194304L) != 0);   
        unknown24OpponentSizeConditions.setSelected((entry.opponentSizeConditions & 8388608L) != 0);   

        unknown21OpponentSizeConditions.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.opponentSizeConditions |= 1048576L;
            } else {
                entry.opponentSizeConditions &= ~1048576L;
            }
        });
        unknown22OpponentSizeConditions.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.opponentSizeConditions |= 2097152L;
            } else {
                entry.opponentSizeConditions &= ~2097152L;
            }
        });
        unknown23OpponentSizeConditions.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.opponentSizeConditions |= 4194304L;
            } else {
                entry.opponentSizeConditions &= ~4194304L;
            }
        });
        unknown24OpponentSizeConditions.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.opponentSizeConditions |= 8388608L;
            } else {
                entry.opponentSizeConditions &= ~8388608L;
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

        unknown25OpponentSizeConditions.setSelected((entry.opponentSizeConditions & 16777216L) != 0); 
        unknown26OpponentSizeConditions.setSelected((entry.opponentSizeConditions & 33554432L) != 0);  
        unknown27OpponentSizeConditions.setSelected((entry.opponentSizeConditions & 67108864L) != 0);  
        unknown28OpponentSizeConditions.setSelected((entry.opponentSizeConditions & 134217728L) != 0); 

        unknown25OpponentSizeConditions.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.opponentSizeConditions |= 16777216L;
            } else {
                entry.opponentSizeConditions &= ~16777216L;
            }
        });
        unknown26OpponentSizeConditions.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.opponentSizeConditions |= 33554432L;
            } else {
                entry.opponentSizeConditions &= ~33554432L;
            }
        });
        unknown27OpponentSizeConditions.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.opponentSizeConditions |= 67108864L;
            } else {
                entry.opponentSizeConditions &= ~67108864L;
            }
        });
        unknown28OpponentSizeConditions.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.opponentSizeConditions |= 134217728L;
            } else {
                entry.opponentSizeConditions &= ~134217728L;
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

        unknown29OpponentSizeConditions.setSelected((entry.opponentSizeConditions & 268435456L) != 0); 
        unknown30OpponentSizeConditions.setSelected((entry.opponentSizeConditions & 536870912L) != 0);
        unknown31OpponentSizeConditions.setSelected((entry.opponentSizeConditions & 1073741824L) != 0);
        unknown32OpponentSizeConditions.setSelected((entry.opponentSizeConditions & 2147483648L) != 0);

        unknown29OpponentSizeConditions.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.opponentSizeConditions |= 268435456L;
            } else {
                entry.opponentSizeConditions &= ~268435456L;
            }
        });
        unknown30OpponentSizeConditions.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.opponentSizeConditions |= 536870912L;
            } else {
                entry.opponentSizeConditions &= ~536870912L;
            }
        });
        unknown31OpponentSizeConditions.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.opponentSizeConditions |= 1073741824L;
            } else {
                entry.opponentSizeConditions &= ~1073741824L;
            }
        });
        unknown32OpponentSizeConditions.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.opponentSizeConditions |= 2147483648L;
            } else {
                entry.opponentSizeConditions &= ~2147483648L;
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

        Spinner<Integer> minimumLoopDurationSpinner=new Spinner<>(0,65535,entry.minimumLoopDuration);
        minimumLoopDurationSpinner.setEditable(true);
        minimumLoopDurationSpinner.valueProperty().addListener((obs,oldValue,newValue)->{
            if(newValue!=null){
                entry.minimumLoopDuration = newValue;
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
        
        Spinner<Integer> maximumLoopDurationSpinner=new Spinner<>(0,65535,entry.maximumLoopDuration);
        maximumLoopDurationSpinner.setEditable(true);
        maximumLoopDurationSpinner.valueProperty().addListener((obs,oldValue,newValue)->{
            if(newValue!=null){
                entry.maximumLoopDuration = newValue;
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

        standing.setSelected((entry.primaryActivatorConditions & 1L) != 0);
        floating.setSelected((entry.primaryActivatorConditions & 2L) != 0);
        touchingGround.setSelected((entry.primaryActivatorConditions & 4L) != 0);
        onAttackHit.setSelected((entry.primaryActivatorConditions & 8L) != 0);

        standing.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.primaryActivatorConditions |= 1L;
            } else {
                entry.primaryActivatorConditions &= ~1L;
            }
        });
        floating.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.primaryActivatorConditions |= 2L;
            } else {
                entry.primaryActivatorConditions &= ~2L;
            }
        });
        touchingGround.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.primaryActivatorConditions |= 4L;
            } else {
                entry.primaryActivatorConditions &= ~4L;
            }
        });
        onAttackHit.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.primaryActivatorConditions |= 8L;
            } else {
                entry.primaryActivatorConditions &= ~8L;
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

        attackBlocked.setSelected((entry.primaryActivatorConditions & 16L) != 0);
        closeToTarget.setSelected((entry.primaryActivatorConditions & 32L) != 0);
        farFromTarget.setSelected((entry.primaryActivatorConditions & 64L) != 0);
        inBaseForm.setSelected((entry.primaryActivatorConditions & 128L) != 0);

        attackBlocked.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.primaryActivatorConditions |= 16L;
            } else {
                entry.primaryActivatorConditions &= ~16L;
            }
        });
        closeToTarget.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.primaryActivatorConditions |= 32L;
            } else {
                entry.primaryActivatorConditions &= ~32L;
            }
        });
        farFromTarget.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.primaryActivatorConditions |= 64L;
            } else {
                entry.primaryActivatorConditions &= ~64L;
            }
        });
        inBaseForm.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.primaryActivatorConditions |= 128L;
            } else {
                entry.primaryActivatorConditions &= ~128;
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

        inTransformedState.setSelected((entry.primaryActivatorConditions & 256L) != 0);
        flashOnOffUnlessTargeting.setSelected((entry.primaryActivatorConditions & 512L) != 0);
        unknown11PrimaryActivatorConditions.setSelected((entry.primaryActivatorConditions & 1024L) != 0);
        idle.setSelected((entry.primaryActivatorConditions & 2048L) != 0);

        inTransformedState.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.primaryActivatorConditions |= 256L;
            } else {
                entry.primaryActivatorConditions &= ~256L;
            }
        });
        flashOnOffUnlessTargeting.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.primaryActivatorConditions |= 512L;
            } else {
                entry.primaryActivatorConditions &= ~512L;
            }
        });
        unknown11PrimaryActivatorConditions.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.primaryActivatorConditions |= 1024L;
            } else {
                entry.primaryActivatorConditions &= ~1024L;
            }
        });
        idle.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.primaryActivatorConditions |= 2048L;
            } else {
                entry.primaryActivatorConditions &= ~2048L;
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

        counterMelee.setSelected((entry.primaryActivatorConditions & 4096L) != 0);
        counterProjectile.setSelected((entry.primaryActivatorConditions & 8192L) != 0);
        kiBelow100.setSelected((entry.primaryActivatorConditions & 16384L) != 0);
        kiAbove0.setSelected((entry.primaryActivatorConditions & 32768L) != 0);

        counterMelee.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.primaryActivatorConditions |= 4096L;
            } else {
                entry.primaryActivatorConditions &= ~4096L;
            }
        });
        counterProjectile.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.primaryActivatorConditions |= 8192L;
            } else {
                entry.primaryActivatorConditions &= ~8192L;
            }
        });
        kiBelow100.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.primaryActivatorConditions |= 16384L;
            } else {
                entry.primaryActivatorConditions &= ~16384L;
            }
        });
        kiAbove0.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.primaryActivatorConditions |= 32768L;
            } else {
                entry.primaryActivatorConditions &= ~32768L;
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

        unknown17PrimaryActivatorConditions.setSelected((entry.primaryActivatorConditions & 65536L) != 0);
        unknown18PrimaryActivatorConditions.setSelected((entry.primaryActivatorConditions & 131072L) != 0);
        ground.setSelected((entry.primaryActivatorConditions & 262144L) != 0);
        opponent.setSelected((entry.primaryActivatorConditions & 524288L) != 0);

        unknown17PrimaryActivatorConditions.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.primaryActivatorConditions |= 65536L;
            } else {
                entry.primaryActivatorConditions &= ~65536L;
            }
        });
        unknown18PrimaryActivatorConditions.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.primaryActivatorConditions |= 131072L;
            } else {
                entry.primaryActivatorConditions &= ~131072L;
            }
        });
        ground.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.primaryActivatorConditions |= 262144L;
            } else {
                entry.primaryActivatorConditions &= ~262144L;
            }
        });
        opponent.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.primaryActivatorConditions |= 524288L;
            } else {
                entry.primaryActivatorConditions &= ~524288L;
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

        opponentKnockback.setSelected((entry.primaryActivatorConditions & 1048576L) != 0);
        unknown22PrimaryActivatorConditions.setSelected((entry.primaryActivatorConditions & 2097152L) != 0);
        targetingOpponent.setSelected((entry.primaryActivatorConditions & 4194304L) != 0);
        unknown24PrimaryActivatorConditions.setSelected((entry.primaryActivatorConditions & 8388608L) != 0);

        opponentKnockback.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.primaryActivatorConditions |= 1048576L;
            } else {
                entry.primaryActivatorConditions &= ~1048576L;
            }
        });
        unknown22PrimaryActivatorConditions.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.primaryActivatorConditions |= 2097152L;
            } else {
                entry.primaryActivatorConditions &= ~2097152L;
            }
        });
        targetingOpponent.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.primaryActivatorConditions |= 4194304L;
            } else {
                entry.primaryActivatorConditions &= ~4194304L;
            }
        });
        unknown24PrimaryActivatorConditions.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.primaryActivatorConditions |= 8388608L;
            } else {
                entry.primaryActivatorConditions &= ~8388608L;
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

        activateProjectile.setSelected((entry.primaryActivatorConditions & 16777216L) != 0);
        staminaAboveZero.setSelected((entry.primaryActivatorConditions & 33554432L) != 0);
        notNearStageCeiling.setSelected((entry.primaryActivatorConditions & 67108864L) != 0);
        notNearCertainObjects.setSelected((entry.primaryActivatorConditions & 134217728L) != 0);

        activateProjectile.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.primaryActivatorConditions |= 16777216L;
            } else {
                entry.primaryActivatorConditions &= ~16777216L;
            }
        });
        staminaAboveZero.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.primaryActivatorConditions |= 33554432L;
            } else {
                entry.primaryActivatorConditions &= ~33554432L;
            }
        });
        notNearStageCeiling.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.primaryActivatorConditions |= 67108864L;
            } else {
                entry.primaryActivatorConditions &= ~67108864L;
            }
        });
        notNearCertainObjects.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.primaryActivatorConditions |= 134217728L;
            } else {
                entry.primaryActivatorConditions &= ~134217728L;
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
        Label healthLabel = new Label("Health");
        healthLabel.getStyleClass().add("titled-address-label");

        CheckBox usersHealth_OneUse = new CheckBox("Users Health (One Use)");
        CheckBox targetsHealthLessThan25 = new CheckBox("Target's Health < 25%");
        CheckBox currentBacEntryHits = new CheckBox("Current BAC Entry Hits"); 
        CheckBox usersHealth = new CheckBox("Users Health");

        usersHealth_OneUse.setSelected((entry.primaryActivatorConditions & 268435456L) != 0);
        targetsHealthLessThan25.setSelected((entry.primaryActivatorConditions & 536870912L) != 0);
        currentBacEntryHits.setSelected((entry.primaryActivatorConditions & 1073741824L) != 0);
        usersHealth.setSelected((entry.primaryActivatorConditions & 2147483648L) != 0);

        usersHealth_OneUse.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.primaryActivatorConditions |= 268435456L;
            } else {
                entry.primaryActivatorConditions &= ~268435456L;
            }
        });
        targetsHealthLessThan25.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.primaryActivatorConditions |= 536870912L;
            } else {
                entry.primaryActivatorConditions &= ~536870912L;
            }
        });
        currentBacEntryHits.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.primaryActivatorConditions |= 1073741824L;
            } else {
                entry.primaryActivatorConditions &= ~1073741824L;
            }
        });
        usersHealth.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.primaryActivatorConditions |= 2147483648L;
            } else {
                entry.primaryActivatorConditions &= ~2147483648L;
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

        idleActivatorState.setSelected((entry.activatorState & 1L) != 0);
        comboSkill.setSelected((entry.activatorState & 2L) != 0);
        boosting.setSelected((entry.activatorState & 4L) != 0);
        guarding.setSelected((entry.activatorState & 8L) != 0);

        idleActivatorState.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.activatorState |= 1L;
            } else {
                entry.activatorState &= ~1L;
            }
        });
        comboSkill.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.activatorState |= 2L;
            } else {
                entry.activatorState &= ~2L;
            }
        });
        boosting.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.activatorState |= 4L;
            } else {
                entry.activatorState &= ~4L;
            }
        });
        guarding.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.activatorState |= 8L;
            } else {
                entry.activatorState &= ~8L;
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

        receivingDamage.setSelected((entry.activatorState & 16L) != 0);
        jumping.setSelected((entry.activatorState & 32L) != 0);
        notBeingDamaged.setSelected((entry.activatorState & 64L) != 0);
        targetAttackingPlayer.setSelected((entry.activatorState & 128L) != 0);

        receivingDamage.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.activatorState |= 16L;
            } else {
                entry.activatorState &= ~16L;
            }
        });
        jumping.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.activatorState |= 32L;
            } else {
                entry.activatorState &= ~32L;
            }
        });
        notBeingDamaged.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.activatorState |= 64L;
            } else {
                entry.activatorState &= ~64L;
            }
        });
        targetAttackingPlayer.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.activatorState |= 128L;
            } else {
                entry.activatorState &= ~128L;
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

        forwards.setSelected((entry.activatorState & 256L) != 0);
        backwards.setSelected((entry.activatorState & 512L) != 0);
        left.setSelected((entry.activatorState & 1024L) != 0);
        right.setSelected((entry.activatorState & 2048L) != 0);

        forwards.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.activatorState |= 256L;
            } else {
                entry.activatorState &= ~256L;
            }
        });
        backwards.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.activatorState |= 512L;
            } else {
                entry.activatorState &= ~512L;
            }
        });
        left.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.activatorState |= 1024L;
            } else {
                entry.activatorState &= ~1024L;
            }
        });
        right.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.activatorState |= 2048L;
            } else {
                entry.activatorState &= ~2048L;
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

        unknown13ActivatorState.setSelected((entry.activatorState & 4096L) != 0);
        unknown14ActivatorState.setSelected((entry.activatorState & 8192L) != 0);
        unknown15ActivatorState.setSelected((entry.activatorState & 16384L) != 0);
        unknown16ActivatorState.setSelected((entry.activatorState & 32768L) != 0);

        unknown13ActivatorState.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.activatorState |= 4096L;
            } else {
                entry.activatorState &= ~4096L;
            }
        });
        unknown14ActivatorState.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.activatorState |= 8192L;
            } else {
                entry.activatorState &= ~8192L;
            }
        });
        unknown15ActivatorState.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.activatorState |= 16384L;
            } else {
                entry.activatorState &= ~16384L;
            }
        });
        unknown16ActivatorState.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.activatorState |= 32768L;
            } else {
                entry.activatorState &= ~32768L;
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

        unknown17ActivatorState.setSelected((entry.activatorState & 65536L) != 0);
        unknown18ActivatorState.setSelected((entry.activatorState & 131072L) != 0);
        unknown19ActivatorState.setSelected((entry.activatorState & 262144L) != 0);
        unknown20ActivatorState.setSelected((entry.activatorState & 524288L) != 0);

        unknown17ActivatorState.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.activatorState |= 65536L;
            } else {
                entry.activatorState &= ~65536L;
            }
        });
        unknown18ActivatorState.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.activatorState |= 131072L;
            } else {
                entry.activatorState &= ~131072L;
            }
        });
        unknown19ActivatorState.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.activatorState |= 262144L;
            } else {
                entry.activatorState &= ~262144L;
            }
        });
        unknown20ActivatorState.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.activatorState |= 524288L;
            } else {
                entry.activatorState &= ~524288L;
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

        unknown21ActivatorState.setSelected((entry.activatorState & 1048576L) != 0);
        unknown22ActivatorState.setSelected((entry.activatorState & 2097152L) != 0);
        unknown23ActivatorState.setSelected((entry.activatorState & 4194304L) != 0);
        unknown24ActivatorState.setSelected((entry.activatorState & 8388608L) != 0);

        unknown21ActivatorState.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.activatorState |= 1048576L;
            } else {
                entry.activatorState &= ~1048576L;
            }
        });
        unknown22ActivatorState.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.activatorState |= 2097152L;
            } else {
                entry.activatorState &= ~2097152L;
            }
        });
        unknown23ActivatorState.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.activatorState |= 4194304L;
            } else {
                entry.activatorState &= ~4194304L;
            }
        });
        unknown24ActivatorState.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.activatorState |= 8388608L;
            } else {
                entry.activatorState &= ~8388608L;
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

        unknown25ActivatorState.setSelected((entry.activatorState & 16777216L) != 0);
        unknown26ActivatorState.setSelected((entry.activatorState & 33554432L) != 0);
        unknown27ActivatorState.setSelected((entry.activatorState & 67108864L) != 0);
        unknown28ActivatorState.setSelected((entry.activatorState & 134217728L) != 0);

        unknown25ActivatorState.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.activatorState |= 16777216L;
            } else {
                entry.activatorState &= ~16777216L;
            }
        });
        unknown26ActivatorState.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.activatorState |= 33554432L;
            } else {
                entry.activatorState &= ~33554432L;
            }
        });
        unknown27ActivatorState.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.activatorState |= 67108864L;
            } else {
                entry.activatorState &= ~67108864L;
            }
        });
        unknown28ActivatorState.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.activatorState |= 134217728L;
            } else {
                entry.activatorState &= ~134217728L;
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

        unknown29ActivatorState.setSelected((entry.activatorState & 268435456L) != 0);
        unknown30ActivatorState.setSelected((entry.activatorState & 536870912L) != 0);
        unknown31ActivatorState.setSelected((entry.activatorState & 1073741824L) != 0);
        unknown32ActivatorState.setSelected((entry.activatorState & 2147483648L) != 0);

        unknown28ActivatorState.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.activatorState |= 134217728L;
            } else {
                entry.activatorState &= ~134217728L;
            }
        });
        unknown29ActivatorState.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.activatorState |= 268435456L;
            } else {
                entry.activatorState &= ~268435456L;
            }
        });
        unknown30ActivatorState.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.activatorState |= 536870912L;
            } else {
                entry.activatorState &= ~536870912L;
            }
        });
        unknown31ActivatorState.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.activatorState |= 1073741824L;
            } else {
                entry.activatorState &= ~1073741824L;
            }
        });
        unknown32ActivatorState.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.activatorState |= 2147483648L;
            } else {
                entry.activatorState &= ~2147483648L;
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

    private VBox createBACVBox(BcmEntry entry){

        VBox BACVBox=new VBox(12);
        BACVBox.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

        //BAC entry primary
        HBox BACEntryPrimaryHBox=new HBox(40);
        BACEntryPrimaryHBox.setPadding(new Insets(20,0,0,8));
        Label BACEntryPrimaryLabel=new Label("BAC Entry Primary");
        BACEntryPrimaryLabel.setPrefWidth(160);
        
        Spinner<Integer> BACEntryPrimarySpinner=new Spinner<>(Short.MIN_VALUE,Short.MAX_VALUE,entry.bacEntryPrimary);
        BACEntryPrimarySpinner.setEditable(true);
        BACEntryPrimarySpinner.valueProperty().addListener((obs,oldValue,newValue)->{
            if(newValue!=null){
                entry.bacEntryPrimary = newValue.shortValue();
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

        Spinner<Integer> BACEntryChargeSpinner=new Spinner<>(Short.MIN_VALUE,Short.MAX_VALUE,entry.bacEntryCharge);
        BACEntryChargeSpinner.setEditable(true);
        BACEntryChargeSpinner.valueProperty().addListener((obs,oldValue,newValue)->{
            if(newValue!=null){
                entry.bacEntryCharge = newValue.shortValue();
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

        Spinner<Integer> BACEntryUserConnectSpinner=new Spinner<>(Short.MIN_VALUE,Short.MAX_VALUE,entry.bacEntryUserConnect);
        BACEntryUserConnectSpinner.setEditable(true);
        BACEntryUserConnectSpinner.valueProperty().addListener((obs,oldValue,newValue)->{
            if(newValue!=null){
                entry.bacEntryUserConnect = newValue.shortValue();
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
        
        Spinner<Integer> BACEntryVictimConnectSpinner=new Spinner<>(Short.MIN_VALUE,Short.MAX_VALUE,entry.bacEntryVictimConnect);
        BACEntryVictimConnectSpinner.setEditable(true);
        BACEntryVictimConnectSpinner.valueProperty().addListener((obs,oldValue,newValue)->{
            if(newValue!=null){
                entry.bacEntryVictimConnect = newValue.shortValue();
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

        Spinner<Integer> BACEntryAirborneSpinner=new Spinner<>(Short.MIN_VALUE,Short.MAX_VALUE,entry.bacEntryAirborne);
        BACEntryAirborneSpinner.setEditable(true);
        BACEntryAirborneSpinner.valueProperty().addListener((obs,oldValue,newValue)->{
            if(newValue!=null){
                entry.bacEntryAirborne = newValue.shortValue();
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

        Spinner<Integer> BACEntryTargetingOverrideSpinner=new Spinner<>(0,65535,entry.bacEntryTargetingOverride);
        BACEntryTargetingOverrideSpinner.setEditable(true);
        BACEntryTargetingOverrideSpinner.valueProperty().addListener((obs,oldValue,newValue)->{
            if(newValue!=null){
                entry.bacEntryTargetingOverride = newValue;
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

        switch (entry.bacRandomFlags) {
            case 1 -> randomBACEntry.setSelected(true);
            case 2 -> noTargetCorrection.setSelected(true);
            case 3 -> threeInstanceSetup.setSelected(true);
            case 4 -> unknown4.setSelected(true);
            case 5 -> unknown5.setSelected(true);
            case 6 -> unknown6.setSelected(true);
            case 7 -> unknown7.setSelected(true);
            case 8 -> unknown8.setSelected(true);
            case 9 -> unknown9.setSelected(true);
            default -> none.setSelected(true);
        }

        randomFlagToggleGroup.selectedToggleProperty().addListener((obs,oldValue,newValue)->{
            if(newValue.isSelected()){
                RadioButton selectedRadio = (RadioButton) newValue;
                if (selectedRadio == none) { 
                    entry.bacRandomFlags = 0;
                }
                else if (selectedRadio == randomBACEntry) { 
                    entry.bacRandomFlags = 1;
                }
                else if (selectedRadio == noTargetCorrection) { 
                    entry.bacRandomFlags = 2;
                }
                else if (selectedRadio == threeInstanceSetup) { 
                    entry.bacRandomFlags = 3;
                }
                else if (selectedRadio == unknown4) { 
                    entry.bacRandomFlags = 4;
                }
                else if (selectedRadio == unknown5) { 
                    entry.bacRandomFlags = 5;
                }
                else if (selectedRadio == unknown6) { 
                    entry.bacRandomFlags = 6;
                }
                else if (selectedRadio == unknown7) { 
                    entry.bacRandomFlags = 7;
                }
                else if (selectedRadio == unknown8) { 
                    entry.bacRandomFlags = 8;
                }
                else if (selectedRadio == unknown9) { 
                    entry.bacRandomFlags = 9;
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

    private VBox createMiscVBox(BcmEntry entry){
        VBox miscVBox=new VBox(12);
        miscVBox.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

        //ki cost
        HBox kiCostHBox=new HBox(40);
        kiCostHBox.setPadding(new Insets(20,0,0,8));
        Label kiCostLabel=new Label("Ki Cost");
        kiCostLabel.setPrefWidth(160);

        Spinner<Double> kiCostSpinner=new Spinner<>(0,4294967295.0,(double)entry.kiCost);
        kiCostSpinner.setEditable(true);
        kiCostSpinner.valueProperty().addListener((obs,oldValue,newValue)->{
            if(newValue!=null){
                entry.kiCost = newValue.longValue();
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

        Spinner<Double> receiverLinkIdSpinner=new Spinner<>(0,4294967295.0,(double)entry.receiverLinkId);
        receiverLinkIdSpinner.setEditable(true);
        receiverLinkIdSpinner.valueProperty().addListener((obs,oldValue,newValue)->{
            if(newValue!=null){
                entry.receiverLinkId = newValue.longValue();
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

        Spinner<Double> staminaCostSpinner=new Spinner<>(0,4294967295.0,(double)entry.staminaCost);
        staminaCostSpinner.setEditable(true);
        staminaCostSpinner.valueProperty().addListener((obs,oldValue,newValue)->{
            if(newValue!=null){
                entry.staminaCost = newValue.longValue();
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

        Spinner<Double> kiRequiredSpinner=new Spinner<>(0,4294967295.0,(double)entry.kiRequired);
        kiRequiredSpinner.setEditable(true);
        kiRequiredSpinner.valueProperty().addListener((obs,oldValue,newValue)->{
            if(newValue!=null){
                entry.kiRequired = newValue.longValue();
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

        Spinner<Double> healthRequiredSpinner=new Spinner<>(Float.MIN_VALUE,Float.MAX_VALUE,(double)entry.healthRequired);
        healthRequiredSpinner.setEditable(true);
        healthRequiredSpinner.valueProperty().addListener((obs,oldValue,newValue)->{
            if(newValue!=null){
                entry.healthRequired = newValue.floatValue();
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

        Spinner<Integer> transformationStageSpinner=new Spinner<>(Short.MIN_VALUE,Short.MAX_VALUE,entry.transformationStage);
        transformationStageSpinner.setEditable(true);
        transformationStageSpinner.valueProperty().addListener((obs,oldValue,newValue)->{
            if(newValue!=null){
                entry.transformationStage = newValue.shortValue();
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

        Spinner<Integer> cusAuraSpinner=new Spinner<>(Short.MIN_VALUE,Short.MAX_VALUE,entry.cusAura);
        cusAuraSpinner.setEditable(true);
        cusAuraSpinner.valueProperty().addListener((obs,oldValue,newValue)->{
            if(newValue!=null){
                entry.cusAura = newValue.shortValue();
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

        switch ((int)entry.raceGender) {
            case 1 -> rosterCharactersOnly.setSelected(true);
            case 2 -> maleHumansOnly.setSelected(true);
            case 3 -> femaleHumansOnly.setSelected(true);
            case 4 -> maleSaiyansOnly.setSelected(true);
            case 5 -> femaleSaiyansOnly.setSelected(true);
            case 6 -> namekiansOnly.setSelected(true);
            case 7 -> friezaRaceOnly.setSelected(true);
            case 8 -> maleMajinsOnly.setSelected(true);
            case 9 -> femaleMajinsOnly.setSelected(true);
            default -> allCharactersDefault.setSelected(true);
        }
        raceGenderToggleGroup.selectedToggleProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue.isSelected()) {
                RadioButton selectedRadio = (RadioButton) newValue;
                if (selectedRadio == allCharactersDefault) {
                    entry.raceGender = 0L;
                }
                else if (selectedRadio == rosterCharactersOnly) {
                    entry.raceGender = 1L;
                }
                else if (selectedRadio == maleHumansOnly) {
                    entry.raceGender = 2L;
                }
                else if (selectedRadio == femaleHumansOnly) {
                    entry.raceGender = 3L;
                }
                else if (selectedRadio == maleSaiyansOnly) {
                    entry.raceGender = 4L;
                }
                else if (selectedRadio == femaleSaiyansOnly) {
                    entry.raceGender = 5L;
                }
                else if (selectedRadio == namekiansOnly) {
                    entry.raceGender = 6L;
                }
                else if (selectedRadio == friezaRaceOnly) {
                    entry.raceGender = 7L;
                }
                else if (selectedRadio == maleMajinsOnly) {
                    entry.raceGender = 8L;
                }
                else if (selectedRadio == femaleMajinsOnly) {
                    entry.raceGender = 9L;
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

    private VBox createUnknownVBox(BcmEntry entry){
        VBox unknownVBox=new VBox(20);
        unknownVBox.setPadding(new Insets(20,0,0,8));

        //I_00
        HBox unknown00HBox=new HBox(2);
        unknown00HBox.setPadding(new Insets(20,0,0,8));
        Label lblI00=new Label("I_00: ");
        lblI00.setPrefWidth(120);
        TextField txtI00=new TextField(String.valueOf(entry.unknown0));
        txtI00.textProperty().addListener((obs, oldText, newText) -> {
            if (txtI00.getText().contains("-")) {
                return;
            }
            try {
                entry.unknown0 = Long.parseLong(newText); 
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
        TextField txtI36=new TextField(String.valueOf(entry.unknown36));
        txtI36.textProperty().addListener((obs, oldText, newText) -> {
            if (txtI36.getText().contains("-")) {
                return;
            }
            try {
                entry.unknown36 = Short.parseShort(newText); 
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
        TextField txtI68=new TextField(String.valueOf(entry.unknown68));
        txtI68.textProperty().addListener((obs, oldText, newText) -> {
            if (txtI68.getText().contains("-")) {
                return;
            }
            try {
                entry.unknown68 = Long.parseLong(newText); 
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
        TextField txtI72=new TextField(String.valueOf(entry.unknown72));
        txtI72.textProperty().addListener((obs, oldText, newText) -> {
            if (txtI72.getText().contains("-")) {
                return;
            }
            try {
                entry.unknown72 = Long.parseLong(newText); 
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
        TextField txtI80=new TextField(String.valueOf(entry.unknown80));
        txtI80.textProperty().addListener((obs, oldText, newText) -> {
            if (txtI80.getText().contains("-")) {
                return;
            }
            try {
                entry.unknown80 = Long.parseLong(newText); 
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
        TextField txtI88=new TextField(String.valueOf(entry.unknown88));
        txtI88.textProperty().addListener((obs, oldText, newText) -> {
            if (txtI88.getText().contains("-")) {
                return;
            }
            try {
                entry.unknown88 = Long.parseLong(newText); 
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
        TextField txtI104=new TextField(String.valueOf(entry.unknown104));
        txtI104.textProperty().addListener((obs, oldText, newText) -> {
            if (txtI104.getText().contains("-")) {
                return;
            }
            try {
                entry.unknown104 = Long.parseLong(newText); 
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
        treeView.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue)->{
            if (newValue == null) {
                return;
            }
            // //input tab
            tabPane.getTabs().get(0).setContent(createInputsVBox(bcmEntries.get(treeView.getRow(newValue))));
            // //activator tab
            tabPane.getTabs().get(1).setContent(createActivatorScrollPane(bcmEntries.get(treeView.getRow(newValue))));
            // //BAC tab
            tabPane.getTabs().get(2).setContent(createBACVBox(bcmEntries.get(treeView.getRow(newValue))));
            // //misc tab
            tabPane.getTabs().get(3).setContent(createMiscVBox(bcmEntries.get(treeView.getRow(newValue))));
            //unknown tab
            tabPane.getTabs().get(4).setContent(createUnknownVBox(bcmEntries.get(treeView.getRow(newValue))));
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

        copyContainer = new BcmEntry(bcmEntries.get(treeView.getSelectionModel().getSelectedIndex()));
    }
    private void Paste() {
        if (currentEntry == null || copyContainer == null) {
            return;
        }
        
        bcmEntries.set(treeView.getSelectionModel().getSelectedIndex(), new BcmEntry(copyContainer));

        if (treeView.getSelectionModel().getSelectedItem() != null) {
            tabPane.getTabs().get(0).setContent(createInputsVBox(bcmEntries.get(treeView.getSelectionModel().getSelectedIndex())));
            tabPane.getTabs().get(1).setContent(createActivatorScrollPane(bcmEntries.get(treeView.getSelectionModel().getSelectedIndex())));
            tabPane.getTabs().get(2).setContent(createBACVBox(bcmEntries.get(treeView.getSelectionModel().getSelectedIndex())));
            tabPane.getTabs().get(3).setContent(createMiscVBox(bcmEntries.get(treeView.getSelectionModel().getSelectedIndex())));
            tabPane.getTabs().get(4).setContent(createUnknownVBox(bcmEntries.get(treeView.getSelectionModel().getSelectedIndex())));
        }
        
    }
    private void Delete() {
        if (currentEntry == null || currentEntry.getParent() == null) {
            return;
        }
        
        bcmEntries.remove(treeView.getSelectionModel().getSelectedIndex());
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
            bcmEntries.add(treeView.getRow(currentEntry) + 1, new BcmEntry());
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
            bcmEntries.add(treeView.getRow(currentEntry) - 1, new BcmEntry());
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
        bcmEntries.add(treeView.getRow(currentEntry) + 1, new BcmEntry());
        allEntries.add(treeView.getRow(currentEntry) + 1, newChild);
        int[] index = {0};
        renameTreeItems(treeView.getRoot(), index);
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
                BcmEntry bcmEntry = new BcmEntry();
                int entryStartOffset = 16 + (i * 112);
                int siblingOffset = entryStartOffset + 48;
                int childOffset = entryStartOffset + 52;
                
                channel.position(entryStartOffset);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                bcmEntry.unknown0 = toUint32(intBuffer.getInt());

                channel.position(entryStartOffset+4);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                bcmEntry.directionalInputs = toUint32(intBuffer.getInt());
                
                channel.position(entryStartOffset+8);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                bcmEntry.buttonInputs = toUint32(intBuffer.getInt());
            
                channel.position(entryStartOffset+12);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                bcmEntry.holdDownConditions = toUint32(intBuffer.getInt());
                
                channel.position(entryStartOffset+16);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                bcmEntry.opponentSizeConditions = toUint32(intBuffer.getInt());
                
                channel.position(entryStartOffset+20);
                shortBuffer.clear();
                channel.read(shortBuffer);
                shortBuffer.flip();
                bcmEntry.minimumLoopDuration = toUShort(shortBuffer.getShort());

                channel.position(entryStartOffset+22);
                shortBuffer.clear();
                channel.read(shortBuffer);
                shortBuffer.flip();
                bcmEntry.maximumLoopDuration = toUShort(shortBuffer.getShort());

                channel.position(entryStartOffset+24);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                bcmEntry.primaryActivatorConditions = toUint32(intBuffer.getInt());
                
                channel.position(entryStartOffset+28);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                bcmEntry.activatorState = toUint32(intBuffer.getInt());
              
                channel.position(entryStartOffset+32);
                shortBuffer.clear();
                channel.read(shortBuffer);
                shortBuffer.flip();
                bcmEntry.bacEntryPrimary = shortBuffer.getShort();

                channel.position(entryStartOffset+34);
                shortBuffer.clear();
                channel.read(shortBuffer);
                shortBuffer.flip();
                bcmEntry.bacEntryCharge = shortBuffer.getShort();

                channel.position(entryStartOffset+36);
                shortBuffer.clear();
                channel.read(shortBuffer);
                shortBuffer.flip();
                bcmEntry.unknown36 = shortBuffer.getShort();

                channel.position(entryStartOffset+38);
                shortBuffer.clear();
                channel.read(shortBuffer);
                shortBuffer.flip();
                bcmEntry.bacEntryUserConnect = shortBuffer.getShort();

                channel.position(entryStartOffset+40);
                shortBuffer.clear();
                channel.read(shortBuffer);
                shortBuffer.flip();
                bcmEntry.bacEntryVictimConnect = shortBuffer.getShort();

                channel.position(entryStartOffset+42);
                shortBuffer.clear();
                channel.read(shortBuffer);
                shortBuffer.flip();
                bcmEntry.bacEntryAirborne = shortBuffer.getShort();

                channel.position(entryStartOffset+44);
                shortBuffer.clear();
                channel.read(shortBuffer);
                shortBuffer.flip();
                bcmEntry.bacEntryTargetingOverride = toUShort(shortBuffer.getShort());
                
                channel.position(entryStartOffset+46);
                shortBuffer.clear();
                channel.read(shortBuffer);
                shortBuffer.flip();
                bcmEntry.bacRandomFlags = toUShort(shortBuffer.getShort());
               
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
                bcmEntry.kiCost = toUint32(intBuffer.getInt());

                channel.position(entryStartOffset+68);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                bcmEntry.unknown68 = toUint32(intBuffer.getInt());

                channel.position(entryStartOffset+72);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                bcmEntry.unknown72 = toUint32(intBuffer.getInt());

                channel.position(entryStartOffset+76);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                bcmEntry.receiverLinkId = toUint32(intBuffer.getInt());

                channel.position(entryStartOffset+80);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                bcmEntry.unknown80 = toUint32(intBuffer.getInt());

                channel.position(entryStartOffset+84);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                bcmEntry.staminaCost = toUint32(intBuffer.getInt());

                channel.position(entryStartOffset+88);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                bcmEntry.unknown88 = toUint32(intBuffer.getInt());

                channel.position(entryStartOffset+92);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                bcmEntry.kiRequired = toUint32(intBuffer.getInt());

                channel.position(entryStartOffset+96);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                bcmEntry.healthRequired = intBuffer.getFloat();

                channel.position(entryStartOffset+100);
                shortBuffer.clear();
                channel.read(shortBuffer);
                shortBuffer.flip();
                bcmEntry.transformationStage = shortBuffer.getShort();

                channel.position(entryStartOffset+102);
                shortBuffer.clear();
                channel.read(shortBuffer);
                shortBuffer.flip();
                bcmEntry.cusAura = shortBuffer.getShort();

                channel.position(entryStartOffset+104);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                bcmEntry.unknown104 = toUint32(intBuffer.getInt());
                
                channel.position(entryStartOffset+108);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                bcmEntry.raceGender = toUint32(intBuffer.getInt());
                bcmEntries.add(bcmEntry);
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
                BcmEntry bcmEntry = bcmEntries.get(i);
                int entryStartOffset = 16 + (112 * i);
                int siblingOffset = entryStartOffset + 48;
                int childOffset = entryStartOffset + 52;
                int rootParentOffset=entryStartOffset+56;
                int parentOffset=entryStartOffset+60;
                
                channel.position(entryStartOffset);
                intBuffer.clear();
                intBuffer.putInt(((int)bcmEntry.unknown0));
                intBuffer.flip();
                channel.write(intBuffer);
                    
                channel.position(entryStartOffset+4);
                intBuffer.clear();
                intBuffer.putInt((int)bcmEntry.directionalInputs);
                intBuffer.flip();
                channel.write(intBuffer);

                channel.position(entryStartOffset+8);
                intBuffer.clear();
                intBuffer.putInt((int)bcmEntry.buttonInputs);
                intBuffer.flip();
                channel.write(intBuffer);

                channel.position(entryStartOffset+12);
                intBuffer.clear();
                intBuffer.putInt((int)bcmEntry.holdDownConditions);
                intBuffer.flip();
                channel.write(intBuffer);

                channel.position(entryStartOffset+16);
                intBuffer.clear();
                intBuffer.putInt((int)bcmEntry.opponentSizeConditions);
                intBuffer.flip();
                channel.write(intBuffer);

                channel.position(entryStartOffset+20);
                shortBuffer.clear();
                shortBuffer.putShort((short)bcmEntry.minimumLoopDuration);
                shortBuffer.flip();
                channel.write(shortBuffer);

                channel.position(entryStartOffset+22);
                shortBuffer.clear();
                shortBuffer.putShort((short)bcmEntry.maximumLoopDuration);
                shortBuffer.flip();
                channel.write(shortBuffer);

                channel.position(entryStartOffset+24);
                intBuffer.clear();
                intBuffer.putInt((int)bcmEntry.primaryActivatorConditions);
                intBuffer.flip();
                channel.write(intBuffer);

                channel.position(entryStartOffset+28);
                intBuffer.clear();
                intBuffer.putInt((int)bcmEntry.activatorState);
                intBuffer.flip();
                channel.write(intBuffer);

                channel.position(entryStartOffset+32);
                shortBuffer.clear();
                shortBuffer.putShort(bcmEntry.bacEntryPrimary);
                shortBuffer.flip();
                channel.write(shortBuffer);

                channel.position(entryStartOffset+34);
                shortBuffer.clear();
                shortBuffer.putShort(bcmEntry.bacEntryCharge);
                shortBuffer.flip();
                channel.write(shortBuffer);

                channel.position(entryStartOffset+36);
                shortBuffer.clear();
                shortBuffer.putShort(bcmEntry.unknown36);
                shortBuffer.flip();
                channel.write(shortBuffer);

                channel.position(entryStartOffset+38);
                shortBuffer.clear();
                shortBuffer.putShort(bcmEntry.bacEntryUserConnect);
                shortBuffer.flip();
                channel.write(shortBuffer);

                channel.position(entryStartOffset+40);
                shortBuffer.clear();
                shortBuffer.putShort(bcmEntry.bacEntryVictimConnect);
                shortBuffer.flip();
                channel.write(shortBuffer);

                channel.position(entryStartOffset+42);
                shortBuffer.clear();
                shortBuffer.putShort(bcmEntry.bacEntryAirborne);
                shortBuffer.flip();
                channel.write(shortBuffer);

                channel.position(entryStartOffset+44);
                shortBuffer.clear();
                shortBuffer.putShort((short)bcmEntry.bacEntryTargetingOverride);
                shortBuffer.flip();
                channel.write(shortBuffer);

                channel.position(entryStartOffset+46);
                shortBuffer.clear();
                shortBuffer.putShort((short)bcmEntry.bacRandomFlags);
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
                else if(allEntries.indexOf(allEntries.get(i).getParent())!=0 && i!=0){
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
                intBuffer.putInt((int)bcmEntry.kiCost);
                intBuffer.flip();
                channel.write(intBuffer);

                channel.position(entryStartOffset+68);
                intBuffer.clear();
                intBuffer.putInt((int)bcmEntry.unknown68);
                intBuffer.flip();
                channel.write(intBuffer);

                channel.position(entryStartOffset+72);
                intBuffer.clear();
                intBuffer.putInt((int)bcmEntry.unknown72);
                intBuffer.flip();
                channel.write(intBuffer);

                channel.position(entryStartOffset+76);
                intBuffer.clear();
                intBuffer.putInt((int)bcmEntry.receiverLinkId);
                intBuffer.flip();
                channel.write(intBuffer);

                channel.position(entryStartOffset+80);
                intBuffer.clear();
                intBuffer.putInt((int)bcmEntry.unknown80);
                intBuffer.flip();
                channel.write(intBuffer);

                channel.position(entryStartOffset+84);
                intBuffer.clear();
                intBuffer.putInt((int)bcmEntry.staminaCost);
                intBuffer.flip();
                channel.write(intBuffer);

                channel.position(entryStartOffset+88);
                intBuffer.clear();
                intBuffer.putInt((int)bcmEntry.unknown88);
                intBuffer.flip();
                channel.write(intBuffer);

                channel.position(entryStartOffset+92);
                intBuffer.clear();
                intBuffer.putInt((int)bcmEntry.kiRequired);
                intBuffer.flip();
                channel.write(intBuffer);

                channel.position(entryStartOffset+96);
                intBuffer.clear();
                intBuffer.putFloat(bcmEntry.healthRequired);
                intBuffer.flip();
                channel.write(intBuffer);

                channel.position(entryStartOffset+100);
                intBuffer.clear();
                intBuffer.putInt(bcmEntry.transformationStage);
                intBuffer.flip();
                channel.write(intBuffer);

                channel.position(entryStartOffset+102);
                intBuffer.clear();
                intBuffer.putInt(bcmEntry.cusAura);
                intBuffer.flip();
                channel.write(intBuffer);

                channel.position(entryStartOffset+104);
                intBuffer.clear();
                intBuffer.putInt((int)bcmEntry.unknown104);
                intBuffer.flip();
                channel.write(intBuffer);

                channel.position(entryStartOffset+108);
                intBuffer.clear();
                intBuffer.putInt((int)bcmEntry.raceGender);
                intBuffer.flip();
                channel.write(intBuffer);
            }
        }
         catch (IOException e) {
            System.err.println(e);
        }
    }
}

class BcmEntry {
    // Inputs
    public long directionalInputs;
    public long buttonInputs;
    public long holdDownConditions;
    
    // Activator
    public long opponentSizeConditions;
    public int minimumLoopDuration;
    public int maximumLoopDuration;
    public long primaryActivatorConditions;
    public long activatorState;

    // BAC
    public short bacEntryPrimary;
    public short bacEntryCharge;
    public short bacEntryUserConnect;
    public short bacEntryVictimConnect;
    public short bacEntryAirborne;
    public int bacEntryTargetingOverride;
    public int bacRandomFlags;

    // Misc
    public long kiCost;
    public long receiverLinkId;
    public long staminaCost;
    public long kiRequired;
    public float healthRequired;
    public short transformationStage;
    public short cusAura;
    public long raceGender;

    // Unknowns
    public long unknown0;
    public short unknown36;
    public long unknown68;
    public long unknown72;
    public long unknown80;
    public long unknown88;
    public long unknown104;

    public BcmEntry() {}

    public BcmEntry(BcmEntry other) {
        this.directionalInputs = other.directionalInputs;
        this.buttonInputs = other.buttonInputs;
        this.holdDownConditions = other.holdDownConditions;
        this.opponentSizeConditions = other.opponentSizeConditions;
        this.minimumLoopDuration = other.minimumLoopDuration;
        this.maximumLoopDuration = other.maximumLoopDuration;
        this.primaryActivatorConditions = other.primaryActivatorConditions;
        this.activatorState = other.activatorState;
        this.bacEntryPrimary = other.bacEntryPrimary;
        this.bacEntryCharge = other.bacEntryCharge;
        this.bacEntryUserConnect = other.bacEntryUserConnect;
        this.bacEntryVictimConnect = other.bacEntryVictimConnect;
        this.bacEntryAirborne = other.bacEntryAirborne;
        this.bacEntryTargetingOverride = other.bacEntryTargetingOverride;
        this.bacRandomFlags = other.bacRandomFlags;
        this.kiCost = other.kiCost;
        this.receiverLinkId = other.receiverLinkId;
        this.staminaCost = other.staminaCost;
        this.kiRequired = other.kiRequired;
        this.healthRequired = other.healthRequired;
        this.transformationStage = other.transformationStage;
        this.cusAura = other.cusAura;
        this.raceGender = other.raceGender;
        this.unknown0 = other.unknown0;
        this.unknown36 = other.unknown36;
        this.unknown68 = other.unknown68;
        this.unknown72 = other.unknown72;
        this.unknown80 = other.unknown80;
        this.unknown88 = other.unknown88;
        this.unknown104 = other.unknown104;
    }
}