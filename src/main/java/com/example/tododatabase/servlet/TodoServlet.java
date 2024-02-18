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
        this.todoDAO = new TodoDAO(); // Ensure this is correctly connected to your DB
    }


    @Override
protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    HttpSession session = request.getSession(false); // Check if session exists
    if (session == null || session.getAttribute("user") == null) {
        response.sendRedirect(request.getContextPath() + "/user/login"); // Redirect to login page
        return;
    }

    String action = request.getPathInfo();
    if (action == null) {
        action = "/list"; // Default action
    }

    try {
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
    } catch (SQLException ex) {
        LOGGER.log(Level.SEVERE, "Database error", ex);
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database operation failed.");
    }
}


@Override
protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    HttpSession session = request.getSession(false);
    if (session == null || session.getAttribute("user") == null) {
        response.sendRedirect(request.getContextPath() + "/user/login");
        return;
    }

    String action = request.getPathInfo();

    try {
        switch (action) {
            case "/insert":
                insertTodo(request, response);
                break;
            case "/update":
                updateTodo(request, response);
                break;
            default:
                listTodos(request, response);
                break;
        }
    } catch (SQLException ex) {
        LOGGER.log(Level.SEVERE, "Database error", ex);
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database operation failed.");
    }
}

private void showNewForm(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    request.getRequestDispatcher("/WEB-INF/views/todo-form.jsp").forward(request, response);
}


private void insertTodo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        Date targetDate = null;
        try {
            targetDate = new SimpleDateFormat("yyyy-MM-dd").parse(request.getParameter("targetDate"));
        } catch (ParseException e) {
            LOGGER.log(Level.SEVERE, "Error parsing date", e);
        }

        boolean isDone = "on".equals(request.getParameter("isDone"));
        HttpSession session = request.getSession(); // Use existing session
        User currentUser = (User) session.getAttribute("user");
        long userId = currentUser.getId();

        // Adjusted to use the constructor with userId for creating a new Todo object
        Todo newTodo = new Todo(0, userId, title, description, targetDate, isDone); // Assuming ID is auto-generated and not needed here
        todoDAO.insertTodo(newTodo); // Corrected to match the updated TodoDAO method signature
        response.sendRedirect("list");
    }
    private void listTodos(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");
        long userId = currentUser.getId();

        List<Todo> listTodo = todoDAO.selectAllTodos(userId); // Correct method call to TodoDAO
        request.setAttribute("listTodo", listTodo);
        request.getRequestDispatcher("/WEB-INF/views/todo-list.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        long id = Long.parseLong(request.getParameter("id"));
        long userId = ((User) request.getSession().getAttribute("user")).getId(); // Fetch userId from session

        Todo existingTodo = todoDAO.selectTodo(id, userId); // Corrected to include userId
        request.setAttribute("todo", existingTodo);
        request.getRequestDispatcher("/WEB-INF/views/todo-form.jsp").forward(request, response);
    }

    private void updateTodo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        long id = Long.parseLong(request.getParameter("id"));
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        Date targetDate = null;
        try {
            targetDate = new SimpleDateFormat("yyyy-MM-dd").parse(request.getParameter("targetDate"));
        } catch (ParseException e) {
            LOGGER.log(Level.SEVERE, "Error parsing date", e);
        }

        boolean isDone = "on".equals(request.getParameter("isDone"));
        long userId = ((User) request.getSession().getAttribute("user")).getId(); // Fetch userId from session

        Todo todo = new Todo(id, userId, title, description, targetDate, isDone); // Correct constructor call
        todoDAO.updateTodo(todo); // Correct method call to TodoDAO
        response.sendRedirect("list");
    }

    private void deleteTodo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        long id = Long.parseLong(request.getParameter("id"));
        long userId = ((User)request.getSession().getAttribute("user")).getId(); // Fetch userId from session

        todoDAO.deleteTodo(id, userId); // Correct method call to TodoDAO for user-specific deletion
        response.sendRedirect("list");
    }
}
