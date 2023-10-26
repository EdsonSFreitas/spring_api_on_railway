package freitas.io.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

/**
 * @author Edson da Silva Freitas
 * {@code @created} 13/10/2023
 * {@code @project} api-railway
 */

@Configuration
public class OpenApi30Config {

    private final String moduleName;
    private final String apiVersion;
    private final String contactName;
    private final String contactUrl;
    private final String contactEmail;
    private final String apiDescription;


    public OpenApi30Config(
            @Value("${module-name}") String moduleName,
            @Value("${api-version}") String apiVersion,
            @Value("${api-contact-name}") String contactName,
            @Value("${api-contact-url}") String contactUrl,
            @Value("${api-contact-email}") String contactEmail,
            @Value("${api-description}") String apiDescription) {
        this.moduleName = moduleName;
        this.apiVersion = apiVersion;
        this.apiDescription = apiDescription;
        this.contactName = contactName;
        this.contactUrl = contactUrl;
        this.contactEmail = contactEmail;
    }

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth";
        final String apiTitle = String.format("%s API", StringUtils.capitalize(moduleName));


        Contact contact = new Contact()
                .name(contactName)
                .url(contactUrl)
                .email(contactEmail);


        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(
                        new Components()
                                .addSecuritySchemes(securitySchemeName,
                                        new SecurityScheme()
                                                .name(securitySchemeName)
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT")
                                )
                )
                .info(new Info()
                        .title(apiTitle)
                        .description(apiDescription)
                        .version(apiVersion)
                        .contact(contact)
                );
    }

    @Bean
    public GroupedOpenApi apiV1() {
        return GroupedOpenApi.builder()
                .group("api-1.0")
                .pathsToExclude("/api/v2/**", "/v2/**", "/**/v3/**")
                .pathsToMatch("/api/v1.0/**", "/v1/**")
                .build();
    }

    @Bean
    public GroupedOpenApi apiV1_1() {
        return GroupedOpenApi.builder()
                .group("api-1.1")
                .pathsToExclude("/api/v1/**", "/v1/**", "/**/v2/**", "/**/v3/**")
                .pathsToMatch("/api/v1.1/**", "/v1.1/**")
                .build();
    }

    @Bean
    public GroupedOpenApi apiV1_2() {
        return GroupedOpenApi.builder()
                .group("api-1.2")
                .pathsToExclude("/api/v1.0/**", "/v1.1/**", "/**/v2/**", "/**/v3/**")
                .pathsToMatch("/api/v1.2/**", "/v1.2/**")
                .build();
    }
}