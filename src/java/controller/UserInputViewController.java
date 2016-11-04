package controller;

import crypt.HashText;
import crypt.Salt;
import enumType.FormType;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import message.Message;
import model.tableUsersEntity;
import orm.DataBase;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static javafx.scene.paint.Color.*;

/**
 * Created by ildar on 27.06.2016.
 */
public class UserInputViewController implements Initializable{

    private DataBase db;
    private List<String> loginName;
    private String currentLogin;
    private boolean okClicked = false;
    private boolean loginValid = false;

    private FormType frmType;

    @FXML TextField nameField;
    @FXML TextField surnameField;
    @FXML TextField loginField;
    @FXML Label loginLabel;
    @FXML PasswordField passwordField;
    @FXML Label passwordLabel;
    @FXML CheckBox isAdmin;
    @FXML CheckBox isLocked;
    @FXML DatePicker dataRegistration;
    @FXML TextField tfLocation;
    @FXML TextField tfPhoneNumber;
    @FXML TextField tfEmail;
    @FXML Button btnReg;
    @FXML Pane panelAdmin; //id = #PanelAdmin

    private static Stage stage;
    private tableUsersEntity usersEntity;

    public UserInputViewController() {
    }

    public void setDialogStage(Stage dialogStage, FormType frmType){
        stage = dialogStage;
        this.frmType = frmType;
        stage.setOnShown(event -> {
            switch (frmType){
                case Registration:
                    stage.setHeight(stage.getHeight()- panelAdmin.getHeight());
                    panelAdmin.setVisible(false);
                case RegistrationAdmin:
                    btnReg.setText("Зарегистрировать");
                    break;
                case Update:
                    stage.setHeight(stage.getHeight()- panelAdmin.getHeight());
                    panelAdmin.setVisible(false);
                case UpdateAdmin:
                    btnReg.setText("Изменить");
                    //loginField.setEditable(false);
                    break;
            }

        });
    }

    public void setUser(tableUsersEntity user){
        this.usersEntity = user;
        if (frmType == FormType.Update || frmType == FormType.UpdateAdmin){
            nameField.setText(user.getName());
            surnameField.setText(user.getSurname());
            currentLogin = user.getLogin();
            loginField.setText(user.getLogin());
            loginLabel.setText("Ваш текущий логин");
            passwordLabel.setText("Введите новый пароль");
            isAdmin.selectedProperty().setValue(user.isAdmin());
            isLocked.selectedProperty().setValue(user.isLocked());
            dataRegistration.setValue(user.getDataRegistration().toLocalDateTime().toLocalDate());
            tfLocation.setText(user.getLocation());
            tfPhoneNumber.setText(user.getPhoneNumber());
            tfEmail.setText(user.getEmail());
            loginValid = true;
        }
    }

    public boolean isOkClicked(){
        return okClicked;
    }

    public void exit(ActionEvent actionEvent){
        stage.close();
    }

    private void registration(ActionEvent actionEvent){
        if (isInputValid()){
            usersEntity.setName(nameField.getText().trim());
            usersEntity.setSurname(surnameField.getText().trim());
            usersEntity.setLogin(loginField.getText().trim());
            usersEntity.setAdmin(isAdmin.selectedProperty().getValue());
            usersEntity.setLocked(isLocked.selectedProperty().getValue());
            if (dataRegistration.getValue() == null) {
                usersEntity.setDataRegistration(Timestamp.valueOf(LocalDateTime.now()));
            }
            else
                usersEntity.setDataRegistration(Timestamp.valueOf(dataRegistration.getValue().atStartOfDay()));
            String salt = Salt.generator();
            usersEntity.setSalt(salt);
            String password = HashText.getHash(passwordField.getText().trim(), salt);
            usersEntity.setPasswordHash(password);
            usersEntity.setLocation(tfLocation.getText().trim());
            usersEntity.setPhoneNumber(tfPhoneNumber.getText().trim());
            usersEntity.setEmail(tfEmail.getText().trim());
            okClicked = true;
            stage.close();
        }
    }

    private boolean isInputValid() {
        String errorMessage = "";
        if (nameField.getText() == null || nameField.getText().length() == 0) {
            errorMessage += "Введите имя!\n";
        }else if (nameField.getText().length() > 20){
            errorMessage += "Имя: кол-во символов > 20\n";
        }
        if (surnameField.getText() == null || surnameField.getText().length() == 0) {
            errorMessage += "Введите фамилию!\n";
        }else if (surnameField.getText().length() > 20){
            errorMessage += "Фамилия: кол-во символов > 20\n";
        }
        if (loginField.getText() == null || loginField.getText().length() == 0) {
            errorMessage += "Введите логин!\n";
        } else if (!loginValid){
            errorMessage += "Данный логин уже существует!\n";
        } else if (loginField.getText().length() > 20){
            errorMessage += "Логин: кол-во символов > 20\n";
        }
        if (passwordField.getText() == null || passwordField.getText().length() == 0) {
            errorMessage += "Введите пароль!\n";
        }else if (passwordField.getText().length() > 30){
            errorMessage += "Пароль: кол-во символов > 30\n";
        }
        if (tfLocation.getText() == null || tfLocation.getText().length() == 0){
            errorMessage += "Введите местоположение!\n";
        } else if (tfLocation.getText().length() > 50){
            errorMessage += "Местоположение: кол-во символов > 50\n";
        }
        if (tfPhoneNumber.getText() == null || tfPhoneNumber.getText().length() == 0){
            errorMessage += "Введите номер телефона!\n";
        }else if (tfPhoneNumber.getText().length() > 20){
            errorMessage += "Номер телефона: кол-во символов > 20\n";
        }
        if (tfEmail.getText() == null || tfEmail.getText().length() == 0){
            errorMessage += "Введите email!\n";
        }else if (tfEmail.getText().length() > 30){
            errorMessage += "Email: кол-во символов > 30\n";
        }
        return Message.errorMessage(errorMessage, stage);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        db = DataBase.INSTANCE;
        loginName = new ArrayList<>();
        ObservableList<tableUsersEntity> users = db.getUsersData();
        loginName.addAll(users.stream().map(tableUsersEntity::getLogin).collect(Collectors.toList()));
        loginField.textProperty().addListener((observable, oldValue, newValue) -> {
            loginTextChange(newValue);
        });
        btnReg.setOnAction(this::registration);
    }

    private void loginTextChange(String newValue){
        switch (frmType){
            case Registration:
            case RegistrationAdmin:
                correctLogin(newValue);
                break;
            case Update:
            case UpdateAdmin:
                if (newValue.equals(currentLogin)){
                    loginLabel.setText("Ваш текущий логин");
                    loginLabel.setTextFill(BLACK);
                    loginValid = true;
                }else correctLogin(newValue);
                break;
        }
    }

    private void correctLogin(String newValue){
        loginValid = false;
        if (loginName.contains(newValue)) {
            loginLabel.setText("Логин уже занят");
            loginLabel.setTextFill(RED);
        }else if (newValue.isEmpty()){
            loginLabel.setText("Введите логин");
            loginLabel.setTextFill(RED);
        }else{
            loginLabel.setText("Логин свободен");
            loginLabel.setTextFill(GREEN);
            loginValid = true;
        }
    }
}
