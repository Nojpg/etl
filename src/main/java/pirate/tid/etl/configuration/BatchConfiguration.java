package pirate.tid.etl.configuration;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import pirate.tid.etl.domain.Account;
import pirate.tid.etl.domain.AccountName;
import pirate.tid.etl.domain.CustomerName;
import pirate.tid.etl.repository.AccountDataRepository;
import pirate.tid.etl.service.CsvToDbCusItemProcessor;
import pirate.tid.etl.service.CsvToDbItemProcessor;
import pirate.tid.etl.service.Reader;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Created by Nojpg on 28.09.17.
 */

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;
    @Autowired
    public StepBuilderFactory stepBuilderFactory;
    @Qualifier("dataSource")
    @Autowired
    public DataSource dataSource;
    @Autowired
    AccountDataRepository accountDataRepository;
    @Autowired
    public Reader readerListener;

    @Autowired
    public FlowDecision flowDecision;

    @Bean
    public MultiResourceItemReader<CustomerName> resourceItemReader(){
        MultiResourceItemReader<CustomerName> reader = new MultiResourceItemReader<>();
//        reader.setResources(new Resource[]{("/home/nojpg/IdeaProjects/etl/src/main/resources/input/*.csv")});

        Resource[] resources = null;
        ResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();
        try {
            resources = patternResolver.getResources("/input/*.csv");
//            for (int j = 0; j < resources.length; j++) {
//                System.out.println(resources[j]);
//            }
            List<Resource> ew =Arrays.stream(resources).filter((Resource name) -> !(name.getFilename().equals("AccountName.csv"))).collect(Collectors.toList());

            resources = ew.toArray(new Resource[0]);
//            Arrays.stream(resources).forEach(name ->
//                    System.out.println(name.getFilename()));
//            Resource[] objects = (Resource[]) Arrays.stream(resources).filter(name ->
//                    !(name.getFilename() == "AccountName.csv")
//
//            ).toArray();
//            for (int i = 0; i < objects.length; i++) {
//                System.out.println(objects[i]);
//            }
//            String path = "/home/nojpg/IdeaProjects/etl/src/main/resources/input/";
//            String inputCsv[] = new File(path).list((dir, name) ->{
//                if(!(name.equals("AccountName.csv"))){
//                    name.endsWith(".csv");
//                    return true;
//                }
//                return false;
//            });
//            for (int i = 0; i < inputCsv.length; i++) {
//                System.out.println(inputCsv[i]);
//            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert resources != null;
        reader.setResources(resources);
        reader.setDelegate(customerNameFlatFileItemReader());
//        reader.setStrict(false);
        return reader;
    }

    @Bean
    public FlatFileItemReader<CustomerName> customerNameFlatFileItemReader() {
        FlatFileItemReader<CustomerName> reader = new FlatFileItemReader<>();
        reader.setLineMapper(new DefaultLineMapper<CustomerName>(){{
            setFieldSetMapper(new BeanWrapperFieldSetMapper<CustomerName>(){{
                setTargetType(CustomerName.class);
            }});
            setLineTokenizer(new DelimitedLineTokenizer(){{

                setNames(new String[]{"trafficVolume", "date", "address"});
            }});

        }});
        reader.setStrict(false);
        return reader;
    }


    @Bean
    public FlatFileItemReader<AccountName> csvToDbReader() {
        FlatFileItemReader<AccountName> reader = new FlatFileItemReader<>();
        reader.setResource(new ClassPathResource("input/AccountName.csv"));
        reader.setLineMapper(new DefaultLineMapper<AccountName>() {{
            setLineTokenizer(new DelimitedLineTokenizer() {{
                setNames(new String[]{"accountName", "trafficVolume", "date","city", "street", "house"});
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<AccountName>() {{
                setTargetType(AccountName.class);
            }});
        }});
        reader.setStrict(false);

        return reader;
    }

    @Bean
    public CsvToDbItemProcessor csvAccount() {
        return new CsvToDbItemProcessor();
    }

    @Bean
    public CsvToDbCusItemProcessor csvCustomer() {
        return new CsvToDbCusItemProcessor();
    }

    @Bean
    public RepositoryItemWriter<Account> csvToDbWriter(){
        RepositoryItemWriter<Account> writer = new RepositoryItemWriter<>();
        writer.setRepository(accountDataRepository);
        writer.setMethodName("save");
        return writer;
    }

    @Bean
    public Job accountJob(JobCompletionNotificationListener listener, @Qualifier("csvToDbStep1") Step csvToDbStep1, @Qualifier("csvToDpStep2") Step csvToDpStep2) {
        return jobBuilderFactory.get("accountJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .start(csvToDbStep1)
//                .start(csvToDpStep2)
                .next(csvToDpStep2)
                .build();
    }

    @Bean
    public Step csvToDbStep1() {
        return stepBuilderFactory.get("step1")
                .<AccountName, Account>chunk(10)
                .reader(csvToDbReader())
                .processor(csvAccount())
                .writer(csvToDbWriter())
                .listener(readerListener)
                .build();
    }

    @Bean
    public Step csvToDpStep2(){
        return stepBuilderFactory.get("step2")
                .<CustomerName, Account>chunk(10)
                .reader(resourceItemReader())
                .processor(csvCustomer())
                .writer(csvToDbWriter())
                .listener(readerListener)
                .build();
    }
}
