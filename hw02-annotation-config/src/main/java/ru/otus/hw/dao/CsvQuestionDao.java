package ru.otus.hw.dao;

import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CsvQuestionDao implements QuestionDao {
    private static final char SEPARATOR = '|';

    private final TestFileNameProvider fileNameProvider;


    @Override
    public List<Question> findAll() {
        return getListQuestionDto().stream()
                .map(QuestionDto::toDomainObject)
                .collect(Collectors.toList());
    }

    private List<QuestionDto> getListQuestionDto() {
        if (fileNameProvider.getTestFileName().isEmpty()) {
            return new ArrayList<>();
        }
        ClassLoader classLoader = getClass().getClassLoader();
        try (InputStream inputStream = classLoader.getResourceAsStream(fileNameProvider.getTestFileName())) {
            if (inputStream != null) {
                try (InputStreamReader inputStreamReader = new InputStreamReader(inputStream)) {
                    return new CsvToBeanBuilder<QuestionDto>(inputStreamReader)
                            .withType(QuestionDto.class)
                            .withSeparator(SEPARATOR).build()
                            .parse();
                }
            } else {
                String msg = String.format("The resource %s could not be found", fileNameProvider.getTestFileName());
                throw new NullPointerException(msg);
            }
        } catch (IOException | NullPointerException e) {
            throw new QuestionReadException("Failed to read file", e);
        }
    }
}
