package model;

/**
 * Created by ildar on 23.06.2016.
 */

import com.sun.org.glassfish.gmbal.Description;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="Employee", schema="dbo", catalog="SPYTNIK")
@Description(value = "Сотрудники")
public class tableEmployeeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @Description(value = "id")
    private Integer id;

    @Basic
    @Column(name = "name")
    @Description(value = "Имя")
    private String name;

    @Basic
    @Column(name = "surname")
    @Description(value = "Фамилия")
    private String surname;

    @Basic
    @Column(name = "first_Name")
    @Description(value = "Отчество")
    private String firstName;

    @Basic
    @Column(name = "qualification")
    @Description(value = "Квалификация")
    private String qualification;

    @Basic
    @Column(name = "phone_number")
    @Description(value = "Номер телефона")
    private String phoneNumber;

    @Basic
    @Column(name = "location")
    @Description(value = "Место жительства")
    private String location;

    @Basic
    @Column(name = "status")
    @Description(value = "Статус")
    private String status;

    @Basic
    @Column(name = "pasport_series")
    @Description(value = "Серия паспорта")
    private Integer pasportSeries;

    @Basic
    @Column(name = "pasport_number")
    @Description(value = "Номер паспорта")
    private Integer pasportNumber;

    @Basic
    @Column(name = "pasport_registration")
    @Description(value = "Место регистрации")
    private String pasportRegistration;

    @OneToMany(mappedBy = "employee")
    private Set<tableAccountEntity> accountEntities = new HashSet<tableAccountEntity>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getPasportSeries() {
        return pasportSeries;
    }

    public void setPasportSeries(Integer pasportSeries) {
        this.pasportSeries = pasportSeries;
    }

    public Integer getPasportNumber() {
        return pasportNumber;
    }

    public void setPasportNumber(Integer pasportNumber) {
        this.pasportNumber = pasportNumber;
    }

    public String getPasportRegistration() {
        return pasportRegistration;
    }

    public void setPasportRegistration(String pasportRegistration) {
        this.pasportRegistration = pasportRegistration;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        tableEmployeeEntity that = (tableEmployeeEntity) o;

        if (id != that.id) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (surname != null ? !surname.equals(that.surname) : that.surname != null) return false;
        if (firstName != null ? !firstName.equals(that.firstName) : that.firstName != null) return false;
        if (qualification != null ? !qualification.equals(that.qualification) : that.qualification != null)
            return false;
        if (phoneNumber != null ? !phoneNumber.equals(that.phoneNumber) : that.phoneNumber != null) return false;
        if (location != null ? !location.equals(that.location) : that.location != null) return false;
        if (status != null ? !status.equals(that.status) : that.status != null) return false;
        if (pasportSeries != null ? !pasportSeries.equals(that.pasportSeries) : that.pasportSeries != null)
            return false;
        if (pasportNumber != null ? !pasportNumber.equals(that.pasportNumber) : that.pasportNumber != null)
            return false;
        if (pasportRegistration != null ? !pasportRegistration.equals(that.pasportRegistration) : that.pasportRegistration != null)
            return false;

        return true;
    }

    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (surname != null ? surname.hashCode() : 0);
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (qualification != null ? qualification.hashCode() : 0);
        result = 31 * result + (phoneNumber != null ? phoneNumber.hashCode() : 0);
        result = 31 * result + (location != null ? location.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (pasportSeries != null ? pasportSeries.hashCode() : 0);
        result = 31 * result + (pasportNumber != null ? pasportNumber.hashCode() : 0);
        result = 31 * result + (pasportRegistration != null ? pasportRegistration.hashCode() : 0);
        return result;
    }
}
