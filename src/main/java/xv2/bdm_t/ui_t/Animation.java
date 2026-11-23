package xv2.bdm.ui;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Spinner;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import uicomponents.boxes.HBoxFactory;
import uicomponents.boxes.VBoxFactory;
import uicomponents.labels.LabelFactory;
import xv2.bdm.Bdm.BdmSubEntry;

public class Animation implements VBoxFactory, HBoxFactory, LabelFactory {
    private BdmSubEntry bdmSubEntry;
    private Spinner<Double> userAnimationSpeedSP = createUserAnimationSpeedSP();
    private Spinner<Double> victimAnimationSpeedSP = createVictimAnimationSpeedSP();
    private Spinner<Integer> userAnimationTimeSP = createUserAnimationTimeSP();
    private Spinner<Integer> victimAnimationTimeSP = createVictimAnimationTimeSP();

    HBox[] hBoxs = new HBox[] {
        createHBox(0, Pos.CENTER_LEFT, createLabel("User Animation Time", 160), userAnimationTimeSP),
        createHBox(0, Pos.CENTER_LEFT, createLabel("User Animation Speed", 160), userAnimationSpeedSP),
        createHBox(0, Pos.CENTER_LEFT, createLabel("Victim Animation Time", 160), victimAnimationTimeSP),
        createHBox(0, Pos.CENTER_LEFT, createLabel("Victim Animation Speed", 160), victimAnimationSpeedSP),
    };

    private Spinner<Double> createUserAnimationSpeedSP() {
        Spinner<Double> spinner = new Spinner<>(-Float.MAX_VALUE, Float.MAX_VALUE, 0);
        spinner.setEditable(true);
        spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            bdmSubEntry.setUserAnimationSpeed(newValue.floatValue());
        });

        return spinner;
    }

    private Spinner<Integer> createVictimAnimationTimeSP() {
        Spinner<Integer> spinner = new Spinner<>(0, 65535, 0);
        spinner.setEditable(true);
        spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            bdmSubEntry.setVictimAnimationTime(newValue.intValue());
        });

        return spinner;
    }

    private Spinner<Integer> createUserAnimationTimeSP() {
        Spinner<Integer> spinner = new Spinner<>(0, 65535, 0);
        spinner.setEditable(true);
        spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            bdmSubEntry.setUserAnimationTime(newValue.intValue());
        });

        return spinner;
    }

    private Spinner<Double> createVictimAnimationSpeedSP() {
        Spinner<Double> spinner = new Spinner<>(-Float.MAX_VALUE, Float.MAX_VALUE, 0);
        spinner.setEditable(true);
        spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            bdmSubEntry.setVictimAnimationSpeed(newValue.floatValue());
        });

        return spinner;
    }

    public void setBdmEntry(BdmSubEntry bdmSubEntry) {
        this.bdmSubEntry = bdmSubEntry;
        updateItems();
    }

    private void updateItems() {
        userAnimationTimeSP.getValueFactory().setValue(bdmSubEntry.getUserAnimationTime());
        userAnimationSpeedSP.getValueFactory().setValue((double) bdmSubEntry.getUserAnimationSpeed());
        victimAnimationTimeSP.getValueFactory().setValue(bdmSubEntry.getVictimAnimationTime());
        victimAnimationSpeedSP.getValueFactory().setValue((double) bdmSubEntry.getVictimAnimationSpeed());
    }

    @Override
    public VBox createVBox() {
        return createVBox(30, new Insets(20, 0, 0, 8), hBoxs);
    }
}