import java.io.*;
import java.lang.Exception;
import java.lang.String;
import java.lang.System;
import javax.servlet.*;
import javax.servlet.http.*;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;

public class ChatServlet extends HttpServlet {

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-M-yyyy HH:mm ");
    private JSONParser jsonParser = new JSONParser();

    @Override
    public void init() throws ServletException {
        try {
            ArrayList<ArrayList<String> > messages = XMLUtil.readData();
            for(int i = 0; i < messages.size(); i++) {
                for(int j = 0; j < messages.get(i).size(); j++){
                    System.out.print(messages.get(i).get(j) + " ");
                }
                System.out.println();
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setStatus(response.SC_OK);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try{
            printGetMessage(ServletUtil.getMessageBody(request));
            response.setStatus(response.SC_OK);
        }
        catch (ParseException e){
            System.out.println(e);
            response.setStatus(response.SC_BAD_REQUEST);
        }
    }

    private void printGetMessage(String jsonValue) throws ParseException {
        Date date = new Date();
        JSONObject jsonObj = ServletUtil.getJSONObject(jsonValue);
        String author = (String) jsonObj.get("author");
        String text = (String) jsonObj.get("text");
        if(author == null || text == null) {
            throw new ParseException(ParseException.ERROR_UNEXPECTED_TOKEN);
        }
        System.out.println(dateFormat.format(date) + author + " : " + text);
    }

    @Override
    public void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setStatus(response.SC_OK);
    }

    @Override
    public void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setStatus(response.SC_OK);
    }

    @Override
    public void destroy() {
    }

}