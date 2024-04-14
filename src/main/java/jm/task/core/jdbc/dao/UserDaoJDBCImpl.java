package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static jm.task.core.jdbc.util.Util.getConnection;

public class UserDaoJDBCImpl implements UserDao {
    public UserDaoJDBCImpl() {

    }

    Connection connection = getConnection();

    public void createUsersTable()  {
        try (Statement statement = connection.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS users ("
                    + "id INT PRIMARY KEY AUTO_INCREMENT,"
                    + "name VARCHAR(45),"
                    + "lastName VARCHAR(45),"
                    + "age INT"
                    + ")";
            statement.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void dropUsersTable() {
        try (Statement statement = connection.createStatement()) {
            String sql = "DROP TABLE IF EXISTS users";
            statement.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO users(name, lastname, age) VALUES (?, ?, ?)")) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setInt(3, age);

            preparedStatement.executeUpdate();
            System.out.println("User с именем — " + name + " добавлен в базу данных");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void removeUserById(long id) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM users WHERE id = ?")) {
            preparedStatement.setLong(1, id);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM users")) {

            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String lastName = resultSet.getString("lastName");
                Byte age = resultSet.getByte("age");

                User user = new User(name, lastName, age);
                userList.add(user);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return userList;
    }

    public void cleanUsersTable() {
        try (Statement statement = connection.createStatement();) {
            statement.executeUpdate("TRUNCATE TABLE users");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
