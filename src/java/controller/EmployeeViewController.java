package controller;

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
import model.tableDetailEntity;
import model.tableEmployeeEntity;
import orm.DataBase;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by ildar on 27.09.2016.
 */
public class EmployeeViewController implements Initializable {

    private static DataBase db;
    private static Stage stage;

    @FXML TableView<tableEmployeeEntity> tbEmployee;
    @FXML TableColumn<tableEmployeeEntity, Integer> tbcId;
    @FXML TableColumn<tableEmployeeEntity, String> tbcName;
    @FXML TableColumn<tableEmployeeEntity, String> tbcSurname;
    @FXML TableColumn<tableEmployeeEntity, String> tbcFirstname;
    @FXML TableColumn<tableEmployeeEntity, String> tbcQualification;
    @FXML TableColumn<tableEmployeeEntity, String> tbcPhoneNumber;
    @FXML TableColumn<tableEmployeeEntity, String> tbcLocation;
    @FXML TableColumn<tableEmployeeEntity, String> tbcStatus;
    @FXML TableColumn<tableEmployeeEntity, Integer> tbcPasportSeries;
    @FXML TableColumn<tableEmployeeEntity, Integer> tbcPasportNumber;
    @FXML TableColumn<tableEmployeeEntity, String> tbcPasportRegistration;




    public EmployeeViewController() {
    }

    static {
        db = DataBase.INSTANCE;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tbcId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tbcName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tbcSurname.setCellValueFactory(new PropertyValueFactory<>("surname"));
        tbcFirstname.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        tbcQualification.setCellValueFactory(new PropertyValueFactory<>("qualification"));
        tbcPhoneNumber.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        tbcLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
        tbcStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        tbcPasportSeries.setCellValueFactory(new PropertyValueFactory<>("pasportSeries"));
        tbcPasportNumber.setCellValueFactory(new PropertyValueFactory<>("pasportNumber"));
        tbcPasportRegistration.setCellValueFactory(new PropertyValueFactory<>("pasportRegistration"));

        tbcId.setVisible(false);

        ContextMenu ctMenu = new ContextMenu();
        MenuItem miAddEmployee = new MenuItem("Добавить нового сотрудника");
            miAddEmployee.setOnAction(event -> addEmployee());
        MenuItem miChangeEmployee = new MenuItem("Изменить данные сотрудника");
            miChangeEmployee.setOnAction(event -> {
                if (!tbEmployee.getSelectionModel().isEmpty())
                    changeEmployee();
            });
        /*
        MenuItem miDeleteEmployee = new MenuItem("Удалить из базы данных");
            miDeleteEmployee.setOnAction(event -> {
                if (!tbEmployee.getSelectionModel().isEmpty())
                    deleteEmployee();
            });
            */
        ctMenu.getItems().addAll(miAddEmployee, miChangeEmployee); // , miDeleteEmployee

        tbEmployee.setItems(db.getEmployees());
        tbEmployee.setContextMenu(ctMenu);
    }

    private void deleteEmployee() {
        tableEmployeeEntity employee = new tableEmployeeEntity();
        constructorEmployee(employee);
        db.deleteEmployee(employee);
        tbEmployee.getItems().clear();
        tbEmployee.setItems(db.getEmployees());
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Удаление сторудника");
        alert.setHeaderText("Сотрудник удален!");
        alert.showAndWait();
    }

    private void changeEmployee() {
        tableEmployeeEntity employee = new tableEmployeeEntity();
        constructorEmployee(employee);
        if (showEmployeeInputView(employee, FormType.changeEmployee)){
            db.changeEmployee(employee);
            tbEmployee.getItems().clear();
            tbEmployee.setItems(db.getEmployees());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Изменение данных");
            alert.setHeaderText("Данные сотрудника изменены");
            alert.showAndWait();
        }
    }

    private void constructorEmployee(tableEmployeeEntity employee){
        int number = tbEmployee.getSelectionModel().getSelectedIndex();
        employee.setId(tbcId.getCellData(number));
        employee.setName(tbcName.getCellData(number));
        employee.setSurname(tbcSurname.getCellData(number));
        employee.setFirstName(tbcFirstname.getCellData(number));
        employee.setPhoneNumber(tbcPhoneNumber.getCellData(number));
        employee.setLocation(tbcLocation.getCellData(number));
        employee.setQualification(tbcQualification.getCellData(number));
        employee.setStatus(tbcStatus.getCellData(number));
        employee.setPasportSeries(tbcPasportSeries.getCellData(number));
        employee.setPasportNumber(tbcPasportNumber.getCellData(number));
        employee.setPasportRegistration(tbcPasportRegistration.getCellData(number));
    }

    private void addEmployee() {
        tableEmployeeEntity employee = new tableEmployeeEntity();
        if (showEmployeeInputView(employee, FormType.addEmployee)){
            db.addEmployee(employee);
            tbEmployee.getItems().clear();
            tbEmployee.setItems(db.getEmployees());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Регистрация");
            alert.setHeaderText("Пользователь успешно зарегистрирован");
            alert.showAndWait();
        }
    }

    private boolean showEmployeeInputView(tableEmployeeEntity employee, FormType type) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("view/employee_input.view.fxml"));
            AnchorPane rootLayout = loader.load();

            //stage
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(stage);
            dialogStage.setResizable(false);
            dialogStage.setScene(new Scene(rootLayout));

            //create controller
            EmployeeInputViewController controller = loader.getController();
            controller.setDialogStage(dialogStage, type);
            controller.setEmployee(employee);
            dialogStage.showAndWait();
            return controller.isOkClicked();
        }catch (IOException ex){
            ex.printStackTrace();
            return false;
        }
    }


    public void setDialogStage(Stage dialogStage) {
        stage = dialogStage;
    }
}
