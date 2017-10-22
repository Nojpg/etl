package pirate.tid.etl.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Objects;

@Component
@Slf4j
public class FlowDecision implements JobExecutionDecider{
    @Override
    public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
        String path = "/home/nojpg/IdeaProjects/etl/src/main/resources/input/";
        String inputCsv[] = new File(path).list((dir, name) -> name.endsWith(".csv"));

        if (Objects.requireNonNull(inputCsv).length == 0){
            return FlowExecutionStatus.FAILED;
        }


        return FlowExecutionStatus.COMPLETED;
    }
}
