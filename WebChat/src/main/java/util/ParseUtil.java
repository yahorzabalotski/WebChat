
import java.lang.Integer;
import java.lang.String;
import java.lang.System;
import java.util.List;
import java.util.Date;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class ParseUtil {

    private static UserDAOImpl userDAOImpl = new UserDAOImpl();

    public static String messageToJSON(List<Message> messages) {
        JSONArray jsonArray = new JSONArray();
        for(Message message: messages) {
            jsonArray.add(messageToJSON(message));
        }
        return jsonArray.toJSONString();
    }

    public static String messageToJSON(Message message) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("text", message.getText());
        jsonObject.put("author", userDAOImpl.selectById(message.getUserId()).getName());
        jsonObject.put("id", message.getId());
        // put date
        return (String) jsonObject.toJSONString();
    }

    public static Message parseMessage(String json){
        try {
            JSONObject obj = getJSONObject(json);
            return new Message(Integer.parseInt( (String) obj.get("id")), new Integer(0), (String) obj.get("text"), new Date());
        } catch (ParseException e) {
            return null;
        }
    }

    public static String getUserName(String json) {
        try {
            JSONObject obj = getJSONObject(json);
            return (String) obj.get("name");
        } catch (ParseException e) {
            return null;
        }
    }

    public static JSONObject getJSONObject(String json) throws ParseException{
        JSONParser jsonParser = new JSONParser();
        return (JSONObject) jsonParser.parse(json.trim());
    }

    public static Integer parseMessageId(String json) {
        try {
            JSONObject obj = getJSONObject(json);
            return Integer.parseInt((String) obj.get("id"));
        } catch (ParseException e) {
            return null;
        }
    }
}