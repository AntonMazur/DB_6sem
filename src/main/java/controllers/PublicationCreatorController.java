package controllers;

import entities.Book;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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
    @FXML private TextField fileLink;

    private Book book = null;

    @FXML
    private void createPublication(ActionEvent event){
        if (!isInteger(year.getText())){
            new Alert(Alert.AlertType.ERROR, "'Year' must be integer!", ButtonType.OK).showAndWait();
            return;
        }

        book = new Book();
        book.setNextId();

        if (!isEmpty(year)){
            book.setYear(Integer.parseInt(year.getText()));
        }

        book.setName(name.getText())
                .setEdition(edition.getText())
                .setAuthors(authors.getText())
                .setOtherData(outputData.getText())
                .setDOI(DOI.getText())
                .setWOS(WOS.getText())
                .setSCOPULUS(SCOPULUS.getText())
                .setFileLink(isEmpty(fileLink) ? null : fileLink.getText())
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

    @Override
    public Object getResult() {
        return book;
    }
}
