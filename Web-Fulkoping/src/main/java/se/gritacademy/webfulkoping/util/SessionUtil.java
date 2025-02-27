package se.gritacademy.webfulkoping.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import se.gritacademy.webfulkoping.model.User;


public class SessionUtil {

    /**
     * Kontrollerar om användaren är inloggad, uppdaterar session-attribut och returnerar användaren.
     *
     * @param req - HTTP-requesten som skickas från användarens webbläsare till servern
     * @return - User-objektet om användaren är inloggad, annars null
     */
    public static User checkUserSession(HttpServletRequest req) {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        setSessionAttributes(req, user);
        return user;
    }

    /**
     * Sätter session-attribut baserat på om en användare är inloggad eller inte.
     *
     * @param req - HTTP-requesten som skickas från användarens webbläsare till servern
     * @param user - User-objektet från en inloggad användare, annars null
     */
    private static void setSessionAttributes(HttpServletRequest req, User user) {
        if (user != null) {
            HttpSession session = req.getSession();
            session.setAttribute("user", user);
            req.setAttribute("isLoggedIn", true);
            req.setAttribute("username", user.getName());
        } else {
            req.setAttribute("isLoggedIn", false);
        }
    }
}