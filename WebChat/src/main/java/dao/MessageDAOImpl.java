
import java.lang.Exception;
import java.lang.Integer;
import java.lang.System;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.lang.Override;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

public class MessageDAOImpl implements MessageDAO {
    private static Logger logger = Logger.getLogger(MessageDAOImpl.class.getName());

    @Override
    public void add(Message message){
        try(Connection connection = DatabaseManager.getConnection();
            PreparedStatement statement = connection.prepareStatement("INSERT INTO message (id, text, date, user_id) values(DEFAULT, ?, ?, ?);")) {
            statement.setString(1, message.getText());
            statement.setDate(2, new java.sql.Date(message.getDate().getTime()));
            statement.setInt(3, message.getUserId());
            statement.executeQuery();
        } catch (SQLException e) {
            logger.error(e);
        }
    }

    @Override
    public void update(Message message){
        try(Connection connection = DatabaseManager.getConnection();
        PreparedStatement statement = connection.prepareStatement("UPDATE messages SET text = ?, date = ? WHERE id = ?;")){
            statement.setString(1, message.getText());
            statement.setDate(2, new java.sql.Date(message.getDate().getTime()));
            statement.setInt(3, message.getId());
            statement.executeQuery();
        } catch (SQLException e) {
            logger.error(e);
        }
    }

    @Override
    public void delete(Integer id){
        try(Connection connection = DatabaseManager.getConnection();
            PreparedStatement statement = connection.prepareStatement("DELETE FROM messages WHERE id = ?;")){
            statement.setInt(1, id);
            statement.executeQuery();
        } catch (SQLException e) {
            logger.error(e);
        }
    }

    @Override
    public Message selectById(Integer id){
        Message message = null;

        try(Connection connection = DatabaseManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM messages WHERE id = ?;")){
            preparedStatement.setInt(1, id);
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    message = new Message(resultSet.getInt("id"), resultSet.getInt("user_id"), resultSet.getString("text"), resultSet.getDate(3));
                }
            }
        } catch (SQLException e) {
            logger.error(e);
        }
        return message;
    }

    @Override
    public List<Message> selectAfter(Integer id){
        List<Message> list = new ArrayList<Message>();

        try(Connection connection = DatabaseManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM messages WHERE id > ?;")){
            preparedStatement.setInt(1, id);
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    list.add(new Message(resultSet.getInt("id"), resultSet.getInt("user_id"), resultSet.getString("text"), resultSet.getDate(3)));
                }
            }
        } catch (SQLException e) {
            logger.error(e);
        }

        return list;
    }
}