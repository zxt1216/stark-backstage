package com.sean.arya.stark.backstage.common.utils;

import com.jcraft.jsch.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Sean
 * @date 2018/5/13 12:52
 * @description  Sftp工具类,连结用后需要手动关闭
 * @vesion 1.0.0
 */
@Slf4j
public class Sftp {
    private String host;
    private int port=22;
    private String username;
    private String password;
    private int timeout=0;
    //异步上传(文件夹)线程池大小
    private int poolSize=10;
    public Sftp(String host,Integer port,String username,String password,Integer timeout){
        this.host=host;
        this.port=port==null?this.port:port;
        this.username=username;
        this.password=password;
        this.timeout=timeout==null?this.timeout:timeout;
    }
    public Sftp(String host,Integer port,String username,String password,Integer timeout,Integer poolSize){
        this.host=host;
        this.port=port==null?this.port:port;
        this.username=username;
        this.password=password;
        this.timeout=timeout==null?this.timeout:timeout;
        this.poolSize=poolSize==null?this.poolSize:poolSize;
    }
    public ChannelSftp getChannel()throws JSchException {
        JSch jsch = new JSch();
        Session session = jsch.getSession(username, host, port);
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);//为session对象设置properties
        session.setTimeout(timeout); // 设置超时时间/单位毫秒
        session.setPassword(password); // 设置密码
        session.connect();
        Channel channel = session.openChannel("sftp");
        channel.connect();
        return (ChannelSftp) channel;
    }
    public void release(ChannelSftp channel)throws JSchException{
        if(channel!=null){
            Session session=channel.getSession();
            if(session!=null){
                session.disconnect();
            }
            channel.disconnect();
        }
    }

    /**
     * 上传
     * 若source为文件夹,则name不需要传递[即使传递也会被忽略]
     * 若source为文件,则name选传,缺省时,使用源文件名称
     * @param source
     * @param dist 目的目录
     * @param name 目的文件名
     * @param async
     */
    public void upload(String source,String dist,String name,Boolean async) throws JSchException, SftpException{        
        File file= new File(source);
        if(!file.exists())return;
        try {
            if (file.isDirectory()) {
                ChannelSftp channel=getChannel();
                boolean mk=mkdir(dist,channel);
                if(!mk) {
                    release(channel);
                    log.error("make dir:"+dist+"error!");
                    throw new RuntimeException("create dir :"+dist+" error!");
                }
                try {
                    ExecutorService server=Executors.newFixedThreadPool(poolSize);
                    uploadDir(file, channel, ".", async, server);
                    server.shutdown();
                }finally {
                    release(channel);
                }
            } else {
                name = StringUtils.isEmpty(name) ? file.getName() : name;
                try (final InputStream is = new FileInputStream(file)) {
                    upload(is, dist, name, async);
                }
            }
        }catch (IOException e) {
            log.error("upload file source:{},dist:{},name:{} error!", source, dist, name, e);
            throw new RuntimeException(e);
        }
    }

    private void uploadDir(File file, ChannelSftp channel, String sub, Boolean async, ExecutorService server)throws SftpException,IOException {
        for(File f: file.listFiles()){
            if(f.isDirectory()){
                uploadDir(f,channel,sub+"/"+f.getName(),async,server);
            }else{
                try (final InputStream is = new FileInputStream(f)) {
                    if(async!=null && async) {
                        server.submit(new Thread(()->{
                            try{
                                channel.put(is, sub + "/"+ f.getName());
                            }catch (Exception e){
                                log.error("upload file to dir:{} error!",sub,e);
                            }
                        }));
                    }else{
                        channel.put(is, sub+"/"+f.getName());
                    }
                }
            }
        }
    }

    /**
     * 上传
     * @param bytes 字节流
     * @param dir   目录
     * @param name  文件名
     * @param async  是否异步
     * @throws JSchException
     * @throws SftpException
     */
    public void upload(byte[] bytes,final String dir,final String name,Boolean async)throws JSchException,SftpException{
        this.upload(new ByteArrayInputStream(bytes),dir,name,async);
    }

    /**
     * 上传
     * @param is 输入流
     * @param dir 目录
     * @param name 文件名
     * @param async 是否异步
     * @throws JSchException
     * @throws SftpException
     */
    private void upload(final InputStream is,final String dir,final String name, Boolean async)throws JSchException,SftpException{
        ChannelSftp channel=getChannel();
        boolean mk=mkdir(dir,channel);
        if(!mk){
            release(channel);
            log.error("create dir:{} failed",dir);
            throw new RuntimeException("create dir:"+dir+"error!");
        }
        if(async!=null && async){
            new Thread(()->{
                try{
                    try {
                        channel.put(is, name);
                    }finally {
                        release(channel);
                    }                    
                }catch (Exception e){
                    log.error("upload file to dir:{} error!",dir,e);
                }
            }).start();
        }else{
            try {
                channel.put(is, name);
            }finally {
                release(channel);
            }           
        }
    }
    /**
     * 上传
     * @param is 输入流
     * @param dir 目录
     * @param name 文件名
     * @throws JSchException
     * @throws SftpException
     */
    public void upload(final InputStream is,final String dir,final String name)throws JSchException,SftpException{
        this.upload(is,dir,name,false);
    }

    /**
     * 下载
     * 如果source为目录 isDir必须指明为true  name传值也会被忽略
     * 如果source为文件 则name选传,为空时默认源文件名称
     * @param source 目录或文件
     * @param dist 目录
     * @param name 名称
     * @param isDir 是否是目录[默认是文件]
     * @throws JSchException
     * @throws SftpException
     */
    public void download(final String source,String dist,final String name,Boolean isDir,Boolean async)throws JSchException,SftpException{
        ChannelSftp channel=getChannel();
        if(isDir!=null && isDir){
            try {
                if (!StringUtils.isEmpty(source)) {
                    channel.cd(source);
                }
                if(async!=null && async){
                    ExecutorService server=Executors.newFixedThreadPool(poolSize);
                    downloadDir("./", dist.endsWith("/") ? dist : dist + "/", channel, server);
                    server.shutdown();
                }else {
                    downloadDir("./", dist.endsWith("/") ? dist : dist + "/", channel);
                }
            }finally {
                release(channel);
            }
        }else {

            if(!dist.endsWith("/")){
                dist=dist+"/";
            }
            final String distName;
            if(StringUtils.isEmpty(name)){
                String[] folders=source.split("/");
                distName=dist+folders[folders.length-1];
            }else{
                distName=dist+name;
            }
            if(async!=null && async){
                new Thread(()->{
                    try{
                        try {
                            channel.get(source, distName);
                        }finally {
                            release(channel);
                        }
                    }catch (Exception e){
                        log.error("down file from dir:{} error!",source,e);
                    }
                }).start();
            }else{
                try {
                    channel.get(source, distName);
                }finally {
                    release(channel);
                }
            }
        }
    }

    private void downloadDir(String source,String dist,ChannelSftp channel, ExecutorService server)throws SftpException {
        Vector v=channel.ls(source);
        Enumeration enu = v.elements();//ChannelSftp.LsEntry
        while(enu.hasMoreElements()){
            ChannelSftp.LsEntry entity=(ChannelSftp.LsEntry)enu.nextElement();
            if(entity.getFilename().equals(".") || entity.getFilename().equals(".."))continue;
            if(entity.getAttrs().isDir()){
                downloadDir(source+entity.getFilename()+"/",dist+entity.getFilename()+"/",channel,server);
            }else{
                server.submit(new Thread(()->{
                    try{
                        channel.get(source+entity.getFilename(),dist+entity.getFilename());
                    }catch (Exception e){
                        log.error("down file from dir:{} error!",source,e);
                    }
                }));
            }
        }
    }

    private void downloadDir(String source,String dist,ChannelSftp channel)throws SftpException{
        Vector v=channel.ls(source);
        Enumeration enu = v.elements();//ChannelSftp.LsEntry
        while(enu.hasMoreElements()){
            ChannelSftp.LsEntry entity=(ChannelSftp.LsEntry)enu.nextElement();
            if(entity.getFilename().equals(".") || entity.getFilename().equals(".."))continue;
            if(entity.getAttrs().isDir()){
                downloadDir(source+entity.getFilename()+"/",dist+entity.getFilename()+"/",channel);
            }else{
                channel.get(source+entity.getFilename(),dist+entity.getFilename());
            }
        }
    }

    /**
     * 下载文件到输出流
     * @param source 文件 必须是文件
     * @param os 输出流
     * @throws JSchException
     * @throws SftpException
     */
    public void download(String source,OutputStream os)throws JSchException,SftpException{
        ChannelSftp channel = getChannel();
        try {            
            channel.get(source, os);
        }finally{
            release(channel);
        }
    }

    /**
     * @param dir 目录文件
     * @param channel
     * @return
     */
    private boolean mkdir(String dir,ChannelSftp channel){
        if(dir.startsWith("/")){
            try {
                channel.cd("/");
            } catch (SftpException e) {
                log.error("cd into / error!");
            }
            dir=dir.substring(1);
        }
        if(dir.endsWith("/")){
            dir=dir.substring(0,dir.length()-1);
        }
        if(dir.length()==0)return true;
        String[] folders=dir.split("/");
        try {
            for (String folder : folders) {
                try {
                    channel.cd(folder);
                } catch (SftpException se) {
                    channel.mkdir(folder);
                }
            }
        }catch (Exception e){
            return false;
        }
        return true;
    }

    /**
     * 删除目录/文件
     * @param dir 目录或文件
     * @param force 是否强制删除
     * @throws JSchException
     * @throws SftpException
     */
    public void remove(String dir,Boolean force)throws JSchException,SftpException{
        if(StringUtils.isEmpty(dir))return;
        ChannelSftp channel =getChannel();
        try {
            if (force != null && force) {
                try {
                    channel.rmdir(dir);
                } catch (SftpException se) {
                    rmdir(dir.endsWith("/")?dir:dir+"/",channel);
                }
            } else {
                channel.rmdir(dir);
            }
        }finally {
            release(channel);
        }
    }

    /**
     * 删除目录
     * @param dir 目录 不能是文件
     * @param channel
     * @throws SftpException
     */
    private void rmdir(String dir, ChannelSftp channel)throws SftpException{
        Vector v=channel.ls(dir);
        Enumeration enu = v.elements();//ChannelSftp.LsEntry
        while(enu.hasMoreElements()){
            ChannelSftp.LsEntry entity=(ChannelSftp.LsEntry)enu.nextElement();
            if(entity.getFilename().equals(".") || entity.getFilename().equals(".."))continue;
            if(entity.getAttrs().isDir()){
                rmdir(dir+entity.getFilename()+"/",channel);
            }else{
                channel.rm(dir+entity.getFilename());
            }
        }
        channel.rmdir(dir);
    }
    public List<ChannelSftp.LsEntry> list(String dir)throws SftpException,JSchException{
        if(StringUtils.isEmpty(dir)){
            dir=".";
        }
        ChannelSftp channel=getChannel();
        List<ChannelSftp.LsEntry> result=new ArrayList<>();
        try {
            if (dir.trim().equals(".")) dir = channel.pwd();
            Vector v = channel.ls(dir);
            Enumeration enu = v.elements();//ChannelSftp.LsEntry
            while (enu.hasMoreElements()) {
                ChannelSftp.LsEntry entity = (ChannelSftp.LsEntry) enu.nextElement();
                if (entity.getFilename().equals(".") || entity.getFilename().equals("..")) continue;
                result.add(entity);
            }
        } finally{
            release(channel);
        }
        return result;
    }


    public static void main(String[] args)throws Exception{
        Sftp sftp=new Sftp("192.168.130.215",22,"loan","loan123",0,10);
        List<ChannelSftp.LsEntry> list3 = sftp.list("files");
        for(ChannelSftp.LsEntry entry:list3){
            System.out.println(entry.getFilename());
        }
    }
}
