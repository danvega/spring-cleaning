package dev.danvega.talks.talk;

import org.jspecify.annotations.Nullable;
import org.springframework.data.repository.ListCrudRepository;

public interface TalkRepository extends ListCrudRepository<Talk, Long> {

	@Nullable Talk findByTitleIgnoreCase(String title);

}
