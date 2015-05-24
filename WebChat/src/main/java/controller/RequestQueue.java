
import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.servlet.AsyncContext;
import javax.servlet.AsyncListener;
import javax.servlet.AsyncEvent;

public class RequestQueue {
    private static Queue<AsyncContext> queue = new ConcurrentLinkedQueue<AsyncContext>();

    public static void addListener(final AsyncContext asyncContext){
        asyncContext.addListener(new AsyncListener(){
            public void onTimeout(AsyncEvent event) throws IOException {
                removeListener(asyncContext);
            }

            public void onStartAsync(AsyncEvent event) throws IOException {
            }

            public void onError(AsyncEvent event) throws IOException {
                removeListener(asyncContext);
            }

            public void onComplete(AsyncEvent event) throws IOException {
                removeListener(asyncContext);
            }
        });

        queue.add(asyncContext);
    }

    public static void removeListener(AsyncContext asyncContext){
        queue.remove(asyncContext);
    }

    public static void notifyAllListener(Message message){

    }

}