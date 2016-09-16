package model;

import com.sun.org.glassfish.gmbal.Description;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by ildar on 23.06.2016.
 */

@Entity
@Table(name = "Comments", schema = "dbo", catalog = "SPYTNIK")
@Description(value = "Комментарии")
public class tableCommentsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", nullable = false)
    @Description(value = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "login")
    @Description(value = "Логин")
    private tableUsersEntity login;

    @Basic
    @Column(name = "data")
    @Description(value = "Дата")
    private Timestamp data;

    @Basic
    @Column(name = "text")
    @Description(value = "Текст")
    private String text;

    @ManyToOne
    @JoinColumn(name = "id_account_data")
    private tableAccountDataEntity accountDataEntity;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public tableUsersEntity getLogin() {
        return login;
    }

    public void setLogin(tableUsersEntity login) {
        this.login = login;
    }

    public tableAccountDataEntity getAccountDataEntity() {
        return accountDataEntity;
    }

    public void setAccountDataEntity(tableAccountDataEntity accountDataEntity) {
        this.accountDataEntity = accountDataEntity;
    }

    public Timestamp getData() {
        return data;
    }

    public void setData(Timestamp data) {
        this.data = data;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        tableCommentsEntity that = (tableCommentsEntity) o;

        if (login != null ? !login.equals(that.login) : that.login != null) return false;
        if (data != null ? !data.equals(that.data) : that.data != null) return false;
        if (text != null ? !text.equals(that.text) : that.text != null) return false;

        return true;
    }

    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (login != null ? login.hashCode() : 0);
        result = 31 * result + (data != null ? data.hashCode() : 0);
        result = 31 * result + (text != null ? text.hashCode() : 0);
        result = 31 * result + (accountDataEntity != null ? accountDataEntity.hashCode() : 0);
        return result;
    }
}
