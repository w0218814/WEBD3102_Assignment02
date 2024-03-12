<%@ page import="com.example.orderdatabase.model.User" %>
<%@ page session="true" %>

<%
    // Debug: Check if the session and user object exist
    System.out.println("Checking session in header.jsp");
    User loggedInUser = (User) session.getAttribute("user");
    if (loggedInUser != null) {
        // Debug: Print user details to console
        System.out.println("User in header: " + loggedInUser.getUsername() + ", Role ID: " + loggedInUser.getRoleId());
%>
<!-- Styles and navigation for logged-in users -->
<style>
    /* Style for the navigation bar */
    .navbar {
        background-color: #f8f9fa;
        padding: 10px;
        display: flex;
        justify-content: space-between;
        align-items: center;
    }
    .navbar a {
        text-decoration: none;
        margin: 0 10px;
        color: #007bff;
    }
    .user-info-header {
        text-align: right;
    }
</style>

<div class="navbar">
    <div>
        <a href="<%= request.getContextPath() %>/">Home</a>
        <% if (loggedInUser.getRoleId() == 1) { %>
        <a href="<%= request.getContextPath() %>/admin/listUsers">User Management</a>
        <a href="<%= request.getContextPath() %>/admin/adminConsole">Admin Console</a>
        <% } else if (loggedInUser.getRoleId() == 2) { %>
        <a href="<%= request.getContextPath() %>/product/list">Products</a>
        <% } %>
    </div>
    <div class="user-info-header">
        <p>Welcome, <%= loggedInUser.getFullName() %></p>
        <a href="<%= request.getContextPath() %>/user/logout" class="btn btn-warning">Logout</a>
    </div>
</div>
<%
} else {
    // Debug: Print a message if the user object is not found in the session
    System.out.println("No user in session");
%>
<div class="login-reminder">
    <p>Please <a href="<%=request.getContextPath()%>/login">login</a> to view more content.</p>
</div>
<%
    }
%>
