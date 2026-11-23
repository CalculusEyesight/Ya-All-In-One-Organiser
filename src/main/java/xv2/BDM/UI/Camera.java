package xv2.bdm.ui;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import uicomponents.boxes.HBoxFactory;
import uicomponents.boxes.VBoxFactory;
import uicomponents.labels.LabelFactory;
import xv2.bdm.Bdm.BdmSubEntry;

public class Camera implements VBoxFactory, HBoxFactory, LabelFactory {
    private BdmSubEntry bdmSubEntry;
    private Label label = new Label();
    private Spinner<Integer> cameraShakeTypeSP = createCameraShakeTypeSP();
    private Spinner<Integer> cameraShakeTimeSP = createCameraShakeTimeSP();
    private Spinner<Integer> userBpeIdSP = createUserBpeIdSP();
    private Spinner<Integer> victimBpeIdSP = createVictimBpeIdSP();
    
    HBox[] hBoxs = new HBox[] {
        createHBox(0, Pos.CENTER_LEFT, createLabel("Camera Shake Type", 130), createHBox(15, Pos.CENTER_LEFT, new Node[] {cameraShakeTypeSP, label}, false)), 
        createHBox(0, Pos.CENTER_LEFT, createLabel("Camera Shake Time", 130), cameraShakeTimeSP),
        createHBox(0, Pos.CENTER_LEFT, createLabel("User BPE ID", 130), userBpeIdSP),
        createHBox(0, Pos.CENTER_LEFT, createLabel("Victim BPE ID", 130), victimBpeIdSP),
    };

    private Spinner<Integer> createCameraShakeTypeSP() {
        Spinner<Integer> spinner = new Spinner<>(Short.MIN_VALUE, Short.MAX_VALUE, 0);
        spinner.setEditable(true);
        spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            bdmSubEntry.setCameraShakeType(newValue.shortValue());
        });

        label.getStyleClass().add("custom-label");
        label.textProperty().bind(
            Bindings.createStringBinding(() -> {
                return switch (spinner.getValue().intValue()) {
                    case -1, 3, 4, 5, 9 -> "None";
                    case 0 -> "Rumble";
                    case 1 -> "Heavy Rumble";
                    case 2 -> "Extreme Rumble";
                    case 6, 10 -> "Camera Zoom";
                    case 7 -> "Static Camera";
                    case 8 -> "Camera Focus";
                    default -> "Unknown";
                };
            }, spinner.valueProperty())
        );

        return spinner;
    }

    private Spinner<Integer> createCameraShakeTimeSP() {
        Spinner<Integer> spinner = new Spinner<>(0, 65535, 0);
        spinner.setEditable(true);
        spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            bdmSubEntry.setCameraShakeTime(newValue.intValue());
        });

        return spinner;
    }

    private Spinner<Integer> createUserBpeIdSP() {
        Spinner<Integer> spinner = new Spinner<>(Short.MIN_VALUE, Short.MAX_VALUE, 0);
        spinner.setEditable(true);
        spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            bdmSubEntry.setUserBpeId(newValue.shortValue());
        });

        return spinner;
    }

    private Spinner<Integer> createVictimBpeIdSP() {
        Spinner<Integer> spinner = new Spinner<>(Short.MIN_VALUE, Short.MAX_VALUE, 0);
        spinner.setEditable(true);
        spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            bdmSubEntry.setVictimBpeId(newValue.shortValue());
        });

        return spinner;
    }

    public void setBdmEntry(BdmSubEntry bdmSubEntry) {
        this.bdmSubEntry = bdmSubEntry;
        updateItems();
    }

    private void updateItems() {
        cameraShakeTimeSP.getValueFactory().setValue(bdmSubEntry.getCameraShakeTime());
        cameraShakeTypeSP.getValueFactory().setValue((int) bdmSubEntry.getCameraShakeType());
        userBpeIdSP.getValueFactory().setValue((int) bdmSubEntry.getUserBpeId());
        victimBpeIdSP.getValueFactory().setValue((int) bdmSubEntry.getVictimBpeId());
    }

    @Override
    public VBox createVBox() {
        return createVBox(25, new Insets(20, 0, 0, 8), hBoxs);
    }
}