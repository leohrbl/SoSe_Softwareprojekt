package com.example.application.data.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.example.application.data.repository.RezeptZutatenRepository;
import com.example.application.data.service.RezeptZutatenService;

/**
 * Rezept_Zutat Entität mit einer ManyToOne Beziehung zu der Repzept Entität und
 * einer ManyToOne Beziehung zu der Zutat Entität.
 * Die Rezept_Zutat Klasse hat eine id, ein Rezept, eine Zutat und die Menge.
 * Die Klasse hat Getter und Setter Methoden für alle
 * Instanzvariablen und eine toString Methode
 * 
 * @author Philipp Laupichler
 * @see Rezept
 * @see Zutat
 * @see RezeptZutatenRepository
 * @see RezeptZutatenService
 */
@Entity
public class Rezept_Zutat {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    @JoinColumn(name = "Rezept_id")
    private Rezept rezept;

    @ManyToOne
    @JoinColumn(name = "Zutat_id")
    private Zutat zutat;

    private int Menge;

    public Rezept_Zutat() {
    }

    public Rezept_Zutat(long id, Rezept rezept, Zutat zutat, int Menge) {
        this.id = id;
        this.rezept = rezept;
        this.zutat = zutat;
        this.Menge = Menge;
    }

    public Rezept_Zutat(Rezept rezept, Zutat zutat, int Menge) {

        this.rezept = rezept;
        this.zutat = zutat;
        this.Menge = Menge;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Rezept getRezept() {
        return this.rezept;
    }

    public void setRezept(Rezept rezept) {
        this.rezept = rezept;
    }

    public Zutat getZutat() {
        return this.zutat;
    }

    public void setZutat(Zutat zutat) {
        this.zutat = zutat;
    }

    public int getMenge() {
        return this.Menge;
    }

    public void setMenge(int Menge) {
        this.Menge = Menge;
    }

    public Rezept_Zutat id(long id) {
        setId(id);
        return this;
    }

    public Rezept_Zutat rezept(Rezept rezept) {
        setRezept(rezept);
        return this;
    }

    public Rezept_Zutat zutat(Zutat zutat) {
        setZutat(zutat);
        return this;
    }

    public Rezept_Zutat Menge(int Menge) {
        setMenge(Menge);
        return this;
    }

    @Override
    public String toString() {
        return "{" +
                " id='" + getId() + "'" +
                ", rezept='" + getRezept() + "'" +
                ", zutat='" + getZutat() + "'" +
                ", Menge='" + getMenge() + "'" +
                "}";
    }

}
