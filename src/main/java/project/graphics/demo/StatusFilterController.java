package project.graphics.demo;

import javafx.scene.control.CheckBox;
import project.framework.CriteriaListFactory;
import project.framework.Provider;
import project.framework.Service;

import java.io.IOException;
import java.util.List;
import java.util.Vector;

public class StatusFilterController {
    private Vector<String> selectedStatuses = new Vector<>();
    private Vector<String> filteredStatuses = new Vector<>();
    private List<CheckBox> statusesCheckBox = new Vector<>();
    private List<CheckBox> filteredStatusCheckBox = new Vector<>();
    private Vector<String> invalidSelectedStatuses = new Vector<>();

    public StatusFilterController() {
        Vector<String> allStatuses = CriteriaListFactory.getStatusList();
        for(String status : allStatuses) {
            CheckBox statusCheckBox = new CheckBox(status);
            statusCheckBox.getStyleClass().add("countries-check-box");
            statusCheckBox.setDisable(true);
            statusCheckBox.selectedProperty().addListener((observableValue, aBoolean, t1) -> {
                if (statusCheckBox.isSelected())
                    selectedStatuses.add(statusCheckBox.getText());
                else {
                    selectedStatuses.remove(statusCheckBox.getText());
                    statusCheckBox.setStyle("-fx-text-fill:  black;");
                }
            });
            statusesCheckBox.add(statusCheckBox);
        }
    }

    /**
     *creates a list of the selected checkboxes to filter
     *
     *
     * @param c, p, t vectors of the selected checkboxes
     *
     * @throws IOException
     *
     * @return filteredStatusCheckBox list of the checkboxes????
     *
     *
     * @see
     */
    public List<CheckBox> getCheckBoxes(Vector<String> c, Vector<Provider> p, Vector<String> t) {
        filteredStatuses.clear();
        invalidSelectedStatuses.clear();

        statusesCheckBox.forEach(CBox -> {
            CBox.setDisable(true);
        });

        for (Provider prov : p) {

            if (c.contains(prov.getCountryCode())) {

                Service[] services = prov.getServices();
                for (Service s : services) {
                    String[] types = s.getServiceTypes();

                    for (String type : types) {
                        String cs = s.getCurrentStatus();
                        if (t.contains(type) && !filteredStatuses.contains(cs))
                            filteredStatuses.add(cs);
                    }
                }

                for (CheckBox CBox : statusesCheckBox) {
                    if (filteredStatuses.contains(CBox.getText())) {
                        CBox.setStyle("-fx-text-fill:  black;");
                        CBox.setDisable(false);
                    }
                }
            }
        }

        statusesCheckBox.forEach(CBox -> {
            if (CBox.isSelected() && !filteredStatuses.contains(CBox.getText())) {
                filteredStatusCheckBox.add(CBox);
                invalidSelectedStatuses.add(CBox.getText());
                CBox.setStyle("-fx-text-fill: #b50202;");
                CBox.setDisable(false);
            }});

        return statusesCheckBox;
    }

    public Vector<String> getCriteria() {
        Vector<String> tmp = new Vector<>();
        for (String s : selectedStatuses) {
            if (!invalidSelectedStatuses.contains(s)) tmp.add(s);
        }
        if (tmp.isEmpty()) {
            for (String s : filteredStatuses) tmp.add(s);
            tmp.add(null);
        }
        else {
            int invalids = 0;
            for (String invalid : invalidSelectedStatuses) {
                tmp.add(invalid);
                invalids++;
            }
            tmp.add(Integer.toString(invalids));
        }
        return tmp;
    }

    public Vector<String> getFilterCriteria() {
        if (selectedStatuses.isEmpty())
            return CriteriaListFactory.getStatusList();
        return selectedStatuses;
    }
}
