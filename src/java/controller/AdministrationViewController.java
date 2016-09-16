package controller;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by ildar on 14.09.2016.
 */
public class AdministrationViewController implements Initializable{

    private static Stage stage;
    private BorderPane rootLayout;


    public AdministrationViewController() {
    }

    public AdministrationViewController(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("view/administration.view.fxml"));
        rootLayout = loader.load();

        stage.setTitle("Автосервис 'Спутник' - Администрирование");
        stage.setResizable(true);
        stage.setScene(new Scene(rootLayout));
        AdministrationViewController.stage = stage;
        AdministrationViewController.stage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
