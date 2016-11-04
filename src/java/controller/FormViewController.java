package controller;

import enumType.FormType;
import enumType.StatusCar;
import enumType.StatusPayment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import message.Message;
import model.modelOject.AboutDetail;
import model.modelOject.WorkAndDetail;
import model.tableAccountDataEntity;
import model.tableAccountEntity;
import model.tableCommentsEntity;
import orm.DataBase;
import packer.PackerInListWorkAndDetail;

import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * Created by ildar on 10.10.2016.
 */
public class FormViewController implements Initializable {

    private tableAccountDataEntity accountData;
    private static DataBase db;
    private Stage stage;
    private AdministrationViewController main;
    ObservableList<WorkAndDetail> listWorkAndDetail = FXCollections.observableArrayList();
    private tableCommentsEntity comment;
    private FormType type;

    @FXML Label lbNameSurnameUser;
    @FXML Label lbFirmCar;
    @FXML Label lbMarkCar;
    @FXML Label lbNumberCar;
    @FXML Label lbDateRegistration;
    @FXML Label lbConf;

    @FXML Label lbNameUser;
    @FXML Label lbSurnameUser;
    @FXML Label lbLoginUser;
    @FXML Label lbDateRegistrationUser;
    @FXML Label lbLocation;
    @FXML Label lbPhoneNumber;
    @FXML Label lbEmailUser;
    @FXML CheckBox chbLocked;
    @FXML CheckBox chbAdmin;
    @FXML CheckBox chbСarOwnerIssued;

    @FXML Button btnClose;
    @FXML Button btnComment;

    @FXML TableView<WorkAndDetail> tbWork;
    @FXML TableColumn<WorkAndDetail, String> tbcEmployee;
    @FXML TableColumn<WorkAndDetail, String> tbcWork;
    @FXML TableColumn<WorkAndDetail, Integer> tbcPriceWork;

    @FXML TableView<AboutDetail> tbDetail;
    @FXML TableColumn<AboutDetail, String> tbcDetail;
    @FXML TableColumn<AboutDetail, Integer> tbcPriceDetail;
    @FXML TableColumn<AboutDetail, Integer> tbcCount;
    @FXML TableColumn<AboutDetail, String> tbcUnit;

    @FXML TextArea taComment;

    public FormViewController() {
    }

    static {
        db = DataBase.INSTANCE;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {



        tbcEmployee.setCellValueFactory(Cell -> Cell.getValue().employeeProperty());
        tbcWork.setCellValueFactory(Cell -> Cell.getValue().workProperty());
        tbcPriceWork.setCellValueFactory(Cell -> Cell.getValue().priceWorkProperty().asObject());

        tbcDetail.setCellValueFactory(Cell -> Cell.getValue().detailProperty());
        tbcPriceDetail.setCellValueFactory(Cell -> Cell.getValue().priceDetailProperty().asObject());
        tbcCount.setCellValueFactory(Cell -> Cell.getValue().countProperty().asObject());
        tbcUnit.setCellValueFactory(Cell -> Cell.getValue().unitProperty());

        tbWork.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) tbDetail.setItems(newValue.getListDetail());
        });

        btnClose.setOnAction(event -> closeForm());
    }

    private void closeForm() {
        if (chbСarOwnerIssued.isVisible() && chbСarOwnerIssued.isSelected()){
            accountData.setStatusCar(StatusCar.issuedOwner.getStatusValue());
            db.updateAccountData(accountData);
            if (type == FormType.adminView) main.updateListView();
        }
        stage.close();
    }

    void setAccountData(tableAccountDataEntity accountData) {
        this.accountData = accountData;
        initData();
        addWorkAndDetail();
    }

    private void initData() {
        lbNameSurnameUser.setText(accountData.getLoginUser().getName() + " " + accountData.getLoginUser().getSurname());
        lbFirmCar.setText(accountData.getCar().getFirm());
        lbMarkCar.setText(accountData.getCar().getMark());
        lbNumberCar.setText(accountData.getCar().getNumberCar());
        lbDateRegistration.setText(accountData.getDate().toString());
        lbConf.setText(accountData.isConfidentially()?"Защищено":"Для всех");

        lbNameUser.setText(accountData.getLoginUser().getName());
        lbSurnameUser.setText(accountData.getLoginUser().getSurname());
        lbLoginUser.setText(accountData.getLoginUser().getLogin());
        lbDateRegistrationUser.setText(accountData.getLoginUser().getDataRegistration().toString());
        lbLocation.setText(accountData.getLoginUser().getLocation());
        lbPhoneNumber.setText(accountData.getLoginUser().getPhoneNumber());
        lbEmailUser.setText(accountData.getLoginUser().getEmail());

        chbLocked.setSelected(accountData.getLoginUser().isLocked());
        chbAdmin.setSelected(accountData.getLoginUser().isAdmin());

        if (Objects.equals(accountData.getStatusCar(), StatusCar.renovated.getStatusValue()) && type==FormType.adminView){
            chbСarOwnerIssued.setVisible(true);
        }else {
            chbСarOwnerIssued.setVisible(false);
        }

        if (type == FormType.userView){
            btnComment.setVisible(true);
            btnComment.setOnAction(event -> openComment());
            comment = db.getComment(accountData);
            if (comment != null){
                taComment.setText(comment.getText());
            }
        }
    }

    private void openComment() {
        if (taComment.getText() != null && taComment.getText().length() != 0){
            if (comment != null){
                comment.setText(taComment.getText().trim());
                comment.setData(Timestamp.valueOf(LocalDateTime.now()));
                db.updateComment(comment);
                Message.notification("Комментарий", "Комментарий обновлен!", Alert.AlertType.INFORMATION, stage);
            }else {
                comment = new tableCommentsEntity();
                comment.setData(Timestamp.valueOf(LocalDateTime.now()));
                comment.setAccountDataEntity(accountData);
                comment.setText(taComment.getText().trim());
                db.createComment(comment);
                Message.notification("Комментарий", "Комментарий сохранен!", Alert.AlertType.INFORMATION, stage);
            }
        }else {
            Message.errorMessage("Напишите комментарий!", stage);
        }
    }

    private void addWorkAndDetail() {
        PackerInListWorkAndDetail.addWorkAndDetail(db, accountData, listWorkAndDetail);
        if (listWorkAndDetail != null)
            tbWork.setItems(listWorkAndDetail);
    }

    void setStage(Stage stage, FormType type) {
        this.stage = stage;
        this.type = type;
        if (type == FormType.adminView){
            taComment.setVisible(false);

        }
    }

    public void setMain(AdministrationViewController main) {
        this.main = main;
    }
}
