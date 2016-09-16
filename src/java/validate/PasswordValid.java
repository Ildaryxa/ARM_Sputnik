package validate;

import javafx.scene.control.PasswordField;

/**
 * Created by ildar on 30.06.2016.
 */
public class PasswordValid extends PasswordField {

    public PasswordValid(){
        this.setPromptText("Максимум 30 символов");

        textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() >30){
                setText(oldValue);
            }
        });
    }

    @Override
    public void replaceText(int start, int end, String text) {
        if (!text.matches("[а-яА-Я\\s]")){
            super.replaceText(start, end, text);
        }
    }

    @Override
    public void replaceSelection(String replacement) {
        super.replaceSelection(replacement);
    }
}
