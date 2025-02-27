package se.gritacademy.webfulkoping.service;

import se.gritacademy.webfulkoping.dao.LoanDAO;
import se.gritacademy.webfulkoping.dao.MediaDAO;
import se.gritacademy.webfulkoping.dao.UserDAO;
import se.gritacademy.webfulkoping.model.Loan;
import se.gritacademy.webfulkoping.model.Media;
import se.gritacademy.webfulkoping.model.User;

import java.time.LocalDate;

public class LoanService {
    //LoanDAO objekt för att kommunicera med databasen
    private LoanDAO loanDAO = new LoanDAO();
    //UserDAO objekt för att kommunicera med databasen
    private UserDAO userDAO = new UserDAO();
    //MediaDAO objekt för att kommunicera med databasen
    private MediaDAO mediaDAO = new MediaDAO();

    /**
     * Kontrollerar att användaren är inloggad och att Media-objektet är tillgängligt,
     * och isåfall skapas ett lån och Media-objektets status ändras till isRented.
     * Användaren meddelas oavsett utfallet
     *
     * @param user - Användaren som ska låna
     * @param mediaId - Media-objektets id som ska lånas
     * @return - En sträng med ett meddelande till användaren beroende på utfall
     */
    public String createLoan(User user, int mediaId) {
        if (user == null) {
            return "Du måste vara inloggad för att kunna låna!";
        }
        Media media = findMediaAndCheckAvailability(mediaId);
        if (media == null) {
            return media.getTitle() + " finns inte eller är redan utlånad!";
        }
        return updateMediaAndCreateLoan(media, user);
    }

    /**
     * Kontrollerar om Media-objektet är tillgängligt för lån
     *
     * @param mediaId - Media-objektet som ska lånas id
     * @return - Null om Media-objektet inte finns eller inte är ledigt, annars Media-objektet
     */
    private Media findMediaAndCheckAvailability(int mediaId) {
        Media media = mediaDAO.findById(mediaId);
        if (media == null) {
            return null;
        }
        LocalDate loanEndDate = loanDAO.findEndDateByMediaId(mediaId);
        if (loanEndDate != null) {
            return null;
        }
        return media;
    }

    /**
     * Sätter Media-objektets status till isRented och uppdaterar statusen i databasen,
     * samt skapar ett nytt Loan-objekt och skapar det i databasen.
     * Användaren får ett meddelande om att lånet lyckas
     *
     * @param media - Media-objektet som ska lånas
     * @param user - Användaren som ska låna
     * @return - En sträng med ett meddelande till användren om lyckat lån
     */
    private String updateMediaAndCreateLoan(Media media, User user) {
        media.setIsRented(true);
        mediaDAO.update(media);

        Loan loan = new Loan(media, user);
        loanDAO.create(loan);

        return "Du har lånat " + media.getTitle() + ", återlämna senast " + loan.getEndDate();
    }
}