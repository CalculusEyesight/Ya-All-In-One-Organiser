package mainuisetup;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCombination;

public class Menus {
    protected static final MenuBar menuBar = new MenuBar();
    protected static final Menu fileMenu = new Menu("File");
    protected static final Menu optionsMenu = new Menu("Options");
    protected static final Menu helpMenu = new Menu("Help");
    protected static final Menu performanceMenu = new Menu("Performance");
    protected static final ToggleGroup performanceGroup = new ToggleGroup();
    protected static final MenuItem loadFolder = new MenuItem("Load Folder   Ctrl+O");
    protected static final MenuItem saveFolder = new MenuItem("Save Folder   Ctrl+S");
    protected static final RadioMenuItem efficiencyMode = new RadioMenuItem("Efficiency Mode");
    protected static final RadioMenuItem defaultMode = new RadioMenuItem("Default");
    protected static final RadioMenuItem performanceMode = new RadioMenuItem("High Performance Mode");
    protected static final MenuItem idList = new MenuItem("ID List");
    protected static final MenuItem commonBdmIdList = new MenuItem("Common BDM ID List");
    protected static final MenuItem skillMovesetManual = new MenuItem("Skill/Moveset Manual");
    public static final CheckMenuItem autoGroup = new CheckMenuItem("Auto Group EMBs");
    protected static final CheckMenuItem toggleDarkMode = new CheckMenuItem("Dark Mode");

    protected static void setupMenus() {
        loadFolder.setAccelerator(KeyCombination.valueOf("Shortcut+O"));
        saveFolder.setAccelerator(KeyCombination.valueOf("Shortcut+S"));

        efficiencyMode.setToggleGroup(performanceGroup);
        defaultMode.setToggleGroup(performanceGroup);
        performanceMode.setToggleGroup(performanceGroup);
        defaultMode.setSelected(true);

        fileMenu.getItems().addAll(loadFolder, saveFolder);
        performanceMenu.getItems().addAll(efficiencyMode, defaultMode, performanceMode);
        optionsMenu.getItems().addAll(performanceMenu, autoGroup, toggleDarkMode);
        helpMenu.getItems().addAll(idList, commonBdmIdList, skillMovesetManual);
        
        menuBar.getMenus().addAll(fileMenu, optionsMenu, helpMenu);
    }
}