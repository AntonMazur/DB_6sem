package mainWin;

import controllers.MainWindowController;
import entities.Book;
import javafx.scene.control.TextArea;
import model.Model;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Creator: Mazur Anton
 * Date: 23.02.18
 * Time: 17:43
 **/

public class MainWindow extends Application {
    private static SessionFactory sessionFactory;
    public static ExecutorService parallelTasksExecutor;

    static {
        parallelTasksExecutor = Executors.newFixedThreadPool(4);
    }

    public static Future executeParallel(Runnable runnable){
        return parallelTasksExecutor.submit(runnable);
    }

    public static void main(String[] args) throws Exception {
        launch(args);
        sessionFactory.close();
        return;
    }

    @Override
    public void start(Stage stage) throws Exception {
        executeParallel(Model::init);
        MainWindowController.setMainStage(stage);
        Configuration config = new Configuration().configure();
        sessionFactory = config.buildSessionFactory();
        Model.setSessionFactory(sessionFactory);
        String fxmlFile = "/fxml/mainWindow.fxml";
        FXMLLoader loader = new FXMLLoader();
        Parent root = loader.load(getClass().getResourceAsStream(fxmlFile));
        stage.setTitle("Library (as user)");
        Scene primaryScene = new Scene(root);
        primaryScene.getStylesheets().add
              (getClass().getResource("/css/mainWindowLightSide.css").toExternalForm());
        stage.setScene(primaryScene);
        stage.setMaximized(true);
        stage.show();
        return;
    }
}
