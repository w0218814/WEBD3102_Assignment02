<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.example.tododatabase.model.Todo" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ include file="header.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title><%= request.getAttribute("todo") != null ? "Edit Todo" : "Add Todo" %></title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</head>
<body>

<div class="container">
    <h2 class="mt-5"><%= request.getAttribute("todo") != null ? "Edit Todo" : "Add Todo" %></h2>
    <form action="<%= request.getContextPath() %>/todo/<%= request.getAttribute("todo") != null ? "update" : "insert" %>" method="post">
        <input type="hidden" name="id" value="<%= request.getAttribute("todo") != null ? ((Todo) request.getAttribute("todo")).getId() : "" %>"/>
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
                <input class="form-check-input" type="checkbox" name="isDone" <%= request.getAttribute("todo") != null && ((Todo) request.getAttribute("todo")).isDone ? "checked" : "" %>> Done
            </label>
        </div>
        <button type="submit" class="btn btn-primary"><%= request.getAttribute("todo") != null ? "Update" : "Submit" %></button>
    </form>
</div>
</body>
</html>
