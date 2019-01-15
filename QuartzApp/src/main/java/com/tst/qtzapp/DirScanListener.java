package com.tst.qtzapp;

import org.quartz.jobs.DirectoryScanListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class DirScanListener implements DirectoryScanListener {

  private final static Logger logger = LoggerFactory.getLogger(DirScanListener.class);

  public static final String LISTENER_NAME = "jcgDirScanListenerName";

  public void filesUpdatedOrAdded(File[] files) {
    for (File file : files) {
      logger.info("File update to {}", file.getName());
    }
  }

  public String getName() {
    return LISTENER_NAME;
  }
}
