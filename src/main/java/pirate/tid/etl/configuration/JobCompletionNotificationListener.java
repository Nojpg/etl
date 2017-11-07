package pirate.tid.etl.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pirate.tid.etl.domain.Account;
import pirate.tid.etl.repository.AccountDataRepository;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by Nojpg on 28.09.17.
 */

@Component
@Slf4j
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {
    private static final String OUTPUT_ZIP_FILE = "/home/sovereign/Public/etl/src/main/resources/output/zipped.zip";
    private static final String SOURCE_FOLDER = "/home/sovereign/Public/etl/src/main/resources/input/";
    private List<String> fileList = new ArrayList<>();


    @Autowired
    private AccountDataRepository accountDataRepository;

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("Job finished");
            accountDataRepository.findAll().forEach(account -> log.info("Found " + account + "in the database."));
            generateFileList(new File(SOURCE_FOLDER));
            zipIt(OUTPUT_ZIP_FILE);
        }
    }


    private void zipIt(String zipFile){

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


        String path = "/home/sovereign/Public/etl/src/main/resources/input";
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
//    @Override
//    public void beforeJob(JobExecution jobExecution) {
//        if (jobExecution.getStatus() == BatchStatus.COMPLETED){
//            System.out.println("Зафейлились");
//        }
//    }
}
