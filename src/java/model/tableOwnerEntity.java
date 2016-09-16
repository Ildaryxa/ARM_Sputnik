package model;

import com.sun.org.glassfish.gmbal.Description;

import javax.persistence.*;

/**
 * Created by ildar on 23.06.2016.
 */
@Entity
@Table(name = "Owner", schema = "dbo", catalog = "SPYTNIK")
@Description(value = "Владельцы")
public class tableOwnerEntity {

    @Id
    @Column(name="login", nullable = false)
    @Description(value = "Логин")
    private String login;

    @Basic
    @Column(name = "company_name")
    @Description(value = "Наименование компании")
    private String companyName;

    @Basic
    @Column(name = "location")
    @Description(value = "Местоположение")
    private String location;

    @Basic
    @Column(name = "requisites")
    @Description(value = "Реквизиты")
    private String requisites;

    @Basic
    @Column(name = "phone_number")
    @Description(value = "Номер телефона")
    private String phoneNumber;

    @Basic
    @Column(name = "email")
    @Description(value = "E-mail")
    private String email;
    /*
    @OneToMany(mappedBy = "loginOwner")
    private Set<tableAccountDataEntity> accountDataEntities = new HashSet<tableAccountDataEntity>();
    */

    /*
    @OneToOne(mappedBy = "owner")
    private tableUsersEntity user;
    */

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getRequisites() {
        return requisites;
    }

    public void setRequisites(String requisites) {
        this.requisites = requisites;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        tableOwnerEntity that = (tableOwnerEntity) o;

        if (login != null ? !login.equals(that.login) : that.login != null) return false;
        if (companyName != null ? !companyName.equals(that.companyName) : that.companyName != null) return false;
        if (location != null ? !location.equals(that.location) : that.location != null) return false;
        if (requisites != null ? !requisites.equals(that.requisites) : that.requisites != null) return false;
        if (phoneNumber != null ? !phoneNumber.equals(that.phoneNumber) : that.phoneNumber != null) return false;
        if (email != null ? !email.equals(that.email) : that.email != null) return false;

        return true;
    }

    public int hashCode() {
        int result = login != null ? login.hashCode() : 0;
        result = 31 * result + (companyName != null ? companyName.hashCode() : 0);
        result = 31 * result + (location != null ? location.hashCode() : 0);
        result = 31 * result + (requisites != null ? requisites.hashCode() : 0);
        result = 31 * result + (phoneNumber != null ? phoneNumber.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        return result;
    }
}
