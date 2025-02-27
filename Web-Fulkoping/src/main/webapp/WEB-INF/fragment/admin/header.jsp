<!DOCTYPE html>
<html>
<head>
    <%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <title>${pageTitle}</title>
    <link rel="icon" href="${pageContext.request.contextPath}/image/favicon.png" type="image/png">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Orbitron:wght@400..900&display=swap" rel="stylesheet">

</head>

<body>

<section class="user-info-container">
    <c:if test="${not empty isLoggedIn && isLoggedIn}">
        <p>Hej, ${username}!</p>
        <a href="/logout">
            <button>Logga ut</button>
        </a>
    </c:if>
</section>

<main>
    <header>
        <%= request.getContextPath() %>
        <h1>Fulköpings bibliotek<img src="${pageContext.request.contextPath}/image/header-girl-reading.gif" alt="Girl reading book" width="190" height="210" style="margin: 10px;"></h1>
    </header>
<%@include file="/WEB-INF/fragment/admin/navbar.jsp" %>