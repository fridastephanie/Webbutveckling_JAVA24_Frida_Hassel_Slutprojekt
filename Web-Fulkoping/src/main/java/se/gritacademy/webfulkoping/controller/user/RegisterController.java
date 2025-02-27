package se.gritacademy.webfulkoping.controller.user;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import se.gritacademy.webfulkoping.dao.UserDAO;
import se.gritacademy.webfulkoping.model.User;
import se.gritacademy.webfulkoping.util.SessionUtil;

import java.io.IOException;

@WebServlet("/register")
public class RegisterController extends HttpServlet {
    //UserDAO objekt för att kunna hantera User-objekt från databasen
    private UserDAO userDAO = new UserDAO();

    /**
     * Hämtar den aktuella sessionen och kontrollerar om användaren redan är inloggad,
     * isåfall skickas användaren till indexsidan(/)
     * Om användaren inte är inloggad skickas begäran vidare till register.jsp
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
            req.getRequestDispatcher("view/user/register.jsp").forward(req, res);
        } catch(Throwable e){
            e.printStackTrace();
            req.setAttribute("message", "Ett oväntat fel har uppstått, vänligen försök igen senare!");
            req.getRequestDispatcher("view/user/index.jsp").forward(req, res);
        }
    }

    /**
     * Hämtar användarens inmatade namn, e-post och lösenord och kontrollerar att inget fält är tomt,
     * felaktiga format eller att e-postadressen redan finns, isåfall visas ett felmeddelande för användaren.
     * Annars skapas ett nytt User-objekt som sparas i databasen, och användaren skickas till login-sidan
     *
     * @param req an {@link HttpServletRequest} object that contains the request the client has made of the servlet     *
     * @param res an {@link HttpServletResponse} object that contains the response the servlet sends to the client
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {
            String name = req.getParameter("inputName");
            String email = req.getParameter("inputEmail");
            String password = req.getParameter("inputPassword");

            String validationError = validateFields(name, email, password);
            if (validationError != null) {
                req.setAttribute("message", validationError);
                req.getRequestDispatcher("view/user/register.jsp").forward(req, res);
                return;
            }
            String emailFormatError = validateEmailFormat(email);
            if (emailFormatError != null) {
                req.setAttribute("message", emailFormatError);
                req.getRequestDispatcher("view/user/register.jsp").forward(req, res);
                return;
            }

            User existingUser = userDAO.getUserByEmail(email);
            if (existingUser != null) {
                req.setAttribute("message", "E-postadressen finns redan registrerad!");
                req.getRequestDispatcher("view/user/register.jsp").forward(req, res);
                return;
            }

            registerUser(name, email, password);
            req.setAttribute("message", "Ditt kontot har skapats!");
            req.getRequestDispatcher("view/user/register.jsp").forward(req, res);
        } catch(Throwable e){
            e.printStackTrace();
            req.setAttribute("message", "Ett oväntat fel har uppstått, vänligen försök igen senare!");;
            req.getRequestDispatcher("view/user/index.jsp").forward(req, res);
        }
    }

    /**
     * Kontrollerar att användaren fyllt i alla uppgifter, annars returneras ett Error-meddelande
     *
     * @param name     - Användarens namn
     * @param email    - Användarens e-post
     * @param password - Användarens lösenord
     * @return - Null om allt är ifyllt, annars ett Error-meddelande
     */
    private String validateFields(String name, String email, String password) {
        if (name == null && email == null && password == null || name.isEmpty() && email.isEmpty() && password.isEmpty()) {
            return "Alla fält måste fyllas i!";
        }
        if (name == null || name.isEmpty()) {
            return "Namn måste fyllas i!";
        }
        if (email == null || email.isEmpty()) {
            return "E-post måste fyllas i!";
        }
        if (password == null || password.isEmpty()) {
            return "Lösenord måste fyllas i!";
        }
        return null;
    }

    /**
     * Kontrollerar att användarens angivna e-postadress är en korrekt e-postadress
     *
     * @param email - Användarens angivna e-postadress
     */
    private String validateEmailFormat(String email) {
        if (email == null || !email.matches("^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            return "Ogiltig e-postadress: " + email;
        }
        return null;
    }

    /**
     * Skapar ett nytt User-objekt och sparar det i databasen
     *
     * @param name     - Användarens angivna namn
     * @param email    - Användarens angivna e-post
     * @param password - Användarens angivna lösenord
     */
    private void registerUser(String name, String email, String password) {
        User newUser = new User(name, email, password);
        userDAO.create(newUser);
    }
}