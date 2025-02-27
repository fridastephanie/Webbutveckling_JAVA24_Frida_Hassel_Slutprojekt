package se.gritacademy.webfulkoping.controller.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import se.gritacademy.webfulkoping.dao.MediaDAO;
import se.gritacademy.webfulkoping.model.Media;
import se.gritacademy.webfulkoping.model.MediaType;
import se.gritacademy.webfulkoping.model.User;
import se.gritacademy.webfulkoping.model.UserType;
import se.gritacademy.webfulkoping.util.SessionUtil;

import java.io.IOException;

@WebServlet("/add-media")
public class AddMediaController extends HttpServlet {
    //MediaDAO objekt för att kunna hantera Media-objekt från databasen
    private MediaDAO mediaDAO = new MediaDAO();

    /**
     * Hämtar den aktuella sessionen och kontrollerar om användaren är inloggad och har userType Admin,
     * isåfall vidarebefodras användaren till addMedia.jsp, annars vidarebefodras begäran till /login
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
                req.getRequestDispatcher("view/admin/addMedia.jsp").forward(req, res);
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
     * och om allt är korrekt ifyllt skapas ett nytt Media-objekt
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

            String title = req.getParameter("title");
            String author = req.getParameter("author");
            String isbn = req.getParameter("isbn");
            String genre = req.getParameter("genre");
            String description = req.getParameter("description");
            String mediaTypeStr = req.getParameter("mediaType");

            String validationError = validateFields(title, author, isbn, genre, description, mediaTypeStr);
            if (validationError != null) {
                req.setAttribute("message", validationError);
                req.getRequestDispatcher("view/admin/addMedia.jsp").forward(req, res);
                return;
            }

            createNewMedia(title, author, isbn, genre, description, mediaTypeStr);
            req.setAttribute("message", "Media har lagts till!");
            req.getRequestDispatcher("view/admin/addMedia.jsp").forward(req, res);
        } catch(Throwable e){
            e.printStackTrace();
            req.setAttribute("message", "Ett oväntat fel har uppstått, vänligen försök igen senare!");
            req.getRequestDispatcher("view/admin/dashboard.jsp").forward(req, res);
        }
    }

    /**
     * Kontrollerar att användaren fyllt i alla uppgifter, annars returneras ett Error-meddelande
     *
     * @param title - Den inhämtade titeln för Media-objektet
     * @param author - Den inhämtade författaren för Media-objektet
     * @param isbn - Det inhämtade ISBN för Media-objektet
     * @param genre - Den inhämtade genren för Media-objektet
     * @param description - Den inhämtade beskrivningen för Media-objektet
     * @param mediaType - Den inhämtade mediaTypen för Media-objektet
     * @return - Null om allt är ifyllt, annars ett Error-meddelande
     */
    private String validateFields(String title, String author, String isbn, String genre, String description, String mediaType) {
        if (title == null || title.isEmpty() || author == null || author.isEmpty() || isbn == null || isbn.isEmpty() ||
                genre == null || genre.isEmpty() || description == null || description.isEmpty() || mediaType == null || mediaType.isEmpty()) {
            return "Alla fält måste fyllas i!";
        }
        try {
            MediaType.valueOf(mediaType);
        } catch (Throwable e) {
            return "Ogiltig mediatyp!";
        }
        return null;
    }

    /**
     * Skapar ett nytt Media-objekt med de angivna värdena från fälten
     *
     * @param title - Media-objektets angivna titel
     * @param author - Media-objektets angivna författare
     * @param isbn - Media-objektets angivna ISBN
     * @param genre - Media-objektets angivna genre
     * @param description - Media-objektets angivna beskrivning
     * @param mediaTypeStr - Media-objektets angivna mediaType i String-format
     */
    private void createNewMedia(String title, String author, String isbn, String genre, String description, String mediaTypeStr) {
        MediaType mediaType = MediaType.valueOf(mediaTypeStr);
        Media newMedia = new Media(title, author, isbn, genre, description);
        newMedia.setMediaType(mediaType);
        mediaDAO.create(newMedia);
    }
}