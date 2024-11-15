package wastewise.form.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@AllArgsConstructor
@Getter
@Setter
@Document(collection = "formCollection")
@NoArgsConstructor
public class FormItem {

    @Id
    private String id;

    @Field("location")
    Location location;

    @Field("email")
    String email;

    @Field("description")
    private String description;

    @Field("name")
    private String name;

    @Field("phone_number")
    private String phoneNumber;
}