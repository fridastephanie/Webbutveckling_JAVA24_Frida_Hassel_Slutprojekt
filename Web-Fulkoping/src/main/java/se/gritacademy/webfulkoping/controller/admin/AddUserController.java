package se.gritacademy.webfulkoping.controller.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import se.gritacademy.webfulkoping.dao.UserDAO;
import se.gritacademy.webfulkoping.model.User;
import se.gritacademy.webfulkoping.model.UserType;
import se.gritacademy.webfulkoping.util.SessionUtil;

import java.io.IOException;

@WebServlet("/add-user")
public class AddUserController extends HttpServlet {
    //UserDAO objekt för att kunna hantera User-objekt från databasen
    private UserDAO userDAO = new UserDAO();

    /**
     * Hämtar den aktuella sessionen och kontrollerar om användaren är inloggad och har userType Admin,
     * isåfall vidarebefodras användaren till addUser.jsp, annars vidarebefodras begäran till /login
     *
     * @param req an {@link HttpServletRequest} object that contains the request the client has made of the servlet
     * @param res an {@link HttpServletResponse} object that contains the response the servlet sends to the client
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {
            User user = SessionUtil.checkUserSession(req);

            if (user != null && user.getUserType() == UserType.ADMIN) {
                req.getRequestDispatcher("view/admin/addUser.jsp").forward(req, res);
            } else {
                res.sendRedirect(req.getContextPath() + "/login");
            }
        } catch(Throwable e){
            e.printStackTrace();
            req.setAttribute("message", "Ett oväntat fel har uppstått, vänligen försök igen senare!");
            req.getRequestDispatcher("view/admin/dashboard.jsp").forward(req, res);
        }
    }

    /**
     * Hämtar den aktuella sessionen och kontrollerar om användaren är inloggad och har userType Admin,
     * annars vidarebefodras användaren till /login. Annars hämtas och valideras värdena från formuläret,
     * och om allt är korrekt ifyllt skapas en ny användare
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
            User user = SessionUtil.checkUserSession(req);

            if (user == null || user.getUserType() != UserType.ADMIN) {
                res.sendRedirect(req.getContextPath() + "/login");
                return;
            }

            String name = req.getParameter("inputName");
            String email = req.getParameter("inputEmail");
            String password = req.getParameter("inputPassword");
            String userTypeStr = req.getParameter("userType");

            String validationError = validateFields(name, email, password, userTypeStr);
            if (validationError != null) {
                req.setAttribute("message", validationError);
                req.getRequestDispatcher("view/admin/addUser.jsp").forward(req, res);
                return;
            }

            String emailFormatError = validateEmailFormat(email);
            if (emailFormatError != null) {
                req.setAttribute("message", emailFormatError);
                req.getRequestDispatcher("view/admin/addUser.jsp").forward(req, res);
                return;
            }

            if (userDAO.getUserByEmail(email) != null) {
                req.setAttribute("message", "E-postadressen finns redan registrerad!");
                req.getRequestDispatcher("view/admin/addUser.jsp").forward(req, res);
                return;
            }

            createNewUser(name, email, password, userTypeStr);
            req.setAttribute("message", "Användaren har skapats!");
            req.getRequestDispatcher("view/admin/addUser.jsp").forward(req, res);
        } catch(Throwable e){
            e.printStackTrace();
            req.setAttribute("message", "Ett oväntat fel har uppstått, vänligen försök igen senare!");
            req.getRequestDispatcher("view/admin/dashboard.jsp").forward(req, res);
        }
    }

    /**
     * Kontrollerar att användaren fyllt i alla uppgifter, annars returneras ett Error-meddelande
     *
     * @param name     - Användarens namn
     * @param email    - Användarens e-post
     * @param password - Användarens lösenord
     * @param userType - Användarens userType
     * @return - Null om allt är ifyllt, annars ett Error-meddelande
     */
    private String validateFields(String name, String email, String password, String userType) {
        if (name == null || name.isEmpty() || email == null || email.isEmpty() || password == null || password.isEmpty() || userType == null || userType.isEmpty()) {
            return "Alla fält måste fyllas i!";
        }
        if (!userType.equals("USER") && !userType.equals("ADMIN")) {
            return "Ogiltig användartyp!";
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
     * Skapar en ny användare med de angivna värdena från fälten
     *
     * @param name - Det angivna namnet till användaren
     * @param email - Den angivna e-postadressen till användaren
     * @param password - Det angivna lösenordet till användaren
     * @param userTypeString - Den angivna userTypen i String-format
     * @throws ServletException
     * @throws IOException
     */
    private void createNewUser(String name, String email, String password, String userTypeString) throws ServletException, IOException {
        UserType userType = UserType.valueOf(userTypeString);
        User newUser = new User(name, email, password);
        newUser.setUserType(userType);
        userDAO.create(newUser);
    }
}