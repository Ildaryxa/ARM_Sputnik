package model;

import com.sun.org.glassfish.gmbal.Description;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by ildar on 23.06.2016.
 */
@Entity
@Table(name = "Users", schema = "dbo", catalog = "SPYTNIK")
@Description(value = "Пользователи")
public class tableUsersEntity implements Comparable<tableUsersEntity> {

    @Basic
    @Column(name = "name")
    @Description(value = "Имя")
    private String name;

    @Basic
    @Column(name = "surname")
    @Description(value = "Фамилия")
    private String surname;

    @Id
    @Column(name="login", nullable = false)
    @Description(value = "Логин")
    private String login;

    @Basic
    @Column(name = "passwordHash")
    @Description(value = "Хеш пароля")
    private String passwordHash;

    @Basic
    @Column(name = "salt")
    @Description(value = "Соль")
    private String salt;

    @Basic
    @Column(name = "isAdmin")
    @Description(value = "Админ?")
    private boolean isAdmin;

    @Basic
    @Column(name = "dataRegistration")
    @Description(value = "Дата регистрации")
    private Timestamp dataRegistration;

    @Basic
    @Column(name = "locked")
    @Description(value = "Бан")
    private boolean locked;

    @OneToMany(mappedBy = "login")
    private Set<tableCommentsEntity> commentsEntities = new HashSet<tableCommentsEntity>();

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

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public Timestamp getDataRegistration() {
        return dataRegistration;
    }

    public void setDataRegistration(Timestamp dataRegistration) {
        this.dataRegistration = dataRegistration;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        tableUsersEntity that = (tableUsersEntity) o;

        if (locked != that.locked) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (surname != null ? !surname.equals(that.surname) : that.surname != null) return false;
        if (login != null ? !login.equals(that.login) : that.login != null) return false;
        if (passwordHash != null ? !passwordHash.equals(that.passwordHash) : that.passwordHash != null) return false;
        if (salt != null ? !salt.equals(that.salt) : that.salt != null) return false;
        if (dataRegistration != null ? !dataRegistration.equals(that.dataRegistration) : that.dataRegistration != null)
            return false;

        return true;
    }

    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (surname != null ? surname.hashCode() : 0);
        result = 31 * result + (login != null ? login.hashCode() : 0);
        result = 31 * result + (passwordHash != null ? passwordHash.hashCode() : 0);
        result = 31 * result + (salt != null ? salt.hashCode() : 0);
        result = 31 * result + (dataRegistration != null ? dataRegistration.hashCode() : 0);
        result = 31 * result + (locked ? 1 : 0);
        return result;
    }

    @Override
    public int compareTo(tableUsersEntity o) {
        if (getSurname().compareTo(o.getSurname()) == 0) {
            if (getName().compareTo(o.getName()) == 0){
                return getLogin().compareTo(o.getLogin());
            }else
                return -1;
        }else
            return -1;
    }
}
