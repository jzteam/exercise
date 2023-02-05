package cn.jzteam.module.log.enu;

public enum EnumAlarmLevel {
    P1("P1", true, true),
    P2("P2", true, true),
    P3("P3", false, true);
    private final String code;

    private final boolean errorLog;

    private final boolean needManualPushLark;

    EnumAlarmLevel(String code, boolean errorLog, boolean needManualPushLark) {
        this.code = code;
        this.errorLog = errorLog;
        this.needManualPushLark = needManualPushLark;
    }

    public String getCode() {
        return this.code;
    }

    public boolean needManualPushLark() {
        return this.needManualPushLark;
    }

    public boolean isErrorLog (){
        return errorLog;
    }
}
