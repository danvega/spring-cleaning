package dev.danvega.talks.talk;

import org.springframework.stereotype.Component;
import tools.jackson.databind.cfg.DateTimeFeature;
import tools.jackson.databind.json.JsonMapper;

import java.util.List;

/**
 * Writes the schedule feed that the conference mobile app reads.
 * The feed uses its own mapper, so the app's JSON settings do not change it.
 */
@Component
public class ScheduleExporter {

	// Jackson 3 writes dates as ISO-8601 strings by default. The mobile app still expects arrays.
	private final JsonMapper mapper = JsonMapper.builder()
			.enable(DateTimeFeature.WRITE_DATES_AS_TIMESTAMPS)
			.build();

	public String export(List<Talk> talks) {
		return mapper.writeValueAsString(talks);
	}

}
