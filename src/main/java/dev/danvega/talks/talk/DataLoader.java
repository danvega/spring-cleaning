package dev.danvega.talks.talk;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

	private final TalkRepository talks;

	public DataLoader(TalkRepository talks) {
		this.talks = talks;
	}

	@Override
	public void run(String... args) {
		talks.saveAll(List.of(
				new Talk(null, "Spring Boot 4 Migration", "Dan Vega", LocalDateTime.of(2026, 10, 1, 9, 30), "Ballroom A", 300),
				new Talk(null, "JSON with Jackson 3", "Maya Chen", LocalDateTime.of(2026, 10, 1, 11, 0), "Room 204", 120),
				new Talk(null, "Null Safety with JSpecify", "Sam Rivera", LocalDateTime.of(2026, 10, 2, 14, 15), null, 80)
		));
	}

}
