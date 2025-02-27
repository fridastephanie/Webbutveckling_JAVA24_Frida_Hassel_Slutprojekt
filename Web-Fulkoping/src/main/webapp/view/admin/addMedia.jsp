<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Fulköpings bibliotek - Skapa media" />
<%@include file="/WEB-INF/fragment/admin/header.jsp" %>

<section class="libary-container">

    <div class="card mb-3 w-100 media-detail-card">
        <div class="row g-0 m-3">
            <div class="col-md-5 d-flex justify-content-center align-items-center">
                <img src="${pageContext.request.contextPath}/image/addMedia-book-stack.png" alt="Media" class="img-fluid">
            </div>
            <div class="col-md-7 d-flex justify-content-center">
                <div class="card-body lh-sm w-100 d-flex flex-column m-3">
                    <form action="/add-media" method="POST" class="w-100">
                        <div class="mb-4 w-100">
                            <label for="title" class="form-label">Titel:</label>
                            <input type="text" class="form-control" id="title" name="title" maxlength="40" required>
                        </div>
                        <div class="mb-4 w-100">
                            <label for="author" class="form-label">Författare:</label>
                            <input type="text" class="form-control" id="author" name="author" maxlength="40" required>
                        </div>
                        <div class="mb-4 w-100">
                            <label for="isbn" class="form-label">ISBN:</label>
                            <input type="text" class="form-control" id="isbn" name="isbn" maxlength="40" required>
                        </div>
                        <div class="mb-4 w-100">
                            <label for="genre" class="form-label">Genre:</label>
                            <input type="text" class="form-control" id="genre" name="genre" maxlength="40" required>
                        </div>
                        <div class="mb-4 w-100">
                            <label for="description" class="form-label">Beskrivning:</label>
                            <textarea class="form-control" id="description" name="description" style="resize: none;" maxlength="200" required></textarea>
                        </div>
                        <label for="mediaType">Mediatyp:</label>
                        <div class="d-flex gap-2 align-items-center" id="mediaType">
                            <input type="radio" name="mediaType" value="BOOK" checked> Bok
                            <input type="radio" name="mediaType" value="MAGAZINE"> Tidning
                            <input type="radio" name="mediaType" value="MOVIE"> Film
                            <input type="radio" name="mediaType" value="CD"> CD
                        </div>
                        <button type="submit" class="my-2">Skapa media</button>
                    </form>
                </div>
            </div>
        </div>
    </div>

</section>

<%@include file="/WEB-INF/fragment/footer.jsp" %>