package dev.danvega.talks.config;

import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

	@Bean
	Jackson2ObjectMapperBuilderCustomizer prettyPrint() {
		return builder -> builder.featuresToEnable(SerializationFeature.INDENT_OUTPUT);
	}

}
