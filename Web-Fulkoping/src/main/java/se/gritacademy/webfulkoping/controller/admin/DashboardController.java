package se.gritacademy.webfulkoping.controller.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import se.gritacademy.webfulkoping.model.User;
import se.gritacademy.webfulkoping.model.UserType;
import se.gritacademy.webfulkoping.util.SessionUtil;

import java.io.IOException;

@WebServlet("/dashboard")
public class DashboardController extends HttpServlet{

    /**
     * Hämtar den aktuella sessionen och kontrollerar om användaren är inloggad och har userType Admin,
     * isåfall vidarebefodras användaren till /dashboard.jsp, annars vidarebefodras begäran till /login
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
                req.getRequestDispatcher("view/admin/dashboard.jsp").forward(req, res);
            } else {
                req.getRequestDispatcher("/login").forward(req, res);
            }
        } catch(Throwable e){
            e.printStackTrace();
            req.setAttribute("message", "Ett oväntat fel har uppstått, vänligen försök igen senare!");
            req.getRequestDispatcher("view/admin/dashboard.jsp").forward(req, res);
        }
    }
}
