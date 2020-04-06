package ui.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import model.ColumnListing;
import model.DatabaseConnectionHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class JoinController extends ProjectionController {

    @FXML
    private ListView<String> tableToJoin;
    private ObservableList<String> tables = FXCollections.observableArrayList();
    private FilteredList<String> filtered = new FilteredList<>(tables, (p)->false);

    @Override @FXML
    void initialize() {
        super.initialize();
        DatabaseConnectionHandler handler = DatabaseConnectionHandler.getInstance();
        handler.onSetup((t)-> tables.setAll(t));
        tableToJoin.setItems(filtered);
        tableList.getSelectionModel().selectedItemProperty().addListener((o, oldVal, newVal) ->
                filtered.setPredicate(s -> getCommon(s, tableList.getSelectionModel().getSelectedItem()).size() > 0)
        );
    }

    private List<Pair<String, Integer>> getCommon(String table1, String table2) {
        List<Pair<String, Integer>> commonStrings = new ArrayList<>();
        DatabaseConnectionHandler handler = DatabaseConnectionHandler.getInstance();
        try {
            List<Pair<String, Integer>> col1 = handler.getColumns(table1);
            List<Pair<String, Integer>> col2 = handler.getColumns(table2);
            for (Pair<String, Integer> pair1 : col1) {
                for (Pair<String, Integer> pair2 : col2) {
                    if(pair1.getKey().equals(pair2.getKey())) {
                        commonStrings.add(pair1);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return commonStrings;
    }

    @Override
    void runAction() {
        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM ")
                .append(tableList.getSelectionModel().getSelectedItem())
                .append(" t1 ");

        query.append("INNER JOIN ")
                .append(tableToJoin.getSelectionModel().getSelectedItem())
                .append(" t2 USING (");
        query.append(
            checkboxes.stream().filter(p->p.getValue().isSelected()).map(Pair::getKey).collect(Collectors.joining(","))
        ).append(")");
        try {
            ResultSet resultSet = DatabaseConnectionHandler.getInstance().runQuery(query.toString());
            displayResults(results, resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    void fillPrompts(GridPane grid, List<ColumnListing> storage) {
        tableToJoin.getSelectionModel().selectedItemProperty().addListener((o, oldVal, table1) -> {
            String table2 = tableList.getSelectionModel().getSelectedItem();
            if(table1 == null || table2 == null) return;
            addCheckboxes(grid, getCommon(table1, table2));
        });
    }
}
