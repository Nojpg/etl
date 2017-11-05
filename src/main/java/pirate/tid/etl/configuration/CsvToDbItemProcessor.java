package pirate.tid.etl.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import pirate.tid.etl.domain.Account;
import pirate.tid.etl.domain.AccountName;

/**
 * Created by Nojpg on 28.09.17.
 */

@Slf4j
public class CsvToDbItemProcessor implements ItemProcessor<AccountName, Account> {

    @Override
    public Account process(AccountName accountName) {
        Account transformedData = new Account();
        transformedData.setAccountName(accountName.getAccountName());
        Double wrapper = (Double.parseDouble(accountName.getTrafficVolume().split("")[0]) / 1024) / 1024;
        transformedData.setTrafficVolume(String.valueOf(wrapper) + " Gb");
        transformedData.setAddress(accountName.getCity() + ", " + accountName.getStreet() + ", " + accountName.getHouse());
        log.info("transformed accounts: " + transformedData.toString());
        return transformedData;
    }
}
