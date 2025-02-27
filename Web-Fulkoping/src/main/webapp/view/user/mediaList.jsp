<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.List, se.gritacademy.webfulkoping.model.Media" %>
<c:set var="pageTitle" value="Fulköpings bibliotek - Produktlista" />
<%@include file="/WEB-INF/fragment/user/header.jsp" %>


<section class="libary-container">
    <c:choose>
        <c:when test="${not empty param.search && empty mediaList}">
            <p class="no-results">Din sökning på <strong>"${param.search}"</strong> gav inga träffar</p>
        </c:when>
        <c:otherwise>
            <div class="row">
                <c:forEach var="media" items="${mediaList}">
                    <div class="col-12 col-md-4 col-lg-3">
                        <div class="card media-card">
                            <img src="${pageContext.request.contextPath}/image/${media.imageUrl}" class="card-img-top" alt="${media.title}">
                            <div class="card-body">
                                <h5 class="card-title">${media.title}</h5>
                                <p class="card-text">Av: ${media.author}</p>
                                <button><a href="media?id=${media.id}" style="color: black">Mer info</a></button>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:otherwise>
    </c:choose>
</section>

<%@include file="/WEB-INF/fragment/footer.jsp" %>
