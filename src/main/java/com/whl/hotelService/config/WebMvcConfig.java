package com.whl.hotelService.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/js/**").addResourceLocations("classpath:/static/js/"); //.setCachePeriod(60*60*24*365);
        registry.addResourceHandler("/css/**").addResourceLocations("classpath:/static/css/"); //.setCachePeriod(60*60*24*365);
        registry.addResourceHandler("/images/**").addResourceLocations("classpath:/static/images/"); //.setCachePeriod(60*60*24*365);
        registry.addResourceHandler("/json/**").addResourceLocations("classpath:/static/json/"); //.setCachePeriod(60*60*24*365);
        registry.addResourceHandler("/font/**").addResourceLocations("classpath:/static/font/");//.setCachePeriod(60*60*24*365);
        // http://localhost:8080/imageboard/[이미지경로] 입력하면 이미지가 나옴. 이게 없으면 이미지 접근 불가능
        registry.addResourceHandler("/hotelimage/**").addResourceLocations("file:/hotelimage/");//.setCachePeriod(60*60*24*365);
        registry.addResourceHandler("/roomimage/**").addResourceLocations("file:/roomimage/");//.setCachePeriod(60*60*24*365);
    }


}