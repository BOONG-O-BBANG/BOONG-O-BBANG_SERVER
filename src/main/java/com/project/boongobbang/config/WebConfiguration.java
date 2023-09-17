package com.project.boongobbang.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 모든 요청 경로에 대해
                .allowedMethods("GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS") // 허용할 HTTP 메소드
                .allowedHeaders("*") // 허용할 헤더
                .allowedOrigins(
                        "http://boong-vpc-ec2-deploy-lb-999176414.ap-northeast-2.elb.amazonaws.com",
                        "http://d2bczezs33iv0e.cloudfront.net"
                ) // 허용할 origin을 명시적으로 설정
                .allowCredentials(true); // 쿠키, 헤더 정보 등을 포함할 지 결정
    }
}