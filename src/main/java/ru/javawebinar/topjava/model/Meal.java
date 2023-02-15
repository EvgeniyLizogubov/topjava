package ru.javawebinar.topjava.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Meal extends AbstractBaseEntity {
    private final String description;
    private final LocalDateTime dateTime;
    private final int calories;
    private Integer userId;

    public Meal(String description, LocalDateTime dateTime, int calories) {
        this(null, description, dateTime, calories);
    }

    public Meal(Integer id, String description, LocalDateTime dateTime, int calories) {
        super(id);
        this.description = description;
        this.dateTime = dateTime;
        this.calories = calories;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public int getCalories() {
        return calories;
    }

    public LocalDate getDate() {
        return dateTime.toLocalDate();
    }

    public LocalTime getTime() {
        return dateTime.toLocalTime();
    }

    public boolean isNew() {
        return id == null;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Meal{" +
                "id=" + id +
                ", dateTime=" + dateTime +
                ", description='" + description +
                ", calories=" + calories +
                ", userId=" + userId +
                '}';
    }
}
