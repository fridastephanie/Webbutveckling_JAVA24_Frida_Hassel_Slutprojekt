<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/fragment/modal.jsp" %>

</main>
<footer>

</footer>
<script>
    document.addEventListener("DOMContentLoaded", function () {
        var messageModal = new bootstrap.Modal(document.getElementById("messageModal"));
        var closeModalBtn = document.getElementById("closeModalBtn");
        var modalMessage = document.getElementById("modalMessage").textContent.trim();

        if (modalMessage !== "") {
            messageModal.show();
        }

        closeModalBtn.addEventListener("click", function () {
            messageModal.hide();
            setTimeout(function () {
                var url = new URL(window.location.href);
                url.searchParams.delete("action");

                if (modalMessage.includes("Ett oväntat fel")) {
                    window.location.href = "/";
                } else {
                    window.location.href = url.toString();
                }
            }, 300);
        });
    });
</script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
</body>
</html>