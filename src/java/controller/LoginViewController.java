package controller;

import crypt.HashText;
import enumType.FormType;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import message.Message;
import model.tableUsersEntity;
import orm.DataBase;
import orm.HibernateSessionFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Created by ildar on 16.09.2016.
 */
public class LoginViewController implements Initializable {

    private static Stage stage;
    private BorderPane rootLayout;
    DataBase db;

    @FXML ComboBox<HibernateSessionFactory.DBMS> schemeCBox;
    @FXML TextField loginTextField;
    @FXML PasswordField passTextField;

    public LoginViewController() {
    }

    public LoginViewController(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("view/login.view.fxml"));
        //loader.setLocation(MainApp.class.getResource("../View/PersonEditDialog.fxml"));
        rootLayout = loader.load();

        stage.setTitle("Автосервис 'Спутник' - Вход");
        stage.setScene(new Scene(rootLayout));
        LoginViewController.stage = stage;
        LoginViewController.stage.setResizable(false);
        LoginViewController.stage.show();
        initMenuBar();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initCBox();
        db = DataBase.INSTANCE;
    }

    private void initMenuBar() {
        MenuBar menuBar = (MenuBar) rootLayout.getTop();
        menuBar.getMenus().clear();
        Menu about = new Menu("О компании");
        about.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

            }
        });
        Menu reviews = new Menu("Отзывы");
        reviews.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

            }
        });
        menuBar.getMenus().addAll(about, reviews);
    }

    private void initCBox() {
        List<HibernateSessionFactory.DBMS> list = new ArrayList<>();
        Collections.addAll(list, HibernateSessionFactory.DBMS.values());
        schemeCBox.setItems(FXCollections.observableArrayList(list));
        schemeCBox.getSelectionModel().select(HibernateSessionFactory.DBMS.MSSQL);
        HibernateSessionFactory.buildSessionFactory(HibernateSessionFactory.DBMS.MSSQL);
        /*todo позже открыть, когда добавим еще одну БД
        schemeCBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            HibernateSessionFactory.buildSessionFactory(newValue);
            System.out.println(newValue);
        });*/
    }

    public void exit(ActionEvent actionEvent){
        System.exit(0);
    }

    public void authorization(ActionEvent actionEvent) throws IOException {
        if (isValid()){
            String login = loginTextField.getText().trim();
            String password = passTextField.getText().trim();
            tableUsersEntity user = db.getUser(login);
            if (user != null){
                String passHash = HashText.getHash(password, user.getSalt());
                if (Objects.equals(passHash, user.getPasswordHash())){
                    //вход админки или пользовательской части
                    if (user.isAdmin()){
                        new AdministrationViewController(stage);
                    }else {

                    }
                }else {
                    new Alert(Alert.AlertType.ERROR, "Логин или пароль введены не правильно", ButtonType.OK).showAndWait();
                }
            }else {
                new Alert(Alert.AlertType.ERROR, "Логин или пароль введены не правильно", ButtonType.OK).showAndWait();
            }
        }

    }


    public void registration(ActionEvent actionEvent) throws IOException {
        tableUsersEntity user = new tableUsersEntity();
        user.setDataRegistration(Timestamp.valueOf(LocalDateTime.now()));
        boolean okClicked = showUserInputView(user);
        if (okClicked){
            /*
            System.out.println(user.getName());
            System.out.println(user.getSurname());
            System.out.println(user.getLogin());
            System.out.println(user.getSalt());
            System.out.println(user.getPasswordHash());
            System.out.println(user.getDataRegistration());
            System.out.println(user.isAdmin());
            System.out.println(user.isLocked());
            */
            db.addUser(user);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.initOwner(stage);
            alert.setTitle("Регистрация");
            alert.setHeaderText("Пользователь успешно зарегистрирован");
            alert.showAndWait();
            //new Alert(Alert.AlertType.INFORMATION, "", ButtonType.OK).showAndWait();
        }
    }

    private boolean showUserInputView(tableUsersEntity usersEntity){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("view/user_input.view.fxml"));
            AnchorPane rootLayout = loader.load();

            //stage
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(stage);
            dialogStage.setResizable(false);
            dialogStage.setScene(new Scene(rootLayout));
            dialogStage.setTitle("'Спутник' - Регистрация");

            //create controller
            UserInputViewController controller = loader.getController();
            controller.setDialogStage(dialogStage, FormType.Registration);
            controller.setUser(usersEntity);
            dialogStage.showAndWait();
            return controller.isOkClicked();
        }catch (IOException ex){
            ex.printStackTrace();
            return false;
        }
    }

    private boolean isValid() {
        //todo добавить проверку логина и пароля
        String errorMessage = "";
        if (loginTextField.getText() == null || loginTextField.getText().length() == 0) {
            errorMessage += "Введите логин!\n";
        }
        if (passTextField.getText() == null || passTextField.getText().length() == 0) {
            errorMessage += "Введите пароль!\n";
        }
        return Message.errorMessage(errorMessage, stage);
    }
}
