<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Fulköpings bibliotek - Startsida" />
<%@include file="/WEB-INF/fragment/user/header.jsp" %>


<section class="libary-container index">

    <div class="card mb-3 w-100">
        <div class="row g-0 align-items-center">
            <div class="col-md-5 d-flex justify-content-center">
                <img src="${pageContext.request.contextPath}/image/index-libary.jpg"
                     alt="Holding books"
                     class="img-fluid rounded-circle shadow image-custom" style="max-width: 400px">
            </div>

            <div class="col-md-6">
                <div class="card-body lh-sm">
                    <h2>Välkommen!</h2>
                    <p>Upptäck en värld av böcker, filmer, tidningar och musik – allt samlat på ett och samma ställe!</p>
                    <p>Hos oss kan du låna, inspireras och utforska både klassiker och nyheter.</p>
                    <p>Oavsett om du vill dyka ner i en spännande roman, hitta fakta för dina studier eller bara koppla av med en bra film, så har vi något för dig.</p>
                    <p>För att kunna låna media, måste du antingen <a href="/login">logga in</a> eller <a href="/register">skapa ett konto</a> först.</p>
                </div>
            </div>
        </div>
    </div>

</section>

<%@include file="/WEB-INF/fragment/footer.jsp" %>
