package demo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "item")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {

    @Id
    private String id;

    @Field(type = FieldType.Text, name = "name")
    private String name;
}
