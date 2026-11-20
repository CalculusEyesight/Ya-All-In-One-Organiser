package UiComponents.Panes;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public interface StackPaneFactory {
    default StackPane createStackPane(String text, Node node) {
        Label label = new Label(text);
        label.getStyleClass().add("titled-address-label");
        label.setTranslateY(-8); 
        label.setTranslateX(10);

        StackPane stackPane = new StackPane(node, label);
        StackPane.setAlignment(label, Pos.TOP_LEFT);

        return stackPane;
    }
}