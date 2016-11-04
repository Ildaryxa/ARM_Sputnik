package controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.tableCommentsEntity;
import model.tableUsersEntity;
import orm.DataBase;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Created by ildar on 11.10.2016.
 */
public class ReviewsController implements Initializable{

    @FXML ListView<String> lvReviews;

    private static DataBase db;
    private Stage stage;
    private ObservableList<tableCommentsEntity> comments;

    public ReviewsController() {
    }

    static {
        db = DataBase.INSTANCE;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        ContextMenu ctMenu = new ContextMenu();

        MenuItem miDeleteComment = new MenuItem("Удалить комментарий");
            miDeleteComment.setOnAction(event -> {
                if (!lvReviews.getSelectionModel().isEmpty())
                    deleteComment();
            });
        MenuItem miLockedUser = new MenuItem("Заблокировать пользователя");
            miLockedUser.setOnAction(event -> {
                if (!lvReviews.getSelectionModel().isEmpty())
                    lockedUser();
            });
        ctMenu.getItems().addAll(miDeleteComment, miLockedUser);
        lvReviews.setContextMenu(ctMenu);

        updateListView();
    }

    private void lockedUser() {
        tableUsersEntity user = comments.get(lvReviews.getSelectionModel().getSelectedIndex()).getAccountDataEntity().getLoginUser();
        user.setLocked(true);
        db.changeUser(user);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initOwner(stage);
        alert.setTitle("Пользователь заблокирован!");
        alert.setHeaderText("Удалить комментарий?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK)
            deleteComment();
    }

    private void deleteComment() {
        tableCommentsEntity delComment = comments.get(lvReviews.getSelectionModel().getSelectedIndex());
        db.deleteComment(delComment);
        updateListView();
    }

    private void updateListView() {
        comments = db.getComments();
        lvReviews.getItems().clear();
        String nameSurname;
        String firmMark;
        String com;
        String date;
        for (tableCommentsEntity comment : comments){
            nameSurname = comment.getAccountDataEntity().getLoginUser().getName() + " " + comment.getAccountDataEntity().getLoginUser().getSurname() + " : ";
            firmMark = comment.getAccountDataEntity().getCar().getFirm() + " " + comment.getAccountDataEntity().getCar().getMark() + " " + comment.getAccountDataEntity().getCar().getNumberCar() + "\n";
            com = comment.getText() + "\n";
            date = comment.getData().toString();
            lvReviews.getItems().add(nameSurname + firmMark + com + date);
        }
    }

    public void setStage(Stage stage){
        this.stage = stage;
    }
}
