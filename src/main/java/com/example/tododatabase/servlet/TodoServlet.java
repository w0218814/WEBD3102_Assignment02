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
        this.todoDAO = new TodoDAO();
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getPathInfo();
        if (action == null) {
            action = "/list"; // Default action if no action specified
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
            throw new ServletException("Database error", ex);
        }
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
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
            throw new ServletException("Database error", ex);
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
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");
        long userId = currentUser.getId(); // Assuming User object has getId() method to retrieve user ID

        Todo newTodo = new Todo(title, description, targetDate, isDone); // Assuming this constructor exists in Todo class
        todoDAO.insertTodo(newTodo, userId); // Pass both Todo object and userId to insertTodo
        response.sendRedirect("list");
    }

    private void listTodos(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        List<Todo> listTodo = todoDAO.selectAllTodos();
        request.setAttribute("listTodo", listTodo);
        request.getRequestDispatcher("/WEB-INF/views/todo-list.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        long id = Long.parseLong(request.getParameter("id"));
        Todo existingTodo = todoDAO.selectTodo(id);
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
        Todo todo = new Todo(id, title, description, targetDate, isDone);
        todoDAO.updateTodo(todo);
        response.sendRedirect("list");
    }
    private void deleteTodo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        long id = Long.parseLong(request.getParameter("id"));
        todoDAO.deleteTodo(id);
        response.sendRedirect("list");
    }

    private void handleError(HttpServletResponse response, Exception e)
            throws IOException {
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
    }
}
