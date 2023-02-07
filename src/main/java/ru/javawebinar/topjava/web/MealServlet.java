package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.MemoryDao;
import ru.javawebinar.topjava.dao.MealMemoryDao;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {

    private static final Logger log = getLogger(MealServlet.class);
    private static final int CALORIES_PER_DAY = 2000;
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final String INSERT_OR_EDIT = "/meal.jsp";
    private static final String LIST_MEALS = "/meals.jsp";
    private final MemoryDao memoryDao;

    public MealServlet() {
        super();
        memoryDao = new MealMemoryDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("redirect to meals");
        String forward;
        String action = request.getParameter("action");
        if (action == null) action = "listUser";

        if (action.equalsIgnoreCase("delete")) {
            int mealId = Integer.parseInt(request.getParameter("mealId"));
            memoryDao.delete(mealId);
            forward = LIST_MEALS;
            List<MealTo> mealsTo = MealsUtil.filteredByStreams(memoryDao.getAll(), LocalTime.MIN, LocalTime.MAX, CALORIES_PER_DAY);
            request.setAttribute("mealsTo", mealsTo);
            request.setAttribute("dateTimeFormatter", DATE_TIME_FORMATTER);
        } else if (action.equalsIgnoreCase("edit")) {
            int mealId = Integer.parseInt(request.getParameter("mealId"));
            Meal meal = memoryDao.getById(mealId);
            forward = INSERT_OR_EDIT;
            request.setAttribute("meal", meal);
        } else if (action.equalsIgnoreCase("insert")) {
            forward = INSERT_OR_EDIT;
        } else if (action.equalsIgnoreCase("listUser")) {
            forward = LIST_MEALS;
            List<MealTo> mealsTo = MealsUtil.filteredByStreams(memoryDao.getAll(), LocalTime.MIN, LocalTime.MAX, CALORIES_PER_DAY);
            request.setAttribute("mealsTo", mealsTo);
            request.setAttribute("dateTimeFormatter", DATE_TIME_FORMATTER);
        } else {
            forward = INSERT_OR_EDIT;
        }

        request.getRequestDispatcher(forward).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        LocalDateTime localDateTime = LocalDateTime.parse(request.getParameter("dob"));
        String description = request.getParameter("description");
        int calories = Integer.parseInt(request.getParameter("calories"));

        Meal meal = new Meal(localDateTime, description, calories);
        String mealId = request.getParameter("mealId");
        if (mealId == null || mealId.isEmpty()) {
            memoryDao.add(meal);
        } else {
            meal.setId(Integer.parseInt(mealId));
            memoryDao.edit(meal);
        }

        List<MealTo> mealsTo = MealsUtil.filteredByStreams(memoryDao.getAll(), LocalTime.MIN, LocalTime.MAX, CALORIES_PER_DAY);
        request.setAttribute("mealsTo", mealsTo);
        request.setAttribute("dateTimeFormatter", DATE_TIME_FORMATTER);
        request.getRequestDispatcher(LIST_MEALS).forward(request, response);
    }
}
