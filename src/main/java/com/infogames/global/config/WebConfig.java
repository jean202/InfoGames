package com.infogames.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

/**
 * 업로드된 파일(첨부/에디터 이미지)을 정적 리소스로 제공.
 * 물리 경로(file.upload.path) 하위 파일을 /uploads/** 로 노출한다.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${file.upload.path:uploads}")
    private String uploadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String location = "file:" + Paths.get(uploadPath).toAbsolutePath() + "/";
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(location);
    }
}
