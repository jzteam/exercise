package cn.jzteam.module.log;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;

public class TestLog4j {

    public static void main(String[] args) {
        // INFO, console, logFile
        Logger log1 = Logger.getLogger("test1");
        log1.info("log1.info 测试------INFO, console, logFile------");
        // DEBUG, console, dailyFile
        Log log2 = LogFactory.getLog("test2");
        log2.info("log2.info 测试------DEBUG, console, dailyFile------");
    }
}
