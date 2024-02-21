<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.example.tododatabase.model.Todo" %> <!-- Importing Todo class -->
<%@ page import="java.text.SimpleDateFormat" %> <!-- Importing SimpleDateFormat class -->
<%@ include file="header.jsp" %> <!-- Including header.jsp file -->

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title><%= request.getAttribute("todo") != null ? "Edit Todo" : "Add Todo" %></title> <!-- Dynamic title based on whether it's editing or adding a todo -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css"> <!-- Bootstrap CSS -->
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script> <!-- Bootstrap JS -->
</head>
<body>

<div class="container">
    <h2 class="mt-5"><%= request.getAttribute("todo") != null ? "Edit Todo" : "Add Todo" %></h2> <!-- Dynamic heading based on whether it's editing or adding a todo -->
    <form action="<%= request.getContextPath() %>/todo/<%= request.getAttribute("todo") != null ? "update" : "insert" %>" method="post"> <!-- Form action depends on whether it's editing or adding -->
        <% if (request.getAttribute("todo") != null) { %>
        <input type="hidden" name="id" value="<%= ((Todo) request.getAttribute("todo")).getId() %>"/> <!-- Hidden field for todo ID if editing -->
        <input type="hidden" name="userId" value="<%= ((Todo) request.getAttribute("todo")).getUserId() %>"/> <!-- Hidden field for userId -->
        <% } %>
        <div class="form-group">
            <label for="title">Title:</label>
            <input type="text" class="form-control" id="title" name="title" value="<%= request.getAttribute("todo") != null ? ((Todo) request.getAttribute("todo")).getTitle() : "" %>" required>
        </div>
        <div class="form-group">
            <label for="description">Description:</label>
            <textarea class="form-control" id="description" name="description" required><%= request.getAttribute("todo") != null ? ((Todo) request.getAttribute("todo")).getDescription() : "" %></textarea>
        </div>
        <div class="form-group">
            <label for="targetDate">Target Date:</label>
            <input type="date" class="form-control" id="targetDate" name="targetDate" value="<%= request.getAttribute("todo") != null ? new SimpleDateFormat("yyyy-MM-dd").format(((Todo) request.getAttribute("todo")).getTargetDate()) : "" %>" required>
        </div>
        <div class="form-group form-check">
            <label class="form-check-label">
                <input class="form-check-input" type="checkbox" name="isDone" <%= request.getAttribute("todo") != null && ((Todo) request.getAttribute("todo")).isDone ? "checked" : "" %>> Done <!-- Checkbox for marking todo as done -->
            </label>
        </div>
        <button type="submit" class="btn btn-primary"><%= request.getAttribute("todo") != null ? "Update" : "Submit" %></button> <!-- Button text changes based on whether it's editing or adding -->
    </form>
</div>
</body>
</html>
