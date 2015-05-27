
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.Integer;
import java.lang.Override;
import java.lang.System;
import java.util.Queue;
import java.util.List;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.servlet.AsyncContext;
import javax.servlet.AsyncListener;
import javax.servlet.AsyncEvent;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

public class RequestQueue {
    private static Logger logger = Logger.getLogger(RequestQueue.class.getName());
    private static Queue<AsyncContext> queue = new ConcurrentLinkedQueue<AsyncContext>();

    public void addListener(final AsyncContext asyncContext){

        asyncContext.addListener(new AsyncListener() {
            @Override
            public void onTimeout(AsyncEvent event) throws IOException {
                removeListener(asyncContext);
            }

            @Override
            public void onStartAsync(AsyncEvent event) throws IOException {
            }

            @Override
            public void onError(AsyncEvent event) throws IOException {
                removeListener(asyncContext);
            }

            @Override
            public void onComplete(AsyncEvent event) throws IOException {
                removeListener(asyncContext);
            }
        });

        queue.add(asyncContext);
    }

    public void removeListener(AsyncContext asyncContext){
        queue.remove(asyncContext);
    }

    public void notifyAllListener(String message){
        for(AsyncContext asyncContext: queue){
            try{
                PrintWriter printWriter = asyncContext.getResponse().getWriter();
                printWriter.print(message);
                printWriter.flush();
            } catch (IOException e) {
                logger.error(e);
            } finally {
                asyncContext.complete();
            }
        }
    }

}