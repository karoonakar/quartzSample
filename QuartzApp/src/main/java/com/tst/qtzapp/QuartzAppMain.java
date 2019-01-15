package com.tst.qtzapp;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.jobs.DirectoryScanJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/// java -jar QuartzApp-0.0.1-SNAPSHOT-jar-with-dependencies.jar C:\DevTools\Quatz_projects\QuartzApp\QuartzApp\target\test.txt

public class QuartzAppMain {
  private final static Logger logger = LoggerFactory.getLogger(QuartzAppMain.class);
  
  public static void main(String[] args) {
    if (args == null || args.length < 1 || args[0] == null || args[0].isEmpty()) {
      logger.error("You must provide a filename to this program.");
      return;
    }

    String fileName = args[0]; 
    
    logger.info("Scheduler started monitoring filename: {}", fileName);
    String dirName = fileName;
    dirScan(dirName);
  }
  
  private static void dirScan(String dirName) {
    Trigger trigger = TriggerBuilder.newTrigger().withIdentity("FileScanTriggerName", "group1")
        .withSchedule(CronScheduleBuilder.cronSchedule("0/5 * * * * ?")).build();
    Scheduler scheduler;
    try {
      scheduler = new StdSchedulerFactory().getScheduler();
      scheduler.start();

      JobKey jobKey = new JobKey("FileScanJobName", "group1");
      JobDetail job =
          JobBuilder.newJob(DirectoryScanJob.class).withIdentity(jobKey).build();
      job.getJobDataMap().put(DirectoryScanJob.DIRECTORY_NAME, dirName);
      job.getJobDataMap().put(DirectoryScanJob.DIRECTORY_SCAN_LISTENER_NAME, DirScanListener.LISTENER_NAME);
      scheduler.getContext().put(DirScanListener.LISTENER_NAME, new DirScanListener());
      scheduler.scheduleJob(job, trigger);

    } catch (SchedulerException e) {
      logger.error(e.getMessage());
    }
  }

}
