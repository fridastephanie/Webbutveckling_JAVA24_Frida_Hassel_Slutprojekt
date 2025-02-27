package se.gritacademy.webfulkoping.controller.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import se.gritacademy.webfulkoping.dao.LoanDAO;
import se.gritacademy.webfulkoping.dao.UserDAO;
import se.gritacademy.webfulkoping.model.Loan;
import se.gritacademy.webfulkoping.model.User;
import se.gritacademy.webfulkoping.model.UserType;
import se.gritacademy.webfulkoping.util.SessionUtil;

import java.io.IOException;
import java.util.List;

@WebServlet("/user-management")
public class UserManagementController extends HttpServlet {
    //UserDAO objekt för att kunna hantera User-objekt från databasen
    private UserDAO userDAO = new UserDAO();
    //LoanDAO objekt för att kunna hantera Loan-objekt från databasen
    private LoanDAO loanDAO = new LoanDAO();

    /**
     *
     * Kontrollerar om användaren är inloggad samt om användartypen är Admin (annars skickas användaren till login),
     * annars kontrolleras om användaren ska kolla på sökresultatet i av användare i en lista
     * eller en användardetalj-sida (userDetail.jsp)
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
            String searchedUserId = req.getParameter("id");

            if (user == null || user.getUserType() != UserType.ADMIN) {
                res.sendRedirect(req.getContextPath() + "/login");
                return;
            }
            if (searchedUserId != null && !searchedUserId.isEmpty()) {
                showUserDetails(req, res, searchedUserId);
            } else {
                showUserList(req, res);
            }
        } catch(Throwable e){
            e.printStackTrace();
            req.setAttribute("message", "Ett oväntat fel har uppstått, vänligen försök igen senare!");
            req.getRequestDispatcher("view/admin/dashboard.jsp").forward(req, res);
        }
    }

    /**
     * Kontrollerar att det finns en inloggad användare och att den är Admin, annars omdirrigeras användaren till /login.
     * Annars hämtas id från användaren som sökts upp och ska uppdateras, därefter uppdateras de fält som användaren valt att
     * uppdatera samt uppdateras userDetail sidan.
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
            User loggedInUser = SessionUtil.checkUserSession(req);
            if (loggedInUser != null && loggedInUser.getUserType() == UserType.ADMIN) {

                int userId = Integer.parseInt(req.getParameter("id"));
                User userToUpdate = userDAO.findById(userId);
                if (userToUpdate == null) {
                    req.setAttribute("message", "Användaren hittades inte!");
                    req.getRequestDispatcher("view/admin/user-management.jsp").forward(req, res);
                    return;
                }

                updateName(req, userToUpdate, res);
                updateEmail(req, userToUpdate, res);
                updatePassword(req, userToUpdate, res);
                updateUserType(req, userToUpdate, res);
                userDAO.update(userToUpdate);

                if (loggedInUser.getId() == userToUpdate.getId()) {
                    req.getSession().setAttribute("user", userToUpdate);
                }
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
     * Uppdaterar användarens namn om det är giltigt.
     * @param req - HTTP-requesten som skickas från användarens webbläsare till servern
     * @param userToUpdate - User-objektet som ska uppdateras
     * @param res - HTTP-responsen som servern skickar tillbaka till användarens webbläsare
     */
    private void updateName(HttpServletRequest req, User userToUpdate, HttpServletResponse res) throws ServletException, IOException {
        String newName = req.getParameter("name");
        if (newName != null && !newName.trim().isEmpty()) {
            userToUpdate.setName(newName);
            req.setAttribute("message", "Namnet har uppdaterats!");
            req.getRequestDispatcher("view/admin/userDetail.jsp").forward(req, res);
        }
    }

    /**
     * Uppdaterar användarens e-postadress och kontrollerar att den är unik och har korrekt format.
     *
     * @param req - HTTP-requesten som skickas från användarens webbläsare till servern
     * @param userToUpdate - User-objektet som ska uppdateras
     * @param res - HTTP-responsen som servern skickar tillbaka till användarens webbläsare
     * @throws ServletException
     * @throws IOException
     */
    private void updateEmail(HttpServletRequest req, User userToUpdate, HttpServletResponse res) throws ServletException, IOException {
        String newEmail = req.getParameter("email");
        if (newEmail != null && !newEmail.trim().isEmpty() && !newEmail.equals(userToUpdate.getEmail())) {
               User existingUser = userDAO.getUserByEmail(newEmail);
            if (existingUser != null) {
                req.setAttribute("message", "E-postadressen används redan av en annan användare!");
                req.getRequestDispatcher("view/admin/userDetail.jsp").forward(req, res);
                return;
            }
            String emailFormatError = validateEmailFormat(newEmail);
            if (emailFormatError != null) {
                req.setAttribute("message", emailFormatError);
                req.getRequestDispatcher("view/admin/userDetail.jsp").forward(req, res);
                return;
            }
            userToUpdate.setEmail(newEmail);
            req.setAttribute("message", "E-postadressen har uppdaterats!");
            req.getRequestDispatcher("view/admin/userDetail.jsp").forward(req, res);
        }
    }

    /**
     * Uppdaterar användarens lösenord om det är giltigt
     *
     * @param req - HTTP-requesten som skickas från användarens webbläsare till servern
     * @param userToUpdate - User-objektet som ska uppdateras
     * @param res - HTTP-responsen som servern skickar tillbaka till användarens webbläsare
     */
    private void updatePassword(HttpServletRequest req, User userToUpdate, HttpServletResponse res) throws ServletException, IOException {
        String newPassword = req.getParameter("password");
        if (newPassword != null && !newPassword.trim().isEmpty()) {
            userToUpdate.setPassword(newPassword);
            req.setAttribute("message", "Lösenordet har uppdaterats!");
            req.getRequestDispatcher("view/admin/userDetail.jsp").forward(req, res);
        }
    }

    /**
     * Uppdaterar användartypen om den är giltig
     *
     * @param req - HTTP-requesten som skickas från användarens webbläsare till servern
     * @param userToUpdate - User-objektet som ska uppdateras
     * @param res - HTTP-responsen som servern skickar tillbaka till användarens webbläsare
     */
    private void updateUserType(HttpServletRequest req, User userToUpdate, HttpServletResponse res) throws ServletException, IOException {
        String userTypeParam = req.getParameter("userType");
        if (userTypeParam != null) {
            userToUpdate.setUserType(UserType.valueOf(userTypeParam));
            req.setAttribute("message", "Behörigheten har uppdaterats!");
            req.getRequestDispatcher("view/admin/userDetail.jsp").forward(req, res);
        }
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
     * Visar detaljer för ett specifikt User-objekt och sparar den föregående URL:en från sessionen
     *
     * @param req - HTTP-requesten som skickas från användarens webbläsare till servern
     * @param res - HTTP-responsen som servern skickar tillbaka till användarens webbläsare
     * @param searchedUserId - Den uppsökta användarens id som vi vill se detaljerna om
     * @throws ServletException
     * @throws IOException
     */
    private void showUserDetails(HttpServletRequest req, HttpServletResponse res, String searchedUserId) throws ServletException, IOException {
        User searchedUser = userDAO.findById(Integer.parseInt(searchedUserId));
        if (searchedUser == null) {
            res.sendRedirect(req.getContextPath() + "/user-management");
            return;
        }

        String previousUrl = (String) req.getSession().getAttribute("previousUrl");
        req.setAttribute("previousUrl", previousUrl);

        req.setAttribute("user", searchedUser);
        List<Loan> activeLoans = loanDAO.findActiveLoansByUserId(searchedUser.getId());
        List<Loan> loanHistory = loanDAO.findLoanHistoryByUserId(searchedUser.getId());

        req.setAttribute("activeLoans", activeLoans);
        req.setAttribute("loanHistory", loanHistory);
        req.getRequestDispatcher("view/admin/userDetail.jsp").forward(req, res);
    }

    /**
     * Visar en lista av User-objekt baserat på sökordet, samt sparar den aktuella URL:en i sessionen
     *
     * @param req - HTTP-requesten som skickas från användarens webbläsare till servern
     * @param res - HTTP-responsen som servern skickar tillbaka till användarens webbläsare
     */
    private void showUserList(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String searchWord = req.getParameter("search");

        if (searchWord != null && !searchWord.trim().isEmpty()) {
            List<User> searchResults = userDAO.searchUsersByNameOrEmail(searchWord);
            req.setAttribute("searchResults", searchResults);
            req.setAttribute("searchWord", searchWord);
        }

        String currentUrl = req.getRequestURI();
        String queryString = req.getQueryString();
        String fullUrl = (queryString != null) ? currentUrl + "?" + queryString : currentUrl;
        req.getSession().setAttribute("previousUrl", fullUrl);

        req.getRequestDispatcher("view/admin/userManagement.jsp").forward(req, res);
    }
}

