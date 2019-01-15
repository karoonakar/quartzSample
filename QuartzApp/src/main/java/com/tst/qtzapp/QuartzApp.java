package com.tst.qtzapp;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.jobs.FileScanJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuartzApp {
  private final static Logger logger = LoggerFactory.getLogger(QuartzApp.class);
  
  public static void main(String[] args) {
    if (args == null || args.length < 1 || args[0] == null || args[0].isEmpty()) {
      logger.error("You must provide a filename to this program.");
      return;
    }

    String fileName = args[0]; 
    
    logger.info("Scheduler started monitoring filename: {}", fileName);
    fileScan(fileName);
  }
  
  private static void fileScan(String filename) {
    Trigger trigger = TriggerBuilder.newTrigger().withIdentity("FileScanTriggerName", "group1")
        .withSchedule(CronScheduleBuilder.cronSchedule("0/5 * * * * ?")).build();
    Scheduler scheduler;
    try {
      scheduler = new StdSchedulerFactory().getScheduler();
      scheduler.start();

      JobKey jobKey = new JobKey("FileScanJobName", "group1");
      JobDetail job =
          JobBuilder.newJob(FileScanJob.class).withIdentity(jobKey).build();
      job.getJobDataMap().put(FileScanJob.FILE_NAME, filename);
      job.getJobDataMap().put(FileScanJob.FILE_SCAN_LISTENER_NAME, FileScanListener.LISTENER_NAME);
      scheduler.getContext().put(FileScanListener.LISTENER_NAME, new FileScanListener());
      scheduler.scheduleJob(job, trigger);

    } catch (SchedulerException e) {
      logger.error(e.getMessage());
    }
  }

}
