package ui.controllers;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import model.ColumnListing;
import model.DatabaseConnectionHandler;
import model.TableEntry;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UpdateController extends DIUController {
    private List<ColumnListing> where = new ArrayList<>();
    @FXML
    private GridPane wherePrompts;

    @FXML @Override
    void initialize() {
        super.initialize();
        fillPrompts(wherePrompts, where);
    }

    @Override
    void runAction() {
        String tableName = tableList.getSelectionModel().getSelectedItem();
        List<TableEntry> dataList = data.stream().filter(
                p->p.getInput().getText().length() > 0
        ).map((p) ->
                new TableEntry(p.getType(), p.getInput().getText(), p.getColumnName())
        ).collect(Collectors.toList());
        List<TableEntry> whereList = where.stream().filter(
                p->p.getInput().getText().length() > 0
        ).map((p) ->
                new TableEntry(p.getType(), p.getInput().getText(), p.getColumnName())
        ).collect(Collectors.toList());
        DatabaseConnectionHandler.getInstance().update(tableName, dataList, whereList);
        for (Node child : wherePrompts.getChildren()) {
            if(child instanceof TextField)
                ((TextField) child).clear();
        }
    }
}
