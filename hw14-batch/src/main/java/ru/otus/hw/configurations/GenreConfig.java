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
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.data.builder.MongoItemWriterBuilder;
import org.springframework.batch.item.database.JpaCursorItemReader;
import org.springframework.batch.item.database.builder.JpaCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import ru.otus.hw.mappers.MigrationMapper;
import ru.otus.hw.models.mongo.GenreDocument;
import ru.otus.hw.models.sql.Genre;


@Configuration
@RequiredArgsConstructor
@Slf4j
public class GenreConfig {

    public static final String TRANSFORM_GENRE_STEP_NAME = "transformGenreStep";

    public static final String GENRE_ITEM_READER_NAME = "genreItemReader";

    private static final int CHUNK_SIZE = 5;

    @Autowired
    private final JobRepository jobRepository;

    @Autowired
    private final PlatformTransactionManager platformTransactionManager;

    @Bean
    public JpaCursorItemReader<Genre> readerGenre(EntityManagerFactory factory) {
        return new JpaCursorItemReaderBuilder<Genre>()
                .name(GENRE_ITEM_READER_NAME)
                .entityManagerFactory(factory)
                .saveState(true)
                .queryString("select g from Genre g")
                .build();
    }

    @Bean
    public ItemProcessor<Genre, GenreDocument> processorGenre(MigrationMapper migrationMapper) {
        return migrationMapper::toGenreDocument;
    }

    @Bean
    public MongoItemWriter<GenreDocument> writerGenre(MongoTemplate template) {
        return new MongoItemWriterBuilder<GenreDocument>()
                .template(template)
                .collection("genres")
                .build();
    }

    @Bean
    public Step transformGenreStep(ItemReader<Genre> readerGenre,
                                   ItemWriter<GenreDocument> writerGenre,
                                   ItemProcessor<Genre, GenreDocument> processorGenre) {
        return new StepBuilder(TRANSFORM_GENRE_STEP_NAME, jobRepository)
                .<Genre, GenreDocument>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(readerGenre)
                .processor(processorGenre)
                .writer(writerGenre)
                .listener(new ItemReadListener<>() {
                    @Override
                    public void beforeRead() {
                        log.info("Starting to read genres");
                    }

                    @Override
                    public void afterRead(@NonNull Genre genre) {
                        log.info("Genre {} reading completed", genre.getName());
                    }

                    @Override
                    public void onReadError(@NonNull Exception ex) {
                        log.info("Error reading genres");
                    }
                })
                .build();
    }
}
