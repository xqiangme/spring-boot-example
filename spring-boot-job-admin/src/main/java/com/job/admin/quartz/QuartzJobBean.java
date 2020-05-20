package com.job.admin.quartz;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.MDC;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Method;

/**
 * Quartz 定时任务核心 Bean
 *
 * @author mengq
 */
@Slf4j
public class QuartzJobBean implements Job {

    public static final String JOB_ID = "jobId";
    public static final String TARGET_CLASS = "class";
    public static final String TARGET_METHOD = "method";
    public static final String TARGET_ARGUMENTS = "arguments";

    private static ApplicationContext applicationContext;

    public static void setAc(ApplicationContext applicationContext) {
        QuartzJobBean.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 通过类名和方法名去获取目标对象，再通过反射执行 类名和方法名保存在jobDetail中
     */
    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //任务ID
        Integer jobId = (Integer) context.getMergedJobDataMap().get(JOB_ID);
        //目标类名
        String targetClass = (String) context.getMergedJobDataMap().get(TARGET_CLASS);
        //目标方法
        String targetMethod = (String) context.getMergedJobDataMap().get(TARGET_METHOD);
        //方法参数
        String methodArgs = (String) context.getMergedJobDataMap().get(TARGET_ARGUMENTS);

        if (StringUtils.isEmpty(targetClass) || StringUtils.isEmpty(targetMethod)) {
            return;
        }

        long startTime = System.currentTimeMillis();
        try {
            //任务日志标识
            MDC.put("logId", RandomStringUtils.randomAlphanumeric(15));
            log.info("[ QuartzJob ] >> job start jobId:{} , targetClass:{} ,targetMethod:{} , methodArgs:{}", jobId, targetClass, targetMethod, methodArgs);
            //任务参数
            Object[] jobArs = this.getJobArgs(methodArgs);
            Object target = applicationContext.getBean(targetClass);
            if (null != target) {
                Class tc = target.getClass();
                Class[] parameterType = null;
                if (jobArs != null) {
                    parameterType = new Class[jobArs.length];
                    for (int i = 0; i < jobArs.length; i++) {
                        parameterType[i] = String.class;
                    }
                }
                //执行任务方法
                Method method = tc.getDeclaredMethod(targetMethod, parameterType);
                if (null != method) {
                    method.invoke(target, jobArs);
                }
            }
        } catch (Exception e) {
            log.error("[ QuartzJob ] >> job execute exception jobId:{} , targetClass:{} ,targetMethod:{} , methodArgs:{}"
                    , jobId, targetClass, targetMethod, methodArgs, e);
            throw new JobExecutionException(e);
        }
        log.info("[ QuartzJob ] >> job end jobId:{} , targetClass:{} ,targetMethod:{} , methodArgs:{} , time:{} ms"
                , jobId, targetClass, targetMethod, methodArgs, (System.currentTimeMillis() - startTime));
    }

    /**
     * 处理Job设置的参数
     * 多个使用#&分隔，推荐使用单个String 类型JSON参数
     *
     * @param methodArgs
     * @return
     */
    private Object[] getJobArgs(String methodArgs) {
        //参数处理
        Object[] args = null;
        if (!StringUtils.isEmpty(methodArgs)) {
            methodArgs = methodArgs + " ";
            String[] argString = methodArgs.split("#&");
            args = new Object[argString.length];
            for (int i = 0; i < argString.length; i++) {
                args[i] = argString[i].trim();
            }
        }
        return args;
    }
}
