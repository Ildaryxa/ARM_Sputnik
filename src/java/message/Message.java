package message;

import javafx.scene.control.Alert;
import javafx.stage.Stage;

/**
 * Created by ildar on 03.07.2016.
 */
public class Message {
    public static boolean errorMessage(String error, Stage stage){
        if (error.length() == 0){
            return true;
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(stage);
            alert.setTitle("Ошибка!");
            alert.setHeaderText("Пожалуйста, заполните данные");
            alert.setContentText(error);
            alert.showAndWait();
            return false;
        }
    }
}
