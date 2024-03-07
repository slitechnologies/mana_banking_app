package com.manaba.manababanking;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.context.annotation.Configuration;


@OpenAPIDefinition(info = @Info(
        title = "Mana Bank APP API",
        description = "This is a Spring Boot Banking App API",
        version = "1.0.1",

        contact = @Contact(
                name = "Tinodashe Kayenie",
                email = "superlightintellex@gmail.com",
                url = "https://github.com/slitechnologies/mana_banking_app"
        ),
        license = @License(
                name = "sli Technologies",
                url = "https://github.com/slitechnologies/mana_banking_app"
        )
),
        externalDocs = @ExternalDocumentation(
                description="The sli Technologies Banking App Documentation",
                url = "https://github.com/slitechnologies/mana_banking_app"
        )

)
@Configuration
public class OpenApIConfig {

}
