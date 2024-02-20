<%@ page import="com.example.tododatabase.model.User" %>
<%@ page session="true" %>

<%-- Check if the user is logged in --%>
<%
    User loggedInUser = (User) session.getAttribute("user");
    if (loggedInUser != null) {
%>
<!-- Inline styles for positioning and coloring -->
<style>
    .user-info-header {
        position: fixed; /* Fixed position */
        right: 0;       /* Align to the right */
        top: 0;         /* Align to the top */
        margin: 10px;   /* Margin from the top and right edges */
    }
    .user-info-header p {
        display: inline; /* Make the paragraph inline to align with the logout button */
        margin-right: 10px; /* Margin between the username and the logout button */
        font-weight: bold; /* Make text bold */
        color: blue; /* Change font color to blue */
    }
    .user-info-header a {
        text-decoration: none; /* Remove underline from the link */
    }
</style>
<div class="user-info-header">
    <p>Welcome, <%= loggedInUser.getFullName() %></p> <!-- Using getFullName() based on your assumption -->
    <a href="<%= request.getContextPath() %>/user/logout" class="btn btn-warning">Logout</a>
</div>
<%
    }
%>
