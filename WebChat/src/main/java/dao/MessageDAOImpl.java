
import java.lang.Exception;
import java.lang.Integer;
import java.lang.System;
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
    }

    @Override
    public void update(Message message){
    }

    @Override
    public void delete(Integer id){

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
        return null;
    }
}