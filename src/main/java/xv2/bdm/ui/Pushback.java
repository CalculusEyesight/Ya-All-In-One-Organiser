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

public class Pushback implements VBoxFactory, HBoxFactory, LabelFactory {
    BdmSubEntry bdmSubEntry;
    Spinner<Double> pushbackStrengthSP = createPushbackStrengthSP();
    Spinner<Double> pushbackAccelerationSP = createPushbackAccelerationSP();
    Spinner<Integer> userStuntSP = createUserStuntSP();
    Spinner<Integer> victimStuntSp = createVictimStuntSP();
    Spinner<Integer> knockbackDurationSP = createKnockbackDurationSP();
    Spinner<Integer> knockbackGroundImpactTimeSP = createKnockbackGroundImpactTimeSP();
    Spinner<Integer> knockbackRecoveryAfterImpactTimeSP = createKnockbackRecoveryAfterImpactTimeSP();
    Spinner<Double> knockbackStrengthXSP = createKnockbackStrengthXSP();
    Spinner<Double> knockbackStrengthYSP = createKnockbackStrengthYSP();
    Spinner<Double> knockbackStrengthZSP = createKnockbackStrengthZSP();
    Spinner<Double> knockbackDragYSP = createKnockbackDragYSP();
    Spinner<Integer> knockbackGravityTimeSP = createKnockbackGravityTimeSP();
    Spinner<Integer> victimInvincibilityTimeSP = createVictimInvincibilityTimeSP();

    private HBox[] hBoxs = new HBox[] {
        createHBox(0, Pos.CENTER_LEFT, createLabel("Pushback Strength", 230), pushbackStrengthSP), 
        createHBox(0, Pos.CENTER_LEFT, createLabel("Pushback Acceleration", 230), pushbackAccelerationSP),
        createHBox(0, Pos.CENTER_LEFT, createLabel("User Stunt", 230), userStuntSP), 
        createHBox(0, Pos.CENTER_LEFT, createLabel("Victim Stunt", 230), victimStuntSp),
        createHBox(0, Pos.CENTER_LEFT, createLabel("Knockback Duration", 230), knockbackDurationSP),  
        createHBox(0, Pos.CENTER_LEFT, createLabel("Knockback Ground Impact Time", 230), knockbackGroundImpactTimeSP),  
        createHBox(0, Pos.CENTER_LEFT, createLabel("Knockback Recovery After Impact Time", 230), knockbackRecoveryAfterImpactTimeSP),
        createHBox(0, Pos.CENTER_LEFT, createLabel("Knockback Strength X", 230), knockbackStrengthXSP), 
        createHBox(0, Pos.CENTER_LEFT, createLabel("Knockback Strength Y", 230), knockbackStrengthYSP),
        createHBox(0, Pos.CENTER_LEFT, createLabel("Knockback Strength Z", 230), knockbackStrengthZSP), 
        createHBox(0, Pos.CENTER_LEFT, createLabel("Knockback Drag Y", 230), knockbackDragYSP),
        createHBox(0, Pos.CENTER_LEFT, createLabel("Knockback Gravity Time", 230), knockbackGravityTimeSP), 
        createHBox(0, Pos.CENTER_LEFT, createLabel("Victim Invincibility Time", 230), victimInvincibilityTimeSP)
    };

    private Spinner<Double> createPushbackStrengthSP() {
        Spinner<Double> spinner = new Spinner<>(-Float.MAX_VALUE, Float.MAX_VALUE, 0);
        spinner.setEditable(true);
        spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            bdmSubEntry.setPushbackStrength(newValue.floatValue());
        });

        return spinner;
    }

    private Spinner<Double> createPushbackAccelerationSP() {
        Spinner<Double> spinner = new Spinner<>(-Float.MAX_VALUE, Float.MAX_VALUE, 0);
        spinner.setEditable(true);
        spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            bdmSubEntry.setPushbackAcceleration(newValue.floatValue());
        });

        return spinner;
    }

    private Spinner<Integer> createUserStuntSP() {
        Spinner<Integer> spinner = new Spinner<>(0, 65535, 0);
        spinner.setEditable(true);
        spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            bdmSubEntry.setUserStunt(newValue.intValue());
        });

        return spinner;
    }

    private Spinner<Integer> createVictimStuntSP() {
        Spinner<Integer> spinner = new Spinner<>(0, 65535, 0);
        spinner.setEditable(true);
        spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            bdmSubEntry.setVictimStunt(newValue.intValue());
        });

        return spinner;
    }

    private Spinner<Integer> createKnockbackDurationSP() {
        Spinner<Integer> spinner = new Spinner<>(0, 65535, 0);
        spinner.setEditable(true);
        spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            bdmSubEntry.setKnockbackDuration(newValue.intValue());
        });

        return spinner;
    }

    private Spinner<Integer> createKnockbackGroundImpactTimeSP() {
        Spinner<Integer> spinner = new Spinner<>(0, 65535, 0);
        spinner.setEditable(true);
        spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            bdmSubEntry.setKnockbackGroundImpactTime(newValue.intValue());
        });

        return spinner;
    }

    private Spinner<Integer> createKnockbackRecoveryAfterImpactTimeSP() {
        Spinner<Integer> spinner = new Spinner<>(0, 65535, 0);
        spinner.setEditable(true);
        spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            bdmSubEntry.setKnockbackRecoveryAfterImpactTime(newValue.intValue());
        });

        return spinner;
    }

    private Spinner<Double> createKnockbackStrengthXSP() {
        Spinner<Double> spinner = new Spinner<>(-Float.MAX_VALUE, Float.MAX_VALUE, 0);
        spinner.setEditable(true);
        spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            bdmSubEntry.setKnockbackStrengthX(newValue.floatValue());
        });

        return spinner;
    }

    private Spinner<Double> createKnockbackStrengthYSP() {
        Spinner<Double> spinner = new Spinner<>(-Float.MAX_VALUE, Float.MAX_VALUE, 0);
        spinner.setEditable(true);
        spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            bdmSubEntry.setKnockbackStrengthY(newValue.floatValue());
        });

        return spinner;
    }

    private Spinner<Double> createKnockbackStrengthZSP() {
        Spinner<Double> spinner = new Spinner<>(-Float.MAX_VALUE, Float.MAX_VALUE, 0);
        spinner.setEditable(true);
        spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            bdmSubEntry.setKnockbackStrengthZ(newValue.floatValue());
        });

        return spinner;
    }

    private Spinner<Double> createKnockbackDragYSP() {
        Spinner<Double> spinner = new Spinner<>(-Float.MAX_VALUE, Float.MAX_VALUE, 0);
        spinner.setEditable(true);
        spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            bdmSubEntry.setKnockbackDragY(newValue.floatValue());
        });

        return spinner;
    }

    private Spinner<Integer> createKnockbackGravityTimeSP() {
        Spinner<Integer> spinner = new Spinner<>(0, 65535, 0);
        spinner.setEditable(true);
        spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            bdmSubEntry.setKnockbackGravityTime(newValue.intValue());
        });

        return spinner;
    }

    private Spinner<Integer> createVictimInvincibilityTimeSP() {
        Spinner<Integer> spinner = new Spinner<>(Short.MIN_VALUE, Short.MAX_VALUE, 0);
        spinner.setEditable(true);
        spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            bdmSubEntry.setVictimInvincibilityTime(newValue.shortValue());
        });

        return spinner;
    }

    @Override
    public VBox createVBox() {
        return createVBox(27, new Insets(20, 0, 0, 8), hBoxs);
    }

    public void setBdmEntry(BdmSubEntry bdmSubEntry) {
        this.bdmSubEntry = bdmSubEntry;
        updateItems();
    }

    private void updateItems() {
        pushbackStrengthSP.getValueFactory().setValue((double) bdmSubEntry.getPushbackStrength());
        pushbackAccelerationSP.getValueFactory().setValue((double) bdmSubEntry.getPushbackAcceleration());
        userStuntSP.getValueFactory().setValue(bdmSubEntry.getUserStunt());
        victimStuntSp.getValueFactory().setValue(bdmSubEntry.getVictimStunt());
        knockbackDurationSP.getValueFactory().setValue(bdmSubEntry.getKnockbackDuration());
        knockbackGroundImpactTimeSP.getValueFactory().setValue(bdmSubEntry.getKnockbackGroundImpactTime());
        knockbackRecoveryAfterImpactTimeSP.getValueFactory().setValue(bdmSubEntry.getKnockbackRecoveryAfterImpactTime());
        knockbackStrengthXSP.getValueFactory().setValue((double) bdmSubEntry.getKnockbackStrengthX());
        knockbackStrengthYSP.getValueFactory().setValue((double) bdmSubEntry.getKnockbackStrengthY());
        knockbackStrengthZSP.getValueFactory().setValue((double) bdmSubEntry.getKnockbackStrengthZ());
        knockbackDragYSP.getValueFactory().setValue((double) bdmSubEntry.getKnockbackDragY());
        knockbackGravityTimeSP.getValueFactory().setValue(bdmSubEntry.getKnockbackGravityTime());
        victimInvincibilityTimeSP.getValueFactory().setValue((int) bdmSubEntry.getVictimInvincibilityTime());
    }
}