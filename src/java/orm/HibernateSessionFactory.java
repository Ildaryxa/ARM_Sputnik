package orm;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Created by ildar on 24.05.2016.
 */
public class HibernateSessionFactory {

    private static SessionFactory sessionFactory;

    public enum DBMS{
        MSSQL,
        PostgreSQL,
        MYSQL;

        public String getConnectionString(){
            switch (this){
                case MSSQL: return "hibernate/hibernate.mssql.cfg.xml";
                case PostgreSQL: return "";
                case MYSQL: return "";
            }
            return null;
        }
    }

    public static void buildSessionFactory(DBMS dbms) {
        try {
            System.out.println(dbms.getConnectionString());
            sessionFactory = new Configuration().configure(dbms.getConnectionString()).buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static Session getSession() {
        return sessionFactory.openSession();
    }

}
