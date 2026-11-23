package xv2.bdm;
import java.io.File;
import java.io.IOException;
import javafx.application.Platform;
import javafx.scene.control.Tab;
import mainuisetup.Threading;
import xv2.Xv2File;

public class BdmFile extends Xv2File {
    BdmUi bdmUi;
    public BdmFile(File file) {
        super(file);
    }

    @Override
    public void loadFile(File file, Tab tab) {
        BdmParser parser = new BdmParser();

        Threading.executorService.submit(() -> {
            try {
                this.bdmUi = parser.parseBdm(file.toPath());
                Platform.runLater(() -> {
                    tab.setContent(bdmUi.createHBox());
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void saveFile(File selectedDirectory, Tab tab) {
        BdmWriter writer = new BdmWriter(this.bdmUi);

        Threading.executorService.submit(() -> {
            try {
                writer.writeBdm(selectedDirectory.toPath().resolve(tab.getText()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}