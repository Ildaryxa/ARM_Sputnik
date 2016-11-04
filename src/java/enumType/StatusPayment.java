package enumType;

/**
 * Created by ildar on 08.10.2016.
 */
public enum StatusPayment {
    paid ("Оплачено"),
    isNotPaid ("Не оплачено");
    private final String statusValue;

    StatusPayment(String statusValue){
        this.statusValue = statusValue;
    }

    public String getStatusPaymentValue(){
        return statusValue;
    }
}
