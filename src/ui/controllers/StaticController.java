package ui.controllers;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import model.DatabaseConnectionHandler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StaticController {

    @FXML
    private Button aggButton, nestedButton, divButton;

    @FXML
    private TableView<ObservableList<String>> aggResults, nestedResults, divResults;

    @FXML
    private void initialize() {
        aggResults.setVisible(false);
        nestedResults.setVisible(false);
        divResults.setVisible(false);

        aggButton.setOnAction(event -> doQuery("resources/sql/aggregation.sql", aggResults));
        nestedButton.setOnAction(event -> doQuery("resources/sql/nestedAggregation.sql", nestedResults));
        divButton.setOnAction(event -> doQuery("resources/sql/division.sql", divResults));
    }

    private void doQuery(String filepath, TableView<ObservableList<String>> table) {
        try {
            String query = new String(Files.readAllBytes(Paths.get(filepath)));
            ResultSet rs = DatabaseConnectionHandler.getInstance().runQuery(query);
            QueryController.displayResults(table, rs);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
}
