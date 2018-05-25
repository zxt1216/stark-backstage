package com.sean.arya.stark.backstage.common.utils;

import com.google.gson.*;
import com.google.gson.internal.bind.DateTypeAdapter;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Sean
 * @date 2018/5/11 14:00
 * @description 简单封装gson
 * @vesion 1.0.0
 */
@Slf4j
public class GsonUtil {
    private static final Gson gson=new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss")
            .registerTypeAdapter(Date.class,new DateTypeAdapter())
            .registerTypeAdapter(LocalDate.class,(JsonSerializer<LocalDate>) (localDate,type,jsonSerializationContext)->new JsonPrimitive(localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
            .registerTypeAdapter(LocalDateTime.class,(JsonSerializer<LocalDateTime>)(localDateTime,type,jsonSerializationContext)-> new JsonPrimitive(localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))))
            .registerTypeAdapter(LocalDate.class,(JsonDeserializer<LocalDate>) (localDate,type,jsonDeserializationContext)->LocalDate.parse(localDate.getAsJsonPrimitive().getAsString(),DateTimeFormatter.ISO_LOCAL_DATE))
            .registerTypeAdapter(LocalDateTime.class,(JsonDeserializer<LocalDateTime>)(localDateTime,type,jsonDeserializationContext)-> LocalDateTime.parse(localDateTime.getAsJsonPrimitive().getAsString(),DateTimeFormatter.ISO_LOCAL_DATE_TIME))
            .create();
    private static final JsonParser jsonParser= new JsonParser();
    private GsonUtil(){}

    /**
     * json 转 T 对象
     * @param json
     * @param type 类型
     * @param <T>
     * @return
     */
    public static <T> T fromJson(String json, Type type){
        try{
            gson.fromJson(json, type);
        }catch (JsonSyntaxException e){
            log.error("gson parse to Object exception",e);
        }
        return null;
    }

    /**
     * json 转 T 对象
     * @param json
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T fromJson(String json, Class<T> clazz){
        try{
            gson.fromJson(json,clazz);
        }catch (JsonSyntaxException e){
            log.error("gson parse to Object exception",e);
        }
        return null;
    }

    /**
     *  对象 转 json
     * @param obj
     * @return
     */
    public static String toJson(Object obj) {
        try {
            return gson.toJson(obj);
        } catch (Exception e) {
            log.error("gson parse to String error", e);
        }
        return null;
    }

    /**
     * 对象 转 json
     * @param obj
     * @param type
     * @return
     */
    public static String toJson(Object obj, Type type) {
        try {
            return gson.toJson(obj, type);
        } catch (Exception e) {
            log.error("gson parse to String error", e);
        }
        return null;
    }

    /**
     * map 转 json对象  注意  若map.getValue()是一个JsonElement时会被转为Josn,否则转为String
     * @param map
     * @return
     */
    public static String toJson(Map<String, Object> map) {
        if (null == map){
            return "";
        }
        JsonObject jsonObject = new JsonObject();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if(entry.getValue() instanceof JsonElement){
                jsonObject.add(entry.getKey(), (JsonElement) entry.getValue());
            }else {
                jsonObject.addProperty(entry.getKey(), entry.getValue().toString());
            }
        }
        return jsonObject.toString();
    }

    /**
     * 合并对象
     * @param src 源对象
     * @param dist 目的对象
     * @return
     */
    public static void append(JsonObject src,JsonObject dist){
        for(Map.Entry<String,JsonElement> entry:src.entrySet()){
            dist.add(entry.getKey(),entry.getValue());
        }
    }

    /**
     * 将  JsonObject 转为 Map  注意 Map的值均为jsonElement.getAsString()的结果
     * @param src JsonObject
     * @return
     */
    public static Map<String,String> toMap(JsonObject src){
        Map<String, String> map=new HashMap<>();
        for(Map.Entry<String,JsonElement> entry:src.entrySet()){
            map.put(entry.getKey(),entry.getValue().getAsString());
        }
        return map;
    }
    /**
     *  json 转 JsonObject
     * @param json json对象，不能是json数组
     * @return
     */
    public static JsonObject toJsonObject(String json){
        return jsonParser.parse(json).getAsJsonObject();
    }
    /**
     * jsonArray 转JsonArray
     * @param jsonArray  json数组,不能是json对象
     * @return
     */
    public static JsonArray toJsonArray(String jsonArray){
        return jsonParser.parse(jsonArray).getAsJsonArray();
    }
//    public static void main(String[] a) {
//        Map<String, Object> map=new HashMap<>();
//        map.put("1","fdsnjlnfjd");
//        map.put("2","tt");
//        map.put("3","ttttt");
//        map.put("4","fdsnjlttnfjd");
//        map.put("5",new JsonObject());
//        ((JsonObject)map.get("5")).addProperty("6","dfkd");
//        System.out.println(toJson(map));
//        JsonObject jo=new JsonObject();
//        jo.addProperty("a","b");
//        Map<String, String> map2 = toMap(jo);
//        System.out.println(map2);
//        String json = "[{\"1\":\"2\"},{\"A\":\"B\"}]";
//        System.out.println(toJson(toJsonArray(json)));
//    }
}
