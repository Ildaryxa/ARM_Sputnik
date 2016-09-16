package model;

import com.sun.org.glassfish.gmbal.Description;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by ildar on 23.06.2016.
 */
@Entity
@Table(name="Detail",schema="dbo", catalog="SPYTNIK")
@Description(value = "Детали")
public class tableDetailEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Description(value = "id")
    private Integer id;

    @Basic
    @Column(name = "name_detail")
    @Description(value = "Наименование детали")
    private String nameDetail;

    @Basic
    @Column(name = "price_of_detail")
    @Description(value = "цена детали")
    private Integer priceOfDetail;

    @Basic
    @Column(name = "unit")
    @Description(value = "Ед. измерения")
    private String unit;

    @OneToMany(mappedBy = "detail")
    private Set<tableAccountEntity> accountEntities = new HashSet<tableAccountEntity>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNameDetail() {
        return nameDetail;
    }

    public void setNameDetail(String nameDetail) {
        this.nameDetail = nameDetail;
    }

    public int getPriceOfDetail() {
        return priceOfDetail;
    }

    public void setPriceOfDetail(int priceOfDetail) {
        this.priceOfDetail = priceOfDetail;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        tableDetailEntity that = (tableDetailEntity) o;

        if (id != that.id) return false;
        if (priceOfDetail != that.priceOfDetail) return false;
        if (nameDetail != null ? !nameDetail.equals(that.nameDetail) : that.nameDetail != null) return false;
        if (unit != null ? !unit.equals(that.unit) : that.unit != null) return false;

        return true;
    }

    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (nameDetail != null ? nameDetail.hashCode() : 0);
        result = 31 * result + (priceOfDetail != null ? priceOfDetail.hashCode() : 0);
        result = 31 * result + (unit != null ? unit.hashCode() : 0);
        return result;
    }
}
