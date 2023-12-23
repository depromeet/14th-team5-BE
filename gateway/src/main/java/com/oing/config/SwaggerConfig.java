package com.oing.config;

import com.oing.config.properties.WebProperties;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

@OpenAPIDefinition(
        servers = @Server(url = "https://dev.api.no5ing.kr/", description = "Default Server URL"),
        info = @Info(
                title = "no5ing 백엔드 API 명세",
                description = "springdoc을 이용한 Swagger API 문서입니다.",
                version = "1.0",
                contact = @Contact(
                        name = "springdoc 공식문서",
                        url = "https://springdoc.org/"
                )
        )
)
@RequiredArgsConstructor
@Configuration
public class SwaggerConfig {
    private final WebProperties webProperties;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components().addSecuritySchemes("앱 엑세스 토큰", bearerAuth()))
                .addSecurityItem(new SecurityRequirement().addList("앱 엑세스 토큰"));
    }

    public SecurityScheme bearerAuth() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.APIKEY)
                .in(SecurityScheme.In.HEADER)
                .name(webProperties.headerNames().accessToken());
    }
}
