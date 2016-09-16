package orm;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.tableUsersEntity;

import java.util.Collection;
import java.util.Collections;

/**
 * Created by ildar on 28.06.2016.
 */
public enum  DataBase {
    INSTANCE;

    private static DataBaseConnection connection;

    static {
        connection = DataBaseConnection.INSTANCE;
    }

    private ObservableList users;

    private ObservableList addCollection(Collection collection) {
        ObservableList observableList = FXCollections.observableArrayList();
        observableList.addAll(collection);
        Collections.sort(observableList);
        return observableList;
    }

    public ObservableList getUsersData(){
        users = addCollection(connection.getUsersData());
        return users;
    }

    public void addUser(tableUsersEntity user){
        connection.addUser(user);
    }

    public tableUsersEntity getUser(String login) {
        return connection.getUser(login);
    }
}
