package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jpa для работы с комментариями ")
@DataJpaTest
@Import({JpaCommentRepository.class})
class JpaCommentRepositoryTest {

    @Autowired
    private JpaCommentRepository repositoryJpa;

    @Autowired
    private TestEntityManager em;

    @DisplayName("должен сохранять новый коментарий")
    @Test
    void shouldSaveNewComment() {
        var expectedComment = new Comment(3, "Отличная книга!",  em.find(Book.class, 1L));
        var returnedComment = repositoryJpa.save(expectedComment);

        assertThat(returnedComment).isNotNull()
                .isEqualTo(expectedComment);

        assertThat(repositoryJpa.findById(returnedComment.getId()).orElse(new Comment())).isEqualTo(returnedComment);
    }
}