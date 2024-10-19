package ru.guryanov.daf.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // Разрешить CORS для всех путей
                .allowedOrigins("null")  // Разрешить доступ для вашего домена
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")  // Разрешить указанные методы
                .allowedHeaders("*")  // Разрешить любые заголовки
                .allowCredentials(true);  // Если нужны куки или авторизация
    }
}