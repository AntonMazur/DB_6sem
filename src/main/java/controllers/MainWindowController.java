package controllers;

import entities.Book;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.Reflection;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import lombok.SneakyThrows;
import model.Model;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import utilities.PublicationAction;


import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Creator: Mazur Anton
 * Date: 25.02.18
 * Time: 17:56
 **/

public class MainWindowController {

    private static String pw = "qwerty";
    private static boolean isLogIn;
    private static Stage mainStage;
    private static ObservableList<Book> tableData = FXCollections.observableArrayList();
    private static List<Book> allBooks = new ArrayList<>();
    private static String FXML_ROOT_FOLDER = "/fxml/";
    private static final String ADMIN_CSS = "/css/mainWindowDarkSide.css";
    private static final String USER_CSS = "/css/mainWindowLightSide.css";
    @FXML private Label roleLabel;
    @FXML private Button logIn;
    @FXML private Button reloadDB;
    @FXML private Button findBooks;
    @FXML private Button addPublication;

    @FXML private TableView<Book> libTable;
    @FXML private TableColumn<Book, String> id;
    @FXML private TableColumn<Book, String> description;


    public static void setMainStage(Stage mainStage){
        MainWindowController.mainStage = mainStage;
    }

    @FXML
    private void reloadDB(ActionEvent event) {
        Book.resetCounter();
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        description.setCellFactory(tc -> {
                    TableCell<Book, String> cell = new TableCell<>();
                    Text text = new Text();
                    text.setFill(isLogIn ? Color.WHITE : Color.BLACK);
                    cell.setGraphic(text);
                    cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
                    text.wrappingWidthProperty().bind(description.widthProperty());
                    text.textProperty().bind(cell.itemProperty());
                    return cell; });
        description.setCellValueFactory(new PropertyValueFactory<>("bookDescription"));
        tableData.clear();
        allBooks.clear();
        allBooks.addAll(Arrays.asList(Model.getAllBooks()));
        tableData.addAll(allBooks);
        libTable.setItems(tableData);
    }


    @FXML
    private void logIn(ActionEvent actionEvent) {
        if (!isLogIn){
            Resultable logWindowController = (Resultable) openWindow("logInWindow.fxml", "Log in", Modality.WINDOW_MODAL, false);
            if ((boolean) logWindowController.getResult()) {
                isLogIn = true;
                logIn.setText("Выйти");
                mainStage.setTitle("Библиотека (режим администратора)");
                roleLabel.setText("АДМИНИСТРАТОР");
                ObservableList<String> styleSheets = mainStage.getScene().getStylesheets();
                styleSheets.clear();
                styleSheets.add(getClass().getResource(ADMIN_CSS).toExternalForm());
                addPublication.setVisible(true);
            } else{
                return;
            }
        } else {
            if ((Boolean)((Resultable) openWindow("logOutWindow.fxml", "Подтверждение", Modality.WINDOW_MODAL, true)).getResult()){
                logOut();
            }
            else{
                return;
            }
        }
        mainStage.close();
        mainStage.show();
        updateBookTable();
    }

    @FXML
    private void addPublication(ActionEvent event){
        Book addedBook = ((Book)((PublicationCreatorController) openWindow(
                "publicationCreator.fxml",
                "Добавление публикации",
                Modality.WINDOW_MODAL,
                false)).getResult());
        if (addedBook != null){
            Model.insertBook(addedBook);
            tableData.add(addedBook);
            libTable.refresh();
        }

    }

    private void logOut() {
        isLogIn = false;
        logIn.setText("Войти");
        mainStage.setTitle("Бибилиотека (режим пользователя)");
        roleLabel.setText("ПОЛЬЗОВАТЕЛЬ");
        ObservableList<String> styleSheets = mainStage.getScene().getStylesheets();
        styleSheets.clear();
        styleSheets.add(getClass().getResource(USER_CSS).toExternalForm());
        addPublication.visibleProperty().setValue(false);
    }

    private static void applyReflectionToUINode(Node node) {
        Reflection reflection = new Reflection();
        reflection.setFraction(0.7);
        node.setEffect(reflection);
    }

    private Object openWindow(String fxmlFileName, String title, Modality modality, boolean isResizable){
        FXMLLoader loader = new FXMLLoader();
        Parent root;
        try{
            root = loader.load(getClass().getResourceAsStream(FXML_ROOT_FOLDER + fxmlFileName));
        } catch(IOException ex) {
            throw new RuntimeException(ex);
        }
        Stage stage = new Stage();
        if (modality != null) {
            stage.initModality(modality);
            stage.initOwner(mainStage);
        }
        stage.setResizable(isResizable);
        stage.setTitle(title);
        stage.setScene(new Scene(root));
        stage.showAndWait();
        return loader.getController();
    }

    private Object openWindow(String fxmlFileName, String cssFileName, String title, Modality modality, boolean isResizable){
        FXMLLoader loader = new FXMLLoader();
        Parent root;
        try{
            root = loader.load(getClass().getResourceAsStream(FXML_ROOT_FOLDER + fxmlFileName));
        } catch(IOException ex) {
            throw new RuntimeException(ex);
        }
        Stage stage = new Stage();
        if (modality != null){
            stage.initModality(modality);
            stage.initOwner(mainStage);
        }
        stage.setResizable(isResizable);
        stage.setTitle(title);
        stage.setScene(new Scene(root));
        stage.getScene().getStylesheets().add(getClass().getResource(cssFileName).toExternalForm());
        stage.showAndWait();
        return loader.getController();
    }

    @FXML
    private void initialize() {
        reloadDB(null);
        applyReflectionToUINode(reloadDB);
        applyReflectionToUINode(logIn);
        applyReflectionToUINode(roleLabel);
        applyReflectionToUINode(addPublication);
        applyReflectionToUINode(findBooks);
        addPublication.managedProperty().bind(addPublication.visibleProperty());
        roleLabel.setText("ПОЛЬЗОВАТЕЛЬ");
    }

    @FXML
    private void showPublicationMenu(MouseEvent event) {
        Book book = libTable.getSelectionModel().getSelectedItem();
        if (event.getClickCount() == 2) {
            if (isLogIn) {
                PublicationMenuController.setBook(book);
                PublicationAction pubAction =(PublicationAction)((PublicationMenuController) openWindow("publicationMenu.fxml", book.getName(), Modality.WINDOW_MODAL, true)).getResult();
                if (pubAction == PublicationAction.UPDATE) {
                    Model.updateBook(book);
                    updateBookTable();
                }
                if (pubAction == PublicationAction.DELETE) {
                    Model.deleteBook(book);
                    tableData.remove(book);
                    updateBookTable();
                }
            }
            else {
                book.openPublicationIfExist();
            }
        }
        else{
            Toolkit
                    .getDefaultToolkit()
                    .getSystemClipboard()
                    .setContents(
                            new StringSelection(book.getBookDescription()),
                            null);
        }

    }

    @FXML
    private void findBooks(){
        SearchWindowController.setInitialBooks(allBooks.stream());
        Book[] result = (Book[]) ((Resultable) openWindow(
                "searchWindow.fxml",
                isLogIn ? "/css/searchWindowDark.css" : "/css/searchWindowLight.css",
                "Поиск",
                Modality.APPLICATION_MODAL,
                false)).getResult();
        if (result == null) {
            return;
        }
        tableData.clear();
        tableData.addAll(result);
        updateBookTable();
    }

    private void updateBookTable(){
        libTable.refresh();
    }

}
