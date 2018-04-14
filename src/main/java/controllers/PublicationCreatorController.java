package controllers;

import entities.Book;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;


public class PublicationCreatorController implements Resultable{
    @FXML private TextField year;
    @FXML private TextField authors;
    @FXML private TextField name;
    @FXML private TextField edition;
    @FXML private TextField outputData;
    @FXML private TextField WOS;
    @FXML private TextField SCOPULUS;
    @FXML private TextField DOI;
    private String fileLink = "";

    private Book book = null;

    @FXML
    private void createPublication(ActionEvent event){
        if (!isInteger(year.getText())){
            CustomAlertController.setAlertParams("'Год' должно быть целочисленным");
            openWindow("customAlert.fxml", "/css/customAlertDark.css", "Ошибка", false);

            return;
        }

        book = new Book();

        if (!isEmpty(year)){
            book.setYear(Integer.parseInt(year.getText()));
        }

        book.setName(name.getText())
                .setEdition(edition.getText())
                .setAuthors(authors.getText())
                .setOutputData(outputData.getText())
                .setDOI(DOI.getText())
                .setWOS(WOS.getText())
                .setSCOPULUS(SCOPULUS.getText())
                .setFileLink(fileLink)
                .updateBookDescription();
        close();
    }

    private void close(){
        ((Stage) year.getScene().getWindow()).close();
    }

    private boolean isEmpty(TextField textField){
        return textField.getText().trim().isEmpty();
    }

    private boolean isInteger(String token){
        if (token.isEmpty()){
            return true;
        }
        Pattern intDetection = Pattern.compile("^\\s*\\d*\\s*$");
        return intDetection.matcher(token).matches();
    }

    @FXML
    private void addFile(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF-files","*.pdf"));
        File file = fileChooser.showOpenDialog(year.getScene().getWindow());
        if (file != null) {
            fileLink = file.getPath();
        }
    }

    @Override
    public Object getResult() {
        return book;
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
        stage.initOwner(year.getScene().getWindow());
        stage.setResizable(isResizable);
        stage.setTitle(title);
        stage.setScene(new Scene(root));
        stage.getScene().getStylesheets().add(getClass().getResource(cssFileName).toExternalForm());
        stage.showAndWait();
        return loader.getController();
    }
}
