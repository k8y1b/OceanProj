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
        tryToLogin(0);
        handler.databaseSetup();
    }

    private void tryToLogin(int tries) {
        if(tries > 4) Platform.exit();
        Optional<Pair<String, String>> pair = new LoginDialog().showAndWait();
        if(!pair.isPresent()) Platform.exit();
        else {

            boolean login = handler.login(pair.get().getKey(), pair.get().getValue());
            if(!login) tryToLogin(tries + 1);
        }
    }
}
