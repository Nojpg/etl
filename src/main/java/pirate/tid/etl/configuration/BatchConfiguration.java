package pirate.tid.etl.configuration;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import pirate.tid.etl.domain.Account;
import pirate.tid.etl.repository.AccountDataRepository;
import pirate.tid.etl.service.ETLItemProcessor;
import pirate.tid.etl.domain.AccountName;

import javax.sql.DataSource;

/**
 * Created by Nojpg on 28.09.17.
 */

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

//    @Autowired
    AccountDataRepository accountDataRepository;

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Qualifier("dataSource")
    @Autowired
    public DataSource dataSource;

    @Bean
    public FlatFileItemReader<AccountName> reader(){
        FlatFileItemReader<AccountName> reader = new FlatFileItemReader<AccountName>();
        reader.setResource(new ClassPathResource("AccountName.csv"));
        reader.setLineMapper(new DefaultLineMapper<AccountName>(){{
            setLineTokenizer(new DelimitedLineTokenizer(){{
                setNames(new String[]{"accountName", "trafficVolume", "city", "street", "house"});
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<AccountName>() {{
                setTargetType(AccountName.class);
            }});
                       }});
        return reader;
    }

    @Bean
    public ETLItemProcessor processor(){
        return new ETLItemProcessor();
    }

    @Bean
    public JdbcBatchItemWriter<Account> writer(){
        JdbcBatchItemWriter<Account> writer = new JdbcBatchItemWriter<Account>();
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Account>());
        writer.setSql("INSERT INTO provider (account_name, traffic_volume, address) VALUES (:accountName, :trafficVolume, :address)");
        writer.setDataSource(dataSource);
        return writer;
    }

    @Bean
    public Job accountJob(JobCompletionNotificationListener listener){
        return jobBuilderFactory.get("accountJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1())
                .end()
                .build();
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .<AccountName, Account> chunk(10)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }
}
