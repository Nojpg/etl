package pirate.tid.etl.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pirate.tid.etl.domain.Account;
import pirate.tid.etl.domain.AccountName;

/**
 * Created by Nojpg on 28.09.17.
 */
@Repository
public interface AccountDataRepository extends CrudRepository<Account, Long> {
}
