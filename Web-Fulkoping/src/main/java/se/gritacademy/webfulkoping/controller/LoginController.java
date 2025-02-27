package se.gritacademy.webfulkoping.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import se.gritacademy.webfulkoping.dao.UserDAO;
import se.gritacademy.webfulkoping.model.User;
import se.gritacademy.webfulkoping.model.UserType;
import se.gritacademy.webfulkoping.util.HashingPasswordUtil;
import se.gritacademy.webfulkoping.util.SessionUtil;

import java.io.IOException;

@WebServlet("/login")
public class LoginController extends HttpServlet {
    //UserDAO objekt för att kommunicera med databasen
    private UserDAO userDAO = new UserDAO();

    /**
     * Hämtar den aktuella sessionen och kontrollerar om användaren redan är inloggad,
     * isåfall skickas användaren till indexsidan(/)
     * Om användaren inte är inloggad skickas begäran vidare till login.jsp
     *
     * @param req an {@link HttpServletRequest} object that contains the request the client has made of the servlet
     * @param res an {@link HttpServletResponse} object that contains the response the servlet sends to the client
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {
            if (SessionUtil.checkUserSession(req) != null) {
                res.sendRedirect(req.getContextPath() + "/");
                return;
            }
            req.getRequestDispatcher("view/login.jsp").forward(req, res);
        } catch(Throwable e){
            req.setAttribute("message", "Ett fel uppstod: " + e.getCause());
            req.getRequestDispatcher("view/user/index.jsp").forward(req, res);
        }
    }

    /**
     * Hämtar användarens inmatade e-post och lösenord och kontrollerar om det stämmer med databasen,
     * isåfall setts attributen för sessionens User-objekt, därefter kontrolleras om avändaren
     * är av typen admin eller vanlig user och skickas vidsare till rätt view som inloggad.
     * Om användaren inte hittas i databasen, skickas en popup ruta till användaren om felaktiga inloggningsuppgifter
     *
     * @param req an {@link HttpServletRequest} object that contains the request the client has made of the servlet
     * @param res an {@link HttpServletResponse} object that contains the response the servlet sends to the client
     *
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {
            String email = req.getParameter("inputEmail");
            String password = req.getParameter("inputPassword");
            User user = authenticateUser(email, password);

            if (user != null) {
                loginUser(req, user);
                if (user.getUserType() == UserType.ADMIN) {
                    res.sendRedirect(req.getContextPath() + "/dashboard");
                } else {
                    res.sendRedirect(req.getContextPath() + "/");
                }
            } else {
                showLoginError(req, res);
            }
        } catch(Throwable e){
            e.printStackTrace();
            req.setAttribute("message", "Ett oväntat fel har uppstått, vänligen försök igen senare!");
            req.getRequestDispatcher("view/user/index.jsp").forward(req, res);
        }
    }
    /**
     * Autentiserar användaren genom att hämta användaren från databasen och verifiera lösenordet
     *
     * @param email - Angiven e-post
     * @param password - Angivet lösenord
     * @return - User-objektet om autentisering lyckas, annars null
     */
    private User authenticateUser(String email, String password) {
        User user = userDAO.getUserByEmail(email);
        return (user != null && HashingPasswordUtil.verifyPassword(password, user.getPassword())) ? user : null;
    }

    /**
     * Loggar in användaren genom att spara användaren i sessionen och uppdatera attribut
     *
     * @param req - HTTP-requesten som skickas från användarens webbläsare till servern
     * @param user - Inloggade användaren
     */
    private void loginUser(HttpServletRequest req, User user) {
        HttpSession session = req.getSession();
        session.setAttribute("user", user);
        SessionUtil.checkUserSession(req);
    }

    /**
     * Visar ett felmeddelande för användaren via en popup om inloggningen misslyckas
     *
     * @param req - HTTP-requesten som skickas från användarens webbläsare till servern
     * @param res - HTTP-responsen som servern skickar tillbaka till användarens webbläsare
     * @throws ServletException
     * @throws IOException
     */
    private void showLoginError(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.setAttribute("message", "Felaktigt användarnamn eller lösenord!");
        req.getRequestDispatcher("view/login.jsp").forward(req, res);
    }
}