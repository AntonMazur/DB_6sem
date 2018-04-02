package controllers;

import entities.Book;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.control.Button;
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


import java.io.IOException;


/**
 * Creator: Mazur Anton
 * Date: 25.02.18
 * Time: 17:56
 **/

public class MainWindowController {

    private static String pw = "qwerty";
    private static boolean isLogIn;
    private static Stage mainStage;
    private static Stage openedStage;
    private static int field = 0;
    private static ObservableList<Book> tableData = FXCollections.observableArrayList();
    private static String FXML_ROOT_FOLDER = "/fxml/";
    private static final String ADMIN_CSS = "/css/mainWindowDarkSide.css";
    private static final String USER_CSS = "/css/mainWindowLightSide.css";
    @FXML private Label roleLabel;
    @FXML private Button logIn;
    @FXML private Button reloadDB;
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
                    return cell;
                });
        description.setCellValueFactory(new PropertyValueFactory<>("bookDescription"));
        tableData.clear();
        tableData.addAll(Model.getAllBooks());
        libTable.setItems(tableData);
    }


    @FXML
    private void logIn(ActionEvent actionEvent) {
        if (!isLogIn){
            Resultable logWindowController = (Resultable) openWindow("logInWindow.fxml", "Log in", Modality.WINDOW_MODAL, true);
            if ((boolean) logWindowController.getResult()) {
                isLogIn = true;
                logIn.setText("LogOut");
                mainStage.setTitle("Library (as administrator)");
                roleLabel.setText("ADMINISTRATOR");
                ObservableList<String> styleSheets = mainStage.getScene().getStylesheets();
                styleSheets.clear();
                styleSheets.add(getClass().getResource(ADMIN_CSS).toExternalForm());
                addPublication.setVisible(true);
            } else{
                return;
            }
        } else {
            if ((Boolean)((Resultable) openWindow("logOutWindow.fxml", "Confirmation", Modality.WINDOW_MODAL, false)).getResult()){
                logOut();
            }
            else{
                return;
            }
        }
        mainStage.close();
        mainStage.show();
    }

    @FXML
    private void addPublication(ActionEvent event){
        Book addedBook = ((Book)((PublicationCreatorController) openWindow(
                "publicationCreator.fxml",
                "Add new publication",
                Modality.WINDOW_MODAL,
                true)).getResult());
        if (addedBook != null){
            tableData.add(addedBook);
            libTable.refresh();
        }

    }

    @FXML
    private void mockMethod(){
        new Alert(Alert.AlertType.ERROR, "Mazur Anton the best in the world").show();
    }

    private void logOut() {
        isLogIn = false;
        logIn.setText("LogIn");
        mainStage.setTitle("Library (as user)");
        roleLabel.setText("USER");
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
        } catch(IOException ex){
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
        addPublication.managedProperty().bind(addPublication.visibleProperty());
        roleLabel.setText("USER");
    }

    @FXML
    private void showPublicationMenu(MouseEvent event) {
        Book book = libTable.getSelectionModel().getSelectedItem();
        PublicationMenuController.setBook(book);
        openWindow("publicationMenu.fxml", book.getName(), Modality.WINDOW_MODAL, true);
        libTable.refresh();
    }

    private void updateBookTable(){
        libTable.refresh();
    }

}
