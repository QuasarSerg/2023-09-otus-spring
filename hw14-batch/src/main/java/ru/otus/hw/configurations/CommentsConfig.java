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
import ru.otus.hw.models.mongo.CommentDocument;
import ru.otus.hw.models.sql.Comment;


@Configuration
@RequiredArgsConstructor
@Slf4j
public class CommentsConfig {

    public static final String TRANSFORM_COMMENT_STEP_NAME = "transformCommentStep";

    public static final String COMMENT_ITEM_READER_NAME = "commentItemReader";

    private static final int CHUNK_SIZE = 5;

    @Autowired
    private final JobRepository jobRepository;

    @Autowired
    private final PlatformTransactionManager platformTransactionManager;

    @Bean
    public JpaCursorItemReader<Comment> readerComment(EntityManagerFactory factory) {
        return new JpaCursorItemReaderBuilder<Comment>()
                .name(COMMENT_ITEM_READER_NAME)
                .entityManagerFactory(factory)
                .saveState(true)
                .queryString("select c from Comment c")
                .build();
    }

    @Bean
    public ItemProcessor<Comment, CommentDocument> processorComment(MigrationMapper migrationMapper) {
        return migrationMapper::toCommentDocument;
    }

    @Bean
    public MongoItemWriter<CommentDocument> writerComment(MongoTemplate template) {
        return new MongoItemWriterBuilder<CommentDocument>()
                .template(template)
                .collection("comments")
                .build();
    }

    @Bean
    public Step transformCommentStep(JpaCursorItemReader<Comment> readerComment,
                                     MongoItemWriter<CommentDocument> writerComment,
                                     ItemProcessor<Comment, CommentDocument> processorComment) {
        return new StepBuilder(TRANSFORM_COMMENT_STEP_NAME, jobRepository)
                .<Comment, CommentDocument>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(readerComment)
                .processor(processorComment)
                .writer(writerComment)
                .listener(new ItemReadListener<>() {
                    @Override
                    public void beforeRead() {
                        log.info("Starting to read comments");
                    }

                    @Override
                    public void afterRead(@NonNull Comment comment) {
                        log.info("Comment {} reading completed", comment.getText());
                    }

                    @Override
                    public void onReadError(@NonNull Exception ex) {
                        log.info("Error reading comments");
                    }
                }).build();
    }
}

