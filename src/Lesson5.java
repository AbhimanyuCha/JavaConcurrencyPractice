/*
    COMPLETABLE FUTURES
    ------------------------------------------
    Perform asynchronous (non blocking) computations which could also be a synchronous

    In java asynchronous programming is easy, you create a new thread implementing
    Runnable or callable, then you assign the task to that thread while the main thread continues to
    execute other instructions, once the child thread is done it will return some value or it will get killed.

    main thread gets blocked in case when we use futures and we want to get the value of the future.

    The problem gets highlighted in the use case when we want to get one response and then
    pass that response to another api call.

    Example
        Fetch Order -> Enrich order -> Payment -> Dispatch -> SendMail.

    Synchronous execution is required.

    By using Futures we will have code like this :

    ExecutorService service = Executors.newFixedThreadPool(10);

    Future<Order> future = service.submit(getOrderTask());
    Order order = future.get();

    Future<Order> future1 = service.submit(enrichOrderTask(order));
    order = future1.get();

    Future<Order> future2 = service.submit(performPaymentTask(order));
    order = future2.get();

    Future<Order> future3 = service.submit(dispatchTask(order));
    order = future3.get();

    Future<Order> future4 = service.submit(sendMailTask(order));
    order = future4.get();

    This is blocking operations.
    There is a dependency one of the task cannot be started before another one is finished.

    So lets say for 10 orders this will be a blocking operation.


    Here we use Completable Future, which helps in allocating a thread for a complete chain of
    operation for one order.

    for(int i = 0 ; i < 10 ; i++){
        CompletableFuture.supplyAsync(() -> getOrder())
                .thenApply(order -> enrich(order))
                .thenApply(order -> performPayment(order))
                .thenApply(oder -> dispatch(order))
                .thenAccept(oder -> sendMail(order));


    thenApplyAsync takes another parameter as an executor service which will helpful if we are
    providing another executor service to do that task once the previous one has finished.
 */

import java.util.concurrent.*;

public class Lesson5 {
    static final class Order{
        int order_id;
        int request_id;
        int amount;
        String name;
        String address;
        String mail;
    }

    static class GetOrderTask implements Callable<Order>{

        @Override
        public Order call() throws Exception {
            return null;
        }
    }

    static class EnrichOrderTask implements Callable<Order>{
        GetOrderTask order;
        public EnrichOrderTask(GetOrderTask o){
            this.order = o;
        }
        @Override
        public Order call() throws Exception {
            return null;
        }
    }

    static class PaymentTask implements Callable<Order>{

        public PaymentTask(EnrichOrderTask order) {
        }

        @Override
        public Order call() throws Exception {
            return null;
        }
    }

    static class DispatchTask implements Callable<Order>{

        @Override
        public Order call() throws Exception {
            return null;
        }
    }

    static class SendMailTask implements Callable<Order>{
        DispatchTask o;
        SendMailTask(DispatchTask o){
            this.o = o;
        }
        @Override
        public Order call() throws Exception {
            return null;
        }
    }


    public static void main(String[] args) {

        for(int requestId = 0 ; requestId <= 100 ; requestId++){
            ExecutorService cpuBound = Executors.newFixedThreadPool(4);
            ExecutorService ioBound = Executors.newCachedThreadPool();
            CompletableFuture.supplyAsync(() -> new GetOrderTask(), ioBound)
                    .thenApplyAsync(order -> new EnrichOrderTask(order), cpuBound)
                    .thenApplyAsync((order) -> new PaymentTask(order), cpuBound)
                    .thenApplyAsync((order) -> new DispatchTask(), ioBound)
                    .thenAccept((order)-> new SendMailTask(order));
        }
    }

}
