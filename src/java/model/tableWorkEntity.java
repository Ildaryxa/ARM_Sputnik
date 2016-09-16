package model;

import com.sun.org.glassfish.gmbal.Description;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by ildar on 23.06.2016.
 */
@Entity
@Table(name="Work", schema="dbo", catalog="SPYTNIK")
@Description(value = "Работы")
public class tableWorkEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", nullable = false)
    @Description(value = "id")
    private Integer id;

    @Basic
    @Column(name = "name_work")
    @Description(value = "Наименование работы")
    private String nameWork;

    @Basic
    @Column(name = "price_Of_work")
    @Description(value = "Стоимость работы")
    private Integer priceOfWork;

    @OneToMany(mappedBy = "work")
    private Set<tableAccountEntity> accountEntities = new HashSet<tableAccountEntity>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNameWork() {
        return nameWork;
    }

    public void setNameWork(String nameWork) {
        this.nameWork = nameWork;
    }

    public int getPriceOfWork() {
        return priceOfWork;
    }

    public void setPriceOfWork(int priceOfWork) {
        this.priceOfWork = priceOfWork;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        tableWorkEntity that = (tableWorkEntity) o;

        if (id != that.id) return false;
        if (priceOfWork != that.priceOfWork) return false;
        if (nameWork != null ? !nameWork.equals(that.nameWork) : that.nameWork != null) return false;

        return true;
    }

    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (nameWork != null ? nameWork.hashCode() : 0);
        result = 31 * result + (priceOfWork != null ? priceOfWork.hashCode() : 0);
        return result;
    }
}
