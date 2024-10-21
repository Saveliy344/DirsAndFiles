package ru.guryanov.daf.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // Разрешить CORS для всех путей
                .allowedOrigins("http://localhost:3000")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")  // Разрешить указанные методы
                .allowedHeaders("*")  // Разрешить любые заголовки
                .allowCredentials(true);  // Если нужны куки или авторизация
    }
}