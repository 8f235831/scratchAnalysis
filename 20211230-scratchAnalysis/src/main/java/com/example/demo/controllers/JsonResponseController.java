package com.example.demo.controllers;


import com.alibaba.fastjson.JSON;
import com.example.demo.util.Analysis;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.MultipartConfigElement;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@RestController
public class JsonResponseController
{
    @RequestMapping(value = "/sourceSubmit", method = RequestMethod.POST)
    public String reply(
            HttpServletResponse response,
            @RequestParam(value = "file-data", required = true) MultipartFile file
    )
    {
        byte[] content = new byte[0];
        try
        {
            if(file == null)
            {
                response.sendError(400, "Bad Request");
                return "Failed to upload file.";
            }
            try(InputStream inputStream = file.getInputStream())
            {
                content = inputStream.readAllBytes();
                ZipInputStream zipInputStream = new ZipInputStream(
                        new ByteArrayInputStream(content));
                while(true)
                {
                    ZipEntry zipEntry = zipInputStream.getNextEntry();
                    if(zipEntry == null)
                    {
                        response.sendError(400, "Bad Request");
                        return "Failed to parse file.";
                    }
                    if(!zipEntry.isDirectory() &&
                       zipEntry.getName().equals("project.json"))
                    {
                        if(zipEntry.getSize() >= 100000)
                        {
                            response.sendError(413, "Request Entity Too Large");
                            return "Source code is too large to unzip.";
                        }
                        else
                        {
                            //unzip d
                            content = zipInputStream.readAllBytes();
                            return JSON.toJSONString(
                                    Analysis.analyse(new String(content)));
                        }
                    }
                }
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
            return "failed";
        }
    }
}

@Configuration
class JsonResponseControllerConfig
{
    // 设置文件上传限制小于15MB。
    @Bean
    public MultipartConfigElement multipartConfigElement()
    {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(DataSize.parse("100MB"));
        factory.setMaxRequestSize(DataSize.parse("100MB"));
        return factory.createMultipartConfig();
    }
}