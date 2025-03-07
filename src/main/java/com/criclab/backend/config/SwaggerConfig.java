package com.criclab.backend.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPIConfig() {
        return new OpenAPI().info(
                        new Info()
                                .title("CricLab Backend API")
                                .description("A Real-time Cricket Score Application by Avaneesh Chopdekar")
                                .version("1.0.0")
                )
                .servers(
                        List.of(
                                new Server().url("http://localhost:8080")
                                        .description("Local Development Server"),
                                new Server().url("https://criclab-backend.onrender.com")
                                        .description("Production Server")
                        )
                ) // TODO: Change production server url after deployment
                .tags(
                        List.of(
                                new Tag().name("Admin Authentication").description("Endpoints for authentication"),
                                new Tag().name("User Management").description("Endpoints for user management"),
                                new Tag().name("Matches").description("Endpoints for managing matches")
                        )
                )
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth")
                )
                .components(new Components().addSecuritySchemes(
                        "bearerAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .in(SecurityScheme.In.HEADER)
                                .name("Authorization")
                ));
    }
}