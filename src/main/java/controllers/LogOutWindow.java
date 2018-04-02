package controllers;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.Window;


public class LogOutWindow implements Resultable{
    @FXML private ImageView background;
    private boolean isLogOut = false;


    public void initialize(){
        background.setImage(new Image("pictures/kylo-ren.gif"));
    }

    @FXML
    private void logOut(){
        isLogOut = true;
        close();
    }

    @FXML
    private void close(){
        ((Stage) background.getScene().getWindow()).close();
    }


    @Override
    public Object getResult() {
        return isLogOut;
    }
}
