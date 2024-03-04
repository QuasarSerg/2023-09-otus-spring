package ru.otus.hw.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.converters.CommentConverter;
import ru.otus.hw.services.CommentService;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@ShellComponent
public class CommentCommands {
    private final CommentService commentService;

    private final CommentConverter commentConverter;

    @ShellMethod(value = "Find comment by id", key = "cbid")
    public String findCommentById(String id) {
        return commentService.findCommentById(id)
                .map(commentConverter::commentToString)
                .orElse("Comment with id %s not found".formatted(id));
    }

    //acbbid 1
    @ShellMethod(value = "Find all comment by book id", key = "acbbid")
    public String findAllCommentByBookId(String id) {
        return commentService.findAllByBookId(id).stream()
                .map(commentConverter::commentToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    //cins test 1
    @ShellMethod(value = "Insert comment", key = "cins")
    public String insertComment(String text, String bookId) {
        var savedComment = commentService.insert(text, bookId);
        return commentConverter.commentToString(savedComment);
    }

    //cupd 2 text3 1
    @ShellMethod(value = "Update comment", key = "cupd")
    public String updateComment(String id, String text, String bookId) {
        var savedComment = commentService.update(id, text, bookId);
        return commentConverter.commentToString(savedComment);
    }

    @ShellMethod(value = "Delete comment by id", key = "cdel")
    public void deleteComment(String id) {
        commentService.deleteById(id);
    }
}
