package model.modelOject;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import model.tableDetailEntity;

/**
 * Created by ildar on 08.10.2016.
 */
public class AboutDetail {

    private int id;
    private final StringProperty detail;
    private final IntegerProperty priceDetail;
    private final IntegerProperty count;
    private final StringProperty unit;

    private tableDetailEntity detailEntity;

    public AboutDetail(tableDetailEntity detailEntity, int count) {
        this.detailEntity = detailEntity;

        this.detail = new SimpleStringProperty(detailEntity.getNameDetail());
        this.priceDetail = new SimpleIntegerProperty(detailEntity.getPriceOfDetail());
        this.count = new SimpleIntegerProperty(count);
        this.unit = new SimpleStringProperty(detailEntity.getUnit());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public StringProperty detailProperty() {
        return detail;
    }

    public IntegerProperty priceDetailProperty() {
        return priceDetail;
    }

    public IntegerProperty countProperty() {
        return count;
    }

    public StringProperty unitProperty() {
        return unit;
    }

    public tableDetailEntity getDetailTableEntity() {
        return detailEntity;
    }

}
