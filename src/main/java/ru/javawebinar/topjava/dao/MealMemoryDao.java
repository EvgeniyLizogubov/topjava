package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MealMemoryDao implements MemoryDao {
    private static final ConcurrentHashMap<Integer, Meal> MEAL_REPOSITORY_MAP = new ConcurrentHashMap<>();

    static {
        MEAL_REPOSITORY_MAP.put(0, new Meal(0, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500));
        MEAL_REPOSITORY_MAP.put(1, new Meal(1, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000));
        MEAL_REPOSITORY_MAP.put(2, new Meal(2, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500));
        MEAL_REPOSITORY_MAP.put(3, new Meal(3, LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100));
        MEAL_REPOSITORY_MAP.put(4, new Meal(4, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000));
        MEAL_REPOSITORY_MAP.put(5, new Meal(5, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500));
        MEAL_REPOSITORY_MAP.put(6, new Meal(6, LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410));
    }
    private static final AtomicInteger MEAL_ID_HOLDER = new AtomicInteger(7);

    public void add(Meal meal) {
        final int mealId = MEAL_ID_HOLDER.incrementAndGet();
        meal.setId(mealId);
        MEAL_REPOSITORY_MAP.put(mealId, meal);
    }

    public void delete(int mealId) {
        MEAL_REPOSITORY_MAP.remove(mealId);
    }

    public void edit(Meal meal) {
        MEAL_REPOSITORY_MAP.put(meal.getId(), meal);
    }

    public List<Meal> getAll() {
        return new ArrayList<>(MEAL_REPOSITORY_MAP.values());
    }

    public Meal getById(int id) {
        return MEAL_REPOSITORY_MAP.get(id);
    }
}
