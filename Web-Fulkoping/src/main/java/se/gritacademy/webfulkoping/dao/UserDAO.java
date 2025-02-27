package se.gritacademy.webfulkoping.dao;

import org.hibernate.Session;
import se.gritacademy.webfulkoping.model.User;
import se.gritacademy.webfulkoping.util.HibernateUtil;

import java.util.ArrayList;
import java.util.List;

public class UserDAO extends GenericDAO<User> {

    /**
     * Konstruktor initierar GenericDAO med ett User-objekt
     *
     */
    public UserDAO() {
        super(User.class);
    }

    /**
     * Söker efter användare med en specifik e-postadress
     *
     * @param email - Användarens angivna e-postadress
     * @return - En User från databasen om e-postadressen hittas, annars null
     */
    public User getUserByEmail(String email) {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            return session.createQuery("FROM User where email = :email", User.class)
                    .setParameter("email", email)
                    .uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Söker efter användare med ett specifikt namn eller e-postadress
     *
     * @param searchWord - Användarens angivna sökord
     * @return - En lista från databasen om namn eller e-postadress hittas, annars null
     */
    public List<User> searchUsersByNameOrEmail(String searchWord) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String query = "FROM User WHERE name ILIKE :query OR email ILIKE :query";
            return session.createQuery(query, User.class)
                    .setParameter("query", "%" + searchWord + "%")
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
