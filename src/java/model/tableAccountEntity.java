package model;

import com.sun.org.glassfish.gmbal.Description;

import javax.persistence.*;

/**
 * Created by ildar on 23.06.2016.
 */
@Entity
@Table(name="Account", schema="dbo", catalog="SPYTNIK")
@Description(value = "Счет")
public class tableAccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", nullable = false)
    @Description(value = "id")
    private Integer id;

    @Basic
    @Column(name = "amount")
    @Description(value = "кол-во")
    private Integer amount;

    @ManyToOne
    @JoinColumn(name = "id_employee")
    private tableEmployeeEntity employee;

    @ManyToOne
    @JoinColumn(name = "id_work")
    private tableWorkEntity work;

    @ManyToOne
    @JoinColumn(name = "id_detail")
    private tableDetailEntity detail;

    @ManyToOne
    @JoinColumn(name = "id_check")
    private tableAccountDataEntity check;

    public tableAccountEntity() {
    }

    public tableAccountEntity(tableEmployeeEntity employee, tableWorkEntity work, tableDetailEntity detail, tableAccountDataEntity check) {
        this.employee = employee;
        this.work = work;
        this.detail = detail;
        this.check = check;
    }

    public tableEmployeeEntity getEmployee() {
        return employee;
    }

    public void setEmployee(tableEmployeeEntity employee) {
        this.employee = employee;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public tableWorkEntity getWork() {
        return work;
    }

    public void setWork(tableWorkEntity work) {
        this.work = work;
    }

    public tableDetailEntity getDetail() {
        return detail;
    }

    public void setDetail(tableDetailEntity detail) {
        this.detail = detail;
    }

    public tableAccountDataEntity getCheck() {
        return check;
    }

    public void setCheck(tableAccountDataEntity check) {
        this.check = check;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        tableAccountEntity that = (tableAccountEntity) o;

        if (id != that.id) return false;
        if (amount != that.amount) return false;
        if (employee != that.employee) return false;
        if (work != that.work) return false;
        if (detail != that.detail) return false;
        if (check != that.check) return false;

        return true;
    }

    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + amount;
        result = 31 * result + (employee != null ? employee.hashCode() : 0);
        result = 31 * result + (work != null ? work.hashCode() : 0);
        result = 31 * result + (detail != null ? detail.hashCode() : 0);
        result = 31 * result + (check != null ? check.hashCode() : 0);
        return result;
    }
}
