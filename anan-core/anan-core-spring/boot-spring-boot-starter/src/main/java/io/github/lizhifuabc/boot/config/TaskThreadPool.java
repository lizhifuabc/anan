package io.github.lizhifuabc.boot.config;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 任务线程池
 *
 * @author lizhifu
 * @since 2025/2/21
 */
public class TaskThreadPool {
    /**
     * 并发比例
     */
    public static final int concurrentRate = 3;

    /**
     * 核心线程数
     */
    private static final int ASYNC_CORE_THREADS = 3, CONCURRENT_CORE_THREADS = ASYNC_CORE_THREADS * concurrentRate;

    /*
     * 最大线程数
     * */
    private static final int ASYNC_MAX_THREADS = ASYNC_CORE_THREADS + 1, CONCURRENT_MAX_THREADS = ASYNC_MAX_THREADS * concurrentRate;

    /*
     * 队列大小
     * */
    private static final int ASYNC_QUEUE_SIZE = 2000, CONCURRENT_QUEUE_SIZE = 20000;

    /*
     * 线程池的线程前缀
     * */
    public static final String ASYNC_THREAD_PREFIX = "excel-async-pool-", CONCURRENT_THREAD_PREFIX = "excel-concurrent-pool-";

    /*
     * 空闲线程的存活时间（单位秒），三分钟
     * */
    private static final int KEEP_ALIVE_SECONDS = 60 * 3;

    /*
     * 拒绝策略：如果队列、线程数已满，本次提交的任务返回给线程自己执行
     * */
    public static final ThreadPoolExecutor.AbortPolicy ASYNC_REJECTED_HANDLER =
            new ThreadPoolExecutor.AbortPolicy();
    public static final ThreadPoolExecutor.CallerRunsPolicy CONCURRENT_REJECTED_HANDLER =
            new ThreadPoolExecutor.CallerRunsPolicy();
    /*
     * 异步线程池
     * */
    private volatile static ThreadPoolTaskExecutor asyncThreadPool, concurrentThreadPool;


    /**
     * DCL单例式懒加载：获取异步线程池
     * 异步任务（asyncThreadPool）通常是指那些不需要立即响应的任务，可以在后台异步执行。典型场景可能是一些 IO 密集型操作、文件处理等任务。
     * <br>异步任务通常是 IO 密集型的任务，不需要消耗太多 CPU 时间，线程可以较长时间处于空闲状态。因此，异步线程池的核心线程数相对较少。
     * @return 异步线程池
     */
    public static ThreadPoolTaskExecutor getAsyncThreadPool() {
        if (asyncThreadPool == null) {
            synchronized (TaskThreadPool.class) {
                if (asyncThreadPool == null) {
                    asyncThreadPool = new ThreadPoolTaskExecutor();
                    asyncThreadPool.setCorePoolSize(ASYNC_CORE_THREADS);
                    asyncThreadPool.setMaxPoolSize(ASYNC_MAX_THREADS);
                    asyncThreadPool.setQueueCapacity(ASYNC_QUEUE_SIZE);
                    asyncThreadPool.setKeepAliveSeconds(KEEP_ALIVE_SECONDS);
                    asyncThreadPool.setThreadNamePrefix(ASYNC_THREAD_PREFIX);
                    asyncThreadPool.setWaitForTasksToCompleteOnShutdown(true);
                    asyncThreadPool.setRejectedExecutionHandler(ASYNC_REJECTED_HANDLER);
                    asyncThreadPool.initialize();
                    return asyncThreadPool;
                }
            }
        }
        return asyncThreadPool;
    }

    /**
     * DCL单例式懒加载：获取并发线程池
     * 并发任务（concurrentThreadPool）则更侧重于处理需要更高并发能力的任务，例如高负载计算、并行任务处理等。
     * @return 并发线程池
     */
    public static ThreadPoolTaskExecutor getConcurrentThreadPool() {
        if (concurrentThreadPool == null) {
            synchronized (TaskThreadPool.class) {
                if (concurrentThreadPool == null) {
                    concurrentThreadPool = new ThreadPoolTaskExecutor();
                    concurrentThreadPool.setCorePoolSize(CONCURRENT_CORE_THREADS);
                    concurrentThreadPool.setMaxPoolSize(CONCURRENT_MAX_THREADS);
                    concurrentThreadPool.setKeepAliveSeconds(KEEP_ALIVE_SECONDS);
                    concurrentThreadPool.setQueueCapacity(CONCURRENT_QUEUE_SIZE);
                    concurrentThreadPool.setThreadNamePrefix(CONCURRENT_THREAD_PREFIX);
                    concurrentThreadPool.setWaitForTasksToCompleteOnShutdown(true);
                    concurrentThreadPool.setRejectedExecutionHandler(CONCURRENT_REJECTED_HANDLER);
                    concurrentThreadPool.initialize();
                    return concurrentThreadPool;
                }
            }
        }
        return concurrentThreadPool;
    }
}
