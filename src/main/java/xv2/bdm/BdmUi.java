package xv2.bdm;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import uicomponents.boxes.HBoxFactory;
import uicomponents.menus.ContextMenuFactory;
import xv2.bdm.ui.Animation;
import xv2.bdm.ui.Camera;
import xv2.bdm.ui.Effects;
import xv2.bdm.ui.Main;
import xv2.bdm.ui.Miscellaneous;
import xv2.bdm.ui.Pushback;
import xv2.bdm.ui.Sound;
import xv2.bdm.ui.Unknown;

public class BdmUi implements ContextMenuFactory, HBoxFactory  {
    public ListView<Bdm> listView = createListView();
    private Bdm currentEntry;
    private Bdm container;
    private int tabIndex = 0;
    private int currentIndex = 0;
    private Unknown unknown = new Unknown();
    private Animation animation = new Animation();
    private Camera camera = new Camera();
    private Effects effects = new Effects();
    private Sound sound = new Sound();
    private Main main = new Main();
    private Miscellaneous miscellaneous = new Miscellaneous();
    private Pushback pushback = new Pushback();
    private TabPane subTabPane = createSubTabPane();
    private TabPane tabPane = createTabPane();
    
    public HBox createHBox() {
        HBox hBox = new HBox(listView, tabPane);
        HBox.setHgrow(tabPane, Priority.ALWAYS);
        return hBox;
    }

    private ListView<Bdm> createListView() {
        ListView<Bdm> listView = new ListView<>();
        keysListener(listView);

        listView.setContextMenu(getDefaultContextMenu());
        listView.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue == null) return;

            currentEntry = newValue;
            currentIndex = listView.getItems().indexOf(currentEntry);
            updateItems();
        });

        return listView;
    }

    public TabPane createTabPane() {
        Tab defaultTab = new Tab("0: Default");
        Tab counterHitFrontTab = new Tab("1: Counter Hit (Front)");
        Tab primaryKnockbackTab = new Tab("2: Primary Knockback");
        Tab backTab = new Tab("3: Back");
        Tab groundImpactTab = new Tab("4: Ground Impact");
        Tab guardingTab = new Tab("5: Guarding");
        Tab stumbleTab = new Tab("6: Stumble");
        Tab counterHitBackTab = new Tab("7: Counter Hit (Back)");
        Tab floatingKnockbackTab = new Tab("8: Floating Knockback");
        Tab lyingOnGroundTab = new Tab("9: Lying On Ground");

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

        TabPane tabPane = new TabPane(defaultTab, counterHitFrontTab, 
            primaryKnockbackTab, backTab, 
            groundImpactTab, guardingTab,
            stumbleTab, counterHitBackTab,
            floatingKnockbackTab, lyingOnGroundTab
        );

        tabPane.getSelectionModel().selectedIndexProperty().addListener((obsevable, oldValue, newValue) -> {
            if (listView.getSelectionModel().getSelectedIndex() < 0 || tabPane.getSelectionModel().getSelectedIndex() < 0) return;

            tabIndex = newValue.intValue();
            tabPane.getTabs().get(oldValue.intValue()).setContent(null);
            tabPane.getTabs().get(tabIndex).setContent(subTabPane);
            updateItems();
        });

        tabPane.getTabs().get(0).setContent(subTabPane);
        return tabPane;
    }

    private TabPane createSubTabPane() {
        Tab mainTab = new Tab("Main", main.createVBox());
        Tab animationTab = new Tab("Animation", animation.createVBox());
        Tab soundTab = new Tab("Sound", sound.createVBox());
        Tab effectsTab = new Tab("Effects", effects.createScrollPane());
        Tab pushbackStunKnockbacTab = new Tab("Pushback/Stun/Knockback", pushback.createVBox());
        Tab cameraTab = new Tab("Camera", camera.createVBox());
        Tab miscTab = new Tab("Misc", miscellaneous.createVBox());
        Tab unknownTab = new Tab("Unknown", unknown.createVBox());

        mainTab.setClosable(false);
        animationTab.setClosable(false);
        soundTab.setClosable(false);
        effectsTab.setClosable(false);
        pushbackStunKnockbacTab.setClosable(false);
        cameraTab.setClosable(false);
        miscTab.setClosable(false);
        unknownTab.setClosable(false);

        return new TabPane(mainTab, animationTab,
            soundTab, effectsTab,
            pushbackStunKnockbacTab, cameraTab,
            miscTab, unknownTab
        );
    }

    @Override
    public void copy() {
        listView.getContextMenu().getItems().get(1).setDisable(false);
        container = new Bdm(currentEntry);
    }

    @Override
    public void paste() {
        container.setEntry(currentEntry.getEntry());
        listView.getItems().set(listView.getItems().indexOf(currentEntry), new Bdm(container));
        listView.refresh();
    }

    @Override
    public void delete() {
        if (listView.getItems().size() == 1) return;

        listView.getItems().remove(currentEntry);
        int startIndex = currentIndex == 0 ? currentIndex : currentIndex + 1;
        for (int i = startIndex; i < listView.getItems().size(); i++) {
            listView.getItems().get(i).setEntry(listView.getItems().get(i).getEntry() - 1);
        }

        listView.refresh();
    }

    @Override
    public void append() {
        listView.getItems().add(currentIndex + 1, new Bdm(currentEntry.getEntry() + 1));
        for (int i = currentIndex + 2; i < listView.getItems().size(); i++) {
            listView.getItems().get(i).setEntry(listView.getItems().get(i).getEntry() + 1);
        }

        listView.refresh();
    }

    @Override
    public void insert() {
        listView.getItems().add(currentIndex, new Bdm(currentEntry.getEntry()));
        IO.println(currentIndex);
        for (int i = currentIndex + 1; i < listView.getItems().size(); i++) {
            listView.getItems().get(i).setEntry(listView.getItems().get(i).getEntry() + 1);
        }

        listView.refresh();
    }

    private void updateItems() {
        animation.setBdmEntry(currentEntry.subEntries[tabIndex]);
        main.setBdmEntry(currentEntry.subEntries[tabIndex]);
        camera.setBdmEntry(currentEntry.subEntries[tabIndex]);
        sound.setBdmEntry(currentEntry.subEntries[tabIndex]);
        effects.setBdmEntry(currentEntry.subEntries[tabIndex]);
        unknown.setBdmEntry(currentEntry.subEntries[tabIndex]);
        miscellaneous.setBdmEntry(currentEntry.subEntries[tabIndex]);
        pushback.setBdmEntry(currentEntry.subEntries[tabIndex]);
    }
}