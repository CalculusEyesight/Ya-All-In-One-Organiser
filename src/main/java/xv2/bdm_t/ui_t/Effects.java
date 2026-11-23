package xv2.bdm.ui;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Spinner;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import uicomponents.boxes.HBoxFactory;
import uicomponents.boxes.VBoxFactory;
import uicomponents.buttons.RadioButtonFactory;
import uicomponents.labels.LabelFactory;
import uicomponents.panes.GridPaneFactory;
import xv2.bdm.Bdm.BdmSubEntry;

public class Effects implements RadioButtonFactory, HBoxFactory, VBoxFactory, LabelFactory, GridPaneFactory {
    BdmSubEntry bdmSubEntry;
    ToggleGroup effect1EepkTypeTG = createEffect1EepkTypeTG();
    ToggleGroup effect2EepkTypeTG = createEffect2EepkTypeTG();
    ToggleGroup effect3EepkTypeTG = createEffect3EepkTypeTG();
    Spinner<Integer> effect1IdSP = createEffect1IdSP();
    Spinner<Integer> effect2IdSP = createEffect2IdSP();
    Spinner<Integer> effect3IdSP = createEffect3IdSP();
    Spinner<Integer> effect1SkillIdSP = createEffect1SkillIdSP();
    Spinner<Integer> effect2SkillIdSP = createEffect2SkillIdSP();
    Spinner<Integer> effect3SkillIdSP = createEffect3SkillIdSP();

    RadioButton[] effectEepk1 = new RadioButton[] {
        createRadioButton("Common", effect1EepkTypeTG),
        createRadioButton("StageBG", effect1EepkTypeTG),
        createRadioButton("Character", effect1EepkTypeTG),
        createRadioButton("Awoken Skill", effect1EepkTypeTG),
        createRadioButton("Super Skill", effect1EepkTypeTG),
        createRadioButton("Ultimate Skill", effect1EepkTypeTG),
        createRadioButton("Evasive Skill", effect1EepkTypeTG),
        createRadioButton("Ki Blast Skill", effect1EepkTypeTG),
        createRadioButton("Stage", effect1EepkTypeTG),
    };

    RadioButton[] effectEepk2 = new RadioButton[] {
        createRadioButton("Common", effect2EepkTypeTG),
        createRadioButton("StageBG", effect2EepkTypeTG),
        createRadioButton("Character", effect2EepkTypeTG),
        createRadioButton("Awoken Skill", effect2EepkTypeTG),
        createRadioButton("Super Skill", effect2EepkTypeTG),
        createRadioButton("Ultimate Skill", effect2EepkTypeTG),
        createRadioButton("Evasive Skill", effect2EepkTypeTG),
        createRadioButton("Ki Blast Skill", effect2EepkTypeTG),
        createRadioButton("Stage", effect2EepkTypeTG),
    };

    RadioButton[] effectEepk3 = new RadioButton[] {
        createRadioButton("Common", effect3EepkTypeTG),
        createRadioButton("StageBG", effect3EepkTypeTG),
        createRadioButton("Character", effect3EepkTypeTG),
        createRadioButton("Awoken Skill", effect3EepkTypeTG),
        createRadioButton("Super Skill", effect3EepkTypeTG),
        createRadioButton("Ultimate Skill", effect3EepkTypeTG),
        createRadioButton("Evasive Skill", effect3EepkTypeTG),
        createRadioButton("Ki Blast Skill", effect3EepkTypeTG),
        createRadioButton("Stage", effect3EepkTypeTG),
    };

    HBox[] hBoxs = new HBox[] {
        createHBox(0, Pos.CENTER_LEFT, createLabel("Effect 1 ID", 160), effect1IdSP), 
        createHBox(0, Pos.CENTER_LEFT, createLabel("Effect 1 Skill ID", 160), effect1SkillIdSP),
        createHBox(0, Pos.CENTER_LEFT, createLabel("Effect 1 EEPK Type", 160), createGridPane(3, 3, effectEepk1, true)),
        createHBox(0, Pos.CENTER_LEFT, createLabel("Effect 2 ID", 160), effect2IdSP),
        createHBox(0, Pos.CENTER_LEFT, createLabel("Effect 2 Skill ID", 160), effect2SkillIdSP), 
        createHBox(0, Pos.CENTER_LEFT, createLabel("Effect 2 EEPK Type", 160), createGridPane(3, 3, effectEepk2, true)),
        createHBox(0, Pos.CENTER_LEFT, createLabel("Effect 3 ID", 160), effect3IdSP),
        createHBox(0, Pos.CENTER_LEFT, createLabel("Effect 3 Skill ID", 160), effect3SkillIdSP), 
        createHBox(0, Pos.CENTER_LEFT, createLabel("Effect 3 EEPK Type", 160), createGridPane(3, 3, effectEepk3, true))
    };

    private ToggleGroup createEffect1EepkTypeTG() {
        ToggleGroup toggleGroup = new ToggleGroup();
        toggleGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            int selectedIndex = toggleGroup.getToggles().indexOf(newToggle);
            switch (selectedIndex) {
                case 0 -> bdmSubEntry.setEffect1EepkType(0);
                case 1 -> bdmSubEntry.setEffect1EepkType(1);
                case 2 -> bdmSubEntry.setEffect1EepkType(2);
                case 3 -> bdmSubEntry.setEffect1EepkType(3);
                case 4 -> bdmSubEntry.setEffect1EepkType(5);
                case 5 -> bdmSubEntry.setEffect1EepkType(6);
                case 6 -> bdmSubEntry.setEffect1EepkType(7);
                case 7 -> bdmSubEntry.setEffect1EepkType(9);
                case 8 -> bdmSubEntry.setEffect1EepkType(11);
            }
        });

        return toggleGroup;
    }

    private ToggleGroup createEffect2EepkTypeTG() {
        ToggleGroup toggleGroup = new ToggleGroup();
        toggleGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            int selectedIndex = toggleGroup.getToggles().indexOf(newToggle);
            switch (selectedIndex) {
                case 0 -> bdmSubEntry.setEffect2EepkType(0);
                case 1 -> bdmSubEntry.setEffect2EepkType(1);
                case 2 -> bdmSubEntry.setEffect2EepkType(2);
                case 3 -> bdmSubEntry.setEffect2EepkType(3);
                case 4 -> bdmSubEntry.setEffect2EepkType(5);
                case 5 -> bdmSubEntry.setEffect2EepkType(6);
                case 6 -> bdmSubEntry.setEffect2EepkType(7);
                case 7 -> bdmSubEntry.setEffect2EepkType(9);
                case 8 -> bdmSubEntry.setEffect2EepkType(11);
            }
        });

        return toggleGroup;
    }

    private ToggleGroup createEffect3EepkTypeTG() {
        ToggleGroup toggleGroup = new ToggleGroup();
        toggleGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            int selectedIndex = toggleGroup.getToggles().indexOf(newToggle);
            switch (selectedIndex) {
                case 0 -> bdmSubEntry.setEffect3EepkType(0);
                case 1 -> bdmSubEntry.setEffect3EepkType(1);
                case 2 -> bdmSubEntry.setEffect3EepkType(2);
                case 3 -> bdmSubEntry.setEffect3EepkType(3);
                case 4 -> bdmSubEntry.setEffect3EepkType(5);
                case 5 -> bdmSubEntry.setEffect3EepkType(6);
                case 6 -> bdmSubEntry.setEffect3EepkType(7);
                case 7 -> bdmSubEntry.setEffect3EepkType(9);
                case 8 -> bdmSubEntry.setEffect3EepkType(11);
            }
        });

        return toggleGroup;
    }

    private Spinner<Integer> createEffect1IdSP() {
        Spinner<Integer> spinner = new Spinner<>(Short.MIN_VALUE, Short.MAX_VALUE, 0);
        spinner.setEditable(true);
        spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            bdmSubEntry.setEffect1Id(newValue.shortValue());
        });

        return spinner;
    }

    private Spinner<Integer> createEffect2IdSP() {
        Spinner<Integer> spinner = new Spinner<>(Short.MIN_VALUE, Short.MAX_VALUE, 0);
        spinner.setEditable(true);
        spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            bdmSubEntry.setEffect2Id(newValue.shortValue());
        });

        return spinner;
    }

    private Spinner<Integer> createEffect3IdSP() {
        Spinner<Integer> spinner = new Spinner<>(Short.MIN_VALUE, Short.MAX_VALUE, 0);
        spinner.setEditable(true);
        spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            bdmSubEntry.setEffect3Id(newValue.shortValue());
        });

        return spinner;
    }

    private Spinner<Integer> createEffect1SkillIdSP() {
        Spinner<Integer> spinner = new Spinner<>(0, 65535, 0);
        spinner.setEditable(true);
        spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            bdmSubEntry.setEffect1SkillId(newValue.intValue());
        });

        return spinner;
    }

    private Spinner<Integer> createEffect2SkillIdSP() {
        Spinner<Integer> spinner = new Spinner<>(0, 65535, 0);
        spinner.setEditable(true);
        spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            bdmSubEntry.setEffect2SkillId(newValue.intValue());
        });

        return spinner;
    }

    private Spinner<Integer> createEffect3SkillIdSP() {
        Spinner<Integer> spinner = new Spinner<>(0, 65535, 0);
        spinner.setEditable(true);
        spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            bdmSubEntry.setEffect3SkillId(newValue.intValue());
        });

        return spinner;
    }

    private Toggle effect1EepkTypeToggleSelection() {
        switch (bdmSubEntry.getEffect1EepkType()) {
            case 0 -> {return effect1EepkTypeTG.getToggles().get(0);}
            case 1 -> {return effect1EepkTypeTG.getToggles().get(1);}
            case 2 -> {return effect1EepkTypeTG.getToggles().get(2);}
            case 3 -> {return effect1EepkTypeTG.getToggles().get(3);}
            case 5 -> {return effect1EepkTypeTG.getToggles().get(4);}
            case 6 -> {return effect1EepkTypeTG.getToggles().get(5);}
            case 7 -> {return effect1EepkTypeTG.getToggles().get(6);}
            case 9 -> {return effect1EepkTypeTG.getToggles().get(7);}
            case 11 -> {return effect1EepkTypeTG.getToggles().get(8);}
            default -> {return effect1EepkTypeTG.getToggles().get(0); }
        }
    }

    private Toggle effect2EepkTypeToggleSelection() {
        switch (bdmSubEntry.getEffect2EepkType()) {
            case 0 -> {return effect2EepkTypeTG.getToggles().get(0);}
            case 1 -> {return effect2EepkTypeTG.getToggles().get(1);}
            case 2 -> {return effect2EepkTypeTG.getToggles().get(2);}
            case 3 -> {return effect2EepkTypeTG.getToggles().get(3);}
            case 5 -> {return effect2EepkTypeTG.getToggles().get(4);}
            case 6 -> {return effect2EepkTypeTG.getToggles().get(5);}
            case 7 -> {return effect2EepkTypeTG.getToggles().get(6);}
            case 9 -> {return effect2EepkTypeTG.getToggles().get(7);}
            case 11 -> {return effect2EepkTypeTG.getToggles().get(8);}
            default -> {return effect2EepkTypeTG.getToggles().get(0); }
        }
    }

    private Toggle effect3EepkTypeToggleSelection() {
        switch (bdmSubEntry.getEffect3EepkType()) {
            case 0 -> {return effect3EepkTypeTG.getToggles().get(0);}
            case 1 -> {return effect3EepkTypeTG.getToggles().get(1);}
            case 2 -> {return effect3EepkTypeTG.getToggles().get(2);}
            case 3 -> {return effect3EepkTypeTG.getToggles().get(3);}
            case 5 -> {return effect3EepkTypeTG.getToggles().get(4);}
            case 6 -> {return effect3EepkTypeTG.getToggles().get(5);}
            case 7 -> {return effect3EepkTypeTG.getToggles().get(6);}
            case 9 -> {return effect3EepkTypeTG.getToggles().get(7);}
            case 11 -> {return effect3EepkTypeTG.getToggles().get(8);}
            default -> {return effect3EepkTypeTG.getToggles().get(0); }
        }
    }

    @Override
    public VBox createVBox() {
        return createVBox(35, new Insets(20, 0, 20, 8), hBoxs);
    }

    public ScrollPane createScrollPane() {
        return new ScrollPane(createVBox());
    }

    public void setBdmEntry(BdmSubEntry bdmSubEntry) {
        this.bdmSubEntry = bdmSubEntry;
        updateItems();
    }

    private void updateItems() {
        effect1IdSP.getValueFactory().setValue((int) bdmSubEntry.getEffect1Id());
        effect2IdSP.getValueFactory().setValue((int) bdmSubEntry.getEffect2Id());
        effect3IdSP.getValueFactory().setValue((int) bdmSubEntry.getEffect3Id());
        effect1SkillIdSP.getValueFactory().setValue(bdmSubEntry.getEffect1SkillId());
        effect2SkillIdSP.getValueFactory().setValue(bdmSubEntry.getEffect2SkillId());
        effect3SkillIdSP.getValueFactory().setValue(bdmSubEntry.getEffect3SkillId());
        effect1EepkTypeTG.selectToggle(effect1EepkTypeToggleSelection());
        effect2EepkTypeTG.selectToggle(effect2EepkTypeToggleSelection());
        effect3EepkTypeTG.selectToggle(effect3EepkTypeToggleSelection());
    }
}