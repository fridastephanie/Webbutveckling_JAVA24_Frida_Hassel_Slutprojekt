package se.gritacademy.webfulkoping.model;

import jakarta.persistence.*;
import se.gritacademy.webfulkoping.util.HashingPasswordUtil;

import java.util.*;

@Entity
@Table(name="UserAccount")
public class User {

    //Användarens id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", nullable = false)
    private int id;

    //Användarens namn
    @Column(name="name", nullable = false)
    private String name;

    //Användarens e-post
    @Column(name="email", nullable = false, unique = true)
    private String email;

    //Användarens lösenord
    @Column(name="password", nullable = false)
    private String password;

    //User-objektets användartyp
    @Enumerated(EnumType.STRING)
    @Column(name = "userType", nullable = false)
    private UserType userType;

    //Användarens lånehistorik, med OneToMany relation eftersom ett User-objekt kan ha flera lån
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Loan> loans = new ArrayList<>();

    /**
     * Tom konstruktor
     */
    public User() {}

    /**
     * Konstruktor för User som inhämtar användarens namn, email och lösenord
     * och därefter via set metoderna ser till att de inmatade värdena upppfyller kraven
     *
     * @param name - Användarens angivna namn
     * @param email - Användarens angivna email
     * @param password - Användarens angivna lösenord
     */
    public User(String name, String email, String password) {
        setName(name);
        setEmail(email);
        setPassword(password);
    }

    /**
     * Sätter userType till USER om inget annat anges
     */
    @PrePersist
    public void prePersist() {
        if (this.userType == null) {
            this.userType = UserType.USER;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    /**
     * Sätter namnet efter att ha formaterat det så att varje ord börjar med stor bokstav och resten av bokstäverna är små
     *
     * @param name - Namnet som ska sättas för objektet
     */
    public void setName(String name) {
        if (name != null && !name.trim().isEmpty()) {
            String[] words = name.split("(?<=\\b)(?=\\s|-)|(?<=\\s|-)");
            StringBuilder formattedName = new StringBuilder();

            for (String word : words) {
                if (!word.isEmpty()) {
                    formattedName.append(word.substring(0, 1).toUpperCase());
                    formattedName.append(word.substring(1).toLowerCase());
                }
            }
            name = formattedName.toString().trim();
        }
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    /**
     * Sätter e-post efter att ha validerat att den är i rätt format, samt gör alla bokstäver till små bokstäver
     *
     * @param email - Den e-postadress som ska sättas för objektet
     */
    public void setEmail(String email) {
        if (email == null || !email.matches("^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            throw new IllegalArgumentException("Ogiltig e-postadress: " + email);
        }
        this.email = email.toLowerCase();
    }

    public String getPassword() {
        return password;
    }

    /**
     * Kontrollerar att password inte är null eller tom, därefter hashas lösenordet
     *
     * @param password - Användarens inmatade lösenord
     */
    public void setPassword(String password) {
        if (password != null && !password.trim().isEmpty()) {
            this.password = HashingPasswordUtil.createHash(password);
        }
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public List<Loan> getLoans() {
        return loans;
    }

    public void setLoans(List<Loan> loans) {
        this.loans = loans;
    }
}
