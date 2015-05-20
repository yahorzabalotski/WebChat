
import java.util.List;

public interface MessageDAO{
    void add(Message message);
    void update(Message message);
    void delete(Integer id);
    Message selectById(Integer id);
    List<Message> selectAfter(Integer id);
}