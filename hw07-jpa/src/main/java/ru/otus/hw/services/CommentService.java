package ru.otus.hw.services;

import ru.otus.hw.models.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    List<Comment> findAllByBookId(long bookId);

    Optional<Comment> findCommentById(long id);

    Comment insert(String text, long authorId);

    void deleteById(long id);

    Comment update(long id, String text, long authorId);

}
