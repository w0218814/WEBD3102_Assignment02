<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>${todo != null ? 'Edit Todo' : 'Add Todo'}</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</head>
<body>

<div class="container">
    <h2 class="mt-5">${todo != null ? 'Edit Todo' : 'Add Todo'}</h2>
    <form action="${pageContext.request.contextPath}/todo/${todo != null ? 'update' : 'new'}" method="post">
        <input type="hidden" name="id" value="${todo != null ? todo.id : ''}"/>
        <div class="form-group">
            <label for="title">Title:</label>
            <input type="text" class="form-control" id="title" name="title" value="${todo != null ? todo.title : ''}" required>
        </div>
        <div class="form-group">
            <label for="description">Description:</label>
            <textarea class="form-control" id="description" name="description" required>${todo != null ? todo.description : ''}</textarea>
        </div>
        <div class="form-group">
            <label for="targetDate">Target Date:</label>
            <input type="date" class="form-control" id="targetDate" name="targetDate" value="${todo != null ? todo.targetDate : ''}" required>
        </div>
        <div class="form-group form-check">
            <label class="form-check-label">
                <input class="form-check-input" type="checkbox" name="isDone" ${todo != null && todo.isDone ? 'checked' : ''} value="true"> Done
            </label>
        </div>
        <button type="submit" class="btn btn-primary">${todo != null ? 'Update' : 'Submit'}</button>
    </form>
</div>

</body>
</html>
