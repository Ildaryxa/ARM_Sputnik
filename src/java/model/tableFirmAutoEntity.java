package model;

import com.sun.org.glassfish.gmbal.Description;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by ildar on 23.06.2016.
 */
@Entity
@Table(name="Firm_auto", schema="dbo", catalog="SPYTNIK")
public class tableFirmAutoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", nullable = false)
    @Description(value = "id")
    private Integer id;

    @Basic
    @Column(name = "firm")
    private String firm;

    @OneToMany(mappedBy = "firm")
    private Set<tableCarEntity> accountDataEntities = new HashSet<tableCarEntity>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirm() {
        return firm;
    }

    public void setFirm(String firm) {
        this.firm = firm;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        tableFirmAutoEntity that = (tableFirmAutoEntity) o;

        if (id != that.id) return false;
        if (firm != null ? !firm.equals(that.firm) : that.firm != null) return false;

        return true;
    }

    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (firm != null ? firm.hashCode() : 0);
        return result;
    }
}
