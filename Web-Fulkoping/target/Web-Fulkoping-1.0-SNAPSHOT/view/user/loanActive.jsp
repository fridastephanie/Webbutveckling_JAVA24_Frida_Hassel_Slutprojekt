<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Fulköpings bibliotek - Aktiva lån" />
<%@include file="/WEB-INF/fragment/user/header.jsp" %>


<section class="libary-container">

    <h2>${loanTitle}</h2>

    <c:if test="${empty loans}">
        <p>Du har inga pågående lån</p>
    </c:if>
    <div class="row">
        <c:forEach var="loan" items="${loans}">
            <div class="card border border-secondary.subtle rounded-3 mb-2">
                <div class="card-body lh-sm">
                    <p class="mb-1"><strong>Titel:</strong> ${loan.media.title}</p>
                    <p class="mb-1"><strong>Författare:</strong> ${loan.media.author}</p>
                    <p class="mb-1"><strong>Lånades:</strong> ${loan.startDate}</p>
                    <p class="mb-1"><strong>Återlämnas senast:</strong> ${loan.endDate}</p>
                    <form action="${pageContext.request.contextPath}/active-loans" method="post" class="mt-2">
                        <input type="hidden" name="loanId" value="${loan.id}">
                        <button type="submit" class="btn-sm">Återlämna</button>
                    </form>
                </div>
            </div>
        </c:forEach>
    </div>

</section>

<%@include file="/WEB-INF/fragment/footer.jsp" %>
