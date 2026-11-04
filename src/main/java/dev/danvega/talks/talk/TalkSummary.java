package dev.danvega.talks.talk;

// Version 2.0 of the talks list: no seat counts, and a room is always set
public record TalkSummary(String title, String speaker, String room) {

	static TalkSummary of(Talk talk) {
		return new TalkSummary(talk.title(), talk.speaker(), talk.room() != null ? talk.room() : "TBA");
	}

}
