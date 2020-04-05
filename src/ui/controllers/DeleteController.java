package ui.controllers;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.util.Pair;
import model.DatabaseConnectionHandler;
import model.TableEntry;

import java.util.List;
import java.util.stream.Collectors;

public class DeleteController extends DIUController {

    @Override
    void runAction() {
        String tableName = tableList.getSelectionModel().getSelectedItem();
        List<TableEntry> list = data.stream().filter(
                p->p.getInput().getText().length() > 0
        ).map((p) ->
                new TableEntry(p.getType(), p.getInput().getText(), p.getColumnName())
        ).collect(Collectors.toList());
        DatabaseConnectionHandler.getInstance().delete(tableName, list);
    }
}
