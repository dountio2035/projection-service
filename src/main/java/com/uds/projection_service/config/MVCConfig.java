package com.uds.projection_service.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MVCConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(@SuppressWarnings("null") ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**")
        .addResourceHandler("/videos/**")
                .addResourceLocations("classpath:/projection/images/")
                .addResourceLocations("classpath:/projection/videos/");
    }
}
