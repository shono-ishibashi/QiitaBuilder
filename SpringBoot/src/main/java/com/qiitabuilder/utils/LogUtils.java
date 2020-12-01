package com.qiitabuilder.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogUtils {

    protected final static Logger logger = LoggerFactory.getLogger(LogUtils.class);

    public static void info(String msg, Throwable t) {
        logger.info(msg,t);
    }
    public static void warn(String msg, Throwable t) {
        logger.warn(msg,t);
    }
    public static void error(String msg, Throwable t) {
        logger.error(msg,t);
    }
    public static void debug(String msg, Throwable t) {
        logger.debug(msg,t);
    }
    public static void trace(String msg, Throwable t) {
        logger.trace(msg,t);
    }
}
