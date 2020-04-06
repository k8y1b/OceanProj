package ui.controllers;

import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import model.ColumnListing;
import model.DatabaseConnectionHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProjectionController extends QueryController {
    @Override
    void runAction() {
        StringBuilder query = new StringBuilder();
        query.append("SELECT (");
        query.append(
            checkboxes.stream().filter(p->p.getValue().isSelected()).map(Pair::getKey).collect(Collectors.joining(","))
        ).append(") FROM ")
        .append(tableList.getSelectionModel().getSelectedItem());
        try {
            ResultSet resultSet = DatabaseConnectionHandler.getInstance().runQuery(query.toString());
            displayResults(results, resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    List<Pair<String, CheckBox>> checkboxes = new ArrayList<>();
    @Override
    void fillPrompts(GridPane grid, List<ColumnListing> storage) {
        tableList.getSelectionModel().selectedItemProperty().addListener((o, oldVal, newVal) -> {
            try {
                addCheckboxes(grid, DatabaseConnectionHandler.getInstance().getColumns(newVal));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    void addCheckboxes(GridPane grid, List<Pair<String, Integer>> columns) {
        grid.getChildren().clear();
        checkboxes.clear();
        int row = 0;
        for (Pair<String, Integer> column : columns) {
            Label label = new Label(column.getKey());
            CheckBox checkBox = new CheckBox();
            checkBox.setSelected(false);
            checkBox.setAllowIndeterminate(false);
            grid.addRow(row, label, checkBox);
            checkboxes.add(new Pair<>(column.getKey(), checkBox));
            row++;
        }
    }
}
