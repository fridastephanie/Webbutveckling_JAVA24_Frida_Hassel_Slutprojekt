package se.gritacademy.webfulkoping.service;

import se.gritacademy.webfulkoping.dao.LoanDAO;
import se.gritacademy.webfulkoping.dao.MediaDAO;
import se.gritacademy.webfulkoping.model.Loan;
import se.gritacademy.webfulkoping.model.Media;

import java.time.LocalDate;

public class ReturnService {
    //LoanDAO objekt för att kommunicera med databasen
    private LoanDAO loanDAO = new LoanDAO();
    //MediaDAO objekt för att kommunicera med databasen
    private MediaDAO mediaDAO = new MediaDAO();

    /**
     * Hanterar återlämning av ett lån och uppdaterar Loan-objektets slutdatum och Media-objektets isRented status
     *
     * @param loanId - Lånets id
     * @param userId - Användarens id
     * @return - False om lånet inte hittas, true om lånet återlämnas
     */
    public boolean returnLoan(int loanId, int userId) {
        Loan loan = loanDAO.findById(loanId);
        if (loan == null || loan.getUser().getId() != userId) {
           return false;
        }
        updateLoanReturnDetails(loan);
        updateMediaStatus(loan);
        return true;
    }

    /**
     * Uppdaterar Loan-objektets endDate och returnedDate till dagens datum
     *
     * @param loan - Loan-objektet som ska uppdateras
     */
    private void updateLoanReturnDetails(Loan loan) {
        loan.setEndDate(LocalDate.now());
        loan.setReturnedDate(LocalDate.now());
        loanDAO.update(loan);
    }

    /**
     * Uppdaterar Media-objektets isRented status till false och uppdaterar det i databasen
     *
     * @param loan - Loan-objektet vars Media ska återlämnas och uppdateras
     */
    private void updateMediaStatus(Loan loan) {
        Media media = loan.getMedia();
        media.setIsRented(false);
        mediaDAO.update(media);
    }
}