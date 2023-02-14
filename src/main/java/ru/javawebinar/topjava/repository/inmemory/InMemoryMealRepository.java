package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryMealRepository.class);
    private final Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(meal -> save(meal, 1));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        log.info("save {} by user with id {}", meal, userId);
        if (meal.getUserId() != userId) return null;

        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            repository.put(meal.getId(), meal);
            return meal;
        }
        // handle case: update, but not present in storage
        try {
            return repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
        } catch (NullPointerException e) {
            return null;
        }
    }

    @Override
    public boolean delete(int id, int userId) {
        log.info("delete {} by user with id {}", id, userId);
        try {
            if (repository.get(id).getUserId() != userId) return false;
            return repository.remove(id) != null;
        } catch (NullPointerException e) {
            return false;
        }
    }

    @Override
    public Meal get(int id, int userId) {
        log.info("get {} by user with id {}", id, userId);
        if (repository.get(id).getUserId() != userId) return null;
        try {
            return repository.get(id);
        } catch (NullPointerException e) {
            return null;
        }
    }

    @Override
    public Collection<Meal> getAll(int userId) {
        log.info("getAll by user with id {}", userId);
        return repository.values().stream()
                .filter(meal -> meal.getUserId() == userId)
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }
}

