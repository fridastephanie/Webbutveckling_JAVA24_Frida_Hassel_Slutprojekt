<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="se.gritacademy.webfulkoping.model.Media" %>
<c:set var="pageTitle" value="Fulköpings bibliotek - ${media.title}" />
<%@include file="/WEB-INF/fragment/user/header.jsp" %>

<section class="libary-container">

    <div class="card mb-3 w-100 media-detail-card">
        <div class="row g-0">
            <div class="col-md-4">
                <img src="${pageContext.request.contextPath}/image/${media.imageUrl}" alt="${media.title}" class="img-fluid rounded-start shadow">
            </div>
            <div class="col-md-8">
                <div class="card-body lh-sm">
                    <h2>${media.title}</h2>
                    <p><strong>Av:</strong> ${media.author}</p>
                    <p><strong>Genre:</strong> ${media.genre}</p>
                    <p><strong>ISBN:</strong> ${media.isbn}</p>
                    <p><strong>Beskrivning:</strong> ${media.description}</p>
                    <p><strong>Status:</strong>
                        <c:choose>
                            <c:when test="${media.isRented}">
                                Utlånad - <small><i>Förväntas åter: ${loanEndDate}</i></small>
                            </c:when>
                            <c:otherwise>
                                Ledig
                            </c:otherwise>
                        </c:choose>
                    </p>

                    <c:if test="${not empty isLoggedIn && isLoggedIn}">
                        <c:if test="${loanEndDate != null}">
                            <button disabled>Utlånad</button>
                        </c:if>
                        <c:if test="${loanEndDate == null}">
                            <form action="${pageContext.request.contextPath}/media" method="get">
                                <input type="hidden" name="id" value="${media.id}">
                                <input type="hidden" name="action" value="loan">
                                <button type="submit">Låna</button>
                            </form>
                        </c:if>
                    </c:if>
                </div>
            </div>
        </div>
        <div class="d-flex justify-content-start">
            <button style="margin-left: 15px; margin-bottom: 20px"><a href="${pageContext.request.contextPath}${previousUrl}">Tillbaka</a></button>
        </div>
    </div>

</section>

<%@include file="/WEB-INF/fragment/footer.jsp" %>