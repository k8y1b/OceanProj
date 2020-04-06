package ui.controllers;

import model.DatabaseConnectionHandler;
import model.TableEntry;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class SelectionController extends QueryController {
    @Override
    void runAction() {
        List<TableEntry> list = data.stream().filter(
                p->p.getInput().getText().length() > 0
        ).map((p) ->
                new TableEntry(p.getType(), p.getInput().getText(), p.getColumnName())
        ).collect(Collectors.toList());

        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM ").append(
            tableList.getSelectionModel().getSelectedItem()
        ).append(" t WHERE ").append(
            list.stream().map(e -> "t."+e.getColumnName()+"=(?)").collect(Collectors.joining(" AND "))
        );
        try {
            ResultSet resultSet = DatabaseConnectionHandler.getInstance().runParametrizedQuery(query.toString(), list);
            QueryController.displayResults(results, resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
