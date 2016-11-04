package orm;

import enumType.StatusCar;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.*;
import org.hibernate.Session;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import java.util.Collection;
import java.util.Objects;


/**
 * Created by ildar on 28.06.2016.
 */
public enum  DataBaseConnection {
    INSTANCE;

    public Collection getUsersData(){
        try (Session session = HibernateSessionFactory.getSession()){
            return session.createCriteria(tableUsersEntity.class).list();
        }
    }

    public void addUser(tableUsersEntity user){
        try (Session session = HibernateSessionFactory.getSession()){
            session.beginTransaction();
            session.save(user);
            session.getTransaction().commit();
        }
    }

    public tableUsersEntity getUser(String login) {
        try (Session session = HibernateSessionFactory.getSession()){
            return (tableUsersEntity) session.createCriteria(tableUsersEntity.class).add(Restrictions.eq("login", login)).uniqueResult();
        }
    }

    public void changeUser(tableUsersEntity user){
        try (Session session = HibernateSessionFactory.getSession()){
            session.beginTransaction();
            session.update(user);
            session.getTransaction().commit();
        }
    }

    public void changeUser(tableUsersEntity user, tableUsersEntity userOld) {
        try (Session session = HibernateSessionFactory.getSession()){
            session.beginTransaction();
            ObservableList<tableAccountDataEntity> accountData  = FXCollections.observableArrayList();
            accountData.addAll(getAccountDataForUser(userOld));
            session.save(user);
            for (tableAccountDataEntity account : accountData){
                account.setLoginUser(user);
                session.update(account);
            }
            session.delete(userOld);
            session.getTransaction().commit();
        }
    }

    public void deleteUser(tableUsersEntity user) {
        try (Session session = HibernateSessionFactory.getSession()){
            session.beginTransaction();
            session.delete(user);
            session.getTransaction().commit();
        }
    }

    public Collection getWorks() {
        try (Session session = HibernateSessionFactory.getSession()){
            return session.createCriteria(tableWorkEntity.class).list();
        }
    }

    public void addWork(tableWorkEntity newWork) {
        try (Session session = HibernateSessionFactory.getSession()){
            session.beginTransaction();
            session.save(newWork);
            session.getTransaction().commit();
        }
    }

    public void updateWork(tableWorkEntity updateWork) {
        try (Session session = HibernateSessionFactory.getSession()){
            session.beginTransaction();
            session.update(updateWork);
            session.getTransaction().commit();
        }
    }

    public void deleteWork(tableWorkEntity newWork) {
        try (Session session = HibernateSessionFactory.getSession()){
            session.beginTransaction();
            session.delete(newWork);
            session.getTransaction().commit();
        }
    }

    public Collection getDetails() {
        try (Session session = HibernateSessionFactory.getSession()){
            return session.createCriteria(tableDetailEntity.class).list();
        }
    }

    public void deleteDetail(tableDetailEntity detail) {
        try (Session session = HibernateSessionFactory.getSession()){
            session.beginTransaction();
            session.delete(detail);
            session.getTransaction().commit();
        }
    }

    public void addDetail(tableDetailEntity detail) {
        try (Session session = HibernateSessionFactory.getSession()){
            session.beginTransaction();
            session.save(detail);
            session.getTransaction().commit();
        }
    }

    public void updateDetail(tableDetailEntity detail) {
        try (Session session = HibernateSessionFactory.getSession()){
            session.beginTransaction();
            session.update(detail);
            session.getTransaction().commit();
        }
    }

    public Collection getEmployees() {
        try (Session session = HibernateSessionFactory.getSession()){
            return session.createCriteria(tableEmployeeEntity.class).list();
        }
    }

    public void addEmployee(tableEmployeeEntity employee) {
        try (Session session = HibernateSessionFactory.getSession()){
            session.beginTransaction();
            session.save(employee);
            session.getTransaction().commit();
        }
    }

    public void changeEmployee(tableEmployeeEntity employee) {
        try (Session session = HibernateSessionFactory.getSession()){
            session.beginTransaction();
            session.update(employee);
            session.getTransaction().commit();
        }
    }

    public void deleteEmployee(tableEmployeeEntity employee) {
        try (Session session = HibernateSessionFactory.getSession()){
            session.beginTransaction();
            session.delete(employee);
            session.getTransaction().commit();
        }
    }

    public Collection getFirmAuto() {
        try (Session session = HibernateSessionFactory.getSession()){
            return session.createCriteria(tableFirmAutoEntity.class).list();
        }
    }

    public Collection getCars() {
        try (Session session = HibernateSessionFactory.getSession()){
            return session.createCriteria(tableCarEntity.class).list();
        }
    }

    public void addCar(tableCarEntity car) {
        try (Session session = HibernateSessionFactory.getSession()){
            session.beginTransaction();
            session.save(car);
            session.getTransaction().commit();
        }
    }

    public int getCarId(String numberCar) {
        try (Session session = HibernateSessionFactory.getSession()){
            tableCarEntity car = (tableCarEntity) session.createCriteria(tableCarEntity.class).add(Restrictions.eq("number_car", numberCar)).uniqueResult();
            return car.getId();
        }
    }

    public void addContract(tableAccountDataEntity accountData) {
        try (Session session = HibernateSessionFactory.getSession()){
            session.beginTransaction();
            session.save(accountData);
            session.getTransaction().commit();
        }
    }


    public Collection getAccountDates() {
        try (Session session = HibernateSessionFactory.getSession()){
            return session.createCriteria(tableAccountDataEntity.class).add(Restrictions.eq("statusCar", StatusCar.repair.getStatusValue())).list();
        }
    }

    public Collection getAccountDataForUser(tableUsersEntity user) {
        try (Session session = HibernateSessionFactory.getSession()){
            return session.createCriteria(tableAccountDataEntity.class).add(Restrictions.eq("loginUser", user)).list();
        }
    }

    public int getCountContract() {
        try (Session session = HibernateSessionFactory.getSession()){
            return Integer.parseInt(session.createCriteria(tableAccountDataEntity.class).setProjection(Projections.rowCount()).uniqueResult().toString());
        }
    }

    public Collection getAccountDataReady() {
        try (Session session = HibernateSessionFactory.getSession()){
            return session.createCriteria(tableAccountDataEntity.class).add(Restrictions.ne("statusCar", StatusCar.repair.getStatusValue())).list();
        }
    }

    public void deleteAccountData(tableAccountDataEntity accountData) {
        try (Session session = HibernateSessionFactory.getSession()){
            session.beginTransaction();
            tableCommentsEntity commentsEntity = getComment(accountData);
            session.delete(commentsEntity);
            session.delete(accountData);
            session.delete(accountData.getCar());
            session.getTransaction().commit();
        }
    }

    public void updateAccountData(tableAccountDataEntity accountData) {
        try (Session session = HibernateSessionFactory.getSession()){
            session.beginTransaction();
            session.update(accountData);
            session.update(accountData.getCar());
            session.getTransaction().commit();
        }
    }

    public Collection getAccount(tableAccountDataEntity accountData) {
        try (Session session = HibernateSessionFactory.getSession()){
            return session.createCriteria(tableAccountEntity.class).add(Restrictions.eq("check", accountData)).list();
        }
    }

    public Integer addAccount(tableAccountEntity accountEntity) {
        try (Session session = HibernateSessionFactory.getSession()){
            session.beginTransaction();
            session.save(accountEntity);
            session.getTransaction().commit();
        }
        return accountEntity.getId();
    }

    public void deleteAccount(tableAccountEntity accountEntity) {
        try (Session session = HibernateSessionFactory.getSession()){
            session.beginTransaction();
            session.delete(accountEntity);
            session.getTransaction().commit();
        }
    }


    public tableCommentsEntity getComment(tableAccountDataEntity accountData) {
        try (Session session = HibernateSessionFactory.getSession()){
            return (tableCommentsEntity) session.createCriteria(tableCommentsEntity.class).add(Restrictions.eq("accountDataEntity", accountData)).uniqueResult();
        }
    }

    public void updateComment(tableCommentsEntity comment) {
        try (Session session = HibernateSessionFactory.getSession()){
            session.beginTransaction();
            session.update(comment);
            session.getTransaction().commit();
        }
    }

    public void createComment(tableCommentsEntity comment) {
        try (Session session = HibernateSessionFactory.getSession()){
            session.beginTransaction();
            session.save(comment);
            session.getTransaction().commit();
        }
    }

    public Collection getComments() {
        try (Session session = HibernateSessionFactory.getSession()){
            return session.createCriteria(tableCommentsEntity.class).list();
        }
    }

    public void deleteComment(tableCommentsEntity delComment) {
        try (Session session = HibernateSessionFactory.getSession()){
            session.beginTransaction();
            session.delete(delComment);
            session.getTransaction().commit();
        }
    }

    public Collection getFields() {
        try (Session session = HibernateSessionFactory.getSession()){
            return session.createCriteria(tableFieldEntity.class).list();
        }
    }
}
