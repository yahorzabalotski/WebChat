import java.io.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.*;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Override;
import java.lang.System;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.AsyncContext;
import javax.servlet.AsyncListener;
import javax.servlet.AsyncEvent;

import org.apache.log4j.Logger;

@javax.servlet.annotation.WebServlet(asyncSupported = true)
public class ChatServlet extends HttpServlet{

    private static Logger logger = Logger.getLogger(ChatServlet.class.getName());
    private static RequestQueue requestQueue = new RequestQueue();
    private MessageDAO messageDAO = new MessageDAOImpl();
    private UserDAO userDAO = new UserDAOImpl();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException{
        logger.info("doGet");
        Integer userId = (Integer) request.getSession(true).getAttribute("userId");
        if(userId != null) {
            List<Message> list = messageDAO.selectAfter(userDAO.getLastMessageId(userId));
            if (list.size() == 0) {
                addListener(request, userId);
            } else {
                userDAO.setLastMessageId(userId, list.get(list.size() - 1).getId());
                sendGetResponse(response, list);
            }
        } else {
            logger.info("Bad userId in session.");
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException{
        logger.info("doPost");
        try{
            String requestBody = ServletUtil.getMessageBody(request);
            String name = ParseUtil.getUserName(requestBody);
            if(name != null) {
                User user = new User(new Integer(0), name);
                userDAO.add(user);
                request.getSession().setAttribute("userId", user.getId());
            } else {
                logger.info("Bad user name in session.");
            }
        } catch (IOException e){
            logger.error(e);
        }
        response.setStatus(response.SC_OK);
    }

    @Override
    public void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        logger.info("doPut");
        try {
            Integer id = (Integer) request.getSession(true).getAttribute("userId");
            if(id != null) {
                String requestBody = ServletUtil.getMessageBody(request);
                Message message = ParseUtil.parseMessage(requestBody);
                message.setUserId(id);
                if(message.getId() == 0) {
                    messageDAO.add(message);
                } else {
                    messageDAO.update(message);
                }
                List<Message> list = new ArrayList<>();
                list.add(message);
                requestQueue.notifyAllListener(ParseUtil.messageToJSON(list));
            }
        } catch (IOException e) {
            logger.error(e);
        }
        response.setStatus(response.SC_OK);
    }

    @Override
    public void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        logger.info("doDelete");
        try {
            Integer id = (Integer) request.getSession(true).getAttribute("userId");
            if(id != null) {
                String requestBody = ServletUtil.getMessageBody(request);
                Message message = new Message(ParseUtil.parseMessageId(requestBody) ,id, "", new Date());
                messageDAO.update(message);
                String notify = ParseUtil.messageToJSON(message);
                List<Message> list = new ArrayList<>();
                list.add(message);
                requestQueue.notifyAllListener(ParseUtil.messageToJSON(list));
            }
        } catch (IOException e) {
            logger.error(e);
        }
        response.setStatus(response.SC_OK);
    }

    private void sendGetResponse(HttpServletResponse response, List<Message> list) {
        try {
            PrintWriter printWriter = response.getWriter();
            printWriter.print(ParseUtil.messageToJSON(list));
            response.setStatus(response.SC_OK);
        } catch (IOException e) {
            logger.error(e);
        }
    }

    private void addListener(final HttpServletRequest request, final Integer userId) {
        request.setAttribute("org.apache.catalina.ASYNC_SUPPORTED", true);
        final AsyncContext asyncContext = request.startAsync();
        asyncContext.setTimeout(Integer.MAX_VALUE - 1);
        requestQueue.addListener(asyncContext);
        asyncContext.addListener(new AsyncListener() {

            @Override
            public void onTimeout(AsyncEvent event) throws IOException {
            }

            @Override
            public void onStartAsync(AsyncEvent event) throws IOException {
            }

            @Override
            public void onError(AsyncEvent event) throws IOException {
            }

            @Override
            public void onComplete(AsyncEvent event) throws IOException {
                Integer lastId = userDAO.getLastMessageId(userId);
                userDAO.setLastMessageId(userId, lastId + 1);
            }
        });
    }
}