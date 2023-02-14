package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MealsUtil {
    public static final int DEFAULT_CALORIES_PER_DAY = 2000;

    public static final List<Meal> meals = Arrays.asList(
            new Meal("Завтрак", LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), 500, 1),
            new Meal("Обед", LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), 1000, 1),
            new Meal("Ужин", LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), 500, 1),
            new Meal("Еда на граничное значение", LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), 100, 1),
            new Meal("Завтрак", LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), 1000, 1),
            new Meal("Обед", LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), 500, 1),
            new Meal("Ужин", LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), 410, 1)
    );

    public static List<MealTo> getTos(Collection<Meal> meals, int caloriesPerDay) {
        return filterByPredicate(meals, caloriesPerDay, meal -> true);
    }

    public static List<MealTo> getFilteredTos(Collection<Meal> meals, int caloriesPerDay, LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime) {
        return filterByPredicate(meals, caloriesPerDay, meal -> DateTimeUtil.isBetweenHalfOpen(meal.getTime(), startTime, endTime)
                && DateTimeUtil.isBetweenClose(meal.getDate(), startDate, endDate));
    }

    private static List<MealTo> filterByPredicate(Collection<Meal> meals, int caloriesPerDay, Predicate<Meal> filter) {
        Map<LocalDate, Integer> caloriesSumByDate = meals.stream()
                .collect(
                        Collectors.groupingBy(Meal::getDate, Collectors.summingInt(Meal::getCalories))
//                      Collectors.toMap(Meal::getDate, Meal::getCalories, Integer::sum)
                );

        return meals.stream()
                .filter(filter)
                .map(meal -> createTo(meal, caloriesSumByDate.get(meal.getDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }

    private static MealTo createTo(Meal meal, boolean excess) {
        return new MealTo(meal.getId(), meal.getDateTime(), meal.getName(), meal.getCalories(), excess);
    }
}
