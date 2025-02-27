package se.gritacademy.webfulkoping.dao;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import se.gritacademy.webfulkoping.util.HibernateUtil;

import java.util.List;

public abstract class GenericDAO<T> {
    // Den aktuella entitetsklassen
    private Class<T> entityClass;

    /**
     * Konstruktor som tar emot en specifik klassreferens och initierar GenericDAO med den
     *
     * @param entityClass - Den angivna klassen som blir den entitetsklassen som ska hanteras av GenericDAO
     */
    public GenericDAO(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    /**
     * Skapar ett nytt objekt i databasen av den angivna objekttypen som tas emot
     *
     * @param entity - Det objekt som ska sparas i databasen
     */
    public void create(T entity) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.save(entity);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    /**
     * Söker efter ett objekt baserat på det angivna id:t i databasen
     *
     * @param id - Det angivna id som ska sökas efter i databasen
     * @return - Objekt av det aktuella entitetsklassen om det hittas, annars returneras null
     */
    public T findById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(entityClass, id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Skapar en lista med alla objekt som hittas av entitetsklassen som finns i databasen
     * och sorterar resultaten baserat på det angivna fältet
     *
     * @param sortBy - Fältet som resultaten ska sorteras efter
     * @return - En lista med alla objekt av entitetsklassen om objekt hittas, annars returneras en tom lista eller null
     */
    public List<T> findAll(String sortBy) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String query = "FROM " + entityClass.getName();

              if (sortBy != null && !sortBy.isEmpty()) {
                query += " ORDER BY " + sortBy;
            }

            Query<T> result = session.createQuery(query, entityClass);
            return result.list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Uppdaterar/ändrar ett redan befintligt objekt av entitetsklassen i databasen
     *
     * @param entity - Det objekt av entitetsklassen som ska uppdateras/ändras
     */
    public void update(T entity) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(entity);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    /**
     * Raderar ett befintligt objekt av entitetsklassen i databasen
     *
     * @param entity - Det objekt av entitetsklassen som ska raderas
     */
    public void delete(T entity) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.delete(entity);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }
}
