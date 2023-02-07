package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface MemoryDao {
    void add(Meal meal);

    void delete(int mealId);

    void edit(Meal meal);

    List<Meal> getAll();

    Meal getById(int id);
}
