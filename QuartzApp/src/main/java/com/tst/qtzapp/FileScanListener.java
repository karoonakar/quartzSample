package com.tst.qtzapp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileScanListener implements org.quartz.jobs.FileScanListener {

  private final static Logger logger = LoggerFactory.getLogger(FileScanListener.class);

  public static final String LISTENER_NAME = "jcgFileScanListenerName";

  public void fileUpdated(String fileName) {
    logger.info("File update to {}", fileName);
  }

  public String getName() {
    return LISTENER_NAME;
  }
}
