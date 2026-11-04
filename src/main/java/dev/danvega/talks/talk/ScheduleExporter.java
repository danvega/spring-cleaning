package dev.danvega.talks.talk;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Writes the schedule feed that the conference mobile app reads.
 * The feed uses its own mapper, so the app's JSON settings do not change it.
 */
@Component
public class ScheduleExporter {

	private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

	public String export(List<Talk> talks) {
		try {
			return mapper.writeValueAsString(talks);
		}
		catch (JsonProcessingException e) {
			throw new IllegalStateException("Could not export the schedule", e);
		}
	}

}
