package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class CustomAlertController {
    private static String msg;
    @FXML private Label msgText;

    public static void setAlertParams(String msg){
        CustomAlertController.msg = msg;
    }

    @FXML
    private void initialize() {
        msgText.setText(msg);
    }
}
