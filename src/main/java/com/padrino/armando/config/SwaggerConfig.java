package com.padrino.armando.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("AGV Llantas - API")
                        .version("1.0")
                        .description("Sistema de Gestión de Inventario para AGV Neutécnica E.I.R.L.")
                        .contact(new Contact()
                                .name("AGV Llantas")
                        )
                );
    }
}