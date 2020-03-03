package com.example.demo.upload;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

@RestController
@RequiredArgsConstructor
public class UploadController {

    private final UploadMessagingGateway gateway;
    private final uploadConfiguration configuration;

    @GetMapping("api/upload")
    public String uploadFile(){
        String resultUpload = "Servicio ejecutado";

        /*File file = new File("C:\\Users\\lenovo\\Pictures\\ferreteria\\cocina.jpg");
        gateway.uploadFile(file);*/

        configuration.upload();

        //configuration.download();

        return resultUpload;
    }

}
