package model;

import com.sun.org.glassfish.gmbal.Description;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by ildar on 23.06.2016.
 */
@Entity
@Table(name="Car", schema="dbo", catalog="SPYTNIK")
@Description(value = "Машины")
public class tableCarEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", nullable = false)
    @Description(value = "id")
    private Integer id;


    @ManyToOne
    @JoinColumn(name = "id_firm")
    @Description(value = "Фирма")
    private tableFirmAutoEntity firm;

    @Basic
    @Column(name = "mark")
    @Description(value = "Марка")
    private String mark;

    @Basic
    @Column(name = "number_car")
    @Description(value = "Номер автомобиля")
    private String numberCar;

    @OneToMany(mappedBy = "car")
    private Set<tableAccountDataEntity> accountDataEntities = new HashSet<tableAccountDataEntity>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public tableFirmAutoEntity getFirm() {
        return firm;
    }

    public void setFirm(tableFirmAutoEntity firm) {
        this.firm = firm;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getNumberCar() {
        return numberCar;
    }

    public void setNumberCar(String numberCar) {
        this.numberCar = numberCar;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        tableCarEntity that = (tableCarEntity) o;

        if (id != that.id) return false;
        if (firm != that.firm) return false;
        if (mark != null ? !mark.equals(that.mark) : that.mark != null) return false;
        if (numberCar != null ? !numberCar.equals(that.numberCar) : that.numberCar != null) return false;

        return true;
    }

    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (firm != null ? firm.hashCode() : 0);
        result = 31 * result + (mark != null ? mark.hashCode() : 0);
        result = 31 * result + (numberCar != null ? numberCar.hashCode() : 0);
        return result;
    }
}
