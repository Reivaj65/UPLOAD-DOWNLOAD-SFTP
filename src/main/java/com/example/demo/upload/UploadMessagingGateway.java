package com.example.demo.upload;


import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

import java.io.File;

@MessagingGateway
public interface UploadMessagingGateway {

    @Gateway(requestChannel = "uploadChannel")
    public void uploadFile(File file);

}
