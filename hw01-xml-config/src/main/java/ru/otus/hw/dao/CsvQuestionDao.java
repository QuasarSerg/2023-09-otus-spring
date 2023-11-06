package ru.otus.hw.dao;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class CsvQuestionDao implements QuestionDao {
    private final TestFileNameProvider fileNameProvider;

    @Override
    public List<Question> findAll() {
        // Использовать CsvToBean
        // https://opencsv.sourceforge.net/#collection_based_bean_fields_one_to_many_mappings
        // Использовать QuestionReadException
        try {
            FileReader fileReader = new FileReader(fileNameProvider.getTestFileName());
            CsvToBean<QuestionDto> csvToBean = new CsvToBeanBuilder<QuestionDto>(fileReader)
                    .withType(QuestionDto.class)
                    .withSeparator('|').build();
            return csvToBean.parse().stream()
                    .map(QuestionDto::toDomainObject)
                    .collect(Collectors.toList());
        } catch (FileNotFoundException e) {
            throw new QuestionReadException("Failed to read file!", e);
        }
    }
}
