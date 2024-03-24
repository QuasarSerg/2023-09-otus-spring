package ru.otus.hw.models.mongo;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "genres")
public class GenreDocument {
    @Id
    private String id;

    @Field(name = "name")
    private String name;

    public GenreDocument(String name) {
        this.name = name;
    }
}
