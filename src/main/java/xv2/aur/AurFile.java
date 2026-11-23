package xv2.aur;
import java.io.File;
import java.io.IOException;
import javafx.application.Platform;
import javafx.scene.control.Tab;
import mainuisetup.Threading;
import xv2.Xv2File;

public class AurFile extends Xv2File {
    AurAuraUi aurAuraUi;
    public AurFile(File file) {
        super(file);
    }

    @Override
    public void loadFile(File file, Tab tab) {
        AurParser parser = new AurParser();

        Threading.executorService.submit(() -> {
            try {
                this.aurAuraUi = parser.parseAur(file.toPath());
                Platform.runLater(() -> {
                    tab.setContent(aurAuraUi.createSplitPane());
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void saveFile(File selectedDirectory, Tab tab) {
        AurWriter writer = new AurWriter(aurAuraUi);

        Threading.executorService.submit(() -> {
            try {
                writer.writeAur(selectedDirectory.toPath().resolve(tab.getText()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}