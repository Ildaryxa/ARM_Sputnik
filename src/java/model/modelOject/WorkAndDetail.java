package model.modelOject;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.tableDetailEntity;
import model.tableEmployeeEntity;
import model.tableWorkEntity;

/**
 * Created by ildar on 08.10.2016.
 */
public class WorkAndDetail {

    private int id;
    private final StringProperty employee;
    private final StringProperty work;
    private final IntegerProperty priceWork;

    private tableWorkEntity workEntity;
    private tableEmployeeEntity employeeEntity;
    private ObservableList<AboutDetail> listDetail = FXCollections.observableArrayList();

    public WorkAndDetail(tableWorkEntity workEntity, tableEmployeeEntity employeeEntity) {
        this.workEntity = workEntity;
        this.employeeEntity = employeeEntity;

        this.employee = new SimpleStringProperty(employeeEntity.getName() + " " + employeeEntity.getSurname());
        this.work = new SimpleStringProperty(workEntity.getNameWork());
        this.priceWork = new SimpleIntegerProperty(workEntity.getPriceOfWork());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ObservableList<AboutDetail> getListDetail() {
        return listDetail;
    }

    public void addDetail(AboutDetail detail) {
        listDetail.add(detail);
    }

    public StringProperty employeeProperty() {
        return employee;
    }

    public StringProperty workProperty() {
        return work;
    }

    public IntegerProperty priceWorkProperty() {
        return priceWork;
    }

    public tableWorkEntity getWorkEntity() {
        return workEntity;
    }

    public tableEmployeeEntity getEmployeeEntity() {
        return employeeEntity;
    }
}
