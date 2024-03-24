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
import ru.otus.hw.models.mongo.BookDocument;
import ru.otus.hw.models.sql.Book;


@Configuration
@RequiredArgsConstructor
@Slf4j
public class BookConfig {

    public static final String TRANSFORM_BOOK_STEP_NAME = "transformBookStep";

    public static final String BOOK_ITEM_READER_NAME = "bookItemReader";

    private static final int CHUNK_SIZE = 5;

    @Autowired
    private final JobRepository jobRepository;

    @Autowired
    private final PlatformTransactionManager platformTransactionManager;

    @Bean
    public ItemReader<Book> readerBook(EntityManagerFactory factory) {
        return new JpaCursorItemReaderBuilder<Book>()
                .name(BOOK_ITEM_READER_NAME)
                .entityManagerFactory(factory)
                .saveState(true)
                .queryString("select b from Book b")
                .build();
    }

    @Bean
    public ItemProcessor<Book, BookDocument> processorBook(MigrationMapper migrationMapper) {
        return migrationMapper::toBookDocument;
    }

    @Bean
    public ItemWriter<BookDocument> writerBook(MongoTemplate template) {
        return new MongoItemWriterBuilder<BookDocument>()
                .template(template)
                .collection("books")
                .build();
    }

    @Bean
    public Step transformBookStep(ItemReader<Book> readerBook,
                                  ItemWriter<BookDocument> writerBook,
                                  ItemProcessor<Book, BookDocument> processorBook) {
        return new StepBuilder(TRANSFORM_BOOK_STEP_NAME, jobRepository)
                .<Book, BookDocument>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(readerBook)
                .processor(processorBook)
                .writer(writerBook)
                .listener(new ItemReadListener<>() {
                    @Override
                    public void beforeRead() {
                        log.info("Starting to read books");
                    }

                    @Override
                    public void afterRead(@NonNull Book book) {
                        log.info("Book {} reading completed", book.getTitle());
                    }

                    @Override
                    public void onReadError(@NonNull Exception ex) {
                        log.info("Error reading books");
                    }
                }).build();
    }
}
