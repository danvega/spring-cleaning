package dev.danvega.talks.talk;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.lang.Nullable;

public interface TalkRepository extends ListCrudRepository<Talk, Long> {

	@Nullable
	Talk findByTitleIgnoreCase(String title);

}
