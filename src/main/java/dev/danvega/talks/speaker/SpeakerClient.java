package dev.danvega.talks.speaker;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;

// The base URL comes from spring.http.serviceclient.speakers in application.yaml
public interface SpeakerClient {

	@GetExchange("/speakers/{name}")
	SpeakerProfile get(@PathVariable String name);

}
