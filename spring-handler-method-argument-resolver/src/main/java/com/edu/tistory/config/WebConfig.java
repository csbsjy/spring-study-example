package com.edu.tistory.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.edu.tistory.custom.LoginUserResolver;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public LoginUserResolver loginUserResolver() {
        return new LoginUserResolver();
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginUserResolver());
        WebMvcConfigurer.super.addArgumentResolvers(resolvers);
    }
}
