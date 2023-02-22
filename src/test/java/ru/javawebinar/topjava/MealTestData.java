package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {

    public static final int USER_ID = START_SEQ;
    public static final int ADMIN_ID = START_SEQ + 1;
    public static final int USER_MEAL_ID = START_SEQ + 3;
    public static final int NOT_FOUND = START_SEQ + 100;

    public static final Meal userMeal = new Meal(USER_MEAL_ID, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500);
    public static final Meal adminMeal = new Meal(USER_MEAL_ID + 2, LocalDateTime.of(2015, Month.JUNE, 1, 10, 0), "Админ завтрак", 510);
    public static final Meal nextAdminMeal = new Meal(USER_MEAL_ID + 3, LocalDateTime.of(2015, Month.JUNE, 2, 14, 0), "Админ обед", 1500);
    public static final Meal lastAdminMeal = new Meal(USER_MEAL_ID + 4, LocalDateTime.of(2015, Month.JUNE, 3, 19, 0), "Админ ужин", 1);

    public static Meal getNew() {
        return new Meal(null, LocalDateTime.of(2020, Month.AUGUST, 30, 16, 20), "Шавуха", 666);
    }

    public static Meal getUpdated() {
        Meal updated = new Meal(userMeal);
        updated.setDateTime(LocalDateTime.of(2020, Month.JANUARY, 1, 1, 1));
        updated.setDescription("Завтрак чемпиона");
        updated.setCalories(300);
        return updated;
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingRecursiveFieldByFieldElementComparator().isEqualTo(expected);
    }
}
