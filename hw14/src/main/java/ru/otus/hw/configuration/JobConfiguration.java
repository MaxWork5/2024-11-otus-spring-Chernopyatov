package ru.otus.hw.configuration;

import lombok.AllArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.MongoPagingItemReader;
import org.springframework.batch.item.data.builder.MongoPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.transaction.PlatformTransactionManager;
import ru.otus.hw.cache.MongoJpaCache;
import ru.otus.hw.configuration.writer.JpaAuthorWriter;
import ru.otus.hw.configuration.writer.JpaBookWriter;
import ru.otus.hw.configuration.writer.JpaCommentWriter;
import ru.otus.hw.configuration.writer.JpaGenreWriter;
import ru.otus.hw.entities.mongo.MongoAuthor;
import ru.otus.hw.entities.mongo.MongoBook;
import ru.otus.hw.entities.mongo.MongoComment;
import ru.otus.hw.entities.mongo.MongoGenre;
import ru.otus.hw.repositories.jpa.JpaAuthorRepository;
import ru.otus.hw.repositories.jpa.JpaBookRepository;
import ru.otus.hw.repositories.jpa.JpaCommentRepository;
import ru.otus.hw.repositories.jpa.JpaGenreRepository;

import java.util.HashMap;

@Configuration
@AllArgsConstructor
public class JobConfiguration {
    public static final String IMPORT_LIBRARY_JOB_NAME = "importLibraryJobS";

    private static final int CHUNK_SIZE = 8;

    private JobRepository jobRepository;

    private PlatformTransactionManager platformTransactionManager;

    @Bean
    public MongoPagingItemReader<MongoAuthor> mongoAuthorItemReader(MongoOperations operations) {
        return new MongoPagingItemReaderBuilder<MongoAuthor>()
                .name("authorItemReader")
                .template(operations)
                .jsonQuery("{}")
                .targetType(MongoAuthor.class)
                .sorts(new HashMap<>())
                .build();
    }

    @Bean
    public MongoPagingItemReader<MongoGenre> mongoGenreItemReader(MongoOperations operations) {
        return new MongoPagingItemReaderBuilder<MongoGenre>()
                .name("genreItemReader")
                .template(operations)
                .jsonQuery("{}")
                .targetType(MongoGenre.class)
                .sorts(new HashMap<>())
                .build();
    }

    @Bean
    public MongoPagingItemReader<MongoBook> mongoBookItemReader(MongoOperations operations) {
        return new MongoPagingItemReaderBuilder<MongoBook>()
                .name("bookItemReader")
                .template(operations)
                .jsonQuery("{}")
                .targetType(MongoBook.class)
                .sorts(new HashMap<>())
                .build();
    }

    @Bean
    public MongoPagingItemReader<MongoComment> mongoCommentItemReader(MongoOperations operations) {
        return new MongoPagingItemReaderBuilder<MongoComment>()
                .name("commentItemReader")
                .template(operations)
                .jsonQuery("{}")
                .targetType(MongoComment.class)
                .sorts(new HashMap<>())
                .build();
    }

    @Bean
    public Job job(Flow splitFlow, Step bookStep, Step commentStep) {
        return new JobBuilder(IMPORT_LIBRARY_JOB_NAME, jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(splitFlow)
                .next(bookStep)
                .next(commentStep)
                .build()
                .build();
    }

    @Bean
    public Flow splitFlow(Flow authorFlow, Flow genreFlow) {
        return new FlowBuilder<SimpleFlow>("splitFlow")
                .split(taskExecutor())
                .add(authorFlow, genreFlow)
                .build();
    }

    @Bean
    public Flow authorFlow(Step authorStep) {
        return new FlowBuilder<SimpleFlow>("authorFlow")
                .start(authorStep)
                .build();
    }

    @Bean
    public Flow genreFlow(Step genreStep) {
        return new FlowBuilder<SimpleFlow>("genreFlow")
                .start(genreStep)
                .build();
    }

    @Bean
    public Step authorStep(ItemReader<MongoAuthor> reader, ItemWriter<MongoAuthor> writer) {
        return new StepBuilder("authorStep", jobRepository)
                .<MongoAuthor, MongoAuthor>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(reader)
                .writer(writer)
                .build();
    }

    @Bean
    public Step bookStep(ItemReader<MongoBook> reader, ItemWriter<MongoBook> writer) {
        return new StepBuilder("bookStep", jobRepository)
                .<MongoBook, MongoBook>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(reader)
                .writer(writer)
                .build();
    }

    @Bean
    public Step commentStep(ItemReader<MongoComment> reader, ItemWriter<MongoComment> writer) {
        return new StepBuilder("commentStep", jobRepository)
                .<MongoComment, MongoComment>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(reader)
                .writer(writer)
                .build();
    }

    @Bean
    public Step genreStep(ItemReader<MongoGenre> reader, ItemWriter<MongoGenre> writer) {
        return new StepBuilder("genreStep", jobRepository)
                .<MongoGenre, MongoGenre>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(reader)
                .writer(writer)
                .build();
    }

    @Bean
    public ItemWriter<MongoAuthor> jpaAuthorItemWriter(MongoJpaCache cache, JpaAuthorRepository jpaAuthorRepository) {
        return new JpaAuthorWriter(cache, jpaAuthorRepository);
    }

    @Bean
    public ItemWriter<MongoGenre>jpaGenreItemWriter(MongoJpaCache cache, JpaGenreRepository jpaGenreRepository) {
        return new JpaGenreWriter(cache, jpaGenreRepository);
    }

    @Bean
    public ItemWriter<MongoBook>jpaBookItemWriter(MongoJpaCache cache, JpaBookRepository jpaBookRepository) {
        return new JpaBookWriter(cache, jpaBookRepository);
    }

    @Bean
    public ItemWriter<MongoComment>jpaCommentItemWriter(MongoJpaCache cache, JpaCommentRepository jpaCommentRepository) {
        return new JpaCommentWriter(cache, jpaCommentRepository);
    }

    @Bean
    public TaskExecutor taskExecutor() {
        return new SimpleAsyncTaskExecutor("spring_batch");
    }
}
