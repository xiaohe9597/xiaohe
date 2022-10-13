package quartz;

import lombok.experimental.Builder;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author MG02004
 * @createTime 2022/9/19 10:59
 * @description
 */
public class SimpleJob implements Job {

    /**
     * job执行模块
     *
     * @param jobExecutionContext
     * @throws JobExecutionException
     */
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDataMap jobDataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        System.out.println("jobDataMap paramter: " + jobDataMap.get("name"));
        System.out.println("jobDataMap paramter: " + jobDataMap.get("age"));
        System.out.println("jobDataMap paramter: " + jobDataMap.get("sex"));
        System.out.println("quartz 练习" + new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
    }
}
