package enumType;

/**
 * Created by ildar on 04.10.2016.
 */
public enum StatusCar {
    repair ("В ремонте"),
    renovated ("Отремонтировано"),
    issuedOwner ("Выдан владельцу");
    private final String statusValue;

    StatusCar(String statusValue){
        this.statusValue = statusValue;
    }

    public String getStatusValue(){
        return statusValue;
    }
}
