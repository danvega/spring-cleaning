package dev.danvega.talks.speaker;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Stands in for an external speaker API so the demo runs offline.
 * It fails two calls out of three, so the retries have something to do.
 */
@RestController
@RequestMapping("/fake/speakers")
class FakeSpeakerApi {

	private final AtomicInteger calls = new AtomicInteger();

	@GetMapping("/{name}")
	ResponseEntity<SpeakerProfile> get(@PathVariable String name) {
		if (calls.incrementAndGet() % 3 != 0) {
			return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
		}
		return ResponseEntity.ok(new SpeakerProfile(name, "Broadcom", name + " speaks about Spring."));
	}

}
