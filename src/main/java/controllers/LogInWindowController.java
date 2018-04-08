package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

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
            CustomAlertController.setAlertParams("Incorrect password! Try again");
            openWindow("customAlert.fxml", "/css/customAlertLight.css", "Error", false);
            pwField.setBorder(
                    new Border(
                            new BorderStroke(
                                    Color.RED,
                                    BorderStrokeStyle.SOLID,
                                    CornerRadii.EMPTY,
                                    BorderWidths.DEFAULT)));
        }
    }

    private Object openWindow(String fxmlFileName, String cssFileName, String title, boolean isResizable){
        FXMLLoader loader = new FXMLLoader();
        Parent root;
        try{
            root = loader.load(getClass().getResourceAsStream("/fxml/" + fxmlFileName));
        } catch(IOException ex){
            throw new RuntimeException(ex);
        }
        Stage stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(pwField.getScene().getWindow());
        stage.setResizable(isResizable);
        stage.setTitle(title);
        stage.setScene(new Scene(root));
        stage.getScene().getStylesheets().add(getClass().getResource(cssFileName).toExternalForm());
        stage.showAndWait();
        return loader.getController();
    }


    @Override
    public Object getResult() {
        return isSuccessedLogin;
    }
}
