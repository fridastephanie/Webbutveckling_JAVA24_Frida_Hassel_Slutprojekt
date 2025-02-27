package se.gritacademy.webfulkoping.controller.user;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import se.gritacademy.webfulkoping.dao.LoanDAO;
import se.gritacademy.webfulkoping.dao.MediaDAO;
import se.gritacademy.webfulkoping.model.Media;
import se.gritacademy.webfulkoping.model.MediaType;
import se.gritacademy.webfulkoping.model.User;
import se.gritacademy.webfulkoping.model.UserType;
import se.gritacademy.webfulkoping.service.LoanService;
import se.gritacademy.webfulkoping.util.SessionUtil;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/media")
public class MediaController extends HttpServlet {
    //MediaDAO objekt för att kunna hantera Media-objekt från databasen
    private MediaDAO mediaDAO = new MediaDAO();
    //LoanDAO objekt för att kunna hantera Loan-uppgifter från databasen
    private LoanDAO loanDAO = new LoanDAO();
    //LoanService objekt för att hantera lån-logik
    private LoanService loanService = new LoanService();

    /**
     * Kontrollerar om användaren är inloggad samt om användartypen är Admin (isåfall skickas användaren till /dashboard),
     * annars kontrolleras om användaren ska kolla på medialistor (mediaList.jsp),
     * media detalj-sida (mediaDetail.jsp) och hanterar om en användare vill låna en media
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
            String action = req.getParameter("action");
            String mediaId = req.getParameter("id");

            if (user != null && user.getUserType() == UserType.ADMIN) {
                res.sendRedirect(req.getContextPath() + "/dashboard");
                return;
            }

            if ("loan".equals(action) && mediaId != null && !mediaId.isEmpty()) {
                handleLoanRequest(req, res, user, mediaId);
                return;
            }
            if (mediaId != null && !mediaId.isEmpty()) {
                showMediaDetails(req, res, mediaId);
            } else {
                showMediaList(req, res);
            }
        }
        catch(Throwable e){
            e.printStackTrace();
            req.setAttribute("message", "Ett oväntat fel har uppstått, vänligen försök igen senare!");
            req.getRequestDispatcher("view/user/index.jsp").forward(req, res);
        }
    }

    /**
     * Hanterar en låneförfrågan från en User för ett Media-objekt
     *
     * @param req - HTTP-requesten som skickas från användarens webbläsare till servern
     * @param res - HTTP-responsen som servern skickar tillbaka till användarens webbläsare
     * @param user - Den inloggade användare
     * @param mediaId - ID för Media-objektet som användaren vill låna
     */
    private void handleLoanRequest(HttpServletRequest req, HttpServletResponse res, User user, String mediaId) throws ServletException, IOException {
        Media media = mediaDAO.findById(Integer.parseInt(mediaId));

        if (user == null) {
            res.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        if (media == null) {
            res.sendRedirect(req.getContextPath() + "/media");
            return;
        }

        if (media.getIsRented()) {
            res.sendRedirect(req.getContextPath() + "/media?id=" + mediaId);
            return;
        }

        LocalDate loanEndDate = loanDAO.findEndDateByMediaId(Integer.parseInt(mediaId));
        String message = loanService.createLoan(user, Integer.parseInt(mediaId));
        redirectWithMessage(req, res, media, loanEndDate, message);
    }

    /**
     * Skapar ett meddelande i en popup ruta för användaren samt skickar användaren till mediaDetail.jsp
     *
     * @param req - HTTP-requesten som skickas från användarens webbläsare till servern
     * @param res - HTTP-responsen som servern skickar tillbaka till användarens webbläsare
     * @param media - Media-objektet som användaren försöker låna
     * @param loanEndDate - Sista återlämningsdagen för det aktuella lånet
     * @param message - Meddelandet som ska skrivas ut till användaren i popup rutan
     *
     * @throws ServletException
     * @throws IOException
     */
    private void redirectWithMessage(HttpServletRequest req, HttpServletResponse res, Media media, LocalDate loanEndDate, String message) throws ServletException, IOException {
        req.setAttribute("message", message);
        req.setAttribute("media", media);
        req.setAttribute("loanEndDate", loanEndDate);
        req.getRequestDispatcher("view/user/mediaDetail.jsp").forward(req, res);
    }

    /**
     * Visar detaljer för ett specifikt Media-objekt och sparar den föregående URL:en från sessionen
     *
     * @param req - HTTP-requesten som skickas från användarens webbläsare till servern
     * @param res - HTTP-responsen som servern skickar tillbaka till användarens webbläsare
     * @param mediaId - Det valda Media-objektets id
     */
    private void showMediaDetails(HttpServletRequest req, HttpServletResponse res, String mediaId) throws ServletException, IOException {
        Media media = mediaDAO.findById(Integer.parseInt(mediaId));
        LocalDate loanEndDate = loanDAO.findEndDateByMediaId(Integer.parseInt(mediaId));

        String previousUrl = (String) req.getSession().getAttribute("previousUrl");
        req.setAttribute("previousUrl", previousUrl);

        req.setAttribute("media", media);
        req.setAttribute("loanEndDate", loanEndDate);
        req.getRequestDispatcher("view/user/mediaDetail.jsp").forward(req, res);
    }

    /**
     * Visar en lista av Media-objekt baserat på kategori eller sökord samt sparar den aktuella URL:en i sessionen
     *
     * @param req - HTTP-requesten som skickas från användarens webbläsare till servern
     * @param res - HTTP-responsen som servern skickar tillbaka till användarens webbläsare
     */
    private void showMediaList(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String category = req.getParameter("category");
        String searchWord = req.getParameter("search");

        List<Media> mediaList = new ArrayList<>();

        if (searchWord != null && !searchWord.isEmpty()) {
            mediaList = mediaDAO.searchMedia(searchWord);
        } else {
            if (category == null || category.equals("all")) {
                mediaList = mediaDAO.findAll("title");
            } else {
                try {
                    MediaType mediaType = MediaType.valueOf(category.toUpperCase());
                    mediaList = mediaDAO.getMediaByCategory(mediaType);
                } catch (IllegalArgumentException e) {
                    mediaList = mediaDAO.findAll("title");
                }
            }
        }
        String currentUrl = req.getRequestURI();
        String queryString = req.getQueryString();

        String fullUrl = (queryString != null) ? currentUrl + "?" + queryString : currentUrl;
        req.getSession().setAttribute("previousUrl", fullUrl);

        req.setAttribute("mediaList", mediaList);
        req.getRequestDispatcher("view/user/mediaList.jsp").forward(req, res);
    }
}