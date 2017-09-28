package pirate.tid.etl.configuration;

import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import org.slf4j.Logger;
import pirate.tid.etl.domain.Account;
import pirate.tid.etl.repository.AccountDataRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nojpg on 28.09.17.
 */

@Component
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {


    private static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener.class);

    private final JdbcTemplate jdbcTemplate;

    private AccountDataRepository accountDataRepository;

    @Autowired
    public JobCompletionNotificationListener(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("Job finished");

            List<Account> results = jdbcTemplate.query("SELECT account_name, traffic_volume, address FROM provider", new RowMapper<Account>() {
                @Override
                public Account mapRow(ResultSet resultSet, int i) throws SQLException {
                    return new Account(resultSet.getString(1), resultSet.getString("traffic_volume"), resultSet.getString("address"));
                }
            });

            for (Account account: results){
                log.info("Found <" + account + "in the database.");
            };
        }
    }
}
