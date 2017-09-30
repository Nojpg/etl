package pirate.tid.etl.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import pirate.tid.etl.domain.Account;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;
/**
 * Created by Nojpg on 30.09.17.
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@SpringBootTest
public class AccountDataRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AccountDataRepository accountDataRepository;

    @Test
    public void testAccountDataRepositorySave(){
        Account account = new Account(
                "account_name_for_test",
                "traffic_volume_for_test",
                "address_for_test");
        accountDataRepository.save(account);
        assertThat(accountDataRepository.findByAccountName("account_name_for_test").getAccountName()).isEqualTo("account_name_for_test");
        assertNotNull(accountDataRepository.findByAccountName("account_name_for_test"));
    }

    @Test
    public void testAccountDataRepositoryFindOne() throws Exception{
        Account entityForTest = this.entityManager.persist(new Account(
                "account_name_for_test",
                "traffic_volume_for_test",
                "address_for_test"));
        Account account = this.accountDataRepository.findByAccountName("account_name_for_test");
        assertNotNull(accountDataRepository.findByAccountName("account_name_for_test"));
        assertThat(account.getAccountName()).isEqualTo(entityForTest.getAccountName());
    }
}