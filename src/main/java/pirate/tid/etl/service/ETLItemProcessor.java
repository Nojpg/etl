package pirate.tid.etl.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import pirate.tid.etl.domain.Account;
import pirate.tid.etl.domain.AccountName;

/**
 * Created by Nojpg on 28.09.17.
 */

@Slf4j
public class ETLItemProcessor implements ItemProcessor<AccountName, Account> {

    @Override
    public Account process(AccountName accountName) throws Exception {
        Account transformedData = new Account();
        transformedData.setAccountName(accountName.getAccountName());
        int wrapper = (Integer.parseInt(accountName.getTrafficVolume())/1024)/1024;
        transformedData.setTrafficVolume(String.valueOf(wrapper));
        transformedData.setAddress(accountName.getCity() + accountName.getStreet() + accountName.getHouse());
        log.info("transformed: " + transformedData.toString());
        return transformedData;
    }
}
