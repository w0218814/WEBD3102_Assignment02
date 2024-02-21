package com.example.tododatabase.servlet;

import com.example.tododatabase.dao.TodoDAO;
import com.example.tododatabase.dao.UserDAO;
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

// This is the servlet definition and mapping for all /todo/* URLs
@WebServlet(name = "TodoServlet", urlPatterns = {"/todo/*"})
public class TodoServlet extends HttpServlet {
    // DAOs are initialized in the init() method below
    private TodoDAO todoDAO;
    private UserDAO userDAO; // Added UserDAO for admin user management functionality
    private final static Logger LOGGER = Logger.getLogger(TodoServlet.class.getName());

    // This method initializes the DAOs needed for this servlet
    @Override
    public void init() {
        this.todoDAO = new TodoDAO();
        this.userDAO = new UserDAO(); // Initialize UserDAO
    }

    // doGet handles GET requests to the /todo/* URL patterns
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

    // doPost handles POST requests to the /todo/* URL patterns
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

    // Shows the form to add a new todo item
    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/todo-form.jsp").forward(request, response);
    }

    // Shows the form to edit an existing todo item
    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        long id = Long.parseLong(request.getParameter("id"));
        long userId = Long.parseLong(request.getParameter("userId")); // Retrieve userId from request
        Todo existingTodo = todoDAO.selectTodo(id, userId);
        request.setAttribute("todo", existingTodo);
        request.setAttribute("userId", userId); // Set userId in request for the form
        request.getRequestDispatcher("/WEB-INF/views/todo-form.jsp").forward(request, response);
    }


    // Lists all todos for the current user or for the user selected by admin
    private void listTodos(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");
        Long userId = currentUser.getId(); // Default to the current user

        // Check if the user is admin and a userId parameter is provided
        if ("admin".equalsIgnoreCase(currentUser.getRoleName())) {
            if (request.getParameter("userId") != null) {
                try {
                    userId = Long.parseLong(request.getParameter("userId"));
                } catch (NumberFormatException e) {
                    LOGGER.log(Level.WARNING, "Invalid user ID provided by admin", e);
                    // Optionally redirect to an error page or set an error message
                }
            }
            // Modification: Fetch all users for admin
            List<User> allUsers = userDAO.selectAllUsers(); // Fetch all users for admin
            request.setAttribute("allUsers", allUsers);
            ; // Set list of users for admin
        }
        List<Todo> listTodo = todoDAO.selectAllTodos(userId); // Ensure selectAllTodos method exists in TodoDAO
        request.setAttribute("listTodo", listTodo);
        request.getRequestDispatcher("/WEB-INF/views/todo-list.jsp").forward(request, response);
    }

    // Processes the insertion of a new todo item
    private void insertTodo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        // Extract todo details from request parameters
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        Date targetDate = null;
        try {
            targetDate = new SimpleDateFormat("yyyy-MM-dd").parse(request.getParameter("targetDate"));
        } catch (ParseException e) {
            LOGGER.log(Level.SEVERE, "Error parsing date", e);
        }

        boolean isDone = request.getParameter("isDone") != null;
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");
        long userId = currentUser.getId();

        // Create a new Todo object and insert it into the database
        Todo newTodo = new Todo(0, userId, title, description, targetDate, isDone);
        todoDAO.insertTodo(newTodo);
        response.sendRedirect(request.getContextPath() + "/todo/list");
    }

    // Processes the update of an existing todo item
    private void updateTodo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        long id = Long.parseLong(request.getParameter("id"));
        // Fetch the userId from the form instead of the session
        long userId = Long.parseLong(request.getParameter("userId"));
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        Date targetDate = null;
        try {
            targetDate = new SimpleDateFormat("yyyy-MM-dd").parse(request.getParameter("targetDate"));
        } catch (ParseException e) {
            LOGGER.log(Level.SEVERE, "Error parsing date", e);
            // Consider redirecting to an error page or showing an error message
        }

        // Check if the isDone checkbox was checked on the form
        boolean isDone = "on".equals(request.getParameter("isDone"));

        // Create a new Todo object with the information from the form
        Todo todo = new Todo(id, userId, title, description, targetDate, isDone);

        // Update the todo in the database
        boolean updated = todoDAO.updateTodo(todo);
        if (updated) {
            // Redirect to the list of todos, potentially for the specific user you just edited
            response.sendRedirect(request.getContextPath() + "/todo/list?userId=" + userId);
        } else {
            // If the update fails, log the error and potentially redirect to an error page
            LOGGER.log(Level.SEVERE, "Failed to update the todo item with id: " + id);
            // You could pass the error as a query parameter or as a session attribute
            response.sendRedirect(request.getContextPath() + "/todo/edit?id=" + id + "&error=Update failed");
        }
    }
    // Processes the deletion of an existing todo item
    private void deleteTodo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        long id = Long.parseLong(request.getParameter("id"));
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");
        long userId = currentUser.getId();

        boolean deleted = todoDAO.deleteTodo(id, userId);
        if (deleted) {
            response.sendRedirect(request.getContextPath() + "/todo/list");
        } else {
            LOGGER.log(Level.SEVERE, "Failed to delete the todo item with id: " + id);
            response.sendRedirect(request.getContextPath() + "/todo/list?error=Delete failed");
        }
    }
}
