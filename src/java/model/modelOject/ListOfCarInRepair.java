package model.modelOject;

import javafx.beans.property.*;
import model.tableAccountDataEntity;

import java.time.LocalDateTime;

/**
 * Created by ildar on 04.10.2016.
 */
public class ListOfCarInRepair {

    private final IntegerProperty idContract;
    private final StringProperty nameUser;
    private final StringProperty phoneNumber;
    private final StringProperty carName;
    private final StringProperty carNumber;
    private final ObjectProperty<LocalDateTime> dateRegistration;
    private tableAccountDataEntity accountData;

    public ListOfCarInRepair(Integer idContract,String nameUser, String phoneNumber, String carName, String carNumber, LocalDateTime dateRegistration) {
        this.idContract = new SimpleIntegerProperty(idContract);
        this.nameUser = new SimpleStringProperty(nameUser);
        this.phoneNumber = new SimpleStringProperty(phoneNumber);
        this.carName = new SimpleStringProperty(carName);
        this.carNumber = new SimpleStringProperty(carNumber);
        this.dateRegistration = new SimpleObjectProperty(dateRegistration);
    }

    public tableAccountDataEntity getAccountData() {
        return accountData;
    }

    public void setAccountData(tableAccountDataEntity accountData) {
        this.accountData = accountData;
    }

    public IntegerProperty getIdContract() {
        return idContract;
    }

    public StringProperty getNameUser() {
        return nameUser;
    }

    public StringProperty getPhoneNumber() {
        return phoneNumber;
    }

    public StringProperty getCarName() {
        return carName;
    }

    public StringProperty getCarNumber() {
        return carNumber;
    }

    public ObjectProperty<LocalDateTime> getDateRegistration() {
        return dateRegistration;
    }
}
