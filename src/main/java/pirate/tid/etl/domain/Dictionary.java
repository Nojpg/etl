package pirate.tid.etl.domain;

import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotNull;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
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

    public List<String> dateList = new ArrayList<>();

    public Dictionary(String type){
        this.generate(type);


    }

    private void generate(@NonNull @NotNull String type){
        String pattern;
        if (!type.equals(" kb")) {
            type = " mb";
            pattern = "MM-dd-uuuu";
        } else{
            pattern = "dd-MM-uuuu";
        }
        String finalType = type;
        trafficVolumeMb = trafficVolumeMb.stream().map(volume -> {
            if (finalType.equals(" kb")){
                return String.valueOf(Integer.parseInt(String.valueOf(volume))*1024) + finalType;
            } else return String.valueOf(volume) + finalType;
        }).collect(Collectors.toList());

        LocalDate startDate = LocalDate.of(1990, 1, 1); //start date
        long start = startDate.toEpochDay();

        LocalDate endDate = LocalDate.now(); //end date
        long end = endDate.toEpochDay();

        for (int i = 0; i < 10; i++) {
            long randomEpochDay = ThreadLocalRandom.current().longs(start, end).findAny().getAsLong();
            String date = LocalDate.ofEpochDay(randomEpochDay).format(DateTimeFormatter.ofPattern(pattern));
            dateList.add(date);
        }
    }
}
