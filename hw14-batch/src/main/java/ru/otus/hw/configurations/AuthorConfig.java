package ru.otus.hw.configurations;

import jakarta.persistence.EntityManagerFactory;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.builder.MongoItemWriterBuilder;
import org.springframework.batch.item.database.builder.JpaCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import ru.otus.hw.mappers.MigrationMapper;
import ru.otus.hw.models.mongo.AuthorDocument;
import ru.otus.hw.models.sql.Author;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class AuthorConfig {

    public static final String TRANSFORM_AUTHOR_STEP_NAME = "transformAuthorStep";

    public static final String AUTHOR_ITEM_READER_NAME = "authorItemReader";

    private static final int CHUNK_SIZE = 5;

    @Autowired
    private final JobRepository jobRepository;

    @Autowired
    private final PlatformTransactionManager platformTransactionManager;

    @Bean
    public ItemReader<Author> readerAuthor(EntityManagerFactory factory) {
        return new JpaCursorItemReaderBuilder<Author>()
                .name(AUTHOR_ITEM_READER_NAME)
                .entityManagerFactory(factory)
                .saveState(true)
                .queryString("select a from Author a")
                .build();
    }

    @Bean
    public ItemProcessor<Author, AuthorDocument> processorAuthor(MigrationMapper migrationMapper) {
        return migrationMapper::toAuthorDocument;
    }

    @Bean
    public ItemWriter<AuthorDocument> writerAuthor(MongoTemplate template) {
        return new MongoItemWriterBuilder<AuthorDocument>()
                .template(template)
                .collection("authors")
                .build();
    }

    @Bean
    public Step transformAuthorStep(ItemReader<Author> readerAuthor,
                                    ItemWriter<AuthorDocument> writerAuthor,
                                    ItemProcessor<Author, AuthorDocument> processorAuthor) {
        return new StepBuilder(TRANSFORM_AUTHOR_STEP_NAME, jobRepository)
                .<Author, AuthorDocument>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(readerAuthor)
                .processor(processorAuthor)
                .writer(writerAuthor)
                .listener(new ItemReadListener<>() {
                    @Override
                    public void beforeRead() {
                        log.info("Starting to read Authors");
                    }

                    @Override
                    public void afterRead(@NonNull Author author) {
                        log.info("Author {} reading completed", author.getFullName());
                    }

                    @Override
                    public void onReadError(@NonNull Exception ex) {
                        log.info("Error reading authors");
                    }
                })
                .build();
    }
}
