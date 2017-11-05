package pirate.tid.etl.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import pirate.tid.etl.domain.Account;
import pirate.tid.etl.domain.CustomerName;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

/**
 * Created by Nojpg on 30.09.17.
 */
@Slf4j
public class CsvToDbCusItemProcessor implements ItemProcessor<CustomerName, Account>{
    static int count = 0;


    @Override
    public Account process(CustomerName customerName){
        String path = "/home/nojpg/IdeaProjects/etl/src/main/resources/input/";
        List<String> names = Arrays.stream(Objects.requireNonNull(new File(path).list((
                (dir, name) -> !name.equals("AccountName.csv") && name.endsWith(".csv")
        )))).map(name -> name.split("\\.")[0])
                .collect(toList());
        customerName.setCustomerName(names.get(count));
        count++;
        Account transformedData = new Account();
        transformedData.setAccountName(customerName.getCustomerName());
        Double wrapper = (Double.parseDouble(customerName.getTrafficVolume().split(" ")[0]))/1024;
        transformedData.setTrafficVolume(String.valueOf(wrapper) + " Gb");
        transformedData.setAddress(customerName.getAddress());
        log.info("transformed customers: " + transformedData.toString());
        return transformedData;
    }
}
