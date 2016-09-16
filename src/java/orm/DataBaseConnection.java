package orm;

import model.tableUsersEntity;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import java.util.Collection;


/**
 * Created by ildar on 28.06.2016.
 */
public enum  DataBaseConnection {
    INSTANCE;

    public Collection getUsersData(){
        try (Session session = HibernateSessionFactory.getSession()){
            return session.createCriteria(tableUsersEntity.class).list();
        }
    }

    public void addUser(tableUsersEntity user){
        try (Session session = HibernateSessionFactory.getSession()){
            session.beginTransaction();
            session.save(user);
            session.getTransaction().commit();
        }
    }

    public tableUsersEntity getUser(String login) {
        try (Session session = HibernateSessionFactory.getSession()){
            return (tableUsersEntity) session.createCriteria(tableUsersEntity.class).add(Restrictions.eq("login", login)).uniqueResult();
        }
    }
}
