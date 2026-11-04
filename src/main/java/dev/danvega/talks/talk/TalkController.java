package dev.danvega.talks.talk;

import dev.danvega.talks.speaker.SpeakerProfile;
import dev.danvega.talks.speaker.SpeakerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/talks")
public class TalkController {

	private final TalkRepository talks;
	private final ScheduleExporter exporter;
	private final SpeakerService speakers;

	public TalkController(TalkRepository talks, ScheduleExporter exporter, SpeakerService speakers) {
		this.talks = talks;
		this.exporter = exporter;
		this.speakers = speakers;
	}

	@GetMapping
	public List<Talk> findAll() {
		return talks.findAll();
	}

	@GetMapping("/{id}")
	public ResponseEntity<Talk> findById(@PathVariable Long id) {
		return ResponseEntity.of(talks.findById(id));
	}

	@GetMapping("/{id}/speaker")
	public ResponseEntity<SpeakerProfile> speaker(@PathVariable Long id) {
		return ResponseEntity.of(talks.findById(id).map(talk -> speakers.profile(talk.speaker())));
	}

	@GetMapping("/search")
	public ResponseEntity<Talk> findByTitle(@RequestParam String title) {
		Talk talk = talks.findByTitleIgnoreCase(title);
		if (talk == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(talk);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Talk create(@RequestBody TalkRequest request) {
		return talks.save(new Talk(null, request.title(), request.speaker(), request.startsAt(), request.room(), request.seats()));
	}

	@GetMapping(value = "/export", produces = MediaType.APPLICATION_JSON_VALUE)
	public String export() {
		return exporter.export(talks.findAll());
	}

}
