package ui.controllers;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Pair;
import model.DatabaseConnectionHandler;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

public class InsertController {
    @FXML
    private ListView<String> tableList;
    @FXML
    private VBox prompts;

    @FXML
    private void initialize() {
        DatabaseConnectionHandler handler = DatabaseConnectionHandler.getInstance();
        handler.onSetup((tables)-> tableList.getItems().setAll(tables));
        tableList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tableList.getSelectionModel().selectedItemProperty().addListener((o, oldVal, newVal) -> {
            try {
                List<Pair<String, Integer>> columns = handler.getColumns(newVal);
                prompts.getChildren().clear();
                for (Pair<String, Integer> column : columns) {
                    prompts.getChildren().add(getColumnEntry(column.getKey(), column.getValue()));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    private Node getColumnEntry(String key, Integer value) {
        Label label = new Label(key);
        TextField input = new TextField();
        for (Field field : Types.class.getFields()) {
            try {
                if(field.get(null).equals(value)) {
                    input.setPromptText(field.getName());
                    break;
                }
            } catch (IllegalAccessException ignored) {
            }
        }

        return new HBox(label, input);
    }
}
