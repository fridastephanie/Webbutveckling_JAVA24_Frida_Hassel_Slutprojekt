<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.List, se.gritacademy.webfulkoping.model.Media" %>
<c:set var="pageTitle" value="Fulköpings bibliotek - Användardetaljer" />
<%@include file="/WEB-INF/fragment/admin/header.jsp" %>


<section class="libary-container">
    <div class="container mt-4">
        <div class="d-flex flex-wrap gap-4">

            <div class="flex-grow-1 p-3 border rounded" style="flex-basis: 30%; min-width: 300px;">
                <h2>Användare</h2>
                <form method="POST" action="${pageContext.request.contextPath}/user-management?id=${user.id}">
                <label for="name">Namn:</label>
                    <input type="text" id="name" name="name" value="${user.name}" required>
                    <button type="submit" class="my-2">Ändra namn</button>
                </form>
                <form method="POST" action="${pageContext.request.contextPath}/user-management?id=${user.id}">
                    <label for="email">E-post:</label>
                    <input type="email" id="email" name="email" value="${user.email}" required>
                    <button type="submit" class="my-2">Ändra e-post</button>
                </form>
                <form method="POST" action="${pageContext.request.contextPath}/user-management?id=${user.id}">
                    <label for="password">Lösenord:</label>
                    <input type="password" id="password" name="password" placeholder="Ange nytt lösenord" required>
                    <button type="submit" class="my-2">Ändra lösenord</button>
                </form>
                <form method="POST" action="${pageContext.request.contextPath}/user-management?id=${user.id}">
                    <label for="userType">Användartyp:</label>
                    <div class="d-flex gap-2 align-items-center" id="userType">
                        <input type="radio" name="userType" value="USER" ${user.userType == 'USER' ? 'checked' : ''}> User
                        <input type="radio" name="userType" value="ADMIN" ${user.userType == 'ADMIN' ? 'checked' : ''}> Admin
                    </div>
                    <button type="submit" class="my-1">Ändra behörighet</button>
                </form>
            </div>

            <div class="flex-grow-1 p-3 border rounded" style="flex-basis: 30%; min-width: 300px;">
                <h2>Aktiva lån</h2>
                <c:if test="${empty activeLoans}">
                    <p>Inga aktiva lån</p>
                </c:if>
                <div class="row">
                    <c:forEach var="loan" items="${activeLoans}">
                        <div class="col-12 mb-3">
                            <div class="card border border-secondary.subtle rounded-3 d-flex">
                                <div class="card-body lh-sm flex-grow-1">
                                    <p class="mb-1"><strong>Titel:</strong> ${loan.media.title}</p>
                                    <p class="mb-1"><strong>Lånades:</strong> ${loan.startDate}</p>
                                    <p class="mb-1"><strong>Åter senast:</strong> ${loan.endDate}</p>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>

            <div class="flex-grow-1 p-3 border rounded" style="flex-basis: 30%; min-width: 300px;">
                <h2>Lånehistorik</h2>
                <c:if test="${empty loanHistory}">
                    <p>Ingen lånehistorik</p>
                </c:if>
                <div class="row">
                    <c:forEach var="loan" items="${loanHistory}">
                        <div class="col-12 mb-3">
                            <div class="card border border-secondary.subtle rounded-3 d-flex">
                                <div class="card-body lh-sm flex-grow-1">
                                    <p class="mb-1"><strong>Titel:</strong> ${loan.media.title}</p>
                                    <p class="mb-1"><strong>Lånades:</strong> ${loan.startDate}</p>
                                    <p class="mb-1"><strong>Återlämnades:</strong> ${loan.returnedDate}</p>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </div>
        <div class="d-flex justify-content-start">
            <button style="margin:15px"><a href="${pageContext.request.contextPath}${previousUrl}">Tillbaka</a></button>
        </div>
    </div>
</section>

<%@include file="/WEB-INF/fragment/footer.jsp" %>

