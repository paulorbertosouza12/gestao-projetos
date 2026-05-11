package br.com.paulobarros.config;


import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    OpenAPI customOpenAPI(){
        return new OpenAPI()
             .info(new Info()
                     .title("Sistema de Gestão de Projetos")
                         .version("1.0.0")
                         .description("API para gerenciamento de projetos")
                         .license(new License()
                                 .name("Apache 2.0"))
             );
    }

}
