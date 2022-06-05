package com.example.application.data.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.example.application.data.repository.RezeptRepository;
import com.example.application.data.service.RezeptService;

/**
 * Rezept Entität mit einer OneToMany Beziehung zu der Repzept_Zutaten Entität.
 * Das Rezept hat eine id, ein Bild, einen Titel, die Zubereitung, Protionen und
 * die Zutaten. Die Klasse hat Getter und Setter Methoden für alle
 * Instanzvariablen. Sowie eine weitere Methode, um nur die Zutaten zu kriegen
 * 
 * @author Philipp Laupichler
 * @see Rezept_Zutat
 * @see RezeptRepository
 * @see RezeptService
 */
@Entity
public class Rezept implements Comparable<Rezept> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] bild;
    @Column(unique = true)
    private String titel;
    @Column(columnDefinition = "TEXT")
    private String zubereitung;
    private int portionen;
    @OneToMany(mappedBy = "rezept", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<Rezept_Zutat> zutaten = new HashSet<>();

    @ManyToOne

    @JoinColumn(name = "kategorie_id")
    private Kategorie kategorie;

    public Rezept() {
    }

    public Rezept(long id, byte[] bild, String titel, String zubereitung, int portionen, Set<Rezept_Zutat> zutaten) {
        this.id = id;
        this.bild = bild;
        this.titel = titel;
        this.zubereitung = zubereitung;
        this.portionen = portionen;
        this.zutaten = zutaten;
    }

    public Rezept(byte[] bild, String titel, String zubereitung, int portionen) {
        this.bild = bild;
        this.titel = titel;
        this.zubereitung = zubereitung;
        this.portionen = portionen;
    }

    /**
     * Konstruktor, der auch den Parameter Kategorie enthält
     * 
     * @param id
     * @param bild
     * @param titel
     * @param zubereitung
     * @param portionen
     * @param zutaten
     * @param kategorie
     * @author Anna Karle
     */
    public Rezept(long id, byte[] bild, String titel, String zubereitung, int portionen, Set<Rezept_Zutat> zutaten,
            Kategorie kategorie) {
        this.id = id;
        this.bild = bild;
        this.titel = titel;
        this.zubereitung = zubereitung;
        this.portionen = portionen;
        this.zutaten = zutaten;
        this.kategorie = kategorie;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public byte[] getBild() {
        return this.bild;
    }

    public void setBild(byte[] bild) {
        this.bild = bild;
    }

    public String getTitel() {
        return this.titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public String getZubereitung() {
        return this.zubereitung;
    }

    public void setZubereitung(String zubereitung) {
        this.zubereitung = zubereitung;
    }

    public int getPortionen() {
        return this.portionen;
    }

    public void setPortionen(int portionen) {
        this.portionen = portionen;
    }

    public Set<Rezept_Zutat> getZutatenFromRezept_Zutaten() {
        return this.zutaten;
    }

    public void setZutaten(Set<Rezept_Zutat> zutaten) {
        this.zutaten = zutaten;
    }

    public void addZutat(Rezept_Zutat rezept_Zutat) {
        zutaten.add(rezept_Zutat);
    }

    public Rezept id(long id) {
        setId(id);
        return this;
    }

    public Rezept bild(byte[] bild) {
        setBild(bild);
        return this;
    }

    public Rezept titel(String titel) {
        setTitel(titel);
        return this;
    }

    public Rezept zubereitung(String zubereitung) {
        setZubereitung(zubereitung);
        return this;
    }

    public Rezept portionen(int portionen) {
        setPortionen(portionen);
        return this;
    }

    public Rezept zutaten(Set<Rezept_Zutat> zutaten) {
        setZutaten(zutaten);
        return this;
    }

    /*
     * @Override
     * public boolean equals(Object o) {
     * if (o == this)
     * return true;
     * if (!(o instanceof Rezept)) {
     * return false;
     * }
     * Rezept rezept = (Rezept) o;
     * return id == rezept.id && Objects.equals(bild, rezept.bild) &&
     * Objects.equals(titel, rezept.titel) && Objects.equals(zubereitung,
     * rezept.zubereitung) && portionen == rezept.portionen;
     * }
     * 
     * @Override
     * public int hashCode() {
     * return Objects.hash(id, bild, titel, zubereitung, portionen);
     * }
     * 
     */

    public Set<Zutat> getZutatenFromZutat() {
        Set<Zutat> zutaten = new HashSet<>();
        for (Rezept_Zutat zutat : this.zutaten) {
            zutaten.add(zutat.getZutat());
        }
        return zutaten;
    }

    /**
     * Getter und Setter Methoden für die Instanzvariable Kategorie
     * 
     * @author Anna Karle
     */
    public Kategorie getKategorie() {
        return kategorie;
    }

    public void setKategorie(Kategorie kategorie) {
        this.kategorie = kategorie;
    }

    @Override
    public String toString() {
        return "{" +
                " id='" + getId() + "'" +
                ", bild='" + getBild() + "'" +
                ", titel='" + getTitel() + "'" +
                ", zubereitung='" + getZubereitung() + "'" +
                ", portionen='" + getPortionen() + "'" +
                ", zutaten='" + getZutatenFromZutat().toString() + "'" +
                "}";
    }

    /**
     * Methode toString, die die Variable Kategorie enthält
     * 
     * @author Anna Karle
     */
    public String toStringMitKategorie() {
        return "{" +
                " id='" + this.id + "'" +
                ", bild='" + this.bild + "'" +
                ", titel='" + this.titel + "'" +
                ", zubereitung='" + this.zubereitung + "'" +
                ", portionen='" + this.portionen + "'" +
                ", zutaten='" + getZutatenFromZutat().toString() + "'" +
                ", kategorie= '" + this.kategorie.toString() + "'" +
                "}";
    }

    /**
     * Implementierung des Comparable Interfaces zum sortierten Ausgeben der Rezepte
     * anhand der Kategorie
     * 
     * @author Léo Hérubel
     * @param rezept
     * @return
     */
    @Override
    public int compareTo(Rezept rezept) {
        return (int) this.getKategorie().getSequenceNr() - (int) rezept.getKategorie().getSequenceNr();
    }

    // private byte[] getImageAsByteArray() {
    // try {
    // return
    // IOUtils.toByteArray(this.bild.getClass().getResourceAsStream(this.bild.getSrc()));
    // } catch (IOException e) {
    // e.printStackTrace();
    // return null;
    // }
    // }

}
