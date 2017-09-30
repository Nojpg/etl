package pirate.tid.etl.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import pirate.tid.etl.domain.Account;
import pirate.tid.etl.domain.AccountName;

/**
 * Created by Nojpg on 30.09.17.
 */
@Slf4j
public class DbToCsvItemProcessor implements ItemProcessor<Account, AccountName>{
    @Override
    public AccountName process(Account account) throws Exception {
        AccountName transformedData = new AccountName();
        transformedData.setAccountName(account.getAccountName());
        double wrapper = (Double.parseDouble(account.getTrafficVolume())*1024)*1024;
        transformedData.setTrafficVolume(String.valueOf(wrapper) + " Kb");
        String[] strings = account.getAddress().split(", ");
        transformedData.setCity(strings[0]);
        transformedData.setStreet(strings[1]);
        transformedData.setHouse(strings[3]);
        log.info("transformed: " + transformedData.toString());
        return transformedData;
    }
}
