package controllers;

import entities.Book;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;


public class PublicationMenuController implements Resultable{
    @FXML private TextArea bookDescription;
    private static Book book;
    private Boolean isDescriptionEdited;

    public static void setBook(Book book) {
        PublicationMenuController.book = book;
    }

    @FXML
    private void updatePublicationDescription(Event event){
        book.setBookDescription(bookDescription.getText());
        isDescriptionEdited = true;
    }

    @FXML
    private void initialize(){
        bookDescription.setText(book.getBookDescription());

    }

    @Override
    public Object getResult() {
        return null;
    }
}
