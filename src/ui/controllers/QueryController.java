package ui.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public abstract class QueryController extends DIUController {
    @FXML
    TableView<ObservableList<String>> results;

    @FXML @Override
    void initialize() {
        super.initialize();
        results.setVisible(false);
    }



    /*
    * Some help got from
    * https://stackoverflow.com/questions/18941093/how-to-fill-up-a-tableview-with-database-data
    * */
    void displayResults(ResultSet rs) throws SQLException {
        ResultSetMetaData md = rs.getMetaData();

        results.getItems().clear();
        results.getColumns().clear();
        for(int i=0; i < md.getColumnCount(); i++) {
            TableColumn<ObservableList<String>, String> col = new TableColumn<>(md.getColumnName(i + 1));
            int finalI = i;
            col.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(finalI))
            );
            results.getColumns().add(col);
        }

        while(rs.next()) {
            ObservableList<String> row = FXCollections.observableArrayList();
            for(int i = 0; i < md.getColumnCount(); i++) {
                row.add(rs.getString(i+1));
            }
            results.getItems().add(row);
        }

        results.setVisible(true);
        rs.close();
    }
}
