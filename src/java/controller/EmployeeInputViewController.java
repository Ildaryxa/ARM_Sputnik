package controller;

import enumType.FormType;
import enumType.Status;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import message.Message;
import model.tableEmployeeEntity;
import validate.Check;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.StringJoiner;

/**
 * Created by ildar on 27.09.2016.
 */
public class EmployeeInputViewController implements Initializable {

    @FXML TextField tfName;
    @FXML TextField tfSurname;
    @FXML TextField tfFirstName;
    @FXML TextField tfQualification;
    @FXML TextField tfPhoneNumber;
    @FXML TextField tfLocation;
    @FXML ComboBox<String> cmbStatus;
    @FXML TextField tfPasportSerial;
    @FXML TextField tfPasportNumber;
    @FXML TextField taRegistration;
    @FXML Button btnCancel;
    @FXML Button btnAccept;

    private static Stage stage;
    private FormType type;
    private tableEmployeeEntity employeeEntity;
    private boolean okClicked = false;

    public EmployeeInputViewController() {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<String> statuses = FXCollections.observableArrayList();
        for(Status s : Status.values()){
            statuses.add(s.getStatus());
        }
        cmbStatus.setItems(statuses);
        cmbStatus.setValue(Status.working.getStatus());

        btnCancel.setOnAction(event -> stage.close());

        btnAccept.setOnAction(event -> {
            acceptEmployee();
        });
    }

    private void acceptEmployee() {
        if (isValid()) {
            employeeEntity.setName(tfName.getText().trim());
            employeeEntity.setSurname(tfSurname.getText().trim());
            employeeEntity.setFirstName(tfFirstName.getText().trim());
            employeeEntity.setQualification(tfQualification.getText().trim());
            employeeEntity.setPhoneNumber(tfPhoneNumber.getText().trim());
            employeeEntity.setLocation(tfLocation.getText().trim());
            employeeEntity.setStatus(cmbStatus.getValue());
            employeeEntity.setPasportNumber(!tfPasportNumber.getText().isEmpty() ?  Integer.valueOf(tfPasportNumber.getText().trim()) : null);
            employeeEntity.setPasportSeries(!tfPasportSerial.getText().isEmpty() ?  Integer.valueOf(tfPasportSerial.getText().trim()) : null);
            employeeEntity.setPasportRegistration( !taRegistration.getText().isEmpty() ? taRegistration.getText().trim() : "");

            okClicked = true;
            stage.close();
        }
    }

    void setDialogStage(Stage dialogStage, FormType frmType){
        stage = dialogStage;
        type = frmType;
        stage.setOnShown(event -> {
            switch (type){
                case addEmployee:
                        stage.setTitle("'Спутник' - Добавление нового сотрудника");
                    break;
                case changeEmployee:
                        stage.setTitle("'Спутник' - изменение сотрудника");
                    break;
            }
        });
    }

    void setEmployee(tableEmployeeEntity employee){
        this.employeeEntity = employee;
        if (type == FormType.changeEmployee){
            tfName.setText(employeeEntity.getName());
            tfSurname.setText(employeeEntity.getSurname());
            String firstName = employeeEntity.getFirstName();
            tfFirstName.setText( firstName == null ? "" : firstName);
            tfQualification.setText(employeeEntity.getQualification());
            tfPhoneNumber.setText(employeeEntity.getPhoneNumber());
            tfLocation.setText(employeeEntity.getLocation());
            cmbStatus.setValue(employeeEntity.getStatus());
            String pasportNumber = String.valueOf(employeeEntity.getPasportNumber());
            tfPasportNumber.setText( !pasportNumber.equals("null") ? pasportNumber : "");
            String pasportSeries = String.valueOf(employeeEntity.getPasportSeries());
            tfPasportSerial.setText( !pasportSeries.equals("null") ? pasportSeries : "");
            String pasportRegistration = employeeEntity.getPasportRegistration();
            taRegistration.setText( pasportRegistration == null ? "" : pasportRegistration);
        }
    }


    public boolean isOkClicked() {
        return okClicked;
    }

    public boolean isValid() {
        String errorMessage = "";
        if (tfName.getText() == null || tfName.getText().length()==0){
            errorMessage += "Введите имя!\n";
        }else if (tfName.getText().length() > 30){
            errorMessage += "Имя: кол-во символов >30";
        }
        if (tfSurname.getText() == null || tfSurname.getText().length() == 0){
            errorMessage += "Введите фамилию!\n";
        }else if (tfSurname.getText().length() > 30){
            errorMessage += "Фамилия: кол-во символов >30\n";
        }
        if (tfFirstName.getText()!= null && tfFirstName.getText().length()>30){
            errorMessage += "Отчество: кол-во символов >30\n";
        }
        if (tfQualification.getText() == null || tfQualification.getText().length() == 0){
            errorMessage += "Введите квалификацию сотрудника!\n";
        }else if (tfQualification.getText().length()>20){
            errorMessage += "Квалификация: кол-во символов > 20\n";
        }
        if (tfPhoneNumber.getText() == null || tfPhoneNumber.getText().length() == 0){
            errorMessage += "Введите номер телефона!\n";
        }else if (tfPhoneNumber.getText().length() > 20){
            errorMessage += "Номер телефона: кол-во символов > 20\n";
        }
        if (tfLocation.getText() == null || tfLocation.getText().length() == 0){
            errorMessage += "Введите свой адрес!\n";
        }else if (tfLocation.getText().length() > 50){
            errorMessage += "Адрес: кол-во символов > 50\n";
        }
        if (tfPasportNumber!=null && tfPasportNumber.getText().length()>6){
            errorMessage += "Номер паспорта: кол-во символов > 6\n";
        }
        if (tfPasportNumber != null && !Check.checkString(tfPasportNumber.getText())){
            errorMessage += "Номер паспорта: ошибка в записи числа!\n";
        }
        if (tfPasportSerial!=null && tfPasportSerial.getText().length()>4){
            errorMessage += "Серия паспорта: кол-во символов > 4\n";
        }
        if (tfPasportSerial != null && !Check.checkString(tfPasportSerial.getText())){
            errorMessage += "Серия паспорта: ошибка в записи числа!\n";
        }
        if (tfPasportSerial != null && taRegistration.getText().length() > 50){
            errorMessage += "Регистрация: кол-во символов > 50\n";
        }

        return Message.errorMessage(errorMessage, stage);
    }


}
