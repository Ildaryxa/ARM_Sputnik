import controller.LoginViewController;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Created by ildar on 16.09.2016.
 */
public class Start extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        new LoginViewController(primaryStage);
    }
}
