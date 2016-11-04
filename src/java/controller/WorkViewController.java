package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import message.Message;
import model.tableWorkEntity;
import orm.DataBase;
import validate.Check;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by ildar on 22.09.2016.
 */
public class WorkViewController implements Initializable {

    private enum actionOnWork{
        changeRecord,
        addRecord
    }
    private static DataBase db;
    private tableWorkEntity newWork;
    private actionOnWork action;
    private int idOldRecord;

    @FXML TableView<tableWorkEntity> tbWork;
    @FXML TableColumn<tableWorkEntity, String> tbcNameWork;
    @FXML TableColumn<tableWorkEntity, Integer> tbcPriceWork;
    @FXML TableColumn<tableWorkEntity, Integer> tbcId;
    @FXML TextField tfNewWork;
    @FXML TextField tfNewPrice;
    @FXML Button btAddWork;

    private static Stage stage;

    static {
        db = DataBase.INSTANCE;
    }

    public WorkViewController() {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        action = actionOnWork.addRecord;
        tbcId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tbcId.setVisible(false);
        tbcNameWork.setCellValueFactory(new PropertyValueFactory<>("nameWork"));
        tbcPriceWork.setCellValueFactory(new PropertyValueFactory<>("priceOfWork"));
        tbWork.setItems(db.getWorks());
        btAddWork.setOnAction(event -> addWork());
        ContextMenu ctMenu = new ContextMenu();
        MenuItem miChange = new MenuItem("Изменить запись");
        miChange.setOnAction(event -> {
            if (!tbWork.getSelectionModel().isEmpty())
                changeWorkRecord();
        });
        MenuItem miDelete = new MenuItem("Удалить запись");
        miDelete.setOnAction(event -> {
            if (!tbWork.getSelectionModel().isEmpty())
                deleteWorkRecord();
        });
        ctMenu.getItems().addAll(miChange, miDelete);
        tbWork.setContextMenu(ctMenu);
    }

    private void deleteWorkRecord() {
        idOldRecord = tbcId.getCellData(tbWork.getSelectionModel().getSelectedIndex());
        String nameOldRecord = tbcNameWork.getCellData(tbWork.getSelectionModel().getSelectedIndex());
        int priceOldRecord = tbcPriceWork.getCellData(tbWork.getSelectionModel().getSelectedIndex());

        newWork = new tableWorkEntity();
        newWork.setId(idOldRecord);
        newWork.setNameWork(nameOldRecord);
        newWork.setPriceOfWork(priceOldRecord);

        db.deleteWork(newWork);
        tbWork.getItems().clear();
        tbWork.setItems(db.getWorks());
    }

    private void changeWorkRecord() {
        idOldRecord = tbcId.getCellData(tbWork.getSelectionModel().getSelectedIndex());
        String nameOldRecord = tbcNameWork.getCellData(tbWork.getSelectionModel().getSelectedIndex());
        int priceOldRecord = tbcPriceWork.getCellData(tbWork.getSelectionModel().getSelectedIndex());

        tfNewWork.setText(nameOldRecord);
        tfNewPrice.setText(String.valueOf(priceOldRecord));
        action = actionOnWork.changeRecord;
    }

    private void addWork() {
        if (isValid()) {
            newWork = new tableWorkEntity();
            newWork.setNameWork(tfNewWork.getText().trim());
            newWork.setPriceOfWork(Integer.valueOf(tfNewPrice.getText().trim()));
            if (action == actionOnWork.addRecord){
                db.addWork(newWork);
            } else{
                newWork.setId(idOldRecord);
                db.updateWork(newWork);
                action = actionOnWork.addRecord;
            }
            tfNewWork.clear();
            tfNewPrice.clear();
            tbWork.getItems().clear();
            tbWork.setItems(db.getWorks());
        }
    }

    void setDialogStage(Stage dialogStage){
        stage = dialogStage;
    }

    private boolean isValid() {
        String errorMessage = "";
        if (tfNewWork.getText() == null || tfNewWork.getText().length()==0){
            errorMessage += "Введите работу!\n";
        }else if (tfNewWork.getText().length() > 100){
            errorMessage += "Наименование работы: кол-во символов > 100\n";
        }
        if (tfNewPrice.getText() == null || tfNewPrice.getText().length() == 0){
            errorMessage += "Введите цену работы!\n";
        }else if (tfNewPrice.getText().length()>6){
            errorMessage += "Цена работы: кол-во символов > 6\n";
        }else if (!Check.checkString(tfNewPrice.getText())){
            errorMessage += "Цена работы: ошибка в записи числа!\n";
        }
        return Message.errorMessage(errorMessage, stage);
    }
}
