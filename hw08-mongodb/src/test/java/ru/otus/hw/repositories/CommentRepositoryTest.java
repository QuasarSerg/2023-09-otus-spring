package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jpa для работы с комментариями ")
@DataMongoTest
class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @DisplayName("должен сохранять новый коментарий")
    @Test
    void shouldSaveNewComment() {
        Query queryBook = new Query();
        queryBook.addCriteria(Criteria.where("title").is("BookTitle_1"));
        Book book = mongoTemplate.findOne(queryBook, Book.class);

        var expectedComment = new Comment("Отличная книга!", book);
        var returnedComment = commentRepository.save(expectedComment);

        assertThat(returnedComment).isNotNull()
                .matches(comment -> !comment.getId().isEmpty())
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedComment);
    }
}