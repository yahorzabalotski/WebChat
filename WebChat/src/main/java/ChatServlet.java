import java.io.*;
import java.io.IOException;
import java.lang.*;
import javax.servlet.*;
import javax.servlet.http.*;

import org.json.simple.JSONValue;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.System;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.*;
import java.text.SimpleDateFormat;
import java.util.LinkedList;

public class ChatServlet extends HttpServlet {

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-M-yyyy HH:mm ");
    private JSONParser jsonParser = new JSONParser();

    private static String AddState = "add";

    private List<State> list = new ArrayList<State>();

    public void init() throws ServletException {
        try {
            System.out.println("ok");
            list.addAll(XMLUtil.readData());
            System.out.println("ok");
            for(int i = 0; i < list.size(); i++) {
                Message message = new Message();
                message.parseJSONValue(list.get(i).getJSONValue());
                printGetMessage(message);
            }
            System.out.println(list.size());

        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try{
            List json = new LinkedList<String>();
            for(int i = 0; i < list.size(); i++){
                json.add(list.get(i).getState());
                json.add(list.get(i).getJSONValue());
            }
            StringWriter jsonOut = new StringWriter();
            JSONValue.writeJSONString(json, jsonOut);
            PrintWriter out = response.getWriter();
            out.println(jsonOut.toString());
            System.out.println(jsonOut.toString());
            System.out.println(list.size());
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
        response.setStatus(response.SC_OK);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        try {
            Message message = getMessage(ServletUtil.getMessageBody(request));
            State state = new State(AddState, message.getJSONValue());
            list.add(state);
            printGetMessage(message);
            response.setStatus(response.SC_OK);
        }
        catch (ParseException e){
            System.out.println(e);
            response.setStatus(response.SC_BAD_REQUEST);
        }
    }

    private String getUniqId(){
        return Integer.toString(list.size());
    }

    private void printGetMessage(Message message){
        System.out.println(dateFormat.format(message.getDate()) + message.getAuthor() + " : " + message.getText());
    }

    private Message getMessage(String jsonValue) throws ParseException {
        JSONObject jsonObj = ServletUtil.getJSONObject(jsonValue);
        Message message = new Message(new Date(), (String) jsonObj.get("author"), (String) jsonObj.get("text"), getUniqId());
        return message;
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
        try {
            XMLUtil.save(list);
            System.out.println("destroy");
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

}