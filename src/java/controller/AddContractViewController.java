package controller;

import LoaderForm.Form;
import enumType.FormType;
import enumType.StatusCar;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import message.Message;
import model.modelOject.ListOfCarInRepair;
import model.tableAccountDataEntity;
import model.tableCarEntity;
import model.tableUsersEntity;
import orm.DataBase;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

/**
 * Created by ildar on 03.10.2016.
 */
public class AddContractViewController implements Initializable {

    private static DataBase db;
    ObservableList<tableUsersEntity> users;

    @FXML Label lbNumberContract;
    @FXML ComboBox<String> cmbUsers;
    @FXML TextField tfFirmCar;
    @FXML TextField tfMarkCar;
    @FXML TextField tfNumberCar;
    @FXML DatePicker dtpRegistration;
    @FXML CheckBox chbConfidentially;
    @FXML TextField tfName;
    @FXML TextField tfSurName;
    @FXML TextField tfLogin;
    @FXML TextField tfDataRegistration;
    @FXML CheckBox chbLocked;
    @FXML CheckBox chbAdmin;
    @FXML TextField tfLocation;
    @FXML TextField tfPhoneNumber;
    @FXML TextField tfEmail;
    @FXML Button btnAddUser;
    @FXML Button btnAddContract;
    @FXML Button btnCancel;
    @FXML Button btnAddWork;
    private static Stage stage;
    private boolean okClicked;
    private AdministrationViewController main;
    private ListOfCarInRepair carRepair;
    private FormType type;



    public AddContractViewController() {
        okClicked = false;
    }

    static {
        db = DataBase.INSTANCE;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnAddWork.setVisible(false);
        updateCmbUsers();
        cmbUsers.setOnAction(event -> {
            if (cmbUsers.getSelectionModel().getSelectedIndex()!=-1)
                updateInfoUser(cmbUsers.getSelectionModel().getSelectedIndex());
            else
                updateInfoUser(0);
        });

        dtpRegistration.setValue(LocalDateTime.now().toLocalDate());

        btnCancel.setOnAction(event -> {
            main.updateTableCarRepair();
            stage.close();
        });

        btnAddUser.setOnAction(event -> openAddNewUser());

        btnAddContract.setOnAction(event -> addContract());

        btnAddWork.setOnAction(event -> addWork());
    }

    private void addWork() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("view/add_work.fxml"));
            AnchorPane rootLayout = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(stage);
            dialogStage.setResizable(false);
            dialogStage.setScene(new Scene(rootLayout));
            dialogStage.setTitle("Добавление услуг и деталей");

            AddWorkViewController workViewController = loader.getController();
            workViewController.setStage(dialogStage);
            workViewController.setAccountData(carRepair.getAccountData());

            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addContract() {
        if (isValid()){

            tableCarEntity car = new tableCarEntity();
            car.setFirm(tfFirmCar.getText().trim());
            car.setMark(tfMarkCar.getText().trim());
            car.setNumberCar(tfNumberCar.getText().trim());

            tableAccountDataEntity accountData = new tableAccountDataEntity();
            if (dtpRegistration.getValue() == null){
                accountData.setDate(Timestamp.valueOf(LocalDateTime.now()));
            }else{
                accountData.setDate(Timestamp.valueOf(dtpRegistration.getValue().atStartOfDay()));
            }
            accountData.setCar(car);
            accountData.setLoginUser(db.getUser(tfLogin.getText()));
            accountData.setConfidentially(chbConfidentially.isSelected());

            if (type == FormType.addContract){
                db.addCar(car);
                accountData.setStatusCar(StatusCar.repair.getStatusValue());
                accountData.setStatusPayment(false);
                db.addContract(accountData);
            }else{
                carRepair.getAccountData().setDate(accountData.getDate());
                carRepair.getAccountData().setLoginUser(accountData.getLoginUser());
                carRepair.getAccountData().setConfidentially(accountData.isConfidentially());
                carRepair.getAccountData().getCar().setFirm(car.getFirm());
                carRepair.getAccountData().getCar().setMark(car.getMark());
                carRepair.getAccountData().getCar().setNumberCar(car.getNumberCar());

                db.updateAccountData(carRepair.getAccountData());
            }

            okClicked = true;
            main.updateTableCarRepair();
            stage.close();
        }
    }

    private void openAddNewUser() {
        try {
            Form form = new Form();
            UserViewController controller = form.getLoader("view/users.view.fxml", "Список пользователей автосервиса", false, stage).getController();
            controller.setStage(form.getStage());
            form.showForm();
            updateCmbUsers();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void updateCmbUsers(){
        if (!cmbUsers.getItems().isEmpty())
            cmbUsers.getItems().clear();
        users = db.getUsersData();
        for(tableUsersEntity user : users){
            cmbUsers.getItems().add(user.getName() + " " + user.getSurname());
        }
        cmbUsers.getSelectionModel().selectFirst();
        updateInfoUser(cmbUsers.getSelectionModel().getSelectedIndex());
    }

    private void updateInfoUser(int number){
        tfName.setText(users.get(number).getName());
        tfSurName.setText(users.get(number).getSurname());
        tfLogin.setText(users.get(number).getLogin());
        tfDataRegistration.setText(users.get(number).getDataRegistration().toString());
        chbLocked.setSelected(users.get(number).isLocked());
        chbAdmin.setSelected(users.get(number).isAdmin());
        tfLocation.setText(users.get(number).getLocation());
        tfPhoneNumber.setText(users.get(number).getPhoneNumber());
        tfEmail.setText(users.get(number).getEmail());
    }

    void setStage(Stage dialogStage, FormType type) {
        stage = dialogStage;
        this.type = type;
    }

    private boolean isValid() {
        String errorMessage = "";
        if (tfMarkCar.getText() == null || tfMarkCar.getText().length() == 0){
            errorMessage += "Не указана марка автомобиля!\n";
        }else if (tfMarkCar.getText().length() > 30){
            errorMessage += "Длина марки автомобиля слишком большая!\n";
        }

        if (tfFirmCar.getText() == null || tfFirmCar.getText().length() == 0){
            errorMessage += "Не указана фирма автомобиля!\n";
        }else if (tfFirmCar.getText().length() > 30){
            errorMessage += "Длина фирмы автомобиля слишком большая!\n";
        }

        if (tfNumberCar.getText() == null || tfNumberCar.getText().length() == 0){
            errorMessage += "Не указан гос. номер автомобиля!\n";
        }else if (tfNumberCar.getText().length() > 30){
            errorMessage += "Длина гос. номера автомобиля слишком большая!\n";
        }

        return Message.errorMessage(errorMessage, stage);
    }

    boolean isOkClicked() {
        return okClicked;
    }

    void setMain(AdministrationViewController main) {
        this.main = main;
    }

    void setContract(ListOfCarInRepair list) {
        carRepair = list;
        cmbUsers.setValue(carRepair.getNameUser().get());
        tfFirmCar.setText(carRepair.getAccountData().getCar().getFirm());
        tfMarkCar.setText(carRepair.getAccountData().getCar().getMark());
        tfNumberCar.setText(carRepair.getCarNumber().get());
        dtpRegistration.setValue(carRepair.getDateRegistration().get().toLocalDate());
        chbConfidentially.setSelected(carRepair.getAccountData().isConfidentially());
        btnAddWork.setVisible(true);
        updateInfoUser(cmbUsers.getSelectionModel().getSelectedIndex());
    }
}
