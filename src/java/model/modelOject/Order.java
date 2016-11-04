package model.modelOject;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import model.tableFieldEntity;

/**
 * Created by User on 30.06.2016.
 */
public class Order {

    private ObjectProperty<tableFieldEntity> field;
    private StringProperty order;

    public Order(tableFieldEntity field, String order) {
        this.field = new SimpleObjectProperty<>(field);
        this.order = new SimpleStringProperty(order);
    }

    public tableFieldEntity getField() {
        return field.get();
    }

    public ObjectProperty<tableFieldEntity> fieldProperty() {
        return field;
    }

    public void setField(tableFieldEntity field) {
        this.field.set(field);
    }

    public String getOrder() {
        return order.get();
    }

    public StringProperty orderProperty() {
        return order;
    }

    public void setOrder(String order) {
        this.order.set(order);
    }
}
