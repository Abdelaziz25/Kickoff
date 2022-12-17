package back.kickoff.kickoffback.repositories;

import back.kickoff.kickoffback.model.Player;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component
@Repository
public interface PlayerRepositry extends CrudRepository<Player, Long> {
}
