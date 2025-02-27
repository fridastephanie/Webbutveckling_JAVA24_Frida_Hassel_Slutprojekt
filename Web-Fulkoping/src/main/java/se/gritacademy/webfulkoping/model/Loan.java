package se.gritacademy.webfulkoping.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name="Loan")
public class Loan {

    //Id för lånet
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", nullable = false)
    private int id;

    //Media-objektet som lånas, med ManyToOne relation eftersom flera lån kan referera till ett Media-objekt
    @ManyToOne
    @JoinColumn(name = "mediaId", nullable = false)
    private Media media;

    //User-objektet som lånar, med ManyToOne relation eftersom flera lån kan referera till ett User-objekt
    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    // Lånets startdatum
    @Column(name = "startDate", nullable = false)
    private LocalDate startDate;

    //Lånets slutdatum
    @Column(name = "endDate", nullable = false)
    private LocalDate endDate;

    //Datumet för när lånet returnerades
    @Column(name = "returnedDate")
    private LocalDate returnedDate;

    /**
     * Tom konstruktor
     */
    public Loan() {}

    /**
     * Konstruktor för Loan som inhämtar Media-objektet och User-objektet,
     * samt sätter startDate till dagens datum och endDate till +30dagar från dagens datum och returnedDate till null
     *
     * @param media - Media-objektet som ska lånas
     * @param user - Användaren som ska låna
     */
    public Loan(Media media, User user) {
        this.media = media;
        this.user = user;
        this.startDate = LocalDate.now();
        this.endDate = LocalDate.now().plusDays(30);
        this.returnedDate = null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Media getMedia() {
        return media;
    }

    public void setMedia(Media media) {
        this.media = media;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalDate getReturnedDate() {
        return returnedDate;
    }

    public void setReturnedDate(LocalDate returnedDate) {
        this.returnedDate = returnedDate;
    }
}
