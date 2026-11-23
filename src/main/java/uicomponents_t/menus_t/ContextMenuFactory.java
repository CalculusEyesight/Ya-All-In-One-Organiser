package uicomponents.menus;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;

public interface ContextMenuFactory {
    default ContextMenu getDefaultContextMenu() {
        MenuItem[] menuItems = new MenuItem[] {
            new MenuItem("Copy Ctrl+C"),
            new MenuItem("Paste Ctrl+V"),
            new MenuItem("Delete Del"),
            new MenuItem("Append Ctrl+A"),
            new MenuItem("Insert Ctrl+I")
        };

        ContextMenu contextMenu = new ContextMenu(menuItems);
        contextMenu.getItems().get(1).setDisable(true);
        contextMenu.getItems().get(0).setOnAction(e -> copy());
        contextMenu.getItems().get(1).setOnAction(e -> paste());
        contextMenu.getItems().get(2).setOnAction(e -> delete());
        contextMenu.getItems().get(3).setOnAction(e -> append());
        contextMenu.getItems().get(4).setOnAction(e -> insert());
 
        return contextMenu;
    }

    default void keysListener(Node node) {
        node.setOnKeyPressed(e -> {
            if (e.isControlDown() && e.getCode() == KeyCode.C) copy();
            else if (e.isControlDown() && e.getCode() == KeyCode.V) paste();
            else if (e.getCode() == KeyCode.DELETE) delete();
            else if (e.isControlDown() && e.getCode() == KeyCode.A) append();
            else if (e.isControlDown() && e.getCode() == KeyCode.I) insert();
        });
    }

    void copy();
    void paste();
    void delete();
    void append();
    void insert();
}