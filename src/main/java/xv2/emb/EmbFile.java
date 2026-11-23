package xv2.emb;
import java.io.File;
import java.io.IOException;
import javafx.application.Platform;
import javafx.scene.control.Tab;
import mainuisetup.Menus;
import mainuisetup.Threading;
import xv2.Xv2File;

public class EmbFile extends Xv2File {
    EmbUi embUi;

    public EmbFile(File file) {
        super(file);
    }

    @Override
    public void loadFile(File file, Tab tab) {
        Menus.autoGroup.setDisable(false);
        EmbParser parser = new EmbParser();

        Threading.executorService.submit(() -> {
            try {
                this.embUi = parser.parseEmb(file.toPath());
                Platform.runLater(() -> {
                    tab.setContent(embUi.createVBox());
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void saveFile(File selectedDirectory, Tab tab) {
        EmbWriter writer = new EmbWriter(this.embUi);

        Threading.executorService.submit(() -> {
            try {
                writer.writeEmb(selectedDirectory.toPath().resolve(tab.getText()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    } 
}