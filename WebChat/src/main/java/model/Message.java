import java.lang.String;
import java.util.Date;
import java.text.SimpleDateFormat;

public class Message{
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-M-yyyy HH:mm ");

    private Integer userId;
    private Integer id;
    private String text;
    private Date date;

    public Message(Integer id, Integer userId, String text, Date date){
        this.id = id;
        this.userId = userId;
        this.text = text;
        this.date = date;
    }

    public Integer getUserId(){
        return userId;
    }

    public void setUserId(Integer userId){
        this.userId = userId;
    }

    public Integer getId(){
        return id;
    }

    public void setId(Integer id){
        this.id = id;
    }

    public String getText(){
        return text;
    }

    public void setText(String text){
        this.text = text;
    }

    public Date getDate(){
        return date;
    }

    public void setDate(Date date){
        this.date = date;
    }
}