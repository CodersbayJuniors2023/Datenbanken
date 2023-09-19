package connection;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class DatabaseQuery {
    public static void main(String[] args) {
        // db settings must be loaded from the properties file
        try (InputStream configFileInput = DatabaseQuery.class.getClassLoader()
                .getResourceAsStream("connection.properties")) {
            Properties properties = new Properties();
            // load property file
            properties.load(configFileInput);
            try {
                // establish connection
                Connection connection = DriverManager.getConnection(properties.getProperty("db.url"),
                        properties.getProperty("db.user"), properties.getProperty("db.password"));

                // create SQL statement
                Statement statement = connection.createStatement();

                String query = "SELECT * FROM guests;";
                // execute query
                ResultSet resultSet = statement.executeQuery(query);

                // print result
                while (resultSet.next()) {
                    int id = resultSet.getInt("guest_id");
                    String name = resultSet.getString("name");

                    System.out.println("ID: " + id + ", Name: " + name);
                }

            /*
                write a method which takes a parameter for the following statement.
                Execute it with a prepared statement.
                SELECT guest_id from guests where name=parameter
             */
                findAndPrintPersonIDByName(connection, "Storm");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void findAndPrintPersonIDByName(Connection connection, String name) {
        String findByNameQueryString = "SELECT guest_id from guests where name=?;";

        try {
            PreparedStatement findByName = connection.prepareStatement(findByNameQueryString);
            // set the parameter
            findByName.setString(1, name);
            ResultSet resultSet = findByName.executeQuery();

            while (resultSet.next()) {
                System.out.println(name + " has the ID: " + resultSet.getInt("guest_id"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
