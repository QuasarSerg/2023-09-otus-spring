package ru.otus.hw.commands;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.otus.hw.mappers.MigrationMapper;
import ru.otus.hw.models.mongo.AuthorDocument;
import ru.otus.hw.models.mongo.BookDocument;
import ru.otus.hw.models.mongo.CommentDocument;
import ru.otus.hw.models.mongo.GenreDocument;
import ru.otus.hw.models.sql.Book;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@SpringBatchTest
class BatchCommandsTest {
    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private JobRepositoryTestUtils jobRepositoryTestUtils;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private MigrationMapper migrationMapper;


    @Autowired
    private EntityManager em;

    private List<BookDocument> expectedBookList;


    @BeforeEach
    void setUp() {
        clearData();
        generateTestData();
    }

    private void clearData() {
        mongoTemplate.dropCollection(AuthorDocument.class);
        mongoTemplate.dropCollection(GenreDocument.class);
        mongoTemplate.dropCollection(BookDocument.class);
        mongoTemplate.dropCollection(CommentDocument.class);
        jobRepositoryTestUtils.removeJobExecutions();
    }

    private void generateTestData() {
        List<Book> bookList = em.createQuery("select b from Book b " +
                "left join fetch b.author " +
                "left join fetch b.genre", Book.class).getResultList();
        expectedBookList = bookList.stream().map(migrationMapper::toBookDocument).toList();
    }

    @Test
    void shouldStartMigrationJob() throws Exception {
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(new JobParametersBuilder()
                .addLocalDateTime("time", LocalDateTime.now())
                .toJobParameters());
        assertThat(jobExecution.getExitStatus().getExitCode()).isEqualTo("COMPLETED");

        List<AuthorDocument> authorDocumentList = mongoTemplate.findAll(AuthorDocument.class);
        List<GenreDocument> genreDocumentList = mongoTemplate.findAll(GenreDocument.class);
        List<BookDocument> bookDocumentList = mongoTemplate.findAll(BookDocument.class);
        List<CommentDocument> commentDocumentList = mongoTemplate.findAll(CommentDocument.class);

        assertThat(authorDocumentList.size()).isEqualTo(3);
        assertThat(genreDocumentList.size()).isEqualTo(3);
        assertThat(bookDocumentList.size()).isEqualTo(3);
        assertThat(commentDocumentList.size()).isEqualTo(2);

        assertThat(bookDocumentList)
                .usingRecursiveComparison().isEqualTo(expectedBookList);
    }
}