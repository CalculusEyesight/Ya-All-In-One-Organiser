package UiComponents.Buttons;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

public interface RadioButtonFactory {
    default RadioButton createRadioButton(String text, ToggleGroup toggleGroup) {
        RadioButton radioButton = new RadioButton(text);
        radioButton.setToggleGroup(toggleGroup);
        
        return radioButton;
    }
}