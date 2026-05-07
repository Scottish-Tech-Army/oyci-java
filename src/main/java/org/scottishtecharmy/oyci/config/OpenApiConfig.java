package org.scottishtecharmy.oyci.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI oyciOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("OYCI Event State Service API")
                        .description("REST API for Ochills Youth Community Improvement (OYCI) application. " +
                                "Manage staff, qualifications, event types, event instances, and designations.")
                        .version("v1.0")
                        .contact(new Contact()
                                .name("OYCI Development Team")
                                .email("dev@oyci.org")));
    }
}
