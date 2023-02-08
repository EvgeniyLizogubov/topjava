package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.MealDao;
import ru.javawebinar.topjava.dao.MemoryMealDao;
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
    private MealDao mealDao;

    public void init() {
        mealDao = new MemoryMealDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("redirect to meals");
        String action = request.getParameter("action");
        if (action == null) action = "listUser";
        switch (action) {
            case "delete":
                log.debug("'Delete' selected");
                delete(request, response);
                break;
            case "edit":
                log.debug("'Edit' selected");
                edit(request, response);
                break;
            case "insert":
                log.debug("'Add meal' selected");
                insert(request, response);
                break;
            default:
                log.debug("trying to load meal's list");
                showListMeals(request, response);
                break;
        }
    }

    private void delete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = getIdParameter(request);
        log.debug("trying to delete meal");
        mealDao.delete(id);
        response.sendRedirect("meals");
    }

    private void edit(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        int id = getIdParameter(request);
        Meal meal = mealDao.getById(id);
        request.setAttribute("meal", meal);
        request.getRequestDispatcher(INSERT_OR_EDIT).forward(request, response);
    }

    private int getIdParameter(HttpServletRequest request) {
        return Integer.parseInt(request.getParameter("id"));
    }

    private void insert(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.setAttribute("currentDateTime", LocalDateTime.now());
        request.setAttribute("dateTimeFormatter", DATE_TIME_FORMATTER);
        request.getRequestDispatcher(INSERT_OR_EDIT).forward(request, response);
    }

    private void showListMeals(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        List<MealTo> mealsTo = MealsUtil.filteredByStreams(mealDao.getAll(), LocalTime.MIN, LocalTime.MAX, CALORIES_PER_DAY);
        request.setAttribute("mealsTo", mealsTo);
        request.setAttribute("dateTimeFormatter", DATE_TIME_FORMATTER);
        request.getRequestDispatcher(LIST_MEALS).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.debug("redirect to meal");
        request.setCharacterEncoding("UTF-8");
        LocalDateTime localDateTime = LocalDateTime.parse(request.getParameter("dateTime"));
        String description = request.getParameter("description");
        int calories = Integer.parseInt(request.getParameter("calories"));

        Meal meal = new Meal(localDateTime, description, calories);
        String id = request.getParameter("id");
        if (id == null || id.isEmpty()) {
            log.debug("trying to add new meal");
            mealDao.add(meal);
        } else {
            meal.setId(Integer.parseInt(id));
            log.debug("trying to edit meal");
            mealDao.edit(meal);
        }

        response.sendRedirect("meals");
    }
}
