package pirate.tid.etl.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pirate.tid.etl.domain.Account;
import pirate.tid.etl.repository.AccountDataRepository;

/**
 * Created by Nojpg on 28.09.17.
 */

@Component
@Slf4j
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {

    @Autowired
    private AccountDataRepository accountDataRepository;

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("Job finished");
            accountDataRepository.findAll().forEach(account -> log.info("Found " + account + "in the database."));
        }
    }

//    @Override
//    public void beforeJob(JobExecution jobExecution) {
//        if (jobExecution.getStatus() == BatchStatus.COMPLETED){
//            System.out.println("Зафейлились");
//        }
//    }
}
