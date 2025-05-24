package ru.otus.hw.configuration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.hw.repositories.jpa.JpaAuthorRepository;
import ru.otus.hw.repositories.jpa.JpaBookRepository;
import ru.otus.hw.repositories.jpa.JpaCommentRepository;
import ru.otus.hw.repositories.jpa.JpaGenreRepository;
import ru.otus.hw.repositories.mongo.MongoAuthorRepository;
import ru.otus.hw.repositories.mongo.MongoBookRepository;
import ru.otus.hw.repositories.mongo.MongoCommentRepository;
import ru.otus.hw.repositories.mongo.MongoGenreRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.otus.hw.configuration.JobConfiguration.IMPORT_LIBRARY_JOB_NAME;

@DisplayName("Тестирования Job-ы по миграции данных из Mongo в H2")
@SpringBatchTest
@SpringBootTest
class JobConfigurationTest {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private JobRepositoryTestUtils jobRepositoryTestUtils;

    @Autowired
    private JpaAuthorRepository jpaAuthorRepository;

    @Autowired
    private JpaBookRepository jpaBookRepository;

    @Autowired
    private JpaCommentRepository jpaCommentRepository;

    @Autowired
    private JpaGenreRepository jpaGenreRepository;

    @Autowired
    private MongoAuthorRepository mongoAuthorRepository;

    @Autowired
    private MongoBookRepository mongoBookRepository;

    @Autowired
    private MongoCommentRepository mongoCommentRepository;

    @Autowired
    private MongoGenreRepository mongoGenreRepository;

    @BeforeEach
    void clearMetaData() {
        jobRepositoryTestUtils.removeJobExecutions();
    }

    @DisplayName("должен перенести все данные из Mongo в H2")
    @Test
    void job() throws Exception {
        Job job = jobLauncherTestUtils.getJob();
        JobParameters parameters = new JobParametersBuilder().toJobParameters();
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(parameters);

        assertThat(job).isNotNull().extracting(Job::getName).isEqualTo(IMPORT_LIBRARY_JOB_NAME);
        assertThat(jobExecution.getExitStatus().getExitCode()).isEqualTo("COMPLETED");
        assertThat(jpaAuthorRepository.findAll().size()).isGreaterThan(1).isEqualTo(mongoAuthorRepository.findAll().size());
        assertThat(jpaBookRepository.findAll().size()).isGreaterThan(1).isEqualTo(mongoBookRepository.findAll().size());
        assertThat(jpaCommentRepository.findAll().size()).isGreaterThan(1).isEqualTo(mongoCommentRepository.findAll().size());
        assertThat(jpaGenreRepository.findAll().size()).isGreaterThan(1).isEqualTo(mongoGenreRepository.findAll().size());
    }
}