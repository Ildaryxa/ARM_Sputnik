package model;

import com.sun.org.glassfish.gmbal.Description;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by ildar on 23.06.2016.
 */
@Entity
@Table(name="Account_data", schema="dbo", catalog="sputnik")
@Description(value = "Списки")
public class tableAccountDataEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", nullable = false)
    @Description(value = "id")
    private Integer id;

    @Basic
    @Column(name = "date")
    @Description(value = "Дата подачи")
    private Timestamp date;

    @ManyToOne
    @JoinColumn(name = "id_car")
    private tableCarEntity car;

    @ManyToOne
    @JoinColumn(name = "login_user")
    @Description(value = "Логин пользователя")
    private tableUsersEntity loginUser;

    @Basic
    @Column(name = "confidentially")
    @Description(value = "Приватность")
    private boolean confidentially;

    @Basic
    @Column(name = "status_car")
    @Description(value = "Статус ремонта")
    private String statusCar;

    @Basic
    @Column(name = "status_payment")
    @Description(value = "Статус оплаты")
    private boolean statusPayment;

    /*
    @OneToMany(mappedBy = "accountDataEntity")
    private Set<tableCommentsEntity> commentsEntities = new HashSet<tableCommentsEntity>();

    @OneToMany(mappedBy = "check")
    private Set<tableAccountEntity> accountEntities = new HashSet<tableAccountEntity>();
    */
    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public tableCarEntity getCar() {
        return car;
    }

    public void setCar(tableCarEntity idCar) {
        this.car = idCar;
    }

    public tableUsersEntity getLoginUser() {
        return loginUser;
    }

    public void setLoginUser(tableUsersEntity loginUser) {
        this.loginUser = loginUser;
    }

    public String getStatusCar() {
        return statusCar;
    }

    public void setStatusCar(String statusCar) {
        this.statusCar = statusCar;
    }

    public boolean isStatusPayment() {
        return statusPayment;
    }

    public void setStatusPayment(boolean statusPayment) {
        this.statusPayment = statusPayment;
    }

    public boolean isConfidentially() {
        return confidentially;
    }

    public void setConfidentially(boolean confidentially) {
        this.confidentially = confidentially;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        tableAccountDataEntity that = (tableAccountDataEntity) o;

        if (id != that.id) return false;
        if (car != that.car) return false;
        if (date != null ? !date.equals(that.date) : that.date != null) return false;
        if (loginUser != null ? !loginUser.equals(that.loginUser) : that.loginUser != null) return false;
        if (confidentially != that.confidentially) return false;
        if (statusCar != null ? !statusCar.equals(that.statusCar) : that.statusCar != null) return false;
        if (statusPayment != that.statusPayment) return false;

        return true;
    }

    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (car != null ? car.hashCode() : 0);
        result = 31 * result + (loginUser != null ? loginUser.hashCode() : 0);
        result = 31 * result + (confidentially ? 1 : 0);
        result = 31 * result + (statusCar != null ? statusCar.hashCode() : 0);
        result = 31 * result + (statusPayment ? 1 : 0);
        return result;
    }
}
