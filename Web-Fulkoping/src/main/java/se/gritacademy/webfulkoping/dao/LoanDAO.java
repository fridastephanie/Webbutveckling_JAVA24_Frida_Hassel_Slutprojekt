package se.gritacademy.webfulkoping.dao;

import jakarta.persistence.NoResultException;
import org.hibernate.Session;
import se.gritacademy.webfulkoping.model.Loan;
import se.gritacademy.webfulkoping.util.HibernateUtil;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

public class LoanDAO extends GenericDAO<Loan> {

    /**
     * Konstruktor initierar GenericDAO med ett Loan-objekt
     *
     */
    public LoanDAO() {
        super(Loan.class);
    }

    /**
     * Hämtar slutdatumet för det senaste lånet av ett specifikt Media-objekt
     *
     * @param mediaId - ID för det Media-objekt som vi vill hämta lånets slutdatum för
     * @return - Slutdatumet (endDate) för det senaste lånet om det finns, annars null om inget lån är aktivt
     */
    public LocalDate findEndDateByMediaId(int mediaId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String query = "SELECT l.endDate FROM Loan l WHERE l.media.id = :mediaId AND l.returnedDate IS NULL ORDER BY l.startDate DESC";
            return (LocalDate) session.createQuery(query)
                    .setParameter("mediaId", mediaId)
                    .setMaxResults(1)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Hämtar en lista på alla användarens aktiva lån (där returnedDate är null)
     *
     * @param userId - Användarens id
     * @return - EN lista med användarens aktiva lån
     */
    public List<Loan> findActiveLoansByUserId(int userId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "FROM Loan l WHERE l.user.id = :userId AND l.returnedDate IS NULL", Loan.class)
                    .setParameter("userId", userId)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    /**
     * Hämtar en lista på användarens lånehistorik (med ej pågående lån, där returnedDate är not null)
     *
     * @param userId - Användarens id
     * @return - EN lista med användarens lånehistorik
     */
    public List<Loan> findLoanHistoryByUserId(int userId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "FROM Loan l WHERE l.user.id = :userId AND l.returnedDate IS NOT NULL", Loan.class)
                    .setParameter("userId", userId)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
