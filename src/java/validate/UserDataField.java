package validate;

import javafx.scene.control.TextField;


/**
 * Created by ildar on 29.06.2016.
 */
public class UserDataField extends TextField {

    public UserDataField(){
        this.setPromptText("Максимум 20 символов");

        textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 20) {
                setText(oldValue);
            }
        });
    }

    @Override
    public void replaceText(int start, int end, String text) {
        if (getId().equals("loginField")){
            if (!text.matches("[а-яА-Я\\s\\W]")){
                super.replaceText(start, end, text);
            }
        }else{
            if (!text.matches("[\\s]")){
                super.replaceText(start, end, text);
            }
        }
    }
    @Override
    public void replaceSelection(String replacement) {
        super.replaceSelection(replacement);
    }
}
