import entities.Book;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.event.ActionEvent;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;


/**
 * Creator: Mazur Anton
 * Date: 25.02.18
 * Time: 17:56
 **/

public class Controller {

    private static String pw = "qwerty";
    private static boolean isLogIn;
    private static Stage mainStage;
    private static Stage openedStage;
    private static int field = 0;
    private static ObservableList<Book> tableData = FXCollections.observableArrayList();
    @FXML private PasswordField pwField;

    @FXML private TableView<Book> libTable;
    @FXML private TableColumn<Book, String> year;
    @FXML private TableColumn<Book, String> authors;
    @FXML private TableColumn<Book, String> name;
    @FXML private TableColumn<Book, String> edition;
    @FXML private TableColumn<Book, String> otherData;



    public static void setMainStage(Stage mainStage){
        Controller.mainStage = mainStage;
    }

    @FXML private void reloadDB(ActionEvent event) throws IOException{
        year.setCellValueFactory(new PropertyValueFactory<>("year"));
        authors.setCellValueFactory(new PropertyValueFactory<>("authors"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        edition.setCellValueFactory(new PropertyValueFactory<>("edition"));
        otherData.setCellValueFactory(new PropertyValueFactory<>("otherData"));
        tableData.add(Model.getBook(++field));
        libTable.setItems(tableData);
    }

    @FXML private void tryLogIn(ActionEvent event){
        if (pwField.getText().equals(pw)){
            isLogIn = true;
            openedStage.close();
            openWindow("/fxml/mainWindowAdmin.fxml", "Library(as administrator)", null);

        }
    }

    @FXML private void logIn(ActionEvent actionEvent) {
        openWindow("/fxml/logWindow.fxml", "Log in", Modality.WINDOW_MODAL);
    }

    private void mockMethod(){

    }

    @FXML private void logOut(ActionEvent actionEvent) {
        if (isLogIn){
            isLogIn = false;
            openWindow("/fxml/mainWindowUser.fxml", "Library(as user)", null);
        }
    }

    @FXML
    private void deleteRecord(ActionEvent actionEvent) {

    }

    @FXML
    private void editRecord(ActionEvent actionEvent) {

    }



    private void openWindow(String pathToFxml, String title, Modality modality){
        FXMLLoader loader = new FXMLLoader();
        Parent root;
        try{
            root = loader.load(getClass().getResourceAsStream(pathToFxml));
        } catch(IOException ex){
            throw new RuntimeException(ex);
        }
        Stage stage = new Stage();
        if (modality == null){
            mainStage.close();
            mainStage = stage;
        }
        else{
            stage.initModality(modality);
            stage.initOwner(mainStage);
        }
        stage.setTitle(title);
        stage.setScene(new Scene(root));
        openedStage = stage;
        stage.show();
    }
}
