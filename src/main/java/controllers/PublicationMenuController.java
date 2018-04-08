package controllers;

import entities.Book;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import utilities.PublicationAction;



public class PublicationMenuController implements Resultable{
    @FXML private TextField year;
    @FXML private TextField authors;
    @FXML private TextField name;
    @FXML private TextField edition;
    @FXML private TextField outputData;
    @FXML private TextField indexes;
    @FXML private TextField fileLink;
    private static Book book;
    private PublicationAction pubAction = PublicationAction.NOTHING;

    public static void setBook(Book book) {
        PublicationMenuController.book = book;
    }

    @FXML
    private void updatePublicationDescription(Event event){

        String[] idxs = indexes.getText().split("\\|");
        book.updateBook(
                (year.getText()).isEmpty() ? 0 : Integer.parseInt(year.getText()),
                authors.getText(),
                name.getText(),
                edition.getText(),
                outputData.getText(),
                "",
                "",
                "",
                fileLink.getText());
        pubAction = PublicationAction.UPDATE;
        close();
    }

    @FXML
    private void initialize(){
        year.setText(Integer.toString(book.getYear()));
        authors.setText(book.getAuthors());
        name.setText(book.getName());
        edition.setText(book.getEdition());
        outputData.setText(book.getOutputData());
        indexes.setText(book.getDOI() + "|" + book.getSCOPULUS() + "|" + book.getWOS());
        fileLink.setText(book.getFileLink());
    }

    @FXML
    private void deletePublication(){
        pubAction = PublicationAction.DELETE;
        close();
    }

    @FXML
    private void openPublication(){
        book.openPublicationIfExist();
    }

    private void close() {
        ((Stage) year.getScene().getWindow()).close();
    }

    @Override
    public Object getResult() {
        return pubAction;
    }
}
