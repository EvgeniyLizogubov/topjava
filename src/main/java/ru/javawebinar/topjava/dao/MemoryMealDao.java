package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MemoryMealDao implements MealDao {
    private final Map<Integer, Meal> mealRepositoryMap = new ConcurrentHashMap<>();
    private final AtomicInteger mealIdHolder = new AtomicInteger();

    public Meal add(Meal meal) {
        final int mealId = mealIdHolder.incrementAndGet();
        meal.setId(mealId);
        return mealRepositoryMap.put(mealId, meal);
    }

    public void delete(int id) {
        mealRepositoryMap.remove(id);
    }

    public Meal edit(Meal meal) {
        if (mealRepositoryMap.containsKey(meal.getId())) {
            return mealRepositoryMap.put(meal.getId(), meal);
        }
        return null;
    }

    public List<Meal> getAll() {
        return new ArrayList<>(mealRepositoryMap.values());
    }

    public Meal getById(int id) {
        return mealRepositoryMap.get(id);
    }

    {
        add(new Meal(0, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500));
        add(new Meal(1, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000));
        add(new Meal(2, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500));
        add(new Meal(3, LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100));
        add(new Meal(4, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000));
        add(new Meal(5, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500));
        add(new Meal(6, LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410));
    }
}
