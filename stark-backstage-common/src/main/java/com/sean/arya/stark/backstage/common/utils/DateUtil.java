package com.sean.arya.stark.backstage.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author Sean
 * @date 2018/5/10 13:22
 * @description  Date 与LocalDate/LocalDateTime 互相转换  toString
 * 默认toString 格式 yyyy-MM-dd (HH:mm:ss)
 * @vesion 1.0.0
 */
public class DateUtil {
    public static final int _DATE=0;
    public static final int _TIME=1;
    private static final String DATE_FORMAT="yyyy-MM-dd";
    private static final String TIME_FORMAT="yyyy-MM-dd HH:mm:ss";
    private DateUtil(){}
    //transformer
    public static Date toDate(LocalDate date){
        return toDate(date.atTime(0,0,0));
    }
    public static Date toDate(LocalDateTime date){
        return Date.from(date.atZone(ZoneId.systemDefault()).toInstant());
    }
    public static LocalDate toLocalDate(Date date){
        return toLocalDateTime(date).toLocalDate();
    }
    public static LocalDateTime toLocalDateTime(Date date){
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }
    public static Date toDate(String date)throws ParseException{
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);
    }
    public static LocalDate toLocalDate(String date){
        return LocalDate.parse(date,DateTimeFormatter.ofPattern(DATE_FORMAT));
    }
    public static LocalDateTime toLocalDateTime(String date){
        return LocalDateTime.parse(date,DateTimeFormatter.ofPattern(TIME_FORMAT));
    }

    //default toString
    /**
     * 转化为String
     * @param date 日期
     * @return
     */
    public static String toString(Date date){
        return toString(date,_TIME);
    }
    /**
     * 转化为String
     * @param date 日期
     * @param mode mode 0 日期格式 1 日期+时间格式
     * @return
     */
    public static String toString(Date date,int mode){
        switch(mode){
            case _DATE:
                return new SimpleDateFormat(DATE_FORMAT).format(date);
            case _TIME:
                return new SimpleDateFormat(TIME_FORMAT).format(date);
            default:
                return new SimpleDateFormat(TIME_FORMAT).format(date);
        }
    }
    public static String toString(LocalDate date){
        return date.format(DateTimeFormatter.ofPattern(DATE_FORMAT));
    }
    public static String toString(LocalDateTime date){
        return toString(date,_TIME);
    }
    /**
     * 转化为String
     * @param date
     * @param mode mode 0 日期格式 1 日期+时间格式
     * @return
     */
    public static String toString(LocalDateTime date,int mode){
        switch(mode){
            case _DATE:
                return date.format(DateTimeFormatter.ofPattern(DATE_FORMAT));
            case _TIME:
                return date.format(DateTimeFormatter.ofPattern(TIME_FORMAT));
            default:
                return date.format(DateTimeFormatter.ofPattern(TIME_FORMAT));
        }
    }
    //define toString
    public static String toString(Date date,String pattern){
        return new SimpleDateFormat(pattern).format(date);
    }
    public static String toString(LocalDate date,String pattern){
        return date.format(DateTimeFormatter.ofPattern(pattern));
    }
    public static String toString(LocalDateTime date,String pattern){
        return date.format(DateTimeFormatter.ofPattern(pattern));
    }

    public static void main(String[] args){
        System.out.println(toLocalDateTime("2017-02-01"));
    }
}
