<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Fulköpings bibliotek - Hantera användare" />
<%@include file="/WEB-INF/fragment/admin/header.jsp" %>


<section class="libary-container">
  <label for="search" class="form-label">Sök på användares namn eller e-post:</label>
  <div class="d-flex align-items-center">
    <form class="d-flex w-100" role="search" method="GET" action="user-management">
      <input class="form-control me-2" type="search" name="search" id="search" placeholder="Sök användare" aria-label="Sök" value="${searchWord}">
      <button type="submit" class="my-2">Sök</button>
    </form>
  </div>

  <c:if test="${not empty searchWord}">
    <c:if test="${empty searchResults}">
      <p class="no-results">Din sökning på <strong>"${param.search}"</strong> gav inga träffar</p>
    </c:if>
  </c:if>

  <c:if test="${not empty searchResults}">
    <div class="row my-2">
      <c:forEach var="user" items="${searchResults}">
        <div class="card border border-secondary.subtle rounded-3 mb-2">
          <div class="card-body lh-sm">
            <p class="mb-1"><strong>Namn:</strong> ${user.name}</p>
            <p class="mb-1"><strong>E-post:</strong> ${user.email}</p>
              <input type="hidden" name="id" value="${user.id}" />
              <button><a href="user-management?id=${user.id}" style="color: black">Mer info</a></button>
          </div>
        </div>
      </c:forEach>
    </div>
  </c:if>
</section>


<%@include file="/WEB-INF/fragment/footer.jsp" %>