import java.lang.String;
import java.util.Date;
import java.text.SimpleDateFormat;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class Message{
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-M-yyyy HH:mm ");
    private String text;
    private String author;
    private Date date;
    private String id;

    JSONParser jsonParser = new JSONParser();

    Message(){
        this.date = new Date();
        this.author = "";
        this.text = "";
        this.id = "";
    }

    Message(Date date, String author, String text, String id){
        this.date = date;
        this.author = author;
        this.text = text;
        this.id = id;
    }

    public void setDate(Date date){
        this.date = date;
    }

    public Date getDate(){
        return date;
    }

    public void setAuthor(String author){
        this.author = author;
    }

    public String getAuthor(){
        return this.author;
    }

    public void setText(String text){
        this.text = text;
    }

    public String getText(){
        return text;
    }

    public String getId(){
        return id;
    }

    public void setId(String id){
        this.id = id;
    }

    public String getJSONValue(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("date", dateFormat.format(date));
        jsonObject.put("author", author);
        jsonObject.put("text", text);
        jsonObject.put("id", id);
        return jsonObject.toJSONString();
    }

    public void parseJSONValue(String json) throws ParseException, java.text.ParseException{
        System.out.println(json);
        JSONObject object = getJSONObject(json);
        date = dateFormat.parse((String) object.get("date"));
        author = (String) object.get("author");
        text = (String) object.get("text");
        id = (String) object.get("id");
    }


    private JSONObject getJSONObject(String json) throws ParseException {
        return (JSONObject) jsonParser.parse(json.trim());
    }
}