package com.example.orderdatabase.servlet;

import com.example.orderdatabase.dao.UserDAO;
import com.example.orderdatabase.model.User;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "AdminServlet", urlPatterns = {"/admin/*"})
public class AdminServlet extends HttpServlet {
    private UserDAO userDAO;

    @Override
    public void init() {
        userDAO = new UserDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getPathInfo();
        try {
            if (action == null) {
                action = "/listUsers"; // default action
            }
            switch (action) {
                case "/listUsers":
                    showUserList(request, response);
                    break;
                case "/editUser":
                    editUser(request, response);
                    break;
                case "/adminConsole":
                    request.getRequestDispatcher("/WEB-INF/views/adminConsole.jsp").forward(request, response);
                    break;
                // Additional GET actions as necessary
            }
        } catch (SQLException ex) {
            throw new ServletException("Database error occurred", ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getPathInfo();
        try {
            switch (action) {
                case "/register":
                    insertOrUpdateUser(request, response);
                    break;
                // Potential for more POST actions as necessary
            }
        } catch (SQLException ex) {
            throw new ServletException("Database error occurred", ex);
        }
    }
    private void showUserList(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        List<User> listUser = userDAO.selectAllUsers();
        request.setAttribute("allUsers", listUser);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/adminRegister.jsp");
        dispatcher.forward(request, response);
    }

    private void editUser(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        String userIdStr = request.getParameter("userId");
        if (userIdStr != null && !userIdStr.isEmpty()) {
            try {
                long userId = Long.parseLong(userIdStr);
                User user = userDAO.selectUserById(userId);
                request.setAttribute("user", user);
            } catch (NumberFormatException e) {
                // Log error or redirect as necessary
            }
        }
        showUserList(request, response); // Assuming you're using the same JSP for listing and editing
    }

    private void insertOrUpdateUser(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        String idStr = request.getParameter("id");
        long id = 0;
        boolean idProvided = false;
        if (idStr != null && !idStr.isEmpty()) {
            try {
                id = Long.parseLong(idStr);
                idProvided = true;
            } catch (NumberFormatException e) {
                // Log or handle the error as necessary. Consider sending back an error message or redirecting to an error page.
            }
        }

        // Extract user details from the request.
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        // Extract additional fields.
        String street = request.getParameter("street");
        String city = request.getParameter("city");
        String nearbyLandmark = request.getParameter("nearbyLandmark");
        String province = request.getParameter("province");
        String postalCode = request.getParameter("postalCode");
        String phoneNumber = request.getParameter("phoneNumber");

        // Default role if parsing fails.
        int roleId = 2;
        try {
            roleId = Integer.parseInt(request.getParameter("roleId"));
        } catch (NumberFormatException e) {
            // Handle default or error case as necessary.
        }

        String hashedPassword = "";
        if (password != null && !password.trim().isEmpty()) {
            hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        }

        User user = new User();
        user.setUsername(username);
        user.setFullName(fullName);
        user.setEmail(email);
        user.setStreet(street);
        user.setCity(city);
        user.setNearbyLandmark(nearbyLandmark);
        user.setProvince(province);
        user.setPostalCode(postalCode);
        user.setPhoneNumber(phoneNumber);
        user.setRoleId(roleId);

        if (idProvided) {
            user.setId(id);
            userDAO.updateUser(user);
            if (!hashedPassword.isEmpty()) {
                userDAO.updatePassword(id, hashedPassword);
            }
        } else {
            userDAO.insertUser(user, hashedPassword, roleId);
        }

        // After inserting or updating, redirect to the list of users.
        response.sendRedirect(request.getContextPath() + "/admin/listUsers");
    }
}
