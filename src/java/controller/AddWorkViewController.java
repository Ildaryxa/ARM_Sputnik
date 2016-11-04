package controller;

import enumType.StatusCar;
import enumType.StatusPayment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import message.Message;
import model.*;
import model.modelOject.AboutDetail;
import model.modelOject.WorkAndDetail;
import orm.DataBase;
import packer.PackerInListWorkAndDetail;

import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Created by ildar on 08.10.2016.
 */
public class AddWorkViewController implements Initializable {

    private static DataBase db;
    private Stage stage;
    private tableAccountDataEntity accountData;
    ObservableList<tableEmployeeEntity> employees;
    ObservableList<tableWorkEntity> works;
    ObservableList<tableDetailEntity> details;
    ObservableList<WorkAndDetail> listWorkAndDetail = FXCollections.observableArrayList();

    @FXML TableView<WorkAndDetail> tbWork;
    @FXML TableColumn<WorkAndDetail, String> tbcEmployee;
    @FXML TableColumn<WorkAndDetail, String> tbcWork;
    @FXML TableColumn<WorkAndDetail, Integer> tbcPriceWork;

    @FXML TableView<AboutDetail> tbDetail;
    @FXML TableColumn<AboutDetail, String> tbcDetail;
    @FXML TableColumn<AboutDetail, Integer> tbcPriceDetail;
    @FXML TableColumn<AboutDetail, Integer> tbcCount;
    @FXML TableColumn<AboutDetail, String> tbcUnit;

    @FXML ComboBox<String> cmbEmployee;
    @FXML ComboBox<String> cmbWork;
    @FXML ComboBox<String> cmbDetail;
    @FXML ComboBox<Integer> cmbCount;
    @FXML ComboBox<String> cmbStatusCar;
    @FXML ComboBox<String> cmbStatusPay;

    @FXML Button btnAccept;
    @FXML Button btnCancel;
    @FXML Button btnAdd;

    static {
        db = DataBase.INSTANCE;
    }

    public AddWorkViewController() {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        employees = db.getEmployees();
        works = db.getWorks();
        details = db.getDetails();

        ObservableList<String> statusCars = FXCollections.observableArrayList();
        for(StatusCar s : StatusCar.values()){
            statusCars.add(s.getStatusValue());
        }

        cmbStatusCar.setItems(statusCars);
        cmbStatusCar.setValue(StatusCar.repair.getStatusValue());

        ObservableList<String> statusPay = FXCollections.observableArrayList();
        for(StatusPayment s: StatusPayment.values()){
            statusPay.add(s.getStatusPaymentValue());
        }

        cmbStatusPay.setItems(statusPay);
        cmbStatusPay.setValue(StatusPayment.isNotPaid.getStatusPaymentValue());
        cmbStatusPay.setVisible(false);

        for (tableEmployeeEntity employee : employees){
            cmbEmployee.getItems().add(employee.getName() + " " + employee.getSurname());
        }
        cmbEmployee.getSelectionModel().selectFirst();
        for (tableWorkEntity work : works){
            cmbWork.getItems().add(work.getNameWork());
        }
        cmbWork.getSelectionModel().selectFirst();
        cmbDetail.getItems().add("-");
        for (tableDetailEntity detail : details){
            cmbDetail.getItems().add(detail.getNameDetail());
        }
        cmbDetail.getSelectionModel().selectFirst();
        cmbCount.getItems().addAll(0,1,2,3,4,5,6,7,8,9,10);
        cmbCount.getSelectionModel().selectFirst();

        tbcEmployee.setCellValueFactory(Cell -> Cell.getValue().employeeProperty());
        tbcWork.setCellValueFactory(Cell -> Cell.getValue().workProperty());
        tbcPriceWork.setCellValueFactory(Cell -> Cell.getValue().priceWorkProperty().asObject());

        tbcDetail.setCellValueFactory(Cell -> Cell.getValue().detailProperty());
        tbcPriceDetail.setCellValueFactory(Cell -> Cell.getValue().priceDetailProperty().asObject());
        tbcCount.setCellValueFactory(Cell -> Cell.getValue().countProperty().asObject());
        tbcUnit.setCellValueFactory(Cell -> Cell.getValue().unitProperty());

        ContextMenu ctMenu = new ContextMenu();
        MenuItem miDelete = new MenuItem("Удалить запись");
            miDelete.setOnAction(event -> deleteRecordInTable());

        ctMenu.getItems().add(miDelete);

        tbWork.setContextMenu(ctMenu);
        tbDetail.setContextMenu(ctMenu);

        btnAdd.setOnAction(event -> addWorkAndDetail());

        btnCancel.setOnAction(event -> stage.close());

        btnAccept.setOnAction(event -> accept());

        tbWork.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) tbDetail.setItems(newValue.getListDetail());
        });

        cmbStatusCar.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (!Objects.equals(newValue, StatusCar.repair.getStatusValue())){
                cmbStatusPay.setVisible(true);
            }else {
                cmbStatusPay.setValue(StatusPayment.isNotPaid.getStatusPaymentValue());
                cmbStatusPay.setVisible(false);
            }
        });
    }

    private void accept() {
        if (Objects.equals(cmbStatusPay.getValue(), StatusPayment.paid.getStatusPaymentValue())){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.initOwner(stage);
            alert.setTitle("Подтверждение договора");
            alert.setHeaderText("Машина отремонтирована и оплачена,\n после этого действия редактировать договор будет невозможно!");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                accountData.setStatusPayment(Objects.equals(cmbStatusPay.getValue(), StatusPayment.paid.getStatusPaymentValue()));
                accountData.setStatusCar(cmbStatusCar.getValue());
                db.updateAccountData(accountData);
                stage.close();
            }
        } else {
            accountData.setStatusPayment(Objects.equals(cmbStatusPay.getValue(), StatusPayment.paid.getStatusPaymentValue()));
            accountData.setStatusCar(cmbStatusCar.getValue());
            db.updateAccountData(accountData);
            stage.close();
        }
    }

    private void deleteRecordInTable() {
        if (tbWork.isFocused()){
            //выделение на таблице с работой
            if (!tbWork.getSelectionModel().isEmpty()){
                WorkAndDetail workAndDetail = tbWork.getSelectionModel().getSelectedItem();
                if (!workAndDetail.getListDetail().isEmpty()){
                    for (AboutDetail aboutDetail:workAndDetail.getListDetail()){
                        deleteToTheDataBase(aboutDetail.getId(), workAndDetail.getWorkEntity(), workAndDetail.getEmployeeEntity(), aboutDetail.getDetailTableEntity(), aboutDetail.countProperty().get());
                    }
                }else{
                    deleteToTheDataBase(workAndDetail.getId(), workAndDetail.getWorkEntity(), workAndDetail.getEmployeeEntity(), null, null);
                }
                listWorkAndDetail.remove(workAndDetail);
            }
        }else{
            //выделение на таблице с деталями
            if (!tbDetail.getSelectionModel().isEmpty()){
                WorkAndDetail workAndDetail = tbWork.getSelectionModel().getSelectedItem();
                AboutDetail aboutDetail = tbDetail.getSelectionModel().getSelectedItem();
                deleteToTheDataBase(aboutDetail.getId(), workAndDetail.getWorkEntity(), workAndDetail.getEmployeeEntity(), aboutDetail.getDetailTableEntity(), aboutDetail.countProperty().get());
                listWorkAndDetail.get(listWorkAndDetail.indexOf(workAndDetail)).getListDetail().remove(aboutDetail);
                if (listWorkAndDetail.get(listWorkAndDetail.indexOf(workAndDetail)).getListDetail().isEmpty()){
                    listWorkAndDetail.remove(workAndDetail);
                }
            }
        }
    }

    private void addOldWorkAndDetail() {
        PackerInListWorkAndDetail.addWorkAndDetail(db, accountData, listWorkAndDetail);
        if (listWorkAndDetail != null)
            tbWork.setItems(listWorkAndDetail);
    }

    private void addWorkAndDetail() {
        if (isValid()){
            WorkAndDetail workAndDetail;
            AboutDetail aboutDetail = null;
            tableWorkEntity work;
            tableEmployeeEntity employee;

            employee = employees.get(cmbEmployee.getSelectionModel().getSelectedIndex());
            work = works.get(cmbWork.getSelectionModel().getSelectedIndex());

            workAndDetail = new WorkAndDetail(work, employee);
            if (!cmbDetail.getValue().equals("-")){
                tableDetailEntity detail = details.get(cmbDetail.getSelectionModel().getSelectedIndex()-1);
                aboutDetail = new AboutDetail(detail, cmbCount.getValue());
            }

            boolean check = true;
            for (WorkAndDetail WandD : listWorkAndDetail){
                if (Objects.equals(WandD.employeeProperty().get(), workAndDetail.employeeProperty().get()) && Objects.equals(WandD.workProperty().get(), workAndDetail.workProperty().get())){
                    //если запись с работником и его выполненной работой уже существует
                    check = false;
                    if (aboutDetail != null){
                        boolean prov = true;
                        for (AboutDetail aD : WandD.getListDetail()){
                            prov = !Objects.equals(aD.detailProperty().get(), aboutDetail.detailProperty().get());
                        }
                        if (prov) {
                            if (WandD.getListDetail().isEmpty())
                                deleteToTheDataBase(WandD.getId(), work, employee, null, null);
                            WandD.addDetail(aboutDetail);
                            int id = addToTheDataBase(work, employee, aboutDetail.getDetailTableEntity(), aboutDetail.countProperty().get());
                            aboutDetail.setId(id);
                        } else {
                            Message.errorMessage("Указана одинаковая запись!", stage);
                        }
                    }
                }
            }
            if (check) {
                int id;
                if (aboutDetail != null){
                    id = addToTheDataBase(work, employee, aboutDetail.getDetailTableEntity(), aboutDetail.countProperty().get());
                    aboutDetail.setId(id);
                    workAndDetail.addDetail(aboutDetail);
                }else {
                    id = addToTheDataBase(work, employee, null, null);
                    workAndDetail.setId(id);
                }
                listWorkAndDetail.add(workAndDetail);
            }
            tbWork.setItems(listWorkAndDetail);
        }
    }

    private tableAccountEntity packerInAcoount(tableWorkEntity work, tableEmployeeEntity employee, tableDetailEntity detail, Integer count){
        tableAccountEntity accountEntity = new tableAccountEntity();
        accountEntity.setCheck(accountData);
        accountEntity.setWork(work);
        accountEntity.setEmployee(employee);
        accountEntity.setDetail(detail);
        accountEntity.setAmount(count);

        return accountEntity;
    }

    private void deleteToTheDataBase(int id, tableWorkEntity work, tableEmployeeEntity employee, tableDetailEntity detail, Integer count){
        tableAccountEntity account = packerInAcoount(work, employee, detail, count);
        account.setId(id);
        db.deleteAccount(account);
    }

    private int addToTheDataBase(tableWorkEntity work, tableEmployeeEntity employee, tableDetailEntity detail, Integer count) {
        return db.addAccount(packerInAcoount(work, employee, detail, count));
    }

    private boolean isValid() {
        if (!cmbDetail.getValue().equals("-") && cmbCount.getValue()==0){
            Message.errorMessage("Укажите кол-во деталей!", stage);
            return false;
        }
        return true;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    void setAccountData(tableAccountDataEntity accountData) {
        this.accountData = accountData;
        cmbStatusCar.setValue(accountData.getStatusCar());
        if (!Objects.equals(cmbStatusCar.getValue(), StatusCar.repair.getStatusValue())){
            cmbStatusCar.setVisible(true);
            cmbStatusPay.setValue( accountData.isStatusPayment() ? StatusPayment.paid.getStatusPaymentValue() : StatusPayment.isNotPaid.getStatusPaymentValue());
        }

        addOldWorkAndDetail();
    }
}
