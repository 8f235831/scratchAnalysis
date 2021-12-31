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

/**
 * Controller类。
 */
@RestController
public class JsonResponseController
{
    /**
     * 响应对/sourceSubmit的POST请求，其中file-data项为MultipartFile类型且必需。
     * 采用在服务器端解压的模式。
     *
     * @param file 上传的文件。
     */
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
                // 上传失败，返回400。
                response.sendError(400, "Bad Request");
                return "Failed to upload file.";
            }
            try(InputStream inputStream = file.getInputStream())
            {
                // 按zip格式解压sb3文件。
                content = inputStream.readAllBytes();
                ZipInputStream zipInputStream = new ZipInputStream(
                        new ByteArrayInputStream(content));
                // 首先读取zipEntry。
                while(true)
                {
                    // 寻找project.json文件。
                    ZipEntry zipEntry = zipInputStream.getNextEntry();
                    if(zipEntry == null)
                    {
                        // 上传的压缩文件不包含project.json文件，返回400。
                        response.sendError(400, "Bad Request");
                        return "Failed to parse file.";
                    }
                    if(!zipEntry.isDirectory() &&
                       zipEntry.getName().equals("project.json"))
                    {
                        // 找到project.json后执行如下：
                        if(zipEntry.getSize() >= 500000)
                        {
                            // project.json文件大于500kb，返回413。
                            response.sendError(413, "Request Entity Too Large");
                            return "Source code is too large to unzip.";
                        }
                        else
                        {
                            // 解压zip文件，并返回解析结果。
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
            // 任何错误。
            e.printStackTrace();
            return "failed";
        }
    }
}

@Configuration
class JsonResponseControllerConfig
{
    /**
     * 设置文件上传限制小于15MB。
     */
    @Bean
    public MultipartConfigElement multipartConfigElement()
    {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(DataSize.parse("100MB"));
        factory.setMaxRequestSize(DataSize.parse("100MB"));
        return factory.createMultipartConfig();
    }
}