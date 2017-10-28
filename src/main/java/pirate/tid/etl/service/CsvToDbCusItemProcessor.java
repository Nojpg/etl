package pirate.tid.etl.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import pirate.tid.etl.domain.Account;
import pirate.tid.etl.domain.AccountName;
import pirate.tid.etl.domain.CustomerName;
import pirate.tid.etl.domain.Dictionary;

import java.util.Random;

/**
 * Created by Nojpg on 30.09.17.
 */
@Slf4j
public class CsvToDbCusItemProcessor implements ItemProcessor<CustomerName, Account>{

    @Override
    public Account process(CustomerName customerName){
        Account transformedData = new Account();
        transformedData.setAccountName(customerName.getCustomerName());
        System.out.println(transformedData.getAccountName()); //TODO accName
        Double wrapper = (Double.parseDouble(customerName.getTrafficVolume().split(" ")[0]))/1024;
        transformedData.setTrafficVolume(String.valueOf(wrapper) + " Gb");
        transformedData.setAddress(customerName.getAddress());
        log.info("transformed customers: " + transformedData.toString());
        return transformedData;
    }
}
