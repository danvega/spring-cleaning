package dev.danvega.talks.talk;

import org.springframework.lang.Nullable;

import java.time.LocalDateTime;

public record TalkRequest(String title, String speaker, LocalDateTime startsAt, @Nullable String room, int seats) {
}
