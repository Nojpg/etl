package pirate.tid.etl.configuration;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.stereotype.Component;
import pirate.tid.etl.domain.AccountName;
import pirate.tid.etl.domain.CustomerName;
import pirate.tid.etl.domain.Dictionary;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Component
public class StepListener implements StepExecutionListener{
    private List<AccountName> accountNames = new ArrayList<>();
    private static final String COMMA_DELIMITER = ",";

    private List<CustomerName> customerNames = new ArrayList<>();


    @Override
    public void beforeStep(StepExecution stepExecution) {
        for (int i = 0; i < 10; i++) {
            AccountName accountName = new AccountName();
            Dictionary dictionary = new Dictionary(" kb");
            accountName.setHouse(String.valueOf(dictionary.houseList.get(i)));
            accountName.setTrafficVolume(String.valueOf(dictionary.trafficVolumeMb.get(i)));
            accountName.setStreet(dictionary.streetList.get(i));
            accountName.setCity(dictionary.cityList.get(i));
            accountName.setAccountName(dictionary.nameList.get(i));
            accountName.setDate(dictionary.dateList.get(i));
            accountNames.add(accountName);
        }
        try {
            writeToCsvAccount(accountNames, "/home/sovereign/Public/etl/src/main/resources/input/AccountName.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < 10; i++) {
            CustomerName customerName = new CustomerName();
            Dictionary dictionary = new Dictionary(" mb");
            customerName.setCustomerName(dictionary.nameList.get(i));
            customerName.setTrafficVolume(String.valueOf(dictionary.trafficVolumeMb.get(i)));
            customerName.setDate(dictionary.dateList.get(i));
            customerName.setAddress(dictionary.cityList.get(i) + " " + dictionary.streetList.get(i) + " " + dictionary.houseList.get(i));
            customerNames.add(customerName);
        }

        for (int i = 0; i < customerNames.size(); i++) {
            try {
                writeToCsvCustomer(customerNames, i,"/home/sovereign/Public/etl/src/main/resources/input/" + customerNames.get(i).getCustomerName() + ".csv");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return null;
    }

    private void writeToCsvAccount(List<AccountName> list, String fileName) throws IOException{
        FileWriter fileWriter = null;

        try {
            fileWriter = new FileWriter(fileName);

            FileWriter finalFileWriter = fileWriter;
            list.forEach(data -> {
                try {
                    finalFileWriter.append(data.getAccountName());
                    finalFileWriter.append(COMMA_DELIMITER);
                    finalFileWriter.append(data.getTrafficVolume());
                    finalFileWriter.append(COMMA_DELIMITER);
                    finalFileWriter.append(data.getDate());
                    finalFileWriter.append(COMMA_DELIMITER);
                    finalFileWriter.append(data.getCity());
                    finalFileWriter.append(COMMA_DELIMITER);
                    finalFileWriter.append(data.getStreet());
                    finalFileWriter.append(COMMA_DELIMITER);
                    finalFileWriter.append(data.getHouse());
                    finalFileWriter.append(System.lineSeparator());
                } catch (IOException e) {
                    e.printStackTrace();
                }

            });
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        try {
            Objects.requireNonNull(fileWriter).flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        }
    }

    private void writeToCsvCustomer(List<CustomerName> list, int i,String fileName) throws IOException {


            FileWriter fileWriter = null;
            fileWriter = new FileWriter(fileName);
            FileWriter finalFileWriter = fileWriter;
            try {
                finalFileWriter.append(list.get(i).getTrafficVolume());
                finalFileWriter.append(COMMA_DELIMITER);
                finalFileWriter.append(list.get(i).getDate());
                finalFileWriter.append(COMMA_DELIMITER);
                finalFileWriter.append(list.get(i).getAddress());
                finalFileWriter.append(System.lineSeparator());
            } catch (IOException e) {
                e.printStackTrace();
            }
            Objects.requireNonNull(fileWriter).flush();
            fileWriter.close();
    }
}
