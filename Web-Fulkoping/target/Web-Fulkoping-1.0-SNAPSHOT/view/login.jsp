<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Fulköpings bibliotek - Logga in" />
<%@include file="/WEB-INF/fragment/user/header.jsp" %>

<section class="libary-container login">

    <div class="card mb-3 w-100 media-detail-card">
        <div class="row g-0 m-3">
            <div class="col-md-5">
                <img src="${pageContext.request.contextPath}/image/login-register-books.png" alt="Böcker" class="img-fluid">
            </div>
            <div class="col-md-7 d-flex justify-content-center">
                <div class="card-body lh-sm w-100 d-flex flex-column m-3">
                    <form action="/login" method="POST" class="w-100">
                        <div class="mb-4 w-100">
                            <label for="inputEmail" class="form-label">E-post:</label>
                            <input type="email" class="form-control w-100" id="inputEmail" name="inputEmail" required>
                        </div>
                        <div class="mb-4 w-100">
                            <label for="inputPassword" class="form-label">Lösenord:</label>
                            <input type="password" class="form-control w-100" id="inputPassword" name="inputPassword" required>
                        </div>
                        <button type="submit">Logga in</button>
                    </form>
                </div>
            </div>
        </div>
    </div>

</section>

<%@include file="/WEB-INF/fragment/footer.jsp" %>