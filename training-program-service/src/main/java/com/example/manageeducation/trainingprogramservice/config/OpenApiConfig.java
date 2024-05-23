package com.example.manageeducation.trainingprogramservice.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Admin",
                        email = "damanhbangthpt@gmail.com",
                        url = "https://manageducation.monoinfinity.net"
                ),
                description = "Website help customer can manage student and teacher.",
                title = "Manage Education",
                version = "1.0",
                license = @License(
                        name = "Licence name",
                        url = "https://manageducation.monoinfinity.net"
                ),
                termsOfService = "Terms of service"
        ),
        servers = {
                @Server(
                        description = "Prod ENV",
                        url = "https://manageducation.monoinfinity.net"
                ),
                @Server(
                        description = "Local ENV",
                        url = "http://localhost:8080"
                )
        },
        security = {
                @SecurityRequirement(
                        name = "bearerAuth"
                )
        }
)
@SecurityScheme(
        name = "bearerAuth",
        description = "JWT auth description",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
@Configuration
public class OpenApiConfig {
}
