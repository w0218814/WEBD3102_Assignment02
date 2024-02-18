<%@ page import="com.example.tododatabase.model.User" %>
<%@ page session="true" %>

<%-- Check if the user is logged in --%>
<%
    User loggedInUser = (User) session.getAttribute("user");
    if (loggedInUser != null) {
%>
<div class="user-info-header">
    <p>Welcome, <%= loggedInUser.getFullName() %></p> <!-- Assuming getName() is the correct method -->
    <a href="<%= request.getContextPath() %>/logout" class="btn btn-warning">Logout</a>
</div>
<%
    } else {
        // If the user is not logged in, redirect to the login page
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
%>
