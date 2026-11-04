package dev.danvega.talks.speaker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.resilience.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

@Service
public class SpeakerService {

	private static final Logger log = LoggerFactory.getLogger(SpeakerService.class);

	private final SpeakerClient speakers;

	public SpeakerService(SpeakerClient speakers) {
		this.speakers = speakers;
	}

	// maxRetries counts only the retries: 1 call + 3 retries, the same as maxAttempts = 4 before
	@Retryable(maxRetries = 3, includes = HttpServerErrorException.class, delay = 200, multiplier = 2)
	public SpeakerProfile profile(String name) {
		log.info("Calling the speaker API for {}", name);
		return speakers.get(name);
	}

}
