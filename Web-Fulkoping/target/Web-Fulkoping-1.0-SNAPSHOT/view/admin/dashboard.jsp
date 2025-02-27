<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Fulköpings bibliotek - Startsida" />
<%@include file="/WEB-INF/fragment/admin/header.jsp" %>


<section class="libary-container index">

  <div class="card mb-3 w-100">
    <div class="row g-0 m-3 align-items-center">
      <div class="col-md-5 d-flex justify-content-center">
        <img src="${pageContext.request.contextPath}/image/index-libary.jpg"
             alt="Holding books"
             class="img-fluid rounded-circle shadow image-custom" style="max-width: 400px">
      </div>

      <div class="col-md-6">
        <div class="card-body lh-sm">
          <h2>Adminsidan</h2>
          <p>Som administratör har du möjlighet att skapa nya användare samt lägga till ny media i biblioteket.</p>
          <p>Dessutom kan du söka upp och redigera befintliga användares uppgifter, samt se deras aktiva lån och lånehistorik.</p>
          <p>Om du vill låna något media måste du skapa ett nytt vanligt användarkonto för dig själv.</p>
        </div>
      </div>
    </div>
  </div>

</section>

<%@include file="/WEB-INF/fragment/footer.jsp" %>