package com.xuhh.threadpool.test;

import com.google.common.collect.Lists;
import com.xuhh.threadpool.util.ThreadPoolManager;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FutureResponseTest {
    //线程池 -- 任务执行委托给executor
    private final ExecutorService executor ;//= ;

    FutureResponseTest(ExecutorService executor){
        this.executor = executor;
    }

    public ExecutorService getExecutor() {
        return executor;
    }

    private static Logger log = LoggerFactory.getLogger(FutureResponseTest.class);

    //调度请求，获得返回结果，并进行汇总处理
    public static void main(String[] args) throws Exception {
        List<String> hostIds =new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            hostIds.add("ids"+i);
        }
        List<List<String>> requestList = Lists.partition(hostIds, 20);

        long startTime = System.currentTimeMillis();//开始时间
        //创建完成服务CompletionService

        ExecutorService service =ThreadPoolManager.getInstance().getCacheExecutorService();
        CompletionService<String> completionService = new ExecutorCompletionService<>(service);//相当于一组计算的句柄，计算执行部分委托给Executor

        //开始请求--计算任务提前开始且每个都是分开的不相互影响
        for (int i = 0; i < requestList.size(); i++) {
            List<String> request = requestList.get(i);
            completionService.submit(new Callable<String>() {//提交任务
                @Override
                public String call() throws Exception {
                    System.out.println("Thread.currentThread().getName() = " + Thread.currentThread().getName());
                    return  requestForService(request);
                }
            });
        }
        try {
            //将数据进行汇总
            List<String> listAll = new ArrayList<>();
            for (int i = 0; i < requestList.size(); i++) {
                //获得结果，未获得前会阻塞
                Future<String> f = completionService.take();
                String payMent = f.get();
                listAll.add(payMent);
            }
        } catch (InterruptedException e) {
            // TODO: 任务调用get的线程在获得结果之前被中断
            //重新设置线程的中断状态
            Thread.currentThread().interrupt();
            log.info("任务调用get的线程在获得结果之前被中断！" + e);
        } catch (ExecutionException e) {
           // throw launderThrowable(e.getCause());
        } finally {
            service.shutdown();
        }
        long endTime = System.currentTimeMillis();//结束时间
        log.info("消耗时间：" + (endTime - startTime) + "毫秒！");
    }

    //模拟第三方服务
    public static String requestForService(List<String> request) throws InterruptedException, Exception{
        if(null == request) {
            throw new Exception("请求为空！");
        }
//        if(request.getParam() <= 0) {
//            throw new Exception("参数小于0，无法进行扣款！" + request);
//        }

        System.out.println("开始处理请求...");

        //为了简便直接返回一个结果即可
        List<String> list = new ArrayList<>();
        String hostString = StringUtils.join(request.toArray(), ",");
        System.out.println("资源id为{}"+hostString);
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        String str = "Linux,Windows";
        String url = "192.168" + "/store" + "?apikey=" + "1312" + "&direction=BOTH" + "&resource_id="
                + hostString + "&target_resource_class_code=" + str;
        System.out.println("请求的url {}"+url);
        String result = "["+hostString+"]";
        //模拟-使消耗时间长一些
        Thread.sleep(3000);
        System.out.println("请求的result {}"+result);
        return result;
    }}