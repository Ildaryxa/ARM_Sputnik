package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import message.Message;
import model.tableDetailEntity;
import model.tableWorkEntity;
import orm.DataBase;
import validate.Check;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by ildar on 27.09.2016.
 */
public class DetailViewController implements Initializable {

    private enum actionOnDetail{
        changeRecord,
        addRecord
    }

    private static DataBase db;
    private tableDetailEntity detail;
    private actionOnDetail action;
    private static Stage stage;
    private int idOldRecord;

    @FXML TableView<tableDetailEntity> tbDetail;
    @FXML TableColumn<tableDetailEntity, Integer> tbcId;
    @FXML TableColumn<tableDetailEntity, String> tbcNameDetail;
    @FXML TableColumn<tableDetailEntity, Integer> tbcPriceDetail;
    @FXML TableColumn<tableDetailEntity, String> tbcUnit;
    @FXML TextField tfNameDetail;
    @FXML TextField tfNewPrice;
    @FXML TextField tfUnit;
    @FXML Button btAddDetail;

    static {
        db = DataBase.INSTANCE;
    }

    void setDialogStage(Stage dialogStage) {
        stage = dialogStage;
    }

    public DetailViewController() {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        action = actionOnDetail.addRecord;
        tbcId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tbcId.setVisible(false);
        tbcNameDetail.setCellValueFactory(new PropertyValueFactory<>("nameDetail"));
        tbcPriceDetail.setCellValueFactory(new PropertyValueFactory<>("priceOfDetail"));
        tbcUnit.setCellValueFactory(new PropertyValueFactory<>("unit"));
        tbDetail.setItems(db.getDetails());

        btAddDetail.setOnAction(event -> addDetail());
        ContextMenu ctMenu = new ContextMenu();
        MenuItem miChange = new MenuItem("Изменить запись");
        miChange.setOnAction(event -> {
            if (!tbDetail.getSelectionModel().isEmpty())
                changeDetailRecord();
        });
        MenuItem miDelete = new MenuItem("Удалить запись");
        miDelete.setOnAction(event -> {
            if (!tbDetail.getSelectionModel().isEmpty())
                deleteDetailRecord();
        });
        ctMenu.getItems().addAll(miChange, miDelete);
        tbDetail.setContextMenu(ctMenu);
    }

    private void deleteDetailRecord() {
        int number = tbDetail.getSelectionModel().getSelectedIndex();
        idOldRecord = tbcId.getCellData(number);
        String nameOldRecord = tbcNameDetail.getCellData(number);
        int priceOldRecord = tbcPriceDetail.getCellData(number);
        String unit = tbcUnit.getCellData(number);

        detail = new tableDetailEntity();
        detail.setId(idOldRecord);
        detail.setNameDetail(nameOldRecord);
        detail.setPriceOfDetail(priceOldRecord);
        detail.setUnit(unit);

        db.deleteDetail(detail);
        tbDetail.getItems().clear();
        tbDetail.setItems(db.getDetails());
    }

    private void changeDetailRecord() {
        int number = tbDetail.getSelectionModel().getSelectedIndex();
        idOldRecord = tbcId.getCellData(number);
        String nameOldRecord = tbcNameDetail.getCellData(number);
        int priceOldRecord = tbcPriceDetail.getCellData(number);
        String unitOldRecord = tbcUnit.getCellData(number);

        tfNameDetail.setText(nameOldRecord);
        tfNewPrice.setText(String.valueOf(priceOldRecord));
        tfUnit.setText(unitOldRecord);
        action = actionOnDetail.changeRecord;
    }

    private void addDetail() {
        if (isValid()){
            detail = new tableDetailEntity();
            detail.setNameDetail(tfNameDetail.getText().trim());
            detail.setPriceOfDetail(Integer.valueOf(tfNewPrice.getText().trim()));
            detail.setUnit(tfUnit.getText().trim());
            if (action == actionOnDetail.addRecord){
                db.addDetail(detail);
            }else{
                detail.setId(idOldRecord);
                db.updateDetail(detail);
                action = actionOnDetail.addRecord;
            }
            tfNameDetail.clear();
            tfNewPrice.clear();
            tfUnit.clear();
            tbDetail.getItems().clear();
            tbDetail.setItems(db.getDetails());
        }
    }

    private boolean isValid() {
        String errorMessage = "";
        if (tfNameDetail.getText() == null || tfNameDetail.getText().length()==0){
            errorMessage += "Введите деталь!\n";
        }else if (tfNameDetail.getText().length() > 50){
            errorMessage += "Наименование детали: кол-во символов > 50\n";
        }
        if (tfNewPrice.getText() == null || tfNewPrice.getText().length() == 0){
            errorMessage += "Введите цену детали!\n";
        }else if (tfNewPrice.getText().length()>6){
            errorMessage += "Цена детали: кол-во символов > 6\n";
        }else if (!Check.checkString(tfNewPrice.getText())){
            errorMessage += "Цена детали: ошибка в записи числа!\n";
        }
        if (tfUnit.getText() == null || tfUnit.getText().length()==0){
            errorMessage += "Введите единицу измерения (не более 5 символов)!\n";
        }else if (tfUnit.getText().length() > 5){
            errorMessage += "Наименование единицы измерения: кол-во символов > 5\n";
        }
        return Message.errorMessage(errorMessage, stage);
    }
}
