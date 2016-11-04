package controller;

import LoaderForm.Form;
import LoaderForm.ShowForm;
import crypt.HashText;
import crypt.Salt;
import enumType.FormType;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import message.Message;
import model.tableUsersEntity;
import orm.DataBase;
import orm.HibernateSessionFactory;

import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * Created by ildar on 16.09.2016.
 */
public class LoginViewController implements Initializable {

    private Stage stage;
    private BorderPane rootLayout;
    DataBase db;

    @FXML ComboBox<HibernateSessionFactory.DBMS> schemeCBox;
    @FXML TextField loginTextField;
    @FXML PasswordField passTextField;

    public LoginViewController() {
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initCBox();
        db = DataBase.INSTANCE;
    }

    private void initCBox() {
        List<HibernateSessionFactory.DBMS> list = new ArrayList<>();
        Collections.addAll(list, HibernateSessionFactory.DBMS.values());
        schemeCBox.setItems(FXCollections.observableArrayList(list));
        schemeCBox.getSelectionModel().select(HibernateSessionFactory.DBMS.MSSQL);
        HibernateSessionFactory.buildSessionFactory(HibernateSessionFactory.DBMS.MSSQL);
        //HibernateSessionFactory.buildSessionFactory(HibernateSessionFactory.DBMS.MYSQL);
        schemeCBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            HibernateSessionFactory.getSessionFactory().close();
            HibernateSessionFactory.buildSessionFactory(newValue);
            System.out.println(newValue);
        });
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
                        openAdministration(user);
                    }else if (user.isLocked()) {
                        Message.errorMessage("К сожалению, вы заблокированы", stage);
                    } else{
                        //вход обычного пользователя
                        openUser(user);
                    }
                }else {
                    new Alert(Alert.AlertType.ERROR, "Логин или пароль введены не правильно", ButtonType.OK).showAndWait();
                }
            }else {
                new Alert(Alert.AlertType.ERROR, "Логин или пароль введены не правильно", ButtonType.OK).showAndWait();
            }
        }

    }

    private void openUser(tableUsersEntity user) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("view/form_User.fxml"));
        rootLayout = loader.load();

        stage.setTitle("Автосервис 'Спутник' Пользователь: " + user.getName() + " " + user.getSurname());
        stage.setResizable(false);
        stage.setScene(new Scene(rootLayout));

        FormUser userController = loader.getController();
        userController.setStage(stage);
        userController.setUser(user);

        stage.show();
    }

    private void openAdministration(tableUsersEntity user) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("view/administration.view.fxml"));
        rootLayout = loader.load();

        stage.setTitle("Автосервис 'Спутник' - Администрирование Вход: " + user.getName() + " " + user.getSurname());
        stage.setResizable(true);
        stage.setScene(new Scene(rootLayout));

        AdministrationViewController adminController = loader.getController();
        adminController.setData(stage, user);

        stage.show();
    }


    public void registration(ActionEvent actionEvent) throws IOException {
        tableUsersEntity user = new tableUsersEntity();
        boolean okClicked = ShowForm.showUserInputView(user, FormType.Registration, "'Спутник' - Регистрация", stage);
        if (okClicked){
            db.addUser(user);
            Message.notification("Регистрация", "Пользователь успешно зарегистрирован", Alert.AlertType.INFORMATION, stage);
        }
    }
/*
    private boolean showUserInputView(tableUsersEntity usersEntity){
        try{
            Form form = new Form();
            UserInputViewController controller = form.getLoader("view/user_input.view.fxml", "'Спутник' - Регистрация",false, stage).getController();
            controller.setDialogStage(form.getStage(), FormType.Registration);
            controller.setUser(usersEntity);
            form.showForm();
            return controller.isOkClicked();
        }catch (IOException ex){
            ex.printStackTrace();
            return false;
        }
    }
*/
    private boolean isValid() {
        //todo добавить проверку логина и пароля
        String errorMessage = "";
        if (loginTextField.getText() == null || loginTextField.getText().length() == 0) {
            errorMessage += "Введите логин!\n";
        }else if (loginTextField.getText().length() > 20){
            errorMessage += "Логин: кол-во символов > 20\n";
        }
        if (passTextField.getText() == null || passTextField.getText().length() == 0) {
            errorMessage += "Введите пароль!\n";
        } else if (passTextField.getText().length() > 20){
            errorMessage += "Пароль: кол-во символов > 20\n";
        }
        return Message.errorMessage(errorMessage, stage);
    }
}
