package cat.institutmarianao.sailing.ws;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Sailing", version = "2024-2025"))
@SecurityScheme(name = "Bearer Authentication", type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT")
//@SecurityScheme(name = "Basic Authentication", type = SecuritySchemeType.HTTP, scheme = "basic", in = SecuritySchemeIn.HEADER)
public class SailingWsApplication {
	public static final String DATE_PATTERN = "yyyy-MM-dd";
	public static final String TIME_PATTERN = "HH:mm";
	public static final String DATE_TIME_PATTERN = DATE_PATTERN + " " + TIME_PATTERN;

	public static void main(String[] args) {
		SpringApplication.run(SailingWsApplication.class, args);
	}

}
