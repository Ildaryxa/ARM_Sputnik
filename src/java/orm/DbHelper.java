package orm;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.mysql.jdbc.Driver;

/**
 * Created by ildar on 17.10.2016.
 */
public enum DbHelper {
    INSTANCE;
    static private Connection connection;

    static {
        try {
            DriverManager.registerDriver(new Driver());

            connection = DriverManager.getConnection("jdbc:mysql://localhost/sputnik", "root", "root");
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void openConnection() throws SQLException {
        connection = DriverManager.getConnection("jdbc:mysql://localhost/sputnik", "root", "root");
    }

    public void closeConnection() throws SQLException {
        connection.close();
    }

    public ResultSet sendGet(String query) throws SQLException {
        Statement s = null;
        ResultSet rs = null;
        s = connection.createStatement();
        rs = s.executeQuery(query);
        return rs;
    }

    public Connection getConnection() {
        return connection;
    }



    public List<String> getList(String tableName, String fieldName) {
        String query = "SELECT DISTINCT " + fieldName + " FROM " + tableName + ";";
        try {
            openConnection();
            ResultSet rs = sendGet(query);
            List<String> res = new ArrayList<>();
            while (rs.next()) {
                String val = rs.getString(fieldName);
                res.add(val);
            }
            closeConnection();
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
