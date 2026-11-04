package dev.danvega.talks.speaker;

import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Service
public class SpeakerService {

	private static final Logger log = LoggerFactory.getLogger(SpeakerService.class);

	private final SpeakerClient speakers;

	public SpeakerService(SpeakerClient speakers) {
		this.speakers = speakers;
	}

	// maxAttempts counts the first call too: 1 call + 3 retries
	@Retryable(retryFor = FeignException.class, maxAttempts = 4, backoff = @Backoff(delay = 200, multiplier = 2))
	public SpeakerProfile profile(String name) {
		log.info("Calling the speaker API for {}", name);
		return speakers.get(name);
	}

}
