package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(9, 0), LocalTime.of(11, 0), 2000);
        mealsTo.forEach(System.out::println);

        System.out.println(filteredByStreams(meals, null, null, 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        if (meals == null) return null;

        List<UserMealWithExcess> result = new ArrayList<>();
        Map<LocalDate, Integer> sumOfCaloriesPerDay = new HashMap<>();
        LocalTime startLocalTime = startTime == null ? LocalTime.of(0,0) : startTime;
        LocalTime endLocalTime = endTime == null ? LocalTime.of(23,59) : endTime;

        for (UserMeal meal : meals) {
            LocalDate date = meal.getDateTime().toLocalDate();
            int calories = meal.getCalories();

            sumOfCaloriesPerDay.merge(date, calories, Integer::sum);
        }

        for (UserMeal meal : meals) {

            LocalDateTime date = meal.getDateTime();
            int calories = meal.getCalories();
            String description = meal.getDescription();
            boolean exceed = sumOfCaloriesPerDay.get(date.toLocalDate()) > caloriesPerDay;

            if ((date.toLocalTime().isAfter(startLocalTime) || date.toLocalTime().equals(startLocalTime)) && date.toLocalTime().isBefore(endLocalTime)) {
                UserMealWithExcess userMealWithExcess = new UserMealWithExcess(date, description, calories, exceed);

                result.add(userMealWithExcess);
            }

        }

        return result;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        if (meals == null) return null;

        LocalTime startLocalTime = startTime == null ? LocalTime.of(0,0) : startTime;
        LocalTime endLocalTime = endTime == null ? LocalTime.of(23,59) : endTime;

        final Map<LocalDate, Integer> sumOfCaloriesPerDay = meals.stream().collect(Collectors.toMap(meal -> meal.getDateTime().toLocalDate(), UserMeal::getCalories, Integer::sum));

        return meals.stream()
                .filter(meal -> meal.getDateTime().toLocalTime().isAfter(startLocalTime) || meal.getDateTime().toLocalTime().equals(startLocalTime))
                .filter(meal -> meal.getDateTime().toLocalTime().isBefore(endLocalTime))
                .map(meal -> new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(), sumOfCaloriesPerDay.get(meal.getDateTime().toLocalDate()) > caloriesPerDay)).collect(Collectors.toList());
    }
}
