package dev.danvega.talks.talk;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// Reached through VersionFilter when a request sends X-API-Version: 2.0
@RestController
@RequestMapping("/api/v2/talks")
public class TalkV2Controller {

	private final TalkRepository talks;

	public TalkV2Controller(TalkRepository talks) {
		this.talks = talks;
	}

	@GetMapping
	public List<TalkSummary> findAll() {
		return talks.findAll().stream().map(TalkSummary::of).toList();
	}

}
