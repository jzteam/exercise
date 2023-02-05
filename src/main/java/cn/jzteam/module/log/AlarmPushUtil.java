package cn.jzteam.module.log;

import cn.jzteam.module.log.enu.EnumAlarmLevel;
import cn.jzteam.module.log.enu.EnumBiz;
import org.apache.log4j.Logger;

public class AlarmPushUtil {

    private static Logger log = Logger.getLogger("test1");

    /**
     * such as log.info("----{}---{}-", "first", "second")
     * @param level
     * @param biz
     * @param message
     * @param args
     */
    public static void log(EnumAlarmLevel level, EnumBiz biz, String message, Object... args) {
        log(level, biz, message, null, args);
    }

    /**
     * such as log.info("----first---second-", e)
     * @param level
     * @param biz
     * @param message
     * @param e
     */
    public static void log(EnumAlarmLevel level, EnumBiz biz, String message, Throwable e) {
        log(level, biz, message, e);
    }

    /**
     * Throwable and args only exists one
     * @param level
     * @param biz
     * @param message
     * @param e
     * @param args
     */
    private static void log(EnumAlarmLevel level, EnumBiz biz, String message, Throwable e, Object... args) {
        if (biz == null) {
            biz = EnumBiz.DEFAULT;
        }
        if (level == null) {
            level = EnumAlarmLevel.P2;
        }
        if (level.isErrorLog()) {
//            log.error("[OKCLarkAlarm_" + level.getCode() + "_" + biz.getCode() + "] " + message, e != null ? e : args);
        } else {
//            log.info("[OKCLarkAlarm_" + level.getCode() + "_" + biz.getCode() + "] " + message,  e != null ? e : args);
        }

        if (level.needManualPushLark()) {
//            larkPushUtil.pushText(AlarmLarkConfig.getConfigByBizAndAlarmLevel(biz, level), new FormattedMessage(message, args).getFormattedMessage());
        }
    }


    public static void main(String[] args) {
        log.info("test start");
        log(EnumAlarmLevel.P1, EnumBiz.VRF, "我想试试{}打异常:", "能不能", new RuntimeException("我是test异常"));
        log.error("test end");
    }
}
