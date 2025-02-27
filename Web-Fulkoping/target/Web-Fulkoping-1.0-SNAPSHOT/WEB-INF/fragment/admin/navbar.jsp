<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<nav class="navbar navbar-expand-md bg-body-tertiary rounded">
  <div class="container-fluid d-flex flex-nowrap align-items-center">

    <button class="navbar-toggler me-2" type="button" data-bs-toggle="offcanvas" data-bs-target="#navbarOffcanvasSm"
            aria-controls="navbarOffcanvasSm" aria-expanded="false" aria-label="Toggle navigation">
      <span class="navbar-toggler-icon"></span>
    </button>

    <div class="offcanvas offcanvas-start" style="width: auto;" tabindex="-1" id="navbarOffcanvasSm" aria-labelledby="navbarOffcanvasSmLabel">
      <div class="offcanvas-header">
        <button type="button" class="btn-close" data-bs-dismiss="offcanvas" aria-label="Close"></button>
      </div>
      <div class="offcanvas-body" style="padding-right: 30px;">
        <ul class="navbar-nav me-auto mb-2 mb-sm-0">
          <li class="nav-item"><a class="nav-link" href="/dashboard">Hem</a></li>
          <li class="nav-item"><a class="nav-link" href="/add-media">Skapa media</a></li>
          <li class="nav-item"><a class="nav-link" href="/add-user">Skapa användare</a></li>
          <li class="nav-item"><a class="nav-link" href="/user-management">Hantera användare</a></li>
        </ul>
      </div>
    </div>
  </div>
</nav>