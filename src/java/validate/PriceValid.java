package validate;

import javafx.scene.control.TextField;

/**
 * Created by ildar on 25.09.2016.
 */
public class PriceValid extends TextField{

    public PriceValid() {
        textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length()>6){
                this.setText(oldValue);
            }
        });
    }

    @Override
    public void replaceText(int start, int end, String text) {
        if (!text.matches("[\\D]")){
            super.replaceText(start, end, text);
        }

    }

    @Override
    public void replaceSelection(String replacement) {
        super.replaceSelection(replacement);
    }
}
