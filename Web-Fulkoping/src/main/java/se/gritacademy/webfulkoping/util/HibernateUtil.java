package se.gritacademy.webfulkoping.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    //Statiskt SessionFactory-objekt som används för att skapa Hibernate-sessioner
    private static SessionFactory sessionFactory = buildSessionFactory();

    /**
     * Kontrollerar om det redan finns ett SessionFactory-objekt, annars skapas ett nytt
     * samt ett Configuration-objektet som läser konfigurationen från "hibernate.cfg.xml"
     * och sätter värdena för miljövariablerna om de finns i "hibernate.cfg.xml"
     *
     * @return - Det skapade SessionFactory-objektet som innehåller alla databasinställningar
     */
    private static SessionFactory buildSessionFactory() {
        try {
            if (sessionFactory == null) {
                Configuration configuration = new Configuration().configure("hibernate.cfg.xml");
                if (System.getenv("DB_URL") != null) {
                    configuration.setProperty("hibernate.connection.url", System.getenv("DB_URL"));
                } else {
                    configuration.setProperty("hibernate.connection.url", "jdbc:postgresql://localhost:5432/fulkoping");
                }
                if (System.getenv("DB_USERNAME") != null) {
                    configuration.setProperty("hibernate.connection.username", System.getenv("DB_USERNAME"));
                }
                if (System.getenv("DB_PASSWORD") != null) {
                    configuration.setProperty("hibernate.connection.password", System.getenv("DB_PASSWORD"));
                }
                if (System.getenv("DB_DRIVER") != null) {
                    configuration.setProperty("hibernate.connection.driver", System.getenv("DB_DRIVER"));
                }
                if (System.getenv("DB_DIALECT") != null) {
                    configuration.setProperty("hibernate.connection.dialect", System.getenv("DB_DIALECT"));
                }
                sessionFactory = configuration.buildSessionFactory();
            }
            return sessionFactory;
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    /**
     * Returnerar det skapade SessionFactory-objektet som hanterar alla sessioner
     *
     * @return - Det skapade SessionFactory-objektet
     */
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    /**
     * Stänger SessionFactory och alla sessioner när applikationen avslutas
     */
    public static void shutdown() {
        getSessionFactory().close();
    }
}