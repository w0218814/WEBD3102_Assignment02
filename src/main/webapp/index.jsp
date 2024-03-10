<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%-- Forward to the servlet that handles product details --%>
<%
    response.sendRedirect(request.getContextPath() + "/product/details");
%>
