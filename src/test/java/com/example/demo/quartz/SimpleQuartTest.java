package com.example.demo.quartz;

import org.junit.jupiter.api.Test;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import quartz.SimpleJob;

import java.util.concurrent.TimeUnit;

/**
 * @author MG02004
 * @createTime 2022/9/19 11:19
 * @description
 */
public class SimpleQuartTest {

    /**
     * 基于时间间隔的定时任务
     */
    @Test
    public void test() throws SchedulerException, InterruptedException {
        // 1、创建调度器
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        Scheduler scheduler = schedulerFactory.getScheduler();
        // 2、创建jobDetail实例，并与SimpleJob绑定（Job执行内容）
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("name", "xiaohe");
        jobDataMap.put("age", "27");
        JobDetail jobDetail = JobBuilder.newJob(SimpleJob.class).withIdentity("job1", "group1")
                .setJobData(jobDataMap)
                .usingJobData("sex", "boy")
                .build();
        // 3、构建trigger触发器，定义执行频率和时长
        Trigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger-1", "trigger-group")
                .startNow() //立即生效
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(2) //每两秒执行
                        .repeatForever()).build(); //永久执行

        // 4、将jobDetail 和 trigger交给Scheduler来调度
        scheduler.scheduleJob(jobDetail, trigger);
        // 5、开启scheduler
        scheduler.start();
        // 6、休眠，决定调度器运行时间，这里设置30s
        // 测试方法结束，主线程随之结束导致定时任务也不再执行了，所以需要设置休眠hold住主线程。
        // 项目中的进程一直是存活的，所以无需设置。
        Thread.sleep(30000);
        //TimeUnit.SECONDS.sleep(30);
        // 7、关闭scheduler
        scheduler.shutdown();
    }

//    /**
//     * 基于定时表达式的定时任务
//     */
//    @Test
//    public void cronTest() throws SchedulerException, InterruptedException {
//        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
//        Scheduler scheduler = schedulerFactory.getScheduler();
//        JobDetail jobDetail = JobBuilder.newJob(SimpleJob.class).withIdentity("job1", "group1")
//                .build();
//        Trigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger-1", "trigger-job")
//                .startNow()
//                .withSchedule(CronScheduleBuilder.cronSchedule("0/5 * * * * ?")).build();
//        scheduler.scheduleJob(jobDetail, trigger);
//        scheduler.start();
//        TimeUnit.SECONDS.sleep(30);
//        scheduler.shutdown();
//    }

    /**
     * 多触发器定时任务
     */
//    @Test
//    public void multiJobTest() throws SchedulerException, InterruptedException {
//        // 声明调度器
//        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
//        Scheduler scheduler = schedulerFactory.getScheduler();
//        // 创建JobDetail实例，与执行内容SimpleJob绑定，注意要设置.storeDurably, 否则报错
//        JobDetail jobDetail = JobBuilder.newJob(SimpleJob.class).withIdentity("job1", "job-group")
//                .storeDurably()
//                .build();
//        // 创建trigger 实例
//        Trigger trigger1 = TriggerBuilder.newTrigger().withIdentity("trigger1", "trigger1-group")
//                .startNow()
//                .forJob(jobDetail)
//                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(2).repeatForever())
//                .build();
//        Trigger trigger2 = TriggerBuilder.newTrigger().withIdentity("trigger2", "trigger2-group")
//                .startNow()
//                .forJob(jobDetail)
//                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(3).repeatForever())
//                .build();
//        //调度器中添加job
//        scheduler.addJob(jobDetail, false);
//        scheduler.scheduleJob(trigger1);
//        scheduler.scheduleJob(trigger2);
//        //启动调度器
//        scheduler.start();
//        //休眠20s
//        TimeUnit.SECONDS.sleep(20);
//        //关闭调度器
//        scheduler.shutdown();
//    }
}
