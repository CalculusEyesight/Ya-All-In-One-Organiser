package xv2;
import static xv2.BinaryUtilities.toUByte;
import static xv2.BinaryUtilities.toUShort;
import static xv2.BinaryUtilities.toUint32;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Spinner;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
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
import javafx.scene.paint.Color;
public class Bsa {
    TreeView<String> treeView = new TreeView<>();

    ArrayList<TreeItem<String>> allEntries;
    
    HashMap<TreeItem<String>, BsaMainEntry> bsaMainHashMap = new HashMap<>();
    HashMap<TreeItem<String>, BsaCollisionEntry> bsaCollisionHashMap = new HashMap<>();
    HashMap<TreeItem<String>, BsaExpirationEntry> bsaExpirationHashMap = new HashMap<>();
    HashMap<TreeItem<String>, BsaType0Entry> bsaType0HashMap = new HashMap<>();
    HashMap<TreeItem<String>, BsaType1Entry> bsaType1HashMap = new HashMap<>();
    HashMap<TreeItem<String>, BsaType2Entry> bsaType2HashMap = new HashMap<>();
    HashMap<TreeItem<String>, BsaType3Entry> bsaType3HashMap = new HashMap<>();
    HashMap<TreeItem<String>, BsaType4Entry> bsaType4HashMap = new HashMap<>();
    HashMap<TreeItem<String>, BsaType6Entry> bsaType6HashMap = new HashMap<>();
    HashMap<TreeItem<String>, BsaType7Entry> bsaType7HashMap = new HashMap<>();
    HashMap<TreeItem<String>, BsaType8Entry> bsaType8HashMap = new HashMap<>();
    HashMap<TreeItem<String>, BsaType10Entry> bsaType10HashMap = new HashMap<>();
    HashMap<TreeItem<String>, BsaType12Entry> bsaType12HashMap = new HashMap<>();
    HashMap<TreeItem<String>, BsaType13Entry> bsaType13HashMap = new HashMap<>();
    HashMap<TreeItem<String>, BsaType14Entry> bsaType14HashMap = new HashMap<>();

    TreeItem<String> currentEntry = new TreeItem<>();
    TreeItem<String> grandParentEntry = new TreeItem<>();

    ContextMenu contextMenu = new ContextMenu();
    MenuItem addEntry = new MenuItem("Add Entry");
    Menu addSubEntry = new Menu("Add Sub Entry");
    MenuItem copy = new MenuItem("Copy Ctrl+C");
    MenuItem delete = new MenuItem("Delete Del");
    MenuItem addComment = new MenuItem("Add Comment  Ctrl+Q");
    MenuItem noCopiedItemFound = new MenuItem("no copied item found");
    MenuItem copiedItem = new MenuItem();
    MenuItem pasteItem = new MenuItem("dummy");
    MenuItem addItemCopy = new MenuItem();

    MenuItem collisionMenuItem = new MenuItem("Collision (After Effects)");
    MenuItem expirationMenuItem = new MenuItem("Expiration (After Effects)");
    MenuItem type0MenuItem = new MenuItem("BSA Entry Passing");
    MenuItem type1MenuItem = new MenuItem("Movement");
    MenuItem type2MenuItem = new MenuItem("BSA Type 2");
    MenuItem type3MenuItem = new MenuItem("Hitbox");
    MenuItem type4MenuItem = new MenuItem("Deflection");
    MenuItem type6MenuItem = new MenuItem("Effect");
    MenuItem type7MenuItem = new MenuItem("Sound");
    MenuItem type8MenuItem = new MenuItem("Screen Effect");
    MenuItem type10MenuItem = new MenuItem("BSA Type 10");
    MenuItem type12MenuItem = new MenuItem("BSA Type 12");
    MenuItem type13MenuItem = new MenuItem("BSA Type 13");
    MenuItem type14MenuItem = new MenuItem("BSA Type 14");

    TabPane tabPane = new TabPane();

    Object copyContainer = new Object();
    ArrayList<Object[]> copyListContainer = new ArrayList<>();
    ArrayList<String> copyTypesContainer = new ArrayList<>();

    public Bsa() {
        entriesActionListener();
        entriesKyesListener();
    }

    public SplitPane createSplitPane() {
        SplitPane splitPane = new SplitPane();

        splitPane.getItems().addAll(treeView, tabPane);
        splitPane.setDividerPositions(0.245);
        splitPane.getStylesheets().add(getClass().getResource("/style.css").toExternalForm()); 
        return splitPane;
    }

    private void createBsaMain(BsaMainEntry entry) {
        //i00
        Label i00Label = new Label("I_00");
        i00Label.setPrefWidth(60);

        TextField i00TextField = new TextField(String.valueOf(entry.i00));
        i00TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (i00TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.i00 = Integer.parseInt(newText); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox i00HBox = new HBox(i00Label, i00TextField);
        i00HBox.setAlignment(Pos.CENTER_LEFT);
        //i00

        //impact properties
        Label impactPropertiesLabel = new Label("Impact Properties"); 
        impactPropertiesLabel.setPrefWidth(150);

        //i16_a
        Label A = new Label("A");

        Spinner <Integer> i16_aSpinner  = new Spinner<>(0, 15, entry.i16_a);
        i16_aSpinner.setEditable(true);
        i16_aSpinner.setPrefWidth(60);
        i16_aSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                entry.i16_a = newValue.byteValue();
            }
        });
        //i16_a

        //i16_b
        Label B = new Label("B");

        Spinner <Integer> i16_bSpinner  = new Spinner<>(0, 15, entry.i16_b);
        i16_bSpinner.setEditable(true);
        i16_bSpinner.setPrefWidth(60);
        i16_bSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                entry.i16_b = newValue.byteValue();
            }
        });
        //i16_b

        HBox impactPropertiesHBox = new HBox(15, impactPropertiesLabel, A, i16_aSpinner, B, i16_bSpinner);
        impactPropertiesHBox.setAlignment(Pos.CENTER_LEFT);
        //impact properties

        //i17
        Label i17Label = new Label("I_17");
        i17Label.setPrefWidth(60);

        TextField I17TextField = new TextField(String.valueOf(entry.i17));
        I17TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (I17TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.i17 = Byte.parseByte(newText); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox i17HBox = new HBox(i17Label, I17TextField);
        i17HBox.setAlignment(Pos.CENTER_LEFT);
        //i17

        //i18
        Label i18Label = new Label("I_18");
        i18Label.setPrefWidth(60);

        TextField i18TextField = new TextField(String.valueOf(entry.i18));
        i18TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (i18TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.i18 = Integer.parseInt(newText); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox i18HBox = new HBox(i18Label, i18TextField);
        i18HBox.setAlignment(Pos.CENTER_LEFT);
        //i18

        //lifetime
        Label lifetimeLabel = new Label("Lifetime");
        lifetimeLabel.setPrefWidth(150);
        
        Spinner <Integer> lifetimeSpinner = new Spinner<>(0, 65535, entry.lifetime);
        lifetimeSpinner.setEditable(true);
        lifetimeSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                entry.lifetime = newValue;
            }
        });

        HBox lifetimeHBox = new HBox(15, lifetimeLabel, lifetimeSpinner);
        lifetimeHBox.setAlignment(Pos.CENTER_LEFT);
        //lifetime

        //i24
        Label i24Label = new Label("I_24");
        i24Label.setPrefWidth(60);

        TextField i24TextField = new TextField(String.valueOf(entry.i24));
        i24TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (i24TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.i24 = Integer.parseInt(newText); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox i24HBox = new HBox(i24Label, i24TextField);
        i24HBox.setAlignment(Pos.CENTER_LEFT);
        //i24

        //expires
        Label expiresLabel = new Label("Entry Pass On When Expires");
        expiresLabel.setPrefWidth(150);
        
        Spinner <Integer> expiresSpinner = new Spinner<>(0, 65535, entry.expires);
        expiresSpinner.setEditable(true);
        expiresSpinner.valueProperty().addListener((obs, oldValue,newValue) -> {
            if (newValue != null) { 
                entry.expires = newValue;
            }
        });

        HBox expiresHBox = new HBox(15, expiresLabel, expiresSpinner);
        expiresHBox.setAlignment(Pos.CENTER_LEFT);
        //expires

        //impact projectile
        Label impactProjectileLabel = new Label("Impact Projectile");
        impactProjectileLabel.setPrefWidth(150);
        
        Spinner <Integer> impactProjectileSpinner = new Spinner<>(0, 65535, entry.impactProjectile);
        impactProjectileSpinner.setEditable(true);
        impactProjectileSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                entry.impactProjectile = newValue;
            }
        });

        HBox impactProjectileHBox = new HBox(15, impactProjectileLabel, impactProjectileSpinner);
        impactProjectileHBox.setAlignment(Pos.CENTER_LEFT);
        //impact projectile

        //impact enemy
        Label impactEnemyLabel = new Label("Impact Enemy");
        impactEnemyLabel.setPrefWidth(150);
        
        Spinner <Integer> impactEnemySpinner = new Spinner<>(0, 65535, entry.impactEnemy);
        impactEnemySpinner.setEditable(true);
        impactEnemySpinner.valueProperty().addListener((obs,oldValue,newValue) -> {
            if (newValue != null) {
                entry.impactEnemy = newValue;
            }
        });

        HBox impactEnemyHBox = new HBox(15, impactEnemyLabel, impactEnemySpinner);
        impactEnemyHBox.setAlignment(Pos.CENTER_LEFT);
        //impact enemy

        //impact ground
        Label impactGroundLabel = new Label("Impact Ground");
        impactGroundLabel.setPrefWidth(150);
        
        Spinner <Integer> impactGroundSpinner = new Spinner<>(0, 65535, entry.impactGround);
        impactGroundSpinner.setEditable(true);
        impactGroundSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                entry.impactGround = newValue;
            }
        });
        
        HBox impactGroundHBox = new HBox(15, impactGroundLabel, impactGroundSpinner);
        impactGroundHBox.setAlignment(Pos.CENTER_LEFT);
        //impact ground
        
        //i40
        Label i40Label = new Label("I_40");
        i40Label.setPrefWidth(60);

        TextField i40TextField = new TextField(String.valueOf(entry.i40));
        i40TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (i40TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.i40 = Integer.parseInt(newText); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox i40HBox = new HBox(i40Label, i40TextField);
        i40HBox.setAlignment(Pos.CENTER_LEFT);
        //i40

        //i44
        Label i44Label = new Label("I_44");
        i44Label.setPrefWidth(60);

        TextField i44TextField = new TextField(String.valueOf(entry.i44));
        i44TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (i44TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.i44 = Integer.parseInt(newText); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox i44HBox = new HBox(i44Label, i44TextField);
        i44HBox.setAlignment(Pos.CENTER_LEFT);
        //i44

        //i48
        Label i48Label = new Label("I_48");
        i48Label.setPrefWidth(60);

        TextField i48TextField = new TextField(String.valueOf(entry.i48));
        i48TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (i48TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.i48 = Integer.parseInt(newText); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox i48HBox=new HBox(i48Label, i48TextField);
        i48HBox.setAlignment(Pos.CENTER_LEFT);
        //i48

        //entry
        VBox entryVBox = new VBox(30, 
            impactPropertiesHBox, lifetimeHBox, 
            expiresHBox, impactProjectileHBox, 
            impactEnemyHBox, impactGroundHBox
        );
        entryVBox.setPadding(new Insets(20, 0, 0, 16));

        Tab entryTab = new Tab("Entry", entryVBox);
        entryTab.setClosable(false);
        //entry

        //unknown
        VBox unknownVBox = new VBox(30, 
            i00HBox, i17HBox, 
            i18HBox, i24HBox, 
            i40HBox, i44HBox, 
            i48HBox
        );
        unknownVBox.setPadding(new Insets(20, 0, 0, 16));

        Tab unknownTab = new Tab("Unknown", unknownVBox);
        unknownTab.setClosable(false);
        //unknown

        tabPane.getTabs().addAll(entryTab, unknownTab);
    }

    private void createBsaCollision(BsaCollisionEntry entry) {
        //eepk type
        Label eepkTypeLabel = new Label("EEPK Type");
        eepkTypeLabel.setPrefWidth(100);

        ToggleGroup eepkTypeToggleGroup = new ToggleGroup();

        RadioButton common = new RadioButton("Common");
        common.setToggleGroup(eepkTypeToggleGroup);

        RadioButton stageBG = new RadioButton("Stage BG");
        stageBG.setToggleGroup(eepkTypeToggleGroup);

        RadioButton character = new RadioButton("Character");
        character.setToggleGroup(eepkTypeToggleGroup);

        RadioButton awokenSkill = new RadioButton("Awoken Skill");
        awokenSkill.setToggleGroup(eepkTypeToggleGroup);

        RadioButton superSkill = new RadioButton("Super Skill");
        superSkill.setToggleGroup(eepkTypeToggleGroup);

        RadioButton ultimateSkill = new RadioButton("Ultimate Skill");
        ultimateSkill.setToggleGroup(eepkTypeToggleGroup);

        RadioButton evasiveSkill = new RadioButton("Evasive Skill");
        evasiveSkill.setToggleGroup(eepkTypeToggleGroup);

        RadioButton kiBlastSkill = new RadioButton("Ki Blast Skill");
        kiBlastSkill.setToggleGroup(eepkTypeToggleGroup);

        RadioButton stage = new RadioButton("Stage");
        stage.setToggleGroup(eepkTypeToggleGroup);

        switch (entry.eepkType) {
            case 1 -> stageBG.setSelected(true);
            case 2 -> character.setSelected(true);
            case 3 -> awokenSkill.setSelected(true);
            case 5 -> superSkill.setSelected(true);
            case 6 -> ultimateSkill.setSelected(true);
            case 7 -> evasiveSkill.setSelected(true);
            case 9 -> kiBlastSkill.setSelected(true);
            case 11 -> stage.setSelected(true);
            default -> common.setSelected(true);
        }

        eepkTypeToggleGroup.selectedToggleProperty().addListener((obs, oldValue, newValue)-> {
            if (newValue.isSelected()) {
                if ((RadioButton) newValue == common) { 
                    entry.eepkType = 0;
                }
                else if ((RadioButton) newValue == stageBG) { 
                    entry.eepkType = 1;
                }
                else if ((RadioButton) newValue == character) { 
                    entry.eepkType = 2;
                }
                else if ((RadioButton) newValue == awokenSkill) { 
                    entry.eepkType = 3;
                }
                else if ((RadioButton) newValue == superSkill) { 
                    entry.eepkType = 5;
                }
                else if ((RadioButton) newValue == ultimateSkill) { 
                    entry.eepkType = 6;
                }
                else if ((RadioButton) newValue == evasiveSkill) { 
                    entry.eepkType = 7;
                }
                else if ((RadioButton) newValue == kiBlastSkill) { 
                    entry.eepkType = 9;
                }
                else if ((RadioButton) newValue == stage) {
                    entry.eepkType = 11;
                }
            }
        });

        GridPane eepkTypeGridPane = new GridPane(10, 10);
        eepkTypeGridPane.getStyleClass().add("titled-address-box");
        eepkTypeGridPane.add(common, 0, 0);   
        eepkTypeGridPane.add(stageBG, 1, 0);          
        eepkTypeGridPane.add(character, 2, 0);          
        eepkTypeGridPane.add(awokenSkill, 0, 1);          
        eepkTypeGridPane.add(superSkill, 1, 1);          
        eepkTypeGridPane.add(ultimateSkill, 2, 1);          
        eepkTypeGridPane.add(evasiveSkill, 0, 2);          
        eepkTypeGridPane.add(kiBlastSkill, 1, 2);          
        eepkTypeGridPane.add(stage, 2, 2);          

        HBox eepkTypeHBox=new HBox(eepkTypeLabel, eepkTypeGridPane);
        eepkTypeHBox.setAlignment(Pos.CENTER_LEFT);
        //eepk type

        //skill id
        Label skillIdLabel = new Label("Skill ID");
        skillIdLabel.setPrefWidth(100);
        
        Spinner <Integer> skillIdSpinner = new Spinner<>(0, 65535, entry.skillId);
        skillIdSpinner.setEditable(true);
        skillIdSpinner.valueProperty().addListener((obs, oldValue, newValue)-> {
            if (newValue != null) {
                entry.skillId = newValue;
            }
        });

        HBox skillIdHBox = new HBox(skillIdLabel, skillIdSpinner);
        skillIdHBox.setAlignment(Pos.CENTER_LEFT);
        //skill id

        //effect id
        Label effectIdLabel = new Label("Effect ID");
        effectIdLabel.setPrefWidth(100);
        
        Spinner <Integer> effectIdSpinner = new Spinner<>(0, 65535, entry.effectId);
        effectIdSpinner.setEditable(true);
        effectIdSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue!=null) {
                entry.effectId = newValue;
            }
        });

        HBox effectIdHBox = new HBox(effectIdLabel, effectIdSpinner);
        effectIdHBox.setAlignment(Pos.CENTER_LEFT);
        //effect id
        
        //i06
        Label i06Label = new Label("I_06");
        i06Label.setPrefWidth(60);

        TextField i06TextField = new TextField(String.valueOf(entry.i06));
        i06TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (i06TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.i06 = Integer.parseInt(newText);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox i06HBox = new HBox(i06Label, i06TextField);
        i06HBox.setAlignment(Pos.CENTER_LEFT);
        //i06

        //i08
        Label i08Label = new Label("I_08");
        i08Label.setPrefWidth(60);

        TextField i08TextField = new TextField(String.valueOf(entry.i08));
        i08TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (i08TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.i08 = Integer.parseInt(newText); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox i08HBox = new HBox(i08Label, i08TextField);
        i08HBox.setAlignment(Pos.CENTER_LEFT);
        //i08

        //i12
        Label i12Label = new Label("I_12");
        i12Label.setPrefWidth(60);

        TextField i12TextField=new TextField(String.valueOf(entry.i12));
        i12TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (i12TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.i12 = Integer.parseInt(newText); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });
        HBox i12HBox = new HBox(i12Label, i12TextField);
        i12HBox.setAlignment(Pos.CENTER_LEFT);
        //i12

        //i16
        Label i16Label = new Label("I_16");
        i16Label.setPrefWidth(60);

        TextField i16Textfield = new TextField(String.valueOf(entry.i16));
        i16Textfield.textProperty().addListener((obs, oldText, newText) -> {
            if (i16Textfield.getText().contains("-")) {
                return;
            }
            try {
                entry.i16 = Integer.parseInt(newText); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox i16HBox=new HBox(i16Label, i16Textfield);
        i16HBox.setAlignment(Pos.CENTER_LEFT);
        //i16

        //i20
        Label i20Label = new Label("I_20");
        i20Label.setPrefWidth(60);

        TextField i20TextField = new TextField(String.valueOf(entry.i20));
        i20TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (i20TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.i20 = Integer.parseInt(newText); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox i20HBox = new HBox(i20Label, i20TextField);
        i20HBox.setAlignment(Pos.CENTER_LEFT);
        //i20

        //collision
        VBox collisionVBox = new VBox(30, eepkTypeHBox, skillIdHBox, effectIdHBox);
        collisionVBox.setPadding(new Insets(20, 0, 0, 16));
 
        Tab collisionTab = new Tab("Collision", collisionVBox);
        collisionTab.setClosable(false);
        //collison

        //unknown
        VBox unknownVBox = new VBox(30, i06HBox, i08HBox, i12HBox, i16HBox, i20HBox);
        unknownVBox.setPadding(new Insets(20, 0, 0, 16));

        Tab unknownTab = new Tab("Unknown", unknownVBox);
        unknownTab.setClosable(false);
        //unknown

        tabPane.getTabs().addAll(collisionTab, unknownTab);
    }

    private void createBsaExpiration(BsaExpirationEntry entry) {
        //i00
        Label i00Label = new Label("I_00");
        i00Label.setPrefWidth(60);

        TextField i00TextField = new TextField(String.valueOf(entry.i00));
        i00TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (i00TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.i00 = Integer.parseInt(newText); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox i00HBox = new HBox(i00Label, i00TextField);
        i00HBox.setAlignment(Pos.CENTER_LEFT);
        //i00

        //i02
        Label i02Label = new Label("I_02");
        i02Label.setPrefWidth(60);

        TextField i02TextField = new TextField(String.valueOf(entry.i02));
        i02TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (i02TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.i02 = Integer.parseInt(newText); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox i02HBox = new HBox(i02Label, i02TextField);
        i02HBox.setAlignment(Pos.CENTER_LEFT);
        //i02

        //i04
        Label i04Label = new Label("I_04");
        i04Label.setPrefWidth(60);

        TextField i04TextField = new TextField(String.valueOf(entry.i04));
        i04TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (i04TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.i04 = Integer.parseInt(newText); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox i04HBox = new HBox(i04Label, i04TextField);
        i04HBox.setAlignment(Pos.CENTER_LEFT);
        //i04
        
        //i06
        Label i06Label = new Label("I_06");
        i06Label.setPrefWidth(60);
        
        TextField i06Textfield = new TextField(String.valueOf(entry.i06));
        i06Textfield.textProperty().addListener((obs, oldText, newText) -> {
            if (i06Textfield.getText().contains("-")) {
                return;
            }
            try {
                entry.i06 = Integer.parseInt(newText); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox i06HBox = new HBox(i06Label, i06Textfield);
        i06HBox.setAlignment(Pos.CENTER_LEFT);
        //i06

        //expiration
        VBox expirationVBox = new VBox(30, i00HBox, i02HBox, i04HBox, i06HBox);
        expirationVBox.setPadding(new Insets(20, 0, 0, 16));

        Tab expirationTab = new Tab("Expiration", expirationVBox);
        expirationTab.setClosable(false);
        //expiration

        tabPane.getTabs().add(expirationTab);
    }

    private void createBsaType0(BsaType0Entry entry) {
        //start time
        Label startTimeLabel = new Label("Start Time");
        startTimeLabel.setPrefWidth(120);
        
        Spinner <Integer> startTimeSpinner = new Spinner<>(0, 65535, entry.startTime);
        startTimeSpinner.setEditable(true);
        startTimeSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                entry.startTime = newValue;
            }
        });

        HBox startTimeHBox = new HBox(15,startTimeLabel,startTimeSpinner);
        startTimeHBox.setAlignment(Pos.CENTER_LEFT);
        //start time

        //duration
        Label durationLabel = new Label("Duration");
        durationLabel.setPrefWidth(120);
        
        Spinner <Integer> durationSpinner = new Spinner<>(0, 65535, entry.duration);
        durationSpinner.setEditable(true);
        durationSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                entry.duration = newValue;
            }
        });

        HBox durationHBox = new HBox(15, durationLabel, durationSpinner);
        durationHBox.setAlignment(Pos.CENTER_LEFT);
        //duration

        //first condition
        Label firstConditionLabel = new Label("First Conditon");
        firstConditionLabel.setPrefWidth(120);

        Spinner <Integer> firstConditonSpinner = new Spinner<>(0, 65535, entry.firstCondition);
        firstConditonSpinner.setEditable(true);
        firstConditonSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                entry.firstCondition = newValue;
            }
        });

        Label firstConditionIndicatorLabel = new Label();
        firstConditionIndicatorLabel.setTextFill(Color.CRIMSON);
        
        firstConditionIndicatorLabel.textProperty().bind(
            Bindings.createStringBinding(() -> {
                return switch (firstConditonSpinner.getValue()) {
                    case 4 -> "Pass When Attack Hits?";
                    case 5 -> "BAC Related Pass?";
                    default -> "Unknown";
                };
            }, firstConditonSpinner.valueProperty())
        );
        
        HBox firstConditionHBox = new HBox(15, firstConditionLabel, firstConditonSpinner, firstConditionIndicatorLabel);
        firstConditionHBox.setAlignment(Pos.CENTER_LEFT);
        //first condition

        //second condition
        Label secondConditionLabel = new Label("Second Condition");
        secondConditionLabel.setPrefWidth(130);

        //option 1
        Label option1Label = new Label("Option 1");
        option1Label.getStyleClass().add("titled-address-label");
        option1Label.setTranslateY(-8); 
        option1Label.setTranslateX(10);

        CheckBox unknown1 = new CheckBox("Unknown 1");
        CheckBox unknown2 = new CheckBox("Unknown 2");
        CheckBox unknown3 = new CheckBox("Unknown 3");
        CheckBox bacConditionFromSystem = new CheckBox("Bac Condition From System");

        unknown1.setSelected((entry.secondCondition & 1) != 0);
        unknown2.setSelected((entry.secondCondition & 2) != 0);
        unknown3.setSelected((entry.secondCondition & 4) != 0);
        bacConditionFromSystem.setSelected((entry.secondCondition & 8) != 0);

        unknown1.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.secondCondition |= 1;
            }
            else {
                entry.secondCondition &= ~1;
            }
        });
        unknown2.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.secondCondition |= 2;
            }
            else {
                entry.secondCondition &= ~2;
            }
        });
        unknown3.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.secondCondition |= 4;
            }
            else {
                entry.secondCondition &= ~4;
            }
        });
        bacConditionFromSystem.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.secondCondition |= 8;
            }
            else {
                entry.secondCondition &= ~8;
            }
        });

        VBox Option1Box = new VBox(2, unknown1, unknown2, unknown3, bacConditionFromSystem);

        VBox borderContainerOption1=new VBox(Option1Box);
        borderContainerOption1.getStyleClass().add("titled-address-box");
        borderContainerOption1.setPadding(new Insets(12, 0, 0, 0));

        StackPane option1BoxStackPane = new StackPane(borderContainerOption1,option1Label);
        StackPane.setAlignment(option1Label, Pos.TOP_LEFT);
        //option 1

        //option 2
        Label option2Label = new Label("Option 2");
        option2Label.getStyleClass().add("titled-address-label");
        option2Label.setTranslateY(-8); 
        option2Label.setTranslateX(10);

        CheckBox unknown5 = new CheckBox("Unknown 5");
        CheckBox unknown6 = new CheckBox("Unknown 6");
        CheckBox unknown7 = new CheckBox("Unknown 7");
        CheckBox unknown8 = new CheckBox("Unknown 8");

        unknown5.setSelected((entry.secondCondition & 16) != 0);
        unknown6.setSelected((entry.secondCondition & 32) != 0);
        unknown7.setSelected((entry.secondCondition & 64) != 0);
        unknown8.setSelected((entry.secondCondition & 128) != 0);

        unknown5.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.secondCondition |= 16;
            }
            else {
                entry.secondCondition &= ~16;
            }
        });
        unknown6.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.secondCondition |= 32;
            }
            else {
                entry.secondCondition &= ~32;
            }
        });
        unknown7.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.secondCondition |= 64;
            }
            else {
                entry.secondCondition &= ~64;
            }
        });
        unknown8.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.secondCondition |= 128;
            }
            else {
                entry.secondCondition &= ~128;
            }
        });

        VBox Option2Box = new VBox(2, unknown5, unknown6, unknown7, unknown8);

        VBox borderContainerOption2=new VBox(Option2Box);
        borderContainerOption2.getStyleClass().add("titled-address-box");
        borderContainerOption2.setPadding(new Insets(12, 0, 0, 0));

        StackPane option2BoxStackPane = new StackPane(borderContainerOption2, option2Label);
        StackPane.setAlignment(option2Label, Pos.TOP_LEFT);
        //option 2

        //option 3
        Label option3Label = new Label("Option 3");
        option3Label.getStyleClass().add("titled-address-label");
        option3Label.setTranslateY(-8); 
        option3Label.setTranslateX(10);

        CheckBox unknown9 = new CheckBox("Unknown 9");
        CheckBox unknown10 = new CheckBox("Unknown 10");
        CheckBox unknown11 = new CheckBox("Unknown 11");
        CheckBox unknown12 = new CheckBox("Unknown 12");

        unknown9.setSelected((entry.secondCondition & 256) != 0);
        unknown10.setSelected((entry.secondCondition & 512) != 0);
        unknown11.setSelected((entry.secondCondition & 1024) != 0);
        unknown12.setSelected((entry.secondCondition & 2048) != 0);

        unknown9.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.secondCondition |= 256;
            }
            else {
                entry.secondCondition &= ~256;
            }
        });
        unknown10.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.secondCondition |= 512;
            }
            else {
                entry.secondCondition &= ~512;
            }
        });
        unknown11.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.secondCondition |= 1024;
            }
            else {
                entry.secondCondition &= ~1024;
            }
        });
        unknown12.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.secondCondition |= 2048;
            }
            else {
                entry.secondCondition &= ~2048;
            }
        });

        VBox Option3Box = new VBox(2, unknown9, unknown10, unknown11, unknown12);

        VBox borderContainerOption3=new VBox(Option3Box);
        borderContainerOption3.getStyleClass().add("titled-address-box");
        borderContainerOption3.setPadding(new Insets(12, 0, 0, 0));

        StackPane option3BoxStackPane = new StackPane(borderContainerOption3, option3Label);
        StackPane.setAlignment(option3Label, Pos.TOP_LEFT);
        //option 3

        //option 4
        Label option4Label = new Label("Option 4");
        option4Label.getStyleClass().add("titled-address-label");
        option4Label.setTranslateY(-8); 
        option4Label.setTranslateX(10);

        CheckBox unknown13 = new CheckBox("Unknown 13");
        CheckBox unknown14 = new CheckBox("Unknown 14");
        CheckBox unknown15 = new CheckBox("Unknown 15");
        CheckBox unknown16 = new CheckBox("Unknown 16");

        unknown13.setSelected((entry.secondCondition & 4096) != 0);
        unknown14.setSelected((entry.secondCondition & 8192) != 0);
        unknown15.setSelected((entry.secondCondition & 16384) != 0);
        unknown16.setSelected((entry.secondCondition & 32768) != 0);

        unknown13.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.secondCondition |= 4096;
            }
            else {
                entry.secondCondition &= ~4096;
            }
        });
        unknown14.selectedProperty().addListener((obs, oldValue, newValue) -> { 
            if (newValue) {
                entry.secondCondition |= 8192;
            }
            else {
                entry.secondCondition &= ~8192;
            }
        });
        unknown15.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.secondCondition |= 16384;
            }
            else {
                entry.secondCondition &= ~16384;
            }
        });
        unknown16.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.secondCondition |= 32768;
            }
            else {
                entry.secondCondition &= ~32768;
            }
        });

        VBox Option4Box = new VBox(2, unknown13, unknown14, unknown15, unknown16);

        VBox borderContainerOption4=new VBox(Option4Box);
        borderContainerOption4.getStyleClass().add("titled-address-box");
        borderContainerOption4.setPadding(new Insets(12, 0, 0, 0));

        StackPane option4BoxStackPane = new StackPane(borderContainerOption4, option4Label);
        StackPane.setAlignment(option4Label, Pos.TOP_LEFT);
        //option 4

        HBox secondConditionHBox = new HBox(5, secondConditionLabel, option1BoxStackPane, option2BoxStackPane, option3BoxStackPane, option4BoxStackPane);
        secondConditionHBox.setAlignment(Pos.CENTER_LEFT);
        //second condition

        //bsa entry id
        Label bsaEntryIdLabel = new Label("BSA Entry ID");
        bsaEntryIdLabel.setPrefWidth(120);
        
        Spinner <Integer> bsaEntryIdSpinner = new Spinner<>(0, 65535, entry.bsaEntryId);
        bsaEntryIdSpinner.setEditable(true);
        bsaEntryIdSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                entry.bsaEntryId = newValue;
            }
        });

        HBox bsaEntryIdHBox = new HBox(15, bsaEntryIdLabel, bsaEntryIdSpinner);
        bsaEntryIdHBox.setAlignment(Pos.CENTER_LEFT);
        //bsa entry id

        //jump to bac entry id
        Label i06Label=new Label("Jump To BAC Entry Id?");
        i06Label.setPrefWidth(120);
        
        Spinner <Integer> i06Spinner = new Spinner<>(0, 65535, entry.i06);
        i06Spinner.setEditable(true);
        i06Spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                entry.i06 = newValue;
            }
        });

        HBox i06HBox = new HBox(15, i06Label, i06Spinner);
        i06HBox.setAlignment(Pos.CENTER_LEFT);
        //jump to bac entry id

        //bac condition
        Label bacConditionLabel = new Label("BAC Condition");
        bacConditionLabel.setPrefWidth(120);
        
        Spinner <Double> bacConditionSpinner = new Spinner<>(Float.MIN_VALUE, Float.MAX_VALUE, entry.bacCondition);
        bacConditionSpinner.setEditable(true);
        bacConditionSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                entry.bacCondition = newValue.floatValue();
            }
        });

        HBox bacConditionHBox = new HBox(15, bacConditionLabel, bacConditionSpinner);
        bacConditionHBox.setAlignment(Pos.CENTER_LEFT);
        //bac condition

        //f12
        Label f12Label = new Label("F_12");
        f12Label.setPrefWidth(60);
        
        TextField f12TextField=new TextField(String.valueOf(entry.f12));
        f12TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (f12TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.f12= Float.parseFloat(newText); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox f12HBox = new HBox(f12Label,f12TextField);
        f12HBox.setAlignment(Pos.CENTER_LEFT);
        //f12

        //entry passing
        VBox entryPassingVBox = new VBox(35, startTimeHBox, durationHBox, 
            firstConditionHBox, secondConditionHBox, 
            bsaEntryIdHBox, i06HBox, 
            bacConditionHBox
        );
        entryPassingVBox.setPadding(new Insets(20 ,0, 0, 16));

        Tab entryPassingTab = new Tab("Entry Passing", entryPassingVBox);
        entryPassingTab.setClosable(false);
        //entry passing

        //unknown
        VBox unknownVBox = new VBox(35, f12HBox);
        unknownVBox.setPadding(new Insets(20, 0, 0, 16));

        Tab unknownTab = new Tab("Unknown", unknownVBox);
        unknownTab.setClosable(false);
        //unknown

        tabPane.getTabs().addAll(entryPassingTab, unknownTab);
    }

    private void createBsaType1(BsaType1Entry entry) {
        //start time
        Label startTimeLabel = new Label("Start Time");
        startTimeLabel.setPrefWidth(120);

        Spinner <Integer> startTimeSpinner = new Spinner<>(0, 65535, entry.startTime);
        startTimeSpinner.setEditable(true);
        startTimeSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                entry.startTime = newValue;
            }
        });

        HBox startTimeHBox = new HBox(2, startTimeLabel, startTimeSpinner);
        startTimeHBox.setAlignment(Pos.CENTER_LEFT);
        //start time

        //duration
        Label durationLabel = new Label("Duration");
        durationLabel.setPrefWidth(120);
        
        Spinner <Integer> durationSpinner = new Spinner<>(0, 65535, entry.duration);
        durationSpinner.setEditable(true);
        durationSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                entry.duration = newValue;
            }
        });

        HBox durationHBox = new HBox(2, durationLabel, durationSpinner);
        durationHBox.setAlignment(Pos.CENTER_LEFT);
        //duration

        //motion flags
        Label motionFlagsLabel = new Label("Motion Flags");
        motionFlagsLabel.setPrefWidth(120);

        //option 1
        Label option1Label = new Label("Option 1");
        option1Label.getStyleClass().add("titled-address-label");

        CheckBox unknown1 = new CheckBox("Unknown 1");
        CheckBox unknown2 = new CheckBox("Unknown 2");
        CheckBox unknown3 = new CheckBox("Unknown 3");
        CheckBox unknown4 = new CheckBox("Unknown 4");

        unknown1.setSelected((entry.motionFlags & 1L) != 0);
        unknown2.setSelected((entry.motionFlags & 2L) != 0);
        unknown3.setSelected((entry.motionFlags & 4L) != 0);
        unknown4.setSelected((entry.motionFlags & 8L) != 0);

        unknown1.selectedProperty().addListener((obs, oldValue, newValue) -> {
             if (newValue) {
                entry.motionFlags |= 1L;
            }
            else {
                entry.motionFlags &= ~1L;
            }
        });
        unknown2.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.motionFlags |= 2L;
            }
            else {
                entry.motionFlags &= ~2L;
            }
        });
        unknown3.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.motionFlags |= 4L;
            }
            else {
                entry.motionFlags &= ~4L;
            }
        });
        unknown4.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.motionFlags |= 8L;
            }
            else {
                entry.motionFlags &= ~8L;
            }
        });

        VBox Option1Box = new VBox(2, unknown1, unknown2, unknown3, unknown4);

        VBox borderContainerOption1 = new VBox(Option1Box);
        borderContainerOption1.getStyleClass().add("titled-address-box");
        borderContainerOption1.setPadding(new Insets(12, 0, 0, 0));

        StackPane option1BoxStackPane = new StackPane(borderContainerOption1, option1Label);
        StackPane.setAlignment(option1Label, Pos.TOP_LEFT);
        option1Label.setTranslateY(-8); 
        option1Label.setTranslateX(10);
        //option 1

        //option 2
        Label option2Label = new Label("Option 2");
        option2Label.getStyleClass().add("titled-address-label");

        CheckBox unknown5 = new CheckBox("Unknown 5");
        CheckBox unknown6 = new CheckBox("Unknown 6");
        CheckBox unknown7 = new CheckBox("Unknown 7");
        CheckBox unknown8 = new CheckBox("Unknown 8");

        unknown5.setSelected((entry.motionFlags & 16L) != 0);
        unknown6.setSelected((entry.motionFlags & 32L) != 0);
        unknown7.setSelected((entry.motionFlags & 64L) != 0);
        unknown8.setSelected((entry.motionFlags & 128L) != 0);

        unknown5.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.motionFlags |= 16L;
            }
            else {
                entry.motionFlags &= ~16L;
            }
        });
        unknown6.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.motionFlags |= 32L;
            }
            else {
                entry.motionFlags &= ~32L;
            }
        });
        unknown7.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.motionFlags |= 64L;
            }
            else {
                entry.motionFlags &= ~64L;
            }
        });
        unknown8.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.motionFlags |= 128L;
            }
            else {
                entry.motionFlags &= ~128L;
            }
        });
        VBox Option2Box = new VBox(2, unknown5, unknown6, unknown7, unknown8);

        VBox borderContainerOption2 = new VBox(Option2Box);
        borderContainerOption2.getStyleClass().add("titled-address-box");
        borderContainerOption2.setPadding(new Insets(12, 0, 0, 0));

        StackPane option2BoxStackPane = new StackPane(borderContainerOption2, option2Label);
        StackPane.setAlignment(option2Label, Pos.TOP_LEFT);

        option2Label.setTranslateY(-8); 
        option2Label.setTranslateX(10);
        //option 2

        //option 3
        Label option3Label = new Label("Option 3");
        option3Label.getStyleClass().add("titled-address-label");

        CheckBox unknown9 = new CheckBox("Unknown 9");
        CheckBox unknown10 = new CheckBox("Unknown 10");
        CheckBox unknown11 = new CheckBox("Unknown 11");
        CheckBox unknown12 = new CheckBox("Unknown 12");

        unknown9.setSelected((entry.motionFlags & 256L) != 0);
        unknown10.setSelected((entry.motionFlags & 512L) != 0);
        unknown11.setSelected((entry.motionFlags & 1024L) != 0);
        unknown12.setSelected((entry.motionFlags & 2048L) != 0);

        unknown9.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.motionFlags |= 256L;
            }
            else {
                entry.motionFlags &= ~256L;
            }
        });
        unknown10.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.motionFlags |= 512L;
            }
            else {
                entry.motionFlags &= ~512L;
            }
        });
        unknown11.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.motionFlags |= 1024L;
            }
            else {
                entry.motionFlags &= ~1024L;
            }
        });
        unknown12.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.motionFlags |= 2048L;
            }
            else {
                entry.motionFlags &= ~2048L;
            }
        });
        VBox Option3Box = new VBox(2, unknown9, unknown10, unknown11, unknown12);

        VBox borderContainerOption3 = new VBox(Option3Box);
        borderContainerOption3.getStyleClass().add("titled-address-box");
        borderContainerOption3.setPadding(new Insets(12, 0, 0, 0));

        StackPane option3BoxStackPane = new StackPane(borderContainerOption3,option3Label);
        StackPane.setAlignment(option3Label, Pos.TOP_LEFT);
        option3Label.setTranslateY(-8); 
        option3Label.setTranslateX(10);
        //option 3

        //option 4
        Label option4Label = new Label("Option 4");
        option4Label.getStyleClass().add("titled-address-label");

        CheckBox unknown13 = new CheckBox("Unknown 13");
        CheckBox unknown14 = new CheckBox("Unknown 14");
        CheckBox unknown15 = new CheckBox("Unknown 15");
        CheckBox unknown16 = new CheckBox("Unknown 16");

        unknown13.setSelected((entry.motionFlags & 4096L) != 0);
        unknown14.setSelected((entry.motionFlags & 8192L) != 0);
        unknown15.setSelected((entry.motionFlags & 16384L) != 0);
        unknown16.setSelected((entry.motionFlags & 32768L) != 0);

        unknown13.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.motionFlags |= 4096L;
            }
            else {
                entry.motionFlags &= ~4096L;
            }
        });
        unknown14.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.motionFlags |= 8192L;
            }
            else {
                entry.motionFlags &= ~8192L;
            }
        });
        unknown15.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.motionFlags |= 16384L;
            }
            else {
                entry.motionFlags &= ~16384L;
            }
        });
        unknown16.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.motionFlags |= 32768L;
            }
            else {
                entry.motionFlags &= ~32768L;
            }
        });
        VBox Option4Box = new VBox(2, unknown13, unknown14, unknown15, unknown16);

        VBox borderContainerOption4 = new VBox(Option4Box);
        borderContainerOption4.getStyleClass().add("titled-address-box");
        borderContainerOption4.setPadding(new Insets(12, 0, 0, 0));

        StackPane option4BoxStackPane = new StackPane(borderContainerOption4, option4Label);
        StackPane.setAlignment(option4Label, Pos.TOP_LEFT);
        option4Label.setTranslateY(-8); 
        option4Label.setTranslateX(10);
        //option 4

        //option 5
        Label option5Label = new Label("Option 5");
        option5Label.getStyleClass().add("titled-address-label");

        CheckBox unknown17 = new CheckBox("Unknown 17");
        CheckBox opponentTracking = new CheckBox("Opponent Tracking");
        CheckBox unknown19 = new CheckBox("Unknown 18");
        CheckBox unknown20 = new CheckBox("Unknown 20");

        unknown17.setSelected((entry.motionFlags & 65536L) != 0);
        opponentTracking.setSelected((entry.motionFlags & 131072L) != 0);
        unknown19.setSelected((entry.motionFlags & 262144L) != 0);
        unknown20.setSelected((entry.motionFlags & 524288L) != 0);

        unknown17.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.motionFlags |= 65536L;
            }
            else {
                entry.motionFlags &= ~65536L;
            }
        });
        opponentTracking.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.motionFlags |= 131072L;
            }
            else {
                entry.motionFlags &= ~131072L;
            }
        });
        unknown19.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.motionFlags |= 262144L;
            }
            else {
                entry.motionFlags &= ~262144L;
            }
        });
        unknown20.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.motionFlags |= 524288L;
            }
            else {
                entry.motionFlags &= ~524288L;
            }
        });
        VBox Option5Box = new VBox(2, unknown17, opponentTracking, unknown19, unknown20);

        VBox borderContainerOption5 = new VBox(Option5Box);
        borderContainerOption5.getStyleClass().add("titled-address-box");
        borderContainerOption5.setPadding(new Insets(12,0,0,0));

        StackPane option5BoxStackPane = new StackPane(borderContainerOption5,option5Label);
        StackPane.setAlignment(option5Label, Pos.TOP_LEFT);
        option5Label.setTranslateY(-8); 
        option5Label.setTranslateX(10);
        //option 5

        //option 6
        Label option6Label = new Label("Option 6");
        option6Label.getStyleClass().add("titled-address-label");

        CheckBox unknown21 = new CheckBox("Unknown 21");
        CheckBox unknown22 = new CheckBox("Free Movement?");
        CheckBox unknown23 = new CheckBox("Unknown 23");
        CheckBox unknown24 = new CheckBox("Unknown 24");

        unknown21.setSelected((entry.motionFlags & 1048576L) != 0);
        unknown22.setSelected((entry.motionFlags & 2097152L) != 0);
        unknown23.setSelected((entry.motionFlags & 4194304L) != 0);
        unknown24.setSelected((entry.motionFlags & 8388608L) != 0);

        unknown21.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.motionFlags |= 1048576L;
            }
            else {
                entry.motionFlags &= ~1048576L;
            }
        });
        unknown22.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.motionFlags |= 2097152L;
            }
            else {
                entry.motionFlags &= ~2097152L;
            }
        });
        unknown23.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.motionFlags |= 4194304L;
            }
            else {
                entry.motionFlags &= ~4194304L;
            }
        });
        unknown24.selectedProperty().addListener((obs, oldValue, newValue)-> {
            if (newValue) {
                entry.motionFlags |= 8388608L;
            }
            else {
                entry.motionFlags &= ~8388608L;
            }
        });
        VBox Option6Box = new VBox(2, unknown21, unknown22, unknown23, unknown24);

        VBox borderContainerOption6 = new VBox(Option6Box);
        borderContainerOption6.getStyleClass().add("titled-address-box");
        borderContainerOption6.setPadding(new Insets(12, 0, 0, 0));

        StackPane option6BoxStackPane = new StackPane(borderContainerOption6,option6Label);
        StackPane.setAlignment(option6Label, Pos.TOP_LEFT);
        option6Label.setTranslateY(-8); 
        option6Label.setTranslateX(10);
        //option 6

        //option 7
        Label option7Label = new Label("Option 7");
        option7Label.getStyleClass().add("titled-address-label");

        CheckBox unknown25 = new CheckBox("Unknown 25");
        CheckBox unknown26 = new CheckBox("Unknown 26");
        CheckBox unknown27 = new CheckBox("Unknown 27");
        CheckBox unknown28 = new CheckBox("Unknown 28");

        unknown25.setSelected((entry.motionFlags & 16777216L) != 0);
        unknown26.setSelected((entry.motionFlags & 33554432L) != 0);
        unknown27.setSelected((entry.motionFlags & 67108864L) != 0);
        unknown28.setSelected((entry.motionFlags & 134217728L) != 0);

        unknown25.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.motionFlags |= 16777216L;
            }
            else {
                entry.motionFlags &= ~16777216L;
            }
        });
        unknown26.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.motionFlags |= 33554432L;
            }
            else {
                entry.motionFlags &= ~33554432L;
            }
        });
        unknown27.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.motionFlags |= 67108864L;
            }
            else {
                entry.motionFlags &= ~67108864L;
            }
        });
        unknown28.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.motionFlags |= 134217728L;
            }
            else {
                entry.motionFlags &= ~134217728L;
            }
        });
        VBox Option7Box = new VBox(2, unknown25, unknown26, unknown27, unknown28);

        VBox borderContainerOption7 = new VBox(Option7Box);
        borderContainerOption7.getStyleClass().add("titled-address-box");
        borderContainerOption7.setPadding(new Insets(12, 0, 0,  0));

        StackPane option7BoxStackPane = new StackPane(borderContainerOption7, option7Label);
        StackPane.setAlignment(option7Label, Pos.TOP_LEFT);
        option7Label.setTranslateY(-8); 
        option7Label.setTranslateX(10);
        //option 7

        //option 8
        Label option8Label = new Label("Option 8");
        option8Label.getStyleClass().add("titled-address-label");

        CheckBox unknown29 = new CheckBox("Unknown 29");
        CheckBox unknown30 = new CheckBox("Unknown 30");
        CheckBox unknown31 = new CheckBox("Unknown 31");
        CheckBox unknown32 = new CheckBox("Unknown 32");

        unknown29.setSelected((entry.motionFlags & 268435456L) != 0);
        unknown30.setSelected((entry.motionFlags & 536870912L) != 0);
        unknown31.setSelected((entry.motionFlags & 1073741824L) != 0);
        unknown32.setSelected((entry.motionFlags & 2147483648L) != 0);

        unknown29.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) { 
                entry.motionFlags |= 268435456L;
            }
            else {
                entry.motionFlags &= ~268435456L;
            }
        });
        unknown30.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.motionFlags |= 536870912L;
            }
            else {
                entry.motionFlags &= ~536870912L;
            }
        });
        unknown31.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.motionFlags |= 1073741824L;
            }
            else {
                entry.motionFlags &= ~1073741824L;
            }
        });
        unknown32.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                entry.motionFlags |= 2147483648L;
            }
            else {
                entry.motionFlags &= ~2147483648L;
            }
        });
        VBox Option8Box = new VBox(2, unknown29, unknown30, unknown31, unknown32);

        VBox borderContainerOption8 = new VBox(Option8Box);
        borderContainerOption8.getStyleClass().add("titled-address-box");
        borderContainerOption8.setPadding(new Insets(12, 0, 0, 0));

        StackPane option8BoxStackPane = new StackPane(borderContainerOption8, option8Label);
        StackPane.setAlignment(option8Label, Pos.TOP_LEFT);
        option8Label.setTranslateY(-8); 
        option8Label.setTranslateX(10);
        //option 8

        GridPane motionFlagsGridPane = new GridPane(10, 10);
        motionFlagsGridPane.add(option1BoxStackPane, 0, 0);
        motionFlagsGridPane.add(option2BoxStackPane, 1, 0);
        motionFlagsGridPane.add(option3BoxStackPane, 2, 0);
        motionFlagsGridPane.add(option4BoxStackPane, 3, 0);
        motionFlagsGridPane.add(option5BoxStackPane, 0, 1);
        motionFlagsGridPane.add(option6BoxStackPane, 1, 1);
        motionFlagsGridPane.add(option7BoxStackPane, 2, 1);
        motionFlagsGridPane.add(option8BoxStackPane, 3, 1);

        HBox motionFlagsHBox = new HBox(2, motionFlagsLabel, motionFlagsGridPane);
        motionFlagsHBox.setAlignment(Pos.CENTER_LEFT);
        //motion flags

        //speed x
        Label speedXLabel = new Label("Speed X");
        speedXLabel.setPrefWidth(120);
        
        Spinner <Double> speedXSpinner = new Spinner<>(Float.MIN_VALUE, Float.MAX_VALUE, entry.speedX);
        speedXSpinner.setEditable(true);
        speedXSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                entry.speedX = newValue.floatValue();
            }
        });

        HBox speedXHBox = new HBox(2, speedXLabel, speedXSpinner);
        speedXHBox.setAlignment(Pos.CENTER_LEFT);
        //speed x

        //speed y
        Label speedYLabel = new Label("Speed Y");
        speedYLabel.setPrefWidth(120);
        
        Spinner <Double> speedYSpinner = new Spinner<>(Float.MIN_VALUE, Float.MAX_VALUE, entry.speedY);
        speedYSpinner.setEditable(true);
        speedYSpinner.valueProperty().addListener((obs ,oldValue, newValue) -> {
            if (newValue != null) {
                entry.speedY = newValue.floatValue();
            }
        });

        HBox speedYHBox = new HBox(2, speedYLabel, speedYSpinner);
        speedYHBox.setAlignment(Pos.CENTER_LEFT);
        //speed y

        //speed z
        Label speedZLabel = new Label("Speed Z");
        speedZLabel.setPrefWidth(120);
        
        Spinner <Double> speedZSpinner = new Spinner<>(Float.MIN_VALUE, Float.MAX_VALUE, entry.speedZ);
        speedZSpinner.setEditable(true);
        speedZSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                entry.speedZ = newValue.floatValue();
            }
        });

        HBox speedZHBox = new HBox(2, speedZLabel, speedZSpinner);
        speedZHBox.setAlignment(Pos.CENTER_LEFT);
        //speed z

        //f16
        Label f16Label = new Label("F_16");
        f16Label.setPrefWidth(60);
        
        TextField f16TextField = new TextField(String.valueOf(entry.f16));
        f16TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (f16TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.f16 = Float.parseFloat(newText);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox f16HBox = new HBox(f16Label, f16TextField);
        f16HBox.setAlignment(Pos.CENTER_LEFT);
        //f16

        //acceleration x
        Label accelerationXLabel = new Label("Acceleration X");
        accelerationXLabel.setPrefWidth(120);
        
        Spinner <Double> accelerationXSpinner = new Spinner<>(Float.MIN_VALUE, Float.MAX_VALUE, entry.accelerationX);
        accelerationXSpinner.setEditable(true);
        accelerationXSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                entry.accelerationX = newValue.floatValue();
            }
        }); 

        HBox accelerationXHBox = new HBox(2, accelerationXLabel, accelerationXSpinner);
        accelerationXHBox.setAlignment(Pos.CENTER_LEFT);
        //acceleration x

        //acceleration y
        
        Label accelerationYLabel = new Label("Acceleration Y");
        accelerationYLabel.setPrefWidth(120);
        
        Spinner <Double> accelerationYSpinner = new Spinner<>(Float.MIN_VALUE, Float.MAX_VALUE, entry.accelerationY);
        accelerationYSpinner.setEditable(true);
        accelerationYSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                entry.speedY = newValue.floatValue();
            }
        });

        HBox accelerationYHBox = new HBox(2, accelerationYLabel, accelerationYSpinner);
        accelerationYHBox.setAlignment(Pos.CENTER_LEFT);
        //acceleration y

        //acceleration z
        Label accelerationZLabel = new Label("Acceleration Z");
        accelerationZLabel.setPrefWidth(120);
        
        Spinner <Double> accelerationZSpinner = new Spinner<>(Float.MIN_VALUE, Float.MAX_VALUE, entry.accelerationZ);
        accelerationZSpinner.setEditable(true);
        accelerationZSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                entry.accelerationZ = newValue.floatValue();
            }
        });

        HBox accelerationZHBox = new HBox(2, accelerationZLabel, accelerationZSpinner);
        accelerationZHBox.setAlignment(Pos.CENTER_LEFT);
        //acceleration z

        //falloff strength
        Label falloffStrengthLabel = new Label("Falloff Strength");
        falloffStrengthLabel.setPrefWidth(120);
        
        Spinner <Double> falloffStrengthSpinner = new Spinner<>(Float.MIN_VALUE, Float.MAX_VALUE, entry.fallofStrength);
        falloffStrengthSpinner.setEditable(true);
        falloffStrengthSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                entry.fallofStrength = newValue.floatValue();
            }
        });

        HBox falloffStrengthHBox = new HBox(2, falloffStrengthLabel, falloffStrengthSpinner);
        falloffStrengthHBox.setAlignment(Pos.CENTER_LEFT);
        //falloff strength

        //spread direction x
        Label spreadDirectionXLabel = new Label("Spread Direction X");
        spreadDirectionXLabel.setPrefWidth(120);
        
        Spinner <Double> spreadDirectionXSpinner = new Spinner<>(Float.MIN_VALUE, Float.MAX_VALUE, entry.spreadDirectionX);
        spreadDirectionXSpinner.setEditable(true);
        spreadDirectionXSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                entry.spreadDirectionX = newValue.floatValue();
            }
        });

        HBox spreadDirectionXHBox = new HBox(2, spreadDirectionXLabel, spreadDirectionXSpinner);
        spreadDirectionXHBox.setAlignment(Pos.CENTER_LEFT);
        //spread direction x
        
        //spread direction y
        Label spreadDirectionYLabel = new Label("Spread Direction Y");
        spreadDirectionYLabel.setPrefWidth(120);
        
        Spinner <Double> spreadDirectionYSpinner = new Spinner<>(Float.MIN_VALUE, Float.MAX_VALUE, entry.spreadDirectionY);
        spreadDirectionYSpinner.setEditable(true);
        spreadDirectionYSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                entry.spreadDirectionY = newValue.floatValue();
            }
        });

        HBox spreadDirectionYHBox = new HBox(2, spreadDirectionYLabel, spreadDirectionYSpinner);
        spreadDirectionYHBox.setAlignment(Pos.CENTER_LEFT);
        //spread direction y

        //spread direction z
        Label spreadDirectionZLabel = new Label("Spread Direction Z");
        spreadDirectionZLabel.setPrefWidth(120);
        
        Spinner <Double> spreadDirectionZSpinner = new Spinner<>(Float.MIN_VALUE, Float.MAX_VALUE, entry.spreadDirectionZ);
        spreadDirectionZSpinner.setEditable(true);
        spreadDirectionZSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                entry.spreadDirectionZ = newValue.floatValue();
            }
        });

        HBox spreadDirectionZHBox = new HBox(2, spreadDirectionZLabel, spreadDirectionZSpinner);
        spreadDirectionZHBox.setAlignment(Pos.CENTER_LEFT);
        //spread direction z
        
        //movement
        VBox movementVBox = new VBox(35, 
            startTimeHBox, durationHBox, 
            motionFlagsHBox, speedXHBox, 
            speedYHBox, speedZHBox, 
            accelerationXHBox, accelerationYHBox, 
            accelerationZHBox, falloffStrengthHBox, 
            spreadDirectionXHBox, spreadDirectionYHBox, 
            spreadDirectionZHBox
        );
        movementVBox.setPadding(new Insets(20, 0, 20, 16));

        Tab movementTab = new Tab("Movement", new ScrollPane(movementVBox));

        movementTab.setClosable(false);
        //movement

        //unknown
        VBox unknownVBox = new VBox(30, f16HBox);
        unknownVBox.setPadding(new Insets(20, 0, 0, 16));

        Tab unknownTab = new Tab("Unknown", unknownVBox);
        unknownTab.setClosable(false);
        //unknown

        tabPane.getTabs().addAll(movementTab, unknownTab);
    }

    private void createBsaType2(BsaType2Entry entry) {
        //start time
        Label startTimeLabel = new Label("Start Time");
        startTimeLabel.setPrefWidth(80);

        Spinner <Integer> startTimeSpinner = new Spinner<>(0, 65535, entry.startTime);
        startTimeSpinner.setEditable(true); 
        startTimeSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                entry.startTime = newValue;
            }
        });

        HBox startTimeHBox = new HBox(startTimeLabel, startTimeSpinner);
        startTimeHBox.setAlignment(Pos.CENTER_LEFT);
        //start time

        //duration
        Label durationLabel = new Label("Duration");
        durationLabel.setPrefWidth(80);
        
        Spinner <Integer> durationSpinner = new Spinner<>(0, 65535, entry.duration);
        durationSpinner.setEditable(true);
        durationSpinner.valueProperty().addListener((obs, oldValue ,newValue) -> {
            if (newValue != null) {
                entry.duration = newValue;
            }
        });

        HBox durationHBox = new HBox(durationLabel, durationSpinner);
        durationHBox.setAlignment(Pos.CENTER_LEFT);
        //duration

        //i00
        Label i00Label = new Label("I_00");
        i00Label.setPrefWidth(80);

        TextField i00TextField = new TextField(String.valueOf(entry.i00));
        i00TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (i00TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.i00 = Short.parseShort(newText); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox i00HBox = new HBox(i00Label, i00TextField);
        i00HBox.setAlignment(Pos.CENTER_LEFT);
        //i00

        //i02
        Label i02Label = new Label("I_02");
        i02Label.setPrefWidth(80);

        TextField i02TextField = new TextField(String.valueOf(entry.i02));
        i02TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (i02TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.i02 = Short.parseShort(newText); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox i02HBox = new HBox(i02Label, i02TextField);
        i02HBox.setAlignment(Pos.CENTER_LEFT);
        //i02

        //i04
        Label i04Label = new Label("I_04");
        i04Label.setPrefWidth(80);

        TextField i04TextField = new TextField(String.valueOf(entry.i04));
        i04TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (i04TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.i04 = Short.parseShort(newText); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox i04HBox = new HBox(i04Label, i04TextField);
        i04HBox.setAlignment(Pos.CENTER_LEFT);
        //i04

        //i06
        Label i06Label = new Label("I_06");
        i06Label.setPrefWidth(80);

        TextField i06TextField = new TextField(String.valueOf(entry.i06));
        i06TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (i06TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.i06 = Short.parseShort(newText); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox i06HBox = new HBox(i06Label, i06TextField);
        i06HBox.setAlignment(Pos.CENTER_LEFT);
        //i06

        //type 2
        VBox type2VBox = new VBox(30,startTimeHBox, durationHBox, i00HBox, i02HBox, i04HBox, i06HBox);
        type2VBox.setPadding(new Insets(20, 0, 0, 16));

        Tab type2Tab = new Tab("Type 2", type2VBox);
        type2Tab.setClosable(false);
        //type 2

        tabPane.getTabs().add(type2Tab);
    }

    private void createBsaType3(BsaType3Entry entry) {
        //start time
        Label startTimeLabel = new Label("Start Time");
        startTimeLabel.setPrefWidth(120);

        Spinner <Integer> startTimeSpinner = new Spinner<>(0, 65535, entry.startTime);
        startTimeSpinner.setEditable(true);
        startTimeSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                entry.startTime = newValue;
            }
        });

        HBox startTimeHBox = new HBox(15, startTimeLabel, startTimeSpinner);
        startTimeHBox.setAlignment(Pos.CENTER_LEFT);
        //start time

        //duration
        Label durationLabel = new Label("Duration");
        durationLabel.setPrefWidth(120);
        
        Spinner <Integer> durationSpinner = new Spinner<>(0, 65535, entry.duration);
        durationSpinner.setEditable(true);
        durationSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                entry.duration = newValue;
            }
        });

        HBox durationHBox = new HBox(15, durationLabel, durationSpinner);
        durationHBox.setAlignment(Pos.CENTER_LEFT);
        //duration

        //matrix flags
        CheckBox matrixFlagsCheckBox = new CheckBox("Enable Min and Max Bounds");
        matrixFlagsCheckBox.setSelected(entry.matrixFlag);
        matrixFlagsCheckBox.setStyle("-fx-border-color: black; " + "-fx-border-width: 1px; " + "-fx-padding: 10px;");
        matrixFlagsCheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
            try {
                entry.matrixFlag = newValue;
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });
        //matrix flags

        //i04
        Label i04Label = new Label("I_04");
        i04Label.setPrefWidth(60);

        TextField i04TextField = new TextField(String.valueOf(entry.i04));
        i04TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (i04TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.i04 = Integer.parseInt(newText); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox i04HBox = new HBox(i04Label, i04TextField);
        i04HBox.setAlignment(Pos.CENTER_LEFT);
        //i04

        //i06
        Label impactPropertiesLabel = new Label("I_06"); 
        impactPropertiesLabel.setPrefWidth(120);

        //i06_a
        Label A = new Label("A");

        Spinner <Integer> i06_aSpinner  = new Spinner<>(0, 15, entry.i06_a);
        i06_aSpinner.setEditable(true);
        i06_aSpinner.setPrefWidth(60);
        i06_aSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                entry.i06_a = newValue.byteValue();
            }
        });
        //i06_a

        //i06_b
        Label B = new Label("B");

        Spinner <Integer> i06_bSpinner  = new Spinner<>(0, 15, entry.i06_b);
        i06_bSpinner.setEditable(true);
        i06_bSpinner.setPrefWidth(60);
        i06_bSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                entry.i06_b = newValue.byteValue();
            }
        });
        //i06_b

        //i06_c
        Label C = new Label("C");

        Spinner <Integer> i06_cSpinner  = new Spinner<>(0, 15, entry.i06_c);
        i06_cSpinner.setEditable(true);
        i06_cSpinner.setPrefWidth(60);
        i06_cSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                entry.i06_c = newValue.byteValue();
            }
        });
        //i06_c

        //i06_b
        Label D = new Label("D");

        Spinner <Integer> i06_dSpinner  = new Spinner<>(0, 15, entry.i06_d);
        i06_dSpinner.setEditable(true);
        i06_dSpinner.setPrefWidth(60);
        i06_dSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                entry.i06_d = newValue.byteValue();
            }
        });
        //i06_b

        HBox i06HBox = new HBox(15, impactPropertiesLabel, A , i06_aSpinner, B, i06_bSpinner, C ,i06_cSpinner, D, i06_dSpinner);
        i06HBox.setAlignment(Pos.CENTER_LEFT);
        //i06

        //position x
        Label speedXLabel = new Label("Position X");
        speedXLabel.setPrefWidth(120);
        
        Spinner <Double> positionXSpinner = new Spinner<>(Float.MIN_VALUE, Float.MAX_VALUE, entry.positionX);
        positionXSpinner.setEditable(true);
        positionXSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                entry.positionX = newValue.floatValue();
            }
        });

        HBox positionXHBox = new HBox(15, speedXLabel, positionXSpinner);
        positionXHBox.setAlignment(Pos.CENTER_LEFT);
        //position x

        //position y
        Label speedYLabel = new Label("Position Y");
        speedYLabel.setPrefWidth(120);
        
        Spinner <Double> positionYSpinner = new Spinner<>(Float.MIN_VALUE, Float.MAX_VALUE, entry.positionY);
        positionYSpinner.setEditable(true);
        positionYSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                entry.positionY = newValue.floatValue();
            }
        });

        HBox positionYHBox = new HBox(15, speedYLabel, positionYSpinner);
        positionYHBox.setAlignment(Pos.CENTER_LEFT);
        //position y

        //position z
        Label speedZLabel = new Label("Position Z");
        speedZLabel.setPrefWidth(120);
        
        Spinner <Double> positionZSpinner = new Spinner<>(Float.MIN_VALUE, Float.MAX_VALUE, entry.positionZ);
        positionZSpinner.setEditable(true);
        positionZSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                entry.positionZ = newValue.floatValue();
            }
        });

        HBox positionZHBox = new HBox(15, speedZLabel, positionZSpinner);
        positionZHBox.setAlignment(Pos.CENTER_LEFT);
        //position z

        //hitbox scale
        Label hitboxScaleLabel = new Label("Hitbox Scale");
        hitboxScaleLabel.setPrefWidth(120);
        
        Spinner <Double> hitboxScaleSpinner = new Spinner<>(Float.MIN_VALUE, Float.MAX_VALUE, entry.hitboxScale);
        hitboxScaleSpinner.setEditable(true);
        hitboxScaleSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                entry.hitboxScale = newValue.floatValue();
            }
        });

        HBox hitboxScaleHBox = new HBox(15, hitboxScaleLabel, hitboxScaleSpinner);
        hitboxScaleHBox.setAlignment(Pos.CENTER_LEFT);
        //hitbox scale

        //maximum box x
        Label maximumBoxXLabel = new Label("Maximum Box X");
        maximumBoxXLabel.setPrefWidth(120);
        
        Spinner <Double> maximumBoxXSpinner = new Spinner<>(Float.MIN_VALUE, Float.MAX_VALUE, entry.maximumX);
        maximumBoxXSpinner.setEditable(true); 
        maximumBoxXSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                entry.maximumX = newValue.floatValue();
            }
        });

        HBox maximumBoxXHBox = new HBox(maximumBoxXLabel, maximumBoxXSpinner);
        maximumBoxXHBox.setAlignment(Pos.CENTER_LEFT);
        //maximum box x

        //maximum box y
        Label maximumBoxYLabel = new Label("Maximum Box Y");
        maximumBoxYLabel.setPrefWidth(120);
        
        Spinner <Double> maximumBoxYSpinner = new Spinner<>(Float.MIN_VALUE, Float.MAX_VALUE, entry.maximumY);
        maximumBoxYSpinner.setEditable(true);
        maximumBoxYSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                entry.maximumY = newValue.floatValue();
            }
        });

        HBox maximumBoxYHBox = new HBox(maximumBoxYLabel, maximumBoxYSpinner);
        maximumBoxYHBox.setAlignment(Pos.CENTER_LEFT);
        //maximum box y

        //maximum box z
        Label maximumBoxZLabel = new Label("Maximum Box Z");
        maximumBoxZLabel.setPrefWidth(120);
        
        Spinner <Double> maximumBoxZSpinner = new Spinner<>(Float.MIN_VALUE, Float.MAX_VALUE, entry.maximumZ);
        maximumBoxZSpinner.setEditable(true);
        maximumBoxZSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                entry.maximumZ = newValue.floatValue();
            }
        });

        HBox maximumBoxZHBox = new HBox(maximumBoxZLabel, maximumBoxZSpinner);
        maximumBoxZHBox.setAlignment(Pos.CENTER_LEFT);
        //maximum box z

        //minimum box x
        Label minimumBoxXLabel = new Label("Minimum Box X");
        minimumBoxXLabel.setPrefWidth(120);
        
        Spinner <Double> minimumBoxXSpinner = new Spinner<>(Float.MIN_VALUE, Float.MAX_VALUE, entry.minimumX);
        minimumBoxXSpinner.setEditable(true);
        minimumBoxXSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                entry.minimumX = newValue.floatValue();
            }
        });

        HBox minimumBoxXHBox = new HBox(minimumBoxXLabel, minimumBoxXSpinner);
        minimumBoxXHBox.setAlignment(Pos.CENTER_LEFT);
        //minimum box x

        //maximum box y
        Label minimumBoxYLabel = new Label("Minimum Box Y");
        minimumBoxYLabel.setPrefWidth(120);
        
        Spinner <Double> minimumBoxYSpinner = new Spinner<>(Float.MIN_VALUE, Float.MAX_VALUE, entry.minimumY);
        minimumBoxYSpinner.setEditable(true);
        minimumBoxYSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                entry.minimumY = newValue.floatValue();
            }
        });

        HBox minimumBoxYHBox = new HBox(minimumBoxYLabel, minimumBoxYSpinner);
        minimumBoxYHBox.setAlignment(Pos.CENTER_LEFT);
        //maximum box y

        //maximum box z
        Label minimumBoxZLabel = new Label("Minimum Box Z");
        minimumBoxZLabel.setPrefWidth(120);
        
        Spinner <Double> minimumBoxZSpinner = new Spinner<>(Float.MIN_VALUE, Float.MAX_VALUE, entry.minimumZ);
        minimumBoxZSpinner.setEditable(true);
        minimumBoxZSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                entry.minimumZ = newValue.floatValue();
            }
        });

        HBox minimumBoxZHBox = new HBox(minimumBoxZLabel, minimumBoxZSpinner);
        minimumBoxZHBox.setAlignment(Pos.CENTER_LEFT);
        //maximum box z

        //hit amount
        Label hitAmountLabel = new Label("Hit Amount");
        hitAmountLabel.setPrefWidth(120);

        Spinner <Integer> hitAmountSpinner = new Spinner<>(0, 65535, entry.hitAmount);
        hitAmountSpinner.setEditable(true);
        hitAmountSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                entry.hitAmount = newValue;
            }
        });

        HBox hitAmountHBox = new HBox(15, hitAmountLabel, hitAmountSpinner);
        hitAmountHBox.setAlignment(Pos.CENTER_LEFT);
        //hit amount

        //hitbox lifetime
        Label hitboxLifetimeLabel = new Label("Hitbox Lifetime");
        hitboxLifetimeLabel.setPrefWidth(120);

        Spinner <Integer> hitboxLifetimeSpinner = new Spinner<>(0, 65535, entry.hitboxLifetime);
        hitboxLifetimeSpinner.setEditable(true);
        hitboxLifetimeSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                entry.hitboxLifetime = newValue;
            }
        });

        HBox hitboxLifetimeHBox = new HBox(15, hitboxLifetimeLabel, hitboxLifetimeSpinner);
        hitboxLifetimeHBox.setAlignment(Pos.CENTER_LEFT);
        //hitbox lifetime

        //i52
        Label i52Label = new Label("I_52");
        i52Label.setPrefWidth(60);

        TextField i52TextField = new TextField(String.valueOf(entry.i52));
        i52TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (i52TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.i52 = Integer.parseInt(newText); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox i52HBox = new HBox(i52Label, i52TextField);
        i52HBox.setAlignment(Pos.CENTER_LEFT);
        //i52

        //i54
        Label i54Label=new Label("I_54");
        i54Label.setPrefWidth(60);

        TextField i54TextField=new TextField(String.valueOf(entry.i54));
        i54TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (i54TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.i54 = Integer.parseInt(newText); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox i54HBox = new HBox(i54Label, i54TextField);
        i54HBox.setAlignment(Pos.CENTER_LEFT);
        //i54

        //i56
        Label i56Label = new Label("I_56");
        i56Label.setPrefWidth(60);

        TextField i56TextField = new TextField(String.valueOf(entry.i56));
        i56TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (i56TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.i56 = Integer.parseInt(newText); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox i56HBox = new HBox(i56Label, i56TextField);
        i56HBox.setAlignment(Pos.CENTER_LEFT);
        //i56

        //first hit
        Label firstHitLabel = new Label("BDM ID First Hit");
        firstHitLabel.setPrefWidth(120);

        Spinner <Integer> firstHitSpinner = new Spinner<>(0, 65535, entry.firstHit);
        firstHitSpinner.setEditable(true);
        firstHitSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                entry.firstHit = newValue;
            }
        });

        HBox firstHitHBox = new HBox(15, firstHitLabel, firstHitSpinner);
        firstHitHBox.setAlignment(Pos.CENTER_LEFT);
        //first hit

        //multiple hits
        Label multipleHitsLabel = new Label("BDM ID Multiple Hits");
        multipleHitsLabel.setPrefWidth(120);

        Spinner <Integer> multipleHitsSpinner = new Spinner<>(0, 65535, entry.multipleHits);
        multipleHitsSpinner.setEditable(true);
        multipleHitsSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                entry.multipleHits= newValue;
            }
        });

        HBox multipleHitsHBox = new HBox(15, multipleHitsLabel, multipleHitsSpinner);
        multipleHitsHBox.setAlignment(Pos.CENTER_LEFT);
        //multiple hits

        //last hit
        Label lastHitLabel = new Label("BDM ID Last Hit");
        lastHitLabel.setPrefWidth(120);

        Spinner <Integer> lastHitSpinner = new Spinner<>(0, 65535, entry.lastHit);
        lastHitSpinner.setEditable(true);
        lastHitSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                entry.lastHit = newValue;
            }
        });

        HBox lastHitHBox = new HBox(15, lastHitLabel, lastHitSpinner);
        lastHitHBox.setAlignment(Pos.CENTER_LEFT);
        //first hit

        //hitbox
        VBox hitboxVBox = new VBox(40, 
            startTimeHBox, durationHBox, 
            i06HBox, positionXHBox, positionYHBox, 
            positionZHBox, hitboxScaleHBox, 
            hitAmountHBox, hitboxLifetimeHBox, 
            firstHitHBox, multipleHitsHBox, 
            lastHitHBox
        );
        hitboxVBox.setPadding(new Insets(20, 0, 0, 16));

        Tab hitboxTab = new Tab("Hitbox", hitboxVBox);
        hitboxTab.setClosable(false);
        //hitbox

        //matrix
        VBox matrixVBox = new VBox(30, 
            matrixFlagsCheckBox, maximumBoxXHBox, 
            maximumBoxYHBox, maximumBoxZHBox, 
            minimumBoxXHBox, minimumBoxYHBox, 
            minimumBoxZHBox
        );
        matrixVBox.setPadding(new Insets(20, 0, 0, 16));

        Tab matrixTab = new Tab("Matrix", matrixVBox);
        matrixTab.setClosable(false);
        //matrix

        //unknown
        VBox unknownVBox = new VBox(30, i04HBox, i52HBox, i54HBox, i56HBox);
        unknownVBox.setPadding(new Insets(20, 0, 0, 16));

        Tab unknownTab = new Tab("Unknown", unknownVBox);
        unknownTab.setClosable(false);
        //unknown
        
        tabPane.getTabs().addAll(hitboxTab, matrixTab, unknownTab);
    }

    private void createBsaType4(BsaType4Entry entry) {
        //start time
        Label startTimeLabel = new Label("Start Time");
        startTimeLabel.setPrefWidth(80);
        
        Spinner <Integer> startTimeSpinner = new Spinner<>(0, 65535, entry.startTime);
        startTimeSpinner.setEditable(true);
        startTimeSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                entry.startTime = newValue;
            }
        });

        HBox startTimeHBox = new HBox(startTimeLabel, startTimeSpinner);
        startTimeHBox.setAlignment(Pos.CENTER_LEFT);
        //start time

        //duration
        Label durationLabel = new Label("Duration");
        durationLabel.setPrefWidth(80);
        
        Spinner <Integer> durationSpinner = new Spinner<>(0, 65535, entry.duration);
        durationSpinner.setEditable(true);
        durationSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                entry.duration = newValue;
            }
        });

        HBox durationHBox = new HBox(durationLabel, durationSpinner);
        durationHBox.setAlignment(Pos.CENTER_LEFT);
        //duration

        //i00
        Label i00Label = new Label("I_00");
        i00Label.setPrefWidth(80);

        TextField i00TextField = new TextField(String.valueOf(entry.i00));
        i00TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (i00TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.i00 = Integer.parseInt(newText); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox i00HBox = new HBox(i00Label, i00TextField);
        i00HBox.setAlignment(Pos.CENTER_LEFT);
        //i00

        //i04
        Label i04Label = new Label("I_04");
        i04Label.setPrefWidth(80);

        TextField i04TextField = new TextField(String.valueOf(entry.i04));
        i04TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (i04TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.i04 = Integer.parseInt(newText); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox i04HBox = new HBox(i04Label, i04TextField);
        i04HBox.setAlignment(Pos.CENTER_LEFT);
        //i04

        //i08
        Label i08Label = new Label("I_08");
        i08Label.setPrefWidth(80);

        TextField i08TextField = new TextField(String.valueOf(entry.i08));
        i08TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (i08TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.i08 = Integer.parseInt(newText); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox i08HBox = new HBox(i08Label, i08TextField);
        i08HBox.setAlignment(Pos.CENTER_LEFT);
        //i08

        //f12
        Label f12Label = new Label("F_12");
        f12Label.setPrefWidth(80);

        TextField f12TextField = new TextField(String.valueOf(entry.f12));
        f12TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (f12TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.f12 = Float.parseFloat(newText); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox f12HBox = new HBox(f12Label, f12TextField);
        f12HBox.setAlignment(Pos.CENTER_LEFT);
        //f12

        //f16
        Label f16Label = new Label("F_16");
        f16Label.setPrefWidth(80);

        TextField f16TextField = new TextField(String.valueOf(entry.f16));
        f16TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (f16TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.f16 = Float.parseFloat(newText); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox f16HBox = new HBox(f16Label, f16TextField);
        f16HBox.setAlignment(Pos.CENTER_LEFT);
        //f16

        //f20
        Label f20Label = new Label("F_20");
        f20Label.setPrefWidth(80);

        TextField f20TextField = new TextField(String.valueOf(entry.f20));
        f20TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (f20TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.f20 = Float.parseFloat(newText); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox f20HBox = new HBox(f20Label ,f20TextField);
        f20HBox.setAlignment(Pos.CENTER_LEFT);
        //f20

        //i24
        Label i24Label = new Label("I_24");
        i24Label.setPrefWidth(80);

        TextField i24TextField = new TextField(String.valueOf(entry.i24));
        i24TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (i24TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.i24 = Integer.parseInt(newText); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox i24HBox = new HBox(i24Label, i24TextField);
        i24HBox.setAlignment(Pos.CENTER_LEFT);
        //i24

        //i28
        Label i28Label = new Label("I_28");
        i28Label.setPrefWidth(80);

        TextField i28TextField = new TextField(String.valueOf(entry.i28));
        i28TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (i28TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.i28 = Integer.parseInt(newText); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox i28HBox = new HBox(i28Label, i28TextField);
        i28HBox.setAlignment(Pos.CENTER_LEFT);
        //i28

        //i32
        Label i32Label = new Label("I_32");
        i32Label.setPrefWidth(80);

        TextField i32TextField = new TextField(String.valueOf(entry.i32));
        i32TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (i32TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.i32 = Integer.parseInt(newText); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox i32HBox = new HBox(i32Label, i32TextField);
        i32HBox.setAlignment(Pos.CENTER_LEFT);
        //i32

        //i36
        Label i36Label = new Label("I_36");
        i36Label.setPrefWidth(80);

        TextField i36TextField = new TextField(String.valueOf(entry.i36));
        i36TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (i36TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.i36 = Integer.parseInt(newText); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox i36HBox = new HBox(i36Label, i36TextField);
        i36HBox.setAlignment(Pos.CENTER_LEFT);
        //i36

        //i40
        Label i40Label = new Label("I_40");
        i40Label.setPrefWidth(80);

        TextField i40TextField = new TextField(String.valueOf(entry.i40));
        i40TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (i40TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.i40 = Integer.parseInt(newText); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox i40HBox = new HBox(i40Label, i40TextField);
        i40HBox.setAlignment(Pos.CENTER_LEFT);
        //i40

        //i44
        Label i44Label = new Label("I_44");
        i44Label.setPrefWidth(80);

        TextField i44TextField = new TextField(String.valueOf(entry.i44));
        i44TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (i44TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.i44 = Integer.parseInt(newText); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox i44HBox = new HBox(i44Label, i44TextField);
        i44HBox.setAlignment(Pos.CENTER_LEFT);
        //i44

        //i48
        Label i48Label = new Label("I_48");
        i48Label.setPrefWidth(80);

        TextField i48TextField = new TextField(String.valueOf(entry.i48));
        i48TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (i48TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.i48 = Integer.parseInt(newText); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox i48HBox = new HBox(i48Label, i48TextField);
        i48HBox.setAlignment(Pos.CENTER_LEFT);
        //i48

        //i50
        Label i50Label = new Label("I_50");
        i50Label.setPrefWidth(80);

        TextField i50TextField = new TextField(String.valueOf(entry.i50));
        i50TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (i50TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.i50 = Integer.parseInt(newText); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox i50HBox = new HBox(i50Label, i50TextField);
        i50HBox.setAlignment(Pos.CENTER_LEFT);
        //i50

        //i52
        Label i52Label = new Label("I_52");
        i52Label.setPrefWidth(80);

        TextField i52TextField = new TextField(String.valueOf(entry.i52));
        i52TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (i52TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.i52 = Integer.parseInt(newText); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox i52HBox = new HBox(i52Label,i52TextField);
        i52HBox.setAlignment(Pos.CENTER_LEFT);
        //i52

        //i54
        Label i54Label = new Label("I_54");
        i54Label.setPrefWidth(80);

        TextField i54TextField = new TextField(String.valueOf(entry.i54));
        i54TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (i54TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.i54 = Integer.parseInt(newText); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox i54HBox = new HBox(i54Label, i54TextField);
        i54HBox.setAlignment(Pos.CENTER_LEFT);
        //i54

        //deflection
        VBox deflectionVBox = new VBox(30, 
            startTimeHBox, durationHBox, 
            i00HBox, i04HBox, 
            i08HBox, f12HBox, 
            f16HBox, f20HBox, 
            i24HBox, i28HBox, 
            i32HBox, i36HBox, 
            i40HBox, i44HBox, 
            i48HBox, i50HBox, 
            i52HBox, i54HBox
        );
        deflectionVBox.setPadding(new Insets(20, 0, 20, 16));

        Tab deflectionTab = new Tab("Deflection", new ScrollPane(deflectionVBox));
        deflectionTab.setClosable(false);
        //deflection

        tabPane.getTabs().add(deflectionTab);
    }

    private void createBsaType6(BsaType6Entry entry) {
        //start time
        Label startTimeLabel = new Label("Start Time");
        startTimeLabel.setPrefWidth(100);
        
        Spinner <Integer> startTimeSpinner = new Spinner<>(0, 65535, entry.startTime);
        startTimeSpinner.setEditable(true);
        startTimeSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                entry.startTime = newValue;
            }
        });

        HBox startTimeHBox = new HBox(startTimeLabel, startTimeSpinner);
        startTimeHBox.setAlignment(Pos.CENTER_LEFT);
        //start time

        //duration
        Label durationLabel = new Label("Duration");
        durationLabel.setPrefWidth(100);
        
        Spinner <Integer> durationSpinner = new Spinner<>(0, 65535, entry.duration);
        durationSpinner.setEditable(true);
        durationSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                entry.duration = newValue;
            }
        });

        HBox durationHBox = new HBox(durationLabel, durationSpinner);
        durationHBox.setAlignment(Pos.CENTER_LEFT);
        //duration

        //eepk type
        Label eepkTypeLabel = new Label("EEPK Type");
        eepkTypeLabel.setPrefWidth(100);

        ToggleGroup eepkTypeToggleGroup = new ToggleGroup();
        
        RadioButton common = new RadioButton("Common");
        common.setToggleGroup(eepkTypeToggleGroup);

        RadioButton stageBG = new RadioButton("Stage BG");
        stageBG.setToggleGroup(eepkTypeToggleGroup);

        RadioButton character = new RadioButton("Character");
        character.setToggleGroup(eepkTypeToggleGroup);

        RadioButton awokenSkill = new RadioButton("Awoken Skill");
        awokenSkill.setToggleGroup(eepkTypeToggleGroup);

        RadioButton superSkill = new RadioButton("Super Skill");
        superSkill.setToggleGroup(eepkTypeToggleGroup);

        RadioButton ultimateSkill = new RadioButton("Ultimate Skill");
        ultimateSkill.setToggleGroup(eepkTypeToggleGroup);

        RadioButton evasiveSkill = new RadioButton("Evasive Skill");
        evasiveSkill.setToggleGroup(eepkTypeToggleGroup);

        RadioButton kiBlastSkill = new RadioButton("Ki Blast Skill");
        kiBlastSkill.setToggleGroup(eepkTypeToggleGroup);

        RadioButton stage = new RadioButton("Stage");
        stage.setToggleGroup(eepkTypeToggleGroup);

        switch (entry.eepkType) {
            case 1 -> stageBG.setSelected(true);
            case 2 -> character.setSelected(true);
            case 3 -> awokenSkill.setSelected(true);
            case 5 -> superSkill.setSelected(true);
            case 6 -> ultimateSkill.setSelected(true);
            case 7 -> evasiveSkill.setSelected(true);
            case 9 -> kiBlastSkill.setSelected(true);
            case 11 -> stage.setSelected(true);
            default -> common.setSelected(true);
        }

        eepkTypeToggleGroup.selectedToggleProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue.isSelected()) {
                if ((RadioButton) newValue == common) { 
                    entry.eepkType = 0;
                }
                else if ((RadioButton) newValue == stageBG) { 
                    entry.eepkType = 1;
                }
                else if ((RadioButton) newValue == character) { 
                    entry.eepkType = 2;
                }
                else if ((RadioButton) newValue == awokenSkill) { 
                    entry.eepkType = 3;
                }
                else if ((RadioButton) newValue == superSkill) { 
                    entry.eepkType = 5;
                }
                else if ((RadioButton) newValue == ultimateSkill) { 
                    entry.eepkType = 6;
                }
                else if ((RadioButton) newValue == evasiveSkill) { 
                    entry.eepkType = 7;
                }
                else if ((RadioButton) newValue == kiBlastSkill) { 
                    entry.eepkType = 9;
                }
                else if ((RadioButton) newValue == stage) {
                    entry.eepkType = 11;
                }
            }
        });

        GridPane eepkTypeGridPane = new GridPane(10, 10);
        eepkTypeGridPane.getStyleClass().add("titled-address-box");
        eepkTypeGridPane.add(common, 0, 0);   
        eepkTypeGridPane.add(stageBG, 1, 0);          
        eepkTypeGridPane.add(character, 2, 0);          
        eepkTypeGridPane.add(awokenSkill, 0, 1);          
        eepkTypeGridPane.add(superSkill, 1, 1);          
        eepkTypeGridPane.add(ultimateSkill, 2, 1);          
        eepkTypeGridPane.add(evasiveSkill, 0, 2);          
        eepkTypeGridPane.add(kiBlastSkill, 1, 2);          
        eepkTypeGridPane.add(stage, 2, 2);          

        HBox eepkTypeHBox=new HBox(eepkTypeLabel, eepkTypeGridPane);
        eepkTypeHBox.setAlignment(Pos.CENTER_LEFT);
        //eepk type

        //skill id
        Label skillIdLabel = new Label("Skill ID");
        skillIdLabel.setPrefWidth(100);
        
        Spinner <Integer> skillIdSpinner = new Spinner<>(0, 65535, entry.skillId);
        skillIdSpinner.setEditable(true);
        skillIdSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                entry.skillId = newValue;
            }
        });

        HBox skillIdHBox = new HBox(skillIdLabel, skillIdSpinner);
        skillIdHBox.setAlignment(Pos.CENTER_LEFT);
        //skill id

        //effect id
        Label effectIdLabel = new Label("Effect ID");
        effectIdLabel.setPrefWidth(100);
        
        Spinner <Integer> effectIdSpinner = new Spinner<>(0, 65535, entry.effectId);
        effectIdSpinner.setEditable(true);
        effectIdSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                entry.effectId = newValue;
            }
        });

        HBox effectIdHBox = new HBox(effectIdLabel, effectIdSpinner);
        effectIdHBox.setAlignment(Pos.CENTER_LEFT);
        //effect id

        //i06
        Label i06Label = new Label("I_06");
        i06Label.setPrefWidth(60);

        TextField i06TextField = new TextField(String.valueOf(entry.i06));
        i06TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (i06TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.i06 = Integer.parseInt(newText);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox i06HBox = new HBox(i06Label, i06TextField);
        i06HBox.setAlignment(Pos.CENTER_LEFT);
        //i06

        //effect switch
        Label effectSwitchLabel = new Label("Effect Switch");
        effectSwitchLabel.setPrefWidth(100);

        ToggleGroup effectSwtichGroup = new ToggleGroup();

        RadioButton on = new RadioButton("On");
        on.setToggleGroup(effectSwtichGroup);
        
        RadioButton off = new RadioButton("Off");
        off.setToggleGroup(effectSwtichGroup);

        switch (entry.effectSwitch) {
            case 1 -> off.setSelected(true);
            default -> on.setSelected(true);
        }

        effectSwtichGroup.selectedToggleProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue.isSelected()) {
                if ((RadioButton) newValue == on) { 
                    entry.effectSwitch = 0;
                }
                else if ((RadioButton) newValue == off) { 
                    entry.effectSwitch = 1;
                }
            }
        });

        HBox effectSwitchRadioButtonsHBox = new HBox(15, on, off);
        effectSwitchRadioButtonsHBox.getStyleClass().add("titled-address-box");

        HBox effectsSwitchHBox = new HBox(effectSwitchLabel, effectSwitchRadioButtonsHBox);
        effectsSwitchHBox.setAlignment(Pos.CENTER_LEFT);
        //effects switch

        //i10
        Label i10Label = new Label("I_10");
        i10Label.setPrefWidth(60);

        TextField i10TextField = new TextField(String.valueOf(entry.i10));
        i10TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (i10TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.i10 = Integer.parseInt(newText);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox i10HBox = new HBox(i10Label, i10TextField);
        i10HBox.setAlignment(Pos.CENTER_LEFT);
        //i10

        //position x
        Label speedXLabel = new Label("Position X");
        speedXLabel.setPrefWidth(100);
        
        Spinner <Double> positionXSpinner = new Spinner<>(Float.MIN_VALUE, Float.MAX_VALUE, entry.positionX);
        positionXSpinner.setEditable(true);
        positionXSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                entry.positionX = newValue.floatValue();
            }
        });

        HBox positionXHBox = new HBox(speedXLabel, positionXSpinner);
        positionXHBox.setAlignment(Pos.CENTER_LEFT);
        //position x

        //position y
        Label speedYLabel = new Label("Position Y");
        speedYLabel.setPrefWidth(100);
        
        Spinner <Double> positionYSpinner = new Spinner<>(Float.MIN_VALUE, Float.MAX_VALUE, entry.positionY);
        positionYSpinner.setEditable(true);
        positionYSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                entry.positionY = newValue.floatValue();
            }
        });

        HBox positionYHBox = new HBox(speedYLabel, positionYSpinner);
        positionYHBox.setAlignment(Pos.CENTER_LEFT);
        //position y

        //position z
        Label speedZLabel = new Label("Position Z");
        speedZLabel.setPrefWidth(100);
        
        Spinner <Double> positionZSpinner = new Spinner<>(Float.MIN_VALUE, Float.MAX_VALUE, entry.positionZ);
        positionZSpinner.setEditable(true);
        positionZSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                entry.positionZ = newValue.floatValue();
            }
        });

        HBox positionZHBox = new HBox(speedZLabel, positionZSpinner);
        positionZHBox.setAlignment(Pos.CENTER_LEFT);
        //position z

        //effect
        VBox effectVBox = new VBox(45, startTimeHBox, durationHBox, eepkTypeHBox, skillIdHBox, effectIdHBox, effectsSwitchHBox, positionXHBox, positionYHBox, positionZHBox);
        effectVBox.setPadding(new Insets(20, 0, 0, 16));

        Tab effectTab = new Tab("Effect", effectVBox);
        effectTab.setClosable(false);
        //effect

        //unknown
        VBox unknownVBox = new VBox(30, i06HBox, i10HBox);
        unknownVBox.setPadding(new Insets(20, 0, 0, 16));

        Tab unknownTab = new Tab("Unknown", unknownVBox);
        unknownTab.setClosable(false);
        //unknown

        tabPane.getTabs().addAll(effectTab, unknownTab);
    }

    private void createBsaType7(BsaType7Entry entry) {
        //start time
        Label startTimeLabel = new Label("Start Time");
        startTimeLabel.setPrefWidth(100);
        
        Spinner <Integer> startTimeSpinner = new Spinner<>(0, 65535, entry.startTime);
        startTimeSpinner.setEditable(true);
        startTimeSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                entry.startTime = newValue;
            }
        });

        HBox startTimeHBox = new HBox(startTimeLabel, startTimeSpinner);
        startTimeHBox.setAlignment(Pos.CENTER_LEFT);
        //start time

        //duration
        Label durationLabel = new Label("Duration");
        durationLabel.setPrefWidth(100);
        
        Spinner <Integer> durationSpinner = new Spinner<>(0, 65535, entry.duration);
        durationSpinner.setEditable(true);
        durationSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                entry.duration = newValue;
            }
        });

        HBox durationHBox = new HBox(durationLabel, durationSpinner);
        durationHBox.setAlignment(Pos.CENTER_LEFT);
        //duration

        //acb type
        Label acbTypeLabel = new Label("ACB Type");
        acbTypeLabel.setPrefWidth(100);

        ToggleGroup acbTypeToggleGroup = new ToggleGroup();

        RadioButton common = new RadioButton("Common SE");
        common.setToggleGroup(acbTypeToggleGroup);

        RadioButton characterSE = new RadioButton("Character SE");
        characterSE.setToggleGroup(acbTypeToggleGroup);

        RadioButton characterVOX = new RadioButton("Character VOX");
        characterVOX.setToggleGroup(acbTypeToggleGroup);

        RadioButton skillSE = new RadioButton("Skill SE");
        skillSE.setToggleGroup(acbTypeToggleGroup);

        RadioButton skillVOX = new RadioButton("Skill VOX");
        skillVOX.setToggleGroup(acbTypeToggleGroup);


        switch (entry.acbType) {
            case 1 ->  characterSE.setSelected(true);           
            case 2 ->  characterVOX.setSelected(true);          
            case 3 -> skillSE.setSelected(true);               
            case 4 -> skillVOX.setSelected(true);              
            default -> common.setSelected(true);                
        }

        acbTypeToggleGroup.selectedToggleProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null && newValue.isSelected()) {
                if ((RadioButton) newValue == common)                    { entry.acbType = 0;  }
                else if ((RadioButton) newValue == characterSE)          { entry.acbType = 1;  }
                else if ((RadioButton) newValue == characterVOX)         { entry.acbType = 2;  }
                else if ((RadioButton) newValue == skillSE)              { entry.acbType = 3;  }
                else if ((RadioButton) newValue == skillVOX)             { entry.acbType = 4;  }
            }
        });

        HBox acbTypeRadioButtonsHBox=new HBox(15, common, characterSE, characterVOX, skillSE, skillVOX);
        acbTypeRadioButtonsHBox.getStyleClass().add("titled-address-box");

        HBox acbTypeHBox = new HBox(acbTypeLabel, acbTypeRadioButtonsHBox);
        acbTypeHBox.setAlignment(Pos.CENTER_LEFT);
        //acb type

        //i02
        Label i02Label = new Label("I_02");
        i02Label.setPrefWidth(60);

        TextField i02TextField = new TextField(String.valueOf(entry.i02));
        i02TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (i02TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.i02 = Integer.parseInt(newText);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox i02HBox = new HBox(i02Label, i02TextField);
        i02HBox.setAlignment(Pos.CENTER_LEFT);
        //i02

        //cue id
        Label cueIdLabel = new Label("Cue ID");
        cueIdLabel.setPrefWidth(100);
        
        Spinner <Integer> cueIdSpinner = new Spinner<>(0, 65535, entry.duration);
        cueIdSpinner.setEditable(true);
        cueIdSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                entry.duration = newValue;
            }
        });

        HBox cueIdHBox = new HBox(cueIdLabel, cueIdSpinner);
        cueIdHBox.setAlignment(Pos.CENTER_LEFT);
        //cue id

        //i06
        Label i06Label = new Label("I_06");
        i06Label.setPrefWidth(60);

        TextField i06TextField = new TextField(String.valueOf(entry.i06));
        i06TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (i06TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.i06 = Integer.parseInt(newText);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox i06HBox = new HBox(i06Label, i06TextField);
        i06HBox.setAlignment(Pos.CENTER_LEFT);
        //i06

        //sound
        VBox soundVBox = new VBox(30, startTimeHBox, durationHBox, acbTypeHBox, cueIdHBox);
        soundVBox.setPadding(new Insets(20, 0, 0, 16));

        Tab soundTab = new Tab("Sound",soundVBox);
        soundTab.setClosable(false);
        //sound

        //unknown
        VBox unknownVBox = new VBox(30, i02HBox, i06HBox);
        unknownVBox.setPadding(new Insets(20, 0, 0, 16));

        Tab unknownTab = new Tab("Unknown", unknownVBox);
        unknownTab.setClosable(false);
        //unknown

        tabPane.getTabs().addAll(soundTab, unknownTab);
    }

    private void createBsaType8(BsaType8Entry entry) {
        //start time
        Label startTimeLabel = new Label("Start Time");
        startTimeLabel.setPrefWidth(80);
        
        Spinner <Integer> startTimeSpinner = new Spinner<>(0, 65535, entry.startTime);
        startTimeSpinner.setEditable(true);
        startTimeSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                entry.startTime = newValue;
            }
        });

        HBox startTimeHBox = new HBox(15, startTimeLabel, startTimeSpinner);
        startTimeHBox.setAlignment(Pos.CENTER_LEFT);
        //start time

        //duration
        Label durationLabel = new Label("Duration");
        durationLabel.setPrefWidth(80);
        
        Spinner <Integer> durationSpinner = new Spinner<>(0, 65535, entry.duration);
        durationSpinner.setEditable(true);
        durationSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                entry.duration = newValue;
            }
        });

        HBox durationHBox = new HBox(15, durationLabel, durationSpinner);
        durationHBox.setAlignment(Pos.CENTER_LEFT);
        //duration

        //bpe effect id
        Label bpeEffectIdLabel = new Label("BPE Effect ID");
        bpeEffectIdLabel.setPrefWidth(80);

        Spinner <Integer> bpeEffectIdSpinner = new Spinner<>(0, 65535, entry.bpeEffectId);
        bpeEffectIdSpinner.setEditable(true);
        bpeEffectIdSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                entry.bpeEffectId = newValue;
            }
        });

        Label bpeEffectIdIndicatorLabel = new Label();
        bpeEffectIdIndicatorLabel.setTextFill(Color.CRIMSON);
        
        bpeEffectIdIndicatorLabel.textProperty().bind(
            Bindings.createStringBinding(() -> {
                return switch (bpeEffectIdSpinner.getValue()) {
                    case 0,2 -> "Brightens Up The Screen";
                    case 1 -> "White Screen";
                    case 3 -> "Quick White Flash";
                    case 4 -> "Brightness Wavering";
                    case 5 -> "Red Tint";
                    case 6 -> "Fast Brightness Wavering";
                    case 10 -> "Small Motion Blur";
                    case 11 -> "Strong Motion Blur";
                    case 12,15,16 -> "Quick Motion Blur";
                    case 13 -> "Very Small Blur";
                    case 14 -> "Light Blue Filter";
                    case 17 -> "Magenta Filter";
                    case 18 -> "Two Different Motion Blurs";
                    case 20,21,22,23,26,27 -> "Ripple Blur";
                    case 24,25 -> "Gravely Blur";
                    case 30 -> "Solar Flare Screen Effect (Opponent Blind)";
                    case 31,32,54 -> "Blackening Around The Screen";
                    case 33,35 -> "Faint Black Circle";
                    case 34 -> "A Pair Of Faint Black Circles";
                    case 36 -> "Solar Flare Screen Effect (User Activate)";
                    case 37 -> "Screen Turns Completely Black";
                    case 40 -> "Small Transparent Ring Expanding";
                    case 41,42,46 -> "Transparent Ring";
                    case 43,44,45,52 -> "Big Transparent Ring";
                    case 50,51 -> "Brightening Of The Screen";
                    case 53 -> "Blue Tint";
                    case 55,56,57,61,63 -> "Screen Slightly Darkens And Desaturates";
                    case 60 -> "Screen Flashes A Faint White";
                    case 64 -> "Screen Flashes A Faint Pink For A Second";
                    case 65 -> "Light Blue Filter Faints In And Out";
                    case 66 -> "Black Spheres";
                    case 70 -> "Standard Black Filter And Used During Skill Activation";
                    default -> "Unknown";
                };
            }, bpeEffectIdSpinner.valueProperty())
        );

        HBox bpeEffectIdHBox = new HBox(15, bpeEffectIdLabel, bpeEffectIdSpinner, bpeEffectIdIndicatorLabel);
        bpeEffectIdHBox.setAlignment(Pos.CENTER_LEFT);
        //bpe effect id

        //i02
        Label i02Label = new Label("I_02");
        i02Label.setPrefWidth(80);

        TextField i02TextField = new TextField(String.valueOf(entry.i02));
        i02TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (i02TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.i02 = Integer.parseInt(newText);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox i02HBox = new HBox(15, i02Label, i02TextField);
        i02HBox.setAlignment(Pos.CENTER_LEFT);
        //i02

        //i04
        Label i04Label = new Label("I_04");
        i04Label.setPrefWidth(80);

        TextField i04TextField = new TextField(String.valueOf(entry.i04));
        i04TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (i04TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.i04 = Integer.parseInt(newText);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox i04HBox = new HBox(15, i04Label, i04TextField);
        i04HBox.setAlignment(Pos.CENTER_LEFT);
        //i04

        //i08
        Label i08Label = new Label("I_08");
        i08Label.setPrefWidth(80);

        TextField i08TextField = new TextField(String.valueOf(entry.i08));
        i08TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (i08TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.i08 = Integer.parseInt(newText);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox i08HBox = new HBox(15, i08Label, i08TextField);
        i08HBox.setAlignment(Pos.CENTER_LEFT);
        //i08

        //i12
        Label i12Label = new Label("I_12");
        i12Label.setPrefWidth(80);

        TextField i12TextField = new TextField(String.valueOf(entry.i12));
        i12TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (i12TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.i12 = Integer.parseInt(newText);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox i12HBox = new HBox(15, i12Label, i12TextField);
        i12HBox.setAlignment(Pos.CENTER_LEFT);
        //i12

        //i16
        Label i16Label = new Label("I_16");
        i16Label.setPrefWidth(80);

        TextField i16TextField = new TextField(String.valueOf(entry.i16));
        i16TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (i16TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.i16 = Integer.parseInt(newText);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox i16HBox = new HBox(15, i16Label, i16TextField);
        i16HBox.setAlignment(Pos.CENTER_LEFT);
        //i16

        //i20
        Label i20Label = new Label("I_20");
        i20Label.setPrefWidth(80);

        TextField i20TextField = new TextField(String.valueOf(entry.i20));
        i20TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (i20TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.i20 = Integer.parseInt(newText);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox i20HBox = new HBox(15, i20Label, i20TextField);
        i20HBox.setAlignment(Pos.CENTER_LEFT);
        //i20

        //screen effect
        VBox screenEffectVBox = new VBox(35, 
            startTimeHBox, durationHBox, 
            bpeEffectIdHBox, i02HBox, 
            i04HBox, i08HBox, 
            i12HBox, i16HBox, 
            i20HBox
        );
        screenEffectVBox.setPadding(new Insets(20, 0, 0, 16));

        Tab screenEffectTab = new Tab("Screen Effect", screenEffectVBox);
        screenEffectTab.setClosable(false);
        //screen effect

        tabPane.getTabs().add(screenEffectTab);
    }

    private void createBsaType10(BsaType10Entry entry) {
        //start time
        Label startTimeLabel = new Label("Start Time");
        startTimeLabel.setPrefWidth(60);
        
        Spinner <Integer> startTimeSpinner = new Spinner<>(0, 65535, entry.startTime);
        startTimeSpinner.setEditable(true);
        startTimeSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                entry.startTime = newValue;
            }
        });

        HBox startTimeHBox = new HBox(startTimeLabel, startTimeSpinner);
        startTimeHBox.setAlignment(Pos.CENTER_LEFT);
        //start time

        //duration
        Label durationLabel = new Label("Duration");
        durationLabel.setPrefWidth(60);
        
        Spinner <Integer> durationSpinner = new Spinner<>(0, 65535, entry.duration);
        durationSpinner.setEditable(true);
        durationSpinner.valueProperty().addListener((obs, oldValue,newValue) -> {
            if (newValue != null) {
                entry.duration = newValue;
            }
        });

        HBox durationHBox = new HBox(durationLabel, durationSpinner);
        durationHBox.setAlignment(Pos.CENTER_LEFT);
        //duration

        //skill id
        Label skillIdLabel = new Label("Skill ID");
        skillIdLabel.setPrefWidth(60);
        
        Spinner <Integer> skillIdSpinner = new Spinner<>(0, 65535, entry.skillId);
        skillIdSpinner.setEditable(true);
        skillIdSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                entry.skillId = newValue;
            }
        });

        HBox skillIdHBox = new HBox(skillIdLabel, skillIdSpinner);
        skillIdHBox.setAlignment(Pos.CENTER_LEFT);
        //skill id

        //i04
        Label i04Label = new Label("I_04");
        i04Label.setPrefWidth(60);

        TextField i04TextField = new TextField(String.valueOf(entry.i04));
        i04TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (i04TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.i04 = Integer.parseInt(newText);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox i04HBox = new HBox(i04Label, i04TextField);
        i04HBox.setAlignment(Pos.CENTER_LEFT);
        //i04

        //i06
        Label i06Label = new Label("I_06");
        i06Label.setPrefWidth(60);

        TextField i06TextField = new TextField(String.valueOf(entry.i06));
        i06TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (i06TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.i06 = Integer.parseInt(newText);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox i06HBox = new HBox(i06Label,i06TextField);
        i06HBox.setAlignment(Pos.CENTER_LEFT);
        //i06

        //type 10
        VBox type10VBox = new VBox(30, startTimeHBox, durationHBox, skillIdHBox, i04HBox, i06HBox);
        type10VBox.setPadding(new Insets(20, 0, 0, 16));

        Tab type10Tab = new Tab("Type 10", type10VBox);
        type10Tab.setClosable(false);
        //type 10

        tabPane.getTabs().add(type10Tab);
    }

    private void createBsaType12(BsaType12Entry entry) {
        //start time
        Label startTimeLabel = new Label("Start Time");
        startTimeLabel.setPrefWidth(80);
        
        Spinner <Integer> startTimeSpinner = new Spinner<>(0, 65535, entry.startTime);
        startTimeSpinner.setEditable(true);
        startTimeSpinner.valueProperty().addListener((obs,oldValue,newValue) -> {
            if (newValue != null) {
                entry.startTime = newValue;
            }
        });

        HBox startTimeHBox = new HBox(startTimeLabel, startTimeSpinner);
        startTimeHBox.setAlignment(Pos.CENTER_LEFT);
        //start time

        //duration
        Label durationLabel = new Label("Duration");
        durationLabel.setPrefWidth(80);
        
        Spinner <Integer> durationSpinner = new Spinner<>(0, 65535, entry.duration);
        durationSpinner.setEditable(true);
        durationSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                entry.duration = newValue;
            }
        });

        HBox durationHBox = new HBox(durationLabel ,durationSpinner);
        durationHBox.setAlignment(Pos.CENTER_LEFT);
        //duration

        //f00
        Label f00Label = new Label("F_00");
        f00Label.setPrefWidth(80);

        TextField f00TextField = new TextField(String.valueOf(entry.f00));
        f00TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (f00TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.f00 = Float.parseFloat(newText);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox f00HBox = new HBox(f00Label, f00TextField);
        f00HBox.setAlignment(Pos.CENTER_LEFT);
        //f00

        //eepk type
        Label eepkTypeLabel = new Label("EEPK Type");
        eepkTypeLabel.setPrefWidth(80);

        ToggleGroup eepkTypeToggleGroup = new ToggleGroup();

        RadioButton common = new RadioButton("Common");
        common.setToggleGroup(eepkTypeToggleGroup);

        RadioButton stageBG = new RadioButton("Stage BG");
        stageBG.setToggleGroup(eepkTypeToggleGroup);

        RadioButton character = new RadioButton("Character");
        character.setToggleGroup(eepkTypeToggleGroup);

        RadioButton awokenSkill = new RadioButton("Awoken Skill");
        awokenSkill.setToggleGroup(eepkTypeToggleGroup);

        RadioButton superSkill = new RadioButton("Super Skill");
        superSkill.setToggleGroup(eepkTypeToggleGroup);

        RadioButton ultimateSkill = new RadioButton("Ultimate Skill");
        ultimateSkill.setToggleGroup(eepkTypeToggleGroup);

        RadioButton evasiveSkill = new RadioButton("Evasive Skill");
        evasiveSkill.setToggleGroup(eepkTypeToggleGroup);

        RadioButton kiBlastSkill = new RadioButton("Ki Blast Skill");
        kiBlastSkill.setToggleGroup(eepkTypeToggleGroup);

        RadioButton stage = new RadioButton("Stage");
        stage.setToggleGroup(eepkTypeToggleGroup);

        switch (entry.eepkType) {
            case 1 -> stageBG.setSelected(true);
            case 2 -> character.setSelected(true);
            case 3 -> awokenSkill.setSelected(true);
            case 5 -> superSkill.setSelected(true);
            case 6 -> ultimateSkill.setSelected(true);
            case 7 -> evasiveSkill.setSelected(true);
            case 9 -> kiBlastSkill.setSelected(true);
            case 11 -> stage.setSelected(true);
            default -> common.setSelected(true);
        }

        eepkTypeToggleGroup.selectedToggleProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue.isSelected()) {
                if ((RadioButton) newValue == common) { 
                    entry.eepkType = 0;
                }
                else if ((RadioButton) newValue == stageBG) { 
                    entry.eepkType = 1;
                }
                else if ((RadioButton) newValue == character) { 
                    entry.eepkType = 2;
                }
                else if ((RadioButton) newValue == awokenSkill) { 
                    entry.eepkType = 3;
                }
                else if ((RadioButton) newValue == superSkill) { 
                    entry.eepkType = 5;
                }
                else if ((RadioButton) newValue == ultimateSkill) { 
                    entry.eepkType = 6;
                }
                else if ((RadioButton) newValue == evasiveSkill) { 
                    entry.eepkType = 7;
                }
                else if ((RadioButton) newValue == kiBlastSkill) { 
                    entry.eepkType = 9;
                }
                else if ((RadioButton) newValue == stage) {
                    entry.eepkType = 11;
                }
            }
        });

        GridPane eepkTypeGridPane = new GridPane(10, 10);
        eepkTypeGridPane.getStyleClass().add("titled-address-box");
        eepkTypeGridPane.add(common, 0, 0);   
        eepkTypeGridPane.add(stageBG, 1, 0);          
        eepkTypeGridPane.add(character, 2, 0);          
        eepkTypeGridPane.add(awokenSkill, 0, 1);          
        eepkTypeGridPane.add(superSkill, 1, 1);          
        eepkTypeGridPane.add(ultimateSkill, 2, 1);          
        eepkTypeGridPane.add(evasiveSkill, 0, 2);          
        eepkTypeGridPane.add(kiBlastSkill, 1, 2);          
        eepkTypeGridPane.add(stage, 2, 2);          

        HBox eepkTypeHBox = new HBox(eepkTypeLabel, eepkTypeGridPane);
        eepkTypeHBox.setAlignment(Pos.CENTER_LEFT);
        //eepk type

        //skill id
        Label skillIdLabel = new Label("Skill ID");
        skillIdLabel.setPrefWidth(80);
        
        Spinner <Integer> skillIdSpinner = new Spinner<>(0, 65535, entry.skillId);
        skillIdSpinner.setEditable(true);
        skillIdSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                entry.skillId = newValue;
            }
        });

        HBox skillIdHBox = new HBox(skillIdLabel, skillIdSpinner);
        skillIdHBox.setAlignment(Pos.CENTER_LEFT);
        //skill id

        //i12
        Label i12Label = new Label("I_12");
        i12Label.setPrefWidth(80);

        TextField i12TextField = new TextField(String.valueOf(entry.i12));
        i12TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (i12TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.i12 = Integer.parseInt(newText);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox i12HBox = new HBox(i12Label, i12TextField);
        i12HBox.setAlignment(Pos.CENTER_LEFT);
        //i12

        //f16
        Label f16Label = new Label("F_16");
        f16Label.setPrefWidth(80);

        TextField f16TextField = new TextField(String.valueOf(entry.f16));
        f16TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (f16TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.f16 = Float.parseFloat(newText);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox f16HBox = new HBox(f16Label, f16TextField);
        f16HBox.setAlignment(Pos.CENTER_LEFT);
        //f16

        VBox type12VBox = new VBox(40, startTimeHBox, durationHBox, f00HBox, eepkTypeHBox, skillIdHBox, i12HBox, f16HBox);
        type12VBox.setPadding(new Insets(20, 0, 0, 16));

        Tab type12Tab = new Tab("Type 12", type12VBox);
        type12Tab.setClosable(false);

        tabPane.getTabs().add(type12Tab);
    }

    private void createBsaType13(BsaType13Entry entry) {
        //start time
        Label startTimeLabel = new Label("Start Time");
        startTimeLabel.setPrefWidth(80);
        
        Spinner <Integer> startTimeSpinner = new Spinner<>(0, 65535, entry.startTime);
        startTimeSpinner.setEditable(true);
        startTimeSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                entry.startTime = newValue;
            }
        });

        HBox startTimeHBox = new HBox(startTimeLabel, startTimeSpinner);
        startTimeHBox.setAlignment(Pos.CENTER_LEFT);
        //start time

        //duration
        Label durationLabel = new Label("Duration");
        durationLabel.setPrefWidth(80);
        
        Spinner <Integer> durationSpinner = new Spinner<>(0, 65535, entry.duration);
        durationSpinner.setEditable(true);
        durationSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                entry.duration = newValue;
            }
        });

        HBox durationHBox = new HBox(durationLabel, durationSpinner);
        durationHBox.setAlignment(Pos.CENTER_LEFT);
        //duration

        //i00
        Label i00Label=new Label("I_00");
        i00Label.setPrefWidth(80);

        TextField i00TextField = new TextField(String.valueOf(entry.i00));
        i00TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (i00TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.i00 = Integer.parseInt(newText); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox i00HBox = new HBox(i00Label, i00TextField);
        i00HBox.setAlignment(Pos.CENTER_LEFT);
        //i00

        //i02
        Label i02Label = new Label("I_02");
        i02Label.setPrefWidth(80);

        TextField i02TextField = new TextField(String.valueOf(entry.i02));
        i02TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (i02TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.i02 = Integer.parseInt(newText); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox i02HBox = new HBox(i02Label, i02TextField);
        i02HBox.setAlignment(Pos.CENTER_LEFT);
        //i02

        //f04
        Label f04Label = new Label("F_04");
        f04Label.setPrefWidth(80);

        TextField f04TextField = new TextField(String.valueOf(entry.f04));
        f04TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (f04TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.f04 = Float.parseFloat(newText);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox f04HBox = new HBox(f04Label, f04TextField);
        f04HBox.setAlignment(Pos.CENTER_LEFT);
        //f04

        //f08
        Label f08Label = new Label("F_08");
        f08Label.setPrefWidth(80);

        TextField f08TextField = new TextField(String.valueOf(entry.f08));
        f08TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (f08TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.f08 = Float.parseFloat(newText);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox f08HBox = new HBox(f08Label, f08TextField);
        f08HBox.setAlignment(Pos.CENTER_LEFT);
        //f08

        //i12
        Label i12Label = new Label("I_12");
        i12Label.setPrefWidth(80);

        TextField i12TextField = new TextField(String.valueOf(entry.i12));
        i12TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (i12TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.i12 = Integer.parseInt(newText); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });
        HBox i12HBox = new HBox(i12Label, i12TextField);
        i12HBox.setAlignment(Pos.CENTER_LEFT);
        //i12

        //f16
        Label f16Label = new Label("F_16");
        f16Label.setPrefWidth(80);

        TextField f16TextField = new TextField(String.valueOf(entry.f16));
        f16TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (f16TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.f16 = Float.parseFloat(newText);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });
        
        HBox f16HBox = new HBox(f16Label, f16TextField);
        f16HBox.setAlignment(Pos.CENTER_LEFT);
        //f16

        //i20
        Label i20Label = new Label("I_20");
        i20Label.setPrefWidth(80);

        TextField i20TextField = new TextField(String.valueOf(entry.i20));
        i20TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (i20TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.i20 = Integer.parseInt(newText); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox i20HBox = new HBox(i20Label, i20TextField);
        i20HBox.setAlignment(Pos.CENTER_LEFT);
        //i20

        //i24
        Label i24Label = new Label("I_24");
        i24Label.setPrefWidth(80);

        TextField i24TextField = new TextField(String.valueOf(entry.i24));
        i24TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (i24TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.i24 = Integer.parseInt(newText); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox i24HBox = new HBox(i24Label, i24TextField);
        i24HBox.setAlignment(Pos.CENTER_LEFT);
        //i24

        //i28
        Label i28Label = new Label("I_28");
        i28Label.setPrefWidth(80);

        TextField i28TextField = new TextField(String.valueOf(entry.i28));
        i28TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (i28TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.i28 = Integer.parseInt(newText); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox i28HBox = new HBox(i28Label, i28TextField);
        i28HBox.setAlignment(Pos.CENTER_LEFT);
        //i28

        //type 13
        VBox type13VBox = new VBox(40,
            startTimeHBox, durationHBox, 
            i00HBox, i02HBox, 
            f04HBox, f08HBox, 
            i12HBox, f16HBox, 
            i20HBox, i24HBox, 
            i28HBox
        );
        type13VBox.setPadding(new Insets(20, 0, 0, 16));

        Tab type13Tab = new Tab("Type 13", type13VBox);
        type13Tab.setClosable(false);
        //type 13

        tabPane.getTabs().add(type13Tab);
    }

    private void createBsaType14(BsaType14Entry entry) {
        //start time
        Label startTimeLabel = new Label("Start Time");
        startTimeLabel.setPrefWidth(80);
        
        Spinner <Integer> startTimeSpinner = new Spinner<>(0, 65535, entry.startTime);
        startTimeSpinner.setEditable(true);
        startTimeSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                entry.startTime = newValue;
            }
        });

        HBox startTimeHBox = new HBox(startTimeLabel, startTimeSpinner);
        startTimeHBox.setAlignment(Pos.CENTER_LEFT);
        //start time

        //duration
        Label durationLabel = new Label("Duration");
        durationLabel.setPrefWidth(80);
        
        Spinner <Integer> durationSpinner = new Spinner<>(0, 65535, entry.duration);
        durationSpinner.setEditable(true);
        durationSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                entry.duration = newValue;
            }
        });

        HBox durationHBox = new HBox(durationLabel, durationSpinner);
        durationHBox.setAlignment(Pos.CENTER_LEFT);
        //duration

        //i00
        Label i00Label = new Label("I_00");
        i00Label.setPrefWidth(80);

        TextField i00TextField = new TextField(String.valueOf(entry.i00));
        i00TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (i00TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.i00 = Integer.parseInt(newText); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox i00HBox = new HBox(i00Label ,i00TextField);
        i00HBox.setAlignment(Pos.CENTER_LEFT);
        //i00

        //i02
        Label i02Label = new Label("I_02");
        i02Label.setPrefWidth(80);

        TextField i02TextField = new TextField(String.valueOf(entry.i02));
        i02TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (i02TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.i02 = Integer.parseInt(newText); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox i02HBox = new HBox(i02Label, i02TextField);
        i02HBox.setAlignment(Pos.CENTER_LEFT);
        //i02

        //f04
        Label f04Label = new Label("F_04");
        f04Label.setPrefWidth(80);

        TextField f04TextField = new TextField(String.valueOf(entry.f04));
        f04TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (f04TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.f04 = Float.parseFloat(newText);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox f04HBox = new HBox(f04Label, f04TextField);
        f04HBox.setAlignment(Pos.CENTER_LEFT);
        //f04

        //i08
        Label i08Label = new Label("I_08");
        i08Label.setPrefWidth(80);

        TextField i08TextField = new TextField(String.valueOf(entry.i08));
        i08TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (i08TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.i08 = Long.parseLong(newText);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox i08HBox = new HBox(i08Label, i08TextField);
        i08HBox.setAlignment(Pos.CENTER_LEFT);
        //i08

        //f12
        Label f12Label = new Label("F_12");
        f12Label.setPrefWidth(80);
        
        TextField f12TextField = new TextField(String.valueOf(entry.f12));
        f12TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (f12TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.f12 = Float.parseFloat(newText); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox f12HBox = new HBox(f12Label, f12TextField);
        f12HBox.setAlignment(Pos.CENTER_LEFT);
        //f12

        //i16
        Label i16Label = new Label("I_16");
        i16Label.setPrefWidth(80);

        TextField i16TextField = new TextField(String.valueOf(entry.i16));
        i16TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (i16TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.i16 = Long.parseLong(newText);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox i16HBox = new HBox(i16Label, i16TextField);
        i16HBox.setAlignment(Pos.CENTER_LEFT);
        //i16

        //f20
        Label f20Label = new Label("F_20");
        f20Label.setPrefWidth(80);
        
        TextField f20TextField = new TextField(String.valueOf(entry.f20));
        f20TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (f20TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.f20 = Float.parseFloat(newText); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox f20HBox = new HBox(f20Label, f20TextField);
        f20HBox.setAlignment(Pos.CENTER_LEFT);
        //f20

        //i24
        Label i24Label = new Label("I_24");
        i24Label.setPrefWidth(80);

        TextField i24TextField = new TextField(String.valueOf(entry.i24));
        i24TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (i24TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.i24 = Long.parseLong(newText);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox i24HBox = new HBox(i24Label, i24TextField);
        i24HBox.setAlignment(Pos.CENTER_LEFT);
        //i24

        //f28
        Label f28Label = new Label("F_28");
        f28Label.setPrefWidth(80);
        
        TextField f28TextField = new TextField(String.valueOf(entry.f28));
        f28TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (f28TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.f28 = Float.parseFloat(newText); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox f28HBox = new HBox(f28Label, f28TextField);
        f28HBox.setAlignment(Pos.CENTER_LEFT);
        //f28

        //i32
        Label i32Label = new Label("I_32");
        i32Label.setPrefWidth(80);

        TextField i32TextField = new TextField(String.valueOf(entry.i32));
        i32TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (i32TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.i32 = Long.parseLong(newText);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox i32HBox = new HBox(i32Label, i32TextField);
        i32HBox.setAlignment(Pos.CENTER_LEFT);
        //i32

        //i36
        Label i36Label = new Label("I_36");
        i36Label.setPrefWidth(80);

        TextField i36TextField = new TextField(String.valueOf(entry.i36));
        i36TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (i36TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.i36 = Long.parseLong(newText);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox i36HBox = new HBox(i36Label, i36TextField);
        i36HBox.setAlignment(Pos.CENTER_LEFT);
        //i36

        //i40
        Label i40Label = new Label("I_40");
        i40Label.setPrefWidth(80);

        TextField i40TextField = new TextField(String.valueOf(entry.i40));
        i40TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (i40TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.i40 = Long.parseLong(newText);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox i40HBox = new HBox(i40Label, i40TextField);
        i40HBox.setAlignment(Pos.CENTER_LEFT);
        //i40

        //f44
        Label f44Label = new Label("F_44");
        f44Label.setPrefWidth(80);
        
        TextField f44TextField = new TextField(String.valueOf(entry.f44));
        f44TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (f44TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.f44 = Float.parseFloat(newText); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox f44HBox = new HBox(f44Label, f44TextField);
        f44HBox.setAlignment(Pos.CENTER_LEFT);
        //f44

        //i48
        Label i48Label = new Label("I_48");
        i48Label.setPrefWidth(80);

        TextField i48TextField = new TextField(String.valueOf(entry.i48));
        i48TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (i48TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.i48 = Long.parseLong(newText);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox i48HBox = new HBox(i48Label, i48TextField);
        i48HBox.setAlignment(Pos.CENTER_LEFT);
        //i48

        //f52
        Label f52Label = new Label("F_52");
        f52Label.setPrefWidth(80);
        
        TextField f52TextField = new TextField(String.valueOf(entry.f52));
        f52TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (f52TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.f52 = Float.parseFloat(newText); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox f52HBox = new HBox(f52Label, f52TextField);
        f52HBox.setAlignment(Pos.CENTER_LEFT);
        //f52

        //i56
        Label i56Label = new Label("I_56");
        i56Label.setPrefWidth(80);

        TextField i56TextField = new TextField(String.valueOf(entry.i56));
        i56TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (i56TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.i56 = Long.parseLong(newText);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox i56HBox = new HBox(i56Label, i56TextField);
        i56HBox.setAlignment(Pos.CENTER_LEFT);
        //i56

        //f60
        Label f60Label = new Label("F_60");
        f60Label.setPrefWidth(80);
        
        TextField f60TextField = new TextField(String.valueOf(entry.f60));
        f60TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (f60TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.f60 = Float.parseFloat(newText); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox f60HBox = new HBox(f60Label, f60TextField);
        f60HBox.setAlignment(Pos.CENTER_LEFT);
        //f60

        //i64
        Label i64Label = new Label("I_64");
        i64Label.setPrefWidth(80);

        TextField i64TextField = new TextField(String.valueOf(entry.i64));
        i64TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (i64TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.i64 = Long.parseLong(newText);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox i64HBox = new HBox(i64Label, i64TextField);
        i64HBox.setAlignment(Pos.CENTER_LEFT);
        //i64

        //f68
        Label f68Label = new Label("F_68");
        f68Label.setPrefWidth(80);
        
        TextField f68TextField = new TextField(String.valueOf(entry.f68));
        f68TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (f68TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.f68 = Float.parseFloat(newText); 
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox f68HBox = new HBox(f68Label, f68TextField);
        f68HBox.setAlignment(Pos.CENTER_LEFT);
        //f68

        //i72
        Label i72Label = new Label("I_72");
        i72Label.setPrefWidth(80);

        TextField i72TextField = new TextField(String.valueOf(entry.i72));
        i72TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (i72TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.i72 = Long.parseLong(newText);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox i72HBox = new HBox(i72Label, i72TextField);
        i72HBox.setAlignment(Pos.CENTER_LEFT);
        //i72

        //i76
        Label i76Label = new Label("I_76");
        i76Label.setPrefWidth(80);

        TextField i76TextField = new TextField(String.valueOf(entry.i76));
        i76TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (i76TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.i76 = Long.parseLong(newText);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox i76HBox = new HBox(i76Label, i76TextField);
        i76HBox.setAlignment(Pos.CENTER_LEFT);
        //i76

        //i80
        Label i80Label = new Label("I_80");
        i80Label.setPrefWidth(80);

        TextField i80TextField = new TextField(String.valueOf(entry.i80));
        i80TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (i80TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.i80 = Long.parseLong(newText);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox i80HBox = new HBox(i80Label, i80TextField);
        i80HBox.setAlignment(Pos.CENTER_LEFT);
        //i80

        //i84
        Label i84Label = new Label("I_84");
        i84Label.setPrefWidth(80);

        TextField i84TextField = new TextField(String.valueOf(entry.i84));
        i84TextField.textProperty().addListener((obs, oldText, newText) -> {
            if (i84TextField.getText().contains("-")) {
                return;
            }
            try {
                entry.i84 = Long.parseLong(newText);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

        HBox i84HBox = new HBox(i84Label, i84TextField);
        i84HBox.setAlignment(Pos.CENTER_LEFT);
        //i84

        //type 14
        VBox type14VBox = new VBox(30, 
            startTimeHBox, durationHBox, 
            i00HBox, i02HBox, 
            f04HBox, i08HBox, 
            f12HBox, i16HBox, 
            f20HBox, i24HBox, 
            f28HBox, i32HBox, 
            i36HBox, i40HBox, 
            f44HBox, i48HBox, 
            f52HBox, i56HBox, 
            f60HBox, i64HBox, 
            f68HBox, i72HBox, 
            i76HBox, i80HBox, 
            i84HBox
        );
        type14VBox.setPadding(new Insets(20,0,20,16));

        Tab type14Tab = new Tab("Type 14", new ScrollPane(type14VBox));
        type14Tab.setClosable(false);
        
        tabPane.getTabs().add(type14Tab);
    }

    private void entriesActionListener() {
        addSubEntry.getItems().addAll(collisionMenuItem, expirationMenuItem, type0MenuItem, type1MenuItem, type2MenuItem, type3MenuItem, type4MenuItem, type6MenuItem, type7MenuItem, type8MenuItem, type10MenuItem, type12MenuItem, type13MenuItem, type14MenuItem);

        copiedItem.setVisible(false);
        copiedItem.setDisable(true);
        pasteItem.setVisible(false);
        addItemCopy.setVisible(false);
        
        contextMenu.getItems().addAll(addEntry, addSubEntry, copy, delete, addComment, noCopiedItemFound, copiedItem, pasteItem, addItemCopy);

        treeView.setContextMenu(contextMenu);
        treeView.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            currentEntry = newValue;
            grandParentEntry = newValue;
            
            try {
                while (grandParentEntry.getParent() != treeView.getRoot()) {
                    grandParentEntry = grandParentEntry.getParent();
                }
            } catch (NullPointerException e) {
                return;
            }
            
            addComment.setDisable(true);
            noCopiedItemFound.setDisable(true);
            pasteItem.setDisable(true);

            if (newValue == null) return;

            if (newValue.getValue().contains("Entry") && newValue.getParent() == treeView.getRoot()) {
                int index = tabPane.getSelectionModel().getSelectedIndex();

                tabPane.getTabs().clear();

                createBsaMain(bsaMainHashMap.get(newValue));

                tabPane.getSelectionModel().select(index);

                addComment.setDisable(false);

                if (pasteItem.getText().equals("Paste Entry  Ctrl+V")) {
                    pasteItem.setDisable(false);
                }
            }
            else if (newValue.getParent().getValue().equals("Collision (After Effects)")) {
                int index = tabPane.getSelectionModel().getSelectedIndex();

                tabPane.getTabs().clear();

                createBsaCollision(bsaCollisionHashMap.get(newValue));

                tabPane.getSelectionModel().select(index);
                
                if (pasteItem.getText().contains("Paste Collision  Ctrl+V")) {
                    pasteItem.setDisable(false);
                }
            }
            else if (newValue.getParent().getValue().equals("Expiration (After Effects)")) {
                int index = tabPane.getSelectionModel().getSelectedIndex();
                
                tabPane.getTabs().clear();

                createBsaExpiration(bsaExpirationHashMap.get(newValue));

                tabPane.getSelectionModel().select(index);

                if (pasteItem.getText().contains("Paste Expiration  Ctrl+V")) {
                    pasteItem.setDisable(false);
                }
            }
            else if (newValue.getParent().getValue().equals("BSA Entry Passing")) {
                int index = tabPane.getSelectionModel().getSelectedIndex();

                tabPane.getTabs().clear();

                createBsaType0(bsaType0HashMap.get(newValue));

                tabPane.getSelectionModel().select(index);

                if (pasteItem.getText().contains("Paste BSA Entry Passing  Ctrl+V")) {
                    pasteItem.setDisable(false);
                }
            }
            else if (newValue.getParent().getValue().equals("Movement")) {
                int index = tabPane.getSelectionModel().getSelectedIndex();

                tabPane.getTabs().clear();

                createBsaType1(bsaType1HashMap.get(newValue));

                tabPane.getSelectionModel().select(index);

                if (pasteItem.getText().contains("Movement  Ctrl+V")) {
                    pasteItem.setDisable(false);
                }
            }
            else if (newValue.getParent().getValue().equals("BSA Type 2")) {
                int index = tabPane.getSelectionModel().getSelectedIndex();

                tabPane.getTabs().clear();

                createBsaType2(bsaType2HashMap.get(newValue));

                tabPane.getSelectionModel().select(index);

                if (pasteItem.getText().contains("BSA Type 2  Ctrl+V")) {
                    pasteItem.setDisable(false);
                }
            }
            else if (newValue.getParent().getValue().equals("Hitbox")) {
                int index = tabPane.getSelectionModel().getSelectedIndex();

                tabPane.getTabs().clear();

                createBsaType3(bsaType3HashMap.get(newValue));

                tabPane.getSelectionModel().select(index);

                if (pasteItem.getText().contains("Hitbox  Ctrl+V")) {
                    pasteItem.setDisable(false);
                }
            }
            else if (newValue.getParent().getValue().equals("Deflection")) {

                int index = tabPane.getSelectionModel().getSelectedIndex();

                tabPane.getTabs().clear();

                createBsaType4(bsaType4HashMap.get(newValue));

                tabPane.getSelectionModel().select(index);

                if (pasteItem.getText().contains("Deflection  Ctrl+V")) {
                    pasteItem.setDisable(false);
                }
            }
            else if (newValue.getParent().getValue().equals("Effect")) {
                int index = tabPane.getSelectionModel().getSelectedIndex();

                tabPane.getTabs().clear();

                createBsaType6(bsaType6HashMap.get(newValue));

                tabPane.getSelectionModel().select(index);

                if (pasteItem.getText().contains("Effect  Ctrl+V")) {
                    pasteItem.setDisable(false);
                }
            }
            else if (newValue.getParent().getValue().equals("Sound")) {
                int index = tabPane.getSelectionModel().getSelectedIndex();

                tabPane.getTabs().clear();

                createBsaType7(bsaType7HashMap.get(newValue));

                tabPane.getSelectionModel().select(index);

                if (pasteItem.getText().contains("Sound  Ctrl+V")) {
                    pasteItem.setDisable(false);
                }
            }
            else if (newValue.getParent().getValue().equals("Screen Effect")) {
                int index = tabPane.getSelectionModel().getSelectedIndex();

                tabPane.getTabs().clear();

                createBsaType8(bsaType8HashMap.get(newValue));

                tabPane.getSelectionModel().select(index);

                if (pasteItem.getText().contains("Screen Effect  Ctrl+V")) {
                    pasteItem.setDisable(false);
                }
            }
            else if (newValue.getParent().getValue().equals("BSA Type 10")) {
                int index = tabPane.getSelectionModel().getSelectedIndex();

                tabPane.getTabs().clear();

                createBsaType10(bsaType10HashMap.get(newValue));

                tabPane.getSelectionModel().select(index);

                if (pasteItem.getText().contains("BSA Type 10  Ctrl+V")) {
                    pasteItem.setDisable(false);
                }
            }
            else if (newValue.getParent().getValue().equals("BSA Type 12")) {
                int index = tabPane.getSelectionModel().getSelectedIndex();

                tabPane.getTabs().clear();

                createBsaType12(bsaType12HashMap.get(newValue));

                tabPane.getSelectionModel().select(index);

                if (pasteItem.getText().contains("BSA Type 12  Ctrl+V")) {
                    pasteItem.setDisable(false);
                }
            }
            else if (newValue.getParent().getValue().equals("BSA Type 13")) {
                int index = tabPane.getSelectionModel().getSelectedIndex();

                tabPane.getTabs().clear();

                createBsaType13(bsaType13HashMap.get(newValue));

                tabPane.getSelectionModel().select(index);

                if (pasteItem.getText().contains("BSA Type 13  Ctrl+V")) {
                    pasteItem.setDisable(false);
                }
            }
            else if (newValue.getParent().getValue().equals("BSA Type 14")) {
                int index = tabPane.getSelectionModel().getSelectedIndex();

                tabPane.getTabs().clear();

                createBsaType14(bsaType14HashMap.get(newValue));

                tabPane.getSelectionModel().select(index);

                if (pasteItem.getText().contains("BSA Type 14  Ctrl+V")) {
                    pasteItem.setDisable(false);
                }
            }
            else {
                tabPane.getTabs().clear();
            } 

            if (newValue.getValue().equals("Collision (After Effects)") && pasteItem.getText().contains("Collision List")) {
                pasteItem.setDisable(false);
            }
            else if (newValue.getValue().equals("Expiration (After Effects)") && pasteItem.getText().contains("Expiration List")) {
                pasteItem.setDisable(false);
            }
            else if (newValue.getValue().equals("Movement") && pasteItem.getText().contains("Movement List")) {
                pasteItem.setDisable(false);
            }
            else if (newValue.getValue().equals("BSA Type 2") && pasteItem.getText().contains("Type 2 List")) {
                pasteItem.setDisable(false);
            }
            else if (newValue.getValue().equals("Hitbox") && pasteItem.getText().contains("Hitbox List")) {
                pasteItem.setDisable(false);
            }
            else if (newValue.getValue().equals("Deflection") && pasteItem.getText().contains("Deflection List")) {
                pasteItem.setDisable(false);
            }
            else if (newValue.getValue().equals("Effect") && pasteItem.getText().contains("Effect List")) {
                pasteItem.setDisable(false);
            }
            else if (newValue.getValue().equals("Sound") && pasteItem.getText().contains("Sound List")) {
                pasteItem.setDisable(false);
            }
            else if (newValue.getValue().equals("Screen Effect") && pasteItem.getText().contains("Screen Effect List")) {
                pasteItem.setDisable(false);
            }
            else if (newValue.getValue().equals("BSA Type 10") && pasteItem.getText().contains("Type 10 List")) {
                pasteItem.setDisable(false);
            }
            else if (newValue.getValue().equals("BSA Type 11") && pasteItem.getText().contains("Type 11 List")) {
                pasteItem.setDisable(false);
            }
            else if (newValue.getValue().equals("BSA Type 12") && pasteItem.getText().contains("Type 12 List")) {
                pasteItem.setDisable(false);
            }
            else if (newValue.getValue().equals("BSA Type 13") && pasteItem.getText().contains("Type 13 List")) {
                pasteItem.setDisable(false);
            }
            else if (newValue.getValue().equals("BSA Type 14") && pasteItem.getText().contains("Type 14 List")) {
                pasteItem.setDisable(false);
            }
        });
        treeView.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.SECONDARY) {
                contextMenu.setOnAction(event -> {
                    if (event.getTarget() == addEntry) {
                        AddEntry();
                    }
                    if (event.getTarget() == copy) {
                        Copy();
                    }
                    if (event.getTarget() == delete) {
                        Delete();
                    }
                    if (event.getTarget() == addComment) {
                        AddComment();
                    }
                    if (event.getTarget() == pasteItem) {
                        Paste();
                    }
                    if (event.getTarget() == addItemCopy) {
                        AddItemCopy();
                    }
                });
                addSubEntry.setOnAction(ev -> {
                    TreeItem<String> searchItem = grandParentEntry;
                    
                    switch (addSubEntry.getItems().indexOf(ev.getTarget())) {
                        case 0 -> {
                            boolean hasCollision = false;
                            while (searchItem != null) {
                                for (TreeItem<String> child : searchItem.getChildren()) {
                                    if (child.getValue().equals("Collision (After Effects)")) {
                                        if (searchItem == grandParentEntry) {
                                            hasCollision = true;
                                        }
                                    }
                                }
                                if (searchItem.previousSibling() == null) {
                                    break;
                                }
                                searchItem = searchItem.previousSibling();
                            }
                            if (hasCollision) {
                                TreeItem<String> getParent = grandParentEntry.getChildren().get(0);
                                TreeItem<String> newChild = new TreeItem<>("Entry " + getParent.getChildren().size());

                                getParent.getChildren().add(newChild);

                                bsaCollisionHashMap.put(newChild, new BsaCollisionEntry());

                                treeView.getSelectionModel().select(newChild);
                            } 
                            else {
                                grandParentEntry.getChildren().add(0, new TreeItem<>("Collision (After Effects)"));

                                TreeItem<String> newChild = new TreeItem<>("Entry " + 0);

                                grandParentEntry.getChildren().get(0).getChildren().add(newChild);

                                bsaCollisionHashMap.put(newChild, new BsaCollisionEntry());

                                sortTreeItems(grandParentEntry);

                                treeView.getSelectionModel().select(newChild);
                            }
                        }
                        case 1 -> {
                            boolean hasExpiration = false;
                            TreeItem<String> expirationIndex = new TreeItem<>();
                            while (searchItem != null) {
                                for (TreeItem<String> child : searchItem.getChildren()) {
                                    if (child.getValue().equals("Expiration (After Effects)")) {
                                        if (searchItem == grandParentEntry) {
                                            hasExpiration = true;
                                            expirationIndex = child;
                                        }
                                    }
                                }
                                if (searchItem.previousSibling() == null) {
                                    break;
                                }
                                searchItem = searchItem.previousSibling();
                            }
                            if (hasExpiration) {
                                TreeItem<String> getParent = expirationIndex;
                                TreeItem<String> newChild = new TreeItem<>("Entry " + getParent.getChildren().size());

                                getParent.getChildren().add(newChild);

                                bsaExpirationHashMap.put(newChild, new BsaExpirationEntry());

                                treeView.getSelectionModel().select(newChild);
                            } else {
                                grandParentEntry.getChildren().add(0, new TreeItem<>("Expiration (After Effects)"));

                                TreeItem<String> newChild = new TreeItem<>("Entry " + 0);

                                grandParentEntry.getChildren().get(0).getChildren().add(newChild);

                                bsaExpirationHashMap.put(newChild, new BsaExpirationEntry());

                                sortTreeItems(grandParentEntry);

                                treeView.getSelectionModel().select(newChild);
                            }
                        }
                        case 2 -> {
                            boolean hasType0 = false;
                            TreeItem<String> type0Index = new TreeItem<>();
                            while (searchItem != null) {
                                for (TreeItem<String> child : searchItem.getChildren()) {
                                    if (child.getValue().equals("BSA Entry Passing")) {
                                        if (searchItem == grandParentEntry) {
                                            hasType0 = true;
                                            type0Index = child;
                                        }
                                    }
                                }
                                if (searchItem.previousSibling() == null) {
                                    break;
                                }
                                searchItem = searchItem.previousSibling();
                            }
                            if (hasType0) {
                                TreeItem<String> getParent = type0Index;
                                TreeItem<String> newChild = new TreeItem<>("Entry " + getParent.getChildren().size());

                                getParent.getChildren().add(newChild);

                                bsaType0HashMap.put(newChild, new BsaType0Entry());

                                treeView.getSelectionModel().select(newChild);
                            } else {
                                grandParentEntry.getChildren().add(0, new TreeItem<>("BSA Entry Passing"));

                                TreeItem<String> newChild = new TreeItem<>("Entry " + 0);

                                grandParentEntry.getChildren().get(0).getChildren().add(newChild);

                                bsaType0HashMap.put(newChild, new BsaType0Entry());

                                sortTreeItems(grandParentEntry);

                                treeView.getSelectionModel().select(newChild);
                            }
                        }
                        case 3 -> {
                            boolean hasType1 = false;
                            TreeItem<String> type1Index = new TreeItem<>();
                            while (searchItem != null) {
                                for (TreeItem<String> child : searchItem.getChildren()) {
                                    if (child.getValue().equals("Movement")) {
                                        if (searchItem == grandParentEntry) {
                                            hasType1 = true;
                                            type1Index = child;
                                        }
                                    }
                                }
                                if (searchItem.previousSibling() == null) {
                                    break;
                                }
                                searchItem = searchItem.previousSibling();
                            }
                            if (hasType1) {
                                TreeItem<String> getParent = type1Index;
                                TreeItem<String> newChild = new TreeItem<>("Entry " + getParent.getChildren().size());

                                getParent.getChildren().add(newChild);

                                bsaType1HashMap.put(newChild, new BsaType1Entry());

                                treeView.getSelectionModel().select(newChild);
                            } 
                            else {
                                grandParentEntry.getChildren().add(0, new TreeItem<>("Movement"));

                                TreeItem<String> newChild = new TreeItem<>("Entry " + 0);

                                grandParentEntry.getChildren().get(0).getChildren().add(newChild);

                                bsaType1HashMap.put(newChild, new BsaType1Entry());

                                sortTreeItems(grandParentEntry);

                                treeView.getSelectionModel().select(newChild);
                            }
                        }
                        case 4 -> {
                            boolean hasType2 = false;
                            TreeItem<String> type2Index = new TreeItem<>();
                            while (searchItem != null) {
                                for (TreeItem<String> child : searchItem.getChildren()) {
                                    if (child.getValue().equals("BSA Type 2")) {
                                        if (searchItem == grandParentEntry) {
                                            hasType2 = true;
                                            type2Index = child;
                                        }
                                    }
                                }
                                if (searchItem.previousSibling() == null) {
                                    break;
                                }
                                searchItem = searchItem.previousSibling();
                            }
                            if (hasType2) {
                                TreeItem<String> getParent = type2Index;
                                TreeItem<String> newChild = new TreeItem<>("Entry " + getParent.getChildren().size());

                                getParent.getChildren().add(newChild);

                                bsaType2HashMap.put(newChild, new BsaType2Entry());

                                treeView.getSelectionModel().select(newChild);
                            } 
                            else {
                                grandParentEntry.getChildren().add(0, new TreeItem<>("BSA Type 2"));

                                TreeItem<String> newChild = new TreeItem<>("Entry " + 0);

                                grandParentEntry.getChildren().get(0).getChildren().add(newChild);

                                bsaType2HashMap.put(newChild, new BsaType2Entry());

                                sortTreeItems(grandParentEntry);

                                treeView.getSelectionModel().select(newChild);
                            }
                        }
                        case 5 -> {
                            boolean hasType3 = false;
                            TreeItem<String> type3Index = new TreeItem<>();
                            while (searchItem != null) {
                                for (TreeItem<String> child : searchItem.getChildren()) {
                                    if (child.getValue().equals("Hitbox")) {
                                        if (searchItem == grandParentEntry) {
                                            hasType3 = true;
                                            type3Index = child;
                                        }
                                    }
                                }
                                if (searchItem.previousSibling() == null) {
                                    break;
                                }
                                searchItem = searchItem.previousSibling();
                            }
                            if (hasType3) {
                                TreeItem<String> getParent = type3Index;
                                TreeItem<String> newChild = new TreeItem<>("Entry " + getParent.getChildren().size());

                                getParent.getChildren().add(newChild);

                                bsaType3HashMap.put(newChild, new BsaType3Entry());

                                treeView.getSelectionModel().select(newChild);
                            } 
                            else {
                                grandParentEntry.getChildren().add(0, new TreeItem<>("Hitbox"));

                                TreeItem<String> newChild = new TreeItem<>("Entry " + 0);

                                grandParentEntry.getChildren().get(0).getChildren().add(newChild);

                                bsaType3HashMap.put(newChild, new BsaType3Entry());

                                sortTreeItems(grandParentEntry);

                                treeView.getSelectionModel().select(newChild);
                            }
                        }
                        case 6 -> {
                            boolean hasType4 = false;
                            TreeItem<String> type4Index = new TreeItem<>();
                            while (searchItem != null) {
                                for (TreeItem<String> child : searchItem.getChildren()) {
                                    if (child.getValue().equals("Deflection")) {
                                        if (searchItem == grandParentEntry) {
                                            hasType4 = true;
                                            type4Index = child;
                                        }
                                    }
                                }
                                if (searchItem.previousSibling() == null) {
                                    break;
                                }
                                searchItem = searchItem.previousSibling();
                            }
                            if (hasType4) {
                                TreeItem<String> getParent = type4Index;
                                TreeItem<String> newChild = new TreeItem<>("Entry " + getParent.getChildren().size());

                                getParent.getChildren().add(newChild);

                                bsaType4HashMap.put(newChild, new BsaType4Entry());

                                treeView.getSelectionModel().select(newChild);
                            } 
                            else {
                                grandParentEntry.getChildren().add(0, new TreeItem<>("Deflection"));

                                TreeItem<String> newChild = new TreeItem<>("Entry " + 0);

                                grandParentEntry.getChildren().get(0).getChildren().add(newChild);

                                bsaType4HashMap.put(newChild, new BsaType4Entry());

                                sortTreeItems(grandParentEntry);

                                treeView.getSelectionModel().select(newChild);
                            }
                        }
                        case 7 -> {
                            boolean hasType6 = false;
                            TreeItem<String> type6Index = new TreeItem<>();
                            while (searchItem != null) {
                                for (TreeItem<String> child : searchItem.getChildren()) {
                                    if (child.getValue().equals("Effect")) {
                                        if (searchItem == grandParentEntry) {
                                            hasType6 = true;
                                            type6Index = child;
                                        }
                                    }
                                }
                                if (searchItem.previousSibling() == null) {
                                    break;
                                }
                                searchItem = searchItem.previousSibling();
                            }
                            if (hasType6) {
                                TreeItem<String> getParent = type6Index;
                                TreeItem<String> newChild = new TreeItem<>("Entry " + getParent.getChildren().size());

                                getParent.getChildren().add(newChild);

                                bsaType6HashMap.put(newChild, new BsaType6Entry());

                                treeView.getSelectionModel().select(newChild);
                            } 
                            else {
                                grandParentEntry.getChildren().add(0, new TreeItem<>("Effect"));

                                TreeItem<String> newChild = new TreeItem<>("Entry " + 0);

                                grandParentEntry.getChildren().get(0).getChildren().add(newChild);

                                bsaType6HashMap.put(newChild, new BsaType6Entry());

                                sortTreeItems(grandParentEntry);

                                treeView.getSelectionModel().select(newChild);
                            }
                        }
                        case 8 -> {
                            boolean hasType7 = false;
                            TreeItem<String> type7Index = new TreeItem<>();
                            while (searchItem != null) {
                                for (TreeItem<String> child : searchItem.getChildren()) {
                                    if (child.getValue().equals("Sound")) {
                                        if (searchItem == grandParentEntry) {
                                            hasType7 = true;
                                            type7Index = child;
                                        }
                                    }
                                }
                                if (searchItem.previousSibling() == null) {
                                    break;
                                }
                                searchItem = searchItem.previousSibling();
                            }
                            if (hasType7) {
                                TreeItem<String> getParent = type7Index;
                                TreeItem<String> newChild = new TreeItem<>("Entry " + getParent.getChildren().size());

                                getParent.getChildren().add(newChild);

                                bsaType7HashMap.put(newChild, new BsaType7Entry());

                                treeView.getSelectionModel().select(newChild);
                            } 
                            else {
                                grandParentEntry.getChildren().add(0, new TreeItem<>("Sound"));

                                TreeItem<String> newChild = new TreeItem<>("Entry " + 0);

                                grandParentEntry.getChildren().get(0).getChildren().add(newChild);

                                bsaType7HashMap.put(newChild, new BsaType7Entry());

                                sortTreeItems(grandParentEntry);

                                treeView.getSelectionModel().select(newChild);
                            }
                        }
                        case 9 -> {
                            boolean hasType8 = false;
                            TreeItem<String> type8Index = new TreeItem<>();
                            while (searchItem != null) {
                                for (TreeItem<String> child : searchItem.getChildren()) {
                                    if (child.getValue().equals("Screen Effect")) {
                                        if (searchItem == grandParentEntry) {
                                            hasType8 = true;
                                            type8Index = child;
                                        }
                                    }
                                }
                                if (searchItem.previousSibling() == null) {
                                    break;
                                }
                                searchItem = searchItem.previousSibling();
                            }
                            if (hasType8) {
                                TreeItem<String> getParent = type8Index;
                                TreeItem<String> newChild = new TreeItem<>("Entry " + getParent.getChildren().size());

                                getParent.getChildren().add(newChild);

                                bsaType8HashMap.put(newChild, new BsaType8Entry());

                                treeView.getSelectionModel().select(newChild);
                            } 
                            else {
                                grandParentEntry.getChildren().add(0, new TreeItem<>("Screen Effect"));

                                TreeItem<String> newChild = new TreeItem<>("Entry " + 0);

                                grandParentEntry.getChildren().get(0).getChildren().add(newChild);

                                bsaType8HashMap.put(newChild, new BsaType8Entry());

                                sortTreeItems(grandParentEntry);

                                treeView.getSelectionModel().select(newChild);
                            }
                        }
                        case 10 -> {
                            boolean hasType10 = false;
                            TreeItem<String> type10Index = new TreeItem<>();
                            while (searchItem != null) {
                                for (TreeItem<String> child : searchItem.getChildren()) {
                                    if (child.getValue().equals("BSA Type 10")) {
                                        if (searchItem == grandParentEntry) {
                                            hasType10 = true;
                                            type10Index = child;
                                        }
                                    }
                                }
                                if (searchItem.previousSibling() == null) {
                                    break;
                                }
                                searchItem = searchItem.previousSibling();
                            }
                            if (hasType10) {
                                TreeItem<String> getParent = type10Index;
                                TreeItem<String> newChild = new TreeItem<>("Entry " + getParent.getChildren().size());

                                getParent.getChildren().add(newChild);

                                bsaType10HashMap.put(newChild, new BsaType10Entry());

                                treeView.getSelectionModel().select(newChild);
                            } 
                            else {
                                grandParentEntry.getChildren().add(0, new TreeItem<>("BSA Type 10"));

                                TreeItem<String> newChild = new TreeItem<>("Entry " + 0);

                                grandParentEntry.getChildren().get(0).getChildren().add(newChild);

                                bsaType10HashMap.put(newChild, new BsaType10Entry());

                                sortTreeItems(grandParentEntry);

                                treeView.getSelectionModel().select(newChild);
                            }
                        }
                        case 11 -> {
                            boolean hasType12 = false;
                            TreeItem<String> type12Index = new TreeItem<>();
                            while (searchItem != null) {
                                for (TreeItem<String> child : searchItem.getChildren()) {
                                    if (child.getValue().equals("BSA Type 12")) {
                                        if (searchItem == grandParentEntry) {
                                            hasType12 = true;
                                            type12Index = child;
                                        }
                                    }
                                }
                                if (searchItem.previousSibling() == null) {
                                    break;
                                }
                                searchItem = searchItem.previousSibling();
                            }
                            if (hasType12) {
                                TreeItem<String> getParent = type12Index;
                                TreeItem<String> newChild = new TreeItem<>("Entry " + getParent.getChildren().size());

                                getParent.getChildren().add(newChild);

                                bsaType12HashMap.put(newChild, new BsaType12Entry());

                                treeView.getSelectionModel().select(newChild);
                            } 
                            else {
                                grandParentEntry.getChildren().add(0, new TreeItem<>("BSA Type 12"));

                                TreeItem<String> newChild = new TreeItem<>("Entry " + 0);

                                grandParentEntry.getChildren().get(0).getChildren().add(newChild);

                                bsaType12HashMap.put(newChild, new BsaType12Entry());

                                sortTreeItems(grandParentEntry);

                                treeView.getSelectionModel().select(newChild);
                            }
                        }
                        case 12 -> {
                            boolean hasType13 = false;
                            TreeItem<String> type13Index = new TreeItem<>();
                            while (searchItem != null) {
                                for (TreeItem<String> child : searchItem.getChildren()) {
                                    if (child.getValue().equals("BSA Type 13")) {
                                        if (searchItem == grandParentEntry) {
                                            hasType13 = true;
                                            type13Index = child;
                                        }
                                    }
                                }
                                if (searchItem.previousSibling() == null) {
                                    break;
                                }
                                searchItem = searchItem.previousSibling();
                            }
                            if (hasType13) {
                                TreeItem<String> getParent = type13Index;
                                TreeItem<String> newChild = new TreeItem<>("Entry " + getParent.getChildren().size());

                                getParent.getChildren().add(newChild);

                                bsaType13HashMap.put(newChild, new BsaType13Entry());

                                treeView.getSelectionModel().select(newChild);
                            } 
                            else {
                                grandParentEntry.getChildren().add(0, new TreeItem<>("BSA Type 13"));

                                TreeItem<String> newChild = new TreeItem<>("Entry " + 0);

                                grandParentEntry.getChildren().get(0).getChildren().add(newChild);

                                bsaType13HashMap.put(newChild, new BsaType13Entry());

                                sortTreeItems(grandParentEntry);

                                treeView.getSelectionModel().select(newChild);
                            }
                        }
                        case 13 -> {
                            boolean hasType14 = false;
                            TreeItem<String> type14Index = new TreeItem<>();
                            while (searchItem != null) {
                                for (TreeItem<String> child : searchItem.getChildren()) {
                                    if (child.getValue().equals("BSA Type 14")) {
                                        if (searchItem == grandParentEntry) {
                                            hasType14 = true;
                                            type14Index = child;
                                        }
                                    }
                                }
                                if (searchItem.previousSibling() == null) {
                                    break;
                                }
                                searchItem = searchItem.previousSibling();
                            }
                            if (hasType14) {
                                TreeItem<String> getParent = type14Index;
                                TreeItem<String> newChild = new TreeItem<>("Entry " + getParent.getChildren().size());

                                getParent.getChildren().add(newChild);

                                bsaType14HashMap.put(newChild, new BsaType14Entry());

                                treeView.getSelectionModel().select(newChild);
                            } 
                            else {
                                grandParentEntry.getChildren().add(0, new TreeItem<>("BSA Type 14"));

                                TreeItem<String> newChild = new TreeItem<>("Entry " + 0);

                                grandParentEntry.getChildren().get(0).getChildren().add(newChild);

                                bsaType14HashMap.put(newChild, new BsaType14Entry());

                                sortTreeItems(grandParentEntry);

                                treeView.getSelectionModel().select(newChild);
                            }
                        }
                    }
                });
            }
        });
    }

    private void entriesKyesListener() {
        treeView.setOnKeyPressed(e -> {
            if (e.isControlDown()&&e.getCode() == KeyCode.C) {
                Copy();
            }
            if (e.isControlDown()&&e.getCode() == KeyCode.V) {
                Paste();
            }
            if (e.getCode()==KeyCode.DELETE) {
                Delete();
            }
            if (e.isControlDown()&&e.getCode() == KeyCode.A) {
                AddItemCopy();
            }
            if (e.isControlDown()&&e.getCode() == KeyCode.Q) {
                AddComment();
            }
        });
    }

    private void AddEntry() {
        if (treeView.getSelectionModel().getSelectedIndex() < 0) return;

        allEntries.add(new TreeItem<>("Entry "+ allEntries.size()));

        BsaMainEntry bsaMainEntry = new BsaMainEntry();

        bsaMainHashMap.put(allEntries.getLast(), bsaMainEntry);

        treeView.getRoot().getChildren().add(allEntries.getLast());
    }


    private void Copy() {
        copiedItem.setText("Copied %s");
        pasteItem.setText("Paste %s  Ctrl+V");
        addItemCopy.setText("Add %s Copy  Ctrl+A");

        noCopiedItemFound.setVisible(false);
        copiedItem.setVisible(true);
        pasteItem.setVisible(true);
        addItemCopy.setVisible(true);

        pasteItem.setDisable(false);
        if (currentEntry.getParent() == treeView.getRoot()) {
            copiedItem.setText(String.format(copiedItem.getText(), "Entry"));
            pasteItem.setText(String.format(pasteItem.getText(), "Entry"));
            addItemCopy.setText(String.format(addItemCopy.getText(), "Entry"));

            copyListContainer.clear();
            copyTypesContainer.clear();

            copyContainer = new BsaMainEntry(bsaMainHashMap.get(currentEntry));
            
            for (int i = 0; i < currentEntry.getChildren().size(); i++) {
                switch (currentEntry.getChildren().get(i).getValue()) {
                    case "Collision (After Effects)" -> {
                        copyListContainer.add(new Object[currentEntry.getChildren().get(i).getChildren().size()]);
                        for (int j = 0; j <currentEntry.getChildren().get(i).getChildren().size(); j++ ) {
                            copyListContainer.get(i)[j] = new BsaCollisionEntry(bsaCollisionHashMap.get(currentEntry.getChildren().get(i).getChildren().get(j)));
                        }
                        copyTypesContainer.add("Collision (After Effects)");
                    }
                    case "Expiration (After Effects)" -> {
                        copyListContainer.add(new Object[currentEntry.getChildren().get(i).getChildren().size()]);
                        for (int j = 0; j <currentEntry.getChildren().get(i).getChildren().size(); j++ ) {
                            copyListContainer.get(i)[j] = new BsaExpirationEntry(bsaExpirationHashMap.get(currentEntry.getChildren().get(i).getChildren().get(j)));
                        }
                        copyTypesContainer.add("Expiration (After Effects)");
                    }
                    case "BSA Entry Passing" -> {
                        copyListContainer.add(new Object[currentEntry.getChildren().get(i).getChildren().size()]);
                        for (int j = 0; j <currentEntry.getChildren().get(i).getChildren().size(); j++ ) {
                            copyListContainer.get(i)[j] = new BsaType0Entry(bsaType0HashMap.get(currentEntry.getChildren().get(i).getChildren().get(j)));
                        }
                        copyTypesContainer.add("BSA Entry Passing");
                    }
                    case "Movement" -> {
                        copyListContainer.add(new Object[currentEntry.getChildren().get(i).getChildren().size()]);
                        for (int j = 0; j <currentEntry.getChildren().get(i).getChildren().size(); j++ ) {
                            copyListContainer.get(i)[j] = new BsaType1Entry(bsaType1HashMap.get(currentEntry.getChildren().get(i).getChildren().get(j)));
                        }
                        copyTypesContainer.add("Movement");
                    }
                    case "BSA Type 2" -> {
                        copyListContainer.add(new Object[currentEntry.getChildren().get(i).getChildren().size()]);
                        for (int j = 0; j <currentEntry.getChildren().get(i).getChildren().size(); j++ ) {
                            copyListContainer.get(i)[j] = new BsaType2Entry(bsaType2HashMap.get(currentEntry.getChildren().get(i).getChildren().get(j)));
                        }
                        copyTypesContainer.add("BSA Type 2");
                    }
                    case "Hitbox" -> {
                        copyListContainer.add(new Object[currentEntry.getChildren().get(i).getChildren().size()]);
                        for (int j = 0; j <currentEntry.getChildren().get(i).getChildren().size(); j++ ) {
                            copyListContainer.get(i)[j] = new BsaType3Entry(bsaType3HashMap.get(currentEntry.getChildren().get(i).getChildren().get(j)));
                        }
                        copyTypesContainer.add("Hitbox");
                    }
                    case "Deflection" -> {
                        copyListContainer.add(new Object[currentEntry.getChildren().get(i).getChildren().size()]);
                        for (int j = 0; j <currentEntry.getChildren().get(i).getChildren().size(); j++ ) {
                            copyListContainer.get(i)[j] = new BsaType4Entry(bsaType4HashMap.get(currentEntry.getChildren().get(i).getChildren().get(j)));
                        }
                        copyTypesContainer.add("Deflection");
                    }
                    case "Effect" -> {
                        copyListContainer.add(new Object[currentEntry.getChildren().get(i).getChildren().size()]);
                        for (int j = 0; j <currentEntry.getChildren().get(i).getChildren().size(); j++ ) {
                            copyListContainer.get(i)[j] = new BsaType6Entry(bsaType6HashMap.get(currentEntry.getChildren().get(i).getChildren().get(j)));
                        }
                        copyTypesContainer.add("Effect");
                    }
                    case "Sound" -> {
                        copyListContainer.add(new Object[currentEntry.getChildren().get(i).getChildren().size()]);
                        for (int j = 0; j <currentEntry.getChildren().get(i).getChildren().size(); j++ ) {
                            copyListContainer.get(i)[j] = new BsaType7Entry(bsaType7HashMap.get(currentEntry.getChildren().get(i).getChildren().get(j)));
                        }
                        copyTypesContainer.add("Sound");
                    }
                    case "Screen Effect" -> {
                        copyListContainer.add(new Object[currentEntry.getChildren().get(i).getChildren().size()]);
                        for (int j = 0; j <currentEntry.getChildren().get(i).getChildren().size(); j++ ) {
                            copyListContainer.get(i)[j] = new BsaType8Entry(bsaType8HashMap.get(currentEntry.getChildren().get(i).getChildren().get(j)));
                        }
                        copyTypesContainer.add("Screen Effect");
                    }
                    case "BSA Type 10" -> {
                        copyListContainer.add(new Object[currentEntry.getChildren().get(i).getChildren().size()]);
                        for (int j = 0; j <currentEntry.getChildren().get(i).getChildren().size(); j++ ) {
                            copyListContainer.get(i)[j] = new BsaType10Entry(bsaType10HashMap.get(currentEntry.getChildren().get(i).getChildren().get(j)));
                        }
                        copyTypesContainer.add("BSA Type 10");
                    }
                    case "BSA Type 12" -> {
                        copyListContainer.add(new Object[currentEntry.getChildren().get(i).getChildren().size()]);
                        for (int j = 0; j <currentEntry.getChildren().get(i).getChildren().size(); j++ ) {
                            copyListContainer.get(i)[j] = new BsaType12Entry(bsaType12HashMap.get(currentEntry.getChildren().get(i).getChildren().get(j)));
                        }
                        copyTypesContainer.add("BSA Type 12");
                    }
                    case "BSA Type 13" -> {
                        copyListContainer.add(new Object[currentEntry.getChildren().get(i).getChildren().size()]);
                        for (int j = 0; j <currentEntry.getChildren().get(i).getChildren().size(); j++ ) {
                            copyListContainer.get(i)[j] = new BsaType13Entry(bsaType13HashMap.get(currentEntry.getChildren().get(i).getChildren().get(j)));
                        }
                        copyTypesContainer.add("BSA Type 13");
                    }
                    case "BSA Type 14" -> {
                        copyListContainer.add(new Object[currentEntry.getChildren().get(i).getChildren().size()]);
                        for (int j = 0; j <currentEntry.getChildren().get(i).getChildren().size(); j++ ) {
                            copyListContainer.get(i)[j] = new BsaType14Entry(bsaType14HashMap.get(currentEntry.getChildren().get(i).getChildren().get(j)));
                        }
                        copyTypesContainer.add("BSA Type 14");
                    }
                }
            }
        }
        else if (currentEntry.getChildren().isEmpty()) {
            switch (currentEntry.getParent().getValue()) {
                case "Collision (After Effects)" -> {
                    copyContainer = new BsaCollisionEntry(bsaCollisionHashMap.get(currentEntry));

                    copiedItem.setText(String.format(copiedItem.getText(), "Collision"));
                    pasteItem.setText(String.format(pasteItem.getText(), "Collision"));
                    addItemCopy.setText(String.format(addItemCopy.getText(), "Collision"));
                }
                case "Expiration (After Effects)" -> {
                    copyContainer = new BsaExpirationEntry(bsaExpirationHashMap.get(currentEntry));

                    copiedItem.setText(String.format(copiedItem.getText(), "Expiration"));
                    pasteItem.setText(String.format(pasteItem.getText(), "Expiration"));
                    addItemCopy.setText(String.format(addItemCopy.getText(), "Expiration"));
                }
                case "BSA Entry Passing" -> {
                    copyContainer = new BsaType0Entry(bsaType0HashMap.get(currentEntry));

                    copiedItem.setText(String.format(copiedItem.getText(), "BSA Entry Passing"));
                    pasteItem.setText(String.format(pasteItem.getText(), "BSA Entry Passing"));
                    addItemCopy.setText(String.format(addItemCopy.getText(), "BSA Entry Passing"));
                }
                case "Movement" -> {
                    copyContainer = new BsaType1Entry(bsaType1HashMap.get(currentEntry));

                    copiedItem.setText(String.format(copiedItem.getText(), "Movement"));
                    pasteItem.setText(String.format(pasteItem.getText(), "Movement"));
                    addItemCopy.setText(String.format(addItemCopy.getText(), "Movement"));
                }
                case "BSA Type 2" -> {
                    copyContainer = new BsaType2Entry(bsaType2HashMap.get(currentEntry));

                    copiedItem.setText(String.format(copiedItem.getText(), "BSA Type 2"));
                    pasteItem.setText(String.format(pasteItem.getText(), "BSA Type 2"));
                    addItemCopy.setText(String.format(addItemCopy.getText(), "BSA Type 2"));
                }
                case "Hitbox" -> {
                    copyContainer = new BsaType3Entry(bsaType3HashMap.get(currentEntry));

                    copiedItem.setText(String.format(copiedItem.getText(), "Hitbox"));
                    pasteItem.setText(String.format(pasteItem.getText(), "Hitbox"));
                    addItemCopy.setText(String.format(addItemCopy.getText(), "Hitbox"));
                }
                case "Deflection" -> {
                    copyContainer = new BsaType4Entry(bsaType4HashMap.get(currentEntry));

                    copiedItem.setText(String.format(copiedItem.getText(), "Deflection"));
                    pasteItem.setText(String.format(pasteItem.getText(), "Deflection"));
                    addItemCopy.setText(String.format(addItemCopy.getText(), "Deflection"));
                }
                case "Effect" -> {
                    copyContainer = new BsaType6Entry(bsaType6HashMap.get(currentEntry));

                    copiedItem.setText(String.format(copiedItem.getText(), "Effect"));
                    pasteItem.setText(String.format(pasteItem.getText(), "Effect"));
                    addItemCopy.setText(String.format(addItemCopy.getText(), "Effect"));
                }
                case "Sound" -> {
                    copyContainer = new BsaType7Entry(bsaType7HashMap.get(currentEntry));

                    copiedItem.setText(String.format(copiedItem.getText(), "Sound"));
                    pasteItem.setText(String.format(pasteItem.getText(), "Sound"));
                    addItemCopy.setText(String.format(addItemCopy.getText(), "Sound"));
                }
                case "Screen Effect" -> {
                    copyContainer = new BsaType8Entry(bsaType8HashMap.get(currentEntry));

                    copiedItem.setText(String.format(copiedItem.getText(), "Screen Effect"));
                    pasteItem.setText(String.format(pasteItem.getText(), "Screen Effect"));
                    addItemCopy.setText(String.format(addItemCopy.getText(), "Screen Effect"));
                }
                case "BSA Type 10" -> {
                    copyContainer = new BsaType10Entry(bsaType10HashMap.get(currentEntry));

                    copiedItem.setText(String.format(copiedItem.getText(), "BSA Type 10"));
                    pasteItem.setText(String.format(pasteItem.getText(), "BSA Type 10"));
                    addItemCopy.setText(String.format(addItemCopy.getText(), "BSA Type 10"));
                }
                case "BSA Type 12" -> {
                    copyContainer = new BsaType12Entry(bsaType12HashMap.get(currentEntry));

                    copiedItem.setText(String.format(copiedItem.getText(), "BSA Type 12"));
                    pasteItem.setText(String.format(pasteItem.getText(), "BSA Type 12"));
                    addItemCopy.setText(String.format(addItemCopy.getText(), "BSA Type 12"));
                }
                case "BSA Type 13" -> {
                    copyContainer = new BsaType13Entry(bsaType13HashMap.get(currentEntry));

                    copiedItem.setText(String.format(copiedItem.getText(), "BSA Type 13"));
                    pasteItem.setText(String.format(pasteItem.getText(), "BSA Type 13"));
                    addItemCopy.setText(String.format(addItemCopy.getText(), "BSA Type 13"));
                }
                case "BSA Type 14" -> {
                    copyContainer = new BsaType14Entry(bsaType14HashMap.get(currentEntry));

                    copiedItem.setText(String.format(copiedItem.getText(), "BSA Type 14"));
                    pasteItem.setText(String.format(pasteItem.getText(), "BSA Type 14"));
                    addItemCopy.setText(String.format(addItemCopy.getText(), "BSA Type 14"));
                }
            }
        }
        else {
            copyListContainer.clear();
            copyListContainer.add(new Object[currentEntry.getChildren().size()]);
            switch (currentEntry.getValue()) {
                case "Collision (After Effects)" -> {
                    for (TreeItem<String> child : currentEntry.getChildren()) {
                        copyListContainer.get(0)[currentEntry.getChildren().indexOf(child)] = new BsaCollisionEntry(bsaCollisionHashMap.get(child));
                    }

                    copiedItem.setText(String.format(copiedItem.getText(), "Collision List"));
                    pasteItem.setText(String.format(pasteItem.getText(), "Collision List"));
                    addItemCopy.setText(String.format(addItemCopy.getText(), "Collision List"));
                }
                case "Expiration (After Effects)" -> {
                    for (TreeItem<String> child : currentEntry.getChildren()) {
                        copyListContainer.get(0)[currentEntry.getChildren().indexOf(child)] = new BsaExpirationEntry(bsaExpirationHashMap.get(child));
                    }

                    copiedItem.setText(String.format(copiedItem.getText(), "Expiration List"));
                    pasteItem.setText(String.format(pasteItem.getText(), "Expiration List"));
                    addItemCopy.setText(String.format(addItemCopy.getText(), "Expiration"));
                }
                case "BSA Entry Passing" -> {
                    for (TreeItem<String> child : currentEntry.getChildren()) {
                        copyListContainer.get(0)[currentEntry.getChildren().indexOf(child)] = new BsaType0Entry(bsaType0HashMap.get(child));
                    }

                    copiedItem.setText(String.format(copiedItem.getText(), "BSA Entry Passing List"));
                    pasteItem.setText(String.format(pasteItem.getText(), "BSA Entry Passing List"));
                    addItemCopy.setText(String.format(addItemCopy.getText(), "BSA Entry Passing List"));
                }
                case "Movement" -> {
                    for (TreeItem<String> child : currentEntry.getChildren()) {
                        copyListContainer.get(0)[currentEntry.getChildren().indexOf(child)] = new BsaType1Entry(bsaType1HashMap.get(child));
                    }

                    copiedItem.setText(String.format(copiedItem.getText(), "Movement List"));
                    pasteItem.setText(String.format(pasteItem.getText(), "Movement List"));
                    addItemCopy.setText(String.format(addItemCopy.getText(), "Movement List"));
                }
                case "BSA Type 2" -> {
                    for (TreeItem<String> child : currentEntry.getChildren()) {
                        copyListContainer.get(0)[currentEntry.getChildren().indexOf(child)] = new BsaType2Entry(bsaType2HashMap.get(child));
                    }

                    copiedItem.setText(String.format(copiedItem.getText(), "BSA Type 2 List"));
                    pasteItem.setText(String.format(pasteItem.getText(), "BSA Type 2 List"));
                    addItemCopy.setText(String.format(addItemCopy.getText(), "BSA Type 2 List"));
                }
                case "Hitbox" -> {
                    for (TreeItem<String> child : currentEntry.getChildren()) {
                        copyListContainer.get(0)[currentEntry.getChildren().indexOf(child)] = new BsaType3Entry(bsaType3HashMap.get(child));
                    }

                    copiedItem.setText(String.format(copiedItem.getText(), "Hitbox List"));
                    pasteItem.setText(String.format(pasteItem.getText(), "Hitbox List"));
                    addItemCopy.setText(String.format(addItemCopy.getText(), "Hitbox List"));
                }
                case "Deflection" -> {
                    for (TreeItem<String> child : currentEntry.getChildren()) {
                        copyListContainer.get(0)[currentEntry.getChildren().indexOf(child)] = new BsaType4Entry(bsaType4HashMap.get(child));
                    }

                    copiedItem.setText(String.format(copiedItem.getText(), "Deflection List"));
                    pasteItem.setText(String.format(pasteItem.getText(), "Deflection List"));
                    addItemCopy.setText(String.format(addItemCopy.getText(), "Deflection List"));
                }
                case "Effect" -> {
                    for (TreeItem<String> child : currentEntry.getChildren()) {
                        copyListContainer.get(0)[currentEntry.getChildren().indexOf(child)] = new BsaType6Entry(bsaType6HashMap.get(child));
                    }

                    copiedItem.setText(String.format(copiedItem.getText(), "Effect List"));
                    pasteItem.setText(String.format(pasteItem.getText(), "Effect List"));
                    addItemCopy.setText(String.format(addItemCopy.getText(), "Effect List"));
                }
                case "Sound" -> {
                    for (TreeItem<String> child : currentEntry.getChildren()) {
                        copyListContainer.get(0)[currentEntry.getChildren().indexOf(child)] = new BsaType7Entry(bsaType7HashMap.get(child));
                    }

                    copiedItem.setText(String.format(copiedItem.getText(), "Sound List"));
                    pasteItem.setText(String.format(pasteItem.getText(), "Sound List"));
                    addItemCopy.setText(String.format(addItemCopy.getText(), "Sound List"));
                }
                case "Screen Effect" -> {
                    for (TreeItem<String> child : currentEntry.getChildren()) {
                        copyListContainer.get(0)[currentEntry.getChildren().indexOf(child)] = new BsaType8Entry(bsaType8HashMap.get(child));
                    }

                    copiedItem.setText(String.format(copiedItem.getText(), "Screen Effect List"));
                    pasteItem.setText(String.format(pasteItem.getText(), "Screen Effect List"));
                    addItemCopy.setText(String.format(addItemCopy.getText(), "Screen Effect List"));
                }
                case "BSA Type 10" -> {
                    for (TreeItem<String> child : currentEntry.getChildren()) {
                        copyListContainer.get(0)[currentEntry.getChildren().indexOf(child)] = new BsaType10Entry(bsaType10HashMap.get(child));
                    }

                    copiedItem.setText(String.format(copiedItem.getText(), "BSA Type 10 List"));
                    pasteItem.setText(String.format(pasteItem.getText(), "BSA Type 10 List"));
                    addItemCopy.setText(String.format(addItemCopy.getText(), "BSA Type 10 List"));
                }
                case "BSA Type 12" -> {
                    for (TreeItem<String> child : currentEntry.getChildren()) {
                        copyListContainer.get(0)[currentEntry.getChildren().indexOf(child)] = new BsaType12Entry(bsaType12HashMap.get(child));
                    }

                    copiedItem.setText(String.format(copiedItem.getText(), "BSA Type 12 List"));
                    pasteItem.setText(String.format(pasteItem.getText(), "BSA Type 12 List"));
                    addItemCopy.setText(String.format(addItemCopy.getText(), "BSA Type 12 List"));
                }
                case "BSA Type 13" -> {
                    for (TreeItem<String> child : currentEntry.getChildren()) {
                        copyListContainer.get(0)[currentEntry.getChildren().indexOf(child)] = new BsaType13Entry(bsaType13HashMap.get(child));
                    }

                    copiedItem.setText(String.format(copiedItem.getText(), "BSA Type 13 List"));
                    pasteItem.setText(String.format(pasteItem.getText(), "BSA Type 13 List"));
                    addItemCopy.setText(String.format(addItemCopy.getText(), "BSA Type 13 List"));
                }
                case "BSA Type 14" -> {
                    for (TreeItem<String> child : currentEntry.getChildren()) {
                        copyListContainer.get(0)[currentEntry.getChildren().indexOf(child)] = new BsaType14Entry(bsaType14HashMap.get(child));
                    }

                    copiedItem.setText(String.format(copiedItem.getText(), "BSA Type 14 List"));
                    pasteItem.setText(String.format(pasteItem.getText(), "BSA Type 14 List"));
                    addItemCopy.setText(String.format(addItemCopy.getText(), "BSA Type 14 List"));
                }
            }
        }
    }

    private void Paste() {
        if (currentEntry.getParent() == treeView.getRoot()) {
            for (TreeItem<String> parent : grandParentEntry.getChildren()) {
                switch (parent.getValue()) {
                    case "Collision (After Effects)" -> {
                        for (TreeItem<String> child : parent.getChildren()) {
                            bsaCollisionHashMap.remove(child);
                        }
                    }
                    case "Expiration (After Effects)" -> {
                        for (TreeItem<String> child : parent.getChildren()) {
                            bsaExpirationHashMap.remove(child);
                        }
                    }
                    case "BSA Entry Passing" -> {
                        for (TreeItem<String> child : parent.getChildren()) {
                            bsaType0HashMap.remove(child);
                        }
                    }
                    case "Movement" -> {
                        for (TreeItem<String> child : parent.getChildren()) {
                            bsaType1HashMap.remove(child);
                        }
                    }
                    case "BSA Type 2" -> {
                        for (TreeItem<String> child : parent.getChildren()) {
                            bsaType2HashMap.remove(child);
                        }
                    }
                    case "Hitbox" -> {
                        for (TreeItem<String> child : parent.getChildren()) {
                            bsaType3HashMap.remove(child);
                        }
                    }
                    case "Deflection" -> {
                        for (TreeItem<String> child : parent.getChildren()) {
                            bsaType4HashMap.remove(child);
                        }
                    }
                    case "Effect" -> {
                        for (TreeItem<String> child : parent.getChildren()) {
                            bsaType6HashMap.remove(child);
                        }
                    }
                    case "Sound" -> {
                        for (TreeItem<String> child : parent.getChildren()) {
                            bsaType7HashMap.remove(child);
                        }
                    }
                    case "Screen Effect" -> {
                        for (TreeItem<String> child : parent.getChildren()) {
                            bsaType8HashMap.remove(child);
                        }
                    }
                    case "BSA Type 10" -> {
                        for (TreeItem<String> child : parent.getChildren()) {
                            bsaType10HashMap.remove(child);
                        }
                    }
                    case "BSA Type 12" -> {
                        for (TreeItem<String> child : parent.getChildren()) {
                            bsaType12HashMap.remove(child);
                        }
                    }
                    case "BSA Type 13" -> {
                        for (TreeItem<String> child : parent.getChildren()) {
                            bsaType13HashMap.remove(child);
                        }
                    }
                    case "BSA Type 14" -> {
                        for (TreeItem<String> child : parent.getChildren()) {
                            bsaType14HashMap.remove(child);
                        }
                    }
                }
            }

            currentEntry.getChildren().clear();

            bsaMainHashMap.put(currentEntry, (BsaMainEntry) copyContainer);

            for (int i = 0; i < copyTypesContainer.size(); i++) {
                switch (copyTypesContainer.get(i)) {
                    case "Collision (After Effects)" -> {
                        currentEntry.getChildren().add(i, new TreeItem<>("Collision (After Effects)"));

                        for (int j = 0; j < copyListContainer.get(i).length; j++) {
                            currentEntry.getChildren().get(i).getChildren().add(j, new TreeItem<>("Entry " +j));

                            bsaCollisionHashMap.put(currentEntry.getChildren().get(i).getChildren().get(j), (BsaCollisionEntry) copyListContainer.get(i)[j]);
                        }
                    }
                    case "Expiration (After Effects)" -> {
                        currentEntry.getChildren().add(i, new TreeItem<>("Expiration (After Effects)"));

                        for (int j = 0; j < copyListContainer.get(i).length; j++) {
                            currentEntry.getChildren().get(i).getChildren().add(j, new TreeItem<>("Entry " +j));

                            bsaExpirationHashMap.put(currentEntry.getChildren().get(i).getChildren().get(j), (BsaExpirationEntry) copyListContainer.get(i)[j]);
                        }
                    }
                    case "BSA Entry Passing" -> {
                        currentEntry.getChildren().add(i, new TreeItem<>("BSA Entry Passing"));

                        for (int j = 0; j < copyListContainer.get(i).length; j++) {
                            currentEntry.getChildren().get(i).getChildren().add(j, new TreeItem<>("Entry " +j));

                            bsaType0HashMap.put(currentEntry.getChildren().get(i).getChildren().get(j), (BsaType0Entry) copyListContainer.get(i)[j]);
                        }
                    }
                    case "Movement" -> {
                        currentEntry.getChildren().add(i, new TreeItem<>("Movement"));

                        for (int j = 0; j < copyListContainer.get(i).length; j++) {
                            currentEntry.getChildren().get(i).getChildren().add(j, new TreeItem<>("Entry " +j));

                            bsaType1HashMap.put(currentEntry.getChildren().get(i).getChildren().get(j), (BsaType1Entry) copyListContainer.get(i)[j]);
                        }
                    }
                    case "BSA Type 2" -> {
                        currentEntry.getChildren().add(i, new TreeItem<>("BSA Type 2"));

                        for (int j = 0; j < copyListContainer.get(i).length; j++) {
                            currentEntry.getChildren().get(i).getChildren().add(j, new TreeItem<>("Entry " +j));

                            bsaType2HashMap.put(currentEntry.getChildren().get(i).getChildren().get(j), (BsaType2Entry) copyListContainer.get(i)[j]);
                        }
                    }
                    case "Hitbox" -> {
                        currentEntry.getChildren().add(i, new TreeItem<>("Hitbox"));

                        for (int j = 0; j < copyListContainer.get(i).length; j++) {
                            currentEntry.getChildren().get(i).getChildren().add(j, new TreeItem<>("Entry " +j));

                            bsaType3HashMap.put(currentEntry.getChildren().get(i).getChildren().get(j), (BsaType3Entry) copyListContainer.get(i)[j]);
                        }
                    }
                    case "Deflection" -> {
                        currentEntry.getChildren().add(i, new TreeItem<>("Deflection"));

                        for (int j = 0; j < copyListContainer.get(i).length; j++) {
                            currentEntry.getChildren().get(i).getChildren().add(j, new TreeItem<>("Entry " +j));

                            bsaType4HashMap.put(currentEntry.getChildren().get(i).getChildren().get(j), (BsaType4Entry) copyListContainer.get(i)[j]);
                        }
                    }
                    case "Effect" -> {
                        currentEntry.getChildren().add(i, new TreeItem<>("Effect"));

                        for (int j = 0; j < copyListContainer.get(i).length; j++) {
                            currentEntry.getChildren().get(i).getChildren().add(j, new TreeItem<>("Entry " +j));

                            bsaType6HashMap.put(currentEntry.getChildren().get(i).getChildren().get(j), (BsaType6Entry) copyListContainer.get(i)[j]);
                        }
                    }
                    case "Sound" -> {
                        currentEntry.getChildren().add(i, new TreeItem<>("Sound"));

                        for (int j = 0; j < copyListContainer.get(i).length; j++) {
                            currentEntry.getChildren().get(i).getChildren().add(j, new TreeItem<>("Entry " +j));

                            bsaType7HashMap.put(currentEntry.getChildren().get(i).getChildren().get(j), (BsaType7Entry) copyListContainer.get(i)[j]);
                        }
                    }
                    case "Screen Effect" -> {
                        currentEntry.getChildren().add(i, new TreeItem<>("Screen Effect"));

                        for (int j = 0; j < copyListContainer.get(i).length; j++) {
                            currentEntry.getChildren().get(i).getChildren().add(j, new TreeItem<>("Entry " +j));

                            bsaType8HashMap.put(currentEntry.getChildren().get(i).getChildren().get(j), (BsaType8Entry) copyListContainer.get(i)[j]);
                        }
                    }
                    case "BSA Type 10" -> {
                        currentEntry.getChildren().add(i, new TreeItem<>("BSA Type 10"));

                        for (int j = 0; j < copyListContainer.get(i).length; j++) {
                            currentEntry.getChildren().get(i).getChildren().add(j, new TreeItem<>("Entry " +j));

                            bsaType10HashMap.put(currentEntry.getChildren().get(i).getChildren().get(j), (BsaType10Entry) copyListContainer.get(i)[j]);
                        }
                    }
                    case "BSA Type 12" -> {
                        currentEntry.getChildren().add(i, new TreeItem<>("BSA Type 12"));

                        for (int j = 0; j < copyListContainer.get(i).length; j++) {
                            currentEntry.getChildren().get(i).getChildren().add(j, new TreeItem<>("Entry " +j));

                            bsaType12HashMap.put(currentEntry.getChildren().get(i).getChildren().get(j), (BsaType12Entry) copyListContainer.get(i)[j]);
                        }
                    }
                    case "BSA Type 13" -> {
                        currentEntry.getChildren().add(i, new TreeItem<>("BSA Type 13"));

                        for (int j = 0; j < copyListContainer.get(i).length; j++) {
                            currentEntry.getChildren().get(i).getChildren().add(j, new TreeItem<>("Entry " +j));

                            bsaType13HashMap.put(currentEntry.getChildren().get(i).getChildren().get(j), (BsaType13Entry) copyListContainer.get(i)[j]);
                        }
                    }
                    case "BSA Type 14" -> {
                        currentEntry.getChildren().add(i, new TreeItem<>("BSA Type 14"));

                        for (int j = 0; j < copyListContainer.get(i).length; j++) {
                            currentEntry.getChildren().get(i).getChildren().add(j, new TreeItem<>("Entry " +j));

                            bsaType14HashMap.put(currentEntry.getChildren().get(i).getChildren().get(j), (BsaType14Entry) copyListContainer.get(i)[j]);
                        }
                    }
                }
            }

            tabPane.getTabs().clear();
            createBsaMain(bsaMainHashMap.get(currentEntry));
            tabPane.getSelectionModel().select(tabPane.getSelectionModel().getSelectedIndex());
        }
        else if (currentEntry.getChildren().isEmpty()) {
            switch (currentEntry.getParent().getValue()) {
                case "Collision (After Effects)" -> {
                    bsaCollisionHashMap.put(currentEntry, (BsaCollisionEntry) copyContainer);

                    tabPane.getTabs().clear();
                    createBsaCollision(bsaCollisionHashMap.get(currentEntry));
                    tabPane.getSelectionModel().select(tabPane.getSelectionModel().getSelectedIndex());
                }
                case "Expiration (After Effects)" -> {
                    bsaExpirationHashMap.put(currentEntry, (BsaExpirationEntry) copyContainer);
                    
                    tabPane.getTabs().clear();
                    createBsaExpiration(bsaExpirationHashMap.get(currentEntry));
                    tabPane.getSelectionModel().select(tabPane.getSelectionModel().getSelectedIndex());
                }
                case "BSA Entry Passing" -> {
                    bsaType0HashMap.put(currentEntry, (BsaType0Entry) copyContainer);
                    
                    tabPane.getTabs().clear();
                    createBsaType0(bsaType0HashMap.get(currentEntry));
                    tabPane.getSelectionModel().select(tabPane.getSelectionModel().getSelectedIndex());
                }
                case "Movement" -> {
                    bsaType1HashMap.put(currentEntry, (BsaType1Entry) copyContainer);
                    
                    tabPane.getTabs().clear();
                    createBsaType1(bsaType1HashMap.get(currentEntry));
                    tabPane.getSelectionModel().select(tabPane.getSelectionModel().getSelectedIndex());
                }
                case "BSA Type 2" -> {
                    bsaType2HashMap.put(currentEntry, (BsaType2Entry) copyContainer);
                    
                    tabPane.getTabs().clear();
                    createBsaType2(bsaType2HashMap.get(currentEntry));
                    tabPane.getSelectionModel().select(tabPane.getSelectionModel().getSelectedIndex());
                }
                case "Hitbox" -> {
                    bsaType3HashMap.put(currentEntry, (BsaType3Entry) copyContainer);
                    
                    tabPane.getTabs().clear();
                    createBsaType3(bsaType3HashMap.get(currentEntry));
                    tabPane.getSelectionModel().select(tabPane.getSelectionModel().getSelectedIndex());
                }
                case "Deflection" -> {
                    bsaType4HashMap.put(currentEntry, (BsaType4Entry) copyContainer);
                    
                    tabPane.getTabs().clear();
                    createBsaType4(bsaType4HashMap.get(currentEntry));
                    tabPane.getSelectionModel().select(tabPane.getSelectionModel().getSelectedIndex());
                }
                case "Effect" -> {
                    bsaType6HashMap.put(currentEntry, (BsaType6Entry) copyContainer);
                    
                    tabPane.getTabs().clear();
                    createBsaType6(bsaType6HashMap.get(currentEntry));
                    tabPane.getSelectionModel().select(tabPane.getSelectionModel().getSelectedIndex());
                }
                case "Sound" -> {
                    bsaType7HashMap.put(currentEntry, (BsaType7Entry) copyContainer);
                    
                    tabPane.getTabs().clear();
                    createBsaType7(bsaType7HashMap.get(currentEntry));
                    tabPane.getSelectionModel().select(tabPane.getSelectionModel().getSelectedIndex());
                }
                case "Screen Effect" -> {
                    bsaType8HashMap.put(currentEntry, (BsaType8Entry) copyContainer);
                    
                    tabPane.getTabs().clear();
                    createBsaType8(bsaType8HashMap.get(currentEntry));
                    tabPane.getSelectionModel().select(tabPane.getSelectionModel().getSelectedIndex());
                }
                case "BSA Type 10" -> {
                    bsaType10HashMap.put(currentEntry, (BsaType10Entry) copyContainer);
                    
                    tabPane.getTabs().clear();
                    createBsaType10(bsaType10HashMap.get(currentEntry));
                    tabPane.getSelectionModel().select(tabPane.getSelectionModel().getSelectedIndex());
                }
                case "BSA Type 12" -> {
                    bsaType12HashMap.put(currentEntry, (BsaType12Entry) copyContainer);
                    
                    tabPane.getTabs().clear();
                    createBsaType12(bsaType12HashMap.get(currentEntry));
                    tabPane.getSelectionModel().select(tabPane.getSelectionModel().getSelectedIndex());
                }
                case "BSA Type 13" -> {
                    bsaType13HashMap.put(currentEntry, (BsaType13Entry) copyContainer);
                    
                    tabPane.getTabs().clear();
                    createBsaType13(bsaType13HashMap.get(currentEntry));
                    tabPane.getSelectionModel().select(tabPane.getSelectionModel().getSelectedIndex());
                }
                case "BSA Type 14" -> {
                    bsaType14HashMap.put(currentEntry, (BsaType14Entry) copyContainer);
                    
                    tabPane.getTabs().clear();
                    createBsaType14(bsaType14HashMap.get(currentEntry));
                    tabPane.getSelectionModel().select(tabPane.getSelectionModel().getSelectedIndex());
                }
            }
        }
        else {
            switch (currentEntry.getValue()) {
                case "Collision (After Effects)" -> {
                    for (TreeItem<String> child : currentEntry.getChildren()) {
                        bsaCollisionHashMap.remove(child);
                    }
                }
                case "Expiration (After Effects)" -> {
                    for (TreeItem<String> child : currentEntry.getChildren()) {
                        bsaExpirationHashMap.remove(child);
                    }
                }
                case "BSA Entry Passing" -> {
                    for (TreeItem<String> child : currentEntry.getChildren()) {
                        bsaType0HashMap.remove(child);
                    }
                }
                case "Movement" -> {
                    for (TreeItem<String> child : currentEntry.getChildren()) {
                        bsaType1HashMap.remove(child);
                    }
                }
                case "BSA Type 2" -> {
                    for (TreeItem<String> child : currentEntry.getChildren()) {
                        bsaType2HashMap.remove(child);
                    }
                }
                case "Hitbox" -> {
                    for (TreeItem<String> child : currentEntry.getChildren()) {
                        bsaType3HashMap.remove(child);
                    }
                }
                case "Deflection" -> {
                    for (TreeItem<String> child : currentEntry.getChildren()) {
                        bsaType4HashMap.remove(child);
                    }
                }
                case "Effect" -> {
                    for (TreeItem<String> child : currentEntry.getChildren()) {
                        bsaType6HashMap.remove(child);
                    }
                }
                case "Sound" -> {
                    for (TreeItem<String> child : currentEntry.getChildren()) {
                        bsaType7HashMap.remove(child);
                    }
                }
                case "Screen Effect" -> {
                    for (TreeItem<String> child : currentEntry.getChildren()) {
                        bsaType8HashMap.remove(child);
                    }
                }
                case "BSA Type 10" -> {
                    for (TreeItem<String> child : currentEntry.getChildren()) {
                        bsaType10HashMap.remove(child);
                    }
                }
                case "BSA Type 12" -> {
                    for (TreeItem<String> child : currentEntry.getChildren()) {
                        bsaType12HashMap.remove(child);
                    }
                }
                case "BSA Type 13" -> {
                    for (TreeItem<String> child : currentEntry.getChildren()) {
                        bsaType13HashMap.remove(child);
                    }
                }
                case "BSA Type 14" -> {
                    for (TreeItem<String> child : currentEntry.getChildren()) {
                        bsaType14HashMap.remove(child);
                    }
                }
            }

            currentEntry.getChildren().clear();

            switch (currentEntry.getValue()) {
                case "Collision (After Effects)" -> {
                    for (int i = 0; i < copyListContainer.get(0).length; i++) {
                        currentEntry.getChildren().add(i, new TreeItem<>("Entry " +i));

                        bsaCollisionHashMap.put(currentEntry.getChildren().get(i), (BsaCollisionEntry) copyListContainer.get(0)[i]);
                    }
                }
                case "Expiration (After Effects)" -> {
                    for (int i = 0; i < copyListContainer.get(0).length; i++) {
                        currentEntry.getChildren().add(i, new TreeItem<>("Entry " +i));

                        bsaExpirationHashMap.put(currentEntry.getChildren().get(i), (BsaExpirationEntry) copyListContainer.get(0)[i]);
                    }
                }
                case "BSA Entry Passing" -> {
                    for (int i = 0; i < copyListContainer.get(0).length; i++) {
                        currentEntry.getChildren().add(i, new TreeItem<>("Entry " +i));

                        bsaType0HashMap.put(currentEntry.getChildren().get(i), (BsaType0Entry) copyListContainer.get(0)[i]);
                    }
                }
                case "Movement" -> {
                    for (int i = 0; i < copyListContainer.get(0).length; i++) {
                        currentEntry.getChildren().add(i, new TreeItem<>("Entry " +i));

                        bsaType1HashMap.put(currentEntry.getChildren().get(i), (BsaType1Entry) copyListContainer.get(0)[i]);
                    }
                }
                case "BSA Type 2" -> {
                    for (int i = 0; i < copyListContainer.get(0).length; i++) {
                        currentEntry.getChildren().add(i, new TreeItem<>("Entry " +i));

                        bsaType2HashMap.put(currentEntry.getChildren().get(i), (BsaType2Entry) copyListContainer.get(0)[i]);
                    }
                }
                case "Hitbox" -> {
                    for (int i = 0; i < copyListContainer.get(0).length; i++) {
                        currentEntry.getChildren().add(i, new TreeItem<>("Entry " +i));

                        bsaType3HashMap.put(currentEntry.getChildren().get(i), (BsaType3Entry) copyListContainer.get(0)[i]);
                    }
                }
                case "Deflection" -> {
                    for (int i = 0; i < copyListContainer.get(0).length; i++) {
                        currentEntry.getChildren().add(i, new TreeItem<>("Entry " +i));

                        bsaType4HashMap.put(currentEntry.getChildren().get(i), (BsaType4Entry) copyListContainer.get(0)[i]);
                    }
                }
                case "Effect" -> {
                    for (int i = 0; i < copyListContainer.get(0).length; i++) {
                        currentEntry.getChildren().add(i, new TreeItem<>("Entry " +i));

                        bsaType4HashMap.put(currentEntry.getChildren().get(i), (BsaType4Entry) copyListContainer.get(0)[i]);
                    }
                }
                case "Sound" -> {
                    for (int i = 0; i < copyListContainer.get(0).length; i++) {
                        currentEntry.getChildren().add(i, new TreeItem<>("Entry " +i));

                        bsaType7HashMap.put(currentEntry.getChildren().get(i), (BsaType7Entry) copyListContainer.get(0)[i]);
                    }
                }
                case "Screen Effect" -> {
                    for (int i = 0; i < copyListContainer.get(0).length; i++) {
                        currentEntry.getChildren().add(i, new TreeItem<>("Entry " +i));

                        bsaType8HashMap.put(currentEntry.getChildren().get(i), (BsaType8Entry) copyListContainer.get(0)[i]);
                    }
                }
                case "BSA Type 10" -> {
                    for (int i = 0; i < copyListContainer.get(0).length; i++) {
                        currentEntry.getChildren().add(i, new TreeItem<>("Entry " +i));

                        bsaType10HashMap.put(currentEntry.getChildren().get(i), (BsaType10Entry) copyListContainer.get(0)[i]);
                    }
                }
                case "BSA Type 12" -> {
                    for (int i = 0; i < copyListContainer.get(0).length; i++) {
                        currentEntry.getChildren().add(i, new TreeItem<>("Entry " +i));

                        bsaType12HashMap.put(currentEntry.getChildren().get(i), (BsaType12Entry) copyListContainer.get(0)[i]);
                    }
                }
                case "BSA Type 13" -> {
                    for (int i = 0; i < copyListContainer.get(0).length; i++) {
                        currentEntry.getChildren().add(i, new TreeItem<>("Entry " +i));

                        bsaType13HashMap.put(currentEntry.getChildren().get(i), (BsaType13Entry) copyListContainer.get(0)[i]);
                    }
                }
                case "BSA Type 14" -> {
                    for (int i = 0; i < copyListContainer.get(0).length; i++) {
                        currentEntry.getChildren().add(i, new TreeItem<>("Entry " +i));

                        bsaType14HashMap.put(currentEntry.getChildren().get(i), (BsaType14Entry) copyListContainer.get(0)[i]);
                    }
                }
            }
        }
    }

    private void AddItemCopy() {
        TreeItem<String> searchItem = grandParentEntry;
        switch (addItemCopy.getText()) {
            case "Add Entry Copy  Ctrl+A" -> {
                TreeItem<String> newEntry = new TreeItem<>("Entry " + allEntries.size());

                treeView.getRoot().getChildren().add(newEntry);
                allEntries.add(newEntry);

                bsaMainHashMap.put(newEntry, (BsaMainEntry) copyContainer);

                for (int i = 0; i < copyTypesContainer.size(); i++) {
                    switch (copyTypesContainer.get(i)) {
                        case "Collision (After Effects)" -> {
                            newEntry.getChildren().add(i, new TreeItem<>("Collision (After Effects)"));

                            for (int j = 0; j < copyListContainer.get(i).length; j++) {
                                newEntry.getChildren().get(i).getChildren().add(j, new TreeItem<>("Entry " +j));

                                bsaCollisionHashMap.put(newEntry.getChildren().get(i).getChildren().get(j), (BsaCollisionEntry) copyListContainer.get(i)[j]);
                            }
                        }
                        case "Expiration (After Effects)" -> {
                            newEntry.getChildren().add(i, new TreeItem<>("Expiration (After Effects)"));

                            for (int j = 0; j < copyListContainer.get(i).length; j++) {
                                newEntry.getChildren().get(i).getChildren().add(j, new TreeItem<>("Entry " +j));

                                bsaExpirationHashMap.put(newEntry.getChildren().get(i).getChildren().get(j), (BsaExpirationEntry) copyListContainer.get(i)[j]);
                            }
                        }
                        case "BSA Entry Passing" -> {
                            newEntry.getChildren().add(i, new TreeItem<>("BSA Entry Passing"));

                            for (int j = 0; j < copyListContainer.get(i).length; j++) {
                                newEntry.getChildren().get(i).getChildren().add(j, new TreeItem<>("Entry " +j));

                                bsaType0HashMap.put(newEntry.getChildren().get(i).getChildren().get(j), (BsaType0Entry) copyListContainer.get(i)[j]);
                            }
                        }
                        case "Movement" -> {
                            newEntry.getChildren().add(i, new TreeItem<>("Movement"));

                            for (int j = 0; j < copyListContainer.get(i).length; j++) {
                                newEntry.getChildren().get(i).getChildren().add(j, new TreeItem<>("Entry " +j));

                                bsaType1HashMap.put(newEntry.getChildren().get(i).getChildren().get(j), (BsaType1Entry) copyListContainer.get(i)[j]);
                            }
                        }
                        case "BSA Type 2" -> {
                            newEntry.getChildren().add(i, new TreeItem<>("BSA Type 2"));

                            for (int j = 0; j < copyListContainer.get(i).length; j++) {
                                newEntry.getChildren().get(i).getChildren().add(j, new TreeItem<>("Entry " +j));

                                bsaType2HashMap.put(newEntry.getChildren().get(i).getChildren().get(j), (BsaType2Entry) copyListContainer.get(i)[j]);
                            }
                        }
                        case "Hitbox" -> {
                            newEntry.getChildren().add(i, new TreeItem<>("Hitbox"));

                            for (int j = 0; j < copyListContainer.get(i).length; j++) {
                                newEntry.getChildren().get(i).getChildren().add(j, new TreeItem<>("Entry " +j));

                                bsaType3HashMap.put(newEntry.getChildren().get(i).getChildren().get(j), (BsaType3Entry) copyListContainer.get(i)[j]);
                            }
                        }
                        case "Deflection" -> {
                            newEntry.getChildren().add(i, new TreeItem<>("Deflection"));

                            for (int j = 0; j < copyListContainer.get(i).length; j++) {
                                newEntry.getChildren().get(i).getChildren().add(j, new TreeItem<>("Entry " +j));

                                bsaType4HashMap.put(newEntry.getChildren().get(i).getChildren().get(j), (BsaType4Entry) copyListContainer.get(i)[j]);
                            }
                        }
                        case "Effect" -> {
                            newEntry.getChildren().add(i, new TreeItem<>("Effect"));

                            for (int j = 0; j < copyListContainer.get(i).length; j++) {
                                newEntry.getChildren().get(i).getChildren().add(j, new TreeItem<>("Entry " +j));

                                bsaType6HashMap.put(newEntry.getChildren().get(i).getChildren().get(j), (BsaType6Entry) copyListContainer.get(i)[j]);
                            }
                        }
                        case "Sound" -> {
                            newEntry.getChildren().add(i, new TreeItem<>("Sound"));

                            for (int j = 0; j < copyListContainer.get(i).length; j++) {
                                newEntry.getChildren().get(i).getChildren().add(j, new TreeItem<>("Entry " +j));

                                bsaType7HashMap.put(newEntry.getChildren().get(i).getChildren().get(j), (BsaType7Entry) copyListContainer.get(i)[j]);
                            }
                        }
                        case "Screen Effect" -> {
                            newEntry.getChildren().add(i, new TreeItem<>("Screen Effect"));

                            for (int j = 0; j < copyListContainer.get(i).length; j++) {
                                newEntry.getChildren().get(i).getChildren().add(j, new TreeItem<>("Entry " +j));

                                bsaType8HashMap.put(newEntry.getChildren().get(i).getChildren().get(j), (BsaType8Entry) copyListContainer.get(i)[j]);
                            }
                        }
                        case "BSA Type 10" -> {
                            newEntry.getChildren().add(i, new TreeItem<>("BSA Type 10"));

                            for (int j = 0; j < copyListContainer.get(i).length; j++) {
                                newEntry.getChildren().get(i).getChildren().add(j, new TreeItem<>("Entry " +j));

                                bsaType10HashMap.put(newEntry.getChildren().get(i).getChildren().get(j), (BsaType10Entry) copyListContainer.get(i)[j]);
                            }
                        }
                        case "BSA Type 12" -> {
                            newEntry.getChildren().add(i, new TreeItem<>("BSA Type 12"));

                            for (int j = 0; j < copyListContainer.get(i).length; j++) {
                                newEntry.getChildren().get(i).getChildren().add(j, new TreeItem<>("Entry " +j));

                                bsaType12HashMap.put(newEntry.getChildren().get(i).getChildren().get(j), (BsaType12Entry) copyListContainer.get(i)[j]);
                            }
                        }
                        case "BSA Type 13" -> {
                            newEntry.getChildren().add(i, new TreeItem<>("BSA Type 13"));

                            for (int j = 0; j < copyListContainer.get(i).length; j++) {
                                newEntry.getChildren().get(i).getChildren().add(j, new TreeItem<>("Entry " +j));

                                bsaType13HashMap.put(newEntry.getChildren().get(i).getChildren().get(j), (BsaType13Entry) copyListContainer.get(i)[j]);
                            }
                        }
                        case "BSA Type 14" -> {
                            newEntry.getChildren().add(i, new TreeItem<>("BSA Type 14"));

                            for (int j = 0; j < copyListContainer.get(i).length; j++) {
                                newEntry.getChildren().get(i).getChildren().add(j, new TreeItem<>("Entry " +j));

                                bsaType14HashMap.put(newEntry.getChildren().get(i).getChildren().get(j), (BsaType14Entry) copyListContainer.get(i)[j]);
                            }
                        }
                    }
                }
            }
            case "Add Collision Copy  Ctrl+A" -> {
                boolean hasCollision = false;

                while (searchItem != null) {
                    for (TreeItem<String> child : searchItem.getChildren()) {
                        if (child.getValue().equals("Collision (After Effects)")) {
                            if (searchItem == grandParentEntry) {
                                hasCollision = true;
                            }
                        }
                    }
                    if (searchItem.previousSibling() == null) {
                        break;
                    }

                    searchItem = searchItem.previousSibling();
                }
                if (hasCollision) {
                    TreeItem<String> getParent = grandParentEntry.getChildren().get(0);
                    TreeItem<String> newChild = new TreeItem<>("Entry " + getParent.getChildren().size());

                    getParent.getChildren().add(newChild);

                    bsaCollisionHashMap.put(newChild, (BsaCollisionEntry) copyContainer);

                    treeView.getSelectionModel().select(newChild);
                } else {
                    grandParentEntry.getChildren().add(0, new TreeItem<>("Collision (After Effects)"));

                    TreeItem<String> newChild = new TreeItem<>("Entry " + 0);

                    grandParentEntry.getChildren().get(0).getChildren().add(newChild);

                    bsaCollisionHashMap.put(newChild, (BsaCollisionEntry) copyContainer);

                    sortTreeItems(grandParentEntry);

                    treeView.getSelectionModel().select(newChild);
                }
            }
            case "Add Collision List Copy  Ctrl+A" -> {
                boolean hasCollision = false;

                while (searchItem != null) {
                    for (TreeItem<String> child : searchItem.getChildren()) {
                        if (child.getValue().equals("Collision (After Effects)")) {
                            if (searchItem == grandParentEntry) {
                                hasCollision = true;
                            }
                        }
                    }
                    if (searchItem.previousSibling() == null) {
                        break;
                    }

                    searchItem = searchItem.previousSibling();
                }
                if (hasCollision) {
                    TreeItem<String> getParent = grandParentEntry.getChildren().get(0);

                    for (int i = 0; i < copyListContainer.get(0).length; i++) {
                        TreeItem<String> newChild = new TreeItem<>("Entry " + getParent.getChildren().size());

                        getParent.getChildren().add(newChild);

                        bsaCollisionHashMap.put(newChild, (BsaCollisionEntry) copyListContainer.get(0)[i]);
                    }
                } 
                else {
                    grandParentEntry.getChildren().add(0, new TreeItem<>("Collision (After Effects)"));

                    for (int i = 0; i < copyListContainer.get(0).length; i++) {
                        TreeItem<String> newChild = new TreeItem<>("Entry " + i);

                        grandParentEntry.getChildren().get(0).getChildren().add(newChild);

                        bsaCollisionHashMap.put(newChild, (BsaCollisionEntry) copyListContainer.get(0)[i]);
                    }

                    sortTreeItems(grandParentEntry);
                }
            }
            case "Add Expiration Copy  Ctrl+A" -> {
                boolean hasExpiration = false;

                TreeItem<String> expirationIndex = new TreeItem<>();

                while (searchItem != null) {
                    for (TreeItem<String> child : searchItem.getChildren()) {
                        if (child.getValue().equals("Expiration (After Effects)")) {
                            if (searchItem == grandParentEntry) {
                                hasExpiration = true;
                                expirationIndex = child;
                            }
                        }
                    }
                    if (searchItem.previousSibling() == null) {
                        break;
                    }

                    searchItem = searchItem.previousSibling();
                }
                if (hasExpiration) {
                    TreeItem<String> getParent = expirationIndex;

                    TreeItem<String> newChild = new TreeItem<>("Entry " + getParent.getChildren().size());

                    getParent.getChildren().add(newChild);

                    bsaExpirationHashMap.put(newChild, (BsaExpirationEntry) copyContainer);

                    treeView.getSelectionModel().select(newChild);
                } 
                else {
                    grandParentEntry.getChildren().add(0, new TreeItem<>("Expiration (After Effects)"));

                    TreeItem<String> newChild = new TreeItem<>("Entry " + 0);

                    grandParentEntry.getChildren().get(0).getChildren().add(newChild);

                    bsaExpirationHashMap.put(newChild, (BsaExpirationEntry) copyContainer);

                    sortTreeItems(grandParentEntry);

                    treeView.getSelectionModel().select(newChild);
                }
            }
            case "Add Expiration List Copy  Ctrl+A" -> {
                boolean hasExpiration = false;
                TreeItem<String> expirationIndex = new TreeItem<>();
                while (searchItem != null) {
                    for (TreeItem<String> child : searchItem.getChildren()) {
                        if (child.getValue().equals("Expiration (After Effects)")) {
                            if (searchItem == grandParentEntry) {
                                hasExpiration = true;
                                expirationIndex = child;
                            }
                        }
                    }
                    if (searchItem.previousSibling() == null) {
                        break;
                    }
                    searchItem = searchItem.previousSibling();
                }
                if (hasExpiration) {
                    TreeItem<String> getParent = expirationIndex;
                    for (int i = 0; i < copyListContainer.get(0).length; i++) {
                        TreeItem<String> newChild = new TreeItem<>("Entry " + getParent.getChildren().size());

                        getParent.getChildren().add(newChild);

                        bsaExpirationHashMap.put(newChild, (BsaExpirationEntry) copyListContainer.get(0)[i]);
                    }
                } 
                else {
                    grandParentEntry.getChildren().add(0, new TreeItem<>("Expiration (After Effects)"));

                    for (int i = 0; i < copyListContainer.get(0).length; i++) {
                        TreeItem<String> newChild = new TreeItem<>("Entry " + i);

                        grandParentEntry.getChildren().get(0).getChildren().add(newChild);

                        bsaExpirationHashMap.put(newChild, (BsaExpirationEntry) copyListContainer.get(0)[i]);
                    }

                    sortTreeItems(grandParentEntry);
                }
            }
            case "Add BSA Entry Passing Copy  Ctrl+A" -> {
                boolean hasType0 = false;
                TreeItem<String> type0Index = new TreeItem<>();
                while (searchItem != null) {
                    for (TreeItem<String> child : searchItem.getChildren()) {
                        if (child.getValue().equals("BSA Entry Passing")) {
                            if (searchItem == grandParentEntry) {
                                hasType0 = true;
                                type0Index = child;
                            }
                        }
                    }
                    if (searchItem.previousSibling() == null) {
                        break;
                    }
                    searchItem = searchItem.previousSibling();
                }
                if (hasType0) {
                    TreeItem<String> getParent = type0Index;
                    TreeItem<String> newChild = new TreeItem<>("Entry " + getParent.getChildren().size());

                    getParent.getChildren().add(newChild);

                    bsaType0HashMap.put(newChild, (BsaType0Entry) copyContainer);

                    treeView.getSelectionModel().select(newChild);
                } else {
                    grandParentEntry.getChildren().add(0, new TreeItem<>("BSA Entry Passing"));

                    TreeItem<String> newChild = new TreeItem<>("Entry " + 0);

                    grandParentEntry.getChildren().get(0).getChildren().add(newChild);

                    bsaType0HashMap.put(newChild, (BsaType0Entry) copyContainer);

                    sortTreeItems(grandParentEntry);

                    treeView.getSelectionModel().select(newChild);
                }
            }
            case "Add BSA Entry Passing List Copy  Ctrl+A" -> {
                boolean hasType0 = false;

                TreeItem<String> type0Index = new TreeItem<>();

                while (searchItem != null) {
                    for (TreeItem<String> child : searchItem.getChildren()) {
                        if (child.getValue().equals("BSA Entry Passing")) {
                            if (searchItem == grandParentEntry) {
                                hasType0 = true;
                                type0Index = child;
                            }
                        }
                    }
                    if (searchItem.previousSibling() == null) {
                        break;
                    }
                    searchItem = searchItem.previousSibling();
                }
                if (hasType0) {
                    TreeItem<String> getParent = type0Index;
                    for (int i = 0; i < copyListContainer.get(0).length; i++) {
                        TreeItem<String> newChild = new TreeItem<>("Entry " + getParent.getChildren().size());

                        getParent.getChildren().add(newChild);

                        bsaType0HashMap.put(newChild, (BsaType0Entry) copyListContainer.get(0)[i]);
                    }
                } 
                else {
                    grandParentEntry.getChildren().add(0, new TreeItem<>("BSA Entry Passing"));

                    for (int i = 0; i < copyListContainer.get(0).length; i++) {
                        TreeItem<String> newChild = new TreeItem<>("Entry " + i);

                        grandParentEntry.getChildren().get(0).getChildren().add(newChild);

                        bsaType0HashMap.put(newChild, (BsaType0Entry) copyListContainer.get(0)[i]);
                    }

                    sortTreeItems(grandParentEntry);
                }
            }
            case "Add Movement Copy  Ctrl+A" -> {
                boolean hasType1 = false;

                TreeItem<String> type1Index = new TreeItem<>();

                while (searchItem != null) {
                    for (TreeItem<String> child : searchItem.getChildren()) {
                        if (child.getValue().equals("Movement")) {
                            if (searchItem == grandParentEntry) {
                                hasType1 = true;
                                type1Index = child;
                            }
                        }
                    }
                    if (searchItem.previousSibling() == null) {
                        break;
                    }
                    searchItem = searchItem.previousSibling();
                }
                if (hasType1) {
                    TreeItem<String> getParent = type1Index;
                    TreeItem<String> newChild = new TreeItem<>("Entry " + getParent.getChildren().size());

                    getParent.getChildren().add(newChild);

                    bsaType1HashMap.put(newChild, (BsaType1Entry) copyContainer);

                    treeView.getSelectionModel().select(newChild);
                } 
                else {
                    grandParentEntry.getChildren().add(0, new TreeItem<>("Movement"));

                    TreeItem<String> newChild = new TreeItem<>("Entry " + 0);

                    grandParentEntry.getChildren().get(0).getChildren().add(newChild);

                    bsaType1HashMap.put(newChild, (BsaType1Entry) copyContainer);

                    sortTreeItems(grandParentEntry);

                    treeView.getSelectionModel().select(newChild);
                }
            }
            case "Add Movement List Copy  Ctrl+A" -> {
                boolean hastype1 = false;

                TreeItem<String> type1Index = new TreeItem<>();

                while (searchItem != null) {
                    for (TreeItem<String> child : searchItem.getChildren()) {
                        if (child.getValue().equals("Movement")) {
                            if (searchItem == grandParentEntry) {
                                hastype1 = true;
                                type1Index = child;
                            }
                        }
                    }
                    if (searchItem.previousSibling() == null) {
                        break;
                    }

                    searchItem = searchItem.previousSibling();
                }
                if (hastype1) {
                    TreeItem<String> getParent = type1Index;

                    for (int i = 0; i < copyListContainer.get(0).length; i++) {
                        TreeItem<String> newChild = new TreeItem<>("Entry " + getParent.getChildren().size());

                        getParent.getChildren().add(newChild);

                        bsaType1HashMap.put(newChild, (BsaType1Entry) copyListContainer.get(0)[i]);
                    }
                } 
                else {
                    grandParentEntry.getChildren().add(0, new TreeItem<>("Movement"));

                    for (int i = 0; i < copyListContainer.get(0).length; i++) {
                        TreeItem<String> newChild = new TreeItem<>("Entry " + i);

                        grandParentEntry.getChildren().get(0).getChildren().add(newChild);

                        bsaType1HashMap.put(newChild, (BsaType1Entry) copyListContainer.get(0)[i]);
                    }

                    sortTreeItems(grandParentEntry);
                }
            }
            case "Add BSA Type 2 Copy  Ctrl+A" -> {
                boolean hasType2 = false;

                TreeItem<String> type2Index = new TreeItem<>();

                while (searchItem != null) {
                    for (TreeItem<String> child : searchItem.getChildren()) {
                        if (child.getValue().equals("BSA Type 2")) {
                            if (searchItem == grandParentEntry) {
                                hasType2 = true;
                                type2Index = child;
                            }
                        }
                    }
                    if (searchItem.previousSibling() == null) {
                        break;
                    }

                    searchItem = searchItem.previousSibling();
                }
                if (hasType2) {
                    TreeItem<String> getParent = type2Index;
                    TreeItem<String> newChild = new TreeItem<>("Entry " + getParent.getChildren().size());

                    getParent.getChildren().add(newChild);

                    bsaType2HashMap.put(newChild, (BsaType2Entry) copyContainer);

                    treeView.getSelectionModel().select(newChild);
                }
                else {
                    grandParentEntry.getChildren().add(0, new TreeItem<>("BSA Type 2"));

                    TreeItem<String> newChild = new TreeItem<>("Entry " + 0);

                    grandParentEntry.getChildren().get(0).getChildren().add(newChild);

                    bsaType2HashMap.put(newChild, (BsaType2Entry) copyContainer);

                    sortTreeItems(grandParentEntry);

                    treeView.getSelectionModel().select(newChild);
                }
            }
            case "Add BSA Type 2 List Copy  Ctrl+A" -> {
                boolean hasType2 = false;

                TreeItem<String> type2Index = new TreeItem<>();

                while (searchItem != null) {
                    for (TreeItem<String> child : searchItem.getChildren()) {
                        if (child.getValue().equals("BSA Type 2")) {
                            if (searchItem == grandParentEntry) {
                                hasType2 = true;
                                type2Index = child;
                            }
                        }
                    }
                    if (searchItem.previousSibling() == null) {
                        break;
                    }

                    searchItem = searchItem.previousSibling();
                }
                if (hasType2) {
                    TreeItem<String> getParent = type2Index;

                    for (int i = 0; i < copyListContainer.get(0).length; i++) {
                        TreeItem<String> newChild = new TreeItem<>("Entry " + getParent.getChildren().size());

                        getParent.getChildren().add(newChild);

                        bsaType2HashMap.put(newChild, (BsaType2Entry) copyListContainer.get(0)[i]);
                    }
                } 
                else {
                    grandParentEntry.getChildren().add(0, new TreeItem<>("BSA Type 2"));

                    for (int i = 0; i < copyListContainer.get(0).length; i++) {
                        TreeItem<String> newChild = new TreeItem<>("Entry " + i);

                        grandParentEntry.getChildren().get(0).getChildren().add(newChild);

                        bsaType2HashMap.put(newChild, (BsaType2Entry) copyListContainer.get(0)[i]);
                    }

                    sortTreeItems(grandParentEntry);
                }
            }
            case "Add Hitbox Copy  Ctrl+A" -> {
                boolean hasType3 = false;

                TreeItem<String> type3Index = new TreeItem<>();

                while (searchItem != null) {
                    for (TreeItem<String> child : searchItem.getChildren()) {
                        if (child.getValue().equals("Hitbox")) {
                            if (searchItem == grandParentEntry) {
                                hasType3 = true;
                                type3Index = child;
                            }
                        }
                    }
                    if (searchItem.previousSibling() == null) {
                        break;
                    }

                    searchItem = searchItem.previousSibling();
                }
                if (hasType3) {
                    TreeItem<String> getParent = type3Index;
                    TreeItem<String> newChild = new TreeItem<>("Entry " + getParent.getChildren().size());

                    getParent.getChildren().add(newChild);

                    bsaType3HashMap.put(newChild, (BsaType3Entry) copyContainer);

                    treeView.getSelectionModel().select(newChild);
                } 
                else {
                    grandParentEntry.getChildren().add(0, new TreeItem<>("Hitbox"));

                    TreeItem<String> newChild = new TreeItem<>("Entry " + 0);

                    grandParentEntry.getChildren().get(0).getChildren().add(newChild);

                    bsaType3HashMap.put(newChild, (BsaType3Entry) copyContainer);

                    sortTreeItems(grandParentEntry);

                    treeView.getSelectionModel().select(newChild);
                }
            }
            case "Add Hitbox List Copy  Ctrl+A" -> {
                boolean hasType3 = false;

                TreeItem<String> type3Index = new TreeItem<>();

                while (searchItem != null) {
                    for (TreeItem<String> child : searchItem.getChildren()) {
                        if (child.getValue().equals("Hitbox")) {
                            if (searchItem == grandParentEntry) {
                                hasType3 = true;
                                type3Index = child;
                            }
                        }
                    }
                    if (searchItem.previousSibling() == null) {
                        break;
                    }

                    searchItem = searchItem.previousSibling();
                }
                if (hasType3) {
                    TreeItem<String> getParent = type3Index;
                    for (int i = 0; i < copyListContainer.get(0).length; i++) {
                        TreeItem<String> newChild = new TreeItem<>("Entry " + getParent.getChildren().size());

                        getParent.getChildren().add(newChild);

                        bsaType3HashMap.put(newChild, (BsaType3Entry) copyListContainer.get(0)[i]);
                    }
                } 
                else {
                    grandParentEntry.getChildren().add(0, new TreeItem<>("Hitbox"));

                    for (int i = 0; i < copyListContainer.get(0).length; i++) {
                        TreeItem<String> newChild = new TreeItem<>("Entry " + i);

                        grandParentEntry.getChildren().get(0).getChildren().add(newChild);

                        bsaType3HashMap.put(newChild, (BsaType3Entry) copyListContainer.get(0)[i]);
                    }

                    sortTreeItems(grandParentEntry);
                }
            }
            case "Add Deflection Copy  Ctrl+A" -> {
                boolean hasType4 = false;

                TreeItem<String> type4Index = new TreeItem<>();

                while (searchItem != null) {
                    for (TreeItem<String> child : searchItem.getChildren()) {
                        if (child.getValue().equals("Deflection")) {
                            if (searchItem == grandParentEntry) {
                                hasType4 = true;
                                type4Index = child;
                            }
                        }
                    }
                    if (searchItem.previousSibling() == null) {
                        break;
                    }

                    searchItem = searchItem.previousSibling();
                }
                if (hasType4) {
                    TreeItem<String> getParent = type4Index;
                    TreeItem<String> newChild = new TreeItem<>("Entry " + getParent.getChildren().size());

                    getParent.getChildren().add(newChild);

                    bsaType4HashMap.put(newChild, (BsaType4Entry) copyContainer);

                    treeView.getSelectionModel().select(newChild);
                } 
                else {
                    grandParentEntry.getChildren().add(0, new TreeItem<>("Deflection"));

                    TreeItem<String> newChild = new TreeItem<>("Entry " + 0);

                    grandParentEntry.getChildren().get(0).getChildren().add(newChild);

                    bsaType4HashMap.put(newChild, (BsaType4Entry) copyContainer);

                    sortTreeItems(grandParentEntry);

                    treeView.getSelectionModel().select(newChild);
                }
            }
            case "Add Deflection List Copy  Ctrl+A" -> {
                boolean hasType4 = false;

                TreeItem<String> type4Index = new TreeItem<>();

                while (searchItem != null) {
                    for (TreeItem<String> child : searchItem.getChildren()) {
                        if (child.getValue().equals("Deflection")) {
                            if (searchItem == grandParentEntry) {
                                hasType4 = true;
                                type4Index = child;
                            }
                        }
                    }
                    if (searchItem.previousSibling() == null) {
                        break;
                    }

                    searchItem = searchItem.previousSibling();
                }
                if (hasType4) {
                    TreeItem<String> getParent = type4Index;

                    for (int i = 0; i < copyListContainer.get(0).length; i++) {
                        TreeItem<String> newChild = new TreeItem<>("Entry " + getParent.getChildren().size());

                        getParent.getChildren().add(newChild);

                        bsaType4HashMap.put(newChild, (BsaType4Entry) copyListContainer.get(0)[i]);
                    }
                } 
                else {
                    grandParentEntry.getChildren().add(0, new TreeItem<>("Deflection"));

                    for (int i = 0; i < copyListContainer.get(0).length; i++) {
                        TreeItem<String> newChild = new TreeItem<>("Entry " + i);

                        grandParentEntry.getChildren().get(0).getChildren().add(newChild);

                        bsaType4HashMap.put(newChild, (BsaType4Entry) copyListContainer.get(0)[i]);
                    }

                    sortTreeItems(grandParentEntry);
                }
            }
            case "Add Effect Copy  Ctrl+A" -> {
                boolean hasType6 = false;

                TreeItem<String> type6Index = new TreeItem<>();

                while (searchItem != null) {
                    for (TreeItem<String> child : searchItem.getChildren()) {
                        if (child.getValue().equals("Effect")) {
                            if (searchItem == grandParentEntry) {
                                hasType6 = true;
                                type6Index = child;
                            }
                        }
                    }
                    if (searchItem.previousSibling() == null) {
                        break;
                    }

                    searchItem = searchItem.previousSibling();
                }
                if (hasType6) {
                    TreeItem<String> getParent = type6Index;
                    TreeItem<String> newChild = new TreeItem<>("Entry " + getParent.getChildren().size());

                    getParent.getChildren().add(newChild);

                    bsaType6HashMap.put(newChild, (BsaType6Entry) copyContainer);

                    treeView.getSelectionModel().select(newChild);
                } 
                else {
                    grandParentEntry.getChildren().add(0, new TreeItem<>("Effect"));

                    TreeItem<String> newChild = new TreeItem<>("Entry " + 0);

                    grandParentEntry.getChildren().get(0).getChildren().add(newChild);

                    bsaType6HashMap.put(newChild, (BsaType6Entry) copyContainer);

                    sortTreeItems(grandParentEntry);

                    treeView.getSelectionModel().select(newChild);
                }
            }
            case "Add Effect List Copy  Ctrl+A" -> {
                boolean hasType6 = false;

                TreeItem<String> type6Index = new TreeItem<>();

                while (searchItem != null) {
                    for (TreeItem<String> child : searchItem.getChildren()) {
                        if (child.getValue().equals("Effect")) {
                            if (searchItem == grandParentEntry) {
                                hasType6 = true;
                                type6Index = child;
                            }
                        }
                    }
                    if (searchItem.previousSibling() == null) {
                        break;
                    }

                    searchItem = searchItem.previousSibling();
                }
                if (hasType6) {
                    TreeItem<String> getParent = type6Index;

                    for (int i = 0; i < copyListContainer.get(0).length; i++) {
                        TreeItem<String> newChild = new TreeItem<>("Entry " + getParent.getChildren().size());

                        getParent.getChildren().add(newChild);

                        bsaType6HashMap.put(newChild, (BsaType6Entry) copyListContainer.get(0)[i]);
                    }
                } 
                else {
                    grandParentEntry.getChildren().add(0, new TreeItem<>("Effect"));

                    for (int i = 0; i < copyListContainer.get(0).length; i++) {
                        TreeItem<String> newChild = new TreeItem<>("Entry " + i);

                        grandParentEntry.getChildren().get(0).getChildren().add(newChild);

                        bsaType6HashMap.put(newChild, (BsaType6Entry) copyListContainer.get(0)[i]);
                    }

                    sortTreeItems(grandParentEntry);
                }
            }
            case "Add Sound Copy  Ctrl+A" -> {
                boolean hasType7 = false;

                TreeItem<String> type7Index = new TreeItem<>();

                while (searchItem != null) {
                    for (TreeItem<String> child : searchItem.getChildren()) {
                        if (child.getValue().equals("Sound")) {
                            if (searchItem == grandParentEntry) {
                                hasType7 = true;
                                type7Index = child;
                            }
                        }
                    }
                    if (searchItem.previousSibling() == null) {
                        break;
                    }

                    searchItem = searchItem.previousSibling();
                }
                if (hasType7) {
                    TreeItem<String> getParent = type7Index;
                    TreeItem<String> newChild = new TreeItem<>("Entry " + getParent.getChildren().size());

                    getParent.getChildren().add(newChild);

                    bsaType7HashMap.put(newChild, (BsaType7Entry) copyContainer);

                    treeView.getSelectionModel().select(newChild);
                } 
                else {
                    grandParentEntry.getChildren().add(0, new TreeItem<>("Sound"));

                    TreeItem<String> newChild = new TreeItem<>("Entry " + 0);

                    grandParentEntry.getChildren().get(0).getChildren().add(newChild);

                    bsaType7HashMap.put(newChild, (BsaType7Entry) copyContainer);

                    sortTreeItems(grandParentEntry);

                    treeView.getSelectionModel().select(newChild);
                }
            }
            case "Add Sound List Copy  Ctrl+A" -> {
                boolean hasType7 = false;

                TreeItem<String> type7Index = new TreeItem<>();

                while (searchItem != null) {
                    for (TreeItem<String> child : searchItem.getChildren()) {
                        if (child.getValue().equals("Sound")) {
                            if (searchItem == grandParentEntry) {
                                hasType7 = true;
                                type7Index = child;
                            }
                        }
                    }
                    if (searchItem.previousSibling() == null) {
                        break;
                    }

                    searchItem = searchItem.previousSibling();
                }
                if (hasType7) {
                    TreeItem<String> getParent = type7Index;

                    for (int i = 0; i < copyListContainer.get(0).length; i++) {
                        TreeItem<String> newChild = new TreeItem<>("Entry " + getParent.getChildren().size());

                        getParent.getChildren().add(newChild);

                        bsaType1HashMap.put(newChild, (BsaType1Entry) copyListContainer.get(0)[i]);
                    }
                } 
                else {
                    grandParentEntry.getChildren().add(0, new TreeItem<>("Movement"));

                    for (int i = 0; i < copyListContainer.get(0).length; i++) {
                        TreeItem<String> newChild = new TreeItem<>("Entry " + i);

                        grandParentEntry.getChildren().get(0).getChildren().add(newChild);

                        bsaType1HashMap.put(newChild, (BsaType1Entry) copyListContainer.get(0)[i]);
                    }

                    sortTreeItems(grandParentEntry);
                }
            }
            case "Add Screen Effect Copy  Ctrl+A" -> {
                boolean hasType8 = false;

                TreeItem<String> type8Index = new TreeItem<>();

                while (searchItem != null) {
                    for (TreeItem<String> child : searchItem.getChildren()) {
                        if (child.getValue().equals("Screen Effect")) {
                            if (searchItem == grandParentEntry) {
                                hasType8 = true;
                                type8Index = child;
                            }
                        }
                    }
                    if (searchItem.previousSibling() == null) {
                        break;
                    }

                    searchItem = searchItem.previousSibling();
                }
                if (hasType8) {
                    TreeItem<String> getParent = type8Index;
                    TreeItem<String> newChild = new TreeItem<>("Entry " + getParent.getChildren().size());

                    getParent.getChildren().add(newChild);

                    bsaType8HashMap.put(newChild, new BsaType8Entry());

                    treeView.getSelectionModel().select(newChild);
                } 
                else {
                    grandParentEntry.getChildren().add(0, new TreeItem<>("Screen Effect"));

                    TreeItem<String> newChild = new TreeItem<>("Entry " + 0);

                    grandParentEntry.getChildren().get(0).getChildren().add(newChild);

                    bsaType8HashMap.put(newChild, new BsaType8Entry());

                    sortTreeItems(grandParentEntry);

                    treeView.getSelectionModel().select(newChild);
                }
            }
            case "Add Screen Effect List Copy  Ctrl+A" -> {
                boolean hasType8 = false;

                TreeItem<String> type8Index = new TreeItem<>();

                while (searchItem != null) {
                    for (TreeItem<String> child : searchItem.getChildren()) {
                        if (child.getValue().equals("Screen Effect")) {
                            if (searchItem == grandParentEntry) {
                                hasType8 = true;
                                type8Index = child;
                            }
                        }
                    }
                    if (searchItem.previousSibling() == null) {
                        break;
                    }

                    searchItem = searchItem.previousSibling();
                }
                if (hasType8) {
                    TreeItem<String> getParent = type8Index;

                    for (int i = 0; i < copyListContainer.get(0).length; i++) {
                        TreeItem<String> newChild = new TreeItem<>("Entry " + getParent.getChildren().size());

                        getParent.getChildren().add(newChild);

                        bsaType8HashMap.put(newChild, (BsaType8Entry) copyListContainer.get(0)[i]);
                    }
                } 
                else {
                    grandParentEntry.getChildren().add(0, new TreeItem<>("Screen Effect"));

                    for (int i = 0; i < copyListContainer.get(0).length; i++) {
                        TreeItem<String> newChild = new TreeItem<>("Entry " + i);

                        grandParentEntry.getChildren().get(0).getChildren().add(newChild);

                        bsaType8HashMap.put(newChild, (BsaType8Entry) copyListContainer.get(0)[i]);
                    }

                    sortTreeItems(grandParentEntry);
                }
            }
            case "Add BSA Type 10 Copy  Ctrl+A" -> {
                boolean hasType10 = false;

                TreeItem<String> type10Index = new TreeItem<>();

                while (searchItem != null) {
                    for (TreeItem<String> child : searchItem.getChildren()) {
                        if (child.getValue().equals("BSA Type 10")) {
                            if (searchItem == grandParentEntry) {
                                hasType10 = true;
                                type10Index = child;
                            }
                        }
                    }
                    if (searchItem.previousSibling() == null) {
                        break;
                    }

                    searchItem = searchItem.previousSibling();
                }
                if (hasType10) {
                    TreeItem<String> getParent = type10Index;
                    TreeItem<String> newChild = new TreeItem<>("Entry " + getParent.getChildren().size());

                    getParent.getChildren().add(newChild);

                    bsaType10HashMap.put(newChild, (BsaType10Entry) copyContainer);

                    treeView.getSelectionModel().select(newChild);
                } 
                else {
                    grandParentEntry.getChildren().add(0, new TreeItem<>("BSA Type 10"));

                    TreeItem<String> newChild = new TreeItem<>("Entry " + 0);

                    grandParentEntry.getChildren().get(0).getChildren().add(newChild);

                    bsaType10HashMap.put(newChild, (BsaType10Entry) copyContainer);

                    sortTreeItems(grandParentEntry);

                    treeView.getSelectionModel().select(newChild);
                }
            }
            case "Add BSA Type 10 List Copy  Ctrl+A" -> {
                boolean hastype10 = false;

                TreeItem<String> type10Index = new TreeItem<>();

                while (searchItem != null) {
                    for (TreeItem<String> child : searchItem.getChildren()) {
                        if (child.getValue().equals("BSA Type 10")) {
                            if (searchItem == grandParentEntry) {
                                hastype10 = true;
                                type10Index = child;
                            }
                        }
                    }
                    if (searchItem.previousSibling() == null) {
                        break;
                    }

                    searchItem = searchItem.previousSibling();
                }
                if (hastype10) {
                    TreeItem<String> getParent = type10Index;

                    for (int i = 0; i < copyListContainer.get(0).length; i++) {
                        TreeItem<String> newChild = new TreeItem<>("Entry " + getParent.getChildren().size());

                        getParent.getChildren().add(newChild);

                        bsaType10HashMap.put(newChild, (BsaType10Entry) copyListContainer.get(0)[i]);
                    }
                } 
                else {
                    grandParentEntry.getChildren().add(0, new TreeItem<>("BSA Type 10"));

                    for (int i = 0; i < copyListContainer.get(0).length; i++) {
                        TreeItem<String> newChild = new TreeItem<>("Entry " + i);

                        grandParentEntry.getChildren().get(0).getChildren().add(newChild);

                        bsaType10HashMap.put(newChild, (BsaType10Entry) copyListContainer.get(0)[i]);
                    }

                    sortTreeItems(grandParentEntry);
                }
            }
            case "Add BSA Type 12 Copy  Ctrl+A" -> {
                boolean hasType12 = false;

                TreeItem<String> type12Index = new TreeItem<>();

                while (searchItem != null) {
                    for (TreeItem<String> child : searchItem.getChildren()) {
                        if (child.getValue().equals("BSA Type 12")) {
                            if (searchItem == grandParentEntry) {
                                hasType12 = true;
                                type12Index = child;
                            }
                        }
                    }
                    if (searchItem.previousSibling() == null) {
                        break;
                    }

                    searchItem = searchItem.previousSibling();
                }
                if (hasType12) {
                    TreeItem<String> getParent = type12Index;
                    TreeItem<String> newChild = new TreeItem<>("Entry " + getParent.getChildren().size());

                    getParent.getChildren().add(newChild);

                    bsaType12HashMap.put(newChild, (BsaType12Entry) copyContainer);

                    treeView.getSelectionModel().select(newChild);
                } 
                else {
                    grandParentEntry.getChildren().add(0, new TreeItem<>("BSA Type 12"));

                    TreeItem<String> newChild = new TreeItem<>("Entry " + 0);

                    grandParentEntry.getChildren().get(0).getChildren().add(newChild);

                    bsaType12HashMap.put(newChild, (BsaType12Entry) copyContainer);

                    sortTreeItems(grandParentEntry);

                    treeView.getSelectionModel().select(newChild);
                }
            }
            case "Add BSA Type 12 List Copy  Ctrl+A" -> {
                boolean hastype12 = false;

                TreeItem<String> type12Index = new TreeItem<>();

                while (searchItem != null) {
                    for (TreeItem<String> child : searchItem.getChildren()) {
                        if (child.getValue().equals("BSA Type 12")) {
                            if (searchItem == grandParentEntry) {
                                hastype12 = true;
                                type12Index = child;
                            }
                        }
                    }
                    if (searchItem.previousSibling() == null) {
                        break;
                    }

                    searchItem = searchItem.previousSibling();
                }
                if (hastype12) {
                    TreeItem<String> getParent = type12Index;

                    for (int i = 0; i < copyListContainer.get(0).length; i++) {
                        TreeItem<String> newChild = new TreeItem<>("Entry " + getParent.getChildren().size());

                        getParent.getChildren().add(newChild);

                        bsaType12HashMap.put(newChild, (BsaType12Entry) copyListContainer.get(0)[i]);
                    }
                } 
                else {
                    grandParentEntry.getChildren().add(0, new TreeItem<>("BSA Type 12"));

                    for (int i = 0; i < copyListContainer.get(0).length; i++) {
                        TreeItem<String> newChild = new TreeItem<>("Entry " + i);

                        grandParentEntry.getChildren().get(0).getChildren().add(newChild);

                        bsaType12HashMap.put(newChild, (BsaType12Entry) copyListContainer.get(0)[i]);
                    }

                    sortTreeItems(grandParentEntry);
                }
            }
            case "Add BSA Type 13 Copy  Ctrl+A" -> {
                boolean hasType13 = false;

                TreeItem<String> type13Index = new TreeItem<>();

                while (searchItem != null) {
                    for (TreeItem<String> child : searchItem.getChildren()) {
                        if (child.getValue().equals("BSA Type 13")) {
                            if (searchItem == grandParentEntry) {
                                hasType13 = true;
                                type13Index = child;
                            }
                        }
                    }
                    if (searchItem.previousSibling() == null) {
                        break;
                    }

                    searchItem = searchItem.previousSibling();
                }
                if (hasType13) {
                    TreeItem<String> getParent = type13Index;
                    TreeItem<String> newChild = new TreeItem<>("Entry " + getParent.getChildren().size());

                    getParent.getChildren().add(newChild);

                    bsaType13HashMap.put(newChild, (BsaType13Entry) copyContainer);

                    treeView.getSelectionModel().select(newChild);
                } 
                else {
                    grandParentEntry.getChildren().add(0, new TreeItem<>("BSA Type 13"));

                    TreeItem<String> newChild = new TreeItem<>("Entry " + 0);

                    grandParentEntry.getChildren().get(0).getChildren().add(newChild);

                    bsaType13HashMap.put(newChild, (BsaType13Entry) copyContainer);

                    sortTreeItems(grandParentEntry);

                    treeView.getSelectionModel().select(newChild);
                }
            }
            case "Add BSA Type 13 List Copy  Ctrl+A" -> {
                boolean hastype13 = false;

                TreeItem<String> type13Index = new TreeItem<>();

                while (searchItem != null) {
                    for (TreeItem<String> child : searchItem.getChildren()) {
                        if (child.getValue().equals("BSA Type 13")) {
                            if (searchItem == grandParentEntry) {
                                hastype13 = true;
                                type13Index = child;
                            }
                        }
                    }
                    if (searchItem.previousSibling() == null) {
                        break;
                    }

                    searchItem = searchItem.previousSibling();
                }
                if (hastype13) {
                    TreeItem<String> getParent = type13Index;

                    for (int i = 0; i < copyListContainer.get(0).length; i++) {
                        TreeItem<String> newChild = new TreeItem<>("Entry " + getParent.getChildren().size());

                        getParent.getChildren().add(newChild);

                        bsaType13HashMap.put(newChild, (BsaType13Entry) copyListContainer.get(0)[i]);
                    }
                } 
                else {
                    grandParentEntry.getChildren().add(0, new TreeItem<>("BSA Type 13"));

                    for (int i = 0; i < copyListContainer.get(0).length; i++) {
                        TreeItem<String> newChild = new TreeItem<>("Entry " + i);

                        grandParentEntry.getChildren().get(0).getChildren().add(newChild);

                        bsaType13HashMap.put(newChild, (BsaType13Entry) copyListContainer.get(0)[i]);
                    }

                    sortTreeItems(grandParentEntry);
                }
            }
            case "Add BSA Type 14 Copy  Ctrl+A" -> {
                boolean hasType14 = false;

                TreeItem<String> type14Index = new TreeItem<>();

                while (searchItem != null) {
                    for (TreeItem<String> child : searchItem.getChildren()) {
                        if (child.getValue().equals("BSA Type 14")) {
                            if (searchItem == grandParentEntry) {
                                hasType14 = true;
                                type14Index = child;
                            }
                        }
                    }
                    if (searchItem.previousSibling() == null) {
                        break;
                    }

                    searchItem = searchItem.previousSibling();
                }
                if (hasType14) {
                    TreeItem<String> getParent = type14Index;
                    TreeItem<String> newChild = new TreeItem<>("Entry " + getParent.getChildren().size());

                    getParent.getChildren().add(newChild);

                    bsaType14HashMap.put(newChild, (BsaType14Entry) copyContainer);

                    treeView.getSelectionModel().select(newChild);
                } 
                else {
                    grandParentEntry.getChildren().add(0, new TreeItem<>("BSA Type 14"));

                    TreeItem<String> newChild = new TreeItem<>("Entry " + 0);

                    grandParentEntry.getChildren().get(0).getChildren().add(newChild);

                    bsaType14HashMap.put(newChild, (BsaType14Entry) copyContainer);

                    sortTreeItems(grandParentEntry);

                    treeView.getSelectionModel().select(newChild);
                }
            }
            case "Add BSA Type 14 List Copy  Ctrl+A" -> {
                boolean hastype14 = false;

                TreeItem<String> type14Index = new TreeItem<>();

                while (searchItem != null) {
                    for (TreeItem<String> child : searchItem.getChildren()) {
                        if (child.getValue().equals("BSA Type 14")) {
                            if (searchItem == grandParentEntry) {
                                hastype14 = true;
                                type14Index = child;
                            }
                        }
                    }
                    if (searchItem.previousSibling() == null) {
                        break;
                    }

                    searchItem = searchItem.previousSibling();
                }
                if (hastype14) {
                    TreeItem<String> getParent = type14Index;
                    for (int i = 0; i < copyListContainer.get(0).length; i++) {
                        TreeItem<String> newChild = new TreeItem<>("Entry " + getParent.getChildren().size());

                        getParent.getChildren().add(newChild);

                        bsaType14HashMap.put(newChild, (BsaType14Entry) copyListContainer.get(0)[i]);
                    }
                } 
                else {
                    grandParentEntry.getChildren().add(0, new TreeItem<>("BSA Type 14"));

                    for (int i = 0; i < copyListContainer.get(0).length; i++) {
                        TreeItem<String> newChild = new TreeItem<>("Entry " + i);

                        grandParentEntry.getChildren().get(0).getChildren().add(newChild);

                        bsaType14HashMap.put(newChild, (BsaType14Entry) copyListContainer.get(0)[i]);
                    }

                    sortTreeItems(grandParentEntry);
                }
            }
        }
    }

    private void Delete() {
        if (currentEntry.getParent() == treeView.getRoot()) {
            bsaMainHashMap.remove(currentEntry);

            TreeItem<String> getGrandParent = currentEntry;

            for (TreeItem<String> getParent : getGrandParent.getChildren()) {
                switch (getParent.getValue()) {
                    case "Collision (After Effects)" -> {
                        for (TreeItem<String> child : getParent.getChildren()) {
                            bsaCollisionHashMap.remove(child);
                        }
                    }
                    case "Expiration (After Effects)" -> {
                        for (TreeItem<String> child : getParent.getChildren()) {
                            bsaExpirationHashMap.remove(child);
                        }
                    }
                    case "BSA Entry Passing" -> {
                        for (TreeItem<String> child : getParent.getChildren()) {
                            bsaType0HashMap.remove(child);
                        }
                    }
                    case "Movement" -> {
                        for (TreeItem<String> child : getParent.getChildren()) {
                            bsaType1HashMap.remove(child);
                        }
                    }
                    case "BSA Type 2" -> {
                        for (TreeItem<String> child : getParent.getChildren()) {
                            bsaType2HashMap.remove(child);
                        }
                    }
                    case "Hitbox" -> {
                        for (TreeItem<String> child : getParent.getChildren()) {
                            bsaType3HashMap.remove(child);
                        }
                    }
                    case "Deflection" -> {
                        for (TreeItem<String> child : getParent.getChildren()) {
                            bsaType4HashMap.remove(child);
                        }
                    }
                    case "Effect" -> {
                        for (TreeItem<String> child : getParent.getChildren()) {
                            bsaType6HashMap.remove(child);
                        }
                    }
                    case "Sound" -> {
                        for (TreeItem<String> child : getParent.getChildren()) {
                            bsaType7HashMap.remove(child);
                        }
                    }
                    case "Screen Effect" -> {
                        for (TreeItem<String> child : getParent.getChildren()) {
                            bsaType8HashMap.remove(child);
                        }
                    }
                    case "BSA Type 10" -> {
                        for (TreeItem<String> child : getParent.getChildren()) {
                            bsaType10HashMap.remove(child);
                        }
                    }
                    case "BSA Type 12" -> {
                        for (TreeItem<String> child : getParent.getChildren()) {
                            bsaType12HashMap.remove(child);
                        }
                    }
                    case "BSA Type 13" -> {
                        for (TreeItem<String> child : getParent.getChildren()) {
                            bsaType13HashMap.remove(child);
                        }
                    }
                    case "BSA Type 14" -> {
                        for (TreeItem<String> child : getParent.getChildren()) {
                            bsaType14HashMap.remove(child);
                        }
                    }
                }
            }

            treeView.getRoot().getChildren().remove(getGrandParent);
            allEntries.remove(getGrandParent);

            for (int i = 0; i < treeView.getRoot().getChildren().size(); i++) {
                treeView.getRoot().getChildren().get(i).setValue("Entry " + i);
            }
        }
        else if (currentEntry.getChildren().isEmpty() && currentEntry.getValue().startsWith("Entry")) {
            TreeItem<String> getParent = currentEntry.getParent();

            switch (currentEntry.getParent().getValue()) {
                case "Collision (After Effects)" -> {
                    bsaCollisionHashMap.remove(currentEntry);

                    getParent.getChildren().remove(currentEntry);
                    
                    for (int i = 0; i < getParent.getChildren().size(); i++) {
                        getParent.getChildren().get(i).setValue("Entry " + i);
                    }

                    if (!getParent.getChildren().isEmpty()) {
                        treeView.getSelectionModel().select(getParent.getChildren().getFirst());
                    }
                }
                case "Expiration (After Effects)" -> {
                    bsaExpirationHashMap.remove(currentEntry);

                    getParent.getChildren().remove(currentEntry);
                    
                    for (int i = 0; i < getParent.getChildren().size(); i++) {
                        getParent.getChildren().get(i).setValue("Entry " + i);
                    }

                    if (!getParent.getChildren().isEmpty()) {
                        treeView.getSelectionModel().select(getParent.getChildren().getFirst());
                    }
                }
                case "BSA Entry Passing" -> {
                    bsaType0HashMap.remove(currentEntry);

                    getParent.getChildren().remove(currentEntry);
                    
                    for (int i = 0; i < getParent.getChildren().size(); i++) {
                        getParent.getChildren().get(i).setValue("Entry " + i);
                    }

                    if (!getParent.getChildren().isEmpty()) {
                        treeView.getSelectionModel().select(getParent.getChildren().getFirst());
                    }
                }
                case "Movement" -> {
                    bsaType1HashMap.remove(currentEntry);

                    getParent.getChildren().remove(currentEntry);
                    
                    for (int i = 0; i < getParent.getChildren().size(); i++) {
                        getParent.getChildren().get(i).setValue("Entry " + i);
                    }

                    if (!getParent.getChildren().isEmpty()) {
                        treeView.getSelectionModel().select(getParent.getChildren().getFirst());
                    }
                }
                case "BSA Type 2" -> {
                    bsaType2HashMap.remove(currentEntry);

                    getParent.getChildren().remove(currentEntry);
                    
                    for (int i = 0; i < getParent.getChildren().size(); i++) {
                        getParent.getChildren().get(i).setValue("Entry " + i);
                    }

                    if (!getParent.getChildren().isEmpty()) {
                        treeView.getSelectionModel().select(getParent.getChildren().getFirst());
                    } 
                }
                case "Hitbox" -> {
                    bsaType3HashMap.remove(currentEntry);

                    getParent.getChildren().remove(currentEntry);
                    
                    for (int i = 0; i < getParent.getChildren().size(); i++) {
                        getParent.getChildren().get(i).setValue("Entry " + i);
                    }

                    if (!getParent.getChildren().isEmpty()) {
                        treeView.getSelectionModel().select(getParent.getChildren().getFirst());
                    };
                }
                case "Deflection" -> {
                    bsaType4HashMap.remove(currentEntry);

                    getParent.getChildren().remove(currentEntry);
                    
                    for (int i = 0; i < getParent.getChildren().size(); i++) {
                        getParent.getChildren().get(i).setValue("Entry " + i);
                    }

                    if (!getParent.getChildren().isEmpty()) {
                        treeView.getSelectionModel().select(getParent.getChildren().getFirst());
                    }
                }
                case "Effect" -> {
                    bsaType6HashMap.remove(currentEntry);

                    getParent.getChildren().remove(currentEntry);
                    
                    for (int i = 0; i < getParent.getChildren().size(); i++) {
                        getParent.getChildren().get(i).setValue("Entry " + i);
                    }

                    if (!getParent.getChildren().isEmpty()) {
                        treeView.getSelectionModel().select(getParent.getChildren().getFirst());
                    }
                }
                case "Sound" -> {
                    bsaType7HashMap.remove(currentEntry);

                    getParent.getChildren().remove(currentEntry);
                    
                    for (int i = 0; i < getParent.getChildren().size(); i++) {
                        getParent.getChildren().get(i).setValue("Entry " + i);
                    }

                    if (!getParent.getChildren().isEmpty()) {
                        treeView.getSelectionModel().select(getParent.getChildren().getFirst());
                    }
                }
                case "Screen Effect" -> {
                    bsaType8HashMap.remove(currentEntry);

                    getParent.getChildren().remove(currentEntry);
                    
                    for (int i = 0; i < getParent.getChildren().size(); i++) {
                        getParent.getChildren().get(i).setValue("Entry " + i);
                    }

                    if (!getParent.getChildren().isEmpty()) {
                        treeView.getSelectionModel().select(getParent.getChildren().getFirst());
                    }
                }
                case "BSA Type 10" -> {
                    bsaType10HashMap.remove(currentEntry);

                    getParent.getChildren().remove(currentEntry);
                    
                    for (int i = 0; i < getParent.getChildren().size(); i++) {
                        getParent.getChildren().get(i).setValue("Entry " + i);
                    }

                    if (!getParent.getChildren().isEmpty()) {
                        treeView.getSelectionModel().select(getParent.getChildren().getFirst());
                    }
                }
                case "BSA Type 12" -> {
                    bsaType12HashMap.remove(currentEntry);

                    getParent.getChildren().remove(currentEntry);
                    
                    for (int i = 0; i < getParent.getChildren().size(); i++) {
                        getParent.getChildren().get(i).setValue("Entry " + i);
                    }

                    if (!getParent.getChildren().isEmpty()) {
                        treeView.getSelectionModel().select(getParent.getChildren().getFirst());
                    };
                }
                case "BSA Type 13" -> {
                    bsaType13HashMap.remove(currentEntry);

                    getParent.getChildren().remove(currentEntry);
                    
                    for (int i = 0; i < getParent.getChildren().size(); i++) {
                        getParent.getChildren().get(i).setValue("Entry " + i);
                    }

                    if (!getParent.getChildren().isEmpty()) {
                        treeView.getSelectionModel().select(getParent.getChildren().getFirst());
                    }
                }
                case "BSA Type 14" -> {
                    bsaType14HashMap.remove(currentEntry);

                    getParent.getChildren().remove(currentEntry);
                    
                    for (int i = 0; i < getParent.getChildren().size(); i++) {
                        getParent.getChildren().get(i).setValue("Entry " + i);
                    }

                    if (!getParent.getChildren().isEmpty()) {
                        treeView.getSelectionModel().select(getParent.getChildren().getFirst());
                    }
                }
            }
        }
        else {
            TreeItem<String> getParent = currentEntry;

            switch (currentEntry.getValue()) {
                case "Collision (After Effects)" -> {
                    for (TreeItem<String> child : getParent.getChildren()) {
                        bsaCollisionHashMap.remove(child);
                    }
                    getParent.getChildren().removeAll(getParent.getChildren());
                    getParent.getParent().getChildren().remove(getParent);
                }
                case "Expiration (After Effects)" -> {
                    for (TreeItem<String> child : getParent.getChildren()) {
                        bsaExpirationHashMap.remove(child);
                    }
                    getParent.getChildren().removeAll(getParent.getChildren());
                    getParent.getParent().getChildren().remove(getParent);
                }
                case "BSA Entry Passing" -> {
                    for (TreeItem<String> child : getParent.getChildren()) {
                        bsaType0HashMap.remove(child);
                    }
                    getParent.getChildren().removeAll(getParent.getChildren());
                    getParent.getParent().getChildren().remove(getParent);
                }
                case "Movement" -> {
                    for (TreeItem<String> child : getParent.getChildren()) {
                        bsaType1HashMap.remove(child);
                    }
                    getParent.getChildren().removeAll(getParent.getChildren());
                    getParent.getParent().getChildren().remove(getParent);
                }
                case "BSA Type 2" -> {
                    for (TreeItem<String> child : getParent.getChildren()) {
                        bsaType2HashMap.remove(child);
                    }
                    getParent.getChildren().removeAll(getParent.getChildren());
                    getParent.getParent().getChildren().remove(getParent);
                }
                case "Hitbox" -> {
                    for (TreeItem<String> child : getParent.getChildren()) {
                        bsaType3HashMap.remove(child);
                    }
                    getParent.getChildren().removeAll(getParent.getChildren());
                    getParent.getParent().getChildren().remove(getParent);
                }
                case "Deflection" -> {
                    for (TreeItem<String> child : getParent.getChildren()) {
                        bsaType4HashMap.remove(child);
                    }
                    getParent.getChildren().removeAll(getParent.getChildren());
                    getParent.getParent().getChildren().remove(getParent);
                }
                case "Effect" -> {
                    for (TreeItem<String> child : getParent.getChildren()) {
                        bsaType6HashMap.remove(child);
                    }
                    getParent.getChildren().removeAll(getParent.getChildren());
                    getParent.getParent().getChildren().remove(getParent);
                }
                case "Sound" -> {
                    for (TreeItem<String> child : getParent.getChildren()) {
                        bsaType7HashMap.remove(child);
                    }
                    getParent.getChildren().removeAll(getParent.getChildren());
                    getParent.getParent().getChildren().remove(getParent);
                }
                case "Screen Effect" -> {
                    for (TreeItem<String> child : getParent.getChildren()) {
                        bsaType8HashMap.remove(child);
                    }
                    getParent.getChildren().removeAll(getParent.getChildren());
                    getParent.getParent().getChildren().remove(getParent);
                }
                case "BSA Type 10" -> {
                    for (TreeItem<String> child : getParent.getChildren()) {
                        bsaType10HashMap.remove(child);
                    }
                    getParent.getChildren().removeAll(getParent.getChildren());
                    getParent.getParent().getChildren().remove(getParent);
                }
                case "BSA Type 12" -> {
                    for (TreeItem<String> child : getParent.getChildren()) {
                        bsaType12HashMap.remove(child);
                    }
                    getParent.getChildren().removeAll(getParent.getChildren());
                    getParent.getParent().getChildren().remove(getParent);
                }
                case "BSA Type 13" -> {
                    for (TreeItem<String> child : getParent.getChildren()) {
                        bsaType13HashMap.remove(child);
                    }
                    getParent.getChildren().removeAll(getParent.getChildren());
                    getParent.getParent().getChildren().remove(getParent);
                }
                case "BSA Type 14" -> {
                    for (TreeItem<String> child : getParent.getChildren()) {
                        bsaType14HashMap.remove(child);
                    }
                    getParent.getChildren().removeAll(getParent.getChildren());
                    getParent.getParent().getChildren().remove(getParent);
                }
            }
        }
        
    }

    private void AddComment() {
        if (currentEntry == null) return;

        TextInputDialog textInputDialog=new TextInputDialog();
        textInputDialog.setTitle("Comment");
        textInputDialog.getDialogPane().setContentText("New Comment: ");

        textInputDialog.showAndWait().ifPresent(updatedText -> {
            currentEntry.setValue(currentEntry.getValue()+" - "+ updatedText);
        });
    }

    private void sortTreeItems(TreeItem<String> treeItem) {

        List<String> bsaTypesList = Arrays.asList(
            "Collision (After Effects)",
            "Expiration (After Effects)",
            "BSA Entry Passing",
            "Movement",
            "BSA Type 2",
            "Hitbox",
            "Deflection",
            "Effect",
            "Sound",
            "Screen Effect",
            "BSA Type 10",
            "BSA Type 12",
            "BSA Type 13",
            "BSA Type 14"
        );

        treeItem.getChildren().sort((item1, item2) -> {
            int index1 = bsaTypesList.indexOf(item1.getValue());
            int index2 = bsaTypesList.indexOf(item2.getValue());
            
            return Integer.compare(index1, index2);
        });
    }

    public void bsaReader(Path path) {
        try(FileChannel channel = FileChannel.open(path, StandardOpenOption.READ)) {
            short bsaEntries;
            int offset;
            int entryOffset;
            int collisionOffset;
            int expirationOffset;
            int typesOffset;
            short typesCount;
            short type;
            short typeCount;
            int hdrOffset;
            int dataOffset;
            int collisionEntriesCount;
            int expirationEntriesCount;

            ByteBuffer byteBuffer = ByteBuffer.allocate(1).order(ByteOrder.LITTLE_ENDIAN);
            ByteBuffer shortBuffer = ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN);
            ByteBuffer intBuffer = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
            
            channel.position(18);
            shortBuffer.clear();
            channel.read(shortBuffer);
            shortBuffer.flip();
            bsaEntries = shortBuffer.getShort();

            if (bsaEntries > 0) {
                treeView.setRoot(new TreeItem<>("dummy"));
                treeView.setShowRoot(false);
            }

            allEntries = new ArrayList<>(bsaEntries);

            channel.position(20);
            intBuffer.clear();
            channel.read(intBuffer);
            intBuffer.flip();
            offset = intBuffer.getInt();

            for (int i = 0; i < bsaEntries; i++) {
                int index = 0;

                channel.position(offset + i * 4);
                intBuffer.clear();
                channel.read(intBuffer);
                intBuffer.flip();
                entryOffset = intBuffer.getInt();

                if(entryOffset != 0) {
                    allEntries.add(new TreeItem<>("Entry " + i));
                    treeView.getRoot().getChildren().add(allEntries.get(i));

                    BsaMainEntry bsaMainEntry = new BsaMainEntry();
                    bsaMainHashMap.put(allEntries.get(i), bsaMainEntry);

                    channel.position(entryOffset);
                    intBuffer.clear();
                    channel.read(intBuffer);
                    intBuffer.flip();
                    bsaMainEntry.i00 = intBuffer.getInt();

                    channel.position(entryOffset + 4);
                    shortBuffer.clear();
                    channel.read(shortBuffer);
                    shortBuffer.flip();
                    collisionEntriesCount = toUShort(shortBuffer.getShort());
                    
                    channel.position(entryOffset + 6);
                    shortBuffer.clear();
                    channel.read(shortBuffer);
                    shortBuffer.flip();
                    expirationEntriesCount = toUShort(shortBuffer.getShort());

                    channel.position(entryOffset + 8);
                    intBuffer.clear();
                    channel.read(intBuffer);
                    intBuffer.flip();
                    collisionOffset = intBuffer.getInt();
                    collisionOffset += entryOffset;

                    if (collisionEntriesCount > 0) {
                        allEntries.get(i).getChildren().add(new TreeItem<>("Collision (After Effects)"));

                        for (int j = 0; j < collisionEntriesCount; j++) {
                            BsaCollisionEntry bsaCollisionEntry = new BsaCollisionEntry();

                            allEntries.get(i).getChildren().get(index).getChildren().add(new TreeItem<>("Entry "+ j));

                            bsaCollisionHashMap.put(allEntries.get(i).getChildren().get(index).getChildren().get(j), bsaCollisionEntry);

                            channel.position(collisionOffset + j * 24);
                            shortBuffer.clear();
                            channel.read(shortBuffer);
                            shortBuffer.flip();
                            bsaCollisionEntry.eepkType = toUShort(shortBuffer.getShort());

                            channel.position(collisionOffset + j * 24 + 2);
                            shortBuffer.clear();
                            channel.read(shortBuffer);
                            shortBuffer.flip();
                            bsaCollisionEntry.skillId = toUShort(shortBuffer.getShort());

                            channel.position(collisionOffset + j * 24 + 4);
                            shortBuffer.clear();
                            channel.read(shortBuffer);
                            shortBuffer.flip();
                            bsaCollisionEntry.effectId = toUShort(shortBuffer.getShort());

                            channel.position(collisionOffset + j * 24 + 6);
                            shortBuffer.clear();
                            channel.read(shortBuffer);
                            shortBuffer.flip();
                            bsaCollisionEntry.i06 = toUShort(shortBuffer.getShort());

                            channel.position(collisionOffset + j * 24 + 8);
                            intBuffer.clear();
                            channel.read(intBuffer);
                            intBuffer.flip();
                            bsaCollisionEntry.i08 = intBuffer.getInt();

                            channel.position(collisionOffset + j * 24 + 12);
                            intBuffer.clear();
                            channel.read(intBuffer);
                            intBuffer.flip();
                            bsaCollisionEntry.i12 = intBuffer.getInt();

                            channel.position(collisionOffset + j * 24 + 16);
                            intBuffer.clear();
                            channel.read(intBuffer);
                            intBuffer.flip();
                            bsaCollisionEntry.i16 = intBuffer.getInt();

                            channel.position(collisionOffset+ j * 24 + 20);
                            intBuffer.clear();
                            channel.read(intBuffer);
                            intBuffer.flip();
                            bsaCollisionEntry.i20 = intBuffer.getInt();
                        }

                        index++;
                    } 

                    channel.position(entryOffset + 12);
                    intBuffer.clear();
                    channel.read(intBuffer);
                    intBuffer.flip();
                    expirationOffset = intBuffer.getInt();
                    expirationOffset += entryOffset;

                    if (expirationEntriesCount > 0) {
                        allEntries.get(i).getChildren().add(new TreeItem<>("Expiration (After Effects)"));

                        for (int j = 0; j < expirationEntriesCount; j++) {
                            BsaExpirationEntry bsaExpirationEntry = new BsaExpirationEntry();

                            allEntries.get(i).getChildren().get(index).getChildren().add(new TreeItem<>("Entry " + j));

                            bsaExpirationHashMap.put(allEntries.get(i).getChildren().get(index).getChildren().get(j), bsaExpirationEntry);
                            
                            channel.position(expirationOffset + j * 8);
                            shortBuffer.clear();
                            channel.read(shortBuffer);
                            shortBuffer.flip();
                            bsaExpirationEntry.i00 = toUShort(shortBuffer.getShort());

                            channel.position(expirationOffset + j * 8 + 2);
                            shortBuffer.clear();
                            channel.read(shortBuffer);
                            shortBuffer.flip();
                            bsaExpirationEntry.i02 = toUShort(shortBuffer.getShort());

                            channel.position(expirationOffset + j * 8 + 4);
                            shortBuffer.clear();
                            channel.read(shortBuffer);
                            shortBuffer.flip();
                            bsaExpirationEntry.i04 = toUShort(shortBuffer.getShort());

                            channel.position(expirationOffset + j * 8 + 6);
                            shortBuffer.clear();
                            channel.read(shortBuffer);
                            shortBuffer.flip();
                            bsaExpirationEntry.i06 = toUShort(shortBuffer.getShort());
                        }

                        index++;
                    } 

                    channel.position(entryOffset + 16);
                    byteBuffer.clear();
                    channel.read(byteBuffer);
                    byteBuffer.flip();
                    bsaMainEntry.i16_a = (byte) (byteBuffer.get() & 0x0F);
                    byteBuffer.flip();
                    bsaMainEntry.i16_b = (byte)((byteBuffer.get() >> 4) & 0x0F);

                    channel.position(entryOffset + 17);
                    byteBuffer.clear();
                    channel.read(byteBuffer);
                    byteBuffer.flip();
                    bsaMainEntry.i17 = toUByte(byteBuffer.get());

                    channel.position(entryOffset + 18);
                    intBuffer.clear();
                    channel.read(intBuffer);
                    intBuffer.flip();
                    bsaMainEntry.i18 = intBuffer.getInt();

                    channel.position(entryOffset + 22);
                    shortBuffer.clear();
                    channel.read(shortBuffer);
                    shortBuffer.flip();
                    bsaMainEntry.lifetime = toUShort(shortBuffer.getShort());

                    channel.position(entryOffset + 24);
                    shortBuffer.clear();
                    channel.read(shortBuffer);
                    shortBuffer.flip();
                    bsaMainEntry.i24 = toUShort(shortBuffer.getShort());

                    channel.position(entryOffset + 26);
                    shortBuffer.clear();
                    channel.read(shortBuffer);
                    shortBuffer.flip();
                    bsaMainEntry.expires = toUShort(shortBuffer.getShort());

                    channel.position(entryOffset + 28);
                    shortBuffer.clear();
                    channel.read(shortBuffer);
                    shortBuffer.flip();
                    bsaMainEntry.impactProjectile = toUShort(shortBuffer.getShort());

                    channel.position(entryOffset + 30);
                    shortBuffer.clear();
                    channel.read(shortBuffer);
                    shortBuffer.flip();
                    bsaMainEntry.impactEnemy = toUShort(shortBuffer.getShort());

                    channel.position(entryOffset + 32);
                    shortBuffer.clear();
                    channel.read(shortBuffer);
                    shortBuffer.flip();
                    bsaMainEntry.impactGround = toUShort(shortBuffer.getShort());

                    channel.position(entryOffset + 34);
                    shortBuffer.clear();
                    channel.read(shortBuffer);
                    shortBuffer.flip();
                    typesCount = shortBuffer.getShort();

                    channel.position(entryOffset + 36);
                    intBuffer.clear();
                    channel.read(intBuffer);
                    intBuffer.flip();
                    typesOffset = intBuffer.getInt();
                    typesOffset += entryOffset;

                    channel.position(entryOffset + 40);
                    intBuffer.clear();
                    channel.read(intBuffer);
                    intBuffer.flip();
                    bsaMainEntry.i40 = intBuffer.getInt();

                    channel.position(entryOffset + 44);
                    intBuffer.clear();
                    channel.read(intBuffer);
                    intBuffer.flip();
                    bsaMainEntry.i44 = intBuffer.getInt();

                    channel.position(entryOffset + 48);
                    intBuffer.clear();
                    channel.read(intBuffer);
                    intBuffer.flip();
                    bsaMainEntry.i48 = intBuffer.getInt();

                    for (int j = 0; j < typesCount; j++) {
                        channel.position(typesOffset + j * 16);
                        shortBuffer.clear();
                        channel.read(shortBuffer);
                        shortBuffer.flip();
                        type = shortBuffer.getShort();

                        channel.position(typesOffset + j * 16 + 6);
                        shortBuffer.clear();
                        channel.read(shortBuffer);
                        shortBuffer.flip();
                        typeCount = shortBuffer.getShort();

                        channel.position(typesOffset + j * 16+ 8);
                        intBuffer.clear();
                        channel.read(intBuffer);
                        intBuffer.flip();
                        hdrOffset = intBuffer.getInt();

                        channel.position(typesOffset + j * 16 + 12);
                        intBuffer.clear();
                        channel.read(intBuffer);
                        intBuffer.flip();
                        dataOffset = intBuffer.getInt();

                        switch (type) {
                            case 0 -> {
                                allEntries.get(i).getChildren().add(new TreeItem<>("BSA Entry Passing"));

                                for (int k = 0; k < typeCount; k++) {
                                    BsaType0Entry bsaType0Entry = new BsaType0Entry();

                                    allEntries.get(i).getChildren().get(index).getChildren().add(new TreeItem<>("Entry " + k));

                                    bsaType0HashMap.put(allEntries.get(i).getChildren().get(index).getChildren().get(k), bsaType0Entry);

                                    channel.position(typesOffset + hdrOffset + j * 16 + k * 4);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType0Entry.startTime = toUShort(shortBuffer.getShort());

                                    channel.position(typesOffset + hdrOffset + j * 16 + k * 4 + 2);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType0Entry.duration = toUShort((short)(shortBuffer.getShort()- bsaType0Entry.startTime));
                                    
                                    channel.position(typesOffset + dataOffset + j * 16 + k * 16);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType0Entry.firstCondition = toUShort(shortBuffer.getShort());
                                    
                                    channel.position(typesOffset + dataOffset + j * 16 + k * 16 + 2);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType0Entry.secondCondition = toUShort(shortBuffer.getShort());

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 16 + 4);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType0Entry.bsaEntryId = toUShort(shortBuffer.getShort());

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 16 + 6);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType0Entry.i06 = shortBuffer.getShort();
    
                                    channel.position(typesOffset + dataOffset + j * 16 + k * 16 + 8);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType0Entry.bacCondition = intBuffer.getFloat();

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 16 + 12);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType0Entry.f12 = intBuffer.getFloat();
                                }

                                index++;
                            }
                            case 1 -> {
                                allEntries.get(i).getChildren().add(new TreeItem<>("Movement"));

                                for (int k = 0; k < typeCount; k++) {
                                    BsaType1Entry bsaType1Entry = new BsaType1Entry();

                                    allEntries.get(i).getChildren().get(index).getChildren().add(new TreeItem<>("Entry " + k));

                                    bsaType1HashMap.put(allEntries.get(i).getChildren().get(index).getChildren().get(k), bsaType1Entry);

                                    channel.position(typesOffset + hdrOffset + j * 16 + k * 4);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType1Entry.startTime = toUShort(shortBuffer.getShort());

                                    channel.position(typesOffset + hdrOffset + j * 16 + k * 4 + 2);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType1Entry.duration = toUShort((short)(shortBuffer.getShort() - bsaType1Entry.startTime));

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 48);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType1Entry.motionFlags = toUint32(intBuffer.getInt());

                                    channel.position(typesOffset + dataOffset + j * 16+ k * 48 + 4);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType1Entry.speedZ = intBuffer.getFloat();

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 48 + 8);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType1Entry.speedX = intBuffer.getFloat();

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 48 + 12);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType1Entry.speedY = intBuffer.getFloat();

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 48 + 16);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType1Entry.f16 = intBuffer.getFloat();

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 48 + 20);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType1Entry.accelerationZ = intBuffer.getFloat();

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 48 + 24);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType1Entry.accelerationX = intBuffer.getFloat();

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 48 + 28);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType1Entry.accelerationY = intBuffer.getFloat();

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 48 + 32);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType1Entry.fallofStrength = intBuffer.getFloat();

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 48 + 36);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType1Entry.spreadDirectionX = intBuffer.getFloat();

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 48 + 40);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType1Entry.spreadDirectionY = intBuffer.getFloat();

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 48 + 44);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType1Entry.spreadDirectionZ = intBuffer.getFloat();
                                }

                                index++;
                            }
                            case 2 -> {
                                allEntries.get(i).getChildren().add(new TreeItem<>("BSA Type 2"));

                                for (int k = 0; k < typeCount; k++) {
                                    BsaType2Entry bsaType2Entry = new BsaType2Entry();

                                    allEntries.get(i).getChildren().get(index).getChildren().add(new TreeItem<>("Entry " + k));

                                    bsaType2HashMap.put(allEntries.get(i).getChildren().get(index).getChildren().get(k), bsaType2Entry);
                                    
                                    channel.position(typesOffset + hdrOffset + j * 16 + k * 4);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType2Entry.startTime = toUShort(shortBuffer.getShort());

                                    channel.position(typesOffset + hdrOffset + j * 16 + k * 4 + 2);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType2Entry.duration = toUShort((short)(shortBuffer.getShort() - bsaType2Entry.startTime));

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 8);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType2Entry.i00 = shortBuffer.getShort();

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 8 + 2);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType2Entry.i02 = shortBuffer.getShort();

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 8 + 4);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType2Entry.i04 = shortBuffer.getShort();

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 8 + 6);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType2Entry.i06 = shortBuffer.getShort();
                                }

                                index++;
                            }
                            case 3 -> {
                                allEntries.get(i).getChildren().add(new TreeItem<>("Hitbox"));

                                for (int k = 0; k < typeCount; k++) {
                                    BsaType3Entry bsaType3Entry = new BsaType3Entry();

                                    allEntries.get(i).getChildren().get(index).getChildren().add(new TreeItem<>("Entry " + k));

                                    bsaType3HashMap.put(allEntries.get(i).getChildren().get(index).getChildren().get(k), bsaType3Entry);

                                    channel.position(typesOffset + hdrOffset + j * 16 + k  *4);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType3Entry.startTime = toUShort(shortBuffer.getShort());

                                    channel.position(typesOffset + hdrOffset + j * 16 + k * 4 + 2);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType3Entry.duration = toUShort((short)(shortBuffer.getShort() - bsaType3Entry.startTime));

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 64);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType3Entry.matrixFlag = (intBuffer.getInt() == 1);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 64 + 4);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType3Entry.i04 = toUShort(shortBuffer.getShort());

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 64 + 6);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType3Entry.i06_a = (byte) (shortBuffer.getShort() & 0x0F);
                                    shortBuffer.flip();
                                    bsaType3Entry.i06_b = (byte)((shortBuffer.getShort() >> 4) & 0x0F);
                                    shortBuffer.flip();
                                    bsaType3Entry.i06_c = (byte)((shortBuffer.getShort() >> 8) & 0x0F);
                                    shortBuffer.flip();
                                    bsaType3Entry.i06_d = (byte)((shortBuffer.getShort() >> 12) & 0x0F);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 64 + 8);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType3Entry.positionX = intBuffer.getFloat();

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 64 + 12);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType3Entry.positionY = intBuffer.getFloat();

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 64 + 16);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType3Entry.positionZ = intBuffer.getFloat();

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 64 + 20);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType3Entry.hitboxScale = intBuffer.getFloat();

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 64 + 24);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType3Entry.maximumX = intBuffer.getFloat();

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 64 + 28);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType3Entry.maximumY = intBuffer.getFloat();
                                    
                                    channel.position(typesOffset + dataOffset + j * 16 + k * 64 + 32);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType3Entry.maximumZ = intBuffer.getFloat();

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 64 + 36);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType3Entry.minimumX = intBuffer.getFloat();

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 64 + 40);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType3Entry.minimumY = intBuffer.getFloat();

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 64 + 44);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType3Entry.minimumZ = intBuffer.getFloat();

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 64 + 48);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType3Entry.hitAmount = toUShort(shortBuffer.getShort());

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 64 + 50);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType3Entry.hitboxLifetime = toUShort(shortBuffer.getShort());

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 64 + 52);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType3Entry.i52 = toUShort(shortBuffer.getShort());

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 64 + 54);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType3Entry.i54 = toUShort(shortBuffer.getShort());

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 64 + 56);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType3Entry.i56 = toUShort(shortBuffer.getShort());

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 64 + 58);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType3Entry.firstHit = toUShort(shortBuffer.getShort());

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 64 + 60);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType3Entry.multipleHits = toUShort(shortBuffer.getShort());;

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 64 + 62);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType3Entry.lastHit = toUShort(shortBuffer.getShort());
                                }

                                index++;
                            } 
                            case 4 -> {
                                allEntries.get(i).getChildren().add(new TreeItem<>("Deflection"));

                                for (int k = 0; k < typeCount; k++) {
                                    BsaType4Entry bsaType4Entry = new BsaType4Entry();

                                    allEntries.get(i).getChildren().get(index).getChildren().add(new TreeItem<>("Entry " + k));

                                    bsaType4HashMap.put(allEntries.get(i).getChildren().get(index).getChildren().get(k), bsaType4Entry);

                                    channel.position(typesOffset + hdrOffset + j * 16 + k * 4);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType4Entry.startTime = toUShort(shortBuffer.getShort());

                                    channel.position(typesOffset + hdrOffset + j * 16 + k * 4 + 2);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType4Entry.duration = toUShort((short)(shortBuffer.getShort() - bsaType4Entry.startTime));

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 56);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType4Entry.i00 = intBuffer.getInt();

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 56 + 4);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType4Entry.i04 = intBuffer.getInt();

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 56 + 8);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType4Entry.i08 = intBuffer.getInt();

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 56 + 12);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType4Entry.f12 = intBuffer.getFloat();

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 56 + 16);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType4Entry.f16 = intBuffer.getFloat();

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 56 + 20);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType4Entry.f20 = intBuffer.getFloat();

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 56 + 24);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType4Entry.i24 = intBuffer.getInt();

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 56 + 28);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType4Entry.i28 = intBuffer.getInt();

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 56 + 32);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType4Entry.i32 = intBuffer.getInt();

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 56 + 36);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType4Entry.i36 = intBuffer.getInt();

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 56 + 40);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType4Entry.i40 = intBuffer.getInt();

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 56 + 44);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType4Entry.i44 = intBuffer.getInt();

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 56 + 48);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType4Entry.i48 = toUShort(shortBuffer.getShort());

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 56 + 50);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType4Entry.i50 = toUShort(shortBuffer.getShort());

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 56 + 52);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType4Entry.i52 = toUShort(shortBuffer.getShort());

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 56 + 54);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType4Entry.i54 = toUShort(shortBuffer.getShort());
                                }

                                index++;
                            }
                            case 6 -> {
                                allEntries.get(i).getChildren().add(new TreeItem<>("Effect"));

                                for (int k = 0; k < typeCount; k++) {
                                    BsaType6Entry bsaType6Entry = new BsaType6Entry();

                                    allEntries.get(i).getChildren().get(index).getChildren().add(new TreeItem<>("Entry " + k));

                                    bsaType6HashMap.put(allEntries.get(i).getChildren().get(index).getChildren().get(k), bsaType6Entry);

                                    channel.position(typesOffset + hdrOffset + j * 16 + k * 4);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType6Entry.startTime = toUShort(shortBuffer.getShort());

                                    channel.position(typesOffset + hdrOffset + j * 16 + k * 4 + 2);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType6Entry.duration = toUShort((short)(shortBuffer.getShort() - bsaType6Entry.startTime));

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 24);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType6Entry.eepkType = toUShort(shortBuffer.getShort());

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 24 + 2);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType6Entry.skillId = toUShort(shortBuffer.getShort());

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 24 + 4);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType6Entry.effectId = toUShort(shortBuffer.getShort());

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 24 + 6);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType6Entry.i06 = toUShort(shortBuffer.getShort());

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 24 + 8);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType6Entry.effectSwitch = toUShort(shortBuffer.getShort());

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 24 + 10);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType6Entry.i10 = toUShort(shortBuffer.getShort());

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 24 + 12);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType6Entry.positionX = intBuffer.getFloat();

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 24 + 16);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType6Entry.positionY = intBuffer.getFloat();

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 24 + 20);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType6Entry.positionZ = intBuffer.getFloat();
                                }

                                index++;
                            }
                            case 7 -> {
                                allEntries.get(i).getChildren().add(new TreeItem<>("Sound"));

                                for (int k = 0; k < typeCount; k++) {
                                    BsaType7Entry bsaType7Entry = new BsaType7Entry();

                                    allEntries.get(i).getChildren().get(index).getChildren().add(new TreeItem<>("Entry " + k));

                                    bsaType7HashMap.put(allEntries.get(i).getChildren().get(index).getChildren().get(k), bsaType7Entry);

                                    channel.position(typesOffset + hdrOffset + j * 16 + k * 4);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType7Entry.startTime = toUShort(shortBuffer.getShort());

                                    channel.position(typesOffset + hdrOffset + j * 16 + k * 4 + 2);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType7Entry.duration = toUShort((short)(shortBuffer.getShort() - bsaType7Entry.startTime));

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 8);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType7Entry.acbType = toUShort(shortBuffer.getShort());

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 8 + 2);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType7Entry.i02 = toUShort(shortBuffer.getShort());

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 8 + 4);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType7Entry.cueId = toUShort(shortBuffer.getShort());

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 8 + 6);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType7Entry.i06 = toUShort(shortBuffer.getShort());
                                }

                                index++;
                            }
                            case 8 -> {
                                allEntries.get(i).getChildren().add(new TreeItem<>("Screen Effect"));

                                for (int k = 0; k < typeCount; k++) {
                                    BsaType8Entry bsaType8Entry = new BsaType8Entry();

                                    allEntries.get(i).getChildren().get(index).getChildren().add(new TreeItem<>("Entry " + k));

                                    bsaType8HashMap.put(allEntries.get(i).getChildren().get(index).getChildren().get(k), bsaType8Entry);

                                    channel.position(typesOffset + hdrOffset + j * 16 + k * 4);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType8Entry.startTime = toUShort(shortBuffer.getShort());

                                    channel.position(typesOffset + hdrOffset + j * 16 + k * 4 + 2);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType8Entry.duration = toUShort((short)(shortBuffer.getShort() - bsaType8Entry.startTime));

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 24);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType8Entry.bpeEffectId = toUShort(shortBuffer.getShort());

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 24 + 2);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType8Entry.i02 = toUShort(shortBuffer.getShort());

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 24 + 4);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType8Entry.i04 = toUShort(shortBuffer.getShort());

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 24 + 8);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType8Entry.i08 = intBuffer.getInt();

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 24 + 12);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType8Entry.i12 = intBuffer.getInt();

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 24 + 16);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType8Entry.i16 = intBuffer.getInt();

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 24 + 20);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType8Entry.i20 = intBuffer.getInt();
                                }

                                index++;
                            }
                            case 10 -> {
                                allEntries.get(i).getChildren().add(new TreeItem<>("BSA Type 10"));

                                for (int k=0; k < typeCount; k++) {
                                    BsaType10Entry bsaType10Entry = new BsaType10Entry();

                                    allEntries.get(i).getChildren().get(index).getChildren().add(new TreeItem<>("Entry " + k));

                                    bsaType10HashMap.put(allEntries.get(i).getChildren().get(index).getChildren().get(k), bsaType10Entry);
                                    
                                    channel.position(typesOffset + hdrOffset + j * 16 + k * 4);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType10Entry.startTime = toUShort(shortBuffer.getShort());

                                    channel.position(typesOffset + hdrOffset + j * 16 + k * 4 + 2);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType10Entry.duration = toUShort((short)(shortBuffer.getShort() - bsaType10Entry.startTime));

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 8);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType10Entry.skillId = intBuffer.getInt();

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 8 + 4);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType10Entry.i04 = toUShort(shortBuffer.getShort());

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 8 + 6);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType10Entry.i06 = toUShort(shortBuffer.getShort());
                                }

                                index++;
                            }
                            case 12 -> {
                                allEntries.get(i).getChildren().add(new TreeItem<>("BSA Type 12"));

                                for (int k = 0; k < typeCount; k++) {
                                    BsaType12Entry bsaType12Entry = new BsaType12Entry();

                                    allEntries.get(i).getChildren().get(index).getChildren().add(new TreeItem<>("Entry " + k));

                                    bsaType12HashMap.put(allEntries.get(i).getChildren().get(index).getChildren().get(k), bsaType12Entry);

                                    channel.position(typesOffset + hdrOffset + j * 16 + k * 4);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType12Entry.startTime = toUShort(shortBuffer.getShort());

                                    channel.position(typesOffset + hdrOffset + j * 16 + k * 4 + 2);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType12Entry.duration = toUShort((short)(shortBuffer.getShort() - bsaType12Entry.startTime));

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 20);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType12Entry.f00 = intBuffer.getInt();

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 20 + 4);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType12Entry.eepkType = intBuffer.getInt();

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 20 + 8);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType12Entry.skillId = intBuffer.getInt();

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 20 + 12);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType12Entry.i12 = intBuffer.getInt();

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 20 +16);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType12Entry.f16 = intBuffer.getInt();
                                }

                                index++;
                            }
                            case 13 -> {
                                allEntries.get(i).getChildren().add(new TreeItem<>("BSA Type 13"));

                                for (int k = 0; k < typeCount; k++) {
                                    BsaType13Entry bsaType13Entry = new BsaType13Entry();

                                    allEntries.get(i).getChildren().get(index).getChildren().add(new TreeItem<>("Entry "+ k));

                                    bsaType13HashMap.put(allEntries.get(i).getChildren().get(index).getChildren().get(k), bsaType13Entry);

                                    channel.position(typesOffset + hdrOffset + j * 16 + k * 4);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType13Entry.startTime = toUShort(shortBuffer.getShort());

                                    channel.position(typesOffset + hdrOffset + j * 16 + k * 4 + 2);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType13Entry.duration = toUShort((short)(shortBuffer.getShort() - bsaType13Entry.startTime));

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 32);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType13Entry.i00 = toUShort(shortBuffer.getShort());

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 32 + 2);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType13Entry.i02 = toUShort(shortBuffer.getShort());

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 32 + 4);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType13Entry.f04 = intBuffer.getFloat();

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 32 + 8);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType13Entry.f08 = intBuffer.getFloat();

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 32 + 12);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType13Entry.i12 = intBuffer.getInt();

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 32 + 16);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType13Entry.f16 = intBuffer.getFloat();

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 32 + 20);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType13Entry.i20 = intBuffer.getInt();

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 32 + 24);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType13Entry.i24 = intBuffer.getInt();

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 32 + 28);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType13Entry.i28 = intBuffer.getInt();
                                }

                                index++;
                            }
                            case 14 -> {
                                allEntries.get(i).getChildren().add(new TreeItem<>("BSA Type 14"));

                                for (int k = 0;k < typeCount; k++) {
                                    BsaType14Entry bsaType14Entry = new BsaType14Entry();

                                    allEntries.get(i).getChildren().get(index).getChildren().add(new TreeItem<>("Entry " + k));

                                    bsaType14HashMap.put(allEntries.get(i).getChildren().get(index).getChildren().get(k), bsaType14Entry);

                                    channel.position(typesOffset + hdrOffset + j * 16 + k * 4);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType14Entry.startTime = toUShort(shortBuffer.getShort());

                                    channel.position(typesOffset + hdrOffset + j * 16 + k * 4 + 2);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType14Entry.duration = toUShort((short)(shortBuffer.getShort() - bsaType14Entry.startTime));

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 88);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType14Entry.i00 = toUShort(shortBuffer.getShort());

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 88 + 2);
                                    shortBuffer.clear();
                                    channel.read(shortBuffer);
                                    shortBuffer.flip();
                                    bsaType14Entry.i02 = toUShort(shortBuffer.getShort());

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 88 + 4);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType14Entry.f04 = intBuffer.getFloat();

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 88 + 8);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType14Entry.i08 = toUint32(intBuffer.getInt());

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 88 + 12);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType14Entry.f12 = intBuffer.getFloat();

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 88 + 16);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType14Entry.i16 = toUint32(intBuffer.getInt());

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 88 + 20);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType14Entry.f20 = intBuffer.getFloat();

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 88 + 24);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType14Entry.i24 = toUint32(intBuffer.getInt());

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 88 + 28);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType14Entry.f28 = intBuffer.getFloat();

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 88 + 32);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType14Entry.i32 = toUint32(intBuffer.getInt());

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 88 + 36);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType14Entry.i36 = toUint32(intBuffer.getInt());

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 88 + 40);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType14Entry.i40 = toUint32(intBuffer.getInt());

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 88 + 44);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType14Entry.f44 = intBuffer.getFloat();

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 88 + 48);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType14Entry.i48 = toUint32(intBuffer.getInt());

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 88 + 52);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType14Entry.f52 = intBuffer.getFloat();

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 88 + 56);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType14Entry.i56 = toUint32(intBuffer.getInt());

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 88 + 60);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType14Entry.f60 = intBuffer.getFloat();

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 88 + 64);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType14Entry.i64 = toUint32(intBuffer.getInt());

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 88 + 68);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType14Entry.f68 = intBuffer.getFloat();

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 88 + 72);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType14Entry.i72 = toUint32(intBuffer.getInt());

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 88 + 76);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType14Entry.i76 = toUint32(intBuffer.getInt());

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 88 + 80);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType14Entry.i80 = toUint32(intBuffer.getInt());

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 88 + 84);
                                    intBuffer.clear();
                                    channel.read(intBuffer);
                                    intBuffer.flip();
                                    bsaType14Entry.i84 = toUint32(intBuffer.getInt());
                                }

                                index++;
                            }
                        }
                    }
                }
                else {
                    allEntries.add(new TreeItem<>(null));
                }
            }
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void bsaWriter(Path path) {
        try(FileChannel channel = FileChannel.open(path, StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            int offset = 24;
            int entryOffset = 24 + allEntries.size() * 4;
            int collisionOffset = 52;
            int expirationOffset = 52;
            int  typesOffset = 52;
            short type = 0;
            short typeCount;
            int hdrOffset = 192;
            int dataOffset = 0;
            int collisionEntriesCount = 0;
            int expirationEntriesCount = 0;
            int typeSize = 0;
            int entrySize = 0;
            
            ByteBuffer byteBuffer = ByteBuffer.allocate(1).order(ByteOrder.LITTLE_ENDIAN);
            ByteBuffer shortBuffer = ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN);
            ByteBuffer intBuffer = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);

            channel.position(0);
            channel.write(ByteBuffer.wrap(new byte[] {0x23, 0x42, 0x53, 0x41}));

            channel.position(4);
            channel.write(ByteBuffer.wrap(new byte[]{(byte)0xFE, (byte)0xFF}));

            channel.position(6);
            channel.write(ByteBuffer.wrap(new byte[] {0x18}));

            channel.position(18);
            shortBuffer.clear();
            shortBuffer.putShort((short)allEntries.size());
            shortBuffer.flip();
            channel.write(shortBuffer);

            channel.position(20);
            intBuffer.clear();
            intBuffer.putInt(offset);
            intBuffer.flip();
            channel.write(intBuffer);
            
            for (int i = 0; i < allEntries.size(); i++) {
                if(allEntries.get(i).getValue() != null) {
                    BsaMainEntry bsaMainEntry = new BsaMainEntry();
                    bsaMainEntry = bsaMainHashMap.get(allEntries.get(i));

                    int index = 0;
                    short typesCount = 0;
                    
                    channel.position(offset + i * 4);
                    intBuffer.clear();
                    intBuffer.putInt(entryOffset);
                    intBuffer.flip();
                    channel.write(intBuffer);

                    channel.position(entryOffset);
                    intBuffer.clear();
                    intBuffer.putInt(bsaMainEntry.i00);
                    intBuffer.flip();
                    channel.write(intBuffer);

                    if (allEntries.get(i).getChildren().get(index).getValue().equals("Collision (After Effects)")) {
                        collisionEntriesCount = allEntries.get(i).getChildren().get(index).getChildren().size();
                    }
                    else {
                        collisionEntriesCount = 0;
                        collisionOffset = 0;
                    }
                    
                    channel.position(entryOffset + 4);
                    shortBuffer.clear();
                    shortBuffer.putShort((short) collisionEntriesCount);
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    channel.position(entryOffset + 8);
                    intBuffer.clear();
                    intBuffer.putInt(collisionOffset);
                    intBuffer.flip();
                    channel.write(intBuffer);
                    collisionOffset += entryOffset;
                    
                    if (collisionEntriesCount > 0) {
                        for (int j = 0; j < collisionEntriesCount; j++) {
                            BsaCollisionEntry bsaCollisionEntry = new BsaCollisionEntry();
                            bsaCollisionEntry = bsaCollisionHashMap.get(allEntries.get(i).getChildren().get(index).getChildren().get(j));

                            channel.position(collisionOffset + j * 24);
                            shortBuffer.clear();
                            shortBuffer.putShort((short) bsaCollisionEntry.eepkType);
                            shortBuffer.flip();
                            channel.write(shortBuffer);

                            channel.position(collisionOffset + j * 24 + 2);
                            shortBuffer.clear();
                            shortBuffer.putShort((short) bsaCollisionEntry.skillId);
                            shortBuffer.flip();
                            channel.write(shortBuffer);

                            channel.position(collisionOffset + j * 24 + 4);
                            shortBuffer.clear();
                            shortBuffer.putShort((short) bsaCollisionEntry.effectId);;
                            shortBuffer.flip();
                            channel.write(shortBuffer);

                            channel.position(collisionOffset + j * 24 + 6);
                            shortBuffer.clear();
                            shortBuffer.putShort((short) bsaCollisionEntry.i06);
                            shortBuffer.flip();
                            channel.write(shortBuffer);

                            channel.position(collisionOffset + j * 24 + 8);
                            intBuffer.clear();
                            intBuffer.putInt(bsaCollisionEntry.i08);
                            intBuffer.flip();
                            channel.write(intBuffer);

                            channel.position(collisionOffset + j * 24 + 12);
                            intBuffer.clear();
                            intBuffer.putInt(bsaCollisionEntry.i12);
                            intBuffer.flip();
                            channel.write(intBuffer);

                            channel.position(collisionOffset + j * 24 + 16);
                            intBuffer.clear();
                            intBuffer.putInt(bsaCollisionEntry.i16);
                            intBuffer.flip();
                            channel.write(intBuffer);

                            channel.position(collisionOffset + j * 24 + 20);
                            intBuffer.clear();
                            intBuffer.putInt(bsaCollisionEntry.i20);
                            intBuffer.flip();
                            channel.write(intBuffer);

                            expirationOffset += 24;
                            typesOffset += 24;
                            entrySize += 24;
                        }

                        index++;
                    } 
                    
                    if (allEntries.get(i).getChildren().get(index).getValue().equals("Expiration (After Effects)")) {
                        expirationEntriesCount = allEntries.get(i).getChildren().get(index).getChildren().size();
                    }
                    else {
                        expirationEntriesCount = 0;
                        expirationOffset = 0;
                    }

                    channel.position(entryOffset + 6);
                    shortBuffer.clear();
                    shortBuffer.putShort((short) expirationEntriesCount);
                    shortBuffer.flip();
                    channel.write(shortBuffer);
                    
                    channel.position(entryOffset + 12);
                    intBuffer.clear();
                    intBuffer.putInt(expirationOffset);
                    intBuffer.flip();
                    channel.write(intBuffer);
                    expirationOffset += entryOffset;

                    if (expirationEntriesCount > 0) {
                        for (int j = 0;j < expirationEntriesCount; j++) {
                            BsaExpirationEntry bsaExpirationEntry = new BsaExpirationEntry();
                            bsaExpirationEntry = bsaExpirationHashMap.get(allEntries.get(i).getChildren().get(index).getChildren().get(j));
                        
                            channel.position(expirationOffset + j * 8);
                            shortBuffer.clear();
                            shortBuffer.putShort((short) bsaExpirationEntry.i00);
                            shortBuffer.flip();
                            channel.write(shortBuffer);

                            channel.position(expirationOffset + j * 8 + 2);
                            shortBuffer.clear();
                            shortBuffer.putShort((short) bsaExpirationEntry.i02);
                            shortBuffer.flip();
                            channel.write(shortBuffer);

                            channel.position(expirationOffset + j * 8 + 4);
                            shortBuffer.clear();
                            shortBuffer.putShort((short) bsaExpirationEntry.i04);
                            shortBuffer.flip();
                            channel.write(shortBuffer);

                            channel.position(expirationOffset + j * 8 + 6);
                            shortBuffer.clear();
                            shortBuffer.putShort((short) bsaExpirationEntry.i06);
                            shortBuffer.flip();
                            channel.write(shortBuffer);

                            typesOffset += 8;
                            entrySize += 8;
                        }

                        index++;
                    } 
                    
                    channel.position(entryOffset + 16);
                    byteBuffer.clear();
                    byteBuffer.put((byte) (bsaMainEntry.i16_a | bsaMainEntry.i16_b << 4));
                    byteBuffer.flip();
                    channel.write(byteBuffer);

                    channel.position(entryOffset + 17);
                    byteBuffer.clear();
                    byteBuffer.put((byte) bsaMainEntry.i17);
                    byteBuffer.flip();
                    channel.write(byteBuffer);

                    channel.position(entryOffset + 18);
                    intBuffer.clear();
                    intBuffer.putInt(bsaMainEntry.i18);
                    intBuffer.flip();
                    channel.write(intBuffer);

                    channel.position(entryOffset + 22);
                    shortBuffer.clear();
                    shortBuffer.putShort((short) bsaMainEntry.lifetime);
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    channel.position(entryOffset + 24);
                    shortBuffer.clear();
                    shortBuffer.putShort((short) bsaMainEntry.i24);
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    channel.position(entryOffset + 26);
                    shortBuffer.clear();
                    shortBuffer.putShort((short) bsaMainEntry.expires);
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    channel.position(entryOffset + 28);
                    shortBuffer.clear();
                    shortBuffer.putShort((short) bsaMainEntry.impactProjectile);
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    channel.position(entryOffset + 30);
                    shortBuffer.clear();
                    shortBuffer.putShort((short) bsaMainEntry.impactEnemy);
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    channel.position(entryOffset + 32);
                    shortBuffer.clear();
                    shortBuffer.putShort((short) bsaMainEntry.impactGround);
                    shortBuffer.flip();
                    channel.write(shortBuffer);
                    
                    for (int j = 0; j < allEntries.get(i).getChildren().size(); j++) {
                        if (allEntries.get(i).getChildren().get(j).getValue() != "Collision (After Effects)" && allEntries.get(i).getChildren().get(j).getValue() != "Expiration (After Effects)") {
                            typesCount++;
                        }
                    }

                    hdrOffset = typesCount * 16;
                    
                    channel.position(entryOffset + 34);
                    shortBuffer.clear();
                    shortBuffer.putShort(typesCount);
                    shortBuffer.flip();
                    channel.write(shortBuffer);

                    channel.position(entryOffset + 36);
                    intBuffer.clear();
                    intBuffer.putInt(typesOffset);
                    intBuffer.flip();
                    channel.write(intBuffer);
                    typesOffset += entryOffset;

                    channel.position(entryOffset + 40);
                    intBuffer.clear();
                    intBuffer.putInt(bsaMainEntry.i40);
                    intBuffer.flip();
                    channel.write(intBuffer);

                    channel.position(entryOffset + 44);
                    intBuffer.clear();
                    intBuffer.putInt(bsaMainEntry.i44);
                    intBuffer.flip();
                    channel.write(intBuffer);

                    channel.position(entryOffset + 48);
                    intBuffer.clear();
                    intBuffer.putInt(bsaMainEntry.i48);
                    intBuffer.flip();
                    channel.write(intBuffer);

                    entrySize += 52;

                    for (int j = 0; j < typesCount; j++) {
                        switch(allEntries.get(i).getChildren().get(index).getValue()) {
                            case "BSA Entry Passing" -> {
                                type = 0;
                                typeSize = 20;
                            } 
                            case "Movement" -> {
                                type = 1;
                                typeSize = 52;
                            }        
                            case "BSA Type 2" -> {
                                type = 2;
                                typeSize = 12;
                            }      
                            case "Hitbox" -> {
                                type = 3;
                                typeSize = 68;
                            }           
                            case "Deflection" -> {
                                type = 4;
                                typeSize = 60;
                            }        
                            case "Effect" -> {
                                type = 6;
                                typeSize = 28;
                            }           
                            case "Sound" -> {
                                type = 7;
                                typeSize = 12;
                            }            
                            case "Screen Effect" -> {
                                type = 8;
                                typeSize = 28;
                            }     
                            case "BSA Type 10" -> {
                                type = 10;
                                typeSize = 12;
                            }      
                            case "BSA Type 12" -> {
                                type = 12;
                                typeSize = 24;
                            }       
                            case "BSA Type 13" -> {
                                type = 13;
                                typeSize = 36;
                            }       
                            case "BSA Type 14" -> {
                                type = 14;
                                typeSize = 92;
                            }      
                        }

                        typeCount = (short) allEntries.get(i).getChildren().get(index).getChildren().size();
                        
                        channel.position(typesOffset + j * 16);
                        shortBuffer.clear();
                        shortBuffer.putShort(type);
                        shortBuffer.flip();
                        channel.write(shortBuffer);

                        channel.position(typesOffset + j * 16 + 6);
                        shortBuffer.clear();
                        shortBuffer.putShort(typeCount);
                        shortBuffer.flip();
                        channel.write(shortBuffer);

                        channel.position(typesOffset + j * 16 + 8);
                        intBuffer.clear();
                        intBuffer.putInt(hdrOffset);
                        intBuffer.flip();
                        channel.write(intBuffer);

                        dataOffset = hdrOffset + typeCount * 4;

                        for (int k = 0; k < typeCount; k++) {
                            switch (type) {
                                case 0 -> {
                                    BsaType0Entry bsaType0Entry = bsaType0HashMap.get(allEntries.get(i).getChildren().get(index).getChildren().get(k));

                                    channel.position(typesOffset + hdrOffset + j * 16 + k * 4);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) bsaType0Entry.startTime);
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset + hdrOffset + j * 16 + k * 4 + 2);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) (bsaType0Entry.duration + bsaType0Entry.startTime));
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset  +dataOffset + j * 16 + k * 16);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) bsaType0Entry.firstCondition);
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);
                                    
                                    channel.position(typesOffset + dataOffset + j * 16 + k * 16 + 2);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) bsaType0Entry.secondCondition);
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset+dataOffset + j * 16 + k * 16 + 4);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short)bsaType0Entry.bsaEntryId);
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 16 + 6);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short)bsaType0Entry.i06);
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 16 + 8);
                                    intBuffer.clear();
                                    intBuffer.putFloat(bsaType0Entry.bacCondition);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset+dataOffset + j * 16 + k * 16 + 12);
                                    intBuffer.clear();
                                    intBuffer.putFloat(bsaType0Entry.f12);
                                    intBuffer.flip();
                                    channel.write(intBuffer); 

                                    entrySize += 20;
                                }
                                case 1 -> {
                                    BsaType1Entry bsaType1Entry = bsaType1HashMap.get(allEntries.get(i).getChildren().get(index).getChildren().get(k));

                                    channel.position(typesOffset + hdrOffset + j * 16 + k * 4);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) bsaType1Entry.startTime);
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset+hdrOffset + j * 16 + k * 4 + 2);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) (bsaType1Entry.duration + bsaType1Entry.startTime));
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 48);
                                    intBuffer.clear();
                                    intBuffer.putInt((int) bsaType1Entry.motionFlags);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 48 + 4);
                                    intBuffer.clear();
                                    intBuffer.putFloat(bsaType1Entry.speedZ);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 48 + 8);
                                    intBuffer.clear();
                                    intBuffer.putFloat(bsaType1Entry.speedX);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 48 + 12);
                                    intBuffer.clear();
                                    intBuffer.putFloat(bsaType1Entry.speedY);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 48 + 16);
                                    intBuffer.clear();
                                    intBuffer.putFloat(bsaType1Entry.f16);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 48 + 20);
                                    intBuffer.clear();
                                    intBuffer.putFloat(bsaType1Entry.accelerationZ);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 48 + 24);
                                    intBuffer.clear();
                                    intBuffer.putFloat(bsaType1Entry.accelerationX);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 48 + 28);
                                    intBuffer.clear();
                                    intBuffer.putFloat(bsaType1Entry.accelerationY);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 48 + 32);
                                    intBuffer.clear();
                                    intBuffer.putFloat(bsaType1Entry.fallofStrength);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 48 + 36);
                                    intBuffer.clear();
                                    intBuffer.putFloat(bsaType1Entry.spreadDirectionX);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 48 + 40);
                                    intBuffer.clear();
                                    intBuffer.putFloat(bsaType1Entry.spreadDirectionY);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 48 + 44);
                                    intBuffer.clear();
                                    intBuffer.putFloat(bsaType1Entry.spreadDirectionZ);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    entrySize += 52;
                                }
                                case 2 -> {
                                    BsaType2Entry bsaType2Entry = bsaType2HashMap.get(allEntries.get(i).getChildren().get(index).getChildren().get(k));

                                    channel.position(typesOffset + hdrOffset + j * 16 + k * 4);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) bsaType2Entry.startTime);
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset + hdrOffset + j * 16 + k * 4 + 2);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) (bsaType2Entry.duration + bsaType2Entry.startTime));
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 8);
                                    shortBuffer.clear();
                                    shortBuffer.putShort(bsaType2Entry.i00);
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 8+ 2);
                                    shortBuffer.clear();
                                    shortBuffer.putShort(bsaType2Entry.i02);
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 8 + 4);
                                    shortBuffer.clear();
                                    shortBuffer.putShort(bsaType2Entry.i04);
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 8 + 6);
                                    shortBuffer.clear();
                                    shortBuffer.putShort(bsaType2Entry.i06);
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    entrySize += 12;
                                }
                                case 3 -> {
                                    BsaType3Entry bsaType3Entry = bsaType3HashMap.get(allEntries.get(i).getChildren().get(index).getChildren().get(k));

                                    channel.position(typesOffset + hdrOffset + j * 16 + k * 4);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) bsaType3Entry.startTime);
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset + hdrOffset + j * 16 + k * 4 + 2);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) (bsaType3Entry.duration + bsaType3Entry.startTime));
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 64);
                                    intBuffer.clear();
                                    intBuffer.putInt(bsaType3Entry.matrixFlag ? 1 : 0);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 64 + 4);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) bsaType3Entry.i04);
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 64 + 6);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) (bsaType3Entry.i06_a | bsaType3Entry.i06_b << 4 | bsaType3Entry.i06_c << 8 | bsaType3Entry.i06_d << 12));
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 64 + 8);
                                    intBuffer.clear();
                                    intBuffer.putFloat(bsaType3Entry.positionX);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 64 + 12);
                                    intBuffer.clear();
                                    intBuffer.putFloat(bsaType3Entry.positionY);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 64 + 16);
                                    intBuffer.clear();
                                    intBuffer.putFloat(bsaType3Entry.positionZ);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 64 + 20);
                                    intBuffer.clear();
                                    intBuffer.putFloat(bsaType3Entry.hitboxScale);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 64 + 24);
                                    intBuffer.clear();
                                    intBuffer.putFloat(bsaType3Entry.maximumX);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 64 + 28);
                                    intBuffer.clear();
                                    intBuffer.putFloat(bsaType3Entry.maximumY);
                                    intBuffer.flip();
                                    channel.write(intBuffer);
                                    
                                    channel.position(typesOffset + dataOffset + j * 16 + k * 64 + 32);
                                    intBuffer.clear();
                                    intBuffer.putFloat(bsaType3Entry.maximumZ);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 64 + 36);
                                    intBuffer.clear();
                                    intBuffer.putFloat(bsaType3Entry.minimumX);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset  + dataOffset + j * 16 + k * 64 + 40);
                                    intBuffer.clear();
                                    intBuffer.putFloat(bsaType3Entry.minimumY);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 64 + 44);
                                    intBuffer.clear();
                                    intBuffer.putFloat(bsaType3Entry.minimumZ);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 64 + 48);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) bsaType3Entry.hitAmount);
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 64 + 50);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) bsaType3Entry.hitboxLifetime);
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 64 + 52);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) bsaType3Entry.i52);
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16+ k * 64 + 54);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) bsaType3Entry.i54);
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 64 + 56);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) bsaType3Entry.i56);
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 64 + 58);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) bsaType3Entry.firstHit);
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 64 + 60);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) bsaType3Entry.multipleHits);
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 64 + 62);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) bsaType3Entry.lastHit);
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    entrySize += 68;
                                }
                                case 4 -> {
                                    BsaType4Entry bsaType4Entry = bsaType4HashMap.get(allEntries.get(i).getChildren().get(index).getChildren().get(k));
                                    
                                    channel.position(typesOffset + hdrOffset + j * 16 + k * 4);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) bsaType4Entry.startTime);
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset + hdrOffset + j * 16 + k * 4 + 2);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) (bsaType4Entry.duration + bsaType4Entry.startTime));
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 56);
                                    intBuffer.clear();
                                    intBuffer.putInt(bsaType4Entry.i00);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 56 + 4);
                                    intBuffer.clear();
                                    intBuffer.putInt(bsaType4Entry.i04);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 56 + 8);
                                    intBuffer.clear();
                                    intBuffer.putInt(bsaType4Entry.i08);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 56 + 12);
                                    intBuffer.clear();
                                    intBuffer.putFloat(bsaType4Entry.f12);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 56 + 16);
                                    intBuffer.clear();
                                    intBuffer.putFloat(bsaType4Entry.f16);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 56 + 20);
                                    intBuffer.clear();
                                    intBuffer.putFloat(bsaType4Entry.f20);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 56 + 24);
                                    intBuffer.clear();
                                    intBuffer.putInt(bsaType4Entry.i24);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 56 + 28);
                                    intBuffer.clear();
                                    intBuffer.putInt(bsaType4Entry.i28);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 56 + 32);
                                    intBuffer.clear();
                                    intBuffer.putInt(bsaType4Entry.i32);
                                    intBuffer.flip();
                                    channel.write(intBuffer);;

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 56 + 36);
                                    intBuffer.clear();
                                    intBuffer.putInt(bsaType4Entry.i36);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 56 + 40);
                                    intBuffer.clear();
                                    intBuffer.putInt(bsaType4Entry.i40);
                                    intBuffer.flip();
                                    channel.write(intBuffer);;

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 56 + 44);
                                    intBuffer.clear();
                                    intBuffer.putInt(bsaType4Entry.i44);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 56 + 48);
                                    intBuffer.clear();
                                    intBuffer.putInt(bsaType4Entry.i48);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 56 + 50);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) bsaType4Entry.i50);
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 56 + 52);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) bsaType4Entry.i52);
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 56 + 54);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) bsaType4Entry.i54);
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);
                                    
                                    entrySize += 60;
                                }
                                case 6 -> {
                                    BsaType6Entry bsaType6Entry = bsaType6HashMap.get(allEntries.get(i).getChildren().get(index).getChildren().get(k));

                                    channel.position(typesOffset + hdrOffset + j * 16 + k * 4);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) bsaType6Entry.startTime);
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset + hdrOffset + j * 16 + k * 4 + 2);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) (bsaType6Entry.duration + bsaType6Entry.startTime));
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 24);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) bsaType6Entry.eepkType);
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 24 + 2);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) bsaType6Entry.skillId);
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 24 + 4);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) bsaType6Entry.effectId);
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 24 + 6);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) bsaType6Entry.i06);
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 24 + 8);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) bsaType6Entry.effectSwitch);
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 24 + 10);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) bsaType6Entry.i10);
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 24 + 12);
                                    intBuffer.clear();
                                    intBuffer.putFloat(bsaType6Entry.positionX);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 24 + 16);
                                    intBuffer.clear();
                                    intBuffer.putFloat(bsaType6Entry.positionY);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 24 + 20);
                                    intBuffer.clear();
                                    intBuffer.putFloat(bsaType6Entry.positionZ);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    entrySize += 28;
                                }
                                case 7 -> {
                                    BsaType7Entry bsaType7Entry = bsaType7HashMap.get(allEntries.get(i).getChildren().get(index).getChildren().get(k));

                                    channel.position(typesOffset + hdrOffset + j * 16 + k * 4);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) bsaType7Entry.startTime);
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset + hdrOffset + j * 16+ k * 4 + 2);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) (bsaType7Entry.duration + bsaType7Entry.startTime));
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 8);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) bsaType7Entry.acbType);
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 8 + 2);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) bsaType7Entry.i02);
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 8 + 4);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) bsaType7Entry.cueId);
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 8 + 6);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) bsaType7Entry.i06);
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    entrySize += 12;
                                }
                                case 8 -> {
                                    BsaType8Entry bsaType8Entry = bsaType8HashMap.get(allEntries.get(i).getChildren().get(index).getChildren().get(k));

                                    channel.position(typesOffset + hdrOffset + j * 16 + k * 4);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) bsaType8Entry.startTime);
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset + hdrOffset + j * 16+ k * 4 + 2);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) (bsaType8Entry.duration + bsaType8Entry.startTime));
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 24);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) bsaType8Entry.bpeEffectId);
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 24 + 2);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) bsaType8Entry.i02);
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 24 + 4);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) bsaType8Entry.i04);
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 24 + 8);
                                    intBuffer.clear();
                                    intBuffer.putInt(bsaType8Entry.i08);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 24 + 12);
                                    intBuffer.clear();
                                    intBuffer.putInt(bsaType8Entry.i12);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 24 + 16);
                                    intBuffer.clear();
                                    intBuffer.putInt(bsaType8Entry.i16);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 24+ 20);
                                    intBuffer.clear();
                                    intBuffer.putInt(bsaType8Entry.i20);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    entrySize += 28;
                                }
                                case 10 -> {
                                    BsaType10Entry bsaType10Entry = bsaType10HashMap.get(allEntries.get(i).getChildren().get(index).getChildren().get(k));

                                    channel.position(typesOffset + hdrOffset + j * 16 + k * 4);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) bsaType10Entry.startTime);
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset + hdrOffset + j * 16+ k * 4 + 2);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) (bsaType10Entry.duration + bsaType10Entry.startTime));
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 8);
                                    intBuffer.clear();
                                    intBuffer.putInt(bsaType10Entry.skillId);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 8 + 4);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) bsaType10Entry.i04);
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16+ k * 8 + 6);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) bsaType10Entry.i06);
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    entrySize += 12;
                                }
                                case 12 -> {
                                    BsaType12Entry bsaType12Entry = bsaType12HashMap.get(allEntries.get(i).getChildren().get(index).getChildren().get(k));

                                    channel.position(typesOffset + hdrOffset + j * 16 + k * 4);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) bsaType12Entry.startTime);
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset + hdrOffset + j * 16+ k * 4 + 2);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) (bsaType12Entry.duration + bsaType12Entry.startTime));
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 20);
                                    intBuffer.clear();
                                    intBuffer.putFloat(bsaType12Entry.f00);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 20 + 4);
                                    intBuffer.clear();
                                    intBuffer.putInt(bsaType12Entry.eepkType);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 20 + 8);
                                    intBuffer.clear();
                                    intBuffer.putInt(bsaType12Entry.skillId);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 20 + 12);
                                    intBuffer.clear();
                                    intBuffer.putInt(bsaType12Entry.i12);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 20 +16);
                                    intBuffer.clear();
                                    intBuffer.putFloat(bsaType12Entry.f16);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    entrySize += 24;
                                }
                                case 13 -> {
                                    BsaType13Entry bsaType13Entry = bsaType13HashMap.get(allEntries.get(i).getChildren().get(index).getChildren().get(k));

                                    channel.position(typesOffset + hdrOffset + j * 16 + k * 4);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) bsaType13Entry.startTime);
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset + hdrOffset + j * 16+ k * 4 + 2);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) (bsaType13Entry.duration + bsaType13Entry.startTime));
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 32);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) bsaType13Entry.i00);
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 32 + 2);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) bsaType13Entry.i02);
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 32 + 4);
                                    intBuffer.clear();
                                    intBuffer.putFloat(bsaType13Entry.f04);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 32 + 8);
                                    intBuffer.clear();
                                    intBuffer.putFloat(bsaType13Entry.f08);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 32 + 12);
                                    intBuffer.clear();
                                    intBuffer.putInt(bsaType13Entry.i12);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 32 + 16);
                                    intBuffer.clear();
                                    intBuffer.putFloat(bsaType13Entry.f16);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 32 + 20);
                                    intBuffer.clear();
                                    intBuffer.putInt(bsaType13Entry.i20);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 32 + 24);
                                    intBuffer.clear();
                                    intBuffer.putInt(bsaType13Entry.i24);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 32 + 28);
                                    intBuffer.clear();
                                    intBuffer.putInt(bsaType13Entry.i28);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    entrySize += 36;
                                }
                                case 14 -> {
                                    BsaType14Entry bsaType14Entry = bsaType14HashMap.get(allEntries.get(i).getChildren().get(index).getChildren().get(k));

                                    channel.position(typesOffset + hdrOffset + j * 16 + k * 4);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) bsaType14Entry.startTime);
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset + hdrOffset + j * 16+ k * 4 + 2);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) (bsaType14Entry.duration + bsaType14Entry.startTime));
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 88);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) bsaType14Entry.i00);
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 88 + 2);
                                    shortBuffer.clear();
                                    shortBuffer.putShort((short) bsaType14Entry.i02);
                                    shortBuffer.flip();
                                    channel.write(shortBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 88 + 4);
                                    intBuffer.clear();
                                    intBuffer.putFloat(bsaType14Entry.f04);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 88 + 8);
                                    intBuffer.clear();
                                    intBuffer.putInt((int) bsaType14Entry.i08);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 88 + 12);
                                    intBuffer.clear();
                                    intBuffer.putFloat(bsaType14Entry.f12);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 88 + 16);
                                    intBuffer.clear();
                                    intBuffer.putInt((int) bsaType14Entry.i16);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 88 + 20);
                                    intBuffer.clear();
                                    intBuffer.putFloat(bsaType14Entry.f20);
                                    intBuffer.flip();
                                    channel.write(intBuffer);;

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 88 + 24);
                                    intBuffer.clear();
                                    intBuffer.putInt((int) bsaType14Entry.i24);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 88 + 28);
                                    intBuffer.clear();
                                    intBuffer.putFloat(bsaType14Entry.f28);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 88 + 32);
                                    intBuffer.clear();
                                    intBuffer.putInt((int) bsaType14Entry.i32);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 88 + 36);
                                    intBuffer.clear();
                                    intBuffer.putInt((int) bsaType14Entry.i36);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 88 + 40);
                                    intBuffer.clear();
                                    intBuffer.putInt((int) bsaType14Entry.i40);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 88 + 44);
                                    intBuffer.clear();
                                    intBuffer.putFloat(bsaType14Entry.f44);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 88 + 48);
                                    intBuffer.clear();
                                    intBuffer.putInt((int) bsaType14Entry.i48);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 88 + 52);
                                    intBuffer.clear();
                                    intBuffer.putFloat(bsaType14Entry.f52);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 88 + 56);
                                    intBuffer.clear();
                                    intBuffer.putInt((int) bsaType14Entry.i56);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 88 + 60);
                                    intBuffer.clear();
                                    intBuffer.putFloat(bsaType14Entry.f60);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 88 + 64);
                                    intBuffer.clear();
                                    intBuffer.putInt((int) bsaType14Entry.i64);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 88 + 68);
                                    intBuffer.clear();
                                    intBuffer.putFloat(bsaType14Entry.f68);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 88 + 72);
                                    intBuffer.clear();
                                    intBuffer.putInt((int) bsaType14Entry.i72);
                                    intBuffer.flip();
                                    channel.write(intBuffer);;

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 88 + 76);
                                    intBuffer.clear();
                                    intBuffer.putInt((int) bsaType14Entry.i76);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 88 + 80);
                                    intBuffer.clear();
                                    intBuffer.putInt((int) bsaType14Entry.i80);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    channel.position(typesOffset + dataOffset + j * 16 + k * 88 + 84);
                                    intBuffer.clear();
                                    intBuffer.putInt((int) bsaType14Entry.i84);
                                    intBuffer.flip();
                                    channel.write(intBuffer);

                                    entrySize += 92;
                                }
                            }
                        }

                        index++;
                        
                        hdrOffset += ((typeCount * typeSize) - 16);
                    
                        channel.position(typesOffset + j * 16 + 12);
                        intBuffer.clear();
                        intBuffer.putInt(dataOffset);
                        intBuffer.flip();
                        channel.write(intBuffer);

                        entrySize += 16;
                    }

                    entryOffset += entrySize;
                    entrySize = 0;
                    collisionOffset = 52;
                    expirationOffset = 52;
                    typesOffset = 52;

                }
                else {

                }
            }
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }
}

class BsaMainEntry {
    public int i00;
    public byte i16_a;
    public byte i16_b;
    public int i17;
    public int i18;
    public int lifetime;
    public int i24;
    public int expires;
    public int impactProjectile;
    public int impactEnemy;
    public int impactGround;
    public int i40;
    public int i44;
    public int i48;
    public BsaMainEntry() {}
    public BsaMainEntry(BsaMainEntry other) {
        this.i00 = other.i00;
        this.i16_a = other.i16_a;
        this.i16_b = other.i16_b;
        this.i17 = other.i17;
        this.i18 = other.i18;
        this.lifetime = other.lifetime;
        this.i24 = other.i24;
        this.expires = other.expires;
        this.impactProjectile = other.impactProjectile;
        this.impactEnemy = other.impactEnemy;
        this.impactGround = other.impactGround;
        this.i40 = other.i40;
        this.i44 = other.i44;
        this.i48 = other.i48;
    }
}
class BsaCollisionEntry {
    public int eepkType;
    public int skillId;
    public int effectId;
    public int i06;
    public int i08;
    public int i12;
    public int i16;
    public int i20;

    public BsaCollisionEntry() {}
    public BsaCollisionEntry(BsaCollisionEntry other) {
        this.eepkType = other.eepkType;
        this.skillId = other.skillId;
        this.effectId = other.effectId;
        this.i06 = other.i06;
        this.i08 = other.i08;
        this.i12 = other.i12;
        this.i16 = other.i16;
        this.i20 = other.i20;
    }
}

class BsaExpirationEntry {
    public int i00;
    public int i02;
    public int i04;
    public int i06;

    public BsaExpirationEntry() {}
    public BsaExpirationEntry(BsaExpirationEntry other) {
        this.i00 = other.i00;
        this.i02 = other.i02;
        this.i04 = other.i04;
        this.i06 = other.i06;
    }
}

class BsaType0Entry {
    public int startTime;
    public int duration;
    public int firstCondition;
    public int secondCondition;
    public int bsaEntryId;
    public int i06;
    public float bacCondition;
    public float f12;

    public BsaType0Entry() {}
    public BsaType0Entry(BsaType0Entry other) {
        this.startTime = other.startTime;
        this.duration = other.duration;
        this.firstCondition = other.firstCondition;
        this.secondCondition = other.secondCondition;
        this.bsaEntryId = other.bsaEntryId;
        this.i06 = other.i06;
        this.bacCondition = other.bacCondition;
        this.f12 = other.f12;
    }
}

class BsaType1Entry {
    public int startTime;
    public int duration;
    public long motionFlags;
    public float speedZ;
    public float speedX;
    public float speedY;
    public float f16;
    public float accelerationZ;
    public float accelerationX;
    public float accelerationY;
    public float fallofStrength;
    public float spreadDirectionX;
    public float spreadDirectionY;
    public float spreadDirectionZ;

    public BsaType1Entry() {}
    public BsaType1Entry(BsaType1Entry other) {
        this.startTime = other.startTime;
        this.duration = other.duration;
        this.motionFlags = other.motionFlags;
        this.speedZ = other.speedZ;
        this.speedX = other.speedX;
        this.speedY = other.speedY;
        this.f16 = other.f16;
        this.accelerationZ = other.accelerationZ;
        this.accelerationX = other.accelerationX;
        this.accelerationY = other.accelerationY;
        this.fallofStrength = other.fallofStrength;
        this.spreadDirectionX = other.spreadDirectionX;
        this.spreadDirectionY = other.spreadDirectionY;
        this.spreadDirectionZ = other.spreadDirectionZ;
    }
}

class BsaType2Entry {
    public int startTime;
    public int duration;
    public short i00;
    public short i02;
    public short i04;
    public short i06;
    
    public BsaType2Entry () {}
    public BsaType2Entry (BsaType2Entry other) {
        this.startTime = other.startTime;
        this.duration = other.duration;
        this.i00 = other.i00;
        this.i02 = other.i02;
        this.i04 = other.i04;
        this.i06 = other.i06;
    }
}

class BsaType3Entry {
    public int startTime;
    public int duration;
    public boolean matrixFlag = false;
    public int i04;
    public byte i06_a;
    public byte i06_b;
    public byte i06_c;
    public byte i06_d;
    public float positionX;
    public float positionY;
    public float positionZ;
    public float hitboxScale;
    public float maximumX;
    public float maximumY;
    public float maximumZ;
    public float minimumX;
    public float minimumY;
    public float minimumZ;
    public int hitAmount;
    public int hitboxLifetime;
    public int i52;
    public int i54;
    public int i56;
    public int firstHit;
    public int multipleHits;
    public int lastHit;

    public BsaType3Entry() {}
    public BsaType3Entry(BsaType3Entry other) {
        this.startTime = other.startTime;
        this.duration = other.duration;
        this.matrixFlag = other.matrixFlag;
        this.i04 = other.i04;
        this.i06_a = other.i06_a;
        this.i06_b = other.i06_b;
        this.i06_c = other.i06_c;
        this.i06_d = other.i06_d;
        this.positionX = other.positionX;
        this.positionY = other.positionY;
        this.positionZ = other.positionZ;
        this.hitboxScale = other.hitboxScale;
        this.maximumX = other.maximumX;
        this.maximumY = other.maximumY;
        this.maximumZ = other.maximumZ;
        this.minimumX = other.minimumX;
        this.minimumY = other.minimumY;
        this.minimumZ = other.minimumZ;
        this.hitAmount = other.hitAmount;
        this.hitboxLifetime = other.hitboxLifetime;
        this.i52 = other.i52;
        this.i54 = other.i54;
        this.i56 = other.i56;
        this.firstHit = other.firstHit;
        this.multipleHits = other.multipleHits;
        this.lastHit = other.lastHit;
    }
}

class BsaType4Entry {
    int startTime;
    int duration;
    int i00;
    int i04;
    int i08;
    float f12;
    float f16;
    float f20;
    int i24;
    int i28;
    int i32;
    int i36;
    int i40;
    int i44;
    int i48;
    int i50;
    int i52;
    int i54;

    public BsaType4Entry() {}
    public BsaType4Entry(BsaType4Entry other) {
        this.startTime = other.startTime;
        this.duration = other.duration;
        this.i00 = other.i00;
        this.i04 = other.i04;
        this.i08 = other.i08;
        this.f12 = other.f12;
        this.f16 = other.f16;
        this.f20 = other.f20;
        this.i24 = other.i24;
        this.i28 = other.i28;
        this.i32 = other.i32;
        this.i36 = other.i36;
        this.i40 = other.i40;
        this.i44 = other.i44;
        this.i48 = other.i48;
        this.i50 = other.i50;
        this.i52 = other.i54;
    }
}

class BsaType6Entry {
    int startTime;
    int duration;
    int eepkType;
    int skillId;
    int effectId;
    int i06;
    int effectSwitch;
    int i10;
    float positionX;
    float positionY;
    float positionZ;
    
    BsaType6Entry() {}
    BsaType6Entry(BsaType6Entry other) {
        this.startTime = other.startTime;
        this.duration = other.duration;
        this.eepkType = other.eepkType;
        this.skillId = other.skillId;
        this.effectId = other.effectId;
        this.i06 = other.i06;
        this.effectSwitch = other.effectSwitch;
        this.i10 = other.i10;
        this.positionX = other.positionX;
        this.positionY = other.positionY;
        this.positionZ = other.positionY;
    }
}

class BsaType7Entry {
    int startTime;
    int duration;
    int acbType;
    int i02;
    int cueId;
    int i06;

    BsaType7Entry() {}
    BsaType7Entry(BsaType7Entry other) {
        this.startTime = other.startTime;
        this.duration = other.duration;
        this.acbType = other.acbType;
        this.i02 = other.i02;
        this.cueId = other.cueId;
        this.i06 = other.i06;
    }
}

class BsaType8Entry {
    int startTime;
    int duration;
    int bpeEffectId;
    int i02;
    int i04;
    int i08;
    int i12;
    int i16;
    int i20;

    BsaType8Entry() {}
    BsaType8Entry(BsaType8Entry other) {
        this.startTime = other.startTime;
        this.duration = other.duration;
        this.bpeEffectId = other.bpeEffectId;
        this.i02 = other.i02;
        this.i04 = other.i04;
        this.i08 = other.i08;
        this.i12 = other.i12;
        this.i16 = other.i16;
        this.i20 = other.i20;
    }
}

class BsaType10Entry {
    int startTime;
    int duration;
    int skillId;
    int i04;
    int i06;

    BsaType10Entry() {}
    BsaType10Entry(BsaType10Entry other) {
        this.startTime = other.startTime;
        this.duration = other.duration;
        this.skillId = other.skillId;
        this.i04 = other.i04;
        this.i06 = other.i06;
    }
}

class BsaType12Entry {
    int startTime;
    int duration;
    float f00;
    int eepkType;
    int skillId;
    int i12;
    float f16;

    BsaType12Entry() {}
    BsaType12Entry(BsaType12Entry other) {
        this.startTime = other.startTime;
        this.duration = other.duration;
        this.f00 = other.f00;
        this.eepkType = other.eepkType;
        this.skillId = other.skillId;
        this.i12 = other.i12;
        this.f16 = other.f16;
    }
}

class BsaType13Entry {
    int startTime;
    int duration;
    int i00;
    int i02;
    float f04;
    float f08;
    int i12;
    float f16;
    int i20;
    int i24;
    int i28;

    BsaType13Entry() {}
    BsaType13Entry(BsaType13Entry other) {
        this.startTime = other.startTime;
        this.duration = other.duration;
        this.i00 = other.i00;
        this.i02 = other.i02;
        this.f04 = other.f04;
        this.f08 = other.f08;
        this.i12 = other.i12;
        this.f16 = other.f16;
        this.i20 = other.i20;
        this.i24 = other.i24;
        this.i28 = other.i28;
    }
}

class BsaType14Entry {
    int startTime;
    int duration;
    int i00;
    int i02;
    float f04;
    long i08;
    float f12;
    long i16;
    float f20;
    long i24;
    float f28;
    long i32;
    long i36;
    long i40;
    float f44;
    long i48;
    float f52;
    long i56;
    float f60;
    long i64;
    float f68;
    long i72;
    long i76;
    long i80;
    long i84;

    BsaType14Entry() {}

    BsaType14Entry(BsaType14Entry other) {
        this.startTime = other.startTime;
        this.duration = other.duration;
        this.i00 = other.i00;
        this.i02 = other.i02;
        this.f04 = other.f04;
        this.i08 = other.i08;
        this.f12 = other.f12;
        this.i16 = other.i16;
        this.f20 = other.f20;
        this.i24 = other.i24;
        this.f28 = other.f28;
        this.i32 = other.i32;
        this.i36 = other.i36;
        this.i40 = other.i40;
        this.f44 = other.f44;
        this.i48 = other.i48;
        this.f52 = other.f52;
        this.i56 = other.i56;
        this.f60 = other.f60;
        this.i64 = other.i64;
        this.f68 = other.f68;
        this.i72 = other.i72;
        this.i76 = other.i76;
        this.i80 = other.i80;
        this.i84 = other.i84;
    }
}