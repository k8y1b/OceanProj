package ui.controllers;

import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.util.Pair;
import model.ColumnListing;
import model.DatabaseConnectionHandler;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public abstract class DIUController {
    @FXML
    ListView<String> tableList;
    @FXML
    GridPane prompts;
    @FXML
    private Button button;
    List<ColumnListing> data = new ArrayList<>();

    @FXML
    void initialize() {
        DatabaseConnectionHandler handler = DatabaseConnectionHandler.getInstance();
        handler.onSetup((tables)-> tableList.getItems().setAll(tables));
        tableList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        fillPrompts(prompts, data);
        button.setOnAction(event -> {
            runAction();
            for (Node child : prompts.getChildren()) {
                if(child instanceof TextField)
                    ((TextField) child).clear();
            }
        });
    }

    void fillPrompts(GridPane grid, List<ColumnListing> storage) {
        tableList.getSelectionModel().selectedItemProperty().addListener((o, oldVal, newVal) -> {
            try {
                List<Pair<String, Integer>> columns = DatabaseConnectionHandler.getInstance().getColumns(newVal);
                grid.getChildren().clear();
                storage.clear();
                int row = 0;
                for (Pair<String, Integer> column : columns) {
                    Pair<Label, TextField> pair = getColumnEntry(column.getKey(), column.getValue());
                    storage.add(new ColumnListing(column.getKey(), column.getValue(), pair.getValue()));
                    grid.addRow(row, pair.getKey(), pair.getValue());
                    row++;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    abstract void runAction();

    Pair<Label, TextField> getColumnEntry(String key, Integer value) {
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
