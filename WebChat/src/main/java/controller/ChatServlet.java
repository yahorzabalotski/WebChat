import java.io.*;
import java.io.IOException;
import java.lang.Override;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@javax.servlet.annotation.WebServlet(name = "ChatServlet", asyncSupported = true)
public class ChatServlet extends HttpServlet{

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException{
        System.out.println("Inside get method.");
        response.setStatus(response.SC_OK);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException{
        System.out.println("Inside post method.");
        response.setStatus(response.SC_OK);
    }

    @Override
    public void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        System.out.println("Inside put method.");
        response.setStatus(response.SC_OK);
    }

    @Override
    public void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        System.out.println("Inside delete method.");
        response.setStatus(response.SC_OK);
    }
}