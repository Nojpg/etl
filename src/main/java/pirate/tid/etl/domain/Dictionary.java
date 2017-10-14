package pirate.tid.etl.domain;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Nojpg on 30.09.17.
 */
@Slf4j
@Getter
public class Dictionary {
    List<String> cityList = List.of(
            "Bath", "Bangor", "Brislot", "Belfast", "Bradford",
            "Carlisle", "Exeter", "Lancaster", "Norwich", "Perth");
    List<String> streetList = List.of(
            "Kingsway", "Canal", "Mosley", "Portland", "Sackville",
            "Oxford", "Princess", "Quay", "Spring", "Wilmslow");
    List<String> houseList = List.of(
            "88", "33/2", "44", "321", "56",
            "98", "78", "345", "123", "234/3");
    List<String> nameList = List.of(
            "James", "Alex", "Ben", "Tom", "Ryan",
            "Liam", "Harry", "David", "Joe", "Lewis");

    List<String> trafficVolumeMb = Collections.nCopies(10,
            String.valueOf(new Random().nextInt(1000000)));


    List<String> dateList;

    public Dictionary(String pattern) throws ParseException {
        this.generateTime(pattern);
    }

//    public Dictionary(String pattern, String type) throws ParseException {
//        this.generateTime(pattern);
//        if (type == "accountName"){
//            AccountName accountName = new AccountName();
//        }
//    }

    private List<String> generateTime(String pattern) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);

        Date dateFrom = dateFormat.parse("2000");
        long timestampFrom = dateFrom.getTime();
        Date dateTo = dateFormat.parse("2018");
        long timestampTo = dateTo.getTime();
        long timeRange = timestampTo - timestampFrom;
        long randomTimestamp = timestampFrom + (long)(new Random().nextDouble() * timeRange);
        dateList = Collections.nCopies(10,
                String.valueOf(dateFormat.format(new Date(randomTimestamp))));
        return dateList;
    }

}
