package uicomponents.labels;
import javafx.scene.control.Label;

public interface LabelFactory {
    default Label createLabel(String text, int width) {
        Label label = new Label(text);
        if (width != 0) label.setPrefWidth(width);

        return label;
    }
}