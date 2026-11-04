package dev.danvega.talks.talk;

import dev.danvega.talks.speaker.SpeakerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TalkController.class)
class TalkControllerTest {

	@Autowired
	MockMvc mvc;

	@MockitoBean
	TalkRepository talks;

	@MockitoBean
	ScheduleExporter exporter;

	@MockitoBean
	SpeakerService speakers;

	@Test
	void findAllReturnsTalksWithIsoDates() throws Exception {
		when(talks.findAll()).thenReturn(List.of(
				new Talk(1L, "Spring Boot 4 Migration", "Dan Vega", LocalDateTime.of(2026, 10, 1, 9, 30), "Ballroom A", 300)));

		mvc.perform(get("/api/talks"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].title").value("Spring Boot 4 Migration"))
				.andExpect(jsonPath("$[0].startsAt").value("2026-10-01T09:30:00"));
	}

	@Test
	void findByIdReturnsNotFoundForUnknownTalk() throws Exception {
		when(talks.findById(99L)).thenReturn(Optional.empty());

		mvc.perform(get("/api/talks/99"))
				.andExpect(status().isNotFound());
	}

}
