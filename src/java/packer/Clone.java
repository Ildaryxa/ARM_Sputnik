package packer;

import model.tableUsersEntity;

/**
 * Created by ildar on 11.10.2016.
 */
public class Clone {

    public static tableUsersEntity cloneUsers(tableUsersEntity user){
        tableUsersEntity cloneUser = new tableUsersEntity();
        cloneUser.setName(user.getName());
        cloneUser.setSurname(user.getSurname());
        cloneUser.setLocation(user.getLocation());
        cloneUser.setLogin(user.getLogin());
        cloneUser.setPasswordHash(user.getPasswordHash());
        cloneUser.setSalt(user.getSalt());
        cloneUser.setAdmin(user.isAdmin());
        cloneUser.setLocked(user.isLocked());
        cloneUser.setDataRegistration(user.getDataRegistration());
        cloneUser.setPhoneNumber(user.getPhoneNumber());
        cloneUser.setEmail(user.getEmail());
        return cloneUser;
    }
}
