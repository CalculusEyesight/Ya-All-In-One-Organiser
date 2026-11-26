package xv2.bcm;
import java.util.concurrent.atomic.AtomicInteger;

import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.VBox;
import uicomponents.boxes.HBoxFactory;
import uicomponents.boxes.VBoxFactory;
import uicomponents.labels.LabelFactory;
import uicomponents.menus.ContextMenuFactory;
import uicomponents.panes.SplitPaneFactory;
import uicomponents.views.TreeViewFactory;
import xv2.bcm.ui.Activator;
import xv2.bcm.ui.BAC;
import xv2.bcm.ui.Inputs;
import xv2.bcm.ui.Miscellaneous;
import xv2.bcm.ui.Unknown;

public class BcmUi implements SplitPaneFactory, ContextMenuFactory, VBoxFactory, HBoxFactory, LabelFactory, TreeViewFactory {
    private TreeItem<Bcm> currentEntry;
    private Bcm container;
    private Inputs inputs = new Inputs();
    private Activator activator = new Activator();
    private BAC bac = new BAC();
    private Miscellaneous miscellaneous = new Miscellaneous();
    private Unknown unknown = new Unknown();
    
    protected TreeView<Bcm> treeView = createTreeView();
    private TabPane tabPane = createTabPane();

    private TreeView<Bcm> createTreeView() {
        TreeView<Bcm> treeView = new TreeView<>();
        keysListener(treeView);

        treeView.setContextMenu(getDefaultContextMenu());
        treeView.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue == null) return;

            currentEntry = newValue;
            inputs.setBcmEntry(currentEntry.getValue());
            activator.setBcmEntry(currentEntry.getValue());
            bac.setBcmEntry(currentEntry.getValue());
            miscellaneous.setBcmEntry(currentEntry.getValue());
            unknown.setBcmEntry(currentEntry.getValue());
        });

        return treeView;
    }

    private TabPane createTabPane() {
        Tab inputsTab = new Tab("Inputs", inputs.createVBox());
        Tab activatorTab = new Tab("Activator", activator.createScrollPane());
        Tab bacTab = new Tab("BAC", bac.createVBox());
        Tab miscTab = new Tab("Misc", miscellaneous.createVBox());
        Tab unknownTab = new Tab("Unknown", unknown.createVBox());

        inputsTab.setClosable(false);
        activatorTab.setClosable(false);
        bacTab.setClosable(false);
        miscTab.setClosable(false);
        unknownTab.setClosable(false);

        return new TabPane(inputsTab, activatorTab, bacTab, miscTab, unknownTab);
    }

    @Override
    public SplitPane createSplitPane() {
        SplitPane splitPane = new SplitPane(treeView, tabPane);
        splitPane.setDividerPositions(0.245);
        splitPane.getStyleClass().add("vbox"); 

        return splitPane;
    }

    @Override
    public void copy() {
        treeView.getContextMenu().getItems().get(1).setDisable(false);
        container = new Bcm(currentEntry.getValue());
    }

    @Override
    public void paste() {
        container.setEntry(currentEntry.getValue().getEntry());
        currentEntry.setValue(new Bcm(container));

        inputs.setBcmEntry(currentEntry.getValue());
        activator.setBcmEntry(currentEntry.getValue());
        bac.setBcmEntry(currentEntry.getValue());
        miscellaneous.setBcmEntry(currentEntry.getValue());
        unknown.setBcmEntry(currentEntry.getValue());
        treeView.refresh();
    }

    @Override
    public void delete() {
       if (currentEntry.getParent() == null) return;
        currentEntry.getParent().getChildren().remove(currentEntry);
    
        AtomicInteger index = new AtomicInteger(0);
        renameTreeItems(treeView.getRoot(), index);
    }

    @Override
    public void append() {
        TreeItem<Bcm> parent = currentEntry.getParent();
        if (parent != null) {
            TreeItem<Bcm> newEntry = new TreeItem<>(new Bcm());
            int currentPos = parent.getChildren().indexOf(currentEntry);
            parent.getChildren().add(currentPos + 1, newEntry);

            AtomicInteger index = new AtomicInteger(0);
            renameTreeItems(treeView.getRoot(), index);
        }
    }

    @Override
    public void insert() {
        TreeItem<Bcm> parent = currentEntry.getParent();
        if (parent != null) {
            TreeItem<Bcm> newEntry = new TreeItem<>(new Bcm());
            int currentPos = parent.getChildren().indexOf(currentEntry);
            parent.getChildren().add(currentPos, newEntry);

            AtomicInteger index = new AtomicInteger(0);
            renameTreeItems(treeView.getRoot(), index);
        }
    }

    @Override
    public VBox createVBox() {
        return new VBox();
    }

    public void renameTreeItems(TreeItem<Bcm> item, AtomicInteger index) {
        if (item == null) return;

        item.getValue().setEntry(index.get());
        index.set(index.get() + 1);
        
        for (TreeItem<Bcm> child : item.getChildren()) {
            renameTreeItems(child, index);
        }
    }
}