package com.oohoo.spacestationspringbootstarter.dto.query.enums;

/**
 * @author Lei Li. lei.d.li@capgemini.com
 * @Description
 * @since 21 November 2022
 */
public enum SqlFunctionEnum {
    /**
     * 数字函数
     */
    ABS(" abs(?) ", "MATH"),
    CEIL("ceil(?)","MATH"),
    FLOOR("floor(?)","MATH"),
    RAND("rand(?)","MATH"),
    SIGN("sign(?)","MATH"),
    ROUND("round(?)","MATH"),
    ROUND_DIGITS("round(?, ?)","MATH"),
    TRUNCATE("truncate(?, ?)","MATH"),
    MOD("mod(?, ?)","MATH"),
    POW("pow(?, ?)","MATH"),
    SQRT("sqrt(?)","MATH"),
    EXP("exp(?)","MATH"),
    LOG("log(?)","MATH"),
    LOG10("log10(?)","MATH"),
    RADIANS("radians(?)","MATH"),
    DEGREES("degrees(?)","MATH"),
    SIN("sin(radians(?))","MATH"),
    ASIN("asin(radians(?))","MATH"),
    COS("cos(radians(?))","MATH"),
    ACOS("acos(radians(?))","MATH"),
    TAN("tan(radians(?))","MATH"),
    ATAN("atan(radians(?))","MATH"),
    COT("cot(radians(?))","MATH"),


    /**
     * 字符串函数
     */
    CHAR_LENGTH("char_length(?)","STRING"),
    LENGTH("length(?)","STRING"),
    UPPER("upper(?)","STRING"),
    LOWER("lower(?)","STRING"),
    CONCAT("concat(?)","STRING"),


    /**
     * 时间函数
     */
    UNIX_TIMESTAMP("unix_timestamp(?)","DATE"),
    FROM_UNIXTIME("from_unixtime(?)","DATE"),
    MONTH("month(?)","DATE"),
    MONTH_NAME("monthname(?)","DATE"),
    DAY_NAME("dayname(?)","DATE"),
    WEEK_DAY("weekday(?)","DATE"),
    WEEK("week(?)","DATE"),
    DAY_OF_YEAR("dayofyear(?)","DATE"),
    DAY_OF_MONTH("dayofMonth(?)","DATE"),
    QUARTER("quarter(?)","DATE"),
    HOUR("hour(?)","DATE"),
    MINUTE("minute(?)","DATE"),
    SECOND("second(?)","DATE"),
    TIME_TO_SEC("time_to_sec(?)","DATE"),
    SEC_TO_TIME("sec_to_time(?)","DATE"),
    DATE_DIFF("datediff(?, ?)","DATE"),
    ADD_DATE("adddate(?, ?)","DATE"),
    SUB_DATE("subdate(?, ?)","DATE"),
    ADD_TIME("addtime(?, ?)","DATE"),
    SUB_TIME("subtime(?, ?)","DATE"),


    /**
     * 条件函数
     */

    CASE_WHEN("","CDN"),
    ;

    private String value;

    private String type;

    SqlFunctionEnum(String value, String type) {
        this.value = value;
        this.type = type;
    }

    public String getValue(){
        return this.value;
    }

    public String getType() {
        return this.type;
    }
}
