package uicomponents.boxes;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public interface HBoxFactory {
    default HBox createHBox(int spacing, Pos pos) {
        HBox hBox = new HBox(spacing);
        hBox.setAlignment(pos);

        return hBox;
    }

    default HBox createHBox(int spacing, Node[] nodes) {
        return new HBox(spacing, nodes);
    }

    default HBox createHBox(int spacing, Pos pos, Label label, Node node) {
        HBox hBox = new HBox(spacing, label, node);
        hBox.setAlignment(pos);

        return hBox;
    }

    default HBox createHBox(int spacing, Pos pos, Node[] nodeList, boolean enableStyle) {
        HBox hBox = new HBox(spacing);

        if (enableStyle) hBox.getStyleClass().add("titled-address-box");

        for (int i = 0; i < nodeList.length; i++) {
            hBox.getChildren().add(nodeList[i]);
        }
        
        hBox.setAlignment(pos);

        return hBox;
    }
}