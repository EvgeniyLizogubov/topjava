package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MemoryMealDao implements MealDao {
    private final Map<Integer, Meal> mealRepositoryMap = new ConcurrentHashMap<>();
    private final AtomicInteger mealIdHolder = new AtomicInteger();

    {
        List<Meal> meals = Arrays.asList(
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );
        meals.forEach(this::add);
    }

    @Override
    public Meal add(Meal meal) {
        final int mealId = mealIdHolder.incrementAndGet();
        meal.setId(mealId);
        mealRepositoryMap.put(mealId, meal);
        return meal;
    }

    @Override
    public void delete(int id) {
        mealRepositoryMap.remove(id);
    }

    @Override
    public Meal edit(Meal meal) {
        mealRepositoryMap.put(meal.getId(), meal);
        return meal;
    }

    @Override
    public List<Meal> getAll() {
        return new ArrayList<>(mealRepositoryMap.values());
    }

    @Override
    public Meal getById(int id) {
        return mealRepositoryMap.get(id);
    }
}
