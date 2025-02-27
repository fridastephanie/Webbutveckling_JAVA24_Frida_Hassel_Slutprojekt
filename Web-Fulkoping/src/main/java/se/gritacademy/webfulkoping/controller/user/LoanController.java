package se.gritacademy.webfulkoping.controller.user;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import se.gritacademy.webfulkoping.dao.LoanDAO;
import se.gritacademy.webfulkoping.model.Loan;
import se.gritacademy.webfulkoping.model.User;
import se.gritacademy.webfulkoping.model.UserType;
import se.gritacademy.webfulkoping.service.ReturnService;
import se.gritacademy.webfulkoping.util.SessionUtil;

import java.io.IOException;
import java.util.List;

@WebServlet({"/active-loans", "/loan-history"})
public class LoanController extends HttpServlet {
    //LoanDAO objekt för att kunna hantera Loan-uppgifter från databasen
    private LoanDAO loanDAO = new LoanDAO();
    //ReturnService objekt för att hantera återlämnings-logik
    private ReturnService returnService = new ReturnService();

    /**
     * Kontrollerar om användaren är inloggad, om inte skickas besökaren till login-sidan.
     * Om användaren är inloggad men av userType Admin, skickas användare till /dashboard.
     * Annars skickas användaren till den sida som länken användaren tryckt på går till
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
            if (user == null) {
                res.sendRedirect(req.getContextPath() + "/login");
                return;
            }
            if (user.getUserType() == UserType.ADMIN) {
                res.sendRedirect(req.getContextPath() + "/dashboard");
                return;
            }
            String path = req.getServletPath();
            if (path.equals("/active-loans")) {
                showActiveLoans(req, res, user);
            } else if (path.equals("/loan-history")) {
                showLoanHistory(req, res, user);
            } else {
                req.setAttribute("message", "Ogiltig begäran!");
                req.getRequestDispatcher("view/user/index.jsp").forward(req, res);
            }
        } catch(Throwable e){
            e.printStackTrace();
            req.setAttribute("message", "Ett oväntat fel har uppstått, vänligen försök igen senare!");
            req.getRequestDispatcher("view/user/index.jsp").forward(req, res);
        }
    }

    /**
     * Kontrollerar om användaren är inloggad, om inte skickas besökaren till login-sidan.
     * Om användaren är inloggad återlämnas lånat om inte loanId är null eller tom
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
            if (user == null) {
                res.sendRedirect(req.getContextPath() + "/login");
                return;
            }
            String loanId = req.getParameter("loanId");
            if (loanId != null && !loanId.isEmpty()) {
                boolean returned = returnService.returnLoan(Integer.parseInt(loanId), user.getId());
                if (!returned) {
                    req.setAttribute("message", "Kunde inte genomföra återlämningen!");
                    req.getRequestDispatcher("view/user/loanActive.jsp").forward(req, res);
                } else {
                    req.setAttribute("message", "Återlämning genomförd!");
                    req.getRequestDispatcher("view/user/loanActive.jsp").forward(req, res);
                }
            }
        } catch(Throwable e){
            e.printStackTrace();
            req.setAttribute("message", "Ett oväntat fel har uppstått, vänligen försök igen senare!");
            req.getRequestDispatcher("view/user/index.jsp").forward(req, res);
        }
    }

    /**
     * Hämtar användarens pågående lån från databasen och visar genom att skicka användaren till loanActive.jsp
     *
     * @param req - HTTP-requesten som skickas från användarens webbläsare till servern
     * @param res - HTTP-responsen som servern skickar tillbaka till användarens webbläsare
     * @param user - Den inloggade användaren
     * @throws ServletException
     * @throws IOException
     */
    private void showActiveLoans(HttpServletRequest req, HttpServletResponse res, User user) throws ServletException, IOException {
        List<Loan> activeLoans = loanDAO.findActiveLoansByUserId(user.getId());
        req.setAttribute("loans", activeLoans);
        req.setAttribute("loanTitle", "Aktiva Lån");
        req.setAttribute("isHistory", false);
        req.getRequestDispatcher("view/user/loanActive.jsp").forward(req, res);
    }

    /**
     * Hämtar användarens lånehistorik från databasen och visar genom att skicka användaren till loanHistory.jsp
     *
     * @param req - HTTP-requesten som skickas från användarens webbläsare till servern
     * @param res - HTTP-responsen som servern skickar tillbaka till användarens webbläsare
     * @param user - Den inloggade användaren
     * @throws ServletException
     * @throws IOException
     */
    private void showLoanHistory(HttpServletRequest req, HttpServletResponse res, User user) throws ServletException, IOException {
        List<Loan> loanHistory = loanDAO.findLoanHistoryByUserId(user.getId());
        req.setAttribute("loans", loanHistory);
        req.setAttribute("loanTitle", "Lånehistorik");
        req.setAttribute("isHistory", true);
        req.getRequestDispatcher("view/user/loanHistory.jsp").forward(req, res);
    }
}