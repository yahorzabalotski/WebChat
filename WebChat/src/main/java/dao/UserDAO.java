import java.util.List;

public interface UserDAO{
    void add(User user);
    void update(User user);
    void delete(Integer id);
    User selectById(Integer id);
    List<User> selectAfter(Integer id);
}