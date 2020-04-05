package ui.controllers;

import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.util.Pair;
import model.DatabaseConnectionHandler;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class InsertController {
    @FXML
    private ListView<String> tableList;
    @FXML
    private GridPane prompts;
    @FXML
    private Button insert;
    private List<Pair<Integer,TextField>> insertData = new ArrayList<>();
    @FXML
    private void initialize() {
        DatabaseConnectionHandler handler = DatabaseConnectionHandler.getInstance();
        handler.onSetup((tables)-> tableList.getItems().setAll(tables));
        tableList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tableList.getSelectionModel().selectedItemProperty().addListener((o, oldVal, newVal) -> {
            try {
                List<Pair<String, Integer>> columns = handler.getColumns(newVal);
                prompts.getChildren().clear();
                insertData.clear();
                int row = 0;
                for (Pair<String, Integer> column : columns) {
                    Pair<Label, TextField> pair = getColumnEntry(column.getKey(), column.getValue());
                    insertData.add(new Pair<>(column.getValue(), pair.getValue()));
                    prompts.addRow(row, pair.getKey(), pair.getValue());
                    row++;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        insert.setOnAction(event -> {
            runInsert();
            for (Node child : prompts.getChildren()) {
                if(child instanceof TextField)
                    ((TextField) child).clear();
            }
        });
    }

    private void runInsert() {
        String tableName = tableList.getSelectionModel().getSelectedItem();
        List<Pair<Integer, String>> list = insertData.stream().map((p) ->
            new Pair<>(p.getKey(), p.getValue().getText())
        ).collect(Collectors.toList());
        DatabaseConnectionHandler.getInstance().insert(tableName, list);
    }

    private Pair<Label, TextField> getColumnEntry(String key, Integer value) {
        Label label = new Label(key);
        TextField input = new TextField();
        GridPane.setHgrow(input, Priority.SOMETIMES);
        GridPane.setHalignment(label, HPos.CENTER);
        for (Field field : Types.class.getFields()) {
            try {
                if(field.get(null).equals(value)) {
                    input.setPromptText(field.getName());
                    break;
                }
            } catch (IllegalAccessException ignored) {
            }
        }

        return new Pair<>(label, input);
    }
}
