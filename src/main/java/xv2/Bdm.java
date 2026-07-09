package xv2;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import static xv2.BinaryUtilities.toUShort;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Spinner;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class Bdm {
    ArrayList <String> allEntries;
    ArrayList<BdmEntry> bdmEntries = new ArrayList<>();

    ListView <String> listView=new ListView<>();
    TabPane mainTabPane =new TabPane();

    private ArrayList<BdmEntry> copyContainer = new ArrayList<>();

    ContextMenu contextMenu=new ContextMenu();
    MenuItem copy=new MenuItem("Copy Ctrl+C");
    MenuItem paste=new MenuItem("Paste Ctrl+V");
    MenuItem delete=new MenuItem("Delete Delete");
    MenuItem append=new MenuItem("Append Ctrl+A");
    MenuItem insert=new MenuItem("Insert Ctrl+I");

    Bdm(){
        entriesActionListener();
        entriesKeysListener();
        tabActionListener();
    }

    public HBox createHBox(){
        createMainTabPane();
        createSubTabPane();
        HBox hBox=new HBox(listView,mainTabPane);
        HBox.setHgrow(mainTabPane, Priority.ALWAYS);
        String css = getClass().getResource("/style.css").toExternalForm();
        hBox.getStylesheets().add(css); 
        return hBox;
    }

    public void createMainTabPane(){
        if (mainTabPane.getTabs().isEmpty()) {
            Tab defaultTab = new Tab("0: Default");
            Tab counterHitFrontTab = new Tab("1: Counter Hit (Front)");
            Tab primaryKnockbackTab = new Tab("2: Primary Knockback");
            Tab backTab = new Tab("3: Back");
            Tab groundImpactTab = new Tab("4: Ground Impact");
            Tab guardingTab=new Tab("5: Guarding");
            Tab stumbleTab=new Tab("6: Stumble");
            Tab counterHitBackTab = new Tab("7: Counter Hit (Back)");
            Tab floatingKnockbackTab=new Tab("8: Floating Knockback");
            Tab lyingOnGroundTab=new Tab("9: Lying On Ground");

            defaultTab.setClosable(false);
            counterHitFrontTab.setClosable(false);
            primaryKnockbackTab.setClosable(false);
            backTab.setClosable(false);
            groundImpactTab.setClosable(false);
            guardingTab.setClosable(false);
            stumbleTab.setClosable(false);
            counterHitBackTab.setClosable(false);
            floatingKnockbackTab.setClosable(false);
            lyingOnGroundTab.setClosable(false);

            defaultTab.setContent(createSubTabPane());
            counterHitFrontTab.setContent(createSubTabPane());
            primaryKnockbackTab.setContent(createSubTabPane());
            backTab.setContent(createSubTabPane());
            groundImpactTab.setContent(createSubTabPane());
            guardingTab.setContent(createSubTabPane());
            stumbleTab.setContent(createSubTabPane());
            counterHitBackTab.setContent(createSubTabPane());
            floatingKnockbackTab.setContent(createSubTabPane());
            lyingOnGroundTab.setContent(createSubTabPane());


            mainTabPane.getTabs().addAll(defaultTab, counterHitFrontTab, primaryKnockbackTab, backTab, groundImpactTab,guardingTab,stumbleTab,counterHitBackTab,floatingKnockbackTab,lyingOnGroundTab);
            
        }

    }

    public TabPane createSubTabPane(){
        TabPane subTabPane = new TabPane();
    
        Tab mainTab=new Tab("Main");
        Tab animationTab=new Tab("Animation");
        Tab soundTab=new Tab("Sound");
        Tab effectsTab=new Tab("Effects");
        Tab pushbackStunKnockbacTab=new Tab("Pushback/Stun/Knockback");
        Tab cameraTab=new Tab("Camera");
        Tab miscTab=new Tab("Misc");
        Tab unknownTab=new Tab("Unknown");

        mainTab.setClosable(false);
        animationTab.setClosable(false);
        soundTab.setClosable(false);
        effectsTab.setClosable(false);
        pushbackStunKnockbacTab.setClosable(false);
        cameraTab.setClosable(false);
        miscTab.setClosable(false);
        unknownTab.setClosable(false);
    
        subTabPane.getTabs().addAll(mainTab,animationTab,soundTab,effectsTab,pushbackStunKnockbacTab,cameraTab,miscTab,unknownTab);
        return subTabPane;
    }

    private VBox createMainVBox(BdmSubEntry subEntry){

        VBox mainVBox=new VBox(20);
        mainVBox.setPadding(new Insets(20,0,0,5));

        //damage type
        HBox damageTypeHBox=new HBox(2);
        damageTypeHBox.setPadding(new Insets(20,0,0,8));

        Label damageTypeLabel=new Label("Damage Type");
        damageTypeLabel.setPrefWidth(160);

        GridPane damageTypeGridPane=new GridPane();
        damageTypeGridPane.getStyleClass().add("titled-address-box");
        damageTypeGridPane.setHgap(10);
        damageTypeGridPane.setVgap(10);

        ToggleGroup damageTypeToggleGroup=new ToggleGroup();
        RadioButton noEffect=new RadioButton("No Effect");
        noEffect.setToggleGroup(damageTypeToggleGroup);
        RadioButton block=new RadioButton("Block");
        block.setToggleGroup(damageTypeToggleGroup);
        RadioButton guardBreak=new RadioButton("Guard Break");
        guardBreak.setToggleGroup(damageTypeToggleGroup);
        RadioButton standard =new RadioButton("Standard");
        standard.setToggleGroup(damageTypeToggleGroup);
        RadioButton heavy=new RadioButton("Heavy");
        heavy.setToggleGroup(damageTypeToggleGroup);
        RadioButton knockback = new RadioButton("Knockback");
        knockback.setToggleGroup(damageTypeToggleGroup);
        RadioButton knockback1 = new RadioButton("Knockback 1");
        knockback1.setToggleGroup(damageTypeToggleGroup);
        RadioButton knockback2 = new RadioButton("Knockback 2");
        knockback2.setToggleGroup(damageTypeToggleGroup);
        RadioButton knockback3 = new RadioButton("Knockback 3");
        knockback3.setToggleGroup(damageTypeToggleGroup);
        RadioButton knockback4 = new RadioButton("Knockback 4");
        knockback4.setToggleGroup(damageTypeToggleGroup);
        RadioButton grab = new RadioButton("Grab");
        grab.setToggleGroup(damageTypeToggleGroup);
        RadioButton holdStomach = new RadioButton("Hold Stomach");
        holdStomach.setToggleGroup(damageTypeToggleGroup);
        RadioButton holdEyes = new RadioButton("Hold Eyes");
        holdEyes.setToggleGroup(damageTypeToggleGroup);
        RadioButton knockback5 = new RadioButton("Knockback 5");
        knockback5.setToggleGroup(damageTypeToggleGroup);
        RadioButton electric = new RadioButton("Electric");
        electric.setToggleGroup(damageTypeToggleGroup);
        RadioButton dazed = new RadioButton("Dazed");
        dazed.setToggleGroup(damageTypeToggleGroup);
        RadioButton paralysis = new RadioButton("Paralysis");
        paralysis.setToggleGroup(damageTypeToggleGroup);
        RadioButton freeze = new RadioButton("Freeze");
        freeze.setToggleGroup(damageTypeToggleGroup);
        RadioButton wildCard = new RadioButton("Wild-Card");
        wildCard.setToggleGroup(damageTypeToggleGroup);
        RadioButton unused = new RadioButton("Unused");
        unused.setToggleGroup(damageTypeToggleGroup);
        RadioButton heavyStaminaBreak = new RadioButton("Heavy Stamina Break");
        heavyStaminaBreak.setToggleGroup(damageTypeToggleGroup);
        RadioButton lightStaminaBreak = new RadioButton("Light Stamina Break");
        lightStaminaBreak.setToggleGroup(damageTypeToggleGroup);
        RadioButton giganticKiBlastPush = new RadioButton("Gigantic Ki Blast Push");
        giganticKiBlastPush.setToggleGroup(damageTypeToggleGroup);
        RadioButton brainWash = new RadioButton("Brain Wash");
        brainWash.setToggleGroup(damageTypeToggleGroup);
        RadioButton giganticKiBlastReturn = new RadioButton("Gigantic Ki Blast Return");
        giganticKiBlastReturn.setToggleGroup(damageTypeToggleGroup);
        RadioButton knockback6 = new RadioButton("Knockback 6");
        knockback6.setToggleGroup(damageTypeToggleGroup);
        RadioButton knockback7 = new RadioButton("Knockback 7");
        knockback7.setToggleGroup(damageTypeToggleGroup);
        RadioButton knockback8 = new RadioButton("Knockback 8");
        knockback8.setToggleGroup(damageTypeToggleGroup);
        RadioButton knockback9 = new RadioButton("Knockback 9");
        knockback9.setToggleGroup(damageTypeToggleGroup);
        RadioButton slowOpponent = new RadioButton("Slow Opponent");
        slowOpponent.setToggleGroup(damageTypeToggleGroup);
        RadioButton brainWash2 = new RadioButton("Brain Wash 2");
        brainWash2.setToggleGroup(damageTypeToggleGroup);
        RadioButton timeStop = new RadioButton("Time Stop");
        timeStop.setToggleGroup(damageTypeToggleGroup);
        
        switch (subEntry.damageType) {
            case 1:  block.setSelected(true);                 break;
            case 2:  guardBreak.setSelected(true);            break;
            case 3:  standard.setSelected(true);              break;
            case 4:  heavy.setSelected(true);                 break;
            case 5:  knockback.setSelected(true);             break;
            case 6:  knockback1.setSelected(true);            break;
            case 7:  knockback2.setSelected(true);            break;
            case 8:  knockback3.setSelected(true);            break;
            case 9:  knockback4.setSelected(true);            break;
            case 10: grab.setSelected(true);                  break;
            case 11: holdStomach.setSelected(true);           break;
            case 12: holdEyes.setSelected(true);              break;
            case 13: knockback5.setSelected(true);            break;
            case 14: electric.setSelected(true);              break;
            case 15: dazed.setSelected(true);                 break;
            case 16: paralysis.setSelected(true);             break;
            case 17: freeze.setSelected(true);                break;
            case 18: wildCard.setSelected(true);              break;
            case 19: unused.setSelected(true);                break;
            case 20: heavyStaminaBreak.setSelected(true);     break;
            case 21: lightStaminaBreak.setSelected(true);     break;
            case 22: giganticKiBlastPush.setSelected(true);   break;
            case 23: brainWash.setSelected(true);             break;
            case 24: giganticKiBlastReturn.setSelected(true); break;
            case 25: knockback6.setSelected(true);            break;
            case 26: knockback7.setSelected(true);            break;
            case 27: knockback8.setSelected(true);            break;
            case 28: knockback9.setSelected(true);            break;
            case 29: slowOpponent.setSelected(true);          break;
            case 30: brainWash2.setSelected(true);            break;
            case 31: timeStop.setSelected(true);              break;
            default: noEffect.setSelected(true);              break;
        }

        damageTypeToggleGroup.selectedToggleProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null && newValue.isSelected()) {
                RadioButton selectedRadio = (RadioButton) newValue;
                
                if (selectedRadio == noEffect)                   { subEntry.damageType = 0; }
                else if (selectedRadio == block)                 { subEntry.damageType = 1; }
                else if (selectedRadio == guardBreak)            { subEntry.damageType = 2; }
                else if (selectedRadio == standard)              { subEntry.damageType = 3; }
                else if (selectedRadio == heavy)                 { subEntry.damageType = 4; }
                else if (selectedRadio == knockback)             { subEntry.damageType = 5; }
                else if (selectedRadio == knockback1)            { subEntry.damageType = 6; }
                else if (selectedRadio == knockback2)            { subEntry.damageType = 7; }
                else if (selectedRadio == knockback3)            { subEntry.damageType = 8; }
                else if (selectedRadio == knockback4)            { subEntry.damageType = 9; }
                else if (selectedRadio == grab)                  { subEntry.damageType = 10; }
                else if (selectedRadio == holdStomach)           { subEntry.damageType = 11; }
                else if (selectedRadio == holdEyes)              { subEntry.damageType = 12; }
                else if (selectedRadio == knockback5)            { subEntry.damageType = 13; }
                else if (selectedRadio == electric)              { subEntry.damageType = 14; }
                else if (selectedRadio == dazed)                 { subEntry.damageType = 15; }
                else if (selectedRadio == paralysis)             { subEntry.damageType = 16; }
                else if (selectedRadio == freeze)                { subEntry.damageType = 17; }
                else if (selectedRadio == wildCard)              { subEntry.damageType = 18; }
                else if (selectedRadio == unused)                { subEntry.damageType = 19; }
                else if (selectedRadio == heavyStaminaBreak)     { subEntry.damageType = 20; }
                else if (selectedRadio == lightStaminaBreak)     { subEntry.damageType = 21; }
                else if (selectedRadio == giganticKiBlastPush)   { subEntry.damageType = 22; }
                else if (selectedRadio == brainWash)             { subEntry.damageType = 23; }
                else if (selectedRadio == giganticKiBlastReturn) { subEntry.damageType = 24; }
                else if (selectedRadio == knockback6)            { subEntry.damageType = 25; }
                else if (selectedRadio == knockback7)            { subEntry.damageType = 26; }
                else if (selectedRadio == knockback8)            { subEntry.damageType = 27; }
                else if (selectedRadio == knockback9)            { subEntry.damageType = 28; }
                else if (selectedRadio == slowOpponent)          { subEntry.damageType = 29; }
                else if (selectedRadio == brainWash2)            { subEntry.damageType = 30; }
                else if (selectedRadio == timeStop)              { subEntry.damageType = 31; }
            }
        });

        damageTypeGridPane.add(noEffect, 0, 0);   
        damageTypeGridPane.add(block, 1, 0);          
        damageTypeGridPane.add(guardBreak, 2, 0);          
        damageTypeGridPane.add(standard, 3, 0);          

        damageTypeGridPane.add(heavy, 0, 1);          
        damageTypeGridPane.add(knockback, 1, 1);          
        damageTypeGridPane.add(knockback1, 2, 1);          
        damageTypeGridPane.add(knockback2, 3, 1);          

        damageTypeGridPane.add(knockback3, 0, 2);          
        damageTypeGridPane.add(knockback4, 1, 2);   
        damageTypeGridPane.add(grab, 2, 2);
        damageTypeGridPane.add(holdStomach, 3, 2);

        damageTypeGridPane.add(holdEyes, 0, 3);
        damageTypeGridPane.add(knockback5, 1, 3);
        damageTypeGridPane.add(electric, 2, 3);
        damageTypeGridPane.add(dazed, 3, 3);

        damageTypeGridPane.add(paralysis, 0, 4);
        damageTypeGridPane.add(freeze, 1, 4);
        damageTypeGridPane.add(wildCard, 2, 4);
        damageTypeGridPane.add(unused, 3, 4);

        damageTypeGridPane.add(heavyStaminaBreak, 0, 5);
        damageTypeGridPane.add(lightStaminaBreak, 1, 5);
        damageTypeGridPane.add(giganticKiBlastPush, 2, 5);
        damageTypeGridPane.add(brainWash, 3, 5);

        damageTypeGridPane.add(giganticKiBlastReturn, 0, 6);
        damageTypeGridPane.add(knockback6, 1, 6);
        damageTypeGridPane.add(knockback7, 2, 6);
        damageTypeGridPane.add(knockback8, 3, 6);

        damageTypeGridPane.add(knockback9, 0, 7);
        damageTypeGridPane.add(slowOpponent, 1, 7);
        damageTypeGridPane.add(brainWash2, 2, 7);
        damageTypeGridPane.add(timeStop, 3, 7);
        
        damageTypeHBox.getChildren().addAll(damageTypeLabel,damageTypeGridPane);
        damageTypeHBox.setAlignment(Pos.CENTER_LEFT);
        //damage type

        //secondary type
        HBox secondaryTypeHBox=new HBox(2);
        secondaryTypeHBox.setPadding(new Insets(20,0,0,8));

        Label secondaryTypeLabel=new Label("Secondary Type");
        secondaryTypeLabel.setPrefWidth(160);

        //health properties
        Label healthPropertiesLabel=new Label("Health Properties");
        healthPropertiesLabel.getStyleClass().add("titled-address-label");

        CheckBox restoreHealth=new CheckBox("Restore Health");
        CheckBox unknown2=new CheckBox("Unknown2");
        CheckBox unknown3=new CheckBox("Unknown3");
        CheckBox unknown4= new CheckBox("Unknown4");

        restoreHealth.setSelected((subEntry.secondaryType & 1) != 0);
        unknown2.setSelected((subEntry.secondaryType & 2) != 0);
        unknown3.setSelected((subEntry.secondaryType & 4) != 0);
        unknown4.setSelected((subEntry.secondaryType & 8) != 0);

        restoreHealth.selectedProperty().addListener((obs,oldValue,newValue)->{
            if(newValue){
                subEntry.secondaryType|= 1;
            }
            else{
                subEntry.secondaryType &= ~1;
            }
        });
        unknown2.selectedProperty().addListener((obs,oldValue,newValue)->{
            if(newValue){
                subEntry.secondaryType |= 2;
            }
            else{
                subEntry.secondaryType &= ~2;
            }
        });
        unknown3.selectedProperty().addListener((obs,oldValue,newValue)->{
            if(newValue){
                subEntry.secondaryType |= 4;
            }
            else{
                subEntry.secondaryType &=~ 4;
            }
        });
        unknown4.selectedProperty().addListener((obs,oldValue,newValue)->{
            if(newValue){
                subEntry.secondaryType |= 8;
            }
            else{
                subEntry.secondaryType &=~ 8;
            }
        });
        VBox healthPropertiesBox = new VBox(2,restoreHealth,unknown2,unknown3,unknown4);

        VBox borderContainerHealthProperties=new VBox(healthPropertiesBox);
        borderContainerHealthProperties.getStyleClass().add("titled-address-box");
        borderContainerHealthProperties.setPadding(new Insets(12,0,0,0));

        StackPane healthPropertiesBoxStackPane = new StackPane();
        healthPropertiesBoxStackPane.getChildren().addAll(borderContainerHealthProperties,healthPropertiesLabel);

        StackPane.setAlignment(healthPropertiesLabel, Pos.TOP_LEFT);
        healthPropertiesLabel.setTranslateY(-8); 
        healthPropertiesLabel.setTranslateX(10);
        //health properties

        //unknown
        Label unknownLabel = new Label("Unknown");
        unknownLabel.getStyleClass().add("titled-address-label");

        CheckBox unknown5=new CheckBox("Unknown 5");
        CheckBox unknown6=new CheckBox("Unknown 6");
        CheckBox unknown7=new CheckBox("Unknown 7");
        CheckBox unknown8=new CheckBox("Unknown 8");


        unknown5.setSelected((subEntry.secondaryType & 16L) != 0);
        unknown6.setSelected((subEntry.secondaryType & 32L) != 0);
        unknown7.setSelected((subEntry.secondaryType & 64L) != 0);
        unknown8.setSelected((subEntry.secondaryType & 128L) != 0);


        unknown5.selectedProperty().addListener((obs,oldValue,newValue)->{
            if(newValue){
                subEntry.secondaryType |= 16;
            }
            else{
                subEntry.secondaryType &=~ 16;
            }
        });
        unknown6.selectedProperty().addListener((obs,oldValue,newValue)->{
            if(newValue){
                subEntry.secondaryType |= 32;
            }
            else{
                subEntry.secondaryType &=~ 32;
            }
        });
        unknown7.selectedProperty().addListener((obs,oldValue,newValue)->{
            if(newValue){
                subEntry.secondaryType |= 64;
            }
            else{
                subEntry.secondaryType &=~ 64;
            }
        });
        unknown8.selectedProperty().addListener((obs,oldValue,newValue)->{
            if(newValue){
                subEntry.secondaryType |= 128;
            }
            else{
                subEntry.secondaryType &=~ 128;
            }
        });
        VBox unknownVBox = new VBox(2,unknown5,unknown6,unknown7,unknown8);

        VBox borderContainerunknown=new VBox(unknownVBox);
        borderContainerunknown.getStyleClass().add("titled-address-box");
        borderContainerunknown.setPadding(new Insets(12,0,0,0));

        StackPane unknownStackPane = new StackPane();
        unknownStackPane.getChildren().addAll(borderContainerunknown,unknownLabel);

        StackPane.setAlignment(unknownLabel, Pos.TOP_LEFT);
        unknownLabel.setTranslateY(-8); 
        unknownLabel.setTranslateX(10);
        //unknown

        //damage properties
        Label damagePropertiesLabel = new Label("Damage Priorities");
        damagePropertiesLabel.getStyleClass().add("titled-address-label");

        CheckBox disableEvasiveUsage=new CheckBox("Disable Evasive Usage");
        CheckBox unknown10=new CheckBox("Unknown 10");
        CheckBox bypassTimeStopDamage=new CheckBox("Bypass Time Stop Damage");
        CheckBox bypassSuperArmor=new CheckBox("Bypass Super Armor");


        disableEvasiveUsage.setSelected((subEntry.secondaryType & 256) != 0);
        unknown10.setSelected((subEntry.secondaryType & 512) != 0);
        bypassTimeStopDamage.setSelected((subEntry.secondaryType & 1024) != 0);
        bypassSuperArmor.setSelected((subEntry.secondaryType & 2048) != 0);


        disableEvasiveUsage.selectedProperty().addListener((obs,oldValue,newValue)->{
            if(newValue){
                subEntry.secondaryType |= 256;
            }
            else{
                subEntry.secondaryType &=~ 256;
            }
        });
        unknown10.selectedProperty().addListener((obs,oldValue,newValue)->{
            if(newValue){
                subEntry.secondaryType |= 512;
            }
            else{
                subEntry.secondaryType &=~ 512;
            }
        });
        bypassTimeStopDamage.selectedProperty().addListener((obs,oldValue,newValue)->{
            if(newValue){
                subEntry.secondaryType |= 1024;
            }
            else{
                subEntry.secondaryType &=~ 1024;
            }
        });
        bypassSuperArmor.selectedProperty().addListener((obs,oldValue,newValue)->{
            if(newValue){
                subEntry.secondaryType |= 2048;
            }
            else{
                subEntry.secondaryType &=~ 2048;
            }
        });
        VBox damagePropertiesBox = new VBox(2,disableEvasiveUsage,unknown10,bypassTimeStopDamage,bypassSuperArmor);

        VBox borderContainerDamageProperties=new VBox(damagePropertiesBox);
        borderContainerDamageProperties.getStyleClass().add("titled-address-box");
        borderContainerDamageProperties.setPadding(new Insets(12,0,0,0));

        StackPane damagePropertiesStackPane = new StackPane();
        damagePropertiesStackPane.getChildren().addAll(borderContainerDamageProperties,damagePropertiesLabel);

        StackPane.setAlignment(damagePropertiesLabel, Pos.TOP_LEFT);
        damagePropertiesLabel.setTranslateY(-8); 
        damagePropertiesLabel.setTranslateX(10);
        //damage properties

        //damage orientation
        Label damageOrientationLabel = new Label("Damage Orientation");
        damageOrientationLabel.getStyleClass().add("titled-address-label");

        CheckBox faceOpponentAlways=new CheckBox("Face Opponent Always");
        CheckBox unknown14=new CheckBox("Unknown 14");
        CheckBox unknown15=new CheckBox("Unknown 15");
        CheckBox unknown16=new CheckBox("Unknown 16");


        faceOpponentAlways.setSelected((subEntry.secondaryType & 4096) != 0);
        unknown14.setSelected((subEntry.secondaryType & 8192) != 0);
        unknown15.setSelected((subEntry.secondaryType & 16384) != 0);
        unknown16.setSelected((subEntry.secondaryType & 2048) != 0);


        faceOpponentAlways.selectedProperty().addListener((obs,oldValue,newValue)->{
            if(newValue){
                subEntry.secondaryType |= 4096;
            }
            else{
                subEntry.secondaryType &=~ 4096;
            }
        });
        unknown14.selectedProperty().addListener((obs,oldValue,newValue)->{
            if(newValue){
                subEntry.secondaryType |= 8192;
            }
            else{
                subEntry.secondaryType &=~ 8192;
            }
        });
        unknown15.selectedProperty().addListener((obs,oldValue,newValue)->{
            if(newValue){
                subEntry.secondaryType |= 16384;
            }
            else{
                subEntry.secondaryType &=~ 16384;
            }
        });
        unknown16.selectedProperty().addListener((obs,oldValue,newValue)->{
            if(newValue){
                subEntry.secondaryType |= 32768;
            }
            else{
                subEntry.secondaryType &=~ 32768;
            }
        });
        VBox damageOrientationBox = new VBox(2,faceOpponentAlways,unknown14,unknown15,unknown16);

        VBox borderContainerDamageOrientation=new VBox(damageOrientationBox);
        borderContainerDamageOrientation.getStyleClass().add("titled-address-box");
        borderContainerDamageOrientation.setPadding(new Insets(12,0,0,0));

        StackPane damageOrientationStackPane = new StackPane();
        damageOrientationStackPane.getChildren().addAll(borderContainerDamageOrientation,damageOrientationLabel);

        StackPane.setAlignment(damageOrientationLabel, Pos.TOP_LEFT);
        damageOrientationLabel.setTranslateY(-8); 
        damageOrientationLabel.setTranslateX(10);
        //damage properties

        secondaryTypeHBox.getChildren().addAll(secondaryTypeLabel,healthPropertiesBoxStackPane,unknownStackPane,damagePropertiesStackPane,damageOrientationStackPane);
        secondaryTypeHBox.setAlignment(Pos.CENTER_LEFT);
        //secondary type

        //damage amount
        HBox damageAmountHBox=new HBox(2);
        damageAmountHBox.setPadding(new Insets(20,0,0,8));
        Label damageAmountLabel=new Label("Damage Amount");
        damageAmountLabel.setPrefWidth(160);

        Spinner<Integer> damageAmountSpinner=new Spinner<>(0,65535,subEntry.damageAmount);
        damageAmountSpinner.setEditable(true);
        damageAmountSpinner.valueProperty().addListener((obs,oldValue,newValue)->{
            if(newValue!=null){
                subEntry.damageAmount = newValue;
            }

        });

        damageAmountHBox.getChildren().addAll(damageAmountLabel,damageAmountSpinner);
        damageAmountHBox.setAlignment(Pos.CENTER_LEFT);
        //damage amount

        
        //damage special
        HBox damageSpecialHBox=new HBox(2);
        damageSpecialHBox.setPadding(new Insets(20,0,0,8));
        Label damageSpecialLabel=new Label("Damage Special");
        damageSpecialLabel.setPrefWidth(160);

        Spinner<Integer> damageSpecialSpinner=new Spinner<>(0,65535,subEntry.damageSpecial);
        damageSpecialSpinner.setEditable(true);
        damageSpecialSpinner.valueProperty().addListener((obs,oldValue,newValue)->{
            if(newValue!=null){
                subEntry.damageSpecial = newValue;
            }

        });

        damageSpecialHBox.getChildren().addAll(damageSpecialLabel,damageSpecialSpinner);
        damageSpecialHBox.setAlignment(Pos.CENTER_LEFT);
        //damage special

        //damage special 2
        HBox damageSpecial2HBox=new HBox(2);
        damageSpecial2HBox.setPadding(new Insets(20,0,0,8));
        Label damageSpecial2Label=new Label("Damage Special 2");
        damageSpecial2Label.setPrefWidth(160);

        Spinner<Integer> damageSpecial2Spinner=new Spinner<>(0,65535,subEntry.damageSpecial2);
        damageSpecial2Spinner.setEditable(true);
        damageSpecial2Spinner.valueProperty().addListener((obs,oldValue,newValue)->{
            if(newValue!=null){
                subEntry.damageSpecial2 = newValue;
            }

        });

        damageSpecial2HBox.getChildren().addAll(damageSpecial2Label,damageSpecial2Spinner);
        damageSpecial2HBox.setAlignment(Pos.CENTER_LEFT);
        //damage special 2

        //damage special 3
        HBox damageSpecial3HBox=new HBox(2);
        damageSpecial3HBox.setPadding(new Insets(20,0,0,8));
        Label damageSpecial3Label=new Label("Damage Special 3");
        damageSpecial3Label.setPrefWidth(160);

        Spinner<Integer> damageSpecial3Spinner=new Spinner<>(0,65535,subEntry.damageSpecial3);
        damageSpecial3Spinner.setEditable(true);
        damageSpecial3Spinner.valueProperty().addListener((obs,oldValue,newValue)->{
            if(newValue!=null){
                subEntry.damageSpecial3 = newValue;
            }

        });

        damageSpecial3HBox.getChildren().addAll(damageSpecial3Label,damageSpecial3Spinner);
        damageSpecial3HBox.setAlignment(Pos.CENTER_LEFT);
        //damage special 3

        mainVBox.getChildren().addAll(damageTypeHBox,secondaryTypeHBox,damageAmountHBox,damageSpecialHBox,damageSpecial2HBox,damageSpecial3HBox);

        return mainVBox;

    }

    private VBox createAnimationVBox(BdmSubEntry subEntry){
        VBox animationVBox = new VBox(5);

        //user animation time
        HBox userAnimationTimeHBox=new HBox(2);
        userAnimationTimeHBox.setPadding(new Insets(20,0,0,8));
        Label userAnimationTimeLabel=new Label("User Animation Time");
        userAnimationTimeLabel.setPrefWidth(160);

        Spinner<Integer> userAnimationTimeSpinner=new Spinner<>(0,65535,subEntry.userAnimationTime);
        userAnimationTimeSpinner.setEditable(true);
        userAnimationTimeSpinner.valueProperty().addListener((obs,oldValue,newValue)->{
            if(newValue!=null){
                subEntry.userAnimationTime = newValue;
            }

        });

        userAnimationTimeHBox.getChildren().addAll(userAnimationTimeLabel,userAnimationTimeSpinner);
        userAnimationTimeHBox.setAlignment(Pos.CENTER_LEFT);
        //user animation time

        //user animation speed
        HBox userAnimationSpeedHBox=new HBox(2);
        userAnimationSpeedHBox.setPadding(new Insets(20,0,0,8));
        Label userAnimationSpeed=new Label("User Animation Speed");
        userAnimationSpeed.setPrefWidth(160);

        Spinner<Double> userAnimationSpeedSpinner=new Spinner<>(Float.MIN_VALUE,Float.MAX_VALUE,(double)subEntry.userAnimationSpeed);
        userAnimationSpeedSpinner.setEditable(true);
        userAnimationSpeedSpinner.valueProperty().addListener((obs,oldValue,newValue)->{
            if(newValue!=null){
                subEntry.userAnimationSpeed = newValue.floatValue();
            }
        });

        userAnimationSpeedHBox.getChildren().addAll(userAnimationSpeed,userAnimationSpeedSpinner);
        userAnimationSpeedHBox.setAlignment(Pos.CENTER_LEFT);
        //user animation speed

        //victim animation time
        HBox victimAnimationTimeHBox=new HBox(2);
        victimAnimationTimeHBox.setPadding(new Insets(20,0,0,8));
        Label victimAnimationTimeLabel=new Label("Victim Animation Time");
        victimAnimationTimeLabel.setPrefWidth(160);

        Spinner<Integer> victimAnimationTimeSpinner=new Spinner<>(0,65535,subEntry.victimAnimationTime);
        victimAnimationTimeSpinner.setEditable(true);
        victimAnimationTimeSpinner.valueProperty().addListener((obs,oldValue,newValue)->{
            if(newValue!=null){
                subEntry.victimAnimationTime = newValue;
            }

        });

        victimAnimationTimeHBox.getChildren().addAll(victimAnimationTimeLabel,victimAnimationTimeSpinner);
        victimAnimationTimeHBox.setAlignment(Pos.CENTER_LEFT);
        //vcitim animation time

        //victim animation speed
        HBox victimAnimationSpeedHBox=new HBox(2);
        victimAnimationSpeedHBox.setPadding(new Insets(20,0,0,8));
        Label victimAnimationSpeed=new Label("Victim Animation Speed");
        victimAnimationSpeed.setPrefWidth(160);

        Spinner<Double> victimAnimationSpeedSpinner=new Spinner<>(Float.MIN_VALUE,Float.MAX_VALUE,(double)subEntry.victimAnimationSpeed);
        victimAnimationSpeedSpinner.setEditable(true);
        victimAnimationSpeedSpinner.valueProperty().addListener((obs,oldValue,newValue)->{
            if(newValue!=null){
                subEntry.victimAnimationSpeed = newValue.floatValue();
            }
        });

        victimAnimationSpeedHBox.getChildren().addAll(victimAnimationSpeed,victimAnimationSpeedSpinner);
        victimAnimationSpeedHBox.setAlignment(Pos.CENTER_LEFT);
        //victim animation speed

        animationVBox.getChildren().addAll(userAnimationTimeHBox,userAnimationSpeedHBox,victimAnimationTimeHBox,victimAnimationSpeedHBox);
        
        return animationVBox;
    }

    private VBox createSoundVBox(BdmSubEntry subEntry){
        VBox soundVBox =new VBox(5);

        //sound type
        HBox acbTypeHBox = new HBox(2);
        acbTypeHBox.setPadding(new Insets(20,0,0,8));
        Label acbTypeLabel=new Label("Acb Type");
        acbTypeLabel.setPrefWidth(100);

        HBox acbTypeRadioButtonsHBox=new HBox(15);
        acbTypeRadioButtonsHBox.getStyleClass().add("titled-address-box");
        
        ToggleGroup acbTypeToggleGroup = new ToggleGroup();
        RadioButton common = new RadioButton("Common");
        common.setToggleGroup(acbTypeToggleGroup);
        RadioButton characterSE = new RadioButton("Character SE");
        characterSE.setToggleGroup(acbTypeToggleGroup);
        RadioButton characterVOX = new RadioButton("Character VOX");
        characterVOX.setToggleGroup(acbTypeToggleGroup);
        RadioButton skillSE = new RadioButton("Skill SE");
        skillSE.setToggleGroup(acbTypeToggleGroup);
        RadioButton skillVOX = new RadioButton("Skill VOX");
        skillVOX.setToggleGroup(acbTypeToggleGroup);


        switch (subEntry.acbType) {
            case 2:  characterSE.setSelected(true);           break;
            case 3:  characterVOX.setSelected(true);          break;
            case 10: skillSE.setSelected(true);               break;
            case 11: skillVOX.setSelected(true);              break;
            default: common.setSelected(true);                break;
        }

        acbTypeToggleGroup.selectedToggleProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null && newValue.isSelected()) {
                RadioButton selectedRadio = (RadioButton) newValue;
                
                if (selectedRadio == common)                    { subEntry.acbType = 0;  }
                else if (selectedRadio == characterSE)          { subEntry.acbType = 2;  }
                else if (selectedRadio == characterVOX)         { subEntry.acbType = 3;  }
                else if (selectedRadio == skillSE)              { subEntry.acbType = 10; }
                else if (selectedRadio == skillVOX)             { subEntry.acbType = 11; }
            }
        });
        acbTypeRadioButtonsHBox.getChildren().addAll(common,characterSE,characterVOX,skillSE,skillVOX);
        acbTypeHBox.getChildren().addAll(acbTypeLabel,acbTypeRadioButtonsHBox);
        acbTypeHBox.setAlignment(Pos.CENTER_LEFT);

        //sound type

        //victim animation speed
        HBox cueIdHBox=new HBox(2);
        cueIdHBox.setPadding(new Insets(20,0,0,8));
        Label cueIdSpeed=new Label("Cue Id");
        cueIdSpeed.setPrefWidth(100);

        Spinner<Integer> cueIdSpinner=new Spinner<>(Short.MIN_VALUE,Short.MAX_VALUE,subEntry.cueId);
        cueIdSpinner.setEditable(true);
        cueIdSpinner.valueProperty().addListener((obs,oldValue,newValue)->{
            if(newValue!=null){
                subEntry.cueId = newValue.shortValue();
            }
        });

        cueIdHBox.getChildren().addAll(cueIdSpeed,cueIdSpinner);
        cueIdHBox.setAlignment(Pos.CENTER_LEFT);
        //victim animation speed

        soundVBox.getChildren().addAll(acbTypeHBox,cueIdHBox);

        return soundVBox;
    }

    private ScrollPane createEffectsScrollPane(BdmSubEntry subEntry){
        ScrollPane effectsScrollPane = new ScrollPane();
        VBox effectsVBox = new VBox(5);

        //effect 1 id
        HBox effect1IdHBox=new HBox(2);
        effect1IdHBox.setPadding(new Insets(20,0,0,8));
        Label effect1IdLabel=new Label("Effect 1 Id");
        effect1IdLabel.setPrefWidth(160);

        Spinner<Integer> effect1IdSpinner=new Spinner<>(Short.MIN_VALUE,Short.MAX_VALUE,subEntry.effect1Id);
        effect1IdSpinner.setEditable(true);
        effect1IdSpinner.valueProperty().addListener((obs,oldValue,newValue)->{
            if(newValue!=null){
                subEntry.effect1Id = newValue.shortValue();
            }

        });

        effect1IdHBox.getChildren().addAll(effect1IdLabel,effect1IdSpinner);
        effect1IdHBox.setAlignment(Pos.CENTER_LEFT);
        //effect 1 id

        //effect 1 skill id
        HBox effect1SkillIdHBox=new HBox(2);
        effect1SkillIdHBox.setPadding(new Insets(20,0,0,8));
        Label effect1SkillIdLabel=new Label("Effect 1 Skill Id");
        effect1SkillIdLabel.setPrefWidth(160);

        Spinner<Integer> effect1SkillIdSpinner=new Spinner<>(0,65535,subEntry.effect1SkillId);
        effect1SkillIdSpinner.setEditable(true);
        effect1SkillIdSpinner.valueProperty().addListener((obs,oldValue,newValue)->{
            if(newValue!=null){
                subEntry.effect1SkillId = newValue;
            }

        });

        effect1SkillIdHBox.getChildren().addAll(effect1SkillIdLabel,effect1SkillIdSpinner);
        effect1SkillIdHBox.setAlignment(Pos.CENTER_LEFT);
        //effect 1 skill id

        //effect 1 eepk type
        HBox effect1EepkTypeHBox=new HBox(2);
        effect1EepkTypeHBox.setPadding(new Insets(20,0,0,8));
        Label effect1EepkTypeLabel=new Label("Effect 1 Eepk Type");
        effect1EepkTypeLabel.setPrefWidth(160);

        GridPane effect1EepkTypeGridPane =new GridPane();
        effect1EepkTypeGridPane.setHgap(15);
        effect1EepkTypeGridPane.setVgap(15);
        effect1EepkTypeGridPane.getStyleClass().add("titled-address-box");
         
        ToggleGroup effect1EepkTypeToggleGroup=new ToggleGroup();
        RadioButton commonEffect1 = new RadioButton("Common");
        commonEffect1.setToggleGroup(effect1EepkTypeToggleGroup);
        RadioButton stageBGEffect1 = new RadioButton("StageBG");
        stageBGEffect1.setToggleGroup(effect1EepkTypeToggleGroup);
        RadioButton characterEffect1 = new RadioButton("Character");
        characterEffect1.setToggleGroup(effect1EepkTypeToggleGroup);
        RadioButton awokenSkillEffect1 = new RadioButton("Awoken Skill");
        awokenSkillEffect1.setToggleGroup(effect1EepkTypeToggleGroup);
        RadioButton unknown4Effect1 = new RadioButton("Unknown 4");
        unknown4Effect1.setToggleGroup(effect1EepkTypeToggleGroup);
        RadioButton superSkillEffect1 = new RadioButton("Super Skill");
        superSkillEffect1.setToggleGroup(effect1EepkTypeToggleGroup);
        RadioButton ultimateSkillEffect1 = new RadioButton("Ultimate Skill");
        ultimateSkillEffect1.setToggleGroup(effect1EepkTypeToggleGroup);
        RadioButton evasiveSkillEffect1 = new RadioButton("Evasive Skill");
        evasiveSkillEffect1.setToggleGroup(effect1EepkTypeToggleGroup);
        RadioButton unknown8Effect1 = new RadioButton("Unknown 8");
        unknown8Effect1.setToggleGroup(effect1EepkTypeToggleGroup);
        RadioButton kiBlastSkillEffect1 = new RadioButton("Ki Blast Skill");
        kiBlastSkillEffect1.setToggleGroup(effect1EepkTypeToggleGroup);
        RadioButton unknown10Effect1 = new RadioButton("Unknown 10");
        unknown10Effect1.setToggleGroup(effect1EepkTypeToggleGroup);
        RadioButton stageEffect1 = new RadioButton("Stage");
        stageEffect1.setToggleGroup(effect1EepkTypeToggleGroup);
        RadioButton unknown12Effect1 = new RadioButton("Unknown 12");
        unknown12Effect1.setToggleGroup(effect1EepkTypeToggleGroup);
        RadioButton unknown13Effect1 = new RadioButton("Unknown 13");
        unknown13Effect1.setToggleGroup(effect1EepkTypeToggleGroup);
        RadioButton unknown14Effect1 = new RadioButton("Unknown 14");
        unknown14Effect1.setToggleGroup(effect1EepkTypeToggleGroup);
        RadioButton unknown15Effect1 = new RadioButton("Unknown 15");
        unknown15Effect1.setToggleGroup(effect1EepkTypeToggleGroup);

        switch (subEntry.effect1EepkType) {
            case 1:  stageBGEffect1.setSelected(true);       break;
            case 2:  characterEffect1.setSelected(true);     break;
            case 3:  awokenSkillEffect1.setSelected(true);   break;
            case 4:  unknown4Effect1.setSelected(true);      break;
            case 5:  superSkillEffect1.setSelected(true);    break;
            case 6:  ultimateSkillEffect1.setSelected(true); break;
            case 7:  evasiveSkillEffect1.setSelected(true);   break;
            case 8:  unknown8Effect1.setSelected(true);      break;
            case 9:  kiBlastSkillEffect1.setSelected(true);  break;
            case 10: unknown10Effect1.setSelected(true);     break;
            case 11: stageEffect1.setSelected(true);         break;
            case 12: unknown12Effect1.setSelected(true);     break;
            case 13: unknown13Effect1.setSelected(true);     break;
            case 14: unknown14Effect1.setSelected(true);     break;
            case 15: unknown15Effect1.setSelected(true);     break;
            default: commonEffect1.setSelected(true);        break;
        }

        effect1EepkTypeToggleGroup.selectedToggleProperty().addListener((obs, oldValue, newValue) -> {
    if (newValue != null && newValue.isSelected()) {
        RadioButton selectedRadio = (RadioButton) newValue;
        
        if (selectedRadio == commonEffect1)                    { subEntry.effect1EepkType = 0;  }
            else if (selectedRadio == stageBGEffect1)          { subEntry.effect1EepkType = 1;  }
            else if (selectedRadio == characterEffect1)        { subEntry.effect1EepkType = 2;  }
            else if (selectedRadio == awokenSkillEffect1)      { subEntry.effect1EepkType = 3;  }
            else if (selectedRadio == unknown4Effect1)         { subEntry.effect1EepkType = 4;  }
            else if (selectedRadio == superSkillEffect1)       { subEntry.effect1EepkType = 5;  }
            else if (selectedRadio == ultimateSkillEffect1)    { subEntry.effect1EepkType = 6;  }
            else if (selectedRadio == evasiveSkillEffect1)     { subEntry.effect1EepkType = 7;  }
            else if (selectedRadio == unknown8Effect1)         { subEntry.effect1EepkType = 8;  }
            else if (selectedRadio == kiBlastSkillEffect1)     { subEntry.effect1EepkType = 9;  }
            else if (selectedRadio == unknown10Effect1)        { subEntry.effect1EepkType= 10; }
            else if (selectedRadio == stageEffect1)            { subEntry.effect1EepkType = 11; }
            else if (selectedRadio == unknown12Effect1)        { subEntry.effect1EepkType = 12; }
            else if (selectedRadio == unknown13Effect1)        { subEntry.effect1EepkType = 13; }
            else if (selectedRadio == unknown14Effect1)        { subEntry.effect1EepkType = 14; }
            else if (selectedRadio == unknown15Effect1)        { subEntry.effect1EepkType = 15; }
        }
    });

        effect1EepkTypeGridPane.add(commonEffect1, 0, 0);   
        effect1EepkTypeGridPane.add(stageBGEffect1, 1, 0);          
        effect1EepkTypeGridPane.add(characterEffect1, 2, 0);          
        effect1EepkTypeGridPane.add(awokenSkillEffect1, 3, 0);          

        effect1EepkTypeGridPane.add(unknown4Effect1, 0, 1);          
        effect1EepkTypeGridPane.add(superSkillEffect1, 1, 1);           
        effect1EepkTypeGridPane.add(ultimateSkillEffect1, 2, 1);
        effect1EepkTypeGridPane.add(evasiveSkillEffect1, 3, 1);

        effect1EepkTypeGridPane.add(unknown8Effect1, 0, 2);
        effect1EepkTypeGridPane.add(kiBlastSkillEffect1, 1, 2);
        effect1EepkTypeGridPane.add(unknown10Effect1, 2, 2);
        effect1EepkTypeGridPane.add(stageEffect1, 3, 2);

        effect1EepkTypeGridPane.add(unknown12Effect1, 0, 3);
        effect1EepkTypeGridPane.add(unknown13Effect1, 1, 3);
        effect1EepkTypeGridPane.add(unknown14Effect1, 2, 3);
        effect1EepkTypeGridPane.add(unknown15Effect1, 3, 3);

        
        effect1EepkTypeHBox.getChildren().addAll(effect1EepkTypeLabel,effect1EepkTypeGridPane);
        effect1EepkTypeHBox.setAlignment(Pos.CENTER_LEFT);
        //effect 1 eepk type

        //effect 2 id
        HBox effect2IdHBox=new HBox(2);
        effect2IdHBox.setPadding(new Insets(20,0,0,8));
        Label effect2IdLabel=new Label("Effect 2 Id");
        effect2IdLabel.setPrefWidth(160);

        Spinner<Integer> effect2IdSpinner=new Spinner<>(Short.MIN_VALUE,Short.MAX_VALUE,subEntry.effect2Id);
        effect2IdSpinner.setEditable(true);
        effect2IdSpinner.valueProperty().addListener((obs,oldValue,newValue)->{
            if(newValue!=null){
                subEntry.effect2Id = newValue.shortValue();
            }

        });

        effect2IdHBox.getChildren().addAll(effect2IdLabel,effect2IdSpinner);
        effect2IdHBox.setAlignment(Pos.CENTER_LEFT);
        //effect 2 id

        //effect 2 skill id
        HBox effect2SkillIdHBox=new HBox(2);
        effect2SkillIdHBox.setPadding(new Insets(20,0,0,8));
        Label effect2SkillIdLabel=new Label("Effect 2 Skill Id");
        effect2SkillIdLabel.setPrefWidth(160);

        Spinner<Integer> effect2SkillIdSpinner=new Spinner<>(0,65535,subEntry.effect2SkillId);
        effect2SkillIdSpinner.setEditable(true);
        effect2SkillIdSpinner.valueProperty().addListener((obs,oldValue,newValue)->{
            if(newValue!=null){
                subEntry.effect2SkillId = newValue;
            }

        });

        effect2SkillIdHBox.getChildren().addAll(effect2SkillIdLabel,effect2SkillIdSpinner);
        effect2SkillIdHBox.setAlignment(Pos.CENTER_LEFT);
        //effect 2 skill id

        //effect 2 eepk type
        HBox effect2EepkTypeHBox=new HBox(2);
        effect2EepkTypeHBox.setPadding(new Insets(20,0,0,8));
        Label effect2EepkTypeLabel=new Label("Effect 2 Eepk Type");
        effect2EepkTypeLabel.setPrefWidth(160);

        GridPane effect2EepkTypeGridPane =new GridPane();
        effect2EepkTypeGridPane.setHgap(15);
        effect2EepkTypeGridPane.setVgap(15);
        effect2EepkTypeGridPane.getStyleClass().add("titled-address-box");
         
        ToggleGroup effect2EepkTypeToggleGroup=new ToggleGroup();
        RadioButton commonEffect2 = new RadioButton("Common");
        commonEffect2.setToggleGroup(effect2EepkTypeToggleGroup);
        RadioButton stageBGEffect2 = new RadioButton("StageBG");
        stageBGEffect2.setToggleGroup(effect2EepkTypeToggleGroup);
        RadioButton characterEffect2 = new RadioButton("Character");
        characterEffect2.setToggleGroup(effect2EepkTypeToggleGroup);
        RadioButton awokenSkillEffect2 = new RadioButton("Awoken Skill");
        awokenSkillEffect2.setToggleGroup(effect2EepkTypeToggleGroup);
        RadioButton unknown4Effect2 = new RadioButton("Unknown 4");
        unknown4Effect2.setToggleGroup(effect2EepkTypeToggleGroup);
        RadioButton superSkillEffect2 = new RadioButton("Super Skill");
        superSkillEffect2.setToggleGroup(effect2EepkTypeToggleGroup);
        RadioButton ultimateSkillEffect2 = new RadioButton("Ultimate Skill");
        ultimateSkillEffect2.setToggleGroup(effect2EepkTypeToggleGroup);
        RadioButton evasiveSkillEffect2 = new RadioButton("Evasive Skill");
        evasiveSkillEffect2.setToggleGroup(effect2EepkTypeToggleGroup);
        RadioButton unknown8Effect2 = new RadioButton("Unknown 8");
        unknown8Effect2.setToggleGroup(effect2EepkTypeToggleGroup);
        RadioButton kiBlastSkillEffect2 = new RadioButton("Ki Blast Skill");
        kiBlastSkillEffect2.setToggleGroup(effect2EepkTypeToggleGroup);
        RadioButton unknown10Effect2 = new RadioButton("Unknown 10");
        unknown10Effect2.setToggleGroup(effect2EepkTypeToggleGroup);
        RadioButton stageEffect2 = new RadioButton("Stage");
        stageEffect2.setToggleGroup(effect2EepkTypeToggleGroup);
        RadioButton unknown12Effect2 = new RadioButton("Unknown 12");
        unknown12Effect2.setToggleGroup(effect2EepkTypeToggleGroup);
        RadioButton unknown13Effect2 = new RadioButton("Unknown 13");
        unknown13Effect2.setToggleGroup(effect2EepkTypeToggleGroup);
        RadioButton unknown14Effect2 = new RadioButton("Unknown 14");
        unknown14Effect2.setToggleGroup(effect2EepkTypeToggleGroup);
        RadioButton unknown15Effect2 = new RadioButton("Unknown 15");
        unknown15Effect2.setToggleGroup(effect2EepkTypeToggleGroup);

        switch (subEntry.effect2EepkType) {
            case 1:  stageBGEffect2.setSelected(true);       break;
            case 2:  characterEffect2.setSelected(true);     break;
            case 3:  awokenSkillEffect2.setSelected(true);   break;
            case 4:  unknown4Effect2.setSelected(true);      break;
            case 5:  superSkillEffect2.setSelected(true);    break;
            case 6:  ultimateSkillEffect2.setSelected(true); break;
            case 7:  evasiveSkillEffect2.setSelected(true);   break;
            case 8:  unknown8Effect2.setSelected(true);      break;
            case 9:  kiBlastSkillEffect2.setSelected(true);  break;
            case 10: unknown10Effect2.setSelected(true);     break;
            case 11: stageEffect2.setSelected(true);         break;
            case 12: unknown12Effect2.setSelected(true);     break;
            case 13: unknown13Effect2.setSelected(true);     break;
            case 14: unknown14Effect2.setSelected(true);     break;
            case 15: unknown15Effect2.setSelected(true);     break;
            default: commonEffect2.setSelected(true);        break;
        }

        effect2EepkTypeToggleGroup.selectedToggleProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null && newValue.isSelected()) {
                RadioButton selectedRadio = (RadioButton) newValue;
        
                if      (selectedRadio == commonEffect2)           { subEntry.effect2EepkType = 0;  }
                else if (selectedRadio == stageBGEffect2)          { subEntry.effect2EepkType = 1;  }
                else if (selectedRadio == characterEffect2)        { subEntry.effect2EepkType = 2;  }
                else if (selectedRadio == awokenSkillEffect2)      { subEntry.effect2EepkType = 3;  }
                else if (selectedRadio == unknown4Effect2)         { subEntry.effect2EepkType = 4;  }
                else if (selectedRadio == superSkillEffect2)       { subEntry.effect2EepkType = 5;  }
                else if (selectedRadio == ultimateSkillEffect2)    { subEntry.effect2EepkType = 6;  }
                else if (selectedRadio == evasiveSkillEffect2)     { subEntry.effect2EepkType = 7;  }
                else if (selectedRadio == unknown8Effect2)         { subEntry.effect2EepkType = 8;  }
                else if (selectedRadio == kiBlastSkillEffect2)     { subEntry.effect2EepkType = 9;  }
                else if (selectedRadio == unknown10Effect2)        { subEntry.effect2EepkType = 10; }
                else if (selectedRadio == stageEffect2)            { subEntry.effect2EepkType = 11; }
                else if (selectedRadio == unknown12Effect2)        { subEntry.effect2EepkType = 12; }
                else if (selectedRadio == unknown13Effect2)        { subEntry.effect2EepkType = 13; }
                else if (selectedRadio == unknown14Effect2)        { subEntry.effect2EepkType = 14; }
                else if (selectedRadio == unknown15Effect2)        { subEntry.effect2EepkType = 15; }
            }
        });

        effect2EepkTypeGridPane.add(commonEffect2, 0, 0);   
        effect2EepkTypeGridPane.add(stageBGEffect1, 1, 0);          
        effect2EepkTypeGridPane.add(characterEffect2, 2, 0);          
        effect2EepkTypeGridPane.add(awokenSkillEffect2, 3, 0);          

        effect2EepkTypeGridPane.add(unknown4Effect2, 0, 1);          
        effect2EepkTypeGridPane.add(superSkillEffect2, 1, 1);           
        effect2EepkTypeGridPane.add(ultimateSkillEffect2, 2, 1);
        effect2EepkTypeGridPane.add(evasiveSkillEffect2, 3, 1);

        effect2EepkTypeGridPane.add(unknown8Effect2, 0, 2);
        effect2EepkTypeGridPane.add(kiBlastSkillEffect2, 1, 2);
        effect2EepkTypeGridPane.add(unknown10Effect2, 2, 2);
        effect2EepkTypeGridPane.add(stageEffect2, 3, 2);

        effect2EepkTypeGridPane.add(unknown12Effect2, 0, 3);
        effect2EepkTypeGridPane.add(unknown13Effect2, 1, 3);
        effect2EepkTypeGridPane.add(unknown14Effect2, 2, 3);
        effect2EepkTypeGridPane.add(unknown15Effect2, 3, 3);

        
        effect2EepkTypeHBox.getChildren().addAll(effect2EepkTypeLabel,effect2EepkTypeGridPane);
        effect2EepkTypeHBox.setAlignment(Pos.CENTER_LEFT);
        //effect 2 eepk type

        //effect 3 id
        HBox effect3IdHBox=new HBox(2);
        effect3IdHBox.setPadding(new Insets(20,0,0,8));
        Label effect3IdLabel=new Label("Effect 3 Id");
        effect3IdLabel.setPrefWidth(160);

        Spinner<Integer> effect3IdSpinner=new Spinner<>(Short.MIN_VALUE,Short.MAX_VALUE,subEntry.effect3Id);
        effect3IdSpinner.setEditable(true);
        effect3IdSpinner.valueProperty().addListener((obs,oldValue,newValue)->{
            if(newValue!=null){
                subEntry.effect3Id = newValue.shortValue();
            }

        });

        effect3IdHBox.getChildren().addAll(effect3IdLabel,effect3IdSpinner);
        effect3IdHBox.setAlignment(Pos.CENTER_LEFT);
        //effect 3 id

        //effect 3 skill id
        HBox effect3SkillIdHBox=new HBox(2);
        effect3SkillIdHBox.setPadding(new Insets(20,0,0,8));
        Label effect3SkillIdLabel=new Label("Effect 3 Skill Id");
        effect3SkillIdLabel.setPrefWidth(160);

        Spinner<Integer> effect3SkillIdSpinner=new Spinner<>(0,65535,subEntry.effect3SkillId);
        effect3SkillIdSpinner.setEditable(true);
        effect3SkillIdSpinner.valueProperty().addListener((obs,oldValue,newValue)->{
            if(newValue!=null){
                subEntry.effect3SkillId = newValue;
            }

        });

        effect3SkillIdHBox.getChildren().addAll(effect3SkillIdLabel,effect3SkillIdSpinner);
        effect3SkillIdHBox.setAlignment(Pos.CENTER_LEFT);
        //effect 3 skill id

        //effect 3 eepk type
        HBox effect3EepkTypeHBox=new HBox(2);
        effect3EepkTypeHBox.setPadding(new Insets(20,0,0,8));
        Label effect3EepkTypeLabel=new Label("Effect 3 Eepk Type");
        effect3EepkTypeLabel.setPrefWidth(160);

        GridPane effect3EepkTypeGridPane =new GridPane();
        effect3EepkTypeGridPane.setHgap(15);
        effect3EepkTypeGridPane.setVgap(15);
        effect3EepkTypeGridPane.getStyleClass().add("titled-address-box");
         
        ToggleGroup effect3EepkTypeToggleGroup=new ToggleGroup();
        RadioButton commonEffect3 = new RadioButton("Common");
        commonEffect3.setToggleGroup(effect3EepkTypeToggleGroup);
        RadioButton stageBGEffect3 = new RadioButton("StageBG");
        stageBGEffect3.setToggleGroup(effect3EepkTypeToggleGroup);
        RadioButton characterEffect3 = new RadioButton("Character");
        characterEffect3.setToggleGroup(effect3EepkTypeToggleGroup);
        RadioButton awokenSkillEffect3 = new RadioButton("Awoken Skill");
        awokenSkillEffect3.setToggleGroup(effect3EepkTypeToggleGroup);
        RadioButton unknown4Effect3 = new RadioButton("Unknown 4");
        unknown4Effect3.setToggleGroup(effect3EepkTypeToggleGroup);
        RadioButton superSkillEffect3 = new RadioButton("Super Skill");
        superSkillEffect3.setToggleGroup(effect3EepkTypeToggleGroup);
        RadioButton ultimateSkillEffect3 = new RadioButton("Ultimate Skill");
        ultimateSkillEffect3.setToggleGroup(effect3EepkTypeToggleGroup);
        RadioButton evasiveSkillEffect3 = new RadioButton("Evasive Skill");
        evasiveSkillEffect3.setToggleGroup(effect3EepkTypeToggleGroup);
        RadioButton unknown8Effect3 = new RadioButton("Unknown 8");
        unknown8Effect3.setToggleGroup(effect3EepkTypeToggleGroup);
        RadioButton kiBlastSkillEffect3 = new RadioButton("Ki Blast Skill");
        kiBlastSkillEffect3.setToggleGroup(effect3EepkTypeToggleGroup);
        RadioButton unknown10Effect3 = new RadioButton("Unknown 10");
        unknown10Effect3.setToggleGroup(effect3EepkTypeToggleGroup);
        RadioButton stageEffect3 = new RadioButton("Stage");
        stageEffect3.setToggleGroup(effect3EepkTypeToggleGroup);
        RadioButton unknown12Effect3 = new RadioButton("Unknown 12");
        unknown12Effect3.setToggleGroup(effect3EepkTypeToggleGroup);
        RadioButton unknown13Effect3 = new RadioButton("Unknown 13");
        unknown13Effect3.setToggleGroup(effect3EepkTypeToggleGroup);
        RadioButton unknown14Effect3 = new RadioButton("Unknown 14");
        unknown14Effect3.setToggleGroup(effect3EepkTypeToggleGroup);
        RadioButton unknown15Effect3 = new RadioButton("Unknown 15");
        unknown15Effect3.setToggleGroup(effect3EepkTypeToggleGroup);

        switch (subEntry.effect3EepkType) {
            case 1:  stageBGEffect3.setSelected(true);       break;
            case 2:  characterEffect3.setSelected(true);     break;
            case 3:  awokenSkillEffect3.setSelected(true);   break;
            case 4:  unknown4Effect3.setSelected(true);      break;
            case 5:  superSkillEffect3.setSelected(true);    break;
            case 6:  ultimateSkillEffect3.setSelected(true); break;
            case 7:  evasiveSkillEffect3.setSelected(true);   break;
            case 8:  unknown8Effect3.setSelected(true);      break;
            case 9:  kiBlastSkillEffect3.setSelected(true);  break;
            case 10: unknown10Effect3.setSelected(true);     break;
            case 11: stageEffect3.setSelected(true);         break;
            case 12: unknown12Effect3.setSelected(true);     break;
            case 13: unknown13Effect3.setSelected(true);     break;
            case 14: unknown14Effect3.setSelected(true);     break;
            case 15: unknown15Effect3.setSelected(true);     break;
            default: commonEffect3.setSelected(true);        break;
        }

        effect3EepkTypeToggleGroup.selectedToggleProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null && newValue.isSelected()) {
                RadioButton selectedRadio = (RadioButton) newValue;
        
                if      (selectedRadio == commonEffect3)           { subEntry.effect3EepkType = 0;  }
                else if (selectedRadio == stageBGEffect3)          { subEntry.effect3EepkType = 1;  }
                else if (selectedRadio == characterEffect3)        { subEntry.effect3EepkType = 2;  }
                else if (selectedRadio == awokenSkillEffect3)      { subEntry.effect3EepkType = 3;  }
                else if (selectedRadio == unknown4Effect3)         { subEntry.effect3EepkType = 4;  }
                else if (selectedRadio == superSkillEffect3)       { subEntry.effect3EepkType = 5;  }
                else if (selectedRadio == ultimateSkillEffect3)    { subEntry.effect3EepkType = 6;  }
                else if (selectedRadio == evasiveSkillEffect3)     { subEntry.effect3EepkType = 7;  }
                else if (selectedRadio == unknown8Effect3)         { subEntry.effect3EepkType = 8;  }
                else if (selectedRadio == kiBlastSkillEffect3)     { subEntry.effect3EepkType = 9;  }
                else if (selectedRadio == unknown10Effect3)        { subEntry.effect3EepkType= 10; }
                else if (selectedRadio == stageEffect3)            { subEntry.effect3EepkType = 11; }
                else if (selectedRadio == unknown12Effect3)        { subEntry.effect3EepkType = 12; }
                else if (selectedRadio == unknown13Effect3)        { subEntry.effect3EepkType = 13; }
                else if (selectedRadio == unknown14Effect3)        { subEntry.effect3EepkType = 14; }
                else if (selectedRadio == unknown15Effect3)        { subEntry.effect3EepkType = 15; }
            }
        });

        effect3EepkTypeGridPane.add(commonEffect3, 0, 0);   
        effect3EepkTypeGridPane.add(stageBGEffect1, 1, 0);          
        effect3EepkTypeGridPane.add(characterEffect3, 2, 0);          
        effect3EepkTypeGridPane.add(awokenSkillEffect3, 3, 0);          

        effect3EepkTypeGridPane.add(unknown4Effect3, 0, 1);          
        effect3EepkTypeGridPane.add(superSkillEffect3, 1, 1);           
        effect3EepkTypeGridPane.add(ultimateSkillEffect3, 2, 1);
        effect3EepkTypeGridPane.add(evasiveSkillEffect3, 3, 1);

        effect3EepkTypeGridPane.add(unknown8Effect3, 0, 2);
        effect3EepkTypeGridPane.add(kiBlastSkillEffect3, 1, 2);
        effect3EepkTypeGridPane.add(unknown10Effect3, 2, 2);
        effect3EepkTypeGridPane.add(stageEffect3, 3, 2);

        effect3EepkTypeGridPane.add(unknown12Effect3, 0, 3);
        effect3EepkTypeGridPane.add(unknown13Effect3, 1, 3);
        effect3EepkTypeGridPane.add(unknown14Effect3, 2, 3);
        effect3EepkTypeGridPane.add(unknown15Effect3, 3, 3);

        
        effect3EepkTypeHBox.getChildren().addAll(effect3EepkTypeLabel,effect3EepkTypeGridPane);
        effect3EepkTypeHBox.setAlignment(Pos.CENTER_LEFT);
        //effect 2 eepk type

        effectsVBox.getChildren().addAll(effect1IdHBox,effect1SkillIdHBox,effect1EepkTypeHBox,effect2IdHBox,effect2SkillIdHBox,effect2EepkTypeHBox,effect3IdHBox,effect3SkillIdHBox,effect3EepkTypeHBox);
        effectsScrollPane.setContent(effectsVBox);
        return effectsScrollPane;
    }

    private VBox createPushbackVBox(BdmSubEntry subEntry){
        VBox pushBackVBox = new VBox(5);

        //pushback strength
        HBox pushbackStrengthHBox=new HBox(2);
        pushbackStrengthHBox.setPadding(new Insets(20,0,0,8));
        Label pushbackStrengthLabel=new Label("Pushback Strength");
        pushbackStrengthLabel.setPrefWidth(200);

        Spinner<Double> pushbackStrengthSpinner=new Spinner<>(Float.MIN_VALUE,Float.MAX_VALUE,(double)subEntry.pushbackStrength);
        pushbackStrengthSpinner.setEditable(true);
        pushbackStrengthSpinner.valueProperty().addListener((obs,oldValue,newValue)->{
            if(newValue!=null){
                subEntry.pushbackStrength = newValue.floatValue();
            }
        });

        pushbackStrengthHBox.getChildren().addAll(pushbackStrengthLabel,pushbackStrengthSpinner);
        pushbackStrengthHBox.setAlignment(Pos.CENTER_LEFT);
        //pushback strength

        //pushback acceleration
        HBox pushbackAccelerationHBox=new HBox(2);
        pushbackAccelerationHBox.setPadding(new Insets(20,0,0,8));
        Label pushbackAccelerationLabel=new Label("Pushback Acceleration");
        pushbackAccelerationLabel.setPrefWidth(200);

        Spinner<Double> pushbackAccelerationSpinner=new Spinner<>(Float.MIN_VALUE,Float.MAX_VALUE,(double)subEntry.pushbackAcceleration);
        pushbackAccelerationSpinner.setEditable(true);
        pushbackAccelerationSpinner.valueProperty().addListener((obs,oldValue,newValue)->{
            if(newValue!=null){
                subEntry.pushbackAcceleration = newValue.floatValue();
            }
        });

        pushbackAccelerationHBox.getChildren().addAll(pushbackAccelerationLabel,pushbackAccelerationSpinner);
        pushbackAccelerationHBox.setAlignment(Pos.CENTER_LEFT);
        //pushback acceleration

        //user stunt
        HBox userStuntHBox=new HBox(2);
        userStuntHBox.setPadding(new Insets(20,0,0,8));
        Label userStuntLabel=new Label("User Stunt");
        userStuntLabel.setPrefWidth(200);

        Spinner<Integer> userStuntSpinner=new Spinner<>(0,65535,subEntry.userStunt);
        userStuntSpinner.setEditable(true);
        userStuntSpinner.valueProperty().addListener((obs,oldValue,newValue)->{
            if(newValue!=null){
                subEntry.userStunt = newValue;
            }

        });

        userStuntHBox.getChildren().addAll(userStuntLabel,userStuntSpinner);
        userStuntHBox.setAlignment(Pos.CENTER_LEFT);
        //user stunt

        //victim stunt
        HBox victimStuntHBox=new HBox(2);
        victimStuntHBox.setPadding(new Insets(20,0,0,8));
        Label victimStuntLabel=new Label("Victim Stunt");
        victimStuntLabel.setPrefWidth(200);

        Spinner<Integer> victimStuntSpinner=new Spinner<>(0,65535,subEntry.victimStunt);
        victimStuntSpinner.setEditable(true);
        victimStuntSpinner.valueProperty().addListener((obs,oldValue,newValue)->{
            if(newValue!=null){
                subEntry.victimStunt = newValue;
            }

        });
        victimStuntHBox.getChildren().addAll(victimStuntLabel,victimStuntSpinner);
        victimStuntHBox.setAlignment(Pos.CENTER_LEFT);
        //victim stunt

       

        //knockback duration
        HBox knockbackDurationHBox=new HBox(2);
        knockbackDurationHBox.setPadding(new Insets(20,0,0,8));
        Label knockbackDurationLabel=new Label("Knockback Duration");
        knockbackDurationLabel.setPrefWidth(200);

        Spinner<Integer> knockbackDurationSpinner=new Spinner<>(0,65535,subEntry.knockbackDuration);
        knockbackDurationSpinner.setEditable(true);
        knockbackDurationSpinner.valueProperty().addListener((obs,oldValue,newValue)->{
            if(newValue!=null){
                subEntry.knockbackDuration = newValue;
            }

        });
        knockbackDurationHBox.getChildren().addAll(knockbackDurationLabel,knockbackDurationSpinner);
        knockbackDurationHBox.setAlignment(Pos.CENTER_LEFT);
        //knockback duration

        //knockback ground impact time
        HBox knockbackGroundImpactTimeHBox=new HBox(2);
        knockbackGroundImpactTimeHBox.setPadding(new Insets(20,0,0,8));
        Label knockbackGroundImpactTimeLabel=new Label("Knockback Ground Impact Time");
        knockbackGroundImpactTimeLabel.setPrefWidth(200);

        Spinner<Integer> knockbackGroundImpactSpinner=new Spinner<>(0,65535,subEntry.knockbackGroundImpactTime);
        knockbackGroundImpactSpinner.setEditable(true);
        knockbackGroundImpactSpinner.valueProperty().addListener((obs,oldValue,newValue)->{
            if(newValue!=null){
               subEntry.knockbackGroundImpactTime = newValue;
            }

        });
        knockbackGroundImpactTimeHBox.getChildren().addAll(knockbackGroundImpactTimeLabel,knockbackGroundImpactSpinner);
        knockbackGroundImpactTimeHBox.setAlignment(Pos.CENTER_LEFT);
        //knockback ground impact time

        //knockback ground impact time
        HBox knockbackRecoveryAfterImpactTimeHBox=new HBox(2);
        knockbackRecoveryAfterImpactTimeHBox.setPadding(new Insets(20,0,0,8));
        Label knockbackRecoveryAfterImpactTimeLabel=new Label("Knockback Recovery After Impact");
        knockbackRecoveryAfterImpactTimeLabel.setPrefWidth(200);

        Spinner<Integer> knockbackRecoveryAfterImpactSpinner=new Spinner<>(0,65535,subEntry.knockbackRecoveryAfterImpactTime);
        knockbackRecoveryAfterImpactSpinner.setEditable(true);
        knockbackRecoveryAfterImpactSpinner.valueProperty().addListener((obs,oldValue,newValue)->{
            if(newValue!=null){
                subEntry.knockbackRecoveryAfterImpactTime = newValue;
            }

        });
        knockbackRecoveryAfterImpactTimeHBox.getChildren().addAll(knockbackRecoveryAfterImpactTimeLabel,knockbackRecoveryAfterImpactSpinner);
        knockbackRecoveryAfterImpactTimeHBox.setAlignment(Pos.CENTER_LEFT);
        //knockback recovery after impact

        //knockback strength x
        HBox knockbackStrengthXHBox=new HBox(2);
        knockbackStrengthXHBox.setPadding(new Insets(20,0,0,8));
        Label knockbackStrengthXLabel=new Label("Knockback Strength X");
        knockbackStrengthXLabel.setPrefWidth(200);

        Spinner<Double> knockbackStrengthXSpinner=new Spinner<>(Float.MIN_VALUE,Float.MAX_VALUE,(double)subEntry.knockbackStrengthX);
        knockbackStrengthXSpinner.setEditable(true);
        knockbackStrengthXSpinner.valueProperty().addListener((obs,oldValue,newValue)->{
            if(newValue!=null){
                subEntry.knockbackStrengthX = newValue.floatValue();
            }
        });

        knockbackStrengthXHBox.getChildren().addAll(knockbackStrengthXLabel,knockbackStrengthXSpinner);
        knockbackStrengthXHBox.setAlignment(Pos.CENTER_LEFT);
        //knockback strength x

        //knockback strength y
        HBox knockbackStrengthYHBox=new HBox(2);
        knockbackStrengthYHBox.setPadding(new Insets(20,0,0,8));
        Label knockbackStrengthYLabel=new Label("Knockback Strength Y");
        knockbackStrengthYLabel.setPrefWidth(200);

        Spinner<Double> knockbackStrengthYSpinner=new Spinner<>(Float.MIN_VALUE,Float.MAX_VALUE,(double)subEntry.knockbackStrengthY);
        knockbackStrengthYSpinner.setEditable(true);
        knockbackStrengthYSpinner.valueProperty().addListener((obs,oldValue,newValue)->{
            if(newValue!=null){
                subEntry.knockbackStrengthY = newValue.floatValue();
            }
        });

        knockbackStrengthYHBox.getChildren().addAll(knockbackStrengthYLabel,knockbackStrengthYSpinner);
        knockbackStrengthYHBox.setAlignment(Pos.CENTER_LEFT);
        //knockback strength y

        //knockback strength z
        HBox knockbackStrengthZHBox=new HBox(2);
        knockbackStrengthZHBox.setPadding(new Insets(20,0,0,8));
        Label knockbackStrengthZLabel=new Label("Knockback Strength Z");
        knockbackStrengthZLabel.setPrefWidth(200);

        Spinner<Double> knockbackStrengthZSpinner=new Spinner<>(Float.MIN_VALUE,Float.MAX_VALUE,(double)subEntry.knockbackStrengthZ);
        knockbackStrengthZSpinner.setEditable(true);
        knockbackStrengthZSpinner.valueProperty().addListener((obs,oldValue,newValue)->{
            if(newValue!=null){
                subEntry.knockbackStrengthZ = newValue.floatValue();
            }
        });

        knockbackStrengthZHBox.getChildren().addAll(knockbackStrengthZLabel,knockbackStrengthZSpinner);
        knockbackStrengthZHBox.setAlignment(Pos.CENTER_LEFT);
        //knockback strength z

        //knockback drag y
        HBox knockbackDragYHBox=new HBox(2);
        knockbackDragYHBox.setPadding(new Insets(20,0,0,8));
        Label knockbackSDragYLabel=new Label("Knockback Strength Y");
        knockbackSDragYLabel.setPrefWidth(200);

        Spinner<Double> knockbackSDragYSpinner=new Spinner<>(Float.MIN_VALUE,Float.MAX_VALUE,(double)subEntry.knockbackDragY);
        knockbackSDragYSpinner.setEditable(true);
        knockbackSDragYSpinner.valueProperty().addListener((obs,oldValue,newValue)->{
            if(newValue!=null){
                subEntry.knockbackDragY = newValue.floatValue();
            }
        });

        knockbackDragYHBox.getChildren().addAll(knockbackSDragYLabel,knockbackSDragYSpinner);
        knockbackDragYHBox.setAlignment(Pos.CENTER_LEFT);
        //knockback drag y

        //knockback gravity time
        HBox knockbackGravityTimeHBox=new HBox(2);
        knockbackGravityTimeHBox.setPadding(new Insets(20,0,0,8));
        Label knockbackGravityTimeLabel=new Label("Knockback Gravity Time");
        knockbackGravityTimeLabel.setPrefWidth(200);

        Spinner<Integer> knockbackGravityTimeSpinner=new Spinner<>(Short.MIN_VALUE,Short.MAX_VALUE,subEntry.knockbackGravityTime);
        knockbackGravityTimeSpinner.setEditable(true);
        knockbackGravityTimeSpinner.valueProperty().addListener((obs,oldValue,newValue)->{
            if(newValue!=null){
                subEntry.knockbackGravityTime = newValue;
            }

        });
        knockbackGravityTimeHBox.getChildren().addAll(knockbackGravityTimeLabel,knockbackGravityTimeSpinner);
        knockbackGravityTimeHBox.setAlignment(Pos.CENTER_LEFT);
        //knockback gravity time

        //knockback gravity time
        HBox victimInvincibilityTimeHBox=new HBox(2);
        victimInvincibilityTimeHBox.setPadding(new Insets(20,0,0,8));
        Label victimInvincibilityTimeLabel=new Label("Victim Invincibility Time");
        victimInvincibilityTimeLabel.setPrefWidth(200);

        Spinner<Integer> victimInvincibilityTimeSpinner=new Spinner<>(Short.MIN_VALUE,Short.MAX_VALUE,subEntry.victimInvincibilityTime);
        victimInvincibilityTimeSpinner.setEditable(true);
        victimInvincibilityTimeSpinner.valueProperty().addListener((obs,oldValue,newValue)->{
            if(newValue!=null){
                subEntry.victimInvincibilityTime = newValue.shortValue();
            }

        });
        victimInvincibilityTimeHBox.getChildren().addAll(victimInvincibilityTimeLabel,victimInvincibilityTimeSpinner);
        victimInvincibilityTimeHBox.setAlignment(Pos.CENTER_LEFT);
        //knockback gravity time

        pushBackVBox.getChildren().addAll(pushbackStrengthHBox,pushbackAccelerationHBox,userStuntHBox,victimStuntHBox,knockbackDurationHBox,
        knockbackGroundImpactTimeHBox,knockbackRecoveryAfterImpactTimeHBox,knockbackStrengthXHBox,
        knockbackStrengthYHBox,knockbackStrengthZHBox,knockbackDragYHBox,knockbackGravityTimeHBox,victimInvincibilityTimeHBox);
        
        return pushBackVBox;
    }

    private VBox createCameraVBox(BdmSubEntry subEntry){
        VBox cameraVBox = new VBox(5);

        //camera shake type
        HBox cameraShakeTypeHBox=new HBox(12);
        cameraShakeTypeHBox.setPadding(new Insets(20,0,0,8));
        Label cameraShakeTypeLabel=new Label("Camera Shake Type");
        cameraShakeTypeLabel.setPrefWidth(150);
        Label cameraShakeTypeIndicatorLabel = new Label();

        Spinner<Integer> cameraShakeTypeSpinner=new Spinner<>(Short.MIN_VALUE,Short.MAX_VALUE,subEntry.cameraShakeType);
        cameraShakeTypeSpinner.setEditable(true);
        cameraShakeTypeSpinner.valueProperty().addListener((obs,oldValue,newValue)->{
            if(newValue!=null){
                subEntry.cameraShakeType = newValue.shortValue();
            }

        });
        cameraShakeTypeIndicatorLabel.setTextFill(Color.CRIMSON);
        cameraShakeTypeIndicatorLabel.textProperty().bind(
            Bindings.when(cameraShakeTypeSpinner.valueProperty().isEqualTo(-1).or(cameraShakeTypeSpinner.valueProperty().isEqualTo(3)).or(cameraShakeTypeSpinner.valueProperty().isEqualTo(4).or(cameraShakeTypeSpinner.valueProperty().isEqualTo(5).or(cameraShakeTypeSpinner.valueProperty().isEqualTo(9))))).then("None").
            otherwise(Bindings.when(cameraShakeTypeSpinner.valueProperty().isEqualTo(0)).then("Rumble").
            otherwise(Bindings.when(cameraShakeTypeSpinner.valueProperty().isEqualTo(1)).then("Heavy Rumble").
            otherwise(Bindings.when(cameraShakeTypeSpinner.valueProperty().isEqualTo(2)).then("Extreme Rumble").
            otherwise(Bindings.when(cameraShakeTypeSpinner.valueProperty().isEqualTo(6).or(cameraShakeTypeSpinner.valueProperty().isEqualTo(10))).then("Camera Zoom").
            otherwise(Bindings.when(cameraShakeTypeSpinner.valueProperty().isEqualTo(7)).then("Static Camera").
            otherwise(Bindings.when(cameraShakeTypeSpinner.valueProperty().isEqualTo(8)).then("Camera Focus").
            otherwise("Unknown")))))))
        );
        cameraShakeTypeHBox.getChildren().addAll(cameraShakeTypeLabel,cameraShakeTypeSpinner,cameraShakeTypeIndicatorLabel);
        cameraShakeTypeHBox.setAlignment(Pos.CENTER_LEFT);
        //camera shake type
        

        //camera shake time
        HBox cameraShakeTimeHBox=new HBox(12);
        cameraShakeTimeHBox.setPadding(new Insets(20,0,0,8));
        Label cameraShakeTimeLabel=new Label("Camera Shake Time");
        cameraShakeTimeLabel.setPrefWidth(150);

        Spinner<Integer> cameraShakeTimeSpinner=new Spinner<>(0,65535,subEntry.cameraShakeTime);
        cameraShakeTimeSpinner.setEditable(true);
        cameraShakeTimeSpinner.valueProperty().addListener((obs,oldValue,newValue)->{
            if(newValue!=null){
                subEntry.cameraShakeTime = newValue.shortValue();
            }

        });
       
        cameraShakeTimeHBox.getChildren().addAll(cameraShakeTimeLabel,cameraShakeTimeSpinner);
        cameraShakeTimeHBox.setAlignment(Pos.CENTER_LEFT);
        //camera shake time

        //user bpe id
        HBox userBpeIdHBox=new HBox(12);
        userBpeIdHBox.setPadding(new Insets(20,0,0,8));
        Label userBpeIdLabel=new Label("User Bpe Id");
        userBpeIdLabel.setPrefWidth(150);

        Spinner<Integer> userBpeIdSpinner=new Spinner<>(Short.MIN_VALUE,Short.MAX_VALUE,subEntry.userBpeID);
        userBpeIdSpinner.setEditable(true);
        userBpeIdSpinner.valueProperty().addListener((obs,oldValue,newValue)->{
            if(newValue!=null){
                subEntry.userBpeID = newValue.shortValue();
            }

        });
       
        userBpeIdHBox.getChildren().addAll(userBpeIdLabel,userBpeIdSpinner);
        userBpeIdHBox.setAlignment(Pos.CENTER_LEFT);
        //user bpe id

        //opponent bpe id
        HBox victimBpeIdHBox=new HBox(12);
        victimBpeIdHBox.setPadding(new Insets(20,0,0,8));
        Label victimBpeIdLabel=new Label("Victim Bpe Id");
        victimBpeIdLabel.setPrefWidth(150);

        Spinner<Integer> victimBpeIdSpinner=new Spinner<>(Short.MIN_VALUE,Short.MAX_VALUE,subEntry.victimBpeID);
        victimBpeIdSpinner.setEditable(true);
        victimBpeIdSpinner.valueProperty().addListener((obs,oldValue,newValue)->{
            if(newValue!=null){
                subEntry.victimBpeID = newValue.shortValue();
            }

        });
       
        victimBpeIdHBox.getChildren().addAll(victimBpeIdLabel,victimBpeIdSpinner);
        victimBpeIdHBox.setAlignment(Pos.CENTER_LEFT);
        //opponent bpe id

        cameraVBox.getChildren().addAll(cameraShakeTypeHBox,cameraShakeTimeHBox,userBpeIdHBox,victimBpeIdHBox);
        
        return cameraVBox;
    }

    private VBox createMiscVBox(BdmSubEntry subEntry){
        VBox miscVBox = new VBox(5);

        //transformation type
        HBox transformationTypeHBox=new HBox(12);
        transformationTypeHBox.setPadding(new Insets(20,0,0,8));
        Label transformationTypeLabel=new Label("Transformation Type");
        transformationTypeLabel.setPrefWidth(180);
        Label trasnformationTypeIndicatorLabel = new Label();

        Spinner<Integer> trasnsformationTypeSpinner=new Spinner<>(0,65535,subEntry.transformationType);
        trasnsformationTypeSpinner.setEditable(true);
        trasnsformationTypeSpinner.valueProperty().addListener((obs,oldValue,newValue)->{
            if(newValue!=null){
                subEntry.transformationType = newValue.shortValue();
            }

        });
        trasnformationTypeIndicatorLabel.setTextFill(Color.CRIMSON);
        trasnformationTypeIndicatorLabel.textProperty().bind(
            Bindings.when(trasnsformationTypeSpinner.valueProperty().isEqualTo(0)).then("None").
            otherwise(Bindings.when(trasnsformationTypeSpinner.valueProperty().isEqualTo(1)).then("Candy").
            otherwise("Unknown"))
        );
        transformationTypeHBox.getChildren().addAll(transformationTypeLabel,trasnsformationTypeSpinner,trasnformationTypeIndicatorLabel);
        transformationTypeHBox.setAlignment(Pos.CENTER_LEFT);
        //transformation type

        //aliment Type
        HBox alimentTypeHBox=new HBox(2);
        alimentTypeHBox.setPadding(new Insets(20,0,0,8));

        Label alimentTypeLabel=new Label("Aliment Type");
        alimentTypeLabel.setPrefWidth(190);

        //properties 1
        Label properties1Label=new Label("Properties #1");
        properties1Label.getStyleClass().add("titled-address-label");

        CheckBox unknown1=new CheckBox("Unknown1");
        CheckBox HP_DEF=new CheckBox("HP/DEF");
        CheckBox SPD=new CheckBox("SPD");
        CheckBox target= new CheckBox("Target");

        unknown1.setSelected((subEntry.alimentType & 1) != 0);
        HP_DEF.setSelected((subEntry.alimentType & 2) != 0);
        SPD.setSelected((subEntry.alimentType & 4) != 0);
        target.setSelected((subEntry.alimentType & 8) != 0);

        unknown1.selectedProperty().addListener((obs,oldValue,newValue)->{
            if(newValue){
                subEntry.alimentType |= 1;
            }
            else{
                subEntry.alimentType &= ~1;
            }
        });
        HP_DEF.selectedProperty().addListener((obs,oldValue,newValue)->{
            if(newValue){
                subEntry.alimentType |= 2;
            }
            else{
                subEntry.alimentType &=~ 2;
            }
        });
        SPD.selectedProperty().addListener((obs,oldValue,newValue)->{
            if(newValue){
                subEntry.alimentType |= 4;
            }
            else{
                subEntry.alimentType &=~ 4;
            }
        });
        target.selectedProperty().addListener((obs,oldValue,newValue)->{
            if(newValue){
                subEntry.alimentType |= 8;
            }
            else{
                subEntry.alimentType &=~ 8;
            }
        });
        VBox properties1Box = new VBox(2,unknown1,HP_DEF,SPD,target);

        VBox borderContainerProperties1=new VBox(properties1Box);
        borderContainerProperties1.getStyleClass().add("titled-address-box");
        borderContainerProperties1.setPadding(new Insets(12,0,0,0));

        StackPane properties1BoxStackPane = new StackPane();
        properties1BoxStackPane.getChildren().addAll(borderContainerProperties1,properties1Label);

        StackPane.setAlignment(properties1Label, Pos.TOP_LEFT);
        properties1Label.setTranslateY(-8); 
        properties1Label.setTranslateX(10);
        //properties 1

        //properties 2
        Label properties2Label=new Label("Properties #2");
        properties2Label.getStyleClass().add("titled-address-label");

        CheckBox sealAwokenSkill=new CheckBox("Seal Awoken Skill");
        CheckBox unknown6=new CheckBox("Unknown 6");
        CheckBox unknown7=new CheckBox("Unknown 7");
        CheckBox unknown8= new CheckBox("Unknown 8");

        sealAwokenSkill.setSelected((subEntry.alimentType & 16) != 0);
        unknown6.setSelected((subEntry.alimentType & 32) != 0);
        unknown7.setSelected((subEntry.alimentType & 64) != 0);
        unknown8.setSelected((subEntry.alimentType & 128) != 0);

        sealAwokenSkill.selectedProperty().addListener((obs,oldValue,newValue)->{
            if(newValue){
                subEntry.alimentType |= 16;
            }
            else{
                subEntry.alimentType &=~ 16;
            }
        });
        unknown6.selectedProperty().addListener((obs,oldValue,newValue)->{
            if(newValue){
                subEntry.alimentType |= 32;
            }
            else{
                subEntry.alimentType &=~ 32;
            }
        });
        unknown7.selectedProperty().addListener((obs,oldValue,newValue)->{
            if(newValue){
                subEntry.alimentType |= 64;
            }
            else{
                subEntry.alimentType &=~ 64;
            }
        });
        unknown8.selectedProperty().addListener((obs,oldValue,newValue)->{
            if(newValue){
                subEntry.alimentType |= 128;
            }
            else{
                subEntry.alimentType &=~ 128;
            }
        });
        VBox properties2Box = new VBox(2,sealAwokenSkill,unknown6,unknown7,unknown8);

        VBox borderContainerProperties2=new VBox(properties2Box);
        borderContainerProperties2.getStyleClass().add("titled-address-box");
        borderContainerProperties2.setPadding(new Insets(12,0,0,0));

        StackPane properties2BoxStackPane = new StackPane();
        properties2BoxStackPane.getChildren().addAll(borderContainerProperties2,properties2Label);

        StackPane.setAlignment(properties2Label, Pos.TOP_LEFT);
        properties2Label.setTranslateY(-8); 
        properties2Label.setTranslateX(10);
        //properties 2
        alimentTypeHBox.getChildren().addAll(alimentTypeLabel,properties1BoxStackPane,properties2BoxStackPane);
        alimentTypeHBox.setAlignment(Pos.CENTER_LEFT);
        //aliment type

        //stumble type
        HBox stumbleTypeHBox=new HBox(2);
        stumbleTypeHBox.setPadding(new Insets(20,0,0,8));

        Label stumbleTypeLabel=new Label("Stumble Type");
        stumbleTypeLabel.setPrefWidth(190);

        //stumble group 1
        Label stumbleGroup1Label=new Label("Stumble Group 1");
        stumbleGroup1Label.getStyleClass().add("titled-address-label");

        CheckBox stumbleSet1=new CheckBox("Stumble Set 1");
        CheckBox stumbleSet2=new CheckBox("Stumble Set 2");
        CheckBox stumbleSet3=new CheckBox("Stumble Set 3");
        CheckBox stumbleSet4= new CheckBox("Stumble Set 4");

        stumbleSet1.setSelected((subEntry.stumbleType & 1) != 0);
        stumbleSet2.setSelected((subEntry.stumbleType & 2) != 0);
        stumbleSet3.setSelected((subEntry.stumbleType & 4) != 0);
        stumbleSet4.setSelected((subEntry.stumbleType & 8) != 0);

        stumbleSet1.selectedProperty().addListener((obs,oldValue,newValue)->{
            if(newValue){
                subEntry.stumbleType |= 1;
            }
            else{
                subEntry.stumbleType &=~ 1;
            }
        });
        stumbleSet2.selectedProperty().addListener((obs,oldValue,newValue)->{
            if(newValue){
                subEntry.stumbleType |= 2;
            }
            else{
                subEntry.stumbleType &=~ 2;
            }
        });
        stumbleSet3.selectedProperty().addListener((obs,oldValue,newValue)->{
            if(newValue){
                subEntry.stumbleType |= 4;
            }
            else{
                subEntry.stumbleType &=~ 4;
            }
        });
        stumbleSet4.selectedProperty().addListener((obs,oldValue,newValue)->{
            if(newValue){
                subEntry.stumbleType |= 8;
            }
            else{
                subEntry.stumbleType &=~ 8;
            }
        });
        VBox stumbleGroup1Box = new VBox(2,stumbleSet1,stumbleSet2,stumbleSet3,stumbleSet4);

        VBox borderContainerStumbleGroup1=new VBox(stumbleGroup1Box);
        borderContainerStumbleGroup1.getStyleClass().add("titled-address-box");
        borderContainerStumbleGroup1.setPadding(new Insets(12,0,0,0));

        StackPane stumbleGroup1BoxStackPane = new StackPane();
        stumbleGroup1BoxStackPane.getChildren().addAll(borderContainerStumbleGroup1,stumbleGroup1Label);

        StackPane.setAlignment(stumbleGroup1Label, Pos.TOP_LEFT);
        stumbleGroup1Label.setTranslateY(-8); 
        stumbleGroup1Label.setTranslateX(10);
        //stumble group 1

        //stumble group 2
        Label stumbleGroup2Label=new Label("Stumble Group 2");
        stumbleGroup2Label.getStyleClass().add("titled-address-label");

        CheckBox stumbleSet5=new CheckBox("Stumble Set 5");
        CheckBox stumbleSet6=new CheckBox("Stumble Set 6");
        CheckBox allStumbleSets=new CheckBox("All Stumble Sets");
        CheckBox unknown8StumbleSet= new CheckBox("Unknown 8");

        stumbleSet5.setSelected((subEntry.stumbleType & 16) != 0);
        stumbleSet6.setSelected((subEntry.stumbleType & 32) != 0);
        allStumbleSets.setSelected((subEntry.stumbleType & 64) != 0);
        unknown8StumbleSet.setSelected((subEntry.stumbleType & 128) != 0);

        stumbleSet5.selectedProperty().addListener((obs,oldValue,newValue)->{
            if(newValue){
                subEntry.stumbleType |= 1;
            }
            else{
                subEntry.stumbleType &=~ 1;
            }
        });
        stumbleSet6.selectedProperty().addListener((obs,oldValue,newValue)->{
            if(newValue){
                subEntry.stumbleType |= 2;
            }
            else{
                subEntry.stumbleType &=~ 2;
            }
        });
        allStumbleSets.selectedProperty().addListener((obs,oldValue,newValue)->{
            if(newValue){
                subEntry.stumbleType |= 4;
            }
            else{
                subEntry.stumbleType &=~ 4;
            }
        });
        unknown8StumbleSet.selectedProperty().addListener((obs,oldValue,newValue)->{
            if(newValue){
                subEntry.stumbleType |= 8;
            }
            else{
                subEntry.stumbleType &=~ 8;
            }
        });
        VBox stumbleGroup2Box = new VBox(2,stumbleSet5,stumbleSet6,allStumbleSets,unknown8StumbleSet);

        VBox borderContainerStumbleGroup2=new VBox(stumbleGroup2Box);
        borderContainerStumbleGroup2.getStyleClass().add("titled-address-box");
        borderContainerStumbleGroup2.setPadding(new Insets(12,0,0,0));

        StackPane stumbleGroup2BoxStackPane = new StackPane();
        stumbleGroup2BoxStackPane.getChildren().addAll(borderContainerStumbleGroup2,stumbleGroup2Label);

        StackPane.setAlignment(stumbleGroup2Label, Pos.TOP_LEFT);
        stumbleGroup2Label.setTranslateY(-8); 
        stumbleGroup2Label.setTranslateX(10);
        //stumble group 2

        //stumble group 3
        Label stumbleGroup3Label=new Label("Stumble Group 3");
        stumbleGroup3Label.getStyleClass().add("titled-address-label");

        CheckBox unknown9StumbleSets=new CheckBox("Unknown 9");
        CheckBox unknown10StumbleSets=new CheckBox("Unknown 10");
        CheckBox unknown11StumbleSets=new CheckBox("Unknown 11");
        CheckBox unknown12StumbleSet= new CheckBox("Unknown 12");

        unknown9StumbleSets.setSelected((subEntry.stumbleType & 16) != 0);
        unknown10StumbleSets.setSelected((subEntry.stumbleType & 32) != 0);
        unknown11StumbleSets.setSelected((subEntry.stumbleType & 64) != 0);
        unknown12StumbleSet.setSelected((subEntry.stumbleType & 128) != 0);

        unknown9StumbleSets.selectedProperty().addListener((obs,oldValue,newValue)->{
            if(newValue){
                subEntry.stumbleType |= 16;
            }
            else{
                subEntry.stumbleType &=~ 16;
            }
        });
        unknown10StumbleSets.selectedProperty().addListener((obs,oldValue,newValue)->{
            if(newValue){
                subEntry.stumbleType |= 32;
            }
            else{
                subEntry.stumbleType &=~ 32;
            }
        });
        unknown11StumbleSets.selectedProperty().addListener((obs,oldValue,newValue)->{
            if(newValue){
                subEntry.stumbleType |= 64;
            }
            else{
                subEntry.stumbleType &=~ 64;
            }
        });
        unknown12StumbleSet.selectedProperty().addListener((obs,oldValue,newValue)->{
            if(newValue){
                subEntry.stumbleType |= 128;
            }
            else{
                subEntry.stumbleType &=~ 128;
            }
        });
        VBox stumbleGroup3Box = new VBox(2,unknown9StumbleSets,unknown10StumbleSets,unknown11StumbleSets,unknown12StumbleSet);

        VBox borderContainerStumbleGroup3=new VBox(stumbleGroup3Box);
        borderContainerStumbleGroup3.getStyleClass().add("titled-address-box");
        borderContainerStumbleGroup3.setPadding(new Insets(12,0,0,0));

        StackPane stumbleGroup3BoxStackPane = new StackPane();
        stumbleGroup3BoxStackPane.getChildren().addAll(borderContainerStumbleGroup3,stumbleGroup3Label);

        StackPane.setAlignment(stumbleGroup3Label, Pos.TOP_LEFT);
        stumbleGroup3Label.setTranslateY(-8); 
        stumbleGroup3Label.setTranslateX(10);
        //stumble group 3

       //stumble group 4
        Label stumbleGroup4Label=new Label("Stumble Group 4");
        stumbleGroup4Label.getStyleClass().add("titled-address-label");

        CheckBox unknown13StumbleSets=new CheckBox("Unknown 13");
        CheckBox unknown14StumbleSets=new CheckBox("Unknown 14");
        CheckBox unknown15StumbleSets=new CheckBox("Unknown 15");
        CheckBox unknown16StumbleSet= new CheckBox("Unknown 16");

        unknown13StumbleSets.setSelected((subEntry.stumbleType & 256) != 0);
        unknown14StumbleSets.setSelected((subEntry.stumbleType & 512) != 0);
        unknown15StumbleSets.setSelected((subEntry.stumbleType & 1024) != 0);
        unknown16StumbleSet.setSelected((subEntry.stumbleType & 2048) != 0);

        unknown13StumbleSets.selectedProperty().addListener((obs,oldValue,newValue)->{
            if(newValue){
                subEntry.stumbleType |= 256;
            }
            else{
                subEntry.stumbleType &=~ 256;
            }
        });
        unknown14StumbleSets.selectedProperty().addListener((obs,oldValue,newValue)->{
            if(newValue){
                subEntry.stumbleType |= 512;
            }
            else{
                subEntry.stumbleType &=~ 512;
            }
        });
        unknown15StumbleSets.selectedProperty().addListener((obs,oldValue,newValue)->{
            if(newValue){
                subEntry.stumbleType |= 1024;
            }
            else{
                subEntry.stumbleType &=~ 1024;
            }
        });
        unknown16StumbleSet.selectedProperty().addListener((obs,oldValue,newValue)->{
            if(newValue){
                subEntry.stumbleType |= 2048;
            }
            else{
                subEntry.stumbleType &=~ 2048;
            }
        });
        VBox stumbleGroup4Box = new VBox(2,unknown13StumbleSets,unknown14StumbleSets,unknown15StumbleSets,unknown16StumbleSet);

        VBox borderContainerStumbleGroup4=new VBox(stumbleGroup4Box);
        borderContainerStumbleGroup4.getStyleClass().add("titled-address-box");
        borderContainerStumbleGroup4.setPadding(new Insets(12,0,0,0));

        StackPane stumbleGroup4BoxStackPane = new StackPane();
        stumbleGroup4BoxStackPane.getChildren().addAll(borderContainerStumbleGroup4,stumbleGroup4Label);

        StackPane.setAlignment(stumbleGroup4Label, Pos.TOP_LEFT);
        stumbleGroup4Label.setTranslateY(-8); 
        stumbleGroup4Label.setTranslateX(10);
        //stumble group 4

        stumbleTypeHBox.getChildren().addAll(stumbleTypeLabel,stumbleGroup1BoxStackPane,stumbleGroup2BoxStackPane,stumbleGroup3BoxStackPane,stumbleGroup4BoxStackPane);
        stumbleTypeHBox.setAlignment(Pos.CENTER_LEFT);
        //stumble type
        
        //stamina broken override bdm id
        HBox staminaBrokenOverrideBdmIdHBox=new HBox(12);
        staminaBrokenOverrideBdmIdHBox.setPadding(new Insets(20,0,0,8));
        Label staminaBrokenOverrideBdmIdLabel=new Label("Stamina Broken Override Bdm Id");
        staminaBrokenOverrideBdmIdLabel.setPrefWidth(180);

        Spinner<Integer> staminaBrokenOverrideBdmIdSpinner=new Spinner<>(0,65535,subEntry.staminaBrokenOverrideBdmId);
        staminaBrokenOverrideBdmIdSpinner.setEditable(true);
        staminaBrokenOverrideBdmIdSpinner.valueProperty().addListener((obs,oldValue,newValue)->{
            if(newValue!=null){
                subEntry.staminaBrokenOverrideBdmId = newValue.shortValue();
            }

        });

        staminaBrokenOverrideBdmIdHBox.getChildren().addAll(staminaBrokenOverrideBdmIdLabel,staminaBrokenOverrideBdmIdSpinner);
        staminaBrokenOverrideBdmIdHBox.setAlignment(Pos.CENTER_LEFT);
        //stamina broken override bdm id

        //z vanish enable time
        HBox zVanishEnableTimeHBox=new HBox(12);
        zVanishEnableTimeHBox.setPadding(new Insets(20,0,0,8));
        Label szVanishEnableTimeLabel=new Label("Z Vanish Enable Time");
        szVanishEnableTimeLabel.setPrefWidth(180);

        Spinner<Integer> zVanishEnableTimeSpinner=new Spinner<>(0,65535,subEntry.zVanishEnableTime);
        zVanishEnableTimeSpinner.setEditable(true);
        zVanishEnableTimeSpinner.valueProperty().addListener((obs,oldValue,newValue)->{
            if(newValue!=null){
                subEntry.zVanishEnableTime = newValue.shortValue();
            }

        });

        zVanishEnableTimeHBox.getChildren().addAll(szVanishEnableTimeLabel,zVanishEnableTimeSpinner);
        zVanishEnableTimeHBox.setAlignment(Pos.CENTER_LEFT);
        //z vanish enable time

        miscVBox.getChildren().addAll(transformationTypeHBox,alimentTypeHBox,stumbleTypeHBox,staminaBrokenOverrideBdmIdHBox,zVanishEnableTimeHBox);

        return miscVBox;
    }

    private VBox createUnknownVBox (BdmSubEntry subEntry){
        VBox unknownVBox=new VBox(15);
        unknownVBox.setPadding(new Insets(20,0,0,8));

        //I_02
        HBox unknown02HBox=new HBox(2);
        Label lblI02=new Label("I_02: ");
        lblI02.setPrefWidth(100);
        TextField txtI02=new TextField(String.valueOf(subEntry.i02));
        txtI02.textProperty().addListener((obs, oldText, newText) -> {
            if (txtI02.getText().contains("-")) {
                return;
            }
            try {
                subEntry.i02 = Integer.parseInt(newText);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });
        unknown02HBox.getChildren().addAll(lblI02,txtI02);
        unknown02HBox.setAlignment(Pos.CENTER_LEFT);
        
        //I_02

        //I_06
        HBox unknown06HBox=new HBox(2);
        Label lblI06=new Label("I_06: ");
        lblI06.setPrefWidth(100);
        TextField txtI06=new TextField(String.valueOf(subEntry.i06));
        txtI06.textProperty().addListener((obs, oldText, newText) -> {
            if (txtI06.getText().contains("-")) {
                return;
            }
            try {
                subEntry.i06 = Integer.parseInt(newText);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });
        unknown06HBox.getChildren().addAll(lblI06,txtI06);
        unknown06HBox.setAlignment(Pos.CENTER_LEFT);
        //I_06

        //F_08
        HBox unknown08HBox=new HBox(2);
        Label lblF08=new Label("F_08: ");
        lblF08.setPrefWidth(100);
        TextField txtF08=new TextField(String.valueOf(subEntry.f08));
        txtF08.textProperty().addListener((obs, oldText, newText) -> {
            if (txtF08.getText().contains("-")) {
                return;
            }
            try {
                subEntry.f08=Integer.parseInt(newText);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });
        unknown08HBox.getChildren().addAll(lblF08,txtF08);
        unknown08HBox.setAlignment(Pos.CENTER_LEFT);
        //F_08

        //I_22
        HBox unknown22HBox=new HBox(2);
        Label lblI22=new Label("I_22: ");
        lblI22.setPrefWidth(100);
        TextField txtI22=new TextField(String.valueOf(subEntry.i22));
        txtI22.textProperty().addListener((obs, oldText, newText) -> {
            if (txtI22.getText().contains("-")) {
                return;
            }
            try {
                subEntry.i22 = Integer.parseInt(newText);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });
        unknown22HBox.getChildren().addAll(lblI22,txtI22);
        unknown22HBox.setAlignment(Pos.CENTER_LEFT);
        //I_22

        //I_30
        HBox unknown30HBox=new HBox(2);
        Label lblI30=new Label("I_30: ");
        lblI30.setPrefWidth(100);
        TextField txtI30=new TextField(String.valueOf(subEntry.i30));
        txtI30.textProperty().addListener((obs, oldText, newText) -> {
            if (txtI30.getText().contains("-")) {
                return;
            }
            try {
                subEntry.i30 = Integer.parseInt(newText);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });
        unknown30HBox.getChildren().addAll(lblI30,txtI30);
        unknown30HBox.setAlignment(Pos.CENTER_LEFT);
        //I_30

        //I_38
        HBox unknown38HBox=new HBox(2);
        Label lblI38=new Label("I_38: ");
        lblI38.setPrefWidth(100);
        TextField txtI38=new TextField(String.valueOf(subEntry.i38));
        txtI38.textProperty().addListener((obs, oldText, newText) -> {
            if (txtI38.getText().contains("-")) {
                return;
            }
            try {
                subEntry.i38 = Integer.parseInt(newText);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });
        unknown38HBox.getChildren().addAll(lblI38,txtI38);
        unknown38HBox.setAlignment(Pos.CENTER_LEFT);
        //I_38

        //I_58
        HBox unknown58HBox=new HBox(2);
        Label lblI58=new Label("I_58: ");
        lblI58.setPrefWidth(100);
        TextField txtI58=new TextField(String.valueOf(subEntry.i58));
        txtI58.textProperty().addListener((obs, oldText, newText) -> {
            if (txtI58.getText().contains("-")) {
                return;
            }
            try {
                subEntry.i58 = Integer.parseInt(newText);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });
        unknown58HBox.getChildren().addAll(lblI58,txtI58);
        unknown58HBox.setAlignment(Pos.CENTER_LEFT);
        //I_58

        //I_76
        HBox unknown76HBox=new HBox(2);
        Label lblI76=new Label("I_76: ");
        lblI76.setPrefWidth(100);
        TextField txtI76=new TextField(String.valueOf(subEntry.i76));
        txtI76.textProperty().addListener((obs, oldText, newText) -> {
            if (txtI76.getText().contains("-")) {
                return;
            }
            try {
                subEntry.i76 = Integer.parseInt(newText);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });
        unknown76HBox.getChildren().addAll(lblI76,txtI76);
        unknown76HBox.setAlignment(Pos.CENTER_LEFT);
        //I_76

        //I_82
        HBox unknown82HBox=new HBox(2);
        Label lblI82=new Label("I_82: ");
        lblI82.setPrefWidth(100);
        TextField txtI82=new TextField(String.valueOf(subEntry.i82));
        txtI82.textProperty().addListener((obs, oldText, newText) -> {
            if (txtI82.getText().contains("-")) {
                return;
            }
            try {
                subEntry.i82 = Integer.parseInt(newText);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });
        unknown82HBox.getChildren().addAll(lblI82,txtI82);
        unknown82HBox.setAlignment(Pos.CENTER_LEFT);
        //I_82

        //I_88
        HBox unknown88HBox=new HBox(2);
        Label lblI88=new Label("I_88: ");
        lblI88.setPrefWidth(100);
        TextField txtI88=new TextField(String.valueOf(subEntry.i88));
        txtI88.textProperty().addListener((obs, oldText, newText) -> {
            if (txtI88.getText().contains("-")) {
                return;
            }
            try {
                subEntry.i88 = Integer.parseInt(newText);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });
        unknown88HBox.getChildren().addAll(lblI88,txtI88);
        unknown88HBox.setAlignment(Pos.CENTER_LEFT);
        //I_88

        //I_90
        HBox unknown90HBox=new HBox(2);
        Label lblI90=new Label("I_90: ");
        lblI90.setPrefWidth(100);
        TextField txtI90=new TextField(String.valueOf(subEntry.i90));
        txtI90.textProperty().addListener((obs, oldText, newText) -> {
            if (txtI90.getText().contains("-")) {
                return;
            }
            try {
                subEntry.i90 = Integer.parseInt(newText);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });
        unknown90HBox.getChildren().addAll(lblI90,txtI90);
        unknown90HBox.setAlignment(Pos.CENTER_LEFT);
        //I_90

        //I_92
        HBox unknown92HBox=new HBox(2);
        Label lblI92=new Label("I_92: ");
        lblI92.setPrefWidth(100);
        TextField txtI92=new TextField(String.valueOf(subEntry.i92));
        txtI92.textProperty().addListener((obs, oldText, newText) -> {
            if (txtI92.getText().contains("-")) {
                return;
            }
            try {
                subEntry.i92 = Integer.parseInt(newText);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });
        unknown92HBox.getChildren().addAll(lblI92,txtI92);
        unknown92HBox.setAlignment(Pos.CENTER_LEFT);
        //I_92

        unknownVBox.getChildren().addAll(unknown02HBox,unknown06HBox,unknown08HBox,unknown22HBox,unknown30HBox,unknown38HBox,unknown58HBox,unknown76HBox,unknown82HBox,unknown88HBox,unknown90HBox,unknown92HBox);
        return unknownVBox;
    }

    public void entriesActionListener(){
        paste.setDisable(true);
        contextMenu.getItems().addAll(copy,paste,delete,append,insert);
        listView.setContextMenu(contextMenu);
        listView.getSelectionModel().selectedItemProperty().addListener((obs,oldValue,newValue)->{
            if(newValue==null){
                return;
            }
            if (listView.getSelectionModel().getSelectedIndex() < 0 || mainTabPane.getSelectionModel().getSelectedIndex() < 0) {
                return; 
            }
          
            ((TabPane) mainTabPane.getTabs().get(mainTabPane.getSelectionModel().getSelectedIndex()).getContent()).getTabs().get(0).setContent(createMainVBox(bdmEntries.get(listView.getSelectionModel().getSelectedIndex()).subEntries[mainTabPane.getSelectionModel().getSelectedIndex()]));
            ((TabPane) mainTabPane.getTabs().get(mainTabPane.getSelectionModel().getSelectedIndex()).getContent()).getTabs().get(1).setContent(createAnimationVBox(bdmEntries.get(listView.getSelectionModel().getSelectedIndex()).subEntries[mainTabPane.getSelectionModel().getSelectedIndex()]));
            ((TabPane) mainTabPane.getTabs().get(mainTabPane.getSelectionModel().getSelectedIndex()).getContent()).getTabs().get(2).setContent(createSoundVBox(bdmEntries.get(listView.getSelectionModel().getSelectedIndex()).subEntries[mainTabPane.getSelectionModel().getSelectedIndex()]));
            ((TabPane) mainTabPane.getTabs().get(mainTabPane.getSelectionModel().getSelectedIndex()).getContent()).getTabs().get(3).setContent(createEffectsScrollPane(bdmEntries.get(listView.getSelectionModel().getSelectedIndex()).subEntries[mainTabPane.getSelectionModel().getSelectedIndex()]));
            ((TabPane) mainTabPane.getTabs().get(mainTabPane.getSelectionModel().getSelectedIndex()).getContent()).getTabs().get(4).setContent(createPushbackVBox(bdmEntries.get(listView.getSelectionModel().getSelectedIndex()).subEntries[mainTabPane.getSelectionModel().getSelectedIndex()]));
            ((TabPane) mainTabPane.getTabs().get(mainTabPane.getSelectionModel().getSelectedIndex()).getContent()).getTabs().get(5).setContent(createCameraVBox(bdmEntries.get(listView.getSelectionModel().getSelectedIndex()).subEntries[mainTabPane.getSelectionModel().getSelectedIndex()]));
            ((TabPane) mainTabPane.getTabs().get(mainTabPane.getSelectionModel().getSelectedIndex()).getContent()).getTabs().get(6).setContent(createMiscVBox(bdmEntries.get(listView.getSelectionModel().getSelectedIndex()).subEntries[mainTabPane.getSelectionModel().getSelectedIndex()]));
            ((TabPane) mainTabPane.getTabs().get(mainTabPane.getSelectionModel().getSelectedIndex()).getContent()).getTabs().get(7).setContent(createUnknownVBox(bdmEntries.get(listView.getSelectionModel().getSelectedIndex()).subEntries[mainTabPane.getSelectionModel().getSelectedIndex()]));
        });
        listView.setOnMouseClicked(e->{
            if(e.getButton()==MouseButton.SECONDARY){
                contextMenu.setOnAction(event->{
                    if(event.getTarget()==copy){
                        Copy();
                        paste.setDisable(false);
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
                });
            }
        });
    }

    public void tabActionListener(){
        mainTabPane.getSelectionModel().selectedItemProperty().addListener((obsevable,oldTab,newTab)->{
            if(newTab==null){
                return;
            }
            if (listView.getSelectionModel().getSelectedIndex() < 0 || mainTabPane.getSelectionModel().getSelectedIndex() < 0) {
                return; 
            }

            ((TabPane) mainTabPane.getTabs().get(mainTabPane.getTabs().indexOf(newTab)).getContent()).getTabs().get(0).setContent(createMainVBox(bdmEntries.get(listView.getSelectionModel().getSelectedIndex()).subEntries[mainTabPane.getTabs().indexOf(newTab)]));
            ((TabPane) mainTabPane.getTabs().get(mainTabPane.getTabs().indexOf(newTab)).getContent()).getTabs().get(1).setContent(createAnimationVBox(bdmEntries.get(listView.getSelectionModel().getSelectedIndex()).subEntries[mainTabPane.getTabs().indexOf(newTab)]));
            ((TabPane) mainTabPane.getTabs().get(mainTabPane.getTabs().indexOf(newTab)).getContent()).getTabs().get(2).setContent(createSoundVBox(bdmEntries.get(listView.getSelectionModel().getSelectedIndex()).subEntries[mainTabPane.getTabs().indexOf(newTab)]));
            ((TabPane) mainTabPane.getTabs().get(mainTabPane.getTabs().indexOf(newTab)).getContent()).getTabs().get(3).setContent(createEffectsScrollPane(bdmEntries.get(listView.getSelectionModel().getSelectedIndex()).subEntries[mainTabPane.getTabs().indexOf(newTab)]));
            ((TabPane) mainTabPane.getTabs().get(mainTabPane.getTabs().indexOf(newTab)).getContent()).getTabs().get(4).setContent(createPushbackVBox(bdmEntries.get(listView.getSelectionModel().getSelectedIndex()).subEntries[mainTabPane.getTabs().indexOf(newTab)]));
            ((TabPane) mainTabPane.getTabs().get(mainTabPane.getTabs().indexOf(newTab)).getContent()).getTabs().get(5).setContent(createCameraVBox(bdmEntries.get(listView.getSelectionModel().getSelectedIndex()).subEntries[mainTabPane.getTabs().indexOf(newTab)]));
            ((TabPane) mainTabPane.getTabs().get(mainTabPane.getTabs().indexOf(newTab)).getContent()).getTabs().get(6).setContent(createMiscVBox(bdmEntries.get(listView.getSelectionModel().getSelectedIndex()).subEntries[mainTabPane.getTabs().indexOf(newTab)]));
            ((TabPane) mainTabPane.getTabs().get(mainTabPane.getTabs().indexOf(newTab)).getContent()).getTabs().get(7).setContent(createUnknownVBox(bdmEntries.get(listView.getSelectionModel().getSelectedIndex()).subEntries[mainTabPane.getTabs().indexOf(newTab)]));
            ((TabPane) newTab.getContent()).getSelectionModel().select(((TabPane) oldTab.getContent()).getSelectionModel().getSelectedIndex());
        });
    }

    private void entriesKeysListener(){
        listView.setOnKeyPressed(e->{
            if(e.isControlDown()&&e.getCode()==KeyCode.C){
                Copy();
                paste.setDisable(false);
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
        });
    }

    private void Copy() {
        if (listView.getSelectionModel().getSelectedIndex() < 0) return; 
        copyContainer.clear(); 
        copyContainer.add(new BdmEntry(bdmEntries.get(listView.getSelectionModel().getSelectedIndex()))); 
    }

    private void Paste() {
        if(copyContainer.isEmpty()) return;
        bdmEntries.set(listView.getSelectionModel().getSelectedIndex(),copyContainer.get(0));
        ((TabPane) mainTabPane.getTabs().get(mainTabPane.getSelectionModel().getSelectedIndex()).getContent()).getTabs().get(0).setContent(createMainVBox(bdmEntries.get(listView.getSelectionModel().getSelectedIndex()).subEntries[mainTabPane.getSelectionModel().getSelectedIndex()]));
        ((TabPane) mainTabPane.getTabs().get(mainTabPane.getSelectionModel().getSelectedIndex()).getContent()).getTabs().get(1).setContent(createAnimationVBox(bdmEntries.get(listView.getSelectionModel().getSelectedIndex()).subEntries[mainTabPane.getSelectionModel().getSelectedIndex()]));
        ((TabPane) mainTabPane.getTabs().get(mainTabPane.getSelectionModel().getSelectedIndex()).getContent()).getTabs().get(2).setContent(createSoundVBox(bdmEntries.get(listView.getSelectionModel().getSelectedIndex()).subEntries[mainTabPane.getSelectionModel().getSelectedIndex()]));
        ((TabPane) mainTabPane.getTabs().get(mainTabPane.getSelectionModel().getSelectedIndex()).getContent()).getTabs().get(3).setContent(createEffectsScrollPane(bdmEntries.get(listView.getSelectionModel().getSelectedIndex()).subEntries[mainTabPane.getSelectionModel().getSelectedIndex()]));
        ((TabPane) mainTabPane.getTabs().get(mainTabPane.getSelectionModel().getSelectedIndex()).getContent()).getTabs().get(4).setContent(createPushbackVBox(bdmEntries.get(listView.getSelectionModel().getSelectedIndex()).subEntries[mainTabPane.getSelectionModel().getSelectedIndex()]));
        ((TabPane) mainTabPane.getTabs().get(mainTabPane.getSelectionModel().getSelectedIndex()).getContent()).getTabs().get(5).setContent(createCameraVBox(bdmEntries.get(listView.getSelectionModel().getSelectedIndex()).subEntries[mainTabPane.getSelectionModel().getSelectedIndex()]));
        ((TabPane) mainTabPane.getTabs().get(mainTabPane.getSelectionModel().getSelectedIndex()).getContent()).getTabs().get(6).setContent(createMiscVBox(bdmEntries.get(listView.getSelectionModel().getSelectedIndex()).subEntries[mainTabPane.getSelectionModel().getSelectedIndex()]));
        ((TabPane) mainTabPane.getTabs().get(mainTabPane.getSelectionModel().getSelectedIndex()).getContent()).getTabs().get(7).setContent(createUnknownVBox(bdmEntries.get(listView.getSelectionModel().getSelectedIndex()).subEntries[mainTabPane.getSelectionModel().getSelectedIndex()]));
        
    }

    private void Delete() {
        if (listView.getSelectionModel().getSelectedIndex() < 0) return;
        bdmEntries.remove(listView.getSelectionModel().getSelectedIndex());
        allEntries.remove(listView.getSelectionModel().getSelectedIndex());
        listView.getItems().remove(listView.getSelectionModel().getSelectedIndex());
        for(int i=0;i<listView.getItems().size();i++){
            allEntries.set(i,new String("Entry: "+i));
            listView.getItems().set(i,allEntries.get(i));
        }
    }

   private void Append() {
        if (listView.getSelectionModel().getSelectedIndex() < 0) return;
        bdmEntries.add(listView.getSelectionModel().getSelectedIndex()+1,new BdmEntry());
        allEntries.add(new String("Entry: "+listView.getItems().size()));
        listView.getItems().add("Entry: " + listView.getItems().size());
    }

    private void Insert() {
        if (listView.getSelectionModel().getSelectedIndex() > 0) {
            bdmEntries.add(listView.getSelectionModel().getSelectedIndex()-1,new BdmEntry());
            allEntries.add(new String("Entry: "+listView.getItems().size()));
            listView.getItems().add("Entry: " + listView.getItems().size());
        } 
        else if (listView.getSelectionModel().getSelectedIndex() == 0) {
            bdmEntries.add(listView.getSelectionModel().getSelectedIndex(),new BdmEntry());
            allEntries.add(new String("Entry: "+listView.getItems().size()));
            listView.getItems().add("Entry " + listView.getItems().size());
        }
    }

    public void bdmReader(Path path){
        try(FileChannel channel=FileChannel.open(path, StandardOpenOption.READ)) {
            ByteBuffer intBuffer=ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
            ByteBuffer shortBuffer=ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN);
            int bdmEntriesCount;
            int entryOffset=16;
            
            channel.position(8);
            intBuffer.clear();
            channel.read(intBuffer);
            intBuffer.flip();
            bdmEntriesCount=intBuffer.getInt();

            switch ((int)((path.toFile().length()-16)/bdmEntriesCount)) {
                case 1284:
                    allEntries=new ArrayList<>(bdmEntriesCount);
                    for(int i=0;i<bdmEntriesCount;i++){
                        BdmEntry bdmEntry = new BdmEntry();
                        allEntries.add(new String("Entry "+i));
                        listView.getItems().add(allEntries.get(i));
                        for(int j=0;j<10;j++){
                            BdmSubEntry subEntry = bdmEntry.subEntries[j];
                            channel.position(entryOffset+4+128*j+1284*i);
                            shortBuffer.clear();
                            channel.read(shortBuffer);
                            shortBuffer.flip();
                            subEntry.damageType = toUShort(shortBuffer.getShort());

                            channel.position(entryOffset+6+128*j+1284*i);
                            shortBuffer.clear();
                            channel.read(shortBuffer);
                            shortBuffer.flip();
                            subEntry.i02 = toUShort(shortBuffer.getShort());

                            channel.position(entryOffset+8+128*j+1284*i);
                            shortBuffer.clear();
                            channel.read(shortBuffer);
                            shortBuffer.flip();
                            subEntry.damageAmount = toUShort(shortBuffer.getShort());

                            channel.position(entryOffset+10+128*j+1284*i);
                            shortBuffer.clear();
                            channel.read(shortBuffer);
                            shortBuffer.flip();
                            subEntry.i06 = toUShort(shortBuffer.getShort());

                            channel.position(entryOffset+12+128*j+1284*i);
                            intBuffer.clear();
                            channel.read(intBuffer);
                            intBuffer.flip();
                            subEntry.f08 = intBuffer.getFloat();

                            channel.position(entryOffset+16+128*j+1284*i);
                            shortBuffer.clear();
                            channel.read(shortBuffer);
                            shortBuffer.flip();
                            subEntry.acbType = toUShort(shortBuffer.getShort());

                            channel.position(entryOffset+18+128*j+1284*i);
                            shortBuffer.clear();
                            channel.read(shortBuffer);
                            shortBuffer.flip();
                            subEntry.cueId = shortBuffer.getShort();

                            channel.position(entryOffset+20+128*j+1284*i);
                            shortBuffer.clear();
                            channel.read(shortBuffer);
                            shortBuffer.flip();
                            subEntry.effect1Id = shortBuffer.getShort();

                            channel.position(entryOffset+22+128*j+1284*i);
                            shortBuffer.clear();
                            channel.read(shortBuffer);
                            shortBuffer.flip();
                            subEntry.effect1SkillId = toUShort(shortBuffer.getShort());

                            channel.position(entryOffset+24+128*j+1284*i);
                            shortBuffer.clear();
                            channel.read(shortBuffer);
                            shortBuffer.flip();
                            subEntry.effect1EepkType = toUShort(shortBuffer.getShort());

                            channel.position(entryOffset+26+128*j+1284*i);
                            shortBuffer.clear();
                            channel.read(shortBuffer);
                            shortBuffer.flip();
                            subEntry.i22 = toUShort(shortBuffer.getShort());

                            channel.position(entryOffset+28+128*j+1284*i);
                            shortBuffer.clear();
                            channel.read(shortBuffer);
                            shortBuffer.flip();
                            subEntry.effect2Id = shortBuffer.getShort();

                            channel.position(entryOffset+30+128*j+1284*i);
                            shortBuffer.clear();
                            channel.read(shortBuffer);
                            shortBuffer.flip();
                            subEntry.effect2SkillId =toUShort(shortBuffer.getShort());

                            channel.position(entryOffset+32+128*j+1284*i);
                            shortBuffer.clear();
                            channel.read(shortBuffer);
                            shortBuffer.flip();
                            subEntry.effect2EepkType = toUShort(shortBuffer.getShort());

                            channel.position(entryOffset+34+128*j+1284*i);
                            shortBuffer.clear();
                            channel.read(shortBuffer);
                            shortBuffer.flip();
                            subEntry.i30 = toUShort(shortBuffer.getShort());

                            channel.position(entryOffset+36+128*j+1284*i);
                            shortBuffer.clear();
                            channel.read(shortBuffer);
                            shortBuffer.flip();
                            subEntry.effect3Id = shortBuffer.getShort();

                            channel.position(entryOffset+38+128*j+1284*i);
                            shortBuffer.clear();
                            channel.read(shortBuffer);
                            shortBuffer.flip();
                            subEntry.effect3SkillId = toUShort(shortBuffer.getShort());

                            channel.position(entryOffset+40+128*j+1284*i);
                            shortBuffer.clear();
                            channel.read(shortBuffer);
                            shortBuffer.flip();
                            subEntry.effect3EepkType = toUShort(shortBuffer.getShort());

                            channel.position(entryOffset+42+128*j+1284*i);
                            shortBuffer.clear();
                            channel.read(shortBuffer);
                            shortBuffer.flip();
                            subEntry.i38 = toUShort(shortBuffer.getShort());

                            channel.position(entryOffset+44+128*j+1284*i);
                            intBuffer.clear();
                            channel.read(intBuffer);
                            intBuffer.flip();
                            subEntry.pushbackStrength = intBuffer.getFloat();

                            channel.position(entryOffset+48+128*j+1284*i);
                            intBuffer.clear();
                            channel.read(intBuffer);
                            intBuffer.flip();
                            subEntry.pushbackAcceleration = intBuffer.getFloat();

                            channel.position(entryOffset+52+128*j+1284*i);
                            shortBuffer.clear();
                            channel.read(shortBuffer);
                            shortBuffer.flip();
                            subEntry.userStunt = toUShort(shortBuffer.getShort());

                            channel.position(entryOffset+54+128*j+1284*i);
                            shortBuffer.clear();
                            channel.read(shortBuffer);
                            shortBuffer.flip();
                            subEntry.victimStunt = toUShort(shortBuffer.getShort());

                            channel.position(entryOffset+56+128*j+1284*i);
                            shortBuffer.clear();
                            channel.read(shortBuffer);
                            shortBuffer.flip();
                            subEntry.knockbackDuration = toUShort(shortBuffer.getShort());

                            channel.position(entryOffset+58+128*j+1284*i);
                            shortBuffer.clear();
                            channel.read(shortBuffer);
                            shortBuffer.flip();
                            subEntry.knockbackRecoveryAfterImpactTime = toUShort(shortBuffer.getShort());

                            channel.position(entryOffset+60+128*j+1284*i);
                            shortBuffer.clear();
                            channel.read(shortBuffer);
                            shortBuffer.flip();
                            subEntry.knockbackGroundImpactTime = toUShort(shortBuffer.getShort());

                            channel.position(entryOffset+62+128*j+1284*i);
                            shortBuffer.clear();
                            channel.read(shortBuffer);
                            shortBuffer.flip();
                            subEntry.i58 = toUShort(shortBuffer.getShort());

                            channel.position(entryOffset+64+128*j+1284*i);
                            intBuffer.clear();
                            channel.read(intBuffer);
                            intBuffer.flip();
                            subEntry.knockbackStrengthX = intBuffer.getFloat();

                            channel.position(entryOffset+68+128*j+1284*i);
                            intBuffer.clear();
                            channel.read(intBuffer);
                            intBuffer.flip();
                            subEntry.knockbackStrengthY = intBuffer.getFloat();

                            channel.position(entryOffset+72+128*j+1284*i);
                            intBuffer.clear();
                            channel.read(intBuffer);
                            intBuffer.flip();
                            subEntry.knockbackStrengthZ = intBuffer.getFloat();

                            channel.position(entryOffset+76+128*j+1284*i);
                            intBuffer.clear();
                            channel.read(intBuffer);
                            intBuffer.flip();
                            subEntry.knockbackDragY = intBuffer.getFloat();

                            channel.position(entryOffset+80+128*j+1284*i);
                            shortBuffer.clear();
                            channel.read(shortBuffer);
                            shortBuffer.flip();
                            subEntry.i76 = toUShort(shortBuffer.getShort());

                            channel.position(entryOffset+82+128*j+1284*i);
                            shortBuffer.clear();
                            channel.read(shortBuffer);
                            shortBuffer.flip();
                            subEntry.knockbackGravityTime = toUShort(shortBuffer.getShort());

                            channel.position(entryOffset+84+128*j+1284*i);
                            shortBuffer.clear();
                            channel.read(shortBuffer);
                            shortBuffer.flip();
                            subEntry.victimInvincibilityTime = shortBuffer.getShort();

                            channel.position(entryOffset+86+128*j+1284*i);
                            shortBuffer.clear();
                            channel.read(shortBuffer);
                            shortBuffer.flip();
                            subEntry.i82 = shortBuffer.getShort();

                            channel.position(entryOffset+88+128*j+1284*i);
                            shortBuffer.clear();
                            channel.read(shortBuffer);
                            shortBuffer.flip();
                            subEntry.transformationType = shortBuffer.getShort();

                            channel.position(entryOffset+90+128*j+1284*i);
                            shortBuffer.clear();
                            channel.read(shortBuffer);
                            shortBuffer.flip();
                            subEntry.alimentType = shortBuffer.getShort();

                            channel.position(entryOffset+92+128*j+1284*i);
                            shortBuffer.clear();
                            channel.read(shortBuffer);
                            shortBuffer.flip();
                            subEntry.i88 = toUShort(shortBuffer.getShort());

                            channel.position(entryOffset+94+128*j+1284*i);
                            shortBuffer.clear();
                            channel.read(shortBuffer);
                            shortBuffer.flip();
                            subEntry.i90 = toUShort(shortBuffer.getShort());

                            channel.position(entryOffset+96+128*j+1284*i);
                            shortBuffer.clear();
                            channel.read(shortBuffer);
                            shortBuffer.flip();
                            subEntry.i92 = toUShort(shortBuffer.getShort());

                            channel.position(entryOffset+98+128*j+1284*i);
                            shortBuffer.clear();
                            channel.read(shortBuffer);
                            shortBuffer.flip();
                            subEntry.damageSpecial = toUShort(shortBuffer.getShort());

                            channel.position(entryOffset+100+128*j+1284*i);
                            shortBuffer.clear();
                            channel.read(shortBuffer);
                            shortBuffer.flip();
                            subEntry.damageSpecial2 = toUShort(shortBuffer.getShort());

                            channel.position(entryOffset+102+128*j+1284*i);
                            shortBuffer.clear();
                            channel.read(shortBuffer);
                            shortBuffer.flip();
                            subEntry.damageSpecial3 = toUShort(shortBuffer.getShort());

                            channel.position(entryOffset+104+128*j+1284*i);
                            shortBuffer.clear();
                            channel.read(shortBuffer);
                            shortBuffer.flip();
                            subEntry.stumbleType = toUShort(shortBuffer.getShort());

                            channel.position(entryOffset+106+128*j+1284*i);
                            shortBuffer.clear();
                            channel.read(shortBuffer);
                            shortBuffer.flip();
                            subEntry.secondaryType = toUShort(shortBuffer.getShort());

                            channel.position(entryOffset+108+128*j+1284*i);
                            shortBuffer.clear();
                            channel.read(shortBuffer);
                            shortBuffer.flip();
                            subEntry.cameraShakeType = shortBuffer.getShort();

                            channel.position(entryOffset+110+128*j+1284*i);
                            shortBuffer.clear();
                            channel.read(shortBuffer);
                            shortBuffer.flip();
                            subEntry.cameraShakeTime = toUShort(shortBuffer.getShort());

                            channel.position(entryOffset+112+128*j+1284*i);
                            shortBuffer.clear();
                            channel.read(shortBuffer);
                            shortBuffer.flip();
                            subEntry.userBpeID = shortBuffer.getShort();

                            channel.position(entryOffset+114+128*j+1284*i);
                            shortBuffer.clear();
                            channel.read(shortBuffer);
                            shortBuffer.flip();
                            subEntry.victimBpeID = shortBuffer.getShort();

                            channel.position(entryOffset+116+128*j+1284*i);
                            shortBuffer.clear();
                            channel.read(shortBuffer);
                            shortBuffer.flip();
                            subEntry.staminaBrokenOverrideBdmId = shortBuffer.getShort();

                            channel.position(entryOffset+118+128*j+1284*i);
                            shortBuffer.clear();
                            channel.read(shortBuffer);
                            shortBuffer.flip();
                            subEntry.zVanishEnableTime = toUShort(shortBuffer.getShort());

                            channel.position(entryOffset+120+128*j+1284*i);
                            shortBuffer.clear();
                            channel.read(shortBuffer);
                            shortBuffer.flip();
                            subEntry.userAnimationTime = toUShort(shortBuffer.getShort());

                            channel.position(entryOffset+122+128*j+1284*i);
                            shortBuffer.clear();
                            channel.read(shortBuffer);
                            shortBuffer.flip();
                            subEntry.victimAnimationTime = toUShort(shortBuffer.getShort());

                            channel.position(entryOffset+124+128*j+1284*i);
                            intBuffer.clear();
                            channel.read(intBuffer);
                            intBuffer.flip();
                            subEntry.userAnimationSpeed = intBuffer.getFloat();

                            channel.position(entryOffset+128+128*j+1284*i);
                            intBuffer.clear();
                            channel.read(intBuffer);
                            intBuffer.flip();
                            subEntry.victimAnimationSpeed = intBuffer.getFloat();
                        }
                        bdmEntries.add(bdmEntry);
                    }
                    break;
            
                default:
                    Platform.runLater(()->{
                        Popups.LegacyFormat();
                    });
                    break;
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void bdmWriter(Path path){
        try(FileChannel channel=FileChannel.open(path,StandardOpenOption.WRITE,StandardOpenOption.CREATE,StandardOpenOption.TRUNCATE_EXISTING)) {
            ByteBuffer intBuffer=ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
            ByteBuffer shortBuffer=ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN);

            int entryOffset=16;
            channel.write(ByteBuffer.wrap(new byte[]{0x23,0x42,0x44,(byte)0x4D}));

            channel.position(4);
            channel.write(ByteBuffer.wrap(new byte[]{(byte)0xFE,(byte)0xFF}));

            channel.position(8);
            intBuffer.clear();
            intBuffer.putInt(allEntries.size());
            intBuffer.flip();
            channel.write(intBuffer);

            channel.position(12);
            intBuffer.clear();
            intBuffer.putInt(entryOffset);
            intBuffer.flip();
            channel.write(intBuffer);

            for(int i=0;i<allEntries.size();i++){
                BdmEntry bdmEntry = bdmEntries.get(i);
                channel.position(entryOffset+1284*i);
                intBuffer.clear();
                intBuffer.putInt(i);
                intBuffer.flip();
                channel.write(intBuffer);
                for(int j=0;j<10;j++){
                    BdmSubEntry subEntry = bdmEntry.subEntries[j];
                    channel.position(entryOffset+4+128*j+1284*i);
                    shortBuffer.clear();
                    shortBuffer.putShort((short)subEntry.damageType);
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    channel.position(entryOffset+6+128*j+1284*i);
                    shortBuffer.clear();
                    shortBuffer.putShort((short)subEntry.i02);
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    channel.position(entryOffset+8+128*j+1284*i);
                    shortBuffer.clear();
                    shortBuffer.putShort((short)subEntry.damageAmount);
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    channel.position(entryOffset+10+128*j+1284*i);
                    shortBuffer.clear();
                    shortBuffer.putShort((short)subEntry.i06);
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    channel.position(entryOffset+12+128*j+1284*i);
                    intBuffer.clear();
                    intBuffer.putFloat((float)subEntry.f08);
                    intBuffer.flip();
                    channel.write(intBuffer);

                    channel.position(entryOffset+16+128*j+1284*i);
                    shortBuffer.clear();
                    shortBuffer.putShort((short)subEntry.acbType);
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    channel.position(entryOffset+18+128*j+1284*i);
                    shortBuffer.clear();
                    shortBuffer.putShort(subEntry.cueId);
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    channel.position(entryOffset+20+128*j+1284*i);
                    shortBuffer.clear();
                    shortBuffer.putShort(subEntry.effect1Id);
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    channel.position(entryOffset+22+128*j+1284*i);
                    shortBuffer.clear();
                    shortBuffer.putShort((short)subEntry.effect1SkillId);
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    channel.position(entryOffset+24+128*j+1284*i);
                    shortBuffer.clear();
                    shortBuffer.putShort((short)subEntry.effect1EepkType);
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    channel.position(entryOffset+26+128*j+1284*i);
                    shortBuffer.clear();
                    shortBuffer.putShort((short)subEntry.i22);
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    channel.position(entryOffset+28+128*j+1284*i);
                    shortBuffer.clear();
                    shortBuffer.putShort(subEntry.effect2Id);
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    channel.position(entryOffset+30+128*j+1284*i);
                    shortBuffer.clear();
                    shortBuffer.putShort((short)subEntry.effect2SkillId);
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    channel.position(entryOffset+32+128*j+1284*i);
                    shortBuffer.clear();
                    shortBuffer.putShort((short)subEntry.effect2EepkType);
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    channel.position(entryOffset+34+128*j+1284*i);
                    shortBuffer.clear();
                    shortBuffer.putShort((short)subEntry.i30);
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    channel.position(entryOffset+36+128*j+1284*i);
                    shortBuffer.clear();
                    shortBuffer.putShort(subEntry.effect3Id);
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    channel.position(entryOffset+38+128*j+1284*i);
                    shortBuffer.clear();
                    shortBuffer.putShort((short)subEntry.effect3SkillId);
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    channel.position(entryOffset+40+128*j+1284*i);
                    shortBuffer.clear();
                    shortBuffer.putShort((short)subEntry.effect3EepkType);
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    channel.position(entryOffset+42+128*j+1284*i);
                    shortBuffer.clear();
                    shortBuffer.putShort((short)subEntry.i38);
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    channel.position(entryOffset+44+128*j+1284*i);
                    intBuffer.clear();
                    intBuffer.putFloat((float)subEntry.pushbackStrength);
                    intBuffer.flip();
                    channel.write(intBuffer);

                    channel.position(entryOffset+48+128*j+1284*i);
                    intBuffer.clear();
                    intBuffer.putFloat((float)subEntry.pushbackAcceleration);
                    intBuffer.flip();
                    channel.write(intBuffer);

                    channel.position(entryOffset+52+128*j+1284*i);
                    shortBuffer.clear();
                    shortBuffer.putShort((short)subEntry.userStunt);
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    channel.position(entryOffset+54+128*j+1284*i);
                    shortBuffer.clear();
                    shortBuffer.putShort((short)subEntry.victimStunt);
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    channel.position(entryOffset+56+128*j+1284*i);
                    shortBuffer.clear();
                    shortBuffer.putShort((short)subEntry.knockbackDuration);
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    channel.position(entryOffset+58+128*j+1284*i);
                    shortBuffer.clear();
                    shortBuffer.putShort((short)subEntry.knockbackRecoveryAfterImpactTime);
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    channel.position(entryOffset+60+128*j+1284*i);
                    shortBuffer.clear();
                    shortBuffer.putShort((short)subEntry.knockbackGroundImpactTime);
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    channel.position(entryOffset+62+128*j+1284*i);
                    shortBuffer.clear();
                    shortBuffer.putShort((short)subEntry.i58);
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    channel.position(entryOffset+64+128*j+1284*i);
                    intBuffer.clear();
                    intBuffer.putFloat((float)subEntry.knockbackStrengthX);
                    intBuffer.flip();
                    channel.write(intBuffer);

                    channel.position(entryOffset+68+128*j+1284*i);
                    intBuffer.clear();
                    intBuffer.putFloat((float)subEntry.knockbackStrengthY);
                    intBuffer.flip();
                    channel.write(intBuffer);

                    channel.position(entryOffset+72+128*j+1284*i);
                    intBuffer.clear();
                    intBuffer.putFloat((float)subEntry.knockbackStrengthZ);
                    intBuffer.flip();
                    channel.write(intBuffer);

                    channel.position(entryOffset+76+128*j+1284*i);
                    intBuffer.clear();
                    intBuffer.putFloat((float)subEntry.knockbackDragY);
                    intBuffer.flip();
                    channel.write(intBuffer);

                    channel.position(entryOffset+80+128*j+1284*i);
                    shortBuffer.clear();
                    shortBuffer.putShort((short)subEntry.i76);
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    channel.position(entryOffset+82+128*j+1284*i);
                    shortBuffer.clear();
                    shortBuffer.putShort((short)subEntry.knockbackGravityTime);
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    channel.position(entryOffset+84+128*j+1284*i);
                    shortBuffer.clear();
                    shortBuffer.putShort(subEntry.victimInvincibilityTime);
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    channel.position(entryOffset+86+128*j+1284*i);
                    shortBuffer.clear();
                    shortBuffer.putShort((short)subEntry.i82);
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    channel.position(entryOffset+88+128*j+1284*i);
                    shortBuffer.clear();
                    shortBuffer.putShort((short)subEntry.transformationType);
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    channel.position(entryOffset+90+128*j+1284*i);
                    shortBuffer.clear();
                    shortBuffer.putShort(subEntry.alimentType);
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    channel.position(entryOffset+92+128*j+1284*i);
                    shortBuffer.clear();
                    shortBuffer.putShort((short)subEntry.i88);
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    channel.position(entryOffset+94+128*j+1284*i);
                    shortBuffer.clear();
                    shortBuffer.putShort((short)subEntry.i90);
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    channel.position(entryOffset+96+128*j+1284*i);
                    shortBuffer.clear();
                    shortBuffer.putShort((short)subEntry.i92);
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    channel.position(entryOffset+98+128*j+1284*i);
                    shortBuffer.clear();
                    shortBuffer.putShort((short)subEntry.damageSpecial);
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    channel.position(entryOffset+100+128*j+1284*i);
                    shortBuffer.clear();
                    shortBuffer.putShort((short)subEntry.damageSpecial2);
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    channel.position(entryOffset+102+128*j+1284*i);
                    shortBuffer.clear();
                    shortBuffer.putShort((short)subEntry.damageSpecial3);
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    channel.position(entryOffset+104+128*j+1284*i);
                    shortBuffer.clear();
                    shortBuffer.putShort((short)subEntry.stumbleType);
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    channel.position(entryOffset+106+128*j+1284*i);
                    shortBuffer.clear();
                    shortBuffer.putShort((short)subEntry.secondaryType);
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    channel.position(entryOffset+108+128*j+1284*i);
                    shortBuffer.clear();
                    shortBuffer.putShort(subEntry.cameraShakeType);
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    channel.position(entryOffset+110+128*j+1284*i);
                    shortBuffer.clear();
                    shortBuffer.putShort((short)subEntry.cameraShakeTime);
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    channel.position(entryOffset+112+128*j+1284*i);
                    shortBuffer.clear();
                    shortBuffer.putShort(subEntry.userBpeID);
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    channel.position(entryOffset+114+128*j+1284*i);
                    shortBuffer.clear();
                    shortBuffer.putShort(subEntry.victimBpeID);
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    channel.position(entryOffset+116+128*j+1284*i);
                    shortBuffer.clear();
                    shortBuffer.putShort(subEntry.staminaBrokenOverrideBdmId);
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    channel.position(entryOffset+118+128*j+1284*i);
                    shortBuffer.clear();
                    shortBuffer.putShort((short)subEntry.zVanishEnableTime);
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    channel.position(entryOffset+120+128*j+1284*i);
                    shortBuffer.clear();
                    shortBuffer.putShort((short)subEntry.userAnimationTime);
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    channel.position(entryOffset+122+128*j+1284*i);
                    shortBuffer.clear();
                    shortBuffer.putShort((short)subEntry.victimAnimationTime);
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    channel.position(entryOffset+124+128*j+1284*i);
                    intBuffer.clear();
                    intBuffer.putFloat((float)subEntry.userAnimationSpeed);
                    intBuffer.flip();
                    channel.write(intBuffer);

                    channel.position(entryOffset+128+128*j+1284*i);
                    intBuffer.clear();
                    intBuffer.putFloat((float)subEntry.victimAnimationSpeed);
                    intBuffer.flip();
                    channel.write(intBuffer);
                }
            }
        }
        catch(IOException e){
            e.printStackTrace();

        }
    }

    @Override
    public Object clone() {
        try {
            return (BdmSubEntry) super.clone();
        } catch (CloneNotSupportedException e) {
            return new BdmSubEntry();
        }
    }
}

class BdmEntry {
    public final BdmSubEntry[] subEntries = new BdmSubEntry[10];

    public BdmEntry() {
        for (int i = 0; i < 10; i++) {
            subEntries[i] = new BdmSubEntry();
        }
    }

    public BdmEntry(BdmEntry other) {
        for (int i = 0; i < 10; i++) {
            // Clones each individual sub-entry into the new instance's array
            this.subEntries[i] = new BdmSubEntry(other.subEntries[i]);
        }
    }
}

class BdmSubEntry {
    public int damageType = 0;
    public int i02 = 0;
    public int damageAmount = 0;
    public int i06 = 0;
    public float f08 = 0.0f;
    public int acbType = 0;
    public short cueId = 0;
    public short effect1Id = 0;
    public int effect1SkillId = 0;
    public int effect1EepkType = 0;
    public int i22 = 0;
    public short effect2Id = 0;
    public int effect2SkillId = 0;
    public int effect2EepkType = 0;
    public int i30 = 0;
    public short effect3Id = 0;
    public int effect3SkillId = 0;
    public int effect3EepkType = 0;
    public int i38 = 0;
    public float pushbackStrength = 0.0f;
    public float pushbackAcceleration = 0.0f;
    public int userStunt = 0;
    public int knockbackDuration = 0;
    public int knockbackRecoveryAfterImpactTime = 0;
    public int knockbackGroundImpactTime = 0;
    public int i58 = 0;
    public int victimStunt = 0;
    public float knockbackStrengthX = 0.0f;
    public float knockbackStrengthY = 0.0f;
    public float knockbackStrengthZ = 0.0f;
    public float knockbackDragY = 0.0f;
    public int i76 = 0;
    public int knockbackGravityTime = 0;
    public short victimInvincibilityTime = 0;
    public int i82 = 0;
    public int transformationType = 0;
    public short alimentType = 0;
    public int i88 = 0;
    public int i90 = 0;
    public int i92 = 0;
    public int damageSpecial = 0;
    public int damageSpecial2 = 0;
    public int damageSpecial3 = 0;
    public int stumbleType = 0;
    public int secondaryType = 0;
    public short cameraShakeType = 0;
    public int cameraShakeTime = 0;
    public short userBpeID = 0;
    public short victimBpeID = 0;
    public short staminaBrokenOverrideBdmId = 0;
    public int zVanishEnableTime = 0;
    public int userAnimationTime = 0;
    public int victimAnimationTime = 0;
    public float userAnimationSpeed = 0.0f;
    public float victimAnimationSpeed = 0.0f;

    public BdmSubEntry() {}
    public BdmSubEntry(BdmSubEntry other) {
        this.damageType = other.damageType;
        this.i02 = other.i02;
        this.damageAmount = other.damageAmount;
        this.i06 = other.i06;
        this.f08 = other.f08;
        this.acbType = other.acbType;
        this.cueId = other.cueId;
        this.effect1Id = other.effect1Id;
        this.effect1SkillId = other.effect1SkillId;
        this.effect1EepkType = other.effect1EepkType;
        this.i22 = other.i22;
        this.effect2Id = other.effect2Id;
        this.effect2SkillId = other.effect2SkillId;
        this.effect2EepkType = other.effect2EepkType;
        this.i30 = other.i30;
        this.effect3Id = other.effect3Id;
        this.effect3SkillId = other.effect3SkillId;
        this.effect3EepkType = other.effect3EepkType;
        this.i38 = other.i38;
        this.pushbackStrength = other.pushbackStrength;
        this.pushbackAcceleration = other.pushbackAcceleration;
        this.userStunt = other.userStunt;
        this.knockbackDuration = other.knockbackDuration;
        this.knockbackRecoveryAfterImpactTime = other.knockbackRecoveryAfterImpactTime;
        this.knockbackGroundImpactTime = other.knockbackGroundImpactTime;
        this.i58 = other.i58;
        this.victimStunt = other.victimStunt;
        this.knockbackStrengthX = other.knockbackStrengthX;
        this.knockbackStrengthY = other.knockbackStrengthY;
        this.knockbackStrengthZ = other.knockbackStrengthZ;
        this.knockbackDragY = other.knockbackDragY;
        this.i76 = other.i76;
        this.knockbackGravityTime = other.knockbackGravityTime;
        this.victimInvincibilityTime = other.victimInvincibilityTime;
        this.i82 = other.i82;
        this.transformationType = other.transformationType;
        this.alimentType = other.alimentType;
        this.i88 = other.i88;
        this.i90 = other.i90;
        this.i92 = other.i92;
        this.damageSpecial = other.damageSpecial;
        this.damageSpecial2 = other.damageSpecial2;
        this.damageSpecial3 = other.damageSpecial3;
        this.stumbleType = other.stumbleType;
        this.secondaryType = other.secondaryType;
        this.cameraShakeType = other.cameraShakeType;
        this.cameraShakeTime = other.cameraShakeTime;
        this.userBpeID = other.userBpeID;
        this.victimBpeID = other.victimBpeID;
        this.staminaBrokenOverrideBdmId = other.staminaBrokenOverrideBdmId;
        this.zVanishEnableTime = other.zVanishEnableTime;
        this.userAnimationTime = other.userAnimationTime;
        this.victimAnimationTime = other.victimAnimationTime;
        this.userAnimationSpeed = other.userAnimationSpeed;
        this.victimAnimationSpeed = other.victimAnimationSpeed;
    }

}
