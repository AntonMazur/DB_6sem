import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.event.ActionEvent;

import java.io.IOException;


/**
 * Creator: Mazur Anton
 * Date: 25.02.18
 * Time: 17:56
 **/

public class Controller {

    @FXML
    private void reloadDB(ActionEvent event) throws IOException{
        Alert alert = new Alert(Alert.AlertType.INFORMATION, Model.getRow().toString());
        alert.show();
    }

}
