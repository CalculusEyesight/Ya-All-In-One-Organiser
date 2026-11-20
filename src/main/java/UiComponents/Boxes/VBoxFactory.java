package UiComponents.Boxes;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

public interface VBoxFactory {
   VBox createVBox();

   default VBox createVBox(int spacing, Insets insets, Node node) {
      return new VBox(node);
   } 

   default VBox createVBox(int spacing, Insets insets) {
      VBox vBox = new VBox(spacing);
      vBox.setPadding(insets);

      return vBox;
   }

   default VBox createVBox(int spacing, Insets insets, Node[] nodes) {
      VBox vBox = new VBox(spacing, nodes);
      vBox.setPadding(insets);

      return vBox;
   }
}