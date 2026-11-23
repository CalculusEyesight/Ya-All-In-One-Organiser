package mainuisetup;
import java.util.ArrayList;

import javafx.scene.control.ComboBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class AutoGrouping {
    protected static final ArrayList<ArrayList<Tab>> groupedEmbTabs = new ArrayList<>();
    protected static final ArrayList<ArrayList<Tab>> groupedEmbDytTabs = new ArrayList<>();
    private static final ComboBox<String> embComboBox = new ComboBox<>();
    private static final ComboBox<String> embDytComboBox = new ComboBox<>();

    protected static void setupAutoGrouping(TabPane tabPane) {
        Menus.autoGroup.setDisable(true);
        Menus.autoGroup.selectedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                boolean hasEmb = true;

                while (hasEmb) {
                    String compareEmbDytText = null;
                    String compareEmbText = null;

                    for (Tab tab : tabPane.getTabs()) {
                        if (tab.getText().endsWith("dyt.emb")) {
                            compareEmbDytText = tab.getText().substring(0, Math.min(tab.getText().length(), 7));
                            embDytComboBox.getItems().add(compareEmbDytText);
                            groupedEmbDytTabs.add(new ArrayList<>());
                            hasEmb = true;
                            break;
                        } else if (tab.getText().endsWith("emb")) {
                            compareEmbText = tab.getText().substring(0, Math.min(tab.getText().length(), 7));
                            embComboBox.getItems().add(compareEmbText);
                            groupedEmbTabs.add(new ArrayList<>());
                            hasEmb = true;
                            break;
                        } else {
                            hasEmb = false;
                        }
                    }

                    if (hasEmb) {
                        for (Tab tab : tabPane.getTabs()) {
                            if (tab.getText().endsWith("dyt.emb") && compareEmbDytText != null && tab.getText().contains(compareEmbDytText)) {
                                groupedEmbDytTabs.getLast().add(tab);
                            } else if (tab.getText().endsWith("emb") && compareEmbText != null && tab.getText().contains(compareEmbText)) {
                                groupedEmbTabs.getLast().add(tab);
                            }
                        }

                        if (compareEmbDytText != null) {
                            tabPane.getTabs().removeAll(groupedEmbDytTabs.getLast());
                        } else if (compareEmbText != null) {
                            tabPane.getTabs().removeAll(groupedEmbTabs.getLast());
                        }
                    }
                }

                Tab embGroupTab = new Tab();
                Tab embDytGroupTab = new Tab();

                embGroupTab.setGraphic(embComboBox);
                embDytGroupTab.setGraphic(embDytComboBox);

                embGroupTab.setText(".emb");
                embDytGroupTab.setText(".dyt.emb");

                tabPane.getTabs().add(0, embGroupTab);
                tabPane.getTabs().add(1, embDytGroupTab);
                tabPane.getTabs().get(0).setClosable(false);
                tabPane.getTabs().get(1).setClosable(false);

            } else {
                embComboBox.getItems().clear();
                embDytComboBox.getItems().clear();

                if (tabPane.getTabs().size() > 0) {
                    tabPane.getTabs().remove(0, 2);
                }

                for (ArrayList<Tab> tabs : groupedEmbTabs) {
                    tabPane.getTabs().addAll(tabs);
                }

                for (ArrayList<Tab> tabs : groupedEmbDytTabs) {
                    tabPane.getTabs().addAll(tabs);
                }

                groupedEmbDytTabs.clear();
                groupedEmbTabs.clear();
            }
        });
    }

    protected static void setupEmbComboBoxes(TabPane tabPane) {
        embComboBox.getSelectionModel().selectedIndexProperty().addListener((obs, oldValue, newValue) -> {
            TabPane comboBoxTabPane = new TabPane();

            if (newValue.intValue() != -1) {
                comboBoxTabPane.getTabs().addAll(groupedEmbTabs.get(newValue.intValue()));
                tabPane.getTabs().get(0).setContent(comboBoxTabPane);
            }
        });

        embDytComboBox.getSelectionModel().selectedIndexProperty().addListener((obs, oldValue, newValue) -> {
            TabPane comboBoxTabPane = new TabPane();

            if (newValue.intValue() != -1) {
                comboBoxTabPane.getTabs().addAll(groupedEmbDytTabs.get(newValue.intValue()));
                tabPane.getTabs().get(1).setContent(comboBoxTabPane);
            }
        });
    }
}