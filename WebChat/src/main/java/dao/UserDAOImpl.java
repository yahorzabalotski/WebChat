import java.lang.Override;
import java.util.List;
import java.util.ArrayList;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.log4j.Logger;

public class UserDAOImpl implements UserDAO {
    private static Logger logger = Logger.getLogger(UserDAOImpl.class.getName());

    @Override
    public void add(User user){
        try(Connection connection = DatabaseManager.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO users (id, name) VALUES (DEFAULT , ?);")){
            preparedStatement.setString(1, user.getName());
            preparedStatement.executeQuery();
        } catch (SQLException e) {
            logger.error(e);
        }
    }

    @Override
    public void update(User user){
        try(Connection connection = DatabaseManager.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("UPDATE users SET name = ? WHERE id = ?;")){
            preparedStatement.setString(1, user.getName());
            preparedStatement.setInt(2, user.getId());
            preparedStatement.executeQuery();
        } catch (SQLException e){
            logger.error(e);
        }
    }

    @Override
    public void delete(Integer id){
        /*
        try(Connection connection = DatabaseManager.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM users WHERE id = ?;")) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeQuery();
        } catch (SQLException e){
            logger.error(e);
        }
        */
    }

    @Override
    public User selectById(Integer id){
        User user = null;
        try(Connection connection = DatabaseManager.getConnection();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE id = ?;")) {
            statement.setInt(1, id);
            try(ResultSet resultSet = statement.executeQuery()){
                if(resultSet.next()){
                    user = new User(id, resultSet.getString("name"));
                }
            }
        }
        catch (SQLException e){
            logger.error(e);
        }
        return user;
    }

    @Override
    public List<User> selectAfter(Integer id){
        List<User> list = new ArrayList<User>();

        try(Connection connection = DatabaseManager.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE id > ?")) {
            preparedStatement.setInt(1, id);
            try(ResultSet resultSet = preparedStatement.executeQuery()){
                while (resultSet.next()){
                    list.add(new User(resultSet.getInt(1), resultSet.getString(2)));
                }
            }
        } catch (SQLException e) {
            logger.error(e);
        }
        return list;
    }
}