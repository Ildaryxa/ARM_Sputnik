package model.modelOject;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import model.tableFieldEntity;

/**
 * Created by User on 28.06.2016.
 */
public class Condition{
    private StringProperty fieldName;
    private ObjectProperty<tableFieldEntity> fieldProperty;
    private StringProperty critery;
    private StringProperty value;
    private StringProperty relation;

    public Condition() {
        fieldProperty = new SimpleObjectProperty<>();
        this.fieldName = new SimpleStringProperty();
        this.critery = new SimpleStringProperty();
        this.value = new SimpleStringProperty();
        this.relation = new SimpleStringProperty();
        fieldProperty.addListener(new ChangeListener<tableFieldEntity>() {
            @Override
            public void changed(ObservableValue<? extends tableFieldEntity> observable, tableFieldEntity oldValue, tableFieldEntity newValue) {
                setFieldName(newValue.getTranslate());
            }
        });
    }

    public Condition(tableFieldEntity field, String critery, String value, String relation) {
        //this.field = field;
        fieldProperty = new SimpleObjectProperty<>(field);
        this.fieldName = new SimpleStringProperty(field.getTranslate());
        this.critery = new SimpleStringProperty(critery);
        this.value = new SimpleStringProperty(value);
        this.relation = new SimpleStringProperty(relation);
        fieldProperty.addListener(new ChangeListener<tableFieldEntity>() {
            @Override
            public void changed(ObservableValue<? extends tableFieldEntity> observable, tableFieldEntity oldValue, tableFieldEntity newValue) {
                setFieldName(newValue.getTranslate());
            }
        });
    }

    public tableFieldEntity getFieldProperty() {
        return fieldProperty.get();
    }

    public ObjectProperty<tableFieldEntity> fieldPropertyProperty() {
        return fieldProperty;
    }

    public void setFieldProperty(tableFieldEntity fieldProperty) {
        this.fieldProperty.set(fieldProperty);
        //fieldName.set(fieldProperty.getTranslate());
    }

    public String getFieldName() {
        return fieldName.get();
    }

    public StringProperty fieldNameProperty() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName.set(fieldName);
    }

    public String getCritery() {
        return critery.get();
    }

    public StringProperty criteryProperty() {
        return critery;
    }

    public void setCritery(String critery) {
        this.critery.set(critery);
    }

    public String getValue() {
        return value.get();
    }

    public StringProperty valueProperty() {
        return value;
    }

    public void setValue(String value) {
        this.value.set(value);
    }

    public String getRelation() {
        return relation.get();
    }

    public StringProperty relationProperty() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation.set(relation);
    }


}
