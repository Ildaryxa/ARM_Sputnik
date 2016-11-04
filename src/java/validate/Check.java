package validate;

/**
 * Created by ildar on 11.10.2016.
 */
public class Check {
    public static boolean checkString(String string) {
        try {
            Integer.parseInt(string);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
