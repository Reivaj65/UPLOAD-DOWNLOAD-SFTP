package com.example.demo.upload;

import com.jcraft.jsch.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.expression.common.LiteralExpression;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.sftp.outbound.SftpMessageHandler;
import org.springframework.integration.sftp.session.DefaultSftpSessionFactory;

import java.io.*;
import java.time.LocalDateTime;
import java.util.Properties;

@Configuration
public class uploadConfiguration {

    private String host = "192.168.200.20";
    private Integer port=22;
    private String user ="desarollo";
    private String password = "Int3redes1";

    private JSch jsch;
    private Session session;
    private Channel channel;
    private ChannelSftp sftpChannel;

    private String remoteDir ="/home/desarollo/palermodev/fotos_francisco";
    private String RfileName = "/home/desarollo/palermodev/fotos_francisco/cocina.jpg";
    private String localDir = "C:\\Users\\lenovo\\Desktop\\sftpDownloads";
    private String LfileName = "C:\\Users\\lenovo\\Pictures\\ferreteria\\iluminacion.jpg";

    /*public DefaultSftpSessionFactory gimmeFactory(){
        DefaultSftpSessionFactory factory = new DefaultSftpSessionFactory();
        factory.setHost("0.0.0.0");
        factory.setPort(22);
        factory.setAllowUnknownKeys(true);
        factory.setUser("javier");
        factory.setPassword("12345");
        return factory;
    }


    @Bean
    @ServiceActivator(inputChannel = "uploadChannel")
    SftpMessageHandler uploadHandler(){
        SftpMessageHandler messageHandler= new SftpMessageHandler(gimmeFactory());
        messageHandler.setRemoteDirectoryExpression(new LiteralExpression("/upload"));
        messageHandler.setFileNameGenerator(message -> String.format("myImage_%s.jpg", LocalDateTime.now()));
        return messageHandler;
    }*/

    public void connect() {

        System.out.println("connecting..." + host);
        try {
            jsch = new JSch();
            session = jsch.getSession(user, host, port);
            session.setConfig("StrictHostKeyChecking", "no");
            session.setPassword(password);
            session.connect();

            channel = session.openChannel("sftp");
            channel.connect();
            sftpChannel = (ChannelSftp) channel;

        } catch (JSchException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        System.out.println("disconnecting...");
        sftpChannel.disconnect();
        channel.disconnect();
        session.disconnect();
    }

    public void upload() {

        connect();
        try {
            // Change to output directory
            sftpChannel.cd(remoteDir);

            // Upload file
            File localFile  = new File(LfileName);
            sftpChannel.put(localFile.getAbsolutePath(),localFile.getName());

            System.out.println("File uploaded successfully - "+ localFile.getAbsolutePath());

        } catch (Exception e) {
            e.printStackTrace();
        }
        disconnect();
    }

    public void download() {

        byte[] buffer = new byte[1024];
        BufferedInputStream bis;
        connect();
        try {
            // Change to output directory
            String cdDir = RfileName.substring(0, RfileName.lastIndexOf("/") + 1);
            sftpChannel.cd(cdDir);

            File file = new File(RfileName);
            bis = new BufferedInputStream(sftpChannel.get(file.getName()));

            File newFile = new File(localDir + "/" + file.getName());

            // Download file
            OutputStream os = new FileOutputStream(newFile);
            BufferedOutputStream bos = new BufferedOutputStream(os);
            int readCount;
            while ((readCount = bis.read(buffer)) > 0) {
                bos.write(buffer, 0, readCount);
            }
            bis.close();
            bos.close();
            System.out.println("File downloaded successfully - "+ file.getAbsolutePath());

        } catch (Exception e) {
            e.printStackTrace();
        }
        disconnect();
    }



}
