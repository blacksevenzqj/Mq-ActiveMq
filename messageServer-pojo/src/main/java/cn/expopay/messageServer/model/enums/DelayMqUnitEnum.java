package cn.expopay.messageServer.model.enums;

public enum DelayMqUnitEnum {

    S("秒","Secound",1000),
    M("分钟","Minutes",60000),
    H("小时","Houre",3600000),
    D("天","Date",86400000);

    private final String title;
    private final String mark;
    private final long time;

    private DelayMqUnitEnum(String title, String mark, long time) {
        this.title = title;
        this.mark = mark;
        this.time = time;
    }

    public String getTitle() {
        return title;
    }
    public String getMark() {
        return mark;
    }
    public long getTime() {
        return time;
    }

    public static DelayMqUnitEnum match(String name, DelayMqUnitEnum defaultItem) {
        if (name != null) {
            for (DelayMqUnitEnum item: DelayMqUnitEnum.values()) {
                if (item.name().equalsIgnoreCase(name)) {
                    return item;
                }
            }
        }
        return defaultItem;
    }

}
