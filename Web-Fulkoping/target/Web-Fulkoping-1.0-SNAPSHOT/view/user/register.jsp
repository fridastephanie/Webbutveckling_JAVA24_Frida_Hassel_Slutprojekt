<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Fulköpings bibliotek - Registrera konto" />
<%@include file="/WEB-INF/fragment/user/header.jsp" %>

<section class="libary-container">
    <div class="card mb-3 w-100 media-detail-card">
        <div class="row g-0 m-3">
            <div class="col-md-5">
                <img src="${pageContext.request.contextPath}/image/login-register-books.png" alt="Böcker" class="img-fluid">
            </div>
            <div class="col-md-7 d-flex justify-content-center">
                <div class="card-body lh-sm w-100 d-flex flex-column m-3">
                    <form action="/register" method="POST" class="w-100">
                        <div class="mb-4 w-100">
                            <label for="inputName" class="form-label">Namn:</label>
                            <input type="text" class="form-control flex-fill" id="inputName" name="inputName" value="${inputName != null ? inputName : ''}" maxlength="30" required>
                        </div>
                        <div class="mb-4 w-100">
                            <label for="inputEmail" class="form-label">E-post:</label>
                            <input type="email" class="form-control flex-fill" id="inputEmail" name="inputEmail" value="${inputEmail != null ? inputEmail : ''}" maxlength="50" required>
                        </div>
                        <div class="mb-4 w-100">
                            <label for="inputPassword" class="form-label">Lösenord:</label>
                            <input type="password" class="form-control flex-fill" id="inputPassword" name="inputPassword" required>
                        </div>
                        <button type="submit">Skapa konto</button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</section>


<%@include file="/WEB-INF/fragment/footer.jsp" %>