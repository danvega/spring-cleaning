package dev.danvega.talks.talk;

import org.springframework.data.annotation.Id;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;

// A talk has no id until it is saved, and no room until the schedule is final
public record Talk(@Id @Nullable Long id, String title, String speaker, LocalDateTime startsAt, @Nullable String room, int seats) {
}
