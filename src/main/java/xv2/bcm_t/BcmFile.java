package xv2.bcm;
import java.io.File;
import java.io.IOException;
import javafx.application.Platform;
import javafx.scene.control.Tab;
import mainuisetup.Threading;
import xv2.Xv2File;

public class BcmFile extends Xv2File  {
    BcmUi bcmUi;
    public BcmFile(File file) {
        super(file);
    }

    @Override
    public void loadFile(File file, Tab tab) {
        BcmParser parser = new BcmParser();

        Threading.executorService.submit(() -> {
            try {
                this.bcmUi = parser.parseBcm(file.toPath());
                Platform.runLater(() -> {
                    tab.setContent(bcmUi.createSplitPane());
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void saveFile(File selectedDirectory, Tab tab) {
        BcmWriter writer = new BcmWriter(this.bcmUi);

        Threading.executorService.submit(() -> {
            try {
                writer.writeBcm(selectedDirectory.toPath().resolve(tab.getText()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}