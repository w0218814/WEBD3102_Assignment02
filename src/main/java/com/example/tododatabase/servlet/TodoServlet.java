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
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/user/login");
            return;
        }

        String action = request.getPathInfo() != null ? request.getPathInfo() : "/list";

        try {
            switch (action) {
                case "/new":
                    showNewForm(request, response);
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
            throw new ServletException("Database error", ex);
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
            if ("/insert".equals(action)) {
                insertTodo(request, response);
            } else if ("/update".equals(action)) {
                updateTodo(request, response);
            } else if ("/delete".equals(action)) {
                deleteTodo(request, response);
            } else {
                listTodos(request, response);
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Database error", ex);
            throw new ServletException("Database error", ex);
        }
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/todo-form.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        long id = Long.parseLong(request.getParameter("id"));
        long userId = ((User) request.getSession().getAttribute("user")).getId();
        Todo existingTodo = todoDAO.selectTodo(id, userId);
        request.setAttribute("todo", existingTodo);
        request.getRequestDispatcher("/WEB-INF/views/todo-form.jsp").forward(request, response);
    }

    private void listTodos(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");
        List<Todo> listTodo = todoDAO.selectAllTodos(currentUser.getId());
        request.setAttribute("listTodo", listTodo);
        request.getRequestDispatcher("/WEB-INF/views/todo-list.jsp").forward(request, response);
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

        boolean isDone = request.getParameter("isDone") != null; // Correctly interpret the checkbox status
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");
        long userId = currentUser.getId();

        Todo newTodo = new Todo(0, userId, title, description, targetDate, isDone);
        todoDAO.insertTodo(newTodo);
        response.sendRedirect(request.getContextPath() + "/todo/list");
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

        boolean isDone = request.getParameter("isDone") != null; // Correctly interpret the checkbox status
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");
        long userId = currentUser.getId();

        Todo todo = new Todo(id, userId, title, description, targetDate, isDone);
        todoDAO.updateTodo(todo);
        response.sendRedirect(request.getContextPath() + "/todo/list");
    }


    private void deleteTodo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        long id = Long.parseLong(request.getParameter("id"));
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");
        long userId = currentUser.getId();

        todoDAO.deleteTodo(id, userId);
        response.sendRedirect(request.getContextPath() + "/todo/list");
    }
}