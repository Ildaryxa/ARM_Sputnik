package enumType;

/**
 * Created by ildar on 29.09.2016.
 */
public enum Status {
    fired ("Уволен"),
    holiday ("В отпуске"),
    hospital ("На больничном"),
    working ("Работает");

    private final String statusValue;
    Status(String statusValue){
        this.statusValue = statusValue;
    }

    public String getStatus(){
        return statusValue;
    }
}
