package com.xuhh.threadpool.util;

import javax.annotation.PreDestroy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 缓存线程池
 * @autor: xuht
 * @Date: Created 2018/12/1
 */
public class ThreadPoolManager {
    //线程池定义
    private ExecutorService cacheExecutorService = new ThreadPoolExecutor(50, 200,
            10, TimeUnit.SECONDS, new LinkedBlockingQueue(2000), new ThreadPoolExecutor.AbortPolicy());

    private static ThreadPoolManager instance = new ThreadPoolManager();

    private ThreadPoolManager(){}

    /**
     * 获取实例
     * @return
     */
    public static ThreadPoolManager getInstance(){
        if (instance != null){
            return instance;
        }else {
            return new ThreadPoolManager();
        }
    }
    @PreDestroy
    public void destroy(){
        //线程池关闭
        cacheExecutorService.shutdown();
    }
    public ExecutorService getCacheExecutorService() {
        return cacheExecutorService;
    }
}
