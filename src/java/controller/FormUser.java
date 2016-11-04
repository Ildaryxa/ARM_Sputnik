package controller;

import LoaderForm.Form;
import LoaderForm.ShowForm;
import enumType.FormType;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
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
import model.tableAccountDataEntity;
import model.tableUsersEntity;
import orm.DataBase;
import packer.Clone;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * Created by ildar on 10.10.2016.
 */
public class FormUser implements Initializable {

    @FXML Label lbSurname;
    @FXML Label lbName;
    @FXML Label lbLogin;
    @FXML Label lbDataRegistration;

    @FXML MenuBar mbUser;

    @FXML TableView<tableAccountDataEntity> tbContract;
    @FXML TableColumn<tableAccountDataEntity, String> tbcNameCar;
    @FXML TableColumn<tableAccountDataEntity, String> tbcDataRegistration;
    @FXML TableColumn<tableAccountDataEntity, String> tbcStatusCar;
    @FXML TableColumn<tableAccountDataEntity, String> tbcStatusPay;

    @FXML Button btnCancel;

    private static DataBase db;
    private Stage stage;
    private tableUsersEntity user;
    private ObservableList<tableAccountDataEntity> listAccount;

    public FormUser() {
    }

    static {
        db = DataBase.INSTANCE;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initMenu();
    }

    private void initMenu() {

        mbUser.getMenus().clear();

        Menu controlUser = new Menu("Управление кабинетом");
        MenuItem miChangeUser = new MenuItem("Изменение данных");
            miChangeUser.setOnAction(event -> changeUser());

        controlUser.getItems().add(miChangeUser);
        mbUser.getMenus().add(controlUser);

        tbcNameCar.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getCar().getFirm() + " " + cell.getValue().getCar().getMark()));
        tbcDataRegistration.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getDate().toString()));
        tbcStatusCar.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getStatusCar()));
        tbcStatusPay.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().isStatusPayment()?"Оплачено":"Не оплачено"));

        btnCancel.setOnAction(event -> stage.close());

        tbContract.setRowFactory(param -> {
            TableRow<tableAccountDataEntity> row = new TableRow<tableAccountDataEntity>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount()==2 && (!row.isEmpty())){
                    contractView();
                }
            });
            return row;
        });
    }

    private void contractView() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("view/formView.fxml"));
            AnchorPane rootLayout = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(stage);
            dialogStage.setResizable(false);
            dialogStage.setScene(new Scene(rootLayout));
            dialogStage.setTitle("Просмотр контракта");

            FormViewController viewContractController = loader.getController();
            viewContractController.setStage(dialogStage, FormType.userView);
            viewContractController.setAccountData(tbContract.getSelectionModel().getSelectedItem());

            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void changeUser() {
        tableUsersEntity userOld = Clone.cloneUsers(user);
        if (ShowForm.showUserInputView(user, FormType.Update, "'Спутник' - Изменение пользователя", stage)){
            if (Objects.equals(user.getLogin(), userOld.getLogin()))
                db.changeUser(user);
            else
                db.changeUser(user, userOld);
            Message.notification("Изменение пользователя", "Пользователь успешно изменен", Alert.AlertType.INFORMATION, stage);
            updateInfo(user);
        }else{
            Message.errorMessage("Произошла ошибка при изменении!", stage);
        }
    }



    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setUser(tableUsersEntity user) {
        this.user = user;
        updateInfo(user);
        listAccount = db.getAccountDataForUser(user);
        tbContract.setItems(listAccount);
    }

    private void updateInfo(tableUsersEntity user){
        lbName.setText(user.getName());
        lbSurname.setText(user.getSurname());
        lbLogin.setText(user.getLogin());
        lbDataRegistration.setText(user.getDataRegistration().toString());
    }
}
