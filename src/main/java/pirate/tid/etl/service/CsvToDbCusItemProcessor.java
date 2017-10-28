package pirate.tid.etl.service;

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
        String path = "/home/sovereign/IdeaProjects/etl/src/main/resources/input/";
        List<String> arrayList = new ArrayList<>();
//        names = Arrays.stream(new File(path).list((dir, name) -> {
//            if (!(name.equals("AccountName.csv") && name.endsWith(".csv"))) {
//                return true;
//            }
//            return false;
//        })).collect(toList());


        List<String> names = Arrays.stream(Objects.requireNonNull(new File(path).list((
                (dir, name) -> !name.equals("AccountName.csv")
        )))).map(name -> name.split("\\.")[0])
                .collect(toList());  //TODO add value to pojo+dto
//        System.out.println(Arrays.toString(names[count].split(".")));
//        System.out.println(names[count]);
        System.out.println(names);

//        Stream.of(new File(path).list())
//                .filter(name -> !(name.equals("AccountName.csv")) && name.endsWith(".csv"))
//                .map(name -> name[0].split(".")[0])
//                .forEach(name -> name[0].split(".")[0])
//                .collect(toList());
//        System.out.println(names.get(1));
//        arrayList = Stream.of(names).filter(name -> {
//            !(name == ("AccountName.csv"));
////            return name;
//        }).collect(toList());
//        System.out.println(names);
//        customerName.setCustomerName(names[count]);
//        System.out.println(customerName.getCustomerName());
        count++;
        Account transformedData = new Account();
        transformedData.setAccountName(customerName.getCustomerName());
//        System.out.println(transformedData.getAccountName()); //TODO accName
        Double wrapper = (Double.parseDouble(customerName.getTrafficVolume().split(" ")[0]))/1024;
        transformedData.setTrafficVolume(String.valueOf(wrapper) + " Gb");
        transformedData.setAddress(customerName.getAddress());
        log.info("transformed customers: " + transformedData.toString());
        return transformedData;
    }
}
