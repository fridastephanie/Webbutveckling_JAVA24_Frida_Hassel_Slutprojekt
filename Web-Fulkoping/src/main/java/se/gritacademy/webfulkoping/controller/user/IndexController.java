package se.gritacademy.webfulkoping.controller.user;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import se.gritacademy.webfulkoping.model.User;
import se.gritacademy.webfulkoping.model.UserType;
import se.gritacademy.webfulkoping.util.SessionUtil;

import java.io.IOException;

@WebServlet("/")
public class IndexController extends HttpServlet {

    /**
     * Hämtar den aktuella sessionen och kontrollerar om användaren är inloggad, samt om användaren har userType Admin,
     * isåfall vidarebefodras användaren till /dashboard, annars vidarebefodras begäran till index.jsp
     *
     * @param req an {@link HttpServletRequest} object that contains the request the client has made of the servlet
     * @param res an {@link HttpServletResponse} object that contains the response the servlet sends to the client
     *
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {
            User user = SessionUtil.checkUserSession(req);

            if (user != null && user.getUserType() == UserType.ADMIN) {
                res.sendRedirect(req.getContextPath() + "/dashboard");
            } else {
                req.getRequestDispatcher("view/user/index.jsp").forward(req, res);
            }
        } catch(Throwable e){
            e.printStackTrace();
            req.setAttribute("message", "Ett oväntat fel har uppstått, vänligen försök igen senare!");
            req.getRequestDispatcher("view/user/index.jsp").forward(req, res);
        }
    }
}