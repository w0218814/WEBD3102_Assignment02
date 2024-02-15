package com.example.tododatabase.servlet;

import com.example.tododatabase.dao.TodoDAO;
import com.example.tododatabase.model.Todo;
import com.example.tododatabase.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "TodoServlet", urlPatterns = {"/todo/*"})
public class TodoServlet extends HttpServlet {
    private TodoDAO todoDAO;
    private final static Logger LOGGER = Logger.getLogger(TodoServlet.class.getName());

    @Override
    public void init() {
        todoDAO = new TodoDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getPathInfo();
        try {
            if (action == null) {
                action = "/list"; // default action
            }
            switch (action) {
                case "/new":
                    showNewForm(request, response);
                    break;
                case "/delete":
                    deleteTodo(request, response);
                    break;
                case "/edit":
                    showEditForm(request, response);
                    break;
                case "/list":
                default:
                    listTodos(request, response);
                    break;
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in GET request", e);
            handleError(response, e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getPathInfo();
        try {
            switch (action) {
                case "/new":
                    insertTodo(request, response);
                    break;
                case "/update":
                    updateTodo(request, response);
                    break;
                default:
                    response.sendRedirect(request.getContextPath() + "/todo/list");
                    break;
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in POST request", e);
            handleError(response, e);
        }
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/todo-form.jsp").forward(request, response);
    }

    private void listTodos(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            List<Todo> listTodo = todoDAO.selectAllTodos();
            request.setAttribute("listTodo", listTodo);
            request.getRequestDispatcher("/WEB-INF/views/todo-list.jsp").forward(request, response);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error listing todos", e);
            handleError(response, e);
        }
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            long id = Long.parseLong(request.getParameter("id"));
            Todo existingTodo = todoDAO.selectTodo(id);
            request.setAttribute("todo", existingTodo);
            request.getRequestDispatcher("/WEB-INF/views/todo-form.jsp").forward(request, response);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error showing edit form", e);
            handleError(response, e);
        }
    }

    private void insertTodo(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession();
        User loggedInUser = (User) session.getAttribute("user");
        if (loggedInUser == null) {
            // Redirect user to login page if not logged in
            response.sendRedirect(request.getContextPath() + "/user/login");
            return;
        }

        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String targetDateStr = request.getParameter("targetDate");
        boolean isDone = "true".equals(request.getParameter("isDone"));
        Date targetDate;
        try {
            targetDate = new SimpleDateFormat("yyyy-MM-dd").parse(targetDateStr);
        } catch (ParseException e) {
            LOGGER.log(Level.SEVERE, "Error parsing targetDate", e);
            handleError(response, e);
            return;
        }

        Todo newTodo = new Todo(title, description, targetDate, isDone);
        try {
            todoDAO.insertTodo(newTodo, loggedInUser.getId());
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error inserting todo", e);
            handleError(response, e);
            return;
        }
        response.sendRedirect(request.getContextPath() + "/todo/list");
    }

    private void updateTodo(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ParseException {
        long id = Long.parseLong(request.getParameter("id"));
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String targetDateStr = request.getParameter("targetDate");
        boolean isDone = Boolean.parseBoolean(request.getParameter("isDone"));
        Date targetDate = new SimpleDateFormat("yyyy-MM-dd").parse(targetDateStr);

        Todo todo = new Todo(id, title, description, targetDate, isDone);
        todoDAO.updateTodo(todo);
        response.sendRedirect(request.getContextPath() + "/todo/list");
    }

    private void deleteTodo(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        long id = Long.parseLong(request.getParameter("id"));
        todoDAO.deleteTodo(id);
        response.sendRedirect(request.getContextPath() + "/todo/list");
    }

    private void handleError(HttpServletResponse response, Exception e) throws IOException {
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An unexpected error occurred: " + e.getMessage());
    }
}
