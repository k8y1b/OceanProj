package ui.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.util.Pair;
import model.DatabaseConnectionHandler;
import ui.LoginDialog;

import java.util.Optional;

public class Controller {

    DatabaseConnectionHandler handler = DatabaseConnectionHandler.getInstance();

    @FXML
    private void initialize() {
        if(tryToLogin(0))
            handler.databaseSetup();
    }

    private boolean tryToLogin(int tries) {
        if(tries > 4) return false;
        Optional<Pair<String, String>> pair = new LoginDialog().showAndWait();
        if(!pair.isPresent()) {
            return false;
        }
        else {
            boolean login = handler.login(pair.get().getKey(), pair.get().getValue());
            if(!login) tryToLogin(tries + 1);
            else return true;
        }
        return false;
    }
}
