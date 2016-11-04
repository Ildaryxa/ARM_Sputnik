package LoaderForm;

import controller.UserInputViewController;
import enumType.FormType;
import javafx.stage.Stage;
import model.tableUsersEntity;

import java.io.IOException;

/**
 * Created by ildar on 11.10.2016.
 */
public class ShowForm {

    public static boolean showUserInputView(tableUsersEntity user, FormType type, String title, Stage stage){
        try {
            Form form = new Form();
            UserInputViewController controller = form.getLoader("view/user_input.view.fxml", title, false, stage).getController();
            controller.setDialogStage(form.getStage(), type);
            controller.setUser(user);
            form.showForm();
            return controller.isOkClicked();
        }catch (IOException ex){
            ex.printStackTrace();
            return false;
        }
    }
}
