package model;

import javax.persistence.*;

/**
 * Created by User on 28.06.2016.
 */
@Entity
@Table(name = "field", schema = "dbo", catalog = "sputnik")
public class tableFieldEntity {

    @Id
    @Column(name = "id")
    private Integer id;

    @Basic
    @Column(name = "fieldName")
    private String fieldName;

    @Basic
    @Column(name = "tableName")
    private String tableName;

    @Basic
    @Column(name = "fieldType")
    private String fieldType;

    @Basic
    @Column(name = "transl")
    private String translate;


    public Integer getId() {
        return id;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getTableName() {
        return tableName;
    }

    public String getFieldType() {
        return fieldType;
    }

    public String getTranslate() {
        return translate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        tableFieldEntity that = (tableFieldEntity) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (fieldName != null ? !fieldName.equals(that.fieldName) : that.fieldName != null) return false;
        if (tableName != null ? !tableName.equals(that.tableName) : that.tableName != null) return false;
        if (fieldType != null ? !fieldType.equals(that.fieldType) : that.fieldType != null) return false;
        if (translate != null ? !translate.equals(that.translate) : that.translate != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (fieldName != null ? fieldName.hashCode() : 0);
        result = 31 * result + (tableName != null ? tableName.hashCode() : 0);
        result = 31 * result + (fieldType != null ? fieldType.hashCode() : 0);
        result = 31 * result + (translate != null ? translate.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return translate;
    }
}
