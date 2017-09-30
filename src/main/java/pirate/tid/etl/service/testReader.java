package pirate.tid.etl.service;

import org.springframework.batch.core.ItemReadListener;
import org.springframework.batch.item.data.RepositoryItemReader;
import pirate.tid.etl.domain.Account;

/**
 * Created by Nojpg on 30.09.17.
 */
public class testReader<Account> extends RepositoryItemReader<Account> implements ItemReadListener {
    @Override
    public void beforeRead() {
        System.out.println("foo");
    }

    @Override
    public void afterRead(Object o) {

    }

    @Override
    public void onReadError(Exception e) {

    }

}
