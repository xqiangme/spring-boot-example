package com.example.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author mengqiang
 * 全局工作线程池
 */
public class WorkThreadUtil {

    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(WorkThreadUtil.class);

    /**
     * 线程池的核心线程数
     */
    static int CORE_POOL_SIZE = 10;

    /**
     * 线程池的最大线程数
     */
    static int MAX_POOL_SIZE = 30;

    /**
     * 线程池空闲时线程的存活时长
     */
    static long KEEP_ALIVE_TIME = 60L;

    /**
     * 存放任务的队列，使用的是阻塞队列容量
     */
    static int QUEUE_CAPACITY = 1024;

    public static ExecutorService COMMON_WORK_POOL;

    /**
     * 双检锁保证单例
     */
    public static ExecutorService getInstance() {
        if (null != COMMON_WORK_POOL) {
            return COMMON_WORK_POOL;
        }

        LOGGER.info("[ 公共线程池 ] >> init ");
        System.out.println(("[ 公共线程池 ] >> init "));
        synchronized (WorkThreadUtil.class) {
            if (null != COMMON_WORK_POOL) {
                return COMMON_WORK_POOL;
            }
            COMMON_WORK_POOL = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME,
                    TimeUnit.SECONDS,
                    new LinkedBlockingQueue<Runnable>(QUEUE_CAPACITY),
                    new NamedThreadFactory("common-work-thread"));
        }
        return COMMON_WORK_POOL;
    }

    public static void close() {
        if (null != COMMON_WORK_POOL) {
            COMMON_WORK_POOL.shutdown();
            LOGGER.info("[ 公共线程池 ] >> close ");
        }
    }

    static class NamedThreadFactory implements ThreadFactory {
        private static final AtomicInteger POOL_SEQ = new AtomicInteger(1);

        private final AtomicInteger mThreadNum = new AtomicInteger(1);

        private final String mPrefix;

        private final boolean mDaemo;

        private final ThreadGroup mGroup;

        public NamedThreadFactory() {
            this("pool-" + POOL_SEQ.getAndIncrement(), false);
        }

        public NamedThreadFactory(String prefix) {
            this(prefix, false);
        }

        public NamedThreadFactory(String prefix, boolean daemo) {
            mPrefix = prefix + "-thread-";
            mDaemo = daemo;
            SecurityManager s = System.getSecurityManager();
            mGroup = (s == null) ? Thread.currentThread().getThreadGroup() : s.getThreadGroup();
        }

        @Override
        public Thread newThread(Runnable runnable) {
            String name = mPrefix + mThreadNum.getAndIncrement();
            Thread ret = new Thread(mGroup, runnable, name, 0);
            ret.setDaemon(mDaemo);
            return ret;
        }

        public ThreadGroup getThreadGroup() {
            return mGroup;
        }
    }
}
