package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class LogInWindowController implements Resultable{
    private static String password = "qwerty";
    private boolean isSuccessedLogin;

    @FXML
    private PasswordField pwField;

    public void tryLogIn(){
        isSuccessedLogin = pwField.getText().equals("qwerty") ? true : false;
        if (isSuccessedLogin) {
            ((Stage) pwField.getScene().getWindow()).close();
        }
        else {
            new Alert(
                    Alert.AlertType.ERROR,
                    "Invalid password, please try again",
                    ButtonType.CLOSE)
            .show();
            pwField.setBorder(
                    new Border(
                            new BorderStroke(
                                    Color.RED,
                                    BorderStrokeStyle.SOLID,
                                    CornerRadii.EMPTY,
                                    BorderWidths.DEFAULT)));
        }
    }


    @Override
    public Object getResult() {
        return isSuccessedLogin;
    }
}
