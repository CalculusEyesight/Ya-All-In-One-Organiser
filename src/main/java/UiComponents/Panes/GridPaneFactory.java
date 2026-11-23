package uicomponents.panes;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;

public interface GridPaneFactory {
    default GridPane createGridPane(int columns , int rows, Node[] nodeList, boolean enableStyle) {
        GridPane gridPane = new GridPane(10, 10);
        if (enableStyle) gridPane.getStyleClass().add("titled-address-box");

        int index = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                gridPane.add(nodeList[index], j, i);
                index++;
            }
        }

        return gridPane;
    }
}