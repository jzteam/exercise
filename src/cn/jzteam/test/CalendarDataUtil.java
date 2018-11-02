package cn.jzteam.test;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CalendarDataUtil {

    public static String resolve(List<Content> list){
        // 2018-value
        Map<String, CalendarYear> yearMap = new HashMap<>();
        // 20181-value
        Map<String, CalendarMonth> monthMap = new HashMap<>();

        for(Content content : list){
            Pattern pattern = Pattern.compile("(\\d{4})-(\\d{1,2})-(\\d{1,2})$");
            Matcher matcher = pattern.matcher(content.getName());

            String year = null;
            String month = null;
            String day = null;
            if (matcher.find()) {
                year = matcher.group(1);
                month = matcher.group(2);
                day = matcher.group(3);
            }
            if(StringUtils.isEmpty(year) ||StringUtils.isEmpty(month)||StringUtils.isEmpty(month)){
                // 数据有问题
                continue;
            }

            final CalendarDay calendarDay = new CalendarDay();
            calendarDay.setDay(day);
            calendarDay.setContent(content);

            String monthKey = year + month;
            String yearKey = year;

            // 初始化：年
            CalendarYear calendarYear = yearMap.get(yearKey);
            if(calendarYear == null){
                calendarYear = new CalendarYear();
                calendarYear.setYear(year);
                yearMap.put(yearKey, calendarYear);
            }

            // 初始化：月
            CalendarMonth calendarMonth = monthMap.get(monthKey);
            if(calendarMonth == null){
                // 初始化
                calendarMonth = new CalendarMonth();
                calendarMonth.setMonth(month);
                monthMap.put(monthKey, calendarMonth);
                // monthMap中没有，calendarYear中也一定没有，所以直接add
                calendarYear.getMonths().add(calendarMonth);
            }

            // 获得年月后，添加天
            calendarMonth.getDays().add(calendarDay);
        }

        Map<String, Collection<CalendarYear>> result = new HashMap<>();
        result.put("interCalendData", yearMap.values());

        return JSON.toJSONString(result);
    }


    public static void main(String[] args) {
        // 构造数据
        List<Content> data = new ArrayList<>();
        for(int i = 1;i<12;i+=5){
            for(int m = 1;m<30;m+=10){
                Content c = new Content();
                c.setBelongYear("所属年");
                c.setDateType("公众");
                c.setName("日期 2018-"+i+"-"+m);
                c.setDescription("说明说明");
                data.add(c);
            }
        }

        // 解析
        final String json = resolve(data);
        System.out.println(json);
    }

}

class CalendarYear{
    String year;
    List<CalendarMonth> months = new ArrayList<>();

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public List<CalendarMonth> getMonths() {
        return months;
    }

    public void setMonths(List<CalendarMonth> months) {
        this.months = months;
    }
}

class CalendarMonth{
    String month;
    List<CalendarDay> days = new ArrayList<>();

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public List<CalendarDay> getDays() {
        return days;
    }

    public void setDays(List<CalendarDay> days) {
        this.days = days;
    }
}

class CalendarDay{
    String day;
    Content content;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }
}

class Content{
    String name;
    String dateType;
    String belongYear;
    String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateType() {
        return dateType;
    }

    public void setDateType(String dateType) {
        this.dateType = dateType;
    }

    public String getBelongYear() {
        return belongYear;
    }

    public void setBelongYear(String belongYear) {
        this.belongYear = belongYear;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
