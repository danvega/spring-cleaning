package dev.danvega.talks.config;

import org.springframework.boot.jackson.autoconfigure.JsonMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.SerializationFeature;

@Configuration
public class JacksonConfig {

	@Bean
	JsonMapperBuilderCustomizer prettyPrint() {
		return builder -> builder.enable(SerializationFeature.INDENT_OUTPUT);
	}

}
