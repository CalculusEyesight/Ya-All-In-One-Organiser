package xv2.agd;
import java.io.File;
import java.io.IOException;
import mainuisetup.Threading;
import javafx.application.Platform;
import javafx.scene.control.Tab;
import xv2.Xv2File;

public class AgdFile extends Xv2File {
    private AgdUi agdUi;
    public AgdFile(File file) {
        super(file);
    }

    @Override 
    public void loadFile(File file, Tab tab) {
        AgdParser parser = new AgdParser();

        Threading.executorService.submit(() -> {
            try {
                this.agdUi = parser.parseAgd(file.toPath());
                Platform.runLater(() -> {
                    tab.setContent(agdUi.createVBox());
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void saveFile(File selectedDirectory, Tab tab) {
        AgdWriter writer = new AgdWriter(this.agdUi.getAgd());

        Threading.executorService.submit(() -> {
            try {
                writer.writeAgd(selectedDirectory.toPath().resolve(tab.getText()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}