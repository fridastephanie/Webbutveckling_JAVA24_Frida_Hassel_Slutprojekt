package se.gritacademy.webfulkoping.dao;

import org.hibernate.Session;
import se.gritacademy.webfulkoping.model.Media;
import se.gritacademy.webfulkoping.model.MediaType;
import se.gritacademy.webfulkoping.util.HibernateUtil;

import java.util.Collections;
import java.util.List;

public class MediaDAO extends GenericDAO<Media> {

    /**
     * Konstruktor initierar GenericDAO med ett Media-objekt
     *
     */
    public MediaDAO() {
        super(Media.class);
    }

    /**
     * Hämtar en lista av alla Media-objekt av en viss media-typ som sorteras i bokstavsordningen efter titeln
     *
     * @param mediaType - Den media-typen vars lista ska hämtas
     * @return - En lista med alla result av media-typen, sorterat i bokstavsordninge efter titeln
     */
    public List<Media> getMediaByCategory(MediaType mediaType) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Media WHERE mediaType = :mediaType ORDER BY title", Media.class)
                    .setParameter("mediaType", mediaType)
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    /**
     * Söker efter ett specifikt sökord bland Media-objektens title, författare, isbn, genre och beskrivning
     * och skriver ut resultaten i bokstavsordningen efter titeln
     *
     * @param searchWord - Användarens angivna sökord
     * @return - En lista med alla objekt som innehåller något som matchar sökordet, sorterat i bokstavsordninge efter titeln
     */
    public List<Media> searchMedia(String searchWord) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String query = "FROM Media WHERE " +
                    "title ILIKE :search OR " +
                    "author ILIKE :search OR " +
                    "isbn ILIKE :search OR " +
                    "genre ILIKE :search OR " +
                    "description ILIKE :search ORDER BY title";
            List<Media> results = session.createQuery(query, Media.class)
                    .setParameter("search", "%" + searchWord + "%")
                    .list();
            return results;
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}