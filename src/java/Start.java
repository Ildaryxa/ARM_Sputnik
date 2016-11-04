import controller.AdministrationViewController;
import controller.LoginViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.tableUsersEntity;
import orm.HibernateSessionFactory;

/**
 * Created by ildar on 16.09.2016.
 */
public class Start extends Application {

    private BorderPane rootLayout;

    @Override
    public void start(Stage primaryStage) throws Exception {
        //new LoginViewController(primaryStage);

        /*
        tableUsersEntity user = new tableUsersEntity();
        user.setName("Ильдар");
        user.setSurname("Фасхетдинов");
        HibernateSessionFactory.buildSessionFactory(HibernateSessionFactory.DBMS.MSSQL);
        new AdministrationViewController(primaryStage, user); */

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("view/login.view.fxml"));
        rootLayout = loader.load();

        primaryStage.setTitle("Автосервис 'Спутник' - Вход");
        primaryStage.setScene(new Scene(rootLayout));
        primaryStage.setResizable(false);
        LoginViewController loginController = loader.getController();
        loginController.setStage(primaryStage);
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        HibernateSessionFactory.getSessionFactory().close();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
