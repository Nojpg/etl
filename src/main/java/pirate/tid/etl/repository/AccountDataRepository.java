package pirate.tid.etl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pirate.tid.etl.domain.Account;

/**
 * Created by Nojpg on 28.09.17.
 */

public interface AccountDataRepository extends JpaRepository<Account, Long> {
    Account findByAccountName (String accountName);
}
