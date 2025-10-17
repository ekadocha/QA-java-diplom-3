package ru.education_services.stellarburgers.generators;

import com.github.javafaker.Faker;
import ru.education_services.stellarburgers.model.User;

public class UserGenerator {

    public static Faker faker = new Faker();

    public static User randomUser() {
        return new User()
                .setEmail(faker.internet().safeEmailAddress())
                .setPassword(faker.internet().password(6, 10))
                .setName(faker.name().fullName());
    }

    public static User randomUserWithShortPassword() {
        return new User()
                .setEmail(faker.internet().safeEmailAddress())
                .setPassword(faker.internet().password(1, 6))
                .setName(faker.name().fullName());
    }
}
