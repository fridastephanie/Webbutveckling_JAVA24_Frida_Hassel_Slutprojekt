package se.gritacademy.webfulkoping.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Media")
public class Media {

    //Media-objektets Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    //Media-objektets titel
    @Column(name = "title", nullable = false)
    private String title;

    //Media-objektets författare
    @Column(name = "author", nullable = false)
    private String author;

    //Media-objektets ISBN
    @Column(name = "isbn", nullable = false)
    private String isbn;

    //Media-objektets genre
    @Column(name = "genre", nullable = false)
    private String genre;

    //Media-objektets beskrivning
    @Column(name = "description", nullable = false)
    private String description;

    //Media-objektets bildURL
    @Column(name="imageUrl")
    private String imageUrl;

    //Media-objektets lånestatus
    @Column(name = "isRented", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean isRented;

    //Media-objektets mediatyp
    @Enumerated(EnumType.STRING)
    @Column(name = "mediaType", nullable = false)
    private MediaType mediaType;

    //Media-objektets lånehistorik, med OneToMany relation eftersom ett Media-objekt kan lånas flera gånger
    @OneToMany(mappedBy = "media", cascade = CascadeType.ALL)
    private List<Loan> loans = new ArrayList<>();

    /**
     * Tom konstruktor
     */
    public Media() {}

    /**
     * Konstruktor för Media som inhämtar Media-objektets namn, författare, isbn, genre, beskrivning och bildsökväg
     *
     * @param title - Media-objektets angivna titel
     * @param author - Media-objektets angivna författare
     * @param isbn - Media-objektets angivna isbn
     * @param genre - Media-objektets angivna genre
     * @param description - Media-objektets angivna beskrivning
     */
    public Media (String title, String author, String isbn, String genre, String description) {
        setTitle(title);
        setAuthor(author);
        this.isbn = isbn;
        setGenre(genre);
        this.description = description;
    }

    /**
     * Sätter imageURL till en deafult bild om inget annat anges
     */
    @PrePersist
    public void prePersist() {
        if (this.imageUrl == null) {
            this.imageUrl = "media-no-imageUrl.jpg";
        }
    }

    /**
     * Formaterat en text så att varje ord börjar med stor bokstav och resten av bokstäverna är små
     *
     * @param text - Texten/ordet som ska omformateras
     * @return - Samma text men omfortmatterat
     */
    private String formatText (String text) {
        if (text != null && !text.trim().isEmpty()) {
            String[] words = text.split("(?<=\\b)(?=\\s|-)|(?<=\\s|-)");
            StringBuilder formattedText = new StringBuilder();

            for (String word : words) {
                if (!word.isEmpty()) {
                    formattedText.append(word.substring(0, 1).toUpperCase());
                    formattedText.append(word.substring(1).toLowerCase());
                }
            }
            text = formattedText.toString().trim();
        }
        return text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    /**
     * Sätter titeln efter att ha formaterat det så att varje ord börjar med stor bokstav och resten av bokstäverna är små
     *
     * @param title - Media-objektets titel
     */
    public void setTitle(String title) {
        title = formatText(title);
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    /**
     * Sätter författarens namn efter att ha formaterat det så att varje ord börjar med stor bokstav och resten av bokstäverna är små
     *
     * @param author - Media-objetkets författare
     */
    public void setAuthor(String author) {
        author = formatText(author);
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getGenre() {
        return genre;
    }

    /**
     * Sätter gernen efter att ha formaterat det så att varje ord börjar med stor bokstav och resten av bokstäverna är små
     *
     * @param genre - Media-objektets genre
     */
    public void setGenre(String genre) {
        genre = formatText(genre);
        this.genre = genre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean getIsRented() {
        return isRented;
    }

    public void setIsRented(boolean rented) {
        isRented = rented;
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    public void setMediaType(MediaType mediaType) {
        this.mediaType = mediaType;
    }

    public List<Loan> getLoans() {
        return loans;
    }

    public void setLoans(List<Loan> loans) {
        this.loans = loans;
    }
}