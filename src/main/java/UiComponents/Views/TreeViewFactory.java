package uicomponents.views;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

public interface TreeViewFactory {
    default <T> TreeItem<T> getRootChild(TreeView<T> treeView, TreeItem<T> item) {
        TreeItem<T> thisEntry = item;
        while (thisEntry.getParent() != treeView.getRoot()) {
            thisEntry = thisEntry.getParent();
        }
        
        return thisEntry;
    }
}