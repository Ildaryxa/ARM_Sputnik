package LoaderForm;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by ildar on 02.10.2016.
 */
public class Form {
    Stage dialogStage;
    public FXMLLoader getLoader(String path, String title, Boolean resize, Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource(path));
        AnchorPane rootLayout = loader.load();

        //stage
        dialogStage = new Stage();
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(stage);
        dialogStage.setResizable(resize);
        dialogStage.setScene(new Scene(rootLayout));
        dialogStage.setTitle(title);

        return loader;
    }

    public Stage getStage(){
        return dialogStage;
    }

    public void showForm(){
        dialogStage.showAndWait();
    }

}
