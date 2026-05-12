package com.nexus.contractflow.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    private static final String SECURITY_SCHEME_NAME = "bearerAuth";

    @Bean
    public OpenAPI nexusOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Nexus Gestão - Contract Flow API")
                        .description("Sistema corporativo de gestão de ciclo de vida de contratos, "
                                + "integração com fornecedores e automação de alertas de vigência. "
                                + "Use o endpoint POST /api/v1/auth/login (credenciais padrão: admin@nexus.com / admin123) "
                                + "para obter um token JWT e clique em 'Authorize' acima para testar os endpoints protegidos.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Equipe Nexus")
                                .email("contato@nexus.com"))
                        .license(new License().name("MIT").url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server().url("/").description("Servidor local (porta 8080)")
                ))
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME, new SecurityScheme()
                                .name(SECURITY_SCHEME_NAME)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Insira o token JWT obtido em POST /api/v1/auth/login. Formato: <token>")));
    }
}
