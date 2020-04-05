package ui;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

public class LoginDialog extends Dialog<Pair<String, String>> {
    /*
    * Referenced https://code.makery.ch/blog/javafx-dialogs-official/ heavily
    * slightly different, but honestly there's kind of one way to do a login dialog
    * */
    public LoginDialog() {
        super();
        setTitle("Login");
        setHeaderText("Please login to the database.");
        ButtonType login = new ButtonType("Login", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(login, ButtonType.CANCEL);

        GridPane grid = new GridPane();


        grid.add(new Label("Username:"), 0, 0);
        TextField username = new TextField();
        username.setPromptText("Username");
        grid.add(username, 1, 0);


        grid.add(new Label("Password:"), 0, 1);
        PasswordField password = new PasswordField();
        password.setPromptText("Password");
        grid.add(password, 1, 1);

        Node loginButton = getDialogPane().lookupButton(login);
        loginButton.setDisable(true);
        username.textProperty().addListener((o, oldVal, newVal) -> {
            if(newVal.length() > 0) loginButton.setDisable(false);
            else loginButton.setDisable(true);
        });

        getDialogPane().setContent(grid);
        Platform.runLater(username::requestFocus);

        setResultConverter(button -> {
            if(button.equals(login)) {
                return new Pair<>(username.getText(), password.getText());
            }else return null;
        });
    }
}
