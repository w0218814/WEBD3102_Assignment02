package com.example.tododatabase.servlet;

import com.example.tododatabase.dao.TodoDAO;
import com.example.tododatabase.model.Todo;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@WebServlet("/todo")
public class TodoServlet extends HttpServlet {
    private TodoDAO todoDAO;

    @Override
    public void init() {
        todoDAO = new TodoDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String action = request.getServletPath();
            switch (action) {
                case "/todo/delete":
                    deleteTodo(request, response);
                    break;
                case "/todo/edit":
                    showEditForm(request, response);
                    break;
                case "/todo/list":
                default:
                    listTodos(request, response);
                    break;
            }
        } catch (Exception e) {
            handleError(response, e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String action = request.getServletPath();
            switch (action) {
                case "/todo/new":
                    insertTodo(request, response);
                    break;
                case "/todo/update":
                    updateTodo(request, response);
                    break;
                default:
                    listTodos(request, response);
                    break;
            }
        } catch (Exception e) {
            handleError(response, e);
        }
    }

    private void listTodos(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        try {
            List<Todo> listTodo = todoDAO.selectAllTodos();
            request.setAttribute("listTodo", listTodo);
            request.getRequestDispatcher("/WEB-INF/views/todo-list.jsp").forward(request, response);
        } catch (Exception e) {
            handleError(response, e);
        }
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            long id = Long.parseLong(request.getParameter("id"));
            Todo existingTodo = todoDAO.selectTodo(id);
            request.setAttribute("todo", existingTodo);
            request.getRequestDispatcher("/WEB-INF/views/todo-form.jsp").forward(request, response);
        } catch (Exception e) {
            handleError(response, e);
        }
    }

    private void insertTodo(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ParseException {
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String targetDateStr = request.getParameter("targetDate");
        boolean isDone = Boolean.parseBoolean(request.getParameter("isDone"));
        Date targetDate = new SimpleDateFormat("yyyy-MM-dd").parse(targetDateStr);

        Todo newTodo = new Todo(title, description, targetDate, isDone);
        todoDAO.insertTodo(newTodo);
        response.sendRedirect("list");
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
        response.sendRedirect("list");
    }

    private void deleteTodo(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        long id = Long.parseLong(request.getParameter("id"));
        todoDAO.deleteTodo(id);
        response.sendRedirect("list");
    }


    private void handleError(HttpServletResponse response, Exception e) throws IOException {
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An unexpected error occurred: " + e.getMessage());
    }
}
