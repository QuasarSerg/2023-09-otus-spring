package ru.otus.hw.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.DeleteMapping;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.List;


@Slf4j
@RequiredArgsConstructor
@Controller
public class BookController {
    private final BookService bookService;

    private final AuthorService authorService;

    private final GenreService genreService;

    @GetMapping("/")
    public String listPage(Model model) {
        List<BookDto> bookList = bookService.findAll();
        model.addAttribute("bookList", bookList);

        return "list";
    }

    @GetMapping("/book/edit")
    public String editPage(@RequestParam("id") long id, Model model) {
        BookDto book;
        if (id == 0) {
            book = new BookDto();
            book.setId(id);
        } else {
            book = bookService.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Book with id %d not found".formatted(id)));
        }
        fillModelAttributes(model, book);

        return "edit";
    }

    @PostMapping("/book/edit")
    public String updateBook(@Valid @ModelAttribute("book") BookDto book,
                             BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .forEach(log::warn);

            fillModelAttributes(model, book);

            return "edit";
        }
        bookService.update(book);

        return "redirect:/";
    }

    @DeleteMapping("/book/delete")
    public String deleteBook (@RequestParam("id") long id) {
        bookService.deleteById(id);

        return "redirect:/";
    }

    private void fillModelAttributes(Model model, BookDto book) {
        model.addAttribute("book", book);
        model.addAttribute("authorList", authorService.findAll());
        model.addAttribute("genreList", genreService.findAll());
    }
}