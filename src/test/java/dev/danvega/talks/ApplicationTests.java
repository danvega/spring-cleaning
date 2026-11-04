package dev.danvega.talks;

import dev.danvega.talks.talk.Talk;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ApplicationTests {

	@Autowired
	MockMvc mvc;

	@Test
	void searchFindsTalkByTitle() throws Exception {
		mvc.perform(get("/api/talks/search").param("title", "json with jackson 3"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.speaker").value("Maya Chen"));
	}

	@Test
	void searchReturnsNotFoundForUnknownTitle() throws Exception {
		mvc.perform(get("/api/talks/search").param("title", "No Such Talk"))
				.andExpect(status().isNotFound());
	}

	// The mobile app reads dates from the schedule feed as arrays: [year, month, day, hour, minute]
	@Test
	void exportWritesDatesAsArrays() throws Exception {
		mvc.perform(get("/api/talks/export"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].startsAt[0]").value(2026))
				.andExpect(jsonPath("$[0].startsAt[1]").value(10))
				.andExpect(jsonPath("$[0].startsAt[2]").value(1));
	}

}
