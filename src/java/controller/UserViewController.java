package controller;

import LoaderForm.Form;
import LoaderForm.ShowForm;
import enumType.FormType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import message.Message;
import model.tableUsersEntity;
import orm.DataBase;
import packer.Clone;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * Created by ildar on 02.10.2016.
 */
public class UserViewController implements Initializable {

    private static DataBase db;
    private static Stage stage;

    @FXML TableView<tableUsersEntity> tbUsers;
    @FXML TableColumn<tableUsersEntity, Integer> tbcId;
    @FXML TableColumn<tableUsersEntity, String> tbcName;
    @FXML TableColumn<tableUsersEntity, String> tbcSurName;
    @FXML TableColumn<tableUsersEntity, String> tbcLogin;
    @FXML TableColumn<tableUsersEntity, Boolean> tbcIsAdmin;
    @FXML TableColumn<tableUsersEntity, Boolean> tbcLocked;
    @FXML TableColumn<tableUsersEntity, String> tbcDataRegistration;
    @FXML TableColumn<tableUsersEntity, String> tbcLocation;
    @FXML TableColumn<tableUsersEntity, String> tbcPhoneNumber;
    @FXML TableColumn<tableUsersEntity, String> tbcEmail;


    public UserViewController() {
    }

    static {
        db = DataBase.INSTANCE;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tbcId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tbcName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tbcSurName.setCellValueFactory(new PropertyValueFactory<>("surname"));
        tbcLogin.setCellValueFactory(new PropertyValueFactory<>("login"));
        tbcIsAdmin.setCellValueFactory(new PropertyValueFactory<>("admin"));
        tbcLocked.setCellValueFactory(new PropertyValueFactory<>("locked"));
        tbcDataRegistration.setCellValueFactory(new PropertyValueFactory<>("dataRegistration"));
        tbcLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
        tbcPhoneNumber.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        tbcEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        tbcId.setVisible(false);

        ContextMenu ctMenu = new ContextMenu();
        MenuItem miAddUser = new MenuItem("Добавить нового пользователя");
            miAddUser.setOnAction(event -> addUser());
        MenuItem miChangeUser = new MenuItem("Изменить пользователя");
            miChangeUser.setOnAction(event -> {
                if (!tbUsers.getSelectionModel().isEmpty()){
                    changeUser();
                }
            });
        /*
        MenuItem miDeleteUser = new MenuItem("Удалить пользователя");
            miDeleteUser.setOnAction(event -> {
                if (!tbUsers.getSelectionModel().isEmpty()){
                    deleteUser();
                }
            });
        */
        miAddUser.setOnAction(event -> addUser());

        ctMenu.getItems().addAll(miAddUser, miChangeUser); //, miDeleteUser
        tbUsers.setItems(db.getUsersData());
        tbUsers.setContextMenu(ctMenu);
    }

    private void addUser(){
        tableUsersEntity user = new tableUsersEntity();
        if (ShowForm.showUserInputView(user, FormType.RegistrationAdmin, "'Спутник' - Регистрация", stage)){
            db.addUser(user);
            updateTableView();
            Message.notification("Регистрация", "Пользователь успешно зарегистрирован", Alert.AlertType.INFORMATION, stage);
        }
    }

    private void changeUser(){
        int number = tbUsers.getSelectionModel().getSelectedIndex();
        tableUsersEntity user = db.getUser(tbcLogin.getCellData(number));
        tableUsersEntity userOld = Clone.cloneUsers(user);
        if (ShowForm.showUserInputView(user, FormType.UpdateAdmin, "'Спутник' - Изменение пользователя", stage)){
            if (Objects.equals(user.getLogin(), userOld.getLogin()))
                db.changeUser(user);
            else
                db.changeUser(user, userOld);
            updateTableView();
            Message.notification("Изменение пользователя", "Пользователь успешно изменен", Alert.AlertType.INFORMATION, stage);
        }
    }

    private void deleteUser(){
        int number = tbUsers.getSelectionModel().getSelectedIndex();
        tableUsersEntity user = db.getUser(tbcLogin.getCellData(number));
        db.deleteUser(user);
        updateTableView();
        Message.notification("Удаление пользователя", "Пользователь успешно удален", Alert.AlertType.INFORMATION, stage);
    }
/*
    private boolean showUserInputView(tableUsersEntity user, FormType type, String title){
        try {
            Form form = new Form();
            UserInputViewController controller = form.getLoader("view/user_input.view.fxml", title, false, stage).getController();
            controller.setDialogStage(form.getStage(), type);
            controller.setUser(user);
            form.showForm();
            return controller.isOkClicked();
        }catch (IOException ex){
            ex.printStackTrace();
            return false;
        }
    }
*/
    private void updateTableView(){
        tbUsers.getItems().clear();
        tbUsers.setItems(db.getUsersData());
    }

    public void setStage(Stage dialogStage){
        stage = dialogStage;
    }
}
