package com.sean.arya.stark.backstage.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.FastDateFormat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Sean
 * @date 2018/5/8 10:08
 * @description  序列号生成器,产生标准32位序列号
 * @vesion 1.0.0
 */
@Slf4j
public class KeyGenerator {
    private static int cur=0;
    //17位时间
    private static final FastDateFormat SDF=FastDateFormat.getInstance("yyyyMMddHHmmssSSS");
    //16位ip 公网+内网 公网获取失败为0.xxx.xxx.xxx 外网获取失败取为127.xxx.xxx.xxx
    private static final String IP_HEX;
    //5位序列号
    private static final Integer MAX=999;
    private static long stamp=System.currentTimeMillis();
    private KeyGenerator(){}
    static{
        String ips="";
        int[] ipls=new int[4];
        try{
            int i=0;
            for(String s:InetAddress.getLocalHost().getHostAddress().split("\\.")){
                ipls[i++]=Integer.parseInt(s);
            }
        }catch(UnknownHostException e){
            log.error("Get current machine local ip failed:{}",e);
            Random random=new Random();
            random.setSeed(System.currentTimeMillis());
            ipls=new int[]{127,random.nextInt(256),random.nextInt(256),random.nextInt(256)};
        }
        ips =ipToHex(ipls);
        int[] ipes=new int[4];
        try{
            String[] urls=new String[]{"http://icanhazip.com/","http://ipecho.net/plain","http://checkip.amazonaws.com/","http://bot.whatismyipaddress.com"};
            boolean success=false;
            BufferedReader  in=null;
            for(String ip:urls){
                try {
                    int j=0;
                    in = new BufferedReader(new InputStreamReader(new URL(ip).openStream()));
                    String ss=in.readLine().trim();
                    for(String s:ss.split("\\.")){
                        ipes[j++]=Integer.parseInt(s);
                    }
                    success=true;
                    break;
                }catch (Exception e){
                    continue;
                }finally {
                    if(in!=null) {
                        in.close();
                    }
                }
            }
            if(!success) {
                throw new RuntimeException("get external ip from {http://checkip.amazonaws.com,http://icanhazip.com,http://www.trackip.net/ip,http://myexternalip.com/raw,http://ipecho.net/plain,http://bot.whatismyipaddress.com} failed");
            }
        }catch (Exception e){
            log.error("Get current machine external ip failed:{}",e);
            Random random=new Random();
            random.setSeed(System.currentTimeMillis());
            ipes=new int[]{0,random.nextInt(256),random.nextInt(256),random.nextInt(256)};
        }
        IP_HEX=ipToHex(ipes).concat(ips);
    }

    private static String ipToHex(int[] ipes) {
        StringBuilder sb=new StringBuilder();
        for(int ip:ipes){
            String hex=Integer.toHexString(ip).toUpperCase();
            if(hex.length()==1){
                sb.append(0);
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    public static synchronized String next(){
        try {
            long time=System.currentTimeMillis();
            if(time<stamp){
                throw new RuntimeException("当前时间小于上次时间戳!");
            }else if(stamp==time){
                if(cur>=MAX){
                    while(time<=stamp){
                        time=System.currentTimeMillis();
                    }
                    cur=0;
                }else{
                    cur++;
                }
            }else{
                cur=0;
            }
            stamp=time;
            return SDF.format(stamp).concat(IP_HEX).concat(String.format("%05d",cur));
        }catch (Exception e){
            log.error("获取序列号异常",e);
            throw new RuntimeException("获取序列号异常");
        }
    }
//    public static void main(String[] a)throws Exception{
//        System.out.println(SDF.format(stamp));
//    }
    public static void main(String[] a)throws Exception{
        final List<String> list=Collections.synchronizedList(new ArrayList<>());
        final Set<String> set=new ConcurrentSkipListSet<>();
        ExecutorService pool = Executors.newCachedThreadPool();
        pool.execute(()-> {
            for (int i = 0; i < 10000; i++) {
                String id=next();
                set.add(id);
                list.add(id);
            }
            System.out.println("final");
        });
        pool.execute(()-> {
            for (int i = 0; i < 10000; i++) {
                String id=next();
                set.add(id);
                list.add(id);
            }
            System.out.println("final");
        });
        pool.execute(()-> {
            for (int i = 0; i < 10000; i++) {
                String id=next();
                set.add(id);
                list.add(id);
            }
            System.out.println("final");
        });
        pool.execute(()-> {
            for (int i = 0; i < 10000; i++) {
                String id=next();
                set.add(id);
                list.add(id);
            }
            System.out.println("final");
        });
        pool.execute(()-> {
            for (int i = 0; i < 10000; i++) {
                String id=next();
                set.add(id);
                list.add(id);
            }
            System.out.println("final");
        });
        pool.execute(()-> {
            for (int i = 0; i < 10000; i++) {
                String id=next();
                set.add(id);
                list.add(id);
            }
            System.out.println("final");
        });
        pool.execute(()-> {
            for (int i = 0; i < 10000; i++) {
                String id=next();
                set.add(id);
                list.add(id);
            }
            System.out.println("final");
        });
        pool.execute(()-> {
            for (int i = 0; i < 10000; i++) {
                String id=next();
                set.add(id);
                list.add(id);
            }
            System.out.println("final");
        });
        pool.execute(()-> {
            for (int i = 0; i < 10000; i++) {
                String id=next();
                set.add(id);
                list.add(id);
            }
            System.out.println("final");
        });
        Thread.sleep(8000);
        File f=new File("list.txt");
        FileWriter fw=new FileWriter(f);
        for(String s:list){
            if(set.contains(s)){
                set.remove(s);
            }else{
                fw.write(s==null?"nil":s);
                fw.write("\r\n");
                fw.flush();
            }
        }
        fw.close();
    }
}
