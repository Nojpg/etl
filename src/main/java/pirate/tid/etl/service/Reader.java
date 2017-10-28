package pirate.tid.etl.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MappingIterator;
import lombok.NonNull;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.stereotype.Component;
import pirate.tid.etl.domain.AccountName;
import pirate.tid.etl.domain.CustomerName;
import pirate.tid.etl.domain.Dictionary;
import pirate.tid.etl.domain.DomainSerializable;

import java.io.*;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Component
public class Reader implements StepExecutionListener{
    private List<AccountName> accountNames = new ArrayList<>();
    private static final String COMMA_DELIMITER = ",";
    private List<String> fileList = new ArrayList<>();
    private static final String OUTPUT_ZIP_FILE = "/home/nojpg/IdeaProjects/etl/src/main/resources/output/zipped.zip";
    private static final String SOURCE_FOLDER = "/home/nojpg/IdeaProjects/etl/src/main/resources/input/";
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
        writeToCsv(accountNames, "/home/nojpg/IdeaProjects/etl/src/main/resources/input/AccountName.csv");

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
            writeToCsv(customerNames, "/home/nojpg/IdeaProjects/etl/src/main/resources/input/" + customerNames.get(i).getCustomerName() + ".csv");
        }

    }

    private void writeToCsv(List<? extends DomainSerializable> list, String fileName){
        FileWriter fileWriter = null;

        try {
            if (list.get(0).getClass().equals(AccountName.class)){
                try {
                    fileWriter = new FileWriter(fileName);
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
            } else{
                for (DomainSerializable aList : list) {
                    fileWriter = new FileWriter(fileName);
                    FileWriter finalFileWriter = fileWriter;
                    try {
                        finalFileWriter.append(aList.getTrafficVolume());
                        finalFileWriter.append(COMMA_DELIMITER);
                        finalFileWriter.append(aList.getDate());
                        finalFileWriter.append(COMMA_DELIMITER);
                        finalFileWriter.append(aList.getAddress());
                        finalFileWriter.append(System.lineSeparator());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

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


    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
//        generateFileList(new File(SOURCE_FOLDER));
//        zipIt(OUTPUT_ZIP_FILE);
        return null;
    }

    public void zipIt(String zipFile){

        byte[] buffer = new byte[1024];

        try{
            FileOutputStream fos = new FileOutputStream(zipFile);
            ZipOutputStream zos = new ZipOutputStream(fos);

            System.out.println("Output to Zip : " + zipFile);

            for(String file : this.fileList){

                System.out.println("File Added : " + file);
                ZipEntry ze= new ZipEntry(file);
                zos.putNextEntry(ze);

                FileInputStream in =
                        new FileInputStream(SOURCE_FOLDER + File.separator + file);

                int len;
                while ((len = in.read(buffer)) > 0) {
                    zos.write(buffer, 0, len);
                }

                in.close();
            }

            zos.closeEntry();
            zos.close();

            System.out.println("Done");
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }

    private void generateFileList(File node){


        String path = "/home/nojpg/IdeaProjects/etl/src/main/resources/input/";
        String inputCsv[] = new File(path).list((dir, name) -> name.endsWith(".csv"));
        fileList.addAll(Arrays.asList(Objects.requireNonNull(inputCsv)));


        //add file only
//        if(node.isFile()){
//            fileList.add(generateZipEntry(node.getAbsoluteFile().toString()));
//            System.out.println(fileList);
//        }

//        if(node.isDirectory()){
//            String[] subNote = node.list();
//            for(String filename : subNote){
//                generateFileList(new File(node, filename));
//            }
//        }

    }

    private String generateZipEntry(String file){
        return file.substring(SOURCE_FOLDER.length()+1, file.length());
    }
}
