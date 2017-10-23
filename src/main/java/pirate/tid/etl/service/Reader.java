package pirate.tid.etl.service;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.stereotype.Component;
import pirate.tid.etl.domain.AccountName;
import pirate.tid.etl.domain.Dictionary;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
@Component
public class Reader implements StepExecutionListener{


    //TODO написать генератор в бефор

    @Override
    public void beforeStep(StepExecution stepExecution) {
        for (int i = 0; i < 10; i++) {
            System.out.println("before step ");
            List<AccountName> list = new ArrayList<>();
            try {
                list.add(new Dictionary(" kb"));
            } catch (ParseException e) {
                e.printStackTrace();
            }


        }


//        public AccountName process(String type) throws Exception {
//            AccountName transformedData = new AccountName();
//            int random = new Random().nextInt(10);
//            Dictionary dictionary = new Dictionary(" kb"); //add type
//            transformedData.setAccountName(dictionary.nameList.get(random));
//            transformedData.setCity(dictionary.cityList.get(random));
//            transformedData.setStreet(dictionary.streetList.get(random));
//            transformedData.setTrafficVolume(String.valueOf(Integer.parseInt(String.valueOf(dictionary.trafficVolumeMb.get(random)))));
//            transformedData.setHouse(String.valueOf(dictionary.houseList.get(random)));
//            //TODO add 10 customers
//            log.info("Created: " + transformedData.toString()); //TODO add AccountNameFile and customer files
//            return transformedData;
//        }
    }


    //TODO написать зип в афтер
    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return null;
    }
}
