package packer;

import javafx.collections.ObservableList;
import model.modelOject.AboutDetail;
import model.modelOject.ListOfCarInRepair;
import model.modelOject.WorkAndDetail;
import model.tableAccountDataEntity;
import model.tableAccountEntity;
import orm.DataBase;

import java.util.Objects;

/**
 * Created by ildar on 10.10.2016.
 */
public class PackerInListWorkAndDetail {

    public static void addWorkAndDetail(DataBase db, tableAccountDataEntity accountData, ObservableList<WorkAndDetail> listWorkAndDetail) {

        ObservableList<tableAccountEntity> accountEntities = db.getAccount(accountData);
        for (tableAccountEntity account : accountEntities){
            boolean check = true;
            for (WorkAndDetail WandD : listWorkAndDetail){
                String nameEmployee = account.getEmployee().getName() + " " + account.getEmployee().getSurname();
                if (Objects.equals(WandD.employeeProperty().get(), nameEmployee) && Objects.equals(WandD.workProperty().get(), account.getWork().getNameWork())){
                    //если запись с работником и его выполненной работой уже существует
                    check = false;
                    if (account.getDetail() != null){
                        boolean prov = true;
                        for (AboutDetail aD : WandD.getListDetail()){
                            prov = !Objects.equals(aD.detailProperty().get(), account.getDetail().getNameDetail());
                        }
                        if (prov) {
                            AboutDetail aboutDetail = new AboutDetail(account.getDetail(), account.getAmount());
                            aboutDetail.setId(account.getId());
                            WandD.addDetail(aboutDetail);
                        }
                    }
                }
            }
            if (check){
                WorkAndDetail workAndDetail = new WorkAndDetail(account.getWork(), account.getEmployee());
                if (account.getDetail() != null){
                    AboutDetail aboutDetail = new AboutDetail(account.getDetail(), account.getAmount());
                    aboutDetail.setId(account.getId());
                    workAndDetail.addDetail(aboutDetail);
                }else {
                    workAndDetail.setId(account.getId());
                }
                listWorkAndDetail.add(workAndDetail);
            }
        }
    }
}
