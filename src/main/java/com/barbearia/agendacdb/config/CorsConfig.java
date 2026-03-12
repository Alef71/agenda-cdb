package com.barbearia.agendacdb.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        
        // Libera os cookies/tokens se necessário
        config.setAllowCredentials(true); 
        
        // Libera a porta do seu Live Server (VS Code)
        config.setAllowedOrigins(Arrays.asList("http://127.0.0.1:5500", "http://localhost:5500")); 
        
        // Libera todos os cabeçalhos
        config.setAllowedHeaders(Arrays.asList("*")); 
        
        // Libera todos os métodos HTTP (inclusive o OPTIONS que está dando erro no preflight)
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config); // Aplica para todas as rotas da API
        
        return new CorsFilter(source);
    }
}