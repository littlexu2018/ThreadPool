package com.xuhh.threadpool.test;

import org.junit.Test;

import java.util.Random;
import java.util.concurrent.*;

public class ExecutorCompletionServiceTset1 {

    @Test
    public void test() throws InterruptedException, ExecutionException {
        Executor executor = Executors.newFixedThreadPool(3);
        CompletionService<String> service = new ExecutorCompletionService<>(executor);
        for (int i = 0 ; i < 100 ;i++) {
            int seqNo = i;
            service.submit(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    return "HelloWorld-" + seqNo + "-" + Thread.currentThread().getName();
                }
            });
        }
        for (int j = 0 ; j < 100; j++) {
            Thread.sleep(100);
            System.out.println(service.take().get());
        }
    }

    @Test
    public void case3() throws InterruptedException, ExecutionException {
        Random random = new Random();

        ExecutorService service = Executors.newFixedThreadPool(10);
        ExecutorCompletionService<String> completionService = new ExecutorCompletionService<String>(service);

        for(int i=0; i<50; i++) {
            completionService.submit(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    Thread.sleep(random.nextInt(5000));
                    return Thread.currentThread().getName();
                }
            });
        }

        int completionTask = 0;
        while(completionTask < 50) {
            //如果完成队列中没有数据, 则阻塞; 否则返回队列中的数据
            Future<String> resultHolder = completionService.take();
            System.out.println("result: " + resultHolder.get());
            completionTask++;
        }

        System.out.println(completionTask + " task done !");

        //ExecutorService使用完一定要关闭 (回收资源, 否则系统资源耗尽! .... 呵呵...)
        service.shutdown();
    }

}
