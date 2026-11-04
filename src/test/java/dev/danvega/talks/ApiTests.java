package dev.danvega.talks;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient;

// End to end, over real HTTP. WebTestClient is why this MVC app has WebFlux on the test classpath.
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT,
		properties = { "server.port=8089", "spring.datasource.generate-unique-name=true" })
// Boot 4 no longer registers a WebTestClient for @SpringBootTest on its own
@AutoConfigureWebTestClient
class ApiTests {

	@Autowired
	WebTestClient client;

	@Test
	void speakerProfileSurvivesAFlakyApi() {
		client.get().uri("/api/talks/1/speaker")
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$.name").isEqualTo("Dan Vega");
	}

	@Test
	void versionTwoReturnsSummaries() {
		client.get().uri("/api/talks")
				.header("X-API-Version", "2.0")
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$[2].room").isEqualTo("TBA")
				.jsonPath("$[0].seats").doesNotExist();
	}

	@Test
	void unknownVersionIsRejected() {
		client.get().uri("/api/talks")
				.header("X-API-Version", "3.0")
				.exchange()
				.expectStatus().isBadRequest();
	}

}
