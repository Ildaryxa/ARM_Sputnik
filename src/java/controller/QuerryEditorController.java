package controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Pair;
import message.Message;
import model.modelOject.Condition;
import model.modelOject.Graph;
import model.modelOject.Order;
import model.tableFieldEntity;
import orm.DataBase;
import orm.DbHelper;
import validate.Check;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

/**
 * Created by ildar on 16.10.2016.
 */
public class QuerryEditorController implements Initializable {

    private Stage stage;
    private static DataBase db;
    private String SQLquery;

    @FXML RadioButton ascRBtn;
    @FXML RadioButton desRBtn;
    @FXML HBox criteryBox;

    @FXML TableView resultTable;
    @FXML TabPane pane;

    @FXML ListView<tableFieldEntity> allFields;
    @FXML ListView<tableFieldEntity> selectFields;
    @FXML TableView<Condition> conditionTable;
    @FXML TableColumn<Condition, String> nameFieldColumn;
    @FXML TableColumn<Condition, String> criteryColumn;
    @FXML TableColumn<Condition, String> valueColumn;
    @FXML TableColumn<Condition, String> relationColumn;
    @FXML ComboBox<tableFieldEntity> nameFieldCBox;
    @FXML ComboBox<String> relationCBox;

    @FXML ListView<tableFieldEntity> selectFieldsUnSort;
    @FXML TableColumn<Order, String> fieldColumn;
    @FXML TableColumn<Order, String> orderColumn;
    @FXML TableView<Order> selectFieldsSort;

    static {
        db = DataBase.INSTANCE;
    }

    public QuerryEditorController() {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        allFields.setItems(db.getFields());
        selectFields.setItems(FXCollections.observableArrayList());
        nameFieldCBox.setItems(db.getFields());
        nameFieldCBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (!criteryBox.getChildren().isEmpty()) criteryBox.getChildren().clear();
            setNewCriteryBox(newValue);
        });
        nameFieldCBox.getSelectionModel().selectFirst();

        addRelations();

        initConditionTable();

        ContextMenu menu = new ContextMenu();
        MenuItem miDel = new MenuItem("Удалить запись");
        miDel.setOnAction(event -> {
            if (!conditionTable.getSelectionModel().isEmpty()){
                conditionTable.getItems().remove(conditionTable.getSelectionModel().getSelectedItem());
            }
        });
        menu.getItems().add(miDel);
        conditionTable.setContextMenu(menu);

        fieldColumn.setCellValueFactory(new PropertyValueFactory<>("field"));
        orderColumn.setCellValueFactory(new PropertyValueFactory<>("order"));

        selectFieldsSort.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Order>() {
            @Override
            public void changed(ObservableValue<? extends Order> observable, Order oldValue, Order newValue) {
                if (newValue == null) return;
                if (newValue.getOrder().equals("ASC")) ascRBtn.setSelected(true);
                else desRBtn.setSelected(true);
            }
        });

        ascRBtn.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                Order order = selectFieldsSort.getSelectionModel().getSelectedItem();
                if (order == null) return;

                if (newValue) order.setOrder("ASC");
                else order.setOrder("DESC");

                selectFieldsSort.setItems(selectFieldsSort.getItems());
            }
        });
    }

    private void addRelations() {
        ObservableList<String> list = FXCollections.observableArrayList();
        list.add("И");
        list.add("ИЛИ");
        relationCBox.setItems(FXCollections.observableArrayList(list));
        relationCBox.getSelectionModel().selectFirst();
    }

    private void initConditionTable() {
        nameFieldColumn.setCellValueFactory(cellData -> cellData.getValue().fieldNameProperty());
        criteryColumn.setCellValueFactory(cellData -> cellData.getValue().criteryProperty());
        valueColumn.setCellValueFactory(cellData -> cellData.getValue().valueProperty());
        relationColumn.setCellValueFactory(cellData -> cellData.getValue().relationProperty());
    }


    public void addField(ActionEvent actionEvent) {
        tableFieldEntity item = allFields.getSelectionModel().getSelectedItem();
        if (item == null || item.getTableName() == null) return;

        allFields.getItems().remove(item);
        selectFields.getItems().add(item);
        selectFieldsUnSort.getItems().add(item);
    }


    public void delField(ActionEvent actionEvent) {
        tableFieldEntity item = selectFields.getSelectionModel().getSelectedItem();
        if (item == null) return;

        selectFields.getItems().remove(item);
        allFields.getItems().add(item);
        if (selectFieldsUnSort.getItems().contains(item)) {
            selectFieldsUnSort.getItems().remove(item);
        } else {
            int index = -1;
            for (Order order : selectFieldsSort.getItems()) {
                if (order.getField().equals(item)) {
                    index = selectFieldsSort.getItems().indexOf(order);
                    break;
                }
            }
            selectFieldsSort.getItems().remove(index);
        }
    }

    public void addAllField(ActionEvent actionEvent) {
        while (allFields.getItems().size() != 0) {
            allFields.getSelectionModel().select(0);
            addField(actionEvent);
        }
    }

    public void delAllField(ActionEvent actionEvent) {
        while (selectFields.getItems().size() != 0) {
            selectFields.getSelectionModel().select(0);
            delField(actionEvent);
        }
    }

    public void setStage(Stage stage){
        this.stage = stage;
    }

    private void setNewCriteryBox(tableFieldEntity field) {
        String type = field.getFieldType();

        switch (type){
            case "S":
                addStringCritery(field);
                break;
            case "N":
                addIntegerCritery(field);
                break;
            case "D":
                addDataCritery();
                break;
        }
    }

    private void addDataCritery() {
        VBox left = new VBox();
        left.getChildren().add(new Label("Критерий"));

        ComboBox<String> criteryBox = new ComboBox<>(FXCollections.observableArrayList("=","!=","<",">","<=",">="));
        criteryBox.getSelectionModel().selectFirst();
        left.getChildren().add(criteryBox);

        VBox right = new VBox();
        right.getChildren().add(new Label("Значение"));
        DatePicker picker = new DatePicker();
        picker.setValue(LocalDate.now());
        right.getChildren().add(picker);

        this.criteryBox.getChildren().add(left);
        this.criteryBox.getChildren().add(right);
    }

    private void addIntegerCritery(tableFieldEntity field) {
        VBox left = new VBox();
        left.getChildren().add(new Label("Критерий"));

        ObservableList<String> list = FXCollections.observableArrayList("=","!=","<",">","<=",">=");
        if (field.getFieldName().equals("isAdmin")){
            list.remove(2,6);
        }
        ComboBox<String> criteryBox = new ComboBox<>(FXCollections.observableArrayList(list));

        criteryBox.getSelectionModel().selectFirst();
        left.getChildren().add(criteryBox);

        VBox right = new VBox();
        right.getChildren().add(new Label("Значение"));
        TextField valueField = new TextField();
        right.getChildren().add(valueField);

        this.criteryBox.getChildren().add(left);
        this.criteryBox.getChildren().add(right);
    }

    private void addStringCritery(tableFieldEntity field) {
        VBox left = new VBox();
        left.getChildren().add(new Label("Критерий"));

        ObservableList<String> list = FXCollections.observableArrayList("=","<>","содержит");
        if (field.getFieldName().equals("text")) {
            list.remove(0,2);
        }
        ComboBox<String> criteryBox = new ComboBox<>(FXCollections.observableArrayList(list));
        left.getChildren().add(criteryBox);

        VBox right = new VBox();
        right.getChildren().add(new Label("Значение"));

        List<String> valList = DbHelper.INSTANCE.getList(field.getTableName(), field.getFieldName());
        ComboBox<String> valueBox = new ComboBox(FXCollections.observableArrayList(valList));
        valueBox.getSelectionModel().selectFirst();
        right.getChildren().add(valueBox);

        this.criteryBox.getChildren().add(left);
        this.criteryBox.getChildren().add(right);

        criteryBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals("содержит") && oldValue!=null && !oldValue.equals("содержит")) {
                TextField valueField = new TextField();
                VBox box = ((VBox) this.criteryBox.getChildren().get(1));
                box.getChildren().remove(1);
                box.getChildren().add(valueField);
            } else if (newValue.equals("содержит") && oldValue==null){
                TextField valueField = new TextField();
                VBox box = ((VBox) this.criteryBox.getChildren().get(1));
                box.getChildren().remove(1);
                box.getChildren().add(valueField);
            }

            if (!newValue.equals("содержит") && oldValue!=null && oldValue.equals("содержит")) {
                VBox box = ((VBox) this.criteryBox.getChildren().get(1));
                box.getChildren().remove(1);
                box.getChildren().add(valueBox);
            }
        });
        criteryBox.getSelectionModel().selectFirst();
    }

    public void addCondition(ActionEvent actionEvent) {
        Condition condition = new Condition();
        condition.setFieldProperty(nameFieldCBox.getValue());
        condition.setFieldName(nameFieldCBox.getValue().getTranslate());

        VBox box1 = ((VBox) this.criteryBox.getChildren().get(0));
        ComboBox<String> criteryBox = (ComboBox<String>) box1.getChildren().get(1);
        condition.setCritery(criteryBox.getValue());

        VBox box2 = ((VBox) this.criteryBox.getChildren().get(1));
        switch (nameFieldCBox.getValue().getFieldType()){
            case "S":
                if (criteryBox.getValue().equals("содержит")){
                    TextField field = (TextField) box2.getChildren().get(1);
                    condition.setValue(field.getText().trim());
                }else{
                    ComboBox<String> valueBox = (ComboBox<String>) box2.getChildren().get(1);
                    condition.setValue(valueBox.getValue());
                }
                break;
            case "N":
                TextField field = (TextField) box2.getChildren().get(1);
                if (!Check.checkString(field.getText().trim())){
                    Message.errorMessage("Записано нечисловое значение!", stage);
                    return;
                }
                condition.setValue(field.getText().trim());
                break;
            case "D":
                DatePicker picker = (DatePicker) box2.getChildren().get(1);
                condition.setValue(picker.getValue().toString());
                break;
        }

        condition.setRelation(relationCBox.getValue());

        for (Condition cond : conditionTable.getItems()){
            if (cond.getFieldName().equals(condition.getFieldName()) && cond.getCritery().equals(condition.getCritery()) &&
                    cond.getValue().equals(condition.getValue()) && cond.getRelation().equals(condition.getRelation())){
                return;
            }
        }
        conditionTable.getItems().add(condition);
    }

    public void addFieldSort(ActionEvent actionEvent) {
        tableFieldEntity field = selectFieldsUnSort.getSelectionModel().getSelectedItem();
        if (field != null) {
            selectFieldsSort.getItems().add(new Order(field, "ASC"));
            selectFieldsUnSort.getItems().remove(field);
        }

    }

    public void addAllFieldSort(ActionEvent event){
        while (selectFieldsUnSort.getItems().size()!=0){
            selectFieldsUnSort.getSelectionModel().select(0);
            addFieldSort(event);
        }
    }

    public void delFieldSort(ActionEvent actionEvent) {
        Order order = selectFieldsSort.getSelectionModel().getSelectedItem();
        if (order != null) {
            selectFieldsSort.getItems().remove(order);
            selectFieldsUnSort.getItems().add(order.getField());
        }
    }

    public void delAllFieldSort(ActionEvent actionEvent){
        while (selectFieldsSort.getItems().size()!=0){
            selectFieldsSort.getSelectionModel().select(0);
            delFieldSort(actionEvent);
        }
    }

    public void execute(ActionEvent actionEvent) throws SQLException {
        resultTable.getColumns().clear();
        resultTable.getItems().clear();

        DbHelper.INSTANCE.openConnection();
        ResultSet rs = null;
        Connection connection = DbHelper.INSTANCE.getConnection();

        createSQLQuerry();

        PreparedStatement preparedStatement = connection.prepareStatement(SQLquery);
        for (int i = 0; i < conditionTable.getItems().size(); i++) {
            String value = getContent(i);
            preparedStatement.setString(i+1, value);
        }
        rs = preparedStatement.executeQuery();
        System.out.println(preparedStatement.toString());
        String sql = preparedStatement.toString();
        for(int i = 0 ; i < rs.getMetaData().getColumnCount(); i++){
            final int j = i;
            TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1));
            col.setText(selectFields.getItems().get(i).getTranslate());
            col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList,String>,ObservableValue<String>>(){
                public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                    return new SimpleStringProperty(param.getValue().get(j).toString());
                }
            });
            resultTable.getColumns().add(col);
        }
        ObservableList data = FXCollections.observableArrayList();
        while(rs.next()){
            //Iterate Row
            ObservableList<String> row = FXCollections.observableArrayList();
            for(int i=1 ; i<=rs.getMetaData().getColumnCount(); i++){
                //Iterate Column
                row.add(rs.getString(i));
            }
            data.add(row);

        }
        resultTable.setItems(data);
        DbHelper.INSTANCE.closeConnection();
    }

    private String getContent(int i) {
        return conditionTable.getItems().get(i).getCritery().equals("содержит") ?
                "%" + conditionTable.getItems().get(i).getValue() + "%" :
                conditionTable.getItems().get(i).getValue();
    }

    private void createSQLQuerry() throws SQLException {
        if (selectFields.getItems().size() == 0) {
            new Alert(Alert.AlertType.ERROR, "Поля не выбраны").show();
            return;
        }
        String selectClause = generateSelectClause();
        Pair<List<String>, List<String>> pair;
        String whereClause = null;
        try {
            pair = createWhereClause();

            whereClause = generateWhereClause(pair.getValue());
        } catch (Exception e) {
            Message.errorMessage("Ошибка в условиях", stage);
            return;
        }
        String fromClause = generateFromClause(pair.getKey());
        String orderClause = generateOrderClause();

        SQLquery = "SELECT DISTINCT\n" + selectClause +
                "FROM\n" + fromClause +
                (whereClause.length() < 3 ? "" : "WHERE\n" + whereClause) +
                (orderClause.length() == 0 ? "" : "ORDER BY\n" + orderClause) + ";";
    }

    public void showSql(ActionEvent actionEvent) throws SQLException {
        createSQLQuerry();
        Message.notification("Созданный запрос", SQLquery, Alert.AlertType.INFORMATION, stage);
    }

    private String generateOrderClause() {
        if (selectFieldsSort.getItems().size() == 0) return "";
        String res = "";
        for (int i = 0; i < selectFieldsSort.getItems().size(); i++) {
            Order order = selectFieldsSort.getItems().get(i);
            res += "\t" + order.getField().getTableName() + "." +
                    order.getField().getFieldName() + " " +
                    order.getOrder() +
                    (i < selectFieldsSort.getItems().size() - 1 ? ",\n" : "");
        }
        return res;
    }

    private String generateFromClause(List<String> tables) throws SQLException {
        String res = "";
        for (int i = 0; i < tables.size(); i++)
            res += "\t" + tables.get(i) + (i < tables.size() - 1 ? "," : "") + "\n";
        return res;
    }

    private String generateWhereClause(List<String> links) throws Exception {
        String left = generateConditionClause();
        String right = generateLinkClause(links);
        createWhereClause();
        String res = "";
        if (conditionTable.getItems().size() != 0) res += left;
        if (conditionTable.getItems().size() != 0 && right.length() > 3) res += " AND\n";
        if (right.length() > 3) res += right;
        return res + "\n";
    }

    private String generateLinkClause(List<String> links) throws SQLException {
        String res = "";
        for (int i = 0; i < links.size(); i++)
            res += (i != 0 ? " AND\n\t" : "\t") + links.get(i);

        return res;
    }

    private String generateConditionClause() throws Exception {
        String res = "(";
        ObservableList<Condition> conditions = conditionTable.getItems();

        for (int i = 0; i < conditions.size(); i++) {
            Condition cond = conditions.get(i);
            String item = i != 0 ? "\t(" : "";
            item += cond.getFieldProperty().getTableName() + "." + cond.getFieldProperty().getFieldName() + " ";
            switch (cond.getFieldProperty().getFieldType()) {
                case "S":
                    item += cond.getCritery().equals("<>") ? "<> " : "LIKE ";
                    item += "?)";
                    break;
                case "N":
                    item += cond.getCritery() + " ?)";
                    break;
                case "D":
                    item += cond.getCritery() + " ?)";
                    break;
            }

            res += item;
            if (i < conditions.size() - 1) res += " " +
                    (cond.getRelation().equals("ИЛИ") ? "OR" : "AND") + "\n";
        }
        return (res.contains("OR") ?
                ("\t(" + res + ")") :
                ("\t" + res));
    }

    public Pair<List<String>, List<String>> createWhereClause() throws Exception {
        List<String> firstTableList = generateFirstTableList();
        Graph graph = Graph.build();
        List<String> finishTableList = generateFinishTableList(graph, firstTableList);
        System.out.println(finishTableList); //// TODO: 18.10.2016

        List<String> links = generateLinkList(graph, finishTableList);
        return new Pair<>(finishTableList, links);
    }

    private List<String> generateLinkList(Graph graph, List<String> tables) {
        List<String> links = new ArrayList<>();

        for (int i = 0; i < tables.size() - 1; i++) {
            for (int j = i + 1; j < tables.size(); j++) {
                int indI = graph.getTitle().indexOf(tables.get(i));
                int indJ = graph.getTitle().indexOf(tables.get(j));
                if (graph.getLinks()[indI][indJ] != null) links.add(graph.getLinks()[indI][indJ]);
            }
        }
        return links;
    }

    private List<String> generateFirstTableList() throws Exception {
        List<String> tables = new ArrayList<>();
        for (tableFieldEntity field : selectFields.getItems()) {
            String table = field.getTableName();
            if (!tables.contains(table)) tables.add(table);
        }
        try {
            for (Condition condition : conditionTable.getItems()) {
                String table = condition.getFieldProperty().getTableName();
                if (!tables.contains(table)) tables.add(table);
            }
        } catch (Exception e) {
            throw new Exception();
        }

        return tables;
    }

    private List<String> generateFinishTableList(Graph graph, List<String> srcTables) {
        List<String> fhsTables = new ArrayList<>(srcTables);

        for (int i = 0; i < srcTables.size() - 1; i++) {
            for (int j = i + 1; j < srcTables.size(); j++) {
                String t1 = srcTables.get(i);
                String t2 = srcTables.get(j);
                List<String> pathTables = findPath(graph, t1, t2);
                pathTables.stream().filter(table -> !fhsTables.contains(table)).forEach(fhsTables::add);
            }
        }

        return fhsTables;
    }

    private List<String> findPath(Graph graph, String t1, String t2) {

        //изначально все вершины не просмотрены
        Map<String, Boolean> visited = new HashMap<>();
        for (String table : graph.getTitle())
            visited.put(table, false);

        //первую вершину метим и начинаем обход с нее
        visited.put(t1, true);
        return findPath(graph, t1, t2, visited);
    }

    private List<String> findPath(Graph graph, String t1, String t2, Map<String, Boolean> visited) {
        if (t1.equals(t2)) {
            List<String> tables = new ArrayList<>();
            tables.add(t1);
            return tables;
        } else {
            int index = graph.getTitle().indexOf(t1);

            for (int i = 0; i < graph.getTitle().size(); i++) {
                String table = graph.getTitle().get(i);
                if (visited.get(table) || graph.getLinks()[index][i] == null) continue;

                visited.put(table, true);
                List<String> path = findPath(graph, table, t2, visited);
                visited.put(table, false);
                if (path != null) {
                    path.add(table);
                    return path;
                }
            }
        }
        return null;
    }

    private String generateSelectClause() {
        ObservableList<tableFieldEntity> fieldData = selectFields.getItems();
        String res = "";
        for (int i = 0; i < fieldData.size(); i++) {
            tableFieldEntity field = fieldData.get(i);
            res += "\t" + field.getTableName() + "." + field.getFieldName() + " AS '" + field.getTranslate() + "'";
            if (i < fieldData.size() - 1) {
                res += ", \n";
            } else {
                res += "\n";
            }
        }
        return res;
    }

    public void btnCancel(ActionEvent action){
        stage.close();
    }
}
