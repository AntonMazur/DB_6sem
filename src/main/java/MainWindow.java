import entities.Book;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Creator: Mazur Anton
 * Date: 23.02.18
 * Time: 17:43
 **/

public class MainWindow extends Application {

    public static void main(String[] args) throws Exception {
        launch(args);
        return;
    }

    @Override
    public void start(Stage stage) throws Exception {
        Configuration config = new Configuration().configure();
        SessionFactory sessionFactory = config.buildSessionFactory();
        String fxmlFile = "/fxml/sample.fxml";
        FXMLLoader loader = new FXMLLoader();
        Parent root = loader.load(getClass().getResourceAsStream(fxmlFile));
        stage.setTitle("Library(as user)");
        stage.setScene(new Scene(root));
        stage.show();
        return;
    }
}
