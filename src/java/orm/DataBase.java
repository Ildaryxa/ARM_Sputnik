package orm;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by ildar on 28.06.2016.
 */
public enum  DataBase {
    INSTANCE;

    private static DataBaseConnection connection;

    static {
        connection = DataBaseConnection.INSTANCE;
    }

    private ObservableList<tableUsersEntity> users;
    private ObservableList<tableWorkEntity> works;
    private ObservableList<tableDetailEntity> details;
    private ObservableList<tableEmployeeEntity> employees;
    private int countContract;

    private ObservableList addCollection(Collection collection) {
        ObservableList observableList = FXCollections.observableArrayList();
        observableList.addAll(collection);
        //Collections.sort(observableList);
        return observableList;
    }

    public ObservableList<tableUsersEntity> getUsersData(){
        users = addCollection(connection.getUsersData());
        return users;
    }

    public void addUser(tableUsersEntity user){
        connection.addUser(user);
    }

    public tableUsersEntity getUser(String login) {
        return connection.getUser(login);
    }

    public void changeUser(tableUsersEntity user, tableUsersEntity userOld) {
        connection.changeUser(user, userOld);
    }

    public void changeUser(tableUsersEntity user) {
        connection.changeUser(user);
    }

    public void deleteUser(tableUsersEntity user) {
        connection.deleteUser(user);
    }

    public ObservableList<tableWorkEntity> getWorks(){
        works = addCollection(connection.getWorks());
        return works;
    }

    public void addWork(tableWorkEntity newWork) {
        connection.addWork(newWork);
    }

    public void updateWork(tableWorkEntity updateWork){
        connection.updateWork(updateWork);
    }

    public void deleteWork(tableWorkEntity newWork) {
        connection.deleteWork(newWork);
    }

    public ObservableList<tableDetailEntity> getDetails() {
        details = addCollection(connection.getDetails());
        return details;
    }

    public void deleteDetail(tableDetailEntity detail) {
        connection.deleteDetail(detail);
    }

    public void addDetail(tableDetailEntity detail) {
        connection.addDetail(detail);
    }

    public void updateDetail(tableDetailEntity detail) {
        connection.updateDetail(detail);
    }

    public ObservableList<tableEmployeeEntity> getEmployees() {
        employees = addCollection(connection.getEmployees());
        return employees;
    }

    public void addEmployee(tableEmployeeEntity employee) {
        connection.addEmployee(employee);
    }

    public void changeEmployee(tableEmployeeEntity employee) {
        connection.changeEmployee(employee);
    }

    public void deleteEmployee(tableEmployeeEntity employee) {
        connection.deleteEmployee(employee);
    }

    public ObservableList<tableCarEntity> getCars() {
        ObservableList<tableCarEntity> cars = addCollection(connection.getCars());
        return cars;
    }

    public void addCar(tableCarEntity car) {
        connection.addCar(car);
    }

    public void addContract(tableAccountDataEntity accountData) {
        connection.addContract(accountData);
    }


    public ObservableList<tableAccountDataEntity> getAccountDates() {
        return addCollection(connection.getAccountDates());
    }

    public ObservableList<tableAccountDataEntity> getAccountDataReady() {
        return addCollection(connection.getAccountDataReady());
    }

    public int getCountContract() {
        return connection.getCountContract();
    }

    public void deleteAccountData(tableAccountDataEntity accountData) {
        connection.deleteAccountData(accountData);
    }

    public ObservableList<tableAccountEntity> getAccount(tableAccountDataEntity accountData) {
        return addCollection(connection.getAccount(accountData));
    }

    public Integer addAccount(tableAccountEntity accountEntity) {
        return connection.addAccount(accountEntity);
    }

    public void deleteAccount(tableAccountEntity tableAccountEntity) {
        connection.deleteAccount(tableAccountEntity);
    }

    public void updateAccountData(tableAccountDataEntity accountData) {
        connection.updateAccountData(accountData);
    }

    public void deleteAccountAll(tableAccountDataEntity accountData){
        ObservableList<tableAccountEntity> list = getAccount(accountData);
        list.forEach(this::deleteAccount);
    }

    public ObservableList<tableAccountDataEntity> getAccountDataForUser(tableUsersEntity user) {
        return addCollection(connection.getAccountDataForUser(user));
    }

    public tableCommentsEntity getComment(tableAccountDataEntity accountData) {
        return connection.getComment(accountData);
    }

    public void updateComment(tableCommentsEntity comment) {
        connection.updateComment(comment);
    }

    public void createComment(tableCommentsEntity comment) {
        connection.createComment(comment);
    }

    public ObservableList<tableCommentsEntity> getComments() {
        return addCollection(connection.getComments());
    }

    public void deleteComment(tableCommentsEntity delComment) {
        connection.deleteComment(delComment);
    }

    public ObservableList<tableFieldEntity> getFields() {
        return addCollection(connection.getFields());
    }
}
