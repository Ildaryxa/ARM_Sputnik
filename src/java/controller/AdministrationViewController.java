package controller;

import enumType.FormType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.modelOject.ListOfCarInRepair;
import model.tableAccountDataEntity;
import model.tableCarEntity;
import model.tableUsersEntity;
import orm.DataBase;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * Created by ildar on 14.09.2016.
 */
public class AdministrationViewController implements Initializable{

    @FXML private MenuBar menuBar;
    @FXML private TableView<ListOfCarInRepair> tbCarReair;
    @FXML private TableColumn<ListOfCarInRepair, Integer> tbcId;
    @FXML private TableColumn<ListOfCarInRepair, String> tbcUser;
    @FXML private TableColumn<ListOfCarInRepair, String> tbcPhoneNumber;
    @FXML private TableColumn<ListOfCarInRepair, String> tbcCarName;
    @FXML private TableColumn<ListOfCarInRepair, String> tbcNumberCar;
    @FXML private TableColumn<ListOfCarInRepair, LocalDateTime> tbcDataRegistration;
    @FXML private ListView<String> lvContractArchive;

    private Stage stage;
    private static DataBase db;
    private static BorderPane rootLayout;
    private tableUsersEntity user; //пользователь-администратор, который зашел в админку
    private ObservableList<tableAccountDataEntity> accountDates;
    private ObservableList<tableAccountDataEntity> accountDataReady;
    private ObservableList<ListOfCarInRepair> listOfCarInRepairs = FXCollections.observableArrayList();
    private ObservableList<String> listCarReady = FXCollections.observableArrayList();

    public AdministrationViewController() {
    }

    static {
        db = DataBase.INSTANCE;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initMenuBar();

        updateTableCarRepair();

        tbcId.setCellValueFactory(cellData -> cellData.getValue().getIdContract().asObject());
        tbcUser.setCellValueFactory(cellData -> cellData.getValue().getNameUser());
        tbcPhoneNumber.setCellValueFactory(cellData -> cellData.getValue().getPhoneNumber());
        tbcCarName.setCellValueFactory(cellData -> cellData.getValue().getCarName());
        tbcNumberCar.setCellValueFactory(cellData -> cellData.getValue().getCarNumber());
        tbcDataRegistration.setCellValueFactory(cellData -> cellData.getValue().getDateRegistration());

        tbcId.setVisible(false);

        tbCarReair.setItems(listOfCarInRepairs);

        tbCarReair.setRowFactory(param -> {
            TableRow<ListOfCarInRepair> row = new TableRow<ListOfCarInRepair>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())){
                    updateContract(tbCarReair.getSelectionModel().getSelectedItem());
                }
            });
            return row;
        });

        lvContractArchive.setItems(listCarReady);

        lvContractArchive.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && (!lvContractArchive.getSelectionModel().isEmpty())){
                openContract(lvContractArchive.getSelectionModel().getSelectedIndex());
            }
        });
    }

    private void openContract(int number) {
        if (accountDataReady.get(number).isStatusPayment()){
            //если ремонт оплачен, то редактировать машину уже нельзя
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
                viewContractController.setStage(dialogStage, FormType.adminView);
                viewContractController.setMain(this);
                viewContractController.setAccountData(accountDataReady.get(number));

                dialogStage.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            //если ремонт не оплачен, то редактировать договор еще можно
            tableAccountDataEntity account = accountDataReady.get(number);
            String nameUser = account.getLoginUser().getName() + " " + account.getLoginUser().getSurname();
            String nameCar = account.getCar().getFirm() + " " + account.getCar().getMark();
            ListOfCarInRepair list = new ListOfCarInRepair(account.getId(), nameUser, account.getLoginUser().getPhoneNumber(), nameCar, account.getCar().getNumberCar(), account.getDate().toLocalDateTime());
            list.setAccountData(account);
            updateContract(list);
        }
    }

    void updateListView() {
        accountDataReady = db.getAccountDataReady();
        lvContractArchive.getItems().clear();
        for (tableAccountDataEntity account : accountDataReady){
            listCarReady.add(account.getLoginUser().getName() + " " + account.getLoginUser().getSurname() +
                    " : " + account.getCar().getFirm() + " " + account.getCar().getMark() + " " + account.getCar().getNumberCar() +
                    "\n" + account.getStatusCar() + ", " + (account.isStatusPayment()?"Оплачено":"Не оплачено"));
        }
    }

    private void updateContract(ListOfCarInRepair list) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("view/add_contract.view.fxml"));
            AnchorPane rootLayout = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(stage);
            dialogStage.setResizable(false);
            dialogStage.setScene(new Scene(rootLayout));
            dialogStage.setTitle("Изменение контракта");

            AddContractViewController addContractController = loader.getController();
            addContractController.setStage(dialogStage, FormType.changeContract);
            addContractController.setMain(this);
            addContractController.setContract(list);

            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void updateTableCarRepair(){
        accountDates = db.getAccountDates();
        tbCarReair.getItems().clear();

        String nameUser;
        String nameCar;
        for (tableAccountDataEntity account : this.accountDates){
            nameUser = account.getLoginUser().getName() + " " + account.getLoginUser().getSurname();
            nameCar = account.getCar().getFirm() + " " + account.getCar().getMark();
            listOfCarInRepairs.add(new ListOfCarInRepair(account.getId(), nameUser, account.getLoginUser().getPhoneNumber(), nameCar, account.getCar().getNumberCar(), account.getDate().toLocalDateTime()));
            listOfCarInRepairs.get(listOfCarInRepairs.size()-1).setAccountData(account);
        }

        updateListView();
    }

    private void initMenuBar() {
        menuBar.getMenus().clear();

        Menu program = new Menu("Управление Спутником");
        MenuItem work = new MenuItem("Работа");
            work.setOnAction(event -> openTheForm("view/work.view.fxml", "Редактирование услуг автосервиса", "Work"));
        MenuItem employee = new MenuItem("Сотрудники");
            employee.setOnAction(event -> openTheForm("view/employee.view.fxml", "Список сотрудников автосервиса", "Employees"));
        MenuItem detail = new MenuItem("Детали");
            detail.setOnAction(event -> openTheForm("view/detail.view.fxml", "Поставляемые детали автосервиса", "Detail"));
        MenuItem exit = new MenuItem("Выход");
            exit.setOnAction(event -> System.exit(0));

        MenuItem users = new MenuItem("Пользователи");
            users.setOnAction(event -> openTheForm("view/users.view.fxml", "Список пользователей автосервиса", "Users"));

        Menu addContract = new Menu("Управление договорами");
        MenuItem addNewContract = new MenuItem("Добавить новый контракт");
            addNewContract.setOnAction(event -> openTheForm("view/add_contract.view.fxml", "Добавление нового контракта", "AddContract"));
        MenuItem browseReviews = new MenuItem("Просмотреть отзывы");
            browseReviews.setOnAction(event -> openTheForm("view/browseReviews.fxml", "Просмотр отзывов", "Reviews"));
        MenuItem searchData = new MenuItem("Поиск...");
            searchData.setOnAction(event -> openTheForm("view/querryEditor.fxml", "Нестандартный запрос", "Search"));
        program.getItems().addAll(work, employee, detail, users, new SeparatorMenuItem(), exit);
        addContract.getItems().addAll(addNewContract, browseReviews, searchData);
        menuBar.getMenus().addAll(program, addContract);

        ContextMenu ctMenu = new ContextMenu();
            MenuItem miDeleteContract = new MenuItem("Удалить контракт");

        miDeleteContract.setOnAction(event -> {
            if (tbCarReair.isFocused()){
                if (!tbCarReair.getSelectionModel().isEmpty()) deleteContract(tbCarReair.getSelectionModel().getSelectedItem().getAccountData());
            }else{
                if (!lvContractArchive.getSelectionModel().isEmpty())
                    deleteContract(accountDataReady.get(lvContractArchive.getSelectionModel().getSelectedIndex()));
            }

        });

        ctMenu.getItems().add(miDeleteContract);
        tbCarReair.setContextMenu(ctMenu);
        lvContractArchive.setContextMenu(ctMenu);
    }

    private void deleteContract(tableAccountDataEntity accountData) {
        db.deleteAccountAll(accountData);
        db.deleteAccountData(accountData);
        updateTableCarRepair();
    }


    private void openTheForm(String path, String title, String what){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource(path));
            Parent rootLayout = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(stage);
            dialogStage.setResizable(false);
            dialogStage.setScene(new Scene(rootLayout));
            dialogStage.setTitle(title);
            switch (what){
                case "Search":
                    QuerryEditorController editorController = loader.getController();
                    editorController.setStage(dialogStage);
                    break;
                case "Reviews":
                    ReviewsController reviewsController = loader.getController();
                    reviewsController.setStage(dialogStage);
                    break;
                case "Detail":
                    DetailViewController detailViewController = loader.getController();
                    detailViewController.setDialogStage(dialogStage);
                    break;
                case "Work" :
                    WorkViewController workViewController = loader.getController();
                    workViewController.setDialogStage(dialogStage);
                    break;
                case "Employees":
                    EmployeeViewController employeeController = loader.getController();
                    employeeController.setDialogStage(dialogStage);
                    break;
                case "Users":
                    UserViewController userController = loader.getController();
                    userController.setStage(dialogStage);
                    break;
                case "AddContract":
                    AddContractViewController addContractController = loader.getController();
                    addContractController.setStage(dialogStage, FormType.addContract);
                    addContractController.setMain(this);
                    break;
            }
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void setData(Stage stage, tableUsersEntity user) {
        this.stage = stage;
        this.user = user;
    }
}
