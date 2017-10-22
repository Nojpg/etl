package pirate.tid.etl.domain;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Nojpg on 30.09.17.
 */
@Slf4j
public class Dictionary {
    private String[] cities = {"Bath", "Bangor", "Brislot", "Belfast", "Bradford",
            "Carlisle", "Exeter", "Lancaster", "Norwich", "Perth"};
    public List<String> cityList = Arrays.asList(cities);

    private String[] streets = {"Kingsway", "Canal", "Mosley", "Portland", "Sackville",
            "Oxford", "Princess", "Quay", "Spring", "Wilmslow"};
    public List<String> streetList = Arrays.asList(streets);

    public List<Integer> houseList = Collections.nCopies(10,
            new Random().nextInt(100));

    private String[] names = {"James", "Alex", "Ben", "Tom", "Ryan",
            "Liam", "Harry", "David", "Joe", "Lewis"};
    public List<String> nameList = Arrays.asList(names);

    public List<?> trafficVolumeMb = Collections.nCopies(10,
            (new Random().nextInt(1000000)));

    public List<String> dateList;

    public Dictionary(String type) throws ParseException {
        this.generate(type);
    }

    private void generate(String type){
        String pattern;
        if (type.equals(" kb")){
            pattern = "mm-DD-yyyy";
        } else  {
            pattern = "DD-mm-yyyy";
            type = " mb";
        }
        String finalType = type;
        trafficVolumeMb = trafficVolumeMb.stream().map(volume -> {
            if (finalType.equals(" kb")){
                return String.valueOf(Integer.parseInt(String.valueOf(volume))*1024) + finalType;
            } else return String.valueOf(volume) + finalType;
        }).collect(Collectors.toList());

        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        try {
            Date dateFrom;
            dateFrom = dateFormat.parse("2000");
            long timestampFrom = dateFrom.getTime();
            Date dateTo;
            dateTo = dateFormat.parse("2018");
            long timestampTo = dateTo.getTime();
            long timeRange = timestampTo - timestampFrom;
            long randomTimestamp = timestampFrom + (long)(new Random().nextDouble() * timeRange);
            dateList = Collections.nCopies(10,
                    String.valueOf(dateFormat.format(new Date(randomTimestamp))));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
