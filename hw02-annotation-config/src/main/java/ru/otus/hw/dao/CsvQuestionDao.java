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
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

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
        InputStream inputStream = getInputStream();
        try (InputStreamReader inputStreamReader = new InputStreamReader(inputStream)) {
            return new CsvToBeanBuilder<QuestionDto>(inputStreamReader)
                    .withType(QuestionDto.class)
                    .withSeparator(SEPARATOR).build()
                    .parse();
        } catch (IOException e) {
            throw new QuestionReadException("Failed to read file", e);
        }
    }

    private InputStream getInputStream() {
        if (fileNameProvider.getTestFileName().isEmpty()) {
            throw new QuestionReadException("Failed to read file");
        }
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileNameProvider.getTestFileName());
        if (isNull(inputStream)) {
            throw new QuestionReadException("The resource %s could not be found"
                    .formatted(fileNameProvider.getTestFileName()));
        }
        return inputStream;
    }
}
