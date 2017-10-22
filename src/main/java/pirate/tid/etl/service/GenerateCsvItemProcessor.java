package pirate.tid.etl.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import pirate.tid.etl.domain.Account;
import pirate.tid.etl.domain.AccountName;
import pirate.tid.etl.domain.Dictionary;

import java.util.Random;

/**
 * Created by Nojpg on 30.09.17.
 */
@Slf4j
public class GenerateCsvItemProcessor implements ItemProcessor<String, AccountName>{
    @Override
    public AccountName process(String type) throws Exception {
        AccountName transformedData = new AccountName();
        int random = new Random().nextInt(10);
        Dictionary dictionary = new Dictionary(" kb"); //add type
        transformedData.setAccountName(dictionary.nameList.get(random));
        transformedData.setCity(dictionary.cityList.get(random));
        transformedData.setStreet(dictionary.streetList.get(random));
        transformedData.setTrafficVolume(String.valueOf(Integer.parseInt(String.valueOf(dictionary.trafficVolumeMb.get(random)))));
        transformedData.setHouse(String.valueOf(dictionary.houseList.get(random)));
        //TODO add 10 customers
        log.info("Created: " + transformedData.toString()); //TODO add AccountNameFile and customer files
        return transformedData;
    }
}
