import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

public class DatabaseUtil {
    private static Logger logger = Logger.getLogger(DatabaseUtil.class.getName());

    public static Integer getLastInsertId(Connection connection){
        try(PreparedStatement statement = connection.prepareStatement("SELECT LAST_INSERT_ID();");
            ResultSet resultSet = statement.executeQuery()){
            if(resultSet.next()){
                return new Integer(resultSet.getInt(1));
            }
        } catch (SQLException e) {
            logger.error(e);
        }
        return null;
    }
}