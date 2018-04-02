package controllers;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class CustomAlert extends Alert{

    public CustomAlert(boolean isDarkSide,  AlertType alertType, String message, ButtonType... buttons){
        super(alertType, message, buttons);
    }
}
