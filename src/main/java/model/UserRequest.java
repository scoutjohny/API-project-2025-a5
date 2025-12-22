package model;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.javafaker.Faker;
import lombok.*;

import java.util.Locale;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@With
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserRequest {

    @JsonProperty("firstName")

    private String first_Name;
    private String lastName;
    private String email;

    private String phone;
    private UserLocation userLocation;

    public static UserRequest createUser(){
        Faker faker = new Faker(new Locale("en-US"));

        UserLocation location = UserLocation.builder()
                .city(faker.address().city())
                .state(faker.address().state())
                .street(faker.address().streetAddress())
                .country(faker.address().country())
                .build();

        return UserRequest.builder()
                .first_Name(faker.name().firstName())
                .lastName(faker.name().lastName())
                .email(faker.internet().emailAddress())
                .phone(faker.phoneNumber().phoneNumber())
                .userLocation(location)
                .build();
    }

}
