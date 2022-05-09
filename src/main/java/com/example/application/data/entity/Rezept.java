package com.example.application.data.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.example.application.data.repository.RezeptRepository;
import com.example.application.data.service.RezeptService;
import com.vaadin.flow.component.html.Image;

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
public class Rezept {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private Image bild;
    @Column(unique = true)
    private String titel;
    @Column(columnDefinition = "TEXT")
    private String zubereitung;
    private int portionen;
    @OneToMany(mappedBy = "rezept", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<Rezept_Zutat> zutaten = new HashSet<>();

    /*
     * @MantToOne
     * 
     * @JoinColumn(name = "kategorie_id")
     * private Kategorie kategorie;
     */
    public Rezept() {
    }

    public Rezept(long id, Image bild, String titel, String zubereitung, int portionen, Set<Rezept_Zutat> zutaten) {
        this.id = id;
        this.bild = bild;
        this.titel = titel;
        this.zubereitung = zubereitung;
        this.portionen = portionen;
        this.zutaten = zutaten;
    }

    public Rezept(Image bild, String titel, String zubereitung, int portionen) {
        this.bild = bild;
        this.titel = titel;
        this.zubereitung = zubereitung;
        this.portionen = portionen;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Image getBild() {
        return this.bild;
    }

    public void setBild(Image bild) {
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

    public Rezept bild(Image bild) {
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

}
