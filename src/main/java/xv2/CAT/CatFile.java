package xv2.cat;
import java.io.File;
import java.io.IOException;
import javafx.application.Platform;
import javafx.scene.control.Tab;
import mainuisetup.Threading;
import xv2.Xv2File;

public class CatFile extends Xv2File {
    CatUi catUi;

    public CatFile(File file) {
        super(file);
    }

    @Override
    public void loadFile(File file, Tab tab) {
       CatParser parser = new CatParser();

        Threading.executorService.submit(() -> {
            try {
                this.catUi = parser.parseCat(file.toPath());
                Platform.runLater(() -> {
                    tab.setContent(catUi.createHBox());
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void saveFile(File selectedDirectory, Tab tab) {
        CatWriter writer = new CatWriter(this.catUi);

        Threading.executorService.submit(() -> {
            try {
                writer.writeCat(selectedDirectory.toPath().resolve(tab.getText()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }  
}