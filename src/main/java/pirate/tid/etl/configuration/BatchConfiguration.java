package pirate.tid.etl.configuration;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;

import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;
import pirate.tid.etl.domain.Account;
import pirate.tid.etl.domain.AccountName;
import pirate.tid.etl.repository.AccountDataRepository;
import pirate.tid.etl.service.CsvToDbItemProcessor;
import pirate.tid.etl.service.GenerateCsvItemProcessor;


import javax.sql.DataSource;


/**
 * Created by Nojpg on 28.09.17.
 */

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    //TODO Construtor
    //TODO check before read && before write classes ext repo imp item
    //TODO generate csv
    //TODO dic?

    @Autowired
    public JobBuilderFactory jobBuilderFactory;
    @Autowired
    public StepBuilderFactory stepBuilderFactory;
    @Qualifier("dataSource")
    @Autowired
    public DataSource dataSource;
    @Autowired
    AccountDataRepository accountDataRepository;

    @Bean
    public FlatFileItemReader<AccountName> csvToDbReader() {
        FlatFileItemReader<AccountName> reader = new FlatFileItemReader<>();
        reader.setResource(new ClassPathResource("AccountName.csv"));
        reader.setLineMapper(new DefaultLineMapper<AccountName>() {{
            setLineTokenizer(new DelimitedLineTokenizer() {{
                setNames(new String[]{"accountName", "trafficVolume", "city", "street", "house"});
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<AccountName>() {{
                setTargetType(AccountName.class);
            }});
        }});
        return reader;
    }

    @Bean
    public CsvToDbItemProcessor csvToDbProcessor() {
        return new CsvToDbItemProcessor();
    }

    @Bean
    public RepositoryItemWriter<Account> csvToDbWriter(){
        RepositoryItemWriter<Account> writer = new RepositoryItemWriter<>();
        writer.setRepository(accountDataRepository);
        writer.setMethodName("save");
        return writer;
    }

    @Bean
    public Job accountJob(JobCompletionNotificationListener listener) {
        return jobBuilderFactory.get("accountJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(csvToDbStep1())
                .end()
                .build();
    }

    @Bean
    public Step csvToDbStep1() {
        return stepBuilderFactory.get("step1")
                .<AccountName, Account>chunk(10)
                .reader(csvToDbReader())
                .processor(csvToDbProcessor())
                .writer(csvToDbWriter())
                .build();
    }

//    @Bean //TODO how to do reader
//    public ItemReader<AccountName> csvGeneratorReader() {
//
//        }
//
//        RepositoryItemReader<Account> reader = new RepositoryItemReader<>();
//        reader.setRepository(accountDataRepository);
//        reader.setMethodName("findAll");
//        Map<String, Sort.Direction> sort = new HashMap<>();
//        sort.put("id", Sort.Direction.ASC);
//        reader.setSort(sort);
//        return reader;
//    }
//
    @Bean
    public GenerateCsvItemProcessor javaToCsvItemProcessor() {
        return new GenerateCsvItemProcessor();
    }

    @Bean
    public FlatFileItemWriter<AccountName> javaToCsvWriter(){ //TODO add folder and .csv?
        FlatFileItemWriter<AccountName> writer = new FlatFileItemWriter<>();
        writer.setResource(new FileSystemResource("input/") {
        });
        try {
            writer.afterPropertiesSet(

            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        writer.setLineAggregator(new );
        return writer;
    }

//    @Bean
//    public Job accountJob(JobCompletionNotificationListener listener) {
//        return jobBuilderFactory.get("accountJob")
//                .incrementer(new RunIdIncrementer())
//                .listener(listener)
//                .flow(DbToCsvStep1())
//                .end()
//                .build();
//    }
//
    @Bean
    public Step csvGeneratorStep() {
        return stepBuilderFactory.get("step1").
                <Account, AccountName>chunk(10)
                .reader(DbToCsvReader())
                .processor(javaToCsvItemProcessor())
                .writer(javaToCsvWriter())
                .build()
                .execute();
    }
}
