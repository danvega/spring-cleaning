package dev.danvega.talks.speaker;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "speakers", url = "${speakers.url}")
public interface SpeakerClient {

	@GetMapping("/speakers/{name}")
	SpeakerProfile get(@PathVariable String name);

}
